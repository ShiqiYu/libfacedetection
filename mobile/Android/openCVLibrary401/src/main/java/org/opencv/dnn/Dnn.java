//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.MatOfRotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Net;
import org.opencv.utils.Converters;

// C++: class Dnn
//javadoc: Dnn

public class Dnn {

    // C++: enum Backend
    public static final int
            DNN_BACKEND_DEFAULT = 0,
            DNN_BACKEND_HALIDE = 1,
            DNN_BACKEND_INFERENCE_ENGINE = 2,
            DNN_BACKEND_OPENCV = 3,
            DNN_BACKEND_VKCOM = 4;


    // C++: enum Target
    public static final int
            DNN_TARGET_CPU = 0,
            DNN_TARGET_OPENCL = 1,
            DNN_TARGET_OPENCL_FP16 = 2,
            DNN_TARGET_MYRIAD = 3,
            DNN_TARGET_VULKAN = 4,
            DNN_TARGET_FPGA = 5;


    //
    // C++:  Mat cv::dnn::blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    //

    //javadoc: blobFromImage(image, scalefactor, size, mean, swapRB, crop, ddepth)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop, int ddepth)
    {
        
        Mat retVal = new Mat(blobFromImage_0(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop, ddepth));
        
        return retVal;
    }

    //javadoc: blobFromImage(image, scalefactor, size, mean, swapRB, crop)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop)
    {
        
        Mat retVal = new Mat(blobFromImage_1(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
        
        return retVal;
    }

    //javadoc: blobFromImage(image, scalefactor, size, mean, swapRB)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean, boolean swapRB)
    {
        
        Mat retVal = new Mat(blobFromImage_2(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB));
        
        return retVal;
    }

    //javadoc: blobFromImage(image, scalefactor, size, mean)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size, Scalar mean)
    {
        
        Mat retVal = new Mat(blobFromImage_3(image.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3]));
        
        return retVal;
    }

    //javadoc: blobFromImage(image, scalefactor, size)
    public static Mat blobFromImage(Mat image, double scalefactor, Size size)
    {
        
        Mat retVal = new Mat(blobFromImage_4(image.nativeObj, scalefactor, size.width, size.height));
        
        return retVal;
    }

    //javadoc: blobFromImage(image, scalefactor)
    public static Mat blobFromImage(Mat image, double scalefactor)
    {
        
        Mat retVal = new Mat(blobFromImage_5(image.nativeObj, scalefactor));
        
        return retVal;
    }

    //javadoc: blobFromImage(image)
    public static Mat blobFromImage(Mat image)
    {
        
        Mat retVal = new Mat(blobFromImage_6(image.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::dnn::blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    //

    //javadoc: blobFromImages(images, scalefactor, size, mean, swapRB, crop, ddepth)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop, int ddepth)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_0(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop, ddepth));
        
        return retVal;
    }

    //javadoc: blobFromImages(images, scalefactor, size, mean, swapRB, crop)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB, boolean crop)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_1(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB, crop));
        
        return retVal;
    }

    //javadoc: blobFromImages(images, scalefactor, size, mean, swapRB)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean, boolean swapRB)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_2(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3], swapRB));
        
        return retVal;
    }

    //javadoc: blobFromImages(images, scalefactor, size, mean)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size, Scalar mean)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_3(images_mat.nativeObj, scalefactor, size.width, size.height, mean.val[0], mean.val[1], mean.val[2], mean.val[3]));
        
        return retVal;
    }

    //javadoc: blobFromImages(images, scalefactor, size)
    public static Mat blobFromImages(List<Mat> images, double scalefactor, Size size)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_4(images_mat.nativeObj, scalefactor, size.width, size.height));
        
        return retVal;
    }

    //javadoc: blobFromImages(images, scalefactor)
    public static Mat blobFromImages(List<Mat> images, double scalefactor)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_5(images_mat.nativeObj, scalefactor));
        
        return retVal;
    }

    //javadoc: blobFromImages(images)
    public static Mat blobFromImages(List<Mat> images)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat retVal = new Mat(blobFromImages_6(images_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::dnn::readTensorFromONNX(String path)
    //

    //javadoc: readTensorFromONNX(path)
    public static Mat readTensorFromONNX(String path)
    {
        
        Mat retVal = new Mat(readTensorFromONNX_0(path));
        
        return retVal;
    }


    //
    // C++:  Mat cv::dnn::readTorchBlob(String filename, bool isBinary = true)
    //

    //javadoc: readTorchBlob(filename, isBinary)
    public static Mat readTorchBlob(String filename, boolean isBinary)
    {
        
        Mat retVal = new Mat(readTorchBlob_0(filename, isBinary));
        
        return retVal;
    }

    //javadoc: readTorchBlob(filename)
    public static Mat readTorchBlob(String filename)
    {
        
        Mat retVal = new Mat(readTorchBlob_1(filename));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNet(String framework, vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    //

    //javadoc: readNet(framework, bufferModel, bufferConfig)
    public static Net readNet(String framework, MatOfByte bufferModel, MatOfByte bufferConfig)
    {
        Mat bufferModel_mat = bufferModel;
        Mat bufferConfig_mat = bufferConfig;
        Net retVal = new Net(readNet_0(framework, bufferModel_mat.nativeObj, bufferConfig_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: readNet(framework, bufferModel)
    public static Net readNet(String framework, MatOfByte bufferModel)
    {
        Mat bufferModel_mat = bufferModel;
        Net retVal = new Net(readNet_1(framework, bufferModel_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNet(String model, String config = "", String framework = "")
    //

    //javadoc: readNet(model, config, framework)
    public static Net readNet(String model, String config, String framework)
    {
        
        Net retVal = new Net(readNet_2(model, config, framework));
        
        return retVal;
    }

    //javadoc: readNet(model, config)
    public static Net readNet(String model, String config)
    {
        
        Net retVal = new Net(readNet_3(model, config));
        
        return retVal;
    }

    //javadoc: readNet(model)
    public static Net readNet(String model)
    {
        
        Net retVal = new Net(readNet_4(model));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromCaffe(String prototxt, String caffeModel = String())
    //

    //javadoc: readNetFromCaffe(prototxt, caffeModel)
    public static Net readNetFromCaffe(String prototxt, String caffeModel)
    {
        
        Net retVal = new Net(readNetFromCaffe_0(prototxt, caffeModel));
        
        return retVal;
    }

    //javadoc: readNetFromCaffe(prototxt)
    public static Net readNetFromCaffe(String prototxt)
    {
        
        Net retVal = new Net(readNetFromCaffe_1(prototxt));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromCaffe(vector_uchar bufferProto, vector_uchar bufferModel = std::vector<uchar>())
    //

    //javadoc: readNetFromCaffe(bufferProto, bufferModel)
    public static Net readNetFromCaffe(MatOfByte bufferProto, MatOfByte bufferModel)
    {
        Mat bufferProto_mat = bufferProto;
        Mat bufferModel_mat = bufferModel;
        Net retVal = new Net(readNetFromCaffe_2(bufferProto_mat.nativeObj, bufferModel_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: readNetFromCaffe(bufferProto)
    public static Net readNetFromCaffe(MatOfByte bufferProto)
    {
        Mat bufferProto_mat = bufferProto;
        Net retVal = new Net(readNetFromCaffe_3(bufferProto_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromDarknet(String cfgFile, String darknetModel = String())
    //

    //javadoc: readNetFromDarknet(cfgFile, darknetModel)
    public static Net readNetFromDarknet(String cfgFile, String darknetModel)
    {
        
        Net retVal = new Net(readNetFromDarknet_0(cfgFile, darknetModel));
        
        return retVal;
    }

    //javadoc: readNetFromDarknet(cfgFile)
    public static Net readNetFromDarknet(String cfgFile)
    {
        
        Net retVal = new Net(readNetFromDarknet_1(cfgFile));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromDarknet(vector_uchar bufferCfg, vector_uchar bufferModel = std::vector<uchar>())
    //

    //javadoc: readNetFromDarknet(bufferCfg, bufferModel)
    public static Net readNetFromDarknet(MatOfByte bufferCfg, MatOfByte bufferModel)
    {
        Mat bufferCfg_mat = bufferCfg;
        Mat bufferModel_mat = bufferModel;
        Net retVal = new Net(readNetFromDarknet_2(bufferCfg_mat.nativeObj, bufferModel_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: readNetFromDarknet(bufferCfg)
    public static Net readNetFromDarknet(MatOfByte bufferCfg)
    {
        Mat bufferCfg_mat = bufferCfg;
        Net retVal = new Net(readNetFromDarknet_3(bufferCfg_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromModelOptimizer(String xml, String bin)
    //

    //javadoc: readNetFromModelOptimizer(xml, bin)
    public static Net readNetFromModelOptimizer(String xml, String bin)
    {
        
        Net retVal = new Net(readNetFromModelOptimizer_0(xml, bin));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromONNX(String onnxFile)
    //

    //javadoc: readNetFromONNX(onnxFile)
    public static Net readNetFromONNX(String onnxFile)
    {
        
        Net retVal = new Net(readNetFromONNX_0(onnxFile));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromTensorflow(String model, String config = String())
    //

    //javadoc: readNetFromTensorflow(model, config)
    public static Net readNetFromTensorflow(String model, String config)
    {
        
        Net retVal = new Net(readNetFromTensorflow_0(model, config));
        
        return retVal;
    }

    //javadoc: readNetFromTensorflow(model)
    public static Net readNetFromTensorflow(String model)
    {
        
        Net retVal = new Net(readNetFromTensorflow_1(model));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromTensorflow(vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    //

    //javadoc: readNetFromTensorflow(bufferModel, bufferConfig)
    public static Net readNetFromTensorflow(MatOfByte bufferModel, MatOfByte bufferConfig)
    {
        Mat bufferModel_mat = bufferModel;
        Mat bufferConfig_mat = bufferConfig;
        Net retVal = new Net(readNetFromTensorflow_2(bufferModel_mat.nativeObj, bufferConfig_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: readNetFromTensorflow(bufferModel)
    public static Net readNetFromTensorflow(MatOfByte bufferModel)
    {
        Mat bufferModel_mat = bufferModel;
        Net retVal = new Net(readNetFromTensorflow_3(bufferModel_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Net cv::dnn::readNetFromTorch(String model, bool isBinary = true, bool evaluate = true)
    //

    //javadoc: readNetFromTorch(model, isBinary, evaluate)
    public static Net readNetFromTorch(String model, boolean isBinary, boolean evaluate)
    {
        
        Net retVal = new Net(readNetFromTorch_0(model, isBinary, evaluate));
        
        return retVal;
    }

    //javadoc: readNetFromTorch(model, isBinary)
    public static Net readNetFromTorch(String model, boolean isBinary)
    {
        
        Net retVal = new Net(readNetFromTorch_1(model, isBinary));
        
        return retVal;
    }

    //javadoc: readNetFromTorch(model)
    public static Net readNetFromTorch(String model)
    {
        
        Net retVal = new Net(readNetFromTorch_2(model));
        
        return retVal;
    }


    //
    // C++:  void cv::dnn::NMSBoxes(vector_Rect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices, eta, top_k)
    public static void NMSBoxes(MatOfRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_0(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
        
        return;
    }

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices, eta)
    public static void NMSBoxes(MatOfRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_1(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
        
        return;
    }

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices)
    public static void NMSBoxes(MatOfRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_2(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::dnn::NMSBoxes(vector_Rect2d bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices, eta, top_k)
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_3(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
        
        return;
    }

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices, eta)
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_4(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
        
        return;
    }

    //javadoc: NMSBoxes(bboxes, scores, score_threshold, nms_threshold, indices)
    public static void NMSBoxes(MatOfRect2d bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxes_5(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::dnn::NMSBoxes(vector_RotatedRect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    //

    //javadoc: NMSBoxesRotated(bboxes, scores, score_threshold, nms_threshold, indices, eta, top_k)
    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta, int top_k)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_0(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta, top_k);
        
        return;
    }

    //javadoc: NMSBoxesRotated(bboxes, scores, score_threshold, nms_threshold, indices, eta)
    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices, float eta)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_1(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj, eta);
        
        return;
    }

    //javadoc: NMSBoxesRotated(bboxes, scores, score_threshold, nms_threshold, indices)
    public static void NMSBoxesRotated(MatOfRotatedRect bboxes, MatOfFloat scores, float score_threshold, float nms_threshold, MatOfInt indices)
    {
        Mat bboxes_mat = bboxes;
        Mat scores_mat = scores;
        Mat indices_mat = indices;
        NMSBoxesRotated_2(bboxes_mat.nativeObj, scores_mat.nativeObj, score_threshold, nms_threshold, indices_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::dnn::imagesFromBlob(Mat blob_, vector_Mat& images_)
    //

    //javadoc: imagesFromBlob(blob_, images_)
    public static void imagesFromBlob(Mat blob_, List<Mat> images_)
    {
        Mat images__mat = new Mat();
        imagesFromBlob_0(blob_.nativeObj, images__mat.nativeObj);
        Converters.Mat_to_vector_Mat(images__mat, images_);
        images__mat.release();
        return;
    }


    //
    // C++:  void cv::dnn::resetMyriadDevice()
    //

    //javadoc: resetMyriadDevice()
    public static void resetMyriadDevice()
    {
        
        resetMyriadDevice_0();
        
        return;
    }


    //
    // C++:  void cv::dnn::shrinkCaffeModel(String src, String dst, vector_String layersTypes = std::vector<String>())
    //

    //javadoc: shrinkCaffeModel(src, dst, layersTypes)
    public static void shrinkCaffeModel(String src, String dst, List<String> layersTypes)
    {
        
        shrinkCaffeModel_0(src, dst, layersTypes);
        
        return;
    }

    //javadoc: shrinkCaffeModel(src, dst)
    public static void shrinkCaffeModel(String src, String dst)
    {
        
        shrinkCaffeModel_1(src, dst);
        
        return;
    }


    //
    // C++:  void cv::dnn::writeTextGraph(String model, String output)
    //

    //javadoc: writeTextGraph(model, output)
    public static void writeTextGraph(String model, String output)
    {
        
        writeTextGraph_0(model, output);
        
        return;
    }




    // C++:  Mat cv::dnn::blobFromImage(Mat image, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    private static native long blobFromImage_0(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop, int ddepth);
    private static native long blobFromImage_1(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImage_2(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB);
    private static native long blobFromImage_3(long image_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native long blobFromImage_4(long image_nativeObj, double scalefactor, double size_width, double size_height);
    private static native long blobFromImage_5(long image_nativeObj, double scalefactor);
    private static native long blobFromImage_6(long image_nativeObj);

    // C++:  Mat cv::dnn::blobFromImages(vector_Mat images, double scalefactor = 1.0, Size size = Size(), Scalar mean = Scalar(), bool swapRB = false, bool crop = false, int ddepth = CV_32F)
    private static native long blobFromImages_0(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop, int ddepth);
    private static native long blobFromImages_1(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB, boolean crop);
    private static native long blobFromImages_2(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3, boolean swapRB);
    private static native long blobFromImages_3(long images_mat_nativeObj, double scalefactor, double size_width, double size_height, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native long blobFromImages_4(long images_mat_nativeObj, double scalefactor, double size_width, double size_height);
    private static native long blobFromImages_5(long images_mat_nativeObj, double scalefactor);
    private static native long blobFromImages_6(long images_mat_nativeObj);

    // C++:  Mat cv::dnn::readTensorFromONNX(String path)
    private static native long readTensorFromONNX_0(String path);

    // C++:  Mat cv::dnn::readTorchBlob(String filename, bool isBinary = true)
    private static native long readTorchBlob_0(String filename, boolean isBinary);
    private static native long readTorchBlob_1(String filename);

    // C++:  Net cv::dnn::readNet(String framework, vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    private static native long readNet_0(String framework, long bufferModel_mat_nativeObj, long bufferConfig_mat_nativeObj);
    private static native long readNet_1(String framework, long bufferModel_mat_nativeObj);

    // C++:  Net cv::dnn::readNet(String model, String config = "", String framework = "")
    private static native long readNet_2(String model, String config, String framework);
    private static native long readNet_3(String model, String config);
    private static native long readNet_4(String model);

    // C++:  Net cv::dnn::readNetFromCaffe(String prototxt, String caffeModel = String())
    private static native long readNetFromCaffe_0(String prototxt, String caffeModel);
    private static native long readNetFromCaffe_1(String prototxt);

    // C++:  Net cv::dnn::readNetFromCaffe(vector_uchar bufferProto, vector_uchar bufferModel = std::vector<uchar>())
    private static native long readNetFromCaffe_2(long bufferProto_mat_nativeObj, long bufferModel_mat_nativeObj);
    private static native long readNetFromCaffe_3(long bufferProto_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromDarknet(String cfgFile, String darknetModel = String())
    private static native long readNetFromDarknet_0(String cfgFile, String darknetModel);
    private static native long readNetFromDarknet_1(String cfgFile);

    // C++:  Net cv::dnn::readNetFromDarknet(vector_uchar bufferCfg, vector_uchar bufferModel = std::vector<uchar>())
    private static native long readNetFromDarknet_2(long bufferCfg_mat_nativeObj, long bufferModel_mat_nativeObj);
    private static native long readNetFromDarknet_3(long bufferCfg_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromModelOptimizer(String xml, String bin)
    private static native long readNetFromModelOptimizer_0(String xml, String bin);

    // C++:  Net cv::dnn::readNetFromONNX(String onnxFile)
    private static native long readNetFromONNX_0(String onnxFile);

    // C++:  Net cv::dnn::readNetFromTensorflow(String model, String config = String())
    private static native long readNetFromTensorflow_0(String model, String config);
    private static native long readNetFromTensorflow_1(String model);

    // C++:  Net cv::dnn::readNetFromTensorflow(vector_uchar bufferModel, vector_uchar bufferConfig = std::vector<uchar>())
    private static native long readNetFromTensorflow_2(long bufferModel_mat_nativeObj, long bufferConfig_mat_nativeObj);
    private static native long readNetFromTensorflow_3(long bufferModel_mat_nativeObj);

    // C++:  Net cv::dnn::readNetFromTorch(String model, bool isBinary = true, bool evaluate = true)
    private static native long readNetFromTorch_0(String model, boolean isBinary, boolean evaluate);
    private static native long readNetFromTorch_1(String model, boolean isBinary);
    private static native long readNetFromTorch_2(String model);

    // C++:  void cv::dnn::NMSBoxes(vector_Rect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxes_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxes_1(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxes_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::NMSBoxes(vector_Rect2d bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxes_3(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxes_4(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxes_5(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::NMSBoxes(vector_RotatedRect bboxes, vector_float scores, float score_threshold, float nms_threshold, vector_int& indices, float eta = 1.f, int top_k = 0)
    private static native void NMSBoxesRotated_0(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta, int top_k);
    private static native void NMSBoxesRotated_1(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj, float eta);
    private static native void NMSBoxesRotated_2(long bboxes_mat_nativeObj, long scores_mat_nativeObj, float score_threshold, float nms_threshold, long indices_mat_nativeObj);

    // C++:  void cv::dnn::imagesFromBlob(Mat blob_, vector_Mat& images_)
    private static native void imagesFromBlob_0(long blob__nativeObj, long images__mat_nativeObj);

    // C++:  void cv::dnn::resetMyriadDevice()
    private static native void resetMyriadDevice_0();

    // C++:  void cv::dnn::shrinkCaffeModel(String src, String dst, vector_String layersTypes = std::vector<String>())
    private static native void shrinkCaffeModel_0(String src, String dst, List<String> layersTypes);
    private static native void shrinkCaffeModel_1(String src, String dst);

    // C++:  void cv::dnn::writeTextGraph(String model, String output)
    private static native void writeTextGraph_0(String model, String output);

}
