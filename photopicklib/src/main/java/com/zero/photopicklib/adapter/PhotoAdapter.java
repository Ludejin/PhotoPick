package com.zero.photopicklib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

    private Context mContext;
    private List<Photo> mPhotos = new ArrayList<>();
    private ArrayList<String> mSelImages = new ArrayList<>();
    private int maxCount;

    private int imageSize;

    public PhotoAdapter(Context context, List<Photo> photos, int maxCount) {
        mContext = context;
        mPhotos = photos;
        this.maxCount = maxCount;
        setImageSize(context, 3);
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
        holder.setData(mSelImages, mPhotos.get(position), maxCount, imageSize);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public ArrayList<String> getSelectedImages(){
        return mSelImages;
    }
}
