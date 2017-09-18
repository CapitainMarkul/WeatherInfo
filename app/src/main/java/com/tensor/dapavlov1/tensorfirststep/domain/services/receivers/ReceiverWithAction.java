package com.tensor.dapavlov1.tensorfirststep.domain.services.receivers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * Created by da.pavlov1 on 18.09.2017.
 */

public class ReceiverWithAction {
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    public ReceiverWithAction(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter intentFilter) {
        this.receiver = receiver;
        this.intentFilter = intentFilter;
    }

    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
