package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database;

import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class DbCitiesDataStore implements CitiesDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();

    @Override
    public List<City> getCities() throws EmptyDbException, NetworkConnectException {
        List<City> resultList =
                MapperDbToView.getInstance().getCityViewModelsFromDao(dbClient.loadListAllCity());

        //ак возвращаем результат для того, чтобы правильно обработать
        //CallBack'и которые в CitiesDataRepository
        throw new NetworkConnectException(resultList);
    }
}
