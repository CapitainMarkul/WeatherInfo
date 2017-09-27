package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

@Parcel
public class CityView {
    private String name;
    private String lastTimeUpdate;
    private List<WeatherView> weatherViews;
    private boolean isFavorite;


    @ParcelConstructor
    public CityView(String name, String lastTimeUpdate, List<WeatherView> weatherViews, boolean isFavorite) {
        this.name = name;
        this.lastTimeUpdate = lastTimeUpdate;
        this.weatherViews = weatherViews;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public String getLastTimeUpdate() {
        return lastTimeUpdate;
    }

    public List<WeatherView> getWeatherViews() {
        return weatherViews;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
