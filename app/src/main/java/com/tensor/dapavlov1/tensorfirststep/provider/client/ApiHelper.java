package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.NonNull;

import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

abstract class ApiHelper {
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
