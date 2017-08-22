package com.tensor.dapavlov1.tensorfirststep.presentation.common;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public abstract class BasePresenter<T extends AppCompatActivity> {
    public T activity;

    public void attachActivity(T activity) {
        this.activity = activity;
    }

    public void detachActivity() {
        activity = null;
    }
}
