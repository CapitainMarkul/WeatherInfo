package com.tensor.dapavlov1.tensorfirststep.provider.client;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCityDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoWeather;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.provider.WorkWithDataBase;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class Dao {
    DaoCityDao cityDao;
    Query<DaoCity> query;

    public Dao(DaoCityDao cityDao, Query<DaoCity> query) {
        this.cityDao = cityDao;
        this.query = query;
    }

    public DaoCity getDaoCity(int index) {
        return query.forCurrentThread().list().get(index);
    }

    public List<DaoCity> loadListAllCity() {
        return query.forCurrentThread().list();
    }

    public void updateAllCity(List<ModelCityWeather> modelCityWeathers) {
        //выгружаем старую информацию о погоде
        List<DaoCity> listCityOld = App.getDaoSession().getDaoCityDao().loadAll();
        List<ModelCityWeather> tempList = new ArrayList<>();
        tempList.addAll(modelCityWeathers);

        for (DaoCity itemOld : listCityOld) {
            Iterator<ModelCityWeather> iterator = tempList.iterator();

            while (iterator.hasNext()) {
                ModelCityWeather itemRoot = iterator.next();
                DaoCity itemNewCity = itemRoot.getDaoCity();
                List<DaoWeather> listNewWeather = itemRoot.getWeathers();

                if (itemOld.getName().equals(itemNewCity.getName())) {
                    //Update Time
                    itemOld.setLastTimeUpdate(
                            itemNewCity.getLastTimeUpdate());


                    //Update Weather Info
                    long idCity = itemOld.getWeathers().get(0).getCityId();
                    long counterId = 1;
                    itemOld.getWeathers().clear();
                    for (DaoWeather item : listNewWeather) {
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

    public DaoCity isAdd(DaoCity city) {
        List<DaoCity> daoCityList = loadListAllCity();
        //ситуация с одинаковыми названиями городов - возможна,
        // (Города Кострома, Костромская обл и Кострома Самарская, обл. будут считаться за один город)
        // в случае необходимости можно поправить, если сохранять более полную информацию в БД
        for (DaoCity item : daoCityList) {
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
    public void setInDataBase(DaoCity city, List<DaoWeather> weathers) {
        long cityId = App.getDaoSession().getDaoCityDao().insert(city);
        App.getDaoSession().getDaoWeatherDao().insertInTx(
                attachWeatherToCity(weathers, cityId));
    }

    private List<DaoWeather> attachWeatherToCity(List<DaoWeather> daoWeathers, Long cityId) {
        for (DaoWeather item : daoWeathers) {
            item.setCityId(cityId);
        }
        return daoWeathers;
    }

    public void deleteCity(final int position) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                deleteCity(getDaoCity(position));
            }
        });
    }

    public void deleteCity(DaoCity daoCity) {
                App.getDaoSession().getDaoWeatherDao().deleteInTx(daoCity.getWeathers());
                daoCity.delete();
    }

//    public List<DaoCity> loadListAllCityForCurrentThread() {
//    }
//        return query.forCurrentThread().list();
}
