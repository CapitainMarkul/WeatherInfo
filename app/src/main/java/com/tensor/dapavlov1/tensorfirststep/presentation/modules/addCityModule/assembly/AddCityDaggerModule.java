package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly;

import com.tensor.dapavlov1.tensorfirststep.presentation.common.assembly.PerAddCityScope;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.interactor.AddCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.presenter.AddCityPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel.AddCityViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

@Module
public class AddCityDaggerModule {

    @Provides
    @PerAddCityScope
    AddCityViewModelContract.Presenter provideAddCityPresenter(AddCityPresenter addCityPresenter) {
        return addCityPresenter;
    }

    @Provides
    @PerAddCityScope
    AddCityViewModelContract.ViewModel provideAddCityViewModel() {
        return new AddCityViewModel();
    }

    @Provides
    @PerAddCityScope
    AddCityInteractorPresenterContract.Interactor provideAddCityInteractor() {
        return new AddCityInteractor();
    }
}
