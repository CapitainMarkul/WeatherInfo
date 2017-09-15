package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

/**
 * Created by da.pavlov1 on 08.09.2017.
 */

public interface DelObserver {
    void deleteResult(boolean isSuccess, CityView deletedCity);
}
