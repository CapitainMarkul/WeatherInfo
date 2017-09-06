package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class WeatherView implements Parcelable {
    private String windShort;
    private double windSpeed;
    private double pressure;
    private double temperature;
    private String date;
    private String time;
    private String iconUrl;
    private String iconCode;
    private String description;

    public WeatherView(String windShort, double windSpeed, double pressure, double temperature, String date, String time, String iconUrl, String iconCode, String description) {
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

    protected WeatherView(Parcel in) {
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

    public static final Creator<WeatherView> CREATOR = new Creator<WeatherView>() {
        @Override
        public WeatherView createFromParcel(Parcel in) {
            return new WeatherView(in);
        }

        @Override
        public WeatherView[] newArray(int size) {
            return new WeatherView[size];
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

    @BindingAdapter({"bind:iconUrl", "bind:errorImage"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).into(view);
    }
}
