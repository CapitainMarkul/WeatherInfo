package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper;

import android.support.annotation.Nullable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class ResultWrapper<Type> {
    private Type data;
    private Exception error;

    public ResultWrapper() {
    }

    public ResultWrapper(Type data, Exception error) {
        this.data = data;
        this.error = error;
    }

    public void setData(Type data) {
        this.data = data;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    @Nullable
    public Type getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }
}