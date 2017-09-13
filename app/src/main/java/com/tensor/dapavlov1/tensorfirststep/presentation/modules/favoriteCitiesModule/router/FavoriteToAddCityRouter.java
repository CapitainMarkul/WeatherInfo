package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router.CommonRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityRouterPresenterContract;


/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class FavoriteToAddCityRouter extends CommonRouter<FavoriteCityRouterPresenterContract.Presenter>
        implements FavoriteCityRouterPresenterContract.Router {

    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity) {
        previousActivity.startActivity(new Intent(previousActivity, nextActivity));
    }
}
