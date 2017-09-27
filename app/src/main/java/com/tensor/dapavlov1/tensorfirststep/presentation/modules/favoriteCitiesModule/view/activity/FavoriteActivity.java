package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityFavoriteBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.EmptyListener;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BaseActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper.RecyclerAdapterStorage;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly.FavoriteComponent;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly.FavoriteDaggerModule;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.adapter.FavoriteAdapter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelItemListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel.FavoriteViewModel;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends BaseActivity<FavoriteViewModelContract.ViewModel, FavoriteViewModelContract.Presenter>
        implements DelItemListener, EmptyListener {

    private FavoriteAdapter favoriteAdapter;

    public static final String FAVORITE_CITY_ADAPTER_KEY = FavoriteActivity.class.getSimpleName() + "_ADAPTER";
    public static final String DISPOSABLE_POOL_KEY = FavoriteActivity.class.getSimpleName() + "_DISPOSABLE";

    public static final int UPDATE_INFO_REQUEST = 200;

    public enum WaysUpdatePriority {
        NETWORK, LOCAL
    }
//    public static final String ADD_NEW_CITY_ACTION = FavoriteActivity.class.getSimpleName() + ".ACTION.ADD_NEW_CITY";


    private ActivityFavoriteBinding binding;
    private FavoriteComponent favoriteComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        binding.setViewModel(getViewModel());
        binding.setPresenter(getPresenter());

        favoriteAdapter = createRecyclerAdapter();
        setupRecyclerView();
        setupListeners();
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
        RecyclerAdapterStorage.getInstance().saveAdapter(FAVORITE_CITY_ADAPTER_KEY, favoriteAdapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        FavoriteAdapter tempAdapter = RecyclerAdapterStorage.getInstance().restoreAdapter(FAVORITE_CITY_ADAPTER_KEY);
        favoriteAdapter =
                tempAdapter != null ? tempAdapter : createRecyclerAdapter();

        setupRecyclerView();
    }

    @Override
    protected void inject() {
        favoriteComponent = App.get()
                .presentationComponents()
                .favoriteComponent(new FavoriteDaggerModule());
    }

    @Override
    protected FavoriteViewModelContract.Presenter createPresenter() {
        return favoriteComponent.getPresenter();
    }

    @Override
    protected FavoriteViewModelContract.ViewModel createViewModel() {
        return favoriteComponent.getViewModel();
    }

    private Observable.OnPropertyChangedCallback viewModelObserver = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            FavoriteViewModel viewModel = (FavoriteViewModel) sender;
            if (viewModel.isResetAdapter()) {
                favoriteAdapter.setDefaultSetting();
                getViewModel().setResetAdapter(false);
            }
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
                case BR.dbIsUpdate: {
                    showMessageWithAction(R.string.activity_favorite_update_success);
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
    public void showMessageWithAction(@StringRes int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.activity_favorite_update_request, view -> getPresenter().updateWeathers(WaysUpdatePriority.LOCAL))
                .show();
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

    private void startUpdateWeatherInfo() {
        favoriteAdapter.setDefaultSetting();
        getPresenter().updateWeathers(WaysUpdatePriority.NETWORK);
    }

    private void setupListeners() {
        binding.srRefresh.setOnRefreshListener(() -> startUpdateWeatherInfo());
    }

    public void setItemInAdapter(CityView cityViewWeather) {
        favoriteAdapter.setItem(cityViewWeather);
        //для работы анимации на 1 элементе
        binding.recyclerViewFavorite.scrollToPosition(favoriteAdapter.getItemCount());
    }

    private FavoriteAdapter createRecyclerAdapter() {
        return new FavoriteAdapter().setListener(this);
    }

    private void setupRecyclerView() {
        binding.recyclerViewFavorite.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(favoriteAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            getDisposableManager().disposeAll(DISPOSABLE_POOL_KEY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_INFO_REQUEST && resultCode != RESULT_CANCELED) {
            switch (data.getIntExtra(AddCityActivity.GET_STATE, 0)) {
                case AddCityActivity.INFO_IS_CHANGE_STATE: {
                    startUpdateWeatherInfo();
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void delCityFromAdapter(CityView deletedCity) {
        int deletedPosition = favoriteAdapter.getItems().indexOf(deletedCity);
        favoriteAdapter.getItems().remove(deletedPosition);
        favoriteAdapter.notifyItemRemoved(deletedPosition);
        if (favoriteAdapter.getItems().isEmpty()) {
            onEmpty();
        }
    }

    @Override
    public void onEmpty() {
        getViewModel().showEmptyResult();
    }
}
