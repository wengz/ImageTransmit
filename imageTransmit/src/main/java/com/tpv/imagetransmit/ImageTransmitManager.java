package com.tpv.imagetransmit;

import android.content.Context;

import com.tpv.imagetransmit.andserver.AndServerManager;
import com.tpv.imagetransmit.okhttp.OKHttpUtil;

import java.util.LinkedList;
import java.util.Queue;

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
        ImageSendListener callback = task.callback;
        boolean startUpRes = OKHttpUtil.startUpload(buildStartUploadUrl(task.ip), callback);
        if (!startUpRes) {
            return;
        }
        OKHttpUtil.upLoadFile(buildUploadURL(task.ip), task.files, callback);
    }

    private String buildStartUploadUrl(String ip) {
        return String.format("http://%s:8080/start_upload", ip);
    }

    private String buildUploadURL(String ip) {
        return String.format("http://%s:8080/upload", ip);
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
