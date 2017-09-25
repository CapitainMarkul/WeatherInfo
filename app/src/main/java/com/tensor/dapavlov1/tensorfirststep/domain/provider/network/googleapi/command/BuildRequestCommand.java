package com.tensor.dapavlov1.tensorfirststep.domain.provider.network.googleapi.command;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkUtils;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by da.pavlov1 on 25.09.2017.
 */

public class BuildRequestCommand extends NetworkUtils
        implements NetworkCommand<Request> {
    private final String inputText;

    public BuildRequestCommand(String inputText) {
        this.inputText = inputText;
    }

    @Override
    public Request execute() {
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

    private Map<String, String> createMapForGoogleApi(String key, String types, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("key", key);
        map.put("input", input);
        return map;
    }
}
