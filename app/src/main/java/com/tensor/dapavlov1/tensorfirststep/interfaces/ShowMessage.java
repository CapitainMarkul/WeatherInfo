package com.tensor.dapavlov1.tensorfirststep.interfaces;

import android.support.annotation.StringRes;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public interface ShowMessage {
//    void showMessage(String message);
    void showMessage(@StringRes int message);
    void errorMessage(@StringRes int message);
}
