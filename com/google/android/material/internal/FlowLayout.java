package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class FlowLayout extends ViewGroup {
  private int itemSpacing;
  
  private int lineSpacing;
  
  private boolean singleLine = false;
  
  public FlowLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    loadFromAttributes(paramContext, paramAttributeSet);
  }
  
  private static int getMeasuredDimension(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt2 != Integer.MIN_VALUE) ? ((paramInt2 != 1073741824) ? paramInt3 : paramInt1) : Math.min(paramInt3, paramInt1);
  }
  
  private void loadFromAttributes(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.FlowLayout, 0, 0);
    this.lineSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
    this.itemSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_itemSpacing, 0);
    typedArray.recycle();
  }
  
  protected int getItemSpacing() {
    return this.itemSpacing;
  }
  
  protected int getLineSpacing() {
    return this.lineSpacing;
  }
  
  protected boolean isSingleLine() {
    return this.singleLine;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (getChildCount() == 0)
      return; 
    paramInt2 = ViewCompat.getLayoutDirection((View)this);
    boolean bool = true;
    if (paramInt2 != 1)
      bool = false; 
    if (bool) {
      paramInt2 = getPaddingRight();
    } else {
      paramInt2 = getPaddingLeft();
    } 
    if (bool) {
      paramInt4 = getPaddingLeft();
    } else {
      paramInt4 = getPaddingRight();
    } 
    int i = getPaddingTop();
    int j = paramInt3 - paramInt1 - paramInt4;
    paramInt3 = paramInt2;
    byte b = 0;
    paramInt1 = i;
    while (b < getChildCount()) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        byte b1;
        byte b2;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          b1 = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
          b2 = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
        } else {
          b2 = 0;
          b1 = 0;
        } 
        int m = view.getMeasuredWidth();
        int k = paramInt3;
        paramInt4 = paramInt1;
        if (!this.singleLine) {
          k = paramInt3;
          paramInt4 = paramInt1;
          if (paramInt3 + b1 + m > j) {
            paramInt4 = this.lineSpacing + i;
            k = paramInt2;
          } 
        } 
        paramInt1 = k + b1;
        paramInt3 = view.getMeasuredWidth() + paramInt1;
        i = view.getMeasuredHeight() + paramInt4;
        if (bool) {
          view.layout(j - paramInt3, paramInt4, j - k - b1, i);
        } else {
          view.layout(paramInt1, paramInt4, paramInt3, i);
        } 
        paramInt3 = k + b1 + b2 + view.getMeasuredWidth() + this.itemSpacing;
        paramInt1 = paramInt4;
      } 
      b++;
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m;
    int i1 = View.MeasureSpec.getSize(paramInt1);
    int i4 = View.MeasureSpec.getMode(paramInt1);
    int i3 = View.MeasureSpec.getSize(paramInt2);
    int i2 = View.MeasureSpec.getMode(paramInt2);
    if (i4 == Integer.MIN_VALUE || i4 == 1073741824) {
      m = i1;
    } else {
      m = Integer.MAX_VALUE;
    } 
    int k = getPaddingLeft();
    int n = getPaddingTop();
    int i5 = getPaddingRight();
    int j = n;
    byte b = 0;
    int i = 0;
    while (b < getChildCount()) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        byte b1;
        measureChild(view, paramInt1, paramInt2);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
          ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
          i6 = marginLayoutParams.leftMargin + 0;
          b1 = marginLayoutParams.rightMargin + 0;
        } else {
          i6 = 0;
          b1 = 0;
        } 
        if (k + i6 + view.getMeasuredWidth() > m - i5 && !isSingleLine()) {
          j = getPaddingLeft();
          k = this.lineSpacing + n;
          n = j;
          j = k;
        } else {
          n = k;
        } 
        int i7 = n + i6 + view.getMeasuredWidth();
        int i8 = view.getMeasuredHeight();
        k = i;
        if (i7 > i)
          k = i7; 
        int i6 = n + i6 + b1 + view.getMeasuredWidth() + this.itemSpacing;
        n = j + i8;
        i = k;
        k = i6;
      } 
      b++;
    } 
    setMeasuredDimension(getMeasuredDimension(i1, i4, i), getMeasuredDimension(i3, i2, n));
  }
  
  protected void setItemSpacing(int paramInt) {
    this.itemSpacing = paramInt;
  }
  
  protected void setLineSpacing(int paramInt) {
    this.lineSpacing = paramInt;
  }
  
  public void setSingleLine(boolean paramBoolean) {
    this.singleLine = paramBoolean;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\internal\FlowLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */