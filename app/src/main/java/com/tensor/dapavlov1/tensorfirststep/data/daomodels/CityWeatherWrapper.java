package com.tensor.dapavlov1.tensorfirststep.data.daomodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

//Класс обертка
public class CityWeatherWrapper implements Parcelable {
    private CityDb cityDb;
    private List<WeatherDb> weathers;

    public CityWeatherWrapper(CityDb cityDb, List<WeatherDb> weathers) {
        this.cityDb = cityDb;
        this.weathers = weathers;
    }

    protected CityWeatherWrapper(Parcel in) {
        weathers = in.createTypedArrayList(WeatherDb.CREATOR);
        cityDb = in.readParcelable(CityDb.class.getClassLoader());
    }

    public static final Creator<CityWeatherWrapper> CREATOR = new Creator<CityWeatherWrapper>() {
        @Override
        public CityWeatherWrapper createFromParcel(Parcel in) {
            return new CityWeatherWrapper(in);
        }

        @Override
        public CityWeatherWrapper[] newArray(int size) {
            return new CityWeatherWrapper[size];
        }
    };

    public CityDb getCityDb() {
        return cityDb;
    }

    public List<WeatherDb> getWeathers() {
        return weathers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(weathers);
        parcel.writeParcelable(cityDb, i);
    }
}
