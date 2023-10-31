package com.google.android.material.bottomnavigation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface LabelVisibilityMode {
  public static final int LABEL_VISIBILITY_AUTO = -1;
  
  public static final int LABEL_VISIBILITY_LABELED = 1;
  
  public static final int LABEL_VISIBILITY_SELECTED = 0;
  
  public static final int LABEL_VISIBILITY_UNLABELED = 2;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomnavigation\LabelVisibilityMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */