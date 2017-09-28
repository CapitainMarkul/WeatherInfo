package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import com.android.databinding.library.baseAdapters.BR;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityAddCityBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.checkBoxClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BaseActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityComponent;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityDaggerModule;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.HorizontalWeatherAdapter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel.AddCityViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends BaseActivity<AddCityViewModelContract.ViewModel, AddCityViewModelContract.Presenter>
        implements checkBoxClick {

    private AutoCompleteTextView autoText;
    private ArrayAdapter<String> placesAutoCompleteAdapter;
    private HorizontalWeatherAdapter horizontalWeatherAdapter;

    private ActivityAddCityBinding binding;

    private AddCityComponent injectComponent;

    public static final String GET_STATE = "info_changed";
    public static final String DISPOSABLE_POOL_KEY = AddCityActivity.class.getSimpleName() + "_DISPOSABLE";

    private static boolean IS_CONFIG_CHANGE = false;
    private static boolean IS_TEXT_CHANGED_USE_NOT_KEYBOARD = false;
    private static boolean IS_TEXT_CHANGED_USE_KEYBOARD = true;

    public static final int INFO_IS_NOT_CHANGE_STATE = 0;
    public static final int INFO_IS_CHANGE_STATE = 1;
    private static int CURRENT_STATE = INFO_IS_NOT_CHANGE_STATE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);
        binding.setViewModel(getViewModel());

        setupViews();
        setupRecyclerAdapter();
        setupRecyclerView();
        setupRxListeners();

        if (savedInstanceState == null) {
            //Не показываем карточку "Ваш город не найден"
            getViewModel().setFirstLaunch(true);
            createStringAdapter(new ArrayList<>());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().addOnPropertyChangedCallback(viewModelObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        autoText.clearFocus();

        getViewModel().removeOnPropertyChangedCallback(viewModelObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isChangingConfigurations()) {
            getDisposableManager().disposeAll(DISPOSABLE_POOL_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GET_STATE, CURRENT_STATE);
        IS_CONFIG_CHANGE = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //иначе, после Terminate теряем состояние (действия: добавили город, сделали Terminate)
        CURRENT_STATE = savedInstanceState.getInt(GET_STATE);

        createStringAdapter(getViewModel().getPlaces());
        autoText.setAdapter(placesAutoCompleteAdapter);

        if (getViewModel().getCity() != null) {
            horizontalWeatherAdapter.setItems(getViewModel().getCity().getWeatherViews());
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void inject() {
        injectComponent = App.get()
                .presentationComponents()
                .addCityComponent(new AddCityDaggerModule());
    }

    @Override
    protected AddCityViewModelContract.ViewModel createViewModel() {
        return injectComponent.getViewModel();
    }

    @Override
    protected AddCityViewModelContract.Presenter createPresenter() {
        return injectComponent.getPresenter();
    }

    private void setupViews() {
        autoText = binding.toolBar.tvAutocompleteText;
        binding.cvWeatherCity.cardFullInfo.setVisibility(View.INVISIBLE);
        autoText.setAdapter(placesAutoCompleteAdapter);
    }

    private void createStringAdapter(List<String> items) {
        placesAutoCompleteAdapter = new ArrayAdapter<>(this, R.layout.item_auto_complete, items);
        placesAutoCompleteAdapter.setNotifyOnChange(true);
    }

    private void setupRxListeners() {
        autoText.setThreshold(2);

        autoText.setOnItemClickListener((adapterView, view, i, l) -> {
            IS_TEXT_CHANGED_USE_NOT_KEYBOARD = true;
            IS_TEXT_CHANGED_USE_KEYBOARD = false;
            startSearchingCity();
        });

        getDisposableManager().addDisposable(DISPOSABLE_POOL_KEY,
                RxView.keys(autoText)
                        .filter(event -> {
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                IS_TEXT_CHANGED_USE_NOT_KEYBOARD = true;
                                //перехватываем нажатие кнопки Back
                                if (CURRENT_STATE == INFO_IS_CHANGE_STATE) {
                                    setResult(FavoriteActivity.UPDATE_INFO_REQUEST,
                                            new Intent().putExtra(GET_STATE, CURRENT_STATE));
                                    CURRENT_STATE = INFO_IS_NOT_CHANGE_STATE;
                                }
                                onBackPressed();
                                return false;
                            }
                            return true;
                        })
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .map(mapper -> mapper.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                next -> {
                                    autoText.dismissDropDown();
                                    if (next) {
                                        IS_TEXT_CHANGED_USE_NOT_KEYBOARD = true;
                                        startSearchingCity();
                                    }
                                },
                                throwable -> showMessage(R.string.unknown_error)));

        getDisposableManager().addDisposable(DISPOSABLE_POOL_KEY,
                RxTextView.textChanges(autoText)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .filter(s -> s.length() > 2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> getPresenter().getPlaces(result.toString()),
                                throwable -> showMessage(R.string.unknown_error)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Observable<List<String>> test1() {
        List<String> test = new ArrayList<>();
        test.add("One");
        test.add("Two");
        test.add("Three");
        return Observable.create(e -> e.onNext(test));
    }

    private void test2() {
        test1().flatMap(Observable::fromArray)
                .subscribe(list -> Log.e("d", String.valueOf(list.size())));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startSearchingCity() {
        binding.cvWeatherCity.setCityView(null);
        getPresenter().getWeatherInCity(autoText.getText().toString());
    }

    public void clearInputText() {
        autoText.setText("");
    }

    public void refreshWeathers(final List<WeatherView> weatherViews) {
        horizontalWeatherAdapter.setItems(weatherViews);
        horizontalWeatherAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        binding.cvWeatherCity.rvWeatherOnOtherTime.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.cvWeatherCity.rvWeatherOnOtherTime.setAdapter(horizontalWeatherAdapter);
    }

    private void setupRecyclerAdapter() {
        horizontalWeatherAdapter = new HorizontalWeatherAdapter();
    }

    @Override
    public void showMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageWithAction(@StringRes int message) {
        //nothing
    }

    @Override
    public void showErrorMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    public ActivityAddCityBinding getBinding() {
        return binding;
    }

    private void updatePlacesInAdapter(List<String> places) {
//        placesAutoCompleteAdapter = new ArrayAdapter<>(this,R.layout.item_auto_complete, places);
//        placesAutoCompleteAdapter.clear();
//        placesAutoCompleteAdapter.addAll(places);
//        autoText.setAdapter(placesAutoCompleteAdapter);
////        new Handler().postDelayed(() -> autoText.showDropDown(), 700);
//        autoText.showDropDown();


        // TODO: 28.09.2017 Адаптер будем сетать только когда необходимо

        if (!IS_CONFIG_CHANGE) {
            if (IS_TEXT_CHANGED_USE_KEYBOARD) {
                //Показываем
                IS_TEXT_CHANGED_USE_NOT_KEYBOARD = false;

                createStringAdapter(places);
                autoText.setAdapter(placesAutoCompleteAdapter);
                autoText.showDropDown();
            } else if (IS_TEXT_CHANGED_USE_NOT_KEYBOARD) {
                autoText.dismissDropDown();

                //Следующее изменение возможно только при помощи клавиатуры
                IS_TEXT_CHANGED_USE_KEYBOARD = true;
            }
        } else {
            autoText.clearFocus();
            if (IS_TEXT_CHANGED_USE_NOT_KEYBOARD) {
                autoText.dismissDropDown();
                //Возвращаемся к нормальной стратегии
                IS_CONFIG_CHANGE = false;
                IS_TEXT_CHANGED_USE_KEYBOARD = true;
            } else {
                createStringAdapter(places);
                autoText.setAdapter(placesAutoCompleteAdapter);
                autoText.showDropDown();
            }
        }
    }

    private android.databinding.Observable.OnPropertyChangedCallback viewModelObserver =
            new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                    AddCityViewModel viewModel = (AddCityViewModel) sender;
                    switch (propertyId) {
                        case BR.city: {
                            if (viewModel.getCity() != null) {
                                refreshWeathers(viewModel.getCity().getWeatherViews());
                            }
                            break;
                        }
                        case BR.favorite: {
                            binding.setViewModel(viewModel);
                            break;
                        }
                        case BR.successMessage: {
                            showMessage(viewModel.getSuccessMessage());
                            break;
                        }
                        case BR.errorMessage: {
                            showErrorMessage(viewModel.getErrorMessage());
                            break;
                        }
                        case BR.onClearInputText: {
                            clearInputText();
                            break;
                        }
                        case BR.onItemClick: {
                            onItemClick();
                            break;
                        }
                        case BR.places: {
                            updatePlacesInAdapter(viewModel.getPlaces());
                            break;
                        }
                        case BR.animation: {
                            if (viewModel.isAnimation()) {
                                Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha);
                                binding.cvSearching.tvSearching.startAnimation(anim);
                            } else {
                                binding.cvSearching.tvSearching.clearAnimation();
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    binding.setViewModel(viewModel);
                }
            };

    @Override
    public void onItemClick() {
        changeState(INFO_IS_CHANGE_STATE);
        switchFavorite();
        getPresenter().onFavoriteClick();
    }

    private void changeState(int newState) {
        CURRENT_STATE = newState;
    }

    private void switchFavorite() {
        CheckBox temp = binding.cvWeatherCity.cbAddToFavorite;
        if (temp.isChecked()) {
            temp.setChecked(false);
        } else {
            temp.setChecked(true);
        }
    }

    @Override
    public Activity getComponentsActivity() {
        return this;
    }
}
