package com.tensor.dapavlov1.tensorfirststep.domain.provider.network.googleapi.command;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 25.09.2017.
 */

public class CallCommand implements NetworkCommand<Observable<String>>{

    private final OkHttpClient okHttpClient;
    private final Request request;

    public CallCommand(OkHttpClient okHttpClient, Request request) {
        this.okHttpClient = okHttpClient;
        this.request = request;
    }

    @Override
    public Observable<String> execute() {
        return Observable.create(source -> {
            Call call = okHttpClient.newCall(request);
//            //отменяем запрос, если произошла отписка
//            source.setCancellable(call::cancel);
            // FIXME: 12.09.2017 Здесь ошибка ранней отмены запроса (Когда начинаем стирать символы) (Ошибка, если приходит пустота?)

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
