package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapePathModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
  private static final long ANIMATION_DURATION = 300L;
  
  public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
  
  public static final int FAB_ALIGNMENT_MODE_END = 1;
  
  private Animator attachAnimator;
  
  private int fabAlignmentMode;
  
  AnimatorListenerAdapter fabAnimationListener = new AnimatorListenerAdapter() {
      final BottomAppBar this$0;
      
      public void onAnimationStart(Animator param1Animator) {
        BottomAppBar bottomAppBar = BottomAppBar.this;
        bottomAppBar.maybeAnimateAttachChange(bottomAppBar.fabAttached);
        bottomAppBar = BottomAppBar.this;
        bottomAppBar.maybeAnimateMenuView(bottomAppBar.fabAlignmentMode, BottomAppBar.this.fabAttached);
      }
    };
  
  private boolean fabAttached = true;
  
  private final int fabOffsetEndMode;
  
  private boolean hideOnScroll;
  
  private final MaterialShapeDrawable materialShapeDrawable;
  
  private Animator menuAnimator;
  
  private Animator modeAnimator;
  
  private final BottomAppBarTopEdgeTreatment topEdgeTreatment;
  
  public BottomAppBar(Context paramContext) {
    this(paramContext, (AttributeSet)null, 0);
  }
  
  public BottomAppBar(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.bottomAppBarStyle);
  }
  
  public BottomAppBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray typedArray = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.BottomAppBar, paramInt, R.style.Widget_MaterialComponents_BottomAppBar, new int[0]);
    ColorStateList colorStateList = MaterialResources.getColorStateList(paramContext, typedArray, R.styleable.BottomAppBar_backgroundTint);
    float f2 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleMargin, 0);
    float f3 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
    float f1 = typedArray.getDimensionPixelOffset(R.styleable.BottomAppBar_fabCradleVerticalOffset, 0);
    this.fabAlignmentMode = typedArray.getInt(R.styleable.BottomAppBar_fabAlignmentMode, 0);
    this.hideOnScroll = typedArray.getBoolean(R.styleable.BottomAppBar_hideOnScroll, false);
    typedArray.recycle();
    this.fabOffsetEndMode = getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fabOffsetEndMode);
    this.topEdgeTreatment = new BottomAppBarTopEdgeTreatment(f2, f3, f1);
    ShapePathModel shapePathModel = new ShapePathModel();
    shapePathModel.setTopEdge(this.topEdgeTreatment);
    MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapePathModel);
    this.materialShapeDrawable = materialShapeDrawable;
    materialShapeDrawable.setShadowEnabled(true);
    this.materialShapeDrawable.setPaintStyle(Paint.Style.FILL);
    DrawableCompat.setTintList((Drawable)this.materialShapeDrawable, colorStateList);
    ViewCompat.setBackground((View)this, (Drawable)this.materialShapeDrawable);
  }
  
  private void addFabAnimationListeners(FloatingActionButton paramFloatingActionButton) {
    removeFabAnimationListeners(paramFloatingActionButton);
    paramFloatingActionButton.addOnHideAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
    paramFloatingActionButton.addOnShowAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
  }
  
  private void cancelAnimations() {
    Animator animator = this.attachAnimator;
    if (animator != null)
      animator.cancel(); 
    animator = this.menuAnimator;
    if (animator != null)
      animator.cancel(); 
    animator = this.modeAnimator;
    if (animator != null)
      animator.cancel(); 
  }
  
  private void createCradleShapeAnimation(boolean paramBoolean, List<Animator> paramList) {
    float f1;
    if (paramBoolean)
      this.topEdgeTreatment.setHorizontalOffset(getFabTranslationX()); 
    float f2 = this.materialShapeDrawable.getInterpolation();
    if (paramBoolean) {
      f1 = 1.0F;
    } else {
      f1 = 0.0F;
    } 
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { f2, f1 });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BottomAppBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            BottomAppBar.this.materialShapeDrawable.setInterpolation(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
          }
        });
    valueAnimator.setDuration(300L);
    paramList.add(valueAnimator);
  }
  
  private void createCradleTranslationAnimation(int paramInt, List<Animator> paramList) {
    if (!this.fabAttached)
      return; 
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { this.topEdgeTreatment.getHorizontalOffset(), getFabTranslationX(paramInt) });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          final BottomAppBar this$0;
          
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            BottomAppBar.this.topEdgeTreatment.setHorizontalOffset(((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
            BottomAppBar.this.materialShapeDrawable.invalidateSelf();
          }
        });
    valueAnimator.setDuration(300L);
    paramList.add(valueAnimator);
  }
  
  private void createFabTranslationXAnimation(int paramInt, List<Animator> paramList) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(findDependentFab(), "translationX", new float[] { getFabTranslationX(paramInt) });
    objectAnimator.setDuration(300L);
    paramList.add(objectAnimator);
  }
  
  private void createFabTranslationYAnimation(boolean paramBoolean, List<Animator> paramList) {
    FloatingActionButton floatingActionButton = findDependentFab();
    if (floatingActionButton == null)
      return; 
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(floatingActionButton, "translationY", new float[] { getFabTranslationY(paramBoolean) });
    objectAnimator.setDuration(300L);
    paramList.add(objectAnimator);
  }
  
  private void createMenuViewTranslationAnimation(final int targetMode, final boolean targetAttached, List<Animator> paramList) {
    AnimatorSet animatorSet;
    final ActionMenuView actionMenuView = getActionMenuView();
    if (actionMenuView == null)
      return; 
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[] { 1.0F });
    if ((this.fabAttached || (targetAttached && isVisibleFab())) && (this.fabAlignmentMode == 1 || targetMode == 1)) {
      ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[] { 0.0F });
      objectAnimator1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public boolean cancelled;
            
            final BottomAppBar this$0;
            
            final ActionMenuView val$actionMenuView;
            
            final boolean val$targetAttached;
            
            final int val$targetMode;
            
            public void onAnimationCancel(Animator param1Animator) {
              this.cancelled = true;
            }
            
            public void onAnimationEnd(Animator param1Animator) {
              if (!this.cancelled)
                BottomAppBar.this.translateActionMenuView(actionMenuView, targetMode, targetAttached); 
            }
          });
      animatorSet = new AnimatorSet();
      animatorSet.setDuration(150L);
      animatorSet.playSequentially(new Animator[] { (Animator)objectAnimator1, (Animator)objectAnimator });
      paramList.add(animatorSet);
      return;
    } 
    if (animatorSet.getAlpha() < 1.0F)
      paramList.add(objectAnimator); 
  }
  
  private FloatingActionButton findDependentFab() {
    if (!(getParent() instanceof CoordinatorLayout))
      return null; 
    for (View view : ((CoordinatorLayout)getParent()).getDependents((View)this)) {
      if (view instanceof FloatingActionButton)
        return (FloatingActionButton)view; 
    } 
    return null;
  }
  
  private ActionMenuView getActionMenuView() {
    for (byte b = 0; b < getChildCount(); b++) {
      View view = getChildAt(b);
      if (view instanceof ActionMenuView)
        return (ActionMenuView)view; 
    } 
    return null;
  }
  
  private float getFabTranslationX() {
    return getFabTranslationX(this.fabAlignmentMode);
  }
  
  private int getFabTranslationX(int paramInt) {
    int i = ViewCompat.getLayoutDirection((View)this);
    int j = 0;
    boolean bool = true;
    if (i == 1) {
      i = 1;
    } else {
      i = 0;
    } 
    if (paramInt == 1) {
      j = getMeasuredWidth() / 2;
      int k = this.fabOffsetEndMode;
      paramInt = bool;
      if (i != 0)
        paramInt = -1; 
      j = (j - k) * paramInt;
    } 
    return j;
  }
  
  private float getFabTranslationY() {
    return getFabTranslationY(this.fabAttached);
  }
  
  private float getFabTranslationY(boolean paramBoolean) {
    FloatingActionButton floatingActionButton = findDependentFab();
    if (floatingActionButton == null)
      return 0.0F; 
    Rect rect = new Rect();
    floatingActionButton.getContentRect(rect);
    float f2 = rect.height();
    float f1 = f2;
    if (f2 == 0.0F)
      f1 = floatingActionButton.getMeasuredHeight(); 
    f2 = (floatingActionButton.getHeight() - rect.bottom);
    float f3 = (floatingActionButton.getHeight() - rect.height());
    f1 = -getCradleVerticalOffset() + f1 / 2.0F + f2;
    float f4 = floatingActionButton.getPaddingBottom();
    f2 = -getMeasuredHeight();
    if (!paramBoolean)
      f1 = f3 - f4; 
    return f2 + f1;
  }
  
  private boolean isAnimationRunning() {
    Animator animator = this.attachAnimator;
    if (animator == null || !animator.isRunning()) {
      animator = this.menuAnimator;
      if (animator == null || !animator.isRunning()) {
        animator = this.modeAnimator;
        return (animator != null && animator.isRunning());
      } 
    } 
    return true;
  }
  
  private boolean isVisibleFab() {
    boolean bool;
    FloatingActionButton floatingActionButton = findDependentFab();
    if (floatingActionButton != null && floatingActionButton.isOrWillBeShown()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void maybeAnimateAttachChange(boolean paramBoolean) {
    boolean bool;
    if (!ViewCompat.isLaidOut((View)this))
      return; 
    Animator animator = this.attachAnimator;
    if (animator != null)
      animator.cancel(); 
    ArrayList<Animator> arrayList = new ArrayList();
    if (paramBoolean && isVisibleFab()) {
      bool = true;
    } else {
      bool = false;
    } 
    createCradleShapeAnimation(bool, arrayList);
    createFabTranslationYAnimation(paramBoolean, arrayList);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(arrayList);
    this.attachAnimator = (Animator)animatorSet;
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BottomAppBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BottomAppBar.access$502(BottomAppBar.this, (Animator)null);
          }
        });
    this.attachAnimator.start();
  }
  
  private void maybeAnimateMenuView(int paramInt, boolean paramBoolean) {
    if (!ViewCompat.isLaidOut((View)this))
      return; 
    Animator animator = this.menuAnimator;
    if (animator != null)
      animator.cancel(); 
    ArrayList<Animator> arrayList = new ArrayList();
    if (!isVisibleFab()) {
      paramInt = 0;
      paramBoolean = false;
    } 
    createMenuViewTranslationAnimation(paramInt, paramBoolean, arrayList);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(arrayList);
    this.menuAnimator = (Animator)animatorSet;
    animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          final BottomAppBar this$0;
          
          public void onAnimationEnd(Animator param1Animator) {
            BottomAppBar.access$302(BottomAppBar.this, (Animator)null);
          }
        });
    this.menuAnimator.start();
  }
  
  private void maybeAnimateModeChange(int paramInt) {
    if (this.fabAlignmentMode != paramInt && ViewCompat.isLaidOut((View)this)) {
      Animator animator = this.modeAnimator;
      if (animator != null)
        animator.cancel(); 
      ArrayList<Animator> arrayList = new ArrayList();
      createCradleTranslationAnimation(paramInt, arrayList);
      createFabTranslationXAnimation(paramInt, arrayList);
      AnimatorSet animatorSet = new AnimatorSet();
      animatorSet.playTogether(arrayList);
      this.modeAnimator = (Animator)animatorSet;
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final BottomAppBar this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              BottomAppBar.access$002(BottomAppBar.this, (Animator)null);
            }
          });
      this.modeAnimator.start();
    } 
  }
  
  private void removeFabAnimationListeners(FloatingActionButton paramFloatingActionButton) {
    paramFloatingActionButton.removeOnHideAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
    paramFloatingActionButton.removeOnShowAnimationListener((Animator.AnimatorListener)this.fabAnimationListener);
  }
  
  private void setCutoutState() {
    float f;
    this.topEdgeTreatment.setHorizontalOffset(getFabTranslationX());
    FloatingActionButton floatingActionButton = findDependentFab();
    MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
    if (this.fabAttached && isVisibleFab()) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    materialShapeDrawable.setInterpolation(f);
    if (floatingActionButton != null) {
      floatingActionButton.setTranslationY(getFabTranslationY());
      floatingActionButton.setTranslationX(getFabTranslationX());
    } 
    ActionMenuView actionMenuView = getActionMenuView();
    if (actionMenuView != null) {
      actionMenuView.setAlpha(1.0F);
      if (!isVisibleFab()) {
        translateActionMenuView(actionMenuView, 0, false);
      } else {
        translateActionMenuView(actionMenuView, this.fabAlignmentMode, this.fabAttached);
      } 
    } 
  }
  
  private void translateActionMenuView(ActionMenuView paramActionMenuView, int paramInt, boolean paramBoolean) {
    float f;
    int j;
    if (ViewCompat.getLayoutDirection((View)this) == 1) {
      j = 1;
    } else {
      j = 0;
    } 
    byte b = 0;
    int i;
    for (i = 0; b < getChildCount(); i = k) {
      boolean bool;
      View view = getChildAt(b);
      if (view.getLayoutParams() instanceof Toolbar.LayoutParams && (((Toolbar.LayoutParams)view.getLayoutParams()).gravity & 0x800007) == 8388611) {
        bool = true;
      } else {
        bool = false;
      } 
      int k = i;
      if (bool) {
        if (j) {
          k = view.getLeft();
        } else {
          k = view.getRight();
        } 
        k = Math.max(i, k);
      } 
      b++;
    } 
    if (j) {
      j = paramActionMenuView.getRight();
    } else {
      j = paramActionMenuView.getLeft();
    } 
    if (paramInt == 1 && paramBoolean) {
      f = (i - j);
    } else {
      f = 0.0F;
    } 
    paramActionMenuView.setTranslationX(f);
  }
  
  public ColorStateList getBackgroundTint() {
    return this.materialShapeDrawable.getTintList();
  }
  
  public CoordinatorLayout.Behavior<BottomAppBar> getBehavior() {
    return (CoordinatorLayout.Behavior<BottomAppBar>)new Behavior();
  }
  
  public float getCradleVerticalOffset() {
    return this.topEdgeTreatment.getCradleVerticalOffset();
  }
  
  public int getFabAlignmentMode() {
    return this.fabAlignmentMode;
  }
  
  public float getFabCradleMargin() {
    return this.topEdgeTreatment.getFabCradleMargin();
  }
  
  public float getFabCradleRoundedCornerRadius() {
    return this.topEdgeTreatment.getFabCradleRoundedCornerRadius();
  }
  
  public boolean getHideOnScroll() {
    return this.hideOnScroll;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    cancelAnimations();
    setCutoutState();
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.fabAlignmentMode = savedState.fabAlignmentMode;
    this.fabAttached = savedState.fabAttached;
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.fabAlignmentMode = this.fabAlignmentMode;
    savedState.fabAttached = this.fabAttached;
    return (Parcelable)savedState;
  }
  
  public void replaceMenu(int paramInt) {
    getMenu().clear();
    inflateMenu(paramInt);
  }
  
  public void setBackgroundTint(ColorStateList paramColorStateList) {
    DrawableCompat.setTintList((Drawable)this.materialShapeDrawable, paramColorStateList);
  }
  
  public void setCradleVerticalOffset(float paramFloat) {
    if (paramFloat != getCradleVerticalOffset()) {
      this.topEdgeTreatment.setCradleVerticalOffset(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  public void setFabAlignmentMode(int paramInt) {
    maybeAnimateModeChange(paramInt);
    maybeAnimateMenuView(paramInt, this.fabAttached);
    this.fabAlignmentMode = paramInt;
  }
  
  public void setFabCradleMargin(float paramFloat) {
    if (paramFloat != getFabCradleMargin()) {
      this.topEdgeTreatment.setFabCradleMargin(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  public void setFabCradleRoundedCornerRadius(float paramFloat) {
    if (paramFloat != getFabCradleRoundedCornerRadius()) {
      this.topEdgeTreatment.setFabCradleRoundedCornerRadius(paramFloat);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  void setFabDiameter(int paramInt) {
    float f = paramInt;
    if (f != this.topEdgeTreatment.getFabDiameter()) {
      this.topEdgeTreatment.setFabDiameter(f);
      this.materialShapeDrawable.invalidateSelf();
    } 
  }
  
  public void setHideOnScroll(boolean paramBoolean) {
    this.hideOnScroll = paramBoolean;
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {}
  
  public void setTitle(CharSequence paramCharSequence) {}
  
  public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
    private final Rect fabContentRect = new Rect();
    
    public Behavior() {}
    
    public Behavior(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    private boolean updateFabPositionAndVisibility(FloatingActionButton param1FloatingActionButton, BottomAppBar param1BottomAppBar) {
      ((CoordinatorLayout.LayoutParams)param1FloatingActionButton.getLayoutParams()).anchorGravity = 17;
      param1BottomAppBar.addFabAnimationListeners(param1FloatingActionButton);
      return true;
    }
    
    public boolean onLayoutChild(CoordinatorLayout param1CoordinatorLayout, BottomAppBar param1BottomAppBar, int param1Int) {
      FloatingActionButton floatingActionButton = param1BottomAppBar.findDependentFab();
      if (floatingActionButton != null) {
        updateFabPositionAndVisibility(floatingActionButton, param1BottomAppBar);
        floatingActionButton.getMeasuredContentRect(this.fabContentRect);
        param1BottomAppBar.setFabDiameter(this.fabContentRect.height());
      } 
      if (!param1BottomAppBar.isAnimationRunning())
        param1BottomAppBar.setCutoutState(); 
      param1CoordinatorLayout.onLayoutChild((View)param1BottomAppBar, param1Int);
      return super.onLayoutChild(param1CoordinatorLayout, (View)param1BottomAppBar, param1Int);
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout param1CoordinatorLayout, BottomAppBar param1BottomAppBar, View param1View1, View param1View2, int param1Int1, int param1Int2) {
      boolean bool;
      if (param1BottomAppBar.getHideOnScroll() && super.onStartNestedScroll(param1CoordinatorLayout, (View)param1BottomAppBar, param1View1, param1View2, param1Int1, param1Int2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    protected void slideDown(BottomAppBar param1BottomAppBar) {
      super.slideDown((View)param1BottomAppBar);
      FloatingActionButton floatingActionButton = param1BottomAppBar.findDependentFab();
      if (floatingActionButton != null) {
        floatingActionButton.getContentRect(this.fabContentRect);
        float f = (floatingActionButton.getMeasuredHeight() - this.fabContentRect.height());
        floatingActionButton.clearAnimation();
        floatingActionButton.animate().translationY(-floatingActionButton.getPaddingBottom() + f).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setDuration(175L);
      } 
    }
    
    protected void slideUp(BottomAppBar param1BottomAppBar) {
      super.slideUp((View)param1BottomAppBar);
      FloatingActionButton floatingActionButton = param1BottomAppBar.findDependentFab();
      if (floatingActionButton != null) {
        floatingActionButton.clearAnimation();
        floatingActionButton.animate().translationY(param1BottomAppBar.getFabTranslationY()).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setDuration(225L);
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FabAlignmentMode {}
  
  static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public BottomAppBar.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomAppBar.SavedState(param2Parcel, null);
        }
        
        public BottomAppBar.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new BottomAppBar.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public BottomAppBar.SavedState[] newArray(int param2Int) {
          return new BottomAppBar.SavedState[param2Int];
        }
      };
    
    int fabAlignmentMode;
    
    boolean fabAttached;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      boolean bool;
      this.fabAlignmentMode = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.fabAttached = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.fabAlignmentMode);
      param1Parcel.writeInt(this.fabAttached);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public BottomAppBar.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomAppBar.SavedState(param1Parcel, null);
    }
    
    public BottomAppBar.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new BottomAppBar.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public BottomAppBar.SavedState[] newArray(int param1Int) {
      return new BottomAppBar.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomappbar\BottomAppBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */