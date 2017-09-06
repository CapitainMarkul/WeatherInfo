package com.tensor.dapavlov1.tensorfirststep.presentation.routers;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;


/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class FavoriteToAddCityRouter implements Router {
    @Override
    public void goToActivity(@NonNull Activity previousActivity, @NonNull Class nextActivity) {
        previousActivity.startActivity(new Intent(previousActivity, nextActivity));
    }
}
