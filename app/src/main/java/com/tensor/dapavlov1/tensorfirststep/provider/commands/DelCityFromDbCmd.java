package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityFromDbCmd implements Command {
    private DbClient dbClient;
    private String cityName;

    public DelCityFromDbCmd(DbClient dbClient, String cityName) {
        this.dbClient = dbClient;
        this.cityName = cityName;
    }

    @Override
    public void execute() {
        dbClient.deleteCity(cityName);
    }

    @Override
    public void undo() {

    }
}