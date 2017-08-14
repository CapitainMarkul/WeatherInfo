package com.tensor.dapavlov1.tensorfirststep.interfaces;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public interface Observable {
    void registerObserver(RecyclerViewItemClickListener o);

    void removeObserver(RecyclerViewItemClickListener o);

    void notifyObservers();
}
