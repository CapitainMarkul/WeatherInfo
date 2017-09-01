package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.AnimRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.databinding.ItemCardFullInfoWeatherBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ItemClick;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerEmptyListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.AdapterHorizontalWeather;
import com.tensor.dapavlov1.tensorfirststep.interfaces.RecyclerViewItemClickListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.ViewHolder> {

    private List<City> cityWeathers = new ArrayList<>();
    private RecyclerViewItemClickListener listener;
    private RecyclerEmptyListener emptyListener;

    private boolean isAnimate = true;

    public void setListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setEmptyListener(RecyclerEmptyListener listener) {
        emptyListener = listener;
    }

    public void setItems(List<City> cityWeathers) {
        this.cityWeathers.clear();
        this.cityWeathers.addAll(cityWeathers);
        notifyDataSetChanged();
    }

    public void setItem(City cityWeather) {
        cityWeathers.add(cityWeather);
        isAnimate = true;
        notifyItemInserted(getItemCount());
    }

    public void clearCache() {
        cityWeathers.clear();
    }

    public void isAnimate(boolean isConfigChange) {
        this.isAnimate = isConfigChange;
    }

    public void setDefaultSetting(){
        cityWeathers.clear();
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

        if (isAnimate) {
            setAnimation(holder.binding.cardFullInfo, position, holder.getOldPosition(), R.anim.recycleradd);
        }
    }

    private void setAnimation(View viewToAnimate, int position, int oldPosition, @AnimRes int animateRes) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > oldPosition) {
            Animation animation = AnimationUtils.loadAnimation(App.getContext(),
                    animateRes);
            viewToAnimate.startAnimation(animation);
        }
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
            binding.setSwitchGradient(SwitchGradient.getInstance());
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
            setAnimation(binding.cardFullInfo,position,getOldPosition(), R.anim.recyclerdel);
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
