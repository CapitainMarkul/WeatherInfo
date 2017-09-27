package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */
@Parcel
public class WeatherView {
    private String windShort;
    private double windSpeed;
    private double pressure;
    private double temperature;
    private String date;
    private String time;
    private String iconUrl;
    private String iconCode;
    private String description;

    @ParcelConstructor
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

    @BindingAdapter({"bind:iconUrl", "bind:errorImage"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).into(view);
    }
}
