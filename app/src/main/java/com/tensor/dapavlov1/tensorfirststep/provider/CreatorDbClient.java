package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCityDao;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;

import org.greenrobot.greendao.query.Query;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

public class CreatorDbClient {
    private static CreatorDbClient instance;

    private CreatorDbClient() {
        initQuery();
    }

    public static CreatorDbClient getInstance() {
        if (instance == null) {
            instance = new CreatorDbClient();
        }
        return instance;
    }

    private DbCityDao modelCityDao;
    private Query<DbCity> query;

    private void initQuery() {
        modelCityDao = App.getDaoSession().getDbCityDao();
        query = modelCityDao.queryBuilder().orderAsc(DbCityDao.Properties.Name).build();
    }

    public DbClient createNewDaoClient() {
        return new DbClient(modelCityDao, query);
    }
}
