package com.tensor.dapavlov1.tensorfirststep.provider.client;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCityDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbWeather;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DbClient {
    private DbCityDao cityDao;
    private Query<DbCity> query;

    public DbClient(DbCityDao cityDao, Query<DbCity> query) {
        this.cityDao = cityDao;
        this.query = query;
    }

    public List<DbCity> loadListAllCity() throws EmptyDbException {
        List<DbCity> resultList = query.forCurrentThread().list();
        if (resultList.isEmpty()) {
            throw new EmptyDbException();
        }
        return resultList;
    }

    public void updateAllCity(List<ModelCityWeather> modelCityWeathers) {
        //выгружаем старую информацию о погоде
        List<DbCity> listCityOld = App.getDaoSession().getDbCityDao().loadAll();
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

                    //  удаляем элемент из временного списка
                    tempList.remove(itemNewCity);
                    iterator.remove();
                }
            }
        }
    }

    public DbCity isAdd(DbCity city) throws EmptyDbException {
        List<DbCity> dbCityList = loadListAllCity();
        //ситуация с одинаковыми названиями городов - возможна,
        // (Города Кострома, Костромская обл и Кострома Самарская, обл. будут считаться за один город)
        // в случае необходимости можно поправить, если сохранять более полную информацию в БД
        for (DbCity item : dbCityList) {
            if (item.getName().equals(city.getName())) {
                item.setLastTimeUpdate(city.getLastTimeUpdate());
                item.update();
                return item;    // иначе, на экране добавления не сможем удалить город из избранного!
            }
        }
        return null;
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
        return query.forCurrentThread().list().get(index);
    }

    public void deleteCity(final int position) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                deleteCity(getDaoCity(position));
            }
        });
    }

    public void deleteCity(DbCity dbCity) {
        App.getDaoSession().getDbWeatherDao().deleteInTx(dbCity.getWeathers());
        dbCity.delete();
    }
}
