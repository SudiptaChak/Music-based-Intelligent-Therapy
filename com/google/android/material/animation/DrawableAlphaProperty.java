package com.google.android.material.animation;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import java.util.WeakHashMap;

public class DrawableAlphaProperty extends Property<Drawable, Integer> {
  public static final Property<Drawable, Integer> DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();
  
  private final WeakHashMap<Drawable, Integer> alphaCache = new WeakHashMap<Drawable, Integer>();
  
  private DrawableAlphaProperty() {
    super(Integer.class, "drawableAlphaCompat");
  }
  
  public Integer get(Drawable paramDrawable) {
    return (Build.VERSION.SDK_INT >= 19) ? Integer.valueOf(paramDrawable.getAlpha()) : (this.alphaCache.containsKey(paramDrawable) ? this.alphaCache.get(paramDrawable) : Integer.valueOf(255));
  }
  
  public void set(Drawable paramDrawable, Integer paramInteger) {
    if (Build.VERSION.SDK_INT < 19)
      this.alphaCache.put(paramDrawable, paramInteger); 
    paramDrawable.setAlpha(paramInteger.intValue());
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\animation\DrawableAlphaProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */