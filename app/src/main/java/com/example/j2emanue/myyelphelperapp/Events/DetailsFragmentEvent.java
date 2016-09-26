package com.example.j2emanue.myyelphelperapp.Events;

import android.view.View;

import com.example.j2emanue.myyelphelperapp.Model.Business.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2emanue on 9/25/16.
 */

public class DetailsFragmentEvent extends BaseEvent {

    private View rootView;


    private List<Business>businesses;

    public DetailsFragmentEvent(View rootView, List<Business> businesses) {
        this.businesses = businesses;
        this.rootView = rootView;
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }


    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }
}
