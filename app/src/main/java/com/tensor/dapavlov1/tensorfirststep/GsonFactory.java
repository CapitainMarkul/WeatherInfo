package com.tensor.dapavlov1.tensorfirststep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonCity;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonPlace;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonPlaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonFactory {
    private static GsonFactory instance;

    private GsonFactory() {
    }

    public static GsonFactory getInstance() {
        if (instance == null) {
            instance = new GsonFactory();
        }
        return instance;
    }

    private GsonBuilder gsonBuilder;
    private Gson gsonReader;

    public GsonCity createGsonCityModel(String responceJson) {
        initGson();
        GsonCity gsonCity = gsonReader.fromJson(responceJson, GsonCity.class);
        gsonCity.setDateLastUpdate(TrimDateSingleton.getInstance().getNowTime());
        return gsonCity;
    }

    public List<String> getPlacesName(String responceJson) {
        initGson();
        List<String> resultTitles = new ArrayList<>();
        List<GsonPlace> gsonPlaces = gsonReader.fromJson(responceJson, GsonPlaces.class).getGsonPlaces();
        for (GsonPlace item : gsonPlaces) {
            resultTitles.add(
                    item.getTitlePlace()
            );
        }
        return resultTitles;
    }

    private void initGson() {
        gsonBuilder = new GsonBuilder();
        gsonReader = gsonBuilder.create();
    }
}
