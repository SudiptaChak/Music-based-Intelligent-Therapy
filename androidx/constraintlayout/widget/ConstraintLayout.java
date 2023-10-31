package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.Analyzer;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  static final boolean ALLOWS_EMBEDDED = false;
  
  private static final boolean CACHE_MEASURED_DIMENSION = false;
  
  private static final boolean DEBUG = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-1.1.3";
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private HashMap<String, Integer> mDesignIds = new HashMap<String, Integer>();
  
  private boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOptimizationLevel = 7;
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    ConstraintWidget constraintWidget;
    if (paramInt == 0)
      return (ConstraintWidget)this.mLayoutWidget; 
    View view2 = (View)this.mChildrenByIds.get(paramInt);
    View view1 = view2;
    if (view2 == null) {
      view2 = findViewById(paramInt);
      view1 = view2;
      if (view2 != null) {
        view1 = view2;
        if (view2 != this) {
          view1 = view2;
          if (view2.getParent() == this) {
            onViewAdded(view2);
            view1 = view2;
          } 
        } 
      } 
    } 
    if (view1 == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (view1 == null) {
      view1 = null;
    } else {
      constraintWidget = ((LayoutParams)view1.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  private void init(AttributeSet paramAttributeSet) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(j, this.mMinWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(j, this.mMinHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(j, this.mMaxWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(j, this.mMaxHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(j, this.mOptimizationLevel);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraintSet) {
          j = typedArray.getResourceId(j, 0);
          try {
            ConstraintSet constraintSet = new ConstraintSet();
            this();
            this.mConstraintSet = constraintSet;
            constraintSet.load(getContext(), j);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = j;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void internalMeasureChildren(int paramInt1, int paramInt2) {
    int j = getPaddingTop() + getPaddingBottom();
    int k = getPaddingLeft() + getPaddingRight();
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ConstraintWidget constraintWidget = layoutParams.widget;
        if (!layoutParams.isGuideline && !layoutParams.isHelper) {
          int m;
          boolean bool;
          int n;
          int i1;
          constraintWidget.setVisibility(view.getVisibility());
          int i2 = layoutParams.width;
          int i3 = layoutParams.height;
          if (layoutParams.horizontalDimensionFixed || layoutParams.verticalDimensionFixed || (!layoutParams.horizontalDimensionFixed && layoutParams.matchConstraintDefaultWidth == 1) || layoutParams.width == -1 || (!layoutParams.verticalDimensionFixed && (layoutParams.matchConstraintDefaultHeight == 1 || layoutParams.height == -1))) {
            m = 1;
          } else {
            m = 0;
          } 
          if (m) {
            boolean bool1;
            if (i2 == 0) {
              n = getChildMeasureSpec(paramInt1, k, -2);
              m = 1;
            } else if (i2 == -1) {
              n = getChildMeasureSpec(paramInt1, k, -1);
              m = 0;
            } else {
              if (i2 == -2) {
                m = 1;
              } else {
                m = 0;
              } 
              n = getChildMeasureSpec(paramInt1, k, i2);
            } 
            if (i3 == 0) {
              i1 = getChildMeasureSpec(paramInt2, j, -2);
              bool = true;
            } else if (i3 == -1) {
              i1 = getChildMeasureSpec(paramInt2, j, -1);
              bool = false;
            } else {
              if (i3 == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              i1 = getChildMeasureSpec(paramInt2, j, i3);
            } 
            view.measure(n, i1);
            Metrics metrics = this.mMetrics;
            if (metrics != null)
              metrics.measures++; 
            if (i2 == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setWidthWrapContent(bool1);
            if (i3 == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setHeightWrapContent(bool1);
            n = view.getMeasuredWidth();
            i1 = view.getMeasuredHeight();
          } else {
            m = 0;
            bool = false;
            i1 = i3;
            n = i2;
          } 
          constraintWidget.setWidth(n);
          constraintWidget.setHeight(i1);
          if (m)
            constraintWidget.setWrapWidth(n); 
          if (bool)
            constraintWidget.setWrapHeight(i1); 
          if (layoutParams.needsBaseline) {
            m = view.getBaseline();
            if (m != -1)
              constraintWidget.setBaselineDistance(m); 
          } 
        } 
      } 
    } 
  }
  
  private void internalMeasureDimensions(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: astore #21
    //   3: aload_0
    //   4: invokevirtual getPaddingTop : ()I
    //   7: aload_0
    //   8: invokevirtual getPaddingBottom : ()I
    //   11: iadd
    //   12: istore #9
    //   14: aload_0
    //   15: invokevirtual getPaddingLeft : ()I
    //   18: aload_0
    //   19: invokevirtual getPaddingRight : ()I
    //   22: iadd
    //   23: istore #15
    //   25: aload_0
    //   26: invokevirtual getChildCount : ()I
    //   29: istore #10
    //   31: iconst_0
    //   32: istore_3
    //   33: lconst_1
    //   34: lstore #17
    //   36: iload_3
    //   37: iload #10
    //   39: if_icmpge -> 407
    //   42: aload #21
    //   44: iload_3
    //   45: invokevirtual getChildAt : (I)Landroid/view/View;
    //   48: astore #23
    //   50: aload #23
    //   52: invokevirtual getVisibility : ()I
    //   55: bipush #8
    //   57: if_icmpne -> 63
    //   60: goto -> 401
    //   63: aload #23
    //   65: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   68: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   71: astore #25
    //   73: aload #25
    //   75: getfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   78: astore #22
    //   80: aload #25
    //   82: getfield isGuideline : Z
    //   85: ifne -> 401
    //   88: aload #25
    //   90: getfield isHelper : Z
    //   93: ifeq -> 99
    //   96: goto -> 401
    //   99: aload #22
    //   101: aload #23
    //   103: invokevirtual getVisibility : ()I
    //   106: invokevirtual setVisibility : (I)V
    //   109: aload #25
    //   111: getfield width : I
    //   114: istore #7
    //   116: aload #25
    //   118: getfield height : I
    //   121: istore #6
    //   123: iload #7
    //   125: ifeq -> 382
    //   128: iload #6
    //   130: ifne -> 136
    //   133: goto -> 382
    //   136: iload #7
    //   138: bipush #-2
    //   140: if_icmpne -> 149
    //   143: iconst_1
    //   144: istore #4
    //   146: goto -> 152
    //   149: iconst_0
    //   150: istore #4
    //   152: iload_1
    //   153: iload #15
    //   155: iload #7
    //   157: invokestatic getChildMeasureSpec : (III)I
    //   160: istore #8
    //   162: iload #6
    //   164: bipush #-2
    //   166: if_icmpne -> 175
    //   169: iconst_1
    //   170: istore #5
    //   172: goto -> 178
    //   175: iconst_0
    //   176: istore #5
    //   178: aload #23
    //   180: iload #8
    //   182: iload_2
    //   183: iload #9
    //   185: iload #6
    //   187: invokestatic getChildMeasureSpec : (III)I
    //   190: invokevirtual measure : (II)V
    //   193: aload #21
    //   195: getfield mMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   198: astore #24
    //   200: aload #24
    //   202: ifnull -> 217
    //   205: aload #24
    //   207: aload #24
    //   209: getfield measures : J
    //   212: lconst_1
    //   213: ladd
    //   214: putfield measures : J
    //   217: iload #7
    //   219: bipush #-2
    //   221: if_icmpne -> 230
    //   224: iconst_1
    //   225: istore #16
    //   227: goto -> 233
    //   230: iconst_0
    //   231: istore #16
    //   233: aload #22
    //   235: iload #16
    //   237: invokevirtual setWidthWrapContent : (Z)V
    //   240: iload #6
    //   242: bipush #-2
    //   244: if_icmpne -> 253
    //   247: iconst_1
    //   248: istore #16
    //   250: goto -> 256
    //   253: iconst_0
    //   254: istore #16
    //   256: aload #22
    //   258: iload #16
    //   260: invokevirtual setHeightWrapContent : (Z)V
    //   263: aload #23
    //   265: invokevirtual getMeasuredWidth : ()I
    //   268: istore #6
    //   270: aload #23
    //   272: invokevirtual getMeasuredHeight : ()I
    //   275: istore #7
    //   277: aload #22
    //   279: iload #6
    //   281: invokevirtual setWidth : (I)V
    //   284: aload #22
    //   286: iload #7
    //   288: invokevirtual setHeight : (I)V
    //   291: iload #4
    //   293: ifeq -> 303
    //   296: aload #22
    //   298: iload #6
    //   300: invokevirtual setWrapWidth : (I)V
    //   303: iload #5
    //   305: ifeq -> 315
    //   308: aload #22
    //   310: iload #7
    //   312: invokevirtual setWrapHeight : (I)V
    //   315: aload #25
    //   317: getfield needsBaseline : Z
    //   320: ifeq -> 343
    //   323: aload #23
    //   325: invokevirtual getBaseline : ()I
    //   328: istore #4
    //   330: iload #4
    //   332: iconst_m1
    //   333: if_icmpeq -> 343
    //   336: aload #22
    //   338: iload #4
    //   340: invokevirtual setBaselineDistance : (I)V
    //   343: aload #25
    //   345: getfield horizontalDimensionFixed : Z
    //   348: ifeq -> 401
    //   351: aload #25
    //   353: getfield verticalDimensionFixed : Z
    //   356: ifeq -> 401
    //   359: aload #22
    //   361: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   364: iload #6
    //   366: invokevirtual resolve : (I)V
    //   369: aload #22
    //   371: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   374: iload #7
    //   376: invokevirtual resolve : (I)V
    //   379: goto -> 401
    //   382: aload #22
    //   384: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   387: invokevirtual invalidate : ()V
    //   390: aload #22
    //   392: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   395: invokevirtual invalidate : ()V
    //   398: goto -> 401
    //   401: iinc #3, 1
    //   404: goto -> 33
    //   407: aload #21
    //   409: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   412: invokevirtual solveGraph : ()V
    //   415: iconst_0
    //   416: istore #11
    //   418: iload #11
    //   420: iload #10
    //   422: if_icmpge -> 1295
    //   425: aload #21
    //   427: iload #11
    //   429: invokevirtual getChildAt : (I)Landroid/view/View;
    //   432: astore #24
    //   434: aload #24
    //   436: invokevirtual getVisibility : ()I
    //   439: bipush #8
    //   441: if_icmpne -> 447
    //   444: goto -> 1289
    //   447: aload #24
    //   449: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   452: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   455: astore #25
    //   457: aload #25
    //   459: getfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   462: astore #23
    //   464: aload #25
    //   466: getfield isGuideline : Z
    //   469: ifne -> 1289
    //   472: aload #25
    //   474: getfield isHelper : Z
    //   477: ifeq -> 483
    //   480: goto -> 1289
    //   483: aload #23
    //   485: aload #24
    //   487: invokevirtual getVisibility : ()I
    //   490: invokevirtual setVisibility : (I)V
    //   493: aload #25
    //   495: getfield width : I
    //   498: istore #7
    //   500: aload #25
    //   502: getfield height : I
    //   505: istore #8
    //   507: iload #7
    //   509: ifeq -> 520
    //   512: iload #8
    //   514: ifeq -> 520
    //   517: goto -> 1289
    //   520: aload #23
    //   522: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   525: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   528: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   531: astore #27
    //   533: aload #23
    //   535: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   538: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   541: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   544: astore #28
    //   546: aload #23
    //   548: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   551: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   554: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   557: ifnull -> 580
    //   560: aload #23
    //   562: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   565: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   568: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   571: ifnull -> 580
    //   574: iconst_1
    //   575: istore #4
    //   577: goto -> 583
    //   580: iconst_0
    //   581: istore #4
    //   583: aload #23
    //   585: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   588: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   591: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   594: astore #22
    //   596: aload #23
    //   598: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   601: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   604: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   607: astore #26
    //   609: aload #23
    //   611: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   614: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   617: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   620: ifnull -> 643
    //   623: aload #23
    //   625: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   628: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   631: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   634: ifnull -> 643
    //   637: iconst_1
    //   638: istore #13
    //   640: goto -> 646
    //   643: iconst_0
    //   644: istore #13
    //   646: iload #7
    //   648: ifne -> 672
    //   651: iload #8
    //   653: ifne -> 672
    //   656: iload #4
    //   658: ifeq -> 672
    //   661: iload #13
    //   663: ifeq -> 672
    //   666: lconst_1
    //   667: lstore #17
    //   669: goto -> 1289
    //   672: aload #21
    //   674: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   677: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   680: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   683: if_acmpeq -> 692
    //   686: iconst_1
    //   687: istore #6
    //   689: goto -> 695
    //   692: iconst_0
    //   693: istore #6
    //   695: aload #21
    //   697: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   700: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   703: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   706: if_acmpeq -> 714
    //   709: iconst_1
    //   710: istore_3
    //   711: goto -> 716
    //   714: iconst_0
    //   715: istore_3
    //   716: iload #6
    //   718: ifne -> 729
    //   721: aload #23
    //   723: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   726: invokevirtual invalidate : ()V
    //   729: iload_3
    //   730: ifne -> 741
    //   733: aload #23
    //   735: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   738: invokevirtual invalidate : ()V
    //   741: iload #7
    //   743: ifne -> 840
    //   746: iload #6
    //   748: ifeq -> 817
    //   751: aload #23
    //   753: invokevirtual isSpreadWidth : ()Z
    //   756: ifeq -> 817
    //   759: iload #4
    //   761: ifeq -> 817
    //   764: aload #27
    //   766: invokevirtual isResolved : ()Z
    //   769: ifeq -> 817
    //   772: aload #28
    //   774: invokevirtual isResolved : ()Z
    //   777: ifeq -> 817
    //   780: aload #28
    //   782: invokevirtual getResolvedValue : ()F
    //   785: aload #27
    //   787: invokevirtual getResolvedValue : ()F
    //   790: fsub
    //   791: f2i
    //   792: istore #7
    //   794: aload #23
    //   796: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   799: iload #7
    //   801: invokevirtual resolve : (I)V
    //   804: iload_1
    //   805: iload #15
    //   807: iload #7
    //   809: invokestatic getChildMeasureSpec : (III)I
    //   812: istore #5
    //   814: goto -> 855
    //   817: iload_1
    //   818: iload #15
    //   820: bipush #-2
    //   822: invokestatic getChildMeasureSpec : (III)I
    //   825: istore #5
    //   827: iconst_0
    //   828: istore #12
    //   830: iconst_1
    //   831: istore #4
    //   833: iload #7
    //   835: istore #14
    //   837: goto -> 903
    //   840: iload #7
    //   842: iconst_m1
    //   843: if_icmpne -> 869
    //   846: iload_1
    //   847: iload #15
    //   849: iconst_m1
    //   850: invokestatic getChildMeasureSpec : (III)I
    //   853: istore #5
    //   855: iconst_0
    //   856: istore #4
    //   858: iload #6
    //   860: istore #12
    //   862: iload #7
    //   864: istore #14
    //   866: goto -> 903
    //   869: iload #7
    //   871: bipush #-2
    //   873: if_icmpne -> 882
    //   876: iconst_1
    //   877: istore #4
    //   879: goto -> 885
    //   882: iconst_0
    //   883: istore #4
    //   885: iload_1
    //   886: iload #15
    //   888: iload #7
    //   890: invokestatic getChildMeasureSpec : (III)I
    //   893: istore #5
    //   895: iload #7
    //   897: istore #14
    //   899: iload #6
    //   901: istore #12
    //   903: iload #8
    //   905: ifne -> 996
    //   908: iload_3
    //   909: ifeq -> 978
    //   912: aload #23
    //   914: invokevirtual isSpreadHeight : ()Z
    //   917: ifeq -> 978
    //   920: iload #13
    //   922: ifeq -> 978
    //   925: aload #22
    //   927: invokevirtual isResolved : ()Z
    //   930: ifeq -> 978
    //   933: aload #26
    //   935: invokevirtual isResolved : ()Z
    //   938: ifeq -> 978
    //   941: aload #26
    //   943: invokevirtual getResolvedValue : ()F
    //   946: aload #22
    //   948: invokevirtual getResolvedValue : ()F
    //   951: fsub
    //   952: f2i
    //   953: istore #8
    //   955: aload #23
    //   957: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   960: iload #8
    //   962: invokevirtual resolve : (I)V
    //   965: iload_2
    //   966: iload #9
    //   968: iload #8
    //   970: invokestatic getChildMeasureSpec : (III)I
    //   973: istore #7
    //   975: goto -> 1011
    //   978: iload_2
    //   979: iload #9
    //   981: bipush #-2
    //   983: invokestatic getChildMeasureSpec : (III)I
    //   986: istore #7
    //   988: iconst_0
    //   989: istore_3
    //   990: iconst_1
    //   991: istore #6
    //   993: goto -> 1043
    //   996: iload #8
    //   998: iconst_m1
    //   999: if_icmpne -> 1017
    //   1002: iload_2
    //   1003: iload #9
    //   1005: iconst_m1
    //   1006: invokestatic getChildMeasureSpec : (III)I
    //   1009: istore #7
    //   1011: iconst_0
    //   1012: istore #6
    //   1014: goto -> 1043
    //   1017: iload #8
    //   1019: bipush #-2
    //   1021: if_icmpne -> 1030
    //   1024: iconst_1
    //   1025: istore #6
    //   1027: goto -> 1033
    //   1030: iconst_0
    //   1031: istore #6
    //   1033: iload_2
    //   1034: iload #9
    //   1036: iload #8
    //   1038: invokestatic getChildMeasureSpec : (III)I
    //   1041: istore #7
    //   1043: aload #24
    //   1045: iload #5
    //   1047: iload #7
    //   1049: invokevirtual measure : (II)V
    //   1052: aload_0
    //   1053: astore #22
    //   1055: aload #22
    //   1057: getfield mMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1060: astore #21
    //   1062: aload #21
    //   1064: ifnull -> 1082
    //   1067: aload #21
    //   1069: aload #21
    //   1071: getfield measures : J
    //   1074: lconst_1
    //   1075: ladd
    //   1076: putfield measures : J
    //   1079: goto -> 1082
    //   1082: lconst_1
    //   1083: lstore #19
    //   1085: iload #14
    //   1087: bipush #-2
    //   1089: if_icmpne -> 1098
    //   1092: iconst_1
    //   1093: istore #16
    //   1095: goto -> 1101
    //   1098: iconst_0
    //   1099: istore #16
    //   1101: aload #23
    //   1103: iload #16
    //   1105: invokevirtual setWidthWrapContent : (Z)V
    //   1108: iload #8
    //   1110: bipush #-2
    //   1112: if_icmpne -> 1121
    //   1115: iconst_1
    //   1116: istore #16
    //   1118: goto -> 1124
    //   1121: iconst_0
    //   1122: istore #16
    //   1124: aload #23
    //   1126: iload #16
    //   1128: invokevirtual setHeightWrapContent : (Z)V
    //   1131: aload #24
    //   1133: invokevirtual getMeasuredWidth : ()I
    //   1136: istore #5
    //   1138: aload #24
    //   1140: invokevirtual getMeasuredHeight : ()I
    //   1143: istore #7
    //   1145: aload #23
    //   1147: iload #5
    //   1149: invokevirtual setWidth : (I)V
    //   1152: aload #23
    //   1154: iload #7
    //   1156: invokevirtual setHeight : (I)V
    //   1159: iload #4
    //   1161: ifeq -> 1171
    //   1164: aload #23
    //   1166: iload #5
    //   1168: invokevirtual setWrapWidth : (I)V
    //   1171: iload #6
    //   1173: ifeq -> 1183
    //   1176: aload #23
    //   1178: iload #7
    //   1180: invokevirtual setWrapHeight : (I)V
    //   1183: iload #12
    //   1185: ifeq -> 1201
    //   1188: aload #23
    //   1190: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1193: iload #5
    //   1195: invokevirtual resolve : (I)V
    //   1198: goto -> 1209
    //   1201: aload #23
    //   1203: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1206: invokevirtual remove : ()V
    //   1209: iload_3
    //   1210: ifeq -> 1226
    //   1213: aload #23
    //   1215: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1218: iload #7
    //   1220: invokevirtual resolve : (I)V
    //   1223: goto -> 1234
    //   1226: aload #23
    //   1228: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1231: invokevirtual remove : ()V
    //   1234: aload #25
    //   1236: getfield needsBaseline : Z
    //   1239: ifeq -> 1278
    //   1242: aload #24
    //   1244: invokevirtual getBaseline : ()I
    //   1247: istore_3
    //   1248: aload #22
    //   1250: astore #21
    //   1252: lload #19
    //   1254: lstore #17
    //   1256: iload_3
    //   1257: iconst_m1
    //   1258: if_icmpeq -> 1289
    //   1261: aload #23
    //   1263: iload_3
    //   1264: invokevirtual setBaselineDistance : (I)V
    //   1267: aload #22
    //   1269: astore #21
    //   1271: lload #19
    //   1273: lstore #17
    //   1275: goto -> 1289
    //   1278: aload #22
    //   1280: astore #21
    //   1282: lload #19
    //   1284: lstore #17
    //   1286: goto -> 1289
    //   1289: iinc #11, 1
    //   1292: goto -> 418
    //   1295: return
  }
  
  private void setChildrenConstraints() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isInEditMode : ()Z
    //   4: istore #12
    //   6: aload_0
    //   7: invokevirtual getChildCount : ()I
    //   10: istore #11
    //   12: iconst_0
    //   13: istore_3
    //   14: iload #12
    //   16: ifeq -> 113
    //   19: iconst_0
    //   20: istore_2
    //   21: iload_2
    //   22: iload #11
    //   24: if_icmpge -> 113
    //   27: aload_0
    //   28: iload_2
    //   29: invokevirtual getChildAt : (I)Landroid/view/View;
    //   32: astore #15
    //   34: aload_0
    //   35: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   38: aload #15
    //   40: invokevirtual getId : ()I
    //   43: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   46: astore #14
    //   48: aload_0
    //   49: iconst_0
    //   50: aload #14
    //   52: aload #15
    //   54: invokevirtual getId : ()I
    //   57: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   60: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   63: aload #14
    //   65: bipush #47
    //   67: invokevirtual indexOf : (I)I
    //   70: istore #4
    //   72: aload #14
    //   74: astore #13
    //   76: iload #4
    //   78: iconst_m1
    //   79: if_icmpeq -> 93
    //   82: aload #14
    //   84: iload #4
    //   86: iconst_1
    //   87: iadd
    //   88: invokevirtual substring : (I)Ljava/lang/String;
    //   91: astore #13
    //   93: aload_0
    //   94: aload #15
    //   96: invokevirtual getId : ()I
    //   99: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   102: aload #13
    //   104: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   107: iinc #2, 1
    //   110: goto -> 21
    //   113: iconst_0
    //   114: istore_2
    //   115: iload_2
    //   116: iload #11
    //   118: if_icmpge -> 151
    //   121: aload_0
    //   122: aload_0
    //   123: iload_2
    //   124: invokevirtual getChildAt : (I)Landroid/view/View;
    //   127: invokevirtual getViewWidget : (Landroid/view/View;)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   130: astore #13
    //   132: aload #13
    //   134: ifnonnull -> 140
    //   137: goto -> 145
    //   140: aload #13
    //   142: invokevirtual reset : ()V
    //   145: iinc #2, 1
    //   148: goto -> 115
    //   151: aload_0
    //   152: getfield mConstraintSetId : I
    //   155: iconst_m1
    //   156: if_icmpeq -> 212
    //   159: iconst_0
    //   160: istore_2
    //   161: iload_2
    //   162: iload #11
    //   164: if_icmpge -> 212
    //   167: aload_0
    //   168: iload_2
    //   169: invokevirtual getChildAt : (I)Landroid/view/View;
    //   172: astore #13
    //   174: aload #13
    //   176: invokevirtual getId : ()I
    //   179: aload_0
    //   180: getfield mConstraintSetId : I
    //   183: if_icmpne -> 206
    //   186: aload #13
    //   188: instanceof androidx/constraintlayout/widget/Constraints
    //   191: ifeq -> 206
    //   194: aload_0
    //   195: aload #13
    //   197: checkcast androidx/constraintlayout/widget/Constraints
    //   200: invokevirtual getConstraintSet : ()Landroidx/constraintlayout/widget/ConstraintSet;
    //   203: putfield mConstraintSet : Landroidx/constraintlayout/widget/ConstraintSet;
    //   206: iinc #2, 1
    //   209: goto -> 161
    //   212: aload_0
    //   213: getfield mConstraintSet : Landroidx/constraintlayout/widget/ConstraintSet;
    //   216: astore #13
    //   218: aload #13
    //   220: ifnull -> 229
    //   223: aload #13
    //   225: aload_0
    //   226: invokevirtual applyToInternal : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   229: aload_0
    //   230: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   233: invokevirtual removeAllChildren : ()V
    //   236: aload_0
    //   237: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   240: invokevirtual size : ()I
    //   243: istore #4
    //   245: iload #4
    //   247: ifle -> 279
    //   250: iconst_0
    //   251: istore_2
    //   252: iload_2
    //   253: iload #4
    //   255: if_icmpge -> 279
    //   258: aload_0
    //   259: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   262: iload_2
    //   263: invokevirtual get : (I)Ljava/lang/Object;
    //   266: checkcast androidx/constraintlayout/widget/ConstraintHelper
    //   269: aload_0
    //   270: invokevirtual updatePreLayout : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   273: iinc #2, 1
    //   276: goto -> 252
    //   279: iconst_0
    //   280: istore_2
    //   281: iload_2
    //   282: iload #11
    //   284: if_icmpge -> 317
    //   287: aload_0
    //   288: iload_2
    //   289: invokevirtual getChildAt : (I)Landroid/view/View;
    //   292: astore #13
    //   294: aload #13
    //   296: instanceof androidx/constraintlayout/widget/Placeholder
    //   299: ifeq -> 311
    //   302: aload #13
    //   304: checkcast androidx/constraintlayout/widget/Placeholder
    //   307: aload_0
    //   308: invokevirtual updatePreLayout : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   311: iinc #2, 1
    //   314: goto -> 281
    //   317: iconst_0
    //   318: istore #5
    //   320: iload_3
    //   321: istore_2
    //   322: iload #5
    //   324: iload #11
    //   326: if_icmpge -> 1999
    //   329: aload_0
    //   330: iload #5
    //   332: invokevirtual getChildAt : (I)Landroid/view/View;
    //   335: astore #15
    //   337: aload_0
    //   338: aload #15
    //   340: invokevirtual getViewWidget : (Landroid/view/View;)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   343: astore #14
    //   345: aload #14
    //   347: ifnonnull -> 356
    //   350: iload_2
    //   351: istore #4
    //   353: goto -> 1990
    //   356: aload #15
    //   358: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   361: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   364: astore #13
    //   366: aload #13
    //   368: invokevirtual validate : ()V
    //   371: aload #13
    //   373: getfield helped : Z
    //   376: ifeq -> 388
    //   379: aload #13
    //   381: iload_2
    //   382: putfield helped : Z
    //   385: goto -> 453
    //   388: iload #12
    //   390: ifeq -> 453
    //   393: aload_0
    //   394: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   397: aload #15
    //   399: invokevirtual getId : ()I
    //   402: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   405: astore #16
    //   407: aload_0
    //   408: iload_2
    //   409: aload #16
    //   411: aload #15
    //   413: invokevirtual getId : ()I
    //   416: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   419: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   422: aload #16
    //   424: aload #16
    //   426: ldc_w 'id/'
    //   429: invokevirtual indexOf : (Ljava/lang/String;)I
    //   432: iconst_3
    //   433: iadd
    //   434: invokevirtual substring : (I)Ljava/lang/String;
    //   437: astore #16
    //   439: aload_0
    //   440: aload #15
    //   442: invokevirtual getId : ()I
    //   445: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   448: aload #16
    //   450: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   453: aload #14
    //   455: aload #15
    //   457: invokevirtual getVisibility : ()I
    //   460: invokevirtual setVisibility : (I)V
    //   463: aload #13
    //   465: getfield isInPlaceholder : Z
    //   468: ifeq -> 478
    //   471: aload #14
    //   473: bipush #8
    //   475: invokevirtual setVisibility : (I)V
    //   478: aload #14
    //   480: aload #15
    //   482: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   485: aload_0
    //   486: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   489: aload #14
    //   491: invokevirtual add : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;)V
    //   494: aload #13
    //   496: getfield verticalDimensionFixed : Z
    //   499: ifeq -> 510
    //   502: aload #13
    //   504: getfield horizontalDimensionFixed : Z
    //   507: ifne -> 520
    //   510: aload_0
    //   511: getfield mVariableDimensionsWidgets : Ljava/util/ArrayList;
    //   514: aload #14
    //   516: invokevirtual add : (Ljava/lang/Object;)Z
    //   519: pop
    //   520: aload #13
    //   522: getfield isGuideline : Z
    //   525: ifeq -> 640
    //   528: aload #14
    //   530: checkcast androidx/constraintlayout/solver/widgets/Guideline
    //   533: astore #14
    //   535: aload #13
    //   537: getfield resolvedGuideBegin : I
    //   540: istore #4
    //   542: aload #13
    //   544: getfield resolvedGuideEnd : I
    //   547: istore_3
    //   548: aload #13
    //   550: getfield resolvedGuidePercent : F
    //   553: fstore_1
    //   554: getstatic android/os/Build$VERSION.SDK_INT : I
    //   557: bipush #17
    //   559: if_icmpge -> 581
    //   562: aload #13
    //   564: getfield guideBegin : I
    //   567: istore #4
    //   569: aload #13
    //   571: getfield guideEnd : I
    //   574: istore_3
    //   575: aload #13
    //   577: getfield guidePercent : F
    //   580: fstore_1
    //   581: fload_1
    //   582: ldc_w -1.0
    //   585: fcmpl
    //   586: ifeq -> 601
    //   589: aload #14
    //   591: fload_1
    //   592: invokevirtual setGuidePercent : (F)V
    //   595: iload_2
    //   596: istore #4
    //   598: goto -> 1990
    //   601: iload #4
    //   603: iconst_m1
    //   604: if_icmpeq -> 620
    //   607: aload #14
    //   609: iload #4
    //   611: invokevirtual setGuideBegin : (I)V
    //   614: iload_2
    //   615: istore #4
    //   617: goto -> 1990
    //   620: iload_2
    //   621: istore #4
    //   623: iload_3
    //   624: iconst_m1
    //   625: if_icmpeq -> 1990
    //   628: aload #14
    //   630: iload_3
    //   631: invokevirtual setGuideEnd : (I)V
    //   634: iload_2
    //   635: istore #4
    //   637: goto -> 1990
    //   640: aload #13
    //   642: getfield leftToLeft : I
    //   645: iconst_m1
    //   646: if_icmpne -> 805
    //   649: aload #13
    //   651: getfield leftToRight : I
    //   654: iconst_m1
    //   655: if_icmpne -> 805
    //   658: aload #13
    //   660: getfield rightToLeft : I
    //   663: iconst_m1
    //   664: if_icmpne -> 805
    //   667: aload #13
    //   669: getfield rightToRight : I
    //   672: iconst_m1
    //   673: if_icmpne -> 805
    //   676: aload #13
    //   678: getfield startToStart : I
    //   681: iconst_m1
    //   682: if_icmpne -> 805
    //   685: aload #13
    //   687: getfield startToEnd : I
    //   690: iconst_m1
    //   691: if_icmpne -> 805
    //   694: aload #13
    //   696: getfield endToStart : I
    //   699: iconst_m1
    //   700: if_icmpne -> 805
    //   703: aload #13
    //   705: getfield endToEnd : I
    //   708: iconst_m1
    //   709: if_icmpne -> 805
    //   712: aload #13
    //   714: getfield topToTop : I
    //   717: iconst_m1
    //   718: if_icmpne -> 805
    //   721: aload #13
    //   723: getfield topToBottom : I
    //   726: iconst_m1
    //   727: if_icmpne -> 805
    //   730: aload #13
    //   732: getfield bottomToTop : I
    //   735: iconst_m1
    //   736: if_icmpne -> 805
    //   739: aload #13
    //   741: getfield bottomToBottom : I
    //   744: iconst_m1
    //   745: if_icmpne -> 805
    //   748: aload #13
    //   750: getfield baselineToBaseline : I
    //   753: iconst_m1
    //   754: if_icmpne -> 805
    //   757: aload #13
    //   759: getfield editorAbsoluteX : I
    //   762: iconst_m1
    //   763: if_icmpne -> 805
    //   766: aload #13
    //   768: getfield editorAbsoluteY : I
    //   771: iconst_m1
    //   772: if_icmpne -> 805
    //   775: aload #13
    //   777: getfield circleConstraint : I
    //   780: iconst_m1
    //   781: if_icmpne -> 805
    //   784: aload #13
    //   786: getfield width : I
    //   789: iconst_m1
    //   790: if_icmpeq -> 805
    //   793: iload_2
    //   794: istore #4
    //   796: aload #13
    //   798: getfield height : I
    //   801: iconst_m1
    //   802: if_icmpne -> 1990
    //   805: aload #13
    //   807: getfield resolvedLeftToLeft : I
    //   810: istore #6
    //   812: aload #13
    //   814: getfield resolvedLeftToRight : I
    //   817: istore #4
    //   819: aload #13
    //   821: getfield resolvedRightToLeft : I
    //   824: istore_2
    //   825: aload #13
    //   827: getfield resolvedRightToRight : I
    //   830: istore_3
    //   831: aload #13
    //   833: getfield resolveGoneLeftMargin : I
    //   836: istore #8
    //   838: aload #13
    //   840: getfield resolveGoneRightMargin : I
    //   843: istore #7
    //   845: aload #13
    //   847: getfield resolvedHorizontalBias : F
    //   850: fstore_1
    //   851: getstatic android/os/Build$VERSION.SDK_INT : I
    //   854: bipush #17
    //   856: if_icmpge -> 1066
    //   859: aload #13
    //   861: getfield leftToLeft : I
    //   864: istore #4
    //   866: aload #13
    //   868: getfield leftToRight : I
    //   871: istore #6
    //   873: aload #13
    //   875: getfield rightToLeft : I
    //   878: istore #10
    //   880: aload #13
    //   882: getfield rightToRight : I
    //   885: istore #9
    //   887: aload #13
    //   889: getfield goneLeftMargin : I
    //   892: istore #8
    //   894: aload #13
    //   896: getfield goneRightMargin : I
    //   899: istore #7
    //   901: aload #13
    //   903: getfield horizontalBias : F
    //   906: fstore_1
    //   907: iload #4
    //   909: istore_3
    //   910: iload #6
    //   912: istore_2
    //   913: iload #4
    //   915: iconst_m1
    //   916: if_icmpne -> 976
    //   919: iload #4
    //   921: istore_3
    //   922: iload #6
    //   924: istore_2
    //   925: iload #6
    //   927: iconst_m1
    //   928: if_icmpne -> 976
    //   931: aload #13
    //   933: getfield startToStart : I
    //   936: iconst_m1
    //   937: if_icmpeq -> 952
    //   940: aload #13
    //   942: getfield startToStart : I
    //   945: istore_3
    //   946: iload #6
    //   948: istore_2
    //   949: goto -> 976
    //   952: iload #4
    //   954: istore_3
    //   955: iload #6
    //   957: istore_2
    //   958: aload #13
    //   960: getfield startToEnd : I
    //   963: iconst_m1
    //   964: if_icmpeq -> 976
    //   967: aload #13
    //   969: getfield startToEnd : I
    //   972: istore_2
    //   973: iload #4
    //   975: istore_3
    //   976: iload_3
    //   977: istore #6
    //   979: iload #10
    //   981: istore #4
    //   983: iload #9
    //   985: istore_3
    //   986: iload #10
    //   988: iconst_m1
    //   989: if_icmpne -> 1053
    //   992: iload #10
    //   994: istore #4
    //   996: iload #9
    //   998: istore_3
    //   999: iload #9
    //   1001: iconst_m1
    //   1002: if_icmpne -> 1053
    //   1005: aload #13
    //   1007: getfield endToStart : I
    //   1010: iconst_m1
    //   1011: if_icmpeq -> 1027
    //   1014: aload #13
    //   1016: getfield endToStart : I
    //   1019: istore #4
    //   1021: iload #9
    //   1023: istore_3
    //   1024: goto -> 1053
    //   1027: iload #10
    //   1029: istore #4
    //   1031: iload #9
    //   1033: istore_3
    //   1034: aload #13
    //   1036: getfield endToEnd : I
    //   1039: iconst_m1
    //   1040: if_icmpeq -> 1053
    //   1043: aload #13
    //   1045: getfield endToEnd : I
    //   1048: istore_3
    //   1049: iload #10
    //   1051: istore #4
    //   1053: iload_2
    //   1054: istore #9
    //   1056: iload #4
    //   1058: istore_2
    //   1059: iload #9
    //   1061: istore #4
    //   1063: goto -> 1066
    //   1066: aload #13
    //   1068: getfield circleConstraint : I
    //   1071: iconst_m1
    //   1072: if_icmpeq -> 1111
    //   1075: aload_0
    //   1076: aload #13
    //   1078: getfield circleConstraint : I
    //   1081: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1084: astore #15
    //   1086: aload #15
    //   1088: ifnull -> 1651
    //   1091: aload #14
    //   1093: aload #15
    //   1095: aload #13
    //   1097: getfield circleAngle : F
    //   1100: aload #13
    //   1102: getfield circleRadius : I
    //   1105: invokevirtual connectCircularConstraint : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;FI)V
    //   1108: goto -> 1651
    //   1111: iload #6
    //   1113: iconst_m1
    //   1114: if_icmpeq -> 1156
    //   1117: aload_0
    //   1118: iload #6
    //   1120: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1123: astore #15
    //   1125: aload #15
    //   1127: ifnull -> 1153
    //   1130: aload #14
    //   1132: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1135: aload #15
    //   1137: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1140: aload #13
    //   1142: getfield leftMargin : I
    //   1145: iload #8
    //   1147: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1150: goto -> 1195
    //   1153: goto -> 1195
    //   1156: iload #4
    //   1158: iconst_m1
    //   1159: if_icmpeq -> 1195
    //   1162: aload_0
    //   1163: iload #4
    //   1165: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1168: astore #15
    //   1170: aload #15
    //   1172: ifnull -> 1195
    //   1175: aload #14
    //   1177: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1180: aload #15
    //   1182: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1185: aload #13
    //   1187: getfield leftMargin : I
    //   1190: iload #8
    //   1192: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1195: iload_2
    //   1196: iconst_m1
    //   1197: if_icmpeq -> 1235
    //   1200: aload_0
    //   1201: iload_2
    //   1202: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1205: astore #15
    //   1207: aload #15
    //   1209: ifnull -> 1272
    //   1212: aload #14
    //   1214: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1217: aload #15
    //   1219: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1222: aload #13
    //   1224: getfield rightMargin : I
    //   1227: iload #7
    //   1229: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1232: goto -> 1272
    //   1235: iload_3
    //   1236: iconst_m1
    //   1237: if_icmpeq -> 1272
    //   1240: aload_0
    //   1241: iload_3
    //   1242: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1245: astore #15
    //   1247: aload #15
    //   1249: ifnull -> 1272
    //   1252: aload #14
    //   1254: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1257: aload #15
    //   1259: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1262: aload #13
    //   1264: getfield rightMargin : I
    //   1267: iload #7
    //   1269: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1272: aload #13
    //   1274: getfield topToTop : I
    //   1277: iconst_m1
    //   1278: if_icmpeq -> 1323
    //   1281: aload_0
    //   1282: aload #13
    //   1284: getfield topToTop : I
    //   1287: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1290: astore #15
    //   1292: aload #15
    //   1294: ifnull -> 1371
    //   1297: aload #14
    //   1299: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1302: aload #15
    //   1304: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1307: aload #13
    //   1309: getfield topMargin : I
    //   1312: aload #13
    //   1314: getfield goneTopMargin : I
    //   1317: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1320: goto -> 1371
    //   1323: aload #13
    //   1325: getfield topToBottom : I
    //   1328: iconst_m1
    //   1329: if_icmpeq -> 1371
    //   1332: aload_0
    //   1333: aload #13
    //   1335: getfield topToBottom : I
    //   1338: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1341: astore #15
    //   1343: aload #15
    //   1345: ifnull -> 1371
    //   1348: aload #14
    //   1350: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1353: aload #15
    //   1355: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1358: aload #13
    //   1360: getfield topMargin : I
    //   1363: aload #13
    //   1365: getfield goneTopMargin : I
    //   1368: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1371: aload #13
    //   1373: getfield bottomToTop : I
    //   1376: iconst_m1
    //   1377: if_icmpeq -> 1422
    //   1380: aload_0
    //   1381: aload #13
    //   1383: getfield bottomToTop : I
    //   1386: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1389: astore #15
    //   1391: aload #15
    //   1393: ifnull -> 1470
    //   1396: aload #14
    //   1398: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1401: aload #15
    //   1403: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1406: aload #13
    //   1408: getfield bottomMargin : I
    //   1411: aload #13
    //   1413: getfield goneBottomMargin : I
    //   1416: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1419: goto -> 1470
    //   1422: aload #13
    //   1424: getfield bottomToBottom : I
    //   1427: iconst_m1
    //   1428: if_icmpeq -> 1470
    //   1431: aload_0
    //   1432: aload #13
    //   1434: getfield bottomToBottom : I
    //   1437: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1440: astore #15
    //   1442: aload #15
    //   1444: ifnull -> 1470
    //   1447: aload #14
    //   1449: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1452: aload #15
    //   1454: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1457: aload #13
    //   1459: getfield bottomMargin : I
    //   1462: aload #13
    //   1464: getfield goneBottomMargin : I
    //   1467: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1470: aload #13
    //   1472: getfield baselineToBaseline : I
    //   1475: iconst_m1
    //   1476: if_icmpeq -> 1599
    //   1479: aload_0
    //   1480: getfield mChildrenByIds : Landroid/util/SparseArray;
    //   1483: aload #13
    //   1485: getfield baselineToBaseline : I
    //   1488: invokevirtual get : (I)Ljava/lang/Object;
    //   1491: checkcast android/view/View
    //   1494: astore #16
    //   1496: aload_0
    //   1497: aload #13
    //   1499: getfield baselineToBaseline : I
    //   1502: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1505: astore #15
    //   1507: aload #15
    //   1509: ifnull -> 1599
    //   1512: aload #16
    //   1514: ifnull -> 1599
    //   1517: aload #16
    //   1519: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1522: instanceof androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   1525: ifeq -> 1599
    //   1528: aload #16
    //   1530: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1533: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   1536: astore #16
    //   1538: aload #13
    //   1540: iconst_1
    //   1541: putfield needsBaseline : Z
    //   1544: aload #16
    //   1546: iconst_1
    //   1547: putfield needsBaseline : Z
    //   1550: aload #14
    //   1552: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1555: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1558: aload #15
    //   1560: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1563: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1566: iconst_0
    //   1567: iconst_m1
    //   1568: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength.STRONG : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength;
    //   1571: iconst_0
    //   1572: iconst_1
    //   1573: invokevirtual connect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IILandroidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength;IZ)Z
    //   1576: pop
    //   1577: aload #14
    //   1579: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1582: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1585: invokevirtual reset : ()V
    //   1588: aload #14
    //   1590: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1593: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1596: invokevirtual reset : ()V
    //   1599: fload_1
    //   1600: fconst_0
    //   1601: fcmpl
    //   1602: iflt -> 1619
    //   1605: fload_1
    //   1606: ldc_w 0.5
    //   1609: fcmpl
    //   1610: ifeq -> 1619
    //   1613: aload #14
    //   1615: fload_1
    //   1616: invokevirtual setHorizontalBiasPercent : (F)V
    //   1619: aload #13
    //   1621: getfield verticalBias : F
    //   1624: fconst_0
    //   1625: fcmpl
    //   1626: iflt -> 1651
    //   1629: aload #13
    //   1631: getfield verticalBias : F
    //   1634: ldc_w 0.5
    //   1637: fcmpl
    //   1638: ifeq -> 1651
    //   1641: aload #14
    //   1643: aload #13
    //   1645: getfield verticalBias : F
    //   1648: invokevirtual setVerticalBiasPercent : (F)V
    //   1651: iload #12
    //   1653: ifeq -> 1689
    //   1656: aload #13
    //   1658: getfield editorAbsoluteX : I
    //   1661: iconst_m1
    //   1662: if_icmpne -> 1674
    //   1665: aload #13
    //   1667: getfield editorAbsoluteY : I
    //   1670: iconst_m1
    //   1671: if_icmpeq -> 1689
    //   1674: aload #14
    //   1676: aload #13
    //   1678: getfield editorAbsoluteX : I
    //   1681: aload #13
    //   1683: getfield editorAbsoluteY : I
    //   1686: invokevirtual setOrigin : (II)V
    //   1689: aload #13
    //   1691: getfield horizontalDimensionFixed : Z
    //   1694: ifne -> 1766
    //   1697: aload #13
    //   1699: getfield width : I
    //   1702: iconst_m1
    //   1703: if_icmpne -> 1749
    //   1706: aload #14
    //   1708: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1711: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1714: aload #14
    //   1716: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1719: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1722: aload #13
    //   1724: getfield leftMargin : I
    //   1727: putfield mMargin : I
    //   1730: aload #14
    //   1732: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1735: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1738: aload #13
    //   1740: getfield rightMargin : I
    //   1743: putfield mMargin : I
    //   1746: goto -> 1784
    //   1749: aload #14
    //   1751: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1754: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1757: aload #14
    //   1759: iconst_0
    //   1760: invokevirtual setWidth : (I)V
    //   1763: goto -> 1784
    //   1766: aload #14
    //   1768: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1771: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1774: aload #14
    //   1776: aload #13
    //   1778: getfield width : I
    //   1781: invokevirtual setWidth : (I)V
    //   1784: aload #13
    //   1786: getfield verticalDimensionFixed : Z
    //   1789: ifne -> 1861
    //   1792: aload #13
    //   1794: getfield height : I
    //   1797: iconst_m1
    //   1798: if_icmpne -> 1844
    //   1801: aload #14
    //   1803: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1806: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1809: aload #14
    //   1811: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1814: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1817: aload #13
    //   1819: getfield topMargin : I
    //   1822: putfield mMargin : I
    //   1825: aload #14
    //   1827: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1830: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1833: aload #13
    //   1835: getfield bottomMargin : I
    //   1838: putfield mMargin : I
    //   1841: goto -> 1879
    //   1844: aload #14
    //   1846: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1849: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1852: aload #14
    //   1854: iconst_0
    //   1855: invokevirtual setHeight : (I)V
    //   1858: goto -> 1879
    //   1861: aload #14
    //   1863: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1866: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1869: aload #14
    //   1871: aload #13
    //   1873: getfield height : I
    //   1876: invokevirtual setHeight : (I)V
    //   1879: iconst_0
    //   1880: istore #4
    //   1882: aload #13
    //   1884: getfield dimensionRatio : Ljava/lang/String;
    //   1887: ifnull -> 1900
    //   1890: aload #14
    //   1892: aload #13
    //   1894: getfield dimensionRatio : Ljava/lang/String;
    //   1897: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   1900: aload #14
    //   1902: aload #13
    //   1904: getfield horizontalWeight : F
    //   1907: invokevirtual setHorizontalWeight : (F)V
    //   1910: aload #14
    //   1912: aload #13
    //   1914: getfield verticalWeight : F
    //   1917: invokevirtual setVerticalWeight : (F)V
    //   1920: aload #14
    //   1922: aload #13
    //   1924: getfield horizontalChainStyle : I
    //   1927: invokevirtual setHorizontalChainStyle : (I)V
    //   1930: aload #14
    //   1932: aload #13
    //   1934: getfield verticalChainStyle : I
    //   1937: invokevirtual setVerticalChainStyle : (I)V
    //   1940: aload #14
    //   1942: aload #13
    //   1944: getfield matchConstraintDefaultWidth : I
    //   1947: aload #13
    //   1949: getfield matchConstraintMinWidth : I
    //   1952: aload #13
    //   1954: getfield matchConstraintMaxWidth : I
    //   1957: aload #13
    //   1959: getfield matchConstraintPercentWidth : F
    //   1962: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   1965: aload #14
    //   1967: aload #13
    //   1969: getfield matchConstraintDefaultHeight : I
    //   1972: aload #13
    //   1974: getfield matchConstraintMinHeight : I
    //   1977: aload #13
    //   1979: getfield matchConstraintMaxHeight : I
    //   1982: aload #13
    //   1984: getfield matchConstraintPercentHeight : F
    //   1987: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   1990: iinc #5, 1
    //   1993: iload #4
    //   1995: istore_2
    //   1996: goto -> 322
    //   1999: return
    //   2000: astore #13
    //   2002: goto -> 107
    //   2005: astore #16
    //   2007: goto -> 453
    // Exception table:
    //   from	to	target	type
    //   34	72	2000	android/content/res/Resources$NotFoundException
    //   82	93	2000	android/content/res/Resources$NotFoundException
    //   93	107	2000	android/content/res/Resources$NotFoundException
    //   393	453	2005	android/content/res/Resources$NotFoundException
  }
  
  private void setSelfDimensionBehaviour(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore #6
    //   6: iload_1
    //   7: invokestatic getSize : (I)I
    //   10: istore_1
    //   11: iload_2
    //   12: invokestatic getMode : (I)I
    //   15: istore_3
    //   16: iload_2
    //   17: invokestatic getSize : (I)I
    //   20: istore_2
    //   21: aload_0
    //   22: invokevirtual getPaddingTop : ()I
    //   25: istore #4
    //   27: aload_0
    //   28: invokevirtual getPaddingBottom : ()I
    //   31: istore #5
    //   33: aload_0
    //   34: invokevirtual getPaddingLeft : ()I
    //   37: istore #7
    //   39: aload_0
    //   40: invokevirtual getPaddingRight : ()I
    //   43: istore #8
    //   45: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   48: astore #9
    //   50: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   53: astore #10
    //   55: aload_0
    //   56: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   59: pop
    //   60: iload #6
    //   62: ldc_w -2147483648
    //   65: if_icmpeq -> 112
    //   68: iload #6
    //   70: ifeq -> 104
    //   73: iload #6
    //   75: ldc_w 1073741824
    //   78: if_icmpeq -> 86
    //   81: iconst_0
    //   82: istore_1
    //   83: goto -> 117
    //   86: aload_0
    //   87: getfield mMaxWidth : I
    //   90: iload_1
    //   91: invokestatic min : (II)I
    //   94: iload #7
    //   96: iload #8
    //   98: iadd
    //   99: isub
    //   100: istore_1
    //   101: goto -> 117
    //   104: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   107: astore #9
    //   109: goto -> 81
    //   112: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   115: astore #9
    //   117: iload_3
    //   118: ldc_w -2147483648
    //   121: if_icmpeq -> 166
    //   124: iload_3
    //   125: ifeq -> 158
    //   128: iload_3
    //   129: ldc_w 1073741824
    //   132: if_icmpeq -> 140
    //   135: iconst_0
    //   136: istore_2
    //   137: goto -> 171
    //   140: aload_0
    //   141: getfield mMaxHeight : I
    //   144: iload_2
    //   145: invokestatic min : (II)I
    //   148: iload #4
    //   150: iload #5
    //   152: iadd
    //   153: isub
    //   154: istore_2
    //   155: goto -> 171
    //   158: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   161: astore #10
    //   163: goto -> 135
    //   166: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   169: astore #10
    //   171: aload_0
    //   172: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   175: iconst_0
    //   176: invokevirtual setMinWidth : (I)V
    //   179: aload_0
    //   180: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   183: iconst_0
    //   184: invokevirtual setMinHeight : (I)V
    //   187: aload_0
    //   188: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   191: aload #9
    //   193: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   196: aload_0
    //   197: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   200: iload_1
    //   201: invokevirtual setWidth : (I)V
    //   204: aload_0
    //   205: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   208: aload #10
    //   210: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   213: aload_0
    //   214: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   217: iload_2
    //   218: invokevirtual setHeight : (I)V
    //   221: aload_0
    //   222: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   225: aload_0
    //   226: getfield mMinWidth : I
    //   229: aload_0
    //   230: invokevirtual getPaddingLeft : ()I
    //   233: isub
    //   234: aload_0
    //   235: invokevirtual getPaddingRight : ()I
    //   238: isub
    //   239: invokevirtual setMinWidth : (I)V
    //   242: aload_0
    //   243: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   246: aload_0
    //   247: getfield mMinHeight : I
    //   250: aload_0
    //   251: invokevirtual getPaddingTop : ()I
    //   254: isub
    //   255: aload_0
    //   256: invokevirtual getPaddingBottom : ()I
    //   259: isub
    //   260: invokevirtual setMinHeight : (I)V
    //   263: return
  }
  
  private void updateHierarchy() {
    boolean bool1;
    int i = getChildCount();
    boolean bool2 = false;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool1) {
      this.mVariableDimensionsWidgets.clear();
      setChildrenConstraints();
    } 
  }
  
  private void updatePostMeasures() {
    int i = getChildCount();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePostMeasure(this); 
    } 
    i = this.mConstraintHelpers.size();
    if (i > 0)
      for (b = bool; b < i; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePostMeasure(this);  
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (Build.VERSION.SDK_INT < 14)
      onViewAdded(paramView); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dispatchDraw(Canvas paramCanvas) {
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      int i = getChildCount();
      float f1 = getWidth();
      float f2 = getHeight();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            object = ((String)object).split(",");
            if (object.length == 4) {
              int j = Integer.parseInt((String)object[0]);
              int m = Integer.parseInt((String)object[1]);
              int n = Integer.parseInt((String)object[2]);
              int k = Integer.parseInt((String)object[3]);
              j = (int)(j / 1080.0F * f1);
              m = (int)(m / 1920.0F * f2);
              n = (int)(n / 1080.0F * f1);
              k = (int)(k / 1920.0F * f2);
              object = new Paint();
              object.setColor(-65536);
              float f4 = j;
              float f3 = m;
              float f5 = (j + n);
              paramCanvas.drawLine(f4, f3, f5, f3, (Paint)object);
              float f6 = (m + k);
              paramCanvas.drawLine(f5, f3, f5, f6, (Paint)object);
              paramCanvas.drawLine(f5, f6, f4, f6, (Paint)object);
              paramCanvas.drawLine(f4, f6, f4, f3, (Paint)object);
              object.setColor(-16711936);
              paramCanvas.drawLine(f4, f3, f5, f6, (Paint)object);
              paramCanvas.drawLine(f4, f6, f5, f3, (Paint)object);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public Object getDesignInformation(int paramInt, Object paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      paramObject = paramObject;
      HashMap<String, Integer> hashMap = this.mDesignIds;
      if (hashMap != null && hashMap.containsKey(paramObject))
        return this.mDesignIds.get(paramObject); 
    } 
    return null;
  }
  
  public int getMaxHeight() {
    return this.mMaxHeight;
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizationLevel() {
    return this.mLayoutWidget.getOptimizationLevel();
  }
  
  public View getViewById(int paramInt) {
    return (View)this.mChildrenByIds.get(paramInt);
  }
  
  public final ConstraintWidget getViewWidget(View paramView) {
    ConstraintWidget constraintWidget;
    if (paramView == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (paramView == null) {
      paramView = null;
    } else {
      constraintWidget = ((LayoutParams)paramView.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt3 = getChildCount();
    paramBoolean = isInEditMode();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || paramBoolean) && !layoutParams.isInPlaceholder) {
        int i = constraintWidget.getDrawX();
        int j = constraintWidget.getDrawY();
        int k = constraintWidget.getWidth() + i;
        paramInt4 = constraintWidget.getHeight() + j;
        view.layout(i, j, k, paramInt4);
        if (view instanceof Placeholder) {
          View view1 = ((Placeholder)view).getContent();
          if (view1 != null) {
            view1.setVisibility(0);
            view1.layout(i, j, k, paramInt4);
          } 
        } 
      } 
    } 
    paramInt3 = this.mConstraintHelpers.size();
    if (paramInt3 > 0)
      for (paramInt1 = paramInt2; paramInt1 < paramInt3; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i;
    boolean bool;
    System.currentTimeMillis();
    int m = View.MeasureSpec.getMode(paramInt1);
    int i2 = View.MeasureSpec.getSize(paramInt1);
    int i1 = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    int j = getPaddingLeft();
    int k = getPaddingTop();
    this.mLayoutWidget.setX(j);
    this.mLayoutWidget.setY(k);
    this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
    this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
    if (Build.VERSION.SDK_INT >= 17) {
      boolean bool1;
      ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
      if (getLayoutDirection() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      constraintWidgetContainer.setRtl(bool1);
    } 
    setSelfDimensionBehaviour(paramInt1, paramInt2);
    int i5 = this.mLayoutWidget.getWidth();
    int i4 = this.mLayoutWidget.getHeight();
    if (this.mDirtyHierarchy) {
      this.mDirtyHierarchy = false;
      updateHierarchy();
      i = 1;
    } else {
      i = 0;
    } 
    if ((this.mOptimizationLevel & 0x8) == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.mLayoutWidget.preOptimize();
      this.mLayoutWidget.optimizeForDimensions(i5, i4);
      internalMeasureDimensions(paramInt1, paramInt2);
    } else {
      internalMeasureChildren(paramInt1, paramInt2);
    } 
    updatePostMeasures();
    if (getChildCount() > 0 && i)
      Analyzer.determineGroups(this.mLayoutWidget); 
    if (this.mLayoutWidget.mGroupsWrapOptimized) {
      if (this.mLayoutWidget.mHorizontalWrapOptimized && m == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedWidth < i2) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setWidth(constraintWidgetContainer.mWrapFixedWidth);
        } 
        this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
      if (this.mLayoutWidget.mVerticalWrapOptimized && i1 == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedHeight < n) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setHeight(constraintWidgetContainer.mWrapFixedHeight);
        } 
        this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
    } 
    if ((this.mOptimizationLevel & 0x20) == 32) {
      int i7 = this.mLayoutWidget.getWidth();
      i = this.mLayoutWidget.getHeight();
      if (this.mLastMeasureWidth != i7 && m == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i7); 
      if (this.mLastMeasureHeight != i && i1 == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i); 
      if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > i2)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i2); 
      if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > n)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, n); 
    } 
    if (getChildCount() > 0)
      solveLinearSystem("First pass"); 
    int i3 = this.mVariableDimensionsWidgets.size();
    n = k + getPaddingBottom();
    int i6 = j + getPaddingRight();
    if (i3 > 0) {
      boolean bool1;
      if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        i2 = 1;
      } else {
        i2 = 0;
      } 
      if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      k = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
      j = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
      byte b = 0;
      m = 0;
      i = 0;
      while (b < i3) {
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(b);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (!layoutParams.isHelper && !layoutParams.isGuideline) {
            int i7 = view.getVisibility();
            i1 = m;
            if (i7 != 8 && (!bool || !constraintWidget.getResolutionWidth().isResolved() || !constraintWidget.getResolutionHeight().isResolved())) {
              if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                m = getChildMeasureSpec(paramInt1, i6, layoutParams.width);
              } else {
                m = View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
              } 
              if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                i7 = getChildMeasureSpec(paramInt2, n, layoutParams.height);
              } else {
                i7 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
              } 
              view.measure(m, i7);
              Metrics metrics = this.mMetrics;
              if (metrics != null)
                metrics.additionalMeasures++; 
              int i8 = view.getMeasuredWidth();
              i7 = view.getMeasuredHeight();
              m = k;
              if (i8 != constraintWidget.getWidth()) {
                constraintWidget.setWidth(i8);
                if (bool)
                  constraintWidget.getResolutionWidth().resolve(i8); 
                m = k;
                if (i2 != 0) {
                  m = k;
                  if (constraintWidget.getRight() > k)
                    m = Math.max(k, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                } 
                i1 = 1;
              } 
              if (i7 != constraintWidget.getHeight()) {
                constraintWidget.setHeight(i7);
                if (bool)
                  constraintWidget.getResolutionHeight().resolve(i7); 
                k = j;
                if (bool1) {
                  k = j;
                  if (constraintWidget.getBottom() > j)
                    k = Math.max(j, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                } 
                j = k;
                i1 = 1;
              } 
              k = i1;
              if (layoutParams.needsBaseline) {
                i7 = view.getBaseline();
                k = i1;
                if (i7 != -1) {
                  k = i1;
                  if (i7 != constraintWidget.getBaselineDistance()) {
                    constraintWidget.setBaselineDistance(i7);
                    k = 1;
                  } 
                } 
              } 
              if (Build.VERSION.SDK_INT >= 11)
                i = combineMeasuredStates(i, view.getMeasuredState()); 
              i1 = k;
              k = m;
              m = i1;
            } 
          } 
        } 
        b++;
      } 
      if (m != 0) {
        this.mLayoutWidget.setWidth(i5);
        this.mLayoutWidget.setHeight(i4);
        if (bool)
          this.mLayoutWidget.solveGraph(); 
        solveLinearSystem("2nd pass");
        if (this.mLayoutWidget.getWidth() < k) {
          this.mLayoutWidget.setWidth(k);
          k = 1;
        } else {
          k = 0;
        } 
        if (this.mLayoutWidget.getHeight() < j) {
          this.mLayoutWidget.setHeight(j);
          j = 1;
        } else {
          j = k;
        } 
        if (j != 0)
          solveLinearSystem("3rd pass"); 
      } 
      for (j = 0; j < i3; j++) {
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(j);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null && (view.getMeasuredWidth() != constraintWidget.getWidth() || view.getMeasuredHeight() != constraintWidget.getHeight()) && constraintWidget.getVisibility() != 8) {
          view.measure(View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824));
          Metrics metrics = this.mMetrics;
          if (metrics != null)
            metrics.additionalMeasures++; 
        } 
      } 
    } else {
      i = 0;
    } 
    j = this.mLayoutWidget.getWidth() + i6;
    k = this.mLayoutWidget.getHeight() + n;
    if (Build.VERSION.SDK_INT >= 11) {
      paramInt1 = resolveSizeAndState(j, paramInt1, i);
      i = resolveSizeAndState(k, paramInt2, i << 16);
      paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
      i = Math.min(this.mMaxHeight, i & 0xFFFFFF);
      paramInt1 = paramInt2;
      if (this.mLayoutWidget.isWidthMeasuredTooSmall())
        paramInt1 = paramInt2 | 0x1000000; 
      paramInt2 = i;
      if (this.mLayoutWidget.isHeightMeasuredTooSmall())
        paramInt2 = i | 0x1000000; 
      setMeasuredDimension(paramInt1, paramInt2);
      this.mLastMeasureWidth = paramInt1;
      this.mLastMeasureHeight = paramInt2;
    } else {
      setMeasuredDimension(j, k);
      this.mLastMeasureWidth = j;
      this.mLastMeasureHeight = k;
    } 
  }
  
  public void onViewAdded(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewAdded(paramView); 
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = (ConstraintWidget)new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewRemoved(paramView); 
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mVariableDimensionsWidgets.remove(constraintWidget);
    this.mDirtyHierarchy = true;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (Build.VERSION.SDK_INT < 14)
      onViewRemoved(paramView); 
  }
  
  public void requestLayout() {
    super.requestLayout();
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) {
    this.mConstraintSet = paramConstraintSet;
  }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap<String, Integer>(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mLayoutWidget.setOptimizationLevel(paramInt);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  protected void solveLinearSystem(String paramString) {
    this.mLayoutWidget.layout();
    Metrics metrics = this.mMetrics;
    if (metrics != null)
      metrics.resolutions++; 
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int END = 7;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public int baselineToBaseline = -1;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String dimensionRatio = null;
    
    int dimensionRatioSide = 1;
    
    float dimensionRatioValue = 0.0F;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBottomMargin = -1;
    
    public int goneEndMargin = -1;
    
    public int goneLeftMargin = -1;
    
    public int goneRightMargin = -1;
    
    public int goneStartMargin = -1;
    
    public int goneTopMargin = -1;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    public boolean helped = false;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    boolean horizontalDimensionFixed = true;
    
    public float horizontalWeight = -1.0F;
    
    boolean isGuideline = false;
    
    boolean isHelper = false;
    
    boolean isInPlaceholder = false;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public int matchConstraintDefaultHeight = 0;
    
    public int matchConstraintDefaultWidth = 0;
    
    public int matchConstraintMaxHeight = 0;
    
    public int matchConstraintMaxWidth = 0;
    
    public int matchConstraintMinHeight = 0;
    
    public int matchConstraintMinWidth = 0;
    
    public float matchConstraintPercentHeight = 1.0F;
    
    public float matchConstraintPercentWidth = 1.0F;
    
    boolean needsBaseline = false;
    
    public int orientation = -1;
    
    int resolveGoneLeftMargin = -1;
    
    int resolveGoneRightMargin = -1;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias = 0.5F;
    
    int resolvedLeftToLeft = -1;
    
    int resolvedLeftToRight = -1;
    
    int resolvedRightToLeft = -1;
    
    int resolvedRightToRight = -1;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    boolean verticalDimensionFixed = true;
    
    public float verticalWeight = -1.0F;
    
    ConstraintWidget widget = new ConstraintWidget();
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: aload_2
      //   3: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
      //   6: aload_0
      //   7: iconst_m1
      //   8: putfield guideBegin : I
      //   11: aload_0
      //   12: iconst_m1
      //   13: putfield guideEnd : I
      //   16: aload_0
      //   17: ldc -1.0
      //   19: putfield guidePercent : F
      //   22: aload_0
      //   23: iconst_m1
      //   24: putfield leftToLeft : I
      //   27: aload_0
      //   28: iconst_m1
      //   29: putfield leftToRight : I
      //   32: aload_0
      //   33: iconst_m1
      //   34: putfield rightToLeft : I
      //   37: aload_0
      //   38: iconst_m1
      //   39: putfield rightToRight : I
      //   42: aload_0
      //   43: iconst_m1
      //   44: putfield topToTop : I
      //   47: aload_0
      //   48: iconst_m1
      //   49: putfield topToBottom : I
      //   52: aload_0
      //   53: iconst_m1
      //   54: putfield bottomToTop : I
      //   57: aload_0
      //   58: iconst_m1
      //   59: putfield bottomToBottom : I
      //   62: aload_0
      //   63: iconst_m1
      //   64: putfield baselineToBaseline : I
      //   67: aload_0
      //   68: iconst_m1
      //   69: putfield circleConstraint : I
      //   72: aload_0
      //   73: iconst_0
      //   74: putfield circleRadius : I
      //   77: aload_0
      //   78: fconst_0
      //   79: putfield circleAngle : F
      //   82: aload_0
      //   83: iconst_m1
      //   84: putfield startToEnd : I
      //   87: aload_0
      //   88: iconst_m1
      //   89: putfield startToStart : I
      //   92: aload_0
      //   93: iconst_m1
      //   94: putfield endToStart : I
      //   97: aload_0
      //   98: iconst_m1
      //   99: putfield endToEnd : I
      //   102: aload_0
      //   103: iconst_m1
      //   104: putfield goneLeftMargin : I
      //   107: aload_0
      //   108: iconst_m1
      //   109: putfield goneTopMargin : I
      //   112: aload_0
      //   113: iconst_m1
      //   114: putfield goneRightMargin : I
      //   117: aload_0
      //   118: iconst_m1
      //   119: putfield goneBottomMargin : I
      //   122: aload_0
      //   123: iconst_m1
      //   124: putfield goneStartMargin : I
      //   127: aload_0
      //   128: iconst_m1
      //   129: putfield goneEndMargin : I
      //   132: aload_0
      //   133: ldc 0.5
      //   135: putfield horizontalBias : F
      //   138: aload_0
      //   139: ldc 0.5
      //   141: putfield verticalBias : F
      //   144: aload_0
      //   145: aconst_null
      //   146: putfield dimensionRatio : Ljava/lang/String;
      //   149: aload_0
      //   150: fconst_0
      //   151: putfield dimensionRatioValue : F
      //   154: aload_0
      //   155: iconst_1
      //   156: putfield dimensionRatioSide : I
      //   159: aload_0
      //   160: ldc -1.0
      //   162: putfield horizontalWeight : F
      //   165: aload_0
      //   166: ldc -1.0
      //   168: putfield verticalWeight : F
      //   171: aload_0
      //   172: iconst_0
      //   173: putfield horizontalChainStyle : I
      //   176: aload_0
      //   177: iconst_0
      //   178: putfield verticalChainStyle : I
      //   181: aload_0
      //   182: iconst_0
      //   183: putfield matchConstraintDefaultWidth : I
      //   186: aload_0
      //   187: iconst_0
      //   188: putfield matchConstraintDefaultHeight : I
      //   191: aload_0
      //   192: iconst_0
      //   193: putfield matchConstraintMinWidth : I
      //   196: aload_0
      //   197: iconst_0
      //   198: putfield matchConstraintMinHeight : I
      //   201: aload_0
      //   202: iconst_0
      //   203: putfield matchConstraintMaxWidth : I
      //   206: aload_0
      //   207: iconst_0
      //   208: putfield matchConstraintMaxHeight : I
      //   211: aload_0
      //   212: fconst_1
      //   213: putfield matchConstraintPercentWidth : F
      //   216: aload_0
      //   217: fconst_1
      //   218: putfield matchConstraintPercentHeight : F
      //   221: aload_0
      //   222: iconst_m1
      //   223: putfield editorAbsoluteX : I
      //   226: aload_0
      //   227: iconst_m1
      //   228: putfield editorAbsoluteY : I
      //   231: aload_0
      //   232: iconst_m1
      //   233: putfield orientation : I
      //   236: aload_0
      //   237: iconst_0
      //   238: putfield constrainedWidth : Z
      //   241: aload_0
      //   242: iconst_0
      //   243: putfield constrainedHeight : Z
      //   246: aload_0
      //   247: iconst_1
      //   248: putfield horizontalDimensionFixed : Z
      //   251: aload_0
      //   252: iconst_1
      //   253: putfield verticalDimensionFixed : Z
      //   256: aload_0
      //   257: iconst_0
      //   258: putfield needsBaseline : Z
      //   261: aload_0
      //   262: iconst_0
      //   263: putfield isGuideline : Z
      //   266: aload_0
      //   267: iconst_0
      //   268: putfield isHelper : Z
      //   271: aload_0
      //   272: iconst_0
      //   273: putfield isInPlaceholder : Z
      //   276: aload_0
      //   277: iconst_m1
      //   278: putfield resolvedLeftToLeft : I
      //   281: aload_0
      //   282: iconst_m1
      //   283: putfield resolvedLeftToRight : I
      //   286: aload_0
      //   287: iconst_m1
      //   288: putfield resolvedRightToLeft : I
      //   291: aload_0
      //   292: iconst_m1
      //   293: putfield resolvedRightToRight : I
      //   296: aload_0
      //   297: iconst_m1
      //   298: putfield resolveGoneLeftMargin : I
      //   301: aload_0
      //   302: iconst_m1
      //   303: putfield resolveGoneRightMargin : I
      //   306: aload_0
      //   307: ldc 0.5
      //   309: putfield resolvedHorizontalBias : F
      //   312: aload_0
      //   313: new androidx/constraintlayout/solver/widgets/ConstraintWidget
      //   316: dup
      //   317: invokespecial <init> : ()V
      //   320: putfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
      //   323: aload_0
      //   324: iconst_0
      //   325: putfield helped : Z
      //   328: aload_1
      //   329: aload_2
      //   330: getstatic androidx/constraintlayout/widget/R$styleable.ConstraintLayout_Layout : [I
      //   333: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
      //   336: astore_1
      //   337: aload_1
      //   338: invokevirtual getIndexCount : ()I
      //   341: istore #7
      //   343: iconst_0
      //   344: istore #5
      //   346: iload #5
      //   348: iload #7
      //   350: if_icmpge -> 2059
      //   353: aload_1
      //   354: iload #5
      //   356: invokevirtual getIndex : (I)I
      //   359: istore #6
      //   361: getstatic androidx/constraintlayout/widget/ConstraintLayout$LayoutParams$Table.map : Landroid/util/SparseIntArray;
      //   364: iload #6
      //   366: invokevirtual get : (I)I
      //   369: istore #8
      //   371: iload #8
      //   373: tableswitch default -> 540, 1 -> 2039, 2 -> 2001, 3 -> 1984, 4 -> 1942, 5 -> 1925, 6 -> 1908, 7 -> 1891, 8 -> 1853, 9 -> 1815, 10 -> 1777, 11 -> 1739, 12 -> 1701, 13 -> 1663, 14 -> 1625, 15 -> 1587, 16 -> 1549, 17 -> 1511, 18 -> 1473, 19 -> 1435, 20 -> 1397, 21 -> 1380, 22 -> 1363, 23 -> 1346, 24 -> 1329, 25 -> 1312, 26 -> 1295, 27 -> 1278, 28 -> 1261, 29 -> 1244, 30 -> 1227, 31 -> 1193, 32 -> 1159, 33 -> 1117, 34 -> 1075, 35 -> 1054, 36 -> 1012, 37 -> 970, 38 -> 949
      //   540: iload #8
      //   542: tableswitch default -> 584, 44 -> 683, 45 -> 666, 46 -> 649, 47 -> 635, 48 -> 621, 49 -> 604, 50 -> 587
      //   584: goto -> 2053
      //   587: aload_0
      //   588: aload_1
      //   589: iload #6
      //   591: aload_0
      //   592: getfield editorAbsoluteY : I
      //   595: invokevirtual getDimensionPixelOffset : (II)I
      //   598: putfield editorAbsoluteY : I
      //   601: goto -> 2053
      //   604: aload_0
      //   605: aload_1
      //   606: iload #6
      //   608: aload_0
      //   609: getfield editorAbsoluteX : I
      //   612: invokevirtual getDimensionPixelOffset : (II)I
      //   615: putfield editorAbsoluteX : I
      //   618: goto -> 2053
      //   621: aload_0
      //   622: aload_1
      //   623: iload #6
      //   625: iconst_0
      //   626: invokevirtual getInt : (II)I
      //   629: putfield verticalChainStyle : I
      //   632: goto -> 2053
      //   635: aload_0
      //   636: aload_1
      //   637: iload #6
      //   639: iconst_0
      //   640: invokevirtual getInt : (II)I
      //   643: putfield horizontalChainStyle : I
      //   646: goto -> 2053
      //   649: aload_0
      //   650: aload_1
      //   651: iload #6
      //   653: aload_0
      //   654: getfield verticalWeight : F
      //   657: invokevirtual getFloat : (IF)F
      //   660: putfield verticalWeight : F
      //   663: goto -> 2053
      //   666: aload_0
      //   667: aload_1
      //   668: iload #6
      //   670: aload_0
      //   671: getfield horizontalWeight : F
      //   674: invokevirtual getFloat : (IF)F
      //   677: putfield horizontalWeight : F
      //   680: goto -> 2053
      //   683: aload_1
      //   684: iload #6
      //   686: invokevirtual getString : (I)Ljava/lang/String;
      //   689: astore_2
      //   690: aload_0
      //   691: aload_2
      //   692: putfield dimensionRatio : Ljava/lang/String;
      //   695: aload_0
      //   696: ldc_w NaN
      //   699: putfield dimensionRatioValue : F
      //   702: aload_0
      //   703: iconst_m1
      //   704: putfield dimensionRatioSide : I
      //   707: aload_2
      //   708: ifnull -> 2053
      //   711: aload_2
      //   712: invokevirtual length : ()I
      //   715: istore #8
      //   717: aload_0
      //   718: getfield dimensionRatio : Ljava/lang/String;
      //   721: bipush #44
      //   723: invokevirtual indexOf : (I)I
      //   726: istore #6
      //   728: iload #6
      //   730: ifle -> 792
      //   733: iload #6
      //   735: iload #8
      //   737: iconst_1
      //   738: isub
      //   739: if_icmpge -> 792
      //   742: aload_0
      //   743: getfield dimensionRatio : Ljava/lang/String;
      //   746: iconst_0
      //   747: iload #6
      //   749: invokevirtual substring : (II)Ljava/lang/String;
      //   752: astore_2
      //   753: aload_2
      //   754: ldc_w 'W'
      //   757: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   760: ifeq -> 771
      //   763: aload_0
      //   764: iconst_0
      //   765: putfield dimensionRatioSide : I
      //   768: goto -> 786
      //   771: aload_2
      //   772: ldc_w 'H'
      //   775: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   778: ifeq -> 786
      //   781: aload_0
      //   782: iconst_1
      //   783: putfield dimensionRatioSide : I
      //   786: iinc #6, 1
      //   789: goto -> 795
      //   792: iconst_0
      //   793: istore #6
      //   795: aload_0
      //   796: getfield dimensionRatio : Ljava/lang/String;
      //   799: bipush #58
      //   801: invokevirtual indexOf : (I)I
      //   804: istore #9
      //   806: iload #9
      //   808: iflt -> 921
      //   811: iload #9
      //   813: iload #8
      //   815: iconst_1
      //   816: isub
      //   817: if_icmpge -> 921
      //   820: aload_0
      //   821: getfield dimensionRatio : Ljava/lang/String;
      //   824: iload #6
      //   826: iload #9
      //   828: invokevirtual substring : (II)Ljava/lang/String;
      //   831: astore #10
      //   833: aload_0
      //   834: getfield dimensionRatio : Ljava/lang/String;
      //   837: iload #9
      //   839: iconst_1
      //   840: iadd
      //   841: invokevirtual substring : (I)Ljava/lang/String;
      //   844: astore_2
      //   845: aload #10
      //   847: invokevirtual length : ()I
      //   850: ifle -> 2053
      //   853: aload_2
      //   854: invokevirtual length : ()I
      //   857: ifle -> 2053
      //   860: aload #10
      //   862: invokestatic parseFloat : (Ljava/lang/String;)F
      //   865: fstore_3
      //   866: aload_2
      //   867: invokestatic parseFloat : (Ljava/lang/String;)F
      //   870: fstore #4
      //   872: fload_3
      //   873: fconst_0
      //   874: fcmpl
      //   875: ifle -> 2053
      //   878: fload #4
      //   880: fconst_0
      //   881: fcmpl
      //   882: ifle -> 2053
      //   885: aload_0
      //   886: getfield dimensionRatioSide : I
      //   889: iconst_1
      //   890: if_icmpne -> 907
      //   893: aload_0
      //   894: fload #4
      //   896: fload_3
      //   897: fdiv
      //   898: invokestatic abs : (F)F
      //   901: putfield dimensionRatioValue : F
      //   904: goto -> 2053
      //   907: aload_0
      //   908: fload_3
      //   909: fload #4
      //   911: fdiv
      //   912: invokestatic abs : (F)F
      //   915: putfield dimensionRatioValue : F
      //   918: goto -> 2053
      //   921: aload_0
      //   922: getfield dimensionRatio : Ljava/lang/String;
      //   925: iload #6
      //   927: invokevirtual substring : (I)Ljava/lang/String;
      //   930: astore_2
      //   931: aload_2
      //   932: invokevirtual length : ()I
      //   935: ifle -> 2053
      //   938: aload_0
      //   939: aload_2
      //   940: invokestatic parseFloat : (Ljava/lang/String;)F
      //   943: putfield dimensionRatioValue : F
      //   946: goto -> 2053
      //   949: aload_0
      //   950: fconst_0
      //   951: aload_1
      //   952: iload #6
      //   954: aload_0
      //   955: getfield matchConstraintPercentHeight : F
      //   958: invokevirtual getFloat : (IF)F
      //   961: invokestatic max : (FF)F
      //   964: putfield matchConstraintPercentHeight : F
      //   967: goto -> 2053
      //   970: aload_0
      //   971: aload_1
      //   972: iload #6
      //   974: aload_0
      //   975: getfield matchConstraintMaxHeight : I
      //   978: invokevirtual getDimensionPixelSize : (II)I
      //   981: putfield matchConstraintMaxHeight : I
      //   984: goto -> 2053
      //   987: astore_2
      //   988: aload_1
      //   989: iload #6
      //   991: aload_0
      //   992: getfield matchConstraintMaxHeight : I
      //   995: invokevirtual getInt : (II)I
      //   998: bipush #-2
      //   1000: if_icmpne -> 2053
      //   1003: aload_0
      //   1004: bipush #-2
      //   1006: putfield matchConstraintMaxHeight : I
      //   1009: goto -> 2053
      //   1012: aload_0
      //   1013: aload_1
      //   1014: iload #6
      //   1016: aload_0
      //   1017: getfield matchConstraintMinHeight : I
      //   1020: invokevirtual getDimensionPixelSize : (II)I
      //   1023: putfield matchConstraintMinHeight : I
      //   1026: goto -> 2053
      //   1029: astore_2
      //   1030: aload_1
      //   1031: iload #6
      //   1033: aload_0
      //   1034: getfield matchConstraintMinHeight : I
      //   1037: invokevirtual getInt : (II)I
      //   1040: bipush #-2
      //   1042: if_icmpne -> 2053
      //   1045: aload_0
      //   1046: bipush #-2
      //   1048: putfield matchConstraintMinHeight : I
      //   1051: goto -> 2053
      //   1054: aload_0
      //   1055: fconst_0
      //   1056: aload_1
      //   1057: iload #6
      //   1059: aload_0
      //   1060: getfield matchConstraintPercentWidth : F
      //   1063: invokevirtual getFloat : (IF)F
      //   1066: invokestatic max : (FF)F
      //   1069: putfield matchConstraintPercentWidth : F
      //   1072: goto -> 2053
      //   1075: aload_0
      //   1076: aload_1
      //   1077: iload #6
      //   1079: aload_0
      //   1080: getfield matchConstraintMaxWidth : I
      //   1083: invokevirtual getDimensionPixelSize : (II)I
      //   1086: putfield matchConstraintMaxWidth : I
      //   1089: goto -> 2053
      //   1092: astore_2
      //   1093: aload_1
      //   1094: iload #6
      //   1096: aload_0
      //   1097: getfield matchConstraintMaxWidth : I
      //   1100: invokevirtual getInt : (II)I
      //   1103: bipush #-2
      //   1105: if_icmpne -> 2053
      //   1108: aload_0
      //   1109: bipush #-2
      //   1111: putfield matchConstraintMaxWidth : I
      //   1114: goto -> 2053
      //   1117: aload_0
      //   1118: aload_1
      //   1119: iload #6
      //   1121: aload_0
      //   1122: getfield matchConstraintMinWidth : I
      //   1125: invokevirtual getDimensionPixelSize : (II)I
      //   1128: putfield matchConstraintMinWidth : I
      //   1131: goto -> 2053
      //   1134: astore_2
      //   1135: aload_1
      //   1136: iload #6
      //   1138: aload_0
      //   1139: getfield matchConstraintMinWidth : I
      //   1142: invokevirtual getInt : (II)I
      //   1145: bipush #-2
      //   1147: if_icmpne -> 2053
      //   1150: aload_0
      //   1151: bipush #-2
      //   1153: putfield matchConstraintMinWidth : I
      //   1156: goto -> 2053
      //   1159: aload_1
      //   1160: iload #6
      //   1162: iconst_0
      //   1163: invokevirtual getInt : (II)I
      //   1166: istore #6
      //   1168: aload_0
      //   1169: iload #6
      //   1171: putfield matchConstraintDefaultHeight : I
      //   1174: iload #6
      //   1176: iconst_1
      //   1177: if_icmpne -> 2053
      //   1180: ldc_w 'ConstraintLayout'
      //   1183: ldc_w 'layout_constraintHeight_default="wrap" is deprecated.\\nUse layout_height="WRAP_CONTENT" and layout_constrainedHeight="true" instead.'
      //   1186: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1189: pop
      //   1190: goto -> 2053
      //   1193: aload_1
      //   1194: iload #6
      //   1196: iconst_0
      //   1197: invokevirtual getInt : (II)I
      //   1200: istore #6
      //   1202: aload_0
      //   1203: iload #6
      //   1205: putfield matchConstraintDefaultWidth : I
      //   1208: iload #6
      //   1210: iconst_1
      //   1211: if_icmpne -> 2053
      //   1214: ldc_w 'ConstraintLayout'
      //   1217: ldc_w 'layout_constraintWidth_default="wrap" is deprecated.\\nUse layout_width="WRAP_CONTENT" and layout_constrainedWidth="true" instead.'
      //   1220: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1223: pop
      //   1224: goto -> 2053
      //   1227: aload_0
      //   1228: aload_1
      //   1229: iload #6
      //   1231: aload_0
      //   1232: getfield verticalBias : F
      //   1235: invokevirtual getFloat : (IF)F
      //   1238: putfield verticalBias : F
      //   1241: goto -> 2053
      //   1244: aload_0
      //   1245: aload_1
      //   1246: iload #6
      //   1248: aload_0
      //   1249: getfield horizontalBias : F
      //   1252: invokevirtual getFloat : (IF)F
      //   1255: putfield horizontalBias : F
      //   1258: goto -> 2053
      //   1261: aload_0
      //   1262: aload_1
      //   1263: iload #6
      //   1265: aload_0
      //   1266: getfield constrainedHeight : Z
      //   1269: invokevirtual getBoolean : (IZ)Z
      //   1272: putfield constrainedHeight : Z
      //   1275: goto -> 2053
      //   1278: aload_0
      //   1279: aload_1
      //   1280: iload #6
      //   1282: aload_0
      //   1283: getfield constrainedWidth : Z
      //   1286: invokevirtual getBoolean : (IZ)Z
      //   1289: putfield constrainedWidth : Z
      //   1292: goto -> 2053
      //   1295: aload_0
      //   1296: aload_1
      //   1297: iload #6
      //   1299: aload_0
      //   1300: getfield goneEndMargin : I
      //   1303: invokevirtual getDimensionPixelSize : (II)I
      //   1306: putfield goneEndMargin : I
      //   1309: goto -> 2053
      //   1312: aload_0
      //   1313: aload_1
      //   1314: iload #6
      //   1316: aload_0
      //   1317: getfield goneStartMargin : I
      //   1320: invokevirtual getDimensionPixelSize : (II)I
      //   1323: putfield goneStartMargin : I
      //   1326: goto -> 2053
      //   1329: aload_0
      //   1330: aload_1
      //   1331: iload #6
      //   1333: aload_0
      //   1334: getfield goneBottomMargin : I
      //   1337: invokevirtual getDimensionPixelSize : (II)I
      //   1340: putfield goneBottomMargin : I
      //   1343: goto -> 2053
      //   1346: aload_0
      //   1347: aload_1
      //   1348: iload #6
      //   1350: aload_0
      //   1351: getfield goneRightMargin : I
      //   1354: invokevirtual getDimensionPixelSize : (II)I
      //   1357: putfield goneRightMargin : I
      //   1360: goto -> 2053
      //   1363: aload_0
      //   1364: aload_1
      //   1365: iload #6
      //   1367: aload_0
      //   1368: getfield goneTopMargin : I
      //   1371: invokevirtual getDimensionPixelSize : (II)I
      //   1374: putfield goneTopMargin : I
      //   1377: goto -> 2053
      //   1380: aload_0
      //   1381: aload_1
      //   1382: iload #6
      //   1384: aload_0
      //   1385: getfield goneLeftMargin : I
      //   1388: invokevirtual getDimensionPixelSize : (II)I
      //   1391: putfield goneLeftMargin : I
      //   1394: goto -> 2053
      //   1397: aload_1
      //   1398: iload #6
      //   1400: aload_0
      //   1401: getfield endToEnd : I
      //   1404: invokevirtual getResourceId : (II)I
      //   1407: istore #8
      //   1409: aload_0
      //   1410: iload #8
      //   1412: putfield endToEnd : I
      //   1415: iload #8
      //   1417: iconst_m1
      //   1418: if_icmpne -> 2053
      //   1421: aload_0
      //   1422: aload_1
      //   1423: iload #6
      //   1425: iconst_m1
      //   1426: invokevirtual getInt : (II)I
      //   1429: putfield endToEnd : I
      //   1432: goto -> 2053
      //   1435: aload_1
      //   1436: iload #6
      //   1438: aload_0
      //   1439: getfield endToStart : I
      //   1442: invokevirtual getResourceId : (II)I
      //   1445: istore #8
      //   1447: aload_0
      //   1448: iload #8
      //   1450: putfield endToStart : I
      //   1453: iload #8
      //   1455: iconst_m1
      //   1456: if_icmpne -> 2053
      //   1459: aload_0
      //   1460: aload_1
      //   1461: iload #6
      //   1463: iconst_m1
      //   1464: invokevirtual getInt : (II)I
      //   1467: putfield endToStart : I
      //   1470: goto -> 2053
      //   1473: aload_1
      //   1474: iload #6
      //   1476: aload_0
      //   1477: getfield startToStart : I
      //   1480: invokevirtual getResourceId : (II)I
      //   1483: istore #8
      //   1485: aload_0
      //   1486: iload #8
      //   1488: putfield startToStart : I
      //   1491: iload #8
      //   1493: iconst_m1
      //   1494: if_icmpne -> 2053
      //   1497: aload_0
      //   1498: aload_1
      //   1499: iload #6
      //   1501: iconst_m1
      //   1502: invokevirtual getInt : (II)I
      //   1505: putfield startToStart : I
      //   1508: goto -> 2053
      //   1511: aload_1
      //   1512: iload #6
      //   1514: aload_0
      //   1515: getfield startToEnd : I
      //   1518: invokevirtual getResourceId : (II)I
      //   1521: istore #8
      //   1523: aload_0
      //   1524: iload #8
      //   1526: putfield startToEnd : I
      //   1529: iload #8
      //   1531: iconst_m1
      //   1532: if_icmpne -> 2053
      //   1535: aload_0
      //   1536: aload_1
      //   1537: iload #6
      //   1539: iconst_m1
      //   1540: invokevirtual getInt : (II)I
      //   1543: putfield startToEnd : I
      //   1546: goto -> 2053
      //   1549: aload_1
      //   1550: iload #6
      //   1552: aload_0
      //   1553: getfield baselineToBaseline : I
      //   1556: invokevirtual getResourceId : (II)I
      //   1559: istore #8
      //   1561: aload_0
      //   1562: iload #8
      //   1564: putfield baselineToBaseline : I
      //   1567: iload #8
      //   1569: iconst_m1
      //   1570: if_icmpne -> 2053
      //   1573: aload_0
      //   1574: aload_1
      //   1575: iload #6
      //   1577: iconst_m1
      //   1578: invokevirtual getInt : (II)I
      //   1581: putfield baselineToBaseline : I
      //   1584: goto -> 2053
      //   1587: aload_1
      //   1588: iload #6
      //   1590: aload_0
      //   1591: getfield bottomToBottom : I
      //   1594: invokevirtual getResourceId : (II)I
      //   1597: istore #8
      //   1599: aload_0
      //   1600: iload #8
      //   1602: putfield bottomToBottom : I
      //   1605: iload #8
      //   1607: iconst_m1
      //   1608: if_icmpne -> 2053
      //   1611: aload_0
      //   1612: aload_1
      //   1613: iload #6
      //   1615: iconst_m1
      //   1616: invokevirtual getInt : (II)I
      //   1619: putfield bottomToBottom : I
      //   1622: goto -> 2053
      //   1625: aload_1
      //   1626: iload #6
      //   1628: aload_0
      //   1629: getfield bottomToTop : I
      //   1632: invokevirtual getResourceId : (II)I
      //   1635: istore #8
      //   1637: aload_0
      //   1638: iload #8
      //   1640: putfield bottomToTop : I
      //   1643: iload #8
      //   1645: iconst_m1
      //   1646: if_icmpne -> 2053
      //   1649: aload_0
      //   1650: aload_1
      //   1651: iload #6
      //   1653: iconst_m1
      //   1654: invokevirtual getInt : (II)I
      //   1657: putfield bottomToTop : I
      //   1660: goto -> 2053
      //   1663: aload_1
      //   1664: iload #6
      //   1666: aload_0
      //   1667: getfield topToBottom : I
      //   1670: invokevirtual getResourceId : (II)I
      //   1673: istore #8
      //   1675: aload_0
      //   1676: iload #8
      //   1678: putfield topToBottom : I
      //   1681: iload #8
      //   1683: iconst_m1
      //   1684: if_icmpne -> 2053
      //   1687: aload_0
      //   1688: aload_1
      //   1689: iload #6
      //   1691: iconst_m1
      //   1692: invokevirtual getInt : (II)I
      //   1695: putfield topToBottom : I
      //   1698: goto -> 2053
      //   1701: aload_1
      //   1702: iload #6
      //   1704: aload_0
      //   1705: getfield topToTop : I
      //   1708: invokevirtual getResourceId : (II)I
      //   1711: istore #8
      //   1713: aload_0
      //   1714: iload #8
      //   1716: putfield topToTop : I
      //   1719: iload #8
      //   1721: iconst_m1
      //   1722: if_icmpne -> 2053
      //   1725: aload_0
      //   1726: aload_1
      //   1727: iload #6
      //   1729: iconst_m1
      //   1730: invokevirtual getInt : (II)I
      //   1733: putfield topToTop : I
      //   1736: goto -> 2053
      //   1739: aload_1
      //   1740: iload #6
      //   1742: aload_0
      //   1743: getfield rightToRight : I
      //   1746: invokevirtual getResourceId : (II)I
      //   1749: istore #8
      //   1751: aload_0
      //   1752: iload #8
      //   1754: putfield rightToRight : I
      //   1757: iload #8
      //   1759: iconst_m1
      //   1760: if_icmpne -> 2053
      //   1763: aload_0
      //   1764: aload_1
      //   1765: iload #6
      //   1767: iconst_m1
      //   1768: invokevirtual getInt : (II)I
      //   1771: putfield rightToRight : I
      //   1774: goto -> 2053
      //   1777: aload_1
      //   1778: iload #6
      //   1780: aload_0
      //   1781: getfield rightToLeft : I
      //   1784: invokevirtual getResourceId : (II)I
      //   1787: istore #8
      //   1789: aload_0
      //   1790: iload #8
      //   1792: putfield rightToLeft : I
      //   1795: iload #8
      //   1797: iconst_m1
      //   1798: if_icmpne -> 2053
      //   1801: aload_0
      //   1802: aload_1
      //   1803: iload #6
      //   1805: iconst_m1
      //   1806: invokevirtual getInt : (II)I
      //   1809: putfield rightToLeft : I
      //   1812: goto -> 2053
      //   1815: aload_1
      //   1816: iload #6
      //   1818: aload_0
      //   1819: getfield leftToRight : I
      //   1822: invokevirtual getResourceId : (II)I
      //   1825: istore #8
      //   1827: aload_0
      //   1828: iload #8
      //   1830: putfield leftToRight : I
      //   1833: iload #8
      //   1835: iconst_m1
      //   1836: if_icmpne -> 2053
      //   1839: aload_0
      //   1840: aload_1
      //   1841: iload #6
      //   1843: iconst_m1
      //   1844: invokevirtual getInt : (II)I
      //   1847: putfield leftToRight : I
      //   1850: goto -> 2053
      //   1853: aload_1
      //   1854: iload #6
      //   1856: aload_0
      //   1857: getfield leftToLeft : I
      //   1860: invokevirtual getResourceId : (II)I
      //   1863: istore #8
      //   1865: aload_0
      //   1866: iload #8
      //   1868: putfield leftToLeft : I
      //   1871: iload #8
      //   1873: iconst_m1
      //   1874: if_icmpne -> 2053
      //   1877: aload_0
      //   1878: aload_1
      //   1879: iload #6
      //   1881: iconst_m1
      //   1882: invokevirtual getInt : (II)I
      //   1885: putfield leftToLeft : I
      //   1888: goto -> 2053
      //   1891: aload_0
      //   1892: aload_1
      //   1893: iload #6
      //   1895: aload_0
      //   1896: getfield guidePercent : F
      //   1899: invokevirtual getFloat : (IF)F
      //   1902: putfield guidePercent : F
      //   1905: goto -> 2053
      //   1908: aload_0
      //   1909: aload_1
      //   1910: iload #6
      //   1912: aload_0
      //   1913: getfield guideEnd : I
      //   1916: invokevirtual getDimensionPixelOffset : (II)I
      //   1919: putfield guideEnd : I
      //   1922: goto -> 2053
      //   1925: aload_0
      //   1926: aload_1
      //   1927: iload #6
      //   1929: aload_0
      //   1930: getfield guideBegin : I
      //   1933: invokevirtual getDimensionPixelOffset : (II)I
      //   1936: putfield guideBegin : I
      //   1939: goto -> 2053
      //   1942: aload_1
      //   1943: iload #6
      //   1945: aload_0
      //   1946: getfield circleAngle : F
      //   1949: invokevirtual getFloat : (IF)F
      //   1952: ldc_w 360.0
      //   1955: frem
      //   1956: fstore_3
      //   1957: aload_0
      //   1958: fload_3
      //   1959: putfield circleAngle : F
      //   1962: fload_3
      //   1963: fconst_0
      //   1964: fcmpg
      //   1965: ifge -> 2053
      //   1968: aload_0
      //   1969: ldc_w 360.0
      //   1972: fload_3
      //   1973: fsub
      //   1974: ldc_w 360.0
      //   1977: frem
      //   1978: putfield circleAngle : F
      //   1981: goto -> 2053
      //   1984: aload_0
      //   1985: aload_1
      //   1986: iload #6
      //   1988: aload_0
      //   1989: getfield circleRadius : I
      //   1992: invokevirtual getDimensionPixelSize : (II)I
      //   1995: putfield circleRadius : I
      //   1998: goto -> 2053
      //   2001: aload_1
      //   2002: iload #6
      //   2004: aload_0
      //   2005: getfield circleConstraint : I
      //   2008: invokevirtual getResourceId : (II)I
      //   2011: istore #8
      //   2013: aload_0
      //   2014: iload #8
      //   2016: putfield circleConstraint : I
      //   2019: iload #8
      //   2021: iconst_m1
      //   2022: if_icmpne -> 2053
      //   2025: aload_0
      //   2026: aload_1
      //   2027: iload #6
      //   2029: iconst_m1
      //   2030: invokevirtual getInt : (II)I
      //   2033: putfield circleConstraint : I
      //   2036: goto -> 2053
      //   2039: aload_0
      //   2040: aload_1
      //   2041: iload #6
      //   2043: aload_0
      //   2044: getfield orientation : I
      //   2047: invokevirtual getInt : (II)I
      //   2050: putfield orientation : I
      //   2053: iinc #5, 1
      //   2056: goto -> 346
      //   2059: aload_1
      //   2060: invokevirtual recycle : ()V
      //   2063: aload_0
      //   2064: invokevirtual validate : ()V
      //   2067: return
      //   2068: astore_2
      //   2069: goto -> 2053
      // Exception table:
      //   from	to	target	type
      //   860	872	2068	java/lang/NumberFormatException
      //   885	904	2068	java/lang/NumberFormatException
      //   907	918	2068	java/lang/NumberFormatException
      //   938	946	2068	java/lang/NumberFormatException
      //   970	984	987	java/lang/Exception
      //   1012	1026	1029	java/lang/Exception
      //   1075	1089	1092	java/lang/Exception
      //   1117	1131	1134	java/lang/Exception
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.widget = param1LayoutParams.widget;
    }
    
    public void reset() {
      ConstraintWidget constraintWidget = this.widget;
      if (constraintWidget != null)
        constraintWidget.reset(); 
    }
    
    public void resolveLayoutDirection(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield leftMargin : I
      //   4: istore #4
      //   6: aload_0
      //   7: getfield rightMargin : I
      //   10: istore #5
      //   12: aload_0
      //   13: iload_1
      //   14: invokespecial resolveLayoutDirection : (I)V
      //   17: aload_0
      //   18: iconst_m1
      //   19: putfield resolvedRightToLeft : I
      //   22: aload_0
      //   23: iconst_m1
      //   24: putfield resolvedRightToRight : I
      //   27: aload_0
      //   28: iconst_m1
      //   29: putfield resolvedLeftToLeft : I
      //   32: aload_0
      //   33: iconst_m1
      //   34: putfield resolvedLeftToRight : I
      //   37: aload_0
      //   38: iconst_m1
      //   39: putfield resolveGoneLeftMargin : I
      //   42: aload_0
      //   43: iconst_m1
      //   44: putfield resolveGoneRightMargin : I
      //   47: aload_0
      //   48: aload_0
      //   49: getfield goneLeftMargin : I
      //   52: putfield resolveGoneLeftMargin : I
      //   55: aload_0
      //   56: aload_0
      //   57: getfield goneRightMargin : I
      //   60: putfield resolveGoneRightMargin : I
      //   63: aload_0
      //   64: aload_0
      //   65: getfield horizontalBias : F
      //   68: putfield resolvedHorizontalBias : F
      //   71: aload_0
      //   72: aload_0
      //   73: getfield guideBegin : I
      //   76: putfield resolvedGuideBegin : I
      //   79: aload_0
      //   80: aload_0
      //   81: getfield guideEnd : I
      //   84: putfield resolvedGuideEnd : I
      //   87: aload_0
      //   88: aload_0
      //   89: getfield guidePercent : F
      //   92: putfield resolvedGuidePercent : F
      //   95: aload_0
      //   96: invokevirtual getLayoutDirection : ()I
      //   99: istore_1
      //   100: iconst_0
      //   101: istore_3
      //   102: iconst_1
      //   103: iload_1
      //   104: if_icmpne -> 112
      //   107: iconst_1
      //   108: istore_1
      //   109: goto -> 114
      //   112: iconst_0
      //   113: istore_1
      //   114: iload_1
      //   115: ifeq -> 344
      //   118: aload_0
      //   119: getfield startToEnd : I
      //   122: istore_1
      //   123: iload_1
      //   124: iconst_m1
      //   125: if_icmpeq -> 138
      //   128: aload_0
      //   129: iload_1
      //   130: putfield resolvedRightToLeft : I
      //   133: iconst_1
      //   134: istore_1
      //   135: goto -> 161
      //   138: aload_0
      //   139: getfield startToStart : I
      //   142: istore #6
      //   144: iload_3
      //   145: istore_1
      //   146: iload #6
      //   148: iconst_m1
      //   149: if_icmpeq -> 161
      //   152: aload_0
      //   153: iload #6
      //   155: putfield resolvedRightToRight : I
      //   158: goto -> 133
      //   161: aload_0
      //   162: getfield endToStart : I
      //   165: istore_3
      //   166: iload_3
      //   167: iconst_m1
      //   168: if_icmpeq -> 178
      //   171: aload_0
      //   172: iload_3
      //   173: putfield resolvedLeftToRight : I
      //   176: iconst_1
      //   177: istore_1
      //   178: aload_0
      //   179: getfield endToEnd : I
      //   182: istore_3
      //   183: iload_3
      //   184: iconst_m1
      //   185: if_icmpeq -> 195
      //   188: aload_0
      //   189: iload_3
      //   190: putfield resolvedLeftToLeft : I
      //   193: iconst_1
      //   194: istore_1
      //   195: aload_0
      //   196: getfield goneStartMargin : I
      //   199: istore_3
      //   200: iload_3
      //   201: iconst_m1
      //   202: if_icmpeq -> 210
      //   205: aload_0
      //   206: iload_3
      //   207: putfield resolveGoneRightMargin : I
      //   210: aload_0
      //   211: getfield goneEndMargin : I
      //   214: istore_3
      //   215: iload_3
      //   216: iconst_m1
      //   217: if_icmpeq -> 225
      //   220: aload_0
      //   221: iload_3
      //   222: putfield resolveGoneLeftMargin : I
      //   225: iload_1
      //   226: ifeq -> 239
      //   229: aload_0
      //   230: fconst_1
      //   231: aload_0
      //   232: getfield horizontalBias : F
      //   235: fsub
      //   236: putfield resolvedHorizontalBias : F
      //   239: aload_0
      //   240: getfield isGuideline : Z
      //   243: ifeq -> 434
      //   246: aload_0
      //   247: getfield orientation : I
      //   250: iconst_1
      //   251: if_icmpne -> 434
      //   254: aload_0
      //   255: getfield guidePercent : F
      //   258: fstore_2
      //   259: fload_2
      //   260: ldc -1.0
      //   262: fcmpl
      //   263: ifeq -> 286
      //   266: aload_0
      //   267: fconst_1
      //   268: fload_2
      //   269: fsub
      //   270: putfield resolvedGuidePercent : F
      //   273: aload_0
      //   274: iconst_m1
      //   275: putfield resolvedGuideBegin : I
      //   278: aload_0
      //   279: iconst_m1
      //   280: putfield resolvedGuideEnd : I
      //   283: goto -> 434
      //   286: aload_0
      //   287: getfield guideBegin : I
      //   290: istore_1
      //   291: iload_1
      //   292: iconst_m1
      //   293: if_icmpeq -> 315
      //   296: aload_0
      //   297: iload_1
      //   298: putfield resolvedGuideEnd : I
      //   301: aload_0
      //   302: iconst_m1
      //   303: putfield resolvedGuideBegin : I
      //   306: aload_0
      //   307: ldc -1.0
      //   309: putfield resolvedGuidePercent : F
      //   312: goto -> 434
      //   315: aload_0
      //   316: getfield guideEnd : I
      //   319: istore_1
      //   320: iload_1
      //   321: iconst_m1
      //   322: if_icmpeq -> 434
      //   325: aload_0
      //   326: iload_1
      //   327: putfield resolvedGuideBegin : I
      //   330: aload_0
      //   331: iconst_m1
      //   332: putfield resolvedGuideEnd : I
      //   335: aload_0
      //   336: ldc -1.0
      //   338: putfield resolvedGuidePercent : F
      //   341: goto -> 434
      //   344: aload_0
      //   345: getfield startToEnd : I
      //   348: istore_1
      //   349: iload_1
      //   350: iconst_m1
      //   351: if_icmpeq -> 359
      //   354: aload_0
      //   355: iload_1
      //   356: putfield resolvedLeftToRight : I
      //   359: aload_0
      //   360: getfield startToStart : I
      //   363: istore_1
      //   364: iload_1
      //   365: iconst_m1
      //   366: if_icmpeq -> 374
      //   369: aload_0
      //   370: iload_1
      //   371: putfield resolvedLeftToLeft : I
      //   374: aload_0
      //   375: getfield endToStart : I
      //   378: istore_1
      //   379: iload_1
      //   380: iconst_m1
      //   381: if_icmpeq -> 389
      //   384: aload_0
      //   385: iload_1
      //   386: putfield resolvedRightToLeft : I
      //   389: aload_0
      //   390: getfield endToEnd : I
      //   393: istore_1
      //   394: iload_1
      //   395: iconst_m1
      //   396: if_icmpeq -> 404
      //   399: aload_0
      //   400: iload_1
      //   401: putfield resolvedRightToRight : I
      //   404: aload_0
      //   405: getfield goneStartMargin : I
      //   408: istore_1
      //   409: iload_1
      //   410: iconst_m1
      //   411: if_icmpeq -> 419
      //   414: aload_0
      //   415: iload_1
      //   416: putfield resolveGoneLeftMargin : I
      //   419: aload_0
      //   420: getfield goneEndMargin : I
      //   423: istore_1
      //   424: iload_1
      //   425: iconst_m1
      //   426: if_icmpeq -> 434
      //   429: aload_0
      //   430: iload_1
      //   431: putfield resolveGoneRightMargin : I
      //   434: aload_0
      //   435: getfield endToStart : I
      //   438: iconst_m1
      //   439: if_icmpne -> 604
      //   442: aload_0
      //   443: getfield endToEnd : I
      //   446: iconst_m1
      //   447: if_icmpne -> 604
      //   450: aload_0
      //   451: getfield startToStart : I
      //   454: iconst_m1
      //   455: if_icmpne -> 604
      //   458: aload_0
      //   459: getfield startToEnd : I
      //   462: iconst_m1
      //   463: if_icmpne -> 604
      //   466: aload_0
      //   467: getfield rightToLeft : I
      //   470: istore_1
      //   471: iload_1
      //   472: iconst_m1
      //   473: if_icmpeq -> 502
      //   476: aload_0
      //   477: iload_1
      //   478: putfield resolvedRightToLeft : I
      //   481: aload_0
      //   482: getfield rightMargin : I
      //   485: ifgt -> 535
      //   488: iload #5
      //   490: ifle -> 535
      //   493: aload_0
      //   494: iload #5
      //   496: putfield rightMargin : I
      //   499: goto -> 535
      //   502: aload_0
      //   503: getfield rightToRight : I
      //   506: istore_1
      //   507: iload_1
      //   508: iconst_m1
      //   509: if_icmpeq -> 535
      //   512: aload_0
      //   513: iload_1
      //   514: putfield resolvedRightToRight : I
      //   517: aload_0
      //   518: getfield rightMargin : I
      //   521: ifgt -> 535
      //   524: iload #5
      //   526: ifle -> 535
      //   529: aload_0
      //   530: iload #5
      //   532: putfield rightMargin : I
      //   535: aload_0
      //   536: getfield leftToLeft : I
      //   539: istore_1
      //   540: iload_1
      //   541: iconst_m1
      //   542: if_icmpeq -> 571
      //   545: aload_0
      //   546: iload_1
      //   547: putfield resolvedLeftToLeft : I
      //   550: aload_0
      //   551: getfield leftMargin : I
      //   554: ifgt -> 604
      //   557: iload #4
      //   559: ifle -> 604
      //   562: aload_0
      //   563: iload #4
      //   565: putfield leftMargin : I
      //   568: goto -> 604
      //   571: aload_0
      //   572: getfield leftToRight : I
      //   575: istore_1
      //   576: iload_1
      //   577: iconst_m1
      //   578: if_icmpeq -> 604
      //   581: aload_0
      //   582: iload_1
      //   583: putfield resolvedLeftToRight : I
      //   586: aload_0
      //   587: getfield leftMargin : I
      //   590: ifgt -> 604
      //   593: iload #4
      //   595: ifle -> 604
      //   598: aload_0
      //   599: iload #4
      //   601: putfield leftMargin : I
      //   604: return
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        this.matchConstraintDefaultWidth = 1;
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        this.matchConstraintDefaultHeight = 1;
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = (ConstraintWidget)new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map;
      
      static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        map = sparseIntArray;
        sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map;
    
    static {
      SparseIntArray sparseIntArray = new SparseIntArray();
      map = sparseIntArray;
      sparseIntArray.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\widget\ConstraintLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */