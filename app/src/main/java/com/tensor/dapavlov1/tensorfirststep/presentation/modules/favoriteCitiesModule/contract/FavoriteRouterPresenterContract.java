package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router.MvpRouter;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public interface FavoriteRouterPresenterContract {
    interface Presenter extends MvpRouter.Listener {
    }

    interface Router extends MvpRouter<Presenter> {
        void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity);
        void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity, int requestCode);
    }
}
