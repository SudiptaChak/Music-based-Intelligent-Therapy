package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer extends WidgetContainer {
  private static final boolean DEBUG = false;
  
  static final boolean DEBUG_GRAPH = false;
  
  private static final boolean DEBUG_LAYOUT = false;
  
  private static final int MAX_ITERATIONS = 8;
  
  private static final boolean USE_SNAPSHOT = true;
  
  int mDebugSolverPassCount = 0;
  
  public boolean mGroupsWrapOptimized = false;
  
  private boolean mHeightMeasuredTooSmall = false;
  
  ChainHead[] mHorizontalChainsArray = new ChainHead[4];
  
  int mHorizontalChainsSize = 0;
  
  public boolean mHorizontalWrapOptimized = false;
  
  private boolean mIsRtl = false;
  
  private int mOptimizationLevel = 7;
  
  int mPaddingBottom;
  
  int mPaddingLeft;
  
  int mPaddingRight;
  
  int mPaddingTop;
  
  public boolean mSkipSolver = false;
  
  private Snapshot mSnapshot;
  
  protected LinearSystem mSystem = new LinearSystem();
  
  ChainHead[] mVerticalChainsArray = new ChainHead[4];
  
  int mVerticalChainsSize = 0;
  
  public boolean mVerticalWrapOptimized = false;
  
  public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
  
  private boolean mWidthMeasuredTooSmall = false;
  
  public int mWrapFixedHeight = 0;
  
  public int mWrapFixedWidth = 0;
  
  public ConstraintWidgetContainer() {}
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private void addHorizontalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mHorizontalChainsSize;
    ChainHead[] arrayOfChainHead = this.mHorizontalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mHorizontalChainsArray = Arrays.<ChainHead>copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(paramConstraintWidget, 0, isRtl());
    this.mHorizontalChainsSize++;
  }
  
  private void addVerticalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mVerticalChainsSize;
    ChainHead[] arrayOfChainHead = this.mVerticalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mVerticalChainsArray = Arrays.<ChainHead>copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(paramConstraintWidget, 1, isRtl());
    this.mVerticalChainsSize++;
  }
  
  private void resetChains() {
    this.mHorizontalChainsSize = 0;
    this.mVerticalChainsSize = 0;
  }
  
  void addChain(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      addHorizontalChain(paramConstraintWidget);
    } else if (paramInt == 1) {
      addVerticalChain(paramConstraintWidget);
    } 
  }
  
  public boolean addChildrenToSolver(LinearSystem paramLinearSystem) {
    addToSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof ConstraintWidgetContainer) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[0];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = constraintWidget.mListDimensionBehaviors[1];
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        constraintWidget.addToSolver(paramLinearSystem);
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour2); 
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour1); 
      } else {
        Optimizer.checkMatchParent(this, paramLinearSystem, constraintWidget);
        constraintWidget.addToSolver(paramLinearSystem);
      } 
    } 
    if (this.mHorizontalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 0); 
    if (this.mVerticalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 1); 
    return true;
  }
  
  public void analyze(int paramInt) {
    super.analyze(paramInt);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).analyze(paramInt); 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mSystem.fillMetrics(paramMetrics);
  }
  
  public ArrayList<Guideline> getHorizontalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 0)
          arrayList.add(constraintWidget); 
      } 
    } 
    return (ArrayList)arrayList;
  }
  
  public int getOptimizationLevel() {
    return this.mOptimizationLevel;
  }
  
  public LinearSystem getSystem() {
    return this.mSystem;
  }
  
  public String getType() {
    return "ConstraintLayout";
  }
  
  public ArrayList<Guideline> getVerticalGuidelines() {
    ArrayList<ConstraintWidget> arrayList = new ArrayList();
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = constraintWidget;
        if (constraintWidget.getOrientation() == 1)
          arrayList.add(constraintWidget); 
      } 
    } 
    return (ArrayList)arrayList;
  }
  
  public List<ConstraintWidgetGroup> getWidgetGroups() {
    return this.mWidgetGroups;
  }
  
  public boolean handlesInternalConstraints() {
    return false;
  }
  
  public boolean isHeightMeasuredTooSmall() {
    return this.mHeightMeasuredTooSmall;
  }
  
  public boolean isRtl() {
    return this.mIsRtl;
  }
  
  public boolean isWidthMeasuredTooSmall() {
    return this.mWidthMeasuredTooSmall;
  }
  
  public void layout() {
    boolean bool;
    int i1 = this.mX;
    int m = this.mY;
    int n = Math.max(0, getWidth());
    int k = Math.max(0, getHeight());
    this.mWidthMeasuredTooSmall = false;
    this.mHeightMeasuredTooSmall = false;
    if (this.mParent != null) {
      if (this.mSnapshot == null)
        this.mSnapshot = new Snapshot(this); 
      this.mSnapshot.updateFrom(this);
      setX(this.mPaddingLeft);
      setY(this.mPaddingTop);
      resetAnchors();
      resetSolverVariables(this.mSystem.getCache());
    } else {
      this.mX = 0;
      this.mY = 0;
    } 
    if (this.mOptimizationLevel != 0) {
      if (!optimizeFor(8))
        optimizeReset(); 
      if (!optimizeFor(32))
        optimize(); 
      this.mSystem.graphOptimizer = true;
    } else {
      this.mSystem.graphOptimizer = false;
    } 
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = this.mListDimensionBehaviors[1];
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
    resetChains();
    if (this.mWidgetGroups.size() == 0) {
      this.mWidgetGroups.clear();
      this.mWidgetGroups.add(0, new ConstraintWidgetGroup(this.mChildren));
    } 
    int i = this.mWidgetGroups.size();
    ArrayList<ConstraintWidget> arrayList = this.mChildren;
    if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
      bool = true;
    } else {
      bool = false;
    } 
    int j = 0;
    for (byte b = 0; b < i && !this.mSkipSolver; b++) {
      if (!((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).mSkipSolver) {
        if (optimizeFor(32))
          if (getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED && getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
            this.mChildren = (ArrayList<ConstraintWidget>)((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).getWidgetsToSolve();
          } else {
            this.mChildren = (ArrayList<ConstraintWidget>)((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).mConstrainedGroup;
          }  
        resetChains();
        int i4 = this.mChildren.size();
        int i2;
        for (i2 = 0; i2 < i4; i2++) {
          ConstraintWidget constraintWidget = this.mChildren.get(i2);
          if (constraintWidget instanceof WidgetContainer)
            ((WidgetContainer)constraintWidget).layout(); 
        } 
        i2 = j;
        int i3 = 0;
        boolean bool1 = true;
        j = i;
        i = i2;
        i2 = i3;
        label151: while (bool1) {
          i2++;
          boolean bool2 = bool1;
          try {
            this.mSystem.reset();
            bool2 = bool1;
            resetChains();
            bool2 = bool1;
            createObjectVariables(this.mSystem);
            i3 = 0;
            while (true) {
              if (i3 < i4) {
                bool2 = bool1;
                ConstraintWidget constraintWidget = this.mChildren.get(i3);
                try {
                  constraintWidget.createObjectVariables(this.mSystem);
                  i3++;
                } catch (Exception exception) {
                  continue label151;
                } 
                continue;
              } 
              bool2 = addChildrenToSolver(this.mSystem);
              if (bool2)
                try {
                  this.mSystem.minimize();
                } catch (Exception exception) {
                  bool1 = bool2;
                  continue label151;
                }  
              break;
            } 
          } catch (Exception exception) {
            bool1 = bool2;
            exception.printStackTrace();
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EXCEPTION : ");
            stringBuilder.append(exception);
            printStream.println(stringBuilder.toString());
            bool2 = bool1;
          } 
          if (bool2) {
            updateChildrenFromSolver(this.mSystem, Optimizer.flags);
          } else {
            updateFromSolver(this.mSystem);
            for (i3 = 0; i3 < i4; i3++) {
              ConstraintWidget constraintWidget = this.mChildren.get(i3);
              if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                Optimizer.flags[2] = true;
                break;
              } 
              if (constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                Optimizer.flags[2] = true;
                break;
              } 
            } 
          } 
          if (bool && i2 < 8 && Optimizer.flags[2]) {
            int i6 = 0;
            int i7 = 0;
            i3 = 0;
            while (i6 < i4) {
              ConstraintWidget constraintWidget = this.mChildren.get(i6);
              i7 = Math.max(i7, constraintWidget.mX + constraintWidget.getWidth());
              i3 = Math.max(i3, constraintWidget.mY + constraintWidget.getHeight());
              i6++;
            } 
            i6 = i2;
            i2 = Math.max(this.mMinWidth, i7);
            i7 = Math.max(this.mMinHeight, i3);
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getWidth() < i2) {
              setWidth(i2);
              this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
              bool2 = true;
              i3 = 1;
            } else {
              bool2 = false;
              i3 = i;
            } 
            bool1 = bool2;
            i = i3;
            i2 = i6;
            if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool1 = bool2;
              i = i3;
              i2 = i6;
              if (getHeight() < i7) {
                setHeight(i7);
                this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                bool1 = true;
                i = 1;
                i2 = i6;
              } 
            } 
          } else {
            bool1 = false;
          } 
          i3 = Math.max(this.mMinWidth, getWidth());
          if (i3 > getWidth()) {
            setWidth(i3);
            this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
            bool1 = true;
            i = 1;
          } 
          i3 = Math.max(this.mMinHeight, getHeight());
          if (i3 > getHeight()) {
            setHeight(i3);
            this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
            bool1 = true;
            i = 1;
          } 
          bool2 = bool1;
          int i5 = i;
          if (i == 0) {
            boolean bool3 = bool1;
            i3 = i;
            if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool3 = bool1;
              i3 = i;
              if (n > 0) {
                bool3 = bool1;
                i3 = i;
                if (getWidth() > n) {
                  this.mWidthMeasuredTooSmall = true;
                  this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                  setWidth(n);
                  bool3 = true;
                  i3 = 1;
                } 
              } 
            } 
            bool2 = bool3;
            i5 = i3;
            if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
              bool2 = bool3;
              i5 = i3;
              if (k > 0) {
                bool2 = bool3;
                i5 = i3;
                if (getHeight() > k) {
                  this.mHeightMeasuredTooSmall = true;
                  this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                  setHeight(k);
                  i = 1;
                  bool1 = true;
                  continue;
                } 
              } 
            } 
          } 
          bool1 = bool2;
          i = i5;
        } 
        ((ConstraintWidgetGroup)this.mWidgetGroups.get(b)).updateUnresolvedWidgets();
        i2 = i;
        i = j;
        j = i2;
      } 
    } 
    this.mChildren = arrayList;
    if (this.mParent != null) {
      int i2 = Math.max(this.mMinWidth, getWidth());
      i = Math.max(this.mMinHeight, getHeight());
      this.mSnapshot.applyTo(this);
      setWidth(i2 + this.mPaddingLeft + this.mPaddingRight);
      setHeight(i + this.mPaddingTop + this.mPaddingBottom);
    } else {
      this.mX = i1;
      this.mY = m;
    } 
    if (j != 0) {
      this.mListDimensionBehaviors[0] = dimensionBehaviour2;
      this.mListDimensionBehaviors[1] = dimensionBehaviour1;
    } 
    resetSolverVariables(this.mSystem.getCache());
    if (this == getRootConstraintContainer())
      updateDrawPosition(); 
  }
  
  public void optimize() {
    if (!optimizeFor(8))
      analyze(this.mOptimizationLevel); 
    solveGraph();
  }
  
  public boolean optimizeFor(int paramInt) {
    boolean bool;
    if ((this.mOptimizationLevel & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void optimizeForDimensions(int paramInt1, int paramInt2) {
    if (this.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null)
      this.mResolutionWidth.resolve(paramInt1); 
    if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null)
      this.mResolutionHeight.resolve(paramInt2); 
  }
  
  public void optimizeReset() {
    int i = this.mChildren.size();
    resetResolutionNodes();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetResolutionNodes(); 
  }
  
  public void preOptimize() {
    optimizeReset();
    analyze(this.mOptimizationLevel);
  }
  
  public void reset() {
    this.mSystem.reset();
    this.mPaddingLeft = 0;
    this.mPaddingRight = 0;
    this.mPaddingTop = 0;
    this.mPaddingBottom = 0;
    this.mWidgetGroups.clear();
    this.mSkipSolver = false;
    super.reset();
  }
  
  public void resetGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.invalidateAnchors();
    resolutionAnchor2.invalidateAnchors();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mOptimizationLevel = paramInt;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mPaddingLeft = paramInt1;
    this.mPaddingTop = paramInt2;
    this.mPaddingRight = paramInt3;
    this.mPaddingBottom = paramInt4;
  }
  
  public void setRtl(boolean paramBoolean) {
    this.mIsRtl = paramBoolean;
  }
  
  public void solveGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void updateChildrenFromSolver(LinearSystem paramLinearSystem, boolean[] paramArrayOfboolean) {
    paramArrayOfboolean[2] = false;
    updateFromSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = this.mChildren.get(b);
      constraintWidget.updateFromSolver(paramLinearSystem);
      if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth())
        paramArrayOfboolean[2] = true; 
      if (constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight())
        paramArrayOfboolean[2] = true; 
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\ConstraintWidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */