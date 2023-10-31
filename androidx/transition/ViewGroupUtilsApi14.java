package androidx.transition;

import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
  private static final int LAYOUT_TRANSITION_CHANGING = 4;
  
  private static final String TAG = "ViewGroupUtilsApi14";
  
  private static Method sCancelMethod;
  
  private static boolean sCancelMethodFetched;
  
  private static LayoutTransition sEmptyLayoutTransition;
  
  private static Field sLayoutSuppressedField;
  
  private static boolean sLayoutSuppressedFieldFetched;
  
  private static void cancelLayoutTransition(LayoutTransition paramLayoutTransition) {
    if (!sCancelMethodFetched) {
      try {
        Method method1 = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
        sCancelMethod = method1;
        method1.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
      } 
      sCancelMethodFetched = true;
    } 
    Method method = sCancelMethod;
    if (method != null)
      try {
        method.invoke(paramLayoutTransition, new Object[0]);
      } catch (IllegalAccessException illegalAccessException) {
        Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
      } catch (InvocationTargetException invocationTargetException) {
        Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
      }  
  }
  
  static void suppressLayout(ViewGroup paramViewGroup, boolean paramBoolean) {
    // Byte code:
    //   0: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   3: astore #4
    //   5: iconst_0
    //   6: istore_3
    //   7: iconst_0
    //   8: istore_2
    //   9: aload #4
    //   11: ifnonnull -> 67
    //   14: new androidx/transition/ViewGroupUtilsApi14$1
    //   17: dup
    //   18: invokespecial <init> : ()V
    //   21: astore #4
    //   23: aload #4
    //   25: putstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   28: aload #4
    //   30: iconst_2
    //   31: aconst_null
    //   32: invokevirtual setAnimator : (ILandroid/animation/Animator;)V
    //   35: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   38: iconst_0
    //   39: aconst_null
    //   40: invokevirtual setAnimator : (ILandroid/animation/Animator;)V
    //   43: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   46: iconst_1
    //   47: aconst_null
    //   48: invokevirtual setAnimator : (ILandroid/animation/Animator;)V
    //   51: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   54: iconst_3
    //   55: aconst_null
    //   56: invokevirtual setAnimator : (ILandroid/animation/Animator;)V
    //   59: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   62: iconst_4
    //   63: aconst_null
    //   64: invokevirtual setAnimator : (ILandroid/animation/Animator;)V
    //   67: iload_1
    //   68: ifeq -> 122
    //   71: aload_0
    //   72: invokevirtual getLayoutTransition : ()Landroid/animation/LayoutTransition;
    //   75: astore #4
    //   77: aload #4
    //   79: ifnull -> 112
    //   82: aload #4
    //   84: invokevirtual isRunning : ()Z
    //   87: ifeq -> 95
    //   90: aload #4
    //   92: invokestatic cancelLayoutTransition : (Landroid/animation/LayoutTransition;)V
    //   95: aload #4
    //   97: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   100: if_acmpeq -> 112
    //   103: aload_0
    //   104: getstatic androidx/transition/R$id.transition_layout_save : I
    //   107: aload #4
    //   109: invokevirtual setTag : (ILjava/lang/Object;)V
    //   112: aload_0
    //   113: getstatic androidx/transition/ViewGroupUtilsApi14.sEmptyLayoutTransition : Landroid/animation/LayoutTransition;
    //   116: invokevirtual setLayoutTransition : (Landroid/animation/LayoutTransition;)V
    //   119: goto -> 259
    //   122: aload_0
    //   123: aconst_null
    //   124: invokevirtual setLayoutTransition : (Landroid/animation/LayoutTransition;)V
    //   127: getstatic androidx/transition/ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched : Z
    //   130: ifne -> 170
    //   133: ldc android/view/ViewGroup
    //   135: ldc 'mLayoutSuppressed'
    //   137: invokevirtual getDeclaredField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   140: astore #4
    //   142: aload #4
    //   144: putstatic androidx/transition/ViewGroupUtilsApi14.sLayoutSuppressedField : Ljava/lang/reflect/Field;
    //   147: aload #4
    //   149: iconst_1
    //   150: invokevirtual setAccessible : (Z)V
    //   153: goto -> 166
    //   156: astore #4
    //   158: ldc 'ViewGroupUtilsApi14'
    //   160: ldc 'Failed to access mLayoutSuppressed field by reflection'
    //   162: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   165: pop
    //   166: iconst_1
    //   167: putstatic androidx/transition/ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched : Z
    //   170: getstatic androidx/transition/ViewGroupUtilsApi14.sLayoutSuppressedField : Ljava/lang/reflect/Field;
    //   173: astore #4
    //   175: iload_3
    //   176: istore_1
    //   177: aload #4
    //   179: ifnull -> 220
    //   182: aload #4
    //   184: aload_0
    //   185: invokevirtual getBoolean : (Ljava/lang/Object;)Z
    //   188: istore_1
    //   189: iload_1
    //   190: ifeq -> 209
    //   193: getstatic androidx/transition/ViewGroupUtilsApi14.sLayoutSuppressedField : Ljava/lang/reflect/Field;
    //   196: aload_0
    //   197: iconst_0
    //   198: invokevirtual setBoolean : (Ljava/lang/Object;Z)V
    //   201: goto -> 209
    //   204: astore #4
    //   206: goto -> 212
    //   209: goto -> 220
    //   212: ldc 'ViewGroupUtilsApi14'
    //   214: ldc 'Failed to get mLayoutSuppressed field by reflection'
    //   216: invokestatic i : (Ljava/lang/String;Ljava/lang/String;)I
    //   219: pop
    //   220: iload_1
    //   221: ifeq -> 228
    //   224: aload_0
    //   225: invokevirtual requestLayout : ()V
    //   228: aload_0
    //   229: getstatic androidx/transition/R$id.transition_layout_save : I
    //   232: invokevirtual getTag : (I)Ljava/lang/Object;
    //   235: checkcast android/animation/LayoutTransition
    //   238: astore #4
    //   240: aload #4
    //   242: ifnull -> 259
    //   245: aload_0
    //   246: getstatic androidx/transition/R$id.transition_layout_save : I
    //   249: aconst_null
    //   250: invokevirtual setTag : (ILjava/lang/Object;)V
    //   253: aload_0
    //   254: aload #4
    //   256: invokevirtual setLayoutTransition : (Landroid/animation/LayoutTransition;)V
    //   259: return
    //   260: astore #4
    //   262: iload_2
    //   263: istore_1
    //   264: goto -> 212
    // Exception table:
    //   from	to	target	type
    //   133	153	156	java/lang/NoSuchFieldException
    //   182	189	260	java/lang/IllegalAccessException
    //   193	201	204	java/lang/IllegalAccessException
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ViewGroupUtilsApi14.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */