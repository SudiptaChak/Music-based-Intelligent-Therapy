package androidx.constraintlayout.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  private static final int NONE = -1;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices = new int[8];
  
  private int[] mArrayNextIndices = new int[8];
  
  private float[] mArrayValues = new float[8];
  
  private final Cache mCache;
  
  private boolean mDidFillOnce = false;
  
  private int mHead = -1;
  
  private int mLast = -1;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) {
    int i = paramSolverVariable.usageInRowCount;
    boolean bool = true;
    if (i > 1)
      bool = false; 
    return bool;
  }
  
  final void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    if (paramFloat == 0.0F)
      return; 
    int i = this.mHead;
    if (i == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        i = this.mLast + 1;
        this.mLast = i;
        arrayOfInt1 = this.mArrayIndices;
        if (i >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        float[] arrayOfFloat = this.mArrayValues;
        arrayOfFloat[i] = arrayOfFloat[i] + paramFloat;
        if (arrayOfFloat[i] == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            int[] arrayOfInt = this.mArrayNextIndices;
            arrayOfInt[k] = arrayOfInt[i];
          } 
          if (paramBoolean)
            arrayOfInt1.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          ((SolverVariable)arrayOfInt1).usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[i] != -1)
        i = arrayOfInt.length; 
    } else {
      i++;
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      j = this.ROW_SIZE * 2;
      this.ROW_SIZE = j;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, j);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  SolverVariable chooseSubject(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore #8
    //   6: aconst_null
    //   7: astore #17
    //   9: iconst_0
    //   10: istore #7
    //   12: iconst_0
    //   13: istore #12
    //   15: iload #12
    //   17: istore #11
    //   19: fconst_0
    //   20: fstore #6
    //   22: fconst_0
    //   23: fstore #5
    //   25: aconst_null
    //   26: astore #16
    //   28: iload #8
    //   30: iconst_m1
    //   31: if_icmpeq -> 545
    //   34: iload #7
    //   36: aload_0
    //   37: getfield currentSize : I
    //   40: if_icmpge -> 545
    //   43: aload_0
    //   44: getfield mArrayValues : [F
    //   47: iload #8
    //   49: faload
    //   50: fstore_3
    //   51: aload_0
    //   52: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   55: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   58: aload_0
    //   59: getfield mArrayIndices : [I
    //   62: iload #8
    //   64: iaload
    //   65: aaload
    //   66: astore #13
    //   68: fload_3
    //   69: fconst_0
    //   70: fcmpg
    //   71: ifge -> 103
    //   74: fload_3
    //   75: fstore_2
    //   76: fload_3
    //   77: ldc -0.001
    //   79: fcmpl
    //   80: ifle -> 131
    //   83: aload_0
    //   84: getfield mArrayValues : [F
    //   87: iload #8
    //   89: fconst_0
    //   90: fastore
    //   91: aload #13
    //   93: aload_0
    //   94: getfield mRow : Landroidx/constraintlayout/solver/ArrayRow;
    //   97: invokevirtual removeFromRow : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   100: goto -> 129
    //   103: fload_3
    //   104: fstore_2
    //   105: fload_3
    //   106: ldc 0.001
    //   108: fcmpg
    //   109: ifge -> 131
    //   112: aload_0
    //   113: getfield mArrayValues : [F
    //   116: iload #8
    //   118: fconst_0
    //   119: fastore
    //   120: aload #13
    //   122: aload_0
    //   123: getfield mRow : Landroidx/constraintlayout/solver/ArrayRow;
    //   126: invokevirtual removeFromRow : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   129: fconst_0
    //   130: fstore_2
    //   131: aload #17
    //   133: astore #14
    //   135: aload #16
    //   137: astore #15
    //   139: iload #12
    //   141: istore #9
    //   143: iload #11
    //   145: istore #10
    //   147: fload #6
    //   149: fstore_3
    //   150: fload #5
    //   152: fstore #4
    //   154: fload_2
    //   155: fconst_0
    //   156: fcmpl
    //   157: ifeq -> 507
    //   160: aload #13
    //   162: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   165: getstatic androidx/constraintlayout/solver/SolverVariable$Type.UNRESTRICTED : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   168: if_acmpne -> 310
    //   171: aload #16
    //   173: ifnonnull -> 206
    //   176: aload_0
    //   177: aload #13
    //   179: aload_1
    //   180: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   183: istore #9
    //   185: aload #17
    //   187: astore #14
    //   189: aload #13
    //   191: astore #15
    //   193: iload #11
    //   195: istore #10
    //   197: fload_2
    //   198: fstore_3
    //   199: fload #5
    //   201: fstore #4
    //   203: goto -> 507
    //   206: fload #6
    //   208: fload_2
    //   209: fcmpl
    //   210: ifle -> 225
    //   213: aload_0
    //   214: aload #13
    //   216: aload_1
    //   217: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   220: istore #9
    //   222: goto -> 185
    //   225: aload #17
    //   227: astore #14
    //   229: aload #16
    //   231: astore #15
    //   233: iload #12
    //   235: istore #9
    //   237: iload #11
    //   239: istore #10
    //   241: fload #6
    //   243: fstore_3
    //   244: fload #5
    //   246: fstore #4
    //   248: iload #12
    //   250: ifne -> 507
    //   253: aload #17
    //   255: astore #14
    //   257: aload #16
    //   259: astore #15
    //   261: iload #12
    //   263: istore #9
    //   265: iload #11
    //   267: istore #10
    //   269: fload #6
    //   271: fstore_3
    //   272: fload #5
    //   274: fstore #4
    //   276: aload_0
    //   277: aload #13
    //   279: aload_1
    //   280: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   283: ifeq -> 507
    //   286: iconst_1
    //   287: istore #9
    //   289: aload #17
    //   291: astore #14
    //   293: aload #13
    //   295: astore #15
    //   297: iload #11
    //   299: istore #10
    //   301: fload_2
    //   302: fstore_3
    //   303: fload #5
    //   305: fstore #4
    //   307: goto -> 507
    //   310: aload #17
    //   312: astore #14
    //   314: aload #16
    //   316: astore #15
    //   318: iload #12
    //   320: istore #9
    //   322: iload #11
    //   324: istore #10
    //   326: fload #6
    //   328: fstore_3
    //   329: fload #5
    //   331: fstore #4
    //   333: aload #16
    //   335: ifnonnull -> 507
    //   338: aload #17
    //   340: astore #14
    //   342: aload #16
    //   344: astore #15
    //   346: iload #12
    //   348: istore #9
    //   350: iload #11
    //   352: istore #10
    //   354: fload #6
    //   356: fstore_3
    //   357: fload #5
    //   359: fstore #4
    //   361: fload_2
    //   362: fconst_0
    //   363: fcmpg
    //   364: ifge -> 507
    //   367: aload #17
    //   369: ifnonnull -> 406
    //   372: aload_0
    //   373: aload #13
    //   375: aload_1
    //   376: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   379: istore #9
    //   381: iload #9
    //   383: istore #10
    //   385: aload #13
    //   387: astore #14
    //   389: aload #16
    //   391: astore #15
    //   393: iload #12
    //   395: istore #9
    //   397: fload #6
    //   399: fstore_3
    //   400: fload_2
    //   401: fstore #4
    //   403: goto -> 507
    //   406: fload #5
    //   408: fload_2
    //   409: fcmpl
    //   410: ifle -> 425
    //   413: aload_0
    //   414: aload #13
    //   416: aload_1
    //   417: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   420: istore #9
    //   422: goto -> 381
    //   425: aload #17
    //   427: astore #14
    //   429: aload #16
    //   431: astore #15
    //   433: iload #12
    //   435: istore #9
    //   437: iload #11
    //   439: istore #10
    //   441: fload #6
    //   443: fstore_3
    //   444: fload #5
    //   446: fstore #4
    //   448: iload #11
    //   450: ifne -> 507
    //   453: aload #17
    //   455: astore #14
    //   457: aload #16
    //   459: astore #15
    //   461: iload #12
    //   463: istore #9
    //   465: iload #11
    //   467: istore #10
    //   469: fload #6
    //   471: fstore_3
    //   472: fload #5
    //   474: fstore #4
    //   476: aload_0
    //   477: aload #13
    //   479: aload_1
    //   480: invokespecial isNew : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/LinearSystem;)Z
    //   483: ifeq -> 507
    //   486: iconst_1
    //   487: istore #10
    //   489: fload_2
    //   490: fstore #4
    //   492: fload #6
    //   494: fstore_3
    //   495: iload #12
    //   497: istore #9
    //   499: aload #16
    //   501: astore #15
    //   503: aload #13
    //   505: astore #14
    //   507: aload_0
    //   508: getfield mArrayNextIndices : [I
    //   511: iload #8
    //   513: iaload
    //   514: istore #8
    //   516: iinc #7, 1
    //   519: aload #14
    //   521: astore #17
    //   523: aload #15
    //   525: astore #16
    //   527: iload #9
    //   529: istore #12
    //   531: iload #10
    //   533: istore #11
    //   535: fload_3
    //   536: fstore #6
    //   538: fload #4
    //   540: fstore #5
    //   542: goto -> 28
    //   545: aload #16
    //   547: ifnull -> 553
    //   550: aload #16
    //   552: areturn
    //   553: aload #17
    //   555: areturn
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  final boolean containsKey(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    if (i == -1)
      return false; 
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(solverVariable);
        stringBuilder.append(" = ");
        stringBuilder.append(getVariableValue(b));
        stringBuilder.append(" ");
        printStream.print(stringBuilder.toString());
      } 
    } 
    System.out.println(" }");
  }
  
  void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  SolverVariable getPivotCandidate() {
    // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroidx/constraintlayout/solver/SolverVariable;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnonnull -> 105
    //   9: aload_0
    //   10: getfield mHead : I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_1
    //   16: aconst_null
    //   17: astore #4
    //   19: iload_2
    //   20: iconst_m1
    //   21: if_icmpeq -> 102
    //   24: iload_1
    //   25: aload_0
    //   26: getfield currentSize : I
    //   29: if_icmpge -> 102
    //   32: aload #4
    //   34: astore_3
    //   35: aload_0
    //   36: getfield mArrayValues : [F
    //   39: iload_2
    //   40: faload
    //   41: fconst_0
    //   42: fcmpg
    //   43: ifge -> 86
    //   46: aload_0
    //   47: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   50: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   53: aload_0
    //   54: getfield mArrayIndices : [I
    //   57: iload_2
    //   58: iaload
    //   59: aaload
    //   60: astore #5
    //   62: aload #4
    //   64: ifnull -> 83
    //   67: aload #4
    //   69: astore_3
    //   70: aload #4
    //   72: getfield strength : I
    //   75: aload #5
    //   77: getfield strength : I
    //   80: if_icmpge -> 86
    //   83: aload #5
    //   85: astore_3
    //   86: aload_0
    //   87: getfield mArrayNextIndices : [I
    //   90: iload_2
    //   91: iaload
    //   92: istore_2
    //   93: iinc #1, 1
    //   96: aload_3
    //   97: astore #4
    //   99: goto -> 19
    //   102: aload #4
    //   104: areturn
    //   105: aload_3
    //   106: areturn
  }
  
  SolverVariable getPivotCandidate(boolean[] paramArrayOfboolean, SolverVariable paramSolverVariable) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore #7
    //   6: iconst_0
    //   7: istore #6
    //   9: aconst_null
    //   10: astore #8
    //   12: fconst_0
    //   13: fstore_3
    //   14: iload #7
    //   16: iconst_m1
    //   17: if_icmpeq -> 181
    //   20: iload #6
    //   22: aload_0
    //   23: getfield currentSize : I
    //   26: if_icmpge -> 181
    //   29: aload #8
    //   31: astore #9
    //   33: fload_3
    //   34: fstore #4
    //   36: aload_0
    //   37: getfield mArrayValues : [F
    //   40: iload #7
    //   42: faload
    //   43: fconst_0
    //   44: fcmpg
    //   45: ifge -> 159
    //   48: aload_0
    //   49: getfield mCache : Landroidx/constraintlayout/solver/Cache;
    //   52: getfield mIndexedVariables : [Landroidx/constraintlayout/solver/SolverVariable;
    //   55: aload_0
    //   56: getfield mArrayIndices : [I
    //   59: iload #7
    //   61: iaload
    //   62: aaload
    //   63: astore #10
    //   65: aload_1
    //   66: ifnull -> 86
    //   69: aload #8
    //   71: astore #9
    //   73: fload_3
    //   74: fstore #4
    //   76: aload_1
    //   77: aload #10
    //   79: getfield id : I
    //   82: baload
    //   83: ifne -> 159
    //   86: aload #8
    //   88: astore #9
    //   90: fload_3
    //   91: fstore #4
    //   93: aload #10
    //   95: aload_2
    //   96: if_acmpeq -> 159
    //   99: aload #10
    //   101: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   104: getstatic androidx/constraintlayout/solver/SolverVariable$Type.SLACK : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   107: if_acmpeq -> 128
    //   110: aload #8
    //   112: astore #9
    //   114: fload_3
    //   115: fstore #4
    //   117: aload #10
    //   119: getfield mType : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   122: getstatic androidx/constraintlayout/solver/SolverVariable$Type.ERROR : Landroidx/constraintlayout/solver/SolverVariable$Type;
    //   125: if_acmpne -> 159
    //   128: aload_0
    //   129: getfield mArrayValues : [F
    //   132: iload #7
    //   134: faload
    //   135: fstore #5
    //   137: aload #8
    //   139: astore #9
    //   141: fload_3
    //   142: fstore #4
    //   144: fload #5
    //   146: fload_3
    //   147: fcmpg
    //   148: ifge -> 159
    //   151: aload #10
    //   153: astore #9
    //   155: fload #5
    //   157: fstore #4
    //   159: aload_0
    //   160: getfield mArrayNextIndices : [I
    //   163: iload #7
    //   165: iaload
    //   166: istore #7
    //   168: iinc #6, 1
    //   171: aload #9
    //   173: astore #8
    //   175: fload #4
    //   177: fstore_3
    //   178: goto -> 14
    //   181: aload #8
    //   183: areturn
  }
  
  final SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  final float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    int i = this.mHead;
    if (i == -1) {
      this.mHead = 0;
      this.mArrayValues[0] = paramFloat;
      this.mArrayIndices[0] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        i = this.mLast + 1;
        this.mLast = i;
        arrayOfInt1 = this.mArrayIndices;
        if (i >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int j = 0;
    int k = -1;
    while (i != -1 && j < this.currentSize) {
      if (this.mArrayIndices[i] == ((SolverVariable)arrayOfInt1).id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < ((SolverVariable)arrayOfInt1).id)
        k = i; 
      i = this.mArrayNextIndices[i];
      j++;
    } 
    i = this.mLast;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[i] != -1)
        i = arrayOfInt.length; 
    } else {
      i++;
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        byte b = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (b < arrayOfInt2.length) {
            if (arrayOfInt2[b] == -1) {
              j = b;
              break;
            } 
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      j = this.ROW_SIZE * 2;
      this.ROW_SIZE = j;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, j);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = ((SolverVariable)arrayOfInt1).id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    ((SolverVariable)arrayOfInt1).usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.currentSize >= this.mArrayIndices.length)
      this.mDidFillOnce = true; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    int i = this.mHead;
    if (i == -1)
      return 0.0F; 
    byte b = 0;
    int j = -1;
    while (i != -1 && b < this.currentSize) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          int[] arrayOfInt = this.mArrayNextIndices;
          arrayOfInt[j] = arrayOfInt[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      int k = this.mArrayNextIndices[i];
      b++;
      j = i;
      i = k;
    } 
    return 0.0F;
  }
  
  int sizeInBytes() {
    return this.mArrayIndices.length * 4 * 3 + 0 + 36;
  }
  
  public String toString() {
    int i = this.mHead;
    String str = "";
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(" -> ");
      str = stringBuilder2.toString();
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(str);
      stringBuilder2.append(this.mArrayValues[i]);
      stringBuilder2.append(" : ");
      String str1 = stringBuilder2.toString();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(this.mCache.mIndexedVariables[this.mArrayIndices[i]]);
      str = stringBuilder1.toString();
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  final void updateFromRow(ArrayRow paramArrayRow1, ArrayRow paramArrayRow2, boolean paramBoolean) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        if (this.mArrayIndices[i] == paramArrayRow2.variable.id) {
          float f = this.mArrayValues[i];
          remove(paramArrayRow2.variable, paramBoolean);
          ArrayLinkedVariables arrayLinkedVariables = paramArrayRow2.variables;
          j = arrayLinkedVariables.mHead;
          for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, paramBoolean);
            j = arrayLinkedVariables.mArrayNextIndices[j];
          } 
          paramArrayRow1.constantValue += paramArrayRow2.constantValue * f;
          if (paramBoolean)
            paramArrayRow2.variable.removeFromRow(paramArrayRow1); 
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
  
  void updateFromSystem(ArrayRow paramArrayRow, ArrayRow[] paramArrayOfArrayRow) {
    int i = this.mHead;
    label22: while (true) {
      for (int j = 0; i != -1 && j < this.currentSize; j++) {
        SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
        if (solverVariable.definitionId != -1) {
          float f = this.mArrayValues[i];
          remove(solverVariable, true);
          ArrayRow arrayRow = paramArrayOfArrayRow[solverVariable.definitionId];
          if (!arrayRow.isSimpleDefinition) {
            ArrayLinkedVariables arrayLinkedVariables = arrayRow.variables;
            j = arrayLinkedVariables.mHead;
            for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
              add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, true);
              j = arrayLinkedVariables.mArrayNextIndices[j];
            } 
          } 
          paramArrayRow.constantValue += arrayRow.constantValue * f;
          arrayRow.variable.removeFromRow(paramArrayRow);
          i = this.mHead;
          continue label22;
        } 
        i = this.mArrayNextIndices[i];
      } 
      break;
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\ArrayLinkedVariables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */