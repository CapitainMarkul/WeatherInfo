package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.cloud;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.ApiFactory;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.places.interfaces.PlacesDataStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;

import java.io.IOException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class CloudPlacesDataStore implements PlacesDataStore {
    private final GoogleApiClient googleClient;

    public CloudPlacesDataStore(GoogleApiClient googleApiClient) {
        this.googleClient = googleApiClient;
    }

    @Override
    public Observable<String> getPlaces(String inputText) throws IOException {
        return googleClient.observableGooglePlaceRx(inputText);
    }
}
