package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
  protected static final int ANCHOR_BASELINE = 4;
  
  protected static final int ANCHOR_BOTTOM = 3;
  
  protected static final int ANCHOR_LEFT = 0;
  
  protected static final int ANCHOR_RIGHT = 1;
  
  protected static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  protected ArrayList<ConstraintAnchor> mAnchors;
  
  ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance;
  
  ConstraintWidgetGroup mBelongingGroup = null;
  
  ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  ConstraintAnchor mCenter;
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip;
  
  private String mDebugName;
  
  protected float mDimensionRatio;
  
  protected int mDimensionRatioSide;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  private int mDrawHeight;
  
  private int mDrawWidth;
  
  private int mDrawX;
  
  private int mDrawY;
  
  boolean mGroupsToSolver;
  
  int mHeight;
  
  float mHorizontalBiasPercent;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle;
  
  ConstraintWidget mHorizontalNextWidget;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  boolean mIsHeightWrapContent;
  
  boolean mIsWidthWrapContent;
  
  ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  protected ConstraintAnchor[] mListAnchors;
  
  protected DimensionBehaviour[] mListDimensionBehaviors;
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget;
  
  int mMatchConstraintDefaultHeight = 0;
  
  int mMatchConstraintDefaultWidth = 0;
  
  int mMatchConstraintMaxHeight = 0;
  
  int mMatchConstraintMaxWidth = 0;
  
  int mMatchConstraintMinHeight = 0;
  
  int mMatchConstraintMinWidth = 0;
  
  float mMatchConstraintPercentHeight = 1.0F;
  
  float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget;
  
  protected int mOffsetX;
  
  protected int mOffsetY;
  
  boolean mOptimizerMeasurable;
  
  boolean mOptimizerMeasured;
  
  ConstraintWidget mParent;
  
  int mRelX;
  
  int mRelY;
  
  ResolutionDimension mResolutionHeight;
  
  ResolutionDimension mResolutionWidth;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  int[] mResolvedMatchConstraintDefault = new int[2];
  
  ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType;
  
  float mVerticalBiasPercent;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle;
  
  ConstraintWidget mVerticalNextWidget;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility;
  
  float[] mWeight;
  
  int mWidth;
  
  private int mWrapHeight;
  
  private int mWrapWidth;
  
  protected int mX;
  
  protected int mY;
  
  public ConstraintWidget() {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<ConstraintAnchor>();
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) {
    this(0, 0, paramInt1, paramInt2);
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
    this.mCenter = constraintAnchor;
    this.mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor };
    this.mAnchors = new ArrayList<ConstraintAnchor>();
    this.mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
    this.mParent = null;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mRelX = 0;
    this.mRelY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
    forceUpdateDrawPosition();
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean2, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean3, boolean paramBoolean4, int paramInt5, int paramInt6, int paramInt7, float paramFloat2, boolean paramBoolean5) {
    // Byte code:
    //   0: aload_1
    //   1: aload #7
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   6: astore #30
    //   8: aload_1
    //   9: aload #8
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   14: astore #27
    //   16: aload_1
    //   17: aload #7
    //   19: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   22: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   25: astore #28
    //   27: aload_1
    //   28: aload #8
    //   30: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   36: astore #29
    //   38: aload_1
    //   39: getfield graphOptimizer : Z
    //   42: ifeq -> 128
    //   45: aload #7
    //   47: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   50: getfield state : I
    //   53: iconst_1
    //   54: if_icmpne -> 128
    //   57: aload #8
    //   59: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   62: getfield state : I
    //   65: iconst_1
    //   66: if_icmpne -> 128
    //   69: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   72: ifnull -> 89
    //   75: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   78: astore_3
    //   79: aload_3
    //   80: aload_3
    //   81: getfield resolvedWidgets : J
    //   84: lconst_1
    //   85: ladd
    //   86: putfield resolvedWidgets : J
    //   89: aload #7
    //   91: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   94: aload_1
    //   95: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   98: aload #8
    //   100: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   103: aload_1
    //   104: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   107: iload #15
    //   109: ifne -> 127
    //   112: iload_2
    //   113: ifeq -> 127
    //   116: aload_1
    //   117: aload #4
    //   119: aload #27
    //   121: iconst_0
    //   122: bipush #6
    //   124: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   127: return
    //   128: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   131: ifnull -> 151
    //   134: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   137: astore #26
    //   139: aload #26
    //   141: aload #26
    //   143: getfield nonresolvedWidgets : J
    //   146: lconst_1
    //   147: ladd
    //   148: putfield nonresolvedWidgets : J
    //   151: aload #7
    //   153: invokevirtual isConnected : ()Z
    //   156: istore #23
    //   158: aload #8
    //   160: invokevirtual isConnected : ()Z
    //   163: istore #24
    //   165: aload_0
    //   166: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   169: invokevirtual isConnected : ()Z
    //   172: istore #25
    //   174: iload #23
    //   176: ifeq -> 185
    //   179: iconst_1
    //   180: istore #22
    //   182: goto -> 188
    //   185: iconst_0
    //   186: istore #22
    //   188: iload #22
    //   190: istore #21
    //   192: iload #24
    //   194: ifeq -> 203
    //   197: iload #22
    //   199: iconst_1
    //   200: iadd
    //   201: istore #21
    //   203: iload #21
    //   205: istore #22
    //   207: iload #25
    //   209: ifeq -> 218
    //   212: iload #21
    //   214: iconst_1
    //   215: iadd
    //   216: istore #22
    //   218: iload #14
    //   220: ifeq -> 229
    //   223: iconst_3
    //   224: istore #21
    //   226: goto -> 233
    //   229: iload #16
    //   231: istore #21
    //   233: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   236: aload #5
    //   238: invokevirtual ordinal : ()I
    //   241: iaload
    //   242: istore #16
    //   244: iload #16
    //   246: iconst_1
    //   247: if_icmpeq -> 268
    //   250: iload #16
    //   252: iconst_2
    //   253: if_icmpeq -> 268
    //   256: iload #16
    //   258: iconst_3
    //   259: if_icmpeq -> 268
    //   262: iload #16
    //   264: iconst_4
    //   265: if_icmpeq -> 274
    //   268: iconst_0
    //   269: istore #16
    //   271: goto -> 286
    //   274: iload #21
    //   276: iconst_4
    //   277: if_icmpne -> 283
    //   280: goto -> 268
    //   283: iconst_1
    //   284: istore #16
    //   286: aload_0
    //   287: getfield mVisibility : I
    //   290: bipush #8
    //   292: if_icmpne -> 304
    //   295: iconst_0
    //   296: istore #10
    //   298: iconst_0
    //   299: istore #16
    //   301: goto -> 304
    //   304: iload #20
    //   306: ifeq -> 364
    //   309: iload #23
    //   311: ifne -> 335
    //   314: iload #24
    //   316: ifne -> 335
    //   319: iload #25
    //   321: ifne -> 335
    //   324: aload_1
    //   325: aload #30
    //   327: iload #9
    //   329: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;I)V
    //   332: goto -> 364
    //   335: iload #23
    //   337: ifeq -> 364
    //   340: iload #24
    //   342: ifne -> 364
    //   345: aload_1
    //   346: aload #30
    //   348: aload #28
    //   350: aload #7
    //   352: invokevirtual getMargin : ()I
    //   355: bipush #6
    //   357: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   360: pop
    //   361: goto -> 364
    //   364: iload #16
    //   366: ifne -> 452
    //   369: iload #6
    //   371: ifeq -> 424
    //   374: aload_1
    //   375: aload #27
    //   377: aload #30
    //   379: iconst_0
    //   380: iconst_3
    //   381: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   384: pop
    //   385: iload #11
    //   387: ifle -> 402
    //   390: aload_1
    //   391: aload #27
    //   393: aload #30
    //   395: iload #11
    //   397: bipush #6
    //   399: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   402: iload #12
    //   404: ldc 2147483647
    //   406: if_icmpge -> 421
    //   409: aload_1
    //   410: aload #27
    //   412: aload #30
    //   414: iload #12
    //   416: bipush #6
    //   418: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   421: goto -> 437
    //   424: aload_1
    //   425: aload #27
    //   427: aload #30
    //   429: iload #10
    //   431: bipush #6
    //   433: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   436: pop
    //   437: iload #16
    //   439: istore #12
    //   441: iload #18
    //   443: istore #10
    //   445: iload #17
    //   447: istore #16
    //   449: goto -> 826
    //   452: iload #17
    //   454: istore #9
    //   456: iload #17
    //   458: bipush #-2
    //   460: if_icmpne -> 467
    //   463: iload #10
    //   465: istore #9
    //   467: iload #18
    //   469: istore #12
    //   471: iload #18
    //   473: bipush #-2
    //   475: if_icmpne -> 482
    //   478: iload #10
    //   480: istore #12
    //   482: iload #10
    //   484: istore #17
    //   486: iload #9
    //   488: ifle -> 512
    //   491: aload_1
    //   492: aload #27
    //   494: aload #30
    //   496: iload #9
    //   498: bipush #6
    //   500: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   503: iload #10
    //   505: iload #9
    //   507: invokestatic max : (II)I
    //   510: istore #17
    //   512: iload #17
    //   514: istore #18
    //   516: iload #12
    //   518: ifle -> 542
    //   521: aload_1
    //   522: aload #27
    //   524: aload #30
    //   526: iload #12
    //   528: bipush #6
    //   530: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   533: iload #17
    //   535: iload #12
    //   537: invokestatic min : (II)I
    //   540: istore #18
    //   542: iload #21
    //   544: iconst_1
    //   545: if_icmpne -> 603
    //   548: iload_2
    //   549: ifeq -> 568
    //   552: aload_1
    //   553: aload #27
    //   555: aload #30
    //   557: iload #18
    //   559: bipush #6
    //   561: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   564: pop
    //   565: goto -> 736
    //   568: iload #15
    //   570: ifeq -> 588
    //   573: aload_1
    //   574: aload #27
    //   576: aload #30
    //   578: iload #18
    //   580: iconst_4
    //   581: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   584: pop
    //   585: goto -> 736
    //   588: aload_1
    //   589: aload #27
    //   591: aload #30
    //   593: iload #18
    //   595: iconst_1
    //   596: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   599: pop
    //   600: goto -> 736
    //   603: iload #21
    //   605: iconst_2
    //   606: if_icmpne -> 736
    //   609: aload #7
    //   611: invokevirtual getType : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   614: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   617: if_acmpeq -> 673
    //   620: aload #7
    //   622: invokevirtual getType : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   625: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   628: if_acmpne -> 634
    //   631: goto -> 673
    //   634: aload_1
    //   635: aload_0
    //   636: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   639: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   642: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   645: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   648: astore #5
    //   650: aload_0
    //   651: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   654: astore #26
    //   656: aload_1
    //   657: aload #26
    //   659: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   662: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   665: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   668: astore #26
    //   670: goto -> 709
    //   673: aload_1
    //   674: aload_0
    //   675: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   678: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   681: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   684: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   687: astore #5
    //   689: aload_0
    //   690: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   693: astore #26
    //   695: aload_1
    //   696: aload #26
    //   698: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   701: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   704: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   707: astore #26
    //   709: aload_1
    //   710: aload_1
    //   711: invokevirtual createRow : ()Landroidx/constraintlayout/solver/ArrayRow;
    //   714: aload #27
    //   716: aload #30
    //   718: aload #26
    //   720: aload #5
    //   722: fload #19
    //   724: invokevirtual createRowDimensionRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;F)Landroidx/constraintlayout/solver/ArrayRow;
    //   727: invokevirtual addConstraint : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   730: iconst_0
    //   731: istore #10
    //   733: goto -> 740
    //   736: iload #16
    //   738: istore #10
    //   740: iload #12
    //   742: istore #17
    //   744: iload #10
    //   746: ifeq -> 814
    //   749: iload #22
    //   751: iconst_2
    //   752: if_icmpeq -> 814
    //   755: iload #14
    //   757: ifne -> 814
    //   760: iload #9
    //   762: iload #18
    //   764: invokestatic max : (II)I
    //   767: istore #12
    //   769: iload #12
    //   771: istore #10
    //   773: iload #17
    //   775: ifle -> 787
    //   778: iload #17
    //   780: iload #12
    //   782: invokestatic min : (II)I
    //   785: istore #10
    //   787: aload_1
    //   788: aload #27
    //   790: aload #30
    //   792: iload #10
    //   794: bipush #6
    //   796: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   799: pop
    //   800: iconst_0
    //   801: istore #12
    //   803: iload #17
    //   805: istore #10
    //   807: iload #9
    //   809: istore #16
    //   811: goto -> 826
    //   814: iload #10
    //   816: istore #12
    //   818: iload #9
    //   820: istore #16
    //   822: iload #17
    //   824: istore #10
    //   826: iload #20
    //   828: ifeq -> 1422
    //   831: iload #15
    //   833: ifeq -> 839
    //   836: goto -> 1422
    //   839: iload #23
    //   841: ifne -> 874
    //   844: iload #24
    //   846: ifne -> 874
    //   849: iload #25
    //   851: ifne -> 874
    //   854: iload_2
    //   855: ifeq -> 871
    //   858: aload_1
    //   859: aload #4
    //   861: aload #27
    //   863: iconst_0
    //   864: iconst_5
    //   865: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   868: goto -> 1406
    //   871: goto -> 1406
    //   874: iload #23
    //   876: ifeq -> 901
    //   879: iload #24
    //   881: ifne -> 901
    //   884: iload_2
    //   885: ifeq -> 1406
    //   888: aload_1
    //   889: aload #4
    //   891: aload #27
    //   893: iconst_0
    //   894: iconst_5
    //   895: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   898: goto -> 1406
    //   901: iload #23
    //   903: ifne -> 944
    //   906: iload #24
    //   908: ifeq -> 944
    //   911: aload_1
    //   912: aload #27
    //   914: aload #29
    //   916: aload #8
    //   918: invokevirtual getMargin : ()I
    //   921: ineg
    //   922: bipush #6
    //   924: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   927: pop
    //   928: iload_2
    //   929: ifeq -> 1406
    //   932: aload_1
    //   933: aload #30
    //   935: aload_3
    //   936: iconst_0
    //   937: iconst_5
    //   938: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   941: goto -> 1406
    //   944: iload #23
    //   946: ifeq -> 1406
    //   949: iload #24
    //   951: ifeq -> 1406
    //   954: iload #12
    //   956: ifeq -> 1184
    //   959: iload_2
    //   960: ifeq -> 979
    //   963: iload #11
    //   965: ifne -> 979
    //   968: aload_1
    //   969: aload #27
    //   971: aload #30
    //   973: iconst_0
    //   974: bipush #6
    //   976: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   979: iload #21
    //   981: ifne -> 1086
    //   984: iload #10
    //   986: ifgt -> 1007
    //   989: iload #16
    //   991: ifle -> 997
    //   994: goto -> 1007
    //   997: bipush #6
    //   999: istore #11
    //   1001: iconst_0
    //   1002: istore #9
    //   1004: goto -> 1013
    //   1007: iconst_4
    //   1008: istore #11
    //   1010: iconst_1
    //   1011: istore #9
    //   1013: aload_1
    //   1014: aload #30
    //   1016: aload #28
    //   1018: aload #7
    //   1020: invokevirtual getMargin : ()I
    //   1023: iload #11
    //   1025: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1028: pop
    //   1029: aload_1
    //   1030: aload #27
    //   1032: aload #29
    //   1034: aload #8
    //   1036: invokevirtual getMargin : ()I
    //   1039: ineg
    //   1040: iload #11
    //   1042: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1045: pop
    //   1046: iload #10
    //   1048: ifgt -> 1065
    //   1051: iload #16
    //   1053: ifle -> 1059
    //   1056: goto -> 1065
    //   1059: iconst_0
    //   1060: istore #10
    //   1062: goto -> 1068
    //   1065: iconst_1
    //   1066: istore #10
    //   1068: iconst_5
    //   1069: istore #16
    //   1071: iload #9
    //   1073: istore #11
    //   1075: iload #10
    //   1077: istore #9
    //   1079: iload #16
    //   1081: istore #10
    //   1083: goto -> 1193
    //   1086: iload #21
    //   1088: iconst_1
    //   1089: if_icmpne -> 1105
    //   1092: bipush #6
    //   1094: istore #10
    //   1096: iconst_1
    //   1097: istore #9
    //   1099: iconst_1
    //   1100: istore #11
    //   1102: goto -> 1193
    //   1105: iload #21
    //   1107: iconst_3
    //   1108: if_icmpne -> 1178
    //   1111: iload #14
    //   1113: ifne -> 1136
    //   1116: aload_0
    //   1117: getfield mResolvedDimensionRatioSide : I
    //   1120: iconst_m1
    //   1121: if_icmpeq -> 1136
    //   1124: iload #10
    //   1126: ifgt -> 1136
    //   1129: bipush #6
    //   1131: istore #9
    //   1133: goto -> 1139
    //   1136: iconst_4
    //   1137: istore #9
    //   1139: aload_1
    //   1140: aload #30
    //   1142: aload #28
    //   1144: aload #7
    //   1146: invokevirtual getMargin : ()I
    //   1149: iload #9
    //   1151: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1154: pop
    //   1155: aload_1
    //   1156: aload #27
    //   1158: aload #29
    //   1160: aload #8
    //   1162: invokevirtual getMargin : ()I
    //   1165: ineg
    //   1166: iload #9
    //   1168: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1171: pop
    //   1172: iconst_5
    //   1173: istore #10
    //   1175: goto -> 1096
    //   1178: iconst_0
    //   1179: istore #9
    //   1181: goto -> 1187
    //   1184: iconst_1
    //   1185: istore #9
    //   1187: iconst_5
    //   1188: istore #10
    //   1190: iconst_0
    //   1191: istore #11
    //   1193: iload #9
    //   1195: ifeq -> 1302
    //   1198: aload_1
    //   1199: aload #30
    //   1201: aload #28
    //   1203: aload #7
    //   1205: invokevirtual getMargin : ()I
    //   1208: fload #13
    //   1210: aload #29
    //   1212: aload #27
    //   1214: aload #8
    //   1216: invokevirtual getMargin : ()I
    //   1219: iload #10
    //   1221: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1224: aload #7
    //   1226: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1229: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1232: instanceof androidx/constraintlayout/solver/widgets/Barrier
    //   1235: istore #14
    //   1237: aload #8
    //   1239: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1242: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1245: instanceof androidx/constraintlayout/solver/widgets/Barrier
    //   1248: istore #6
    //   1250: iload #14
    //   1252: ifeq -> 1276
    //   1255: iload #6
    //   1257: ifne -> 1276
    //   1260: iload_2
    //   1261: istore #6
    //   1263: bipush #6
    //   1265: istore #9
    //   1267: iconst_5
    //   1268: istore #10
    //   1270: iconst_1
    //   1271: istore #14
    //   1273: goto -> 1315
    //   1276: iload #14
    //   1278: ifne -> 1302
    //   1281: iload #6
    //   1283: ifeq -> 1302
    //   1286: iload_2
    //   1287: istore #14
    //   1289: iconst_5
    //   1290: istore #9
    //   1292: bipush #6
    //   1294: istore #10
    //   1296: iconst_1
    //   1297: istore #6
    //   1299: goto -> 1315
    //   1302: iload_2
    //   1303: istore #6
    //   1305: iload #6
    //   1307: istore #14
    //   1309: iconst_5
    //   1310: istore #9
    //   1312: iconst_5
    //   1313: istore #10
    //   1315: iload #11
    //   1317: ifeq -> 1328
    //   1320: bipush #6
    //   1322: istore #9
    //   1324: bipush #6
    //   1326: istore #10
    //   1328: iload #12
    //   1330: ifne -> 1338
    //   1333: iload #6
    //   1335: ifne -> 1343
    //   1338: iload #11
    //   1340: ifeq -> 1358
    //   1343: aload_1
    //   1344: aload #30
    //   1346: aload #28
    //   1348: aload #7
    //   1350: invokevirtual getMargin : ()I
    //   1353: iload #10
    //   1355: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1358: iload #12
    //   1360: ifne -> 1368
    //   1363: iload #14
    //   1365: ifne -> 1373
    //   1368: iload #11
    //   1370: ifeq -> 1389
    //   1373: aload_1
    //   1374: aload #27
    //   1376: aload #29
    //   1378: aload #8
    //   1380: invokevirtual getMargin : ()I
    //   1383: ineg
    //   1384: iload #9
    //   1386: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1389: iload_2
    //   1390: ifeq -> 1406
    //   1393: aload_1
    //   1394: aload #30
    //   1396: aload_3
    //   1397: iconst_0
    //   1398: bipush #6
    //   1400: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1403: goto -> 1406
    //   1406: iload_2
    //   1407: ifeq -> 1421
    //   1410: aload_1
    //   1411: aload #4
    //   1413: aload #27
    //   1415: iconst_0
    //   1416: bipush #6
    //   1418: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1421: return
    //   1422: iload #22
    //   1424: iconst_2
    //   1425: if_icmpge -> 1453
    //   1428: iload_2
    //   1429: ifeq -> 1453
    //   1432: aload_1
    //   1433: aload #30
    //   1435: aload_3
    //   1436: iconst_0
    //   1437: bipush #6
    //   1439: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1442: aload_1
    //   1443: aload #4
    //   1445: aload #27
    //   1447: iconst_0
    //   1448: bipush #6
    //   1450: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1453: return
  }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget;
    null = true;
    if (constraintAnchor != null) {
      ConstraintAnchor constraintAnchor1 = (this.mListAnchors[paramInt]).mTarget.mTarget;
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if (constraintAnchor1 != arrayOfConstraintAnchor[paramInt])
        if ((arrayOfConstraintAnchor[++paramInt]).mTarget != null && (this.mListAnchors[paramInt]).mTarget.mTarget == this.mListAnchors[paramInt])
          return null;  
    } 
    return false;
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   8: astore #22
    //   10: aload_1
    //   11: aload_0
    //   12: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   15: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   18: astore #20
    //   20: aload_1
    //   21: aload_0
    //   22: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   25: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   28: astore #19
    //   30: aload_1
    //   31: aload_0
    //   32: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   35: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   38: astore #21
    //   40: aload_1
    //   41: aload_0
    //   42: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   45: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   48: astore #23
    //   50: aload_0
    //   51: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   54: astore #17
    //   56: aload #17
    //   58: ifnull -> 329
    //   61: aload #17
    //   63: ifnull -> 85
    //   66: aload #17
    //   68: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   71: iconst_0
    //   72: aaload
    //   73: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   76: if_acmpne -> 85
    //   79: iconst_1
    //   80: istore #10
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #10
    //   88: aload_0
    //   89: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   92: astore #17
    //   94: aload #17
    //   96: ifnull -> 118
    //   99: aload #17
    //   101: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   104: iconst_1
    //   105: aaload
    //   106: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   109: if_acmpne -> 118
    //   112: iconst_1
    //   113: istore #11
    //   115: goto -> 121
    //   118: iconst_0
    //   119: istore #11
    //   121: aload_0
    //   122: iconst_0
    //   123: invokespecial isChainHead : (I)Z
    //   126: ifeq -> 147
    //   129: aload_0
    //   130: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   133: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   136: aload_0
    //   137: iconst_0
    //   138: invokevirtual addChain : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;I)V
    //   141: iconst_1
    //   142: istore #12
    //   144: goto -> 153
    //   147: aload_0
    //   148: invokevirtual isInHorizontalChain : ()Z
    //   151: istore #12
    //   153: aload_0
    //   154: iconst_1
    //   155: invokespecial isChainHead : (I)Z
    //   158: ifeq -> 179
    //   161: aload_0
    //   162: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   165: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   168: aload_0
    //   169: iconst_1
    //   170: invokevirtual addChain : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;I)V
    //   173: iconst_1
    //   174: istore #13
    //   176: goto -> 185
    //   179: aload_0
    //   180: invokevirtual isInVerticalChain : ()Z
    //   183: istore #13
    //   185: iload #10
    //   187: ifeq -> 238
    //   190: aload_0
    //   191: getfield mVisibility : I
    //   194: bipush #8
    //   196: if_icmpeq -> 238
    //   199: aload_0
    //   200: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   203: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   206: ifnonnull -> 238
    //   209: aload_0
    //   210: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   213: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   216: ifnonnull -> 238
    //   219: aload_1
    //   220: aload_1
    //   221: aload_0
    //   222: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   225: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   228: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   231: aload #20
    //   233: iconst_0
    //   234: iconst_1
    //   235: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   238: iload #11
    //   240: ifeq -> 298
    //   243: aload_0
    //   244: getfield mVisibility : I
    //   247: bipush #8
    //   249: if_icmpeq -> 298
    //   252: aload_0
    //   253: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   256: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   259: ifnonnull -> 298
    //   262: aload_0
    //   263: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   266: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   269: ifnonnull -> 298
    //   272: aload_0
    //   273: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   276: ifnonnull -> 298
    //   279: aload_1
    //   280: aload_1
    //   281: aload_0
    //   282: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   285: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   288: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   291: aload #21
    //   293: iconst_0
    //   294: iconst_1
    //   295: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   298: iload #11
    //   300: istore #15
    //   302: iload #12
    //   304: istore #11
    //   306: iload #13
    //   308: istore #14
    //   310: iload #10
    //   312: istore #12
    //   314: iload #15
    //   316: istore #10
    //   318: iload #11
    //   320: istore #13
    //   322: iload #14
    //   324: istore #11
    //   326: goto -> 355
    //   329: iconst_0
    //   330: istore #14
    //   332: iconst_0
    //   333: istore #12
    //   335: iload #12
    //   337: istore #10
    //   339: iload #10
    //   341: istore #11
    //   343: iload #10
    //   345: istore #13
    //   347: iload #12
    //   349: istore #10
    //   351: iload #14
    //   353: istore #12
    //   355: aload_0
    //   356: getfield mWidth : I
    //   359: istore #5
    //   361: aload_0
    //   362: getfield mMinWidth : I
    //   365: istore_3
    //   366: iload #5
    //   368: istore #4
    //   370: iload #5
    //   372: iload_3
    //   373: if_icmpge -> 379
    //   376: iload_3
    //   377: istore #4
    //   379: aload_0
    //   380: getfield mHeight : I
    //   383: istore #6
    //   385: aload_0
    //   386: getfield mMinHeight : I
    //   389: istore #5
    //   391: iload #6
    //   393: istore_3
    //   394: iload #6
    //   396: iload #5
    //   398: if_icmpge -> 404
    //   401: iload #5
    //   403: istore_3
    //   404: aload_0
    //   405: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   408: iconst_0
    //   409: aaload
    //   410: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   413: if_acmpeq -> 422
    //   416: iconst_1
    //   417: istore #14
    //   419: goto -> 425
    //   422: iconst_0
    //   423: istore #14
    //   425: aload_0
    //   426: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   429: iconst_1
    //   430: aaload
    //   431: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   434: if_acmpeq -> 443
    //   437: iconst_1
    //   438: istore #15
    //   440: goto -> 446
    //   443: iconst_0
    //   444: istore #15
    //   446: aload_0
    //   447: aload_0
    //   448: getfield mDimensionRatioSide : I
    //   451: putfield mResolvedDimensionRatioSide : I
    //   454: aload_0
    //   455: getfield mDimensionRatio : F
    //   458: fstore_2
    //   459: aload_0
    //   460: fload_2
    //   461: putfield mResolvedDimensionRatio : F
    //   464: aload_0
    //   465: getfield mMatchConstraintDefaultWidth : I
    //   468: istore #5
    //   470: aload_0
    //   471: getfield mMatchConstraintDefaultHeight : I
    //   474: istore #7
    //   476: fload_2
    //   477: fconst_0
    //   478: fcmpl
    //   479: ifle -> 833
    //   482: aload_0
    //   483: getfield mVisibility : I
    //   486: bipush #8
    //   488: if_icmpeq -> 833
    //   491: iload #5
    //   493: istore #6
    //   495: aload_0
    //   496: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   499: iconst_0
    //   500: aaload
    //   501: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   504: if_acmpne -> 519
    //   507: iload #5
    //   509: istore #6
    //   511: iload #5
    //   513: ifne -> 519
    //   516: iconst_3
    //   517: istore #6
    //   519: iload #7
    //   521: istore #5
    //   523: aload_0
    //   524: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   527: iconst_1
    //   528: aaload
    //   529: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   532: if_acmpne -> 547
    //   535: iload #7
    //   537: istore #5
    //   539: iload #7
    //   541: ifne -> 547
    //   544: iconst_3
    //   545: istore #5
    //   547: aload_0
    //   548: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   551: iconst_0
    //   552: aaload
    //   553: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   556: if_acmpne -> 598
    //   559: aload_0
    //   560: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   563: iconst_1
    //   564: aaload
    //   565: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   568: if_acmpne -> 598
    //   571: iload #6
    //   573: iconst_3
    //   574: if_icmpne -> 598
    //   577: iload #5
    //   579: iconst_3
    //   580: if_icmpne -> 598
    //   583: aload_0
    //   584: iload #12
    //   586: iload #10
    //   588: iload #14
    //   590: iload #15
    //   592: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   595: goto -> 805
    //   598: aload_0
    //   599: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   602: iconst_0
    //   603: aaload
    //   604: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   607: if_acmpne -> 706
    //   610: iload #6
    //   612: iconst_3
    //   613: if_icmpne -> 706
    //   616: aload_0
    //   617: iconst_0
    //   618: putfield mResolvedDimensionRatioSide : I
    //   621: aload_0
    //   622: getfield mResolvedDimensionRatio : F
    //   625: aload_0
    //   626: getfield mHeight : I
    //   629: i2f
    //   630: fmul
    //   631: f2i
    //   632: istore #4
    //   634: aload_0
    //   635: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   638: iconst_1
    //   639: aaload
    //   640: astore #17
    //   642: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   645: astore #18
    //   647: aload #17
    //   649: aload #18
    //   651: if_acmpeq -> 674
    //   654: iload_3
    //   655: istore #7
    //   657: iload #5
    //   659: istore #6
    //   661: iconst_4
    //   662: istore #5
    //   664: iload #4
    //   666: istore_3
    //   667: iload #7
    //   669: istore #4
    //   671: goto -> 847
    //   674: iload_3
    //   675: istore #7
    //   677: iconst_1
    //   678: istore #9
    //   680: iload #5
    //   682: istore #8
    //   684: iload #4
    //   686: istore_3
    //   687: iload #7
    //   689: istore #4
    //   691: iload #9
    //   693: istore #5
    //   695: iload #6
    //   697: istore #7
    //   699: iload #8
    //   701: istore #6
    //   703: goto -> 858
    //   706: aload_0
    //   707: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   710: iconst_1
    //   711: aaload
    //   712: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   715: if_acmpne -> 805
    //   718: iload #5
    //   720: iconst_3
    //   721: if_icmpne -> 805
    //   724: aload_0
    //   725: iconst_1
    //   726: putfield mResolvedDimensionRatioSide : I
    //   729: aload_0
    //   730: getfield mDimensionRatioSide : I
    //   733: iconst_m1
    //   734: if_icmpne -> 747
    //   737: aload_0
    //   738: fconst_1
    //   739: aload_0
    //   740: getfield mResolvedDimensionRatio : F
    //   743: fdiv
    //   744: putfield mResolvedDimensionRatio : F
    //   747: aload_0
    //   748: getfield mResolvedDimensionRatio : F
    //   751: aload_0
    //   752: getfield mWidth : I
    //   755: i2f
    //   756: fmul
    //   757: f2i
    //   758: istore #8
    //   760: aload_0
    //   761: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   764: iconst_0
    //   765: aaload
    //   766: astore #18
    //   768: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   771: astore #17
    //   773: iload #6
    //   775: istore #9
    //   777: iload #4
    //   779: istore_3
    //   780: iload #8
    //   782: istore #7
    //   784: aload #18
    //   786: aload #17
    //   788: if_acmpeq -> 808
    //   791: iconst_4
    //   792: istore #6
    //   794: iload #8
    //   796: istore #4
    //   798: iload #9
    //   800: istore #5
    //   802: goto -> 847
    //   805: iload_3
    //   806: istore #7
    //   808: iload #4
    //   810: istore_3
    //   811: iload #5
    //   813: istore #8
    //   815: iconst_1
    //   816: istore #5
    //   818: iload #7
    //   820: istore #4
    //   822: iload #6
    //   824: istore #7
    //   826: iload #8
    //   828: istore #6
    //   830: goto -> 858
    //   833: iload #4
    //   835: istore #8
    //   837: iload #7
    //   839: istore #6
    //   841: iload_3
    //   842: istore #4
    //   844: iload #8
    //   846: istore_3
    //   847: iconst_0
    //   848: istore #8
    //   850: iload #5
    //   852: istore #7
    //   854: iload #8
    //   856: istore #5
    //   858: aload_0
    //   859: getfield mResolvedMatchConstraintDefault : [I
    //   862: astore #17
    //   864: aload #17
    //   866: iconst_0
    //   867: iload #7
    //   869: iastore
    //   870: aload #17
    //   872: iconst_1
    //   873: iload #6
    //   875: iastore
    //   876: iload #5
    //   878: ifeq -> 904
    //   881: aload_0
    //   882: getfield mResolvedDimensionRatioSide : I
    //   885: istore #8
    //   887: iload #8
    //   889: ifeq -> 898
    //   892: iload #8
    //   894: iconst_m1
    //   895: if_icmpne -> 904
    //   898: iconst_1
    //   899: istore #14
    //   901: goto -> 907
    //   904: iconst_0
    //   905: istore #14
    //   907: aload_0
    //   908: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   911: iconst_0
    //   912: aaload
    //   913: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   916: if_acmpne -> 932
    //   919: aload_0
    //   920: instanceof androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   923: ifeq -> 932
    //   926: iconst_1
    //   927: istore #15
    //   929: goto -> 935
    //   932: iconst_0
    //   933: istore #15
    //   935: aload_0
    //   936: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   939: invokevirtual isConnected : ()Z
    //   942: iconst_1
    //   943: ixor
    //   944: istore #16
    //   946: aload_0
    //   947: getfield mHorizontalResolution : I
    //   950: iconst_2
    //   951: if_icmpeq -> 1079
    //   954: aload_0
    //   955: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   958: astore #17
    //   960: aload #17
    //   962: ifnull -> 979
    //   965: aload_1
    //   966: aload #17
    //   968: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   971: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   974: astore #17
    //   976: goto -> 982
    //   979: aconst_null
    //   980: astore #17
    //   982: aload_0
    //   983: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   986: astore #18
    //   988: aload #18
    //   990: ifnull -> 1007
    //   993: aload_1
    //   994: aload #18
    //   996: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   999: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1002: astore #18
    //   1004: goto -> 1010
    //   1007: aconst_null
    //   1008: astore #18
    //   1010: aload_0
    //   1011: aload_1
    //   1012: iload #12
    //   1014: aload #18
    //   1016: aload #17
    //   1018: aload_0
    //   1019: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1022: iconst_0
    //   1023: aaload
    //   1024: iload #15
    //   1026: aload_0
    //   1027: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1030: aload_0
    //   1031: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1034: aload_0
    //   1035: getfield mX : I
    //   1038: iload_3
    //   1039: aload_0
    //   1040: getfield mMinWidth : I
    //   1043: aload_0
    //   1044: getfield mMaxDimension : [I
    //   1047: iconst_0
    //   1048: iaload
    //   1049: aload_0
    //   1050: getfield mHorizontalBiasPercent : F
    //   1053: iload #14
    //   1055: iload #13
    //   1057: iload #7
    //   1059: aload_0
    //   1060: getfield mMatchConstraintMinWidth : I
    //   1063: aload_0
    //   1064: getfield mMatchConstraintMaxWidth : I
    //   1067: aload_0
    //   1068: getfield mMatchConstraintPercentWidth : F
    //   1071: iload #16
    //   1073: invokespecial applyConstraints : (Landroidx/constraintlayout/solver/LinearSystem;ZLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/solver/widgets/ConstraintAnchor;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1076: goto -> 1079
    //   1079: aload_0
    //   1080: getfield mVerticalResolution : I
    //   1083: iconst_2
    //   1084: if_icmpne -> 1088
    //   1087: return
    //   1088: aload_0
    //   1089: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1092: iconst_1
    //   1093: aaload
    //   1094: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1097: if_acmpne -> 1113
    //   1100: aload_0
    //   1101: instanceof androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   1104: ifeq -> 1113
    //   1107: iconst_1
    //   1108: istore #12
    //   1110: goto -> 1116
    //   1113: iconst_0
    //   1114: istore #12
    //   1116: iload #5
    //   1118: ifeq -> 1142
    //   1121: aload_0
    //   1122: getfield mResolvedDimensionRatioSide : I
    //   1125: istore_3
    //   1126: iload_3
    //   1127: iconst_1
    //   1128: if_icmpeq -> 1136
    //   1131: iload_3
    //   1132: iconst_m1
    //   1133: if_icmpne -> 1142
    //   1136: iconst_1
    //   1137: istore #13
    //   1139: goto -> 1145
    //   1142: iconst_0
    //   1143: istore #13
    //   1145: aload_0
    //   1146: getfield mBaselineDistance : I
    //   1149: ifle -> 1232
    //   1152: aload_0
    //   1153: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1156: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1159: getfield state : I
    //   1162: iconst_1
    //   1163: if_icmpne -> 1180
    //   1166: aload_0
    //   1167: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1170: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1173: aload_1
    //   1174: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1177: goto -> 1232
    //   1180: aload_1
    //   1181: aload #23
    //   1183: aload #19
    //   1185: aload_0
    //   1186: invokevirtual getBaselineDistance : ()I
    //   1189: bipush #6
    //   1191: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1194: pop
    //   1195: aload_0
    //   1196: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1199: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1202: ifnull -> 1232
    //   1205: aload_1
    //   1206: aload #23
    //   1208: aload_1
    //   1209: aload_0
    //   1210: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1213: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1216: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1219: iconst_0
    //   1220: bipush #6
    //   1222: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1225: pop
    //   1226: iconst_0
    //   1227: istore #14
    //   1229: goto -> 1236
    //   1232: iload #16
    //   1234: istore #14
    //   1236: aload_0
    //   1237: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1240: astore #17
    //   1242: aload #17
    //   1244: ifnull -> 1261
    //   1247: aload_1
    //   1248: aload #17
    //   1250: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1253: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1256: astore #17
    //   1258: goto -> 1264
    //   1261: aconst_null
    //   1262: astore #17
    //   1264: aload_0
    //   1265: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1268: astore #18
    //   1270: aload #18
    //   1272: ifnull -> 1289
    //   1275: aload_1
    //   1276: aload #18
    //   1278: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1281: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1284: astore #18
    //   1286: goto -> 1292
    //   1289: aconst_null
    //   1290: astore #18
    //   1292: aload_0
    //   1293: aload_1
    //   1294: iload #10
    //   1296: aload #18
    //   1298: aload #17
    //   1300: aload_0
    //   1301: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1304: iconst_1
    //   1305: aaload
    //   1306: iload #12
    //   1308: aload_0
    //   1309: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1312: aload_0
    //   1313: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1316: aload_0
    //   1317: getfield mY : I
    //   1320: iload #4
    //   1322: aload_0
    //   1323: getfield mMinHeight : I
    //   1326: aload_0
    //   1327: getfield mMaxDimension : [I
    //   1330: iconst_1
    //   1331: iaload
    //   1332: aload_0
    //   1333: getfield mVerticalBiasPercent : F
    //   1336: iload #13
    //   1338: iload #11
    //   1340: iload #6
    //   1342: aload_0
    //   1343: getfield mMatchConstraintMinHeight : I
    //   1346: aload_0
    //   1347: getfield mMatchConstraintMaxHeight : I
    //   1350: aload_0
    //   1351: getfield mMatchConstraintPercentHeight : F
    //   1354: iload #14
    //   1356: invokespecial applyConstraints : (Landroidx/constraintlayout/solver/LinearSystem;ZLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/solver/widgets/ConstraintAnchor;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1359: iload #5
    //   1361: ifeq -> 1414
    //   1364: aload_0
    //   1365: getfield mResolvedDimensionRatioSide : I
    //   1368: iconst_1
    //   1369: if_icmpne -> 1393
    //   1372: aload_1
    //   1373: aload #21
    //   1375: aload #19
    //   1377: aload #20
    //   1379: aload #22
    //   1381: aload_0
    //   1382: getfield mResolvedDimensionRatio : F
    //   1385: bipush #6
    //   1387: invokevirtual addRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;FI)V
    //   1390: goto -> 1414
    //   1393: aload_1
    //   1394: aload #20
    //   1396: aload #22
    //   1398: aload #21
    //   1400: aload #19
    //   1402: aload_0
    //   1403: getfield mResolvedDimensionRatio : F
    //   1406: bipush #6
    //   1408: invokevirtual addRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;FI)V
    //   1411: goto -> 1414
    //   1414: aload_0
    //   1415: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1418: invokevirtual isConnected : ()Z
    //   1421: ifeq -> 1459
    //   1424: aload_1
    //   1425: aload_0
    //   1426: aload_0
    //   1427: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1430: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1433: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1436: aload_0
    //   1437: getfield mCircleConstraintAngle : F
    //   1440: ldc_w 90.0
    //   1443: fadd
    //   1444: f2d
    //   1445: invokestatic toRadians : (D)D
    //   1448: d2f
    //   1449: aload_0
    //   1450: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1453: invokevirtual getMargin : ()I
    //   1456: invokevirtual addCenterPoint : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;FI)V
    //   1459: return
  }
  
  public boolean allowedInBarrier() {
    boolean bool;
    if (this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void analyze(int paramInt) {
    Optimizer.analyze(paramInt, this);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) {
    connect(paramType1, paramConstraintWidget, paramType2, 0, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt, ConstraintAnchor.Strength paramStrength) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, paramStrength, 0);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    ConstraintAnchor constraintAnchor;
    ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
    boolean bool = false;
    if (paramType1 == type) {
      if (paramType2 == ConstraintAnchor.Type.CENTER) {
        ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        constraintAnchor = getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.BOTTOM);
        bool = true;
        if ((constraintAnchor1 != null && constraintAnchor1.isConnected()) || (constraintAnchor != null && constraintAnchor.isConnected())) {
          paramInt1 = 0;
        } else {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, ConstraintAnchor.Type.LEFT, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.RIGHT, paramConstraintWidget, ConstraintAnchor.Type.RIGHT, 0, paramStrength, paramInt2);
          paramInt1 = 1;
        } 
        if ((constraintAnchor3 != null && constraintAnchor3.isConnected()) || (constraintAnchor2 != null && constraintAnchor2.isConnected())) {
          bool = false;
        } else {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, ConstraintAnchor.Type.TOP, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, ConstraintAnchor.Type.BOTTOM, 0, paramStrength, paramInt2);
        } 
        if (paramInt1 != 0 && bool) {
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, paramInt2);
        } else if (paramInt1 != 0) {
          getAnchor(ConstraintAnchor.Type.CENTER_X).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, paramInt2);
        } else if (bool) {
          getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, paramInt2);
        } 
      } else {
        if (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT) {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          paramType1 = ConstraintAnchor.Type.RIGHT;
          try {
            connect(paramType1, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
            getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
            return;
          } finally {}
        } 
        if (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM) {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
        } 
      } 
    } else {
      ConstraintAnchor constraintAnchor1;
      ConstraintAnchor constraintAnchor2;
      if (paramType1 == ConstraintAnchor.Type.CENTER_X && (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT)) {
        constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        constraintAnchor = paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
        constraintAnchor1.connect(constraintAnchor, 0, paramInt2);
        constraintAnchor2.connect(constraintAnchor, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM)) {
        constraintAnchor1 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor1, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_X && constraintAnchor == ConstraintAnchor.Type.CENTER_X) {
        getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.LEFT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.RIGHT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && constraintAnchor == ConstraintAnchor.Type.CENTER_Y) {
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.TOP), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else {
        ConstraintAnchor constraintAnchor3 = getAnchor((ConstraintAnchor.Type)constraintAnchor1);
        constraintAnchor2 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        if (constraintAnchor3.isValidConnection(constraintAnchor2)) {
          if (constraintAnchor1 == ConstraintAnchor.Type.BASELINE) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.TOP);
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (constraintAnchor != null)
              constraintAnchor.reset(); 
            paramInt1 = bool;
            if (constraintAnchor1 != null) {
              constraintAnchor1.reset();
              paramInt1 = bool;
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.TOP || constraintAnchor1 == ConstraintAnchor.Type.BOTTOM) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.BASELINE);
            if (constraintAnchor != null)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (constraintAnchor1.isConnected()) {
              constraintAnchor.reset();
              constraintAnchor1.reset();
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.LEFT || constraintAnchor1 == ConstraintAnchor.Type.RIGHT) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.CENTER_X);
            if (constraintAnchor1.isConnected()) {
              constraintAnchor.reset();
              constraintAnchor1.reset();
            } 
          } 
          constraintAnchor3.connect(constraintAnchor2, paramInt1, paramStrength, paramInt2);
          constraintAnchor2.getOwner().connectedTo(constraintAnchor3.getOwner());
        } 
      } 
    } 
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt, ConstraintAnchor.Strength.STRONG, 0);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt1, ConstraintAnchor.Strength.STRONG, paramInt2);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt1, paramStrength, paramInt2); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void connectedTo(ConstraintWidget paramConstraintWidget) {}
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void disconnectUnlockedWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget && constraintAnchor.getConnectionCreator() == 2)
        constraintAnchor.reset(); 
    } 
  }
  
  public void disconnectWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget)
        constraintAnchor.reset(); 
    } 
  }
  
  public void forceUpdateDrawPosition() {
    int i = this.mX;
    int k = this.mY;
    int m = this.mWidth;
    int j = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = k;
    this.mDrawWidth = m + i - i;
    this.mDrawHeight = j + k - k;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case null:
        return this.mBottom;
      case null:
        return this.mRight;
      case null:
        return this.mTop;
      case null:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public int getBaselineDistance() {
    return this.mBaselineDistance;
  }
  
  public float getBiasPercent(int paramInt) {
    return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F);
  }
  
  public int getBottom() {
    return getY() + this.mHeight;
  }
  
  public Object getCompanionWidget() {
    return this.mCompanionWidget;
  }
  
  public int getContainerItemSkip() {
    return this.mContainerItemSkip;
  }
  
  public String getDebugName() {
    return this.mDebugName;
  }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) {
    return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null);
  }
  
  public float getDimensionRatio() {
    return this.mDimensionRatio;
  }
  
  public int getDimensionRatioSide() {
    return this.mDimensionRatioSide;
  }
  
  public int getDrawBottom() {
    return getDrawY() + this.mDrawHeight;
  }
  
  public int getDrawHeight() {
    return this.mDrawHeight;
  }
  
  public int getDrawRight() {
    return getDrawX() + this.mDrawWidth;
  }
  
  public int getDrawWidth() {
    return this.mDrawWidth;
  }
  
  public int getDrawX() {
    return this.mDrawX + this.mOffsetX;
  }
  
  public int getDrawY() {
    return this.mDrawY + this.mOffsetY;
  }
  
  public int getHeight() {
    return (this.mVisibility == 8) ? 0 : this.mHeight;
  }
  
  public float getHorizontalBiasPercent() {
    return this.mHorizontalBiasPercent;
  }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    boolean bool = isInHorizontalChain();
    ConstraintWidget constraintWidget = null;
    if (bool) {
      constraintWidget = this;
      ConstraintWidget constraintWidget1 = null;
      while (true) {
        if (constraintWidget1 == null && constraintWidget != null) {
          ConstraintWidget constraintWidget2;
          ConstraintAnchor constraintAnchor2;
          ConstraintAnchor constraintAnchor1 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
          if (constraintAnchor1 == null) {
            constraintAnchor1 = null;
          } else {
            constraintAnchor1 = constraintAnchor1.getTarget();
          } 
          if (constraintAnchor1 == null) {
            constraintAnchor1 = null;
          } else {
            constraintWidget2 = constraintAnchor1.getOwner();
          } 
          if (constraintWidget2 == getParent())
            break; 
          if (constraintWidget2 == null) {
            constraintAnchor2 = null;
          } else {
            constraintAnchor2 = constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
          } 
          if (constraintAnchor2 != null && constraintAnchor2.getOwner() != constraintWidget) {
            constraintWidget1 = constraintWidget;
            continue;
          } 
          constraintWidget = constraintWidget2;
          continue;
        } 
        constraintWidget = constraintWidget1;
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() {
    return this.mListDimensionBehaviors[0];
  }
  
  public int getInternalDrawBottom() {
    return this.mDrawY + this.mDrawHeight;
  }
  
  public int getInternalDrawRight() {
    return this.mDrawX + this.mDrawWidth;
  }
  
  int getInternalDrawX() {
    return this.mDrawX;
  }
  
  int getInternalDrawY() {
    return this.mDrawY;
  }
  
  public int getLeft() {
    return getX();
  }
  
  public int getLength(int paramInt) {
    return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0);
  }
  
  public int getMaxHeight() {
    return this.mMaxDimension[1];
  }
  
  public int getMaxWidth() {
    return this.mMaxDimension[0];
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else {
        i = this.mMatchConstraintMinHeight;
        if (i > 0) {
          this.mHeight = i;
        } else {
          i = 0;
        } 
      } 
      int k = this.mMatchConstraintMaxHeight;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = k; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else {
        i = this.mMatchConstraintMinWidth;
        if (i > 0) {
          this.mWidth = i;
        } else {
          i = 0;
        } 
      } 
      int k = this.mMatchConstraintMaxWidth;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = k; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() {
    return this.mParent;
  }
  
  int getRelativePositioning(int paramInt) {
    return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0);
  }
  
  public ResolutionDimension getResolutionHeight() {
    if (this.mResolutionHeight == null)
      this.mResolutionHeight = new ResolutionDimension(); 
    return this.mResolutionHeight;
  }
  
  public ResolutionDimension getResolutionWidth() {
    if (this.mResolutionWidth == null)
      this.mResolutionWidth = new ResolutionDimension(); 
    return this.mResolutionWidth;
  }
  
  public int getRight() {
    return getX() + this.mWidth;
  }
  
  public WidgetContainer getRootWidgetContainer() {
    ConstraintWidget constraintWidget;
    for (constraintWidget = this; constraintWidget.getParent() != null; constraintWidget = constraintWidget.getParent());
    return (constraintWidget instanceof WidgetContainer) ? (WidgetContainer)constraintWidget : null;
  }
  
  protected int getRootX() {
    return this.mX + this.mOffsetX;
  }
  
  protected int getRootY() {
    return this.mY + this.mOffsetY;
  }
  
  public int getTop() {
    return getY();
  }
  
  public String getType() {
    return this.mType;
  }
  
  public float getVerticalBiasPercent() {
    return this.mVerticalBiasPercent;
  }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    boolean bool = isInVerticalChain();
    ConstraintWidget constraintWidget = null;
    if (bool) {
      constraintWidget = this;
      ConstraintWidget constraintWidget1 = null;
      while (true) {
        if (constraintWidget1 == null && constraintWidget != null) {
          ConstraintWidget constraintWidget2;
          ConstraintAnchor constraintAnchor2;
          ConstraintAnchor constraintAnchor1 = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
          if (constraintAnchor1 == null) {
            constraintAnchor1 = null;
          } else {
            constraintAnchor1 = constraintAnchor1.getTarget();
          } 
          if (constraintAnchor1 == null) {
            constraintAnchor1 = null;
          } else {
            constraintWidget2 = constraintAnchor1.getOwner();
          } 
          if (constraintWidget2 == getParent())
            break; 
          if (constraintWidget2 == null) {
            constraintAnchor2 = null;
          } else {
            constraintAnchor2 = constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
          } 
          if (constraintAnchor2 != null && constraintAnchor2.getOwner() != constraintWidget) {
            constraintWidget1 = constraintWidget;
            continue;
          } 
          constraintWidget = constraintWidget2;
          continue;
        } 
        constraintWidget = constraintWidget1;
        break;
      } 
    } 
    return constraintWidget;
  }
  
  public int getVerticalChainStyle() {
    return this.mVerticalChainStyle;
  }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() {
    return this.mListDimensionBehaviors[1];
  }
  
  public int getVisibility() {
    return this.mVisibility;
  }
  
  public int getWidth() {
    return (this.mVisibility == 8) ? 0 : this.mWidth;
  }
  
  public int getWrapHeight() {
    return this.mWrapHeight;
  }
  
  public int getWrapWidth() {
    return this.mWrapWidth;
  }
  
  public int getX() {
    return this.mX;
  }
  
  public int getY() {
    return this.mY;
  }
  
  public boolean hasAncestor(ConstraintWidget paramConstraintWidget) {
    ConstraintWidget constraintWidget2 = getParent();
    if (constraintWidget2 == paramConstraintWidget)
      return true; 
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (constraintWidget2 == paramConstraintWidget.getParent())
      return false; 
    while (constraintWidget1 != null) {
      if (constraintWidget1 == paramConstraintWidget)
        return true; 
      if (constraintWidget1 == paramConstraintWidget.getParent())
        return true; 
      constraintWidget1 = constraintWidget1.getParent();
    } 
    return false;
  }
  
  public boolean hasBaseline() {
    boolean bool;
    if (this.mBaselineDistance > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) {
    getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, ConstraintAnchor.Strength.STRONG, 0, true);
  }
  
  public boolean isFullyResolved() {
    return ((this.mLeft.getResolutionNode()).state == 1 && (this.mRight.getResolutionNode()).state == 1 && (this.mTop.getResolutionNode()).state == 1 && (this.mBottom.getResolutionNode()).state == 1);
  }
  
  public boolean isHeightWrapContent() {
    return this.mIsHeightWrapContent;
  }
  
  public boolean isInHorizontalChain() {
    return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight));
  }
  
  public boolean isInVerticalChain() {
    return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom));
  }
  
  public boolean isInsideConstraintLayout() {
    ConstraintWidget constraintWidget2 = getParent();
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (constraintWidget2 == null)
      return false; 
    while (constraintWidget1 != null) {
      if (constraintWidget1 instanceof ConstraintWidgetContainer)
        return true; 
      constraintWidget1 = constraintWidget1.getParent();
    } 
    return false;
  }
  
  public boolean isRoot() {
    boolean bool;
    if (this.mParent == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isRootContainer() {
    if (this instanceof ConstraintWidgetContainer) {
      ConstraintWidget constraintWidget = this.mParent;
      if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer))
        return true; 
    } 
    return false;
  }
  
  public boolean isSpreadHeight() {
    int i = this.mMatchConstraintDefaultHeight;
    boolean bool = true;
    if (i != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT)
      bool = false; 
    return bool;
  }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (i == 0) {
      bool1 = bool2;
      if (this.mDimensionRatio == 0.0F) {
        bool1 = bool2;
        if (this.mMatchConstraintMinWidth == 0) {
          bool1 = bool2;
          if (this.mMatchConstraintMaxWidth == 0) {
            bool1 = bool2;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
              bool1 = true; 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public boolean isWidthWrapContent() {
    return this.mIsWidthWrapContent;
  }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    this.mWrapWidth = 0;
    this.mWrapHeight = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    float[] arrayOfFloat = this.mWeight;
    arrayOfFloat[0] = -1.0F;
    arrayOfFloat[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    int[] arrayOfInt = this.mMaxDimension;
    arrayOfInt[0] = Integer.MAX_VALUE;
    arrayOfInt[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    ResolutionDimension resolutionDimension = this.mResolutionWidth;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    resolutionDimension = this.mResolutionHeight;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    this.mBelongingGroup = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
    if (this instanceof ConstraintWidgetContainer)
      return; 
    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getWidth() == getWrapWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getWidth() > getMinWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
    if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getHeight() == getWrapHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getHeight() > getMinHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor1) {
      if (constraintAnchor7.isConnected() && constraintAnchor4.isConnected() && constraintAnchor7.getTarget() == constraintAnchor4.getTarget()) {
        constraintAnchor7.reset();
        constraintAnchor4.reset();
      } 
      if (constraintAnchor6.isConnected() && constraintAnchor5.isConnected() && constraintAnchor6.getTarget() == constraintAnchor5.getTarget()) {
        constraintAnchor6.reset();
        constraintAnchor5.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor2) {
      if (constraintAnchor7.isConnected() && constraintAnchor4.isConnected() && constraintAnchor7.getTarget().getOwner() == constraintAnchor4.getTarget().getOwner()) {
        constraintAnchor7.reset();
        constraintAnchor4.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor3) {
      if (constraintAnchor6.isConnected() && constraintAnchor5.isConnected() && constraintAnchor6.getTarget().getOwner() == constraintAnchor5.getTarget().getOwner()) {
        constraintAnchor6.reset();
        constraintAnchor5.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor7 || paramConstraintAnchor == constraintAnchor4) {
      if (constraintAnchor7.isConnected() && constraintAnchor7.getTarget() == constraintAnchor4.getTarget())
        constraintAnchor1.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor6 || paramConstraintAnchor == constraintAnchor5) && constraintAnchor6.isConnected() && constraintAnchor6.getTarget() == constraintAnchor5.getTarget()) {
      constraintAnchor1.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetAnchors(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = this.mAnchors.get(b);
      if (paramInt == constraintAnchor.getConnectionCreator()) {
        if (constraintAnchor.isVerticalAnchor()) {
          setVerticalBiasPercent(DEFAULT_BIAS);
        } else {
          setHorizontalBiasPercent(DEFAULT_BIAS);
        } 
        constraintAnchor.reset();
      } 
      b++;
    } 
  }
  
  public void resetResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().reset(); 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resolve() {}
  
  public void setBaselineDistance(int paramInt) {
    this.mBaselineDistance = paramInt;
  }
  
  public void setCompanionWidget(Object paramObject) {
    this.mCompanionWidget = paramObject;
  }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
    } else {
      this.mContainerItemSkip = 0;
    } 
  }
  
  public void setDebugName(String paramString) {
    this.mDebugName = paramString;
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mBottom);
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".left");
    solverVariable4.setName(stringBuilder3.toString());
    stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".top");
    solverVariable3.setName(stringBuilder3.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(".right");
    solverVariable2.setName(stringBuilder2.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append(".bottom");
    solverVariable1.setName(stringBuilder1.toString());
    if (this.mBaselineDistance > 0) {
      SolverVariable solverVariable = paramLinearSystem.createObjectVariable(this.mBaseline);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".baseline");
      solverVariable.setName(stringBuilder.toString());
    } 
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    int i = this.mMinWidth;
    if (paramInt1 < i)
      this.mWidth = i; 
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 261
    //   4: aload_1
    //   5: invokevirtual length : ()I
    //   8: ifne -> 14
    //   11: goto -> 261
    //   14: iconst_m1
    //   15: istore #6
    //   17: aload_1
    //   18: invokevirtual length : ()I
    //   21: istore #8
    //   23: aload_1
    //   24: bipush #44
    //   26: invokevirtual indexOf : (I)I
    //   29: istore #9
    //   31: iconst_0
    //   32: istore #7
    //   34: iload #6
    //   36: istore #4
    //   38: iload #7
    //   40: istore #5
    //   42: iload #9
    //   44: ifle -> 114
    //   47: iload #6
    //   49: istore #4
    //   51: iload #7
    //   53: istore #5
    //   55: iload #9
    //   57: iload #8
    //   59: iconst_1
    //   60: isub
    //   61: if_icmpge -> 114
    //   64: aload_1
    //   65: iconst_0
    //   66: iload #9
    //   68: invokevirtual substring : (II)Ljava/lang/String;
    //   71: astore #10
    //   73: aload #10
    //   75: ldc_w 'W'
    //   78: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   81: ifeq -> 90
    //   84: iconst_0
    //   85: istore #4
    //   87: goto -> 108
    //   90: iload #6
    //   92: istore #4
    //   94: aload #10
    //   96: ldc_w 'H'
    //   99: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   102: ifeq -> 108
    //   105: iconst_1
    //   106: istore #4
    //   108: iload #9
    //   110: iconst_1
    //   111: iadd
    //   112: istore #5
    //   114: aload_1
    //   115: bipush #58
    //   117: invokevirtual indexOf : (I)I
    //   120: istore #6
    //   122: iload #6
    //   124: iflt -> 219
    //   127: iload #6
    //   129: iload #8
    //   131: iconst_1
    //   132: isub
    //   133: if_icmpge -> 219
    //   136: aload_1
    //   137: iload #5
    //   139: iload #6
    //   141: invokevirtual substring : (II)Ljava/lang/String;
    //   144: astore #10
    //   146: aload_1
    //   147: iload #6
    //   149: iconst_1
    //   150: iadd
    //   151: invokevirtual substring : (I)Ljava/lang/String;
    //   154: astore_1
    //   155: aload #10
    //   157: invokevirtual length : ()I
    //   160: ifle -> 241
    //   163: aload_1
    //   164: invokevirtual length : ()I
    //   167: ifle -> 241
    //   170: aload #10
    //   172: invokestatic parseFloat : (Ljava/lang/String;)F
    //   175: fstore_3
    //   176: aload_1
    //   177: invokestatic parseFloat : (Ljava/lang/String;)F
    //   180: fstore_2
    //   181: fload_3
    //   182: fconst_0
    //   183: fcmpl
    //   184: ifle -> 241
    //   187: fload_2
    //   188: fconst_0
    //   189: fcmpl
    //   190: ifle -> 241
    //   193: iload #4
    //   195: iconst_1
    //   196: if_icmpne -> 209
    //   199: fload_2
    //   200: fload_3
    //   201: fdiv
    //   202: invokestatic abs : (F)F
    //   205: fstore_2
    //   206: goto -> 243
    //   209: fload_3
    //   210: fload_2
    //   211: fdiv
    //   212: invokestatic abs : (F)F
    //   215: fstore_2
    //   216: goto -> 243
    //   219: aload_1
    //   220: iload #5
    //   222: invokevirtual substring : (I)Ljava/lang/String;
    //   225: astore_1
    //   226: aload_1
    //   227: invokevirtual length : ()I
    //   230: ifle -> 241
    //   233: aload_1
    //   234: invokestatic parseFloat : (Ljava/lang/String;)F
    //   237: fstore_2
    //   238: goto -> 243
    //   241: fconst_0
    //   242: fstore_2
    //   243: fload_2
    //   244: fconst_0
    //   245: fcmpl
    //   246: ifle -> 260
    //   249: aload_0
    //   250: fload_2
    //   251: putfield mDimensionRatio : F
    //   254: aload_0
    //   255: iload #4
    //   257: putfield mDimensionRatioSide : I
    //   260: return
    //   261: aload_0
    //   262: fconst_0
    //   263: putfield mDimensionRatio : F
    //   266: return
    //   267: astore_1
    //   268: goto -> 241
    // Exception table:
    //   from	to	target	type
    //   170	181	267	java/lang/NumberFormatException
    //   199	206	267	java/lang/NumberFormatException
    //   209	216	267	java/lang/NumberFormatException
    //   233	238	267	java/lang/NumberFormatException
  }
  
  public void setDrawHeight(int paramInt) {
    this.mDrawHeight = paramInt;
  }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    paramInt1 -= this.mOffsetX;
    this.mDrawX = paramInt1;
    paramInt2 -= this.mOffsetY;
    this.mDrawY = paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setDrawWidth(int paramInt) {
    this.mDrawWidth = paramInt;
  }
  
  public void setDrawX(int paramInt) {
    paramInt -= this.mOffsetX;
    this.mDrawX = paramInt;
    this.mX = paramInt;
  }
  
  public void setDrawY(int paramInt) {
    paramInt -= this.mOffsetY;
    this.mDrawY = paramInt;
    this.mY = paramInt;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
    this.mOptimizerMeasured = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt3 - paramInt1;
    paramInt3 = paramInt4 - paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
    if (this.mVisibility == 8) {
      this.mWidth = 0;
      this.mHeight = 0;
      return;
    } 
    paramInt1 = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
      paramInt2 = this.mWidth;
      paramInt1 = i;
      if (i < paramInt2)
        paramInt1 = paramInt2; 
    } 
    paramInt2 = paramInt3;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
      paramInt4 = this.mHeight;
      paramInt2 = paramInt3;
      if (paramInt3 < paramInt4)
        paramInt2 = paramInt4; 
    } 
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
    paramInt2 = this.mWidth;
    paramInt1 = this.mMinWidth;
    if (paramInt2 < paramInt1)
      this.mWidth = paramInt1; 
    this.mOptimizerMeasured = true;
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    int i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramType.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            this.mBottom.mGoneMargin = paramInt; 
        } else {
          this.mRight.mGoneMargin = paramInt;
        } 
      } else {
        this.mTop.mGoneMargin = paramInt;
      } 
    } else {
      this.mLeft.mGoneMargin = paramInt;
    } 
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    int i = this.mMinHeight;
    if (paramInt < i)
      this.mHeight = i; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) {
    this.mIsHeightWrapContent = paramBoolean;
  }
  
  public void setHorizontalBiasPercent(float paramFloat) {
    this.mHorizontalBiasPercent = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    paramInt1 = paramInt2 - paramInt1;
    this.mWidth = paramInt1;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setWidth(this.mWrapWidth); 
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    this.mMatchConstraintMaxWidth = paramInt3;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) {
    this.mWeight[0] = paramFloat;
  }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
    } else if (paramInt2 == 1) {
      setHeight(paramInt1);
    } 
  }
  
  public void setMaxHeight(int paramInt) {
    this.mMaxDimension[1] = paramInt;
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxDimension[0] = paramInt;
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
    } else {
      this.mMinHeight = paramInt;
    } 
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
    } else {
      this.mMinWidth = paramInt;
    } 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) {
    this.mParent = paramConstraintWidget;
  }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
    } else if (paramInt2 == 1) {
      this.mRelY = paramInt1;
    } 
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setVerticalBiasPercent(float paramFloat) {
    this.mVerticalBiasPercent = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    paramInt1 = paramInt2 - paramInt1;
    this.mHeight = paramInt1;
    paramInt2 = this.mMinHeight;
    if (paramInt1 < paramInt2)
      this.mHeight = paramInt2; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setHeight(this.mWrapHeight); 
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat < 1.0F && paramInt1 == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) {
    this.mWeight[1] = paramFloat;
  }
  
  public void setVisibility(int paramInt) {
    this.mVisibility = paramInt;
  }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    int i = this.mMinWidth;
    if (paramInt < i)
      this.mWidth = i; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) {
    this.mIsWidthWrapContent = paramBoolean;
  }
  
  public void setWrapHeight(int paramInt) {
    this.mWrapHeight = paramInt;
  }
  
  public void setWrapWidth(int paramInt) {
    this.mWrapWidth = paramInt;
  }
  
  public void setX(int paramInt) {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt) {
    this.mY = paramInt;
  }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean1 && !paramBoolean2) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean1 && paramBoolean2) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1 && paramBoolean1 && paramBoolean2) {
      this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
      this.mResolvedDimensionRatioSide = 1;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    String str1 = this.mType;
    String str2 = "";
    if (str1 != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("type: ");
      stringBuilder1.append(this.mType);
      stringBuilder1.append(" ");
      String str = stringBuilder1.toString();
    } else {
      str1 = "";
    } 
    stringBuilder.append(str1);
    str1 = str2;
    if (this.mDebugName != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("id: ");
      stringBuilder1.append(this.mDebugName);
      stringBuilder1.append(" ");
      str1 = stringBuilder1.toString();
    } 
    stringBuilder.append(str1);
    stringBuilder.append("(");
    stringBuilder.append(this.mX);
    stringBuilder.append(", ");
    stringBuilder.append(this.mY);
    stringBuilder.append(") - (");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mHeight);
    stringBuilder.append(") wrap: (");
    stringBuilder.append(this.mWrapWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mWrapHeight);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void updateDrawPosition() {
    int m = this.mX;
    int i = this.mY;
    int j = this.mWidth;
    int k = this.mHeight;
    this.mDrawX = m;
    this.mDrawY = i;
    this.mDrawWidth = j + m - m;
    this.mDrawHeight = k + i - i;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   17: istore #4
    //   19: aload_1
    //   20: aload_0
    //   21: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   24: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   27: istore #5
    //   29: aload_1
    //   30: aload_0
    //   31: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   34: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   37: istore #6
    //   39: iload #5
    //   41: iload_2
    //   42: isub
    //   43: iflt -> 115
    //   46: iload #6
    //   48: iload #4
    //   50: isub
    //   51: iflt -> 115
    //   54: iload_2
    //   55: ldc_w -2147483648
    //   58: if_icmpeq -> 115
    //   61: iload_2
    //   62: ldc 2147483647
    //   64: if_icmpeq -> 115
    //   67: iload #4
    //   69: ldc_w -2147483648
    //   72: if_icmpeq -> 115
    //   75: iload #4
    //   77: ldc 2147483647
    //   79: if_icmpeq -> 115
    //   82: iload #5
    //   84: ldc_w -2147483648
    //   87: if_icmpeq -> 115
    //   90: iload #5
    //   92: ldc 2147483647
    //   94: if_icmpeq -> 115
    //   97: iload #6
    //   99: ldc_w -2147483648
    //   102: if_icmpeq -> 115
    //   105: iload #6
    //   107: istore_3
    //   108: iload #6
    //   110: ldc 2147483647
    //   112: if_icmpne -> 132
    //   115: iconst_0
    //   116: istore_3
    //   117: iconst_0
    //   118: istore #6
    //   120: iload #6
    //   122: istore_2
    //   123: iload_2
    //   124: istore #5
    //   126: iload_2
    //   127: istore #4
    //   129: iload #6
    //   131: istore_2
    //   132: aload_0
    //   133: iload_2
    //   134: iload #4
    //   136: iload #5
    //   138: iload_3
    //   139: invokevirtual setFrame : (IIII)V
    //   142: return
  }
  
  public void updateResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().update(); 
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    private static final ContentAlignment[] $VALUES;
    
    static {
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      ContentAlignment contentAlignment = new ContentAlignment("RIGHT", 7);
      RIGHT = contentAlignment;
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, contentAlignment };
    }
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    private static final DimensionBehaviour[] $VALUES;
    
    static {
      DimensionBehaviour dimensionBehaviour = new DimensionBehaviour("MATCH_PARENT", 3);
      MATCH_PARENT = dimensionBehaviour;
      $VALUES = new DimensionBehaviour[] { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, dimensionBehaviour };
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ConstraintWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */