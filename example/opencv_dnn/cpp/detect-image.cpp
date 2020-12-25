#include <vector>
#include <string>
#include <iostream>

#include "priorbox.hpp"
#include "utils.hpp"

#include "opencv2/opencv.hpp"

int main(int argc, char* argv[]) {
    if (argc != 3) {
        std::cout << "Usage: " << argv[0] << " <image_file_name> <net_file_name>\n";
        return -1;
    }

    // Build blob
    cv::Mat img = cv::imread(argv[1], cv::IMREAD_COLOR);
    if (img.empty()) {
        std::cerr << "Cannot load the image file " << argv[1] << ".\n";
        return -1;
    }
    cv::Size img_shape = img.size();
    cv::Mat blob = cv::dnn::blobFromImage(img);

    // Load .onnx model using OpenCV's DNN module
    cv::dnn::Net net = cv::dnn::readNet(argv[2]);
    net.setPreferableBackend(cv::dnn::DNN_BACKEND_DEFAULT);
    net.setPreferableTarget(cv::dnn::DNN_TARGET_CPU);

    // Inference hyperparameters
    float conf_thresh = 0.6;
    float nms_thresh = 0.3;
    int keep_top_k = 750;
    // Result
    bool vis = false;
    std::string save_fpath = "./result.jpg";

    // Forward
    std::vector<cv::String> output_names = { "loc", "conf", "iou" };
    std::vector<cv::Mat> output_blobs;
    net.setInput(blob);
    net.forward(output_blobs, output_names);

    // Decode bboxes, landmarks and scores
    PriorBox pb(img_shape, img_shape);
    std::vector<Face> dets = pb.decode(output_blobs[0], output_blobs[1], output_blobs[2], conf_thresh);

    // NMS
    if (dets.size() > 1) {
        nms(dets, nms_thresh);
        if (dets.size() > keep_top_k) { dets.erase(dets.begin()+keep_top_k, dets.end()); }
    }
    else if (dets.size() < 1) {
        std::cout << "No faces found." << std::endl;
        return 1;
    }
    std::cout << "Detection results: " << dets.size() << " faces found." << std::endl;
    for (auto i = 0; i < dets.size(); ++i) {
        BndBox_xyxy bbox = dets[i].bbox;
        float score = dets[i].score;
        std::cout << bbox.top_left << " " << bbox.bottom_right << " " << score << std::endl;
    }

    // Draw and display
    draw(img, dets);
    if (vis) {
        cv::String title = cv::String("Detection Results on") + cv::String(argv[1]);
        cv::imshow(title, img);
        cv::waitKey(0);
        cv::destroyAllWindows();
    }
    cv::imwrite(save_fpath, img);

    return 0;
}