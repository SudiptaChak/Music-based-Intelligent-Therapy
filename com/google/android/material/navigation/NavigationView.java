package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.ThemeEnforcement;

public class NavigationView extends ScrimInsetsFrameLayout {
  private static final int[] CHECKED_STATE_SET = new int[] { 16842912 };
  
  private static final int[] DISABLED_STATE_SET = new int[] { -16842910 };
  
  private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
  
  OnNavigationItemSelectedListener listener;
  
  private final int maxWidth;
  
  private final NavigationMenu menu;
  
  private MenuInflater menuInflater;
  
  private final NavigationMenuPresenter presenter;
  
  public NavigationView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.navigationViewStyle);
  }
  
  public NavigationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    ColorStateList colorStateList1;
    boolean bool;
    ColorStateList colorStateList2;
    this.presenter = new NavigationMenuPresenter();
    this.menu = new NavigationMenu(paramContext);
    TintTypedArray tintTypedArray = ThemeEnforcement.obtainTintedStyledAttributes(paramContext, paramAttributeSet, R.styleable.NavigationView, paramInt, R.style.Widget_Design_NavigationView, new int[0]);
    ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.NavigationView_android_background));
    if (tintTypedArray.hasValue(R.styleable.NavigationView_elevation))
      ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0)); 
    ViewCompat.setFitsSystemWindows((View)this, tintTypedArray.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
    this.maxWidth = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemIconTint)) {
      colorStateList2 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemIconTint);
    } else {
      colorStateList2 = createDefaultColorStateList(16842808);
    } 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
      paramInt = tintTypedArray.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
      bool = true;
    } else {
      paramInt = 0;
      bool = false;
    } 
    paramAttributeSet = null;
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextColor))
      colorStateList1 = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemTextColor); 
    ColorStateList colorStateList3 = colorStateList1;
    if (!bool) {
      colorStateList3 = colorStateList1;
      if (colorStateList1 == null)
        colorStateList3 = createDefaultColorStateList(16842806); 
    } 
    Drawable drawable = tintTypedArray.getDrawable(R.styleable.NavigationView_itemBackground);
    if (tintTypedArray.hasValue(R.styleable.NavigationView_itemHorizontalPadding)) {
      int j = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemHorizontalPadding, 0);
      this.presenter.setItemHorizontalPadding(j);
    } 
    int i = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_itemIconPadding, 0);
    this.menu.setCallback(new MenuBuilder.Callback() {
          final NavigationView this$0;
          
          public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
            boolean bool;
            if (NavigationView.this.listener != null && NavigationView.this.listener.onNavigationItemSelected(param1MenuItem)) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public void onMenuModeChange(MenuBuilder param1MenuBuilder) {}
        });
    this.presenter.setId(1);
    this.presenter.initForMenu(paramContext, (MenuBuilder)this.menu);
    this.presenter.setItemIconTintList(colorStateList2);
    if (bool)
      this.presenter.setItemTextAppearance(paramInt); 
    this.presenter.setItemTextColor(colorStateList3);
    this.presenter.setItemBackground(drawable);
    this.presenter.setItemIconPadding(i);
    this.menu.addMenuPresenter((MenuPresenter)this.presenter);
    addView((View)this.presenter.getMenuView((ViewGroup)this));
    if (tintTypedArray.hasValue(R.styleable.NavigationView_menu))
      inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationView_menu, 0)); 
    if (tintTypedArray.hasValue(R.styleable.NavigationView_headerLayout))
      inflateHeaderView(tintTypedArray.getResourceId(R.styleable.NavigationView_headerLayout, 0)); 
    tintTypedArray.recycle();
  }
  
  private ColorStateList createDefaultColorStateList(int paramInt) {
    TypedValue typedValue = new TypedValue();
    if (!getContext().getTheme().resolveAttribute(paramInt, typedValue, true))
      return null; 
    ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
    if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true))
      return null; 
    int i = typedValue.data;
    int j = colorStateList.getDefaultColor();
    int[] arrayOfInt2 = DISABLED_STATE_SET;
    int[] arrayOfInt3 = CHECKED_STATE_SET;
    int[] arrayOfInt1 = EMPTY_STATE_SET;
    paramInt = colorStateList.getColorForState(DISABLED_STATE_SET, j);
    return new ColorStateList(new int[][] { arrayOfInt2, arrayOfInt3, arrayOfInt1 }, new int[] { paramInt, i, j });
  }
  
  private MenuInflater getMenuInflater() {
    if (this.menuInflater == null)
      this.menuInflater = (MenuInflater)new SupportMenuInflater(getContext()); 
    return this.menuInflater;
  }
  
  public void addHeaderView(View paramView) {
    this.presenter.addHeaderView(paramView);
  }
  
  public MenuItem getCheckedItem() {
    return (MenuItem)this.presenter.getCheckedItem();
  }
  
  public int getHeaderCount() {
    return this.presenter.getHeaderCount();
  }
  
  public View getHeaderView(int paramInt) {
    return this.presenter.getHeaderView(paramInt);
  }
  
  public Drawable getItemBackground() {
    return this.presenter.getItemBackground();
  }
  
  public int getItemHorizontalPadding() {
    return this.presenter.getItemHorizontalPadding();
  }
  
  public int getItemIconPadding() {
    return this.presenter.getItemIconPadding();
  }
  
  public ColorStateList getItemIconTintList() {
    return this.presenter.getItemTintList();
  }
  
  public ColorStateList getItemTextColor() {
    return this.presenter.getItemTextColor();
  }
  
  public Menu getMenu() {
    return (Menu)this.menu;
  }
  
  public View inflateHeaderView(int paramInt) {
    return this.presenter.inflateHeaderView(paramInt);
  }
  
  public void inflateMenu(int paramInt) {
    this.presenter.setUpdateSuspended(true);
    getMenuInflater().inflate(paramInt, (Menu)this.menu);
    this.presenter.setUpdateSuspended(false);
    this.presenter.updateMenuView(false);
  }
  
  protected void onInsetsChanged(WindowInsetsCompat paramWindowInsetsCompat) {
    this.presenter.dispatchApplyWindowInsets(paramWindowInsetsCompat);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getMode(paramInt1);
    if (i != Integer.MIN_VALUE) {
      if (i == 0)
        paramInt1 = View.MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824); 
    } else {
      paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(paramInt1), this.maxWidth), 1073741824);
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    this.menu.restorePresenterStates(savedState.menuState);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    savedState.menuState = new Bundle();
    this.menu.savePresenterStates(savedState.menuState);
    return (Parcelable)savedState;
  }
  
  public void removeHeaderView(View paramView) {
    this.presenter.removeHeaderView(paramView);
  }
  
  public void setCheckedItem(int paramInt) {
    MenuItem menuItem = this.menu.findItem(paramInt);
    if (menuItem != null)
      this.presenter.setCheckedItem((MenuItemImpl)menuItem); 
  }
  
  public void setCheckedItem(MenuItem paramMenuItem) {
    paramMenuItem = this.menu.findItem(paramMenuItem.getItemId());
    if (paramMenuItem != null) {
      this.presenter.setCheckedItem((MenuItemImpl)paramMenuItem);
      return;
    } 
    throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
  }
  
  public void setItemBackground(Drawable paramDrawable) {
    this.presenter.setItemBackground(paramDrawable);
  }
  
  public void setItemBackgroundResource(int paramInt) {
    setItemBackground(ContextCompat.getDrawable(getContext(), paramInt));
  }
  
  public void setItemHorizontalPadding(int paramInt) {
    this.presenter.setItemHorizontalPadding(paramInt);
  }
  
  public void setItemHorizontalPaddingResource(int paramInt) {
    this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconPadding(int paramInt) {
    this.presenter.setItemIconPadding(paramInt);
  }
  
  public void setItemIconPaddingResource(int paramInt) {
    this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(paramInt));
  }
  
  public void setItemIconTintList(ColorStateList paramColorStateList) {
    this.presenter.setItemIconTintList(paramColorStateList);
  }
  
  public void setItemTextAppearance(int paramInt) {
    this.presenter.setItemTextAppearance(paramInt);
  }
  
  public void setItemTextColor(ColorStateList paramColorStateList) {
    this.presenter.setItemTextColor(paramColorStateList);
  }
  
  public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener paramOnNavigationItemSelectedListener) {
    this.listener = paramOnNavigationItemSelectedListener;
  }
  
  public static interface OnNavigationItemSelectedListener {
    boolean onNavigationItemSelected(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public NavigationView.SavedState createFromParcel(Parcel param2Parcel) {
          return new NavigationView.SavedState(param2Parcel, null);
        }
        
        public NavigationView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new NavigationView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public NavigationView.SavedState[] newArray(int param2Int) {
          return new NavigationView.SavedState[param2Int];
        }
      };
    
    public Bundle menuState;
    
    public SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      this.menuState = param1Parcel.readBundle(param1ClassLoader);
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeBundle(this.menuState);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public NavigationView.SavedState createFromParcel(Parcel param1Parcel) {
      return new NavigationView.SavedState(param1Parcel, null);
    }
    
    public NavigationView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new NavigationView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public NavigationView.SavedState[] newArray(int param1Int) {
      return new NavigationView.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\navigation\NavigationView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */