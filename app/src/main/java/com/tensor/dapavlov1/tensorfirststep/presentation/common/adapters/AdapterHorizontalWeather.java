package com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.R;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;

/**
 * Created by da.pavlov1 on 04.08.2017.
 */

public class AdapterHorizontalWeather extends RecyclerView.Adapter<AdapterHorizontalWeather.WeatherItemViewHolder> {

    private List<Weather> weatherNow;

    public void setItems(@NotNull final List<Weather> weathers){
        this.weatherNow = weathers;
    }

    @Override
    public WeatherItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeatherItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(WeatherItemViewHolder holder, int position) {
//        holder.temperature.setText(String.valueOf(
//                weatherNow.get(position).getTemperature()));
//        holder.time.setText(weatherNow.get(position).getTime());
//        holder.date.setText(weatherNow.get(position).getDate());
//
//        Picasso.with(App.getContext())
//                .load(weatherNow.get(position).getIconUrl())
//                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return (weatherNow != null) ? weatherNow.size() : 0;
    }

    public class WeatherItemViewHolder extends RecyclerView.ViewHolder {
//        private Binding binding;

        public WeatherItemViewHolder(View itemView) {
            super(itemView);
//            binding = DataBindingUtil.bind(itemView);
        }
    }
}
