package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerEmptyListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.ViewHolder> {

    private List<City> cityWeathers;
    private RecyclerViewItemClickListener listener;
    private RecyclerEmptyListener emptyListener;

    public void setListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setEmptyListener(RecyclerEmptyListener listener) {
        emptyListener = listener;
    }

    public void setItems(List<City> cityWeathers) {
        this.cityWeathers = cityWeathers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterFavorite.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_full_info_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int WEATHER_NOW = App.getContext().getResources().getInteger(R.integer.weather_now);

        City modelCity = cityWeathers.get(position);
        Weather weatherNow = cityWeathers.get(position).getWeathers().get(WEATHER_NOW);
        List<Weather> modelWeathers = cityWeathers.get(position).getWeathers();

        holder.cityNameInCard.setText(modelCity.getName());
        holder.time.setText(modelCity.getLastTimeUpdate());
        holder.temperature.setText(String.valueOf(weatherNow.getTemperature()));
        holder.description.setText(weatherNow.getDescription());
        holder.windShortTitle.setText(weatherNow.getWindShort());
        holder.windSpeed.setText(String.valueOf(weatherNow.getWindSpeed()));
        holder.pressure.setText(String.valueOf(weatherNow.getPressure()));
        holder.addToFavorite.setChecked(true);

        holder.rootContainer.setBackground(
                SwitchGradient.getInstance().getBackgroung(weatherNow.getIconCode()));

        Picasso.with(App.getContext())
                .load(weatherNow.getIconUrl())
                .into(holder.iconWeather);

        holder.refreshRecycler(modelWeathers);
    }

    @Override
    public int getItemCount() {
        return (cityWeathers != null) ? cityWeathers.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_city) TextView cityNameInCard;
        @BindView(R.id.tv_time) TextView time;
        @BindView(R.id.tv_temperature) TextView temperature;
        @BindView(R.id.tv_description) TextView description;
        @BindView(R.id.tv_wind_short) TextView windShortTitle;
        @BindView(R.id.tv_wind_speed) TextView windSpeed;
        @BindView(R.id.tv_pressure) TextView pressure;
        @BindView(R.id.rl_root_container) RelativeLayout rootContainer;
        @BindView(R.id.cb_add_to_favorite) CheckBox addToFavorite;
        @BindView(R.id.iv_icon_weather) ImageView iconWeather;
        @BindView(R.id.rv_weather_on_other_time) RecyclerView weatherOtherTime;

        private AdapterHorizontalWeather adapterHorizontalWeather;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setupViews();
        }

        private void setupViews() {
            weatherOtherTime.setLayoutManager(
                    new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterHorizontalWeather = new AdapterHorizontalWeather();
            weatherOtherTime.setAdapter(adapterHorizontalWeather);
        }

        public void refreshRecycler(List<Weather> weathers) {
            adapterHorizontalWeather.setItems(weathers);
            adapterHorizontalWeather.notifyDataSetChanged();
        }

        @OnClick(R.id.cb_add_to_favorite)
        void delCity() {
            if (listener != null) {
                listener.onClickDelCityFromDb(getPosition());
                removeCard(getAdapterPosition());
            }
        }

        private void removeCard(int position) {
            cityWeathers.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), cityWeathers.size());
            if(emptyListener != null && cityWeathers.isEmpty()){
                emptyListener.onEmpty();
            }
        }
    }
}
