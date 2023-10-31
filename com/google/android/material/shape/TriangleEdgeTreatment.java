package com.google.android.material.shape;

public class TriangleEdgeTreatment extends EdgeTreatment {
  private final boolean inside;
  
  private final float size;
  
  public TriangleEdgeTreatment(float paramFloat, boolean paramBoolean) {
    this.size = paramFloat;
    this.inside = paramBoolean;
  }
  
  public void getEdgePath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    float f1;
    float f2 = paramFloat1 / 2.0F;
    paramShapePath.lineTo(f2 - this.size * paramFloat2, 0.0F);
    if (this.inside) {
      f1 = this.size;
    } else {
      f1 = -this.size;
    } 
    paramShapePath.lineTo(f2, f1 * paramFloat2);
    paramShapePath.lineTo(f2 + this.size * paramFloat2, 0.0F);
    paramShapePath.lineTo(paramFloat1, 0.0F);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\TriangleEdgeTreatment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */