package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly;

import com.tensor.dapavlov1.tensorfirststep.presentation.common.assembly.PerAddCityScope;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityViewModelContract;

import dagger.Subcomponent;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

@PerAddCityScope
@Subcomponent(modules = AddCityDaggerModule.class)
public interface AddCityComponent {
    AddCityViewModelContract.Presenter getPresenter();

    AddCityViewModelContract.ViewModel getViewModel();

    AddCityInteractorPresenterContract.Interactor getInteractor();
}
