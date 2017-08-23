package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class City implements Parcelable {
    private String name;
    private String lastTimeUpdate;
    private List<Weather> weathers;

    public City(String name, String lastTimeUpdate, List<Weather> weathers) {
        this.name = name;
        this.lastTimeUpdate = lastTimeUpdate;
        this.weathers = weathers;
    }

    protected City(Parcel in) {
        name = in.readString();
        lastTimeUpdate = in.readString();
        weathers = in.createTypedArrayList(Weather.CREATOR);
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

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
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
        parcel.writeTypedList(weathers);
    }
}
