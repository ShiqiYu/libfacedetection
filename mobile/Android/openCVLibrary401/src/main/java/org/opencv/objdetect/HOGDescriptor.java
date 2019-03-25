//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

// C++: class HOGDescriptor
//javadoc: HOGDescriptor

public class HOGDescriptor {

    protected final long nativeObj;
    protected HOGDescriptor(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static HOGDescriptor __fromPtr__(long addr) { return new HOGDescriptor(addr); }

    // C++: enum DescriptorStorageFormat
    public static final int
            DESCR_FORMAT_COL_BY_COL = 0,
            DESCR_FORMAT_ROW_BY_ROW = 1;


    // C++: enum <unnamed>
    public static final int
            DEFAULT_NLEVELS = 64;


    // C++: enum HistogramNormType
    public static final int
            L2Hys = 0;


    //
    // C++:   cv::HOGDescriptor::HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture = 1, double _winSigma = -1, HOGDescriptor_HistogramNormType _histogramNormType = HOGDescriptor::L2Hys, double _L2HysThreshold = 0.2, bool _gammaCorrection = false, int _nlevels = HOGDescriptor::DEFAULT_NLEVELS, bool _signedGradient = false)
    //

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels, _signedGradient)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection, int _nlevels, boolean _signedGradient)
    {
        
        nativeObj = HOGDescriptor_0(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels, _signedGradient);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection, int _nlevels)
    {
        
        nativeObj = HOGDescriptor_1(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection)
    {
        
        nativeObj = HOGDescriptor_2(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold)
    {
        
        nativeObj = HOGDescriptor_3(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType)
    {
        
        nativeObj = HOGDescriptor_4(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma, _histogramNormType);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture, double _winSigma)
    {
        
        nativeObj = HOGDescriptor_5(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture, _winSigma);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture)
    {
        
        nativeObj = HOGDescriptor_6(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins, _derivAperture);
        
        return;
    }

    //javadoc: HOGDescriptor::HOGDescriptor(_winSize, _blockSize, _blockStride, _cellSize, _nbins)
    public   HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins)
    {
        
        nativeObj = HOGDescriptor_7(_winSize.width, _winSize.height, _blockSize.width, _blockSize.height, _blockStride.width, _blockStride.height, _cellSize.width, _cellSize.height, _nbins);
        
        return;
    }


    //
    // C++:   cv::HOGDescriptor::HOGDescriptor(String filename)
    //

    //javadoc: HOGDescriptor::HOGDescriptor(filename)
    public   HOGDescriptor(String filename)
    {
        
        nativeObj = HOGDescriptor_8(filename);
        
        return;
    }


    //
    // C++:   cv::HOGDescriptor::HOGDescriptor()
    //

    //javadoc: HOGDescriptor::HOGDescriptor()
    public   HOGDescriptor()
    {
        
        nativeObj = HOGDescriptor_9();
        
        return;
    }


    //
    // C++:  bool cv::HOGDescriptor::checkDetectorSize()
    //

    //javadoc: HOGDescriptor::checkDetectorSize()
    public  boolean checkDetectorSize()
    {
        
        boolean retVal = checkDetectorSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::HOGDescriptor::load(String filename, String objname = String())
    //

    //javadoc: HOGDescriptor::load(filename, objname)
    public  boolean load(String filename, String objname)
    {
        
        boolean retVal = load_0(nativeObj, filename, objname);
        
        return retVal;
    }

    //javadoc: HOGDescriptor::load(filename)
    public  boolean load(String filename)
    {
        
        boolean retVal = load_1(nativeObj, filename);
        
        return retVal;
    }


    //
    // C++:  double cv::HOGDescriptor::getWinSigma()
    //

    //javadoc: HOGDescriptor::getWinSigma()
    public  double getWinSigma()
    {
        
        double retVal = getWinSigma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  size_t cv::HOGDescriptor::getDescriptorSize()
    //

    //javadoc: HOGDescriptor::getDescriptorSize()
    public  long getDescriptorSize()
    {
        
        long retVal = getDescriptorSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: static vector_float cv::HOGDescriptor::getDaimlerPeopleDetector()
    //

    //javadoc: HOGDescriptor::getDaimlerPeopleDetector()
    public static MatOfFloat getDaimlerPeopleDetector()
    {
        
        MatOfFloat retVal = MatOfFloat.fromNativeAddr(getDaimlerPeopleDetector_0());
        
        return retVal;
    }


    //
    // C++: static vector_float cv::HOGDescriptor::getDefaultPeopleDetector()
    //

    //javadoc: HOGDescriptor::getDefaultPeopleDetector()
    public static MatOfFloat getDefaultPeopleDetector()
    {
        
        MatOfFloat retVal = MatOfFloat.fromNativeAddr(getDefaultPeopleDetector_0());
        
        return retVal;
    }


    //
    // C++:  void cv::HOGDescriptor::compute(Mat img, vector_float& descriptors, Size winStride = Size(), Size padding = Size(), vector_Point locations = std::vector<Point>())
    //

    //javadoc: HOGDescriptor::compute(img, descriptors, winStride, padding, locations)
    public  void compute(Mat img, MatOfFloat descriptors, Size winStride, Size padding, MatOfPoint locations)
    {
        Mat descriptors_mat = descriptors;
        Mat locations_mat = locations;
        compute_0(nativeObj, img.nativeObj, descriptors_mat.nativeObj, winStride.width, winStride.height, padding.width, padding.height, locations_mat.nativeObj);
        
        return;
    }

    //javadoc: HOGDescriptor::compute(img, descriptors, winStride, padding)
    public  void compute(Mat img, MatOfFloat descriptors, Size winStride, Size padding)
    {
        Mat descriptors_mat = descriptors;
        compute_1(nativeObj, img.nativeObj, descriptors_mat.nativeObj, winStride.width, winStride.height, padding.width, padding.height);
        
        return;
    }

    //javadoc: HOGDescriptor::compute(img, descriptors, winStride)
    public  void compute(Mat img, MatOfFloat descriptors, Size winStride)
    {
        Mat descriptors_mat = descriptors;
        compute_2(nativeObj, img.nativeObj, descriptors_mat.nativeObj, winStride.width, winStride.height);
        
        return;
    }

    //javadoc: HOGDescriptor::compute(img, descriptors)
    public  void compute(Mat img, MatOfFloat descriptors)
    {
        Mat descriptors_mat = descriptors;
        compute_3(nativeObj, img.nativeObj, descriptors_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::HOGDescriptor::computeGradient(Mat img, Mat& grad, Mat& angleOfs, Size paddingTL = Size(), Size paddingBR = Size())
    //

    //javadoc: HOGDescriptor::computeGradient(img, grad, angleOfs, paddingTL, paddingBR)
    public  void computeGradient(Mat img, Mat grad, Mat angleOfs, Size paddingTL, Size paddingBR)
    {
        
        computeGradient_0(nativeObj, img.nativeObj, grad.nativeObj, angleOfs.nativeObj, paddingTL.width, paddingTL.height, paddingBR.width, paddingBR.height);
        
        return;
    }

    //javadoc: HOGDescriptor::computeGradient(img, grad, angleOfs, paddingTL)
    public  void computeGradient(Mat img, Mat grad, Mat angleOfs, Size paddingTL)
    {
        
        computeGradient_1(nativeObj, img.nativeObj, grad.nativeObj, angleOfs.nativeObj, paddingTL.width, paddingTL.height);
        
        return;
    }

    //javadoc: HOGDescriptor::computeGradient(img, grad, angleOfs)
    public  void computeGradient(Mat img, Mat grad, Mat angleOfs)
    {
        
        computeGradient_2(nativeObj, img.nativeObj, grad.nativeObj, angleOfs.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::HOGDescriptor::detect(Mat img, vector_Point& foundLocations, vector_double& weights, double hitThreshold = 0, Size winStride = Size(), Size padding = Size(), vector_Point searchLocations = std::vector<Point>())
    //

    //javadoc: HOGDescriptor::detect(img, foundLocations, weights, hitThreshold, winStride, padding, searchLocations)
    public  void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights, double hitThreshold, Size winStride, Size padding, MatOfPoint searchLocations)
    {
        Mat foundLocations_mat = foundLocations;
        Mat weights_mat = weights;
        Mat searchLocations_mat = searchLocations;
        detect_0(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, weights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, searchLocations_mat.nativeObj);
        
        return;
    }

    //javadoc: HOGDescriptor::detect(img, foundLocations, weights, hitThreshold, winStride, padding)
    public  void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights, double hitThreshold, Size winStride, Size padding)
    {
        Mat foundLocations_mat = foundLocations;
        Mat weights_mat = weights;
        detect_1(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, weights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height);
        
        return;
    }

    //javadoc: HOGDescriptor::detect(img, foundLocations, weights, hitThreshold, winStride)
    public  void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights, double hitThreshold, Size winStride)
    {
        Mat foundLocations_mat = foundLocations;
        Mat weights_mat = weights;
        detect_2(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, weights_mat.nativeObj, hitThreshold, winStride.width, winStride.height);
        
        return;
    }

    //javadoc: HOGDescriptor::detect(img, foundLocations, weights, hitThreshold)
    public  void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights, double hitThreshold)
    {
        Mat foundLocations_mat = foundLocations;
        Mat weights_mat = weights;
        detect_3(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, weights_mat.nativeObj, hitThreshold);
        
        return;
    }

    //javadoc: HOGDescriptor::detect(img, foundLocations, weights)
    public  void detect(Mat img, MatOfPoint foundLocations, MatOfDouble weights)
    {
        Mat foundLocations_mat = foundLocations;
        Mat weights_mat = weights;
        detect_4(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, weights_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::HOGDescriptor::detectMultiScale(Mat img, vector_Rect& foundLocations, vector_double& foundWeights, double hitThreshold = 0, Size winStride = Size(), Size padding = Size(), double scale = 1.05, double finalThreshold = 2.0, bool useMeanshiftGrouping = false)
    //

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold, winStride, padding, scale, finalThreshold, useMeanshiftGrouping)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride, Size padding, double scale, double finalThreshold, boolean useMeanshiftGrouping)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_0(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, scale, finalThreshold, useMeanshiftGrouping);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold, winStride, padding, scale, finalThreshold)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride, Size padding, double scale, double finalThreshold)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_1(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, scale, finalThreshold);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold, winStride, padding, scale)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride, Size padding, double scale)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_2(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height, scale);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold, winStride, padding)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride, Size padding)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_3(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold, winStride.width, winStride.height, padding.width, padding.height);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold, winStride)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold, Size winStride)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_4(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold, winStride.width, winStride.height);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights, hitThreshold)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights, double hitThreshold)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_5(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj, hitThreshold);
        
        return;
    }

    //javadoc: HOGDescriptor::detectMultiScale(img, foundLocations, foundWeights)
    public  void detectMultiScale(Mat img, MatOfRect foundLocations, MatOfDouble foundWeights)
    {
        Mat foundLocations_mat = foundLocations;
        Mat foundWeights_mat = foundWeights;
        detectMultiScale_6(nativeObj, img.nativeObj, foundLocations_mat.nativeObj, foundWeights_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::HOGDescriptor::save(String filename, String objname = String())
    //

    //javadoc: HOGDescriptor::save(filename, objname)
    public  void save(String filename, String objname)
    {
        
        save_0(nativeObj, filename, objname);
        
        return;
    }

    //javadoc: HOGDescriptor::save(filename)
    public  void save(String filename)
    {
        
        save_1(nativeObj, filename);
        
        return;
    }


    //
    // C++:  void cv::HOGDescriptor::setSVMDetector(Mat svmdetector)
    //

    //javadoc: HOGDescriptor::setSVMDetector(svmdetector)
    public  void setSVMDetector(Mat svmdetector)
    {
        
        setSVMDetector_0(nativeObj, svmdetector.nativeObj);
        
        return;
    }


    //
    // C++: Size HOGDescriptor::winSize
    //

    //javadoc: HOGDescriptor::get_winSize()
    public  Size get_winSize()
    {
        
        Size retVal = new Size(get_winSize_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: Size HOGDescriptor::blockSize
    //

    //javadoc: HOGDescriptor::get_blockSize()
    public  Size get_blockSize()
    {
        
        Size retVal = new Size(get_blockSize_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: Size HOGDescriptor::blockStride
    //

    //javadoc: HOGDescriptor::get_blockStride()
    public  Size get_blockStride()
    {
        
        Size retVal = new Size(get_blockStride_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: Size HOGDescriptor::cellSize
    //

    //javadoc: HOGDescriptor::get_cellSize()
    public  Size get_cellSize()
    {
        
        Size retVal = new Size(get_cellSize_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: int HOGDescriptor::nbins
    //

    //javadoc: HOGDescriptor::get_nbins()
    public  int get_nbins()
    {
        
        int retVal = get_nbins_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: int HOGDescriptor::derivAperture
    //

    //javadoc: HOGDescriptor::get_derivAperture()
    public  int get_derivAperture()
    {
        
        int retVal = get_derivAperture_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: double HOGDescriptor::winSigma
    //

    //javadoc: HOGDescriptor::get_winSigma()
    public  double get_winSigma()
    {
        
        double retVal = get_winSigma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: HOGDescriptor_HistogramNormType HOGDescriptor::histogramNormType
    //

    //javadoc: HOGDescriptor::get_histogramNormType()
    public  int get_histogramNormType()
    {
        
        int retVal = get_histogramNormType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: double HOGDescriptor::L2HysThreshold
    //

    //javadoc: HOGDescriptor::get_L2HysThreshold()
    public  double get_L2HysThreshold()
    {
        
        double retVal = get_L2HysThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: bool HOGDescriptor::gammaCorrection
    //

    //javadoc: HOGDescriptor::get_gammaCorrection()
    public  boolean get_gammaCorrection()
    {
        
        boolean retVal = get_gammaCorrection_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: vector_float HOGDescriptor::svmDetector
    //

    //javadoc: HOGDescriptor::get_svmDetector()
    public  MatOfFloat get_svmDetector()
    {
        
        MatOfFloat retVal = MatOfFloat.fromNativeAddr(get_svmDetector_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: int HOGDescriptor::nlevels
    //

    //javadoc: HOGDescriptor::get_nlevels()
    public  int get_nlevels()
    {
        
        int retVal = get_nlevels_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: bool HOGDescriptor::signedGradient
    //

    //javadoc: HOGDescriptor::get_signedGradient()
    public  boolean get_signedGradient()
    {
        
        boolean retVal = get_signedGradient_0(nativeObj);
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::HOGDescriptor::HOGDescriptor(Size _winSize, Size _blockSize, Size _blockStride, Size _cellSize, int _nbins, int _derivAperture = 1, double _winSigma = -1, HOGDescriptor_HistogramNormType _histogramNormType = HOGDescriptor::L2Hys, double _L2HysThreshold = 0.2, bool _gammaCorrection = false, int _nlevels = HOGDescriptor::DEFAULT_NLEVELS, bool _signedGradient = false)
    private static native long HOGDescriptor_0(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection, int _nlevels, boolean _signedGradient);
    private static native long HOGDescriptor_1(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection, int _nlevels);
    private static native long HOGDescriptor_2(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, boolean _gammaCorrection);
    private static native long HOGDescriptor_3(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold);
    private static native long HOGDescriptor_4(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType);
    private static native long HOGDescriptor_5(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture, double _winSigma);
    private static native long HOGDescriptor_6(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins, int _derivAperture);
    private static native long HOGDescriptor_7(double _winSize_width, double _winSize_height, double _blockSize_width, double _blockSize_height, double _blockStride_width, double _blockStride_height, double _cellSize_width, double _cellSize_height, int _nbins);

    // C++:   cv::HOGDescriptor::HOGDescriptor(String filename)
    private static native long HOGDescriptor_8(String filename);

    // C++:   cv::HOGDescriptor::HOGDescriptor()
    private static native long HOGDescriptor_9();

    // C++:  bool cv::HOGDescriptor::checkDetectorSize()
    private static native boolean checkDetectorSize_0(long nativeObj);

    // C++:  bool cv::HOGDescriptor::load(String filename, String objname = String())
    private static native boolean load_0(long nativeObj, String filename, String objname);
    private static native boolean load_1(long nativeObj, String filename);

    // C++:  double cv::HOGDescriptor::getWinSigma()
    private static native double getWinSigma_0(long nativeObj);

    // C++:  size_t cv::HOGDescriptor::getDescriptorSize()
    private static native long getDescriptorSize_0(long nativeObj);

    // C++: static vector_float cv::HOGDescriptor::getDaimlerPeopleDetector()
    private static native long getDaimlerPeopleDetector_0();

    // C++: static vector_float cv::HOGDescriptor::getDefaultPeopleDetector()
    private static native long getDefaultPeopleDetector_0();

    // C++:  void cv::HOGDescriptor::compute(Mat img, vector_float& descriptors, Size winStride = Size(), Size padding = Size(), vector_Point locations = std::vector<Point>())
    private static native void compute_0(long nativeObj, long img_nativeObj, long descriptors_mat_nativeObj, double winStride_width, double winStride_height, double padding_width, double padding_height, long locations_mat_nativeObj);
    private static native void compute_1(long nativeObj, long img_nativeObj, long descriptors_mat_nativeObj, double winStride_width, double winStride_height, double padding_width, double padding_height);
    private static native void compute_2(long nativeObj, long img_nativeObj, long descriptors_mat_nativeObj, double winStride_width, double winStride_height);
    private static native void compute_3(long nativeObj, long img_nativeObj, long descriptors_mat_nativeObj);

    // C++:  void cv::HOGDescriptor::computeGradient(Mat img, Mat& grad, Mat& angleOfs, Size paddingTL = Size(), Size paddingBR = Size())
    private static native void computeGradient_0(long nativeObj, long img_nativeObj, long grad_nativeObj, long angleOfs_nativeObj, double paddingTL_width, double paddingTL_height, double paddingBR_width, double paddingBR_height);
    private static native void computeGradient_1(long nativeObj, long img_nativeObj, long grad_nativeObj, long angleOfs_nativeObj, double paddingTL_width, double paddingTL_height);
    private static native void computeGradient_2(long nativeObj, long img_nativeObj, long grad_nativeObj, long angleOfs_nativeObj);

    // C++:  void cv::HOGDescriptor::detect(Mat img, vector_Point& foundLocations, vector_double& weights, double hitThreshold = 0, Size winStride = Size(), Size padding = Size(), vector_Point searchLocations = std::vector<Point>())
    private static native void detect_0(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long weights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height, long searchLocations_mat_nativeObj);
    private static native void detect_1(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long weights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height);
    private static native void detect_2(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long weights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height);
    private static native void detect_3(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long weights_mat_nativeObj, double hitThreshold);
    private static native void detect_4(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long weights_mat_nativeObj);

    // C++:  void cv::HOGDescriptor::detectMultiScale(Mat img, vector_Rect& foundLocations, vector_double& foundWeights, double hitThreshold = 0, Size winStride = Size(), Size padding = Size(), double scale = 1.05, double finalThreshold = 2.0, bool useMeanshiftGrouping = false)
    private static native void detectMultiScale_0(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height, double scale, double finalThreshold, boolean useMeanshiftGrouping);
    private static native void detectMultiScale_1(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height, double scale, double finalThreshold);
    private static native void detectMultiScale_2(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height, double scale);
    private static native void detectMultiScale_3(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height, double padding_width, double padding_height);
    private static native void detectMultiScale_4(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold, double winStride_width, double winStride_height);
    private static native void detectMultiScale_5(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj, double hitThreshold);
    private static native void detectMultiScale_6(long nativeObj, long img_nativeObj, long foundLocations_mat_nativeObj, long foundWeights_mat_nativeObj);

    // C++:  void cv::HOGDescriptor::save(String filename, String objname = String())
    private static native void save_0(long nativeObj, String filename, String objname);
    private static native void save_1(long nativeObj, String filename);

    // C++:  void cv::HOGDescriptor::setSVMDetector(Mat svmdetector)
    private static native void setSVMDetector_0(long nativeObj, long svmdetector_nativeObj);

    // C++: Size HOGDescriptor::winSize
    private static native double[] get_winSize_0(long nativeObj);

    // C++: Size HOGDescriptor::blockSize
    private static native double[] get_blockSize_0(long nativeObj);

    // C++: Size HOGDescriptor::blockStride
    private static native double[] get_blockStride_0(long nativeObj);

    // C++: Size HOGDescriptor::cellSize
    private static native double[] get_cellSize_0(long nativeObj);

    // C++: int HOGDescriptor::nbins
    private static native int get_nbins_0(long nativeObj);

    // C++: int HOGDescriptor::derivAperture
    private static native int get_derivAperture_0(long nativeObj);

    // C++: double HOGDescriptor::winSigma
    private static native double get_winSigma_0(long nativeObj);

    // C++: HOGDescriptor_HistogramNormType HOGDescriptor::histogramNormType
    private static native int get_histogramNormType_0(long nativeObj);

    // C++: double HOGDescriptor::L2HysThreshold
    private static native double get_L2HysThreshold_0(long nativeObj);

    // C++: bool HOGDescriptor::gammaCorrection
    private static native boolean get_gammaCorrection_0(long nativeObj);

    // C++: vector_float HOGDescriptor::svmDetector
    private static native long get_svmDetector_0(long nativeObj);

    // C++: int HOGDescriptor::nlevels
    private static native int get_nlevels_0(long nativeObj);

    // C++: bool HOGDescriptor::signedGradient
    private static native boolean get_signedGradient_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
