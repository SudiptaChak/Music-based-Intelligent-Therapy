package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import java.util.List;

public class AnimatorSetCompat {
  public static void playTogether(AnimatorSet paramAnimatorSet, List<Animator> paramList) {
    int i = paramList.size();
    long l = 0L;
    for (byte b = 0; b < i; b++) {
      Animator animator = paramList.get(b);
      l = Math.max(l, animator.getStartDelay() + animator.getDuration());
    } 
    ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[] { 0, 0 });
    valueAnimator.setDuration(l);
    paramList.add(0, valueAnimator);
    paramAnimatorSet.playTogether(paramList);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\AnimatorSetCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */