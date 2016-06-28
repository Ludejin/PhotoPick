package com.zero.photopicklib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zero.photopicklib.entity.Photo;
import com.zero.photopicklib.viewholder.PhotoViewHolder;
import com.zero.photopicklib.widget.ThumbPhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin_ on 2016/6/21
 * 邮箱：dejin_lu@creawor.com
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO  = 101;

    private Context mContext;
    private List<Photo> mPhotos = new ArrayList<>();
    private ArrayList<String> mSelImages = new ArrayList<>();
    private int maxCount;
    private boolean isCamera;

    private int mCurrDirIndex = 0;

    private int imageSize;

    public PhotoAdapter(Context context, List<Photo> photos, int maxCount, boolean isCamera) {
        mContext = context;
        mPhotos = photos;

        this.maxCount = maxCount;
        this.isCamera = isCamera;
        setImageSize(context, 3);
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && 0 == position) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    public void addData(List<Photo> photos){
        this.mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
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
        return new PhotoViewHolder(new ThumbPhotoView(mContext));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        if (ITEM_TYPE_PHOTO == getItemViewType(position)) {
            Photo mPhoto;
            if (showCamera()) {
                mPhoto = mPhotos.get(position - 1);
            } else {
                mPhoto = mPhotos.get(position);
            }
            holder.setData(mSelImages, mPhoto, maxCount, imageSize);
        } else {
            if (null != onCameraClickListener) {
                holder.setOnCameraClickListener(onCameraClickListener);
                holder.setCamera();
            }
        }
    }

    @Override
    public int getItemCount() {
        int mCount = mPhotos.size();
        return showCamera() ? mCount + 1 : mCount;
    }

    public ArrayList<String> getSelectedImages(){
        return mSelImages;
    }

    public boolean showCamera() {
        return (isCamera && 0 == mCurrDirIndex);
    }

    public void setCurrDirIndex(int currDirIndex) {
        mCurrDirIndex = currDirIndex;
    }

    private View.OnClickListener onCameraClickListener = null;

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }
}
