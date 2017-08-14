package com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.FavoriteActivity.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.AddCityActivity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.FavoriteActivity.adapter.AdapterFavorite;
import com.tensor.dapavlov1.tensorfirststep.interfaces.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.FavoriteActivity.presenter.FavoriteViewPresenter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends MvpAppCompatActivity implements FavoritePresenter, RecyclerViewItemClickListener {
    @InjectPresenter
    FavoriteViewPresenter mPresenter;

    @BindView(R.id.fb_add_new_city) FloatingActionButton addNewCity;
    @BindView(R.id.root_container) CoordinatorLayout rootContainer;
    @BindView(R.id.sr_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_main_favorite) RecyclerView recyclerViewFavorite;
    @BindView(R.id.cv_default) CardView cardEmpty;

    AdapterFavorite adapterFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupListeners();
    }


    @OnClick(R.id.fb_add_new_city)
    void intentAddCity() {
        Intent intent = new Intent(this, AddCityActivity.class);
        startActivity(intent);
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
    public void refreshWeathers(List<City> weathers) {
        adapterFavorite.setItems(weathers);
        adapterFavorite.notifyDataSetChanged();
    }

    @Override
    public void showEmptyCard() {
        cardEmpty.setVisibility(View.VISIBLE);
        stopRefreshLayout();
    }

    @Override
    public void hideEmptyCard() {
        cardEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        recyclerViewFavorite.setVisibility(View.VISIBLE);
        stopRefreshLayout();
    }

    private void stopRefreshLayout() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        recyclerViewFavorite.setVisibility(View.INVISIBLE);
        stopRefreshLayout();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void errorMessage(String message) {
        //TODO: можно добавить кнопку действия (reconnect)
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
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
    public void onClickDelCityFromDb(int position) {
        mPresenter.deleteCity(position);
    }
}
