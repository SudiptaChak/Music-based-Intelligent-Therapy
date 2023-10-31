package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
  private int baseline = -1;
  
  public BaselineLayout(Context paramContext) {
    super(paramContext, null, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet, 0);
  }
  
  public BaselineLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public int getBaseline() {
    return this.baseline;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = getChildCount();
    int k = getPaddingLeft();
    int m = getPaddingRight();
    int i = getPaddingTop();
    for (paramInt2 = 0; paramInt2 < j; paramInt2++) {
      View view = getChildAt(paramInt2);
      if (view.getVisibility() != 8) {
        int i2 = view.getMeasuredWidth();
        int n = view.getMeasuredHeight();
        int i1 = (paramInt3 - paramInt1 - m - k - i2) / 2 + k;
        if (this.baseline != -1 && view.getBaseline() != -1) {
          paramInt4 = this.baseline + i - view.getBaseline();
        } else {
          paramInt4 = i;
        } 
        view.layout(i1, paramInt4, i2 + i1, n + paramInt4);
      } 
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i2 = getChildCount();
    byte b = 0;
    int j = -1;
    int i = -1;
    int k = 0;
    int i1 = 0;
    int n = i1;
    while (b < i2) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        measureChild(view, paramInt1, paramInt2);
        int i5 = view.getBaseline();
        int i3 = j;
        int i4 = i;
        if (i5 != -1) {
          i3 = Math.max(j, i5);
          i4 = Math.max(i, view.getMeasuredHeight() - i5);
        } 
        i1 = Math.max(i1, view.getMeasuredWidth());
        k = Math.max(k, view.getMeasuredHeight());
        n = View.combineMeasuredStates(n, view.getMeasuredState());
        i = i4;
        j = i3;
      } 
      b++;
    } 
    int m = k;
    if (j != -1) {
      m = Math.max(k, Math.max(i, getPaddingBottom()) + j);
      this.baseline = j;
    } 
    i = Math.max(m, getSuggestedMinimumHeight());
    setMeasuredDimension(View.resolveSizeAndState(Math.max(i1, getSuggestedMinimumWidth()), paramInt1, n), View.resolveSizeAndState(i, paramInt2, n << 16));
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\BaselineLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */