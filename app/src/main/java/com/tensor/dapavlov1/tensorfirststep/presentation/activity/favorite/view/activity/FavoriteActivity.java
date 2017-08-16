package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.tensor.dapavlov1.tensorfirststep.RetainedFragment;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter.AdapterFavorite;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.routers.RouterToAddCity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends AppCompatActivity implements com.tensor.dapavlov1.tensorfirststep.interfaces.FavoritePresenter, RecyclerViewItemClickListener {

    FavoritePresenter mPresenter;

    private RetainedFragment retainedFragment;

    @BindView(R.id.fb_add_new_city) FloatingActionButton addNewCity;
    @BindView(R.id.root_container) CoordinatorLayout rootContainer;
    @BindView(R.id.sr_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_main_favorite) RecyclerView recyclerViewFavorite;
    @BindView(R.id.cv_default) CardView cardEmpty;

    AdapterFavorite adapterFavorite;
    RouterToAddCity routerToAddCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mPresenter = new FavoritePresenter(this);

        //return InstanceState
        FragmentManager fragmentManager = getFragmentManager();
        retainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag("data");

        if(retainedFragment == null){
            retainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(retainedFragment, "data").commit();
            retainedFragment.setData(savedInstanceState);
        }

        ButterKnife.bind(this);
        setupRouter();
        setupRecyclerView();
        setupListeners();
    }

    private void setupRouter(){
        routerToAddCity = new RouterToAddCity();
        mPresenter.setRouter(routerToAddCity);
    }

    @OnClick(R.id.fb_add_new_city)
    void intentAddCity() {
        mPresenter.changeActivity(this, AddCityActivity.class);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.showFavoriteCard();
            }
        });

        adapterFavorite.setListener(FavoriteActivity.this);
    }

    private void setupRecyclerView() {
        recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        adapterFavorite = new AdapterFavorite();
        recyclerViewFavorite.setAdapter(adapterFavorite);
    }

    @Override
    public void refreshWeathers(final List<City> weathers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterFavorite.setItems(weathers);
                adapterFavorite.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showEmptyCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardEmpty.setVisibility(View.VISIBLE);
                stopRefreshLayout();
            }
        });
    }

    @Override
    public void hideEmptyCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardEmpty.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewFavorite.setVisibility(View.VISIBLE);
                stopRefreshLayout();
            }
        });
    }

    private void stopRefreshLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewFavorite.setVisibility(View.INVISIBLE);
//                stopRefreshLayout();
            }
        });
    }

    @Override
    public void showMessage(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    //OTHER
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.showFavoriteCard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        retainedFragment.setData(this.get);
    }

    @Override
    public void onClickDelCityFromDb(int position) {
        mPresenter.deleteCity(position);
    }
}
