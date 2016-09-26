package com.example.j2emanue.myyelphelperapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.widget.ImageView;

import com.example.j2emanue.myyelphelperapp.Base.YelpBaseActivity;
import com.example.j2emanue.myyelphelperapp.Constants.ConstantsFragments;
import com.example.j2emanue.myyelphelperapp.Constants.ConstantsSharedPref;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.Events.TokenRecievedEvent;
import com.example.j2emanue.myyelphelperapp.Fragments.DetailsFragment;
import com.example.j2emanue.myyelphelperapp.Fragments.GridFragment;
import com.example.j2emanue.myyelphelperapp.Model.Business.Business;
import com.example.j2emanue.myyelphelperapp.Model.TokenInfo;
import com.example.j2emanue.myyelphelperapp.Services.ServiceGenerator;
import com.example.j2emanue.myyelphelperapp.Services.YelpTokenService;
import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends YelpBaseActivity {

    SecurePreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = new SecurePreferences(this, "my-preferences", "MytopSecretKey", true);

        if (savedInstanceState == null) {
            createGridFragment();
        }

        retrieveTokenAsync();
    }

    private void createGridFragment() {
        // Create a new grid Fragment to be placed in the activity layout
        GridFragment gridFragment = new GridFragment();

        // In case this activity was started from somewhere else,
        //pass the Intent 's extras to the fragment as arguments
        gridFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, gridFragment, ConstantsFragments.TAG_GRID_FRAGMENT).
                addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();//do it immediatley
    }


    @Subscribe
    public void answerDetailsFragmentEventReceived(DetailsFragmentEvent event) {

        Fragment gridFragment = getSupportFragmentManager().findFragmentByTag(ConstantsFragments.TAG_GRID_FRAGMENT);
        Fragment fragmentTarget = new DetailsFragment();
        ImageView thumbnail = (ImageView) event.getRootView();
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
            int myMessage = (int) thumbnail.getTag();
            bundle.putInt(ConstantsFragments.POSITION, myMessage);
            bundle.putParcelableArrayList("model", (ArrayList<Business>) event.getBusinesses());

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
}
