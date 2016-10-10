package com.example.j2emanue.myyelphelperapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j2emanue.myyelphelperapp.Base.YelpBaseFragment;
import com.example.j2emanue.myyelphelperapp.Constants.ConstantsFragments;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Model.Business.BusinessesModel;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.Review;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.User;
import com.example.j2emanue.myyelphelperapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by j2emanue on 9/25/16.
 */

public class DetailsFragment extends YelpBaseFragment {

    @BindView(R.id.ivProfile)
    ImageView profileImage;

    @BindView((R.id.tv_details))
    TextView tv_details;

    @BindView((R.id.viewpager))
    ViewPager viewPager;

    int mPosition;
    List<Business> mModel;
    String mUrl, mLocation, mPhone, mName;
    List<Review> mReviews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        mPosition = bundle.getInt(ConstantsFragments.POSITION);
        mModel = BusinessesModel.getInstance().getBusinesses();
        mUrl = mModel.get(mPosition).getImageUrl();
        mLocation = mModel.get(mPosition).getLocation().getCity();
        mPhone = mModel.get(mPosition).getPhone();
        mName = mModel.get(mPosition).getName();
        mReviews = mModel.get(mPosition).getReviews();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        ButterKnife.bind(this, view);

        if (!TextUtils.isEmpty(mUrl))
            Picasso.with(getActivity())
                    .load(mUrl)
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .centerCrop()
                    .into(profileImage);

        viewPager.setAdapter(new CustomPagerAdapter(getActivity(), parseUrlsFromReviews(mReviews)));

        StringBuilder sb = new StringBuilder(mName)
                .append(" ")
                .append(mLocation)
                .append("\nPhone: ")
                .append(mPhone);

        tv_details.setText(sb.toString());

        return view;


    }

    private List<String> parseUrlsFromReviews(List<Review> reviews) {

        List<String> urls = new ArrayList<String>();
        for (Review review : reviews) {
            User user = review.getUser();
            String url = (String) user.getImageUrl();
            if (url != null)
                urls.add(url);
        }
        return urls;
    }
}