#include <gst/gst.h>
#include <gst/base/gstbasetransform.h>
#include <gst/video/video.h>

#include <opencv2/opencv.hpp>
#include "facedetectcnn.h"

/*

Howto run?

GST_DEBUG=3 gst-launch-1.0 filesrc location=input.mp4 ! decodebin \
    ! videorate ! video/x-raw, framerate=8/1 \
    ! videoscale ! video/x-raw, width=640, height=480 \
    ! videoconvert \
    ! identity sync=true \
    ! queue max-size-buffers=3 leaky=1 \
    ! libfacedetect \
    ! videoconvert \
    ! xvimagesink sync=false
*/


#define TYPE_FACE_DETECT face_detect_get_type()
G_DECLARE_FINAL_TYPE (FaceDetect, face_detect, FACE, DETECT, GstBaseTransform)

struct _FaceDetect {
    GstBaseTransform element;

    guint width;
    guint height;
    guint thresh;
    gboolean boxes;
};

G_DEFINE_TYPE (FaceDetect, face_detect, GST_TYPE_BASE_TRANSFORM)

using namespace cv;

#define DEFAULT_THRESHHOLD 75
#define DEFAULT_BOXES TRUE

enum {
    PROP_0,
    PROP_THRESH,
    PROP_BOXES
};

static GstStaticPadTemplate sink_factory = GST_STATIC_PAD_TEMPLATE (
    "sink",
    GST_PAD_SINK,
    GST_PAD_ALWAYS,
    GST_STATIC_CAPS (GST_VIDEO_CAPS_MAKE("RGB"))
);

static GstStaticPadTemplate src_factory = GST_STATIC_PAD_TEMPLATE (
    "src",
    GST_PAD_SRC,
    GST_PAD_ALWAYS,
    GST_STATIC_CAPS (GST_VIDEO_CAPS_MAKE("RGB"))
);

static void set_property( GObject *object, guint prop_id, const GValue *value, GParamSpec *pspec ) {
    FaceDetect *self = FACE_DETECT(object);
    switch(prop_id) {
        case PROP_THRESH:
            self->thresh = g_value_get_uint(value);
            break;
        case PROP_BOXES:
            self->boxes = g_value_get_boolean(value);
            break;
        default:
            G_OBJECT_WARN_INVALID_PROPERTY_ID(object, prop_id, pspec);
            break;
    }
}

static void get_property( GObject *object, guint prop_id, GValue *value, GParamSpec *pspec ) {
    FaceDetect *self = FACE_DETECT(object);

    switch(prop_id) {
        case PROP_THRESH:
            g_value_set_uint(value, self->thresh);
            break;
        case PROP_BOXES:
            g_value_set_boolean(value, self->boxes);
            break;
        default:
            G_OBJECT_WARN_INVALID_PROPERTY_ID(object, prop_id, pspec);
            break;
    }
}

static gboolean set_caps(GstBaseTransform *trans, GstCaps *incaps, GstCaps *outcaps) {
    FaceDetect *self = FACE_DETECT(trans);
    GstVideoInfo info;

    if (gst_video_info_from_caps (&info, incaps) == FALSE) {
            return FALSE;
    }

    GST_INFO("New Width: %d Height: %d", info.width, info.height);
    self->width = info.width;
    self->height = info.height;

    return TRUE;
}

static GstFlowReturn transform_ip(GstBaseTransform *trans, GstBuffer *buf) {
    FaceDetect *self = FACE_DETECT(trans);
    GST_DEBUG("Processing");

    GstMapInfo info;
    if (gst_buffer_map(buf, &info, GST_MAP_WRITE) == FALSE) {
        GST_ERROR("Cannot map buffer");
        return GST_FLOW_ERROR;
    }
    Size size(self->width, self->height);
    Mat image(size, CV_8UC3, (void *) info.data);

    unsigned char *pBuffer = (unsigned char *) malloc(0x20000);
    int *pResults = facedetect_cnn(pBuffer, (unsigned char*)(image.ptr(0)), image.cols, image.rows, (int)image.step);

    for(int i = 0; i < (pResults ? *pResults : 0); i++) {
        short * p = ((short*)(pResults+1))+142*i;
        int x = p[0];
        int y = p[1];
        int w = p[2];
        int h = p[3];
        int neighbors = p[4];
        int angle = p[5];

        GST_ERROR("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d", x,y,w,h,neighbors, angle);
        if (neighbors >= self->thresh) {
            if (self->boxes) {
                rectangle(image, Rect(x, y, w, h), Scalar(0, 255, 0), 2);
            }
        }
    }

    free(pBuffer);
    gst_buffer_unmap(buf, &info);
    return GST_FLOW_OK;
}

static void face_detect_init (FaceDetect *self)
{
    self->width = 0;
    self->height = 0;
    self->thresh = DEFAULT_THRESHHOLD;
    self->boxes = DEFAULT_BOXES;
}

static void face_detect_class_init (FaceDetectClass *klass)
{
    GObjectClass *object_class = G_OBJECT_CLASS (klass);
    GstElementClass *element_class = GST_ELEMENT_CLASS (klass);
    GstBaseTransformClass *transform_class = GST_BASE_TRANSFORM_CLASS (klass);

    object_class->set_property = set_property;
    object_class->get_property = get_property;

    transform_class->set_caps = set_caps;
    transform_class->transform_ip = transform_ip;

    g_object_class_install_property(object_class, PROP_THRESH,
        g_param_spec_uint( "thresh", "thresh", "Thresh Hold as a percentage", 0, 100, DEFAULT_THRESHHOLD, G_PARAM_READWRITE)
    );

    g_object_class_install_property(object_class, PROP_BOXES,
        g_param_spec_boolean( "boxes", "boxes", "Draw boxes", DEFAULT_BOXES, G_PARAM_READWRITE)
    );

    gst_element_class_set_static_metadata(element_class,
        "Brightness",
        "Sink/Src/Caps",
        "Detects faces in images using libfacedetect",
        "James Stevenson <james@stev.org>"
    );

    gst_element_class_add_static_pad_template (element_class, &sink_factory);
    gst_element_class_add_static_pad_template (element_class, &src_factory);
}


extern "C" gboolean Register_init (GstPlugin *plugin) {
    return gst_element_register (plugin, "libfacedetect", GST_RANK_NONE, TYPE_FACE_DETECT);
}

#ifndef PACKAGE
#define PACKAGE "libfacedetect"
#endif

GST_PLUGIN_DEFINE (
    GST_VERSION_MAJOR,
    GST_VERSION_MINOR,
    facedetect,
    "Detect faces",
    Register_init,
    "0.0.0",
    "BSD",
    "facedetect",
    "https://github.com/ShiqiYu/libfacedetection"
)
