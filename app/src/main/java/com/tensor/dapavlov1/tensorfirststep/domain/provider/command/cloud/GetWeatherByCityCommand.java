package com.tensor.dapavlov1.tensorfirststep.domain.provider.command.cloud;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.CitiesDataRepository;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public class GetWeatherByCityCommand implements CloudCommand<ResultWrapper<Observable<CityView>>> {
    private CitiesDataRepository citiesDataRepository = new CitiesDataRepository();
    private String cityName = "";

    public GetWeatherByCityCommand(@NonNull String cityName) {
        this.cityName = cityName;
    }

    @Override
    public ResultWrapper<Observable<CityView>> execute() {
        return citiesDataRepository.getCity(cityName);
    }
}
