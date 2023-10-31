package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class SidePropagation extends VisibilityPropagation {
  private float mPropagationSpeed = 3.0F;
  
  private int mSide = 80;
  
  private int distance(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mSide : I
    //   4: istore #13
    //   6: iconst_0
    //   7: istore #11
    //   9: iconst_1
    //   10: istore #12
    //   12: iconst_1
    //   13: istore #10
    //   15: iload #13
    //   17: ldc 8388611
    //   19: if_icmpne -> 53
    //   22: aload_1
    //   23: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   26: iconst_1
    //   27: if_icmpne -> 33
    //   30: goto -> 36
    //   33: iconst_0
    //   34: istore #10
    //   36: iload #10
    //   38: ifeq -> 47
    //   41: iconst_5
    //   42: istore #10
    //   44: goto -> 90
    //   47: iconst_3
    //   48: istore #10
    //   50: goto -> 90
    //   53: iload #13
    //   55: istore #10
    //   57: iload #13
    //   59: ldc 8388613
    //   61: if_icmpne -> 90
    //   64: aload_1
    //   65: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   68: iconst_1
    //   69: if_icmpne -> 79
    //   72: iload #12
    //   74: istore #10
    //   76: goto -> 82
    //   79: iconst_0
    //   80: istore #10
    //   82: iload #10
    //   84: ifeq -> 41
    //   87: goto -> 47
    //   90: iload #10
    //   92: iconst_3
    //   93: if_icmpeq -> 170
    //   96: iload #10
    //   98: iconst_5
    //   99: if_icmpeq -> 154
    //   102: iload #10
    //   104: bipush #48
    //   106: if_icmpeq -> 138
    //   109: iload #10
    //   111: bipush #80
    //   113: if_icmpeq -> 122
    //   116: iload #11
    //   118: istore_2
    //   119: goto -> 183
    //   122: iload_3
    //   123: iload #7
    //   125: isub
    //   126: iload #4
    //   128: iload_2
    //   129: isub
    //   130: invokestatic abs : (I)I
    //   133: iadd
    //   134: istore_2
    //   135: goto -> 183
    //   138: iload #9
    //   140: iload_3
    //   141: isub
    //   142: iload #4
    //   144: iload_2
    //   145: isub
    //   146: invokestatic abs : (I)I
    //   149: iadd
    //   150: istore_2
    //   151: goto -> 183
    //   154: iload_2
    //   155: iload #6
    //   157: isub
    //   158: iload #5
    //   160: iload_3
    //   161: isub
    //   162: invokestatic abs : (I)I
    //   165: iadd
    //   166: istore_2
    //   167: goto -> 183
    //   170: iload #8
    //   172: iload_2
    //   173: isub
    //   174: iload #5
    //   176: iload_3
    //   177: isub
    //   178: invokestatic abs : (I)I
    //   181: iadd
    //   182: istore_2
    //   183: iload_2
    //   184: ireturn
  }
  
  private int getMaxDistance(ViewGroup paramViewGroup) {
    int i = this.mSide;
    return (i != 3 && i != 5 && i != 8388611 && i != 8388613) ? paramViewGroup.getHeight() : paramViewGroup.getWidth();
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool;
    int i;
    int j;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return 0L; 
    Rect rect = paramTransition.getEpicenter();
    if (paramTransitionValues2 == null || getViewVisibility(paramTransitionValues1) == 0) {
      bool = true;
    } else {
      paramTransitionValues1 = paramTransitionValues2;
      bool = true;
    } 
    int i2 = getViewX(paramTransitionValues1);
    int k = getViewY(paramTransitionValues1);
    int[] arrayOfInt = new int[2];
    paramViewGroup.getLocationOnScreen(arrayOfInt);
    int i3 = arrayOfInt[0] + Math.round(paramViewGroup.getTranslationX());
    int i1 = arrayOfInt[1] + Math.round(paramViewGroup.getTranslationY());
    int m = i3 + paramViewGroup.getWidth();
    int n = i1 + paramViewGroup.getHeight();
    if (rect != null) {
      i = rect.centerX();
      j = rect.centerY();
    } else {
      i = (i3 + m) / 2;
      j = (i1 + n) / 2;
    } 
    float f = distance((View)paramViewGroup, i2, k, i, j, i3, i1, m, n) / getMaxDistance(paramViewGroup);
    long l2 = paramTransition.getDuration();
    long l1 = l2;
    if (l2 < 0L)
      l1 = 300L; 
    return Math.round((float)(l1 * bool) / this.mPropagationSpeed * f);
  }
  
  public void setPropagationSpeed(float paramFloat) {
    if (paramFloat != 0.0F) {
      this.mPropagationSpeed = paramFloat;
      return;
    } 
    throw new IllegalArgumentException("propagationSpeed may not be 0");
  }
  
  public void setSide(int paramInt) {
    this.mSide = paramInt;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\SidePropagation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */