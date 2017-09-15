package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.tensor.dapavlov1.tensorfirststep.BR;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.interfaces.checkBoxClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;

import org.parceler.Parcel;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */
@Parcel
public class AddCityViewModel extends AbstractViewModel implements checkBoxClick {
    private CityView city;
    private boolean isLoading;
    private boolean firstLaunch;
    private boolean isFavorite;
    private boolean isAnimation;
    private SwitchGradient switchGradient = SwitchGradient.getInstance();

    //Listeners
    private boolean onClearInputText;
    private boolean onItemClick;
//    private boolean onClearInputText;

    @Bindable
    public CityView getCity() {
        return city;
    }

    public void setCityView(CityView city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        setAnimation(isLoading);
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        this.firstLaunch = isFirstLaunch;
        notifyPropertyChanged(BR.firstLaunch);
    }

    @Bindable
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        city.setFavorite(isFavorite);
        notifyPropertyChanged(BR.favorite);
    }

    @Bindable
    public boolean isAnimation() {
        return isAnimation;
    }

    private void setAnimation(boolean isAnimation) {
        this.isAnimation = isAnimation;
        notifyPropertyChanged(BR.animation);
    }

    @Bindable
    public SwitchGradient getSwitchGradient() {
        return switchGradient;
    }


    /**
     * Listeners
     **/

    public View.OnClickListener clearInputText() {
        return view -> notifyPropertyChanged(BR.onClearInputText);
    }

    @Bindable
    public boolean getOnClearInputText() {
        return onClearInputText;
    }

    @Override
    public void onItemClick() {
        notifyPropertyChanged(BR.onItemClick);
    }

    @Bindable
    public boolean getOnItemClick() {
        return onItemClick;
    }
}
