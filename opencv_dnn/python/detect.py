import os
import sys
import argparse
from typing import Tuple

import cv2
import numpy as np

from priorbox import PriorBox
from utils import draw

backends = (cv2.dnn.DNN_BACKEND_DEFAULT,
            cv2.dnn.DNN_BACKEND_HALIDE,
            cv2.dnn.DNN_BACKEND_INFERENCE_ENGINE,
            cv2.dnn.DNN_BACKEND_OPENCV)
targets = (cv2.dnn.DNN_TARGET_CPU,
           cv2.dnn.DNN_TARGET_OPENCL,
           cv2.dnn.DNN_TARGET_OPENCL_FP16,
           cv2.dnn.DNN_TARGET_MYRIAD)

def str2bool(v: str) -> bool:
     if v.lower() in ['true', 'yes', 'on', 'y', 't']:
          return True
     elif v.lower() in ['false', 'no', 'off', 'n', 'f']:
          return False
     else:
          raise NotImplementedError

parser = argparse.ArgumentParser(description='A demo for running libfacedetection using OpenCV\'s DNN module.')
# OpenCV DNN
parser.add_argument('--backend', choices=backends, default=cv2.dnn.DNN_BACKEND_DEFAULT, type=int,
                    help="Choose one of computation backends: "
                         "%d: automatically (by default), "
                         "%d: Halide language (http://halide-lang.org/), "
                         "%d: Intel's Deep Learning Inference Engine (https://software.intel.com/openvino-toolkit), "
                         "%d: OpenCV implementation" % backends)
parser.add_argument('--target', choices=targets, default=cv2.dnn.DNN_TARGET_CPU, type=int,
                    help='Choose one of target computation devices: '
                         '%d: CPU target (by default), '
                         '%d: OpenCL, '
                         '%d: OpenCL fp16 (half-float precision), '
                         '%d: VPU' % targets)
# Location
parser.add_argument('--image', help='Path to the image.')
parser.add_argument('--model', type=str, help='Path to .onnx model file.')
# Inference
parser.add_argument('--conf_thresh', default=0.6, type=float, help='Threshold for filtering out faces with conf < conf_thresh.')
parser.add_argument('--nms_thresh', default=0.3, type=float, help='Threshold for non-max suppression.')
parser.add_argument('--keep_top_k', default=750, type=int, help='Keep keep_top_k for results outputing.')
# Result
parser.add_argument('--vis', default=True, type=str2bool, help='Set True to visualize the result image.')
parser.add_argument('--save', default='result.jpg', type=str, help='Path to save the result image.')
args = parser.parse_args()


# Build the blob
assert os.path.exists(args.image), 'File {} does not exist!'.format(args.image)
img = cv2.imread(args.image, cv2.IMREAD_COLOR)
h, w, _ = img.shape
print('Image size: h={}, w={}'.format(h, w))
blob = cv2.dnn.blobFromImage(img) # 'size' param resize the output to the given shape


# Load the net
net = cv2.dnn.readNet(args.model)
net.setPreferableBackend(args.backend)
net.setPreferableTarget(args.target)


# Run the net
output_names = ['loc', 'conf', 'iou']
net.setInput(blob)
loc, conf, iou = net.forward(output_names)


# Decode bboxes and landmarks
pb = PriorBox(input_shape=(w, h), output_shape=(w, h))
dets = pb.decode(loc, conf, iou, args.conf_thresh)


# NMS
if dets.shape[0] > 0:
     # NMS from OpenCV
     keep_idx = cv2.dnn.NMSBoxes(
          bboxes=dets[:, 0:4].tolist(),
          scores=dets[:, -1].tolist(),
          score_threshold=args.conf_thresh,
          nms_threshold=args.nms_thresh,
          eta=1,
          top_k=args.keep_top_k) # returns [box_num, class_num]
     keep_idx = np.squeeze(keep_idx) # [box_num, class_num] -> [box_num]
     dets = dets[keep_idx]
     print('Detection results: {} faces found'.format(dets.shape[0]))
     for d in dets:
          print('[{x1:.1f}, {y1:.1f}] [{x2:.1f}, {y2:.1f}] {score:.2f}'.format(
               x1=d[0], y1=d[1], x2=d[2], y2=d[3], score=d[-1]))
else:
     print('No faces found.')
     exit()


# Draw boudning boxes and landmarks on the original image
img_res = draw(
     img=img,
     bboxes=dets[:, :4],
     landmarks=np.reshape(dets[:, 4:14], (-1, 5, 2)),
     scores=dets[:, -1]
)
if args.vis:
     cv2.imshow('Detection Results on {}'.format(args.image), img_res)
     cv2.resizeWindow(args.image, w, h)

     cv2.waitKey(0)
     cv2.destroyAllWindows()
# Save the result image
cv2.imwrite(args.save, img_res)
