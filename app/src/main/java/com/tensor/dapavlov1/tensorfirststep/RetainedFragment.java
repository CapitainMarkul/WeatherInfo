package com.tensor.dapavlov1.tensorfirststep;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class RetainedFragment extends Fragment {
    private Bundle savedInstanceState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    public void setData(Bundle data) {
        savedInstanceState = data;
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }
}
