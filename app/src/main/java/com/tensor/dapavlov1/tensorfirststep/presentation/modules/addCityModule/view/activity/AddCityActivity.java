package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import com.android.databinding.library.baseAdapters.BR;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityAddCityBinding;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.interfaces.checkBoxClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BaseActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityComponent;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityDaggerModule;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.adapter.PlacesAutoComplete;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.HorizontalWeatherAdapter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel.AddCityViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper.AdapterStorage;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.PlacesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class AddCityActivity extends BaseActivity<AddCityViewModelContract.ViewModel, AddCityViewModelContract.Presenter>
        implements checkBoxClick {

    private AutoCompleteTextView autoText;
    private PlacesAutoComplete placesAutoComplete;
    private HorizontalWeatherAdapter horizontalWeatherAdapter;

    private ActivityAddCityBinding binding;

    private AddCityComponent injectComponent;

    private final static String simpleName = AddCityActivity.class.getSimpleName();

    public static final String NEW_CITY_ADAPTER_KEY = simpleName + "_ADAPTER";
    public static final String GET_STATE = "info_changed";

    private static final int INFO_IS_NOT_CHANGE_STATE = 0;
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
        // FIXME: 14.09.2017 Ошибка: при перевороте, плодятся Observer'ы
        if (savedInstanceState == null) {
            //Не показываем карточку "Ваш город не найден"
            getViewModel().setFirstLaunch(true);
//            setupRxListeners();
        }
//        Log.e("SIze:", String.valueOf(getDisposableManager().testSize(DISPOSABLE_POOL_KEY)));
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        AdapterStorage.getInstance().restoreAdapter(NEW_CITY_ADAPTER_KEY);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AdapterStorage.getInstance().saveAdapter(NEW_CITY_ADAPTER_KEY, horizontalWeatherAdapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        horizontalWeatherAdapter = AdapterStorage.getInstance().restoreAdapter(NEW_CITY_ADAPTER_KEY);
        setupRecyclerView();
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
        placesAutoComplete = new PlacesAutoComplete(this, R.layout.item_auto_complete);
        autoText.setAdapter(placesAutoComplete);
    }

    // TODO: 29.08.2017 Вопрос об обработке Error в subscribe кнопки?
    // Мы же обрабатываем исключения в коде
    private void setupRxListeners() {

        autoText.setOnItemClickListener((adapterView, view, i, l) -> {
            startSearchingCity();
        });

        RxView.keys(autoText)
                .filter(event -> {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        //перехватываем нажатие кнопки Back
                        if (CURRENT_STATE == INFO_IS_CHANGE_STATE) {
                            Intent intent = new Intent(FavoritePresenter.ADD_NEW_CITY_ACTION);
                            intent.putExtra(GET_STATE, CURRENT_STATE);
                            sendBroadcast(intent);
                            CURRENT_STATE = INFO_IS_NOT_CHANGE_STATE;
                        }
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
                                startSearchingCity();
//                                binding.setFirstLaunch(false);
//                                isTextChanged = false;
//                                if (autoText.isPopupShowing()) {
//                                    autoText.dismissDropDown();
//                                }
                            }
                        },
                        throwable -> showMessage(R.string.unknown_error),
                        () -> {
                        },
                        disposable -> getDisposableManager().addDisposable(DISPOSABLE_POOL_KEY, disposable));

        //Нажание на элемент выпадающего списка
//        RxAutoCompleteTextView.itemClickEvents(autoText)
//                .subscribe(
//                        next -> {
//                            startSearchingCity();
////                            binding.setFirstLaunch(false);
////                            isTextChanged = false;
//                        },
//                        throwable -> showMessage(R.string.unknown_error),
//                        () -> {
//                        },
//                        disposable -> getDisposableManager().addDisposable(DISPOSABLE_POOL_KEY, disposable));

        io.reactivex.Observable e = RxTextView.textChangeEvents(autoText);
        io.reactivex.Observable e2 = RxTextView.textChangeEvents(autoText);

        // TODO: 20.09.2017 Сделай другие модули, а потом доделай это 
        io.reactivex.Observable.combineLatest(e, e2, new BiFunction() {
            @Override
            public Object apply(@NonNull Object o, @NonNull Object o2) throws Exception {
                return null;
            }
        });
//        (emailObservable, passwordObservable) -> {});
        RxTextView.textChanges(autoText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(s -> s.length() > 2)
                .switchMap(new Function<CharSequence, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@NonNull CharSequence charSequence) throws Exception {
                        Object f = PlacesDataRepository.getInstance();

                        return PlacesDataRepository.getInstance().getPlaces(charSequence.toString())
                                .map(s -> GsonFactory.getInstance().getPlacesName(s));
                    }
                })
//                .filter(charSequence -> isTextChanged)
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
                        disposable -> getDisposableManager().addDisposable(DISPOSABLE_POOL_KEY, disposable));

//        // С какого символа начинаем показывать подсказки
        autoText.setThreshold(3);
//
//        RxTextView.textChanges(autoText)
//                .filter(charSequence -> {
//                    //Данное устловие позволяет защититься от показа подсказки, в тот момент,
//                    // когда пользователь уже выбрал один из вариантов, и перевернул экран
//                    if (isConfigChange) {
//                        isConfigChange = false;
//                        return false;
//                    }
//                    return true;
//                })
//                .subscribe(
//                        next -> isTextChanged = true,
//                        throwable -> showMessage(R.string.unknown_error),
//                        () -> {
//                        },
//                        disposable -> disposableManager.addDisposable(ID_POOL_COMPOSITE_DISPOSABLE, disposable));
//

//
//        RxView.clicks(autoText)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        res -> {
//                            if (autoText.getAdapter().getCount() > 0 && !autoText.isPopupShowing()) {
//                                autoText.showDropDown();
//                            }
//                        },
//                        throwable -> showMessage(R.string.unknown_error),
//                        () -> {
//                        },
//                        disposable -> disposableManager.addDisposable(ID_POOL_COMPOSITE_DISPOSABLE, disposable));
//

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e("SIzeDestr:", String.valueOf(getDisposableManager().testSize(DISPOSABLE_POOL_KEY)));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void showErrorMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    public ActivityAddCityBinding getBinding() {
        return binding;
    }

    private Observable.OnPropertyChangedCallback viewModelObserver =
            new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
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
