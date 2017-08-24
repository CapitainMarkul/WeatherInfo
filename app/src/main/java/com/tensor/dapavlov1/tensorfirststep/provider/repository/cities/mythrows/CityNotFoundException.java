package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CityNotFoundException extends Exception {
    private City city;

    public CityNotFoundException(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
