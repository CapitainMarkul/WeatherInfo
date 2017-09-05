package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCityDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbWeather;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient {
    private Query<DbCity> query;
    private DaoSession daoSession;

    //    public DbClient(Query<DbCity> query, DaoSession daoSession) {
//        this.query = query;
//        this.daoSession = daoSession;
//    }
    public DbClient() {
        daoSession = App.getDaoSession();
        query = App.getDaoSession().getDbCityDao().queryBuilder().orderAsc(DbCityDao.Properties.Name).build();
    }

    public Flowable<DbCity> loadListAllCitiesRx() {
        return Flowable.create(e -> {
            try {
                for (DbCity item : loadListAllCities()) {
                    e.onNext(item);
                }
                e.onComplete();
            } catch (EmptyDbException e1) {
                e1.printStackTrace();
            }
        }, BackpressureStrategy.BUFFER);
    }

    public List<DbCity> loadListAllCities() throws EmptyDbException {
        List<DbCity> resultList = query.forCurrentThread().list();
        if (resultList.isEmpty()) {
            throw new EmptyDbException();
        }
        return resultList;
    }

    public List<String> getCityNames() throws EmptyDbException {
        List<String> cityNames = new ArrayList<>();
        for (DbCity item : loadListAllCities()) {
            cityNames.add(item.getName());
        }
        return cityNames;
    }

    public void updateAllCities(List<ModelCityWeather> modelCityWeathers) {
        //выгружаем старую информацию о погоде
        List<DbCity> listCityOld;
        try {
            listCityOld = loadListAllCities();
        } catch (EmptyDbException e) {
            //Если Бд пуста, то и обновлять нечего
            return;
        }

        List<ModelCityWeather> tempList = new ArrayList<>();
        tempList.addAll(modelCityWeathers);

        for (DbCity itemOld : listCityOld) {
            Iterator<ModelCityWeather> iterator = tempList.iterator();

            while (iterator.hasNext()) {
                ModelCityWeather itemRoot = iterator.next();
                DbCity itemNewCity = itemRoot.getDbCity();
                List<DbWeather> listNewWeather = itemRoot.getWeathers();

                if (itemOld.getName().equals(itemNewCity.getName())) {
                    //Update Time
                    itemOld.setLastTimeUpdate(
                            itemNewCity.getLastTimeUpdate());

                    //если возникнут ошибки при обновлении информации в БД
                    try {
                        //Update Weather Info
                        long idCity = itemOld.getWeathers().get(0).getCityId();
                        long counterId = 1;
                        itemOld.getWeathers().clear();
                        for (DbWeather item : listNewWeather) {
                            item.setCityId(idCity);
                            item.setId(counterId);
                            itemOld.getWeathers().add(item);
                            counterId++;
                        }

                        itemOld.update();
                    } catch (Exception e) {
                        continue;
                    }
                    //  удаляем элемент из временного списка
                    tempList.remove(itemNewCity);
                    iterator.remove();
                }
            }
        }
    }

    public DbCity isAdd(String cityName, String lastTimeUpdate) {
        List<DbCity> dbCityList;
        try {
            dbCityList = loadListAllCities();
        } catch (EmptyDbException e) {
            //Если Бд пуста, то и города в ней быть не может
            return null;
        }

        //ситуация с одинаковыми названиями городов - возможна,
        // (Города Кострома, Костромская обл и Кострома Самарская, обл. будут считаться за один город)
        // в случае необходимости можно поправить, если сохранять более полную информацию в БД
        for (DbCity item : dbCityList) {
            if (item.getName().equals(cityName)) {
                item.setLastTimeUpdate(lastTimeUpdate);
                item.update();
                return item;    // иначе, на экране добавления не сможем удалить город из избранного!
            }
        }
        return null;
    }

    public Maybe<DbCity> isAddRx(String cityName, String lastTimeUpdate) throws EmptyDbException {
        return Maybe.just(isAdd(cityName, lastTimeUpdate));
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности Weather существуют, но без привязки к городу
    public void setInDataBase(DbCity city, List<DbWeather> weathers) {
        long cityId = App.getDaoSession().getDbCityDao().insert(city);
        App.getDaoSession().getDbWeatherDao().insertInTx(
                attachWeatherToCity(weathers, cityId));
    }

    private List<DbWeather> attachWeatherToCity(List<DbWeather> dbWeathers, Long cityId) {
        for (DbWeather item : dbWeathers) {
            item.setCityId(cityId);
        }
        return dbWeathers;
    }

    private DbCity getDaoCity(int index) {
        try {
            return query.forCurrentThread().list().get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void deleteCity(final int position) {
        App.getExecutorService().execute(() -> deleteCity(getDaoCity(position)));
    }

    public void deleteCity(@Nullable DbCity dbCity) {
        if (dbCity != null) {
            //Информация о сессии с БД, не переживает Terminate (приходится вот так восстанавливать)

            DbCity temp = searchCity(dbCity.getName());
            App.getDaoSession().getDbWeatherDao().deleteInTx(temp.getWeathers());
            temp.delete();
        }
    }

    private DbCity searchCity(String cityName) {
        return daoSession.getDbCityDao().queryBuilder()
                .where(DbCityDao.Properties.Name.eq(cityName))
                .unique();
    }
}
