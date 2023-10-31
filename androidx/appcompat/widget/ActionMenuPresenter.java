package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = viewGroup.getChildAt(b);
      if (view instanceof MenuView.ItemView && ((MenuView.ItemView)view).getItemData() == paramMenuItem)
        return view; 
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMenu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   4: astore #17
    //   6: iconst_0
    //   7: istore #11
    //   9: aload #17
    //   11: ifnull -> 33
    //   14: aload_0
    //   15: getfield mMenu : Landroidx/appcompat/view/menu/MenuBuilder;
    //   18: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   21: astore #17
    //   23: aload #17
    //   25: invokevirtual size : ()I
    //   28: istore #4
    //   30: goto -> 39
    //   33: aconst_null
    //   34: astore #17
    //   36: iconst_0
    //   37: istore #4
    //   39: aload_0
    //   40: getfield mMaxItems : I
    //   43: istore #7
    //   45: aload_0
    //   46: getfield mActionItemWidthLimit : I
    //   49: istore #10
    //   51: iconst_0
    //   52: iconst_0
    //   53: invokestatic makeMeasureSpec : (II)I
    //   56: istore #12
    //   58: aload_0
    //   59: getfield mMenuView : Landroidx/appcompat/view/menu/MenuView;
    //   62: checkcast android/view/ViewGroup
    //   65: astore #18
    //   67: iconst_0
    //   68: istore #5
    //   70: iconst_0
    //   71: istore #6
    //   73: iload #6
    //   75: istore_1
    //   76: iload_1
    //   77: istore_2
    //   78: iload_1
    //   79: istore_3
    //   80: iload #7
    //   82: istore_1
    //   83: iload #5
    //   85: iload #4
    //   87: if_icmpge -> 166
    //   90: aload #17
    //   92: iload #5
    //   94: invokevirtual get : (I)Ljava/lang/Object;
    //   97: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   100: astore #19
    //   102: aload #19
    //   104: invokevirtual requiresActionButton : ()Z
    //   107: ifeq -> 116
    //   110: iinc #3, 1
    //   113: goto -> 133
    //   116: aload #19
    //   118: invokevirtual requestsActionButton : ()Z
    //   121: ifeq -> 130
    //   124: iinc #2, 1
    //   127: goto -> 133
    //   130: iconst_1
    //   131: istore #6
    //   133: iload_1
    //   134: istore #7
    //   136: aload_0
    //   137: getfield mExpandedActionViewsExclusive : Z
    //   140: ifeq -> 157
    //   143: iload_1
    //   144: istore #7
    //   146: aload #19
    //   148: invokevirtual isActionViewExpanded : ()Z
    //   151: ifeq -> 157
    //   154: iconst_0
    //   155: istore #7
    //   157: iinc #5, 1
    //   160: iload #7
    //   162: istore_1
    //   163: goto -> 83
    //   166: iload_1
    //   167: istore #5
    //   169: aload_0
    //   170: getfield mReserveOverflow : Z
    //   173: ifeq -> 196
    //   176: iload #6
    //   178: ifne -> 191
    //   181: iload_1
    //   182: istore #5
    //   184: iload_2
    //   185: iload_3
    //   186: iadd
    //   187: iload_1
    //   188: if_icmple -> 196
    //   191: iload_1
    //   192: iconst_1
    //   193: isub
    //   194: istore #5
    //   196: iload #5
    //   198: iload_3
    //   199: isub
    //   200: istore_3
    //   201: aload_0
    //   202: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   205: astore #19
    //   207: aload #19
    //   209: invokevirtual clear : ()V
    //   212: aload_0
    //   213: getfield mStrictWidthLimit : Z
    //   216: ifeq -> 242
    //   219: aload_0
    //   220: getfield mMinCellSize : I
    //   223: istore_1
    //   224: iload #10
    //   226: iload_1
    //   227: idiv
    //   228: istore_2
    //   229: iload_1
    //   230: iload #10
    //   232: iload_1
    //   233: irem
    //   234: iload_2
    //   235: idiv
    //   236: iadd
    //   237: istore #8
    //   239: goto -> 247
    //   242: iconst_0
    //   243: istore #8
    //   245: iconst_0
    //   246: istore_2
    //   247: iconst_0
    //   248: istore #9
    //   250: iconst_0
    //   251: istore_1
    //   252: iload #10
    //   254: istore #6
    //   256: iload #4
    //   258: istore #10
    //   260: iload #11
    //   262: istore #4
    //   264: iload #9
    //   266: iload #10
    //   268: if_icmpge -> 758
    //   271: aload #17
    //   273: iload #9
    //   275: invokevirtual get : (I)Ljava/lang/Object;
    //   278: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   281: astore #20
    //   283: aload #20
    //   285: invokevirtual requiresActionButton : ()Z
    //   288: ifeq -> 390
    //   291: aload_0
    //   292: aload #20
    //   294: aconst_null
    //   295: aload #18
    //   297: invokevirtual getItemView : (Landroidx/appcompat/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   300: astore #21
    //   302: aload_0
    //   303: getfield mStrictWidthLimit : Z
    //   306: ifeq -> 327
    //   309: iload_2
    //   310: aload #21
    //   312: iload #8
    //   314: iload_2
    //   315: iload #12
    //   317: iload #4
    //   319: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   322: isub
    //   323: istore_2
    //   324: goto -> 336
    //   327: aload #21
    //   329: iload #12
    //   331: iload #12
    //   333: invokevirtual measure : (II)V
    //   336: aload #21
    //   338: invokevirtual getMeasuredWidth : ()I
    //   341: istore #7
    //   343: iload #6
    //   345: iload #7
    //   347: isub
    //   348: istore #6
    //   350: iload_1
    //   351: istore #5
    //   353: iload_1
    //   354: ifne -> 361
    //   357: iload #7
    //   359: istore #5
    //   361: aload #20
    //   363: invokevirtual getGroupId : ()I
    //   366: istore_1
    //   367: iload_1
    //   368: ifeq -> 378
    //   371: aload #19
    //   373: iload_1
    //   374: iconst_1
    //   375: invokevirtual put : (IZ)V
    //   378: aload #20
    //   380: iconst_1
    //   381: invokevirtual setIsActionButton : (Z)V
    //   384: iload #4
    //   386: istore_1
    //   387: goto -> 746
    //   390: aload #20
    //   392: invokevirtual requestsActionButton : ()Z
    //   395: ifeq -> 733
    //   398: aload #20
    //   400: invokevirtual getGroupId : ()I
    //   403: istore #11
    //   405: aload #19
    //   407: iload #11
    //   409: invokevirtual get : (I)Z
    //   412: istore #16
    //   414: iload_3
    //   415: ifgt -> 423
    //   418: iload #16
    //   420: ifeq -> 445
    //   423: iload #6
    //   425: ifle -> 445
    //   428: aload_0
    //   429: getfield mStrictWidthLimit : Z
    //   432: ifeq -> 439
    //   435: iload_2
    //   436: ifle -> 445
    //   439: iconst_1
    //   440: istore #13
    //   442: goto -> 448
    //   445: iconst_0
    //   446: istore #13
    //   448: iload #13
    //   450: istore #15
    //   452: iload #13
    //   454: istore #14
    //   456: iload #6
    //   458: istore #7
    //   460: iload_2
    //   461: istore #5
    //   463: iload_1
    //   464: istore #4
    //   466: iload #13
    //   468: ifeq -> 598
    //   471: aload_0
    //   472: aload #20
    //   474: aconst_null
    //   475: aload #18
    //   477: invokevirtual getItemView : (Landroidx/appcompat/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   480: astore #21
    //   482: aload_0
    //   483: getfield mStrictWidthLimit : Z
    //   486: ifeq -> 525
    //   489: aload #21
    //   491: iload #8
    //   493: iload_2
    //   494: iload #12
    //   496: iconst_0
    //   497: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   500: istore #5
    //   502: iload_2
    //   503: iload #5
    //   505: isub
    //   506: istore #4
    //   508: iload #4
    //   510: istore_2
    //   511: iload #5
    //   513: ifne -> 534
    //   516: iconst_0
    //   517: istore #15
    //   519: iload #4
    //   521: istore_2
    //   522: goto -> 534
    //   525: aload #21
    //   527: iload #12
    //   529: iload #12
    //   531: invokevirtual measure : (II)V
    //   534: aload #21
    //   536: invokevirtual getMeasuredWidth : ()I
    //   539: istore #5
    //   541: iload #6
    //   543: iload #5
    //   545: isub
    //   546: istore #7
    //   548: iload_1
    //   549: istore #4
    //   551: iload_1
    //   552: ifne -> 559
    //   555: iload #5
    //   557: istore #4
    //   559: aload_0
    //   560: getfield mStrictWidthLimit : Z
    //   563: ifeq -> 574
    //   566: iload #7
    //   568: iflt -> 587
    //   571: goto -> 582
    //   574: iload #7
    //   576: iload #4
    //   578: iadd
    //   579: ifle -> 587
    //   582: iconst_1
    //   583: istore_1
    //   584: goto -> 589
    //   587: iconst_0
    //   588: istore_1
    //   589: iload #15
    //   591: iload_1
    //   592: iand
    //   593: istore #14
    //   595: iload_2
    //   596: istore #5
    //   598: iload #14
    //   600: ifeq -> 621
    //   603: iload #11
    //   605: ifeq -> 621
    //   608: aload #19
    //   610: iload #11
    //   612: iconst_1
    //   613: invokevirtual put : (IZ)V
    //   616: iload_3
    //   617: istore_1
    //   618: goto -> 697
    //   621: iload_3
    //   622: istore_1
    //   623: iload #16
    //   625: ifeq -> 697
    //   628: aload #19
    //   630: iload #11
    //   632: iconst_0
    //   633: invokevirtual put : (IZ)V
    //   636: iconst_0
    //   637: istore_2
    //   638: iload_3
    //   639: istore_1
    //   640: iload_2
    //   641: iload #9
    //   643: if_icmpge -> 697
    //   646: aload #17
    //   648: iload_2
    //   649: invokevirtual get : (I)Ljava/lang/Object;
    //   652: checkcast androidx/appcompat/view/menu/MenuItemImpl
    //   655: astore #21
    //   657: iload_3
    //   658: istore_1
    //   659: aload #21
    //   661: invokevirtual getGroupId : ()I
    //   664: iload #11
    //   666: if_icmpne -> 689
    //   669: iload_3
    //   670: istore_1
    //   671: aload #21
    //   673: invokevirtual isActionButton : ()Z
    //   676: ifeq -> 683
    //   679: iload_3
    //   680: iconst_1
    //   681: iadd
    //   682: istore_1
    //   683: aload #21
    //   685: iconst_0
    //   686: invokevirtual setIsActionButton : (Z)V
    //   689: iinc #2, 1
    //   692: iload_1
    //   693: istore_3
    //   694: goto -> 638
    //   697: iload_1
    //   698: istore_2
    //   699: iload #14
    //   701: ifeq -> 708
    //   704: iload_1
    //   705: iconst_1
    //   706: isub
    //   707: istore_2
    //   708: aload #20
    //   710: iload #14
    //   712: invokevirtual setIsActionButton : (Z)V
    //   715: iconst_0
    //   716: istore_1
    //   717: iload_2
    //   718: istore_3
    //   719: iload #7
    //   721: istore #6
    //   723: iload #5
    //   725: istore_2
    //   726: iload #4
    //   728: istore #5
    //   730: goto -> 746
    //   733: aload #20
    //   735: iload #4
    //   737: invokevirtual setIsActionButton : (Z)V
    //   740: iload_1
    //   741: istore #5
    //   743: iload #4
    //   745: istore_1
    //   746: iinc #9, 1
    //   749: iload_1
    //   750: istore #4
    //   752: iload #5
    //   754: istore_1
    //   755: goto -> 264
    //   758: iconst_1
    //   759: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    return (overflowMenuButton != null) ? overflowMenuButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
    if (actionButtonSubmenu != null) {
      actionButtonSubmenu.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        OverflowMenuButton overflowMenuButton = new OverflowMenuButton(this.mSystemContext);
        this.mOverflowButton = overflowMenuButton;
        if (this.mPendingOverflowIconSet) {
          overflowMenuButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)((resources.getDisplayMetrics()).density * 56.0F);
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null && overflowPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState))
      return; 
    paramParcelable = paramParcelable;
    if (((SavedState)paramParcelable).openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool = paramSubMenuBuilder.hasVisibleItems();
    boolean bool1 = false;
    if (!bool)
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view = findViewForItem(subMenuBuilder.getItem());
    if (view == null)
      return false; 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    int i = paramSubMenuBuilder.size();
    byte b = 0;
    while (true) {
      bool = bool1;
      if (b < i) {
        MenuItem menuItem = paramSubMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null) {
          bool = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    ActionButtonSubmenu actionButtonSubmenu = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
    this.mActionButtonPopup = actionButtonSubmenu;
    actionButtonSubmenu.setForceShowIcon(bool);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
    } else if (this.mMenu != null) {
      this.mMenu.close(false);
    } 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    if (overflowMenuButton != null) {
      overflowMenuButton.setImageDrawable(paramDrawable);
    } else {
      this.mPendingOverflowIconSet = true;
      this.mPendingOverflowIcon = paramDrawable;
    } 
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    MenuBuilder<MenuItemImpl> menuBuilder = this.mMenu;
    byte b = 0;
    if (menuBuilder != null) {
      ArrayList<MenuItemImpl> arrayList = this.mMenu.getActionItems();
      int j = arrayList.size();
      for (byte b1 = 0; b1 < j; b1++) {
        ActionProvider actionProvider = ((MenuItemImpl)arrayList.get(b1)).getSupportActionProvider();
        if (actionProvider != null)
          actionProvider.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      ArrayList arrayList = this.mMenu.getNonActionItems();
    } else {
      menuBuilder = null;
    } 
    int i = b;
    if (this.mReserveOverflow) {
      i = b;
      if (menuBuilder != null) {
        int j = menuBuilder.size();
        if (j == 1) {
          i = ((MenuItemImpl)menuBuilder.get(0)).isActionViewExpanded() ^ true;
        } else {
          i = b;
          if (j > 0)
            i = 1; 
        } 
      } 
    } 
    if (i != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      ViewGroup viewGroup = (ViewGroup)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else {
      OverflowMenuButton overflowMenuButton = this.mOverflowButton;
      if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView)
        ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton); 
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    final ActionMenuPresenter this$0;
    
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter.this.mActionButtonPopup = null;
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    final ActionMenuPresenter this$0;
    
    public ShowableListMenu getPopup() {
      ShowableListMenu showableListMenu;
      if (ActionMenuPresenter.this.mActionButtonPopup != null) {
        showableListMenu = (ShowableListMenu)ActionMenuPresenter.this.mActionButtonPopup.getPopup();
      } else {
        showableListMenu = null;
      } 
      return showableListMenu;
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    final ActionMenuPresenter this$0;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    final ActionMenuPresenter this$0;
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      TooltipCompat.setTooltipText((View)this, getContentDescription());
      setOnTouchListener(new ForwardingListener((View)this) {
            final ActionMenuPresenter.OverflowMenuButton this$1;
            
            final ActionMenuPresenter val$this$0;
            
            public ShowableListMenu getPopup() {
              return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int k = getWidth();
        param1Int2 = getHeight();
        param1Int1 = Math.max(k, param1Int2) / 2;
        int j = getPaddingLeft();
        int i = getPaddingRight();
        param1Int4 = getPaddingTop();
        param1Int3 = getPaddingBottom();
        i = (k + j - i) / 2;
        param1Int2 = (param1Int2 + param1Int4 - param1Int3) / 2;
        DrawableCompat.setHotspotBounds(drawable2, i - param1Int1, param1Int2 - param1Int1, i + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    final ActionMenuPresenter.OverflowMenuButton this$1;
    
    final ActionMenuPresenter val$this$0;
    
    null(View param1View) {
      super(param1View);
    }
    
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    final ActionMenuPresenter this$0;
    
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    final ActionMenuPresenter this$0;
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      boolean bool = false;
      if (param1MenuBuilder == null)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        bool = callback.onOpenSubMenu(param1MenuBuilder); 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ActionMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */