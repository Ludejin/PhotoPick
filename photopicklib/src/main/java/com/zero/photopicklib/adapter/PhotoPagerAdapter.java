package com.zero.photopicklib.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zero.photopicklib.R;
import com.zero.photopicklib.widget.PinchImageView;
import com.zero.photopicklib.widget.PinchImageViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Jin_ on 2016/6/30
 * 邮箱：dejin_lu@creawor.com
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private LinkedList<PinchImageView> viewCache = new LinkedList<>();
    private ArrayList<String> mPaths = new ArrayList<>();
    private PinchImageViewPager mViewPager;
    private Context mContext;

    private View.OnClickListener itemListener;

    public void setItemListener(View.OnClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public PhotoPagerAdapter(ArrayList<String> paths, PinchImageViewPager viewPager, Context context) {
        mPaths = paths;
        mViewPager = viewPager;
        mContext = context;
    }

    private void loadPhoto(String path, PinchImageView mPinchImageView) {
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }

        Glide.with(mContext)
                .load(uri)
                .dontAnimate()
                .dontTransform()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_broken_img)
                .into(mPinchImageView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PinchImageView mPinchImageView;

        if (viewCache.size() > 0) {
            mPinchImageView = viewCache.remove();
            mPinchImageView.reset();
        } else {
            mPinchImageView = new PinchImageView(mContext);
        }

        mPinchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemListener) {
                    itemListener.onClick(v);
                }
            }
        });

        final String path = mPaths.get(position);
        loadPhoto(path, mPinchImageView);

        container.addView(mPinchImageView);

        return mPinchImageView;
    }

    @Override
    public int getCount() {
        return mPaths.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PinchImageView pinchImageView = (PinchImageView) object;
        container.removeView(pinchImageView);
        viewCache.add(pinchImageView);
        Glide.clear(pinchImageView);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        PinchImageView piv = (PinchImageView) object;
        mViewPager.setMainPinchImageView(piv);
    }
}

