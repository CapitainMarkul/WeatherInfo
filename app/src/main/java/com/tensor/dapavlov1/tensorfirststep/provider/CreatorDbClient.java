package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

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
//    private Query<CityDb> query;

    private void initQuery() {
//        modelCityDao = App.getDaoSession();
//        query = modelCityDao.queryBuilder().orderAsc(DbCityDao.Properties.Name).build();
    }

    public DbClient createNewDaoClient() {
//        return new DbClient(modelCityDao, query);
        return new DbClient();
    }
}
