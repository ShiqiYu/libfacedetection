import cv2
import numpy as np

from typing import List, Tuple

def nms(dets: np.ndarray, thresh: float) -> List[int]:
    '''Python implementation of NMS.
    '''
    order = dets[:, -1].ravel().argsort()[::-1]
    dets = dets[order]

    x1 = dets[:, 0]
    y1 = dets[:, 1]
    x2 = dets[:, 2]
    y2 = dets[:, 3]
    scores = dets[:, -1]

    areas = (x2 - x1 + 1) * (y2 - y1 + 1)
    order = scores.argsort()[::-1]

    keep = []
    while order.size > 0:
        i = order[0]
        keep.append(i)
        xx1 = np.maximum(x1[i], x1[order[1:]])
        yy1 = np.maximum(y1[i], y1[order[1:]])
        xx2 = np.minimum(x2[i], x2[order[1:]])
        yy2 = np.minimum(y2[i], y2[order[1:]])

        w = np.maximum(0.0, xx2 - xx1 + 1)
        h = np.maximum(0.0, yy2 - yy1 + 1)
        inter = w * h
        ovr = inter / (areas[i] + areas[order[1:]] - inter)

        inds = np.where(ovr <= thresh)[0]
        order = order[inds + 1]

    dets = dets[keep, :]
    return dets

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

    # convert BGR to RGB for correct color display
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # draw bounding boxes
    if bboxes is not None:
        color = (0, 255, 0)
        thickness = 2
        for idx in range(bboxes.shape[0]):
            bbox = bboxes[idx].astype(np.int)
            cv2.rectangle(img, (bbox[0], bbox[1]), (bbox[2], bbox[3]), color, thickness)
            cv2.putText(img, '{:.4f}'.format(scores[idx]), (bbox[0], bbox[1]+12), cv2.FONT_HERSHEY_DUPLEX, 0.5, (255, 255, 255))

    # draw landmarks
    if landmarks is not None:
        radius = 2
        thickness = 2
        color = [(255, 0, 0), (0, 0, 255), (0, 255, 255), (255, 255, 0), (0, 255, 0)]
        for idx in range(landmarks.shape[0]):
            face_landmarks = landmarks[idx].astype(np.int)
            for idx, landmark in enumerate(face_landmarks):
                cv2.circle(img, (int(landmark[0]), int(landmark[1])), radius, color[idx], thickness)
    return img