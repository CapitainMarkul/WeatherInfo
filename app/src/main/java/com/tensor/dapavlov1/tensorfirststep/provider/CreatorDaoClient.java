package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCityDao;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;

import org.greenrobot.greendao.query.Query;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class CreatorDaoClient {
    private static CreatorDaoClient instance;

    private CreatorDaoClient() {
        initQuery();
    }

    public static CreatorDaoClient getInstance() {
        if (instance == null) {
            instance = new CreatorDaoClient();
        }
        return instance;
    }

    private DbCityDao modelCityDao;
    private Query<DbCity> query;

    private void initQuery() {
        modelCityDao = App.getDaoSession().getDbCityDao();
        query = modelCityDao.queryBuilder().orderAsc(DbCityDao.Properties.Name).build();
    }

    DbClient createNewDaoClient() {
        return new DbClient(modelCityDao, query);
    }
}
