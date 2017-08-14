package com.tensor.dapavlov1.tensorfirststep.data.GsonModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonPlaces {
    @SerializedName("predictions") List<GsonPlace> gsonPlaces;

    public List<GsonPlace> getGsonPlaces() {
        return gsonPlaces;
    }
}
