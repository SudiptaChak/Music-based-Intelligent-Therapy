package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;

public class ChangeClipBounds extends Transition {
  private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
  
  private static final String PROPNAME_CLIP = "android:clipBounds:clip";
  
  private static final String[] sTransitionProperties = new String[] { "android:clipBounds:clip" };
  
  public ChangeClipBounds() {}
  
  public ChangeClipBounds(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    if (view.getVisibility() == 8)
      return; 
    Rect rect = ViewCompat.getClipBounds(view);
    paramTransitionValues.values.put("android:clipBounds:clip", rect);
    if (rect == null) {
      Rect rect1 = new Rect(0, 0, view.getWidth(), view.getHeight());
      paramTransitionValues.values.put("android:clipBounds:bounds", rect1);
    } 
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    ObjectAnimator objectAnimator;
    ViewGroup viewGroup = null;
    paramViewGroup = viewGroup;
    if (paramTransitionValues1 != null) {
      paramViewGroup = viewGroup;
      if (paramTransitionValues2 != null) {
        paramViewGroup = viewGroup;
        if (paramTransitionValues1.values.containsKey("android:clipBounds:clip"))
          if (!paramTransitionValues2.values.containsKey("android:clipBounds:clip")) {
            paramViewGroup = viewGroup;
          } else {
            Rect rect1;
            Rect rect2;
            boolean bool;
            Rect rect4 = (Rect)paramTransitionValues1.values.get("android:clipBounds:clip");
            Rect rect3 = (Rect)paramTransitionValues2.values.get("android:clipBounds:clip");
            if (rect3 == null) {
              bool = true;
            } else {
              bool = false;
            } 
            if (rect4 == null && rect3 == null)
              return null; 
            if (rect4 == null) {
              rect2 = (Rect)paramTransitionValues1.values.get("android:clipBounds:bounds");
              rect1 = rect3;
            } else {
              rect2 = rect4;
              rect1 = rect3;
              if (rect3 == null) {
                rect1 = (Rect)paramTransitionValues2.values.get("android:clipBounds:bounds");
                rect2 = rect4;
              } 
            } 
            if (rect2.equals(rect1))
              return null; 
            ViewCompat.setClipBounds(paramTransitionValues2.view, rect2);
            RectEvaluator rectEvaluator = new RectEvaluator(new Rect());
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofObject(paramTransitionValues2.view, ViewUtils.CLIP_BOUNDS, rectEvaluator, (Object[])new Rect[] { rect2, rect1 });
            objectAnimator = objectAnimator1;
            if (bool) {
              objectAnimator1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                    final ChangeClipBounds this$0;
                    
                    final View val$endView;
                    
                    public void onAnimationEnd(Animator param1Animator) {
                      ViewCompat.setClipBounds(endView, null);
                    }
                  });
              objectAnimator = objectAnimator1;
            } 
          }  
      } 
    } 
    return (Animator)objectAnimator;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ChangeClipBounds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */