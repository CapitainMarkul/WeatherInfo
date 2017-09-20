package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter;

import com.tensor.dapavlov1.tensorfirststep.presentation.common.ActivityComponents;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.MvpViewModel;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public interface MvpPresenter<ViewModel extends MvpViewModel> {
    void attachView(ViewModel viewModel, ActivityComponents activityComponents);

    void detachView();

    ViewModel getViewModel();

//    Activity getActivity();

    void destroy();
}
