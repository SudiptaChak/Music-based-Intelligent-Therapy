package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public class TabItem extends View {
  public final int customLayout;
  
  public final Drawable icon;
  
  public final CharSequence text;
  
  public TabItem(Context paramContext) {
    this(paramContext, null);
  }
  
  public TabItem(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.TabItem);
    this.text = tintTypedArray.getText(R.styleable.TabItem_android_text);
    this.icon = tintTypedArray.getDrawable(R.styleable.TabItem_android_icon);
    this.customLayout = tintTypedArray.getResourceId(R.styleable.TabItem_android_layout, 0);
    tintTypedArray.recycle();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\tabs\TabItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */