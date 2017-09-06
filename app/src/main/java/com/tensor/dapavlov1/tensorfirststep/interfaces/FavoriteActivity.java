package com.tensor.dapavlov1.tensorfirststep.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

import java.util.List;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */
//, IAddCityIntent
public interface FavoriteActivity extends ShowMessage {
    void setItems(List<CityView> weathers);
}
