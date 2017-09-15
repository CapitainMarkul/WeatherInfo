package com.tensor.dapavlov1.tensorfirststep.domain.provider.command.cloud;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.CitiesDataRepository;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public class GetWeatherByCitiesCommand implements CloudCommand<ResultWrapper<Flowable<CityView>>> {
    private CitiesDataRepository citiesDataRepository = new CitiesDataRepository();

    @Override
    public ResultWrapper<Flowable<CityView>> execute() {
        return citiesDataRepository.getCitiesRx();
    }
}
