package com.tensor.dapavlov1.tensorfirststep.domain.provider.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.DbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.DelCityFromDbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.GetCitiesNameCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.SearchCityCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.UpdateAllCitiesCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.UpdateCityCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandUtils;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObservable;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObserver;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient extends DbCommandUtils implements DelObservable {
    private final DaoSession daoSession;
    private List<DelObserver> observers;

    @Inject
    public DbClient(DaoSession daoSession) {
        this.daoSession = daoSession;
        observers = new ArrayList<>();
    }

    public Flowable<List<CityDb>> loadAllCitiesDbRx() {
        return Flowable.fromCallable(() -> {
            try {
                return loadListAllCities(daoSession);
            } catch (EmptyDbException e) {
                //возвращаем пустоту
                return new ArrayList<CityDb>();
            }
        });
    }

    public Flowable<CityView> loadAllCitiesViewRx() {
        return loadAllCitiesDbRx()
                .flatMap(new Function<List<CityDb>, Publisher<CityDb>>() {
                    @Override
                    public Publisher<CityDb> apply(@NonNull List<CityDb> cityDbs) throws Exception {
                        return Flowable.fromIterable(cityDbs);
                    }
                })
                .map(city -> FacadeMap.cityDbToCityVM(city, true));
    }

    public List<String> getCitiesName() {
        DbCommand<List<String>> getCitiesNameCommand = new GetCitiesNameCommand();
        List<String> result = getCitiesNameCommand.execute(daoSession);

        if (result == null) {
            return new ArrayList<String>();
        }
        return result;
    }

    public void updateAllCities(List<CityWeatherWrapper> cityWeatherWrappers) {
        //выгружаем старую информацию о погоде
        DbCommand updateAllCitiesCommand = new UpdateAllCitiesCommand(cityWeatherWrappers);
        updateAllCitiesCommand.execute(daoSession);
    }

    public void updateCity(String cityName, List<WeatherView> weathers) {
        DbCommand updateCommand = new UpdateCityCommand(cityName, weathers);
        updateCommand.execute(daoSession);
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности Weather существуют, но без привязки к городу
//    public void addInDataBase(String cityName, String lastTimeUpdate, List<WeatherDb> weathers) {
    public void addInDataBase(CityView city) {
        DbCommand<Boolean> addCommand = new AddCityInDbCommand(city);
        addCommand.execute(daoSession);
    }

    @Nullable
    public CityDb searchCity(String cityName) {
        DbCommand<CityDb> searchCityCommand = new SearchCityCommand(cityName);
        return searchCityCommand.execute(daoSession);
    }

    public void deleteCity(@NotNull CityView city) {
        DbCommand<Boolean> delCommand = new DelCityFromDbCommand(city);
        if (delCommand.execute(daoSession)) {
            notifyAllObservers(true, city);
        } else {
            notifyAllObservers(false, city);
        }
    }

    /**
     * Observable >>>
     */

    @Override
    public void subscribe(DelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unSubscribe(DelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(boolean isSuccess, CityView deletedCity) {
        DelObserver[] delObserversArray = new DelObserver[observers.size()];
        delObserversArray = observers.toArray(delObserversArray);

        for (DelObserver item : delObserversArray) {
            item.sendDelResult(isSuccess, deletedCity);
        }
    }
}
