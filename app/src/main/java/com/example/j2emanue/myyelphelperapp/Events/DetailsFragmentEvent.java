package com.example.j2emanue.myyelphelperapp.Events;

import android.view.View;

/**
 * Created by j2emanue on 9/25/16.
 */

public class DetailsFragmentEvent extends BaseEvent {


    View view;

    int position;

    public DetailsFragmentEvent(View view,int position) {
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

}
