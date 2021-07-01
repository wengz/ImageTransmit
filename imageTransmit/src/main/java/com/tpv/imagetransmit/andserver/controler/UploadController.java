package com.tpv.imagetransmit.andserver.controler;

import android.util.Log;

import com.tpv.imagetransmit.ImageRecListener;
import com.tpv.imagetransmit.ImageTransmitManager;
import com.tpv.imagetransmit.util.Utils;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tpv.imagetransmit.util.Utils.*;

@RestController
public class UploadController {


    @PostMapping(path = "/upload")
    void upload(@RequestParam(name = "images") MultipartFile[] images) throws Exception {
        Log.d(TAG, "UploadController#upload");

        ImageRecListener imageRecListener = ImageTransmitManager.getInstance().getImageRecListener();
        try {
            List<File> recFiles = new ArrayList<>();
            if (images != null) {
                for (MultipartFile multipartFile : images) {
                    File fileSaveTo = Utils.createNewReceiveFile(multipartFile.getFilename());
                    multipartFile.transferTo(fileSaveTo);
                    recFiles.add(fileSaveTo);
                }
            }
            if (imageRecListener != null) {
                try {
                    imageRecListener.onImageReceive(recFiles);
                } catch (Exception e) {
                    Log.e(TAG, "exception >>> ImageRecListener#onImageReceive " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "exception >>> UploadController#upload"+e.getMessage());

            if (imageRecListener != null) {
                try {
                    imageRecListener.onImageTransFail(e);
                } catch (Exception ex) {
                    Log.e(TAG, "exception >>> ImageRecListener#onImageTransFail " + e.getMessage());
                }
            }
            throw e;
        }
    }

    @PostMapping(path = "/start_upload")
    void startUpload() throws Exception {
        Log.d(TAG, "UploadController#startUpload");

        try {
            ImageRecListener imageRecListener = ImageTransmitManager.getInstance().getImageRecListener();
            if (imageRecListener != null) {
                imageRecListener.onImageRecStart();
            }
        } catch (Exception e) {
            Log.e(TAG, "exception >>> ImageRecListener#onImageRecStart " + e.getMessage());
            throw e;
        }
    }
}
