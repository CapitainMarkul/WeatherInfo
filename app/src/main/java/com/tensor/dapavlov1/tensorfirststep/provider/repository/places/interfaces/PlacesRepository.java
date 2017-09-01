package com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public interface PlacesRepository {
    Observable<String> getPlaces(String inputText);
}
