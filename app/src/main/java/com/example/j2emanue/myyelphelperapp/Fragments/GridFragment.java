package com.example.j2emanue.myyelphelperapp.Fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.j2emanue.myyelphelperapp.Base.YelpBaseFragment;
import com.example.j2emanue.myyelphelperapp.Constants.Constants;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.Events.TokenRecievedEvent;
import com.example.j2emanue.myyelphelperapp.GridAdapter;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Model.Business.BusinessesModel;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.Reviews;
import com.example.j2emanue.myyelphelperapp.R;
import com.example.j2emanue.myyelphelperapp.RecyclerItemClickListener;
import com.example.j2emanue.myyelphelperapp.Services.ServiceGenerator;
import com.example.j2emanue.myyelphelperapp.Services.YelpRestaurantService;
import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by j2emanue on 9/24/16.
 */

public class GridFragment extends YelpBaseFragment {

    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    //@BindView(R.id.grid)
    //GridView mGridView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView((R.id.recycler_view))
    RecyclerView recyclerView;

    private GridAdapter mAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment, container, false);

        ButterKnife.bind(this, view);

        initFabButton();

        return view;
    }

    private void initFabButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GridAdapter) recyclerView.getAdapter()).getFilter().filter("4");
            }
        });
    }


    @Subscribe
    public void answerTokenRecieved(TokenRecievedEvent event) {

        if (!event.isSuccess()) {
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, R.string.token_failure, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {

            //todo: could use java proxy and pass in token transparently. can also cache token.
            //todo: also the activity could have been used to retrieve the businesses.

            final YelpRestaurantService service = ServiceGenerator.createService(YelpRestaurantService.class, event.getToken());

            //randomly using ethiopian restaurants at these coordinates.
            Call<BusinessesModel> call = service.getBusinesses("Ethiopian", "37.786882", "-122.399972");

            call.enqueue(new Callback<BusinessesModel>() {
                @Override
                public void onResponse(Call<BusinessesModel> call, Response<BusinessesModel> response) {
                    final List<Business> businessesList = response.body().getBusinesses();
                    final ArrayList<Business> businessesWithReviews = new ArrayList<Business>();

                    /*already got a stream of businesses. now lets make a second network call to get reviews.
                    for each review well place it into the businesses model using zipWith. update UI afterwards on mainthread
                    we take only 10 per specs*/

                    Observable.from(businessesList)
                            .flatMap(new Func1<Business, Observable<Reviews>>() {
                                @Override
                                public Observable<Reviews> call(Business business) {
                                    Observable<Reviews> review = service.getReviews(business.getId());
                                    return review;
                                }
                            }).take(Constants.MAX_ITEMS)
                            .zipWith(businessesList, new Func2<Reviews, Business, List<Business>>() {

                                @Override
                                public List<Business> call(Reviews reviews, Business business) {
                                    business.setReviews(reviews.getReviews());
                                    businessesWithReviews.add(business);
                                    return businessesWithReviews;
                                }
                            }).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<Business>>() {
                                @Override
                                public void onCompleted() {
                                    Log.d(TAG, businessesWithReviews.toString());
                                    BusinessesModel.getInstance().setBusinesses(businessesWithReviews);
                                    updateUI();
                                }

                                ;

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(TAG, e.toString());
                                }

                                @Override
                                public void onNext(List<Business> businesses) {
                                    Log.d(TAG, businesses.toString());
                                }
                            });
                }

                @Override
                public void onFailure(Call<BusinessesModel> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

        }
    }

    private void updateUI() {

        mProgressBar.setVisibility(View.GONE);
        mAdapter = new GridAdapter(BusinessesModel.getInstance().getBusinesses());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        SquareUtils.getBusInstance().post(new DetailsFragmentEvent(view,position));

                    }
                }
                ));

        mAdapter.notifyDataSetChanged();
    }

}