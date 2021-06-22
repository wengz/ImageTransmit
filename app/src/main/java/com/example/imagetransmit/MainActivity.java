package com.example.imagetransmit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tpv.imagetransmit.ImageRecListener;
import com.tpv.imagetransmit.ImageTransTask;
import com.tpv.imagetransmit.ImageTransmitManager;
import com.tpv.imagetransmit.andserver.AndServerManager;
import com.tpv.imagetransmit.okhttp.OKHttpUtil;
import com.tpv.imagetransmit.util.ImageSendListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String[] mLocalImgs = {
            "/sdcard/Screenshot/Screen20210621170506.jpg",
            "/sdcard/Screenshot/Screen20210621171204.jpg",
            "/sdcard/Screenshot/Screen20210622102905.jpg",
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openPerssion();

        ImageTransmitManager.init(this);
        ImageTransmitManager.getInstance().startRecServer();

        TextView tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTransTask imageTransTask = new ImageTransTask();
                imageTransTask.ip = "127.0.0.1";
                imageTransTask.files = new ArrayList<>();
                imageTransTask.files.add(mLocalImgs[0]);
                imageTransTask.files.add(mLocalImgs[1]);
                imageTransTask.callback = new ImageSendListener() {

                    @Override
                    public void onSuccess() {
                        Log.d("zzz", "onSuccess: imageTransTask_1 >>> success");
                    }

                    @Override
                    public void onFail() {
                        Log.d("zzz", "onSuccess: imageTransTask_1 >>> fail");
                    }
                };
                ImageTransmitManager.getInstance().putTransTask(imageTransTask);

                ImageTransTask imageTransTask2 = new ImageTransTask();
                imageTransTask2.ip = "127.0.0.1";
                imageTransTask2.files = new ArrayList<>();
                imageTransTask2.files.add(mLocalImgs[2]);
                imageTransTask2.callback = new ImageSendListener() {

                    @Override
                    public void onSuccess() {
                        Log.d("zzz", "onSuccess: imageTransTask_2 >>> success");
                    }

                    @Override
                    public void onFail() {
                        Log.d("zzz", "onSuccess: imageTransTask_2 >>> fail");
                    }
                };
                ImageTransmitManager.getInstance().putTransTask(imageTransTask2);

            }
        });

        ImageTransmitManager.getInstance().setImageRecListener(new ImageRecListener(){

            @Override
            public void onImageReceive(List<File> images) {
                for (File image :images) {
                    Log.d("zzz", "onImageReceive: >>> image="+image.getAbsolutePath());
                }
            }
        });

    }

    private void openPerssion() {
        verifyStoragePermissions(this);
    }

    /**
     * @des 权限组的获取
     * @author DELL
     * @time  10:44
     */
    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}