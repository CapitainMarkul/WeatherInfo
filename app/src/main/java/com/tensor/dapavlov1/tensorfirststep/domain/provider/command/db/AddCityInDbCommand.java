package com.tensor.dapavlov1.tensorfirststep.domain.provider.command.db;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;

import javax.inject.Inject;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCommand implements DbCommand {
    // TODO: 21.09.2017 Вопрос касательно инициализации исполнителя в паттерне Команда Исправить.
    DbClient dbClient = App.get().businessComponent().getWeatherService().getDbService();

    private void inject() {
        App.get().businessComponent().inject(this);
    }

    private CityView city;

    public AddCityInDbCommand(CityView city) {
        this.city = city;
    }

    @Override
    public void execute() {
        dbClient.setInDataBase(city);
    }

//    @Override
//    public void undo() {
//        dbClient.deleteCity(city);
//    }
}
