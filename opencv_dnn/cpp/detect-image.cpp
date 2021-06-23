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
    std::vector<Face> nms_dets;
    if (dets.size() > 1) {
        std::vector<cv::Rect> face_boxes;
        std::vector<float> face_scores;
        for (auto d: dets) {
            face_boxes.push_back(d.bbox_tlwh);
            face_scores.push_back(d.score);
        }
        std::vector<int> keep_idx;
        cv::dnn::NMSBoxes(face_boxes, face_scores, conf_thresh, nms_thresh, keep_idx, 1.f, keep_top_k);
        for (size_t i = 0; i < keep_idx.size(); i++) {
            size_t idx = keep_idx[i];
            nms_dets.push_back(dets[idx]);
        }
    }
    else if (dets.size() < 1) {
        std::cout << "No faces found." << std::endl;
        return 1;
    }
    std::cout << "Detection results: " << nms_dets.size() << " faces found." << std::endl;
    for (auto i = 0; i < nms_dets.size(); ++i) {
        Box bbox = nms_dets[i].bbox_tlwh;
        float score = nms_dets[i].score;
        std::cout << "[" << bbox.x << ", " << bbox.y << "] [" << bbox.x + bbox.width << ", " << bbox.y + bbox.height << "] " << score << std::endl;
    }

    // Draw and display
    draw(img, nms_dets);
    if (vis) {
        cv::String title = cv::String("Detection Results on") + cv::String(argv[1]);
        cv::imshow(title, img);
        cv::waitKey(0);
        cv::destroyAllWindows();
    }
    cv::imwrite(save_fpath, img);

    return 0;
}