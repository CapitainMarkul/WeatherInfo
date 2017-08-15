package com.tensor.dapavlov1.tensorfirststep.data.gsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonPlace {
    @SerializedName("description") String titlePlace;

    public String getTitlePlace() {
        return titlePlace;
    }
}
