package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(method = {@ListenerMethod(name = "onFocusChange", parameters = {"android.view.View", "boolean"})}, setter = "setOnFocusChangeListener", targetType = "android.view.View", type = "android.view.View.OnFocusChangeListener")
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface OnFocusChange {
  int[] value() default {-1};
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\OnFocusChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */