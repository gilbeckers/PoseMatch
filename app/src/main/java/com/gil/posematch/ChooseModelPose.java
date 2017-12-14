package com.gil.posematch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseModelPose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model_pose);
    }


    // onClick()-handler voor imageButton
    public void chooseModel(View view){
        int id = view.getId();

        Intent intent = new Intent();

        switch (id){
            case R.id.btn_pose_model1:
                intent.putExtra("pose_model", 1);
                break;
            case R.id.btn_pose_model2:
                intent.putExtra("pose_model", 2);
                break;
            case R.id.btn_pose_model3:
                intent.putExtra("pose_model", 99);
                break;
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
