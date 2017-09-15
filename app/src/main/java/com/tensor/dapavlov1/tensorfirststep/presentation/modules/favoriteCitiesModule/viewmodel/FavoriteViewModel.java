package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */
@Parcel
public class FavoriteViewModel extends AbstractViewModel {
    private List<CityView> citiesView = new ArrayList<>();
    private boolean isLoading;
    private boolean isResetAdapter;
    private CityView lastDeletedCity;

    @Bindable
    public List<CityView> getCitiesView() {
        return citiesView;
    }

    public void addCityView(CityView cityView) {
        citiesView.add(cityView);
        notifyPropertyChanged(BR.citiesView);
    }

    public void setCitiesView(List<CityView> citiesView) {
        this.citiesView = citiesView;
        notifyPropertyChanged(BR.citiesView);
    }

    public CityView getLastCity() {
        if (citiesView.isEmpty()) {
            return null;
        } else {
            return citiesView.get(citiesView.size() - 1);
        }
    }

    public void delCityView(CityView city) {
        citiesView.remove(city);
        setDeletedCity(city);
    }

    @Bindable
    public CityView getLastDeletedCity() {
        return lastDeletedCity;
    }

    private void setDeletedCity(CityView city) {
        this.lastDeletedCity = city;
        notifyPropertyChanged(BR.lastDeletedCity);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean isResetAdapter() {
        return isResetAdapter;
    }

    public void setResetAdapter(boolean resetAdapter) {
        isResetAdapter = resetAdapter;
        notifyPropertyChanged(BR.resetAdapter);
    }

    public void resetAdapter() {
        resetViewModel();
        setResetAdapter(true);
    }

    public void showEmptyResult() {
        resetViewModel();
        setCitiesView(citiesView);
    }

    private void resetViewModel() {
        citiesView.clear();
        isLoading = false;
        lastDeletedCity = null;
    }
}
