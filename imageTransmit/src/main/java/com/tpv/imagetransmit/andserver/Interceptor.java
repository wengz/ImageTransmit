package com.tpv.imagetransmit.andserver;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tpv.imagetransmit.ImageRecListener;
import com.tpv.imagetransmit.ImageTransmitManager;
import com.yanzhenjie.andserver.framework.HandlerInterceptor;
import com.yanzhenjie.andserver.framework.handler.RequestHandler;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;

@com.yanzhenjie.andserver.annotation.Interceptor
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean onIntercept(@NonNull HttpRequest request, @NonNull HttpResponse response, @NonNull RequestHandler handler) throws Exception {
        return false;
    }

}
