package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public abstract class Visibility extends Transition {
  public static final int MODE_IN = 1;
  
  public static final int MODE_OUT = 2;
  
  private static final String PROPNAME_PARENT = "android:visibility:parent";
  
  private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
  
  static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
  
  private static final String[] sTransitionProperties = new String[] { "android:visibility:visibility", "android:visibility:parent" };
  
  private int mMode = 3;
  
  public Visibility() {}
  
  public Visibility(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.VISIBILITY_TRANSITION);
    int i = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)paramAttributeSet, "transitionVisibilityMode", 0, 0);
    typedArray.recycle();
    if (i != 0)
      setMode(i); 
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    int i = paramTransitionValues.view.getVisibility();
    paramTransitionValues.values.put("android:visibility:visibility", Integer.valueOf(i));
    paramTransitionValues.values.put("android:visibility:parent", paramTransitionValues.view.getParent());
    int[] arrayOfInt = new int[2];
    paramTransitionValues.view.getLocationOnScreen(arrayOfInt);
    paramTransitionValues.values.put("android:visibility:screenLocation", arrayOfInt);
  }
  
  private VisibilityInfo getVisibilityChangeInfo(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = new VisibilityInfo();
    visibilityInfo.mVisibilityChange = false;
    visibilityInfo.mFadeIn = false;
    if (paramTransitionValues1 != null && paramTransitionValues1.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mStartVisibility = ((Integer)paramTransitionValues1.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mStartParent = (ViewGroup)paramTransitionValues1.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mStartVisibility = -1;
      visibilityInfo.mStartParent = null;
    } 
    if (paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility")) {
      visibilityInfo.mEndVisibility = ((Integer)paramTransitionValues2.values.get("android:visibility:visibility")).intValue();
      visibilityInfo.mEndParent = (ViewGroup)paramTransitionValues2.values.get("android:visibility:parent");
    } else {
      visibilityInfo.mEndVisibility = -1;
      visibilityInfo.mEndParent = null;
    } 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null) {
      if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent)
        return visibilityInfo; 
      if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
        if (visibilityInfo.mStartVisibility == 0) {
          visibilityInfo.mFadeIn = false;
          visibilityInfo.mVisibilityChange = true;
        } else if (visibilityInfo.mEndVisibility == 0) {
          visibilityInfo.mFadeIn = true;
          visibilityInfo.mVisibilityChange = true;
        } 
      } else if (visibilityInfo.mEndParent == null) {
        visibilityInfo.mFadeIn = false;
        visibilityInfo.mVisibilityChange = true;
      } else if (visibilityInfo.mStartParent == null) {
        visibilityInfo.mFadeIn = true;
        visibilityInfo.mVisibilityChange = true;
      } 
    } else if (paramTransitionValues1 == null && visibilityInfo.mEndVisibility == 0) {
      visibilityInfo.mFadeIn = true;
      visibilityInfo.mVisibilityChange = true;
    } else if (paramTransitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
      visibilityInfo.mFadeIn = false;
      visibilityInfo.mVisibilityChange = true;
    } 
    return visibilityInfo;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    return (visibilityInfo.mVisibilityChange && (visibilityInfo.mStartParent != null || visibilityInfo.mEndParent != null)) ? (visibilityInfo.mFadeIn ? onAppear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility) : onDisappear(paramViewGroup, paramTransitionValues1, visibilityInfo.mStartVisibility, paramTransitionValues2, visibilityInfo.mEndVisibility)) : null;
  }
  
  public int getMode() {
    return this.mMode;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    boolean bool = false;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null)
      return false; 
    if (paramTransitionValues1 != null && paramTransitionValues2 != null && paramTransitionValues2.values.containsKey("android:visibility:visibility") != paramTransitionValues1.values.containsKey("android:visibility:visibility"))
      return false; 
    VisibilityInfo visibilityInfo = getVisibilityChangeInfo(paramTransitionValues1, paramTransitionValues2);
    null = bool;
    if (visibilityInfo.mVisibilityChange) {
      if (visibilityInfo.mStartVisibility != 0) {
        null = bool;
        return (visibilityInfo.mEndVisibility == 0) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public boolean isVisible(TransitionValues paramTransitionValues) {
    boolean bool2 = false;
    if (paramTransitionValues == null)
      return false; 
    int i = ((Integer)paramTransitionValues.values.get("android:visibility:visibility")).intValue();
    View view = (View)paramTransitionValues.values.get("android:visibility:parent");
    boolean bool1 = bool2;
    if (i == 0) {
      bool1 = bool2;
      if (view != null)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    if ((this.mMode & 0x1) != 1 || paramTransitionValues2 == null)
      return null; 
    if (paramTransitionValues1 == null) {
      View view = (View)paramTransitionValues2.view.getParent();
      if ((getVisibilityChangeInfo(getMatchedTransitionValues(view, false), getTransitionValues(view, false))).mVisibilityChange)
        return null; 
    } 
    return onAppear(paramViewGroup, paramTransitionValues2.view, paramTransitionValues1, paramTransitionValues2);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, int paramInt1, TransitionValues paramTransitionValues2, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMode : I
    //   4: iconst_2
    //   5: iand
    //   6: iconst_2
    //   7: if_icmpeq -> 12
    //   10: aconst_null
    //   11: areturn
    //   12: aload_2
    //   13: ifnull -> 25
    //   16: aload_2
    //   17: getfield view : Landroid/view/View;
    //   20: astore #7
    //   22: goto -> 28
    //   25: aconst_null
    //   26: astore #7
    //   28: aload #4
    //   30: ifnull -> 43
    //   33: aload #4
    //   35: getfield view : Landroid/view/View;
    //   38: astore #6
    //   40: goto -> 46
    //   43: aconst_null
    //   44: astore #6
    //   46: aload #6
    //   48: ifnull -> 121
    //   51: aload #6
    //   53: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   56: ifnonnull -> 62
    //   59: goto -> 121
    //   62: iload #5
    //   64: iconst_4
    //   65: if_icmpne -> 71
    //   68: goto -> 78
    //   71: aload #7
    //   73: aload #6
    //   75: if_acmpne -> 92
    //   78: aconst_null
    //   79: astore #8
    //   81: aload #6
    //   83: astore #7
    //   85: aload #8
    //   87: astore #6
    //   89: goto -> 263
    //   92: aload_0
    //   93: getfield mCanRemoveViews : Z
    //   96: ifeq -> 102
    //   99: goto -> 145
    //   102: aload_1
    //   103: aload #7
    //   105: aload #7
    //   107: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   110: checkcast android/view/View
    //   113: invokestatic copyViewImage : (Landroid/view/ViewGroup;Landroid/view/View;Landroid/view/View;)Landroid/view/View;
    //   116: astore #6
    //   118: goto -> 126
    //   121: aload #6
    //   123: ifnull -> 132
    //   126: aconst_null
    //   127: astore #7
    //   129: goto -> 263
    //   132: aload #7
    //   134: ifnull -> 257
    //   137: aload #7
    //   139: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   142: ifnonnull -> 152
    //   145: aload #7
    //   147: astore #6
    //   149: goto -> 126
    //   152: aload #7
    //   154: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   157: instanceof android/view/View
    //   160: ifeq -> 257
    //   163: aload #7
    //   165: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   168: checkcast android/view/View
    //   171: astore #6
    //   173: aload_0
    //   174: aload_0
    //   175: aload #6
    //   177: iconst_1
    //   178: invokevirtual getTransitionValues : (Landroid/view/View;Z)Landroidx/transition/TransitionValues;
    //   181: aload_0
    //   182: aload #6
    //   184: iconst_1
    //   185: invokevirtual getMatchedTransitionValues : (Landroid/view/View;Z)Landroidx/transition/TransitionValues;
    //   188: invokespecial getVisibilityChangeInfo : (Landroidx/transition/TransitionValues;Landroidx/transition/TransitionValues;)Landroidx/transition/Visibility$VisibilityInfo;
    //   191: getfield mVisibilityChange : Z
    //   194: ifne -> 210
    //   197: aload_1
    //   198: aload #7
    //   200: aload #6
    //   202: invokestatic copyViewImage : (Landroid/view/ViewGroup;Landroid/view/View;Landroid/view/View;)Landroid/view/View;
    //   205: astore #6
    //   207: goto -> 126
    //   210: aload #6
    //   212: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   215: ifnonnull -> 251
    //   218: aload #6
    //   220: invokevirtual getId : ()I
    //   223: istore_3
    //   224: iload_3
    //   225: iconst_m1
    //   226: if_icmpeq -> 251
    //   229: aload_1
    //   230: iload_3
    //   231: invokevirtual findViewById : (I)Landroid/view/View;
    //   234: ifnull -> 251
    //   237: aload_0
    //   238: getfield mCanRemoveViews : Z
    //   241: ifeq -> 251
    //   244: aload #7
    //   246: astore #6
    //   248: goto -> 126
    //   251: aconst_null
    //   252: astore #6
    //   254: goto -> 126
    //   257: aconst_null
    //   258: astore #6
    //   260: aconst_null
    //   261: astore #7
    //   263: aload #6
    //   265: ifnull -> 405
    //   268: aload_2
    //   269: ifnull -> 405
    //   272: aload_2
    //   273: getfield values : Ljava/util/Map;
    //   276: ldc 'android:visibility:screenLocation'
    //   278: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   283: checkcast [I
    //   286: astore #7
    //   288: aload #7
    //   290: iconst_0
    //   291: iaload
    //   292: istore #5
    //   294: aload #7
    //   296: iconst_1
    //   297: iaload
    //   298: istore_3
    //   299: iconst_2
    //   300: newarray int
    //   302: astore #7
    //   304: aload_1
    //   305: aload #7
    //   307: invokevirtual getLocationOnScreen : ([I)V
    //   310: aload #6
    //   312: iload #5
    //   314: aload #7
    //   316: iconst_0
    //   317: iaload
    //   318: isub
    //   319: aload #6
    //   321: invokevirtual getLeft : ()I
    //   324: isub
    //   325: invokevirtual offsetLeftAndRight : (I)V
    //   328: aload #6
    //   330: iload_3
    //   331: aload #7
    //   333: iconst_1
    //   334: iaload
    //   335: isub
    //   336: aload #6
    //   338: invokevirtual getTop : ()I
    //   341: isub
    //   342: invokevirtual offsetTopAndBottom : (I)V
    //   345: aload_1
    //   346: invokestatic getOverlay : (Landroid/view/ViewGroup;)Landroidx/transition/ViewGroupOverlayImpl;
    //   349: astore #7
    //   351: aload #7
    //   353: aload #6
    //   355: invokeinterface add : (Landroid/view/View;)V
    //   360: aload_0
    //   361: aload_1
    //   362: aload #6
    //   364: aload_2
    //   365: aload #4
    //   367: invokevirtual onDisappear : (Landroid/view/ViewGroup;Landroid/view/View;Landroidx/transition/TransitionValues;Landroidx/transition/TransitionValues;)Landroid/animation/Animator;
    //   370: astore_1
    //   371: aload_1
    //   372: ifnonnull -> 387
    //   375: aload #7
    //   377: aload #6
    //   379: invokeinterface remove : (Landroid/view/View;)V
    //   384: goto -> 403
    //   387: aload_1
    //   388: new androidx/transition/Visibility$1
    //   391: dup
    //   392: aload_0
    //   393: aload #7
    //   395: aload #6
    //   397: invokespecial <init> : (Landroidx/transition/Visibility;Landroidx/transition/ViewGroupOverlayImpl;Landroid/view/View;)V
    //   400: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   403: aload_1
    //   404: areturn
    //   405: aload #7
    //   407: ifnull -> 477
    //   410: aload #7
    //   412: invokevirtual getVisibility : ()I
    //   415: istore_3
    //   416: aload #7
    //   418: iconst_0
    //   419: invokestatic setTransitionVisibility : (Landroid/view/View;I)V
    //   422: aload_0
    //   423: aload_1
    //   424: aload #7
    //   426: aload_2
    //   427: aload #4
    //   429: invokevirtual onDisappear : (Landroid/view/ViewGroup;Landroid/view/View;Landroidx/transition/TransitionValues;Landroidx/transition/TransitionValues;)Landroid/animation/Animator;
    //   432: astore_1
    //   433: aload_1
    //   434: ifnull -> 469
    //   437: new androidx/transition/Visibility$DisappearListener
    //   440: dup
    //   441: aload #7
    //   443: iload #5
    //   445: iconst_1
    //   446: invokespecial <init> : (Landroid/view/View;IZ)V
    //   449: astore_2
    //   450: aload_1
    //   451: aload_2
    //   452: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   455: aload_1
    //   456: aload_2
    //   457: invokestatic addPauseListener : (Landroid/animation/Animator;Landroid/animation/AnimatorListenerAdapter;)V
    //   460: aload_0
    //   461: aload_2
    //   462: invokevirtual addListener : (Landroidx/transition/Transition$TransitionListener;)Landroidx/transition/Transition;
    //   465: pop
    //   466: goto -> 475
    //   469: aload #7
    //   471: iload_3
    //   472: invokestatic setTransitionVisibility : (Landroid/view/View;I)V
    //   475: aload_1
    //   476: areturn
    //   477: aconst_null
    //   478: areturn
  }
  
  public void setMode(int paramInt) {
    if ((paramInt & 0xFFFFFFFC) == 0) {
      this.mMode = paramInt;
      return;
    } 
    throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
  }
  
  private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
    boolean mCanceled = false;
    
    private final int mFinalVisibility;
    
    private boolean mLayoutSuppressed;
    
    private final ViewGroup mParent;
    
    private final boolean mSuppressLayout;
    
    private final View mView;
    
    DisappearListener(View param1View, int param1Int, boolean param1Boolean) {
      this.mView = param1View;
      this.mFinalVisibility = param1Int;
      this.mParent = (ViewGroup)param1View.getParent();
      this.mSuppressLayout = param1Boolean;
      suppressLayout(true);
    }
    
    private void hideViewWhenNotCanceled() {
      if (!this.mCanceled) {
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null)
          viewGroup.invalidate(); 
      } 
      suppressLayout(false);
    }
    
    private void suppressLayout(boolean param1Boolean) {
      if (this.mSuppressLayout && this.mLayoutSuppressed != param1Boolean) {
        ViewGroup viewGroup = this.mParent;
        if (viewGroup != null) {
          this.mLayoutSuppressed = param1Boolean;
          ViewGroupUtils.suppressLayout(viewGroup, param1Boolean);
        } 
      } 
    }
    
    public void onAnimationCancel(Animator param1Animator) {
      this.mCanceled = true;
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      hideViewWhenNotCanceled();
    }
    
    public void onAnimationPause(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility); 
    }
    
    public void onAnimationRepeat(Animator param1Animator) {}
    
    public void onAnimationResume(Animator param1Animator) {
      if (!this.mCanceled)
        ViewUtils.setTransitionVisibility(this.mView, 0); 
    }
    
    public void onAnimationStart(Animator param1Animator) {}
    
    public void onTransitionCancel(Transition param1Transition) {}
    
    public void onTransitionEnd(Transition param1Transition) {
      hideViewWhenNotCanceled();
      param1Transition.removeListener(this);
    }
    
    public void onTransitionPause(Transition param1Transition) {
      suppressLayout(false);
    }
    
    public void onTransitionResume(Transition param1Transition) {
      suppressLayout(true);
    }
    
    public void onTransitionStart(Transition param1Transition) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
  
  private static class VisibilityInfo {
    ViewGroup mEndParent;
    
    int mEndVisibility;
    
    boolean mFadeIn;
    
    ViewGroup mStartParent;
    
    int mStartVisibility;
    
    boolean mVisibilityChange;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Visibility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */