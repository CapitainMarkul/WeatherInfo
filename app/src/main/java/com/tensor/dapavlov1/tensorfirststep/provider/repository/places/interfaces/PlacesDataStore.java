package com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public interface PlacesDataStore {
    List<String> getPlaces(String inputText) throws IOException;
}
