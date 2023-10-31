package com.google.android.material.card;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.google.android.material.R;

class MaterialCardViewHelper {
  private static final int DEFAULT_STROKE_VALUE = -1;
  
  private final MaterialCardView materialCardView;
  
  private int strokeColor;
  
  private int strokeWidth;
  
  public MaterialCardViewHelper(MaterialCardView paramMaterialCardView) {
    this.materialCardView = paramMaterialCardView;
  }
  
  private void adjustContentPadding() {
    int i = this.materialCardView.getContentPaddingLeft();
    int i2 = this.strokeWidth;
    int n = this.materialCardView.getContentPaddingTop();
    int j = this.strokeWidth;
    int i3 = this.materialCardView.getContentPaddingRight();
    int i1 = this.strokeWidth;
    int k = this.materialCardView.getContentPaddingBottom();
    int m = this.strokeWidth;
    this.materialCardView.setContentPadding(i + i2, n + j, i3 + i1, k + m);
  }
  
  private Drawable createForegroundDrawable() {
    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setCornerRadius(this.materialCardView.getRadius());
    int i = this.strokeColor;
    if (i != -1)
      gradientDrawable.setStroke(this.strokeWidth, i); 
    return (Drawable)gradientDrawable;
  }
  
  int getStrokeColor() {
    return this.strokeColor;
  }
  
  int getStrokeWidth() {
    return this.strokeWidth;
  }
  
  public void loadFromAttributes(TypedArray paramTypedArray) {
    this.strokeColor = paramTypedArray.getColor(R.styleable.MaterialCardView_strokeColor, -1);
    this.strokeWidth = paramTypedArray.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
    updateForeground();
    adjustContentPadding();
  }
  
  void setStrokeColor(int paramInt) {
    this.strokeColor = paramInt;
    updateForeground();
  }
  
  void setStrokeWidth(int paramInt) {
    this.strokeWidth = paramInt;
    updateForeground();
    adjustContentPadding();
  }
  
  void updateForeground() {
    this.materialCardView.setForeground(createForegroundDrawable());
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\card\MaterialCardViewHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */