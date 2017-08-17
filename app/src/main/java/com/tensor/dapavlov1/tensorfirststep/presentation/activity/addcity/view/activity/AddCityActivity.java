package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tensor.dapavlov1.tensorfirststep.RetainedFragment;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter.AddCityPresenter;


import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends AppCompatActivity implements com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter {
    private final static String PRESENTER = "add_city_presenter";
    private final static String LIST_STATE_KEY = "recycler_list_state_weather";

    RetainedFragment retainedFragment;
    AddCityPresenter mPresenter;
    AdapterHorizontalWeather adapterHorizontalWeather;

    @BindInt(R.integer.weather_now) int WEATHER_NOW;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.rl_root_container) RelativeLayout rootContainer;

    @BindView(R.id.tv_autocompleteText) AutoCompleteTextView cityName;
    @BindViews({R.id.cv_nothing_weather, R.id.cv_weather_city}) List<CardView> cardViews;
    @BindView(R.id.cv_nothing_weather) CardView cardNothing;

    @BindView(R.id.cv_weather_city) CardView cardInfo;
    @BindView(R.id.tv_city) TextView cityNameInCard;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_temperature) TextView temperature;
    @BindView(R.id.tv_description) TextView description;
    @BindView(R.id.tv_wind_short) TextView windShortTitle;
    @BindView(R.id.tv_wind_speed) TextView windSpeed;

    @BindView(R.id.tv_pressure) TextView pressure;
    @BindView(R.id.cb_add_to_favorite) CheckBox addToFavorite;
    @BindView(R.id.iv_icon_weather) ImageView iconWeather;

    @BindView(R.id.iv_clear) ImageView clearCityName;
    @BindView(R.id.rv_weather_on_other_time) RecyclerView weatherOtherTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        createRetainedFragment();
        ButterKnife.bind(this);

        setupViews();
        setupRecyclerView();
        setupListeners();
        setupPresenter();
    }

    private void setupPresenter() {
        mPresenter = (AddCityPresenter) retainedFragment.getDataFromMap(PRESENTER);
        if (mPresenter == null) {
            mPresenter = new AddCityPresenter();
            mPresenter.setActivity(this);
            retainedFragment.setDataInMap(PRESENTER, mPresenter);
        } else {
            mPresenter.setActivity(this);
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

        retainedFragment.setParcelableData(
                LIST_STATE_KEY,
                weatherOtherTime.getLayoutManager().onSaveInstanceState());
        retainedFragment.setDataInMap(PRESENTER, mPresenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        cityName.clearFocus();

        mPresenter.setActivity(this);

        if (mPresenter.isRefresh()) {
            showLoading();
        } else {
            hideLoading();
            if (mPresenter.getCachedCity() != null) {
                showInformation(mPresenter.getCachedCity());
                showWeatherCardFullInfo();
            }
        }
    }

    private void setupViews() {
        cardInfo.setVisibility(View.INVISIBLE);
        cityName.setAdapter(new PlacesAutoComplete(this, R.layout.item_list_auto_complete));
    }

    private void setupListeners() {
        //@OnItemClick не поддерживает виджет AutoCompleteTextView # 483
        //https://github.com/JakeWharton/butterknife/issues/102
        cityName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clearChecked();
                mPresenter.getWeatherInCity(cityName.getText().toString());
            }
        });
    }

    @OnClick(R.id.iv_clear)
    void clearInputText() {
        cityName.setText("");
    }

    @OnClick(R.id.cb_add_to_favorite)
    void addToFavorite() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (addToFavorite.isChecked()) {
                    mPresenter.addToFavorite();
                } else {
                    mPresenter.deleteFromFavorite();
                }
            }
        });
    }

    private void clearChecked() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardInfo.setVisibility(View.INVISIBLE);
                addToFavorite.setChecked(false);
            }
        });
    }

    @Override
    public void setChecked(final Boolean checked) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (checked) {
                    addToFavorite.setChecked(true);
                } else {
                    addToFavorite.setChecked(false);
                }
            }
        });
    }

    @Override
    public void showInformation(final City city) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cityNameInCard.setText(city.getName());
                time.setText(city.getLastTimeUpdate());

                Weather weatherNow = city.getWeathers().get(WEATHER_NOW);
                temperature.setText(String.valueOf(weatherNow.getTemperature()));
                description.setText(weatherNow.getDescription());
                windShortTitle.setText(weatherNow.getWindShort());
                windSpeed.setText(String.valueOf(weatherNow.getWindSpeed()));
                pressure.setText(String.valueOf(weatherNow.getPressure()));
                rootContainer.setBackground(
                        SwitchGradient.getInstance().getBackgroung(weatherNow.getIconCode()));
                // ??
                Picasso.with(getApplicationContext())
                        .load(weatherNow.getIconUrl())
                        .into(iconWeather);

                refreshWeathers(city.getWeathers());
            }
        });
    }

    private void setupRecyclerView() {
        weatherOtherTime.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterHorizontalWeather = new AdapterHorizontalWeather();
        weatherOtherTime.setAdapter(adapterHorizontalWeather);
    }

    public void refreshWeathers(final List<Weather> weathers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterHorizontalWeather.setItems(weathers);

                Parcelable parcelable = (Parcelable) retainedFragment.getData(LIST_STATE_KEY);
                if (parcelable != null) {
                    weatherOtherTime.getLayoutManager().onRestoreInstanceState(parcelable);
                }

                adapterHorizontalWeather.notifyDataSetChanged();
            }
        });
    }

    static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.INVISIBLE);
        }
    };

    private void showCard(final CardView cardView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ButterKnife.apply(cardViews, INVISIBLE);
                cardView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showWeatherCardNothingFind() {
        showCard(cardNothing);
    }

    @Override
    public void showWeatherCardFullInfo() {
        showCard(cardInfo);
    }

    @Override
    public void showMessage(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}
