package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.StringRes;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public abstract class AbstractViewModel extends BaseObservable {

    private int errorMessage;
    private int successMessage;

    @Bindable
    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    @Bindable
    public int getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(int successMessage) {
        this.successMessage = successMessage;
        notifyPropertyChanged(BR.successMessage);
    }

    // TODO: 13.09.2017 ласс можно будет расширить
//    @Override
//    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        super.addOnPropertyChangedCallback(callback);
//    }
//
//    @Override
//    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
//        super.removeOnPropertyChangedCallback(callback);
//    }
}
