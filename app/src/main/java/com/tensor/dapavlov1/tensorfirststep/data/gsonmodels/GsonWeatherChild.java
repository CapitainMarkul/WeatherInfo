package com.tensor.dapavlov1.tensorfirststep.data.gsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonWeatherChild {
    @SerializedName("icon") String icon;
    @SerializedName("description") String description;

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
