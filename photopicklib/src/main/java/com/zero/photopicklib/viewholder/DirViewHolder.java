package com.zero.photopicklib.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zero.photopicklib.R;
import com.zero.photopicklib.entity.PhotoDir;

/**
 * Created by Jin_ on 2016/6/22
 * 邮箱：dejin_lu@creawor.com
 */
public class DirViewHolder {
    private ImageView ivCover;
    private TextView tvName;
    private TextView tvCount;
    private ImageView ivSel;

    public DirViewHolder(View rootView) {
        ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
        tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
        tvCount = (TextView) rootView.findViewById(R.id.tv_dir_count);
        ivSel = (ImageView) rootView.findViewById(R.id.iv_dir_sel);
    }

    public void bindData(Context context, PhotoDir photoDir) {
        Glide.with(context)
                .load(photoDir.getCoverPath())
                .dontAnimate()
                .thumbnail(0.1f)
                .into(ivCover);

        ivSel.setVisibility(View.INVISIBLE);

        if (photoDir.isSel()) {
            ivSel.setVisibility(View.VISIBLE);
        }

        tvName.setText(photoDir.getName());
        tvCount.setText(tvCount.getContext().getString(
                R.string.picker_image_count, photoDir.getPhotos().size()));
    }
}
