package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class DbCitiesDataStore implements CitiesDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();

    @Override
    public Flowable<City> getCitiesRx() {
        return Flowable.create(e -> dbClient.loadListAllCitiesRx()
                .subscribeOn(Schedulers.from(App.getExecutorService()))
                .map(city ->
                        MapperDbToView.getInstance().convertDbModelToViewModel(city, city.getWeathers(), true))
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
