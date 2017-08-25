package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.squareup.picasso.Picasso;
import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.RootLoader;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityAddCityBinding;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter.AddCityPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter,
        LoaderManager.LoaderCallbacks<Map<String, Object>> {
    private final static String NEW_CITY_PRESENTER = "new_city_presenter";
    private final static String NEW_CITY_ADAPTER = "new_city_adapter";

    private final static int LOADER_NEW_CITY_ID = 2;

    private AddCityPresenter mPresenter;
    private AdapterHorizontalWeather adapterHorizontalWeather;

    private int WEATHER_NOW = 10;

    private CheckUpdateInOtherActivity checkUpdateInOtherActivity;

    private List<CardView> cardViews = new ArrayList<>();

    private Bundle saveBundle;
    private ActivityAddCityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);

        //@IntegerRes - не смог заставить работать
        WEATHER_NOW = getResources().getInteger(R.integer.weather_now);

        createCardViewList();
        setupLoaders();

        setupViews();
        setupListeners();
        createPresenter();
        setupSingleton();

        saveBundle = savedInstanceState;
    }

    private void createCardViewList() {
        cardViews.add(binding.cvNothingWeather.cvNothing);
        cardViews.add(binding.cvWeatherCity.cardFullInfo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mPresenter.saveData(outState));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupLoaders();
    }

    private void setupSingleton() {
        checkUpdateInOtherActivity = CheckUpdateInOtherActivity.getInstance();
    }

    private void setupLoaders() {
        getSupportLoaderManager().initLoader(LOADER_NEW_CITY_ID, null, this);
    }

    private void createPresenter() {
        mPresenter = new AddCityPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding.toolBar.tvAutocompleteText.clearFocus();

        mPresenter.attachActivity(this);
//        mPresenter.setActivity(this);
        mPresenter.checkEndTask();
    }

    private void setupViews() {
        binding.cvWeatherCity.cardFullInfo.setVisibility(View.INVISIBLE);
        binding.toolBar.tvAutocompleteText.setAdapter(new PlacesAutoComplete(this, R.layout.item_auto_complete));
    }

    @BindingAdapter({"bind:onKeyListener"})
    public static void setOnKeyListener(AutoCompleteTextView view, View.OnKeyListener onKeyListener){
        view.setOnKeyListener(onKeyListener);
    }

    private void setupListeners() {
        //@OnItemClick не поддерживает виджет AutoCompleteTextView # 483
        //https://github.com/JakeWharton/butterknife/issues/102
        binding.toolBar.tvAutocompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                runSearch();
            }
        });

        binding.toolBar.tvAutocompleteText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            runSearch();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void runSearch() {
        clearChecked();
        mPresenter.getWeatherInCity(binding.toolBar.tvAutocompleteText.getText().toString());
    }

    public void clearInputText() {
        binding.toolBar.tvAutocompleteText.setText("");
    }

//    @OnClick(R.id.cb_add_to_favorite)
//    void addToFavorite() {
//        if (binding.cvWeatherCity.cbAddToFavorite.isChecked()) {
//            mPresenter.addToFavorite();
//        } else {
//            mPresenter.deleteFromFavorite();
//        }
//        checkUpdateInOtherActivity.setUpdate(true);
//    }

    public boolean isCheckedNow() {
        checkUpdateInOtherActivity.setUpdate(true);
        return binding.cvWeatherCity.cbAddToFavorite.isChecked();
    }

    private void clearChecked() {
        binding.cvWeatherCity.cardFullInfo.setVisibility(View.INVISIBLE);
        binding.cvWeatherCity.cbAddToFavorite.setChecked(false);
    }

    @Override
    public void cityIsFavorite(final Boolean checked) {
        if (checked) {
            binding.cvWeatherCity.cbAddToFavorite.setChecked(true);
        } else {
            binding.cvWeatherCity.cbAddToFavorite.setChecked(false);
        }
    }

    @Override
    public void showInformation(City city) {
        binding.cvWeatherCity.tvCity.setText(city.getName());
        binding.cvWeatherCity.tvTime.setText(city.getLastTimeUpdate());

        Weather weatherNow = city.getWeathers().get(WEATHER_NOW);
        binding.cvWeatherCity.tvTemperature.setText(String.valueOf(weatherNow.getTemperature()));
        binding.cvWeatherCity.tvDescription.setText(weatherNow.getDescription());
        binding.cvWeatherCity.tvWindShort.setText(weatherNow.getWindShort());
        binding.cvWeatherCity.tvWindSpeed.setText(String.valueOf(weatherNow.getWindSpeed()));
        binding.cvWeatherCity.tvPressure.setText(String.valueOf(weatherNow.getPressure()));
        binding.cvWeatherCity.rlRootContainer.setBackground(
                SwitchGradient.getInstance().getBackgroung(weatherNow.getIconCode()));
        // ??
        Picasso.with(getApplicationContext())
                .load(weatherNow.getIconUrl())
                .into(binding.cvWeatherCity.ivIconWeather);

        refreshWeathers(city.getWeathers());
    }

    public void refreshWeathers(final List<Weather> weathers) {
        adapterHorizontalWeather.setItems(weathers);
        adapterHorizontalWeather.notifyDataSetChanged();
    }

//    static final ButterKnife.Action<View> INVISIBLE = new ButterKnife.Action<View>() {
//        @Override
//        public void apply(@NonNull View view, int index) {
//            view.setVisibility(View.INVISIBLE);
//        }
//    };

    private void hideAllCardViews() {
        for (CardView item : cardViews) {
            item.setVisibility(View.INVISIBLE);
        }
    }

    private void showCard(final CardView cardView) {
        hideAllCardViews();
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showWeatherCardNothingFind() {
        showCard(binding.cvNothingWeather.cvNothing);
    }

    @Override
    public void showWeatherCardFullInfo() {
        showCard(binding.cvWeatherCity.cardFullInfo);
    }

    @Override
    public void showMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        binding.layoutProgressBar.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        binding.layoutProgressBar.progressBar.setVisibility(View.VISIBLE);
    }

    private void setupRecyclerView() {
        binding.cvWeatherCity.rvWeatherOnOtherTime.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.cvWeatherCity.rvWeatherOnOtherTime.setAdapter(adapterHorizontalWeather);
    }

    private void setupRecyclerAdapter() {
        adapterHorizontalWeather = new AdapterHorizontalWeather();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachActivity();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        createPresenter();
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
        binding.toolBar.setMPresenter(mPresenter);
        binding.toolBar.setActivity(this);
        binding.cvWeatherCity.setMPresenter(mPresenter);

        adapterHorizontalWeather = (AdapterHorizontalWeather) dataMap.get(NEW_CITY_ADAPTER);
        mPresenter.attachActivity(this);

        if (saveBundle != null) {
            mPresenter.resumePresenter(saveBundle);
        }

        setupRecyclerView();
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Object>> loader) {

    }
}
