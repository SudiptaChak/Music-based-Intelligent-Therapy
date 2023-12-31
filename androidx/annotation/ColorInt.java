package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface ColorInt {}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\annotation\ColorInt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */