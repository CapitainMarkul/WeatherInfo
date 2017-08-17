package com.tensor.dapavlov1.tensorfirststep.presentation.routers;

import android.app.Activity;
import android.content.Intent;

import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;

import org.jetbrains.annotations.NotNull;

/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class RouterToAddCity implements Router {
//    @Override
//    public void goToNewActivity(@NotNull Activity favoriteActivity) {
//        Intent intent = new Intent(favoriteActivity, AddCityActivity.class);
//        favoriteActivity.startActivity(intent);
//    }

    @Override
    public void goToNewActivity(@NotNull Activity fromActivity, @NotNull Class toActivity) {
        fromActivity.startActivity(new Intent(fromActivity, toActivity));
    }
}
