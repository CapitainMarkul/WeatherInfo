package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.RootLoader;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerEmptyListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter.AdapterFavorite;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.routers.RouterToAddCity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.FavoritePresenter,
        RecyclerViewItemClickListener, RecyclerEmptyListener,
        LoaderManager.LoaderCallbacks<Map<String, Object>> {
    private final static String FAVORITE_PRESENTER = "favorite_presenter";
    private final static String FAVORITE_ADAPTER = "favorite_adapter";

    private final static int LOADER_FAVORITE_ID = 1;

    FavoritePresenter mPresenter;
    AdapterFavorite adapterFavorite;
    RouterToAddCity routerToAddCity;

    @BindView(R.id.fb_add_new_city) FloatingActionButton addNewCity;
    @BindView(R.id.root_container) CoordinatorLayout rootContainer;
    @BindView(R.id.sr_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_main_favorite) RecyclerView recyclerViewFavorite;
    @BindView(R.id.cv_default) CardView cardEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        setupLoaders();
        setupListeners();
    }

    private void chechUpdateInOtherActivity() {
        if (CheckUpdateInOtherActivity.getInstance().isUpdate()) {
            mPresenter.updateWeathers();
            CheckUpdateInOtherActivity.getInstance().setUpdate(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chechUpdateInOtherActivity();
    }

    private void setupRouter() {
        routerToAddCity = new RouterToAddCity();
        mPresenter.setRouter(routerToAddCity);
    }

    private void setupPresenter() {
        mPresenter = new FavoritePresenter();
        mPresenter.attachActivity(this);
//        mPresenter.setActivity(this);
        setupRouter();
    }

    private void launchUpdateWeatherInfo() {
        mPresenter.updateWeathers();
    }

    @OnClick(R.id.fb_add_new_city)
    void intentAddCity() {
        mPresenter.switchActivity(this, AddCityActivity.class);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateWeathers();
            }
        });
    }

    @Override
    public void refreshWeathers(final List<City> weathers) {
        recyclerViewFavorite.setVisibility(View.VISIBLE);
        adapterFavorite.setItems(weathers);
        //restoreStateInstance
        adapterFavorite.notifyDataSetChanged();
    }

    @Override
    public void showEmptyCard() {
        recyclerViewFavorite.setVisibility(View.INVISIBLE);
        cardEmpty.setVisibility(View.VISIBLE);

        runRefreshLayout(false);
    }

    @Override
    public void hideEmptyCard() {
        cardEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        recyclerViewFavorite.setVisibility(View.VISIBLE);
        runRefreshLayout(false);
    }

    public void runRefreshLayout(final Boolean isRefresh) {
        swipeRefreshLayout.setRefreshing(isRefresh);
    }

    @Override
    public void showLoading() {
        recyclerViewFavorite.setVisibility(View.INVISIBLE);
        runRefreshLayout(true);
    }

    @Override
    public void showMessage(@StringRes final int message) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    private Map<String, Object> createConfigMap() {
        Map<String, Object> values = new HashMap<>();
        values.put(FAVORITE_PRESENTER, mPresenter);
        values.put(FAVORITE_ADAPTER, adapterFavorite);
        return values;
    }

    //OTHER
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachActivity();
    }

    @Override
    public void onClickDelCityFromDb(int position) {
        mPresenter.deleteCity(position);
    }

    //Loaders
    private void setupLoaders() {
        getSupportLoaderManager().initLoader(LOADER_FAVORITE_ID, null, this);
    }

    private void setupRecyclerAdapter() {
        adapterFavorite = new AdapterFavorite();
        adapterFavorite.setListener(FavoriteActivity.this);
        adapterFavorite.setEmptyListener(FavoriteActivity.this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        setupPresenter();
        setupRecyclerAdapter();
        setupRecyclerView();

        launchUpdateWeatherInfo();
        return new RootLoader(getBaseContext(), createConfigMap());
    }

    private void setupRecyclerView() {
        recyclerViewFavorite.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewFavorite.setAdapter(adapterFavorite);
        recyclerViewFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> dataMap) {
        mPresenter = (FavoritePresenter) dataMap.get(FAVORITE_PRESENTER);
        runRefreshLayout(!mPresenter.getRefreshComplete());

        //восстаансливаем прошлый адаптер
        adapterFavorite = (AdapterFavorite) dataMap.get(FAVORITE_ADAPTER);
        setupRecyclerView();

        mPresenter.attachActivity(this);

        //если были обновления на другом экране

        //если ответ от сервера уже пришел, то показываем результат
        if (mPresenter.getRefreshComplete()) {
            mPresenter.showCachedCities();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onEmpty() {
        showEmptyCard();
    }
}
