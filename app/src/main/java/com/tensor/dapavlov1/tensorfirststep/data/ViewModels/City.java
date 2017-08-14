package com.tensor.dapavlov1.tensorfirststep.data.ViewModels;

import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class City {
    private String name;
    private String lastTimeUpdate;
    private List<Weather> weathers;

    public City(String name, String lastTimeUpdate, List<Weather> weathers) {
        this.name = name;
        this.lastTimeUpdate = lastTimeUpdate;
        this.weathers = weathers;
    }

    public String getName() {
        return name;
    }

    public String getLastTimeUpdate() {
        return lastTimeUpdate;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }
}
