package com.example.j2emanue.myyelphelperapp;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j2emanue.myyelphelperapp.Constants.ConstantsFragments;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2emanue on 9/26/16.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> implements Filterable {

    private RatingsFilter mRatingsfilter;

    private List<Business> businesses;

    public GridAdapter(List<Business> businesses) {

        this.businesses = businesses;
    }

    @Override
    public GridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_custom_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridAdapter.MyViewHolder holder, int position) {

        Business business = businesses.get(position);
        holder.review.setText(business.getReviews().get(0).getText());

        holder.iv.setTag(position);
        String url = businesses.get(position).getImageUrl();

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTransitionToDetailFragmentEvent(view);
            }
        });

        if (!TextUtils.isEmpty(url))
            Picasso.with(holder.iv.getContext())
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView review;
        public ImageView iv;


        public MyViewHolder(View view) {
            super(view);
            review = (TextView) view.findViewById(R.id.gridview_text);
            iv = (ImageView) view.findViewById(R.id.gridview_image);
        }
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
