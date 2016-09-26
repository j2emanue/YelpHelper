package com.example.j2emanue.myyelphelperapp.Fragments;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.j2emanue.myyelphelperapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by j2emanue on 9/25/16.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> urls;

    public CustomPagerAdapter(Context context, List<String> urls) {
        this.urls = urls;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pageviewer_details, collection, false);

        ImageView imageView = (ImageView) layout.findViewById(R.id.iv_review_photos);

        Picasso.with(mContext)
                .load(urls.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .error(android.R.drawable.stat_notify_error)
                .fit()
                .centerCrop()
                .into(imageView);

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(R.string.customer_photo);
    }

}
