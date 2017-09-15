package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class CityView implements Parcelable {
    private String name;
    private String lastTimeUpdate;
    private List<WeatherView> weatherViews;
    private boolean isFavorite;


    public CityView(String name, String lastTimeUpdate, List<WeatherView> weatherViews, boolean isFavorite) {
        this.name = name;
        this.lastTimeUpdate = lastTimeUpdate;
        this.weatherViews = weatherViews;
        this.isFavorite = isFavorite;
    }

    protected CityView(Parcel in) {
        name = in.readString();
        lastTimeUpdate = in.readString();
        weatherViews = in.createTypedArrayList(WeatherView.CREATOR);
        isFavorite = in.readByte() != 0;
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

    public void setWeatherViews(List<WeatherView> weatherViews) {
        this.weatherViews = weatherViews;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public static final Creator<CityView> CREATOR = new Creator<CityView>() {
        @Override
        public CityView createFromParcel(Parcel in) {
            return new CityView(in);
        }

        @Override
        public CityView[] newArray(int size) {
            return new CityView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(lastTimeUpdate);
        parcel.writeTypedList(weatherViews);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
