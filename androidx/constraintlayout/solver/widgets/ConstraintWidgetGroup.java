package androidx.constraintlayout.solver.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
  public List<ConstraintWidget> mConstrainedGroup;
  
  public final int[] mGroupDimensions = new int[] { -1, -1 };
  
  int mGroupHeight = -1;
  
  int mGroupWidth = -1;
  
  public boolean mSkipSolver = false;
  
  List<ConstraintWidget> mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
  
  List<ConstraintWidget> mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
  
  List<ConstraintWidget> mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
  
  HashSet<ConstraintWidget> mWidgetsToSetHorizontal = new HashSet<ConstraintWidget>();
  
  HashSet<ConstraintWidget> mWidgetsToSetVertical = new HashSet<ConstraintWidget>();
  
  List<ConstraintWidget> mWidgetsToSolve = new ArrayList<ConstraintWidget>();
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList) {
    this.mConstrainedGroup = paramList;
  }
  
  ConstraintWidgetGroup(List<ConstraintWidget> paramList, boolean paramBoolean) {
    this.mConstrainedGroup = paramList;
    this.mSkipSolver = paramBoolean;
  }
  
  private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> paramArrayList, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidget.mGroupsToSolver)
      return; 
    paramArrayList.add(paramConstraintWidget);
    paramConstraintWidget.mGroupsToSolver = true;
    if (paramConstraintWidget.isFullyResolved())
      return; 
    boolean bool = paramConstraintWidget instanceof Helper;
    byte b2 = 0;
    if (bool) {
      Helper helper = (Helper)paramConstraintWidget;
      int j = helper.mWidgetsCount;
      for (byte b = 0; b < j; b++)
        getWidgetsToSolveTraversal(paramArrayList, helper.mWidgets[b]); 
    } 
    int i = paramConstraintWidget.mListAnchors.length;
    for (byte b1 = b2; b1 < i; b1++) {
      ConstraintAnchor constraintAnchor = (paramConstraintWidget.mListAnchors[b1]).mTarget;
      if (constraintAnchor != null) {
        ConstraintWidget constraintWidget = constraintAnchor.mOwner;
        if (constraintAnchor != null && constraintWidget != paramConstraintWidget.getParent())
          getWidgetsToSolveTraversal(paramArrayList, constraintWidget); 
      } 
    } 
  }
  
  private void updateResolvedDimension(ConstraintWidget paramConstraintWidget) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mOptimizerMeasurable : Z
    //   4: ifeq -> 437
    //   7: aload_1
    //   8: invokevirtual isFullyResolved : ()Z
    //   11: ifeq -> 15
    //   14: return
    //   15: aload_1
    //   16: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   19: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   22: astore #5
    //   24: iconst_0
    //   25: istore #4
    //   27: aload #5
    //   29: ifnull -> 37
    //   32: iconst_1
    //   33: istore_3
    //   34: goto -> 39
    //   37: iconst_0
    //   38: istore_3
    //   39: iload_3
    //   40: ifeq -> 55
    //   43: aload_1
    //   44: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   47: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   50: astore #5
    //   52: goto -> 64
    //   55: aload_1
    //   56: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   59: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   62: astore #5
    //   64: aload #5
    //   66: ifnull -> 144
    //   69: aload #5
    //   71: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   74: getfield mOptimizerMeasured : Z
    //   77: ifne -> 89
    //   80: aload_0
    //   81: aload #5
    //   83: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   86: invokespecial updateResolvedDimension : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;)V
    //   89: aload #5
    //   91: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   94: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   97: if_acmpne -> 121
    //   100: aload #5
    //   102: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   105: getfield mX : I
    //   108: aload #5
    //   110: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   113: invokevirtual getWidth : ()I
    //   116: iadd
    //   117: istore_2
    //   118: goto -> 146
    //   121: aload #5
    //   123: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   126: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   129: if_acmpne -> 144
    //   132: aload #5
    //   134: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   137: getfield mX : I
    //   140: istore_2
    //   141: goto -> 146
    //   144: iconst_0
    //   145: istore_2
    //   146: iload_3
    //   147: ifeq -> 163
    //   150: iload_2
    //   151: aload_1
    //   152: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   155: invokevirtual getMargin : ()I
    //   158: isub
    //   159: istore_2
    //   160: goto -> 178
    //   163: iload_2
    //   164: aload_1
    //   165: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   168: invokevirtual getMargin : ()I
    //   171: aload_1
    //   172: invokevirtual getWidth : ()I
    //   175: iadd
    //   176: iadd
    //   177: istore_2
    //   178: aload_1
    //   179: iload_2
    //   180: aload_1
    //   181: invokevirtual getWidth : ()I
    //   184: isub
    //   185: iload_2
    //   186: invokevirtual setHorizontalDimension : (II)V
    //   189: aload_1
    //   190: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   193: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   196: ifnull -> 268
    //   199: aload_1
    //   200: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   203: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   206: astore #5
    //   208: aload #5
    //   210: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   213: getfield mOptimizerMeasured : Z
    //   216: ifne -> 228
    //   219: aload_0
    //   220: aload #5
    //   222: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   225: invokespecial updateResolvedDimension : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;)V
    //   228: aload #5
    //   230: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   233: getfield mY : I
    //   236: aload #5
    //   238: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   241: getfield mBaselineDistance : I
    //   244: iadd
    //   245: aload_1
    //   246: getfield mBaselineDistance : I
    //   249: isub
    //   250: istore_2
    //   251: aload_1
    //   252: iload_2
    //   253: aload_1
    //   254: getfield mHeight : I
    //   257: iload_2
    //   258: iadd
    //   259: invokevirtual setVerticalDimension : (II)V
    //   262: aload_1
    //   263: iconst_1
    //   264: putfield mOptimizerMeasured : Z
    //   267: return
    //   268: aload_1
    //   269: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   272: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   275: ifnull -> 281
    //   278: iconst_1
    //   279: istore #4
    //   281: iload #4
    //   283: ifeq -> 298
    //   286: aload_1
    //   287: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   290: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   293: astore #5
    //   295: goto -> 307
    //   298: aload_1
    //   299: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   302: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   305: astore #5
    //   307: iload_2
    //   308: istore_3
    //   309: aload #5
    //   311: ifnull -> 388
    //   314: aload #5
    //   316: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   319: getfield mOptimizerMeasured : Z
    //   322: ifne -> 334
    //   325: aload_0
    //   326: aload #5
    //   328: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   331: invokespecial updateResolvedDimension : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;)V
    //   334: aload #5
    //   336: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   339: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   342: if_acmpne -> 366
    //   345: aload #5
    //   347: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   350: getfield mY : I
    //   353: aload #5
    //   355: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   358: invokevirtual getHeight : ()I
    //   361: iadd
    //   362: istore_3
    //   363: goto -> 388
    //   366: iload_2
    //   367: istore_3
    //   368: aload #5
    //   370: getfield mType : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   373: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   376: if_acmpne -> 388
    //   379: aload #5
    //   381: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   384: getfield mY : I
    //   387: istore_3
    //   388: iload #4
    //   390: ifeq -> 406
    //   393: iload_3
    //   394: aload_1
    //   395: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   398: invokevirtual getMargin : ()I
    //   401: isub
    //   402: istore_2
    //   403: goto -> 421
    //   406: iload_3
    //   407: aload_1
    //   408: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   411: invokevirtual getMargin : ()I
    //   414: aload_1
    //   415: invokevirtual getHeight : ()I
    //   418: iadd
    //   419: iadd
    //   420: istore_2
    //   421: aload_1
    //   422: iload_2
    //   423: aload_1
    //   424: invokevirtual getHeight : ()I
    //   427: isub
    //   428: iload_2
    //   429: invokevirtual setVerticalDimension : (II)V
    //   432: aload_1
    //   433: iconst_1
    //   434: putfield mOptimizerMeasured : Z
    //   437: return
  }
  
  void addWidgetsToSet(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      this.mWidgetsToSetHorizontal.add(paramConstraintWidget);
    } else if (paramInt == 1) {
      this.mWidgetsToSetVertical.add(paramConstraintWidget);
    } 
  }
  
  public List<ConstraintWidget> getStartWidgets(int paramInt) {
    return (paramInt == 0) ? this.mStartHorizontalWidgets : ((paramInt == 1) ? this.mStartVerticalWidgets : null);
  }
  
  Set<ConstraintWidget> getWidgetsToSet(int paramInt) {
    return (paramInt == 0) ? this.mWidgetsToSetHorizontal : ((paramInt == 1) ? this.mWidgetsToSetVertical : null);
  }
  
  List<ConstraintWidget> getWidgetsToSolve() {
    if (!this.mWidgetsToSolve.isEmpty())
      return this.mWidgetsToSolve; 
    int i = this.mConstrainedGroup.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mConstrainedGroup.get(b);
      if (!constraintWidget.mOptimizerMeasurable)
        getWidgetsToSolveTraversal((ArrayList<ConstraintWidget>)this.mWidgetsToSolve, constraintWidget); 
    } 
    this.mUnresolvedWidgets.clear();
    this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
    this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
    return this.mWidgetsToSolve;
  }
  
  void updateUnresolvedWidgets() {
    int i = this.mUnresolvedWidgets.size();
    for (byte b = 0; b < i; b++)
      updateResolvedDimension(this.mUnresolvedWidgets.get(b)); 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ConstraintWidgetGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */