package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public interface DbCommand<ResultType> {
    ResultType execute(DaoSession daoSession);

//    void undo();
}
