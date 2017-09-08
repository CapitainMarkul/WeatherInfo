package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObservable;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObserver;

import org.greenrobot.greendao.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient implements DelObservable {

    public static DbClient getInstance() {
        return DbClientLoader.INSTANCE;
    }

    private static final class DbClientLoader {
        private static final DbClient INSTANCE = new DbClient();
    }

    private Query<CityDb> query;
    private DaoSession daoSession;
    private List<DelObserver> observers;

    private DbClient() {
        daoSession = App.getDaoSession();
        query = App.getDaoSession().getCityDbDao().queryBuilder().build();
        observers = new ArrayList<>();
    }

    public Flowable<CityDb> loadListAllCitiesRx() {
        return Flowable.create(e -> {
            try {
                for (CityDb item : loadListAllCities()) {
                    e.onNext(item);
                }
                e.onComplete();
            } catch (EmptyDbException e1) {
                e1.printStackTrace();
            }
        }, BackpressureStrategy.BUFFER);
    }

    public List<CityDb> loadListAllCities() throws EmptyDbException {
        List<CityDb> resultList = query.forCurrentThread().list();
        if (resultList.isEmpty()) {
            throw new EmptyDbException();
        }
        return resultList;
    }

    public List<String> getCityNames() throws EmptyDbException {
        List<String> cityNames = new ArrayList<>();
        for (CityDb item : loadListAllCities()) {
            cityNames.add(item.getName());
        }
        return cityNames;
    }

    public void updateAllCities(List<CityWeatherWrapper> cityWeatherWrappers) {
        //выгружаем старую информацию о погоде
        for (CityWeatherWrapper newItem : cityWeatherWrappers) {
            CityDb cityDb = daoSession.getCityDbDao().queryBuilder()
                    .where(CityDbDao.Properties.Name.eq(newItem.getCityDb().getName()))
                    .unique();
            cityDb.setLastTimeUpdate(TrimDateSingleton.getInstance().getNowTime());
            cityDb.getWeathers().clear();
            cityDb.getWeathers().addAll(attachWeatherToCity(newItem.getWeathers(), cityDb.getId(), true));
            cityDb.update();
        }
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности Weather существуют, но без привязки к городу
    public void setInDataBase(String cityName, String lastTimeUpdate, List<WeatherDb> weathers) {
//        CityDb cityDb = new CityDb(null, cityName, lastTimeUpdate);
        long cityId = App.getDaoSession().getCityDbDao().insert(
                new CityDb(null, cityName, lastTimeUpdate));
        App.getDaoSession().getWeatherDbDao().insertInTx(
                attachWeatherToCity(weathers, cityId, false));
    }

    // FIXME: 07.09.2017 Если информацию обновляем, то нужно восстановить ключи, если это вставка нового города - этого делать не нужно
    private List<WeatherDb> attachWeatherToCity(List<WeatherDb> weathers, Long cityId, boolean isUpdate) {
        long counter = 1;
        for (WeatherDb item : weathers) {
            item.setCityId(cityId);
            if (isUpdate) {
                item.setId(counter++);
            }
        }
        return weathers;
    }

    public void deleteCity(@NotNull String cityName) {
        try {
            CityDb temp = searchCity(cityName);
            App.getDaoSession().getWeatherDbDao().deleteInTx(temp.getWeathers());
            temp.delete();

            notifyAllObservers(true);
        } catch (Exception e) {
            notifyAllObservers(false);
        }
    }

    @Nullable
    public CityDb searchCity(String cityName) {
        return daoSession.getCityDbDao().queryBuilder()
                .where(CityDbDao.Properties.Name.eq(cityName))
                .unique();
    }

    @Override
    public void subscribe(DelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(DelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(boolean isSuccess) {
        for (DelObserver item : observers) {
            item.deleteResult(isSuccess);
        }
    }
}
