package com.tensor.dapavlov1.tensorfirststep.provider;

/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class Callback<T> {
    public void onSuccess(T result){
    }
    public void onSuccess(T result, boolean isFavorite){
    }
    public void onFail(String message) {

    }
    public void onFail() {

    }
}
