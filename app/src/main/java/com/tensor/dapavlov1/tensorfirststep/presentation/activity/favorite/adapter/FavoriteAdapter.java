package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.AnimRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.databinding.ItemCardFullInfoWeatherBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ItemClick;
import com.tensor.dapavlov1.tensorfirststep.interfaces.EmptyListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.adapters.HorizontalWeatherAdapter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelItemListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<CityView> cityViewWeathers = new ArrayList<>();
    private DelItemListener listener;
    private EmptyListener emptyListener;

    private boolean isAnimate = true;

    public void setListener(DelItemListener listener) {
        this.listener = listener;
    }

    public void setEmptyListener(EmptyListener listener) {
        emptyListener = listener;
    }

    public void setItems(List<CityView> cityViewWeathers) {
        this.cityViewWeathers.clear();
        this.cityViewWeathers.addAll(cityViewWeathers);
        notifyDataSetChanged();
    }

    public void setItem(CityView cityViewWeather) {
        notifyItemInserted(getItemCount());
        cityViewWeathers.add(cityViewWeather);
        isAnimate = true;
    }

    public void setAnimate(boolean flag) {
        isAnimate = flag;
    }

    public void setDefaultSetting() {
        cityViewWeathers.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_full_info_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("CityView:", cityViewWeathers.get(position).getName());

        holder.binding.setCityView(cityViewWeathers.get(position));
        holder.horizontalWeatherAdapter.setItems(cityViewWeathers.get(position).getWeatherViews());

        if (isAnimate) {
            setAnimation(holder.binding.cardFullInfo, position, holder.getOldPosition(), R.anim.recycleradd);
        }
    }

    private void setAnimation(View viewToAnimate, int position, int oldPosition, @AnimRes int animateRes) {
        // FIXME: 05.09.2017 Зачем old position
        if (position > oldPosition) {
            Animation animation = AnimationUtils.loadAnimation(App.getContext(),
                    animateRes);
            viewToAnimate.startAnimation(animation);
        }
    }

    @Override
    public int getItemCount() {
        return (cityViewWeathers != null) ? cityViewWeathers.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemClick {
        private HorizontalWeatherAdapter horizontalWeatherAdapter;
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
            horizontalWeatherAdapter = new HorizontalWeatherAdapter();
            binding.rvWeatherOnOtherTime.setAdapter(horizontalWeatherAdapter);
        }

        private void removeCard(int position) {
            cityViewWeathers.remove(position);  //удаляем из листаАдаптера
            notifyItemRemoved(position);    //тправляем запрос на обновление списка
            notifyItemRangeChanged(getAdapterPosition(), cityViewWeathers.size());  //склеиваем новый список
            setAnimation(binding.cardFullInfo, position, getOldPosition(), R.anim.recyclerdel);
            if (emptyListener != null && cityViewWeathers.isEmpty()) {
                emptyListener.onEmpty();
            }
        }

        @Override
        public void onItemClick() {
            if (listener != null) {
                listener.onItemClick(getPosition());
                removeCard(getAdapterPosition());
            }
        }
    }
}