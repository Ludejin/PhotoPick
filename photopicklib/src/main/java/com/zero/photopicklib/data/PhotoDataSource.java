package com.zero.photopicklib.data;

import android.content.Context;

import com.zero.photopicklib.entity.PhotoDir;

import java.util.List;

import rx.Observable;

/**
 * Created by Jin_ on 2016/6/21
 */
public interface PhotoDataSource {
    Observable<List<PhotoDir>> getPhotos(Context context, boolean checkImage, boolean showGif, boolean showAll);
}
