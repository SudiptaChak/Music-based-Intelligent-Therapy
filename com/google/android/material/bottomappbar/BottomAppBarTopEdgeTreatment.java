package com.google.android.material.bottomappbar;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment {
  private static final int ANGLE_LEFT = 180;
  
  private static final int ANGLE_UP = 270;
  
  private static final int ARC_HALF = 180;
  
  private static final int ARC_QUARTER = 90;
  
  private float cradleVerticalOffset;
  
  private float fabDiameter;
  
  private float fabMargin;
  
  private float horizontalOffset;
  
  private float roundedCornerRadius;
  
  public BottomAppBarTopEdgeTreatment(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.fabMargin = paramFloat1;
    this.roundedCornerRadius = paramFloat2;
    this.cradleVerticalOffset = paramFloat3;
    if (paramFloat3 >= 0.0F) {
      this.horizontalOffset = 0.0F;
      return;
    } 
    throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
  }
  
  float getCradleVerticalOffset() {
    return this.cradleVerticalOffset;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    float f1 = this.fabDiameter;
    if (f1 == 0.0F) {
      paramShapePath.lineTo(paramFloat1, 0.0F);
      return;
    } 
    float f3 = (this.fabMargin * 2.0F + f1) / 2.0F;
    float f2 = paramFloat2 * this.roundedCornerRadius;
    f1 = paramFloat1 / 2.0F + this.horizontalOffset;
    paramFloat2 = this.cradleVerticalOffset * paramFloat2 + (1.0F - paramFloat2) * f3;
    if (paramFloat2 / f3 >= 1.0F) {
      paramShapePath.lineTo(paramFloat1, 0.0F);
      return;
    } 
    float f4 = f3 + f2;
    float f6 = paramFloat2 + f2;
    float f7 = (float)Math.sqrt((f4 * f4 - f6 * f6));
    float f5 = f1 - f7;
    f4 = f1 + f7;
    float f9 = (float)Math.toDegrees(Math.atan((f7 / f6)));
    f7 = 90.0F - f9;
    f6 = f5 - f2;
    paramShapePath.lineTo(f6, 0.0F);
    float f8 = f2 * 2.0F;
    paramShapePath.addArc(f6, 0.0F, f5 + f2, f8, 270.0F, f9);
    paramShapePath.addArc(f1 - f3, -f3 - paramFloat2, f1 + f3, f3 - paramFloat2, 180.0F - f7, f7 * 2.0F - 180.0F);
    paramShapePath.addArc(f4 - f2, 0.0F, f4 + f2, f8, 270.0F - f9, f9);
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
  
  float getFabCradleMargin() {
    return this.fabMargin;
  }
  
  float getFabCradleRoundedCornerRadius() {
    return this.roundedCornerRadius;
  }
  
  float getFabDiameter() {
    return this.fabDiameter;
  }
  
  float getHorizontalOffset() {
    return this.horizontalOffset;
  }
  
  void setCradleVerticalOffset(float paramFloat) {
    this.cradleVerticalOffset = paramFloat;
  }
  
  void setFabCradleMargin(float paramFloat) {
    this.fabMargin = paramFloat;
  }
  
  void setFabCradleRoundedCornerRadius(float paramFloat) {
    this.roundedCornerRadius = paramFloat;
  }
  
  void setFabDiameter(float paramFloat) {
    this.fabDiameter = paramFloat;
  }
  
  void setHorizontalOffset(float paramFloat) {
    this.horizontalOffset = paramFloat;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomappbar\BottomAppBarTopEdgeTreatment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */