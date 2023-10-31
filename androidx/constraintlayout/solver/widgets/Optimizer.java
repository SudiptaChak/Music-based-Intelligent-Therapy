package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

public class Optimizer {
  static final int FLAG_CHAIN_DANGLING = 1;
  
  static final int FLAG_RECOMPUTE_BOUNDS = 2;
  
  static final int FLAG_USE_OPTIMIZE = 0;
  
  public static final int OPTIMIZATION_BARRIER = 2;
  
  public static final int OPTIMIZATION_CHAIN = 4;
  
  public static final int OPTIMIZATION_DIMENSIONS = 8;
  
  public static final int OPTIMIZATION_DIRECT = 1;
  
  public static final int OPTIMIZATION_GROUPS = 32;
  
  public static final int OPTIMIZATION_NONE = 0;
  
  public static final int OPTIMIZATION_RATIO = 16;
  
  public static final int OPTIMIZATION_STANDARD = 7;
  
  static boolean[] flags = new boolean[3];
  
  static void analyze(int paramInt, ConstraintWidget paramConstraintWidget) {
    int i;
    paramConstraintWidget.updateResolutionNodes();
    ResolutionAnchor resolutionAnchor1 = paramConstraintWidget.mLeft.getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = paramConstraintWidget.mTop.getResolutionNode();
    ResolutionAnchor resolutionAnchor3 = paramConstraintWidget.mRight.getResolutionNode();
    ResolutionAnchor resolutionAnchor4 = paramConstraintWidget.mBottom.getResolutionNode();
    if ((paramInt & 0x8) == 8) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 0)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor1.type != 4 && resolutionAnchor3.type != 4)
      if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || (i && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(2);
          resolutionAnchor3.setType(2);
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.setOpposite(resolutionAnchor3, -paramConstraintWidget.getWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } 
      } else if (i) {
        i = paramConstraintWidget.getWidth();
        resolutionAnchor1.setType(1);
        resolutionAnchor3.setType(1);
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor1.setType(3);
            resolutionAnchor3.setType(3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, 0.0F);
            resolutionAnchor3.setOpposite(resolutionAnchor1, 0.0F);
          } else {
            resolutionAnchor1.setType(2);
            resolutionAnchor3.setType(2);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -i);
            resolutionAnchor3.setOpposite(resolutionAnchor1, i);
            paramConstraintWidget.setWidth(i);
          } 
        } 
      }  
    if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 1)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
      if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || (i != 0 && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaseline.mTarget != null) {
            paramConstraintWidget.mBaseline.getResolutionNode().setType(1);
            resolutionAnchor2.dependsOn(1, paramConstraintWidget.mBaseline.getResolutionNode(), -paramConstraintWidget.mBaselineDistance);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          if (paramInt != 0) {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } else {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -paramConstraintWidget.getHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } 
        return;
      } 
      if (i != 0) {
        i = paramConstraintWidget.getHeight();
        resolutionAnchor2.setType(1);
        resolutionAnchor4.setType(1);
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor2.setType(3);
            resolutionAnchor4.setType(3);
            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0F);
            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0F);
          } else {
            resolutionAnchor2.setType(2);
            resolutionAnchor4.setType(2);
            resolutionAnchor2.setOpposite(resolutionAnchor4, -i);
            resolutionAnchor4.setOpposite(resolutionAnchor2, i);
            paramConstraintWidget.setHeight(i);
            if (paramConstraintWidget.mBaselineDistance > 0)
              paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
          } 
        } 
      } 
    } 
  }
  
  static boolean applyChainOptimized(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   5: astore #19
    //   7: aload #4
    //   9: getfield mLast : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   12: astore #20
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   19: astore #22
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   26: astore #23
    //   28: aload #4
    //   30: getfield mHead : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   33: astore #21
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #11
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   47: astore #24
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   54: astore #4
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: astore_0
    //   63: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   66: astore_0
    //   67: iload_2
    //   68: ifne -> 151
    //   71: aload #21
    //   73: getfield mHorizontalChainStyle : I
    //   76: ifne -> 85
    //   79: iconst_1
    //   80: istore #12
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #12
    //   88: aload #21
    //   90: getfield mHorizontalChainStyle : I
    //   93: iconst_1
    //   94: if_icmpne -> 103
    //   97: iconst_1
    //   98: istore #13
    //   100: goto -> 106
    //   103: iconst_0
    //   104: istore #13
    //   106: iload #12
    //   108: istore #15
    //   110: iload #13
    //   112: istore #14
    //   114: aload #21
    //   116: getfield mHorizontalChainStyle : I
    //   119: iconst_2
    //   120: if_icmpne -> 141
    //   123: iload #13
    //   125: istore #14
    //   127: iload #12
    //   129: istore #15
    //   131: iconst_1
    //   132: istore #12
    //   134: iload #15
    //   136: istore #13
    //   138: goto -> 214
    //   141: iconst_0
    //   142: istore #12
    //   144: iload #15
    //   146: istore #13
    //   148: goto -> 214
    //   151: aload #21
    //   153: getfield mVerticalChainStyle : I
    //   156: ifne -> 165
    //   159: iconst_1
    //   160: istore #12
    //   162: goto -> 168
    //   165: iconst_0
    //   166: istore #12
    //   168: aload #21
    //   170: getfield mVerticalChainStyle : I
    //   173: iconst_1
    //   174: if_icmpne -> 183
    //   177: iconst_1
    //   178: istore #13
    //   180: goto -> 186
    //   183: iconst_0
    //   184: istore #13
    //   186: iload #12
    //   188: istore #15
    //   190: iload #13
    //   192: istore #14
    //   194: aload #21
    //   196: getfield mVerticalChainStyle : I
    //   199: iconst_2
    //   200: if_icmpne -> 141
    //   203: iload #12
    //   205: istore #15
    //   207: iload #13
    //   209: istore #14
    //   211: goto -> 131
    //   214: aload #19
    //   216: astore #4
    //   218: iconst_0
    //   219: istore #17
    //   221: iconst_0
    //   222: istore #16
    //   224: iconst_0
    //   225: istore #15
    //   227: fconst_0
    //   228: fstore #9
    //   230: fconst_0
    //   231: fstore #8
    //   233: iload #16
    //   235: ifne -> 606
    //   238: iload #15
    //   240: istore #18
    //   242: fload #9
    //   244: fstore #5
    //   246: fload #8
    //   248: fstore #6
    //   250: aload #4
    //   252: invokevirtual getVisibility : ()I
    //   255: bipush #8
    //   257: if_icmpeq -> 381
    //   260: iload #15
    //   262: iconst_1
    //   263: iadd
    //   264: istore #18
    //   266: iload_2
    //   267: ifne -> 280
    //   270: aload #4
    //   272: invokevirtual getWidth : ()I
    //   275: istore #15
    //   277: goto -> 287
    //   280: aload #4
    //   282: invokevirtual getHeight : ()I
    //   285: istore #15
    //   287: fload #9
    //   289: iload #15
    //   291: i2f
    //   292: fadd
    //   293: fstore #5
    //   295: fload #5
    //   297: fstore #6
    //   299: aload #4
    //   301: aload #22
    //   303: if_acmpeq -> 322
    //   306: fload #5
    //   308: aload #4
    //   310: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   313: iload_3
    //   314: aaload
    //   315: invokevirtual getMargin : ()I
    //   318: i2f
    //   319: fadd
    //   320: fstore #6
    //   322: fload #6
    //   324: fstore #5
    //   326: aload #4
    //   328: aload #23
    //   330: if_acmpeq -> 351
    //   333: fload #6
    //   335: aload #4
    //   337: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   340: iload_3
    //   341: iconst_1
    //   342: iadd
    //   343: aaload
    //   344: invokevirtual getMargin : ()I
    //   347: i2f
    //   348: fadd
    //   349: fstore #5
    //   351: fload #8
    //   353: aload #4
    //   355: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   358: iload_3
    //   359: aaload
    //   360: invokevirtual getMargin : ()I
    //   363: i2f
    //   364: fadd
    //   365: aload #4
    //   367: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   370: iload_3
    //   371: iconst_1
    //   372: iadd
    //   373: aaload
    //   374: invokevirtual getMargin : ()I
    //   377: i2f
    //   378: fadd
    //   379: fstore #6
    //   381: aload #4
    //   383: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   386: iload_3
    //   387: aaload
    //   388: astore_0
    //   389: iload #17
    //   391: istore #15
    //   393: aload #4
    //   395: invokevirtual getVisibility : ()I
    //   398: bipush #8
    //   400: if_icmpeq -> 499
    //   403: iload #17
    //   405: istore #15
    //   407: aload #4
    //   409: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   412: iload_2
    //   413: aaload
    //   414: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   417: if_acmpne -> 499
    //   420: iload #17
    //   422: iconst_1
    //   423: iadd
    //   424: istore #15
    //   426: iload_2
    //   427: ifne -> 458
    //   430: aload #4
    //   432: getfield mMatchConstraintDefaultWidth : I
    //   435: ifeq -> 440
    //   438: iconst_0
    //   439: ireturn
    //   440: aload #4
    //   442: getfield mMatchConstraintMinWidth : I
    //   445: ifne -> 456
    //   448: aload #4
    //   450: getfield mMatchConstraintMaxWidth : I
    //   453: ifeq -> 487
    //   456: iconst_0
    //   457: ireturn
    //   458: aload #4
    //   460: getfield mMatchConstraintDefaultHeight : I
    //   463: ifeq -> 468
    //   466: iconst_0
    //   467: ireturn
    //   468: aload #4
    //   470: getfield mMatchConstraintMinHeight : I
    //   473: ifne -> 497
    //   476: aload #4
    //   478: getfield mMatchConstraintMaxHeight : I
    //   481: ifeq -> 487
    //   484: goto -> 497
    //   487: aload #4
    //   489: getfield mDimensionRatio : F
    //   492: fconst_0
    //   493: fcmpl
    //   494: ifeq -> 499
    //   497: iconst_0
    //   498: ireturn
    //   499: aload #4
    //   501: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   504: iload_3
    //   505: iconst_1
    //   506: iadd
    //   507: aaload
    //   508: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   511: astore_0
    //   512: aload_0
    //   513: ifnull -> 556
    //   516: aload_0
    //   517: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   520: astore_0
    //   521: aload_0
    //   522: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   525: iload_3
    //   526: aaload
    //   527: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   530: ifnull -> 556
    //   533: aload_0
    //   534: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   537: iload_3
    //   538: aaload
    //   539: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   542: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   545: aload #4
    //   547: if_acmpeq -> 553
    //   550: goto -> 556
    //   553: goto -> 558
    //   556: aconst_null
    //   557: astore_0
    //   558: aload_0
    //   559: ifnull -> 584
    //   562: iload #15
    //   564: istore #17
    //   566: aload_0
    //   567: astore #4
    //   569: iload #18
    //   571: istore #15
    //   573: fload #5
    //   575: fstore #9
    //   577: fload #6
    //   579: fstore #8
    //   581: goto -> 233
    //   584: iconst_1
    //   585: istore #16
    //   587: iload #15
    //   589: istore #17
    //   591: iload #18
    //   593: istore #15
    //   595: fload #5
    //   597: fstore #9
    //   599: fload #6
    //   601: fstore #8
    //   603: goto -> 233
    //   606: aload #19
    //   608: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   611: iload_3
    //   612: aaload
    //   613: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   616: astore #21
    //   618: aload #20
    //   620: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   623: astore_0
    //   624: iload_3
    //   625: iconst_1
    //   626: iadd
    //   627: istore #16
    //   629: aload_0
    //   630: iload #16
    //   632: aaload
    //   633: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   636: astore_0
    //   637: aload #21
    //   639: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   642: ifnull -> 1863
    //   645: aload_0
    //   646: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   649: ifnonnull -> 655
    //   652: goto -> 1863
    //   655: aload #21
    //   657: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   660: getfield state : I
    //   663: iconst_1
    //   664: if_icmpne -> 1861
    //   667: aload_0
    //   668: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   671: getfield state : I
    //   674: iconst_1
    //   675: if_icmpeq -> 681
    //   678: goto -> 1861
    //   681: iload #17
    //   683: ifle -> 695
    //   686: iload #17
    //   688: iload #15
    //   690: if_icmpeq -> 695
    //   693: iconst_0
    //   694: ireturn
    //   695: iload #12
    //   697: ifne -> 719
    //   700: iload #13
    //   702: ifne -> 719
    //   705: iload #14
    //   707: ifeq -> 713
    //   710: goto -> 719
    //   713: fconst_0
    //   714: fstore #5
    //   716: goto -> 769
    //   719: aload #22
    //   721: ifnull -> 740
    //   724: aload #22
    //   726: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   729: iload_3
    //   730: aaload
    //   731: invokevirtual getMargin : ()I
    //   734: i2f
    //   735: fstore #6
    //   737: goto -> 743
    //   740: fconst_0
    //   741: fstore #6
    //   743: fload #6
    //   745: fstore #5
    //   747: aload #23
    //   749: ifnull -> 769
    //   752: fload #6
    //   754: aload #23
    //   756: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   759: iload #16
    //   761: aaload
    //   762: invokevirtual getMargin : ()I
    //   765: i2f
    //   766: fadd
    //   767: fstore #5
    //   769: aload #21
    //   771: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   774: getfield resolvedOffset : F
    //   777: fstore #7
    //   779: aload_0
    //   780: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   783: getfield resolvedOffset : F
    //   786: fstore #6
    //   788: fload #7
    //   790: fload #6
    //   792: fcmpg
    //   793: ifge -> 806
    //   796: fload #6
    //   798: fload #7
    //   800: fsub
    //   801: fstore #6
    //   803: goto -> 813
    //   806: fload #7
    //   808: fload #6
    //   810: fsub
    //   811: fstore #6
    //   813: fload #6
    //   815: fload #9
    //   817: fsub
    //   818: fstore #10
    //   820: iload #17
    //   822: ifle -> 1138
    //   825: iload #17
    //   827: iload #15
    //   829: if_icmpne -> 1138
    //   832: aload #4
    //   834: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   837: ifnull -> 858
    //   840: aload #4
    //   842: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   845: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   848: iload_2
    //   849: aaload
    //   850: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   853: if_acmpne -> 858
    //   856: iconst_0
    //   857: ireturn
    //   858: fload #10
    //   860: fload #9
    //   862: fadd
    //   863: fload #8
    //   865: fsub
    //   866: fstore #6
    //   868: aload #19
    //   870: astore_0
    //   871: aload_0
    //   872: ifnull -> 1136
    //   875: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   878: ifnull -> 932
    //   881: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   884: astore #4
    //   886: aload #4
    //   888: aload #4
    //   890: getfield nonresolvedWidgets : J
    //   893: lconst_1
    //   894: lsub
    //   895: putfield nonresolvedWidgets : J
    //   898: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   901: astore #4
    //   903: aload #4
    //   905: aload #4
    //   907: getfield resolvedWidgets : J
    //   910: lconst_1
    //   911: ladd
    //   912: putfield resolvedWidgets : J
    //   915: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   918: astore #4
    //   920: aload #4
    //   922: aload #4
    //   924: getfield chainConnectionResolved : J
    //   927: lconst_1
    //   928: ladd
    //   929: putfield chainConnectionResolved : J
    //   932: aload_0
    //   933: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   936: iload_2
    //   937: aaload
    //   938: astore #4
    //   940: aload #4
    //   942: ifnonnull -> 955
    //   945: fload #7
    //   947: fstore #5
    //   949: aload_0
    //   950: aload #20
    //   952: if_acmpne -> 1126
    //   955: fload #6
    //   957: iload #17
    //   959: i2f
    //   960: fdiv
    //   961: fstore #5
    //   963: fload #11
    //   965: fconst_0
    //   966: fcmpl
    //   967: ifle -> 1002
    //   970: aload_0
    //   971: getfield mWeight : [F
    //   974: iload_2
    //   975: faload
    //   976: ldc -1.0
    //   978: fcmpl
    //   979: ifne -> 988
    //   982: fconst_0
    //   983: fstore #5
    //   985: goto -> 1002
    //   988: aload_0
    //   989: getfield mWeight : [F
    //   992: iload_2
    //   993: faload
    //   994: fload #6
    //   996: fmul
    //   997: fload #11
    //   999: fdiv
    //   1000: fstore #5
    //   1002: aload_0
    //   1003: invokevirtual getVisibility : ()I
    //   1006: bipush #8
    //   1008: if_icmpne -> 1014
    //   1011: fconst_0
    //   1012: fstore #5
    //   1014: fload #7
    //   1016: aload_0
    //   1017: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1020: iload_3
    //   1021: aaload
    //   1022: invokevirtual getMargin : ()I
    //   1025: i2f
    //   1026: fadd
    //   1027: fstore #7
    //   1029: aload_0
    //   1030: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1033: iload_3
    //   1034: aaload
    //   1035: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1038: aload #21
    //   1040: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1043: fload #7
    //   1045: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1048: aload_0
    //   1049: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1052: iload #16
    //   1054: aaload
    //   1055: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1058: astore #19
    //   1060: aload #21
    //   1062: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1065: astore #22
    //   1067: fload #7
    //   1069: fload #5
    //   1071: fadd
    //   1072: fstore #5
    //   1074: aload #19
    //   1076: aload #22
    //   1078: fload #5
    //   1080: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1083: aload_0
    //   1084: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1087: iload_3
    //   1088: aaload
    //   1089: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1092: aload_1
    //   1093: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1096: aload_0
    //   1097: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1100: iload #16
    //   1102: aaload
    //   1103: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1106: aload_1
    //   1107: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1110: fload #5
    //   1112: aload_0
    //   1113: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1116: iload #16
    //   1118: aaload
    //   1119: invokevirtual getMargin : ()I
    //   1122: i2f
    //   1123: fadd
    //   1124: fstore #5
    //   1126: aload #4
    //   1128: astore_0
    //   1129: fload #5
    //   1131: fstore #7
    //   1133: goto -> 871
    //   1136: iconst_1
    //   1137: ireturn
    //   1138: fload #10
    //   1140: fconst_0
    //   1141: fcmpg
    //   1142: ifge -> 1154
    //   1145: iconst_1
    //   1146: istore #12
    //   1148: iconst_0
    //   1149: istore #13
    //   1151: iconst_0
    //   1152: istore #14
    //   1154: iload #12
    //   1156: ifeq -> 1406
    //   1159: aload #19
    //   1161: astore_0
    //   1162: fload #7
    //   1164: fload #10
    //   1166: fload #5
    //   1168: fsub
    //   1169: aload_0
    //   1170: iload_2
    //   1171: invokevirtual getBiasPercent : (I)F
    //   1174: fmul
    //   1175: fadd
    //   1176: fstore #5
    //   1178: aload_0
    //   1179: astore #4
    //   1181: aload #4
    //   1183: ifnull -> 1419
    //   1186: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1189: ifnull -> 1234
    //   1192: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1195: astore_0
    //   1196: aload_0
    //   1197: aload_0
    //   1198: getfield nonresolvedWidgets : J
    //   1201: lconst_1
    //   1202: lsub
    //   1203: putfield nonresolvedWidgets : J
    //   1206: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1209: astore_0
    //   1210: aload_0
    //   1211: aload_0
    //   1212: getfield resolvedWidgets : J
    //   1215: lconst_1
    //   1216: ladd
    //   1217: putfield resolvedWidgets : J
    //   1220: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1223: astore_0
    //   1224: aload_0
    //   1225: aload_0
    //   1226: getfield chainConnectionResolved : J
    //   1229: lconst_1
    //   1230: ladd
    //   1231: putfield chainConnectionResolved : J
    //   1234: aload #4
    //   1236: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1239: iload_2
    //   1240: aaload
    //   1241: astore #19
    //   1243: aload #19
    //   1245: ifnonnull -> 1258
    //   1248: aload #19
    //   1250: astore_0
    //   1251: aload #4
    //   1253: aload #20
    //   1255: if_acmpne -> 1178
    //   1258: iload_2
    //   1259: ifne -> 1272
    //   1262: aload #4
    //   1264: invokevirtual getWidth : ()I
    //   1267: istore #12
    //   1269: goto -> 1279
    //   1272: aload #4
    //   1274: invokevirtual getHeight : ()I
    //   1277: istore #12
    //   1279: iload #12
    //   1281: i2f
    //   1282: fstore #6
    //   1284: fload #5
    //   1286: aload #4
    //   1288: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1291: iload_3
    //   1292: aaload
    //   1293: invokevirtual getMargin : ()I
    //   1296: i2f
    //   1297: fadd
    //   1298: fstore #5
    //   1300: aload #4
    //   1302: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1305: iload_3
    //   1306: aaload
    //   1307: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1310: aload #21
    //   1312: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1315: fload #5
    //   1317: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1320: aload #4
    //   1322: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1325: iload #16
    //   1327: aaload
    //   1328: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1331: astore_0
    //   1332: aload #21
    //   1334: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1337: astore #22
    //   1339: fload #5
    //   1341: fload #6
    //   1343: fadd
    //   1344: fstore #5
    //   1346: aload_0
    //   1347: aload #22
    //   1349: fload #5
    //   1351: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1354: aload #4
    //   1356: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1359: iload_3
    //   1360: aaload
    //   1361: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1364: aload_1
    //   1365: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1368: aload #4
    //   1370: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1373: iload #16
    //   1375: aaload
    //   1376: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1379: aload_1
    //   1380: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1383: fload #5
    //   1385: aload #4
    //   1387: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1390: iload #16
    //   1392: aaload
    //   1393: invokevirtual getMargin : ()I
    //   1396: i2f
    //   1397: fadd
    //   1398: fstore #5
    //   1400: aload #19
    //   1402: astore_0
    //   1403: goto -> 1178
    //   1406: iload #13
    //   1408: ifne -> 1422
    //   1411: iload #14
    //   1413: ifeq -> 1419
    //   1416: goto -> 1422
    //   1419: goto -> 1859
    //   1422: iload #13
    //   1424: ifeq -> 1437
    //   1427: fload #10
    //   1429: fload #5
    //   1431: fsub
    //   1432: fstore #6
    //   1434: goto -> 1449
    //   1437: fload #10
    //   1439: fstore #6
    //   1441: iload #14
    //   1443: ifeq -> 1449
    //   1446: goto -> 1427
    //   1449: fload #6
    //   1451: iload #15
    //   1453: iconst_1
    //   1454: iadd
    //   1455: i2f
    //   1456: fdiv
    //   1457: fstore #8
    //   1459: iload #14
    //   1461: ifeq -> 1490
    //   1464: iload #15
    //   1466: iconst_1
    //   1467: if_icmple -> 1480
    //   1470: iload #15
    //   1472: iconst_1
    //   1473: isub
    //   1474: i2f
    //   1475: fstore #5
    //   1477: goto -> 1483
    //   1480: fconst_2
    //   1481: fstore #5
    //   1483: fload #6
    //   1485: fload #5
    //   1487: fdiv
    //   1488: fstore #8
    //   1490: aload #19
    //   1492: invokevirtual getVisibility : ()I
    //   1495: bipush #8
    //   1497: if_icmpeq -> 1510
    //   1500: fload #7
    //   1502: fload #8
    //   1504: fadd
    //   1505: fstore #5
    //   1507: goto -> 1514
    //   1510: fload #7
    //   1512: fstore #5
    //   1514: fload #5
    //   1516: fstore #6
    //   1518: iload #14
    //   1520: ifeq -> 1549
    //   1523: fload #5
    //   1525: fstore #6
    //   1527: iload #15
    //   1529: iconst_1
    //   1530: if_icmple -> 1549
    //   1533: aload #22
    //   1535: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1538: iload_3
    //   1539: aaload
    //   1540: invokevirtual getMargin : ()I
    //   1543: i2f
    //   1544: fload #7
    //   1546: fadd
    //   1547: fstore #6
    //   1549: aload #19
    //   1551: astore_0
    //   1552: fload #6
    //   1554: fstore #5
    //   1556: iload #13
    //   1558: ifeq -> 1592
    //   1561: aload #19
    //   1563: astore_0
    //   1564: fload #6
    //   1566: fstore #5
    //   1568: aload #22
    //   1570: ifnull -> 1592
    //   1573: fload #6
    //   1575: aload #22
    //   1577: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1580: iload_3
    //   1581: aaload
    //   1582: invokevirtual getMargin : ()I
    //   1585: i2f
    //   1586: fadd
    //   1587: fstore #5
    //   1589: aload #19
    //   1591: astore_0
    //   1592: aload_0
    //   1593: ifnull -> 1419
    //   1596: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1599: ifnull -> 1653
    //   1602: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1605: astore #4
    //   1607: aload #4
    //   1609: aload #4
    //   1611: getfield nonresolvedWidgets : J
    //   1614: lconst_1
    //   1615: lsub
    //   1616: putfield nonresolvedWidgets : J
    //   1619: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1622: astore #4
    //   1624: aload #4
    //   1626: aload #4
    //   1628: getfield resolvedWidgets : J
    //   1631: lconst_1
    //   1632: ladd
    //   1633: putfield resolvedWidgets : J
    //   1636: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1639: astore #4
    //   1641: aload #4
    //   1643: aload #4
    //   1645: getfield chainConnectionResolved : J
    //   1648: lconst_1
    //   1649: ladd
    //   1650: putfield chainConnectionResolved : J
    //   1653: aload_0
    //   1654: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1657: iload_2
    //   1658: aaload
    //   1659: astore #4
    //   1661: aload #4
    //   1663: ifnonnull -> 1686
    //   1666: fload #5
    //   1668: fstore #6
    //   1670: aload_0
    //   1671: aload #20
    //   1673: if_acmpne -> 1679
    //   1676: goto -> 1686
    //   1679: fload #6
    //   1681: fstore #5
    //   1683: goto -> 1853
    //   1686: iload_2
    //   1687: ifne -> 1699
    //   1690: aload_0
    //   1691: invokevirtual getWidth : ()I
    //   1694: istore #12
    //   1696: goto -> 1705
    //   1699: aload_0
    //   1700: invokevirtual getHeight : ()I
    //   1703: istore #12
    //   1705: iload #12
    //   1707: i2f
    //   1708: fstore #7
    //   1710: fload #5
    //   1712: fstore #6
    //   1714: aload_0
    //   1715: aload #22
    //   1717: if_acmpeq -> 1735
    //   1720: fload #5
    //   1722: aload_0
    //   1723: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1726: iload_3
    //   1727: aaload
    //   1728: invokevirtual getMargin : ()I
    //   1731: i2f
    //   1732: fadd
    //   1733: fstore #6
    //   1735: aload_0
    //   1736: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1739: iload_3
    //   1740: aaload
    //   1741: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1744: aload #21
    //   1746: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1749: fload #6
    //   1751: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1754: aload_0
    //   1755: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1758: iload #16
    //   1760: aaload
    //   1761: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1764: aload #21
    //   1766: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1769: fload #6
    //   1771: fload #7
    //   1773: fadd
    //   1774: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1777: aload_0
    //   1778: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1781: iload_3
    //   1782: aaload
    //   1783: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1786: aload_1
    //   1787: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1790: aload_0
    //   1791: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1794: iload #16
    //   1796: aaload
    //   1797: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1800: aload_1
    //   1801: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1804: fload #6
    //   1806: fload #7
    //   1808: aload_0
    //   1809: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1812: iload #16
    //   1814: aaload
    //   1815: invokevirtual getMargin : ()I
    //   1818: i2f
    //   1819: fadd
    //   1820: fadd
    //   1821: fstore #7
    //   1823: fload #7
    //   1825: fstore #6
    //   1827: aload #4
    //   1829: ifnull -> 1679
    //   1832: fload #7
    //   1834: fstore #5
    //   1836: aload #4
    //   1838: invokevirtual getVisibility : ()I
    //   1841: bipush #8
    //   1843: if_icmpeq -> 1853
    //   1846: fload #7
    //   1848: fload #8
    //   1850: fadd
    //   1851: fstore #5
    //   1853: aload #4
    //   1855: astore_0
    //   1856: goto -> 1592
    //   1859: iconst_1
    //   1860: ireturn
    //   1861: iconst_0
    //   1862: ireturn
    //   1863: iconst_0
    //   1864: ireturn
  }
  
  static void checkMatchParent(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int j = paramConstraintWidget.mLeft.mMargin;
      int i = paramConstraintWidgetContainer.getWidth() - paramConstraintWidget.mRight.mMargin;
      paramConstraintWidget.mLeft.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mLeft);
      paramConstraintWidget.mRight.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mRight);
      paramLinearSystem.addEquality(paramConstraintWidget.mLeft.mSolverVariable, j);
      paramLinearSystem.addEquality(paramConstraintWidget.mRight.mSolverVariable, i);
      paramConstraintWidget.mHorizontalResolution = 2;
      paramConstraintWidget.setHorizontalDimension(j, i);
    } 
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mTop.mMargin;
      int j = paramConstraintWidgetContainer.getHeight() - paramConstraintWidget.mBottom.mMargin;
      paramConstraintWidget.mTop.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mTop);
      paramConstraintWidget.mBottom.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBottom);
      paramLinearSystem.addEquality(paramConstraintWidget.mTop.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mBottom.mSolverVariable, j);
      if (paramConstraintWidget.mBaselineDistance > 0 || paramConstraintWidget.getVisibility() == 8) {
        paramConstraintWidget.mBaseline.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBaseline);
        paramLinearSystem.addEquality(paramConstraintWidget.mBaseline.mSolverVariable, paramConstraintWidget.mBaselineDistance + i);
      } 
      paramConstraintWidget.mVerticalResolution = 2;
      paramConstraintWidget.setVerticalDimension(i, j);
    } 
  }
  
  private static boolean optimizableMatchConstraint(ConstraintWidget paramConstraintWidget, int paramInt) {
    ConstraintWidget.DimensionBehaviour[] arrayOfDimensionBehaviour;
    if (paramConstraintWidget.mListDimensionBehaviors[paramInt] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      return false; 
    float f = paramConstraintWidget.mDimensionRatio;
    boolean bool = true;
    if (f != 0.0F) {
      arrayOfDimensionBehaviour = paramConstraintWidget.mListDimensionBehaviors;
      if (paramInt == 0) {
        paramInt = bool;
      } else {
        paramInt = 0;
      } 
      if (arrayOfDimensionBehaviour[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
      return false;
    } 
    if (paramInt == 0) {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultWidth != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinWidth != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxWidth != 0)
        return false; 
    } else {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultHeight != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinHeight != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxHeight != 0)
        return false; 
    } 
    return true;
  }
  
  static void setOptimizedWidget(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    int j = i + 1;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedTarget = (paramConstraintWidget.getParent()).mLeft.getResolutionNode();
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset = paramInt2;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).state = 1;
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedTarget = paramConstraintWidget.mListAnchors[i].getResolutionNode();
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset = paramConstraintWidget.getLength(paramInt1);
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).state = 1;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\Optimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */