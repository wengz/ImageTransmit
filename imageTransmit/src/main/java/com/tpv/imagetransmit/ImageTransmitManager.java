package com.tpv.imagetransmit;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.util.Log;

import com.tpv.imagetransmit.andserver.AndServerManager;
import com.tpv.imagetransmit.okhttp.OKHttpUtil;
import com.tpv.imagetransmit.util.ImageSendListener;
import com.tpv.imagetransmit.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageTransmitManager {

    private static ImageTransmitManager sInstance;
    private static Context sContext;

    private AndServerManager mAndServerManager;
    private ImageRecListener mImageRecListener;

    private volatile boolean mIsExecutingTask;
    private Queue<ImageTransTask> mTasks;
    private TaskExecutor mTaskExecutor;

    private ImageTransmitManager (){
        mAndServerManager = new AndServerManager(sContext);
        mTasks = new LinkedList<>();
        mIsExecutingTask = false;
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static ImageTransmitManager getInstance(){
        if (sInstance == null) {
            synchronized (ImageTransmitManager.class){
                if (sInstance == null) {
                    sInstance = new ImageTransmitManager();
                }
            }
        }
        return sInstance;
    }

    public void putTransTask(ImageTransTask task) {
        synchronized (mTasks){
            mTasks.add(task);
            if (!mIsExecutingTask) {
                mTaskExecutor = new TaskExecutor();
                new Thread(mTaskExecutor).start();
                mIsExecutingTask = true;
            }
        }
    }

    private class TaskExecutor  implements Runnable{

        @Override
        public void run() {
            while (true) {
                ImageTransTask taskToExecute;
                synchronized (mTasks){
                    taskToExecute = mTasks.poll();
                    if (taskToExecute == null) {
                        mIsExecutingTask = false;
                        break;
                    }
                }
                executeTask(taskToExecute);
            }
        }
    }

    private void executeTask(ImageTransTask task) {
        String url = Utils.buildUploadURL(task.ip);
        OKHttpUtil.upLoadFile(url, task.files,
                new Callback() {

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (task.callback != null) {
                            task.callback.onSuccess();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (task.callback != null) {
                            task.callback.onFail();
                        }
                    }
                });
    }

    public void startRecServer() {
        mAndServerManager.startServer();
    }

    public void stopRecServer() {
        mAndServerManager.stopServer();
    }

    public void setImageRecListener(ImageRecListener imageRecListener) {
        mImageRecListener = imageRecListener;
    }

    public ImageRecListener getImageRecListener() {
        return mImageRecListener;
    }

}
