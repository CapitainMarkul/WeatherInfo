package com.tensor.dapavlov1.tensorfirststep.data.DaoModels;

import com.tensor.dapavlov1.tensorfirststep.App;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.Query;

import java.util.Iterator;
import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class WorkWithDataBase {
    private static WorkWithDataBase instance;

    public WorkWithDataBase() {
    }

    public static WorkWithDataBase getInstance() {
        if (instance == null) {
            instance = new WorkWithDataBase();
        }
        return instance;
    }


    private DaoCityDao modelCityDao;
    private Query<DaoCity> query;

    //    //если пометить весь метод, то все параметры становятся NotNull??
    @NotNull
    private List<DaoWeather> attachWeatherToCity(List<DaoWeather> daoWeathers, Long cityId) {
        for (DaoWeather item : daoWeathers) {
            item.setCityId(cityId);
        }
        return daoWeathers;
    }

    public void updateAllCity(List<ModelCityWeather> modelCityWeathers) {
        //выгружаем старую информацию о погоде
        List<DaoCity> listCityOld = App.getDaoSession().getDaoCityDao().loadAll();

        for (DaoCity itemOld : listCityOld) {
            Iterator<ModelCityWeather> iterator = modelCityWeathers.iterator();

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
                    modelCityWeathers.remove(itemNewCity);
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

    public DaoCity getDaoCity(int index){
        initQuery();
        return query.list().get(index);
    }

    public List<DaoCity> loadListAllCity() {
        initQuery();
        return query.list();
    }

    public List<DaoCity> loadListAllCityForCurrentThread() {
        initQuery();
        return query.forCurrentThread().list();
    }

    private void initQuery() {
        modelCityDao = App.getDaoSession().getDaoCityDao();
        query = modelCityDao.queryBuilder().orderAsc(DaoCityDao.Properties.Name).build();
    }

    //Сначала в БД заносится город, узнаем его ID,  прикрепляем к нему Лист с погодой
    //на текущий момент сущности Weather существуют, но без привязки к городу
    public void setInDataBase(DaoCity city, List<DaoWeather> weathers) {
        long cityId = App.getDaoSession().getDaoCityDao().insert(city);
        App.getDaoSession().getDaoWeatherDao().insertInTx(
                attachWeatherToCity(weathers, cityId));
    }

    public void deleteCity(final int position) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                deleteCity(WorkWithDataBase.getInstance().getDaoCity(position));
            }
        });
    }

    public void deleteCity(DaoCity daoCity) {
        App.getDaoSession().getDaoWeatherDao().deleteInTx(daoCity.getWeathers());
        daoCity.delete();
    }
}
