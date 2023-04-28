package com.grad.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static byte[] getBytesFromUri(Uri uri, Context context){
        try{

            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            FileInputStream fis = new FileInputStream(fileDescriptor);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            fis.close();
            byte[] res = bos.toByteArray();
            bos.close();
            return res;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //第二中方法，将输入流写入临时的文件中,不推荐，因为临时文件会持久化
    public static byte[] getBytesFromUri2(Uri uri, Context context){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("temp", null, context.getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            return  FileUtils.readFileToByteArray(tempFile);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getRealPathFromUri(Context context , Uri uri) {
        Log.e("wjj", "uri = " + uri.toString());

        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            Log.e("wjj", "is ExternalStorageProvider");
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        }

        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {
            Log.e("wjj", "is DownloadsProvider");
            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            return getFilePath(context, contentUri, null, null);
        }

        // MediaProvider
        else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
            Log.e("wjj", "is MediaProvider");
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }else {
                contentUri = MediaStore.Files.getContentUri("external");
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {split[1]};

            return getFilePath(context, contentUri, selection, selectionArgs);
        }

        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e("wjj", "is Media Store");
            return getFilePath(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("wjj", "is Filed");
            return uri.getPath();
        }
        return null;
    }

    public static String getFilePath(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;

        String[] targetColumns = {MediaStore.MediaColumns.DATA};

        try {
            //select targetColumns from $uri where $selection = $selectionArgs

            cursor = context.getContentResolver().query(uri, targetColumns, selection, selectionArgs, null);

            if (cursor != null) {
                cursor.moveToFirst();
                return cursor.getString(0);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
