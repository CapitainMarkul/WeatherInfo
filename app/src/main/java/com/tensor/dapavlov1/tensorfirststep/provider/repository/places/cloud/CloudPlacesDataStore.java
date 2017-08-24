package com.tensor.dapavlov1.tensorfirststep.provider.repository.places.cloud;

import com.tensor.dapavlov1.tensorfirststep.provider.ApiFabric;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.interfaces.PlacesDataStore;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 24.08.2017.
 */

public class CloudPlacesDataStore implements PlacesDataStore {
    private GoogleApiClient googleClient = ApiFabric.getInstance().crateClientGoogleApi();

    @Override
    public List<String> getPlaces(String inputText) throws IOException {
        return GsonFactory.getInstance().getPlacesName(
                googleClient.getJsonFromGooglePlaceApi(inputText));
    }
}