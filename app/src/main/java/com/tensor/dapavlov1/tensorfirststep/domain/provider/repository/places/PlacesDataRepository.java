package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.cloud.CloudPlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces.PlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces.PlacesRepository;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class PlacesDataRepository extends CheckConnect implements PlacesRepository {
    public static PlacesDataRepository getInstance() {
        return PlacesDataRepositoryLoader.INSTANCE;
    }

    private static final class PlacesDataRepositoryLoader {
        private static final PlacesDataRepository INSTANCE = new PlacesDataRepository();
    }

    private PlacesDataRepository() {
        inject();
    }

    @Inject GoogleApiClient googleApiClient;
//    private PlacesDataStoreFactory placesDataStoreFactory = PlacesDataStoreFactory.getInstance();

    private void inject() {
        App.get().businessComponent().inject(this);
    }

    @Override
    public Observable<String> getPlaces(String inputText) {
        try {
            PlacesDataStore placesDataStore = createPlacesDataStore();
            return placesDataStore.getPlaces(inputText);
        } catch (NetworkConnectException e) {
            return Observable.empty();
        } catch (IOException e) {
            return Observable.empty();
        }
    }

    private PlacesDataStore createPlacesDataStore() throws NetworkConnectException {
        if (isOnline(App.get())) {
            return new CloudPlacesDataStore(googleApiClient);
        } else {
            throw new NetworkConnectException();
        }
    }
}
