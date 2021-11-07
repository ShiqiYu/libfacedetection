import argparse

import numpy as np
import cv2 as cv

def str2bool(v: str) -> bool:
    if v.lower() in ['true', 'yes', 'on', 'y', 't']:
        return True
    elif v.lower() in ['false', 'no', 'off', 'n', 'f']:
        return False
    else:
        raise NotImplementedError

def visualize(image, faces, print_flag=False, fps=None):
    output = image.copy()

    if fps:
        cv.putText(output, 'FPS: {:.2f}'.format(fps), (0, 15), cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0))

    for idx, face in enumerate(faces):
        if print_flag:
            print('Face {}, top-left coordinates: ({:.0f}, {:.0f}), box width: {:.0f}, box height {:.0f}, score: {:.2f}'.format(idx, face[0], face[1], face[2], face[3], face[-1]))

        coords = face[:-1].astype(np.int32)
        # Draw face bounding box
        cv.rectangle(output, (coords[0], coords[1]), (coords[0]+coords[2], coords[1]+coords[3]), (0, 255, 0), 2)
        # Draw landmarks
        cv.circle(output, (coords[4], coords[5]), 2, (255, 0, 0), 2)
        cv.circle(output, (coords[6], coords[7]), 2, (0, 0, 255), 2)
        cv.circle(output, (coords[8], coords[9]), 2, (0, 255, 0), 2)
        cv.circle(output, (coords[10], coords[11]), 2, (255, 0, 255), 2)
        cv.circle(output, (coords[12], coords[13]), 2, (0, 255, 255), 2)
        # Put score
        cv.putText(output, '{:.4f}'.format(face[-1]), (coords[0], coords[1]+15), cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0))

    return output

def main():
    backends = (cv.dnn.DNN_BACKEND_DEFAULT,
                cv.dnn.DNN_BACKEND_HALIDE,
                cv.dnn.DNN_BACKEND_INFERENCE_ENGINE,
                cv.dnn.DNN_BACKEND_OPENCV)
    targets = (cv.dnn.DNN_TARGET_CPU,
               cv.dnn.DNN_TARGET_OPENCL,
               cv.dnn.DNN_TARGET_OPENCL_FP16,
               cv.dnn.DNN_TARGET_MYRIAD)

    parser = argparse.ArgumentParser(description='A demo for running libfacedetection using OpenCV\'s DNN module.')
    parser.add_argument('--backend', choices=backends, default=cv.dnn.DNN_BACKEND_DEFAULT, type=int,
                        help='Choose one of computation backends: '
                             '%d: automatically (by default), '
                             '%d: Halide language (http://halide-lang.org/), '
                             '%d: Intel\'s Deep Learning Inference Engine (https://software.intel.com/openvino-toolkit), '
                             '%d: OpenCV implementation' % backends)
    parser.add_argument('--target', choices=targets, default=cv.dnn.DNN_TARGET_CPU, type=int,
                        help='Choose one of target computation devices: '
                             '%d: CPU target (by default), '
                             '%d: OpenCL, '
                             '%d: OpenCL fp16 (half-float precision), '
                             '%d: VPU' % targets)
    # Location
    parser.add_argument('--input', '-i', help='Path to the image. Omit to call default camera')
    parser.add_argument('--model', '-m', type=str, help='Path to .onnx model file.')
    # Inference parameters
    parser.add_argument('--score_threshold', default=0.6, type=float, help='Threshold for filtering out faces with conf < conf_thresh.')
    parser.add_argument('--nms_threshold', default=0.3, type=float, help='Threshold for non-max suppression.')
    parser.add_argument('--top_k', default=5000, type=int, help='Keep keep_top_k for results outputing.')
    # Result
    parser.add_argument('--vis', default=True, type=str2bool, help='Set True to visualize the result image. Invalid when using camera.')
    parser.add_argument('--save', default=False, type=str2bool, help='Set True to save as result.jpg. Invalid when using camera.')
    args = parser.parse_args()

    # Instantiate yunet
    yunet = cv.FaceDetectorYN.create(
        model=args.model,
        config='',
        input_size=(320, 320),
        score_threshold=args.score_threshold,
        nms_threshold=args.nms_threshold,
        top_k=5000,
        backend_id=args.backend,
        target_id=args.target
    )

    if args.input is not None:
        image = cv.imread(args.input)

        yunet.setInputSize((image.shape[1], image.shape[0]))
        _, faces = yunet.detect(image) # faces: None, or nx15 np.array

        vis_image = visualize(image, faces)
        if args.save:
            print('result.jpg saved.')
            cv.imwrite('result.jpg', vis_image)
        if args.vis:
            cv.namedWindow(args.input, cv.WINDOW_AUTOSIZE)
            cv.imshow(args.input, vis_image)
            cv.waitKey(0)
    else:
        device_id = 0
        cap = cv.VideoCapture(device_id)
        frame_w = int(cap.get(cv.CAP_PROP_FRAME_WIDTH))
        frame_h = int(cap.get(cv.CAP_PROP_FRAME_HEIGHT))
        yunet.setInputSize([frame_w, frame_h])

        tm = cv.TickMeter()
        while cv.waitKey(1) < 0:
            has_frame, frame = cap.read()
            if not has_frame:
                print('No frames grabbed!')

            tm.start()
            _, faces = yunet.detect(frame) # # faces: None, or nx15 np.array
            tm.stop()

            frame = visualize(frame, faces, fps=tm.getFPS())
            cv.imshow('libfacedetection demo', frame)

            tm.reset()

if __name__ == '__main__':
    main()