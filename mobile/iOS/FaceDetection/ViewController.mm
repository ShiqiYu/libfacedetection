//
//  ViewController.m
//  FaceDetection
//
//  Created by Robin on 2019/3/15.
//  Copyright © 2019 . All rights reserved.
//

#import <opencv2/opencv.hpp>
#import <opencv2/imgcodecs/ios.h>
#import "ViewController.h"
#import "facedetectcnn.h"

//define the buffer size. Do not change the size!
#define DETECT_BUFFER_SIZE 0x20000
using namespace cv;

@implementation ViewController

- (UIImage *)loadImageAndDectect:(const char *)image_file{
    Mat img = imread(image_file);
    if (img.empty()) {
        fprintf(stderr, "Can not load the image file %s.\n", image_file);
        return nil;
    }
    
    int *pResults = NULL;
    unsigned char * pBuffer = (unsigned char *)malloc(DETECT_BUFFER_SIZE);
    if (!pBuffer) {
        fprintf(stderr, "Can not alloc buffer.\n");
        return nil;
    }
    pResults = facedetect_cnn(pBuffer, (unsigned char *)(img.ptr(0)), img.cols, img.rows, (int)img.step);
    printf("%d faces detected.\n", (pResults ? *pResults : 0));
    Mat result_cnn = img.clone();
    cvtColor(img, result_cnn, cv::COLOR_BGR2RGB);
    //print the detection results
    for(int i = 0; i < (pResults ? *pResults : 0); i++)
    {
        short * p = ((short*)(pResults+1))+142*i;
        int confidence = p[0];
        int x = p[1];
        int y = p[2];
        int w = p[3];
        int h = p[4];
        
        printf("face%drect=[%d, %d, %d, %d], confidence=%d\n",i,x,y,w,h,confidence);
        rectangle(result_cnn, cv::Rect(x, y, w, h), Scalar(0, 255, 0), 2);
        string str = to_string(confidence);
        putText(result_cnn, str, cv::Point(x-2,y-2), cv::FONT_HERSHEY_SIMPLEX, 1.0,Scalar(0, 255, 0));
        for (int j = 5; j < 14; j += 2) {
            int p_x = p[j];
            int p_y = p[j+1];
            printf("landmark%d=[%d, %d]\n",(j-5)/2,p_x,p_y);
            circle(result_cnn, cv::Point(p_x,p_y), 2,Scalar(255, 0, 0),-1,cv::LINE_AA);
        }
    }
    
    free(pBuffer);
    
    return MatToUIImage(result_cnn);
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:[UIScreen mainScreen].bounds];
    imageView.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:imageView];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"test" ofType:@".jpg"];
    
    imageView.image = [self loadImageAndDectect:[path UTF8String]];
}


@end
