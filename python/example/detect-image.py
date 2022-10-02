import sys
import cv2
from pylibfacedetection import FaceDetector

image = cv2.imread(sys.argv[1])

detector = FaceDetector()
results = detector.detect(image)

for result in results:
    confidence, x, y, w, h = result
    cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), thickness=2)

cv2.imshow('Result', image)
cv2.waitKey(0)
cv2.destroyAllWindows()
