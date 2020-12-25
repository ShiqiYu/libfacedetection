#include <vector>
#include <string>
#include <iostream>

#include "priorbox.hpp"
#include "utils.hpp"

#include "opencv2/opencv.hpp"

int main(int argc, char* argv[]) {

    if(argc != 3)
    {
        printf("Usage: %s <camera index>\n", argv[0]);
        return -1;
    }

    // Load .onnx model using OpenCV's DNN module
    cv::dnn::Net net = cv::dnn::readNet(argv[2]);
    net.setPreferableBackend(cv::dnn::DNN_BACKEND_DEFAULT);
    net.setPreferableTarget(cv::dnn::DNN_TARGET_CPU);

    cv::VideoCapture cap;
    cv::Mat im, blob;
    cv::TickMeter cvtm;
    cv::Size img_shape;
    std::vector<cv::String> output_names = { "loc", "conf", "iou" };
    std::vector<cv::Mat> output_blobs;

    cv::String title = cv::String("Detection Results on") + cv::String(argv[1]);

    // Inference hyperparameters
    float conf_thresh = 0.6;
    float nms_thresh = 0.3;
    int keep_top_k = 750;
    // Result
    bool vis = false;

    if( isdigit(argv[1][0]))
    {
        cap.open(argv[1][0]-'0');
        if(! cap.isOpened())
        {
            std::cerr << "Cannot open the camera." << std::endl;
            return 0;
        }
    }

    cap >> im;
    img_shape = im.size();
    PriorBox pb(img_shape, img_shape);
    std::vector<Face> dets;

    if( cap.isOpened())
    {
        while(true)
        {
            cap >> im;
            cvtm.start();

            // Build blob
            blob = cv::dnn::blobFromImage(im, 1.0,  cv::Size(), cv::Scalar());

            // Forward
            net.setInput(blob);
            net.forward(output_blobs, output_names);

            // Decode bboxes, landmarks and scores
            dets = pb.decode(output_blobs[0], output_blobs[1], output_blobs[2], conf_thresh);

            // NMS
            if (dets.size() > 1) {
                nms(dets, nms_thresh);
                if (dets.size() > keep_top_k) { dets.erase(dets.begin()+keep_top_k, dets.end()); }
            }
            else if (dets.size() < 1) {
                std::cout << "No faces found." << std::endl;
            }

            cvtm.stop();

            std::string timeLabel = cv::format("Inference time: %.2f ms", cvtm.getTimeMilli());
            cv::putText(im, timeLabel, cv::Point(0, 15), cv::FONT_HERSHEY_SIMPLEX, 0.5, cv::Scalar(0, 255, 0), 1);

            draw(im, dets);

            cvtm.reset();
            cv::imshow(title, im);
            if((cv::waitKey(1)& 0xFF) == 27)
                break;
        }
    }

    return 0;
}