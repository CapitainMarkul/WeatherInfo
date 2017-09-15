package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.dbstore;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class DbCitiesStore implements CitiesDataStore {
    private DbClient dbClient = DbClient.getInstance();

    @Override
    public Flowable<CityView> getCitiesRx() {
        return Flowable.create(e ->
                dbClient.loadListAllCitiesRx()
                        .subscribeOn(Schedulers.from(App.getExecutorService()))
                        .flatMap(new Function<List<CityDb>, Publisher<CityDb>>() {
                            @Override
                            public Publisher<CityDb> apply(@NonNull List<CityDb> cityDbs) throws Exception {
                                return Flowable.fromIterable(cityDbs);
                            }
                        })
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
}