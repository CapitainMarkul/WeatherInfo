package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router.CommonRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;


/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class FavoriteRouter extends CommonRouter<FavoriteRouterPresenterContract.Presenter>
        implements FavoriteRouterPresenterContract.Router {

//    private static final int UPDATE_INFO_REQUEST = 200;
//    public final static String ADD_NEW_CITY_ACTION = FavoriteRouter.class.getSimpleName() + ".ACTION.ADD_NEW_CITY";

    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity) {
        previousActivity.startActivity(new Intent(previousActivity, nextActivity));
    }

    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity, int requestCode) {
//        Intent resultIntent = new Intent(previousActivity, nextActivity);
//        resultIntent.setAction(ADD_NEW_CITY_ACTION);

        previousActivity.startActivityForResult(new Intent(previousActivity, nextActivity), requestCode);
    }
}
