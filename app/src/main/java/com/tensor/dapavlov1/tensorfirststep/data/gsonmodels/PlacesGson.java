package com.tensor.dapavlov1.tensorfirststep.data.gsonmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class PlacesGson {
    @SerializedName("predictions") List<PlaceGson> placeGsons;

    public List<PlaceGson> getPlaceGsons() {
        return placeGsons;
    }
}
