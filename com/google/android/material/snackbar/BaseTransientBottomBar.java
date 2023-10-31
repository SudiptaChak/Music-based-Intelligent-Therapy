package com.google.android.material.snackbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.internal.ThemeEnforcement;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
  static final int ANIMATION_DURATION = 250;
  
  static final int ANIMATION_FADE_DURATION = 180;
  
  public static final int LENGTH_INDEFINITE = -2;
  
  public static final int LENGTH_LONG = 0;
  
  public static final int LENGTH_SHORT = -1;
  
  static final int MSG_DISMISS = 1;
  
  static final int MSG_SHOW = 0;
  
  private static final int[] SNACKBAR_STYLE_ATTR;
  
  private static final boolean USE_OFFSET_API;
  
  static final Handler handler;
  
  private final AccessibilityManager accessibilityManager;
  
  private Behavior behavior;
  
  private List<BaseCallback<B>> callbacks;
  
  private final ContentViewCallback contentViewCallback;
  
  private final Context context;
  
  private int duration;
  
  final SnackbarManager.Callback managerCallback = new SnackbarManager.Callback() {
      final BaseTransientBottomBar this$0;
      
      public void dismiss(int param1Int) {
        BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, param1Int, 0, BaseTransientBottomBar.this));
      }
      
      public void show() {
        BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
      }
    };
  
  private final ViewGroup targetParent;
  
  protected final SnackbarBaseLayout view;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    USE_OFFSET_API = bool;
    SNACKBAR_STYLE_ATTR = new int[] { R.attr.snackbarStyle };
    handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
          public boolean handleMessage(Message param1Message) {
            int i = param1Message.what;
            if (i != 0) {
              if (i != 1)
                return false; 
              ((BaseTransientBottomBar)param1Message.obj).hideView(param1Message.arg1);
              return true;
            } 
            ((BaseTransientBottomBar)param1Message.obj).showView();
            return true;
          }
        });
  }
  
  protected BaseTransientBottomBar(ViewGroup paramViewGroup, View paramView, ContentViewCallback paramContentViewCallback) {
    if (paramViewGroup != null) {
      if (paramView != null) {
        if (paramContentViewCallback != null) {
          this.targetParent = paramViewGroup;
          this.contentViewCallback = paramContentViewCallback;
          Context context = paramViewGroup.getContext();
          this.context = context;
          ThemeEnforcement.checkAppCompatTheme(context);
          SnackbarBaseLayout snackbarBaseLayout = (SnackbarBaseLayout)LayoutInflater.from(this.context).inflate(getSnackbarBaseLayoutResId(), this.targetParent, false);
          this.view = snackbarBaseLayout;
          snackbarBaseLayout.addView(paramView);
          ViewCompat.setAccessibilityLiveRegion((View)this.view, 1);
          ViewCompat.setImportantForAccessibility((View)this.view, 1);
          ViewCompat.setFitsSystemWindows((View)this.view, true);
          ViewCompat.setOnApplyWindowInsetsListener((View)this.view, new OnApplyWindowInsetsListener() {
                final BaseTransientBottomBar this$0;
                
                public WindowInsetsCompat onApplyWindowInsets(View param1View, WindowInsetsCompat param1WindowInsetsCompat) {
                  param1View.setPadding(param1View.getPaddingLeft(), param1View.getPaddingTop(), param1View.getPaddingRight(), param1WindowInsetsCompat.getSystemWindowInsetBottom());
                  return param1WindowInsetsCompat;
                }
              });
          ViewCompat.setAccessibilityDelegate((View)this.view, new AccessibilityDelegateCompat() {
                final BaseTransientBottomBar this$0;
                
                public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
                  super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
                  param1AccessibilityNodeInfoCompat.addAction(1048576);
                  param1AccessibilityNodeInfoCompat.setDismissable(true);
                }
                
                public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
                  if (param1Int == 1048576) {
                    BaseTransientBottomBar.this.dismiss();
                    return true;
                  } 
                  return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
                }
              });
          this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
          return;
        } 
        throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
      } 
      throw new IllegalArgumentException("Transient bottom bar must have non-null content");
    } 
    throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
  }
  
  private void animateViewOut(final int event) {
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setIntValues(new int[] { 0, getTranslationYBottom() });
    valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.setDuration(250L);
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          final int val$event;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewHidden(event);
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
          }
        });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          private int previousAnimatedIntValue = 0;
          
          final BaseTransientBottomBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            int i = ((Integer)param1ValueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
              ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, i - this.previousAnimatedIntValue);
            } else {
              BaseTransientBottomBar.this.view.setTranslationY(i);
            } 
            this.previousAnimatedIntValue = i;
          }
        });
    valueAnimator.start();
  }
  
  private int getTranslationYBottom() {
    int j = this.view.getHeight();
    ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
    int i = j;
    if (layoutParams instanceof ViewGroup.MarginLayoutParams)
      i = j + ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin; 
    return i;
  }
  
  public B addCallback(BaseCallback<B> paramBaseCallback) {
    if (paramBaseCallback == null)
      return (B)this; 
    if (this.callbacks == null)
      this.callbacks = new ArrayList<BaseCallback<B>>(); 
    this.callbacks.add(paramBaseCallback);
    return (B)this;
  }
  
  void animateViewIn() {
    final int translationYBottom = getTranslationYBottom();
    if (USE_OFFSET_API) {
      ViewCompat.offsetTopAndBottom((View)this.view, i);
    } else {
      this.view.setTranslationY(i);
    } 
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setIntValues(new int[] { i, 0 });
    valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
    valueAnimator.setDuration(250L);
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BaseTransientBottomBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BaseTransientBottomBar.this.onViewShown();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
          }
        });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          private int previousAnimatedIntValue = translationYBottom;
          
          final BaseTransientBottomBar this$0;
          
          final int val$translationYBottom;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            int i = ((Integer)param1ValueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
              ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, i - this.previousAnimatedIntValue);
            } else {
              BaseTransientBottomBar.this.view.setTranslationY(i);
            } 
            this.previousAnimatedIntValue = i;
          }
        });
    valueAnimator.start();
  }
  
  public void dismiss() {
    dispatchDismiss(3);
  }
  
  protected void dispatchDismiss(int paramInt) {
    SnackbarManager.getInstance().dismiss(this.managerCallback, paramInt);
  }
  
  public Behavior getBehavior() {
    return this.behavior;
  }
  
  public Context getContext() {
    return this.context;
  }
  
  public int getDuration() {
    return this.duration;
  }
  
  protected SwipeDismissBehavior<? extends View> getNewBehavior() {
    return new Behavior();
  }
  
  protected int getSnackbarBaseLayoutResId() {
    int i;
    if (hasSnackbarStyleAttr()) {
      i = R.layout.mtrl_layout_snackbar;
    } else {
      i = R.layout.design_layout_snackbar;
    } 
    return i;
  }
  
  public View getView() {
    return (View)this.view;
  }
  
  protected boolean hasSnackbarStyleAttr() {
    TypedArray typedArray = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
    boolean bool = false;
    int i = typedArray.getResourceId(0, -1);
    typedArray.recycle();
    if (i != -1)
      bool = true; 
    return bool;
  }
  
  final void hideView(int paramInt) {
    if (shouldAnimate() && this.view.getVisibility() == 0) {
      animateViewOut(paramInt);
    } else {
      onViewHidden(paramInt);
    } 
  }
  
  public boolean isShown() {
    return SnackbarManager.getInstance().isCurrent(this.managerCallback);
  }
  
  public boolean isShownOrQueued() {
    return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
  }
  
  void onViewHidden(int paramInt) {
    SnackbarManager.getInstance().onDismissed(this.managerCallback);
    List<BaseCallback<B>> list = this.callbacks;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((BaseCallback<BaseTransientBottomBar>)this.callbacks.get(i)).onDismissed(this, paramInt);  
    ViewParent viewParent = this.view.getParent();
    if (viewParent instanceof ViewGroup)
      ((ViewGroup)viewParent).removeView((View)this.view); 
  }
  
  void onViewShown() {
    SnackbarManager.getInstance().onShown(this.managerCallback);
    List<BaseCallback<B>> list = this.callbacks;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((BaseCallback<BaseTransientBottomBar>)this.callbacks.get(i)).onShown(this);  
  }
  
  public B removeCallback(BaseCallback<B> paramBaseCallback) {
    if (paramBaseCallback == null)
      return (B)this; 
    List<BaseCallback<B>> list = this.callbacks;
    if (list == null)
      return (B)this; 
    list.remove(paramBaseCallback);
    return (B)this;
  }
  
  public B setBehavior(Behavior paramBehavior) {
    this.behavior = paramBehavior;
    return (B)this;
  }
  
  public B setDuration(int paramInt) {
    this.duration = paramInt;
    return (B)this;
  }
  
  boolean shouldAnimate() {
    AccessibilityManager accessibilityManager = this.accessibilityManager;
    boolean bool = true;
    List list = accessibilityManager.getEnabledAccessibilityServiceList(1);
    if (list == null || !list.isEmpty())
      bool = false; 
    return bool;
  }
  
  public void show() {
    SnackbarManager.getInstance().show(getDuration(), this.managerCallback);
  }
  
  final void showView() {
    if (this.view.getParent() == null) {
      ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
      if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams)layoutParams;
        Behavior behavior = this.behavior;
        SwipeDismissBehavior<? extends View> swipeDismissBehavior = behavior;
        if (behavior == null)
          swipeDismissBehavior = getNewBehavior(); 
        if (swipeDismissBehavior instanceof Behavior)
          ((Behavior)swipeDismissBehavior).setBaseTransientBottomBar(this); 
        swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
              final BaseTransientBottomBar this$0;
              
              public void onDismiss(View param1View) {
                param1View.setVisibility(8);
                BaseTransientBottomBar.this.dispatchDismiss(0);
              }
              
              public void onDragStateChanged(int param1Int) {
                if (param1Int != 0) {
                  if (param1Int == 1 || param1Int == 2)
                    SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback); 
                } else {
                  SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
                } 
              }
            });
        layoutParams1.setBehavior((CoordinatorLayout.Behavior)swipeDismissBehavior);
        layoutParams1.insetEdge = 80;
      } 
      this.targetParent.addView((View)this.view);
    } 
    this.view.setOnAttachStateChangeListener(new OnAttachStateChangeListener() {
          final BaseTransientBottomBar this$0;
          
          public void onViewAttachedToWindow(View param1View) {}
          
          public void onViewDetachedFromWindow(View param1View) {
            if (BaseTransientBottomBar.this.isShownOrQueued())
              BaseTransientBottomBar.handler.post(new Runnable() {
                    final BaseTransientBottomBar.null this$1;
                    
                    public void run() {
                      BaseTransientBottomBar.this.onViewHidden(3);
                    }
                  }); 
          }
        });
    if (ViewCompat.isLaidOut((View)this.view)) {
      if (shouldAnimate()) {
        animateViewIn();
      } else {
        onViewShown();
      } 
    } else {
      this.view.setOnLayoutChangeListener(new OnLayoutChangeListener() {
            final BaseTransientBottomBar this$0;
            
            public void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
              BaseTransientBottomBar.this.view.setOnLayoutChangeListener((BaseTransientBottomBar.OnLayoutChangeListener)null);
              if (BaseTransientBottomBar.this.shouldAnimate()) {
                BaseTransientBottomBar.this.animateViewIn();
              } else {
                BaseTransientBottomBar.this.onViewShown();
              } 
            }
          });
    } 
  }
  
  public static abstract class BaseCallback<B> {
    public static final int DISMISS_EVENT_ACTION = 1;
    
    public static final int DISMISS_EVENT_CONSECUTIVE = 4;
    
    public static final int DISMISS_EVENT_MANUAL = 3;
    
    public static final int DISMISS_EVENT_SWIPE = 0;
    
    public static final int DISMISS_EVENT_TIMEOUT = 2;
    
    public void onDismissed(B param1B, int param1Int) {}
    
    public void onShown(B param1B) {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface DismissEvent {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DismissEvent {}
  
  public static class Behavior extends SwipeDismissBehavior<View> {
    private final BaseTransientBottomBar.BehaviorDelegate delegate = new BaseTransientBottomBar.BehaviorDelegate(this);
    
    private void setBaseTransientBottomBar(BaseTransientBottomBar<?> param1BaseTransientBottomBar) {
      this.delegate.setBaseTransientBottomBar(param1BaseTransientBottomBar);
    }
    
    public boolean canSwipeDismissView(View param1View) {
      return this.delegate.canSwipeDismissView(param1View);
    }
    
    public boolean onInterceptTouchEvent(CoordinatorLayout param1CoordinatorLayout, View param1View, MotionEvent param1MotionEvent) {
      this.delegate.onInterceptTouchEvent(param1CoordinatorLayout, param1View, param1MotionEvent);
      return super.onInterceptTouchEvent(param1CoordinatorLayout, param1View, param1MotionEvent);
    }
  }
  
  public static class BehaviorDelegate {
    private SnackbarManager.Callback managerCallback;
    
    public BehaviorDelegate(SwipeDismissBehavior<?> param1SwipeDismissBehavior) {
      param1SwipeDismissBehavior.setStartAlphaSwipeDistance(0.1F);
      param1SwipeDismissBehavior.setEndAlphaSwipeDistance(0.6F);
      param1SwipeDismissBehavior.setSwipeDirection(0);
    }
    
    public boolean canSwipeDismissView(View param1View) {
      return param1View instanceof BaseTransientBottomBar.SnackbarBaseLayout;
    }
    
    public void onInterceptTouchEvent(CoordinatorLayout param1CoordinatorLayout, View param1View, MotionEvent param1MotionEvent) {
      int i = param1MotionEvent.getActionMasked();
      if (i != 0) {
        if (i == 1 || i == 3)
          SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback); 
      } else if (param1CoordinatorLayout.isPointInChildBounds(param1View, (int)param1MotionEvent.getX(), (int)param1MotionEvent.getY())) {
        SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
      } 
    }
    
    public void setBaseTransientBottomBar(BaseTransientBottomBar<?> param1BaseTransientBottomBar) {
      this.managerCallback = param1BaseTransientBottomBar.managerCallback;
    }
  }
  
  @Deprecated
  public static interface ContentViewCallback extends ContentViewCallback {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Duration {}
  
  protected static interface OnAttachStateChangeListener {
    void onViewAttachedToWindow(View param1View);
    
    void onViewDetachedFromWindow(View param1View);
  }
  
  protected static interface OnLayoutChangeListener {
    void onLayoutChange(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  protected static class SnackbarBaseLayout extends FrameLayout {
    private final AccessibilityManager accessibilityManager;
    
    private BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener;
    
    private BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener;
    
    private final AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener;
    
    protected SnackbarBaseLayout(Context param1Context) {
      this(param1Context, (AttributeSet)null);
    }
    
    protected SnackbarBaseLayout(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.SnackbarLayout);
      if (typedArray.hasValue(R.styleable.SnackbarLayout_elevation))
        ViewCompat.setElevation((View)this, typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0)); 
      typedArray.recycle();
      this.accessibilityManager = (AccessibilityManager)param1Context.getSystemService("accessibility");
      AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener = new AccessibilityManagerCompat.TouchExplorationStateChangeListener() {
          final BaseTransientBottomBar.SnackbarBaseLayout this$0;
          
          public void onTouchExplorationStateChanged(boolean param2Boolean) {
            BaseTransientBottomBar.SnackbarBaseLayout.this.setClickableOrFocusableBasedOnAccessibility(param2Boolean);
          }
        };
      this.touchExplorationStateChangeListener = touchExplorationStateChangeListener;
      AccessibilityManagerCompat.addTouchExplorationStateChangeListener(this.accessibilityManager, touchExplorationStateChangeListener);
      setClickableOrFocusableBasedOnAccessibility(this.accessibilityManager.isTouchExplorationEnabled());
    }
    
    private void setClickableOrFocusableBasedOnAccessibility(boolean param1Boolean) {
      setClickable(param1Boolean ^ true);
      setFocusable(param1Boolean);
    }
    
    protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
      if (onAttachStateChangeListener != null)
        onAttachStateChangeListener.onViewAttachedToWindow((View)this); 
      ViewCompat.requestApplyInsets((View)this);
    }
    
    protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
      if (onAttachStateChangeListener != null)
        onAttachStateChangeListener.onViewDetachedFromWindow((View)this); 
      AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.onLayout(param1Boolean, param1Int1, param1Int2, param1Int3, param1Int4);
      BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener = this.onLayoutChangeListener;
      if (onLayoutChangeListener != null)
        onLayoutChangeListener.onLayoutChange((View)this, param1Int1, param1Int2, param1Int3, param1Int4); 
    }
    
    void setOnAttachStateChangeListener(BaseTransientBottomBar.OnAttachStateChangeListener param1OnAttachStateChangeListener) {
      this.onAttachStateChangeListener = param1OnAttachStateChangeListener;
    }
    
    void setOnLayoutChangeListener(BaseTransientBottomBar.OnLayoutChangeListener param1OnLayoutChangeListener) {
      this.onLayoutChangeListener = param1OnLayoutChangeListener;
    }
  }
  
  class null implements AccessibilityManagerCompat.TouchExplorationStateChangeListener {
    final BaseTransientBottomBar.SnackbarBaseLayout this$0;
    
    public void onTouchExplorationStateChanged(boolean param1Boolean) {
      this.this$0.setClickableOrFocusableBasedOnAccessibility(param1Boolean);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\snackbar\BaseTransientBottomBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */