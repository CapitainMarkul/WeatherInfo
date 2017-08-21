package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class GoogleApi extends ApiHelper {
    private OkHttpClient okHttpClient;

    public GoogleApi() {
        okHttpClient = new OkHttpClient();
    }

    @Nullable
    public String getJsonFromGooglePlaceApi(String inputText) throws IOException {
        Request request = new Request.Builder().url(
                createUrl(
                        BuildConfig.GOOGLE_API_SCHEME,
                        BuildConfig.GOOGLE_API_HOST,
                        BuildConfig.GOOGLE_API_SEGMENTS,
                        createMapForGoogleApi(
                                BuildConfig.GOOGLE_API_KEY,
                                BuildConfig.GOOGLE_API_TYPES,
                                inputText
                        ))).build();
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    private Map<String, String> createMapForGoogleApi(String key, String types, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("key", key);
        map.put("input", input);
        return map;
    }
}
