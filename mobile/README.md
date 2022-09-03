##Mobile support for iOS and Android. 


##iOS

Updated for the latest libfacedetection! I try it in iOS and successful run. 

1. download or clone this lib in your computer;
2. create New Xcode project;
3. add this lib's src to your project;
4. add the system lib `libc++.tbd` and other you need framework(eg. ACFoundation.framework etc.);
5. download `opencv2.framework` and add it in your project;
6. follow the example file to write the code.

**!!!** 
1. modify  `facedetectcnn.h`
```objc
//#define _ENABLE_AVX2 //Please enable it if X64 CPU
#define _ENABLE_NEON //Please enable it if ARM CPU
//#include "facedetection_export.h"
#define FACEDETECTION_EXPORT
```

2. modify  `.m` to `.mm` 
3. import lib in your `.mm`
```objc
#import <opencv2/opencv.hpp>
#import <opencv2/imgcodecs/ios.h>
#import "ViewController.h"
#import "facedetectcnn.h"
```
4. modify 
> you must import the `opencv2/opencv.hpp` first  !!!


MyCode:
```objc
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
```
![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/iOS/screenshot1.png)

![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/iOS/screenshot2.png)

##Android

Just transport this latetest libfacedetection to Android and run successfully and simplified use for opencv in only 3 steps.

I also update apk in `Android/Facedetection/release` folder so you can just install on your android device to test it.

Modified cmakelist.txt for android and configures for opencv. So all you need to do is to add opencv for android to it and RUN IT.

Here is the steps for developers:
   
1.clone this porject and make sure cmake,ndk and lldb(if u need debug c++ code) is downloaded.    
2.download opencv sdk for android from [OpenCV-release](https://opencv.org/releases.html) and unzip `OpenCV-android-sdk` to the root dir of this project.  
3. modify  `facedetectcnn.h`

```c++
//#define _ENABLE_AVX2 //Please enable it if X64 CPU
#define _ENABLE_NEON //Please enable it if ARM CPU
//#include "facedetection_export.h"
#define FACEDETECTION_EXPORT
```
4.run it!

![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/Android/screenshot1.jpg)

![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/Android/screenshot2.jpg)