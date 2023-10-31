package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

class Chain {
  private static final boolean DEBUG = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt) {
    int i;
    byte b1;
    ChainHead[] arrayOfChainHead;
    byte b2 = 0;
    if (paramInt == 0) {
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
      b1 = 0;
    } else {
      b1 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    while (b2 < i) {
      ChainHead chainHead = arrayOfChainHead[b2];
      chainHead.define();
      if (paramConstraintWidgetContainer.optimizeFor(4)) {
        if (!Optimizer.applyChainOptimized(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead))
          applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead); 
      } else {
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead);
      } 
      b2++;
    } 
  }
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   5: astore #25
    //   7: aload #4
    //   9: getfield mLast : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   12: astore #22
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   19: astore #19
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   26: astore #23
    //   28: aload #4
    //   30: getfield mHead : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   33: astore #17
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #5
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   47: astore #16
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   54: astore #16
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #12
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #12
    //   77: iload_2
    //   78: ifne -> 136
    //   81: aload #17
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #8
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #8
    //   98: aload #17
    //   100: getfield mHorizontalChainStyle : I
    //   103: iconst_1
    //   104: if_icmpne -> 113
    //   107: iconst_1
    //   108: istore #9
    //   110: goto -> 116
    //   113: iconst_0
    //   114: istore #9
    //   116: iload #8
    //   118: istore #10
    //   120: iload #9
    //   122: istore #11
    //   124: aload #17
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 198
    //   133: goto -> 188
    //   136: aload #17
    //   138: getfield mVerticalChainStyle : I
    //   141: ifne -> 150
    //   144: iconst_1
    //   145: istore #8
    //   147: goto -> 153
    //   150: iconst_0
    //   151: istore #8
    //   153: aload #17
    //   155: getfield mVerticalChainStyle : I
    //   158: iconst_1
    //   159: if_icmpne -> 168
    //   162: iconst_1
    //   163: istore #9
    //   165: goto -> 171
    //   168: iconst_0
    //   169: istore #9
    //   171: iload #8
    //   173: istore #10
    //   175: iload #9
    //   177: istore #11
    //   179: aload #17
    //   181: getfield mVerticalChainStyle : I
    //   184: iconst_2
    //   185: if_icmpne -> 198
    //   188: iconst_1
    //   189: istore #13
    //   191: iload #8
    //   193: istore #10
    //   195: goto -> 205
    //   198: iconst_0
    //   199: istore #13
    //   201: iload #11
    //   203: istore #9
    //   205: aload #25
    //   207: astore #18
    //   209: iconst_0
    //   210: istore #8
    //   212: iload #9
    //   214: istore #11
    //   216: aconst_null
    //   217: astore #24
    //   219: aconst_null
    //   220: astore #20
    //   222: iload #8
    //   224: ifne -> 606
    //   227: aload #18
    //   229: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   232: iload_3
    //   233: aaload
    //   234: astore #16
    //   236: iload #12
    //   238: ifne -> 255
    //   241: iload #13
    //   243: ifeq -> 249
    //   246: goto -> 255
    //   249: iconst_4
    //   250: istore #9
    //   252: goto -> 258
    //   255: iconst_1
    //   256: istore #9
    //   258: aload #16
    //   260: invokevirtual getMargin : ()I
    //   263: istore #15
    //   265: iload #15
    //   267: istore #14
    //   269: aload #16
    //   271: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   274: ifnull -> 301
    //   277: iload #15
    //   279: istore #14
    //   281: aload #18
    //   283: aload #25
    //   285: if_acmpeq -> 301
    //   288: iload #15
    //   290: aload #16
    //   292: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   295: invokevirtual getMargin : ()I
    //   298: iadd
    //   299: istore #14
    //   301: iload #13
    //   303: ifeq -> 327
    //   306: aload #18
    //   308: aload #25
    //   310: if_acmpeq -> 327
    //   313: aload #18
    //   315: aload #19
    //   317: if_acmpeq -> 327
    //   320: bipush #6
    //   322: istore #9
    //   324: goto -> 343
    //   327: iload #10
    //   329: ifeq -> 343
    //   332: iload #12
    //   334: ifeq -> 343
    //   337: iconst_4
    //   338: istore #9
    //   340: goto -> 343
    //   343: aload #16
    //   345: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   348: ifnull -> 427
    //   351: aload #18
    //   353: aload #19
    //   355: if_acmpne -> 381
    //   358: aload_1
    //   359: aload #16
    //   361: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   364: aload #16
    //   366: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   369: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   372: iload #14
    //   374: iconst_5
    //   375: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   378: goto -> 402
    //   381: aload_1
    //   382: aload #16
    //   384: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   387: aload #16
    //   389: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   392: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   395: iload #14
    //   397: bipush #6
    //   399: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   402: aload_1
    //   403: aload #16
    //   405: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   408: aload #16
    //   410: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   413: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   416: iload #14
    //   418: iload #9
    //   420: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   423: pop
    //   424: goto -> 427
    //   427: iload #12
    //   429: ifeq -> 512
    //   432: aload #18
    //   434: invokevirtual getVisibility : ()I
    //   437: bipush #8
    //   439: if_icmpeq -> 486
    //   442: aload #18
    //   444: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   447: iload_2
    //   448: aaload
    //   449: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   452: if_acmpne -> 486
    //   455: aload_1
    //   456: aload #18
    //   458: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   461: iload_3
    //   462: iconst_1
    //   463: iadd
    //   464: aaload
    //   465: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   468: aload #18
    //   470: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   473: iload_3
    //   474: aaload
    //   475: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   478: iconst_0
    //   479: iconst_5
    //   480: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   483: goto -> 486
    //   486: aload_1
    //   487: aload #18
    //   489: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   492: iload_3
    //   493: aaload
    //   494: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   497: aload_0
    //   498: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   501: iload_3
    //   502: aaload
    //   503: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   506: iconst_0
    //   507: bipush #6
    //   509: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   512: aload #18
    //   514: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   517: iload_3
    //   518: iconst_1
    //   519: iadd
    //   520: aaload
    //   521: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   524: astore #21
    //   526: aload #20
    //   528: astore #16
    //   530: aload #21
    //   532: ifnull -> 588
    //   535: aload #21
    //   537: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   540: astore #21
    //   542: aload #20
    //   544: astore #16
    //   546: aload #21
    //   548: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   551: iload_3
    //   552: aaload
    //   553: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   556: ifnull -> 588
    //   559: aload #21
    //   561: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   564: iload_3
    //   565: aaload
    //   566: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   569: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   572: aload #18
    //   574: if_acmpeq -> 584
    //   577: aload #20
    //   579: astore #16
    //   581: goto -> 588
    //   584: aload #21
    //   586: astore #16
    //   588: aload #16
    //   590: ifnull -> 600
    //   593: aload #16
    //   595: astore #18
    //   597: goto -> 603
    //   600: iconst_1
    //   601: istore #8
    //   603: goto -> 216
    //   606: aload #23
    //   608: ifnull -> 677
    //   611: aload #22
    //   613: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   616: astore #16
    //   618: iload_3
    //   619: iconst_1
    //   620: iadd
    //   621: istore #8
    //   623: aload #16
    //   625: iload #8
    //   627: aaload
    //   628: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   631: ifnull -> 677
    //   634: aload #23
    //   636: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   639: iload #8
    //   641: aaload
    //   642: astore #16
    //   644: aload_1
    //   645: aload #16
    //   647: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   650: aload #22
    //   652: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   655: iload #8
    //   657: aaload
    //   658: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   661: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   664: aload #16
    //   666: invokevirtual getMargin : ()I
    //   669: ineg
    //   670: iconst_5
    //   671: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   674: goto -> 677
    //   677: iload #12
    //   679: ifeq -> 727
    //   682: aload_0
    //   683: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   686: astore_0
    //   687: iload_3
    //   688: iconst_1
    //   689: iadd
    //   690: istore #8
    //   692: aload_1
    //   693: aload_0
    //   694: iload #8
    //   696: aaload
    //   697: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   700: aload #22
    //   702: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   705: iload #8
    //   707: aaload
    //   708: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   711: aload #22
    //   713: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   716: iload #8
    //   718: aaload
    //   719: invokevirtual getMargin : ()I
    //   722: bipush #6
    //   724: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   727: aload #4
    //   729: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   732: astore_0
    //   733: aload_0
    //   734: ifnull -> 1026
    //   737: aload_0
    //   738: invokevirtual size : ()I
    //   741: istore #8
    //   743: iload #8
    //   745: iconst_1
    //   746: if_icmple -> 1026
    //   749: aload #4
    //   751: getfield mHasUndefinedWeights : Z
    //   754: ifeq -> 776
    //   757: aload #4
    //   759: getfield mHasComplexMatchWeights : Z
    //   762: ifne -> 776
    //   765: aload #4
    //   767: getfield mWidgetsMatchCount : I
    //   770: i2f
    //   771: fstore #6
    //   773: goto -> 780
    //   776: fload #5
    //   778: fstore #6
    //   780: fconst_0
    //   781: fstore #7
    //   783: aconst_null
    //   784: astore #16
    //   786: iconst_0
    //   787: istore #9
    //   789: iload #9
    //   791: iload #8
    //   793: if_icmpge -> 1026
    //   796: aload_0
    //   797: iload #9
    //   799: invokevirtual get : (I)Ljava/lang/Object;
    //   802: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidget
    //   805: astore #18
    //   807: aload #18
    //   809: getfield mWeight : [F
    //   812: iload_2
    //   813: faload
    //   814: fstore #5
    //   816: fload #5
    //   818: fconst_0
    //   819: fcmpg
    //   820: ifge -> 869
    //   823: aload #4
    //   825: getfield mHasComplexMatchWeights : Z
    //   828: ifeq -> 863
    //   831: aload_1
    //   832: aload #18
    //   834: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   837: iload_3
    //   838: iconst_1
    //   839: iadd
    //   840: aaload
    //   841: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   844: aload #18
    //   846: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   849: iload_3
    //   850: aaload
    //   851: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   854: iconst_0
    //   855: iconst_4
    //   856: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   859: pop
    //   860: goto -> 906
    //   863: fconst_1
    //   864: fstore #5
    //   866: goto -> 869
    //   869: fload #5
    //   871: fconst_0
    //   872: fcmpl
    //   873: ifne -> 913
    //   876: aload_1
    //   877: aload #18
    //   879: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   882: iload_3
    //   883: iconst_1
    //   884: iadd
    //   885: aaload
    //   886: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   889: aload #18
    //   891: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   894: iload_3
    //   895: aaload
    //   896: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   899: iconst_0
    //   900: bipush #6
    //   902: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   905: pop
    //   906: fload #7
    //   908: fstore #5
    //   910: goto -> 1016
    //   913: aload #16
    //   915: ifnull -> 1012
    //   918: aload #16
    //   920: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   923: iload_3
    //   924: aaload
    //   925: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   928: astore #20
    //   930: aload #16
    //   932: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   935: astore #16
    //   937: iload_3
    //   938: iconst_1
    //   939: iadd
    //   940: istore #12
    //   942: aload #16
    //   944: iload #12
    //   946: aaload
    //   947: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   950: astore #27
    //   952: aload #18
    //   954: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   957: iload_3
    //   958: aaload
    //   959: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   962: astore #16
    //   964: aload #18
    //   966: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   969: iload #12
    //   971: aaload
    //   972: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   975: astore #21
    //   977: aload_1
    //   978: invokevirtual createRow : ()Landroidx/constraintlayout/solver/ArrayRow;
    //   981: astore #26
    //   983: aload #26
    //   985: fload #7
    //   987: fload #6
    //   989: fload #5
    //   991: aload #20
    //   993: aload #27
    //   995: aload #16
    //   997: aload #21
    //   999: invokevirtual createRowEqualMatchDimensions : (FFFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;)Landroidx/constraintlayout/solver/ArrayRow;
    //   1002: pop
    //   1003: aload_1
    //   1004: aload #26
    //   1006: invokevirtual addConstraint : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   1009: goto -> 1012
    //   1012: aload #18
    //   1014: astore #16
    //   1016: iinc #9, 1
    //   1019: fload #5
    //   1021: fstore #7
    //   1023: goto -> 789
    //   1026: aload #19
    //   1028: ifnull -> 1232
    //   1031: aload #19
    //   1033: aload #23
    //   1035: if_acmpeq -> 1043
    //   1038: iload #13
    //   1040: ifeq -> 1232
    //   1043: aload #25
    //   1045: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1048: iload_3
    //   1049: aaload
    //   1050: astore #16
    //   1052: aload #22
    //   1054: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1057: astore_0
    //   1058: iload_3
    //   1059: iconst_1
    //   1060: iadd
    //   1061: istore #8
    //   1063: aload_0
    //   1064: iload #8
    //   1066: aaload
    //   1067: astore #18
    //   1069: aload #25
    //   1071: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1074: iload_3
    //   1075: aaload
    //   1076: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1079: ifnull -> 1099
    //   1082: aload #25
    //   1084: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1087: iload_3
    //   1088: aaload
    //   1089: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1092: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1095: astore_0
    //   1096: goto -> 1101
    //   1099: aconst_null
    //   1100: astore_0
    //   1101: aload #22
    //   1103: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1106: iload #8
    //   1108: aaload
    //   1109: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1112: ifnull -> 1134
    //   1115: aload #22
    //   1117: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1120: iload #8
    //   1122: aaload
    //   1123: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1126: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1129: astore #4
    //   1131: goto -> 1137
    //   1134: aconst_null
    //   1135: astore #4
    //   1137: aload #19
    //   1139: aload #23
    //   1141: if_acmpne -> 1163
    //   1144: aload #19
    //   1146: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1149: iload_3
    //   1150: aaload
    //   1151: astore #16
    //   1153: aload #19
    //   1155: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1158: iload #8
    //   1160: aaload
    //   1161: astore #18
    //   1163: aload_0
    //   1164: ifnull -> 2319
    //   1167: aload #4
    //   1169: ifnull -> 2319
    //   1172: iload_2
    //   1173: ifne -> 1186
    //   1176: aload #17
    //   1178: getfield mHorizontalBiasPercent : F
    //   1181: fstore #5
    //   1183: goto -> 1193
    //   1186: aload #17
    //   1188: getfield mVerticalBiasPercent : F
    //   1191: fstore #5
    //   1193: aload #16
    //   1195: invokevirtual getMargin : ()I
    //   1198: istore_2
    //   1199: aload #18
    //   1201: invokevirtual getMargin : ()I
    //   1204: istore #8
    //   1206: aload_1
    //   1207: aload #16
    //   1209: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1212: aload_0
    //   1213: iload_2
    //   1214: fload #5
    //   1216: aload #4
    //   1218: aload #18
    //   1220: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1223: iload #8
    //   1225: iconst_5
    //   1226: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1229: goto -> 2319
    //   1232: iload #10
    //   1234: ifeq -> 1732
    //   1237: aload #19
    //   1239: ifnull -> 1732
    //   1242: aload #4
    //   1244: getfield mWidgetsMatchCount : I
    //   1247: ifle -> 1269
    //   1250: aload #4
    //   1252: getfield mWidgetsCount : I
    //   1255: aload #4
    //   1257: getfield mWidgetsMatchCount : I
    //   1260: if_icmpne -> 1269
    //   1263: iconst_1
    //   1264: istore #12
    //   1266: goto -> 1272
    //   1269: iconst_0
    //   1270: istore #12
    //   1272: aload #19
    //   1274: astore #4
    //   1276: aload #4
    //   1278: astore #18
    //   1280: aload #4
    //   1282: ifnull -> 2319
    //   1285: aload #4
    //   1287: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1290: iload_2
    //   1291: aaload
    //   1292: astore #16
    //   1294: aload #16
    //   1296: ifnull -> 1321
    //   1299: aload #16
    //   1301: invokevirtual getVisibility : ()I
    //   1304: bipush #8
    //   1306: if_icmpne -> 1321
    //   1309: aload #16
    //   1311: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1314: iload_2
    //   1315: aaload
    //   1316: astore #16
    //   1318: goto -> 1294
    //   1321: aload #16
    //   1323: ifnonnull -> 1339
    //   1326: aload #4
    //   1328: aload #23
    //   1330: if_acmpne -> 1336
    //   1333: goto -> 1339
    //   1336: goto -> 1711
    //   1339: aload #4
    //   1341: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1344: iload_3
    //   1345: aaload
    //   1346: astore #20
    //   1348: aload #20
    //   1350: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1353: astore #27
    //   1355: aload #20
    //   1357: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1360: ifnull -> 1376
    //   1363: aload #20
    //   1365: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1368: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1371: astore #17
    //   1373: goto -> 1379
    //   1376: aconst_null
    //   1377: astore #17
    //   1379: aload #18
    //   1381: aload #4
    //   1383: if_acmpeq -> 1402
    //   1386: aload #18
    //   1388: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1391: iload_3
    //   1392: iconst_1
    //   1393: iadd
    //   1394: aaload
    //   1395: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1398: astore_0
    //   1399: goto -> 1454
    //   1402: aload #17
    //   1404: astore_0
    //   1405: aload #4
    //   1407: aload #19
    //   1409: if_acmpne -> 1454
    //   1412: aload #17
    //   1414: astore_0
    //   1415: aload #18
    //   1417: aload #4
    //   1419: if_acmpne -> 1454
    //   1422: aload #25
    //   1424: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1427: iload_3
    //   1428: aaload
    //   1429: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1432: ifnull -> 1452
    //   1435: aload #25
    //   1437: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1440: iload_3
    //   1441: aaload
    //   1442: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1445: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1448: astore_0
    //   1449: goto -> 1454
    //   1452: aconst_null
    //   1453: astore_0
    //   1454: aload #20
    //   1456: invokevirtual getMargin : ()I
    //   1459: istore #13
    //   1461: aload #4
    //   1463: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1466: astore #17
    //   1468: iload_3
    //   1469: iconst_1
    //   1470: iadd
    //   1471: istore #14
    //   1473: aload #17
    //   1475: iload #14
    //   1477: aaload
    //   1478: invokevirtual getMargin : ()I
    //   1481: istore #9
    //   1483: aload #16
    //   1485: ifnull -> 1520
    //   1488: aload #16
    //   1490: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1493: iload_3
    //   1494: aaload
    //   1495: astore #17
    //   1497: aload #17
    //   1499: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1502: astore #21
    //   1504: aload #4
    //   1506: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1509: iload #14
    //   1511: aaload
    //   1512: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1515: astore #20
    //   1517: goto -> 1572
    //   1520: aload #22
    //   1522: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1525: iload #14
    //   1527: aaload
    //   1528: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1531: astore #26
    //   1533: aload #26
    //   1535: ifnull -> 1548
    //   1538: aload #26
    //   1540: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1543: astore #17
    //   1545: goto -> 1551
    //   1548: aconst_null
    //   1549: astore #17
    //   1551: aload #4
    //   1553: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1556: iload #14
    //   1558: aaload
    //   1559: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1562: astore #20
    //   1564: aload #17
    //   1566: astore #21
    //   1568: aload #26
    //   1570: astore #17
    //   1572: iload #9
    //   1574: istore #8
    //   1576: aload #17
    //   1578: ifnull -> 1591
    //   1581: iload #9
    //   1583: aload #17
    //   1585: invokevirtual getMargin : ()I
    //   1588: iadd
    //   1589: istore #8
    //   1591: iload #13
    //   1593: istore #9
    //   1595: aload #18
    //   1597: ifnull -> 1616
    //   1600: iload #13
    //   1602: aload #18
    //   1604: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1607: iload #14
    //   1609: aaload
    //   1610: invokevirtual getMargin : ()I
    //   1613: iadd
    //   1614: istore #9
    //   1616: aload #27
    //   1618: ifnull -> 1336
    //   1621: aload_0
    //   1622: ifnull -> 1336
    //   1625: aload #21
    //   1627: ifnull -> 1336
    //   1630: aload #20
    //   1632: ifnull -> 1336
    //   1635: aload #4
    //   1637: aload #19
    //   1639: if_acmpne -> 1654
    //   1642: aload #19
    //   1644: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1647: iload_3
    //   1648: aaload
    //   1649: invokevirtual getMargin : ()I
    //   1652: istore #9
    //   1654: aload #4
    //   1656: aload #23
    //   1658: if_acmpne -> 1677
    //   1661: aload #23
    //   1663: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1666: iload #14
    //   1668: aaload
    //   1669: invokevirtual getMargin : ()I
    //   1672: istore #8
    //   1674: goto -> 1677
    //   1677: iload #12
    //   1679: ifeq -> 1689
    //   1682: bipush #6
    //   1684: istore #13
    //   1686: goto -> 1692
    //   1689: iconst_4
    //   1690: istore #13
    //   1692: aload_1
    //   1693: aload #27
    //   1695: aload_0
    //   1696: iload #9
    //   1698: ldc 0.5
    //   1700: aload #21
    //   1702: aload #20
    //   1704: iload #8
    //   1706: iload #13
    //   1708: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1711: aload #4
    //   1713: invokevirtual getVisibility : ()I
    //   1716: bipush #8
    //   1718: if_icmpeq -> 1725
    //   1721: aload #4
    //   1723: astore #18
    //   1725: aload #16
    //   1727: astore #4
    //   1729: goto -> 1280
    //   1732: bipush #8
    //   1734: istore #8
    //   1736: iload #11
    //   1738: ifeq -> 2319
    //   1741: aload #19
    //   1743: ifnull -> 2319
    //   1746: aload #4
    //   1748: getfield mWidgetsMatchCount : I
    //   1751: ifle -> 1773
    //   1754: aload #4
    //   1756: getfield mWidgetsCount : I
    //   1759: aload #4
    //   1761: getfield mWidgetsMatchCount : I
    //   1764: if_icmpne -> 1773
    //   1767: iconst_1
    //   1768: istore #9
    //   1770: goto -> 1776
    //   1773: iconst_0
    //   1774: istore #9
    //   1776: aload #19
    //   1778: astore #4
    //   1780: aload #4
    //   1782: astore #16
    //   1784: aload #4
    //   1786: ifnull -> 2159
    //   1789: aload #4
    //   1791: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1794: iload_2
    //   1795: aaload
    //   1796: astore_0
    //   1797: aload_0
    //   1798: ifnull -> 1820
    //   1801: aload_0
    //   1802: invokevirtual getVisibility : ()I
    //   1805: iload #8
    //   1807: if_icmpne -> 1820
    //   1810: aload_0
    //   1811: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1814: iload_2
    //   1815: aaload
    //   1816: astore_0
    //   1817: goto -> 1797
    //   1820: aload #4
    //   1822: aload #19
    //   1824: if_acmpeq -> 2132
    //   1827: aload #4
    //   1829: aload #23
    //   1831: if_acmpeq -> 2132
    //   1834: aload_0
    //   1835: ifnull -> 2132
    //   1838: aload_0
    //   1839: aload #23
    //   1841: if_acmpne -> 1849
    //   1844: aconst_null
    //   1845: astore_0
    //   1846: goto -> 1849
    //   1849: aload #4
    //   1851: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1854: iload_3
    //   1855: aaload
    //   1856: astore #17
    //   1858: aload #17
    //   1860: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1863: astore #26
    //   1865: aload #17
    //   1867: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1870: ifnull -> 1883
    //   1873: aload #17
    //   1875: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1878: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1881: astore #18
    //   1883: aload #16
    //   1885: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1888: astore #18
    //   1890: iload_3
    //   1891: iconst_1
    //   1892: iadd
    //   1893: istore #14
    //   1895: aload #18
    //   1897: iload #14
    //   1899: aaload
    //   1900: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1903: astore #27
    //   1905: aload #17
    //   1907: invokevirtual getMargin : ()I
    //   1910: istore #13
    //   1912: aload #4
    //   1914: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1917: iload #14
    //   1919: aaload
    //   1920: invokevirtual getMargin : ()I
    //   1923: istore #12
    //   1925: aload_0
    //   1926: ifnull -> 1971
    //   1929: aload_0
    //   1930: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1933: iload_3
    //   1934: aaload
    //   1935: astore #18
    //   1937: aload #18
    //   1939: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1942: astore #20
    //   1944: aload #18
    //   1946: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1949: ifnull -> 1965
    //   1952: aload #18
    //   1954: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1957: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1960: astore #17
    //   1962: goto -> 2023
    //   1965: aconst_null
    //   1966: astore #17
    //   1968: goto -> 2023
    //   1971: aload #4
    //   1973: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1976: iload #14
    //   1978: aaload
    //   1979: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1982: astore #21
    //   1984: aload #21
    //   1986: ifnull -> 1999
    //   1989: aload #21
    //   1991: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   1994: astore #18
    //   1996: goto -> 2002
    //   1999: aconst_null
    //   2000: astore #18
    //   2002: aload #4
    //   2004: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2007: iload #14
    //   2009: aaload
    //   2010: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2013: astore #17
    //   2015: aload #18
    //   2017: astore #20
    //   2019: aload #21
    //   2021: astore #18
    //   2023: iload #12
    //   2025: istore #8
    //   2027: aload #18
    //   2029: ifnull -> 2042
    //   2032: iload #12
    //   2034: aload #18
    //   2036: invokevirtual getMargin : ()I
    //   2039: iadd
    //   2040: istore #8
    //   2042: iload #13
    //   2044: istore #12
    //   2046: aload #16
    //   2048: ifnull -> 2067
    //   2051: iload #13
    //   2053: aload #16
    //   2055: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2058: iload #14
    //   2060: aaload
    //   2061: invokevirtual getMargin : ()I
    //   2064: iadd
    //   2065: istore #12
    //   2067: iload #9
    //   2069: ifeq -> 2079
    //   2072: bipush #6
    //   2074: istore #13
    //   2076: goto -> 2082
    //   2079: iconst_4
    //   2080: istore #13
    //   2082: aload #26
    //   2084: ifnull -> 2125
    //   2087: aload #27
    //   2089: ifnull -> 2125
    //   2092: aload #20
    //   2094: ifnull -> 2125
    //   2097: aload #17
    //   2099: ifnull -> 2125
    //   2102: aload_1
    //   2103: aload #26
    //   2105: aload #27
    //   2107: iload #12
    //   2109: ldc 0.5
    //   2111: aload #20
    //   2113: aload #17
    //   2115: iload #8
    //   2117: iload #13
    //   2119: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2122: goto -> 2125
    //   2125: bipush #8
    //   2127: istore #8
    //   2129: goto -> 2132
    //   2132: aload #4
    //   2134: invokevirtual getVisibility : ()I
    //   2137: iload #8
    //   2139: if_icmpeq -> 2145
    //   2142: goto -> 2149
    //   2145: aload #16
    //   2147: astore #4
    //   2149: aload #4
    //   2151: astore #16
    //   2153: aload_0
    //   2154: astore #4
    //   2156: goto -> 1784
    //   2159: aload #19
    //   2161: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2164: iload_3
    //   2165: aaload
    //   2166: astore_0
    //   2167: aload #25
    //   2169: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2172: iload_3
    //   2173: aaload
    //   2174: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2177: astore #4
    //   2179: aload #23
    //   2181: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2184: astore #16
    //   2186: iload_3
    //   2187: iconst_1
    //   2188: iadd
    //   2189: istore_2
    //   2190: aload #16
    //   2192: iload_2
    //   2193: aaload
    //   2194: astore #16
    //   2196: aload #22
    //   2198: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2201: iload_2
    //   2202: aaload
    //   2203: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2206: astore #17
    //   2208: aload #4
    //   2210: ifnull -> 2285
    //   2213: aload #19
    //   2215: aload #23
    //   2217: if_acmpeq -> 2242
    //   2220: aload_1
    //   2221: aload_0
    //   2222: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2225: aload #4
    //   2227: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2230: aload_0
    //   2231: invokevirtual getMargin : ()I
    //   2234: iconst_5
    //   2235: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   2238: pop
    //   2239: goto -> 2285
    //   2242: aload #17
    //   2244: ifnull -> 2285
    //   2247: aload_1
    //   2248: aload_0
    //   2249: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2252: aload #4
    //   2254: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2257: aload_0
    //   2258: invokevirtual getMargin : ()I
    //   2261: ldc 0.5
    //   2263: aload #16
    //   2265: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2268: aload #17
    //   2270: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2273: aload #16
    //   2275: invokevirtual getMargin : ()I
    //   2278: iconst_5
    //   2279: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2282: goto -> 2285
    //   2285: aload #17
    //   2287: ifnull -> 2319
    //   2290: aload #19
    //   2292: aload #23
    //   2294: if_acmpeq -> 2319
    //   2297: aload_1
    //   2298: aload #16
    //   2300: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2303: aload #17
    //   2305: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2308: aload #16
    //   2310: invokevirtual getMargin : ()I
    //   2313: ineg
    //   2314: iconst_5
    //   2315: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   2318: pop
    //   2319: iload #10
    //   2321: ifne -> 2329
    //   2324: iload #11
    //   2326: ifeq -> 2537
    //   2329: aload #19
    //   2331: ifnull -> 2537
    //   2334: aload #19
    //   2336: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2339: iload_3
    //   2340: aaload
    //   2341: astore #17
    //   2343: aload #23
    //   2345: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2348: astore_0
    //   2349: iload_3
    //   2350: iconst_1
    //   2351: iadd
    //   2352: istore #8
    //   2354: aload_0
    //   2355: iload #8
    //   2357: aaload
    //   2358: astore #16
    //   2360: aload #17
    //   2362: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2365: ifnull -> 2381
    //   2368: aload #17
    //   2370: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2373: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2376: astore #4
    //   2378: goto -> 2384
    //   2381: aconst_null
    //   2382: astore #4
    //   2384: aload #16
    //   2386: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2389: ifnull -> 2404
    //   2392: aload #16
    //   2394: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2397: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2400: astore_0
    //   2401: goto -> 2406
    //   2404: aconst_null
    //   2405: astore_0
    //   2406: aload #22
    //   2408: aload #23
    //   2410: if_acmpeq -> 2446
    //   2413: aload #22
    //   2415: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2418: iload #8
    //   2420: aaload
    //   2421: astore #18
    //   2423: aload #24
    //   2425: astore_0
    //   2426: aload #18
    //   2428: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2431: ifnull -> 2443
    //   2434: aload #18
    //   2436: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2439: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2442: astore_0
    //   2443: goto -> 2446
    //   2446: aload #19
    //   2448: aload #23
    //   2450: if_acmpne -> 2472
    //   2453: aload #19
    //   2455: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2458: iload_3
    //   2459: aaload
    //   2460: astore #17
    //   2462: aload #19
    //   2464: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2467: iload #8
    //   2469: aaload
    //   2470: astore #16
    //   2472: aload #4
    //   2474: ifnull -> 2537
    //   2477: aload_0
    //   2478: ifnull -> 2537
    //   2481: aload #17
    //   2483: invokevirtual getMargin : ()I
    //   2486: istore_2
    //   2487: aload #23
    //   2489: ifnonnull -> 2499
    //   2492: aload #22
    //   2494: astore #18
    //   2496: goto -> 2503
    //   2499: aload #23
    //   2501: astore #18
    //   2503: aload #18
    //   2505: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   2508: iload #8
    //   2510: aaload
    //   2511: invokevirtual getMargin : ()I
    //   2514: istore_3
    //   2515: aload_1
    //   2516: aload #17
    //   2518: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2521: aload #4
    //   2523: iload_2
    //   2524: ldc 0.5
    //   2526: aload_0
    //   2527: aload #16
    //   2529: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   2532: iload_3
    //   2533: iconst_5
    //   2534: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   2537: return
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\Chain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */