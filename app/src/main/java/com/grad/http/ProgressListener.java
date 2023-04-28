package com.grad.http;

public interface ProgressListener {
    void onProgress(long bytesWritten, long contentLength, boolean done);
}