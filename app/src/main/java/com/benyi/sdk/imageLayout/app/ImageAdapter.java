package com.benyi.sdk.imageLayout.app;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by MuFe on 2018/6/6.
 */

public class ImageAdapter extends BaseAdapter{
    private List<String> list;
    public ImageAdapter(List<String> list) {
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(100,100));
        Glide.with(imageView.getContext())
                .load(list.get(position))
                .into(imageView);
        return imageView;
    }
}
