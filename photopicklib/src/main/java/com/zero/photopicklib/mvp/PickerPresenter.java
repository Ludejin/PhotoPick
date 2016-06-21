package com.zero.photopicklib.mvp;

import android.content.Context;

import com.zero.photopicklib.data.PhotoRepository;
import com.zero.photopicklib.entity.PhotoDir;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public class PickerPresenter implements PickerContract.Presenter {

    private CompositeSubscription mSubscriptions;
    private PhotoRepository mPhotoRepository;
    private PickerContract.View mView;
    private Context mContext;
    private boolean checkImg;
    private boolean showGif;

    public PickerPresenter(PhotoRepository photoRepository, PickerContract.View view,
                           Context context, boolean checkImg, boolean showGif) {
        mPhotoRepository = photoRepository;
        mView = view;
        mContext = context;
        this.checkImg = checkImg;
        this.showGif = showGif;

        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void getPhotos(Context context, boolean checkImage, boolean showGif) {
        mSubscriptions.clear();
        Subscription subscription = mPhotoRepository.getPhotos(context, checkImage, showGif)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PhotoDir>>() {
                    @Override
                    public void call(List<PhotoDir> photoDirs) {
                        mView.getPhotoSuc(photoDirs);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        getPhotos(mContext, checkImg, showGif);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
