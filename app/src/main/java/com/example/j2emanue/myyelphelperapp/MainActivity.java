package com.example.j2emanue.myyelphelperapp;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.j2emanue.myyelphelperapp.Base.YelpBaseActivity;
import com.example.j2emanue.myyelphelperapp.Constants.Constants;
import com.example.j2emanue.myyelphelperapp.Constants.ConstantsFragments;
import com.example.j2emanue.myyelphelperapp.Constants.ConstantsSharedPref;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.Events.TokenRecievedEvent;
import com.example.j2emanue.myyelphelperapp.Fragments.DetailsFragment;
import com.example.j2emanue.myyelphelperapp.Fragments.GridFragment;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Model.Business.BusinessesModel;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.Reviews;
import com.example.j2emanue.myyelphelperapp.Model.TokenInfo;
import com.example.j2emanue.myyelphelperapp.Services.ServiceGenerator;
import com.example.j2emanue.myyelphelperapp.Services.YelpRestaurantService;
import com.example.j2emanue.myyelphelperapp.Services.YelpTokenService;
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

public class MainActivity extends YelpBaseActivity {


    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;

    @BindView(R.id.coordinator_layout_main)
    CoordinatorLayout mCoordinatorLayout;

    SecurePreferences mPrefs;

    GridFragment gridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPrefs = new SecurePreferences(this, "my-preferences", "MytopSecretKey", true);

        createGridFragment(savedInstanceState);

        retrieveTokenAsync();



    }

    private void createGridFragment(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            // Create a new grid Fragment to be placed in the activity layout
            gridFragment = new GridFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, gridFragment, ConstantsFragments.TAG_GRID_FRAGMENT).
                    addToBackStack(null).commit();
        } else {
            gridFragment = (GridFragment) getSupportFragmentManager().findFragmentByTag(ConstantsFragments.TAG_GRID_FRAGMENT);
        }

    }


    @Subscribe
    public void answerDetailsFragmentEventReceived(DetailsFragmentEvent event) {

        Fragment gridFragment = getSupportFragmentManager().findFragmentByTag(ConstantsFragments.TAG_GRID_FRAGMENT);
        Fragment fragmentTarget = new DetailsFragment();
        View clickedView = event.getRootView();
        ImageView thumbnail = (ImageView) clickedView.findViewById(R.id.gridview_image);
        thumbnail.setTransitionName(ConstantsFragments.SHARED_ELEMENT_TRANSITION_NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inflate transitions to apply
            Transition changeTransform = TransitionInflater.from(this).
                    inflateTransition(R.transition.change_view_transform);
            Transition explodeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.explode);

            // Setup exit transition on first fragment
            gridFragment.setSharedElementReturnTransition(changeTransform);
            gridFragment.setExitTransition(explodeTransform);

            // Setup enter transition on second fragment
            fragmentTarget.setSharedElementEnterTransition(changeTransform);
            fragmentTarget.setEnterTransition(explodeTransform);


            Bundle bundle = new Bundle();
            int myMessage = event.getPosition();
            bundle.putInt(ConstantsFragments.POSITION, myMessage);
            fragmentTarget.setArguments(bundle);

            // Add second fragment by replacing first
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragmentTarget, ConstantsFragments.DETAILS_FRAGMENT)
                    .addToBackStack("detailsTransaction")
                    .addSharedElement(thumbnail, ConstantsFragments.SHARED_ELEMENT_TRANSITION_NAME);
            // Apply the transaction
            ft.commit();
        } else {
            // Code to run on older devices
        }

    }


    private void retrieveTokenAsync() {

        String grantType = getString(R.string.grant_type);
        String clientId = BuildConfig.CLIENT_ID;
        String secret = BuildConfig.CLIENT_SECRET;


        if (mPrefs.containsKey(ConstantsSharedPref.TOKEN)) {
            SquareUtils.getBusInstance().post(new TokenRecievedEvent(true, mPrefs.getString(ConstantsSharedPref.TOKEN), null));
            return;
        }
        YelpTokenService apiTokenService = ServiceGenerator.createService(YelpTokenService.class, null);

        Call<TokenInfo> call = apiTokenService.getTokenInfo(grantType, clientId, secret);

        call.enqueue(new Callback<TokenInfo>() {
            @Override
            public void onResponse(Call<TokenInfo> call, Response<TokenInfo> response) {
                String token = response.body().getAccessToken();

                mPrefs.put(ConstantsSharedPref.TOKEN, token);//we can cache it later
                SquareUtils.getBusInstance().post(new TokenRecievedEvent(true, token, null));
                Log.d(TAG, response.message().toString());
            }

            @Override
            public void onFailure(Call<TokenInfo> call, Throwable t) {
                SquareUtils.getBusInstance().post(new TokenRecievedEvent(false, null, t.getMessage()));
                Log.d(TAG, t.getMessage().toString());
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
                                    hideSpinner();
                                    gridFragment.updateUI();
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

    private void hideSpinner() {
        mProgressBar.setVisibility(View.GONE);
    }
}
