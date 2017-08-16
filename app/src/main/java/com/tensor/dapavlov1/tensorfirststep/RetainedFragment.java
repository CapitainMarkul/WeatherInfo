package com.tensor.dapavlov1.tensorfirststep;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class RetainedFragment extends Fragment {
    private Bundle savedInstanceState = new Bundle();
    private Map<String, Object> mapSavedState = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setDataInMap(String key, Object value) {
        mapSavedState.put(key, value);
    }

    public Object getDataFromMap(String key) {
        return mapSavedState.get(key);
    }

    public void setParcelableData(String key, Parcelable data) {
        savedInstanceState.putParcelable(key, data);
    }

    public Object getData(String key) {
        return savedInstanceState.get(key);
    }
}
