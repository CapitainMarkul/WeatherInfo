package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.DelCityByIndexCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.invokers.RemoteControlDb;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityNotFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class DbCityDataStore implements CityDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
    private RemoteControlDb remoteControlDb;

    public DbCityDataStore() {
        remoteControlDb = new RemoteControlDb();
    }

    @Override
    public void add(ModelCityWeather city) {
        remoteControlDb.setCommand(
                new AddCityInDbCommand(dbClient, city));
        remoteControlDb.execute();
    }

    @Override
    public void delete(Object city) {
        //удаление по индексу
        if (city instanceof Integer) {
            remoteControlDb.setCommand(
                    new DelCityByIndexCommand(dbClient, (Integer) city));
            remoteControlDb.execute();
        }
        //удаление по элементу
        else if (city instanceof ModelCityWeather) {
            remoteControlDb.setCommand(
                    new AddCityInDbCommand(dbClient, (ModelCityWeather) city));
            remoteControlDb.undo();
        }
    }

    // TODO: 24.08.2017 Пустой метод!
    @Override
    public City getCity(String fullCityName) throws NetworkConnectException, EmptyResponseException, CityNotFoundException, CityFoundException {
        return null;
    }
}
