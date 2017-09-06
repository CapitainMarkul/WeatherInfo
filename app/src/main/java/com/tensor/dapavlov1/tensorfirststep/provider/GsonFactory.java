package com.tensor.dapavlov1.tensorfirststep.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.CityGson;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.PlaceGson;
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

    public CityGson createGsonCityModel(String responseJson) {
        CityGson cityGson = new Gson().fromJson(responseJson, CityGson.class);
        cityGson.setDateLastUpdate(TrimDateSingleton.getInstance().getNowTime());
        return cityGson;
    }

    public List<String> getPlacesName(String responseJson) {
        List<String> resultTitles = new ArrayList<>();

        JsonArray jsonArray = new JsonParser().parse(responseJson).getAsJsonObject().get("predictions").getAsJsonArray();
        for (JsonElement item : jsonArray) {
            resultTitles.add(new Gson().fromJson(item, PlaceGson.class).getTitlePlace());
        }
        return resultTitles;
    }
}
