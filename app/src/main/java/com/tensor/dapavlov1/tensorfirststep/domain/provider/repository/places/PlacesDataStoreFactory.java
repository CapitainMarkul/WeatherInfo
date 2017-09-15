package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.cloud.CloudPlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces.PlacesDataStore;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class PlacesDataStoreFactory extends CheckConnect {
    public static PlacesDataStoreFactory getInstance() {
        return PlacesDataStoreFactoryLoader.INSTANCE;
    }

    private static final class PlacesDataStoreFactoryLoader {
        private static final PlacesDataStoreFactory INSTANCE = new PlacesDataStoreFactory();
    }

    private PlacesDataStoreFactory() {
    }

    public PlacesDataStore createPlacesDataStore() throws NetworkConnectException {
        if (isOnline(App.getContext())) {
            return new CloudPlacesDataStore();
        } else {
            throw new NetworkConnectException();
        }
    }
}
