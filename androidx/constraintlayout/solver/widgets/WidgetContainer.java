package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
  protected ArrayList<ConstraintWidget> mChildren = new ArrayList<ConstraintWidget>();
  
  public WidgetContainer() {}
  
  public WidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public WidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static Rectangle getBounds(ArrayList<ConstraintWidget> paramArrayList) {
    Rectangle rectangle = new Rectangle();
    if (paramArrayList.size() == 0)
      return rectangle; 
    int n = paramArrayList.size();
    int m = Integer.MAX_VALUE;
    byte b = 0;
    int j = 0;
    int i = j;
    int k = Integer.MAX_VALUE;
    while (b < n) {
      ConstraintWidget constraintWidget = paramArrayList.get(b);
      int i1 = m;
      if (constraintWidget.getX() < m)
        i1 = constraintWidget.getX(); 
      int i2 = k;
      if (constraintWidget.getY() < k)
        i2 = constraintWidget.getY(); 
      int i3 = j;
      if (constraintWidget.getRight() > j)
        i3 = constraintWidget.getRight(); 
      int i4 = i;
      if (constraintWidget.getBottom() > i)
        i4 = constraintWidget.getBottom(); 
      b++;
      m = i1;
      k = i2;
      j = i3;
      i = i4;
    } 
    rectangle.setBounds(m, k, j - m, i - k);
    return rectangle;
  }
  
  public void add(ConstraintWidget paramConstraintWidget) {
    this.mChildren.add(paramConstraintWidget);
    if (paramConstraintWidget.getParent() != null)
      ((WidgetContainer)paramConstraintWidget.getParent()).remove(paramConstraintWidget); 
    paramConstraintWidget.setParent(this);
  }
  
  public void add(ConstraintWidget... paramVarArgs) {
    int i = paramVarArgs.length;
    for (byte b = 0; b < i; b++)
      add(paramVarArgs[b]); 
  }
  
  public ConstraintWidget findWidget(float paramFloat1, float paramFloat2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getDrawX : ()I
    //   4: istore #6
    //   6: aload_0
    //   7: invokevirtual getDrawY : ()I
    //   10: istore #5
    //   12: aload_0
    //   13: invokevirtual getWidth : ()I
    //   16: istore #4
    //   18: aload_0
    //   19: invokevirtual getHeight : ()I
    //   22: istore_3
    //   23: fload_1
    //   24: iload #6
    //   26: i2f
    //   27: fcmpl
    //   28: iflt -> 66
    //   31: fload_1
    //   32: iload #4
    //   34: iload #6
    //   36: iadd
    //   37: i2f
    //   38: fcmpg
    //   39: ifgt -> 66
    //   42: fload_2
    //   43: iload #5
    //   45: i2f
    //   46: fcmpl
    //   47: iflt -> 66
    //   50: fload_2
    //   51: iload_3
    //   52: iload #5
    //   54: iadd
    //   55: i2f
    //   56: fcmpg
    //   57: ifgt -> 66
    //   60: aload_0
    //   61: astore #9
    //   63: goto -> 69
    //   66: aconst_null
    //   67: astore #9
    //   69: iconst_0
    //   70: istore_3
    //   71: aload_0
    //   72: getfield mChildren : Ljava/util/ArrayList;
    //   75: invokevirtual size : ()I
    //   78: istore #4
    //   80: iload_3
    //   81: iload #4
    //   83: if_icmpge -> 235
    //   86: aload_0
    //   87: getfield mChildren : Ljava/util/ArrayList;
    //   90: iload_3
    //   91: invokevirtual get : (I)Ljava/lang/Object;
    //   94: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidget
    //   97: astore #11
    //   99: aload #11
    //   101: instanceof androidx/constraintlayout/solver/widgets/WidgetContainer
    //   104: ifeq -> 135
    //   107: aload #11
    //   109: checkcast androidx/constraintlayout/solver/widgets/WidgetContainer
    //   112: fload_1
    //   113: fload_2
    //   114: invokevirtual findWidget : (FF)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   117: astore #11
    //   119: aload #9
    //   121: astore #10
    //   123: aload #11
    //   125: ifnull -> 225
    //   128: aload #11
    //   130: astore #9
    //   132: goto -> 221
    //   135: aload #11
    //   137: invokevirtual getDrawX : ()I
    //   140: istore #8
    //   142: aload #11
    //   144: invokevirtual getDrawY : ()I
    //   147: istore #6
    //   149: aload #11
    //   151: invokevirtual getWidth : ()I
    //   154: istore #7
    //   156: aload #11
    //   158: invokevirtual getHeight : ()I
    //   161: istore #5
    //   163: aload #9
    //   165: astore #10
    //   167: fload_1
    //   168: iload #8
    //   170: i2f
    //   171: fcmpl
    //   172: iflt -> 225
    //   175: aload #9
    //   177: astore #10
    //   179: fload_1
    //   180: iload #7
    //   182: iload #8
    //   184: iadd
    //   185: i2f
    //   186: fcmpg
    //   187: ifgt -> 225
    //   190: aload #9
    //   192: astore #10
    //   194: fload_2
    //   195: iload #6
    //   197: i2f
    //   198: fcmpl
    //   199: iflt -> 225
    //   202: aload #9
    //   204: astore #10
    //   206: fload_2
    //   207: iload #5
    //   209: iload #6
    //   211: iadd
    //   212: i2f
    //   213: fcmpg
    //   214: ifgt -> 225
    //   217: aload #11
    //   219: astore #9
    //   221: aload #9
    //   223: astore #10
    //   225: iinc #3, 1
    //   228: aload #10
    //   230: astore #9
    //   232: goto -> 80
    //   235: aload #9
    //   237: areturn
  }
  
  public ArrayList<ConstraintWidget> findWidgets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    Rectangle rectangle = new Rectangle();
    rectangle.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      ConstraintWidget constraintWidget = this.mChildren.get(paramInt1);
      Rectangle rectangle1 = new Rectangle();
      rectangle1.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
      if (rectangle.intersects(rectangle1))
        arrayList.add(constraintWidget); 
    } 
    return arrayList;
  }
  
  public ArrayList<ConstraintWidget> getChildren() {
    return this.mChildren;
  }
  
  public ConstraintWidgetContainer getRootConstraintContainer() {
    ConstraintWidgetContainer constraintWidgetContainer;
    ConstraintWidget constraintWidget = getParent();
    if (this instanceof ConstraintWidgetContainer) {
      constraintWidgetContainer = (ConstraintWidgetContainer)this;
    } else {
      constraintWidgetContainer = null;
    } 
    while (constraintWidget != null) {
      ConstraintWidget constraintWidget1 = constraintWidget.getParent();
      if (constraintWidget instanceof ConstraintWidgetContainer)
        constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget; 
      constraintWidget = constraintWidget1;
    } 
    return constraintWidgetContainer;
  }
  
  public void layout() {
    updateDrawPosition();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof WidgetContainer)
        ((WidgetContainer)constraintWidget).layout(); 
    } 
  }
  
  public void remove(ConstraintWidget paramConstraintWidget) {
    this.mChildren.remove(paramConstraintWidget);
    paramConstraintWidget.setParent(null);
  }
  
  public void removeAllChildren() {
    this.mChildren.clear();
  }
  
  public void reset() {
    this.mChildren.clear();
    super.reset();
  }
  
  public void resetSolverVariables(Cache paramCache) {
    super.resetSolverVariables(paramCache);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetSolverVariables(paramCache); 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    super.setOffset(paramInt1, paramInt2);
    paramInt2 = this.mChildren.size();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
      ((ConstraintWidget)this.mChildren.get(paramInt1)).setOffset(getRootX(), getRootY()); 
  }
  
  public void updateDrawPosition() {
    super.updateDrawPosition();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.setOffset(getDrawX(), getDrawY());
      if (!(constraintWidget instanceof ConstraintWidgetContainer))
        constraintWidget.updateDrawPosition(); 
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\WidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */