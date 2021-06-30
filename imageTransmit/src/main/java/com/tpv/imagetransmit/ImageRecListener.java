package com.tpv.imagetransmit;

import java.io.File;
import java.util.List;

public interface ImageRecListener {

    void onImageReceive(List<File> images);

    void onImageTransFail(Throwable e);

    void onImageRecStart();
}
