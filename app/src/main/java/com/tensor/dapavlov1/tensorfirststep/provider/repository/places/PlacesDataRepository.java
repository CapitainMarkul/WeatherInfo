package com.tensor.dapavlov1.tensorfirststep.provider.repository.places;

import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces.PlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces.PlacesRepository;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class PlacesDataRepository implements PlacesRepository {
    public static PlacesDataRepository getInstance() {
        return PlacesDataRepositoryLoader.INSTANCE;
    }

    private static final class PlacesDataRepositoryLoader {
        private static final PlacesDataRepository INSTANCE = new PlacesDataRepository();
    }

    private PlacesDataRepository() {
    }

    private PlacesDataStoreFactory placesDataStoreFactory = PlacesDataStoreFactory.getInstance();

    @Override
    public List<String> getPlaces(String inputText) {
        try {
            PlacesDataStore placesDataStore = placesDataStoreFactory.createPlacesDataStore();
            return placesDataStore.getPlaces(inputText);
        } catch (NetworkConnectException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
