package com.zero.photopicklib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zero.photopicklib.R;
import com.zero.photopicklib.entity.PhotoDir;
import com.zero.photopicklib.viewholder.DirViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin_ on 2016/6/22
 * 邮箱：dejin_lu@creawor.com
 */
public class PopupDirAdapter extends BaseAdapter {

    private List<PhotoDir> mPhotoDirs = new ArrayList<>();
    private Context mContext;

    public PopupDirAdapter(List<PhotoDir> photoDirs, Context context) {
        mPhotoDirs = photoDirs;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPhotoDirs.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoDirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPhotoDirs.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DirViewHolder holder;
        if (null == convertView) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.item_dir, parent, false);
            holder = new DirViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DirViewHolder) convertView.getTag();
        }

        holder.bindData(mContext, mPhotoDirs.get(position));
        return convertView;
    }

    public void setSelPos(int selPos) {
        for (PhotoDir mDir : mPhotoDirs) {
            mDir.setSel(false);
        }
        mPhotoDirs.get(selPos).setSel(true);
        notifyDataSetChanged();
    }
}
