package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5F;
  
  private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0F;
  
  private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5F;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  public static final int SWIPE_DIRECTION_ANY = 2;
  
  public static final int SWIPE_DIRECTION_END_TO_START = 1;
  
  public static final int SWIPE_DIRECTION_START_TO_END = 0;
  
  float alphaEndSwipeDistance = 0.5F;
  
  float alphaStartSwipeDistance = 0.0F;
  
  private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      private static final int INVALID_POINTER_ID = -1;
      
      private int activePointerId = -1;
      
      private int originalCapturedViewLeft;
      
      final SwipeDismissBehavior this$0;
      
      private boolean shouldDismiss(View param1View, float param1Float) {
        int j = param1Float cmp 0.0F;
        boolean bool2 = false;
        boolean bool1 = false;
        boolean bool3 = false;
        if (j != 0) {
          boolean bool;
          if (ViewCompat.getLayoutDirection(param1View) == 1) {
            bool = true;
          } else {
            bool = false;
          } 
          if (SwipeDismissBehavior.this.swipeDirection == 2)
            return true; 
          if (SwipeDismissBehavior.this.swipeDirection == 0) {
            if (bool) {
              bool1 = bool3;
              if (param1Float < 0.0F)
                return true; 
            } else {
              bool1 = bool3;
              if (j > 0)
                return true; 
            } 
            return bool1;
          } 
          bool1 = bool2;
          if (SwipeDismissBehavior.this.swipeDirection == 1)
            if (bool) {
              bool1 = bool2;
              if (j > 0)
                return true; 
            } else {
              bool1 = bool2;
              if (param1Float < 0.0F)
                return true; 
            }  
          return bool1;
        } 
        int k = param1View.getLeft();
        int i = this.originalCapturedViewLeft;
        j = Math.round(param1View.getWidth() * SwipeDismissBehavior.this.dragDismissThreshold);
        if (Math.abs(k - i) >= j)
          bool1 = true; 
        return bool1;
      }
      
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        int i;
        if (ViewCompat.getLayoutDirection(param1View) == 1) {
          param1Int2 = 1;
        } else {
          param1Int2 = 0;
        } 
        if (SwipeDismissBehavior.this.swipeDirection == 0) {
          if (param1Int2 != 0) {
            i = this.originalCapturedViewLeft - param1View.getWidth();
            param1Int2 = this.originalCapturedViewLeft;
          } else {
            i = this.originalCapturedViewLeft;
            param1Int2 = param1View.getWidth();
            param1Int2 += i;
          } 
        } else {
          if (SwipeDismissBehavior.this.swipeDirection == 1) {
            if (param1Int2 != 0) {
              i = this.originalCapturedViewLeft;
              param1Int2 = param1View.getWidth();
            } else {
              i = this.originalCapturedViewLeft - param1View.getWidth();
              param1Int2 = this.originalCapturedViewLeft;
              return SwipeDismissBehavior.clamp(i, param1Int1, param1Int2);
            } 
          } else {
            i = this.originalCapturedViewLeft - param1View.getWidth();
            param1Int2 = this.originalCapturedViewLeft;
            param1Int2 = param1View.getWidth() + param1Int2;
            return SwipeDismissBehavior.clamp(i, param1Int1, param1Int2);
          } 
          param1Int2 += i;
        } 
        return SwipeDismissBehavior.clamp(i, param1Int1, param1Int2);
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        return param1View.getTop();
      }
      
      public int getViewHorizontalDragRange(View param1View) {
        return param1View.getWidth();
      }
      
      public void onViewCaptured(View param1View, int param1Int) {
        this.activePointerId = param1Int;
        this.originalCapturedViewLeft = param1View.getLeft();
        ViewParent viewParent = param1View.getParent();
        if (viewParent != null)
          viewParent.requestDisallowInterceptTouchEvent(true); 
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (SwipeDismissBehavior.this.listener != null)
          SwipeDismissBehavior.this.listener.onDragStateChanged(param1Int); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        float f1 = this.originalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.alphaStartSwipeDistance;
        float f2 = this.originalCapturedViewLeft + param1View.getWidth() * SwipeDismissBehavior.this.alphaEndSwipeDistance;
        float f3 = param1Int1;
        if (f3 <= f1) {
          param1View.setAlpha(1.0F);
        } else if (f3 >= f2) {
          param1View.setAlpha(0.0F);
        } else {
          param1View.setAlpha(SwipeDismissBehavior.clamp(0.0F, 1.0F - SwipeDismissBehavior.fraction(f1, f2, f3), 1.0F));
        } 
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        boolean bool;
        this.activePointerId = -1;
        int i = param1View.getWidth();
        if (shouldDismiss(param1View, param1Float1)) {
          int j = param1View.getLeft();
          int k = this.originalCapturedViewLeft;
          if (j < k) {
            i = k - i;
          } else {
            i = k + i;
          } 
          bool = true;
        } else {
          i = this.originalCapturedViewLeft;
          bool = false;
        } 
        if (SwipeDismissBehavior.this.viewDragHelper.settleCapturedViewAt(i, param1View.getTop())) {
          ViewCompat.postOnAnimation(param1View, new SwipeDismissBehavior.SettleRunnable(param1View, bool));
        } else if (bool && SwipeDismissBehavior.this.listener != null) {
          SwipeDismissBehavior.this.listener.onDismiss(param1View);
        } 
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        boolean bool;
        if (this.activePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(param1View)) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
    };
  
  float dragDismissThreshold = 0.5F;
  
  private boolean interceptingEvents;
  
  OnDismissListener listener;
  
  private float sensitivity = 0.0F;
  
  private boolean sensitivitySet;
  
  int swipeDirection = 2;
  
  ViewDragHelper viewDragHelper;
  
  static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
  }
  
  static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return Math.min(Math.max(paramInt1, paramInt2), paramInt3);
  }
  
  private void ensureViewDragHelper(ViewGroup paramViewGroup) {
    if (this.viewDragHelper == null) {
      ViewDragHelper viewDragHelper;
      if (this.sensitivitySet) {
        viewDragHelper = ViewDragHelper.create(paramViewGroup, this.sensitivity, this.dragCallback);
      } else {
        viewDragHelper = ViewDragHelper.create((ViewGroup)viewDragHelper, this.dragCallback);
      } 
      this.viewDragHelper = viewDragHelper;
    } 
  }
  
  static float fraction(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1);
  }
  
  public boolean canSwipeDismissView(View paramView) {
    return true;
  }
  
  public int getDragState() {
    boolean bool;
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null) {
      bool = viewDragHelper.getViewDragState();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    boolean bool = this.interceptingEvents;
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i == 1 || i == 3)
        this.interceptingEvents = false; 
    } else {
      bool = paramCoordinatorLayout.isPointInChildBounds((View)paramV, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
      this.interceptingEvents = bool;
    } 
    if (bool) {
      ensureViewDragHelper((ViewGroup)paramCoordinatorLayout);
      return this.viewDragHelper.shouldInterceptTouchEvent(paramMotionEvent);
    } 
    return false;
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null) {
      viewDragHelper.processTouchEvent(paramMotionEvent);
      return true;
    } 
    return false;
  }
  
  public void setDragDismissDistance(float paramFloat) {
    this.dragDismissThreshold = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setEndAlphaSwipeDistance(float paramFloat) {
    this.alphaEndSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setListener(OnDismissListener paramOnDismissListener) {
    this.listener = paramOnDismissListener;
  }
  
  public void setSensitivity(float paramFloat) {
    this.sensitivity = paramFloat;
    this.sensitivitySet = true;
  }
  
  public void setStartAlphaSwipeDistance(float paramFloat) {
    this.alphaStartSwipeDistance = clamp(0.0F, paramFloat, 1.0F);
  }
  
  public void setSwipeDirection(int paramInt) {
    this.swipeDirection = paramInt;
  }
  
  public static interface OnDismissListener {
    void onDismiss(View param1View);
    
    void onDragStateChanged(int param1Int);
  }
  
  private class SettleRunnable implements Runnable {
    private final boolean dismiss;
    
    final SwipeDismissBehavior this$0;
    
    private final View view;
    
    SettleRunnable(View param1View, boolean param1Boolean) {
      this.view = param1View;
      this.dismiss = param1Boolean;
    }
    
    public void run() {
      if (SwipeDismissBehavior.this.viewDragHelper != null && SwipeDismissBehavior.this.viewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.view, this);
      } else if (this.dismiss && SwipeDismissBehavior.this.listener != null) {
        SwipeDismissBehavior.this.listener.onDismiss(this.view);
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\behavior\SwipeDismissBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */