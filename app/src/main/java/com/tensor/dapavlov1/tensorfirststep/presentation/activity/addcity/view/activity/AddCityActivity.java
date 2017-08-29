package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.RootLoader;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityAddCityBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ItemClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter.AddCityPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter,
        LoaderManager.LoaderCallbacks<Map<String, Object>>, ItemClick {
    private final static String NEW_CITY_PRESENTER = "new_city_presenter";
    private final static String NEW_CITY_ADAPTER = "new_city_adapter";

    private final static int LOADER_NEW_CITY_ID = 2;

    private AddCityPresenter mPresenter;
    private AdapterHorizontalWeather adapterHorizontalWeather;

    private CheckUpdateInOtherActivity checkUpdateInOtherActivity;

    private List<CardView> cardViews = new ArrayList<>();

    private Bundle saveBundle;
    private ActivityAddCityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);
        binding.cvWeatherCity.setEvents(this);
        binding.cvWeatherCity.setSwitchGradient(SwitchGradient.getInstance());

        createCardViewList();
        setupLoaders();

        setupViews();
        setupRxListeners();
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
        mPresenter.checkEndTask();
    }

    private void setupViews() {
        binding.cvWeatherCity.cardFullInfo.setVisibility(View.INVISIBLE);
        binding.toolBar.tvAutocompleteText.setAdapter(new PlacesAutoComplete(this, R.layout.item_auto_complete));
    }

    // TODO: 29.08.2017 Вопрос об обработке Error в subscribe кнопки?
    // Мы же обрабатываем исключения в коде
    private void setupRxListeners() {
        RxAutoCompleteTextView.itemClickEvents(binding.toolBar.tvAutocompleteText)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(
                        next -> runSearch(),
                        Throwable::printStackTrace
                );

        RxView.keys(binding.toolBar.tvAutocompleteText)
                .map(mapper -> mapper.getAction() == KeyEvent.ACTION_DOWN)
                .subscribe(
                        next -> {
                            if (next) {
                                // TODO: 29.08.2017 проверить, на какие еще клавиши Триггерит
                                runSearch();
                            }
                        }
                );
    }

    public void runSearch() {
        binding.cvWeatherCity.setCity(null);
        mPresenter.getWeatherInCity(binding.toolBar.tvAutocompleteText.getText().toString());
    }

    public void clearInputText() {
        binding.toolBar.tvAutocompleteText.setText("");
    }

    public boolean isCheckedNow() {
        checkUpdateInOtherActivity.setUpdate(true);
        return binding.cvWeatherCity.cbAddToFavorite.isChecked();
    }

    @Override
    public void cityIsFavorite(final Boolean checked) {
        if (checked) {
            binding.cvWeatherCity.cbAddToFavorite.setChecked(true);
        } else {
            binding.cvWeatherCity.cbAddToFavorite.setChecked(false);
        }
    }

    public void refreshWeathers(final List<Weather> weathers) {
        adapterHorizontalWeather.setItems(weathers);
        adapterHorizontalWeather.notifyDataSetChanged();
    }

    @Override
    public void showMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
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

    public ActivityAddCityBinding getBinding() {
        return binding;
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

    @Override
    public void onItemClick() {
        mPresenter.onFavoriteClick();
    }
}
