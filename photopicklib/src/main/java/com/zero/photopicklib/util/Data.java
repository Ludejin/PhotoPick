package com.zero.photopicklib.util;

import android.content.Context;
import android.database.Cursor;

import com.zero.photopicklib.R;
import com.zero.photopicklib.entity.PhotoDir;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;

/**
 * see
 * https://github.com/crosswall/Android-PickPhotos/blob/master/pickphotos/src/main/java/me/crosswall/photo/pick/data/Data.java
 */
public class Data {
    public final static int INDEX_ALL_PHOTOS = 0;
    public static List<PhotoDir> getDataFromCursor(Context context, Cursor data, boolean checkImageStatus){
        List<PhotoDir> directories = new ArrayList<>();
        PhotoDir photoDirectoryAll = new PhotoDir();
        photoDirectoryAll.setName(context.getString(R.string.picker_all_image));
        photoDirectoryAll.setId("ALL");


        while (data.moveToNext()) {

            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));

            if (checkImageStatus) {
                if (!BitmapUtil.checkImgCorrupted(path)) {
                    PhotoDir photoDirectory = new PhotoDir();
                    photoDirectory.setId(bucketId);
                    photoDirectory.setName(name);

                    if (!directories.contains(photoDirectory)) {
                        photoDirectory.setCoverPath(path);
                        photoDirectory.addPhoto(imageId, path);
                        photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                        directories.add(photoDirectory);
                    } else {
                        directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                    }

                    photoDirectoryAll.addPhoto(imageId, path);
                }
            } else {

                PhotoDir photoDirectory = new PhotoDir();
                photoDirectory.setId(bucketId);
                photoDirectory.setName(name);

                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(path);
                    photoDirectory.addPhoto(imageId, path);
                    photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                }

                photoDirectoryAll.addPhoto(imageId, path);
            }


        }
        if (photoDirectoryAll.getPhotoPaths().size() > 0) {
            photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
        }
        directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);

        return directories;
    }
}
