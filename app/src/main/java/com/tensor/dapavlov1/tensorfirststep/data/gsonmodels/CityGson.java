package com.tensor.dapavlov1.tensorfirststep.data.gsonmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class CityGson {
    @SerializedName("city_name") String name;
    @SerializedName("data") List<WeatherRootGson> weathers;

    // ateLastUpdate не приходит в responceJson. (в ответе приходит время с промежутком в 3 часа т.е. (15:00, 18:00 и т.д.).
    //Для пользователя я показываю точное время (в промежутке этих 3 часов)
    // т.е. 15:42 и т.д. Поэтому создал свое поле, для более удобного маппинга в будущем )
    private String dateLastUpdate = "";

    public String getDateLastUpdate() {
        return dateLastUpdate;
    }

    public void setDateLastUpdate(String dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getName() {
        return name;
    }

    public List<WeatherRootGson> getWeathers() {
        return weathers;
    }
}
