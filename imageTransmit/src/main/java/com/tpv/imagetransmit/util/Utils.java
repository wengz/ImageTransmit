package com.tpv.imagetransmit.util;

import android.os.Environment;

import java.io.File;

public class Utils {

    public static final String TAG = "ImageTransmit";

    public static final String FOLDER_NAME_IMAGE_TRANSMIT = "image_transmit";
    public static final String FOLDER_NAME_RECEIVE = "receive";

    public static File createNewReceiveFile(String rawRecFileName) {
        String newRecFileName = System.currentTimeMillis()+"_"+rawRecFileName;
        File newRecFile = new File(Environment.getExternalStorageDirectory()+"/"+FOLDER_NAME_IMAGE_TRANSMIT+"/"+FOLDER_NAME_RECEIVE, newRecFileName);
        return newRecFile;
    }

}
