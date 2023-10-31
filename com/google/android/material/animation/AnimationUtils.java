package com.google.android.material.animation;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimationUtils {
  public static final TimeInterpolator DECELERATE_INTERPOLATOR;
  
  public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR;
  
  public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR;
  
  public static final TimeInterpolator LINEAR_INTERPOLATOR = (TimeInterpolator)new LinearInterpolator();
  
  public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR;
  
  static {
    FAST_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new FastOutSlowInInterpolator();
    FAST_OUT_LINEAR_IN_INTERPOLATOR = (TimeInterpolator)new FastOutLinearInInterpolator();
    LINEAR_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new LinearOutSlowInInterpolator();
    DECELERATE_INTERPOLATOR = (TimeInterpolator)new DecelerateInterpolator();
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return paramFloat1 + paramFloat3 * (paramFloat2 - paramFloat1);
  }
  
  public static int lerp(int paramInt1, int paramInt2, float paramFloat) {
    return paramInt1 + Math.round(paramFloat * (paramInt2 - paramInt1));
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\AnimationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */