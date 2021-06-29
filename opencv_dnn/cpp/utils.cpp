#include "utils.hpp"

void draw(cv::Mat& img,
          const std::vector<Face>& faces) {

    const int thickness = 2;
    const cv::Scalar bbox_color = {  0, 255,   0};
    const cv::Scalar text_color = {255, 255, 255};
    const std::vector<cv::Scalar> landmarks_color = {
        {255,   0,   0}, // right eye
        {  0,   0, 255}, // left eye
        {  0, 255,   0}, // nose
        {255,   0, 255}, // mouth right
        {  0, 255, 255}  // mouth left
    };

    for (auto i = 0; i < faces.size(); ++i) {
        // draw bbox
        cv::rectangle(img,
                      cv::Rect(faces[i].bbox_tlwh),
                      bbox_color,
                      thickness);
        // put score by the corner of bbox
        std::string str_score = std::to_string(faces[i].score);
        if (str_score.size() > 6) {
            str_score.erase(6);
        }
        cv::putText(img,
                    str_score,
                    cv::Point(faces[i].bbox_tlwh.x, faces[i].bbox_tlwh.y + 12),
                    cv::FONT_HERSHEY_DUPLEX,
                    0.5, // Font scale
                    text_color);
        // draw landmarks
        const int radius = 2;
        cv::circle(img, cv::Point(faces[i].landmarks.right_eye),   radius, landmarks_color[0], thickness);
        cv::circle(img, cv::Point(faces[i].landmarks.left_eye),    radius, landmarks_color[1], thickness);
        cv::circle(img, cv::Point(faces[i].landmarks.nose_tip),    radius, landmarks_color[2], thickness);
        cv::circle(img, cv::Point(faces[i].landmarks.mouth_right), radius, landmarks_color[3], thickness);
        cv::circle(img, cv::Point(faces[i].landmarks.mouth_left),  radius, landmarks_color[4], thickness);
    }
}
