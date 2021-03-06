package com.tpv.imagetransmit.okhttp;

import android.util.Log;

import com.tpv.imagetransmit.ImageSendListener;
import com.yanzhenjie.andserver.http.StatusCode;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import static com.tpv.imagetransmit.util.Utils.*;

public class OKHttpUtil {

    public static boolean startUpload(String url, ImageSendListener callback) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).post(RequestBody.create(null, "")).build();
            Call call = okHttpClient.newCall(request) ;
            Response response = call.execute();
            if (response.code() == StatusCode.SC_OK) {
                return true;
            } else {
                if (callback != null) {
                    callback.onFail();
                }
                return false;
            }
        }catch (Exception e){
            Log.d(TAG, "exception >>> OKHttpUtil#startUpload: "+e.getMessage());

            if (callback != null) {
                callback.onFail();
            }
            return false;
        }
    }

    public static void upLoadFile(String url, List<String> fileNames, ImageSendListener callback){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(getRequest(url,fileNames)) ;
            Response response = call.execute();
            if (response.code() == StatusCode.SC_OK) {
                if (callback != null) {
                    callback.onSuccess();
                }
            } else {
                if (callback != null) {
                    callback.onFail();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "exception >>> OKHttpUtil#upLoadFile: "+e.getMessage());
            if (callback != null) {
                callback.onFail();
            }
        }
    }

    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }

    /**
     * ??????????????????????????????????????????RequestBody
     * @param fileNames ?????????????????????
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //??????MultipartBody.Builder??????????????????????????????
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //?????????????????????
            File file = new File(fileNames.get(i)); //????????????
            //?????????????????????????????????????????????
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //???Builder?????????????????????
                    "images",  //???????????????
                    file.getName(), //?????????????????????????????????????????????
                    RequestBody.create(MediaType.parse(fileType), file) //??????RequestBody???????????????????????????
            );
        }
        return builder.build(); //??????Builder????????????
    }

    /**
     * ????????????MimeType
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,????????????????????????
        }
        return contentType;
    }
}
