package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
  private Map<String, Integer> mCalledMethods = new HashMap<String, Integer>();
  
  public boolean approveCall(String paramString, int paramInt) {
    int i;
    Integer integer = this.mCalledMethods.get(paramString);
    boolean bool = false;
    if (integer != null) {
      i = integer.intValue();
    } else {
      i = 0;
    } 
    if ((i & paramInt) != 0)
      bool = true; 
    this.mCalledMethods.put(paramString, Integer.valueOf(paramInt | i));
    return bool ^ true;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\MethodCallsLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */