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
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames 完整的文件路径
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "images",  //请求的名字
                    file.getName(), //文件的名字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }

    /**
     * 获取文件MimeType
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }
}
