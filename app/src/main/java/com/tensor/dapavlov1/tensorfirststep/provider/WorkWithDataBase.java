package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.provider.client.Dao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCityDao;

import org.greenrobot.greendao.query.Query;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class WorkWithDataBase {
    private static WorkWithDataBase instance;

    private WorkWithDataBase() {
        initQuery();
    }

    public static WorkWithDataBase getInstance() {
        if (instance == null) {
            instance = new WorkWithDataBase();
        }
        return instance;
    }

    private DaoCityDao modelCityDao;
    private Query<DaoCity> query;

    private void initQuery() {
        modelCityDao = App.getDaoSession().getDaoCityDao();
        query = modelCityDao.queryBuilder().orderAsc(DaoCityDao.Properties.Name).build();
    }

    public Dao createNewDaoClient(){
        return new Dao(modelCityDao, query);
    }
}
