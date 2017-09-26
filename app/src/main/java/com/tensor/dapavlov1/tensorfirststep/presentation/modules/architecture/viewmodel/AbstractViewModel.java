package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel;

import android.databinding.BaseObservable;

import com.tensor.dapavlov1.tensorfirststep.BR;


/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public abstract class AbstractViewModel extends BaseObservable implements MvpViewModel {

    private int errorMessage;
    private int successMessage;

    @Override
    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    @Override
    public int getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(int successMessage) {
        this.successMessage = successMessage;
        notifyPropertyChanged(BR.successMessage);
    }
}
