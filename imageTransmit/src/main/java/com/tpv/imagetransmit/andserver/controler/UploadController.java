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

@RestController
public class UploadController {

    public static final String TAG = "UploadController";

    @PostMapping(path = "/upload")
    void upload(@RequestParam(name = "images") MultipartFile[] images) throws IOException {
        List<File> recFiles = new ArrayList<>();
        if (images != null) {
            for (MultipartFile multipartFile : images) {
                File fileSaveTo = Utils.createNewReceiveFile(multipartFile.getFilename());
                multipartFile.transferTo(fileSaveTo);
                recFiles.add(fileSaveTo);
            }
        }
        ImageRecListener imageRecListener = ImageTransmitManager.getInstance().getImageRecListener();
        if (imageRecListener != null) {
            imageRecListener.onImageReceive(recFiles);
        }
    }

    @PostMapping(path = "/start_upload")
    void startUpload() {
        ImageRecListener imageRecListener = ImageTransmitManager.getInstance().getImageRecListener();
        if (imageRecListener != null) {
            imageRecListener.onImageRecStart();
        }
    }
}
