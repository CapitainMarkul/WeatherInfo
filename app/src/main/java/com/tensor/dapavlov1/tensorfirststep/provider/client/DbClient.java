package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;

import org.greenrobot.greendao.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient {
    private Query<CityDb> query;
    private DaoSession daoSession;

    public DbClient() {
        daoSession = App.getDaoSession();
        query = App.getDaoSession().getCityDbDao().queryBuilder().orderAsc(CityDbDao.Properties.Name).build();
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
        List<CityDb> listCityOld;
        try {
            listCityOld = loadListAllCities();
        } catch (EmptyDbException e) {
            //Если Бд пуста, то и обновлять нечего
            return;
        }

        List<CityWeatherWrapper> tempList = new ArrayList<>();
        tempList.addAll(cityWeatherWrappers);

        for (CityDb itemOld : listCityOld) {
            Iterator<CityWeatherWrapper> iterator = tempList.iterator();

            while (iterator.hasNext()) {
                CityWeatherWrapper itemRoot = iterator.next();
                CityDb itemNewCity = itemRoot.getCityDb();
                List<WeatherDb> listNewWeather = itemRoot.getWeathers();

                if (itemOld.getName().equals(itemNewCity.getName())) {
                    //Update Time
                    itemOld.setLastTimeUpdate(
                            itemNewCity.getLastTimeUpdate());

                    //если возникнут ошибки при обновлении информации в БД
                    try {
                        //Update WeatherView Info
                        long idCity = itemOld.getWeathers().get(0).getCityId();
                        long counterId = 1;
                        itemOld.getWeathers().clear();
                        for (WeatherDb item : listNewWeather) {
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

    public CityDb isAdd(String cityName, String lastTimeUpdate) {
        List<CityDb> cityDbList;
        try {
            cityDbList = loadListAllCities();
        } catch (EmptyDbException e) {
            //Если Бд пуста, то и города в ней быть не может
            return null;
        }

        //ситуация с одинаковыми названиями городов - возможна,
        // (Города Кострома, Костромская обл и Кострома Самарская, обл. будут считаться за один город)
        // в случае необходимости можно поправить, если сохранять более полную информацию в БД
        for (CityDb item : cityDbList) {
            if (item.getName().equals(cityName)) {
                item.setLastTimeUpdate(lastTimeUpdate);
                item.update();
                return item;    // иначе, на экране добавления не сможем удалить город из избранного!
            }
        }
        return null;
    }

    public Maybe<CityDb> isAddRx(String cityName, String lastTimeUpdate) throws EmptyDbException {
        return Maybe.just(isAdd(cityName, lastTimeUpdate));
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности WeatherView существуют, но без привязки к городу
    public void setInDataBase(CityDb city, List<WeatherDb> weathers) {
        long cityId = App.getDaoSession().getCityDbDao().insert(city);
        App.getDaoSession().getWeatherDbDao().insertInTx(
                attachWeatherToCity(weathers, cityId));
    }

    private List<WeatherDb> attachWeatherToCity(List<WeatherDb> weatherDbs, Long cityId) {
        for (WeatherDb item : weatherDbs) {
            item.setCityId(cityId);
        }
        return weatherDbs;
    }

//    private CityDb getDaoCity(int index) {
//        try {
//            return query.forCurrentThread().list().get(index);
//        } catch (IndexOutOfBoundsException e) {
//            return null;
//        }
//    }

//    public void deleteCity(final int position) {
//        App.getExecutorService().execute(() -> deleteCity(getDaoCity(position)));
//    }

    public void deleteCity(@NotNull String cityName){
        CityDb temp = searchCity(cityName);
        App.getDaoSession().getWeatherDbDao().deleteInTx(temp.getWeathers());
        temp.delete();
    }

//    public void deleteCity(@Nullable CityDb cityDb) {
//        if (cityDb != null) {
//            //Информация о сессии с БД, не переживает Terminate (приходится вот так восстанавливать)
//
//            CityDb temp = searchCity(cityDb.getName());
//            App.getDaoSession().getWeatherDbDao().deleteInTx(temp.getWeathers());
//            temp.delete();
//        }
//    }

    private CityDb searchCity(String cityName) {
        return daoSession.getCityDbDao().queryBuilder()
                .where(CityDbDao.Properties.Name.eq(cityName))
                .unique();
    }
}
