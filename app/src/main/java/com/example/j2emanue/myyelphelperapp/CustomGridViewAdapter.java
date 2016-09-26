package com.example.j2emanue.myyelphelperapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j2emanue.myyelphelperapp.Constants.ConstantsFragments;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.Review;
import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2emanue on 9/24/16.
 */

public class CustomGridViewAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private  List<Business> businesses;
    private RatingsFilter mRatingsfilter;
    LayoutInflater inflater;

    public CustomGridViewAdapter(Context c, List<Business> businesses) {
        mContext = c;
        this.businesses = businesses;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return businesses.size();
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View grid;


        if (convertView == null) {
            grid = inflater.inflate(R.layout.gridview_custom_layout, null);
        } else {
            grid = (View) convertView;

        }

        String url = businesses.get(pos).getImageUrl();
        Review review = businesses.get(pos).getReviews().get(0);

        TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
        textView.setText(review.getText());

        ImageView imageView = (ImageView) grid.findViewById(R.id.gridview_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTransitionToDetailFragmentEvent(view);
            }
        });

        imageView.setTag(pos);

        if (!TextUtils.isEmpty(url))
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(imageView);

        return grid;
    }

    private void postTransitionToDetailFragmentEvent(View view) {
        ImageView iv = (ImageView) view;
        iv.setTransitionName(ConstantsFragments.SHARED_ELEMENT_TRANSITION_NAME);
        SquareUtils.getBusInstance().post(new DetailsFragmentEvent(view, businesses));

    }

    @Override
    public Filter getFilter() {
        if (mRatingsfilter == null) {
            mRatingsfilter = new RatingsFilter();
        }
        return mRatingsfilter;

    }

    public class RatingsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                double desiredRating = Integer.parseInt(constraint.toString());

                List<Business> filterList = new ArrayList<Business>();
                for (int i = 0; i < businesses.size(); i++) {
                    if (businesses.get(i).getRating()>=desiredRating){

                        filterList.add(businesses.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = businesses.size();
                results.values = businesses;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            businesses = (ArrayList<Business>) results.values;
            notifyDataSetChanged();
        }
    }

}