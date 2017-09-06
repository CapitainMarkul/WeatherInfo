package com.tensor.dapavlov1.tensorfirststep;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 21.08.2017.
 */

public class BaseLoader extends Loader<Map<String, Object>> {
    private Map<String, Object> rootMap = new HashMap<>();

    public BaseLoader(Context context, Map<String, Object> values) {
        super(context);
        rootMap = values;
    }

    @Override
    protected void onStartLoading() {
        super.deliverResult(rootMap);
    }
}
