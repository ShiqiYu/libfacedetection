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
    Mat result_cnn = img.clone();;
    //print the detection results
    for(int i = 0; i < (pResults ? *pResults : 0); i++)
    {
        short * p = ((short*)(pResults+1))+142*i;
        int x = p[0];
        int y = p[1];
        int w = p[2];
        int h = p[3];
        int neighbors = p[4];
        int angle = p[5];
        
        printf("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d\n", x,y,w,h,neighbors, angle);
        rectangle(result_cnn, cv::Rect(x, y, w, h), Scalar(0, 255, 0), 2);
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
