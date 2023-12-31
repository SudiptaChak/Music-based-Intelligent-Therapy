package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {
  public static Collection<PathSegment> flatten(Path paramPath) {
    return flatten(paramPath, 0.5F);
  }
  
  public static Collection<PathSegment> flatten(Path paramPath, float paramFloat) {
    float[] arrayOfFloat = paramPath.approximate(paramFloat);
    int i = arrayOfFloat.length / 3;
    ArrayList<PathSegment> arrayList = new ArrayList(i);
    for (byte b = 1; b < i; b++) {
      int j = b * 3;
      int k = (b - 1) * 3;
      float f2 = arrayOfFloat[j];
      float f4 = arrayOfFloat[j + 1];
      float f3 = arrayOfFloat[j + 2];
      paramFloat = arrayOfFloat[k];
      float f1 = arrayOfFloat[k + 1];
      float f5 = arrayOfFloat[k + 2];
      if (f2 != paramFloat && (f4 != f1 || f3 != f5))
        arrayList.add(new PathSegment(new PointF(f1, f5), paramFloat, new PointF(f4, f3), f2)); 
    } 
    return arrayList;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\graphics\PathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */