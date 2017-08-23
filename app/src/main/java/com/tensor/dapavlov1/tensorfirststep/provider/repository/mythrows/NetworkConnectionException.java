package com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.util.List;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class NetworkConnectionException extends Exception {
    private List<City> oldCitiesInfo;

    public NetworkConnectionException (List<City> oldCitiesInfo) {
        this.oldCitiesInfo = oldCitiesInfo;
    }

    public List<City> getOldCitiesInfo() {
        return oldCitiesInfo;
    }
}
