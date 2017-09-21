package com.tensor.dapavlov1.tensorfirststep.domain.provider.db;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.DbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.DelCityFromDbCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.GetCitiesNameCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.SearchCityCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.UpdateAllCitiesCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.UpdateCityCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandHelper;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObservable;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObserver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient extends DbCommandHelper implements DelObservable {
    private final DaoSession daoSession;
    private List<DelObserver> observers;

    @Inject
    public DbClient(DaoSession daoSession) {
        this.daoSession = daoSession;
//        query = daoSession.getCityDbDao().queryBuilder().build();
        observers = new ArrayList<>();
    }

    public Flowable<List<CityDb>> loadListAllCitiesRx() {
        return Flowable.fromCallable(() -> {
            try {
                return loadListAllCities(daoSession);
            } catch (EmptyDbException e) {
                //возвращаем пустоту
                return new ArrayList<CityDb>();
            }
        });
    }

    public List<String> getCitiesName() throws EmptyDbException {
        DbCommand<List<String>> getCitiesNameCommand = new GetCitiesNameCommand();
        List<String> result = getCitiesNameCommand.execute(daoSession);

        if (result == null) {
            throw new EmptyDbException();
        }
        return result;
    }

    public void updateAllCities(List<CityWeatherWrapper> cityWeatherWrappers) {
        //выгружаем старую информацию о погоде
        DbCommand updateAllCitiesCommand = new UpdateAllCitiesCommand(cityWeatherWrappers);
        updateAllCitiesCommand.execute(daoSession);
    }

    public void updateCity(CityWeatherWrapper cityWeatherWrapper) {
        DbCommand updateCommand = new UpdateCityCommand(cityWeatherWrapper);
        updateCommand.execute(daoSession);
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности Weather существуют, но без привязки к городу
//    public void addInDataBase(String cityName, String lastTimeUpdate, List<WeatherDb> weathers) {
    public void addInDataBase(CityView city) {
        DbCommand<Boolean> addCommand = new AddCityInDbCommand(city);
        if (addCommand.execute(daoSession)) {
            // TODO: 21.09.2017 Город добавлен в избранное (Функционал можно внести в Observable)
        } else {
            // TODO: 21.09.2017 Произошла ошибка добавления
        }
    }

    @Nullable
    public CityDb searchCity(String cityName) {
        DbCommand<CityDb> searchCityCommand = new SearchCityCommand(cityName);
        return searchCityCommand.execute(daoSession);
    }

//        CityDb cityDb = new CityDb(null, cityName, lastTimeUpdate);
//        long cityId = daoSession.getCityDbDao().insert(
//                new CityDb(null, city.getName(), city.getLastTimeUpdate()));
//        daoSession.getWeatherDbDao().insertInTx(
//                attachWeatherToCity(
//                        ViewToDbMap.convertWeathersToDbType(city.getWeatherViews()), cityId, false));

    public void deleteCity(@NotNull CityView city) {
        DbCommand<Boolean> delCommand = new DelCityFromDbCommand(city);
        if (delCommand.execute(daoSession)) {
            notifyAllObservers(true, city);
        } else {
            notifyAllObservers(false, city);
        }
    }

//    private List<CityDb> loadListAllCities() throws EmptyDbException {
//        List<CityDb> resultList = query.forCurrentThread().list();
//        if (resultList.isEmpty()) {
//            throw new EmptyDbException();
//        }
//        return resultList;
//    }

//    // FIXME: 07.09.2017 Если информацию обновляем, то нужно восстановить ключи, если это вставка нового города - этого делать не нужно
//    private List<WeatherDb> attachWeatherToCity(List<WeatherDb> weathers, Long cityId, boolean isUpdate) {
//        long counter = 1;
//        for (WeatherDb item : weathers) {
//            item.setCityId(cityId);
//            if (isUpdate) {
//                item.setId(counter++);
//            }
//        }
//        return weathers;
//    }

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
