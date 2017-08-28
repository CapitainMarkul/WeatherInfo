package com.tensor.dapavlov1.tensorfirststep.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.util.List;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */
//, IAddCityIntent
public interface FavoritePresenter extends Loading, ShowMessage {
    void refreshAdapter(List<City> weathers);
//
//    void showEmptyCard();
//
//    void hideEmptyCard();
}
