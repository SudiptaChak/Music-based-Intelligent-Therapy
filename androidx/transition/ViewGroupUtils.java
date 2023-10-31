package androidx.transition;

import android.os.Build;
import android.view.ViewGroup;

class ViewGroupUtils {
  static ViewGroupOverlayImpl getOverlay(ViewGroup paramViewGroup) {
    return (ViewGroupOverlayImpl)((Build.VERSION.SDK_INT >= 18) ? new ViewGroupOverlayApi18(paramViewGroup) : ViewGroupOverlayApi14.createFrom(paramViewGroup));
  }
  
  static void suppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    if (Build.VERSION.SDK_INT >= 18) {
      ViewGroupUtilsApi18.suppressLayout(paramViewGroup, paramBoolean);
    } else {
      ViewGroupUtilsApi14.suppressLayout(paramViewGroup, paramBoolean);
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewGroupUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */