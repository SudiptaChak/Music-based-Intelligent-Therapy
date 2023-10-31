package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {
  private Button actionView;
  
  private int maxInlineActionWidth;
  
  private int maxWidth;
  
  private TextView messageView;
  
  public SnackbarContentLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public SnackbarContentLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SnackbarLayout);
    this.maxWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
    this.maxInlineActionWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
    typedArray.recycle();
  }
  
  private static void updateTopBottomPadding(View paramView, int paramInt1, int paramInt2) {
    if (ViewCompat.isPaddingRelative(paramView)) {
      ViewCompat.setPaddingRelative(paramView, ViewCompat.getPaddingStart(paramView), paramInt1, ViewCompat.getPaddingEnd(paramView), paramInt2);
    } else {
      paramView.setPadding(paramView.getPaddingLeft(), paramInt1, paramView.getPaddingRight(), paramInt2);
    } 
  }
  
  private boolean updateViewsWithinLayout(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1;
    int i = getOrientation();
    boolean bool2 = true;
    if (paramInt1 != i) {
      setOrientation(paramInt1);
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (this.messageView.getPaddingTop() != paramInt2 || this.messageView.getPaddingBottom() != paramInt3) {
      updateTopBottomPadding((View)this.messageView, paramInt2, paramInt3);
      bool1 = bool2;
    } 
    return bool1;
  }
  
  public void animateContentIn(int paramInt1, int paramInt2) {
    this.messageView.setAlpha(0.0F);
    ViewPropertyAnimator viewPropertyAnimator = this.messageView.animate().alpha(1.0F);
    long l2 = paramInt2;
    viewPropertyAnimator = viewPropertyAnimator.setDuration(l2);
    long l1 = paramInt1;
    viewPropertyAnimator.setStartDelay(l1).start();
    if (this.actionView.getVisibility() == 0) {
      this.actionView.setAlpha(0.0F);
      this.actionView.animate().alpha(1.0F).setDuration(l2).setStartDelay(l1).start();
    } 
  }
  
  public void animateContentOut(int paramInt1, int paramInt2) {
    this.messageView.setAlpha(1.0F);
    ViewPropertyAnimator viewPropertyAnimator = this.messageView.animate().alpha(0.0F);
    long l1 = paramInt2;
    viewPropertyAnimator = viewPropertyAnimator.setDuration(l1);
    long l2 = paramInt1;
    viewPropertyAnimator.setStartDelay(l2).start();
    if (this.actionView.getVisibility() == 0) {
      this.actionView.setAlpha(1.0F);
      this.actionView.animate().alpha(0.0F).setDuration(l1).setStartDelay(l2).start();
    } 
  }
  
  public Button getActionView() {
    return this.actionView;
  }
  
  public TextView getMessageView() {
    return this.messageView;
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    this.messageView = (TextView)findViewById(R.id.snackbar_text);
    this.actionView = (Button)findViewById(R.id.snackbar_action);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: iload_2
    //   3: invokespecial onMeasure : (II)V
    //   6: iload_1
    //   7: istore_3
    //   8: aload_0
    //   9: getfield maxWidth : I
    //   12: ifle -> 50
    //   15: aload_0
    //   16: invokevirtual getMeasuredWidth : ()I
    //   19: istore #5
    //   21: aload_0
    //   22: getfield maxWidth : I
    //   25: istore #4
    //   27: iload_1
    //   28: istore_3
    //   29: iload #5
    //   31: iload #4
    //   33: if_icmple -> 50
    //   36: iload #4
    //   38: ldc 1073741824
    //   40: invokestatic makeMeasureSpec : (II)I
    //   43: istore_3
    //   44: aload_0
    //   45: iload_3
    //   46: iload_2
    //   47: invokespecial onMeasure : (II)V
    //   50: aload_0
    //   51: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   54: getstatic com/google/android/material/R$dimen.design_snackbar_padding_vertical_2lines : I
    //   57: invokevirtual getDimensionPixelSize : (I)I
    //   60: istore #4
    //   62: aload_0
    //   63: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   66: getstatic com/google/android/material/R$dimen.design_snackbar_padding_vertical : I
    //   69: invokevirtual getDimensionPixelSize : (I)I
    //   72: istore #6
    //   74: aload_0
    //   75: getfield messageView : Landroid/widget/TextView;
    //   78: invokevirtual getLayout : ()Landroid/text/Layout;
    //   81: invokevirtual getLineCount : ()I
    //   84: istore_1
    //   85: iconst_0
    //   86: istore #5
    //   88: iload_1
    //   89: iconst_1
    //   90: if_icmple -> 98
    //   93: iconst_1
    //   94: istore_1
    //   95: goto -> 100
    //   98: iconst_0
    //   99: istore_1
    //   100: iload_1
    //   101: ifeq -> 146
    //   104: aload_0
    //   105: getfield maxInlineActionWidth : I
    //   108: ifle -> 146
    //   111: aload_0
    //   112: getfield actionView : Landroid/widget/Button;
    //   115: invokevirtual getMeasuredWidth : ()I
    //   118: aload_0
    //   119: getfield maxInlineActionWidth : I
    //   122: if_icmple -> 146
    //   125: iload #5
    //   127: istore_1
    //   128: aload_0
    //   129: iconst_1
    //   130: iload #4
    //   132: iload #4
    //   134: iload #6
    //   136: isub
    //   137: invokespecial updateViewsWithinLayout : (III)Z
    //   140: ifeq -> 174
    //   143: goto -> 172
    //   146: iload_1
    //   147: ifeq -> 153
    //   150: goto -> 157
    //   153: iload #6
    //   155: istore #4
    //   157: iload #5
    //   159: istore_1
    //   160: aload_0
    //   161: iconst_0
    //   162: iload #4
    //   164: iload #4
    //   166: invokespecial updateViewsWithinLayout : (III)Z
    //   169: ifeq -> 174
    //   172: iconst_1
    //   173: istore_1
    //   174: iload_1
    //   175: ifeq -> 184
    //   178: aload_0
    //   179: iload_3
    //   180: iload_2
    //   181: invokespecial onMeasure : (II)V
    //   184: return
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\snackbar\SnackbarContentLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */