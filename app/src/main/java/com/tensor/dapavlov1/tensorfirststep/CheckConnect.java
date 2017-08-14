package com.tensor.dapavlov1.tensorfirststep;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class CheckConnect {
    private static CheckConnect instance;

    public static CheckConnect getInstance() {
        if (instance == null) {
            instance = new CheckConnect();
        }
        return instance;
    }

    private CheckConnect() {
    }

    public boolean isOnline(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
