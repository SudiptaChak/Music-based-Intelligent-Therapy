package androidx.constraintlayout.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
  private boolean mDefined;
  
  protected ConstraintWidget mFirst;
  
  protected ConstraintWidget mFirstMatchConstraintWidget;
  
  protected ConstraintWidget mFirstVisibleWidget;
  
  protected boolean mHasComplexMatchWeights;
  
  protected boolean mHasDefinedWeights;
  
  protected boolean mHasUndefinedWeights;
  
  protected ConstraintWidget mHead;
  
  private boolean mIsRtl = false;
  
  protected ConstraintWidget mLast;
  
  protected ConstraintWidget mLastMatchConstraintWidget;
  
  protected ConstraintWidget mLastVisibleWidget;
  
  private int mOrientation;
  
  protected float mTotalWeight = 0.0F;
  
  protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
  
  protected int mWidgetsCount;
  
  protected int mWidgetsMatchCount;
  
  public ChainHead(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mFirst = paramConstraintWidget;
    this.mOrientation = paramInt;
    this.mIsRtl = paramBoolean;
  }
  
  private void defineChainProperties() {
    int i = this.mOrientation * 2;
    ConstraintWidget constraintWidget1 = this.mFirst;
    boolean bool3 = false;
    ConstraintWidget constraintWidget2 = constraintWidget1;
    boolean bool1 = false;
    while (!bool1) {
      this.mWidgetsCount++;
      ConstraintWidget[] arrayOfConstraintWidget = constraintWidget1.mNextChainWidget;
      int j = this.mOrientation;
      ConstraintWidget constraintWidget = null;
      arrayOfConstraintWidget[j] = null;
      constraintWidget1.mListNextMatchConstraintsWidget[this.mOrientation] = null;
      if (constraintWidget1.getVisibility() != 8) {
        if (this.mFirstVisibleWidget == null)
          this.mFirstVisibleWidget = constraintWidget1; 
        this.mLastVisibleWidget = constraintWidget1;
        if (constraintWidget1.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget1.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || constraintWidget1.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || constraintWidget1.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
          this.mWidgetsMatchCount++;
          float f = constraintWidget1.mWeight[this.mOrientation];
          if (f > 0.0F)
            this.mTotalWeight += constraintWidget1.mWeight[this.mOrientation]; 
          if (isMatchConstraintEqualityCandidate(constraintWidget1, this.mOrientation)) {
            if (f < 0.0F) {
              this.mHasUndefinedWeights = true;
            } else {
              this.mHasDefinedWeights = true;
            } 
            if (this.mWeightedMatchConstraintsWidgets == null)
              this.mWeightedMatchConstraintsWidgets = new ArrayList<ConstraintWidget>(); 
            this.mWeightedMatchConstraintsWidgets.add(constraintWidget1);
          } 
          if (this.mFirstMatchConstraintWidget == null)
            this.mFirstMatchConstraintWidget = constraintWidget1; 
          ConstraintWidget constraintWidget3 = this.mLastMatchConstraintWidget;
          if (constraintWidget3 != null)
            constraintWidget3.mListNextMatchConstraintsWidget[this.mOrientation] = constraintWidget1; 
          this.mLastMatchConstraintWidget = constraintWidget1;
        } 
      } 
      if (constraintWidget2 != constraintWidget1)
        constraintWidget2.mNextChainWidget[this.mOrientation] = constraintWidget1; 
      ConstraintAnchor constraintAnchor = (constraintWidget1.mListAnchors[i + 1]).mTarget;
      constraintWidget2 = constraintWidget;
      if (constraintAnchor != null) {
        ConstraintWidget constraintWidget3 = constraintAnchor.mOwner;
        constraintWidget2 = constraintWidget;
        if ((constraintWidget3.mListAnchors[i]).mTarget != null)
          if ((constraintWidget3.mListAnchors[i]).mTarget.mOwner != constraintWidget1) {
            constraintWidget2 = constraintWidget;
          } else {
            constraintWidget2 = constraintWidget3;
          }  
      } 
      if (constraintWidget2 == null) {
        constraintWidget2 = constraintWidget1;
        bool1 = true;
      } 
      constraintWidget = constraintWidget1;
      constraintWidget1 = constraintWidget2;
      constraintWidget2 = constraintWidget;
    } 
    this.mLast = constraintWidget1;
    if (this.mOrientation == 0 && this.mIsRtl) {
      this.mHead = constraintWidget1;
    } else {
      this.mHead = this.mFirst;
    } 
    boolean bool2 = bool3;
    if (this.mHasDefinedWeights) {
      bool2 = bool3;
      if (this.mHasUndefinedWeights)
        bool2 = true; 
    } 
    this.mHasComplexMatchWeights = bool2;
  }
  
  private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget paramConstraintWidget, int paramInt) {
    boolean bool;
    if (paramConstraintWidget.getVisibility() != 8 && paramConstraintWidget.mListDimensionBehaviors[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 0 || paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 3)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void define() {
    if (!this.mDefined)
      defineChainProperties(); 
    this.mDefined = true;
  }
  
  public ConstraintWidget getFirst() {
    return this.mFirst;
  }
  
  public ConstraintWidget getFirstMatchConstraintWidget() {
    return this.mFirstMatchConstraintWidget;
  }
  
  public ConstraintWidget getFirstVisibleWidget() {
    return this.mFirstVisibleWidget;
  }
  
  public ConstraintWidget getHead() {
    return this.mHead;
  }
  
  public ConstraintWidget getLast() {
    return this.mLast;
  }
  
  public ConstraintWidget getLastMatchConstraintWidget() {
    return this.mLastMatchConstraintWidget;
  }
  
  public ConstraintWidget getLastVisibleWidget() {
    return this.mLastVisibleWidget;
  }
  
  public float getTotalWeight() {
    return this.mTotalWeight;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ChainHead.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */