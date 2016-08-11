package com.zero.photopicklib.mvp;

import android.content.Context;

import com.zero.photopicklib.entity.PhotoDir;

import java.util.List;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public interface PickerContract {
    interface View extends BaseView<PickerPresenter> {
        void getPhotoSuc(List<PhotoDir> photoDirs);
    }

    interface Presenter extends BasePresenter {
        void getPhotos(Context context, boolean checkImage, boolean showGif, boolean showAll);
    }
}
