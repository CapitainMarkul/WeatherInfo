package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCommand implements Command {
    private DbClient dbClient;
    private ModelCityWeather modelCityWeather;

    public AddCityInDbCommand(DbClient dbClient, ModelCityWeather modelCityWeather) {
        this.dbClient = dbClient;
        this.modelCityWeather = modelCityWeather;
    }

    @Override
    public void execute() {
        dbClient.setInDataBase(
                modelCityWeather.getDbCity(),
                modelCityWeather.getWeathers());
    }

    @Override
    public void undo() {
        dbClient.deleteCity(modelCityWeather.getDbCity());
    }
}
