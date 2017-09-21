package com.tensor.dapavlov1.tensorfirststep.domain.provider.command.db;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;

import javax.inject.Inject;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityFromDbCommand implements DbCommand {
    //    private DbClient dbClient = DbClient.getInstance();   Не могу т.к. Dagger 2
    DbClient dbClient = App.get().businessComponent().getWeatherService().getDbService();
    private CityView city;

//    private void inject(){
//        App.get().businessComponent().inject(this);
//    }

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
