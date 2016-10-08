package com.example.j2emanue.myyelphelperapp.Events;

import android.view.View;

import com.example.j2emanue.myyelphelperapp.Model.Business.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2emanue on 9/25/16.
 */

public class DetailsFragmentEvent extends BaseEvent {


    View view;
    private List<Business>businesses;

    int position;

    public DetailsFragmentEvent(View view, List<Business> businesses,int position) {
        this.businesses = businesses;
        this.view = view;
        this.position = position;
    }

    public View getRootView() {
        return view;
    }

    public void setRootView(View rootView) {
        this.view = view;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }
}
