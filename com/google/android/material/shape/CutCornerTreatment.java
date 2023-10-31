package com.google.android.material.shape;

public class CutCornerTreatment extends CornerTreatment {
  private final float size;
  
  public CutCornerTreatment(float paramFloat) {
    this.size = paramFloat;
  }
  
  public void getCornerPath(float paramFloat1, float paramFloat2, ShapePath paramShapePath) {
    paramShapePath.reset(0.0F, this.size * paramFloat2);
    double d1 = paramFloat1;
    double d3 = Math.sin(d1);
    double d4 = this.size;
    double d2 = paramFloat2;
    paramShapePath.lineTo((float)(d3 * d4 * d2), (float)(Math.cos(d1) * this.size * d2));
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\shape\CutCornerTreatment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */