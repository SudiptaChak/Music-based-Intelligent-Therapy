package com.google.android.material.math;

public final class MathUtils {
  public static final float DEFAULT_EPSILON = 1.0E-4F;
  
  public static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return (float)Math.hypot((paramFloat3 - paramFloat1), (paramFloat4 - paramFloat2));
  }
  
  public static float distanceToFurthestCorner(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    return max(dist(paramFloat1, paramFloat2, paramFloat3, paramFloat4), dist(paramFloat1, paramFloat2, paramFloat5, paramFloat4), dist(paramFloat1, paramFloat2, paramFloat5, paramFloat6), dist(paramFloat1, paramFloat2, paramFloat3, paramFloat6));
  }
  
  public static boolean geq(float paramFloat1, float paramFloat2, float paramFloat3) {
    boolean bool;
    if (paramFloat1 + paramFloat3 >= paramFloat2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (1.0F - paramFloat3) * paramFloat1 + paramFloat3 * paramFloat2;
  }
  
  private static float max(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (paramFloat1 <= paramFloat2 || paramFloat1 <= paramFloat3 || paramFloat1 <= paramFloat4)
      if (paramFloat2 > paramFloat3 && paramFloat2 > paramFloat4) {
        paramFloat1 = paramFloat2;
      } else if (paramFloat3 > paramFloat4) {
        paramFloat1 = paramFloat3;
      } else {
        paramFloat1 = paramFloat4;
      }  
    return paramFloat1;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\math\MathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */