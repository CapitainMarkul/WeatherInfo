package com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces;

import java.util.List;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public interface PlacesRepository {
    List<String> getPlaces(String inputText);
}
