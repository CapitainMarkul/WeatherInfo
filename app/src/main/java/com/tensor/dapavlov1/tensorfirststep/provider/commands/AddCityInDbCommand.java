package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DaoClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCommand implements Command {
    private DaoClient daoClient;
    private ModelCityWeather modelCityWeather;

    public AddCityInDbCommand(DaoClient daoClient, ModelCityWeather modelCityWeather) {
        this.daoClient = daoClient;
        this.modelCityWeather = modelCityWeather;
    }

    @Override
    public void execute() {
        daoClient.setInDataBase(
                modelCityWeather.getDaoCity(),
                modelCityWeather.getWeathers());
    }

    @Override
    public void undo() {
        daoClient.deleteCity(modelCityWeather.getDaoCity());
    }
}
