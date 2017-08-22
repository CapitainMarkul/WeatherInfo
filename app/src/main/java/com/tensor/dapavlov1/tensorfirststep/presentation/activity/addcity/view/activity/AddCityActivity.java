package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.tensor.dapavlov1.tensorfirststep.RootLoader;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter.AddCityPresenter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter,
        LoaderManager.LoaderCallbacks<Map<String, Object>> {
    private final static String NEW_CITY_PRESENTER = "new_city_presenter";
    private final static String NEW_CITY_ADAPTER = "new_city_adapter";

    private final static int LOADER_NEW_CITY_ID = 2;

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
        ButterKnife.bind(this);
        setupLoaders();

        setupViews();
//        setupRecyclerView();
        setupListeners();
        setupPresenter();
    }

    private void setupLoaders() {
        getSupportLoaderManager().initLoader(LOADER_NEW_CITY_ID, null, this);
    }

    private void setupPresenter() {
        mPresenter = new AddCityPresenter();
        mPresenter.attachActivity(this);
//        mPresenter.setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        cityName.clearFocus();

        mPresenter.attachActivity(this);
//        mPresenter.setActivity(this);
        mPresenter.checkEndTask();
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
        if (addToFavorite.isChecked()) {
            mPresenter.addToFavorite();
        } else {
            mPresenter.deleteFromFavorite();
        }
    }

    private void clearChecked() {
        cardInfo.setVisibility(View.INVISIBLE);
        addToFavorite.setChecked(false);
    }

    @Override
    public void cityIsFavorite(final Boolean checked) {
        if (checked) {
            addToFavorite.setChecked(true);
        } else {
            addToFavorite.setChecked(false);
        }
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


    public void refreshWeathers(final List<Weather> weathers) {
        adapterHorizontalWeather.setItems(weathers);
        adapterHorizontalWeather.notifyDataSetChanged();
    }

    static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setVisibility(View.INVISIBLE);
        }
    };

    private void showCard(final CardView cardView) {
        ButterKnife.apply(cardViews, INVISIBLE);
        cardView.setVisibility(View.VISIBLE);
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
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        weatherOtherTime.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        weatherOtherTime.setAdapter(adapterHorizontalWeather);
    }

    private void setupRecyclerAdapter() {
        adapterHorizontalWeather = new AdapterHorizontalWeather();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachActivity();
//        mPresenter.setActivity(null);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        setupPresenter();
        setupRecyclerAdapter();
        setupRecyclerView();
        return new RootLoader(getBaseContext(), createConfigMap());
    }

    private Map<String, Object> createConfigMap() {
        Map<String, Object> values = new HashMap<>();
        values.put(NEW_CITY_PRESENTER, mPresenter);
        values.put(NEW_CITY_ADAPTER, adapterHorizontalWeather);
        return values;
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> dataMap) {
        mPresenter = (AddCityPresenter) dataMap.get(NEW_CITY_PRESENTER);

        adapterHorizontalWeather = (AdapterHorizontalWeather) dataMap.get(NEW_CITY_ADAPTER);
        setupRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Object>> loader) {

    }
}
