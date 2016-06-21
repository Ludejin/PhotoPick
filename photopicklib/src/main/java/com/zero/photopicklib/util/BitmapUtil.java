package com.zero.photopicklib.util;

import android.graphics.BitmapFactory;

/**
 * see https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/util/BitmapUtil.java
 */
public class BitmapUtil {
    /**
     * 检查文件是否损坏
     * Check if the file is corrupted
     * @param filePath
     * @return
     */
    public static boolean checkImgCorrupted(String filePath) {
        BitmapFactory.Options options = null;
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);
        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            return true;
        }
        return false;
    }
}
