package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.RootLoader;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityAddCityBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ItemClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter.AddCityPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.PlacesDataRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter,
        LoaderManager.LoaderCallbacks<Map<String, Object>>, ItemClick {
    private final static String NEW_CITY_PRESENTER = "new_city_presenter";
    private final static String NEW_CITY_ADAPTER = "new_city_adapter";
    private final static String IS_TEXT_CHANGED = "is_text_changed";

    private final static String IS_CONFIG_CHANGE = "is_config_change";

    private final static int LOADER_NEW_CITY_ID = 2;

    private PlacesAutoComplete placesAutoComplete;

    private AutoCompleteTextView autoText;

    private AddCityPresenter mPresenter;
    private AdapterHorizontalWeather adapterHorizontalWeather;

    private CheckUpdateInOtherActivity checkUpdateInOtherActivity;

    private boolean isTextChanged = true;
    private boolean isConfigChange = false;

    private Bundle saveBundle;
    private ActivityAddCityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);
        binding.cvWeatherCity.setEvents(this);
        binding.cvWeatherCity.setSwitchGradient(SwitchGradient.getInstance());

        binding.setFirstLaunch(true);

        setupLoaders();

        setupViews();
        setupRxListeners();
        createPresenter();
        setupSingleton();

        saveBundle = savedInstanceState;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_CONFIG_CHANGE, isChangingConfigurations());
        outState.putBoolean(IS_TEXT_CHANGED, isTextChanged);
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
        autoText.clearFocus();

        mPresenter.attachActivity(this);
        mPresenter.checkEndTask();
    }

    private void setupViews() {
        autoText = binding.toolBar.tvAutocompleteText;

        binding.cvWeatherCity.cardFullInfo.setVisibility(View.INVISIBLE);

        placesAutoComplete = new PlacesAutoComplete(this, R.layout.item_auto_complete);
        autoText.setAdapter(placesAutoComplete);
    }

    // TODO: 29.08.2017 Вопрос об обработке Error в subscribe кнопки?
    // Мы же обрабатываем исключения в коде
    private void setupRxListeners() {
        RxAutoCompleteTextView.itemClickEvents(autoText)
                .subscribe(
                        next -> {
                            runSearch();
                            binding.setFirstLaunch(false);
                            isTextChanged = false;
                        },
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        DisposableManager::addDisposable);

        // С какого символа начинаем показывать подсказки
        autoText.setThreshold(3);

        RxTextView.textChanges(autoText)
                .filter(charSequence -> {
                    //Данное устловие позволяет защититься от показа подсказки, в тот момент,
                    // когда пользователь уже выбрал один из вариантов, и перевернул экран
                    if (isConfigChange) {
                        isConfigChange = false;
                        return false;
                    }
                    return true;
                })
                .subscribe(
                        next -> isTextChanged = true,
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        DisposableManager::addDisposable);

        RxTextView.textChanges(autoText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(s -> s.length() > 2)
                .switchMap(new Function<CharSequence, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@NonNull CharSequence charSequence) throws Exception {
                        return PlacesDataRepository.getInstance().getPlaces(charSequence.toString())
                                .map(s -> GsonFactory.getInstance().getPlacesName(s));
                    }
                })
                .filter(charSequence -> isTextChanged)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            autoText.setAdapter(placesAutoComplete.setItems(result));
                            autoText.showDropDown();
                        },
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        DisposableManager::addDisposable);

        RxView.clicks(autoText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            if (autoText.getAdapter().getCount() > 0 && !autoText.isPopupShowing()) {
                                autoText.showDropDown();
                            }
                        },
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        DisposableManager::addDisposable);

        RxView.keys(autoText)
                .filter(event -> {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        //перехватываем нажатие кнопки Back
                        onBackPressed();
                        return false;
                    }
                    return true;
                })
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map(mapper -> mapper.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                .subscribe(
                        next -> {
                            if (next) {
                                runSearch();
                                binding.setFirstLaunch(false);
                                isTextChanged = false;
                                if (autoText.isPopupShowing()) {
                                    autoText.dismissDropDown();
                                }
                            }
                        },
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        DisposableManager::addDisposable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void runSearch() {
        binding.cvWeatherCity.setCity(null);
        mPresenter.getWeatherInCity(autoText.getText().toString());
    }

    public void clearInputText() {
        autoText.setText("");
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
        if (!isChangingConfigurations()) {
            Log.e("Size: ", String.valueOf(DisposableManager.testSize()));
            DisposableManager.dispose();
            Log.e("Dis: ", "True");
            Log.e("Size: ", String.valueOf(DisposableManager.testSize()));
        }
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

            //чтобы при перевороте, не появлялась новая подсказка,
            // определяем был ли ConfigChange, и смотрим состояние изменения текста
            isConfigChange = saveBundle.getBoolean(IS_CONFIG_CHANGE);
            isTextChanged = saveBundle.getBoolean(IS_TEXT_CHANGED);

            //при перевороте экрана, нужно восстановить состояние подсказок
            if (autoText.getAdapter().getCount() > 0
                    && !autoText.isPopupShowing() && !isTextChanged) {
                autoText.showDropDown();
            }
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
