package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public interface PlacesRepository {
    Observable<String> getPlaces(String inputText);
}
