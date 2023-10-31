package androidx.slidingpanelayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
  private static final int DEFAULT_FADE_COLOR = -858993460;
  
  private static final int DEFAULT_OVERHANG_SIZE = 32;
  
  private static final int MIN_FLING_VELOCITY = 400;
  
  private static final String TAG = "SlidingPaneLayout";
  
  private boolean mCanSlide;
  
  private int mCoveredFadeColor;
  
  private boolean mDisplayListReflectionLoaded;
  
  final ViewDragHelper mDragHelper;
  
  private boolean mFirstLayout = true;
  
  private Method mGetDisplayList;
  
  private float mInitialMotionX;
  
  private float mInitialMotionY;
  
  boolean mIsUnableToDrag;
  
  private final int mOverhangSize;
  
  private PanelSlideListener mPanelSlideListener;
  
  private int mParallaxBy;
  
  private float mParallaxOffset;
  
  final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList<DisableLayerRunnable>();
  
  boolean mPreservedOpenState;
  
  private Field mRecreateDisplayList;
  
  private Drawable mShadowDrawableLeft;
  
  private Drawable mShadowDrawableRight;
  
  float mSlideOffset;
  
  int mSlideRange;
  
  View mSlideableView;
  
  private int mSliderFadeColor = -858993460;
  
  private final Rect mTmpRect = new Rect();
  
  public SlidingPaneLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mOverhangSize = (int)(32.0F * f + 0.5F);
    setWillNotDraw(false);
    ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
    ViewCompat.setImportantForAccessibility((View)this, 1);
    ViewDragHelper viewDragHelper = ViewDragHelper.create(this, 0.5F, new DragHelperCallback());
    this.mDragHelper = viewDragHelper;
    viewDragHelper.setMinVelocity(f * 400.0F);
  }
  
  private boolean closePane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(0.0F, paramInt)) {
      this.mPreservedOpenState = false;
      return true;
    } 
    return false;
  }
  
  private void dimChildView(View paramView, float paramFloat, int paramInt) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (paramFloat > 0.0F && paramInt != 0) {
      int i = (int)(((0xFF000000 & paramInt) >>> 24) * paramFloat);
      if (layoutParams.dimPaint == null)
        layoutParams.dimPaint = new Paint(); 
      layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(i << 24 | paramInt & 0xFFFFFF, PorterDuff.Mode.SRC_OVER));
      if (paramView.getLayerType() != 2)
        paramView.setLayerType(2, layoutParams.dimPaint); 
      invalidateChildRegion(paramView);
    } else if (paramView.getLayerType() != 0) {
      if (layoutParams.dimPaint != null)
        layoutParams.dimPaint.setColorFilter(null); 
      DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(paramView);
      this.mPostedRunnables.add(disableLayerRunnable);
      ViewCompat.postOnAnimation((View)this, disableLayerRunnable);
    } 
  }
  
  private boolean openPane(View paramView, int paramInt) {
    if (this.mFirstLayout || smoothSlideTo(1.0F, paramInt)) {
      this.mPreservedOpenState = true;
      return true;
    } 
    return false;
  }
  
  private void parallaxOtherViews(float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #9
    //   6: aload_0
    //   7: getfield mSlideableView : Landroid/view/View;
    //   10: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   13: checkcast androidx/slidingpanelayout/widget/SlidingPaneLayout$LayoutParams
    //   16: astore #10
    //   18: aload #10
    //   20: getfield dimWhenOffset : Z
    //   23: istore #8
    //   25: iconst_0
    //   26: istore #4
    //   28: iload #8
    //   30: ifeq -> 62
    //   33: iload #9
    //   35: ifeq -> 47
    //   38: aload #10
    //   40: getfield rightMargin : I
    //   43: istore_3
    //   44: goto -> 53
    //   47: aload #10
    //   49: getfield leftMargin : I
    //   52: istore_3
    //   53: iload_3
    //   54: ifgt -> 62
    //   57: iconst_1
    //   58: istore_3
    //   59: goto -> 64
    //   62: iconst_0
    //   63: istore_3
    //   64: aload_0
    //   65: invokevirtual getChildCount : ()I
    //   68: istore #7
    //   70: iload #4
    //   72: iload #7
    //   74: if_icmpge -> 199
    //   77: aload_0
    //   78: iload #4
    //   80: invokevirtual getChildAt : (I)Landroid/view/View;
    //   83: astore #10
    //   85: aload #10
    //   87: aload_0
    //   88: getfield mSlideableView : Landroid/view/View;
    //   91: if_acmpne -> 97
    //   94: goto -> 193
    //   97: aload_0
    //   98: getfield mParallaxOffset : F
    //   101: fstore_2
    //   102: aload_0
    //   103: getfield mParallaxBy : I
    //   106: istore #5
    //   108: fconst_1
    //   109: fload_2
    //   110: fsub
    //   111: iload #5
    //   113: i2f
    //   114: fmul
    //   115: f2i
    //   116: istore #6
    //   118: aload_0
    //   119: fload_1
    //   120: putfield mParallaxOffset : F
    //   123: iload #6
    //   125: fconst_1
    //   126: fload_1
    //   127: fsub
    //   128: iload #5
    //   130: i2f
    //   131: fmul
    //   132: f2i
    //   133: isub
    //   134: istore #6
    //   136: iload #6
    //   138: istore #5
    //   140: iload #9
    //   142: ifeq -> 150
    //   145: iload #6
    //   147: ineg
    //   148: istore #5
    //   150: aload #10
    //   152: iload #5
    //   154: invokevirtual offsetLeftAndRight : (I)V
    //   157: iload_3
    //   158: ifeq -> 193
    //   161: aload_0
    //   162: getfield mParallaxOffset : F
    //   165: fstore_2
    //   166: iload #9
    //   168: ifeq -> 178
    //   171: fload_2
    //   172: fconst_1
    //   173: fsub
    //   174: fstore_2
    //   175: goto -> 182
    //   178: fconst_1
    //   179: fload_2
    //   180: fsub
    //   181: fstore_2
    //   182: aload_0
    //   183: aload #10
    //   185: fload_2
    //   186: aload_0
    //   187: getfield mCoveredFadeColor : I
    //   190: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   193: iinc #4, 1
    //   196: goto -> 70
    //   199: return
  }
  
  private static boolean viewIsOpaque(View paramView) {
    boolean bool1 = paramView.isOpaque();
    boolean bool = true;
    if (bool1)
      return true; 
    if (Build.VERSION.SDK_INT >= 18)
      return false; 
    Drawable drawable = paramView.getBackground();
    if (drawable != null) {
      if (drawable.getOpacity() != -1)
        bool = false; 
      return bool;
    } 
    return false;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1 = paramView instanceof ViewGroup;
    boolean bool = true;
    if (bool1) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int k = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        int m = paramInt2 + k;
        if (m >= view.getLeft() && m < view.getRight()) {
          int n = paramInt3 + j;
          if (n >= view.getTop() && n < view.getBottom() && canScroll(view, true, paramInt1, m - view.getLeft(), n - view.getTop()))
            return true; 
        } 
      } 
    } 
    if (paramBoolean) {
      if (!isLayoutRtlSupport())
        paramInt1 = -paramInt1; 
      if (paramView.canScrollHorizontally(paramInt1))
        return bool; 
    } 
    return false;
  }
  
  @Deprecated
  public boolean canSlide() {
    return this.mCanSlide;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && super.checkLayoutParams(paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean closePane() {
    return closePane(this.mSlideableView, 0);
  }
  
  public void computeScroll() {
    if (this.mDragHelper.continueSettling(true)) {
      if (!this.mCanSlide) {
        this.mDragHelper.abort();
        return;
      } 
      ViewCompat.postInvalidateOnAnimation((View)this);
    } 
  }
  
  void dispatchOnPanelClosed(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelClosed(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelOpened(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelOpened(paramView); 
    sendAccessibilityEvent(32);
  }
  
  void dispatchOnPanelSlide(View paramView) {
    PanelSlideListener panelSlideListener = this.mPanelSlideListener;
    if (panelSlideListener != null)
      panelSlideListener.onPanelSlide(paramView, this.mSlideOffset); 
  }
  
  public void draw(Canvas paramCanvas) {
    Drawable drawable;
    View view;
    super.draw(paramCanvas);
    if (isLayoutRtlSupport()) {
      drawable = this.mShadowDrawableRight;
    } else {
      drawable = this.mShadowDrawableLeft;
    } 
    if (getChildCount() > 1) {
      view = getChildAt(1);
    } else {
      view = null;
    } 
    if (view != null && drawable != null) {
      int i;
      int j;
      int m = view.getTop();
      int n = view.getBottom();
      int k = drawable.getIntrinsicWidth();
      if (isLayoutRtlSupport()) {
        i = view.getRight();
        j = k + i;
      } else {
        j = view.getLeft();
        i = j;
        k = j - k;
        j = i;
        i = k;
      } 
      drawable.setBounds(i, m, j, n);
      drawable.draw(paramCanvas);
    } 
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save();
    if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
      paramCanvas.getClipBounds(this.mTmpRect);
      if (isLayoutRtlSupport()) {
        Rect rect = this.mTmpRect;
        rect.left = Math.max(rect.left, this.mSlideableView.getRight());
      } else {
        Rect rect = this.mTmpRect;
        rect.right = Math.min(rect.right, this.mSlideableView.getLeft());
      } 
      paramCanvas.clipRect(this.mTmpRect);
    } 
    boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
    paramCanvas.restoreToCount(i);
    return bool;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    return (ViewGroup.LayoutParams)new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return (ViewGroup.LayoutParams)new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutParams layoutParams;
    if (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) {
      layoutParams = new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    } else {
      layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
    return (ViewGroup.LayoutParams)layoutParams;
  }
  
  public int getCoveredFadeColor() {
    return this.mCoveredFadeColor;
  }
  
  public int getParallaxDistance() {
    return this.mParallaxBy;
  }
  
  public int getSliderFadeColor() {
    return this.mSliderFadeColor;
  }
  
  void invalidateChildRegion(View paramView) {
    // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: bipush #17
    //   5: if_icmplt -> 23
    //   8: aload_1
    //   9: aload_1
    //   10: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   13: checkcast androidx/slidingpanelayout/widget/SlidingPaneLayout$LayoutParams
    //   16: getfield dimPaint : Landroid/graphics/Paint;
    //   19: invokestatic setLayerPaint : (Landroid/view/View;Landroid/graphics/Paint;)V
    //   22: return
    //   23: getstatic android/os/Build$VERSION.SDK_INT : I
    //   26: bipush #16
    //   28: if_icmplt -> 166
    //   31: aload_0
    //   32: getfield mDisplayListReflectionLoaded : Z
    //   35: ifne -> 106
    //   38: aload_0
    //   39: ldc android/view/View
    //   41: ldc_w 'getDisplayList'
    //   44: aconst_null
    //   45: checkcast [Ljava/lang/Class;
    //   48: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   51: putfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   54: goto -> 68
    //   57: astore_2
    //   58: ldc 'SlidingPaneLayout'
    //   60: ldc_w 'Couldn't fetch getDisplayList method; dimming won't work right.'
    //   63: aload_2
    //   64: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   67: pop
    //   68: ldc android/view/View
    //   70: ldc_w 'mRecreateDisplayList'
    //   73: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   76: astore_2
    //   77: aload_0
    //   78: aload_2
    //   79: putfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   82: aload_2
    //   83: iconst_1
    //   84: invokevirtual setAccessible : (Z)V
    //   87: goto -> 101
    //   90: astore_2
    //   91: ldc 'SlidingPaneLayout'
    //   93: ldc_w 'Couldn't fetch mRecreateDisplayList field; dimming will be slow.'
    //   96: aload_2
    //   97: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   100: pop
    //   101: aload_0
    //   102: iconst_1
    //   103: putfield mDisplayListReflectionLoaded : Z
    //   106: aload_0
    //   107: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   110: ifnull -> 161
    //   113: aload_0
    //   114: getfield mRecreateDisplayList : Ljava/lang/reflect/Field;
    //   117: astore_2
    //   118: aload_2
    //   119: ifnonnull -> 125
    //   122: goto -> 161
    //   125: aload_2
    //   126: aload_1
    //   127: iconst_1
    //   128: invokevirtual setBoolean : (Ljava/lang/Object;Z)V
    //   131: aload_0
    //   132: getfield mGetDisplayList : Ljava/lang/reflect/Method;
    //   135: aload_1
    //   136: aconst_null
    //   137: checkcast [Ljava/lang/Object;
    //   140: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   143: pop
    //   144: goto -> 166
    //   147: astore_2
    //   148: ldc 'SlidingPaneLayout'
    //   150: ldc_w 'Error refreshing display list state'
    //   153: aload_2
    //   154: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   157: pop
    //   158: goto -> 166
    //   161: aload_1
    //   162: invokevirtual invalidate : ()V
    //   165: return
    //   166: aload_0
    //   167: aload_1
    //   168: invokevirtual getLeft : ()I
    //   171: aload_1
    //   172: invokevirtual getTop : ()I
    //   175: aload_1
    //   176: invokevirtual getRight : ()I
    //   179: aload_1
    //   180: invokevirtual getBottom : ()I
    //   183: invokestatic postInvalidateOnAnimation : (Landroid/view/View;IIII)V
    //   186: return
    // Exception table:
    //   from	to	target	type
    //   38	54	57	java/lang/NoSuchMethodException
    //   68	87	90	java/lang/NoSuchFieldException
    //   125	144	147	java/lang/Exception
  }
  
  boolean isDimmed(View paramView) {
    boolean bool2 = false;
    if (paramView == null)
      return false; 
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    boolean bool1 = bool2;
    if (this.mCanSlide) {
      bool1 = bool2;
      if (layoutParams.dimWhenOffset) {
        bool1 = bool2;
        if (this.mSlideOffset > 0.0F)
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  boolean isLayoutRtlSupport() {
    int i = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (i != 1)
      bool = false; 
    return bool;
  }
  
  public boolean isOpen() {
    return (!this.mCanSlide || this.mSlideOffset == 1.0F);
  }
  
  public boolean isSlideable() {
    return this.mCanSlide;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
    int i = this.mPostedRunnables.size();
    for (byte b = 0; b < i; b++)
      ((DisableLayerRunnable)this.mPostedRunnables.get(b)).run(); 
    this.mPostedRunnables.clear();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getActionMasked : ()I
    //   4: istore #4
    //   6: aload_0
    //   7: getfield mCanSlide : Z
    //   10: istore #5
    //   12: iconst_1
    //   13: istore #6
    //   15: iload #5
    //   17: ifne -> 70
    //   20: iload #4
    //   22: ifne -> 70
    //   25: aload_0
    //   26: invokevirtual getChildCount : ()I
    //   29: iconst_1
    //   30: if_icmple -> 70
    //   33: aload_0
    //   34: iconst_1
    //   35: invokevirtual getChildAt : (I)Landroid/view/View;
    //   38: astore #7
    //   40: aload #7
    //   42: ifnull -> 70
    //   45: aload_0
    //   46: aload_0
    //   47: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   50: aload #7
    //   52: aload_1
    //   53: invokevirtual getX : ()F
    //   56: f2i
    //   57: aload_1
    //   58: invokevirtual getY : ()F
    //   61: f2i
    //   62: invokevirtual isViewUnder : (Landroid/view/View;II)Z
    //   65: iconst_1
    //   66: ixor
    //   67: putfield mPreservedOpenState : Z
    //   70: aload_0
    //   71: getfield mCanSlide : Z
    //   74: ifeq -> 289
    //   77: aload_0
    //   78: getfield mIsUnableToDrag : Z
    //   81: ifeq -> 92
    //   84: iload #4
    //   86: ifeq -> 92
    //   89: goto -> 289
    //   92: iload #4
    //   94: iconst_3
    //   95: if_icmpeq -> 280
    //   98: iload #4
    //   100: iconst_1
    //   101: if_icmpne -> 107
    //   104: goto -> 280
    //   107: iload #4
    //   109: ifeq -> 184
    //   112: iload #4
    //   114: iconst_2
    //   115: if_icmpeq -> 121
    //   118: goto -> 244
    //   121: aload_1
    //   122: invokevirtual getX : ()F
    //   125: fstore_3
    //   126: aload_1
    //   127: invokevirtual getY : ()F
    //   130: fstore_2
    //   131: fload_3
    //   132: aload_0
    //   133: getfield mInitialMotionX : F
    //   136: fsub
    //   137: invokestatic abs : (F)F
    //   140: fstore_3
    //   141: fload_2
    //   142: aload_0
    //   143: getfield mInitialMotionY : F
    //   146: fsub
    //   147: invokestatic abs : (F)F
    //   150: fstore_2
    //   151: fload_3
    //   152: aload_0
    //   153: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   156: invokevirtual getTouchSlop : ()I
    //   159: i2f
    //   160: fcmpl
    //   161: ifle -> 244
    //   164: fload_2
    //   165: fload_3
    //   166: fcmpl
    //   167: ifle -> 244
    //   170: aload_0
    //   171: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   174: invokevirtual cancel : ()V
    //   177: aload_0
    //   178: iconst_1
    //   179: putfield mIsUnableToDrag : Z
    //   182: iconst_0
    //   183: ireturn
    //   184: aload_0
    //   185: iconst_0
    //   186: putfield mIsUnableToDrag : Z
    //   189: aload_1
    //   190: invokevirtual getX : ()F
    //   193: fstore_3
    //   194: aload_1
    //   195: invokevirtual getY : ()F
    //   198: fstore_2
    //   199: aload_0
    //   200: fload_3
    //   201: putfield mInitialMotionX : F
    //   204: aload_0
    //   205: fload_2
    //   206: putfield mInitialMotionY : F
    //   209: aload_0
    //   210: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   213: aload_0
    //   214: getfield mSlideableView : Landroid/view/View;
    //   217: fload_3
    //   218: f2i
    //   219: fload_2
    //   220: f2i
    //   221: invokevirtual isViewUnder : (Landroid/view/View;II)Z
    //   224: ifeq -> 244
    //   227: aload_0
    //   228: aload_0
    //   229: getfield mSlideableView : Landroid/view/View;
    //   232: invokevirtual isDimmed : (Landroid/view/View;)Z
    //   235: ifeq -> 244
    //   238: iconst_1
    //   239: istore #4
    //   241: goto -> 247
    //   244: iconst_0
    //   245: istore #4
    //   247: iload #6
    //   249: istore #5
    //   251: aload_0
    //   252: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   255: aload_1
    //   256: invokevirtual shouldInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   259: ifne -> 277
    //   262: iload #4
    //   264: ifeq -> 274
    //   267: iload #6
    //   269: istore #5
    //   271: goto -> 277
    //   274: iconst_0
    //   275: istore #5
    //   277: iload #5
    //   279: ireturn
    //   280: aload_0
    //   281: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   284: invokevirtual cancel : ()V
    //   287: iconst_0
    //   288: ireturn
    //   289: aload_0
    //   290: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   293: invokevirtual cancel : ()V
    //   296: aload_0
    //   297: aload_1
    //   298: invokespecial onInterceptTouchEvent : (Landroid/view/MotionEvent;)Z
    //   301: ireturn
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isLayoutRtlSupport : ()Z
    //   4: istore #14
    //   6: iload #14
    //   8: ifeq -> 22
    //   11: aload_0
    //   12: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   15: iconst_2
    //   16: invokevirtual setEdgeTrackingEnabled : (I)V
    //   19: goto -> 30
    //   22: aload_0
    //   23: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
    //   26: iconst_1
    //   27: invokevirtual setEdgeTrackingEnabled : (I)V
    //   30: iload #4
    //   32: iload_2
    //   33: isub
    //   34: istore #9
    //   36: iload #14
    //   38: ifeq -> 49
    //   41: aload_0
    //   42: invokevirtual getPaddingRight : ()I
    //   45: istore_2
    //   46: goto -> 54
    //   49: aload_0
    //   50: invokevirtual getPaddingLeft : ()I
    //   53: istore_2
    //   54: iload #14
    //   56: ifeq -> 68
    //   59: aload_0
    //   60: invokevirtual getPaddingLeft : ()I
    //   63: istore #5
    //   65: goto -> 74
    //   68: aload_0
    //   69: invokevirtual getPaddingRight : ()I
    //   72: istore #5
    //   74: aload_0
    //   75: invokevirtual getPaddingTop : ()I
    //   78: istore #11
    //   80: aload_0
    //   81: invokevirtual getChildCount : ()I
    //   84: istore #10
    //   86: aload_0
    //   87: getfield mFirstLayout : Z
    //   90: ifeq -> 122
    //   93: aload_0
    //   94: getfield mCanSlide : Z
    //   97: ifeq -> 113
    //   100: aload_0
    //   101: getfield mPreservedOpenState : Z
    //   104: ifeq -> 113
    //   107: fconst_1
    //   108: fstore #6
    //   110: goto -> 116
    //   113: fconst_0
    //   114: fstore #6
    //   116: aload_0
    //   117: fload #6
    //   119: putfield mSlideOffset : F
    //   122: iload_2
    //   123: istore_3
    //   124: iconst_0
    //   125: istore #7
    //   127: iload #7
    //   129: iload #10
    //   131: if_icmpge -> 426
    //   134: aload_0
    //   135: iload #7
    //   137: invokevirtual getChildAt : (I)Landroid/view/View;
    //   140: astore #16
    //   142: aload #16
    //   144: invokevirtual getVisibility : ()I
    //   147: bipush #8
    //   149: if_icmpne -> 155
    //   152: goto -> 420
    //   155: aload #16
    //   157: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   160: checkcast androidx/slidingpanelayout/widget/SlidingPaneLayout$LayoutParams
    //   163: astore #15
    //   165: aload #16
    //   167: invokevirtual getMeasuredWidth : ()I
    //   170: istore #12
    //   172: aload #15
    //   174: getfield slideable : Z
    //   177: ifeq -> 318
    //   180: aload #15
    //   182: getfield leftMargin : I
    //   185: istore #13
    //   187: aload #15
    //   189: getfield rightMargin : I
    //   192: istore #4
    //   194: iload #9
    //   196: iload #5
    //   198: isub
    //   199: istore #8
    //   201: iload_2
    //   202: iload #8
    //   204: aload_0
    //   205: getfield mOverhangSize : I
    //   208: isub
    //   209: invokestatic min : (II)I
    //   212: iload_3
    //   213: isub
    //   214: iload #13
    //   216: iload #4
    //   218: iadd
    //   219: isub
    //   220: istore #13
    //   222: aload_0
    //   223: iload #13
    //   225: putfield mSlideRange : I
    //   228: iload #14
    //   230: ifeq -> 243
    //   233: aload #15
    //   235: getfield rightMargin : I
    //   238: istore #4
    //   240: goto -> 250
    //   243: aload #15
    //   245: getfield leftMargin : I
    //   248: istore #4
    //   250: iload_3
    //   251: iload #4
    //   253: iadd
    //   254: iload #13
    //   256: iadd
    //   257: iload #12
    //   259: iconst_2
    //   260: idiv
    //   261: iadd
    //   262: iload #8
    //   264: if_icmple -> 272
    //   267: iconst_1
    //   268: istore_1
    //   269: goto -> 274
    //   272: iconst_0
    //   273: istore_1
    //   274: aload #15
    //   276: iload_1
    //   277: putfield dimWhenOffset : Z
    //   280: iload #13
    //   282: i2f
    //   283: aload_0
    //   284: getfield mSlideOffset : F
    //   287: fmul
    //   288: f2i
    //   289: istore #8
    //   291: iload_3
    //   292: iload #4
    //   294: iload #8
    //   296: iadd
    //   297: iadd
    //   298: istore_3
    //   299: aload_0
    //   300: iload #8
    //   302: i2f
    //   303: aload_0
    //   304: getfield mSlideRange : I
    //   307: i2f
    //   308: fdiv
    //   309: putfield mSlideOffset : F
    //   312: iconst_0
    //   313: istore #4
    //   315: goto -> 356
    //   318: aload_0
    //   319: getfield mCanSlide : Z
    //   322: ifeq -> 351
    //   325: aload_0
    //   326: getfield mParallaxBy : I
    //   329: istore_3
    //   330: iload_3
    //   331: ifeq -> 351
    //   334: fconst_1
    //   335: aload_0
    //   336: getfield mSlideOffset : F
    //   339: fsub
    //   340: iload_3
    //   341: i2f
    //   342: fmul
    //   343: f2i
    //   344: istore #4
    //   346: iload_2
    //   347: istore_3
    //   348: goto -> 356
    //   351: iload_2
    //   352: istore_3
    //   353: iconst_0
    //   354: istore #4
    //   356: iload #14
    //   358: ifeq -> 380
    //   361: iload #9
    //   363: iload_3
    //   364: isub
    //   365: iload #4
    //   367: iadd
    //   368: istore #8
    //   370: iload #8
    //   372: iload #12
    //   374: isub
    //   375: istore #4
    //   377: goto -> 393
    //   380: iload_3
    //   381: iload #4
    //   383: isub
    //   384: istore #4
    //   386: iload #4
    //   388: iload #12
    //   390: iadd
    //   391: istore #8
    //   393: aload #16
    //   395: iload #4
    //   397: iload #11
    //   399: iload #8
    //   401: aload #16
    //   403: invokevirtual getMeasuredHeight : ()I
    //   406: iload #11
    //   408: iadd
    //   409: invokevirtual layout : (IIII)V
    //   412: iload_2
    //   413: aload #16
    //   415: invokevirtual getWidth : ()I
    //   418: iadd
    //   419: istore_2
    //   420: iinc #7, 1
    //   423: goto -> 127
    //   426: aload_0
    //   427: getfield mFirstLayout : Z
    //   430: ifeq -> 526
    //   433: aload_0
    //   434: getfield mCanSlide : Z
    //   437: ifeq -> 490
    //   440: aload_0
    //   441: getfield mParallaxBy : I
    //   444: ifeq -> 455
    //   447: aload_0
    //   448: aload_0
    //   449: getfield mSlideOffset : F
    //   452: invokespecial parallaxOtherViews : (F)V
    //   455: aload_0
    //   456: getfield mSlideableView : Landroid/view/View;
    //   459: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   462: checkcast androidx/slidingpanelayout/widget/SlidingPaneLayout$LayoutParams
    //   465: getfield dimWhenOffset : Z
    //   468: ifeq -> 518
    //   471: aload_0
    //   472: aload_0
    //   473: getfield mSlideableView : Landroid/view/View;
    //   476: aload_0
    //   477: getfield mSlideOffset : F
    //   480: aload_0
    //   481: getfield mSliderFadeColor : I
    //   484: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   487: goto -> 518
    //   490: iconst_0
    //   491: istore_2
    //   492: iload_2
    //   493: iload #10
    //   495: if_icmpge -> 518
    //   498: aload_0
    //   499: aload_0
    //   500: iload_2
    //   501: invokevirtual getChildAt : (I)Landroid/view/View;
    //   504: fconst_0
    //   505: aload_0
    //   506: getfield mSliderFadeColor : I
    //   509: invokespecial dimChildView : (Landroid/view/View;FI)V
    //   512: iinc #2, 1
    //   515: goto -> 492
    //   518: aload_0
    //   519: aload_0
    //   520: getfield mSlideableView : Landroid/view/View;
    //   523: invokevirtual updateObscuredViewsVisibility : (Landroid/view/View;)V
    //   526: aload_0
    //   527: iconst_0
    //   528: putfield mFirstLayout : Z
    //   531: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j;
    int n;
    int m = View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    if (m != 1073741824) {
      if (isInEditMode()) {
        if (m == Integer.MIN_VALUE) {
          j = i;
          n = k;
          paramInt1 = paramInt2;
        } else {
          j = i;
          n = k;
          paramInt1 = paramInt2;
          if (m == 0) {
            j = 300;
            n = k;
            paramInt1 = paramInt2;
          } 
        } 
      } else {
        throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
      } 
    } else {
      j = i;
      n = k;
      paramInt1 = paramInt2;
      if (k == 0)
        if (isInEditMode()) {
          j = i;
          n = k;
          paramInt1 = paramInt2;
          if (k == 0) {
            paramInt1 = 300;
            n = Integer.MIN_VALUE;
            j = i;
          } 
        } else {
          throw new IllegalStateException("Height must not be UNSPECIFIED");
        }  
    } 
    if (n != Integer.MIN_VALUE) {
      if (n != 1073741824) {
        paramInt1 = 0;
      } else {
        paramInt1 = paramInt1 - getPaddingTop() - getPaddingBottom();
      } 
      k = paramInt1;
    } else {
      k = paramInt1 - getPaddingTop() - getPaddingBottom();
      paramInt1 = 0;
    } 
    int i2 = j - getPaddingLeft() - getPaddingRight();
    int i3 = getChildCount();
    if (i3 > 2)
      Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported."); 
    this.mSlideableView = null;
    int i1 = 0;
    int i4 = 0;
    m = i2;
    float f = 0.0F;
    paramInt2 = paramInt1;
    while (i1 < i3) {
      View view = getChildAt(i1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (view.getVisibility() == 8) {
        layoutParams.dimWhenOffset = false;
        continue;
      } 
      float f1 = f;
      if (layoutParams.weight > 0.0F) {
        f += layoutParams.weight;
        f1 = f;
        if (layoutParams.width == 0)
          continue; 
      } 
      paramInt1 = layoutParams.leftMargin + layoutParams.rightMargin;
      if (layoutParams.width == -2) {
        paramInt1 = View.MeasureSpec.makeMeasureSpec(i2 - paramInt1, -2147483648);
      } else if (layoutParams.width == -1) {
        paramInt1 = View.MeasureSpec.makeMeasureSpec(i2 - paramInt1, 1073741824);
      } else {
        paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
      } 
      if (layoutParams.height == -2) {
        i = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
      } else if (layoutParams.height == -1) {
        i = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
      } else {
        i = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
      } 
      view.measure(paramInt1, i);
      i = view.getMeasuredWidth();
      int i5 = view.getMeasuredHeight();
      paramInt1 = paramInt2;
      if (n == Integer.MIN_VALUE) {
        paramInt1 = paramInt2;
        if (i5 > paramInt2)
          paramInt1 = Math.min(i5, k); 
      } 
      i = m - i;
      if (i < 0) {
        i6 = 1;
      } else {
        i6 = 0;
      } 
      layoutParams.slideable = i6;
      int i6 = i4 | i6;
      paramInt2 = paramInt1;
      i4 = i6;
      f = f1;
      m = i;
      if (layoutParams.slideable) {
        this.mSlideableView = view;
        m = i;
        f = f1;
        i4 = i6;
        paramInt2 = paramInt1;
      } 
      continue;
      i1++;
    } 
    if (i4 != 0 || f > 0.0F) {
      n = i2 - this.mOverhangSize;
      for (i = 0; i < i3; i++) {
        View view = getChildAt(i);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (view.getVisibility() != 8) {
            if (layoutParams.width == 0 && layoutParams.weight > 0.0F) {
              paramInt1 = 1;
            } else {
              paramInt1 = 0;
            } 
            if (paramInt1 != 0) {
              i1 = 0;
            } else {
              i1 = view.getMeasuredWidth();
            } 
            if (i4 != 0 && view != this.mSlideableView) {
              if (layoutParams.width < 0 && (i1 > n || layoutParams.weight > 0.0F)) {
                if (paramInt1 != 0) {
                  if (layoutParams.height == -2) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
                  } else if (layoutParams.height == -1) {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
                  } else {
                    paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                  } 
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
                } 
                view.measure(View.MeasureSpec.makeMeasureSpec(n, 1073741824), paramInt1);
              } 
            } else if (layoutParams.weight > 0.0F) {
              if (layoutParams.width == 0) {
                if (layoutParams.height == -2) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(k, -2147483648);
                } else if (layoutParams.height == -1) {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
                } else {
                  paramInt1 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                } 
              } else {
                paramInt1 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824);
              } 
              if (i4 != 0) {
                int i6 = i2 - layoutParams.leftMargin + layoutParams.rightMargin;
                int i5 = View.MeasureSpec.makeMeasureSpec(i6, 1073741824);
                if (i1 != i6)
                  view.measure(i5, paramInt1); 
              } else {
                int i5 = Math.max(0, m);
                view.measure(View.MeasureSpec.makeMeasureSpec(i1 + (int)(layoutParams.weight * i5 / f), 1073741824), paramInt1);
              } 
            } 
          } 
        } 
      } 
    } 
    setMeasuredDimension(j, paramInt2 + getPaddingTop() + getPaddingBottom());
    this.mCanSlide = i4;
    if (this.mDragHelper.getViewDragState() != 0 && i4 == 0)
      this.mDragHelper.abort(); 
  }
  
  void onPanelDragged(int paramInt) {
    if (this.mSlideableView == null) {
      this.mSlideOffset = 0.0F;
      return;
    } 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int j = this.mSlideableView.getWidth();
    int i = paramInt;
    if (bool)
      i = getWidth() - paramInt - j; 
    if (bool) {
      paramInt = getPaddingRight();
    } else {
      paramInt = getPaddingLeft();
    } 
    if (bool) {
      j = layoutParams.rightMargin;
    } else {
      j = layoutParams.leftMargin;
    } 
    float f = (i - paramInt + j) / this.mSlideRange;
    this.mSlideOffset = f;
    if (this.mParallaxBy != 0)
      parallaxOtherViews(f); 
    if (layoutParams.dimWhenOffset)
      dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor); 
    dispatchOnPanelSlide(this.mSlideableView);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (savedState.isOpen) {
      openPane();
    } else {
      closePane();
    } 
    this.mPreservedOpenState = savedState.isOpen;
  }
  
  protected Parcelable onSaveInstanceState() {
    boolean bool;
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (isSlideable()) {
      bool = isOpen();
    } else {
      bool = this.mPreservedOpenState;
    } 
    savedState.isOpen = bool;
    return (Parcelable)savedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      this.mFirstLayout = true; 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!this.mCanSlide)
      return super.onTouchEvent(paramMotionEvent); 
    this.mDragHelper.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    if (i != 0) {
      if (i == 1 && isDimmed(this.mSlideableView)) {
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        float f3 = f1 - this.mInitialMotionX;
        float f4 = f2 - this.mInitialMotionY;
        i = this.mDragHelper.getTouchSlop();
        if (f3 * f3 + f4 * f4 < (i * i) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)f1, (int)f2))
          closePane(this.mSlideableView, 0); 
      } 
    } else {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
    } 
    return true;
  }
  
  public boolean openPane() {
    return openPane(this.mSlideableView, 0);
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    super.requestChildFocus(paramView1, paramView2);
    if (!isInTouchMode() && !this.mCanSlide) {
      boolean bool;
      if (paramView1 == this.mSlideableView) {
        bool = true;
      } else {
        bool = false;
      } 
      this.mPreservedOpenState = bool;
    } 
  }
  
  void setAllChildrenVisible() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() == 4)
        view.setVisibility(0); 
    } 
  }
  
  public void setCoveredFadeColor(int paramInt) {
    this.mCoveredFadeColor = paramInt;
  }
  
  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener) {
    this.mPanelSlideListener = paramPanelSlideListener;
  }
  
  public void setParallaxDistance(int paramInt) {
    this.mParallaxBy = paramInt;
    requestLayout();
  }
  
  @Deprecated
  public void setShadowDrawable(Drawable paramDrawable) {
    setShadowDrawableLeft(paramDrawable);
  }
  
  public void setShadowDrawableLeft(Drawable paramDrawable) {
    this.mShadowDrawableLeft = paramDrawable;
  }
  
  public void setShadowDrawableRight(Drawable paramDrawable) {
    this.mShadowDrawableRight = paramDrawable;
  }
  
  @Deprecated
  public void setShadowResource(int paramInt) {
    setShadowDrawable(getResources().getDrawable(paramInt));
  }
  
  public void setShadowResourceLeft(int paramInt) {
    setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setShadowResourceRight(int paramInt) {
    setShadowDrawableRight(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setSliderFadeColor(int paramInt) {
    this.mSliderFadeColor = paramInt;
  }
  
  @Deprecated
  public void smoothSlideClosed() {
    closePane();
  }
  
  @Deprecated
  public void smoothSlideOpen() {
    openPane();
  }
  
  boolean smoothSlideTo(float paramFloat, int paramInt) {
    if (!this.mCanSlide)
      return false; 
    boolean bool = isLayoutRtlSupport();
    LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    if (bool) {
      paramInt = getPaddingRight();
      int j = layoutParams.rightMargin;
      int i = this.mSlideableView.getWidth();
      paramInt = (int)(getWidth() - (paramInt + j) + paramFloat * this.mSlideRange + i);
    } else {
      paramInt = (int)((getPaddingLeft() + layoutParams.leftMargin) + paramFloat * this.mSlideRange);
    } 
    ViewDragHelper viewDragHelper = this.mDragHelper;
    View view = this.mSlideableView;
    if (viewDragHelper.smoothSlideViewTo(view, paramInt, view.getTop())) {
      setAllChildrenVisible();
      ViewCompat.postInvalidateOnAnimation((View)this);
      return true;
    } 
    return false;
  }
  
  void updateObscuredViewsVisibility(View paramView) {
    int i;
    int j;
    byte b1;
    byte b2;
    byte b3;
    byte b4;
    boolean bool = isLayoutRtlSupport();
    if (bool) {
      i = getWidth() - getPaddingRight();
    } else {
      i = getPaddingLeft();
    } 
    if (bool) {
      j = getPaddingLeft();
    } else {
      j = getWidth() - getPaddingRight();
    } 
    int m = getPaddingTop();
    int k = getHeight();
    int n = getPaddingBottom();
    if (paramView != null && viewIsOpaque(paramView)) {
      b4 = paramView.getLeft();
      b1 = paramView.getRight();
      b2 = paramView.getTop();
      b3 = paramView.getBottom();
    } else {
      b4 = 0;
      b1 = 0;
      b2 = 0;
      b3 = 0;
    } 
    int i1 = getChildCount();
    for (byte b5 = 0; b5 < i1; b5++) {
      View view = getChildAt(b5);
      if (view == paramView)
        break; 
      if (view.getVisibility() != 8) {
        if (bool) {
          i2 = j;
        } else {
          i2 = i;
        } 
        int i4 = Math.max(i2, view.getLeft());
        int i3 = Math.max(m, view.getTop());
        if (bool) {
          i2 = i;
        } else {
          i2 = j;
        } 
        int i2 = Math.min(i2, view.getRight());
        int i5 = Math.min(k - n, view.getBottom());
        if (i4 >= b4 && i3 >= b2 && i2 <= b1 && i5 <= b3) {
          i2 = 4;
        } else {
          i2 = 0;
        } 
        view.setVisibility(i2);
      } 
    } 
  }
  
  class AccessibilityDelegate extends AccessibilityDelegateCompat {
    private final Rect mTmpRect = new Rect();
    
    final SlidingPaneLayout this$0;
    
    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat2) {
      Rect rect = this.mTmpRect;
      param1AccessibilityNodeInfoCompat2.getBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInParent(rect);
      param1AccessibilityNodeInfoCompat2.getBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setBoundsInScreen(rect);
      param1AccessibilityNodeInfoCompat1.setVisibleToUser(param1AccessibilityNodeInfoCompat2.isVisibleToUser());
      param1AccessibilityNodeInfoCompat1.setPackageName(param1AccessibilityNodeInfoCompat2.getPackageName());
      param1AccessibilityNodeInfoCompat1.setClassName(param1AccessibilityNodeInfoCompat2.getClassName());
      param1AccessibilityNodeInfoCompat1.setContentDescription(param1AccessibilityNodeInfoCompat2.getContentDescription());
      param1AccessibilityNodeInfoCompat1.setEnabled(param1AccessibilityNodeInfoCompat2.isEnabled());
      param1AccessibilityNodeInfoCompat1.setClickable(param1AccessibilityNodeInfoCompat2.isClickable());
      param1AccessibilityNodeInfoCompat1.setFocusable(param1AccessibilityNodeInfoCompat2.isFocusable());
      param1AccessibilityNodeInfoCompat1.setFocused(param1AccessibilityNodeInfoCompat2.isFocused());
      param1AccessibilityNodeInfoCompat1.setAccessibilityFocused(param1AccessibilityNodeInfoCompat2.isAccessibilityFocused());
      param1AccessibilityNodeInfoCompat1.setSelected(param1AccessibilityNodeInfoCompat2.isSelected());
      param1AccessibilityNodeInfoCompat1.setLongClickable(param1AccessibilityNodeInfoCompat2.isLongClickable());
      param1AccessibilityNodeInfoCompat1.addAction(param1AccessibilityNodeInfoCompat2.getActions());
      param1AccessibilityNodeInfoCompat1.setMovementGranularities(param1AccessibilityNodeInfoCompat2.getMovementGranularities());
    }
    
    public boolean filter(View param1View) {
      return SlidingPaneLayout.this.isDimmed(param1View);
    }
    
    public void onInitializeAccessibilityEvent(View param1View, AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1View, param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(param1AccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(param1View, accessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(param1AccessibilityNodeInfoCompat, accessibilityNodeInfoCompat);
      accessibilityNodeInfoCompat.recycle();
      param1AccessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
      param1AccessibilityNodeInfoCompat.setSource(param1View);
      ViewParent viewParent = ViewCompat.getParentForAccessibility(param1View);
      if (viewParent instanceof View)
        param1AccessibilityNodeInfoCompat.setParent((View)viewParent); 
      int i = SlidingPaneLayout.this.getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = SlidingPaneLayout.this.getChildAt(b);
        if (!filter(view) && view.getVisibility() == 0) {
          ViewCompat.setImportantForAccessibility(view, 1);
          param1AccessibilityNodeInfoCompat.addChild(view);
        } 
      } 
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup param1ViewGroup, View param1View, AccessibilityEvent param1AccessibilityEvent) {
      return !filter(param1View) ? super.onRequestSendAccessibilityEvent(param1ViewGroup, param1View, param1AccessibilityEvent) : false;
    }
  }
  
  private class DisableLayerRunnable implements Runnable {
    final View mChildView;
    
    final SlidingPaneLayout this$0;
    
    DisableLayerRunnable(View param1View) {
      this.mChildView = param1View;
    }
    
    public void run() {
      if (this.mChildView.getParent() == SlidingPaneLayout.this) {
        this.mChildView.setLayerType(0, null);
        SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
      } 
      SlidingPaneLayout.this.mPostedRunnables.remove(this);
    }
  }
  
  private class DragHelperCallback extends ViewDragHelper.Callback {
    final SlidingPaneLayout this$0;
    
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      SlidingPaneLayout.LayoutParams layoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
        param1Int2 = SlidingPaneLayout.this.getWidth() - SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth();
        int i = SlidingPaneLayout.this.mSlideRange;
        param1Int1 = Math.max(Math.min(param1Int1, param1Int2), param1Int2 - i);
      } else {
        param1Int2 = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
        int i = SlidingPaneLayout.this.mSlideRange;
        param1Int1 = Math.min(Math.max(param1Int1, param1Int2), i + param1Int2);
      } 
      return param1Int1;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return param1View.getTop();
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return SlidingPaneLayout.this.mSlideRange;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {
      SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, param1Int2);
    }
    
    public void onViewCaptured(View param1View, int param1Int) {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }
    
    public void onViewDragStateChanged(int param1Int) {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0)
        if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
          SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.updateObscuredViewsVisibility(slidingPaneLayout.mSlideableView);
          slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.dispatchOnPanelClosed(slidingPaneLayout.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = false;
        } else {
          SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
          slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
          SlidingPaneLayout.this.mPreservedOpenState = true;
        }  
    }
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      SlidingPaneLayout.this.onPanelDragged(param1Int1);
      SlidingPaneLayout.this.invalidate();
    }
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   4: checkcast androidx/slidingpanelayout/widget/SlidingPaneLayout$LayoutParams
      //   7: astore #7
      //   9: aload_0
      //   10: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   13: invokevirtual isLayoutRtlSupport : ()Z
      //   16: ifeq -> 109
      //   19: aload_0
      //   20: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   23: invokevirtual getPaddingRight : ()I
      //   26: aload #7
      //   28: getfield rightMargin : I
      //   31: iadd
      //   32: istore #5
      //   34: fload_2
      //   35: fconst_0
      //   36: fcmpg
      //   37: iflt -> 67
      //   40: iload #5
      //   42: istore #4
      //   44: fload_2
      //   45: fconst_0
      //   46: fcmpl
      //   47: ifne -> 79
      //   50: iload #5
      //   52: istore #4
      //   54: aload_0
      //   55: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   58: getfield mSlideOffset : F
      //   61: ldc 0.5
      //   63: fcmpl
      //   64: ifle -> 79
      //   67: iload #5
      //   69: aload_0
      //   70: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   73: getfield mSlideRange : I
      //   76: iadd
      //   77: istore #4
      //   79: aload_0
      //   80: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   83: getfield mSlideableView : Landroid/view/View;
      //   86: invokevirtual getWidth : ()I
      //   89: istore #5
      //   91: aload_0
      //   92: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   95: invokevirtual getWidth : ()I
      //   98: iload #4
      //   100: isub
      //   101: iload #5
      //   103: isub
      //   104: istore #4
      //   106: goto -> 176
      //   109: aload_0
      //   110: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   113: invokevirtual getPaddingLeft : ()I
      //   116: istore #4
      //   118: aload #7
      //   120: getfield leftMargin : I
      //   123: iload #4
      //   125: iadd
      //   126: istore #5
      //   128: fload_2
      //   129: fconst_0
      //   130: fcmpl
      //   131: istore #6
      //   133: iload #6
      //   135: ifgt -> 164
      //   138: iload #5
      //   140: istore #4
      //   142: iload #6
      //   144: ifne -> 176
      //   147: iload #5
      //   149: istore #4
      //   151: aload_0
      //   152: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   155: getfield mSlideOffset : F
      //   158: ldc 0.5
      //   160: fcmpl
      //   161: ifle -> 176
      //   164: iload #5
      //   166: aload_0
      //   167: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   170: getfield mSlideRange : I
      //   173: iadd
      //   174: istore #4
      //   176: aload_0
      //   177: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   180: getfield mDragHelper : Landroidx/customview/widget/ViewDragHelper;
      //   183: iload #4
      //   185: aload_1
      //   186: invokevirtual getTop : ()I
      //   189: invokevirtual settleCapturedViewAt : (II)Z
      //   192: pop
      //   193: aload_0
      //   194: getfield this$0 : Landroidx/slidingpanelayout/widget/SlidingPaneLayout;
      //   197: invokevirtual invalidate : ()V
      //   200: return
    }
    
    public boolean tryCaptureView(View param1View, int param1Int) {
      return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)param1View.getLayoutParams()).slideable;
    }
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    private static final int[] ATTRS = new int[] { 16843137 };
    
    Paint dimPaint;
    
    boolean dimWhenOffset;
    
    boolean slideable;
    
    public float weight = 0.0F;
    
    public LayoutParams() {
      super(-1, -1);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, ATTRS);
      this.weight = typedArray.getFloat(0, 0.0F);
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
    }
  }
  
  public static interface PanelSlideListener {
    void onPanelClosed(View param1View);
    
    void onPanelOpened(View param1View);
    
    void onPanelSlide(View param1View, float param1Float);
  }
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel) {
          return new SlidingPaneLayout.SavedState(param2Parcel, null);
        }
        
        public SlidingPaneLayout.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new SlidingPaneLayout.SavedState(param2Parcel, null);
        }
        
        public SlidingPaneLayout.SavedState[] newArray(int param2Int) {
          return new SlidingPaneLayout.SavedState[param2Int];
        }
      };
    
    boolean isOpen;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOpen = bool;
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.isOpen);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel) {
      return new SlidingPaneLayout.SavedState(param1Parcel, null);
    }
    
    public SlidingPaneLayout.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new SlidingPaneLayout.SavedState(param1Parcel, null);
    }
    
    public SlidingPaneLayout.SavedState[] newArray(int param1Int) {
      return new SlidingPaneLayout.SavedState[param1Int];
    }
  }
  
  public static class SimplePanelSlideListener implements PanelSlideListener {
    public void onPanelClosed(View param1View) {}
    
    public void onPanelOpened(View param1View) {}
    
    public void onPanelSlide(View param1View, float param1Float) {}
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\slidingpanelayout\widget\SlidingPaneLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */