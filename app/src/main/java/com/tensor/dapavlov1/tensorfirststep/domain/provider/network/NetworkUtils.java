package com.tensor.dapavlov1.tensorfirststep.domain.provider.network;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.core.utils.NetworkHelper;

import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public abstract class NetworkUtils extends NetworkHelper {
    protected static String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }

    protected static String createUrl(String scheme, String host, String segment, @NonNull Map<String, String> map) {
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
