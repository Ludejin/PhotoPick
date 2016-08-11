package com.zero.photopicklib.data;

import android.content.Context;

import com.zero.photopicklib.entity.PhotoDir;
import com.zero.photopicklib.util.PhotoUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public class PhotoRepository implements PhotoDataSource {
    @Override
    public Observable<List<PhotoDir>> getPhotos(final Context context, final boolean checkImage,
                                                final boolean showGif, final boolean showAll) {
        return Observable.create(new Observable.OnSubscribe<List<PhotoDir>>() {
            @Override
            public void call(Subscriber<? super List<PhotoDir>> subscriber) {
                List<PhotoDir> photos = PhotoUtil.getPhotos(context, checkImage, showGif, showAll);
                subscriber.onNext(photos);
                subscriber.onCompleted();
            }
        });
    }
}
