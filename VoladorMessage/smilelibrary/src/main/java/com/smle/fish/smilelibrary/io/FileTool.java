package com.smle.fish.smilelibrary.io;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @PACKAGE_NAME：mvpdemo.smile.com.mvpdemo.controller
 * @user：yj
 * @date：2020/3/20
 * @版本：
 * @功能描述： 文件工具类
 */
public class FileTool {

    /**
     * 返回时间戳  一般用于不严格的命名
     *
     * @return
     */
    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    /**
     * 1
     * 创建私有文件
     *
     * @param context
     * @param fileType    (根据文件 类型创建固定文件夹 EFolder为已定义的类型)
     * @param fileName    文件名
     * @param postFixName 后缀名
     * @return
     * @throws IOException
     */
    public static File createPrivateFile(Context context, EFolder fileType, String fileName, String postFixName) throws IOException {
        //此路径需要和 files-path 路径一致
        File interiorFile = new File(context.getFilesDir().getPath() + File.separator + fileType.getValue());
        if (!interiorFile.exists()) {
            interiorFile.mkdirs();
        }
        interiorFile = new File(interiorFile, fileName + postFixName);
        if (!interiorFile.exists()) {
            interiorFile.createNewFile();
        }
        return interiorFile;
    }

    /**
     * 2
     * 创建私有文件 文件名由当前时间戳 {@link FileTool getTmeStamp()}决定
     *
     * @param context
     * @param fileType    (根据文件 类型创建固定文件夹 EFolder为已定义的类型)
     * @param postFixName 后缀名
     * @return
     * @throws IOException
     */
    public static File createPrivateFile(Context context, EFolder fileType, String postFixName) throws IOException {
        return createPrivateFile(context, fileType, getTimeStamp(), postFixName);
    }


    /**
     * 3
     * 创建私有 缓存文件
     * 由于缓存文件 随时都可能被清除，这里就不区分类型文件文件夹，统一存入根目录
     *
     * @param fileName    文件名
     * @param postfixName 后缀名
     */
    public static File createPrivateCacheFile(Context context, String fileName, String postfixName) throws IOException {
        //此路径需要和 cache-path 路径一致
        File file = context.getCacheDir();
        file = File.createTempFile(fileName, postfixName, file);
        return file;
    }

    /**
     * 4
     * 创建私有 缓存文件 文件名由当前时间戳 {@link FileTool getTmeStamp()}决定
     *
     * @param context
     * @param postfixName 后缀名
     * @return 返回创建成果后的文件 file
     * @throws IOException
     */
    public static File createPrivateCacheFile(Context context, String postfixName) throws IOException {
        return createPrivateCacheFile(context, getTimeStamp(), postfixName);
    }


    /**
     * 5
     * 在公开目录创建文件
     *
     * @param context
     * @param fileType    文件类型  （此类型参照  Environment 如 DIRECTORY_PICTURES） 根据文件类型创建目录
     * @param fileName    文件名
     * @param postfixName 后缀名
     * @return 返回创建成功后的文件 file
     * @throws IOException
     */
    public static File createPublicFile(Context context, EFolder fileType, String fileName, String postfixName) throws IOException {
        //应用外部存储目录 需要和  external-files-path   保持一致
        File storageDir = context.getExternalFilesDir(fileType.getValue());
        File file = File.createTempFile(fileName, postfixName, storageDir);
        return file;
    }


    /**
     * 6
     * 在公开目录创建文件
     *
     * @param context
     * @param fileType    文件类型
     * @param postfixName 文件后缀名
     * @return 返回文件创建成果后的 file
     * @throws IOException
     */
    public static File createPublicFile(Context context, EFolder fileType, String postfixName) throws IOException {
        return createPublicFile(context, fileType, getTimeStamp(), postfixName);
    }

    /**
     * 7
     * 在公开目录创建缓存文件
     *
     * @param context
     * @param fileName    文件名
     * @param postfixName 后缀名
     * @return 返回创建成功后的文件 file
     * @throws IOException
     */
    public static File createPublicCacheFile(Context context, String fileName, String postfixName) throws IOException {
        //应用外部存储目录 需要和  external-cache-path   保持一致
        File storageDir = context.getExternalCacheDir();
        File file = File.createTempFile(fileName, postfixName, storageDir);
        return file;
    }

    /**
     * 8
     * 在公开目录创建缓存文件  以系统时间为文件名
     *
     * @param context
     * @param postfixName 后缀名
     * @return 返回创建成功后的文件 file
     * @throws IOException
     */
    public static File createPublicCacheFile(Context context, String postfixName) throws IOException {
        return createPublicCacheFile(context, getTimeStamp(), postfixName);
    }


    /**
     * 在私有目录创建 根据系统时间命名的 jpg文件
     *
     * @param context
     * @return
     * @throws IOException
     */
    public static File createPrivateImageFile(Context context) throws IOException {
        File imageFile = createPrivateFile(context, EFolder.DIRECTORY_PICTURES, getTimeStamp(), ".jpg");
        return imageFile;
    }

    /**
     *
     * @param context
     * @param uri
     * @return
     * @throws FileNotFoundException
     */
    public static FileDescriptor getFileDescriptor(Context context, Uri uri) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.setRequireOriginal(uri);
        }
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        return fileDescriptor;
    }

    /**
     * 根据 uri 获取流
     * @param context
     * @param uri
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileInputStream(Context context, Uri uri) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.setRequireOriginal(uri);
        }
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return inputStream;
    }

    /**
     * 根据不同系统版本 读取图片旋转角度
     *
     * @param context
     * @param uri
     * @return
     */
    public static int readPictureDegree(Context context, Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.setRequireOriginal(uri);
        }
        int degree = 0;

        ExifInterface exifInterface = null;
        InputStream stream = context.getContentResolver().openInputStream(uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exifInterface = new ExifInterface(stream);
        } else {
            String filePath = getRealFilePath(context, uri);
            exifInterface = new ExifInterface(filePath);
        }
        assert exifInterface != null;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        return degree;
    }

    /**
     * 根据不同版本  将uri 转换成实际路径
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}
