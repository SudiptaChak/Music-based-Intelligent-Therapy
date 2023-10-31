package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ChangeScroll extends Transition {
  private static final String[] PROPERTIES = new String[] { "android:changeScroll:x", "android:changeScroll:y" };
  
  private static final String PROPNAME_SCROLL_X = "android:changeScroll:x";
  
  private static final String PROPNAME_SCROLL_Y = "android:changeScroll:y";
  
  public ChangeScroll() {}
  
  public ChangeScroll(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    paramTransitionValues.values.put("android:changeScroll:x", Integer.valueOf(paramTransitionValues.view.getScrollX()));
    paramTransitionValues.values.put("android:changeScroll:y", Integer.valueOf(paramTransitionValues.view.getScrollY()));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    Animator animator;
    ViewGroup viewGroup = null;
    TransitionValues transitionValues = null;
    paramViewGroup = viewGroup;
    if (paramTransitionValues1 != null)
      if (paramTransitionValues2 == null) {
        paramViewGroup = viewGroup;
      } else {
        ObjectAnimator objectAnimator;
        View view = paramTransitionValues2.view;
        int i = ((Integer)paramTransitionValues1.values.get("android:changeScroll:x")).intValue();
        int k = ((Integer)paramTransitionValues2.values.get("android:changeScroll:x")).intValue();
        int m = ((Integer)paramTransitionValues1.values.get("android:changeScroll:y")).intValue();
        int j = ((Integer)paramTransitionValues2.values.get("android:changeScroll:y")).intValue();
        if (i != k) {
          view.setScrollX(i);
          ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(view, "scrollX", new int[] { i, k });
        } else {
          paramViewGroup = null;
        } 
        paramTransitionValues1 = transitionValues;
        if (m != j) {
          view.setScrollY(m);
          objectAnimator = ObjectAnimator.ofInt(view, "scrollY", new int[] { m, j });
        } 
        animator = TransitionUtils.mergeAnimators((Animator)paramViewGroup, (Animator)objectAnimator);
      }  
    return animator;
  }
  
  public String[] getTransitionProperties() {
    return PROPERTIES;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ChangeScroll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */