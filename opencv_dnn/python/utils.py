import cv2
import numpy as np

from typing import List, Tuple

def draw(img: np.ndarray, bboxes: np.ndarray, landmarks: np.ndarray, scores: np.ndarray) -> np.ndarray:
    '''
    This function draws bounding boxes and landmarks on the image and return the result.

    Parameters:
        bboxes    - bboxes of shape [n, 5]. 'n' for number of bboxes, '5' for coordinate and confidence
                    (x1, y1, x2, y2, c).
        landmarks - landmarks of shape [n, 5, 2]. 'n' for number of bboxes, '5' for 5 landmarks
                    (two for eyes center, one for nose tip, two for mouth corners), '2' for coordinate
                    on the image.
    Returns:
        img       - image with bounding boxes and landmarks drawn
    '''

    # draw bounding boxes
    if bboxes is not None:
        color = (0, 255, 0)
        thickness = 2
        for idx in range(bboxes.shape[0]):
            bbox = bboxes[idx].astype(np.int)
            cv2.rectangle(img, (bbox[0], bbox[1]), (bbox[0]+bbox[2], bbox[1]+bbox[3]), color, thickness)
            cv2.putText(img, '{:.4f}'.format(scores[idx]), (bbox[0], bbox[1]+12), cv2.FONT_HERSHEY_DUPLEX, 0.5, (255, 255, 255))

    # draw landmarks
    if landmarks is not None:
        radius = 2
        thickness = 2
        color = [
            (255,   0,   0), # right eye
            (  0,   0, 255), # left eye
            (  0, 255,   0), # nose tip
            (255,   0, 255), # mouth right
            (  0, 255, 255)  # mouth left
        ]
        for idx in range(landmarks.shape[0]):
            face_landmarks = landmarks[idx].astype(np.int)
            for idx, landmark in enumerate(face_landmarks):
                cv2.circle(img, (int(landmark[0]), int(landmark[1])), radius, color[idx], thickness)
    return img
