package com.tensor.dapavlov1.tensorfirststep.domain.provider.common.deleteobservable;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

/**
 * Created by da.pavlov1 on 08.09.2017.
 */

public interface DelObserver {
    void sendDelResult(boolean isSuccess, CityView deletedCity);
}
