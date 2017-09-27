package com.tensor.dapavlov1.tensorfirststep.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tensor.dapavlov1.tensorfirststep.App;

/**
 * Created by da.pavlov1 on 22.09.2017.
 */

public class NetworkHelper {
    protected NetworkHelper() {

    }

    public static boolean isOnline() {
        Context context = App.get();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
