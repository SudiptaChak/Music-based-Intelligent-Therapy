package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;

public class BottomNavigationPresenter implements MenuPresenter {
  private int id;
  
  private MenuBuilder menu;
  
  private BottomNavigationMenuView menuView;
  
  private boolean updateSuspended = false;
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public int getId() {
    return this.id;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    return this.menuView;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    this.menu = paramMenuBuilder;
    this.menuView.initialize(paramMenuBuilder);
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (paramParcelable instanceof SavedState)
      this.menuView.tryRestoreSelectedItemId(((SavedState)paramParcelable).selectedItemId); 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.selectedItemId = this.menuView.getSelectedItemId();
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    return false;
  }
  
  public void setBottomNavigationMenuView(BottomNavigationMenuView paramBottomNavigationMenuView) {
    this.menuView = paramBottomNavigationMenuView;
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {}
  
  public void setId(int paramInt) {
    this.id = paramInt;
  }
  
  public void setUpdateSuspended(boolean paramBoolean) {
    this.updateSuspended = paramBoolean;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    if (this.updateSuspended)
      return; 
    if (paramBoolean) {
      this.menuView.buildMenuView();
    } else {
      this.menuView.updateMenuView();
    } 
  }
  
  static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public BottomNavigationPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new BottomNavigationPresenter.SavedState(param2Parcel);
        }
        
        public BottomNavigationPresenter.SavedState[] newArray(int param2Int) {
          return new BottomNavigationPresenter.SavedState[param2Int];
        }
      };
    
    int selectedItemId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.selectedItemId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.selectedItemId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public BottomNavigationPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new BottomNavigationPresenter.SavedState(param1Parcel);
    }
    
    public BottomNavigationPresenter.SavedState[] newArray(int param1Int) {
      return new BottomNavigationPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\BottomNavigationPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */