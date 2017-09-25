package com.tensor.dapavlov1.tensorfirststep.domain.provider.network.googleapi;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkUtils;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.googleapi.command.BuildRequestCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.googleapi.command.CallCommand;


import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class GoogleApiClient extends NetworkUtils {

    private final OkHttpClient okHttpClient;

    @Inject
    public GoogleApiClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public Observable<String> observableGooglePlaceRx(@NonNull String inputText) {
        NetworkCommand<Observable<String>> callCommand = new CallCommand(okHttpClient, buildRequest(inputText));
        return callCommand.execute();
    }

    private Request buildRequest(@NonNull String inputText){
        NetworkCommand<Request> buildRequestCommand = new BuildRequestCommand(inputText);
        return buildRequestCommand.execute();
    }
}
