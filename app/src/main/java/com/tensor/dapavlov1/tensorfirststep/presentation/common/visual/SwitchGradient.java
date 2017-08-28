package com.tensor.dapavlov1.tensorfirststep.presentation.common.visual;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.R;

/**
 * Created by da.pavlov1 on 08.08.2017.
 */

public class SwitchGradient {
    public static SwitchGradient getInstance() {
        return SwitchGradientLoader.INSTANCE;
    }

    private static final class SwitchGradientLoader {
        private static final SwitchGradient INSTANCE = new SwitchGradient();
    }

    private SwitchGradient() {
    }

    public Drawable getBackground(@Nullable String iconCode) {
        if (iconCode != null) {
            if (iconCode.contains("c01d")) {
                return App.getContext().getResources().getDrawable(R.drawable.background_gradient_cloud_sun, null);
            } else if (iconCode.contains("c01n")) {
                return App.getContext().getResources().getDrawable(R.drawable.background_gradient_night, null);
            } else if (iconCode.contains("c02")
                    || iconCode.contains("a")) {
                return App.getContext().getResources().getDrawable(R.drawable.background_gradient_cloud_sun, null);
            } else if (iconCode.contains("c04")) {
                return App.getContext().getResources().getDrawable(R.drawable.background_gradient_cloud, null);
            } else {
                return App.getContext().getResources().getDrawable(R.drawable.background_gradient_rain, null);
            }
        }
        return null;
    }
}
