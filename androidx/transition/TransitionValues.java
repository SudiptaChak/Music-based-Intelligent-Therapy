package androidx.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
  final ArrayList<Transition> mTargetedTransitions = new ArrayList<Transition>();
  
  public final Map<String, Object> values = new HashMap<String, Object>();
  
  public View view;
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof TransitionValues) {
      View view = this.view;
      paramObject = paramObject;
      if (view == ((TransitionValues)paramObject).view && this.values.equals(((TransitionValues)paramObject).values))
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    return this.view.hashCode() * 31 + this.values.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("TransitionValues@");
    stringBuilder1.append(Integer.toHexString(hashCode()));
    stringBuilder1.append(":\n");
    String str2 = stringBuilder1.toString();
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str2);
    stringBuilder1.append("    view = ");
    stringBuilder1.append(this.view);
    stringBuilder1.append("\n");
    String str1 = stringBuilder1.toString();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str1);
    stringBuilder2.append("    values:");
    str1 = stringBuilder2.toString();
    for (String str : this.values.keySet()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("    ");
      stringBuilder.append(str);
      stringBuilder.append(": ");
      stringBuilder.append(this.values.get(str));
      stringBuilder.append("\n");
      str1 = stringBuilder.toString();
    } 
    return str1;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */