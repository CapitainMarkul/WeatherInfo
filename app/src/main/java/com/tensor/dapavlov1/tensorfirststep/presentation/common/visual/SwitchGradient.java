package com.tensor.dapavlov1.tensorfirststep.presentation.common.visual;

import android.graphics.drawable.Drawable;

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

    public Drawable getBackgroung(String iconCode) {
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
//        switch (iconCode.substring(0,1)){
//            case
//            case "t": return App.getContext().getResources().getDrawable(R.drawable.background_gradient_rain, null);
//            case "d": return App.getContext().getResources().getDrawable(R.drawable.background_gradient_rain, null);
//            case "s": return App.getContext().getResources().getDrawable(R.drawable.background_gradient_rain, null);
//        }
    }

//    public ShapeDrawable createRandomGradient(int widthElement, int heightElement) {
//        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
//        mDrawable.getPaint().setShader(new LinearGradient(0, 0, 0, 350,
//                Color.rgb(genInt(), genInt(), genInt()), Color.parseColor("#80FFFFFF"),
//                Shader.TileMode.REPEAT));
//
//        return mDrawable;
//    }
//
//    private int genInt() {
//        Random random = new Random();
//        return random.nextInt(255);
//    }
}
