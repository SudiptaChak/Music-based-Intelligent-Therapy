package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.SolverVariable;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
  private static final boolean ALLOW_BINARY = false;
  
  public static final int AUTO_CONSTRAINT_CREATOR = 2;
  
  public static final int SCOUT_CREATOR = 1;
  
  private static final int UNSET_GONE_MARGIN = -1;
  
  public static final int USER_CREATOR = 0;
  
  private int mConnectionCreator = 0;
  
  private ConnectionType mConnectionType = ConnectionType.RELAXED;
  
  int mGoneMargin = -1;
  
  public int mMargin = 0;
  
  final ConstraintWidget mOwner;
  
  private ResolutionAnchor mResolutionAnchor = new ResolutionAnchor(this);
  
  SolverVariable mSolverVariable;
  
  private Strength mStrength = Strength.NONE;
  
  ConstraintAnchor mTarget;
  
  final Type mType;
  
  public ConstraintAnchor(ConstraintWidget paramConstraintWidget, Type paramType) {
    this.mOwner = paramConstraintWidget;
    this.mType = paramType;
  }
  
  private boolean isConnectionToMe(ConstraintWidget paramConstraintWidget, HashSet<ConstraintWidget> paramHashSet) {
    if (paramHashSet.contains(paramConstraintWidget))
      return false; 
    paramHashSet.add(paramConstraintWidget);
    if (paramConstraintWidget == getOwner())
      return true; 
    ArrayList<ConstraintAnchor> arrayList = paramConstraintWidget.getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), paramHashSet))
        return true; 
    } 
    return false;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt) {
    return connect(paramConstraintAnchor, paramInt, -1, Strength.STRONG, 0, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, Strength.STRONG, paramInt2, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2, Strength paramStrength, int paramInt3, boolean paramBoolean) {
    if (paramConstraintAnchor == null) {
      this.mTarget = null;
      this.mMargin = 0;
      this.mGoneMargin = -1;
      this.mStrength = Strength.NONE;
      this.mConnectionCreator = 2;
      return true;
    } 
    if (!paramBoolean && !isValidConnection(paramConstraintAnchor))
      return false; 
    this.mTarget = paramConstraintAnchor;
    if (paramInt1 > 0) {
      this.mMargin = paramInt1;
    } else {
      this.mMargin = 0;
    } 
    this.mGoneMargin = paramInt2;
    this.mStrength = paramStrength;
    this.mConnectionCreator = paramInt3;
    return true;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, Strength paramStrength, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, paramStrength, paramInt2, false);
  }
  
  public int getConnectionCreator() {
    return this.mConnectionCreator;
  }
  
  public ConnectionType getConnectionType() {
    return this.mConnectionType;
  }
  
  public int getMargin() {
    if (this.mOwner.getVisibility() == 8)
      return 0; 
    if (this.mGoneMargin > -1) {
      ConstraintAnchor constraintAnchor = this.mTarget;
      if (constraintAnchor != null && constraintAnchor.mOwner.getVisibility() == 8)
        return this.mGoneMargin; 
    } 
    return this.mMargin;
  }
  
  public final ConstraintAnchor getOpposite() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return this.mOwner.mTop;
      case null:
        return this.mOwner.mBottom;
      case null:
        return this.mOwner.mLeft;
      case null:
        return this.mOwner.mRight;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return null;
  }
  
  public ConstraintWidget getOwner() {
    return this.mOwner;
  }
  
  public int getPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
        return 0;
      case null:
        return 1;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return 2;
  }
  
  public ResolutionAnchor getResolutionNode() {
    return this.mResolutionAnchor;
  }
  
  public int getSnapPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return 0;
      case null:
        return 1;
      case null:
        return 0;
      case null:
        return 2;
      case null:
      case null:
        return 0;
      case null:
      case null:
        return 1;
      case null:
        break;
    } 
    return 3;
  }
  
  public SolverVariable getSolverVariable() {
    return this.mSolverVariable;
  }
  
  public Strength getStrength() {
    return this.mStrength;
  }
  
  public ConstraintAnchor getTarget() {
    return this.mTarget;
  }
  
  public Type getType() {
    return this.mType;
  }
  
  public boolean isConnected() {
    boolean bool;
    if (this.mTarget != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget) {
    if (isConnectionToMe(paramConstraintWidget, new HashSet<ConstraintWidget>()))
      return false; 
    ConstraintWidget constraintWidget = getOwner().getParent();
    return (constraintWidget == paramConstraintWidget) ? true : ((paramConstraintWidget.getParent() == constraintWidget));
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget, ConstraintAnchor paramConstraintAnchor) {
    return isConnectionAllowed(paramConstraintWidget);
  }
  
  public boolean isSideAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public boolean isSimilarDimensionConnection(ConstraintAnchor paramConstraintAnchor) {
    Type type2 = paramConstraintAnchor.getType();
    Type type1 = this.mType;
    boolean bool3 = true;
    boolean bool1 = true;
    boolean bool2 = true;
    if (type2 == type1)
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return false;
      case null:
      case null:
      case null:
      case null:
        bool1 = bool2;
        if (type2 != Type.TOP) {
          bool1 = bool2;
          if (type2 != Type.BOTTOM) {
            bool1 = bool2;
            if (type2 != Type.CENTER_Y)
              if (type2 == Type.BASELINE) {
                bool1 = bool2;
              } else {
                bool1 = false;
              }  
          } 
        } 
        return bool1;
      case null:
      case null:
      case null:
        bool1 = bool3;
        if (type2 != Type.LEFT) {
          bool1 = bool3;
          if (type2 != Type.RIGHT)
            if (type2 == Type.CENTER_X) {
              bool1 = bool3;
            } else {
              bool1 = false;
            }  
        } 
        return bool1;
      case null:
        break;
    } 
    if (type2 == Type.BASELINE)
      bool1 = false; 
    return bool1;
  }
  
  public boolean isSnapCompatibleWith(ConstraintAnchor paramConstraintAnchor) {
    int i;
    if (this.mType == Type.CENTER)
      return false; 
    if (this.mType == paramConstraintAnchor.getType())
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 4 && i != 5);
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 2 && i != 3);
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 4 && i != 8);
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 5 && i != 8);
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 2 && i != 7);
      case null:
        i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return !(i != 3 && i != 7);
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public boolean isValidConnection(ConstraintAnchor paramConstraintAnchor) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iconst_0
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: aload_1
    //   9: ifnonnull -> 14
    //   12: iconst_0
    //   13: ireturn
    //   14: aload_1
    //   15: invokevirtual getType : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   18: astore #6
    //   20: aload_0
    //   21: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   24: astore #7
    //   26: aload #6
    //   28: aload #7
    //   30: if_acmpne -> 65
    //   33: aload #7
    //   35: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   38: if_acmpne -> 63
    //   41: aload_1
    //   42: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   45: invokevirtual hasBaseline : ()Z
    //   48: ifeq -> 61
    //   51: aload_0
    //   52: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   55: invokevirtual hasBaseline : ()Z
    //   58: ifne -> 63
    //   61: iconst_0
    //   62: ireturn
    //   63: iconst_1
    //   64: ireturn
    //   65: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type : [I
    //   68: aload_0
    //   69: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   72: invokevirtual ordinal : ()I
    //   75: iaload
    //   76: tableswitch default -> 128, 1 -> 263, 2 -> 204, 3 -> 204, 4 -> 145, 5 -> 145, 6 -> 143, 7 -> 143, 8 -> 143, 9 -> 143
    //   128: new java/lang/AssertionError
    //   131: dup
    //   132: aload_0
    //   133: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   136: invokevirtual name : ()Ljava/lang/String;
    //   139: invokespecial <init> : (Ljava/lang/Object;)V
    //   142: athrow
    //   143: iconst_0
    //   144: ireturn
    //   145: aload #6
    //   147: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   150: if_acmpeq -> 169
    //   153: aload #6
    //   155: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   158: if_acmpne -> 164
    //   161: goto -> 169
    //   164: iconst_0
    //   165: istore_2
    //   166: goto -> 171
    //   169: iconst_1
    //   170: istore_2
    //   171: iload_2
    //   172: istore_3
    //   173: aload_1
    //   174: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   177: instanceof androidx/constraintlayout/solver/widgets/Guideline
    //   180: ifeq -> 202
    //   183: iload_2
    //   184: ifne -> 198
    //   187: iload #4
    //   189: istore_2
    //   190: aload #6
    //   192: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   195: if_acmpne -> 200
    //   198: iconst_1
    //   199: istore_2
    //   200: iload_2
    //   201: istore_3
    //   202: iload_3
    //   203: ireturn
    //   204: aload #6
    //   206: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   209: if_acmpeq -> 228
    //   212: aload #6
    //   214: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   217: if_acmpne -> 223
    //   220: goto -> 228
    //   223: iconst_0
    //   224: istore_2
    //   225: goto -> 230
    //   228: iconst_1
    //   229: istore_2
    //   230: iload_2
    //   231: istore_3
    //   232: aload_1
    //   233: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   236: instanceof androidx/constraintlayout/solver/widgets/Guideline
    //   239: ifeq -> 261
    //   242: iload_2
    //   243: ifne -> 257
    //   246: iload #5
    //   248: istore_2
    //   249: aload #6
    //   251: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   254: if_acmpne -> 259
    //   257: iconst_1
    //   258: istore_2
    //   259: iload_2
    //   260: istore_3
    //   261: iload_3
    //   262: ireturn
    //   263: iload_3
    //   264: istore_2
    //   265: aload #6
    //   267: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   270: if_acmpeq -> 295
    //   273: iload_3
    //   274: istore_2
    //   275: aload #6
    //   277: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   280: if_acmpeq -> 295
    //   283: iload_3
    //   284: istore_2
    //   285: aload #6
    //   287: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   290: if_acmpeq -> 295
    //   293: iconst_1
    //   294: istore_2
    //   295: iload_2
    //   296: ireturn
  }
  
  public boolean isVerticalAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public void reset() {
    this.mTarget = null;
    this.mMargin = 0;
    this.mGoneMargin = -1;
    this.mStrength = Strength.STRONG;
    this.mConnectionCreator = 0;
    this.mConnectionType = ConnectionType.RELAXED;
    this.mResolutionAnchor.reset();
  }
  
  public void resetSolverVariable(Cache paramCache) {
    SolverVariable solverVariable = this.mSolverVariable;
    if (solverVariable == null) {
      this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
    } else {
      solverVariable.reset();
    } 
  }
  
  public void setConnectionCreator(int paramInt) {
    this.mConnectionCreator = paramInt;
  }
  
  public void setConnectionType(ConnectionType paramConnectionType) {
    this.mConnectionType = paramConnectionType;
  }
  
  public void setGoneMargin(int paramInt) {
    if (isConnected())
      this.mGoneMargin = paramInt; 
  }
  
  public void setMargin(int paramInt) {
    if (isConnected())
      this.mMargin = paramInt; 
  }
  
  public void setStrength(Strength paramStrength) {
    if (isConnected())
      this.mStrength = paramStrength; 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mOwner.getDebugName());
    stringBuilder.append(":");
    stringBuilder.append(this.mType.toString());
    return stringBuilder.toString();
  }
  
  public enum ConnectionType {
    RELAXED, STRICT;
    
    private static final ConnectionType[] $VALUES;
    
    static {
      ConnectionType connectionType = new ConnectionType("STRICT", 1);
      STRICT = connectionType;
      $VALUES = new ConnectionType[] { RELAXED, connectionType };
    }
  }
  
  public enum Strength {
    NONE, STRONG, WEAK;
    
    private static final Strength[] $VALUES;
    
    static {
      Strength strength = new Strength("WEAK", 2);
      WEAK = strength;
      $VALUES = new Strength[] { NONE, STRONG, strength };
    }
  }
  
  public enum Type {
    NONE, RIGHT, TOP, BASELINE, BOTTOM, CENTER, CENTER_X, CENTER_Y, LEFT;
    
    private static final Type[] $VALUES;
    
    static {
      RIGHT = new Type("RIGHT", 3);
      BOTTOM = new Type("BOTTOM", 4);
      BASELINE = new Type("BASELINE", 5);
      CENTER = new Type("CENTER", 6);
      CENTER_X = new Type("CENTER_X", 7);
      Type type = new Type("CENTER_Y", 8);
      CENTER_Y = type;
      $VALUES = new Type[] { NONE, LEFT, TOP, RIGHT, BOTTOM, BASELINE, CENTER, CENTER_X, type };
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ConstraintAnchor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */