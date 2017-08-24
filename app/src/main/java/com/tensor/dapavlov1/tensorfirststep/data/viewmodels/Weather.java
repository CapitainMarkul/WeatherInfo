package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class Weather implements Parcelable{
    private String windShort;
    private double windSpeed;
    private double pressure;
    private double temperature;
    private String date;
    private String time;
    private String iconUrl;
    private String iconCode;
    private String description;

    public Weather(String windShort, double windSpeed, double pressure, double temperature, String date, String time, String iconUrl, String iconCode, String description) {
        this.windShort = windShort;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.temperature = temperature;
        this.date = date;
        this.time = time;
        this.iconUrl = iconUrl;
        this.iconCode = iconCode;
        this.description = description;
    }

    protected Weather(Parcel in) {
        windShort = in.readString();
        windSpeed = in.readDouble();
        pressure = in.readDouble();
        temperature = in.readDouble();
        date = in.readString();
        time = in.readString();
        iconUrl = in.readString();
        iconCode = in.readString();
        description = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public String getWindShort() {
        return windShort;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getIconCode() {
        return iconCode;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(windShort);
        parcel.writeDouble(windSpeed);
        parcel.writeDouble(pressure);
        parcel.writeDouble(temperature);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(iconUrl);
        parcel.writeString(iconCode);
        parcel.writeString(description);
    }
}
