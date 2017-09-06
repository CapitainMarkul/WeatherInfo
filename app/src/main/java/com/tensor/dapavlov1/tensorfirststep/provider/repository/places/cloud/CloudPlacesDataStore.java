package com.tensor.dapavlov1.tensorfirststep.provider.repository.places.cloud;

import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces.PlacesDataStore;

import java.io.IOException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class CloudPlacesDataStore implements PlacesDataStore {
    private GoogleApiClient googleClient = ApiFactory.getInstance().crateClientGoogleApi();

    @Override
    public Observable<String> getPlaces(String inputText) throws IOException {
        return googleClient.observableGooglePlaceRx(inputText);
    }
}
