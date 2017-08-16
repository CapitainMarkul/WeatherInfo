package com.tensor.dapavlov1.tensorfirststep.interfaces;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public interface Router {
    void goToNewActivity(@NotNull Activity fromActivity, @NotNull Class toActivity);
}
