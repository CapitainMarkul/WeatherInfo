package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandHelper;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class GetCitiesNameCommand extends DbCommandHelper implements DbCommand<List<String>> {

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
