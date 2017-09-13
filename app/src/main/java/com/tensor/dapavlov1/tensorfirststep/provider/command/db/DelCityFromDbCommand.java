package com.tensor.dapavlov1.tensorfirststep.provider.command.db;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityFromDbCommand implements DbCommand {
    private DbClient dbClient = DbClient.getInstance();
    private CityView city;

    public DelCityFromDbCommand(CityView city) {
        this.city = city;
    }

    @Override
    public void execute() {
        dbClient.deleteCity(city);
    }

//    @Override
//    public void undo() {
//
//    }
}
