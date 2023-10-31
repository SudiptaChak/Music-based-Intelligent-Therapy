package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
  static final int GENERATED_ITEM_PADDING = 4;
  
  static final int MIN_CELL_SIZE = 56;
  
  private static final String TAG = "ActionMenuView";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private boolean mFormatItems;
  
  private int mFormatItemsWidth;
  
  private int mGeneratedItemPadding;
  
  private MenuBuilder mMenu;
  
  MenuBuilder.Callback mMenuBuilderCallback;
  
  private int mMinCellSize;
  
  OnMenuItemClickListener mOnMenuItemClickListener;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private ActionMenuPresenter mPresenter;
  
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    this.mMinCellSize = (int)(56.0F * f);
    this.mGeneratedItemPadding = (int)(f * 4.0F);
    this.mPopupContext = paramContext;
    this.mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ActionMenuItemView actionMenuItemView;
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt3) - paramInt4, View.MeasureSpec.getMode(paramInt3));
    if (paramView instanceof ActionMenuItemView) {
      actionMenuItemView = (ActionMenuItemView)paramView;
    } else {
      actionMenuItemView = null;
    } 
    boolean bool = true;
    if (actionMenuItemView != null && actionMenuItemView.hasText()) {
      paramInt3 = 1;
    } else {
      paramInt3 = 0;
    } 
    paramInt4 = 2;
    if (paramInt2 > 0 && (paramInt3 == 0 || paramInt2 >= 2)) {
      paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt2 * paramInt1, -2147483648), i);
      int k = paramView.getMeasuredWidth();
      int j = k / paramInt1;
      paramInt2 = j;
      if (k % paramInt1 != 0)
        paramInt2 = j + 1; 
      if (paramInt3 != 0 && paramInt2 < 2)
        paramInt2 = paramInt4; 
    } else {
      paramInt2 = 0;
    } 
    if (layoutParams.isOverflowButton || paramInt3 == 0)
      bool = false; 
    layoutParams.expandable = bool;
    layoutParams.cellsUsed = paramInt2;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1 * paramInt2, 1073741824), i);
    return paramInt2;
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_2
    //   1: invokestatic getMode : (I)I
    //   4: istore #12
    //   6: iload_1
    //   7: invokestatic getSize : (I)I
    //   10: istore #5
    //   12: iload_2
    //   13: invokestatic getSize : (I)I
    //   16: istore #6
    //   18: aload_0
    //   19: invokevirtual getPaddingLeft : ()I
    //   22: istore #7
    //   24: aload_0
    //   25: invokevirtual getPaddingRight : ()I
    //   28: istore_1
    //   29: aload_0
    //   30: invokevirtual getPaddingTop : ()I
    //   33: aload_0
    //   34: invokevirtual getPaddingBottom : ()I
    //   37: iadd
    //   38: istore #15
    //   40: iload_2
    //   41: iload #15
    //   43: bipush #-2
    //   45: invokestatic getChildMeasureSpec : (III)I
    //   48: istore #19
    //   50: iload #5
    //   52: iload #7
    //   54: iload_1
    //   55: iadd
    //   56: isub
    //   57: istore #14
    //   59: aload_0
    //   60: getfield mMinCellSize : I
    //   63: istore_1
    //   64: iload #14
    //   66: iload_1
    //   67: idiv
    //   68: istore #5
    //   70: iload #5
    //   72: ifne -> 83
    //   75: aload_0
    //   76: iload #14
    //   78: iconst_0
    //   79: invokevirtual setMeasuredDimension : (II)V
    //   82: return
    //   83: iload_1
    //   84: iload #14
    //   86: iload_1
    //   87: irem
    //   88: iload #5
    //   90: idiv
    //   91: iadd
    //   92: istore #21
    //   94: aload_0
    //   95: invokevirtual getChildCount : ()I
    //   98: istore #20
    //   100: iconst_0
    //   101: istore #13
    //   103: iconst_0
    //   104: istore #9
    //   106: iload #9
    //   108: istore #7
    //   110: iload #7
    //   112: istore_1
    //   113: iload_1
    //   114: istore_2
    //   115: iload_2
    //   116: istore #8
    //   118: lconst_0
    //   119: lstore #23
    //   121: iload_2
    //   122: istore #10
    //   124: iload_1
    //   125: istore #11
    //   127: iload #13
    //   129: istore_2
    //   130: iload #5
    //   132: istore_1
    //   133: iload #9
    //   135: iload #20
    //   137: if_icmpge -> 392
    //   140: aload_0
    //   141: iload #9
    //   143: invokevirtual getChildAt : (I)Landroid/view/View;
    //   146: astore #31
    //   148: aload #31
    //   150: invokevirtual getVisibility : ()I
    //   153: bipush #8
    //   155: if_icmpne -> 165
    //   158: iload #8
    //   160: istore #5
    //   162: goto -> 382
    //   165: aload #31
    //   167: instanceof androidx/appcompat/view/menu/ActionMenuItemView
    //   170: istore #22
    //   172: iinc #11, 1
    //   175: iload #22
    //   177: ifeq -> 200
    //   180: aload_0
    //   181: getfield mGeneratedItemPadding : I
    //   184: istore #5
    //   186: aload #31
    //   188: iload #5
    //   190: iconst_0
    //   191: iload #5
    //   193: iconst_0
    //   194: invokevirtual setPadding : (IIII)V
    //   197: goto -> 200
    //   200: aload #31
    //   202: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   205: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   208: astore #32
    //   210: aload #32
    //   212: iconst_0
    //   213: putfield expanded : Z
    //   216: aload #32
    //   218: iconst_0
    //   219: putfield extraPixels : I
    //   222: aload #32
    //   224: iconst_0
    //   225: putfield cellsUsed : I
    //   228: aload #32
    //   230: iconst_0
    //   231: putfield expandable : Z
    //   234: aload #32
    //   236: iconst_0
    //   237: putfield leftMargin : I
    //   240: aload #32
    //   242: iconst_0
    //   243: putfield rightMargin : I
    //   246: iload #22
    //   248: ifeq -> 268
    //   251: aload #31
    //   253: checkcast androidx/appcompat/view/menu/ActionMenuItemView
    //   256: invokevirtual hasText : ()Z
    //   259: ifeq -> 268
    //   262: iconst_1
    //   263: istore #22
    //   265: goto -> 271
    //   268: iconst_0
    //   269: istore #22
    //   271: aload #32
    //   273: iload #22
    //   275: putfield preventEdgeOffset : Z
    //   278: aload #32
    //   280: getfield isOverflowButton : Z
    //   283: ifeq -> 292
    //   286: iconst_1
    //   287: istore #5
    //   289: goto -> 295
    //   292: iload_1
    //   293: istore #5
    //   295: aload #31
    //   297: iload #21
    //   299: iload #5
    //   301: iload #19
    //   303: iload #15
    //   305: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   308: istore #13
    //   310: iload #10
    //   312: iload #13
    //   314: invokestatic max : (II)I
    //   317: istore #10
    //   319: iload #8
    //   321: istore #5
    //   323: aload #32
    //   325: getfield expandable : Z
    //   328: ifeq -> 337
    //   331: iload #8
    //   333: iconst_1
    //   334: iadd
    //   335: istore #5
    //   337: aload #32
    //   339: getfield isOverflowButton : Z
    //   342: ifeq -> 348
    //   345: iconst_1
    //   346: istore #7
    //   348: iload_1
    //   349: iload #13
    //   351: isub
    //   352: istore_1
    //   353: iload_2
    //   354: aload #31
    //   356: invokevirtual getMeasuredHeight : ()I
    //   359: invokestatic max : (II)I
    //   362: istore_2
    //   363: iload #13
    //   365: iconst_1
    //   366: if_icmpne -> 382
    //   369: lload #23
    //   371: iconst_1
    //   372: iload #9
    //   374: ishl
    //   375: i2l
    //   376: lor
    //   377: lstore #23
    //   379: goto -> 382
    //   382: iinc #9, 1
    //   385: iload #5
    //   387: istore #8
    //   389: goto -> 133
    //   392: iload #7
    //   394: ifeq -> 409
    //   397: iload #11
    //   399: iconst_2
    //   400: if_icmpne -> 409
    //   403: iconst_1
    //   404: istore #9
    //   406: goto -> 412
    //   409: iconst_0
    //   410: istore #9
    //   412: iconst_0
    //   413: istore #5
    //   415: iload_1
    //   416: istore #13
    //   418: iload #9
    //   420: istore #15
    //   422: iload #14
    //   424: istore #9
    //   426: iload #8
    //   428: ifle -> 742
    //   431: iload #13
    //   433: ifle -> 742
    //   436: ldc 2147483647
    //   438: istore #14
    //   440: iconst_0
    //   441: istore #17
    //   443: iconst_0
    //   444: istore #16
    //   446: lconst_0
    //   447: lstore #27
    //   449: iload #16
    //   451: iload #20
    //   453: if_icmpge -> 576
    //   456: aload_0
    //   457: iload #16
    //   459: invokevirtual getChildAt : (I)Landroid/view/View;
    //   462: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   465: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   468: astore #31
    //   470: aload #31
    //   472: getfield expandable : Z
    //   475: ifne -> 492
    //   478: iload #17
    //   480: istore_1
    //   481: iload #14
    //   483: istore #18
    //   485: lload #27
    //   487: lstore #25
    //   489: goto -> 559
    //   492: aload #31
    //   494: getfield cellsUsed : I
    //   497: iload #14
    //   499: if_icmpge -> 520
    //   502: aload #31
    //   504: getfield cellsUsed : I
    //   507: istore #18
    //   509: lconst_1
    //   510: iload #16
    //   512: lshl
    //   513: lstore #25
    //   515: iconst_1
    //   516: istore_1
    //   517: goto -> 559
    //   520: iload #17
    //   522: istore_1
    //   523: iload #14
    //   525: istore #18
    //   527: lload #27
    //   529: lstore #25
    //   531: aload #31
    //   533: getfield cellsUsed : I
    //   536: iload #14
    //   538: if_icmpne -> 559
    //   541: iload #17
    //   543: iconst_1
    //   544: iadd
    //   545: istore_1
    //   546: lload #27
    //   548: lconst_1
    //   549: iload #16
    //   551: lshl
    //   552: lor
    //   553: lstore #25
    //   555: iload #14
    //   557: istore #18
    //   559: iinc #16, 1
    //   562: iload_1
    //   563: istore #17
    //   565: iload #18
    //   567: istore #14
    //   569: lload #25
    //   571: lstore #27
    //   573: goto -> 449
    //   576: iload #5
    //   578: istore_1
    //   579: lload #23
    //   581: lload #27
    //   583: lor
    //   584: lstore #23
    //   586: iload #17
    //   588: iload #13
    //   590: if_icmple -> 596
    //   593: goto -> 745
    //   596: iconst_0
    //   597: istore_1
    //   598: iload_1
    //   599: iload #20
    //   601: if_icmpge -> 736
    //   604: aload_0
    //   605: iload_1
    //   606: invokevirtual getChildAt : (I)Landroid/view/View;
    //   609: astore #31
    //   611: aload #31
    //   613: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   616: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   619: astore #32
    //   621: iconst_1
    //   622: iload_1
    //   623: ishl
    //   624: i2l
    //   625: lstore #29
    //   627: lload #27
    //   629: lload #29
    //   631: land
    //   632: lconst_0
    //   633: lcmp
    //   634: ifne -> 667
    //   637: lload #23
    //   639: lstore #25
    //   641: aload #32
    //   643: getfield cellsUsed : I
    //   646: iload #14
    //   648: iconst_1
    //   649: iadd
    //   650: if_icmpne -> 660
    //   653: lload #23
    //   655: lload #29
    //   657: lor
    //   658: lstore #25
    //   660: lload #25
    //   662: lstore #23
    //   664: goto -> 730
    //   667: iload #15
    //   669: ifeq -> 709
    //   672: aload #32
    //   674: getfield preventEdgeOffset : Z
    //   677: ifeq -> 709
    //   680: iload #13
    //   682: iconst_1
    //   683: if_icmpne -> 709
    //   686: aload_0
    //   687: getfield mGeneratedItemPadding : I
    //   690: istore #5
    //   692: aload #31
    //   694: iload #5
    //   696: iload #21
    //   698: iadd
    //   699: iconst_0
    //   700: iload #5
    //   702: iconst_0
    //   703: invokevirtual setPadding : (IIII)V
    //   706: goto -> 709
    //   709: aload #32
    //   711: aload #32
    //   713: getfield cellsUsed : I
    //   716: iconst_1
    //   717: iadd
    //   718: putfield cellsUsed : I
    //   721: aload #32
    //   723: iconst_1
    //   724: putfield expanded : Z
    //   727: iinc #13, -1
    //   730: iinc #1, 1
    //   733: goto -> 598
    //   736: iconst_1
    //   737: istore #5
    //   739: goto -> 426
    //   742: iload #5
    //   744: istore_1
    //   745: iload #7
    //   747: ifne -> 762
    //   750: iload #11
    //   752: iconst_1
    //   753: if_icmpne -> 762
    //   756: iconst_1
    //   757: istore #5
    //   759: goto -> 765
    //   762: iconst_0
    //   763: istore #5
    //   765: iload #13
    //   767: ifle -> 1112
    //   770: lload #23
    //   772: lconst_0
    //   773: lcmp
    //   774: ifeq -> 1112
    //   777: iload #13
    //   779: iload #11
    //   781: iconst_1
    //   782: isub
    //   783: if_icmplt -> 797
    //   786: iload #5
    //   788: ifne -> 797
    //   791: iload #10
    //   793: iconst_1
    //   794: if_icmple -> 1112
    //   797: lload #23
    //   799: invokestatic bitCount : (J)I
    //   802: i2f
    //   803: fstore #4
    //   805: iload #5
    //   807: ifne -> 900
    //   810: fload #4
    //   812: fstore_3
    //   813: lload #23
    //   815: lconst_1
    //   816: land
    //   817: lconst_0
    //   818: lcmp
    //   819: ifeq -> 848
    //   822: fload #4
    //   824: fstore_3
    //   825: aload_0
    //   826: iconst_0
    //   827: invokevirtual getChildAt : (I)Landroid/view/View;
    //   830: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   833: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   836: getfield preventEdgeOffset : Z
    //   839: ifne -> 848
    //   842: fload #4
    //   844: ldc 0.5
    //   846: fsub
    //   847: fstore_3
    //   848: iload #20
    //   850: iconst_1
    //   851: isub
    //   852: istore #5
    //   854: fload_3
    //   855: fstore #4
    //   857: lload #23
    //   859: iconst_1
    //   860: iload #5
    //   862: ishl
    //   863: i2l
    //   864: land
    //   865: lconst_0
    //   866: lcmp
    //   867: ifeq -> 900
    //   870: fload_3
    //   871: fstore #4
    //   873: aload_0
    //   874: iload #5
    //   876: invokevirtual getChildAt : (I)Landroid/view/View;
    //   879: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   882: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   885: getfield preventEdgeOffset : Z
    //   888: ifne -> 900
    //   891: fload_3
    //   892: ldc 0.5
    //   894: fsub
    //   895: fstore #4
    //   897: goto -> 900
    //   900: fload #4
    //   902: fconst_0
    //   903: fcmpl
    //   904: ifle -> 922
    //   907: iload #13
    //   909: iload #21
    //   911: imul
    //   912: i2f
    //   913: fload #4
    //   915: fdiv
    //   916: f2i
    //   917: istore #7
    //   919: goto -> 925
    //   922: iconst_0
    //   923: istore #7
    //   925: iconst_0
    //   926: istore #8
    //   928: iload_1
    //   929: istore #5
    //   931: iload #8
    //   933: iload #20
    //   935: if_icmpge -> 1115
    //   938: lload #23
    //   940: iconst_1
    //   941: iload #8
    //   943: ishl
    //   944: i2l
    //   945: land
    //   946: lconst_0
    //   947: lcmp
    //   948: ifne -> 957
    //   951: iload_1
    //   952: istore #5
    //   954: goto -> 1103
    //   957: aload_0
    //   958: iload #8
    //   960: invokevirtual getChildAt : (I)Landroid/view/View;
    //   963: astore #32
    //   965: aload #32
    //   967: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   970: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   973: astore #31
    //   975: aload #32
    //   977: instanceof androidx/appcompat/view/menu/ActionMenuItemView
    //   980: ifeq -> 1028
    //   983: aload #31
    //   985: iload #7
    //   987: putfield extraPixels : I
    //   990: aload #31
    //   992: iconst_1
    //   993: putfield expanded : Z
    //   996: iload #8
    //   998: ifne -> 1022
    //   1001: aload #31
    //   1003: getfield preventEdgeOffset : Z
    //   1006: ifne -> 1022
    //   1009: aload #31
    //   1011: iload #7
    //   1013: ineg
    //   1014: iconst_2
    //   1015: idiv
    //   1016: putfield leftMargin : I
    //   1019: goto -> 1022
    //   1022: iconst_1
    //   1023: istore #5
    //   1025: goto -> 1103
    //   1028: aload #31
    //   1030: getfield isOverflowButton : Z
    //   1033: ifeq -> 1065
    //   1036: aload #31
    //   1038: iload #7
    //   1040: putfield extraPixels : I
    //   1043: aload #31
    //   1045: iconst_1
    //   1046: putfield expanded : Z
    //   1049: aload #31
    //   1051: iload #7
    //   1053: ineg
    //   1054: iconst_2
    //   1055: idiv
    //   1056: putfield rightMargin : I
    //   1059: iconst_1
    //   1060: istore #5
    //   1062: goto -> 1103
    //   1065: iload #8
    //   1067: ifeq -> 1079
    //   1070: aload #31
    //   1072: iload #7
    //   1074: iconst_2
    //   1075: idiv
    //   1076: putfield leftMargin : I
    //   1079: iload_1
    //   1080: istore #5
    //   1082: iload #8
    //   1084: iload #20
    //   1086: iconst_1
    //   1087: isub
    //   1088: if_icmpeq -> 1103
    //   1091: aload #31
    //   1093: iload #7
    //   1095: iconst_2
    //   1096: idiv
    //   1097: putfield rightMargin : I
    //   1100: iload_1
    //   1101: istore #5
    //   1103: iinc #8, 1
    //   1106: iload #5
    //   1108: istore_1
    //   1109: goto -> 928
    //   1112: iload_1
    //   1113: istore #5
    //   1115: iload #5
    //   1117: ifeq -> 1188
    //   1120: iconst_0
    //   1121: istore_1
    //   1122: iload_1
    //   1123: iload #20
    //   1125: if_icmpge -> 1188
    //   1128: aload_0
    //   1129: iload_1
    //   1130: invokevirtual getChildAt : (I)Landroid/view/View;
    //   1133: astore #31
    //   1135: aload #31
    //   1137: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1140: checkcast androidx/appcompat/widget/ActionMenuView$LayoutParams
    //   1143: astore #32
    //   1145: aload #32
    //   1147: getfield expanded : Z
    //   1150: ifne -> 1156
    //   1153: goto -> 1182
    //   1156: aload #31
    //   1158: aload #32
    //   1160: getfield cellsUsed : I
    //   1163: iload #21
    //   1165: imul
    //   1166: aload #32
    //   1168: getfield extraPixels : I
    //   1171: iadd
    //   1172: ldc 1073741824
    //   1174: invokestatic makeMeasureSpec : (II)I
    //   1177: iload #19
    //   1179: invokevirtual measure : (II)V
    //   1182: iinc #1, 1
    //   1185: goto -> 1122
    //   1188: iload #12
    //   1190: ldc 1073741824
    //   1192: if_icmpeq -> 1200
    //   1195: iload_2
    //   1196: istore_1
    //   1197: goto -> 1203
    //   1200: iload #6
    //   1202: istore_1
    //   1203: aload_0
    //   1204: iload #9
    //   1206: iload_1
    //   1207: invokevirtual setMeasuredDimension : (II)V
    //   1210: return
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dismissPopupMenus() {
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null)
      actionMenuPresenter.dismissPopupMenus(); 
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    layoutParams.gravity = 16;
    return layoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    if (paramLayoutParams != null) {
      LayoutParams layoutParams;
      if (paramLayoutParams instanceof LayoutParams) {
        layoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        layoutParams = new LayoutParams((ViewGroup.LayoutParams)layoutParams);
      } 
      if (layoutParams.gravity <= 0)
        layoutParams.gravity = 16; 
      return layoutParams;
    } 
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams() {
    LayoutParams layoutParams = generateDefaultLayoutParams();
    layoutParams.isOverflowButton = true;
    return layoutParams;
  }
  
  public Menu getMenu() {
    if (this.mMenu == null) {
      Context context = getContext();
      MenuBuilder menuBuilder = new MenuBuilder(context);
      this.mMenu = menuBuilder;
      menuBuilder.setCallback(new MenuBuilderCallback());
      ActionMenuPresenter actionMenuPresenter1 = new ActionMenuPresenter(context);
      this.mPresenter = actionMenuPresenter1;
      actionMenuPresenter1.setReserveOverflow(true);
      ActionMenuPresenter actionMenuPresenter2 = this.mPresenter;
      MenuPresenter.Callback callback = this.mActionMenuPresenterCallback;
      if (callback == null)
        callback = new ActionMenuPresenterCallback(); 
      actionMenuPresenter2.setCallback(callback);
      this.mMenu.addMenuPresenter((MenuPresenter)this.mPresenter, this.mPopupContext);
      this.mPresenter.setMenuView(this);
    } 
    return (Menu)this.mMenu;
  }
  
  public Drawable getOverflowIcon() {
    getMenu();
    return this.mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public int getWindowAnimations() {
    return 0;
  }
  
  protected boolean hasSupportDividerBeforeChildAt(int paramInt) {
    boolean bool;
    int j = 0;
    if (paramInt == 0)
      return false; 
    View view2 = getChildAt(paramInt - 1);
    View view1 = getChildAt(paramInt);
    int i = j;
    if (paramInt < getChildCount()) {
      i = j;
      if (view2 instanceof ActionMenuChildView)
        i = false | ((ActionMenuChildView)view2).needsDividerAfter(); 
    } 
    j = i;
    if (paramInt > 0) {
      j = i;
      if (view1 instanceof ActionMenuChildView)
        bool = i | ((ActionMenuChildView)view1).needsDividerBefore(); 
    } 
    return bool;
  }
  
  public boolean hideOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder) {
    this.mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
    return this.mMenu.performItemAction((MenuItem)paramMenuItemImpl, 0);
  }
  
  public boolean isOverflowMenuShowPending() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null) {
      actionMenuPresenter.updateMenuView(false);
      if (this.mPresenter.isOverflowMenuShowing()) {
        this.mPresenter.hideOverflowMenu();
        this.mPresenter.showOverflowMenu();
      } 
    } 
  }
  
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mFormatItems) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    int j = getChildCount();
    int i = (paramInt4 - paramInt2) / 2;
    int m = getDividerWidth();
    int k = paramInt3 - paramInt1;
    paramInt1 = k - getPaddingRight() - getPaddingLeft();
    paramBoolean = ViewUtils.isLayoutRtl((View)this);
    paramInt2 = 0;
    paramInt4 = 0;
    paramInt3 = 0;
    while (paramInt2 < j) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.isOverflowButton) {
          int i1;
          int n = view.getMeasuredWidth();
          paramInt4 = n;
          if (hasSupportDividerBeforeChildAt(paramInt2))
            paramInt4 = n + m; 
          int i2 = view.getMeasuredHeight();
          if (paramBoolean) {
            i1 = getPaddingLeft() + layoutParams.leftMargin;
            n = i1 + paramInt4;
          } else {
            n = getWidth() - getPaddingRight() - layoutParams.rightMargin;
            i1 = n - paramInt4;
          } 
          int i3 = i - i2 / 2;
          view.layout(i1, i3, n, i2 + i3);
          paramInt1 -= paramInt4;
          paramInt4 = 1;
        } else {
          paramInt1 -= view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
          hasSupportDividerBeforeChildAt(paramInt2);
          paramInt3++;
        } 
      } 
      paramInt2++;
    } 
    if (j == 1 && paramInt4 == 0) {
      View view = getChildAt(0);
      paramInt1 = view.getMeasuredWidth();
      paramInt2 = view.getMeasuredHeight();
      paramInt3 = k / 2 - paramInt1 / 2;
      paramInt4 = i - paramInt2 / 2;
      view.layout(paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4);
      return;
    } 
    paramInt2 = paramInt3 - (paramInt4 ^ 0x1);
    if (paramInt2 > 0) {
      paramInt1 /= paramInt2;
    } else {
      paramInt1 = 0;
    } 
    paramInt4 = Math.max(0, paramInt1);
    if (paramBoolean) {
      paramInt2 = getWidth() - getPaddingRight();
      paramInt1 = 0;
      while (paramInt1 < j) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            paramInt3 = paramInt2 - layoutParams.rightMargin;
            int n = view.getMeasuredWidth();
            int i1 = view.getMeasuredHeight();
            paramInt2 = i - i1 / 2;
            view.layout(paramInt3 - n, paramInt2, paramInt3, i1 + paramInt2);
            paramInt3 -= n + layoutParams.leftMargin + paramInt4;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
      } 
    } else {
      paramInt2 = getPaddingLeft();
      paramInt1 = 0;
      while (paramInt1 < j) {
        View view = getChildAt(paramInt1);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        paramInt3 = paramInt2;
        if (view.getVisibility() != 8)
          if (layoutParams.isOverflowButton) {
            paramInt3 = paramInt2;
          } else {
            paramInt2 += layoutParams.leftMargin;
            int i1 = view.getMeasuredWidth();
            paramInt3 = view.getMeasuredHeight();
            int n = i - paramInt3 / 2;
            view.layout(paramInt2, n, paramInt2 + i1, paramInt3 + n);
            paramInt3 = paramInt2 + i1 + layoutParams.rightMargin + paramInt4;
          }  
        paramInt1++;
        paramInt2 = paramInt3;
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool2 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mFormatItems = bool1;
    if (bool2 != bool1)
      this.mFormatItemsWidth = 0; 
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mFormatItems) {
      MenuBuilder menuBuilder = this.mMenu;
      if (menuBuilder != null && i != this.mFormatItemsWidth) {
        this.mFormatItemsWidth = i;
        menuBuilder.onItemsChanged(true);
      } 
    } 
    int j = getChildCount();
    if (this.mFormatItems && j > 0) {
      onMeasureExactFormat(paramInt1, paramInt2);
    } else {
      for (i = 0; i < j; i++) {
        LayoutParams layoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
        layoutParams.rightMargin = 0;
        layoutParams.leftMargin = 0;
      } 
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public MenuBuilder peekMenu() {
    return this.mMenu;
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    getMenu();
    this.mPresenter.setOverflowIcon(paramDrawable);
  }
  
  public void setOverflowReserved(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
      } else {
        this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
      } 
    } 
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter) {
    this.mPresenter = paramActionMenuPresenter;
    paramActionMenuPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu() {
    boolean bool;
    ActionMenuPresenter actionMenuPresenter = this.mPresenter;
    if (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static interface ActionMenuChildView {
    boolean needsDividerAfter();
    
    boolean needsDividerBefore();
  }
  
  private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      return false;
    }
  }
  
  public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
    @ExportedProperty
    public int cellsUsed;
    
    @ExportedProperty
    public boolean expandable;
    
    boolean expanded;
    
    @ExportedProperty
    public int extraPixels;
    
    @ExportedProperty
    public boolean isOverflowButton;
    
    @ExportedProperty
    public boolean preventEdgeOffset;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = false;
    }
    
    LayoutParams(int param1Int1, int param1Int2, boolean param1Boolean) {
      super(param1Int1, param1Int2);
      this.isOverflowButton = param1Boolean;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
      this.isOverflowButton = param1LayoutParams.isOverflowButton;
    }
  }
  
  private class MenuBuilderCallback implements MenuBuilder.Callback {
    final ActionMenuView this$0;
    
    public boolean onMenuItemSelected(MenuBuilder param1MenuBuilder, MenuItem param1MenuItem) {
      boolean bool;
      if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onMenuModeChange(MenuBuilder param1MenuBuilder) {
      if (ActionMenuView.this.mMenuBuilderCallback != null)
        ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(param1MenuBuilder); 
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\ActionMenuView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */