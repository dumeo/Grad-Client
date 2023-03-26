package com.grad.information.addpost;

import android.graphics.Bitmap;

public class ImageInfo {
    private Bitmap bitmap;
    private String fileName;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageInfo(Bitmap bitmap, String fileName) {
        this.bitmap = bitmap;
        this.fileName = fileName;
    }
}
