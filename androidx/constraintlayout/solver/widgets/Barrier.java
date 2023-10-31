package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import java.util.ArrayList;

public class Barrier extends Helper {
  public static final int BOTTOM = 3;
  
  public static final int LEFT = 0;
  
  public static final int RIGHT = 1;
  
  public static final int TOP = 2;
  
  private boolean mAllowsGoneWidget = true;
  
  private int mBarrierType = 0;
  
  private ArrayList<ResolutionAnchor> mNodes = new ArrayList<ResolutionAnchor>(4);
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   4: iconst_0
    //   5: aload_0
    //   6: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   9: aastore
    //   10: aload_0
    //   11: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   14: iconst_2
    //   15: aload_0
    //   16: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   19: aastore
    //   20: aload_0
    //   21: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   24: iconst_1
    //   25: aload_0
    //   26: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   29: aastore
    //   30: aload_0
    //   31: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   34: iconst_3
    //   35: aload_0
    //   36: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   39: aastore
    //   40: iconst_0
    //   41: istore_2
    //   42: iload_2
    //   43: aload_0
    //   44: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   47: arraylength
    //   48: if_icmpge -> 76
    //   51: aload_0
    //   52: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   55: iload_2
    //   56: aaload
    //   57: aload_1
    //   58: aload_0
    //   59: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   62: iload_2
    //   63: aaload
    //   64: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   67: putfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   70: iinc #2, 1
    //   73: goto -> 42
    //   76: aload_0
    //   77: getfield mBarrierType : I
    //   80: istore_2
    //   81: iload_2
    //   82: iflt -> 611
    //   85: iload_2
    //   86: iconst_4
    //   87: if_icmpge -> 611
    //   90: aload_0
    //   91: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   94: aload_0
    //   95: getfield mBarrierType : I
    //   98: aaload
    //   99: astore #5
    //   101: iconst_0
    //   102: istore_2
    //   103: iload_2
    //   104: aload_0
    //   105: getfield mWidgetsCount : I
    //   108: if_icmpge -> 203
    //   111: aload_0
    //   112: getfield mWidgets : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   115: iload_2
    //   116: aaload
    //   117: astore #6
    //   119: aload_0
    //   120: getfield mAllowsGoneWidget : Z
    //   123: ifne -> 137
    //   126: aload #6
    //   128: invokevirtual allowedInBarrier : ()Z
    //   131: ifne -> 137
    //   134: goto -> 197
    //   137: aload_0
    //   138: getfield mBarrierType : I
    //   141: istore_3
    //   142: iload_3
    //   143: ifeq -> 151
    //   146: iload_3
    //   147: iconst_1
    //   148: if_icmpne -> 168
    //   151: aload #6
    //   153: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   156: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   159: if_acmpne -> 168
    //   162: iconst_1
    //   163: istore #4
    //   165: goto -> 206
    //   168: aload_0
    //   169: getfield mBarrierType : I
    //   172: istore_3
    //   173: iload_3
    //   174: iconst_2
    //   175: if_icmpeq -> 183
    //   178: iload_3
    //   179: iconst_3
    //   180: if_icmpne -> 197
    //   183: aload #6
    //   185: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   188: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   191: if_acmpne -> 197
    //   194: goto -> 162
    //   197: iinc #2, 1
    //   200: goto -> 103
    //   203: iconst_0
    //   204: istore #4
    //   206: aload_0
    //   207: getfield mBarrierType : I
    //   210: istore_2
    //   211: iload_2
    //   212: ifeq -> 239
    //   215: iload_2
    //   216: iconst_1
    //   217: if_icmpne -> 223
    //   220: goto -> 239
    //   223: aload_0
    //   224: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   227: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   230: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   233: if_acmpne -> 255
    //   236: goto -> 252
    //   239: aload_0
    //   240: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   243: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   246: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   249: if_acmpne -> 255
    //   252: iconst_0
    //   253: istore #4
    //   255: iconst_0
    //   256: istore_2
    //   257: iload_2
    //   258: aload_0
    //   259: getfield mWidgetsCount : I
    //   262: if_icmpge -> 374
    //   265: aload_0
    //   266: getfield mWidgets : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   269: iload_2
    //   270: aaload
    //   271: astore #7
    //   273: aload_0
    //   274: getfield mAllowsGoneWidget : Z
    //   277: ifne -> 291
    //   280: aload #7
    //   282: invokevirtual allowedInBarrier : ()Z
    //   285: ifne -> 291
    //   288: goto -> 368
    //   291: aload_1
    //   292: aload #7
    //   294: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   297: aload_0
    //   298: getfield mBarrierType : I
    //   301: aaload
    //   302: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   305: astore #6
    //   307: aload #7
    //   309: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   312: aload_0
    //   313: getfield mBarrierType : I
    //   316: aaload
    //   317: aload #6
    //   319: putfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   322: aload_0
    //   323: getfield mBarrierType : I
    //   326: istore_3
    //   327: iload_3
    //   328: ifeq -> 355
    //   331: iload_3
    //   332: iconst_2
    //   333: if_icmpne -> 339
    //   336: goto -> 355
    //   339: aload_1
    //   340: aload #5
    //   342: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   345: aload #6
    //   347: iload #4
    //   349: invokevirtual addGreaterBarrier : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Z)V
    //   352: goto -> 368
    //   355: aload_1
    //   356: aload #5
    //   358: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   361: aload #6
    //   363: iload #4
    //   365: invokevirtual addLowerBarrier : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Z)V
    //   368: iinc #2, 1
    //   371: goto -> 257
    //   374: aload_0
    //   375: getfield mBarrierType : I
    //   378: istore_2
    //   379: iload_2
    //   380: ifne -> 437
    //   383: aload_1
    //   384: aload_0
    //   385: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   388: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   391: aload_0
    //   392: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   395: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   398: iconst_0
    //   399: bipush #6
    //   401: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   404: pop
    //   405: iload #4
    //   407: ifne -> 611
    //   410: aload_1
    //   411: aload_0
    //   412: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   415: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   418: aload_0
    //   419: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   422: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   425: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   428: iconst_0
    //   429: iconst_5
    //   430: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   433: pop
    //   434: goto -> 611
    //   437: iload_2
    //   438: iconst_1
    //   439: if_icmpne -> 496
    //   442: aload_1
    //   443: aload_0
    //   444: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   447: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   450: aload_0
    //   451: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   454: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   457: iconst_0
    //   458: bipush #6
    //   460: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   463: pop
    //   464: iload #4
    //   466: ifne -> 611
    //   469: aload_1
    //   470: aload_0
    //   471: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   474: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   477: aload_0
    //   478: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   481: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   484: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   487: iconst_0
    //   488: iconst_5
    //   489: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   492: pop
    //   493: goto -> 611
    //   496: iload_2
    //   497: iconst_2
    //   498: if_icmpne -> 555
    //   501: aload_1
    //   502: aload_0
    //   503: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   506: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   509: aload_0
    //   510: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   513: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   516: iconst_0
    //   517: bipush #6
    //   519: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   522: pop
    //   523: iload #4
    //   525: ifne -> 611
    //   528: aload_1
    //   529: aload_0
    //   530: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   533: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   536: aload_0
    //   537: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   540: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   543: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   546: iconst_0
    //   547: iconst_5
    //   548: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   551: pop
    //   552: goto -> 611
    //   555: iload_2
    //   556: iconst_3
    //   557: if_icmpne -> 611
    //   560: aload_1
    //   561: aload_0
    //   562: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   565: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   568: aload_0
    //   569: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   572: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   575: iconst_0
    //   576: bipush #6
    //   578: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   581: pop
    //   582: iload #4
    //   584: ifne -> 611
    //   587: aload_1
    //   588: aload_0
    //   589: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   592: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   595: aload_0
    //   596: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   599: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   602: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   605: iconst_0
    //   606: iconst_5
    //   607: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   610: pop
    //   611: return
  }
  
  public boolean allowedInBarrier() {
    return true;
  }
  
  public boolean allowsGoneWidget() {
    return this.mAllowsGoneWidget;
  }
  
  public void analyze(int paramInt) {
    ResolutionAnchor resolutionAnchor;
    if (this.mParent == null)
      return; 
    if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2))
      return; 
    paramInt = this.mBarrierType;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3)
            return; 
          resolutionAnchor = this.mBottom.getResolutionNode();
        } else {
          resolutionAnchor = this.mTop.getResolutionNode();
        } 
      } else {
        resolutionAnchor = this.mRight.getResolutionNode();
      } 
    } else {
      resolutionAnchor = this.mLeft.getResolutionNode();
    } 
    resolutionAnchor.setType(5);
    paramInt = this.mBarrierType;
    if (paramInt == 0 || paramInt == 1) {
      this.mTop.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
      this.mBottom.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
    } else {
      this.mLeft.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
      this.mRight.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
    } 
    this.mNodes.clear();
    for (paramInt = 0; paramInt < this.mWidgetsCount; paramInt++) {
      ConstraintWidget constraintWidget = this.mWidgets[paramInt];
      if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
        ResolutionAnchor resolutionAnchor1;
        int i = this.mBarrierType;
        if (i != 0) {
          if (i != 1) {
            if (i != 2) {
              if (i != 3) {
                constraintWidget = null;
              } else {
                resolutionAnchor1 = constraintWidget.mBottom.getResolutionNode();
              } 
            } else {
              resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mTop.getResolutionNode();
            } 
          } else {
            resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mRight.getResolutionNode();
          } 
        } else {
          resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mLeft.getResolutionNode();
        } 
        if (resolutionAnchor1 != null) {
          this.mNodes.add(resolutionAnchor1);
          resolutionAnchor1.addDependent(resolutionAnchor);
        } 
      } 
    } 
  }
  
  public void resetResolutionNodes() {
    super.resetResolutionNodes();
    this.mNodes.clear();
  }
  
  public void resolve() {
    int i = this.mBarrierType;
    float f1 = Float.MAX_VALUE;
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3)
            return; 
          ResolutionAnchor resolutionAnchor = this.mBottom.getResolutionNode();
        } else {
          ResolutionAnchor resolutionAnchor = this.mTop.getResolutionNode();
          int k = this.mNodes.size();
          Object object1 = null;
          i = 0;
          float f = f1;
        } 
      } else {
        ResolutionAnchor resolutionAnchor = this.mRight.getResolutionNode();
      } 
      f1 = 0.0F;
    } else {
      ResolutionAnchor resolutionAnchor = this.mLeft.getResolutionNode();
    } 
    int j = this.mNodes.size();
    Object object = null;
    i = 0;
    float f2 = f1;
  }
  
  public void setAllowsGoneWidget(boolean paramBoolean) {
    this.mAllowsGoneWidget = paramBoolean;
  }
  
  public void setBarrierType(int paramInt) {
    this.mBarrierType = paramInt;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\Barrier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */