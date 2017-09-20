package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel;

import android.databinding.Bindable;

//import com.tensor.dapavlov1.tensorfirststep.BR;
import com.tensor.dapavlov1.tensorfirststep.BR;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.interfaces.checkBoxClick;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;

import org.parceler.Parcel;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */
@Parcel
public class AddCityViewModel extends AbstractViewModel
        implements AddCityViewModelContract.ViewModel, checkBoxClick {
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

    @Override
    public CityView getCity() {
        return city;
    }

    @Override
    public void setCityView(CityView city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        setAnimation(isLoading);
        notifyPropertyChanged(BR.loading);
    }

    @Override
    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    @Override
    public void setFirstLaunch(boolean isFirstLaunch) {
        this.firstLaunch = isFirstLaunch;
        notifyPropertyChanged(BR.firstLaunch);
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        city.setFavorite(isFavorite);
        notifyPropertyChanged(BR.favorite);
    }

    @Override
    public boolean isAnimation() {
        return isAnimation;
    }

    private void setAnimation(boolean isAnimation) {
        this.isAnimation = isAnimation;
        notifyPropertyChanged(BR.animation);
    }

    @Override
    public SwitchGradient getSwitchGradient() {
        return switchGradient;
    }


    /**
     * Listeners
     **/
    @Override
    public void clearInputText() {
        notifyPropertyChanged(BR.onClearInputText);
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
