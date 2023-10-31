package com.google.android.material.bottomnavigation;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import com.google.android.material.R;
import com.google.android.material.internal.TextScale;

public class BottomNavigationMenuView extends ViewGroup implements MenuView {
  private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
  
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private final int activeItemMaxWidth;
  
  private final int activeItemMinWidth;
  
  private BottomNavigationItemView[] buttons;
  
  private final int inactiveItemMaxWidth;
  
  private final int inactiveItemMinWidth;
  
  private Drawable itemBackground;
  
  private int itemBackgroundRes;
  
  private final int itemHeight;
  
  private boolean itemHorizontalTranslationEnabled;
  
  private int itemIconSize;
  
  private ColorStateList itemIconTint;
  
  private final Pools.Pool<BottomNavigationItemView> itemPool = (Pools.Pool<BottomNavigationItemView>)new Pools.SynchronizedPool(5);
  
  private int itemTextAppearanceActive;
  
  private int itemTextAppearanceInactive;
  
  private final ColorStateList itemTextColorDefault;
  
  private ColorStateList itemTextColorFromUser;
  
  private int labelVisibilityMode;
  
  private MenuBuilder menu;
  
  private final View.OnClickListener onClickListener;
  
  private BottomNavigationPresenter presenter;
  
  private int selectedItemId = 0;
  
  private int selectedItemPosition = 0;
  
  private final TransitionSet set;
  
  private int[] tempChildWidths;
  
  public BottomNavigationMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public BottomNavigationMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Resources resources = getResources();
    this.inactiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
    this.inactiveItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
    this.activeItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
    this.activeItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
    this.itemHeight = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_height);
    this.itemTextColorDefault = createDefaultColorStateList(16842808);
    AutoTransition autoTransition = new AutoTransition();
    this.set = (TransitionSet)autoTransition;
    autoTransition.setOrdering(0);
    this.set.setDuration(115L);
    this.set.setInterpolator((TimeInterpolator)new FastOutSlowInInterpolator());
    this.set.addTransition((Transition)new TextScale());
    this.onClickListener = new View.OnClickListener() {
        final BottomNavigationMenuView this$0;
        
        public void onClick(View param1View) {
          MenuItemImpl menuItemImpl = ((BottomNavigationItemView)param1View).getItemData();
          if (!BottomNavigationMenuView.this.menu.performItemAction((MenuItem)menuItemImpl, BottomNavigationMenuView.this.presenter, 0))
            menuItemImpl.setChecked(true); 
        }
      };
    this.tempChildWidths = new int[5];
  }
  
  private BottomNavigationItemView getNewItem() {
    BottomNavigationItemView bottomNavigationItemView2 = (BottomNavigationItemView)this.itemPool.acquire();
    BottomNavigationItemView bottomNavigationItemView1 = bottomNavigationItemView2;
    if (bottomNavigationItemView2 == null)
      bottomNavigationItemView1 = new BottomNavigationItemView(getContext()); 
    return bottomNavigationItemView1;
  }
  
  private boolean isShifting(int paramInt1, int paramInt2) {
    boolean bool = true;
    if ((paramInt1 == -1) ? (paramInt2 > 3) : (paramInt1 == 0))
      bool = false; 
    return bool;
  }
  
  public void buildMenuView() {
    removeAllViews();
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int j = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < j; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        if (bottomNavigationItemView != null)
          this.itemPool.release(bottomNavigationItemView); 
      } 
    } 
    if (this.menu.size() == 0) {
      this.selectedItemId = 0;
      this.selectedItemPosition = 0;
      this.buttons = null;
      return;
    } 
    this.buttons = new BottomNavigationItemView[this.menu.size()];
    boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
    int i;
    for (i = 0; i < this.menu.size(); i++) {
      this.presenter.setUpdateSuspended(true);
      this.menu.getItem(i).setCheckable(true);
      this.presenter.setUpdateSuspended(false);
      BottomNavigationItemView bottomNavigationItemView = getNewItem();
      this.buttons[i] = bottomNavigationItemView;
      bottomNavigationItemView.setIconTintList(this.itemIconTint);
      bottomNavigationItemView.setIconSize(this.itemIconSize);
      bottomNavigationItemView.setTextColor(this.itemTextColorDefault);
      bottomNavigationItemView.setTextAppearanceInactive(this.itemTextAppearanceInactive);
      bottomNavigationItemView.setTextAppearanceActive(this.itemTextAppearanceActive);
      bottomNavigationItemView.setTextColor(this.itemTextColorFromUser);
      Drawable drawable = this.itemBackground;
      if (drawable != null) {
        bottomNavigationItemView.setItemBackground(drawable);
      } else {
        bottomNavigationItemView.setItemBackground(this.itemBackgroundRes);
      } 
      bottomNavigationItemView.setShifting(bool);
      bottomNavigationItemView.setLabelVisibilityMode(this.labelVisibilityMode);
      bottomNavigationItemView.initialize((MenuItemImpl)this.menu.getItem(i), 0);
      bottomNavigationItemView.setItemPosition(i);
      bottomNavigationItemView.setOnClickListener(this.onClickListener);
      addView((View)bottomNavigationItemView);
    } 
    i = Math.min(this.menu.size() - 1, this.selectedItemPosition);
    this.selectedItemPosition = i;
    this.menu.getItem(i).setChecked(true);
  }
  
  public ColorStateList createDefaultColorStateList(int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (!getContext().getTheme().resolveAttribute(paramInt, typedValue, true))
      return null; 
    ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
    if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true))
      return null; 
    paramInt = typedValue.data;
    int j = colorStateList.getDefaultColor();
    int[] arrayOfInt1 = DISABLED_STATE_SET;
    int[] arrayOfInt3 = CHECKED_STATE_SET;
    int[] arrayOfInt2 = EMPTY_STATE_SET;
    int i = colorStateList.getColorForState(DISABLED_STATE_SET, j);
    return new ColorStateList(new int[][] { arrayOfInt1, arrayOfInt3, arrayOfInt2 }, new int[] { i, paramInt, j });
  }
  
  public ColorStateList getIconTintList() {
    return this.itemIconTint;
  }
  
  public Drawable getItemBackground() {
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    return (arrayOfBottomNavigationItemView != null && arrayOfBottomNavigationItemView.length > 0) ? arrayOfBottomNavigationItemView[0].getBackground() : this.itemBackground;
  }
  
  @Deprecated
  public int getItemBackgroundRes() {
    return this.itemBackgroundRes;
  }
  
  public int getItemIconSize() {
    return this.itemIconSize;
  }
  
  public int getItemTextAppearanceActive() {
    return this.itemTextAppearanceActive;
  }
  
  public int getItemTextAppearanceInactive() {
    return this.itemTextAppearanceInactive;
  }
  
  public ColorStateList getItemTextColor() {
    return this.itemTextColorFromUser;
  }
  
  public int getLabelVisibilityMode() {
    return this.labelVisibilityMode;
  }
  
  public int getSelectedItemId() {
    return this.selectedItemId;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.menu = paramMenuBuilder;
  }
  
  public boolean isItemHorizontalTranslationEnabled() {
    return this.itemHorizontalTranslationEnabled;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getChildCount();
    int j = paramInt4 - paramInt2;
    paramInt2 = 0;
    paramInt4 = 0;
    while (paramInt2 < i) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
          int k = paramInt3 - paramInt1 - paramInt4;
          view.layout(k - view.getMeasuredWidth(), 0, k, j);
        } else {
          view.layout(paramInt4, 0, view.getMeasuredWidth() + paramInt4, j);
        } 
        paramInt4 += view.getMeasuredWidth();
      } 
      paramInt2++;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.getSize(paramInt1);
    int i = this.menu.getVisibleItems().size();
    int k = getChildCount();
    int m = View.MeasureSpec.makeMeasureSpec(this.itemHeight, 1073741824);
    if (isShifting(this.labelVisibilityMode, i) && this.itemHorizontalTranslationEnabled) {
      View view = getChildAt(this.selectedItemPosition);
      paramInt2 = this.activeItemMinWidth;
      paramInt1 = paramInt2;
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.activeItemMaxWidth, -2147483648), m);
        paramInt1 = Math.max(paramInt2, view.getMeasuredWidth());
      } 
      if (view.getVisibility() != 8) {
        paramInt2 = 1;
      } else {
        paramInt2 = 0;
      } 
      paramInt2 = i - paramInt2;
      int n = Math.min(j - this.inactiveItemMinWidth * paramInt2, Math.min(paramInt1, this.activeItemMaxWidth));
      i = j - n;
      if (paramInt2 == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = paramInt2;
      } 
      j = Math.min(i / paramInt1, this.inactiveItemMaxWidth);
      paramInt2 = i - paramInt2 * j;
      paramInt1 = 0;
      while (paramInt1 < k) {
        if (getChildAt(paramInt1).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          if (paramInt1 == this.selectedItemPosition) {
            i = n;
          } else {
            i = j;
          } 
          arrayOfInt[paramInt1] = i;
          i = paramInt2;
          if (paramInt2 > 0) {
            arrayOfInt = this.tempChildWidths;
            arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
            i = paramInt2 - 1;
          } 
        } else {
          this.tempChildWidths[paramInt1] = 0;
          i = paramInt2;
        } 
        paramInt1++;
        paramInt2 = i;
      } 
    } else {
      if (i == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = i;
      } 
      int n = Math.min(j / paramInt1, this.activeItemMaxWidth);
      i = j - i * n;
      paramInt1 = 0;
      while (paramInt1 < k) {
        if (getChildAt(paramInt1).getVisibility() != 8) {
          int[] arrayOfInt = this.tempChildWidths;
          arrayOfInt[paramInt1] = n;
          paramInt2 = i;
          if (i > 0) {
            arrayOfInt[paramInt1] = arrayOfInt[paramInt1] + 1;
            paramInt2 = i - 1;
          } 
        } else {
          this.tempChildWidths[paramInt1] = 0;
          paramInt2 = i;
        } 
        paramInt1++;
        i = paramInt2;
      } 
    } 
    paramInt1 = 0;
    paramInt2 = 0;
    while (paramInt1 < k) {
      View view = getChildAt(paramInt1);
      if (view.getVisibility() != 8) {
        view.measure(View.MeasureSpec.makeMeasureSpec(this.tempChildWidths[paramInt1], 1073741824), m);
        (view.getLayoutParams()).width = view.getMeasuredWidth();
        paramInt2 += view.getMeasuredWidth();
      } 
      paramInt1++;
    } 
    setMeasuredDimension(View.resolveSizeAndState(paramInt2, View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824), 0), View.resolveSizeAndState(this.itemHeight, m, 0));
  }
  
  public void setIconTintList(ColorStateList paramColorStateList) {
    this.itemIconTint = paramColorStateList;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setIconTintList(paramColorStateList); 
    } 
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.itemBackground = paramDrawable;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setItemBackground(paramDrawable); 
    } 
  }
  
  public void setItemBackgroundRes(int paramInt) {
    this.itemBackgroundRes = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setItemBackground(paramInt); 
    } 
  }
  
  public void setItemHorizontalTranslationEnabled(boolean paramBoolean) {
    this.itemHorizontalTranslationEnabled = paramBoolean;
  }
  
  public void setItemIconSize(int paramInt) {
    this.itemIconSize = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setIconSize(paramInt); 
    } 
  }
  
  public void setItemTextAppearanceActive(int paramInt) {
    this.itemTextAppearanceActive = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        bottomNavigationItemView.setTextAppearanceActive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          bottomNavigationItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextAppearanceInactive(int paramInt) {
    this.itemTextAppearanceInactive = paramInt;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++) {
        BottomNavigationItemView bottomNavigationItemView = arrayOfBottomNavigationItemView[b];
        bottomNavigationItemView.setTextAppearanceInactive(paramInt);
        ColorStateList colorStateList = this.itemTextColorFromUser;
        if (colorStateList != null)
          bottomNavigationItemView.setTextColor(colorStateList); 
      } 
    } 
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.itemTextColorFromUser = paramColorStateList;
    BottomNavigationItemView[] arrayOfBottomNavigationItemView = this.buttons;
    if (arrayOfBottomNavigationItemView != null) {
      int i = arrayOfBottomNavigationItemView.length;
      for (byte b = 0; b < i; b++)
        arrayOfBottomNavigationItemView[b].setTextColor(paramColorStateList); 
    } 
  }
  
  public void setLabelVisibilityMode(int paramInt) {
    this.labelVisibilityMode = paramInt;
  }
  
  public void setPresenter(BottomNavigationPresenter paramBottomNavigationPresenter) {
    this.presenter = paramBottomNavigationPresenter;
  }
  
  void tryRestoreSelectedItemId(int paramInt) {
    int i = this.menu.size();
    for (byte b = 0; b < i; b++) {
      MenuItem menuItem = this.menu.getItem(b);
      if (paramInt == menuItem.getItemId()) {
        this.selectedItemId = paramInt;
        this.selectedItemPosition = b;
        menuItem.setChecked(true);
        break;
      } 
    } 
  }
  
  public void updateMenuView() {
    MenuBuilder menuBuilder = this.menu;
    if (menuBuilder != null && this.buttons != null) {
      int i = menuBuilder.size();
      if (i != this.buttons.length) {
        buildMenuView();
        return;
      } 
      int j = this.selectedItemId;
      byte b;
      for (b = 0; b < i; b++) {
        MenuItem menuItem = this.menu.getItem(b);
        if (menuItem.isChecked()) {
          this.selectedItemId = menuItem.getItemId();
          this.selectedItemPosition = b;
        } 
      } 
      if (j != this.selectedItemId)
        TransitionManager.beginDelayedTransition(this, (Transition)this.set); 
      boolean bool = isShifting(this.labelVisibilityMode, this.menu.getVisibleItems().size());
      for (b = 0; b < i; b++) {
        this.presenter.setUpdateSuspended(true);
        this.buttons[b].setLabelVisibilityMode(this.labelVisibilityMode);
        this.buttons[b].setShifting(bool);
        this.buttons[b].initialize((MenuItemImpl)this.menu.getItem(b), 0);
        this.presenter.setUpdateSuspended(false);
      } 
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\BottomNavigationMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */