package com.tensor.dapavlov1.tensorfirststep.data.daomodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

//Класс обертка
public class ModelCityWeather implements Parcelable {
    private DbCity dbCity;
    private List<DbWeather> weathers;

    public ModelCityWeather(DbCity dbCity, List<DbWeather> weathers) {
        this.dbCity = dbCity;
        this.weathers = weathers;
    }

    protected ModelCityWeather(Parcel in) {
        weathers = in.createTypedArrayList(DbWeather.CREATOR);
        dbCity = in.readParcelable(DbCity.class.getClassLoader());
    }

    public static final Creator<ModelCityWeather> CREATOR = new Creator<ModelCityWeather>() {
        @Override
        public ModelCityWeather createFromParcel(Parcel in) {
            return new ModelCityWeather(in);
        }

        @Override
        public ModelCityWeather[] newArray(int size) {
            return new ModelCityWeather[size];
        }
    };

    public DbCity getDbCity() {
        return dbCity;
    }

    public List<DbWeather> getWeathers() {
        return weathers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(weathers);
        parcel.writeParcelable(dbCity, i);
    }
}
