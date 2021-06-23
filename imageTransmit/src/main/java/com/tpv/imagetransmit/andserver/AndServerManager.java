package com.tpv.imagetransmit.andserver;

import android.content.Context;
import android.util.Log;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.util.concurrent.TimeUnit;

public class AndServerManager {

    public static final String TAG = "AndServerManager";

    private Server mServer;

    public AndServerManager(Context context) {
        mServer = AndServer.webServer(context)
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        Log.d(TAG, "The server started successfully");
                    }

                    @Override
                    public void onStopped() {
                        Log.d(TAG, "The server has stopped");
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.d(TAG, "An exception occurred while the server was starting");
                    }
                })
                .build();
    }

    public void startServer() {
        if (mServer.isRunning()) {
        } else {
            mServer.startup();
        }
    }

    public void stopServer() {
        if (mServer.isRunning()) {
            mServer.shutdown();
        } else {
            Log.w(TAG, "The server has not started yet.");
        }
    }
}
