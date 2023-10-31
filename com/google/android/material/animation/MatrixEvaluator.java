package com.google.android.material.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator implements TypeEvaluator<Matrix> {
  private final float[] tempEndValues = new float[9];
  
  private final Matrix tempMatrix = new Matrix();
  
  private final float[] tempStartValues = new float[9];
  
  public Matrix evaluate(float paramFloat, Matrix paramMatrix1, Matrix paramMatrix2) {
    paramMatrix1.getValues(this.tempStartValues);
    paramMatrix2.getValues(this.tempEndValues);
    for (byte b = 0; b < 9; b++) {
      float[] arrayOfFloat1 = this.tempEndValues;
      float f2 = arrayOfFloat1[b];
      float[] arrayOfFloat2 = this.tempStartValues;
      float f1 = arrayOfFloat2[b];
      arrayOfFloat1[b] = arrayOfFloat2[b] + (f2 - f1) * paramFloat;
    } 
    this.tempMatrix.setValues(this.tempEndValues);
    return this.tempMatrix;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\MatrixEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */