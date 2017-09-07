package com.tensor.dapavlov1.tensorfirststep.data.daomodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

@Entity(nameInDb = "Weather")
public class WeatherDb implements Parcelable {
    @Id
    private Long id;

    @NotNull
    private String windShort;
    @NotNull
    private double windSpeed;

    @NotNull
    private double pressure;
    @NotNull
    private double temperature;

    @NotNull
    private String date;
    @NotNull
    private String time;

    @NotNull
    private String iconUrl;
    @NotNull
    private String iconCode;
    @NotNull
    private String description;

    @NotNull
    private Long cityId;

    public WeatherDb(@NotNull String windShort, double windSpeed,
                     double pressure, double temperature, @NotNull String date,
                     @NotNull String time, @NotNull String iconUrl, @NotNull String iconCode,
                     @NotNull String description) {
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

    protected WeatherDb(Parcel in) {
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

    @Generated(hash = 1791781359)
    public WeatherDb(Long id, @NotNull String windShort, double windSpeed, double pressure,
            double temperature, @NotNull String date, @NotNull String time,
            @NotNull String iconUrl, @NotNull String iconCode, @NotNull String description,
            @NotNull Long cityId) {
        this.id = id;
        this.windShort = windShort;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.temperature = temperature;
        this.date = date;
        this.time = time;
        this.iconUrl = iconUrl;
        this.iconCode = iconCode;
        this.description = description;
        this.cityId = cityId;
    }

    @Generated(hash = 273700809)
    public WeatherDb() {
    }


    public static final Creator<WeatherDb> CREATOR = new Creator<WeatherDb>() {
        @Override
        public WeatherDb createFromParcel(Parcel in) {
            return new WeatherDb(in);
        }

        @Override
        public WeatherDb[] newArray(int size) {
            return new WeatherDb[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeLong(id);
        parcel.writeString(windShort);
        parcel.writeDouble(windSpeed);
        parcel.writeDouble(pressure);
        parcel.writeDouble(temperature);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(iconUrl);
        parcel.writeString(iconCode);
        parcel.writeString(description);
//        parcel.writeLong(cityId);

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWindShort() {
        return this.windShort;
    }

    public void setWindShort(String windShort) {
        this.windShort = windShort;
    }

    public double getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconCode() {
        return this.iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
