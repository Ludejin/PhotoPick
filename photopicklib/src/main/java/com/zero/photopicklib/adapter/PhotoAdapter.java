package com.zero.photopicklib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.event.OnPhotoClickListener;
import com.zero.photopicklib.viewholder.PhotoViewHolder;
import com.zero.photopicklib.widget.ThumbPhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin_ on 2016/6/21
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;

    private Context mContext;
    private Toolbar mToolbar;
    private TextView mTitle;
    private List<Photo> mPhotos = new ArrayList<>();
    private ArrayList<String> mSelImages = new ArrayList<>();
    private int maxCount;
    private int spanCount;
    private boolean isCamera;

    private int mCurrDirIndex = 0;

    private int imageSize;

    public PhotoAdapter(Context context, List<Photo> photos, ArrayList<String> selImages,
                        int maxCount, int spanCount, boolean isCamera, Toolbar toolbar, TextView title) {
        mContext = context;
        mPhotos = photos;
        mSelImages = selImages;
        mToolbar = toolbar;
        mTitle = title;

        this.spanCount = spanCount;
        this.maxCount = maxCount;
        this.isCamera = isCamera;

        setImageSize(context, this.spanCount);
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && 0 == position) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    public void addData(List<Photo> photos) {
        this.mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        this.mPhotos.clear();
        notifyDataSetChanged();
    }

    private void setImageSize(Context context, int columnNumber) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new ThumbPhotoView(mContext), mContext, mTitle);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        if (ITEM_TYPE_PHOTO == getItemViewType(position)) {
            int photoIndex;
            if (showCamera()) {
                photoIndex = position - 1;
            } else {
                photoIndex = position;
            }
            Photo mPhoto = mPhotos.get(photoIndex);
            holder.setData(mSelImages, mPhoto, maxCount, imageSize, photoIndex);

            if (null != mOnPhotoClickListener) {
                holder.setPhotoClickListener(mOnPhotoClickListener);
            }
        } else {
            holder.setCamera();
            if (null != onCameraClickListener) {
                holder.setOnCameraClickListener(onCameraClickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        int mCount = mPhotos.size();
        return showCamera() ? mCount + 1 : mCount;
    }

    public ArrayList<String> getSelectedImages() {
        return mSelImages;
    }

    public boolean showCamera() {
        return (isCamera && 0 == mCurrDirIndex);
    }

    public void setCurrDirIndex(int currDirIndex) {
        mCurrDirIndex = currDirIndex;
    }

    private View.OnClickListener onCameraClickListener = null;
    private OnPhotoClickListener mOnPhotoClickListener = null;

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        mOnPhotoClickListener = onPhotoClickListener;
    }
}
