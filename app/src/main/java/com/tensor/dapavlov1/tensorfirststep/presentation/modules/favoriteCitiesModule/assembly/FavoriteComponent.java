package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly;

import com.tensor.dapavlov1.tensorfirststep.presentation.common.assembly.PerFavoriteScope;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteViewModelContract;

import dagger.Subcomponent;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

@PerFavoriteScope
@Subcomponent(modules = FavoriteDaggerModule.class)
public interface FavoriteComponent {
    FavoriteViewModelContract.Presenter getPresenter();
    FavoriteViewModelContract.ViewModel getViewModel();

    FavoriteInteractorPresenterContract.Interactor getInteractor();
    FavoriteRouterPresenterContract.Router getRouter();
}
