package androidx.transition;

import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;

class PropertyValuesHolderUtils {
  static PropertyValuesHolder ofPointF(Property<?, PointF> paramProperty, Path paramPath) {
    return (Build.VERSION.SDK_INT >= 21) ? PropertyValuesHolder.ofObject(paramProperty, null, paramPath) : PropertyValuesHolder.ofFloat(new PathProperty(paramProperty, paramPath), new float[] { 0.0F, 1.0F });
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\PropertyValuesHolderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */