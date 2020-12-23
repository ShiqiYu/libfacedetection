import os
import sys
import argparse
from typing import Tuple

import cv2
import numpy as np

from priorbox import PriorBox
from utils import nms, draw

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
parser.add_argument('--conf_thresh', default=0.3, type=float, help='Threshold for filtering out faces with conf < conf_thresh.')
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
dets = pb.decode(np.squeeze(loc, axis=0), np.squeeze(conf, axis=0), np.squeeze(iou, axis=0), args.conf_thresh)


# NMS
if dets.shape[0] > 0:
     dets = nms(dets, args.nms_thresh)
     faces = dets[:args.keep_top_k, :]
     print('Detection results: {} faces found'.format(faces.shape[0]))
     print(faces)
else:
     print('No faces found.')
     exit()


# Draw boudning boxes and landmarks on the original image
img_res = draw(cv2.cvtColor(img, cv2.COLOR_BGR2RGB), faces[:, :4], np.reshape(faces[:, 4:14], (-1, 5, 2)), faces[:, -1])
if args.vis:
     cv2.imshow('Detection Results on {}'.format(args.image), img_res)
     cv2.resizeWindow(args.image, w, h)

     cv2.waitKey(0)
     cv2.destroyAllWindows()
# Save the result image
cv2.imwrite(args.save, img_res)