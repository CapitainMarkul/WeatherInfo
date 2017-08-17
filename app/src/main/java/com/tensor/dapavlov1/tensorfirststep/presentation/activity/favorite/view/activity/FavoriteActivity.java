package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
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
    private final static String PRESENTER = "favorite_presenter";
    private final static String LIST_STATE_KEY = "recycler_list_state_cities";
    private final static String GOTO_OTHER_ACTIVITY = "goto";

    private RetainedFragment retainedFragment;
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
        //return InstanceState
        createRetainedFragment();

        ButterKnife.bind(this);
        setupRecyclerView();
        setupListeners();
        setupPresenter();
        setupRouter();
    }

    private void setupPresenter() {
        mPresenter = (FavoritePresenter) retainedFragment.getDataFromMap(PRESENTER);
        if (mPresenter == null) {
            mPresenter = new FavoritePresenter();
            mPresenter.setActivity(this);
            retainedFragment.setDataInMap(PRESENTER, mPresenter);

            //updateWeather
            mPresenter.updateWeathers();
            setRefreshLayout(true);
        } else {
            mPresenter.setActivity(this);
        }
        if (mPresenter.getRefresh()) {
            refreshWeathers(mPresenter.getCachedCities());
        }
    }

    private void createRetainedFragment() {
        retainedFragment = (RetainedFragment) getFragmentManager().findFragmentByTag("data");
        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            getFragmentManager().beginTransaction().add(retainedFragment, "data").commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //detach favoriteActivity
        mPresenter.setActivity(null);

        retainedFragment.setParcelableData(
                LIST_STATE_KEY,
                recyclerViewFavorite.getLayoutManager().onSaveInstanceState());
        retainedFragment.setDataInMap(PRESENTER, mPresenter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setActivity(this);
        //если пришли сюда с другого экрана
        if (retainedFragment.getDataFromMap(GOTO_OTHER_ACTIVITY) == null
                || (Boolean) retainedFragment.getDataFromMap(GOTO_OTHER_ACTIVITY)) {
            retainedFragment.setDataInMap(GOTO_OTHER_ACTIVITY, false);
            mPresenter.updateWeathers();
        } else {
            setRefreshLayout(mPresenter.getRefresh());

            if (!mPresenter.getRefresh()) {
                //кешированные данные
                refreshWeathers(mPresenter.getCachedCities());
            }
        }
    }

    private void setupRouter() {
        routerToAddCity = new RouterToAddCity();
        mPresenter.setRouter(routerToAddCity);
    }

    @OnClick(R.id.fb_add_new_city)
    void intentAddCity() {
        retainedFragment.setDataInMap(GOTO_OTHER_ACTIVITY, true);
        mPresenter.changeActivity(this, AddCityActivity.class);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.updateWeathers();
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
//                hideLoading();
                adapterFavorite.setItems(weathers);
                //restoreStateInstance
                Parcelable parcelable = (Parcelable) retainedFragment.getData(LIST_STATE_KEY);
                if (parcelable != null) {
                    recyclerViewFavorite.getLayoutManager().onRestoreInstanceState(parcelable);
                }
                adapterFavorite.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showEmptyCard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retainedFragment.setDataInMap(REFRESH, false);
                cardEmpty.setVisibility(View.VISIBLE);
                setRefreshLayout(false);
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
                retainedFragment.setDataInMap(REFRESH, false);
                recyclerViewFavorite.setVisibility(View.VISIBLE);
                setRefreshLayout(false);
            }
        });
    }

    public void setRefreshLayout(final Boolean isRefresh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefresh);
            }
        });
    }

    private static final String REFRESH = "status_refresh";

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retainedFragment.setDataInMap(REFRESH, true);
                recyclerViewFavorite.setVisibility(View.INVISIBLE);
                setRefreshLayout(true);
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mPresenter.updateWeathers();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: 17.08.2017 КОгда уничтожать презентер? Destroy вызывается и при пересоздании активити
//        retainedFragment.setDataInMap(PRESENTER, null);
    }

    @Override
    public void onClickDelCityFromDb(int position) {
        mPresenter.deleteCity(position);
    }
}
