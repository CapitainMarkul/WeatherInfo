package com.tensor.dapavlov1.tensorfirststep.interfaces;

import android.app.Activity;
import android.support.annotation.NonNull;


/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public interface Router {
    void goToNewActivity(@NonNull Activity fromActivity, @NonNull Class toActivity);
}
