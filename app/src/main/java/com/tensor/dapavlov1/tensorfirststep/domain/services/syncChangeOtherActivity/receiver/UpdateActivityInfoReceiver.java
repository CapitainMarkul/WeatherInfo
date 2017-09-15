package com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class UpdateActivityInfoReceiver extends BroadcastReceiver {
    public static UpdateActivityInfoReceiver getInstance() {
        return UpdateActivityInfoReceiverLoader.INSTANCE;
    }

    private static final class UpdateActivityInfoReceiverLoader {
        private static final UpdateActivityInfoReceiver INSTANCE = new UpdateActivityInfoReceiver();
    }

    private UpdateActivityInfoReceiver() {
    }

    private Receiver receiver;

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void reset() {
        receiver = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (receiver != null) {
            receiver.onReceiveResult(intent);
        }
    }

    public interface Receiver {
        void onReceiveResult(Intent intent);
    }
}
