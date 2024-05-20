/*
By downloading, copying, installing or using the software you agree to this license.
If you do not agree to this license, do not download, install,
copy or use the software.


                  License Agreement For libfacedetection
                     (3-clause BSD License)

Copyright (c) 2018-2020, Shiqi Yu, all rights reserved.
shiqi.yu@gmail.com

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

  * Neither the names of the copyright holders nor the names of the contributors
    may be used to endorse or promote products derived from this software
    without specific prior written permission.

This software is provided by the copyright holders and contributors "as is" and
any express or implied warranties, including, but not limited to, the implied
warranties of merchantability and fitness for a particular purpose are disclaimed.
In no event shall copyright holders or contributors be liable for any direct,
indirect, incidental, special, exemplary, or consequential damages
(including, but not limited to, procurement of substitute goods or services;
loss of use, data, or profits; or business interruption) however caused
and on any theory of liability, whether in contract, strict liability,
or tort (including negligence or otherwise) arising in any way out of
the use of this software, even if advised of the possibility of such damage.
*/

#include <time.h>
#include <stdio.h>
#include "stb_draw.h"
#include "facedetectcnn.h"

//define the buffer size. Do not change the size!
//0x9000 = 1024 * (16 * 2 + 4), detect 1024 face at most
#define DETECT_BUFFER_SIZE 0x9000
using namespace std;

int main(int argc, char* argv[])
{
    if(argc != 3)
    {
        printf("Usage: %s <image_file_name> <image_out_name>\n", argv[0]);
        return -1;
    }

	//load an image and convert it to gray (single-channel)
    int H, W, C;
    unsigned char *image = stbi_load(argv[1], &W, &H, &C, 0);

	int * pResults = NULL; 
    //pBuffer is used in the detection functions.
    //If you call functions in multiple threads, please create one buffer for each thread!
    unsigned char * pBuffer = (unsigned char *)malloc(DETECT_BUFFER_SIZE);
    if(!pBuffer)
    {
        fprintf(stderr, "Can not alloc buffer.\n");
        return -1;
    }
	

	///////////////////////////////////////////
	// CNN face detection 
	// Best detection rate
	//////////////////////////////////////////
	//!!! The input image must be a BGR one (three-channel) instead of RGB
	//!!! DO NOT RELEASE pResults !!!
    clock_t start, end;
    double cpu_time_used;
    start = clock();

	pResults = facedetect_cnn(pBuffer, image, W, H, W*3);
    
    end = clock(); 
    cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;   
    printf("time = %f s\n", cpu_time_used);
    
    printf("%d faces detected.\n", (pResults ? *pResults : 0));

	unsigned char *result_image = image;
	//print the detection results
	for(int i = 0; i < (pResults ? *pResults : 0); i++)
	{
        short * p = ((short*)(pResults + 1)) + 16*i;
		int confidence = p[0];
		int x = p[1];
		int y = p[2];
		int w = p[3];
		int h = p[4];
        
        //show the score of the face. Its range is [0-100]
        char sScore[256];
        snprintf(sScore, 256, "%d", confidence);
        COLOR color = {0, 0, 255};
        putText(result_image, W, H, sScore, x, y-3, 0.6, color, 1);
        //draw face rectangle
        color = {0, 255, 0};
        jpg_rectangle(result_image, W, x, y, x+w, y+h, color, 2);
        //draw five face landmarks in different colors
        color = {255, 0, 0};
        jpg_circle(result_image, W, p[5 + 0], p[5 + 1], 2, color, 2);
        jpg_circle(result_image, W, p[5 + 2], p[5 + 3], 2, color, 2);
        jpg_circle(result_image, W, p[5 + 4], p[5 + 5], 2, color, 2);
        jpg_circle(result_image, W, p[5 + 6], p[5 + 7], 2, color, 2);
        jpg_circle(result_image, W, p[5 + 8], p[5 + 9], 2, color, 2);
        
        //print the result
        printf("face %d: confidence=%d, [%d, %d, %d, %d] (%d,%d) (%d,%d) (%d,%d) (%d,%d) (%d,%d)\n", 
                i, confidence, x, y, w, h, 
                p[5], p[6], p[7], p[8], p[9], p[10], p[11], p[12], p[13],p[14]);

	}
    stbi_write_jpg(argv[2], W, H, C, result_image, 0);

    //release the buffer
    free(pBuffer);

	return 0;
}
