package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

public interface MvpViewModel extends Observable {

    @Bindable
    int getErrorMessage();
    void setErrorMessage(int errorMessage);

    @Bindable
    int getSuccessMessage();
    void setSuccessMessage(int successMessage);
}
