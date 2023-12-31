package androidx.constraintlayout.solver.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Analyzer {
  public static void determineGroups(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    if ((paramConstraintWidgetContainer.getOptimizationLevel() & 0x20) != 32) {
      singleGroup(paramConstraintWidgetContainer);
      return;
    } 
    paramConstraintWidgetContainer.mSkipSolver = true;
    paramConstraintWidgetContainer.mGroupsWrapOptimized = false;
    paramConstraintWidgetContainer.mHorizontalWrapOptimized = false;
    paramConstraintWidgetContainer.mVerticalWrapOptimized = false;
    ArrayList<ConstraintWidget> arrayList = paramConstraintWidgetContainer.mChildren;
    List<ConstraintWidgetGroup> list = paramConstraintWidgetContainer.mWidgetGroups;
    if (paramConstraintWidgetContainer.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (paramConstraintWidgetContainer.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (bool1 || bool2) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    list.clear();
    for (ConstraintWidget constraintWidget : arrayList) {
      constraintWidget.mBelongingGroup = null;
      constraintWidget.mGroupsToSolver = false;
      constraintWidget.resetResolutionNodes();
    } 
    for (ConstraintWidget constraintWidget : arrayList) {
      if (constraintWidget.mBelongingGroup == null && !determineGroups(constraintWidget, list, bool3)) {
        singleGroup(paramConstraintWidgetContainer);
        paramConstraintWidgetContainer.mSkipSolver = false;
        return;
      } 
    } 
    Iterator<ConstraintWidgetGroup> iterator = list.iterator();
    int j = 0;
    int i;
    for (i = 0; iterator.hasNext(); i = Math.max(i, getMaxDimension(constraintWidgetGroup, 1))) {
      ConstraintWidgetGroup constraintWidgetGroup = iterator.next();
      j = Math.max(j, getMaxDimension(constraintWidgetGroup, 0));
    } 
    if (bool1) {
      paramConstraintWidgetContainer.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      paramConstraintWidgetContainer.setWidth(j);
      paramConstraintWidgetContainer.mGroupsWrapOptimized = true;
      paramConstraintWidgetContainer.mHorizontalWrapOptimized = true;
      paramConstraintWidgetContainer.mWrapFixedWidth = j;
    } 
    if (bool2) {
      paramConstraintWidgetContainer.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      paramConstraintWidgetContainer.setHeight(i);
      paramConstraintWidgetContainer.mGroupsWrapOptimized = true;
      paramConstraintWidgetContainer.mVerticalWrapOptimized = true;
      paramConstraintWidgetContainer.mWrapFixedHeight = i;
    } 
    setPosition(list, 0, paramConstraintWidgetContainer.getWidth());
    setPosition(list, 1, paramConstraintWidgetContainer.getHeight());
  }
  
  private static boolean determineGroups(ConstraintWidget paramConstraintWidget, List<ConstraintWidgetGroup> paramList, boolean paramBoolean) {
    ConstraintWidgetGroup constraintWidgetGroup = new ConstraintWidgetGroup(new ArrayList<ConstraintWidget>(), true);
    paramList.add(constraintWidgetGroup);
    return traverse(paramConstraintWidget, constraintWidgetGroup, paramList, paramBoolean);
  }
  
  private static int getMaxDimension(ConstraintWidgetGroup paramConstraintWidgetGroup, int paramInt) {
    int k = paramInt * 2;
    List<ConstraintWidget> list = paramConstraintWidgetGroup.getStartWidgets(paramInt);
    int j = list.size();
    byte b = 0;
    int i = 0;
    while (b < j) {
      boolean bool;
      ConstraintWidget constraintWidget = list.get(b);
      ConstraintAnchor[] arrayOfConstraintAnchor = constraintWidget.mListAnchors;
      int m = k + 1;
      if ((arrayOfConstraintAnchor[m]).mTarget == null || ((constraintWidget.mListAnchors[k]).mTarget != null && (constraintWidget.mListAnchors[m]).mTarget != null)) {
        bool = true;
      } else {
        bool = false;
      } 
      i = Math.max(i, getMaxDimensionTraversal(constraintWidget, paramInt, bool, 0));
      b++;
    } 
    paramConstraintWidgetGroup.mGroupDimensions[paramInt] = i;
    return i;
  }
  
  private static int getMaxDimensionTraversal(ConstraintWidget paramConstraintWidget, int paramInt1, boolean paramBoolean, int paramInt2) {
    int i;
    int j;
    byte b;
    int i3;
    boolean bool = paramConstraintWidget.mOptimizerMeasurable;
    int i2 = 0;
    if (!bool)
      return 0; 
    if (paramConstraintWidget.mBaseline.mTarget != null && paramInt1 == 1) {
      k = 1;
    } else {
      k = 0;
    } 
    if (paramBoolean) {
      m = paramConstraintWidget.getBaselineDistance();
      n = paramConstraintWidget.getHeight() - paramConstraintWidget.getBaselineDistance();
      j = paramInt1 * 2;
      i = j + 1;
    } else {
      m = paramConstraintWidget.getHeight() - paramConstraintWidget.getBaselineDistance();
      n = paramConstraintWidget.getBaselineDistance();
      i = paramInt1 * 2;
      j = i + 1;
    } 
    if ((paramConstraintWidget.mListAnchors[i]).mTarget != null && (paramConstraintWidget.mListAnchors[j]).mTarget == null) {
      byte b1 = -1;
      b = j;
      j = i;
      i = b;
      b = b1;
    } else {
      b = 1;
    } 
    if (k)
      paramInt2 -= m; 
    int i4 = paramConstraintWidget.mListAnchors[j].getMargin() * b + getParentBiasOffset(paramConstraintWidget, paramInt1);
    int i1 = paramInt2 + i4;
    if (paramInt1 == 0) {
      paramInt2 = paramConstraintWidget.getWidth();
    } else {
      paramInt2 = paramConstraintWidget.getHeight();
    } 
    int i5 = paramInt2 * b;
    Iterator<ResolutionNode> iterator = (paramConstraintWidget.mListAnchors[j].getResolutionNode()).dependents.iterator();
    for (paramInt2 = i2; iterator.hasNext(); paramInt2 = Math.max(paramInt2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, paramInt1, paramBoolean, i1)));
    iterator = (paramConstraintWidget.mListAnchors[i].getResolutionNode()).dependents.iterator();
    for (i2 = 0; iterator.hasNext(); i2 = Math.max(i2, getMaxDimensionTraversal(((ResolutionAnchor)iterator.next()).myAnchor.mOwner, paramInt1, paramBoolean, i5 + i1)));
    if (k) {
      i3 = paramInt2 - m;
      i2 += n;
    } else {
      if (paramInt1 == 0) {
        i3 = paramConstraintWidget.getWidth();
      } else {
        i3 = paramConstraintWidget.getHeight();
      } 
      i2 += i3 * b;
      i3 = paramInt2;
    } 
    if (paramInt1 == 1) {
      iterator = (paramConstraintWidget.mBaseline.getResolutionNode()).dependents.iterator();
      for (paramInt2 = 0; iterator.hasNext(); paramInt2 = Math.max(paramInt2, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, paramInt1, paramBoolean, n * b + i1))) {
        ResolutionAnchor resolutionAnchor = (ResolutionAnchor)iterator.next();
        if (b == 1) {
          paramInt2 = Math.max(paramInt2, getMaxDimensionTraversal(resolutionAnchor.myAnchor.mOwner, paramInt1, paramBoolean, m + i1));
          continue;
        } 
      } 
      if ((paramConstraintWidget.mBaseline.getResolutionNode()).dependents.size() > 0 && !k)
        if (b == 1) {
          paramInt2 += m;
        } else {
          paramInt2 -= n;
        }  
    } else {
      paramInt2 = 0;
    } 
    int n = Math.max(i3, Math.max(i2, paramInt2));
    int k = i5 + i1;
    int m = i1;
    paramInt2 = k;
    if (b == -1) {
      paramInt2 = i1;
      m = k;
    } 
    if (paramBoolean) {
      Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, m);
      paramConstraintWidget.setFrame(m, paramInt2, paramInt1);
    } else {
      paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1);
      paramConstraintWidget.setRelativePositioning(m, paramInt1);
    } 
    if (paramConstraintWidget.getDimensionBehaviour(paramInt1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && paramConstraintWidget.mDimensionRatio != 0.0F)
      paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1); 
    if ((paramConstraintWidget.mListAnchors[j]).mTarget != null && (paramConstraintWidget.mListAnchors[i]).mTarget != null) {
      ConstraintWidget constraintWidget = paramConstraintWidget.getParent();
      if ((paramConstraintWidget.mListAnchors[j]).mTarget.mOwner == constraintWidget && (paramConstraintWidget.mListAnchors[i]).mTarget.mOwner == constraintWidget)
        paramConstraintWidget.mBelongingGroup.addWidgetsToSet(paramConstraintWidget, paramInt1); 
    } 
    return i4 + n;
  }
  
  private static int getParentBiasOffset(ConstraintWidget paramConstraintWidget, int paramInt) {
    int i = paramInt * 2;
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.mListAnchors[i];
    ConstraintAnchor constraintAnchor2 = paramConstraintWidget.mListAnchors[i + 1];
    if (constraintAnchor1.mTarget != null && constraintAnchor1.mTarget.mOwner == paramConstraintWidget.mParent && constraintAnchor2.mTarget != null && constraintAnchor2.mTarget.mOwner == paramConstraintWidget.mParent) {
      float f;
      i = paramConstraintWidget.mParent.getLength(paramInt);
      if (paramInt == 0) {
        f = paramConstraintWidget.mHorizontalBiasPercent;
      } else {
        f = paramConstraintWidget.mVerticalBiasPercent;
      } 
      paramInt = paramConstraintWidget.getLength(paramInt);
      return (int)((i - constraintAnchor1.getMargin() - constraintAnchor2.getMargin() - paramInt) * f);
    } 
    return 0;
  }
  
  private static void invalidate(ConstraintWidgetContainer paramConstraintWidgetContainer, ConstraintWidget paramConstraintWidget, ConstraintWidgetGroup paramConstraintWidgetGroup) {
    paramConstraintWidgetGroup.mSkipSolver = false;
    paramConstraintWidgetContainer.mSkipSolver = false;
    paramConstraintWidget.mOptimizerMeasurable = false;
  }
  
  private static int resolveDimensionRatio(ConstraintWidget paramConstraintWidget) {
    byte b;
    if (paramConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      float f;
      if (paramConstraintWidget.mDimensionRatioSide == 0) {
        f = paramConstraintWidget.getHeight() * paramConstraintWidget.mDimensionRatio;
      } else {
        f = paramConstraintWidget.getHeight() / paramConstraintWidget.mDimensionRatio;
      } 
      b = (int)f;
      paramConstraintWidget.setWidth(b);
    } else if (paramConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      float f;
      if (paramConstraintWidget.mDimensionRatioSide == 1) {
        f = paramConstraintWidget.getWidth() * paramConstraintWidget.mDimensionRatio;
      } else {
        f = paramConstraintWidget.getWidth() / paramConstraintWidget.mDimensionRatio;
      } 
      b = (int)f;
      paramConstraintWidget.setHeight(b);
    } else {
      b = -1;
    } 
    return b;
  }
  
  private static void setConnection(ConstraintAnchor paramConstraintAnchor) {
    ResolutionAnchor resolutionAnchor = paramConstraintAnchor.getResolutionNode();
    if (paramConstraintAnchor.mTarget != null && paramConstraintAnchor.mTarget.mTarget != paramConstraintAnchor)
      paramConstraintAnchor.mTarget.getResolutionNode().addDependent(resolutionAnchor); 
  }
  
  public static void setPosition(List<ConstraintWidgetGroup> paramList, int paramInt1, int paramInt2) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      for (ConstraintWidget constraintWidget : ((ConstraintWidgetGroup)paramList.get(b)).getWidgetsToSet(paramInt1)) {
        if (constraintWidget.mOptimizerMeasurable)
          updateSizeDependentWidgets(constraintWidget, paramInt1, paramInt2); 
      } 
    } 
  }
  
  private static void singleGroup(ConstraintWidgetContainer paramConstraintWidgetContainer) {
    paramConstraintWidgetContainer.mWidgetGroups.clear();
    paramConstraintWidgetContainer.mWidgetGroups.add(0, new ConstraintWidgetGroup(paramConstraintWidgetContainer.mChildren));
  }
  
  private static boolean traverse(ConstraintWidget paramConstraintWidget, ConstraintWidgetGroup paramConstraintWidgetGroup, List<ConstraintWidgetGroup> paramList, boolean paramBoolean) {
    if (paramConstraintWidget == null)
      return true; 
    paramConstraintWidget.mOptimizerMeasured = false;
    ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)paramConstraintWidget.getParent();
    if (paramConstraintWidget.mBelongingGroup == null) {
      paramConstraintWidget.mOptimizerMeasurable = true;
      paramConstraintWidgetGroup.mConstrainedGroup.add(paramConstraintWidget);
      paramConstraintWidget.mBelongingGroup = paramConstraintWidgetGroup;
      if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null && paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null && paramConstraintWidget.mBaseline.mTarget == null && paramConstraintWidget.mCenter.mTarget == null) {
        invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
        if (paramBoolean)
          return false; 
      } 
      if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
        constraintWidgetContainer.getVerticalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (paramBoolean) {
          invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
          return false;
        } 
        if (paramConstraintWidget.mTop.mTarget.mOwner != paramConstraintWidget.getParent() || paramConstraintWidget.mBottom.mTarget.mOwner != paramConstraintWidget.getParent())
          invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup); 
      } 
      if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
        constraintWidgetContainer.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (paramBoolean) {
          invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
          return false;
        } 
        if (paramConstraintWidget.mLeft.mTarget.mOwner != paramConstraintWidget.getParent() || paramConstraintWidget.mRight.mTarget.mOwner != paramConstraintWidget.getParent())
          invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup); 
      } 
      if (paramConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        i = 1;
      } else {
        i = 0;
      } 
      if (paramConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        j = 1;
      } else {
        j = 0;
      } 
      if ((i ^ j) != 0 && paramConstraintWidget.mDimensionRatio != 0.0F) {
        resolveDimensionRatio(paramConstraintWidget);
      } else if (paramConstraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || paramConstraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
        invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
        if (paramBoolean)
          return false; 
      } 
      if (((paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) || (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mLeft.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mRight.mTarget == null) || (paramConstraintWidget.mRight.mTarget != null && paramConstraintWidget.mRight.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mLeft.mTarget == null) || (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mLeft.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mRight.mTarget != null && paramConstraintWidget.mRight.mTarget.mOwner == paramConstraintWidget.mParent)) && paramConstraintWidget.mCenter.mTarget == null && !(paramConstraintWidget instanceof Guideline) && !(paramConstraintWidget instanceof Helper))
        paramConstraintWidgetGroup.mStartHorizontalWidgets.add(paramConstraintWidget); 
      if (((paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) || (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mTop.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mBottom.mTarget == null) || (paramConstraintWidget.mBottom.mTarget != null && paramConstraintWidget.mBottom.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mTop.mTarget == null) || (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mTop.mTarget.mOwner == paramConstraintWidget.mParent && paramConstraintWidget.mBottom.mTarget != null && paramConstraintWidget.mBottom.mTarget.mOwner == paramConstraintWidget.mParent)) && paramConstraintWidget.mCenter.mTarget == null && paramConstraintWidget.mBaseline.mTarget == null && !(paramConstraintWidget instanceof Guideline) && !(paramConstraintWidget instanceof Helper))
        paramConstraintWidgetGroup.mStartVerticalWidgets.add(paramConstraintWidget); 
      if (paramConstraintWidget instanceof Helper) {
        invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
        if (paramBoolean)
          return false; 
        Helper helper = (Helper)paramConstraintWidget;
        for (i = 0; i < helper.mWidgetsCount; i++) {
          if (!traverse(helper.mWidgets[i], paramConstraintWidgetGroup, paramList, paramBoolean))
            return false; 
        } 
      } 
      int j = paramConstraintWidget.mListAnchors.length;
      for (int i = 0; i < j; i++) {
        ConstraintAnchor constraintAnchor = paramConstraintWidget.mListAnchors[i];
        if (constraintAnchor.mTarget != null && constraintAnchor.mTarget.mOwner != paramConstraintWidget.getParent()) {
          if (constraintAnchor.mType == ConstraintAnchor.Type.CENTER) {
            invalidate(constraintWidgetContainer, paramConstraintWidget, paramConstraintWidgetGroup);
            if (paramBoolean)
              return false; 
          } else {
            setConnection(constraintAnchor);
          } 
          if (!traverse(constraintAnchor.mTarget.mOwner, paramConstraintWidgetGroup, paramList, paramBoolean))
            return false; 
        } 
      } 
      return true;
    } 
    if (paramConstraintWidget.mBelongingGroup != paramConstraintWidgetGroup) {
      paramConstraintWidgetGroup.mConstrainedGroup.addAll(paramConstraintWidget.mBelongingGroup.mConstrainedGroup);
      paramConstraintWidgetGroup.mStartHorizontalWidgets.addAll(paramConstraintWidget.mBelongingGroup.mStartHorizontalWidgets);
      paramConstraintWidgetGroup.mStartVerticalWidgets.addAll(paramConstraintWidget.mBelongingGroup.mStartVerticalWidgets);
      if (!paramConstraintWidget.mBelongingGroup.mSkipSolver)
        paramConstraintWidgetGroup.mSkipSolver = false; 
      paramList.remove(paramConstraintWidget.mBelongingGroup);
      Iterator<ConstraintWidget> iterator = paramConstraintWidget.mBelongingGroup.mConstrainedGroup.iterator();
      while (iterator.hasNext())
        ((ConstraintWidget)iterator.next()).mBelongingGroup = paramConstraintWidgetGroup; 
    } 
    return true;
  }
  
  private static void updateSizeDependentWidgets(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int j = paramInt1 * 2;
    ConstraintAnchor constraintAnchor1 = paramConstraintWidget.mListAnchors[j];
    ConstraintAnchor constraintAnchor2 = paramConstraintWidget.mListAnchors[j + 1];
    if (constraintAnchor1.mTarget != null && constraintAnchor2.mTarget != null) {
      i = 1;
    } else {
      i = 0;
    } 
    if (i) {
      Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, getParentBiasOffset(paramConstraintWidget, paramInt1) + constraintAnchor1.getMargin());
      return;
    } 
    if (paramConstraintWidget.mDimensionRatio != 0.0F && paramConstraintWidget.getDimensionBehaviour(paramInt1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
      paramInt2 = resolveDimensionRatio(paramConstraintWidget);
      i = (int)(paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset;
      (constraintAnchor2.getResolutionNode()).resolvedTarget = constraintAnchor1.getResolutionNode();
      (constraintAnchor2.getResolutionNode()).resolvedOffset = paramInt2;
      (constraintAnchor2.getResolutionNode()).state = 1;
      paramConstraintWidget.setFrame(i, i + paramInt2, paramInt1);
      return;
    } 
    int i = paramInt2 - paramConstraintWidget.getRelativePositioning(paramInt1);
    paramInt2 = i - paramConstraintWidget.getLength(paramInt1);
    paramConstraintWidget.setFrame(paramInt2, i, paramInt1);
    Optimizer.setOptimizedWidget(paramConstraintWidget, paramInt1, paramInt2);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\Analyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */