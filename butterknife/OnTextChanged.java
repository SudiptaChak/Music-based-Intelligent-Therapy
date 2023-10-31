package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(callbacks = OnTextChanged.Callback.class, setter = "addTextChangedListener", targetType = "android.widget.TextView", type = "android.text.TextWatcher")
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface OnTextChanged {
  Callback callback() default Callback.TEXT_CHANGED;
  
  int[] value() default {-1};
  
  public enum Callback {
    AFTER_TEXT_CHANGED, BEFORE_TEXT_CHANGED, TEXT_CHANGED;
    
    private static final Callback[] $VALUES;
    
    static {
      Callback callback = new Callback("AFTER_TEXT_CHANGED", 2);
      AFTER_TEXT_CHANGED = callback;
      $VALUES = new Callback[] { TEXT_CHANGED, BEFORE_TEXT_CHANGED, callback };
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\OnTextChanged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */