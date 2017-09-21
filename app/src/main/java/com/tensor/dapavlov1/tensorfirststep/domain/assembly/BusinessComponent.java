package com.tensor.dapavlov1.tensorfirststep.domain.assembly;

import com.tensor.dapavlov1.tensorfirststep.core.CoreComponent;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.CitiesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.PlacesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;

import dagger.Component;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@PerBusinessScope
@Component(modules = BusinessModule.class, dependencies = CoreComponent.class)
public interface BusinessComponent {
    // TODO: 21.09.2017 Что будет предоставлять

    void inject(WeatherService weatherService);
    void inject(CitiesDataRepository citiesDataRepository);
    void inject(PlacesDataRepository placesDataRepository);
    //Добавлять inject для команд - ерунда
//    void inject(DelCityFromDbCommand delCityFromDbCommand);
    void inject(AddCityInDbCommand addCityInDbCommand);

    WeatherService getWeatherService();
//    DbClient getDbClient();
}
