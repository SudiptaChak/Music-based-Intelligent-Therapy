package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
  private static final float HIDE_FRICTION = 0.1F;
  
  private static final float HIDE_THRESHOLD = 0.5F;
  
  public static final int PEEK_HEIGHT_AUTO = -1;
  
  public static final int STATE_COLLAPSED = 4;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_EXPANDED = 3;
  
  public static final int STATE_HALF_EXPANDED = 6;
  
  public static final int STATE_HIDDEN = 5;
  
  public static final int STATE_SETTLING = 2;
  
  int activePointerId;
  
  private BottomSheetCallback callback;
  
  int collapsedOffset;
  
  private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
      final BottomSheetBehavior this$0;
      
      public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
        return param1View.getLeft();
      }
      
      public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
        int i = BottomSheetBehavior.this.getExpandedOffset();
        if (BottomSheetBehavior.this.hideable) {
          param1Int2 = BottomSheetBehavior.this.parentHeight;
        } else {
          param1Int2 = BottomSheetBehavior.this.collapsedOffset;
        } 
        return MathUtils.clamp(param1Int1, i, param1Int2);
      }
      
      public int getViewVerticalDragRange(View param1View) {
        return BottomSheetBehavior.this.hideable ? BottomSheetBehavior.this.parentHeight : BottomSheetBehavior.this.collapsedOffset;
      }
      
      public void onViewDragStateChanged(int param1Int) {
        if (param1Int == 1)
          BottomSheetBehavior.this.setStateInternal(1); 
      }
      
      public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        BottomSheetBehavior.this.dispatchOnSlide(param1Int2);
      }
      
      public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
        // Byte code:
        //   0: iconst_0
        //   1: istore #4
        //   3: iconst_4
        //   4: istore #5
        //   6: fload_3
        //   7: fconst_0
        //   8: fcmpg
        //   9: ifge -> 85
        //   12: aload_0
        //   13: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   16: invokestatic access$000 : (Lcom/google/android/material/bottomsheet/BottomSheetBehavior;)Z
        //   19: ifeq -> 37
        //   22: aload_0
        //   23: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   26: getfield fitToContentsOffset : I
        //   29: istore #5
        //   31: iconst_3
        //   32: istore #4
        //   34: goto -> 356
        //   37: aload_1
        //   38: invokevirtual getTop : ()I
        //   41: aload_0
        //   42: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   45: getfield halfExpandedOffset : I
        //   48: if_icmple -> 67
        //   51: aload_0
        //   52: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   55: getfield halfExpandedOffset : I
        //   58: istore #4
        //   60: bipush #6
        //   62: istore #5
        //   64: goto -> 70
        //   67: iconst_3
        //   68: istore #5
        //   70: iload #4
        //   72: istore #6
        //   74: iload #5
        //   76: istore #4
        //   78: iload #6
        //   80: istore #5
        //   82: goto -> 356
        //   85: aload_0
        //   86: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   89: getfield hideable : Z
        //   92: ifeq -> 148
        //   95: aload_0
        //   96: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   99: aload_1
        //   100: fload_3
        //   101: invokevirtual shouldHide : (Landroid/view/View;F)Z
        //   104: ifeq -> 148
        //   107: aload_1
        //   108: invokevirtual getTop : ()I
        //   111: aload_0
        //   112: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   115: getfield collapsedOffset : I
        //   118: if_icmpgt -> 133
        //   121: fload_2
        //   122: invokestatic abs : (F)F
        //   125: fload_3
        //   126: invokestatic abs : (F)F
        //   129: fcmpg
        //   130: ifge -> 148
        //   133: aload_0
        //   134: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   137: getfield parentHeight : I
        //   140: istore #5
        //   142: iconst_5
        //   143: istore #4
        //   145: goto -> 356
        //   148: fload_3
        //   149: fconst_0
        //   150: fcmpl
        //   151: ifeq -> 189
        //   154: fload_2
        //   155: invokestatic abs : (F)F
        //   158: fload_3
        //   159: invokestatic abs : (F)F
        //   162: fcmpl
        //   163: ifle -> 169
        //   166: goto -> 189
        //   169: aload_0
        //   170: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   173: getfield collapsedOffset : I
        //   176: istore #6
        //   178: iload #5
        //   180: istore #4
        //   182: iload #6
        //   184: istore #5
        //   186: goto -> 356
        //   189: aload_1
        //   190: invokevirtual getTop : ()I
        //   193: istore #6
        //   195: aload_0
        //   196: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   199: invokestatic access$000 : (Lcom/google/android/material/bottomsheet/BottomSheetBehavior;)Z
        //   202: ifeq -> 258
        //   205: iload #6
        //   207: aload_0
        //   208: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   211: getfield fitToContentsOffset : I
        //   214: isub
        //   215: invokestatic abs : (I)I
        //   218: iload #6
        //   220: aload_0
        //   221: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   224: getfield collapsedOffset : I
        //   227: isub
        //   228: invokestatic abs : (I)I
        //   231: if_icmpge -> 246
        //   234: aload_0
        //   235: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   238: getfield fitToContentsOffset : I
        //   241: istore #4
        //   243: goto -> 67
        //   246: aload_0
        //   247: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   250: getfield collapsedOffset : I
        //   253: istore #4
        //   255: goto -> 70
        //   258: iload #6
        //   260: aload_0
        //   261: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   264: getfield halfExpandedOffset : I
        //   267: if_icmpge -> 303
        //   270: iload #6
        //   272: iload #6
        //   274: aload_0
        //   275: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   278: getfield collapsedOffset : I
        //   281: isub
        //   282: invokestatic abs : (I)I
        //   285: if_icmpge -> 291
        //   288: goto -> 67
        //   291: aload_0
        //   292: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   295: getfield halfExpandedOffset : I
        //   298: istore #4
        //   300: goto -> 60
        //   303: iload #6
        //   305: aload_0
        //   306: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   309: getfield halfExpandedOffset : I
        //   312: isub
        //   313: invokestatic abs : (I)I
        //   316: iload #6
        //   318: aload_0
        //   319: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   322: getfield collapsedOffset : I
        //   325: isub
        //   326: invokestatic abs : (I)I
        //   329: if_icmpge -> 344
        //   332: aload_0
        //   333: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   336: getfield halfExpandedOffset : I
        //   339: istore #4
        //   341: goto -> 60
        //   344: aload_0
        //   345: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   348: getfield collapsedOffset : I
        //   351: istore #4
        //   353: goto -> 70
        //   356: aload_0
        //   357: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   360: getfield viewDragHelper : Landroidx/customview/widget/ViewDragHelper;
        //   363: aload_1
        //   364: invokevirtual getLeft : ()I
        //   367: iload #5
        //   369: invokevirtual settleCapturedViewAt : (II)Z
        //   372: ifeq -> 404
        //   375: aload_0
        //   376: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   379: iconst_2
        //   380: invokevirtual setStateInternal : (I)V
        //   383: aload_1
        //   384: new com/google/android/material/bottomsheet/BottomSheetBehavior$SettleRunnable
        //   387: dup
        //   388: aload_0
        //   389: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   392: aload_1
        //   393: iload #4
        //   395: invokespecial <init> : (Lcom/google/android/material/bottomsheet/BottomSheetBehavior;Landroid/view/View;I)V
        //   398: invokestatic postOnAnimation : (Landroid/view/View;Ljava/lang/Runnable;)V
        //   401: goto -> 413
        //   404: aload_0
        //   405: getfield this$0 : Lcom/google/android/material/bottomsheet/BottomSheetBehavior;
        //   408: iload #4
        //   410: invokevirtual setStateInternal : (I)V
        //   413: return
      }
      
      public boolean tryCaptureView(View param1View, int param1Int) {
        int i = BottomSheetBehavior.this.state;
        boolean bool = true;
        if (i == 1)
          return false; 
        if (BottomSheetBehavior.this.touchingScrollingChild)
          return false; 
        if (BottomSheetBehavior.this.state == 3 && BottomSheetBehavior.this.activePointerId == param1Int) {
          View view = BottomSheetBehavior.this.nestedScrollingChildRef.get();
          if (view != null && view.canScrollVertically(-1))
            return false; 
        } 
        if (BottomSheetBehavior.this.viewRef == null || BottomSheetBehavior.this.viewRef.get() != param1View)
          bool = false; 
        return bool;
      }
    };
  
  private boolean fitToContents = true;
  
  int fitToContentsOffset;
  
  int halfExpandedOffset;
  
  boolean hideable;
  
  private boolean ignoreEvents;
  
  private Map<View, Integer> importantForAccessibilityMap;
  
  private int initialY;
  
  private int lastNestedScrollDy;
  
  private int lastPeekHeight;
  
  private float maximumVelocity;
  
  private boolean nestedScrolled;
  
  WeakReference<View> nestedScrollingChildRef;
  
  int parentHeight;
  
  private int peekHeight;
  
  private boolean peekHeightAuto;
  
  private int peekHeightMin;
  
  private boolean skipCollapsed;
  
  int state = 4;
  
  boolean touchingScrollingChild;
  
  private VelocityTracker velocityTracker;
  
  ViewDragHelper viewDragHelper;
  
  WeakReference<V> viewRef;
  
  public BottomSheetBehavior() {}
  
  public BottomSheetBehavior(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BottomSheetBehavior_Layout);
    TypedValue typedValue = typedArray.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
    if (typedValue != null && typedValue.data == -1) {
      setPeekHeight(typedValue.data);
    } else {
      setPeekHeight(typedArray.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
    } 
    setHideable(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
    setFitToContents(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
    setSkipCollapsed(typedArray.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
    typedArray.recycle();
    this.maximumVelocity = ViewConfiguration.get(paramContext).getScaledMaximumFlingVelocity();
  }
  
  private void calculateCollapsedOffset() {
    if (this.fitToContents) {
      this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
    } else {
      this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
    } 
  }
  
  public static <V extends View> BottomSheetBehavior<V> from(V paramV) {
    ViewGroup.LayoutParams layoutParams = paramV.getLayoutParams();
    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
      CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior();
      if (behavior instanceof BottomSheetBehavior)
        return (BottomSheetBehavior<V>)behavior; 
      throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    } 
    throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
  }
  
  private int getExpandedOffset() {
    boolean bool;
    if (this.fitToContents) {
      bool = this.fitToContentsOffset;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private float getYVelocity() {
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker == null)
      return 0.0F; 
    velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
    return this.velocityTracker.getYVelocity(this.activePointerId);
  }
  
  private void reset() {
    this.activePointerId = -1;
    VelocityTracker velocityTracker = this.velocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.velocityTracker = null;
    } 
  }
  
  private void updateImportantForAccessibility(boolean paramBoolean) {
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null)
      return; 
    ViewParent viewParent = ((View)weakReference.get()).getParent();
    if (!(viewParent instanceof CoordinatorLayout))
      return; 
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)viewParent;
    int i = coordinatorLayout.getChildCount();
    if (Build.VERSION.SDK_INT >= 16 && paramBoolean)
      if (this.importantForAccessibilityMap == null) {
        this.importantForAccessibilityMap = new HashMap<View, Integer>(i);
      } else {
        return;
      }  
    for (byte b = 0; b < i; b++) {
      View view = coordinatorLayout.getChildAt(b);
      if (view != this.viewRef.get())
        if (!paramBoolean) {
          Map<View, Integer> map = this.importantForAccessibilityMap;
          if (map != null && map.containsKey(view))
            ViewCompat.setImportantForAccessibility(view, ((Integer)this.importantForAccessibilityMap.get(view)).intValue()); 
        } else {
          if (Build.VERSION.SDK_INT >= 16)
            this.importantForAccessibilityMap.put(view, Integer.valueOf(view.getImportantForAccessibility())); 
          ViewCompat.setImportantForAccessibility(view, 4);
        }  
    } 
    if (!paramBoolean)
      this.importantForAccessibilityMap = null; 
  }
  
  void dispatchOnSlide(int paramInt) {
    View view = (View)this.viewRef.get();
    if (view != null) {
      BottomSheetCallback bottomSheetCallback = this.callback;
      if (bottomSheetCallback != null) {
        int i = this.collapsedOffset;
        if (paramInt > i) {
          bottomSheetCallback.onSlide(view, (i - paramInt) / (this.parentHeight - i));
        } else {
          bottomSheetCallback.onSlide(view, (i - paramInt) / (i - getExpandedOffset()));
        } 
      } 
    } 
  }
  
  View findScrollingChild(View paramView) {
    if (ViewCompat.isNestedScrollingEnabled(paramView))
      return paramView; 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      byte b = 0;
      int i = viewGroup.getChildCount();
      while (b < i) {
        View view = findScrollingChild(viewGroup.getChildAt(b));
        if (view != null)
          return view; 
        b++;
      } 
    } 
    return null;
  }
  
  public final int getPeekHeight() {
    int i;
    if (this.peekHeightAuto) {
      i = -1;
    } else {
      i = this.peekHeight;
    } 
    return i;
  }
  
  int getPeekHeightMin() {
    return this.peekHeightMin;
  }
  
  public boolean getSkipCollapsed() {
    return this.skipCollapsed;
  }
  
  public final int getState() {
    return this.state;
  }
  
  public boolean isFitToContents() {
    return this.fitToContents;
  }
  
  public boolean isHideable() {
    return this.hideable;
  }
  
  public boolean onInterceptTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    View view;
    boolean bool = paramV.isShown();
    boolean bool1 = false;
    if (!bool) {
      this.ignoreEvents = true;
      return false;
    } 
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    V v = null;
    if (i != 0) {
      if (i == 1 || i == 3) {
        this.touchingScrollingChild = false;
        this.activePointerId = -1;
        if (this.ignoreEvents) {
          this.ignoreEvents = false;
          return false;
        } 
      } 
    } else {
      int j = (int)paramMotionEvent.getX();
      this.initialY = (int)paramMotionEvent.getY();
      WeakReference<View> weakReference1 = this.nestedScrollingChildRef;
      if (weakReference1 != null) {
        View view1 = weakReference1.get();
      } else {
        weakReference1 = null;
      } 
      if (weakReference1 != null && paramCoordinatorLayout.isPointInChildBounds((View)weakReference1, j, this.initialY)) {
        this.activePointerId = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
        this.touchingScrollingChild = true;
      } 
      if (this.activePointerId == -1 && !paramCoordinatorLayout.isPointInChildBounds((View)paramV, j, this.initialY)) {
        bool = true;
      } else {
        bool = false;
      } 
      this.ignoreEvents = bool;
    } 
    if (!this.ignoreEvents) {
      ViewDragHelper viewDragHelper = this.viewDragHelper;
      if (viewDragHelper != null && viewDragHelper.shouldInterceptTouchEvent(paramMotionEvent))
        return true; 
    } 
    WeakReference<View> weakReference = this.nestedScrollingChildRef;
    paramV = v;
    if (weakReference != null)
      view = weakReference.get(); 
    bool = bool1;
    if (i == 2) {
      bool = bool1;
      if (view != null) {
        bool = bool1;
        if (!this.ignoreEvents) {
          bool = bool1;
          if (this.state != 1) {
            bool = bool1;
            if (!paramCoordinatorLayout.isPointInChildBounds(view, (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())) {
              bool = bool1;
              if (this.viewDragHelper != null) {
                bool = bool1;
                if (Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop())
                  bool = true; 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool;
  }
  
  public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, V paramV, int paramInt) {
    if (ViewCompat.getFitsSystemWindows((View)paramCoordinatorLayout) && !ViewCompat.getFitsSystemWindows((View)paramV))
      paramV.setFitsSystemWindows(true); 
    int i = paramV.getTop();
    paramCoordinatorLayout.onLayoutChild((View)paramV, paramInt);
    this.parentHeight = paramCoordinatorLayout.getHeight();
    if (this.peekHeightAuto) {
      if (this.peekHeightMin == 0)
        this.peekHeightMin = paramCoordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min); 
      this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - paramCoordinatorLayout.getWidth() * 9 / 16);
    } else {
      this.lastPeekHeight = this.peekHeight;
    } 
    this.fitToContentsOffset = Math.max(0, this.parentHeight - paramV.getHeight());
    this.halfExpandedOffset = this.parentHeight / 2;
    calculateCollapsedOffset();
    paramInt = this.state;
    if (paramInt == 3) {
      ViewCompat.offsetTopAndBottom((View)paramV, getExpandedOffset());
    } else if (paramInt == 6) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.halfExpandedOffset);
    } else if (this.hideable && paramInt == 5) {
      ViewCompat.offsetTopAndBottom((View)paramV, this.parentHeight);
    } else {
      paramInt = this.state;
      if (paramInt == 4) {
        ViewCompat.offsetTopAndBottom((View)paramV, this.collapsedOffset);
      } else if (paramInt == 1 || paramInt == 2) {
        ViewCompat.offsetTopAndBottom((View)paramV, i - paramV.getTop());
      } 
    } 
    if (this.viewDragHelper == null)
      this.viewDragHelper = ViewDragHelper.create((ViewGroup)paramCoordinatorLayout, this.dragCallback); 
    this.viewRef = new WeakReference<V>(paramV);
    this.nestedScrollingChildRef = new WeakReference<View>(findScrollingChild((View)paramV));
    return true;
  }
  
  public boolean onNestedPreFling(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, float paramFloat1, float paramFloat2) {
    boolean bool;
    if (paramView == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(paramCoordinatorLayout, (View)paramV, paramView, paramFloat1, paramFloat2))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onNestedPreScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
    if (paramInt3 == 1)
      return; 
    if (paramView != (View)this.nestedScrollingChildRef.get())
      return; 
    paramInt1 = paramV.getTop();
    int i = paramInt1 - paramInt2;
    if (paramInt2 > 0) {
      if (i < getExpandedOffset()) {
        paramArrayOfint[1] = paramInt1 - getExpandedOffset();
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(3);
      } else {
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } 
    } else if (paramInt2 < 0 && !paramView.canScrollVertically(-1)) {
      paramInt3 = this.collapsedOffset;
      if (i <= paramInt3 || this.hideable) {
        paramArrayOfint[1] = paramInt2;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramInt2);
        setStateInternal(1);
      } else {
        paramArrayOfint[1] = paramInt1 - paramInt3;
        ViewCompat.offsetTopAndBottom((View)paramV, -paramArrayOfint[1]);
        setStateInternal(4);
      } 
    } 
    dispatchOnSlide(paramV.getTop());
    this.lastNestedScrollDy = paramInt2;
    this.nestedScrolled = true;
  }
  
  public void onRestoreInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV, Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramCoordinatorLayout, (View)paramV, savedState.getSuperState());
    if (savedState.state == 1 || savedState.state == 2) {
      this.state = 4;
      return;
    } 
    this.state = savedState.state;
  }
  
  public Parcelable onSaveInstanceState(CoordinatorLayout paramCoordinatorLayout, V paramV) {
    return (Parcelable)new SavedState(super.onSaveInstanceState(paramCoordinatorLayout, (View)paramV), this.state);
  }
  
  public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView1, View paramView2, int paramInt1, int paramInt2) {
    boolean bool = false;
    this.lastNestedScrollDy = 0;
    this.nestedScrolled = false;
    if ((paramInt1 & 0x2) != 0)
      bool = true; 
    return bool;
  }
  
  public void onStopNestedScroll(CoordinatorLayout paramCoordinatorLayout, V paramV, View paramView, int paramInt) {
    paramInt = paramV.getTop();
    int i = getExpandedOffset();
    byte b = 3;
    if (paramInt == i) {
      setStateInternal(3);
      return;
    } 
    if (paramView == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
      if (this.lastNestedScrollDy > 0) {
        paramInt = getExpandedOffset();
      } else if (this.hideable && shouldHide((View)paramV, getYVelocity())) {
        paramInt = this.parentHeight;
        b = 5;
      } else if (this.lastNestedScrollDy == 0) {
        i = paramV.getTop();
        if (this.fitToContents) {
          if (Math.abs(i - this.fitToContentsOffset) < Math.abs(i - this.collapsedOffset)) {
            paramInt = this.fitToContentsOffset;
          } else {
            paramInt = this.collapsedOffset;
            b = 4;
          } 
        } else {
          paramInt = this.halfExpandedOffset;
          if (i < paramInt) {
            if (i < Math.abs(i - this.collapsedOffset)) {
              paramInt = 0;
            } else {
              paramInt = this.halfExpandedOffset;
              b = 6;
            } 
          } else {
            if (Math.abs(i - paramInt) < Math.abs(i - this.collapsedOffset)) {
              paramInt = this.halfExpandedOffset;
            } else {
              paramInt = this.collapsedOffset;
              b = 4;
            } 
            b = 6;
          } 
        } 
      } else {
        paramInt = this.collapsedOffset;
        b = 4;
      } 
      if (this.viewDragHelper.smoothSlideViewTo((View)paramV, paramV.getLeft(), paramInt)) {
        setStateInternal(2);
        ViewCompat.postOnAnimation((View)paramV, new SettleRunnable((View)paramV, b));
      } else {
        setStateInternal(b);
      } 
      this.nestedScrolled = false;
    } 
  }
  
  public boolean onTouchEvent(CoordinatorLayout paramCoordinatorLayout, V paramV, MotionEvent paramMotionEvent) {
    if (!paramV.isShown())
      return false; 
    int i = paramMotionEvent.getActionMasked();
    if (this.state == 1 && i == 0)
      return true; 
    ViewDragHelper viewDragHelper = this.viewDragHelper;
    if (viewDragHelper != null)
      viewDragHelper.processTouchEvent(paramMotionEvent); 
    if (i == 0)
      reset(); 
    if (this.velocityTracker == null)
      this.velocityTracker = VelocityTracker.obtain(); 
    this.velocityTracker.addMovement(paramMotionEvent);
    if (i == 2 && !this.ignoreEvents && Math.abs(this.initialY - paramMotionEvent.getY()) > this.viewDragHelper.getTouchSlop())
      this.viewDragHelper.captureChildView((View)paramV, paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex())); 
    return this.ignoreEvents ^ true;
  }
  
  public void setBottomSheetCallback(BottomSheetCallback paramBottomSheetCallback) {
    this.callback = paramBottomSheetCallback;
  }
  
  public void setFitToContents(boolean paramBoolean) {
    int i;
    if (this.fitToContents == paramBoolean)
      return; 
    this.fitToContents = paramBoolean;
    if (this.viewRef != null)
      calculateCollapsedOffset(); 
    if (this.fitToContents && this.state == 6) {
      i = 3;
    } else {
      i = this.state;
    } 
    setStateInternal(i);
  }
  
  public void setHideable(boolean paramBoolean) {
    this.hideable = paramBoolean;
  }
  
  public final void setPeekHeight(int paramInt) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: iload_1
    //   3: iconst_m1
    //   4: if_icmpne -> 24
    //   7: aload_0
    //   8: getfield peekHeightAuto : Z
    //   11: ifne -> 42
    //   14: aload_0
    //   15: iconst_1
    //   16: putfield peekHeightAuto : Z
    //   19: iload_2
    //   20: istore_1
    //   21: goto -> 73
    //   24: aload_0
    //   25: getfield peekHeightAuto : Z
    //   28: ifne -> 47
    //   31: aload_0
    //   32: getfield peekHeight : I
    //   35: iload_1
    //   36: if_icmpeq -> 42
    //   39: goto -> 47
    //   42: iconst_0
    //   43: istore_1
    //   44: goto -> 73
    //   47: aload_0
    //   48: iconst_0
    //   49: putfield peekHeightAuto : Z
    //   52: aload_0
    //   53: iconst_0
    //   54: iload_1
    //   55: invokestatic max : (II)I
    //   58: putfield peekHeight : I
    //   61: aload_0
    //   62: aload_0
    //   63: getfield parentHeight : I
    //   66: iload_1
    //   67: isub
    //   68: putfield collapsedOffset : I
    //   71: iload_2
    //   72: istore_1
    //   73: iload_1
    //   74: ifeq -> 110
    //   77: aload_0
    //   78: getfield state : I
    //   81: iconst_4
    //   82: if_icmpne -> 110
    //   85: aload_0
    //   86: getfield viewRef : Ljava/lang/ref/WeakReference;
    //   89: astore_3
    //   90: aload_3
    //   91: ifnull -> 110
    //   94: aload_3
    //   95: invokevirtual get : ()Ljava/lang/Object;
    //   98: checkcast android/view/View
    //   101: astore_3
    //   102: aload_3
    //   103: ifnull -> 110
    //   106: aload_3
    //   107: invokevirtual requestLayout : ()V
    //   110: return
  }
  
  public void setSkipCollapsed(boolean paramBoolean) {
    this.skipCollapsed = paramBoolean;
  }
  
  public final void setState(final int finalState) {
    if (finalState == this.state)
      return; 
    WeakReference<V> weakReference = this.viewRef;
    if (weakReference == null) {
      if (finalState == 4 || finalState == 3 || finalState == 6 || (this.hideable && finalState == 5))
        this.state = finalState; 
      return;
    } 
    final View child = (View)weakReference.get();
    if (view == null)
      return; 
    ViewParent viewParent = view.getParent();
    if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
      view.post(new Runnable() {
            final BottomSheetBehavior this$0;
            
            final View val$child;
            
            final int val$finalState;
            
            public void run() {
              BottomSheetBehavior.this.startSettlingAnimation(child, finalState);
            }
          });
    } else {
      startSettlingAnimation(view, finalState);
    } 
  }
  
  void setStateInternal(int paramInt) {
    if (this.state == paramInt)
      return; 
    this.state = paramInt;
    if (paramInt == 6 || paramInt == 3) {
      updateImportantForAccessibility(true);
    } else if (paramInt == 5 || paramInt == 4) {
      updateImportantForAccessibility(false);
    } 
    View view = (View)this.viewRef.get();
    if (view != null) {
      BottomSheetCallback bottomSheetCallback = this.callback;
      if (bottomSheetCallback != null)
        bottomSheetCallback.onStateChanged(view, paramInt); 
    } 
  }
  
  boolean shouldHide(View paramView, float paramFloat) {
    boolean bool1 = this.skipCollapsed;
    boolean bool = true;
    if (bool1)
      return true; 
    if (paramView.getTop() < this.collapsedOffset)
      return false; 
    if (Math.abs(paramView.getTop() + paramFloat * 0.1F - this.collapsedOffset) / this.peekHeight <= 0.5F)
      bool = false; 
    return bool;
  }
  
  void startSettlingAnimation(View paramView, int paramInt) {
    // Byte code:
    //   0: iload_2
    //   1: iconst_4
    //   2: if_icmpne -> 13
    //   5: aload_0
    //   6: getfield collapsedOffset : I
    //   9: istore_3
    //   10: goto -> 84
    //   13: iload_2
    //   14: bipush #6
    //   16: if_icmpne -> 54
    //   19: aload_0
    //   20: getfield halfExpandedOffset : I
    //   23: istore #4
    //   25: aload_0
    //   26: getfield fitToContents : Z
    //   29: ifeq -> 48
    //   32: aload_0
    //   33: getfield fitToContentsOffset : I
    //   36: istore_3
    //   37: iload #4
    //   39: iload_3
    //   40: if_icmpgt -> 48
    //   43: iconst_3
    //   44: istore_2
    //   45: goto -> 84
    //   48: iload #4
    //   50: istore_3
    //   51: goto -> 84
    //   54: iload_2
    //   55: iconst_3
    //   56: if_icmpne -> 67
    //   59: aload_0
    //   60: invokespecial getExpandedOffset : ()I
    //   63: istore_3
    //   64: goto -> 84
    //   67: aload_0
    //   68: getfield hideable : Z
    //   71: ifeq -> 128
    //   74: iload_2
    //   75: iconst_5
    //   76: if_icmpne -> 128
    //   79: aload_0
    //   80: getfield parentHeight : I
    //   83: istore_3
    //   84: aload_0
    //   85: getfield viewDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   88: aload_1
    //   89: aload_1
    //   90: invokevirtual getLeft : ()I
    //   93: iload_3
    //   94: invokevirtual smoothSlideViewTo : (Landroid/view/View;II)Z
    //   97: ifeq -> 122
    //   100: aload_0
    //   101: iconst_2
    //   102: invokevirtual setStateInternal : (I)V
    //   105: aload_1
    //   106: new com/google/android/material/bottomsheet/BottomSheetBehavior$SettleRunnable
    //   109: dup
    //   110: aload_0
    //   111: aload_1
    //   112: iload_2
    //   113: invokespecial <init> : (Lcom/google/android/material/bottomsheet/BottomSheetBehavior;Landroid/view/View;I)V
    //   116: invokestatic postOnAnimation : (Landroid/view/View;Ljava/lang/Runnable;)V
    //   119: goto -> 127
    //   122: aload_0
    //   123: iload_2
    //   124: invokevirtual setStateInternal : (I)V
    //   127: return
    //   128: new java/lang/StringBuilder
    //   131: dup
    //   132: invokespecial <init> : ()V
    //   135: astore_1
    //   136: aload_1
    //   137: ldc_w 'Illegal state argument: '
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload_1
    //   145: iload_2
    //   146: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: new java/lang/IllegalArgumentException
    //   153: dup
    //   154: aload_1
    //   155: invokevirtual toString : ()Ljava/lang/String;
    //   158: invokespecial <init> : (Ljava/lang/String;)V
    //   161: athrow
  }
  
  public static abstract class BottomSheetCallback {
    public abstract void onSlide(View param1View, float param1Float);
    
    public abstract void onStateChanged(View param1View, int param1Int);
  }
  
  protected static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomSheetBehavior.SavedState(param2Parcel, null);
        }
        
        public BottomSheetBehavior.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomSheetBehavior.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomSheetBehavior.SavedState[] newArray(int param2Int) {
          return new BottomSheetBehavior.SavedState[param2Int];
        }
      };
    
    final int state;
    
    public SavedState(Parcel param1Parcel) {
      this(param1Parcel, (ClassLoader)null);
    }
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.state = param1Parcel.readInt();
    }
    
    public SavedState(Parcelable param1Parcelable, int param1Int) {
      super(param1Parcelable);
      this.state = param1Int;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.state);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomSheetBehavior.SavedState(param1Parcel, null);
    }
    
    public BottomSheetBehavior.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomSheetBehavior.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomSheetBehavior.SavedState[] newArray(int param1Int) {
      return new BottomSheetBehavior.SavedState[param1Int];
    }
  }
  
  private class SettleRunnable implements Runnable {
    private final int targetState;
    
    final BottomSheetBehavior this$0;
    
    private final View view;
    
    SettleRunnable(View param1View, int param1Int) {
      this.view = param1View;
      this.targetState = param1Int;
    }
    
    public void run() {
      if (BottomSheetBehavior.this.viewDragHelper != null && BottomSheetBehavior.this.viewDragHelper.continueSettling(true)) {
        ViewCompat.postOnAnimation(this.view, this);
      } else {
        BottomSheetBehavior.this.setStateInternal(this.targetState);
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomsheet\BottomSheetBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */