package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.AnimRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tensor.dapavlov1.tensorfirststep.databinding.CardWeatherFullInfoBinding;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.interfaces.checkBoxClick;
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

    private int lastAnimateElement = 0; //Не позволяет анимировать элементы, которые уже были показаны
    private boolean isAnimate = true;

    public void setListener(DelItemListener listener) {
        this.listener = listener;
    }

    public void setItems(List<CityView> cityViewWeathers) {
        this.cityViewWeathers.clear();
        this.cityViewWeathers.addAll(cityViewWeathers);
        notifyDataSetChanged();
    }

    public List<CityView> getItems() {
        return cityViewWeathers;
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
        lastAnimateElement = 0; // позволяем анимации снова выполняться
        cityViewWeathers.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weather_full_info, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CityView cityView = cityViewWeathers.get(position);
        holder.binding.setCityView(cityView);

        holder.horizontalWeatherAdapter.setItems(cityView.getWeatherViews());
        holder.horizontalWeatherAdapter.notifyDataSetChanged();

        if (isAnimate) {
            setAnimation(holder.binding.cardFullInfo, position, R.anim.recycleradd);
        }
    }

    public void setAnimation(View viewToAnimate, int position, @AnimRes int animateRes) {
        if (position >= lastAnimateElement) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    animateRes);
            viewToAnimate.startAnimation(animation);
            lastAnimateElement++;
        }
    }

    @Override
    public int getItemCount() {
        return (cityViewWeathers != null) ? cityViewWeathers.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements checkBoxClick {
        private HorizontalWeatherAdapter horizontalWeatherAdapter;
        private CardWeatherFullInfoBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setCheckBoxClick(this);
            binding.setSwitchGradient(SwitchGradient.getInstance());
            setupViews(itemView.getContext());
        }

        private void setupViews(Context context) {
            binding.rvWeatherOnOtherTime.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            horizontalWeatherAdapter = new HorizontalWeatherAdapter();
            binding.rvWeatherOnOtherTime.setAdapter(horizontalWeatherAdapter);
        }

        @Override
        public void onItemClick() {
            if (listener != null) {
                int cachedPosition = getLayoutPosition();
                listener.onItemClick(cityViewWeathers.get(cachedPosition));
            }
        }
    }
}