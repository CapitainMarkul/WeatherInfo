package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DaoClient;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCityDao;

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

    private DaoCityDao modelCityDao;
    private Query<DaoCity> query;

    private void initQuery() {
        modelCityDao = App.getDaoSession().getDaoCityDao();
        query = modelCityDao.queryBuilder().orderAsc(DaoCityDao.Properties.Name).build();
    }

    DaoClient createNewDaoClient() {
        return new DaoClient(modelCityDao, query);
    }
}
