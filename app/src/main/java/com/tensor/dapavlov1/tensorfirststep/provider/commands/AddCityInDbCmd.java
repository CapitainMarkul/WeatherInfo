package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCmd implements Command {
    private DbClient dbClient;
    private CityWeatherWrapper cityWeatherWrapper;

    public AddCityInDbCmd(DbClient dbClient, CityWeatherWrapper cityWeatherWrapper) {
        this.dbClient = dbClient;
        this.cityWeatherWrapper = cityWeatherWrapper;
    }

    @Override
    public void execute() {
        dbClient.setInDataBase(
                cityWeatherWrapper.getCityDb(),
                cityWeatherWrapper.getWeathers());
    }

    @Override
    public void undo() {
        dbClient.deleteCity(cityWeatherWrapper.getCityDb().getName());
    }
}
