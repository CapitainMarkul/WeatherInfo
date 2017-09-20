package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly;

import com.tensor.dapavlov1.tensorfirststep.presentation.common.assembly.PerFavoriteScope;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor.FavoriteCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router.FavoriteRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel.FavoriteViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

@Module
public class FavoriteDaggerModule {

    @Provides
    @PerFavoriteScope
    FavoriteViewModelContract.Presenter provideFavoritePresetner(FavoritePresenter favoritePresenter) {
        return favoritePresenter;
    }

    @Provides
    @PerFavoriteScope
    FavoriteViewModelContract.ViewModel provideFavoriteViewModel() {
        return new FavoriteViewModel();
    }

    @Provides
    @PerFavoriteScope
    FavoriteInteractorPresenterContract.Interactor provideFavoriteInteractor() {
        return new FavoriteCityInteractor();
    }

    @Provides
    @PerFavoriteScope
    FavoriteRouterPresenterContract.Router provideFavoriteRouter() {
        return new FavoriteRouter();
    }
}
