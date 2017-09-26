package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandUtils;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.exceptions.EmptyDbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class GetCitiesNameCommand extends DbCommandUtils implements DbCommand<List<String>> {

    @Override
    public List<String> execute(DaoSession daoSession) {
        List<String> cityNames = new ArrayList<>();

        try {
            for (CityDb item : loadListAllCities(daoSession)) {
                cityNames.add(item.getName());
            }
        } catch (EmptyDbException e) {
            return null;
        }

        return cityNames;
    }
}
