package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router.CommonRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;


/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class FavoriteRouter extends CommonRouter<FavoriteRouterPresenterContract.Presenter>
        implements FavoriteRouterPresenterContract.Router {

    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity) {
        previousActivity.startActivity(new Intent(previousActivity, nextActivity));
    }

    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity, int requestCode) {
        previousActivity.startActivityForResult(new Intent(previousActivity, nextActivity), requestCode);
    }
}
