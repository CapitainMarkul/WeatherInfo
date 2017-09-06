package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import java.util.List;

/**
 * Created by Dmitry on 07.06.2017.
 */

public class PlacesAutoComplete extends ArrayAdapter<String> implements Filterable {
    private Context context;
    private int resource;

    public PlacesAutoComplete(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    public ArrayAdapter<String> setItems(List<String> list) {
        return new ArrayAdapter<>(context, resource, list);
    }
}
