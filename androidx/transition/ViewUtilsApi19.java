package androidx.transition;

import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi19 extends ViewUtilsBase {
  private static final String TAG = "ViewUtilsApi19";
  
  private static Method sGetTransitionAlphaMethod;
  
  private static boolean sGetTransitionAlphaMethodFetched;
  
  private static Method sSetTransitionAlphaMethod;
  
  private static boolean sSetTransitionAlphaMethodFetched;
  
  private void fetchGetTransitionAlphaMethod() {
    if (!sGetTransitionAlphaMethodFetched) {
      try {
        Method method = View.class.getDeclaredMethod("getTransitionAlpha", new Class[0]);
        sGetTransitionAlphaMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ViewUtilsApi19", "Failed to retrieve getTransitionAlpha method", noSuchMethodException);
      } 
      sGetTransitionAlphaMethodFetched = true;
    } 
  }
  
  private void fetchSetTransitionAlphaMethod() {
    if (!sSetTransitionAlphaMethodFetched) {
      try {
        Method method = View.class.getDeclaredMethod("setTransitionAlpha", new Class[] { float.class });
        sSetTransitionAlphaMethod = method;
        method.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ViewUtilsApi19", "Failed to retrieve setTransitionAlpha method", noSuchMethodException);
      } 
      sSetTransitionAlphaMethodFetched = true;
    } 
  }
  
  public void clearNonTransitionAlpha(View paramView) {}
  
  public float getTransitionAlpha(View paramView) {
    fetchGetTransitionAlphaMethod();
    Method method = sGetTransitionAlphaMethod;
    if (method != null)
      try {
        return ((Float)method.invoke(paramView, new Object[0])).floatValue();
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException(invocationTargetException.getCause());
      }  
    return super.getTransitionAlpha((View)invocationTargetException);
  }
  
  public void saveNonTransitionAlpha(View paramView) {}
  
  public void setTransitionAlpha(View paramView, float paramFloat) {
    fetchSetTransitionAlphaMethod();
    Method method = sSetTransitionAlphaMethod;
    if (method != null) {
      try {
        method.invoke(paramView, new Object[] { Float.valueOf(paramFloat) });
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {
        throw new RuntimeException(invocationTargetException.getCause());
      } 
    } else {
      invocationTargetException.setAlpha(paramFloat);
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewUtilsApi19.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */