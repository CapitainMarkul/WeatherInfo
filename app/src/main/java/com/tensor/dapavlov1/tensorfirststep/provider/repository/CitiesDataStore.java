package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows.NetworkConnectionException;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesDataStore {
//    List<City> getCities(List<City> citiesFromDb);
    List<City> getCities() throws NetworkConnectionException, EmptyDbException;
}
