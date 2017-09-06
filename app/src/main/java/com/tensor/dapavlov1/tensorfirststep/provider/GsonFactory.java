package com.tensor.dapavlov1.tensorfirststep.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.CityGson;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.PlaceGson;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.PlacesGson;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimDateSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonFactory {
    public static GsonFactory getInstance() {
        return GsonFactoryLoader.INSTANCE;
    }

    private static final class GsonFactoryLoader {
        private static final GsonFactory INSTANCE = new GsonFactory();
    }

    private GsonFactory() {
    }

    private GsonBuilder gsonBuilder;
    private Gson gsonReader;

    public CityGson createGsonCityModel(String responceJson) {
        initGson();
        CityGson cityGson = gsonReader.fromJson(responceJson, CityGson.class);
        cityGson.setDateLastUpdate(TrimDateSingleton.getInstance().getNowTime());

        return cityGson;
    }

    public List<String> getPlacesName(String responceJson) {
        initGson();
        List<String> resultTitles = new ArrayList<>();
        List<PlaceGson> placeGsons = gsonReader.fromJson(responceJson, PlacesGson.class).getPlaceGsons();
        for (PlaceGson item : placeGsons) {
            resultTitles.add(
                    item.getTitlePlace());
        }
        return resultTitles;
    }

    private void initGson() {
        gsonBuilder = new GsonBuilder();
        gsonReader = gsonBuilder.create();
    }
}
