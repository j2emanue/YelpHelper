package com.example.j2emanue.myyelphelperapp.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;

/**
 * Created by j2emanue on 9/24/16.
 */

public  abstract class YelpBaseFragment extends android.support.v4.app.Fragment {

    public final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SquareUtils.getBusInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SquareUtils.getBusInstance().unregister(this);
    }
}
