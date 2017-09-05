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
    public static CreatorDbClient getInstance() {
        return CreatorDbClientLoader.INSTANCE;
    }

    private static final class CreatorDbClientLoader {
        private static final CreatorDbClient INSTANCE = new CreatorDbClient();
    }

    private CreatorDbClient() {
        initQuery();
    }

//    private DbCityDao modelCityDao;
//    private Query<DbCity> query;

    private void initQuery() {
//        modelCityDao = App.getDaoSession();
//        query = modelCityDao.queryBuilder().orderAsc(DbCityDao.Properties.Name).build();
    }

    public DbClient createNewDaoClient() {
//        return new DbClient(modelCityDao, query);
        return new DbClient();
    }
}
