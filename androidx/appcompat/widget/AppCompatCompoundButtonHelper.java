package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

class AppCompatCompoundButtonHelper {
  private ColorStateList mButtonTintList = null;
  
  private PorterDuff.Mode mButtonTintMode = null;
  
  private boolean mHasButtonTint = false;
  
  private boolean mHasButtonTintMode = false;
  
  private boolean mSkipNextApply;
  
  private final CompoundButton mView;
  
  AppCompatCompoundButtonHelper(CompoundButton paramCompoundButton) {
    this.mView = paramCompoundButton;
  }
  
  void applyButtonTint() {
    Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
    if (drawable != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
      drawable = DrawableCompat.wrap(drawable).mutate();
      if (this.mHasButtonTint)
        DrawableCompat.setTintList(drawable, this.mButtonTintList); 
      if (this.mHasButtonTintMode)
        DrawableCompat.setTintMode(drawable, this.mButtonTintMode); 
      if (drawable.isStateful())
        drawable.setState(this.mView.getDrawableState()); 
      this.mView.setButtonDrawable(drawable);
    } 
  }
  
  int getCompoundPaddingLeft(int paramInt) {
    int i = paramInt;
    if (Build.VERSION.SDK_INT < 17) {
      Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
      i = paramInt;
      if (drawable != null)
        i = paramInt + drawable.getIntrinsicWidth(); 
    } 
    return i;
  }
  
  ColorStateList getSupportButtonTintList() {
    return this.mButtonTintList;
  }
  
  PorterDuff.Mode getSupportButtonTintMode() {
    return this.mButtonTintMode;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mView : Landroid/widget/CompoundButton;
    //   4: invokevirtual getContext : ()Landroid/content/Context;
    //   7: aload_1
    //   8: getstatic androidx/appcompat/R$styleable.CompoundButton : [I
    //   11: iload_2
    //   12: iconst_0
    //   13: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
    //   16: astore_1
    //   17: aload_1
    //   18: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonCompat : I
    //   21: invokevirtual hasValue : (I)Z
    //   24: ifeq -> 63
    //   27: aload_1
    //   28: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonCompat : I
    //   31: iconst_0
    //   32: invokevirtual getResourceId : (II)I
    //   35: istore_2
    //   36: iload_2
    //   37: ifeq -> 63
    //   40: aload_0
    //   41: getfield mView : Landroid/widget/CompoundButton;
    //   44: aload_0
    //   45: getfield mView : Landroid/widget/CompoundButton;
    //   48: invokevirtual getContext : ()Landroid/content/Context;
    //   51: iload_2
    //   52: invokestatic getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   55: invokevirtual setButtonDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   58: iconst_1
    //   59: istore_2
    //   60: goto -> 65
    //   63: iconst_0
    //   64: istore_2
    //   65: iload_2
    //   66: ifne -> 110
    //   69: aload_1
    //   70: getstatic androidx/appcompat/R$styleable.CompoundButton_android_button : I
    //   73: invokevirtual hasValue : (I)Z
    //   76: ifeq -> 110
    //   79: aload_1
    //   80: getstatic androidx/appcompat/R$styleable.CompoundButton_android_button : I
    //   83: iconst_0
    //   84: invokevirtual getResourceId : (II)I
    //   87: istore_2
    //   88: iload_2
    //   89: ifeq -> 110
    //   92: aload_0
    //   93: getfield mView : Landroid/widget/CompoundButton;
    //   96: aload_0
    //   97: getfield mView : Landroid/widget/CompoundButton;
    //   100: invokevirtual getContext : ()Landroid/content/Context;
    //   103: iload_2
    //   104: invokestatic getDrawable : (Landroid/content/Context;I)Landroid/graphics/drawable/Drawable;
    //   107: invokevirtual setButtonDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   110: aload_1
    //   111: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonTint : I
    //   114: invokevirtual hasValue : (I)Z
    //   117: ifeq -> 134
    //   120: aload_0
    //   121: getfield mView : Landroid/widget/CompoundButton;
    //   124: aload_1
    //   125: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonTint : I
    //   128: invokevirtual getColorStateList : (I)Landroid/content/res/ColorStateList;
    //   131: invokestatic setButtonTintList : (Landroid/widget/CompoundButton;Landroid/content/res/ColorStateList;)V
    //   134: aload_1
    //   135: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonTintMode : I
    //   138: invokevirtual hasValue : (I)Z
    //   141: ifeq -> 163
    //   144: aload_0
    //   145: getfield mView : Landroid/widget/CompoundButton;
    //   148: aload_1
    //   149: getstatic androidx/appcompat/R$styleable.CompoundButton_buttonTintMode : I
    //   152: iconst_m1
    //   153: invokevirtual getInt : (II)I
    //   156: aconst_null
    //   157: invokestatic parseTintMode : (ILandroid/graphics/PorterDuff$Mode;)Landroid/graphics/PorterDuff$Mode;
    //   160: invokestatic setButtonTintMode : (Landroid/widget/CompoundButton;Landroid/graphics/PorterDuff$Mode;)V
    //   163: aload_1
    //   164: invokevirtual recycle : ()V
    //   167: return
    //   168: astore_3
    //   169: aload_1
    //   170: invokevirtual recycle : ()V
    //   173: aload_3
    //   174: athrow
    //   175: astore_3
    //   176: goto -> 63
    // Exception table:
    //   from	to	target	type
    //   17	36	168	finally
    //   40	58	175	android/content/res/Resources$NotFoundException
    //   40	58	168	finally
    //   69	88	168	finally
    //   92	110	168	finally
    //   110	134	168	finally
    //   134	163	168	finally
  }
  
  void onSetButtonDrawable() {
    if (this.mSkipNextApply) {
      this.mSkipNextApply = false;
      return;
    } 
    this.mSkipNextApply = true;
    applyButtonTint();
  }
  
  void setSupportButtonTintList(ColorStateList paramColorStateList) {
    this.mButtonTintList = paramColorStateList;
    this.mHasButtonTint = true;
    applyButtonTint();
  }
  
  void setSupportButtonTintMode(PorterDuff.Mode paramMode) {
    this.mButtonTintMode = paramMode;
    this.mHasButtonTintMode = true;
    applyButtonTint();
  }
  
  static interface DirectSetButtonDrawableInterface {
    void setButtonDrawable(Drawable param1Drawable);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\AppCompatCompoundButtonHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */