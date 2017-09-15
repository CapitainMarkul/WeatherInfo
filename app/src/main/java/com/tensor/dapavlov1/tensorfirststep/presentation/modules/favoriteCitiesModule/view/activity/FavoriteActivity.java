package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityFavoriteBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.EmptyListener;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BaseActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper.AdapterStorage;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.adapter.FavoriteAdapter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelItemListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel.FavoriteViewModel;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends BaseActivity<FavoriteViewModel, FavoritePresenter>
        implements DelItemListener, EmptyListener {

    private FavoriteAdapter favoriteAdapter;
    private CheckUpdateInOtherActivity checkUpdateInOtherActivity = CheckUpdateInOtherActivity.getInstance();

    public static final String FAVORITE_CITY_ADAPTER_KEY = FavoriteActivity.class.getSimpleName() + "_ADAPTER";

    private ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        binding.setViewModel(getViewModel());
        binding.setPresenter(getPresenter());

        setupRecyclerAdapter();
        setupRecyclerView();
        setupListeners();

        if(savedInstanceState == null){
            startUpdateWeatherInfo();
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
        getViewModel().removeOnPropertyChangedCallback(viewModelObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AdapterStorage.getInstance().saveAdapter(FAVORITE_CITY_ADAPTER_KEY, favoriteAdapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        favoriteAdapter =
                AdapterStorage.getInstance().restoreAdapter(FAVORITE_CITY_ADAPTER_KEY);
        setupRecyclerView();
    }

    @Override
    protected FavoritePresenter createPresenter() {
        return new FavoritePresenter();
    }

    @Override
    protected FavoriteViewModel createViewModel() {
        return new FavoriteViewModel();
    }


    private Observable.OnPropertyChangedCallback viewModelObserver = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            FavoriteViewModel viewModel = (FavoriteViewModel) sender;
            switch (propertyId) {
                case BR.citiesView: {
                    if (favoriteAdapter != null && viewModel.getLastCity() != null) {
                        setItemInAdapter(viewModel.getLastCity());
                    }
                    break;
                }
                case BR.lastDeletedCity: {
                    delCityFromAdapter(viewModel.getLastDeletedCity());
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
                default:
                    break;
            }
            binding.setViewModel(viewModel);
        }
    };


    @Override
    public void showMessage(@StringRes int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(@StringRes int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Activity getComponentsActivity() {
        return this;
    }

    @Override
    public void onItemClick(CityView city) {
        getPresenter().deleteCity(city);
    }


//    private void checkUpdateInOtherActivity() {
//        if (checkUpdateInOtherActivity.isUpdate()) {
//            startUpdateWeatherInfo();
//            checkUpdateInOtherActivity.setUpdate(false);
//        }
//    }

    //
//

    private void startUpdateWeatherInfo() {
        favoriteAdapter.setDefaultSetting();
        getPresenter().updateWeathers();
    }

    private void setupListeners() {
        binding.srRefresh.setOnRefreshListener(() -> startUpdateWeatherInfo());
    }

    //
//    @Override
//    public void setItems(final List<CityView> weathers) {
//        favoriteAdapter.setItems(weathers);
//    }
//
    public void setItemInAdapter(CityView cityViewWeather) {
        favoriteAdapter.setItem(cityViewWeather);
        //для работы анимации на 1 элементе
        binding.recyclerViewFavorite.scrollToPosition(favoriteAdapter.getItemCount());
    }

    private void setupRecyclerAdapter() {
        favoriteAdapter = new FavoriteAdapter();
        favoriteAdapter.setListener(this);
//        favoriteAdapter.setEmptyListener(FavoriteActivity.this);
    }

    private void setupRecyclerView() {
        binding.recyclerViewFavorite.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(favoriteAdapter);
        // FIXME: 15.09.2017 Здесь убрать настройки видимости
        binding.recyclerViewFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AdapterStorage.getInstance().restoreAdapter(FAVORITE_CITY_ADAPTER_KEY);
    }

    public void delCityFromAdapter(CityView deletedCity) {
        int deletedPosition = favoriteAdapter.getItems().indexOf(deletedCity);
        favoriteAdapter.getItems().remove(deletedPosition);
        favoriteAdapter.notifyItemRemoved(deletedPosition);
        // TODO: 13.09.2017 Подумать насчет анимации
//        favoriteAdapter.notifyItemRangeChanged(deletedPosition, favoriteAdapter.getItemCount());
//        favoriteAdapter.setAnimation(binding.cardFullInfo, position, R.anim.recyclerdel);
        if (favoriteAdapter.getItems().isEmpty()) {
            onEmpty();
        }
    }
    //
//    @Override
//    public Loader onCreateLoader(int id, Bundle args) {
//        setupPresenter();
//        setupRecyclerAdapter();
//        setupRecyclerView();
//
//        //Ситуация, на другом экране делаем Terminate, потом добавляем город в избранное.
//        // Возвращаемся и получаем, что лоадер создается новый и вызывает этот метод
//        // + были изменения на другом экране, следовательно метод checkUpdateInOtherActivity() тоже начнет обновление списка.
//        // В итоге мы получаем дублирующиеся результаты.
//        // Решение: если был Terminate, не обновляем информацию при создании Лоадера
//        if (!checkUpdateInOtherActivity.isUpdate()) {
//            startUpdateWeatherInfo();
//        }
//        return new BaseLoader(getBaseContext(), createConfigMap());
//    }
//

    //
//    @Override
//    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> dataMap) {
//        mPresenter = (FavoritePresenter) dataMap.get(FAVORITE_PRESENTER);
//
//        binding.setMPresenter(mPresenter);
//
//        //восстаансливаем прошлый адаптер
//        favoriteAdapter = (FavoriteAdapter) dataMap.get(FAVORITE_ADAPTER);
//        setupRecyclerView();
//
//        mPresenter.attachActivity(this);
//
//        //если ответ от сервера уже пришел, то показываем результат
//        if (mPresenter.isLoadingComplete()) {
//            favoriteAdapter.setAnimate(isChangingConfigurations());
//            mPresenter.showCachedCities();
//        } else {
//            if (binding.getCitiesView() != null && binding.getCityView() != null) {
//                binding.setLoading(true);
//            } else {
//                if (binding.cardWeatherDefault.cvDefault.getVisibility() == View.VISIBLE) {
//                    binding.setLoading(true);
//                }
//            }
//        }
//
//
//    }

    @Override
    public void onEmpty() {
        getViewModel().reset();
    }
}
