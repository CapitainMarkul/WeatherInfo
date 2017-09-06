package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.AddCityInDbCmd;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.DelCityFromDbCmd;
import com.tensor.dapavlov1.tensorfirststep.provider.invokers.DbExecutor;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class DbCitiesStore implements CitiesDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
    private DbExecutor dbExecutor;

    @Override
    public Flowable<CityView> getCitiesRx() {
        return Flowable.create(e -> dbClient.loadListAllCitiesRx()
                .subscribeOn(Schedulers.from(App.getExecutorService()))
                .map(city ->
                        DbToViewMap.getInstance().convertDbModelToViewModel(city, city.getWeathers(), true))
                .subscribe(
                        result -> {
                            e.onNext(result);
                            // TODO: 31.08.2017 Здесь можно установить задержку, для плавности анимации, при загрузке из хранилища
                            Thread.sleep(100);
                        },
                        e::onError,
                        e::onComplete), BackpressureStrategy.BUFFER);
    }


    // TODO: 06.09.2017 Паттерн команда, понять как его здесь правильно применить
    public DbCitiesStore() {
        dbExecutor = new DbExecutor();
    }

    @Override
    public void add(CityWeatherWrapper city) {
        dbExecutor.setCommand(
                new AddCityInDbCmd(dbClient, city));
        dbExecutor.execute();
    }

    @Override
    public void delete(String cityName) {
        dbExecutor.setCommand(
                new DelCityFromDbCmd(dbClient, cityName));
        dbExecutor.execute();
    }
}
