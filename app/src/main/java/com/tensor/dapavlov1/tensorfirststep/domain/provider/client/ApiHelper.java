package com.tensor.dapavlov1.tensorfirststep.domain.provider.client;

import android.support.annotation.NonNull;

import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

abstract class ApiHelper {
    protected OkHttpClient okHttpClient;

    protected ApiHelper() {
        okHttpClient = new OkHttpClient();
    }

    protected String createUrl(String scheme, String host, String segment, @NonNull Map<String, String> map) {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .addPathSegments(segment);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.toString();
    }
}
