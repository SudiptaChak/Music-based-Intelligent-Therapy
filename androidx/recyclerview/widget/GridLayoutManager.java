package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, boolean paramBoolean) {
    byte b;
    int i = 0;
    paramInt2 = -1;
    if (paramBoolean) {
      b = 1;
      boolean bool = false;
      paramInt2 = paramInt1;
      paramInt1 = bool;
    } else {
      paramInt1--;
      b = -1;
    } 
    while (paramInt1 != paramInt2) {
      View view = this.mSet[paramInt1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = i;
      i += layoutParams.mSpanSize;
      paramInt1 += b;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: aload_0
    //   4: ifnull -> 28
    //   7: aload_0
    //   8: arraylength
    //   9: iload_1
    //   10: iconst_1
    //   11: iadd
    //   12: if_icmpne -> 28
    //   15: aload_0
    //   16: astore #8
    //   18: aload_0
    //   19: aload_0
    //   20: arraylength
    //   21: iconst_1
    //   22: isub
    //   23: iaload
    //   24: iload_2
    //   25: if_icmpeq -> 35
    //   28: iload_1
    //   29: iconst_1
    //   30: iadd
    //   31: newarray int
    //   33: astore #8
    //   35: iconst_0
    //   36: istore #5
    //   38: aload #8
    //   40: iconst_0
    //   41: iconst_0
    //   42: iastore
    //   43: iload_2
    //   44: iload_1
    //   45: idiv
    //   46: istore #6
    //   48: iload_2
    //   49: iload_1
    //   50: irem
    //   51: istore #7
    //   53: iconst_0
    //   54: istore_3
    //   55: iload #5
    //   57: istore_2
    //   58: iload #4
    //   60: iload_1
    //   61: if_icmpgt -> 115
    //   64: iload_2
    //   65: iload #7
    //   67: iadd
    //   68: istore_2
    //   69: iload_2
    //   70: ifle -> 94
    //   73: iload_1
    //   74: iload_2
    //   75: isub
    //   76: iload #7
    //   78: if_icmpge -> 94
    //   81: iload #6
    //   83: iconst_1
    //   84: iadd
    //   85: istore #5
    //   87: iload_2
    //   88: iload_1
    //   89: isub
    //   90: istore_2
    //   91: goto -> 98
    //   94: iload #6
    //   96: istore #5
    //   98: iload_3
    //   99: iload #5
    //   101: iadd
    //   102: istore_3
    //   103: aload #8
    //   105: iload #4
    //   107: iload_3
    //   108: iastore
    //   109: iinc #4, 1
    //   112: goto -> 58
    //   115: aload #8
    //   117: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    if (paramInt == 1) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    int i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (paramInt != 0) {
      while (i > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int j = paramState.getItemCount();
      paramInt = paramAnchorInfo.mPosition;
      while (paramInt < j - 1) {
        int m = paramInt + 1;
        int k = getSpanIndex(paramRecycler, paramState, m);
        if (k > i) {
          paramInt = m;
          i = k;
        } 
      } 
      paramAnchorInfo.mPosition = paramInt;
    } 
  }
  
  private void ensureViewSet() {
    View[] arrayOfView = this.mSet;
    if (arrayOfView == null || arrayOfView.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. ");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int i = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int i = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 1;
    } 
    return this.mSpanSizeLookup.getSpanSize(i);
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(paramFloat * this.mSpanCount), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int j = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int i = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      i = getChildMeasureSpec(k, paramInt, i, layoutParams.width, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), j, layoutParams.height, true);
    } else {
      paramInt = getChildMeasureSpec(k, paramInt, j, layoutParams.height, false);
      i = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), i, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, i, paramInt, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    int j;
    if (getOrientation() == 1) {
      j = getWidth() - getPaddingRight();
      i = getPaddingLeft();
    } else {
      j = getHeight() - getPaddingBottom();
      i = getPaddingTop();
    } 
    calculateItemBorders(j - i);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, Math.max(0, paramLayoutState.mScrollingOffset));
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    ensureLayoutState();
    int j = this.mOrientationHelper.getStartAfterPadding();
    int i = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    View view2 = null;
    View view1;
    for (view1 = null; paramInt1 != paramInt2; view1 = view3) {
      View view5 = getChildAt(paramInt1);
      int k = getPosition(view5);
      View view4 = view2;
      View view3 = view1;
      if (k >= 0) {
        view4 = view2;
        view3 = view1;
        if (k < paramInt3)
          if (getSpanIndex(paramRecycler, paramState, k) != 0) {
            view4 = view2;
            view3 = view1;
          } else if (((RecyclerView.LayoutParams)view5.getLayoutParams()).isItemRemoved()) {
            view4 = view2;
            view3 = view1;
            if (view1 == null) {
              view3 = view5;
              view4 = view2;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view5) >= i || this.mOrientationHelper.getDecoratedEnd(view5) < j) {
            view4 = view2;
            view3 = view1;
            if (view2 == null) {
              view4 = view5;
              view3 = view1;
            } 
          } else {
            return view5;
          }  
      } 
      paramInt1 += b;
      view2 = view4;
    } 
    if (view2 != null)
      view1 = view2; 
    return view1;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1 && isLayoutRTL()) {
      int[] arrayOfInt1 = this.mCachedBorders;
      int i = this.mSpanCount;
      return arrayOfInt1[i - paramInt1] - arrayOfInt1[i - paramInt1 - paramInt2];
    } 
    int[] arrayOfInt = this.mCachedBorders;
    return arrayOfInt[paramInt2 + paramInt1] - arrayOfInt[paramInt1];
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    StringBuilder stringBuilder;
    int j;
    int m;
    boolean bool;
    int i2 = this.mOrientationHelper.getModeInOther();
    if (i2 != 1073741824) {
      j = 1;
    } else {
      j = 0;
    } 
    if (getChildCount() > 0) {
      m = this.mCachedBorders[this.mSpanCount];
    } else {
      m = 0;
    } 
    if (j)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int i = this.mSpanCount;
    if (!bool)
      i = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition); 
    int k = 0;
    byte b = 0;
    while (b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0) {
      int i4 = paramLayoutState.mCurrentPosition;
      int i3 = getSpanSize(paramRecycler, paramState, i4);
      if (i3 <= this.mSpanCount) {
        i -= i3;
        if (i < 0)
          break; 
        View view = paramLayoutState.next(paramRecycler);
        if (view == null)
          break; 
        k += i3;
        this.mSet[b] = view;
        b++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Item at position ");
      stringBuilder.append(i4);
      stringBuilder.append(" requires ");
      stringBuilder.append(i3);
      stringBuilder.append(" spans but GridLayoutManager has only ");
      stringBuilder.append(this.mSpanCount);
      stringBuilder.append(" spans.");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    if (b == 0) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    float f = 0.0F;
    assignSpans((RecyclerView.Recycler)stringBuilder, paramState, b, k, bool);
    int n = 0;
    i = 0;
    while (n < b) {
      View view = this.mSet[n];
      if (paramLayoutState.mScrapList == null) {
        if (bool) {
          addView(view);
        } else {
          addView(view, 0);
        } 
      } else if (bool) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
      calculateItemDecorationsForChild(view, this.mDecorInsets);
      measureChild(view, i2, false);
      int i3 = this.mOrientationHelper.getDecoratedMeasurement(view);
      k = i;
      if (i3 > i)
        k = i3; 
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      float f2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0F / layoutParams.mSpanSize;
      float f1 = f;
      if (f2 > f)
        f1 = f2; 
      n++;
      i = k;
      f = f1;
    } 
    k = i;
    if (j) {
      guessMeasurement(f, m);
      i = 0;
      j = 0;
      while (true) {
        k = i;
        if (j < b) {
          View view = this.mSet[j];
          measureChild(view, 1073741824, true);
          m = this.mOrientationHelper.getDecoratedMeasurement(view);
          k = i;
          if (m > i)
            k = m; 
          j++;
          i = k;
          continue;
        } 
        break;
      } 
    } 
    for (i = 0; i < b; i++) {
      View view = this.mSet[i];
      if (this.mOrientationHelper.getDecoratedMeasurement(view) != k) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        j = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        m = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        n = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
          m = getChildMeasureSpec(n, 1073741824, m, layoutParams.width, false);
          j = View.MeasureSpec.makeMeasureSpec(k - j, 1073741824);
        } else {
          m = View.MeasureSpec.makeMeasureSpec(k - m, 1073741824);
          j = getChildMeasureSpec(n, 1073741824, j, layoutParams.height, false);
        } 
        measureChildWithDecorationsAndMargin(view, m, j, true);
      } 
    } 
    int i1 = 0;
    paramLayoutChunkResult.mConsumed = k;
    if (this.mOrientation == 1) {
      if (paramLayoutState.mLayoutDirection == -1) {
        i = paramLayoutState.mOffset;
        n = 0;
        m = 0;
        j = i - k;
        k = n;
      } else {
        j = paramLayoutState.mOffset;
        i = j + k;
        k = 0;
        m = 0;
      } 
    } else {
      if (paramLayoutState.mLayoutDirection == -1) {
        m = paramLayoutState.mOffset;
        k = m - k;
      } else {
        i = paramLayoutState.mOffset;
        m = i + k;
        k = i;
      } 
      i = 0;
      j = 0;
    } 
    while (i1 < b) {
      View view = this.mSet[i1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (this.mOrientation == 1) {
        if (isLayoutRTL()) {
          k = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
          n = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          m = k;
          k -= n;
        } else {
          m = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
          n = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          k = m;
          m = n + m;
        } 
        n = i;
        i = j;
        j = m;
      } else {
        n = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
        i2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
        i = n;
        j = m;
        n = i2 + n;
      } 
      layoutDecoratedWithMargins(view, k, i, j, n);
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        paramLayoutChunkResult.mIgnoreConsumed = true; 
      paramLayoutChunkResult.mFocusable |= view.hasFocusable();
      i2 = i1 + 1;
      i1 = i;
      i = n;
      m = j;
      j = i1;
      i1 = i2;
    } 
    Arrays.fill((Object[])this.mSet, (Object)null);
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #22
    //   7: aconst_null
    //   8: astore #21
    //   10: aload #22
    //   12: ifnonnull -> 17
    //   15: aconst_null
    //   16: areturn
    //   17: aload #22
    //   19: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   22: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   25: astore #23
    //   27: aload #23
    //   29: getfield mSpanIndex : I
    //   32: istore #15
    //   34: aload #23
    //   36: getfield mSpanIndex : I
    //   39: aload #23
    //   41: getfield mSpanSize : I
    //   44: iadd
    //   45: istore #14
    //   47: aload_0
    //   48: aload_1
    //   49: iload_2
    //   50: aload_3
    //   51: aload #4
    //   53: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;)Landroid/view/View;
    //   56: ifnonnull -> 61
    //   59: aconst_null
    //   60: areturn
    //   61: aload_0
    //   62: iload_2
    //   63: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   66: iconst_1
    //   67: if_icmpne -> 76
    //   70: iconst_1
    //   71: istore #20
    //   73: goto -> 79
    //   76: iconst_0
    //   77: istore #20
    //   79: iload #20
    //   81: aload_0
    //   82: getfield mShouldReverseLayout : Z
    //   85: if_icmpeq -> 93
    //   88: iconst_1
    //   89: istore_2
    //   90: goto -> 95
    //   93: iconst_0
    //   94: istore_2
    //   95: iload_2
    //   96: ifeq -> 115
    //   99: aload_0
    //   100: invokevirtual getChildCount : ()I
    //   103: iconst_1
    //   104: isub
    //   105: istore_2
    //   106: iconst_m1
    //   107: istore #6
    //   109: iconst_m1
    //   110: istore #8
    //   112: goto -> 126
    //   115: aload_0
    //   116: invokevirtual getChildCount : ()I
    //   119: istore #6
    //   121: iconst_1
    //   122: istore #8
    //   124: iconst_0
    //   125: istore_2
    //   126: aload_0
    //   127: getfield mOrientation : I
    //   130: iconst_1
    //   131: if_icmpne -> 147
    //   134: aload_0
    //   135: invokevirtual isLayoutRTL : ()Z
    //   138: ifeq -> 147
    //   141: iconst_1
    //   142: istore #9
    //   144: goto -> 150
    //   147: iconst_0
    //   148: istore #9
    //   150: aload_0
    //   151: aload_3
    //   152: aload #4
    //   154: iload_2
    //   155: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   158: istore #16
    //   160: iconst_m1
    //   161: istore #12
    //   163: iconst_m1
    //   164: istore #10
    //   166: iconst_0
    //   167: istore #7
    //   169: iconst_0
    //   170: istore #5
    //   172: iload_2
    //   173: istore #11
    //   175: aconst_null
    //   176: astore_1
    //   177: iload #10
    //   179: istore_2
    //   180: iload #6
    //   182: istore #10
    //   184: iload #7
    //   186: istore #6
    //   188: iload #11
    //   190: iload #10
    //   192: if_icmpeq -> 562
    //   195: aload_0
    //   196: aload_3
    //   197: aload #4
    //   199: iload #11
    //   201: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   204: istore #7
    //   206: aload_0
    //   207: iload #11
    //   209: invokevirtual getChildAt : (I)Landroid/view/View;
    //   212: astore #23
    //   214: aload #23
    //   216: aload #22
    //   218: if_acmpne -> 224
    //   221: goto -> 562
    //   224: aload #23
    //   226: invokevirtual hasFocusable : ()Z
    //   229: ifeq -> 250
    //   232: iload #7
    //   234: iload #16
    //   236: if_icmpeq -> 250
    //   239: aload #21
    //   241: ifnull -> 247
    //   244: goto -> 562
    //   247: goto -> 552
    //   250: aload #23
    //   252: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   255: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   258: astore #24
    //   260: aload #24
    //   262: getfield mSpanIndex : I
    //   265: istore #17
    //   267: aload #24
    //   269: getfield mSpanIndex : I
    //   272: aload #24
    //   274: getfield mSpanSize : I
    //   277: iadd
    //   278: istore #18
    //   280: aload #23
    //   282: invokevirtual hasFocusable : ()Z
    //   285: ifeq -> 305
    //   288: iload #17
    //   290: iload #15
    //   292: if_icmpne -> 305
    //   295: iload #18
    //   297: iload #14
    //   299: if_icmpne -> 305
    //   302: aload #23
    //   304: areturn
    //   305: aload #23
    //   307: invokevirtual hasFocusable : ()Z
    //   310: ifeq -> 318
    //   313: aload #21
    //   315: ifnull -> 330
    //   318: aload #23
    //   320: invokevirtual hasFocusable : ()Z
    //   323: ifne -> 336
    //   326: aload_1
    //   327: ifnonnull -> 336
    //   330: iconst_1
    //   331: istore #7
    //   333: goto -> 479
    //   336: iload #17
    //   338: iload #15
    //   340: invokestatic max : (II)I
    //   343: istore #7
    //   345: iload #18
    //   347: iload #14
    //   349: invokestatic min : (II)I
    //   352: iload #7
    //   354: isub
    //   355: istore #19
    //   357: aload #23
    //   359: invokevirtual hasFocusable : ()Z
    //   362: ifeq -> 408
    //   365: iload #19
    //   367: iload #6
    //   369: if_icmple -> 375
    //   372: goto -> 330
    //   375: iload #19
    //   377: iload #6
    //   379: if_icmpne -> 476
    //   382: iload #17
    //   384: iload #12
    //   386: if_icmple -> 395
    //   389: iconst_1
    //   390: istore #7
    //   392: goto -> 398
    //   395: iconst_0
    //   396: istore #7
    //   398: iload #9
    //   400: iload #7
    //   402: if_icmpne -> 476
    //   405: goto -> 330
    //   408: aload #21
    //   410: ifnonnull -> 476
    //   413: iconst_1
    //   414: istore #13
    //   416: iconst_1
    //   417: istore #7
    //   419: aload_0
    //   420: aload #23
    //   422: iconst_0
    //   423: iconst_1
    //   424: invokevirtual isViewPartiallyVisible : (Landroid/view/View;ZZ)Z
    //   427: ifeq -> 476
    //   430: iload #19
    //   432: iload #5
    //   434: if_icmple -> 444
    //   437: iload #13
    //   439: istore #7
    //   441: goto -> 479
    //   444: iload #19
    //   446: iload #5
    //   448: if_icmpne -> 473
    //   451: iload #17
    //   453: iload_2
    //   454: if_icmple -> 460
    //   457: goto -> 463
    //   460: iconst_0
    //   461: istore #7
    //   463: iload #9
    //   465: iload #7
    //   467: if_icmpne -> 476
    //   470: goto -> 330
    //   473: goto -> 476
    //   476: iconst_0
    //   477: istore #7
    //   479: iload #7
    //   481: ifeq -> 552
    //   484: aload #23
    //   486: invokevirtual hasFocusable : ()Z
    //   489: ifeq -> 523
    //   492: aload #24
    //   494: getfield mSpanIndex : I
    //   497: istore #12
    //   499: iload #18
    //   501: iload #14
    //   503: invokestatic min : (II)I
    //   506: iload #17
    //   508: iload #15
    //   510: invokestatic max : (II)I
    //   513: isub
    //   514: istore #6
    //   516: aload #23
    //   518: astore #21
    //   520: goto -> 552
    //   523: aload #24
    //   525: getfield mSpanIndex : I
    //   528: istore_2
    //   529: iload #18
    //   531: iload #14
    //   533: invokestatic min : (II)I
    //   536: iload #17
    //   538: iload #15
    //   540: invokestatic max : (II)I
    //   543: isub
    //   544: istore #5
    //   546: aload #23
    //   548: astore_1
    //   549: goto -> 552
    //   552: iload #11
    //   554: iload #8
    //   556: iadd
    //   557: istore #11
    //   559: goto -> 188
    //   562: aload #21
    //   564: ifnull -> 573
    //   567: aload #21
    //   569: astore_1
    //   570: goto -> 573
    //   573: aload_1
    //   574: areturn
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      boolean bool;
      int j = layoutParams.getSpanIndex();
      int k = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(j, k, i, 1, bool, false));
    } else {
      boolean bool;
      int j = layoutParams.getSpanIndex();
      int k = layoutParams.getSpanSize();
      if (this.mSpanCount > 1 && layoutParams.getSpanSize() == this.mSpanCount) {
        bool = true;
      } else {
        bool = false;
      } 
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, j, k, bool, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int[] arrayOfInt;
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      arrayOfInt = this.mCachedBorders;
      paramInt1 = chooseSize(paramInt1, arrayOfInt[arrayOfInt.length - 1] + i, getMinimumWidth());
    } else {
      paramInt1 = chooseSize(paramInt1, arrayOfInt.width() + i, getMinimumWidth());
      arrayOfInt = this.mCachedBorders;
      paramInt2 = chooseSize(paramInt2, arrayOfInt[arrayOfInt.length - 1] + j, getMinimumHeight());
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt == this.mSpanCount)
      return; 
    this.mPendingSpanCountChange = true;
    if (paramInt >= 1) {
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Span count should be at least 1. Provided ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (!paramBoolean) {
      super.setStackFromEnd(false);
      return;
    } 
    throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    int findReferenceIndexFromCache(int param1Int) {
      int j = this.mSpanIndexCache.size() - 1;
      int i = 0;
      while (i <= j) {
        int k = i + j >>> 1;
        if (this.mSpanIndexCache.keyAt(k) < param1Int) {
          i = k + 1;
          continue;
        } 
        j = k - 1;
      } 
      param1Int = i - 1;
      return (param1Int >= 0 && param1Int < this.mSpanIndexCache.size()) ? this.mSpanIndexCache.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int i = this.mSpanIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanIndex(param1Int1, param1Int2);
      this.mSpanIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      int k = getSpanSize(param1Int1);
      byte b = 0;
      int i = 0;
      int j;
      for (j = i; b < param1Int1; j = m) {
        int m;
        int n = getSpanSize(b);
        int i1 = i + n;
        if (i1 == param1Int2) {
          m = j + 1;
          i = 0;
        } else {
          i = i1;
          m = j;
          if (i1 > param1Int2) {
            m = j + 1;
            i = n;
          } 
        } 
        b++;
      } 
      param1Int1 = j;
      if (i + k > param1Int2)
        param1Int1 = j + 1; 
      return param1Int1;
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: aload_0
      //   1: iload_1
      //   2: invokevirtual getSpanSize : (I)I
      //   5: istore #8
      //   7: iload #8
      //   9: iload_2
      //   10: if_icmpne -> 15
      //   13: iconst_0
      //   14: ireturn
      //   15: aload_0
      //   16: getfield mCacheSpanIndices : Z
      //   19: ifeq -> 64
      //   22: aload_0
      //   23: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   26: invokevirtual size : ()I
      //   29: ifle -> 64
      //   32: aload_0
      //   33: iload_1
      //   34: invokevirtual findReferenceIndexFromCache : (I)I
      //   37: istore #5
      //   39: iload #5
      //   41: iflt -> 64
      //   44: aload_0
      //   45: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   48: iload #5
      //   50: invokevirtual get : (I)I
      //   53: aload_0
      //   54: iload #5
      //   56: invokevirtual getSpanSize : (I)I
      //   59: iadd
      //   60: istore_3
      //   61: goto -> 124
      //   64: iconst_0
      //   65: istore #4
      //   67: iconst_0
      //   68: istore_3
      //   69: iload #4
      //   71: iload_1
      //   72: if_icmpge -> 133
      //   75: aload_0
      //   76: iload #4
      //   78: invokevirtual getSpanSize : (I)I
      //   81: istore #6
      //   83: iload_3
      //   84: iload #6
      //   86: iadd
      //   87: istore #7
      //   89: iload #7
      //   91: iload_2
      //   92: if_icmpne -> 104
      //   95: iconst_0
      //   96: istore_3
      //   97: iload #4
      //   99: istore #5
      //   101: goto -> 124
      //   104: iload #4
      //   106: istore #5
      //   108: iload #7
      //   110: istore_3
      //   111: iload #7
      //   113: iload_2
      //   114: if_icmple -> 124
      //   117: iload #6
      //   119: istore_3
      //   120: iload #4
      //   122: istore #5
      //   124: iload #5
      //   126: iconst_1
      //   127: iadd
      //   128: istore #4
      //   130: goto -> 69
      //   133: iload #8
      //   135: iload_3
      //   136: iadd
      //   137: iload_2
      //   138: if_icmpgt -> 143
      //   141: iload_3
      //   142: ireturn
      //   143: iconst_0
      //   144: ireturn
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\GridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */