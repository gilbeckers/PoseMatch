package com.gil.posematch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.util.ArrayList;

public class UploadImageActivity extends AppCompatActivity {

    private TextView txtStatus;
    private ImageView imageView;
    private ServerInterface restClient;
    private Bitmap image;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(MainActivity.TAG, "OpenCV not loaded");
        } else {
            Log.d(MainActivity.TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtStatus.setText("Start upload...");

        // Get the Intent that started this activity and extract the img (File)
        File pictureFile = (File) getIntent().getExtras().get(MainActivity.EXTRA_IMAGE);
        int modelId = (int) getIntent().getExtras().get(MainActivity.EXTRA_MODEL_POSE);

        imageView = (ImageView) findViewById(R.id.imageView);
        image = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
        imageView.setImageBitmap(image);

        restClient = new ServerInterface(this);
        //Log.d(MainActivity.TAG, "Start upload to server");
        //restClient.uploadImg(pictureFile);
        Log.d(MainActivity.TAG, "Start findmatch to server");
        restClient.findMatch(pictureFile, modelId);

    }

    public void signalImgUploadProgress(long bytesWritten, long totalSize){
        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtStatus.setText("Uploading ... " + bytesWritten/totalSize + "% complete");

    }


    // is called by ServerInteface when async HTTP POST is finished
    public void signalImgUploadReady(boolean result){
        if(result) {
            txtStatus.setText("Upload finished , match: " + restClient.isMatchOrNot());

            ArrayList<Keypoint> keypoints = restClient.getKeypointListPerson1();
            Bitmap newImg = PlotKeyPoints.drawKeypoints(image, keypoints);
            imageView.setImageBitmap(newImg);
        }
    }


}
