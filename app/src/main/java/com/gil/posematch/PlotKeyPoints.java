package com.gil.posematch;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by Gil on 26/11/2017.
 */

public class PlotKeyPoints {

    public static Bitmap drawKeypoints(Bitmap bitmap, ArrayList<Keypoint> keypoints){
        Mat src = new Mat(bitmap.getHeight(),bitmap.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap,src);

        for(Keypoint kp:keypoints){
            Log.d(MainActivity.TAG, "plotting : " + kp.getX() + "  " + kp.getY());
            drawCircle(src, kp.getX(), kp.getY());
        }

        Bitmap result = Bitmap.createBitmap(src.cols(),src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src,result);
        return result;
    }

    public static void drawCircle(Mat img, double x, double y ){
        Imgproc.circle(img, new Point(x,y), 30, new Scalar(255,0, 0, 255), 10);
    }

}
