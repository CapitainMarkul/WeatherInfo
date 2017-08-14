package com.tensor.dapavlov1.tensorfirststep.interfaces;

import android.view.View;

import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoCity;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public interface RecyclerViewItemClickListener {
    void onClickDelCityFromDb(final int position);
}
