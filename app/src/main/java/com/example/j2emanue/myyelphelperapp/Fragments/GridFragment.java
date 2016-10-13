package com.example.j2emanue.myyelphelperapp.Fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j2emanue.myyelphelperapp.Base.YelpBaseFragment;
import com.example.j2emanue.myyelphelperapp.Events.DetailsFragmentEvent;
import com.example.j2emanue.myyelphelperapp.GridAdapter;
import com.example.j2emanue.myyelphelperapp.Model.Business.BusinessesModel;
import com.example.j2emanue.myyelphelperapp.R;
import com.example.j2emanue.myyelphelperapp.RecyclerItemClickListener;
import com.example.j2emanue.myyelphelperapp.Utilities.SquareUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by j2emanue on 9/24/16.
 */

public class GridFragment extends YelpBaseFragment {

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView((R.id.recycler_view))
    RecyclerView recyclerView;

    public GridAdapter mAdapter;

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


     //   updateUI();

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

    private void initRecyclerView() {
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
    }


    public void updateUI() {
        initRecyclerView();
        initFabButton();
    }



}