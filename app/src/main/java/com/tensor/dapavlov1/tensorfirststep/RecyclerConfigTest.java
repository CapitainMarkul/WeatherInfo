package com.tensor.dapavlov1.tensorfirststep;

import android.support.v7.widget.RecyclerView;

/**
 * Created by da.pavlov1 on 29.08.2017.
 */
// TODO: 29.08.2017 Сделать конфигуратор Адаптера, проверить как он будет переживать изменения конфигурации
public class RecyclerConfigTest {
//    private final RecyclerView.Adapter adapter;
    //singleton

    public void applyConfig(RecyclerView recyclerView){

    }

    public static class Builder{
        public Builder (RecyclerView.Adapter adapter){

        }

        public RecyclerConfigTest build(){
            return new RecyclerConfigTest();
        }
    }
}
