package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces;

import java.io.IOException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public interface PlacesDataStore {
    Observable<String> getPlaces(String inputText) throws IOException;
}
