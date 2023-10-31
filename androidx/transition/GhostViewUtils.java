package androidx.transition;

import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
  static GhostViewImpl addGhost(View paramView, ViewGroup paramViewGroup, Matrix paramMatrix) {
    return (Build.VERSION.SDK_INT >= 21) ? GhostViewApi21.addGhost(paramView, paramViewGroup, paramMatrix) : GhostViewApi14.addGhost(paramView, paramViewGroup);
  }
  
  static void removeGhost(View paramView) {
    if (Build.VERSION.SDK_INT >= 21) {
      GhostViewApi21.removeGhost(paramView);
    } else {
      GhostViewApi14.removeGhost(paramView);
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\GhostViewUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */