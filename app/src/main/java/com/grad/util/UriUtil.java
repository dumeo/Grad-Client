package com.grad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

public class UriUtil {
    @SuppressLint("Range")
    public static String getFileNameFromUri(Uri imageUri, Context context) {
        String result = null;
        if (imageUri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(imageUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = imageUri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
