package com.grad.information.addpost;

import okhttp3.RequestBody;

public class ImageItem {

        public RequestBody file;
        public String filename;

        public ImageItem(RequestBody image, String filename) {
            this.file = image;
            this.filename = filename;
        }

}
