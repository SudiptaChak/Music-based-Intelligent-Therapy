package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {
  private static final int INVALID_POINTER = -1;
  
  private int activePointerId = -1;
  
  private Runnable flingRunnable;
  
  private boolean isBeingDragged;
  
  private int lastMotionY;
  
  OverScroller scroller;
  
  private int touchSlop = -1;
  
  private VelocityTracker velocityTracker;
  
  public HeaderBehavior() {}
  
  public HeaderBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  private void ensureVelocityTracker() {
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
  }
  
  boolean canDragView(V paramV) {
    return false;
  }
  
  final boolean fling(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, float paramFloat) {
    FlingRunnable flingRunnable;
    Runnable runnable = this.flingRunnable;
    if (runnable != null) {
      paramV.removeCallbacks(runnable);
      this.flingRunnable = null;
    } 
    if (this.scroller == null)
      this.scroller = new OverScroller(paramV.getContext()); 
    this.scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(paramFloat), 0, 0, paramInt1, paramInt2);
    if (this.scroller.computeScrollOffset()) {
      flingRunnable = new FlingRunnable(paramCoordinatorLayout, paramV);
      this.flingRunnable = flingRunnable;
      ViewCompat.postOnAnimation((View)paramV, flingRunnable);
      return true;
    } 
    onFlingFinished((CoordinatorLayout)flingRunnable, paramV);
    return false;
  }
  
  int getMaxDragOffset(V paramV) {
    return -paramV.getHeight();
  }
  
  int getScrollRangeForDragFling(V paramV) {
    return paramV.getHeight();
  }
  
  int getTopBottomOffsetForScrollingSibling() {
    return getTopAndBottomOffset();
  }
  
  void onFlingFinished(CoordinatorLayout paramCoordinatorLayout, V paramV) {}
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (this.touchSlop < 0)
      this.touchSlop = ViewConfiguration.get(paramCoordinatorLayout.getContext()).getScaledTouchSlop(); 
    if (paramMotionEvent.getAction() == 2 && this.isBeingDragged)
      return true; 
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i != 1)
        if (i != 2) {
          if (i != 3)
            VelocityTracker velocityTracker1 = this.velocityTracker; 
        } else {
          i = this.activePointerId;
          if (i != -1) {
            i = paramMotionEvent.findPointerIndex(i);
            if (i != -1) {
              i = (int)paramMotionEvent.getY(i);
              if (Math.abs(i - this.lastMotionY) > this.touchSlop) {
                this.isBeingDragged = true;
                this.lastMotionY = i;
              } 
            } 
          } 
          VelocityTracker velocityTracker1 = this.velocityTracker;
        }  
      this.isBeingDragged = false;
      this.activePointerId = -1;
      velocityTracker = this.velocityTracker;
      if (velocityTracker != null) {
        velocityTracker.recycle();
        this.velocityTracker = null;
      } 
    } else {
      this.isBeingDragged = false;
      i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if (canDragView(paramV) && velocityTracker.isPointInChildBounds((View)paramV, i, j)) {
        this.lastMotionY = j;
        this.activePointerId = paramMotionEvent.getPointerId(0);
        ensureVelocityTracker();
      } 
    } 
    VelocityTracker velocityTracker = this.velocityTracker;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (this.touchSlop < 0)
      this.touchSlop = ViewConfiguration.get(paramCoordinatorLayout.getContext()).getScaledTouchSlop(); 
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3)
            velocityTracker = this.velocityTracker; 
        } else {
          i = paramMotionEvent.findPointerIndex(this.activePointerId);
          if (i == -1)
            return false; 
          int k = (int)paramMotionEvent.getY(i);
          int j = this.lastMotionY - k;
          i = j;
          if (!this.isBeingDragged) {
            int n = Math.abs(j);
            int m = this.touchSlop;
            i = j;
            if (n > m) {
              this.isBeingDragged = true;
              if (j > 0) {
                i = j - m;
              } else {
                i = j + m;
              } 
            } 
          } 
          if (this.isBeingDragged) {
            this.lastMotionY = k;
            scroll((CoordinatorLayout)velocityTracker, paramV, i, getMaxDragOffset(paramV), 0);
          } 
          velocityTracker = this.velocityTracker;
        } 
      } else {
        VelocityTracker velocityTracker1 = this.velocityTracker;
        if (velocityTracker1 != null) {
          velocityTracker1.addMovement(paramMotionEvent);
          this.velocityTracker.computeCurrentVelocity(1000);
          float f = this.velocityTracker.getYVelocity(this.activePointerId);
          fling((CoordinatorLayout)velocityTracker, paramV, -getScrollRangeForDragFling(paramV), 0, f);
        } 
      } 
      this.isBeingDragged = false;
      this.activePointerId = -1;
      velocityTracker = this.velocityTracker;
      if (velocityTracker != null) {
        velocityTracker.recycle();
        this.velocityTracker = null;
      } 
    } else {
      i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if (velocityTracker.isPointInChildBounds((View)paramV, i, j) && canDragView(paramV)) {
        this.lastMotionY = j;
        this.activePointerId = paramMotionEvent.getPointerId(0);
        ensureVelocityTracker();
      } else {
        return false;
      } 
    } 
    VelocityTracker velocityTracker = this.velocityTracker;
  }
  
  final int scroll(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, getTopBottomOffsetForScrollingSibling() - paramInt1, paramInt2, paramInt3);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    return setHeaderTopBottomOffset(paramCoordinatorLayout, paramV, paramInt, -2147483648, 2147483647);
  }
  
  int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt1, int paramInt2, int paramInt3) {
    int i = getTopAndBottomOffset();
    if (paramInt2 != 0 && i >= paramInt2 && i <= paramInt3) {
      paramInt1 = MathUtils.clamp(paramInt1, paramInt2, paramInt3);
      if (i != paramInt1) {
        setTopAndBottomOffset(paramInt1);
        return i - paramInt1;
      } 
    } 
    return 0;
  }
  
  private class FlingRunnable implements Runnable {
    private final V layout;
    
    private final CoordinatorLayout parent;
    
    final HeaderBehavior this$0;
    
    FlingRunnable(CoordinatorLayout param1CoordinatorLayout, V param1V) {
      this.parent = param1CoordinatorLayout;
      this.layout = param1V;
    }
    
    public void run() {
      if (this.layout != null && HeaderBehavior.this.scroller != null)
        if (HeaderBehavior.this.scroller.computeScrollOffset()) {
          HeaderBehavior<V> headerBehavior = HeaderBehavior.this;
          headerBehavior.setHeaderTopBottomOffset(this.parent, this.layout, headerBehavior.scroller.getCurrY());
          ViewCompat.postOnAnimation((View)this.layout, this);
        } else {
          HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
        }  
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\appbar\HeaderBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */