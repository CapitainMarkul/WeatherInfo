package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.databinding.ItemCardFullInfoWeatherBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ItemClick;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerEmptyListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;

import java.util.List;

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
        holder.binding.setCity(cityWeathers.get(position));
        holder.adapterHorizontalWeather.setItems(cityWeathers.get(position).getWeathers());
    }

    @Override
    public int getItemCount() {
        return (cityWeathers != null) ? cityWeathers.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemClick {
        private AdapterHorizontalWeather adapterHorizontalWeather;
        private ItemCardFullInfoWeatherBinding binding;


        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setEvents(this);
            setupViews();
        }

        private void setupViews() {
            binding.rvWeatherOnOtherTime.setLayoutManager(
                    new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterHorizontalWeather = new AdapterHorizontalWeather();
            binding.rvWeatherOnOtherTime.setAdapter(adapterHorizontalWeather);
        }

        private void removeCard(int position) {
            cityWeathers.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), cityWeathers.size());
            if (emptyListener != null && cityWeathers.isEmpty()) {
                emptyListener.onEmpty();
            }
        }

        @Override
        public void onItemClick() {
            if (listener != null) {
                listener.onClickDelCityFromDb(getPosition());
                removeCard(getAdapterPosition());
            }
        }
    }
}
