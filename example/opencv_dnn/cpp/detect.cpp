#include <vector>
#include <string>
#include <iostream>

#include "priorbox.hpp"
#include "utils.hpp"

#include "opencv2/opencv.hpp"

cv::Size get_input_shape(std::string model_fpath) {
    size_t start = model_fpath.find("_") + 1;
    size_t end = model_fpath.find(".onnx");
    int width = std::stoi(
        model_fpath.substr(start, end-start)
    );
    int height = int(0.75 * width);

    return cv::Size(width, height);
}

int main(int argc, char* argv[]) {
    // Location
    std::string img_fpath = "../example2.jpg";
    std::string model_fpath = "../YuFaceDetectNet_320.onnx";
    // Inference
    float conf_thresh = 0.6;
    float nms_thresh = 0.3;
    int keep_top_k = 750;
    // Result
    bool vis = true;
    std::string save_fpath = "./result.jpg";

    cv::Size input_shape = get_input_shape(model_fpath);

    // Load .onnx model using OpenCV's DNN module
    cv::dnn::Net net = cv::dnn::readNet(model_fpath);
    net.setPreferableBackend(cv::dnn::DNN_BACKEND_DEFAULT);
    net.setPreferableTarget(cv::dnn::DNN_TARGET_CPU);

    // Build blob
    cv::Mat img = cv::imread(img_fpath, cv::IMREAD_COLOR);
    cv::Size output_shape = img.size();
    cv::Mat blob = cv::dnn::blobFromImage(img, 1.0, input_shape);

    // Forward
    std::vector<cv::String> output_names = { "loc", "conf" };
    std::vector<cv::Mat> output_blobs;
    net.setInput(blob);
    net.forward(output_blobs, output_names);

    // Decode bboxes, landmarks and scores
    PriorBox pb(input_shape, output_shape);
    std::vector<Face> dets = pb.decode(output_blobs[0], output_blobs[1]);

    // Ignore low scores
    dets.erase(
        std::remove_if(dets.begin(), dets.end(), [&conf_thresh](const Face& f) { return f.score <= conf_thresh; }),
        dets.end()
    );

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
        cv::String title = cv::String("Detection Results on") + img_fpath;
        cv::imshow(title, img);
        cv::waitKey(0);
        cv::destroyAllWindows();
    }
    cv::imwrite(save_fpath, img);

    return 0;
}