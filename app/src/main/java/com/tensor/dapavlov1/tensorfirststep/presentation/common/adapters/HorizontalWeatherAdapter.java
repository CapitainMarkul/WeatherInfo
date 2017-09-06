package com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.databinding.ItemHorizontalRecyclerBinding;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

/**
 * Created by da.pavlov1 on 04.08.2017.
 */

public class HorizontalWeatherAdapter
        extends RecyclerView.Adapter<HorizontalWeatherAdapter.WeatherItemHolder> {

    private List<WeatherView> weatherViewNow;

    public void setItems(@NotNull final List<WeatherView> weatherViews) {
        this.weatherViewNow = weatherViews;
    }

    @Override
    public WeatherItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeatherItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(WeatherItemHolder holder, int position) {
        holder.binding.setWeatherView(weatherViewNow.get(position));
    }

    @Override
    public int getItemCount() {
        return (weatherViewNow != null) ? weatherViewNow.size() : 0;
    }

    public class WeatherItemHolder extends RecyclerView.ViewHolder {
        private ItemHorizontalRecyclerBinding binding;

        public WeatherItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
