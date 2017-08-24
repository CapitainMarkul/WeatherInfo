package com.tensor.dapavlov1.tensorfirststep.provider.repository.places;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.cloud.CloudPlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces.PlacesDataStore;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class PlacesDataStoreFactory extends CheckConnect {
    private static final PlacesDataStoreFactory ourInstance = new PlacesDataStoreFactory();

    public static PlacesDataStoreFactory getInstance() {
        return ourInstance;
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
