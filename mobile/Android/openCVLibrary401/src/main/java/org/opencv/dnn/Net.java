//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.dnn.DictValue;
import org.opencv.dnn.Layer;
import org.opencv.dnn.Net;
import org.opencv.utils.Converters;

// C++: class Net
//javadoc: Net

public class Net {

    protected final long nativeObj;
    protected Net(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static Net __fromPtr__(long addr) { return new Net(addr); }

    //
    // C++:   cv::dnn::Net::Net()
    //

    //javadoc: Net::Net()
    public   Net()
    {
        
        nativeObj = Net_0();
        
        return;
    }


    //
    // C++:  Mat cv::dnn::Net::forward(String outputName = String())
    //

    //javadoc: Net::forward(outputName)
    public  Mat forward(String outputName)
    {
        
        Mat retVal = new Mat(forward_0(nativeObj, outputName));
        
        return retVal;
    }

    //javadoc: Net::forward()
    public  Mat forward()
    {
        
        Mat retVal = new Mat(forward_1(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::dnn::Net::getParam(LayerId layer, int numParam = 0)
    //

    //javadoc: Net::getParam(layer, numParam)
    public  Mat getParam(DictValue layer, int numParam)
    {
        
        Mat retVal = new Mat(getParam_0(nativeObj, layer.getNativeObjAddr(), numParam));
        
        return retVal;
    }

    //javadoc: Net::getParam(layer)
    public  Mat getParam(DictValue layer)
    {
        
        Mat retVal = new Mat(getParam_1(nativeObj, layer.getNativeObjAddr()));
        
        return retVal;
    }


    //
    // C++: static Net cv::dnn::Net::readFromModelOptimizer(String xml, String bin)
    //

    //javadoc: Net::readFromModelOptimizer(xml, bin)
    public static Net readFromModelOptimizer(String xml, String bin)
    {
        
        Net retVal = new Net(readFromModelOptimizer_0(xml, bin));
        
        return retVal;
    }


    //
    // C++:  Ptr_Layer cv::dnn::Net::getLayer(LayerId layerId)
    //

    //javadoc: Net::getLayer(layerId)
    public  Layer getLayer(DictValue layerId)
    {
        
        Layer retVal = Layer.__fromPtr__(getLayer_0(nativeObj, layerId.getNativeObjAddr()));
        
        return retVal;
    }


    //
    // C++:  bool cv::dnn::Net::empty()
    //

    //javadoc: Net::empty()
    public  boolean empty()
    {
        
        boolean retVal = empty_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::dnn::Net::getLayerId(String layer)
    //

    //javadoc: Net::getLayerId(layer)
    public  int getLayerId(String layer)
    {
        
        int retVal = getLayerId_0(nativeObj, layer);
        
        return retVal;
    }


    //
    // C++:  int cv::dnn::Net::getLayersCount(String layerType)
    //

    //javadoc: Net::getLayersCount(layerType)
    public  int getLayersCount(String layerType)
    {
        
        int retVal = getLayersCount_0(nativeObj, layerType);
        
        return retVal;
    }


    //
    // C++:  int64 cv::dnn::Net::getFLOPS(MatShape netInputShape)
    //

    //javadoc: Net::getFLOPS(netInputShape)
    public  long getFLOPS(MatOfInt netInputShape)
    {
        Mat netInputShape_mat = netInputShape;
        long retVal = getFLOPS_0(nativeObj, netInputShape_mat.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int64 cv::dnn::Net::getFLOPS(int layerId, MatShape netInputShape)
    //

    //javadoc: Net::getFLOPS(layerId, netInputShape)
    public  long getFLOPS(int layerId, MatOfInt netInputShape)
    {
        Mat netInputShape_mat = netInputShape;
        long retVal = getFLOPS_1(nativeObj, layerId, netInputShape_mat.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int64 cv::dnn::Net::getFLOPS(int layerId, vector_MatShape netInputShapes)
    //

    //javadoc: Net::getFLOPS(layerId, netInputShapes)
    public  long getFLOPS(int layerId, List<MatOfInt> netInputShapes)
    {
        
        long retVal = getFLOPS_2(nativeObj, layerId, netInputShapes);
        
        return retVal;
    }


    //
    // C++:  int64 cv::dnn::Net::getFLOPS(vector_MatShape netInputShapes)
    //

    //javadoc: Net::getFLOPS(netInputShapes)
    public  long getFLOPS(List<MatOfInt> netInputShapes)
    {
        
        long retVal = getFLOPS_3(nativeObj, netInputShapes);
        
        return retVal;
    }


    //
    // C++:  int64 cv::dnn::Net::getPerfProfile(vector_double& timings)
    //

    //javadoc: Net::getPerfProfile(timings)
    public  long getPerfProfile(MatOfDouble timings)
    {
        Mat timings_mat = timings;
        long retVal = getPerfProfile_0(nativeObj, timings_mat.nativeObj);
        
        return retVal;
    }


    //
    // C++:  vector_String cv::dnn::Net::getLayerNames()
    //

    //javadoc: Net::getLayerNames()
    public  List<String> getLayerNames()
    {
        
        List<String> retVal = getLayerNames_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  vector_String cv::dnn::Net::getUnconnectedOutLayersNames()
    //

    //javadoc: Net::getUnconnectedOutLayersNames()
    public  List<String> getUnconnectedOutLayersNames()
    {
        
        List<String> retVal = getUnconnectedOutLayersNames_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  vector_int cv::dnn::Net::getUnconnectedOutLayers()
    //

    //javadoc: Net::getUnconnectedOutLayers()
    public  MatOfInt getUnconnectedOutLayers()
    {
        
        MatOfInt retVal = MatOfInt.fromNativeAddr(getUnconnectedOutLayers_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  void cv::dnn::Net::connect(String outPin, String inpPin)
    //

    //javadoc: Net::connect(outPin, inpPin)
    public  void connect(String outPin, String inpPin)
    {
        
        connect_0(nativeObj, outPin, inpPin);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::enableFusion(bool fusion)
    //

    //javadoc: Net::enableFusion(fusion)
    public  void enableFusion(boolean fusion)
    {
        
        enableFusion_0(nativeObj, fusion);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::forward(vector_Mat& outputBlobs, String outputName = String())
    //

    //javadoc: Net::forward(outputBlobs, outputName)
    public  void forward(List<Mat> outputBlobs, String outputName)
    {
        Mat outputBlobs_mat = new Mat();
        forward_2(nativeObj, outputBlobs_mat.nativeObj, outputName);
        Converters.Mat_to_vector_Mat(outputBlobs_mat, outputBlobs);
        outputBlobs_mat.release();
        return;
    }

    //javadoc: Net::forward(outputBlobs)
    public  void forward(List<Mat> outputBlobs)
    {
        Mat outputBlobs_mat = new Mat();
        forward_3(nativeObj, outputBlobs_mat.nativeObj);
        Converters.Mat_to_vector_Mat(outputBlobs_mat, outputBlobs);
        outputBlobs_mat.release();
        return;
    }


    //
    // C++:  void cv::dnn::Net::forward(vector_Mat& outputBlobs, vector_String outBlobNames)
    //

    //javadoc: Net::forward(outputBlobs, outBlobNames)
    public  void forward(List<Mat> outputBlobs, List<String> outBlobNames)
    {
        Mat outputBlobs_mat = new Mat();
        forward_4(nativeObj, outputBlobs_mat.nativeObj, outBlobNames);
        Converters.Mat_to_vector_Mat(outputBlobs_mat, outputBlobs);
        outputBlobs_mat.release();
        return;
    }


    //
    // C++:  void cv::dnn::Net::forward(vector_vector_Mat& outputBlobs, vector_String outBlobNames)
    //

    // Unknown type 'vector_vector_Mat' (O), skipping the function


    //
    // C++:  void cv::dnn::Net::getLayerTypes(vector_String& layersTypes)
    //

    //javadoc: Net::getLayerTypes(layersTypes)
    public  void getLayerTypes(List<String> layersTypes)
    {
        
        getLayerTypes_0(nativeObj, layersTypes);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::getLayersShapes(MatShape netInputShape, vector_int& layersIds, vector_vector_MatShape& inLayersShapes, vector_vector_MatShape& outLayersShapes)
    //

    // Unknown type 'vector_vector_MatShape' (O), skipping the function


    //
    // C++:  void cv::dnn::Net::getLayersShapes(vector_MatShape netInputShapes, vector_int& layersIds, vector_vector_MatShape& inLayersShapes, vector_vector_MatShape& outLayersShapes)
    //

    // Unknown type 'vector_vector_MatShape' (O), skipping the function


    //
    // C++:  void cv::dnn::Net::getMemoryConsumption(MatShape netInputShape, size_t& weights, size_t& blobs)
    //

    //javadoc: Net::getMemoryConsumption(netInputShape, weights, blobs)
    public  void getMemoryConsumption(MatOfInt netInputShape, long[] weights, long[] blobs)
    {
        Mat netInputShape_mat = netInputShape;
        double[] weights_out = new double[1];
        double[] blobs_out = new double[1];
        getMemoryConsumption_0(nativeObj, netInputShape_mat.nativeObj, weights_out, blobs_out);
        if(weights!=null) weights[0] = (long)weights_out[0];
        if(blobs!=null) blobs[0] = (long)blobs_out[0];
        return;
    }


    //
    // C++:  void cv::dnn::Net::getMemoryConsumption(int layerId, MatShape netInputShape, size_t& weights, size_t& blobs)
    //

    //javadoc: Net::getMemoryConsumption(layerId, netInputShape, weights, blobs)
    public  void getMemoryConsumption(int layerId, MatOfInt netInputShape, long[] weights, long[] blobs)
    {
        Mat netInputShape_mat = netInputShape;
        double[] weights_out = new double[1];
        double[] blobs_out = new double[1];
        getMemoryConsumption_1(nativeObj, layerId, netInputShape_mat.nativeObj, weights_out, blobs_out);
        if(weights!=null) weights[0] = (long)weights_out[0];
        if(blobs!=null) blobs[0] = (long)blobs_out[0];
        return;
    }


    //
    // C++:  void cv::dnn::Net::getMemoryConsumption(int layerId, vector_MatShape netInputShapes, size_t& weights, size_t& blobs)
    //

    //javadoc: Net::getMemoryConsumption(layerId, netInputShapes, weights, blobs)
    public  void getMemoryConsumption(int layerId, List<MatOfInt> netInputShapes, long[] weights, long[] blobs)
    {
        double[] weights_out = new double[1];
        double[] blobs_out = new double[1];
        getMemoryConsumption_2(nativeObj, layerId, netInputShapes, weights_out, blobs_out);
        if(weights!=null) weights[0] = (long)weights_out[0];
        if(blobs!=null) blobs[0] = (long)blobs_out[0];
        return;
    }


    //
    // C++:  void cv::dnn::Net::setHalideScheduler(String scheduler)
    //

    //javadoc: Net::setHalideScheduler(scheduler)
    public  void setHalideScheduler(String scheduler)
    {
        
        setHalideScheduler_0(nativeObj, scheduler);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::setInput(Mat blob, String name = "", double scalefactor = 1.0, Scalar mean = Scalar())
    //

    //javadoc: Net::setInput(blob, name, scalefactor, mean)
    public  void setInput(Mat blob, String name, double scalefactor, Scalar mean)
    {
        
        setInput_0(nativeObj, blob.nativeObj, name, scalefactor, mean.val[0], mean.val[1], mean.val[2], mean.val[3]);
        
        return;
    }

    //javadoc: Net::setInput(blob, name, scalefactor)
    public  void setInput(Mat blob, String name, double scalefactor)
    {
        
        setInput_1(nativeObj, blob.nativeObj, name, scalefactor);
        
        return;
    }

    //javadoc: Net::setInput(blob, name)
    public  void setInput(Mat blob, String name)
    {
        
        setInput_2(nativeObj, blob.nativeObj, name);
        
        return;
    }

    //javadoc: Net::setInput(blob)
    public  void setInput(Mat blob)
    {
        
        setInput_3(nativeObj, blob.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::setInputsNames(vector_String inputBlobNames)
    //

    //javadoc: Net::setInputsNames(inputBlobNames)
    public  void setInputsNames(List<String> inputBlobNames)
    {
        
        setInputsNames_0(nativeObj, inputBlobNames);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::setParam(LayerId layer, int numParam, Mat blob)
    //

    //javadoc: Net::setParam(layer, numParam, blob)
    public  void setParam(DictValue layer, int numParam, Mat blob)
    {
        
        setParam_0(nativeObj, layer.getNativeObjAddr(), numParam, blob.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::setPreferableBackend(int backendId)
    //

    //javadoc: Net::setPreferableBackend(backendId)
    public  void setPreferableBackend(int backendId)
    {
        
        setPreferableBackend_0(nativeObj, backendId);
        
        return;
    }


    //
    // C++:  void cv::dnn::Net::setPreferableTarget(int targetId)
    //

    //javadoc: Net::setPreferableTarget(targetId)
    public  void setPreferableTarget(int targetId)
    {
        
        setPreferableTarget_0(nativeObj, targetId);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::Net::Net()
    private static native long Net_0();

    // C++:  Mat cv::dnn::Net::forward(String outputName = String())
    private static native long forward_0(long nativeObj, String outputName);
    private static native long forward_1(long nativeObj);

    // C++:  Mat cv::dnn::Net::getParam(LayerId layer, int numParam = 0)
    private static native long getParam_0(long nativeObj, long layer_nativeObj, int numParam);
    private static native long getParam_1(long nativeObj, long layer_nativeObj);

    // C++: static Net cv::dnn::Net::readFromModelOptimizer(String xml, String bin)
    private static native long readFromModelOptimizer_0(String xml, String bin);

    // C++:  Ptr_Layer cv::dnn::Net::getLayer(LayerId layerId)
    private static native long getLayer_0(long nativeObj, long layerId_nativeObj);

    // C++:  bool cv::dnn::Net::empty()
    private static native boolean empty_0(long nativeObj);

    // C++:  int cv::dnn::Net::getLayerId(String layer)
    private static native int getLayerId_0(long nativeObj, String layer);

    // C++:  int cv::dnn::Net::getLayersCount(String layerType)
    private static native int getLayersCount_0(long nativeObj, String layerType);

    // C++:  int64 cv::dnn::Net::getFLOPS(MatShape netInputShape)
    private static native long getFLOPS_0(long nativeObj, long netInputShape_mat_nativeObj);

    // C++:  int64 cv::dnn::Net::getFLOPS(int layerId, MatShape netInputShape)
    private static native long getFLOPS_1(long nativeObj, int layerId, long netInputShape_mat_nativeObj);

    // C++:  int64 cv::dnn::Net::getFLOPS(int layerId, vector_MatShape netInputShapes)
    private static native long getFLOPS_2(long nativeObj, int layerId, List<MatOfInt> netInputShapes);

    // C++:  int64 cv::dnn::Net::getFLOPS(vector_MatShape netInputShapes)
    private static native long getFLOPS_3(long nativeObj, List<MatOfInt> netInputShapes);

    // C++:  int64 cv::dnn::Net::getPerfProfile(vector_double& timings)
    private static native long getPerfProfile_0(long nativeObj, long timings_mat_nativeObj);

    // C++:  vector_String cv::dnn::Net::getLayerNames()
    private static native List<String> getLayerNames_0(long nativeObj);

    // C++:  vector_String cv::dnn::Net::getUnconnectedOutLayersNames()
    private static native List<String> getUnconnectedOutLayersNames_0(long nativeObj);

    // C++:  vector_int cv::dnn::Net::getUnconnectedOutLayers()
    private static native long getUnconnectedOutLayers_0(long nativeObj);

    // C++:  void cv::dnn::Net::connect(String outPin, String inpPin)
    private static native void connect_0(long nativeObj, String outPin, String inpPin);

    // C++:  void cv::dnn::Net::enableFusion(bool fusion)
    private static native void enableFusion_0(long nativeObj, boolean fusion);

    // C++:  void cv::dnn::Net::forward(vector_Mat& outputBlobs, String outputName = String())
    private static native void forward_2(long nativeObj, long outputBlobs_mat_nativeObj, String outputName);
    private static native void forward_3(long nativeObj, long outputBlobs_mat_nativeObj);

    // C++:  void cv::dnn::Net::forward(vector_Mat& outputBlobs, vector_String outBlobNames)
    private static native void forward_4(long nativeObj, long outputBlobs_mat_nativeObj, List<String> outBlobNames);

    // C++:  void cv::dnn::Net::getLayerTypes(vector_String& layersTypes)
    private static native void getLayerTypes_0(long nativeObj, List<String> layersTypes);

    // C++:  void cv::dnn::Net::getMemoryConsumption(MatShape netInputShape, size_t& weights, size_t& blobs)
    private static native void getMemoryConsumption_0(long nativeObj, long netInputShape_mat_nativeObj, double[] weights_out, double[] blobs_out);

    // C++:  void cv::dnn::Net::getMemoryConsumption(int layerId, MatShape netInputShape, size_t& weights, size_t& blobs)
    private static native void getMemoryConsumption_1(long nativeObj, int layerId, long netInputShape_mat_nativeObj, double[] weights_out, double[] blobs_out);

    // C++:  void cv::dnn::Net::getMemoryConsumption(int layerId, vector_MatShape netInputShapes, size_t& weights, size_t& blobs)
    private static native void getMemoryConsumption_2(long nativeObj, int layerId, List<MatOfInt> netInputShapes, double[] weights_out, double[] blobs_out);

    // C++:  void cv::dnn::Net::setHalideScheduler(String scheduler)
    private static native void setHalideScheduler_0(long nativeObj, String scheduler);

    // C++:  void cv::dnn::Net::setInput(Mat blob, String name = "", double scalefactor = 1.0, Scalar mean = Scalar())
    private static native void setInput_0(long nativeObj, long blob_nativeObj, String name, double scalefactor, double mean_val0, double mean_val1, double mean_val2, double mean_val3);
    private static native void setInput_1(long nativeObj, long blob_nativeObj, String name, double scalefactor);
    private static native void setInput_2(long nativeObj, long blob_nativeObj, String name);
    private static native void setInput_3(long nativeObj, long blob_nativeObj);

    // C++:  void cv::dnn::Net::setInputsNames(vector_String inputBlobNames)
    private static native void setInputsNames_0(long nativeObj, List<String> inputBlobNames);

    // C++:  void cv::dnn::Net::setParam(LayerId layer, int numParam, Mat blob)
    private static native void setParam_0(long nativeObj, long layer_nativeObj, int numParam, long blob_nativeObj);

    // C++:  void cv::dnn::Net::setPreferableBackend(int backendId)
    private static native void setPreferableBackend_0(long nativeObj, int backendId);

    // C++:  void cv::dnn::Net::setPreferableTarget(int targetId)
    private static native void setPreferableTarget_0(long nativeObj, int targetId);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
