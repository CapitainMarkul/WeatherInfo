package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class GoogleApiClient extends ApiHelper {
    private OkHttpClient okHttpClient;

    public GoogleApiClient() {
        okHttpClient = new OkHttpClient();
    }

    private Map<String, String> createMapForGoogleApi(String key, String types, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("key", key);
        map.put("input", input);
        return map;
    }

    private Request createRequest(@NonNull String inputText) {
        return new Request.Builder().url(
                createUrl(
                        BuildConfig.GOOGLE_API_SCHEME,
                        BuildConfig.GOOGLE_API_HOST,
                        BuildConfig.GOOGLE_API_SEGMENTS,
                        createMapForGoogleApi(
                                BuildConfig.GOOGLE_API_KEY,
                                BuildConfig.GOOGLE_API_TYPES,
                                inputText
                        ))).build();
    }

    public Observable<String> observableGooglePlaceRx(@NonNull String inputText) {
        return Observable.create(source -> {
            Call call = okHttpClient.newCall(createRequest(inputText));

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    source.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    source.onNext(response.body().string());
                    source.onComplete();
                }
            });
        });
    }
}
