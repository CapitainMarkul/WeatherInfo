package com.tensor.dapavlov1.tensorfirststep.presentation.common;

import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.MvpViewModel;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public abstract class BasePresenter<ViewModel extends MvpViewModel>
        implements MvpPresenter<ViewModel> {

    @Inject Lazy<WeatherService> weatherService;

    public WeatherService getWeatherService() {
        return weatherService.get();
    }

    private ViewModel viewModel;
    private ActivityComponents activityComponents;
    private DisposableManager disposableManager = DisposableManager.getInstance();

    @Override
    public void attachView(ViewModel viewModel, ActivityComponents activityComponents) {
        this.activityComponents = activityComponents;
        this.viewModel = viewModel;
    }

    @Override
    public void detachView() {
        activityComponents = null;
//        viewModel = null;
    }

    //    @Override
    public ActivityComponents getActivity() {
        return activityComponents;
    }

    @Override
    public ViewModel getViewModel() {
        return viewModel;
    }

    protected DisposableManager getDisposableManager() {
        return disposableManager;
    }
}
