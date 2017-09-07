package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

import java.util.List;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCmd implements Command {
    private DbClient dbClient;
    private String cityName;
    private String lastTimeUpdate;
    private List<WeatherDb> weathers;

    public AddCityInDbCmd(DbClient dbClient, String cityName, String lastTimeUpdate, List<WeatherDb> weathers) {
        this.dbClient = dbClient;
        this.cityName = cityName;
        this.weathers = weathers;
        this.lastTimeUpdate = lastTimeUpdate;
    }

    @Override
    public void execute() {
        dbClient.setInDataBase(
                cityName,
                lastTimeUpdate,
                weathers);
    }

    @Override
    public void undo() {
        dbClient.deleteCity(cityName);
    }
}
