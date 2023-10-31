package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 == null || paramArrayOfPathDataNode2 == null)
      return false; 
    if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfPathDataNode1.length; b++) {
      if ((paramArrayOfPathDataNode1[b]).mType != (paramArrayOfPathDataNode2[b]).mType || (paramArrayOfPathDataNode1[b]).mParams.length != (paramArrayOfPathDataNode2[b]).mParams.length)
        return false; 
    } 
    return true;
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOffloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    ArrayList<PathDataNode> arrayList = new ArrayList();
    int j = 1;
    int i = 0;
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error in parsing ");
        stringBuilder.append(paramString);
        throw new RuntimeException(stringBuilder.toString(), runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (byte b = 0; b < paramArrayOfPathDataNode.length; b++)
      arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    paramExtractFloatResult.mEndWithNegOrDot = false;
    int i = paramInt;
    boolean bool1 = false;
    boolean bool3 = false;
    boolean bool2 = bool3;
    while (i < paramString.length()) {
      char c = paramString.charAt(i);
      if (c != ' ') {
        if (c != 'E' && c != 'e') {
          switch (c) {
            default:
              bool1 = false;
              break;
            case '.':
              if (!bool3) {
                bool1 = false;
                bool3 = true;
                break;
              } 
              paramExtractFloatResult.mEndWithNegOrDot = true;
            case '-':
            
            case ',':
              bool1 = false;
              bool2 = true;
              break;
          } 
        } else {
          bool1 = true;
        } 
        if (bool2)
          break; 
        continue;
      } 
      i++;
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      null = new float[paramString.length()];
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      this();
      int k = paramString.length();
      int i = 1;
      int j;
      for (j = 0; i < k; j = m) {
        extract(paramString, i, extractFloatResult);
        int n = extractFloatResult.mEndPosition;
        int m = j;
        if (i < n) {
          null[j] = Float.parseFloat(paramString.substring(i, n));
          m = j + 1;
        } 
        if (extractFloatResult.mEndWithNegOrDot) {
          i = n;
          j = m;
          continue;
        } 
        i = n + 1;
      } 
      return copyOfRange(null, 0, j);
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("error in parsing \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      throw new RuntimeException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  public static boolean interpolatePathDataNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2, PathDataNode[] paramArrayOfPathDataNode3, float paramFloat) {
    if (paramArrayOfPathDataNode1 != null && paramArrayOfPathDataNode2 != null && paramArrayOfPathDataNode3 != null) {
      if (paramArrayOfPathDataNode1.length == paramArrayOfPathDataNode2.length && paramArrayOfPathDataNode2.length == paramArrayOfPathDataNode3.length) {
        boolean bool = canMorph(paramArrayOfPathDataNode2, paramArrayOfPathDataNode3);
        byte b = 0;
        if (!bool)
          return false; 
        while (b < paramArrayOfPathDataNode1.length) {
          paramArrayOfPathDataNode1[b].interpolatePathDataNode(paramArrayOfPathDataNode2[b], paramArrayOfPathDataNode3[b], paramFloat);
          b++;
        } 
        return true;
      } 
      throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
    } 
    throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).mType = (paramArrayOfPathDataNode2[b]).mType;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).mParams.length; b1++)
        (paramArrayOfPathDataNode1[b]).mParams[b1] = (paramArrayOfPathDataNode2[b]).mParams[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    public float[] mParams;
    
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.mType = param1Char;
      this.mParams = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = param1PathDataNode.mType;
      float[] arrayOfFloat = param1PathDataNode.mParams;
      this.mParams = PathParser.copyOfRange(arrayOfFloat, 0, arrayOfFloat.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, char param1Char1, char param1Char2, float[] param1ArrayOffloat2) {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: faload
      //   3: fstore #13
      //   5: aload_1
      //   6: iconst_1
      //   7: faload
      //   8: fstore #11
      //   10: aload_1
      //   11: iconst_2
      //   12: faload
      //   13: fstore #12
      //   15: aload_1
      //   16: iconst_3
      //   17: faload
      //   18: fstore #14
      //   20: aload_1
      //   21: iconst_4
      //   22: faload
      //   23: fstore #10
      //   25: aload_1
      //   26: iconst_5
      //   27: faload
      //   28: fstore #9
      //   30: fload #13
      //   32: fstore #6
      //   34: fload #11
      //   36: fstore #5
      //   38: fload #12
      //   40: fstore #7
      //   42: fload #14
      //   44: fstore #8
      //   46: iload_3
      //   47: lookupswitch default -> 216, 65 -> 320, 67 -> 313, 72 -> 291, 76 -> 232, 77 -> 232, 81 -> 269, 83 -> 269, 84 -> 232, 86 -> 291, 90 -> 238, 97 -> 320, 99 -> 313, 104 -> 291, 108 -> 232, 109 -> 232, 113 -> 269, 115 -> 269, 116 -> 232, 118 -> 291, 122 -> 238
      //   216: fload #14
      //   218: fstore #8
      //   220: fload #12
      //   222: fstore #7
      //   224: fload #11
      //   226: fstore #5
      //   228: fload #13
      //   230: fstore #6
      //   232: iconst_2
      //   233: istore #15
      //   235: goto -> 340
      //   238: aload_0
      //   239: invokevirtual close : ()V
      //   242: aload_0
      //   243: fload #10
      //   245: fload #9
      //   247: invokevirtual moveTo : (FF)V
      //   250: fload #10
      //   252: fstore #6
      //   254: fload #6
      //   256: fstore #7
      //   258: fload #9
      //   260: fstore #5
      //   262: fload #5
      //   264: fstore #8
      //   266: goto -> 232
      //   269: iconst_4
      //   270: istore #15
      //   272: fload #13
      //   274: fstore #6
      //   276: fload #11
      //   278: fstore #5
      //   280: fload #12
      //   282: fstore #7
      //   284: fload #14
      //   286: fstore #8
      //   288: goto -> 340
      //   291: iconst_1
      //   292: istore #15
      //   294: fload #13
      //   296: fstore #6
      //   298: fload #11
      //   300: fstore #5
      //   302: fload #12
      //   304: fstore #7
      //   306: fload #14
      //   308: fstore #8
      //   310: goto -> 340
      //   313: bipush #6
      //   315: istore #15
      //   317: goto -> 324
      //   320: bipush #7
      //   322: istore #15
      //   324: fload #14
      //   326: fstore #8
      //   328: fload #12
      //   330: fstore #7
      //   332: fload #11
      //   334: fstore #5
      //   336: fload #13
      //   338: fstore #6
      //   340: fload #6
      //   342: fstore #13
      //   344: fload #5
      //   346: fstore #6
      //   348: iconst_0
      //   349: istore #17
      //   351: iload_2
      //   352: istore #16
      //   354: fload #9
      //   356: fstore #11
      //   358: fload #10
      //   360: fstore #12
      //   362: fload #13
      //   364: fstore #5
      //   366: iload #17
      //   368: istore_2
      //   369: iload_2
      //   370: aload #4
      //   372: arraylength
      //   373: if_icmpge -> 2104
      //   376: iload_3
      //   377: bipush #65
      //   379: if_icmpeq -> 1960
      //   382: iload_3
      //   383: bipush #67
      //   385: if_icmpeq -> 1853
      //   388: iload_3
      //   389: bipush #72
      //   391: if_icmpeq -> 1827
      //   394: iload_3
      //   395: bipush #81
      //   397: if_icmpeq -> 1740
      //   400: iload_3
      //   401: bipush #86
      //   403: if_icmpeq -> 1714
      //   406: iload_3
      //   407: bipush #97
      //   409: if_icmpeq -> 1574
      //   412: iload_3
      //   413: bipush #99
      //   415: if_icmpeq -> 1431
      //   418: iload_3
      //   419: bipush #104
      //   421: if_icmpeq -> 1403
      //   424: iload_3
      //   425: bipush #113
      //   427: if_icmpeq -> 1303
      //   430: iload_3
      //   431: bipush #118
      //   433: if_icmpeq -> 1278
      //   436: iload_3
      //   437: bipush #76
      //   439: if_icmpeq -> 1233
      //   442: iload_3
      //   443: bipush #77
      //   445: if_icmpeq -> 1163
      //   448: iload_3
      //   449: bipush #83
      //   451: if_icmpeq -> 1018
      //   454: iload_3
      //   455: bipush #84
      //   457: if_icmpeq -> 907
      //   460: iload_3
      //   461: bipush #108
      //   463: if_icmpeq -> 852
      //   466: iload_3
      //   467: bipush #109
      //   469: if_icmpeq -> 784
      //   472: iload_3
      //   473: bipush #115
      //   475: if_icmpeq -> 618
      //   478: iload_3
      //   479: bipush #116
      //   481: if_icmpeq -> 487
      //   484: goto -> 2093
      //   487: iload #16
      //   489: bipush #113
      //   491: if_icmpeq -> 527
      //   494: iload #16
      //   496: bipush #116
      //   498: if_icmpeq -> 527
      //   501: iload #16
      //   503: bipush #81
      //   505: if_icmpeq -> 527
      //   508: iload #16
      //   510: bipush #84
      //   512: if_icmpne -> 518
      //   515: goto -> 527
      //   518: fconst_0
      //   519: fstore #8
      //   521: fconst_0
      //   522: fstore #7
      //   524: goto -> 541
      //   527: fload #5
      //   529: fload #7
      //   531: fsub
      //   532: fstore #7
      //   534: fload #6
      //   536: fload #8
      //   538: fsub
      //   539: fstore #8
      //   541: iload_2
      //   542: iconst_0
      //   543: iadd
      //   544: istore #17
      //   546: aload #4
      //   548: iload #17
      //   550: faload
      //   551: fstore #9
      //   553: iload_2
      //   554: iconst_1
      //   555: iadd
      //   556: istore #16
      //   558: aload_0
      //   559: fload #7
      //   561: fload #8
      //   563: fload #9
      //   565: aload #4
      //   567: iload #16
      //   569: faload
      //   570: invokevirtual rQuadTo : (FFFF)V
      //   573: fload #5
      //   575: aload #4
      //   577: iload #17
      //   579: faload
      //   580: fadd
      //   581: fstore #9
      //   583: fload #6
      //   585: aload #4
      //   587: iload #16
      //   589: faload
      //   590: fadd
      //   591: fstore #10
      //   593: fload #8
      //   595: fload #6
      //   597: fadd
      //   598: fstore #8
      //   600: fload #7
      //   602: fload #5
      //   604: fadd
      //   605: fstore #7
      //   607: fload #10
      //   609: fstore #6
      //   611: fload #9
      //   613: fstore #5
      //   615: goto -> 484
      //   618: iload #16
      //   620: bipush #99
      //   622: if_icmpeq -> 658
      //   625: iload #16
      //   627: bipush #115
      //   629: if_icmpeq -> 658
      //   632: iload #16
      //   634: bipush #67
      //   636: if_icmpeq -> 658
      //   639: iload #16
      //   641: bipush #83
      //   643: if_icmpne -> 649
      //   646: goto -> 658
      //   649: fconst_0
      //   650: fstore #8
      //   652: fconst_0
      //   653: fstore #7
      //   655: goto -> 680
      //   658: fload #6
      //   660: fload #8
      //   662: fsub
      //   663: fstore #8
      //   665: fload #5
      //   667: fload #7
      //   669: fsub
      //   670: fstore #9
      //   672: fload #8
      //   674: fstore #7
      //   676: fload #9
      //   678: fstore #8
      //   680: iload_2
      //   681: iconst_0
      //   682: iadd
      //   683: istore #19
      //   685: aload #4
      //   687: iload #19
      //   689: faload
      //   690: fstore #13
      //   692: iload_2
      //   693: iconst_1
      //   694: iadd
      //   695: istore #16
      //   697: aload #4
      //   699: iload #16
      //   701: faload
      //   702: fstore #9
      //   704: iload_2
      //   705: iconst_2
      //   706: iadd
      //   707: istore #18
      //   709: aload #4
      //   711: iload #18
      //   713: faload
      //   714: fstore #10
      //   716: iload_2
      //   717: iconst_3
      //   718: iadd
      //   719: istore #17
      //   721: aload_0
      //   722: fload #8
      //   724: fload #7
      //   726: fload #13
      //   728: fload #9
      //   730: fload #10
      //   732: aload #4
      //   734: iload #17
      //   736: faload
      //   737: invokevirtual rCubicTo : (FFFFFF)V
      //   740: aload #4
      //   742: iload #19
      //   744: faload
      //   745: fload #5
      //   747: fadd
      //   748: fstore #10
      //   750: aload #4
      //   752: iload #16
      //   754: faload
      //   755: fload #6
      //   757: fadd
      //   758: fstore #7
      //   760: fload #5
      //   762: aload #4
      //   764: iload #18
      //   766: faload
      //   767: fadd
      //   768: fstore #8
      //   770: aload #4
      //   772: iload #17
      //   774: faload
      //   775: fstore #9
      //   777: fload #10
      //   779: fstore #5
      //   781: goto -> 1548
      //   784: iload_2
      //   785: iconst_0
      //   786: iadd
      //   787: istore #16
      //   789: fload #5
      //   791: aload #4
      //   793: iload #16
      //   795: faload
      //   796: fadd
      //   797: fstore #5
      //   799: iload_2
      //   800: iconst_1
      //   801: iadd
      //   802: istore #17
      //   804: fload #6
      //   806: aload #4
      //   808: iload #17
      //   810: faload
      //   811: fadd
      //   812: fstore #6
      //   814: iload_2
      //   815: ifle -> 835
      //   818: aload_0
      //   819: aload #4
      //   821: iload #16
      //   823: faload
      //   824: aload #4
      //   826: iload #17
      //   828: faload
      //   829: invokevirtual rLineTo : (FF)V
      //   832: goto -> 484
      //   835: aload_0
      //   836: aload #4
      //   838: iload #16
      //   840: faload
      //   841: aload #4
      //   843: iload #17
      //   845: faload
      //   846: invokevirtual rMoveTo : (FF)V
      //   849: goto -> 1222
      //   852: iload_2
      //   853: iconst_0
      //   854: iadd
      //   855: istore #17
      //   857: aload #4
      //   859: iload #17
      //   861: faload
      //   862: fstore #9
      //   864: iload_2
      //   865: iconst_1
      //   866: iadd
      //   867: istore #16
      //   869: aload_0
      //   870: fload #9
      //   872: aload #4
      //   874: iload #16
      //   876: faload
      //   877: invokevirtual rLineTo : (FF)V
      //   880: fload #5
      //   882: aload #4
      //   884: iload #17
      //   886: faload
      //   887: fadd
      //   888: fstore #5
      //   890: aload #4
      //   892: iload #16
      //   894: faload
      //   895: fstore #9
      //   897: fload #6
      //   899: fload #9
      //   901: fadd
      //   902: fstore #6
      //   904: goto -> 484
      //   907: iload #16
      //   909: bipush #113
      //   911: if_icmpeq -> 943
      //   914: iload #16
      //   916: bipush #116
      //   918: if_icmpeq -> 943
      //   921: iload #16
      //   923: bipush #81
      //   925: if_icmpeq -> 943
      //   928: fload #6
      //   930: fstore #10
      //   932: fload #5
      //   934: fstore #9
      //   936: iload #16
      //   938: bipush #84
      //   940: if_icmpne -> 961
      //   943: fload #5
      //   945: fconst_2
      //   946: fmul
      //   947: fload #7
      //   949: fsub
      //   950: fstore #9
      //   952: fload #6
      //   954: fconst_2
      //   955: fmul
      //   956: fload #8
      //   958: fsub
      //   959: fstore #10
      //   961: iload_2
      //   962: iconst_0
      //   963: iadd
      //   964: istore #17
      //   966: aload #4
      //   968: iload #17
      //   970: faload
      //   971: fstore #5
      //   973: iload_2
      //   974: iconst_1
      //   975: iadd
      //   976: istore #16
      //   978: aload_0
      //   979: fload #9
      //   981: fload #10
      //   983: fload #5
      //   985: aload #4
      //   987: iload #16
      //   989: faload
      //   990: invokevirtual quadTo : (FFFF)V
      //   993: aload #4
      //   995: iload #17
      //   997: faload
      //   998: fstore #5
      //   1000: aload #4
      //   1002: iload #16
      //   1004: faload
      //   1005: fstore #6
      //   1007: fload #10
      //   1009: fstore #8
      //   1011: fload #9
      //   1013: fstore #7
      //   1015: goto -> 2093
      //   1018: iload #16
      //   1020: bipush #99
      //   1022: if_icmpeq -> 1054
      //   1025: iload #16
      //   1027: bipush #115
      //   1029: if_icmpeq -> 1054
      //   1032: iload #16
      //   1034: bipush #67
      //   1036: if_icmpeq -> 1054
      //   1039: fload #6
      //   1041: fstore #10
      //   1043: fload #5
      //   1045: fstore #9
      //   1047: iload #16
      //   1049: bipush #83
      //   1051: if_icmpne -> 1072
      //   1054: fload #5
      //   1056: fconst_2
      //   1057: fmul
      //   1058: fload #7
      //   1060: fsub
      //   1061: fstore #9
      //   1063: fload #6
      //   1065: fconst_2
      //   1066: fmul
      //   1067: fload #8
      //   1069: fsub
      //   1070: fstore #10
      //   1072: iload_2
      //   1073: iconst_0
      //   1074: iadd
      //   1075: istore #18
      //   1077: aload #4
      //   1079: iload #18
      //   1081: faload
      //   1082: fstore #7
      //   1084: iload_2
      //   1085: iconst_1
      //   1086: iadd
      //   1087: istore #16
      //   1089: aload #4
      //   1091: iload #16
      //   1093: faload
      //   1094: fstore #6
      //   1096: iload_2
      //   1097: iconst_2
      //   1098: iadd
      //   1099: istore #17
      //   1101: aload #4
      //   1103: iload #17
      //   1105: faload
      //   1106: fstore #5
      //   1108: iload_2
      //   1109: iconst_3
      //   1110: iadd
      //   1111: istore #19
      //   1113: aload_0
      //   1114: fload #9
      //   1116: fload #10
      //   1118: fload #7
      //   1120: fload #6
      //   1122: fload #5
      //   1124: aload #4
      //   1126: iload #19
      //   1128: faload
      //   1129: invokevirtual cubicTo : (FFFFFF)V
      //   1132: aload #4
      //   1134: iload #18
      //   1136: faload
      //   1137: fstore #5
      //   1139: aload #4
      //   1141: iload #16
      //   1143: faload
      //   1144: fstore #7
      //   1146: aload #4
      //   1148: iload #17
      //   1150: faload
      //   1151: fstore #9
      //   1153: aload #4
      //   1155: iload #19
      //   1157: faload
      //   1158: fstore #6
      //   1160: goto -> 1559
      //   1163: iload_2
      //   1164: iconst_0
      //   1165: iadd
      //   1166: istore #17
      //   1168: aload #4
      //   1170: iload #17
      //   1172: faload
      //   1173: fstore #5
      //   1175: iload_2
      //   1176: iconst_1
      //   1177: iadd
      //   1178: istore #16
      //   1180: aload #4
      //   1182: iload #16
      //   1184: faload
      //   1185: fstore #6
      //   1187: iload_2
      //   1188: ifle -> 1208
      //   1191: aload_0
      //   1192: aload #4
      //   1194: iload #17
      //   1196: faload
      //   1197: aload #4
      //   1199: iload #16
      //   1201: faload
      //   1202: invokevirtual lineTo : (FF)V
      //   1205: goto -> 484
      //   1208: aload_0
      //   1209: aload #4
      //   1211: iload #17
      //   1213: faload
      //   1214: aload #4
      //   1216: iload #16
      //   1218: faload
      //   1219: invokevirtual moveTo : (FF)V
      //   1222: fload #6
      //   1224: fstore #11
      //   1226: fload #5
      //   1228: fstore #12
      //   1230: goto -> 2093
      //   1233: iload_2
      //   1234: iconst_0
      //   1235: iadd
      //   1236: istore #16
      //   1238: aload #4
      //   1240: iload #16
      //   1242: faload
      //   1243: fstore #5
      //   1245: iload_2
      //   1246: iconst_1
      //   1247: iadd
      //   1248: istore #17
      //   1250: aload_0
      //   1251: fload #5
      //   1253: aload #4
      //   1255: iload #17
      //   1257: faload
      //   1258: invokevirtual lineTo : (FF)V
      //   1261: aload #4
      //   1263: iload #16
      //   1265: faload
      //   1266: fstore #5
      //   1268: aload #4
      //   1270: iload #17
      //   1272: faload
      //   1273: fstore #6
      //   1275: goto -> 484
      //   1278: iload_2
      //   1279: iconst_0
      //   1280: iadd
      //   1281: istore #16
      //   1283: aload_0
      //   1284: fconst_0
      //   1285: aload #4
      //   1287: iload #16
      //   1289: faload
      //   1290: invokevirtual rLineTo : (FF)V
      //   1293: aload #4
      //   1295: iload #16
      //   1297: faload
      //   1298: fstore #9
      //   1300: goto -> 897
      //   1303: iload_2
      //   1304: iconst_0
      //   1305: iadd
      //   1306: istore #18
      //   1308: aload #4
      //   1310: iload #18
      //   1312: faload
      //   1313: fstore #9
      //   1315: iload_2
      //   1316: iconst_1
      //   1317: iadd
      //   1318: istore #16
      //   1320: aload #4
      //   1322: iload #16
      //   1324: faload
      //   1325: fstore #7
      //   1327: iload_2
      //   1328: iconst_2
      //   1329: iadd
      //   1330: istore #19
      //   1332: aload #4
      //   1334: iload #19
      //   1336: faload
      //   1337: fstore #8
      //   1339: iload_2
      //   1340: iconst_3
      //   1341: iadd
      //   1342: istore #17
      //   1344: aload_0
      //   1345: fload #9
      //   1347: fload #7
      //   1349: fload #8
      //   1351: aload #4
      //   1353: iload #17
      //   1355: faload
      //   1356: invokevirtual rQuadTo : (FFFF)V
      //   1359: aload #4
      //   1361: iload #18
      //   1363: faload
      //   1364: fload #5
      //   1366: fadd
      //   1367: fstore #10
      //   1369: aload #4
      //   1371: iload #16
      //   1373: faload
      //   1374: fload #6
      //   1376: fadd
      //   1377: fstore #7
      //   1379: fload #5
      //   1381: aload #4
      //   1383: iload #19
      //   1385: faload
      //   1386: fadd
      //   1387: fstore #8
      //   1389: aload #4
      //   1391: iload #17
      //   1393: faload
      //   1394: fstore #9
      //   1396: fload #10
      //   1398: fstore #5
      //   1400: goto -> 1548
      //   1403: iload_2
      //   1404: iconst_0
      //   1405: iadd
      //   1406: istore #16
      //   1408: aload_0
      //   1409: aload #4
      //   1411: iload #16
      //   1413: faload
      //   1414: fconst_0
      //   1415: invokevirtual rLineTo : (FF)V
      //   1418: fload #5
      //   1420: aload #4
      //   1422: iload #16
      //   1424: faload
      //   1425: fadd
      //   1426: fstore #5
      //   1428: goto -> 484
      //   1431: aload #4
      //   1433: iload_2
      //   1434: iconst_0
      //   1435: iadd
      //   1436: faload
      //   1437: fstore #7
      //   1439: aload #4
      //   1441: iload_2
      //   1442: iconst_1
      //   1443: iadd
      //   1444: faload
      //   1445: fstore #10
      //   1447: iload_2
      //   1448: iconst_2
      //   1449: iadd
      //   1450: istore #19
      //   1452: aload #4
      //   1454: iload #19
      //   1456: faload
      //   1457: fstore #13
      //   1459: iload_2
      //   1460: iconst_3
      //   1461: iadd
      //   1462: istore #18
      //   1464: aload #4
      //   1466: iload #18
      //   1468: faload
      //   1469: fstore #8
      //   1471: iload_2
      //   1472: iconst_4
      //   1473: iadd
      //   1474: istore #17
      //   1476: aload #4
      //   1478: iload #17
      //   1480: faload
      //   1481: fstore #9
      //   1483: iload_2
      //   1484: iconst_5
      //   1485: iadd
      //   1486: istore #16
      //   1488: aload_0
      //   1489: fload #7
      //   1491: fload #10
      //   1493: fload #13
      //   1495: fload #8
      //   1497: fload #9
      //   1499: aload #4
      //   1501: iload #16
      //   1503: faload
      //   1504: invokevirtual rCubicTo : (FFFFFF)V
      //   1507: aload #4
      //   1509: iload #19
      //   1511: faload
      //   1512: fload #5
      //   1514: fadd
      //   1515: fstore #10
      //   1517: aload #4
      //   1519: iload #18
      //   1521: faload
      //   1522: fload #6
      //   1524: fadd
      //   1525: fstore #7
      //   1527: fload #5
      //   1529: aload #4
      //   1531: iload #17
      //   1533: faload
      //   1534: fadd
      //   1535: fstore #8
      //   1537: aload #4
      //   1539: iload #16
      //   1541: faload
      //   1542: fstore #9
      //   1544: fload #10
      //   1546: fstore #5
      //   1548: fload #6
      //   1550: fload #9
      //   1552: fadd
      //   1553: fstore #6
      //   1555: fload #8
      //   1557: fstore #9
      //   1559: fload #7
      //   1561: fstore #8
      //   1563: fload #5
      //   1565: fstore #7
      //   1567: fload #9
      //   1569: fstore #5
      //   1571: goto -> 484
      //   1574: iload_2
      //   1575: iconst_5
      //   1576: iadd
      //   1577: istore #16
      //   1579: aload #4
      //   1581: iload #16
      //   1583: faload
      //   1584: fstore #8
      //   1586: iload_2
      //   1587: bipush #6
      //   1589: iadd
      //   1590: istore #17
      //   1592: aload #4
      //   1594: iload #17
      //   1596: faload
      //   1597: fstore #10
      //   1599: aload #4
      //   1601: iload_2
      //   1602: iconst_0
      //   1603: iadd
      //   1604: faload
      //   1605: fstore #13
      //   1607: aload #4
      //   1609: iload_2
      //   1610: iconst_1
      //   1611: iadd
      //   1612: faload
      //   1613: fstore #9
      //   1615: aload #4
      //   1617: iload_2
      //   1618: iconst_2
      //   1619: iadd
      //   1620: faload
      //   1621: fstore #7
      //   1623: aload #4
      //   1625: iload_2
      //   1626: iconst_3
      //   1627: iadd
      //   1628: faload
      //   1629: fconst_0
      //   1630: fcmpl
      //   1631: ifeq -> 1640
      //   1634: iconst_1
      //   1635: istore #20
      //   1637: goto -> 1643
      //   1640: iconst_0
      //   1641: istore #20
      //   1643: aload #4
      //   1645: iload_2
      //   1646: iconst_4
      //   1647: iadd
      //   1648: faload
      //   1649: fconst_0
      //   1650: fcmpl
      //   1651: ifeq -> 1660
      //   1654: iconst_1
      //   1655: istore #21
      //   1657: goto -> 1663
      //   1660: iconst_0
      //   1661: istore #21
      //   1663: aload_0
      //   1664: fload #5
      //   1666: fload #6
      //   1668: fload #8
      //   1670: fload #5
      //   1672: fadd
      //   1673: fload #10
      //   1675: fload #6
      //   1677: fadd
      //   1678: fload #13
      //   1680: fload #9
      //   1682: fload #7
      //   1684: iload #20
      //   1686: iload #21
      //   1688: invokestatic drawArc : (Landroid/graphics/Path;FFFFFFFZZ)V
      //   1691: fload #5
      //   1693: aload #4
      //   1695: iload #16
      //   1697: faload
      //   1698: fadd
      //   1699: fstore #5
      //   1701: fload #6
      //   1703: aload #4
      //   1705: iload #17
      //   1707: faload
      //   1708: fadd
      //   1709: fstore #6
      //   1711: goto -> 2085
      //   1714: iload_2
      //   1715: iconst_0
      //   1716: iadd
      //   1717: istore #16
      //   1719: aload_0
      //   1720: fload #5
      //   1722: aload #4
      //   1724: iload #16
      //   1726: faload
      //   1727: invokevirtual lineTo : (FF)V
      //   1730: aload #4
      //   1732: iload #16
      //   1734: faload
      //   1735: fstore #6
      //   1737: goto -> 2093
      //   1740: iload_2
      //   1741: iconst_0
      //   1742: iadd
      //   1743: istore #17
      //   1745: aload #4
      //   1747: iload #17
      //   1749: faload
      //   1750: fstore #5
      //   1752: iload_2
      //   1753: iconst_1
      //   1754: iadd
      //   1755: istore #16
      //   1757: aload #4
      //   1759: iload #16
      //   1761: faload
      //   1762: fstore #7
      //   1764: iload_2
      //   1765: iconst_2
      //   1766: iadd
      //   1767: istore #19
      //   1769: aload #4
      //   1771: iload #19
      //   1773: faload
      //   1774: fstore #6
      //   1776: iload_2
      //   1777: iconst_3
      //   1778: iadd
      //   1779: istore #18
      //   1781: aload_0
      //   1782: fload #5
      //   1784: fload #7
      //   1786: fload #6
      //   1788: aload #4
      //   1790: iload #18
      //   1792: faload
      //   1793: invokevirtual quadTo : (FFFF)V
      //   1796: aload #4
      //   1798: iload #17
      //   1800: faload
      //   1801: fstore #7
      //   1803: aload #4
      //   1805: iload #16
      //   1807: faload
      //   1808: fstore #8
      //   1810: aload #4
      //   1812: iload #19
      //   1814: faload
      //   1815: fstore #5
      //   1817: aload #4
      //   1819: iload #18
      //   1821: faload
      //   1822: fstore #6
      //   1824: goto -> 2093
      //   1827: iload_2
      //   1828: iconst_0
      //   1829: iadd
      //   1830: istore #16
      //   1832: aload_0
      //   1833: aload #4
      //   1835: iload #16
      //   1837: faload
      //   1838: fload #6
      //   1840: invokevirtual lineTo : (FF)V
      //   1843: aload #4
      //   1845: iload #16
      //   1847: faload
      //   1848: fstore #5
      //   1850: goto -> 2093
      //   1853: aload #4
      //   1855: iload_2
      //   1856: iconst_0
      //   1857: iadd
      //   1858: faload
      //   1859: fstore #7
      //   1861: aload #4
      //   1863: iload_2
      //   1864: iconst_1
      //   1865: iadd
      //   1866: faload
      //   1867: fstore #9
      //   1869: iload_2
      //   1870: iconst_2
      //   1871: iadd
      //   1872: istore #18
      //   1874: aload #4
      //   1876: iload #18
      //   1878: faload
      //   1879: fstore #6
      //   1881: iload_2
      //   1882: iconst_3
      //   1883: iadd
      //   1884: istore #16
      //   1886: aload #4
      //   1888: iload #16
      //   1890: faload
      //   1891: fstore #8
      //   1893: iload_2
      //   1894: iconst_4
      //   1895: iadd
      //   1896: istore #19
      //   1898: aload #4
      //   1900: iload #19
      //   1902: faload
      //   1903: fstore #5
      //   1905: iload_2
      //   1906: iconst_5
      //   1907: iadd
      //   1908: istore #17
      //   1910: aload_0
      //   1911: fload #7
      //   1913: fload #9
      //   1915: fload #6
      //   1917: fload #8
      //   1919: fload #5
      //   1921: aload #4
      //   1923: iload #17
      //   1925: faload
      //   1926: invokevirtual cubicTo : (FFFFFF)V
      //   1929: aload #4
      //   1931: iload #19
      //   1933: faload
      //   1934: fstore #5
      //   1936: aload #4
      //   1938: iload #17
      //   1940: faload
      //   1941: fstore #6
      //   1943: aload #4
      //   1945: iload #18
      //   1947: faload
      //   1948: fstore #7
      //   1950: aload #4
      //   1952: iload #16
      //   1954: faload
      //   1955: fstore #8
      //   1957: goto -> 2093
      //   1960: iload_2
      //   1961: iconst_5
      //   1962: iadd
      //   1963: istore #17
      //   1965: aload #4
      //   1967: iload #17
      //   1969: faload
      //   1970: fstore #9
      //   1972: iload_2
      //   1973: bipush #6
      //   1975: iadd
      //   1976: istore #16
      //   1978: aload #4
      //   1980: iload #16
      //   1982: faload
      //   1983: fstore #10
      //   1985: aload #4
      //   1987: iload_2
      //   1988: iconst_0
      //   1989: iadd
      //   1990: faload
      //   1991: fstore #8
      //   1993: aload #4
      //   1995: iload_2
      //   1996: iconst_1
      //   1997: iadd
      //   1998: faload
      //   1999: fstore #7
      //   2001: aload #4
      //   2003: iload_2
      //   2004: iconst_2
      //   2005: iadd
      //   2006: faload
      //   2007: fstore #13
      //   2009: aload #4
      //   2011: iload_2
      //   2012: iconst_3
      //   2013: iadd
      //   2014: faload
      //   2015: fconst_0
      //   2016: fcmpl
      //   2017: ifeq -> 2026
      //   2020: iconst_1
      //   2021: istore #20
      //   2023: goto -> 2029
      //   2026: iconst_0
      //   2027: istore #20
      //   2029: aload #4
      //   2031: iload_2
      //   2032: iconst_4
      //   2033: iadd
      //   2034: faload
      //   2035: fconst_0
      //   2036: fcmpl
      //   2037: ifeq -> 2046
      //   2040: iconst_1
      //   2041: istore #21
      //   2043: goto -> 2049
      //   2046: iconst_0
      //   2047: istore #21
      //   2049: aload_0
      //   2050: fload #5
      //   2052: fload #6
      //   2054: fload #9
      //   2056: fload #10
      //   2058: fload #8
      //   2060: fload #7
      //   2062: fload #13
      //   2064: iload #20
      //   2066: iload #21
      //   2068: invokestatic drawArc : (Landroid/graphics/Path;FFFFFFFZZ)V
      //   2071: aload #4
      //   2073: iload #17
      //   2075: faload
      //   2076: fstore #5
      //   2078: aload #4
      //   2080: iload #16
      //   2082: faload
      //   2083: fstore #6
      //   2085: fload #6
      //   2087: fstore #8
      //   2089: fload #5
      //   2091: fstore #7
      //   2093: iload_2
      //   2094: iload #15
      //   2096: iadd
      //   2097: istore_2
      //   2098: iload_3
      //   2099: istore #16
      //   2101: goto -> 369
      //   2104: aload_1
      //   2105: iconst_0
      //   2106: fload #5
      //   2108: fastore
      //   2109: aload_1
      //   2110: iconst_1
      //   2111: fload #6
      //   2113: fastore
      //   2114: aload_1
      //   2115: iconst_2
      //   2116: fload #7
      //   2118: fastore
      //   2119: aload_1
      //   2120: iconst_3
      //   2121: fload #8
      //   2123: fastore
      //   2124: aload_1
      //   2125: iconst_4
      //   2126: fload #12
      //   2128: fastore
      //   2129: aload_1
      //   2130: iconst_5
      //   2131: fload #11
      //   2133: fastore
      //   2134: return
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d2 = Math.cos(param1Double7);
      double d4 = Math.sin(param1Double7);
      double d6 = Math.cos(param1Double8);
      double d3 = Math.sin(param1Double8);
      param1Double7 = -param1Double3;
      double d7 = param1Double7 * d2;
      double d8 = param1Double4 * d4;
      double d1 = param1Double7 * d4;
      double d9 = param1Double4 * d2;
      double d5 = param1Double9 / i;
      param1Double7 = d3 * d1 + d6 * d9;
      param1Double4 = d7 * d3 - d8 * d6;
      byte b = 0;
      param1Double9 = param1Double5;
      param1Double5 = param1Double4;
      d3 = param1Double8;
      param1Double8 = param1Double6;
      param1Double4 = d1;
      param1Double6 = param1Double9;
      d1 = d5;
      param1Double9 = d4;
      while (b < i) {
        d6 = d3 + d1;
        double d12 = Math.sin(d6);
        double d10 = Math.cos(d6);
        double d11 = param1Double1 + param1Double3 * d2 * d10 - d8 * d12;
        d4 = param1Double2 + param1Double3 * param1Double9 * d10 + d9 * d12;
        d5 = d7 * d12 - d8 * d10;
        d10 = d12 * param1Double4 + d10 * d9;
        d12 = d6 - d3;
        d3 = Math.tan(d12 / 2.0D);
        d3 = Math.sin(d12) * (Math.sqrt(d3 * 3.0D * d3 + 4.0D) - 1.0D) / 3.0D;
        param1Path.rLineTo(0.0F, 0.0F);
        param1Path.cubicTo((float)(param1Double6 + param1Double5 * d3), (float)(param1Double8 + param1Double7 * d3), (float)(d11 - d3 * d5), (float)(d4 - d3 * d10), (float)d11, (float)d4);
        b++;
        param1Double6 = d11;
        d3 = d6;
        param1Double7 = d10;
        param1Double5 = d5;
        param1Double8 = d4;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d6 = Math.toRadians(param1Float7);
      double d8 = Math.cos(d6);
      double d7 = Math.sin(d6);
      double d5 = param1Float1;
      double d9 = param1Float2;
      double d11 = param1Float5;
      double d1 = (d5 * d8 + d9 * d7) / d11;
      double d2 = -param1Float1;
      double d10 = param1Float6;
      double d12 = (d2 * d7 + d9 * d8) / d10;
      double d3 = param1Float3;
      d2 = param1Float4;
      double d4 = (d3 * d8 + d2 * d7) / d11;
      double d13 = (-param1Float3 * d7 + d2 * d8) / d10;
      double d15 = d1 - d4;
      double d14 = d12 - d13;
      d3 = (d1 + d4) / 2.0D;
      d2 = (d12 + d13) / 2.0D;
      double d16 = d15 * d15 + d14 * d14;
      if (d16 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d17 = 1.0D / d16 - 0.25D;
      if (d17 < 0.0D) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points are too far apart ");
        stringBuilder.append(d16);
        Log.w("PathParser", stringBuilder.toString());
        float f = (float)(Math.sqrt(d16) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d16 = Math.sqrt(d17);
      d15 *= d16;
      d14 = d16 * d14;
      if (param1Boolean1 == param1Boolean2) {
        d3 -= d14;
        d2 += d15;
      } else {
        d3 += d14;
        d2 -= d15;
      } 
      d12 = Math.atan2(d12 - d2, d1 - d3);
      d4 = Math.atan2(d13 - d2, d4 - d3) - d12;
      int i = d4 cmp 0.0D;
      if (i >= 0) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d1 = d4;
      if (param1Boolean2 != param1Boolean1)
        if (i > 0) {
          d1 = d4 - 6.283185307179586D;
        } else {
          d1 = d4 + 6.283185307179586D;
        }  
      d3 *= d11;
      d2 *= d10;
      arcToBezier(param1Path, d3 * d8 - d2 * d7, d3 * d7 + d2 * d8, d11, d10, d5, d9, d6, d12, d1);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c = 'm';
      for (byte b = 0; b < param1ArrayOfPathDataNode.length; b++) {
        addCommand(param1Path, arrayOfFloat, c, (param1ArrayOfPathDataNode[b]).mType, (param1ArrayOfPathDataNode[b]).mParams);
        c = (param1ArrayOfPathDataNode[b]).mType;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      this.mType = param1PathDataNode1.mType;
      byte b = 0;
      while (true) {
        float[] arrayOfFloat = param1PathDataNode1.mParams;
        if (b < arrayOfFloat.length) {
          this.mParams[b] = arrayOfFloat[b] * (1.0F - param1Float) + param1PathDataNode2.mParams[b] * param1Float;
          b++;
          continue;
        } 
        break;
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\PathParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */