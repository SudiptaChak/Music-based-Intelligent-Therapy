package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
  private static final int[] FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] HOVERED_STATE_SET;
  
  private static final int[] PRESSED_STATE_SET;
  
  private static final int[] SELECTED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
  
  private static final int[] SELECTED_HOVERED_STATE_SET;
  
  private static final int[] SELECTED_PRESSED_STATE_SET;
  
  private static final int[] SELECTED_STATE_SET;
  
  public static final boolean USE_FRAMEWORK_RIPPLE;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_FRAMEWORK_RIPPLE = bool;
    PRESSED_STATE_SET = new int[] { 16842919 };
    HOVERED_FOCUSED_STATE_SET = new int[] { 16843623, 16842908 };
    FOCUSED_STATE_SET = new int[] { 16842908 };
    HOVERED_STATE_SET = new int[] { 16843623 };
    SELECTED_PRESSED_STATE_SET = new int[] { 16842913, 16842919 };
    SELECTED_HOVERED_FOCUSED_STATE_SET = new int[] { 16842913, 16843623, 16842908 };
    SELECTED_FOCUSED_STATE_SET = new int[] { 16842913, 16842908 };
    SELECTED_HOVERED_STATE_SET = new int[] { 16842913, 16843623 };
    SELECTED_STATE_SET = new int[] { 16842913 };
  }
  
  public static ColorStateList convertToRippleDrawableColor(ColorStateList paramColorStateList) {
    if (USE_FRAMEWORK_RIPPLE) {
      int[] arrayOfInt10 = SELECTED_STATE_SET;
      int i5 = getColorForState(paramColorStateList, SELECTED_PRESSED_STATE_SET);
      int[] arrayOfInt11 = StateSet.NOTHING;
      int i4 = getColorForState(paramColorStateList, PRESSED_STATE_SET);
      return new ColorStateList(new int[][] { arrayOfInt10, arrayOfInt11 }, new int[] { i5, i4 });
    } 
    int[] arrayOfInt4 = SELECTED_PRESSED_STATE_SET;
    int j = getColorForState(paramColorStateList, arrayOfInt4);
    int[] arrayOfInt7 = SELECTED_HOVERED_FOCUSED_STATE_SET;
    int n = getColorForState(paramColorStateList, arrayOfInt7);
    int[] arrayOfInt5 = SELECTED_FOCUSED_STATE_SET;
    int i1 = getColorForState(paramColorStateList, arrayOfInt5);
    int[] arrayOfInt6 = SELECTED_HOVERED_STATE_SET;
    int i = getColorForState(paramColorStateList, arrayOfInt6);
    int[] arrayOfInt8 = SELECTED_STATE_SET;
    int[] arrayOfInt3 = PRESSED_STATE_SET;
    int m = getColorForState(paramColorStateList, arrayOfInt3);
    int[] arrayOfInt1 = HOVERED_FOCUSED_STATE_SET;
    int i2 = getColorForState(paramColorStateList, arrayOfInt1);
    int[] arrayOfInt9 = FOCUSED_STATE_SET;
    int k = getColorForState(paramColorStateList, arrayOfInt9);
    int[] arrayOfInt2 = HOVERED_STATE_SET;
    int i3 = getColorForState(paramColorStateList, arrayOfInt2);
    return new ColorStateList(new int[][] { arrayOfInt4, arrayOfInt7, arrayOfInt5, arrayOfInt6, arrayOfInt8, arrayOfInt3, arrayOfInt1, arrayOfInt9, arrayOfInt2, StateSet.NOTHING }, new int[] { j, n, i1, i, 0, m, i2, k, i3, 0 });
  }
  
  private static int doubleAlpha(int paramInt) {
    return ColorUtils.setAlphaComponent(paramInt, Math.min(Color.alpha(paramInt) * 2, 255));
  }
  
  private static int getColorForState(ColorStateList paramColorStateList, int[] paramArrayOfint) {
    byte b;
    if (paramColorStateList != null) {
      b = paramColorStateList.getColorForState(paramArrayOfint, paramColorStateList.getDefaultColor());
    } else {
      b = 0;
    } 
    int i = b;
    if (USE_FRAMEWORK_RIPPLE)
      i = doubleAlpha(b); 
    return i;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\ripple\RippleUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */