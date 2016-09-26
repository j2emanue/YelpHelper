package com.example.j2emanue.myyelphelperapp.Base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;

/**
 * Created by j2emanue on 9/24/16.
 */

public abstract class YelpBaseActivity extends AppCompatActivity {

    public static final String TAG = "MyYelpApp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SquareUtils.getBusInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SquareUtils.getBusInstance().unregister(this);
    }

    //not used yet
    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }
}
