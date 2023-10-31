package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;

public class ResolutionAnchor extends ResolutionNode {
  public static final int BARRIER_CONNECTION = 5;
  
  public static final int CENTER_CONNECTION = 2;
  
  public static final int CHAIN_CONNECTION = 4;
  
  public static final int DIRECT_CONNECTION = 1;
  
  public static final int MATCH_CONNECTION = 3;
  
  public static final int UNCONNECTED = 0;
  
  float computedValue;
  
  private ResolutionDimension dimension = null;
  
  private int dimensionMultiplier = 1;
  
  ConstraintAnchor myAnchor;
  
  float offset;
  
  private ResolutionAnchor opposite;
  
  private ResolutionDimension oppositeDimension = null;
  
  private int oppositeDimensionMultiplier = 1;
  
  private float oppositeOffset;
  
  float resolvedOffset;
  
  ResolutionAnchor resolvedTarget;
  
  ResolutionAnchor target;
  
  int type = 0;
  
  public ResolutionAnchor(ConstraintAnchor paramConstraintAnchor) {
    this.myAnchor = paramConstraintAnchor;
  }
  
  void addResolvedValue(LinearSystem paramLinearSystem) {
    SolverVariable solverVariable = this.myAnchor.getSolverVariable();
    ResolutionAnchor resolutionAnchor = this.resolvedTarget;
    if (resolutionAnchor == null) {
      paramLinearSystem.addEquality(solverVariable, (int)(this.resolvedOffset + 0.5F));
    } else {
      paramLinearSystem.addEquality(solverVariable, paramLinearSystem.createObjectVariable(resolutionAnchor.myAnchor), (int)(this.resolvedOffset + 0.5F), 6);
    } 
  }
  
  public void dependsOn(int paramInt1, ResolutionAnchor paramResolutionAnchor, int paramInt2) {
    this.type = paramInt1;
    this.target = paramResolutionAnchor;
    this.offset = paramInt2;
    paramResolutionAnchor.addDependent(this);
  }
  
  public void dependsOn(ResolutionAnchor paramResolutionAnchor, int paramInt) {
    this.target = paramResolutionAnchor;
    this.offset = paramInt;
    paramResolutionAnchor.addDependent(this);
  }
  
  public void dependsOn(ResolutionAnchor paramResolutionAnchor, int paramInt, ResolutionDimension paramResolutionDimension) {
    this.target = paramResolutionAnchor;
    paramResolutionAnchor.addDependent(this);
    this.dimension = paramResolutionDimension;
    this.dimensionMultiplier = paramInt;
    paramResolutionDimension.addDependent(this);
  }
  
  public float getResolvedValue() {
    return this.resolvedOffset;
  }
  
  public void remove(ResolutionDimension paramResolutionDimension) {
    ResolutionDimension resolutionDimension = this.dimension;
    if (resolutionDimension == paramResolutionDimension) {
      this.dimension = null;
      this.offset = this.dimensionMultiplier;
    } else if (resolutionDimension == this.oppositeDimension) {
      this.oppositeDimension = null;
      this.oppositeOffset = this.oppositeDimensionMultiplier;
    } 
    resolve();
  }
  
  public void reset() {
    super.reset();
    this.target = null;
    this.offset = 0.0F;
    this.dimension = null;
    this.dimensionMultiplier = 1;
    this.oppositeDimension = null;
    this.oppositeDimensionMultiplier = 1;
    this.resolvedTarget = null;
    this.resolvedOffset = 0.0F;
    this.computedValue = 0.0F;
    this.opposite = null;
    this.oppositeOffset = 0.0F;
    this.type = 0;
  }
  
  public void resolve() {
    int i = this.state;
    int j = 1;
    if (i == 1)
      return; 
    if (this.type == 4)
      return; 
    ResolutionDimension resolutionDimension = this.dimension;
    if (resolutionDimension != null) {
      if (resolutionDimension.state != 1)
        return; 
      this.offset = this.dimensionMultiplier * this.dimension.value;
    } 
    resolutionDimension = this.oppositeDimension;
    if (resolutionDimension != null) {
      if (resolutionDimension.state != 1)
        return; 
      this.oppositeOffset = this.oppositeDimensionMultiplier * this.oppositeDimension.value;
    } 
    if (this.type == 1) {
      ResolutionAnchor resolutionAnchor = this.target;
      if (resolutionAnchor == null || resolutionAnchor.state == 1) {
        resolutionAnchor = this.target;
        if (resolutionAnchor == null) {
          this.resolvedTarget = this;
          this.resolvedOffset = this.offset;
        } else {
          this.resolvedTarget = resolutionAnchor.resolvedTarget;
          resolutionAnchor.resolvedOffset += this.offset;
        } 
        didResolve();
        return;
      } 
    } 
    if (this.type == 2) {
      ResolutionAnchor resolutionAnchor = this.target;
      if (resolutionAnchor != null && resolutionAnchor.state == 1) {
        resolutionAnchor = this.opposite;
        if (resolutionAnchor != null) {
          resolutionAnchor = resolutionAnchor.target;
          if (resolutionAnchor != null && resolutionAnchor.state == 1) {
            float f1;
            if (LinearSystem.getMetrics() != null) {
              Metrics metrics = LinearSystem.getMetrics();
              metrics.centerConnectionResolved++;
            } 
            this.resolvedTarget = this.target.resolvedTarget;
            resolutionAnchor = this.opposite;
            resolutionAnchor.resolvedTarget = resolutionAnchor.target.resolvedTarget;
            ConstraintAnchor.Type type1 = this.myAnchor.mType;
            ConstraintAnchor.Type type2 = ConstraintAnchor.Type.RIGHT;
            int k = 0;
            i = j;
            if (type1 != type2)
              if (this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                i = j;
              } else {
                i = 0;
              }  
            if (i != 0) {
              f1 = this.target.resolvedOffset;
              f2 = this.opposite.target.resolvedOffset;
            } else {
              f1 = this.opposite.target.resolvedOffset;
              f2 = this.target.resolvedOffset;
            } 
            f1 -= f2;
            if (this.myAnchor.mType == ConstraintAnchor.Type.LEFT || this.myAnchor.mType == ConstraintAnchor.Type.RIGHT) {
              f2 = f1 - this.myAnchor.mOwner.getWidth();
              f1 = this.myAnchor.mOwner.mHorizontalBiasPercent;
            } else {
              f2 = f1 - this.myAnchor.mOwner.getHeight();
              f1 = this.myAnchor.mOwner.mVerticalBiasPercent;
            } 
            int m = this.myAnchor.getMargin();
            j = this.opposite.myAnchor.getMargin();
            if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
              f1 = 0.5F;
              j = 0;
            } else {
              k = m;
            } 
            float f3 = k;
            float f4 = j;
            float f2 = f2 - f3 - f4;
            if (i != 0) {
              ResolutionAnchor resolutionAnchor1 = this.opposite;
              resolutionAnchor1.resolvedOffset = resolutionAnchor1.target.resolvedOffset + f4 + f2 * f1;
              this.resolvedOffset = this.target.resolvedOffset - f3 - f2 * (1.0F - f1);
            } else {
              this.resolvedOffset = this.target.resolvedOffset + f3 + f2 * f1;
              ResolutionAnchor resolutionAnchor1 = this.opposite;
              resolutionAnchor1.resolvedOffset = resolutionAnchor1.target.resolvedOffset - f4 - f2 * (1.0F - f1);
            } 
            didResolve();
            this.opposite.didResolve();
            return;
          } 
        } 
      } 
    } 
    if (this.type == 3) {
      ResolutionAnchor resolutionAnchor = this.target;
      if (resolutionAnchor != null && resolutionAnchor.state == 1) {
        resolutionAnchor = this.opposite;
        if (resolutionAnchor != null) {
          resolutionAnchor = resolutionAnchor.target;
          if (resolutionAnchor != null && resolutionAnchor.state == 1) {
            if (LinearSystem.getMetrics() != null) {
              Metrics metrics = LinearSystem.getMetrics();
              metrics.matchConnectionResolved++;
            } 
            resolutionAnchor = this.target;
            this.resolvedTarget = resolutionAnchor.resolvedTarget;
            ResolutionAnchor resolutionAnchor1 = this.opposite;
            ResolutionAnchor resolutionAnchor2 = resolutionAnchor1.target;
            resolutionAnchor1.resolvedTarget = resolutionAnchor2.resolvedTarget;
            resolutionAnchor.resolvedOffset += this.offset;
            resolutionAnchor2.resolvedOffset += resolutionAnchor1.offset;
            didResolve();
            this.opposite.didResolve();
            return;
          } 
        } 
      } 
    } 
    if (this.type == 5)
      this.myAnchor.mOwner.resolve(); 
  }
  
  public void resolve(ResolutionAnchor paramResolutionAnchor, float paramFloat) {
    if (this.state == 0 || (this.resolvedTarget != paramResolutionAnchor && this.resolvedOffset != paramFloat)) {
      this.resolvedTarget = paramResolutionAnchor;
      this.resolvedOffset = paramFloat;
      if (this.state == 1)
        invalidate(); 
      didResolve();
    } 
  }
  
  String sType(int paramInt) {
    return (paramInt == 1) ? "DIRECT" : ((paramInt == 2) ? "CENTER" : ((paramInt == 3) ? "MATCH" : ((paramInt == 4) ? "CHAIN" : ((paramInt == 5) ? "BARRIER" : "UNCONNECTED"))));
  }
  
  public void setOpposite(ResolutionAnchor paramResolutionAnchor, float paramFloat) {
    this.opposite = paramResolutionAnchor;
    this.oppositeOffset = paramFloat;
  }
  
  public void setOpposite(ResolutionAnchor paramResolutionAnchor, int paramInt, ResolutionDimension paramResolutionDimension) {
    this.opposite = paramResolutionAnchor;
    this.oppositeDimension = paramResolutionDimension;
    this.oppositeDimensionMultiplier = paramInt;
  }
  
  public void setType(int paramInt) {
    this.type = paramInt;
  }
  
  public String toString() {
    if (this.state == 1) {
      if (this.resolvedTarget == this) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("[");
        stringBuilder2.append(this.myAnchor);
        stringBuilder2.append(", RESOLVED: ");
        stringBuilder2.append(this.resolvedOffset);
        stringBuilder2.append("]  type: ");
        stringBuilder2.append(sType(this.type));
        return stringBuilder2.toString();
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("[");
      stringBuilder1.append(this.myAnchor);
      stringBuilder1.append(", RESOLVED: ");
      stringBuilder1.append(this.resolvedTarget);
      stringBuilder1.append(":");
      stringBuilder1.append(this.resolvedOffset);
      stringBuilder1.append("] type: ");
      stringBuilder1.append(sType(this.type));
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ ");
    stringBuilder.append(this.myAnchor);
    stringBuilder.append(" UNRESOLVED} type: ");
    stringBuilder.append(sType(this.type));
    return stringBuilder.toString();
  }
  
  public void update() {
    // Byte code:
    //   0: aload_0
    //   1: getfield myAnchor : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   4: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   7: astore_3
    //   8: aload_3
    //   9: ifnonnull -> 13
    //   12: return
    //   13: aload_3
    //   14: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   17: aload_0
    //   18: getfield myAnchor : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   21: if_acmpne -> 37
    //   24: aload_0
    //   25: iconst_4
    //   26: putfield type : I
    //   29: aload_3
    //   30: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   33: iconst_4
    //   34: putfield type : I
    //   37: aload_0
    //   38: getfield myAnchor : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   41: invokevirtual getMargin : ()I
    //   44: istore_2
    //   45: aload_0
    //   46: getfield myAnchor : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   49: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   52: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   55: if_acmpeq -> 73
    //   58: iload_2
    //   59: istore_1
    //   60: aload_0
    //   61: getfield myAnchor : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   64: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   67: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   70: if_acmpne -> 76
    //   73: iload_2
    //   74: ineg
    //   75: istore_1
    //   76: aload_0
    //   77: aload_3
    //   78: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   81: iload_1
    //   82: invokevirtual dependsOn : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;I)V
    //   85: return
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ResolutionAnchor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */