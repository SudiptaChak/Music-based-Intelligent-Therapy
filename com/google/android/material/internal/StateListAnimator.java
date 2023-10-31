package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
  private final Animator.AnimatorListener animationListener = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
      final StateListAnimator this$0;
      
      public void onAnimationEnd(Animator param1Animator) {
        if (StateListAnimator.this.runningAnimator == param1Animator)
          StateListAnimator.this.runningAnimator = null; 
      }
    };
  
  private Tuple lastMatch = null;
  
  ValueAnimator runningAnimator = null;
  
  private final ArrayList<Tuple> tuples = new ArrayList<Tuple>();
  
  private void cancel() {
    ValueAnimator valueAnimator = this.runningAnimator;
    if (valueAnimator != null) {
      valueAnimator.cancel();
      this.runningAnimator = null;
    } 
  }
  
  private void start(Tuple paramTuple) {
    ValueAnimator valueAnimator = paramTuple.animator;
    this.runningAnimator = valueAnimator;
    valueAnimator.start();
  }
  
  public void addState(int[] paramArrayOfint, ValueAnimator paramValueAnimator) {
    Tuple tuple = new Tuple(paramArrayOfint, paramValueAnimator);
    paramValueAnimator.addListener(this.animationListener);
    this.tuples.add(tuple);
  }
  
  public void jumpToCurrentState() {
    ValueAnimator valueAnimator = this.runningAnimator;
    if (valueAnimator != null) {
      valueAnimator.end();
      this.runningAnimator = null;
    } 
  }
  
  public void setState(int[] paramArrayOfint) {
    int i = this.tuples.size();
    byte b = 0;
    while (true) {
      if (b < i) {
        Tuple tuple1 = this.tuples.get(b);
        if (StateSet.stateSetMatches(tuple1.specs, paramArrayOfint)) {
          Tuple tuple2 = tuple1;
          break;
        } 
        b++;
        continue;
      } 
      paramArrayOfint = null;
      break;
    } 
    Tuple tuple = this.lastMatch;
    if (paramArrayOfint == tuple)
      return; 
    if (tuple != null)
      cancel(); 
    this.lastMatch = (Tuple)paramArrayOfint;
    if (paramArrayOfint != null)
      start((Tuple)paramArrayOfint); 
  }
  
  static class Tuple {
    final ValueAnimator animator;
    
    final int[] specs;
    
    Tuple(int[] param1ArrayOfint, ValueAnimator param1ValueAnimator) {
      this.specs = param1ArrayOfint;
      this.animator = param1ValueAnimator;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\StateListAnimator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */