##Mobile support for iOS and Android. 

##iOS

This lib is very useful! I try it in iOS and successful run. 

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
```

2. modify  `.m` to `.mm` 
3. import lib in your `.mm`
```objc
#import <opencv2/opencv.hpp>
#import <opencv2/imgcodecs/ios.h>
#import "ViewController.h"
#import "facedetectcnn.h"
```
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
```
![IMG_0428](https://user-images.githubusercontent.com/5406305/54405990-50f9a700-4713-11e9-8f9e-bb6e54a0423a.PNG)
![IMG_0429](https://user-images.githubusercontent.com/5406305/54405991-51923d80-4713-11e9-9400-1ebe95e3abe5.PNG)
![IMG_0430](https://user-images.githubusercontent.com/5406305/54405993-535c0100-4713-11e9-9a4a-bbb5b3f6c21a.PNG)

##Android

Just transport this lib to Andtoid and run successful with some optimization.

I also build an apk in the `Android/release` folder so you can just install on your android device to test it.

Modified cmakelist.txt for android and configures for opencv. So all you need to do is to add opencv for android to it and RUN IT.

Here is the steps for developers:
   
1.clone this porject and make sure cmake,ndk and lldb(if u need debug c++ code) is downloaded.    
2.download opencv sdk for android from [OpenCV-release](https://opencv.org/releases.html).    
3.import `OpenCV-android-sdk/sdk/java` to this porject as a module so android can use it.  
4.copy opencv c++ header `OpenCV-android-sdk/sdk/natvie/jni/include/opencv2` to this project `libfacedetection/mobile/Android/app/src/main/cpp/` so `jni` can use it.        
5.copy opencv libs `OpenCV-android-sdk/sdk/natvie/libs/` and staticlibs `OpenCV-android-sdk/sdk/natvie/staticlibs/` to this project direct `libfacedetection/mobile/Android/app/src/main/jniLibs/` for compile.  
6.run it!

![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/Screenshot1.jpg)

![](https://raw.githubusercontent.com/dpmaycry/libfacedetection/master/mobile/Screenshot2.jpg)


