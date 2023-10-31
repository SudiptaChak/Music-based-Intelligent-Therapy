package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.MotionTiming;
import java.util.ArrayList;
import java.util.List;

public class FabTransformationScrimBehavior extends ExpandableTransformationBehavior {
  public static final long COLLAPSE_DELAY = 0L;
  
  public static final long COLLAPSE_DURATION = 150L;
  
  public static final long EXPAND_DELAY = 75L;
  
  public static final long EXPAND_DURATION = 150L;
  
  private final MotionTiming collapseTiming = new MotionTiming(0L, 150L);
  
  private final MotionTiming expandTiming = new MotionTiming(75L, 150L);
  
  public FabTransformationScrimBehavior() {}
  
  public FabTransformationScrimBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void createScrimAnimation(View paramView, boolean paramBoolean1, boolean paramBoolean2, List<Animator> paramList, List<Animator.AnimatorListener> paramList1) {
    ObjectAnimator objectAnimator;
    MotionTiming motionTiming;
    if (paramBoolean1) {
      motionTiming = this.expandTiming;
    } else {
      motionTiming = this.collapseTiming;
    } 
    if (paramBoolean1) {
      if (!paramBoolean2)
        paramView.setAlpha(0.0F); 
      objectAnimator = ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { 1.0F });
    } else {
      objectAnimator = ObjectAnimator.ofFloat(objectAnimator, View.ALPHA, new float[] { 0.0F });
    } 
    motionTiming.apply((Animator)objectAnimator);
    paramList.add(objectAnimator);
  }
  
  public boolean layoutDependsOn(CoordinatorLayout paramCoordinatorLayout, View paramView1, View paramView2) {
    return paramView2 instanceof com.google.android.material.floatingactionbutton.FloatingActionButton;
  }
  
  protected AnimatorSet onCreateExpandedStateChangeAnimation(View paramView1, final View child, final boolean expanded, boolean paramBoolean2) {
    ArrayList<Animator> arrayList = new ArrayList();
    createScrimAnimation(child, expanded, paramBoolean2, arrayList, new ArrayList<Animator.AnimatorListener>());
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final FabTransformationScrimBehavior this$0;
          
          final View val$child;
          
          final boolean val$expanded;
          
          public void onAnimationEnd(Animator param1Animator) {
            if (!expanded)
              child.setVisibility(4); 
          }
          
          public void onAnimationStart(Animator param1Animator) {
            if (expanded)
              child.setVisibility(0); 
          }
        });
    return animatorSet;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, View paramView, MotionEvent paramMotionEvent) {
    return super.onTouchEvent(paramCoordinatorLayout, paramView, paramMotionEvent);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\transformation\FabTransformationScrimBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */