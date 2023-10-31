package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Explode extends Visibility {
  private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
  
  private static final TimeInterpolator sAccelerate;
  
  private static final TimeInterpolator sDecelerate = (TimeInterpolator)new DecelerateInterpolator();
  
  private int[] mTempLoc = new int[2];
  
  static {
    sAccelerate = (TimeInterpolator)new AccelerateInterpolator();
  }
  
  public Explode() {
    setPropagation(new CircularPropagation());
  }
  
  public Explode(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setPropagation(new CircularPropagation());
  }
  
  private static float calculateDistance(float paramFloat1, float paramFloat2) {
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  private static float calculateMaxDistance(View paramView, int paramInt1, int paramInt2) {
    paramInt1 = Math.max(paramInt1, paramView.getWidth() - paramInt1);
    paramInt2 = Math.max(paramInt2, paramView.getHeight() - paramInt2);
    return calculateDistance(paramInt1, paramInt2);
  }
  
  private void calculateOut(View paramView, Rect paramRect, int[] paramArrayOfint) {
    int i;
    int j;
    paramView.getLocationOnScreen(this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    int k = arrayOfInt[0];
    int m = arrayOfInt[1];
    Rect rect = getEpicenter();
    if (rect == null) {
      i = paramView.getWidth() / 2 + k + Math.round(paramView.getTranslationX());
      j = paramView.getHeight() / 2 + m + Math.round(paramView.getTranslationY());
    } else {
      i = rect.centerX();
      j = rect.centerY();
    } 
    int i1 = paramRect.centerX();
    int n = paramRect.centerY();
    float f3 = (i1 - i);
    float f4 = (n - j);
    float f2 = f3;
    float f1 = f4;
    if (f3 == 0.0F) {
      f2 = f3;
      f1 = f4;
      if (f4 == 0.0F) {
        f2 = (float)(Math.random() * 2.0D) - 1.0F;
        f1 = (float)(Math.random() * 2.0D) - 1.0F;
      } 
    } 
    f3 = calculateDistance(f2, f1);
    f2 /= f3;
    f3 = f1 / f3;
    f1 = calculateMaxDistance(paramView, i - k, j - m);
    paramArrayOfint[0] = Math.round(f2 * f1);
    paramArrayOfint[1] = Math.round(f1 * f3);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    view.getLocationOnScreen(this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    int m = arrayOfInt[0];
    int j = arrayOfInt[1];
    int i = view.getWidth();
    int k = view.getHeight();
    paramTransitionValues.values.put("android:explode:screenBounds", new Rect(m, j, i + m, k + j));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    super.captureEndValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    if (paramTransitionValues2 == null)
      return null; 
    Rect rect = (Rect)paramTransitionValues2.values.get("android:explode:screenBounds");
    float f3 = paramView.getTranslationX();
    float f1 = paramView.getTranslationY();
    calculateOut((View)paramViewGroup, rect, this.mTempLoc);
    int[] arrayOfInt = this.mTempLoc;
    float f4 = arrayOfInt[0];
    float f2 = arrayOfInt[1];
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues2, rect.left, rect.top, f3 + f4, f1 + f2, f3, f1, sDecelerate);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    float f1;
    float f2;
    if (paramTransitionValues1 == null)
      return null; 
    Rect rect = (Rect)paramTransitionValues1.values.get("android:explode:screenBounds");
    int i = rect.left;
    int j = rect.top;
    float f3 = paramView.getTranslationX();
    float f4 = paramView.getTranslationY();
    int[] arrayOfInt2 = (int[])paramTransitionValues1.view.getTag(R.id.transition_position);
    if (arrayOfInt2 != null) {
      f1 = (arrayOfInt2[0] - rect.left) + f3;
      f2 = (arrayOfInt2[1] - rect.top) + f4;
      rect.offsetTo(arrayOfInt2[0], arrayOfInt2[1]);
    } else {
      f1 = f3;
      f2 = f4;
    } 
    calculateOut((View)paramViewGroup, rect, this.mTempLoc);
    int[] arrayOfInt1 = this.mTempLoc;
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues1, i, j, f3, f4, f1 + arrayOfInt1[0], f2 + arrayOfInt1[1], sAccelerate);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Explode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */