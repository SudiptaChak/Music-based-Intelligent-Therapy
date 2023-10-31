package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.collection.ArraySet;
import androidx.core.util.DebugUtils;
import androidx.core.util.LogWriter;
import androidx.core.view.OneShotPreDrawListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5F);
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  final HashMap<String, Fragment> mActive = new HashMap<String, Fragment>();
  
  final ArrayList<Fragment> mAdded = new ArrayList<Fragment>();
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      final FragmentManagerImpl this$0;
      
      public void run() {
        FragmentManagerImpl.this.execPendingActions();
      }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder>();
  
  boolean mNeedMenuInvalidate;
  
  int mNextFragmentIndex = 0;
  
  private FragmentManagerViewModel mNonConfig;
  
  private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
      final FragmentManagerImpl this$0;
      
      public void handleOnBackPressed() {
        FragmentManagerImpl.this.handleOnBackPressed();
      }
    };
  
  private OnBackPressedDispatcher mOnBackPressedDispatcher;
  
  Fragment mParent;
  
  ArrayList<OpGenerator> mPendingActions;
  
  ArrayList<StartEnterTransitionListener> mPostponedTransactions;
  
  Fragment mPrimaryNav;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  boolean mStopped;
  
  ArrayList<Fragment> mTmpAddedFragments;
  
  ArrayList<Boolean> mTmpIsPop;
  
  ArrayList<BackStackRecord> mTmpRecords;
  
  static {
    DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5F);
  }
  
  private void addAddedFragments(ArraySet<Fragment> paramArraySet) {
    int i = this.mCurState;
    if (i < 1)
      return; 
    int k = Math.min(i, 3);
    int j = this.mAdded.size();
    for (i = 0; i < j; i++) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment.mState < k) {
        moveToState(fragment, k, fragment.getNextAnim(), fragment.getNextTransition(), false);
        if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded)
          paramArraySet.add(fragment); 
      } 
    } 
  }
  
  private void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator paramAnimationOrAnimator, int paramInt) {
    EndViewTransitionAnimation endViewTransitionAnimation;
    final View viewToAnimate = fragment.mView;
    final ViewGroup container = fragment.mContainer;
    viewGroup.startViewTransition(view);
    fragment.setStateAfterAnimating(paramInt);
    if (paramAnimationOrAnimator.animation != null) {
      endViewTransitionAnimation = new EndViewTransitionAnimation(paramAnimationOrAnimator.animation, viewGroup, view);
      fragment.setAnimatingAway(fragment.mView);
      endViewTransitionAnimation.setAnimationListener(new Animation.AnimationListener() {
            final FragmentManagerImpl this$0;
            
            final ViewGroup val$container;
            
            final Fragment val$fragment;
            
            public void onAnimationEnd(Animation param1Animation) {
              container.post(new Runnable() {
                    final FragmentManagerImpl.null this$1;
                    
                    public void run() {
                      if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                      } 
                    }
                  });
            }
            
            public void onAnimationRepeat(Animation param1Animation) {}
            
            public void onAnimationStart(Animation param1Animation) {}
          });
      fragment.mView.startAnimation((Animation)endViewTransitionAnimation);
    } else {
      Animator animator = ((AnimationOrAnimator)endViewTransitionAnimation).animator;
      fragment.setAnimator(((AnimationOrAnimator)endViewTransitionAnimation).animator);
      animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final FragmentManagerImpl this$0;
            
            final ViewGroup val$container;
            
            final Fragment val$fragment;
            
            final View val$viewToAnimate;
            
            public void onAnimationEnd(Animator param1Animator) {
              container.endViewTransition(viewToAnimate);
              param1Animator = fragment.getAnimator();
              fragment.setAnimator(null);
              if (param1Animator != null && container.indexOfChild(viewToAnimate) < 0) {
                FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
                Fragment fragment = fragment;
                fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
              } 
            }
          });
      animator.setTarget(fragment.mView);
      animator.start();
    } 
  }
  
  private void burpActive() {
    this.mActive.values().removeAll(Collections.singleton(null));
  }
  
  private void checkStateLoss() {
    if (!isStateSaved())
      return; 
    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
  }
  
  private void cleanupExec() {
    this.mExecutingActions = false;
    this.mTmpIsPop.clear();
    this.mTmpRecords.clear();
  }
  
  private void dispatchParentPrimaryNavigationFragmentChanged(Fragment paramFragment) {
    if (paramFragment != null && this.mActive.get(paramFragment.mWho) == paramFragment)
      paramFragment.performPrimaryNavigationFragmentChanged(); 
  }
  
  private void dispatchStateChange(int paramInt) {
    try {
      this.mExecutingActions = true;
      moveToState(paramInt, false);
      this.mExecutingActions = false;
      return;
    } finally {
      this.mExecutingActions = false;
    } 
  }
  
  private void endAnimatingAwayFragments() {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null) {
        if (fragment.getAnimatingAway() != null) {
          int i = fragment.getStateAfterAnimating();
          View view = fragment.getAnimatingAway();
          Animation animation = view.getAnimation();
          if (animation != null) {
            animation.cancel();
            view.clearAnimation();
          } 
          fragment.setAnimatingAway(null);
          moveToState(fragment, i, 0, 0, false);
          continue;
        } 
        if (fragment.getAnimator() != null)
          fragment.getAnimator().end(); 
      } 
    } 
  }
  
  private void ensureExecReady(boolean paramBoolean) {
    if (!this.mExecutingActions) {
      if (this.mHost != null) {
        if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
          if (!paramBoolean)
            checkStateLoss(); 
          if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<BackStackRecord>();
            this.mTmpIsPop = new ArrayList<Boolean>();
          } 
          this.mExecutingActions = true;
          try {
            executePostponedTransaction((ArrayList<BackStackRecord>)null, (ArrayList<Boolean>)null);
            return;
          } finally {
            this.mExecutingActions = false;
          } 
        } 
        throw new IllegalStateException("Must be called from main thread of fragment host");
      } 
      throw new IllegalStateException("Fragment host has been destroyed");
    } 
    throw new IllegalStateException("FragmentManager is already executing transactions");
  }
  
  private static void executeOps(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(paramInt1);
      boolean bool1 = ((Boolean)paramArrayList1.get(paramInt1)).booleanValue();
      boolean bool = true;
      if (bool1) {
        backStackRecord.bumpBackStackNesting(-1);
        if (paramInt1 != paramInt2 - 1)
          bool = false; 
        backStackRecord.executePopOps(bool);
      } else {
        backStackRecord.bumpBackStackNesting(1);
        backStackRecord.executeOps();
      } 
      paramInt1++;
    } 
  }
  
  private void executeOpsTogether(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2) {
    int i = paramInt1;
    boolean bool1 = ((BackStackRecord)paramArrayList.get(i)).mReorderingAllowed;
    ArrayList<Fragment> arrayList = this.mTmpAddedFragments;
    if (arrayList == null) {
      this.mTmpAddedFragments = new ArrayList<Fragment>();
    } else {
      arrayList.clear();
    } 
    this.mTmpAddedFragments.addAll(this.mAdded);
    Fragment fragment = getPrimaryNavigationFragment();
    boolean bool = false;
    int j;
    for (j = i; j < paramInt2; j++) {
      BackStackRecord backStackRecord = paramArrayList.get(j);
      if (!((Boolean)paramArrayList1.get(j)).booleanValue()) {
        fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
      } else {
        fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
      } 
      if (bool || backStackRecord.mAddToBackStack) {
        bool = true;
      } else {
        bool = false;
      } 
    } 
    this.mTmpAddedFragments.clear();
    if (!bool1)
      FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, paramInt2, false); 
    executeOps(paramArrayList, paramArrayList1, paramInt1, paramInt2);
    if (bool1) {
      ArraySet<Fragment> arraySet = new ArraySet();
      addAddedFragments(arraySet);
      j = postponePostponableTransactions(paramArrayList, paramArrayList1, paramInt1, paramInt2, arraySet);
      makeRemovedFragmentsInvisible(arraySet);
    } else {
      j = paramInt2;
    } 
    int k = i;
    if (j != i) {
      k = i;
      if (bool1) {
        FragmentTransition.startTransitions(this, paramArrayList, paramArrayList1, paramInt1, j, true);
        moveToState(this.mCurState, true);
        k = i;
      } 
    } 
    while (k < paramInt2) {
      BackStackRecord backStackRecord = paramArrayList.get(k);
      if (((Boolean)paramArrayList1.get(k)).booleanValue() && backStackRecord.mIndex >= 0) {
        freeBackStackIndex(backStackRecord.mIndex);
        backStackRecord.mIndex = -1;
      } 
      backStackRecord.runOnCommitRunnables();
      k++;
    } 
    if (bool)
      reportBackStackChanged(); 
  }
  
  private void executePostponedTransaction(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnonnull -> 16
    //   11: iconst_0
    //   12: istore_3
    //   13: goto -> 22
    //   16: aload #7
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: iconst_0
    //   23: istore #4
    //   25: iload_3
    //   26: istore #6
    //   28: iload #4
    //   30: iload #6
    //   32: if_icmpge -> 252
    //   35: aload_0
    //   36: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   39: iload #4
    //   41: invokevirtual get : (I)Ljava/lang/Object;
    //   44: checkcast androidx/fragment/app/FragmentManagerImpl$StartEnterTransitionListener
    //   47: astore #7
    //   49: aload_1
    //   50: ifnull -> 119
    //   53: aload #7
    //   55: getfield mIsBack : Z
    //   58: ifne -> 119
    //   61: aload_1
    //   62: aload #7
    //   64: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   67: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   70: istore_3
    //   71: iload_3
    //   72: iconst_m1
    //   73: if_icmpeq -> 119
    //   76: aload_2
    //   77: iload_3
    //   78: invokevirtual get : (I)Ljava/lang/Object;
    //   81: checkcast java/lang/Boolean
    //   84: invokevirtual booleanValue : ()Z
    //   87: ifeq -> 119
    //   90: aload_0
    //   91: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   94: iload #4
    //   96: invokevirtual remove : (I)Ljava/lang/Object;
    //   99: pop
    //   100: iload #4
    //   102: iconst_1
    //   103: isub
    //   104: istore #5
    //   106: iload #6
    //   108: iconst_1
    //   109: isub
    //   110: istore_3
    //   111: aload #7
    //   113: invokevirtual cancelTransaction : ()V
    //   116: goto -> 240
    //   119: aload #7
    //   121: invokevirtual isReady : ()Z
    //   124: ifne -> 162
    //   127: iload #6
    //   129: istore_3
    //   130: iload #4
    //   132: istore #5
    //   134: aload_1
    //   135: ifnull -> 240
    //   138: iload #6
    //   140: istore_3
    //   141: iload #4
    //   143: istore #5
    //   145: aload #7
    //   147: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   150: aload_1
    //   151: iconst_0
    //   152: aload_1
    //   153: invokevirtual size : ()I
    //   156: invokevirtual interactsWith : (Ljava/util/ArrayList;II)Z
    //   159: ifeq -> 240
    //   162: aload_0
    //   163: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   166: iload #4
    //   168: invokevirtual remove : (I)Ljava/lang/Object;
    //   171: pop
    //   172: iload #4
    //   174: iconst_1
    //   175: isub
    //   176: istore #5
    //   178: iload #6
    //   180: iconst_1
    //   181: isub
    //   182: istore_3
    //   183: aload_1
    //   184: ifnull -> 235
    //   187: aload #7
    //   189: getfield mIsBack : Z
    //   192: ifne -> 235
    //   195: aload_1
    //   196: aload #7
    //   198: getfield mRecord : Landroidx/fragment/app/BackStackRecord;
    //   201: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   204: istore #4
    //   206: iload #4
    //   208: iconst_m1
    //   209: if_icmpeq -> 235
    //   212: aload_2
    //   213: iload #4
    //   215: invokevirtual get : (I)Ljava/lang/Object;
    //   218: checkcast java/lang/Boolean
    //   221: invokevirtual booleanValue : ()Z
    //   224: ifeq -> 235
    //   227: aload #7
    //   229: invokevirtual cancelTransaction : ()V
    //   232: goto -> 240
    //   235: aload #7
    //   237: invokevirtual completeTransaction : ()V
    //   240: iload #5
    //   242: iconst_1
    //   243: iadd
    //   244: istore #4
    //   246: iload_3
    //   247: istore #6
    //   249: goto -> 28
    //   252: return
  }
  
  private Fragment findFragmentUnder(Fragment paramFragment) {
    ViewGroup viewGroup = paramFragment.mContainer;
    View view = paramFragment.mView;
    if (viewGroup != null && view != null)
      for (int i = this.mAdded.indexOf(paramFragment) - 1; i >= 0; i--) {
        paramFragment = this.mAdded.get(i);
        if (paramFragment.mContainer == viewGroup && paramFragment.mView != null)
          return paramFragment; 
      }  
    return null;
  }
  
  private void forcePostponedTransactions() {
    if (this.mPostponedTransactions != null)
      while (!this.mPostponedTransactions.isEmpty())
        ((StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();  
  }
  
  private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPendingActions : Ljava/util/ArrayList;
    //   6: astore #6
    //   8: iconst_0
    //   9: istore_3
    //   10: aload #6
    //   12: ifnull -> 101
    //   15: aload_0
    //   16: getfield mPendingActions : Ljava/util/ArrayList;
    //   19: invokevirtual size : ()I
    //   22: ifne -> 28
    //   25: goto -> 101
    //   28: aload_0
    //   29: getfield mPendingActions : Ljava/util/ArrayList;
    //   32: invokevirtual size : ()I
    //   35: istore #4
    //   37: iconst_0
    //   38: istore #5
    //   40: iload_3
    //   41: iload #4
    //   43: if_icmpge -> 75
    //   46: iload #5
    //   48: aload_0
    //   49: getfield mPendingActions : Ljava/util/ArrayList;
    //   52: iload_3
    //   53: invokevirtual get : (I)Ljava/lang/Object;
    //   56: checkcast androidx/fragment/app/FragmentManagerImpl$OpGenerator
    //   59: aload_1
    //   60: aload_2
    //   61: invokeinterface generateOps : (Ljava/util/ArrayList;Ljava/util/ArrayList;)Z
    //   66: ior
    //   67: istore #5
    //   69: iinc #3, 1
    //   72: goto -> 40
    //   75: aload_0
    //   76: getfield mPendingActions : Ljava/util/ArrayList;
    //   79: invokevirtual clear : ()V
    //   82: aload_0
    //   83: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   86: invokevirtual getHandler : ()Landroid/os/Handler;
    //   89: aload_0
    //   90: getfield mExecCommit : Ljava/lang/Runnable;
    //   93: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   96: aload_0
    //   97: monitorexit
    //   98: iload #5
    //   100: ireturn
    //   101: aload_0
    //   102: monitorexit
    //   103: iconst_0
    //   104: ireturn
    //   105: astore_1
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_1
    //   109: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	105	finally
    //   15	25	105	finally
    //   28	37	105	finally
    //   46	69	105	finally
    //   75	98	105	finally
    //   101	103	105	finally
    //   106	108	105	finally
  }
  
  private boolean isMenuAvailable(Fragment paramFragment) {
    boolean bool;
    if ((paramFragment.mHasMenu && paramFragment.mMenuVisible) || paramFragment.mChildFragmentManager.checkForMenus()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static AnimationOrAnimator makeFadeAnimation(float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return new AnimationOrAnimator((Animation)alphaAnimation);
  }
  
  static AnimationOrAnimator makeOpenCloseAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)alphaAnimation);
    return new AnimationOrAnimator((Animation)animationSet);
  }
  
  private void makeRemovedFragmentsInvisible(ArraySet<Fragment> paramArraySet) {
    int i = paramArraySet.size();
    for (byte b = 0; b < i; b++) {
      Fragment fragment = (Fragment)paramArraySet.valueAt(b);
      if (!fragment.mAdded) {
        View view = fragment.requireView();
        fragment.mPostponedAlpha = view.getAlpha();
        view.setAlpha(0.0F);
      } 
    } 
  }
  
  private boolean popBackStackImmediate(String paramString, int paramInt1, int paramInt2) {
    execPendingActions();
    ensureExecReady(true);
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null && paramInt1 < 0 && paramString == null && fragment.getChildFragmentManager().popBackStackImmediate())
      return true; 
    boolean bool = popBackStackState(this.mTmpRecords, this.mTmpIsPop, paramString, paramInt1, paramInt2);
    if (bool) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  private int postponePostponableTransactions(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, int paramInt1, int paramInt2, ArraySet<Fragment> paramArraySet) {
    int i = paramInt2 - 1;
    int j;
    for (j = paramInt2; i >= paramInt1; j = k) {
      boolean bool;
      BackStackRecord backStackRecord = paramArrayList.get(i);
      boolean bool1 = ((Boolean)paramArrayList1.get(i)).booleanValue();
      if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(paramArrayList, i + 1, paramInt2)) {
        bool = true;
      } else {
        bool = false;
      } 
      int k = j;
      if (bool) {
        if (this.mPostponedTransactions == null)
          this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>(); 
        StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bool1);
        this.mPostponedTransactions.add(startEnterTransitionListener);
        backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
        if (bool1) {
          backStackRecord.executeOps();
        } else {
          backStackRecord.executePopOps(false);
        } 
        k = j - 1;
        if (i != k) {
          paramArrayList.remove(i);
          paramArrayList.add(k, backStackRecord);
        } 
        addAddedFragments(paramArraySet);
      } 
      i--;
    } 
    return j;
  }
  
  private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1) {
    if (paramArrayList == null || paramArrayList.isEmpty())
      return; 
    if (paramArrayList1 != null && paramArrayList.size() == paramArrayList1.size()) {
      executePostponedTransaction(paramArrayList, paramArrayList1);
      int k = paramArrayList.size();
      int i = 0;
      int j;
      for (j = 0; i < k; j = m) {
        int n = i;
        int m = j;
        if (!((BackStackRecord)paramArrayList.get(i)).mReorderingAllowed) {
          if (j != i)
            executeOpsTogether(paramArrayList, paramArrayList1, j, i); 
          j = i + 1;
          m = j;
          if (((Boolean)paramArrayList1.get(i)).booleanValue())
            while (true) {
              m = j;
              if (j < k) {
                m = j;
                if (((Boolean)paramArrayList1.get(j)).booleanValue()) {
                  m = j;
                  if (!((BackStackRecord)paramArrayList.get(j)).mReorderingAllowed) {
                    j++;
                    continue;
                  } 
                } 
              } 
              break;
            }  
          executeOpsTogether(paramArrayList, paramArrayList1, i, m);
          n = m - 1;
        } 
        i = n + 1;
      } 
      if (j != k)
        executeOpsTogether(paramArrayList, paramArrayList1, j, k); 
      return;
    } 
    throw new IllegalStateException("Internal error with the back stack records");
  }
  
  public static int reverseTransit(int paramInt) {
    char c = ' ';
    if (paramInt != 4097)
      if (paramInt != 4099) {
        if (paramInt != 8194) {
          c = Character.MIN_VALUE;
        } else {
          c = 'ခ';
        } 
      } else {
        c = 'ဃ';
      }  
    return c;
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
    FragmentHostCallback fragmentHostCallback = this.mHost;
    if (fragmentHostCallback != null) {
      try {
        fragmentHostCallback.onDump("  ", (FileDescriptor)null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
    } else {
      try {
        dump("  ", (FileDescriptor)null, (PrintWriter)exception, new String[0]);
      } catch (Exception exception1) {
        Log.e("FragmentManager", "Failed dumping state", exception1);
      } 
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    if (paramInt != 4097) {
      if (paramInt != 4099) {
        if (paramInt != 8194) {
          paramInt = -1;
        } else if (paramBoolean) {
          paramInt = 3;
        } else {
          paramInt = 4;
        } 
      } else if (paramBoolean) {
        paramInt = 5;
      } else {
        paramInt = 6;
      } 
    } else if (paramBoolean) {
      paramInt = 1;
    } else {
      paramInt = 2;
    } 
    return paramInt;
  }
  
  private void updateOnBackPressedCallbackEnabled() {
    ArrayList<OpGenerator> arrayList = this.mPendingActions;
    boolean bool = true;
    if (arrayList != null && !arrayList.isEmpty()) {
      this.mOnBackPressedCallback.setEnabled(true);
      return;
    } 
    OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
    if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent))
      bool = false; 
    onBackPressedCallback.setEnabled(bool);
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<BackStackRecord>(); 
    this.mBackStack.add(paramBackStackRecord);
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("add: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    makeActive(paramFragment);
    if (!paramFragment.mDetached)
      if (!this.mAdded.contains(paramFragment)) {
        synchronized (this.mAdded) {
          this.mAdded.add(paramFragment);
          paramFragment.mAdded = true;
          paramFragment.mRemoving = false;
          if (paramFragment.mView == null)
            paramFragment.mHiddenChanged = false; 
          if (isMenuAvailable(paramFragment))
            this.mNeedMenuInvalidate = true; 
          if (paramBoolean)
            moveToState(paramFragment); 
        } 
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fragment already added: ");
        stringBuilder.append(paramFragment);
        throw new IllegalStateException(stringBuilder.toString());
      }  
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<FragmentManager.OnBackStackChangedListener>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  void addRetainedFragment(Fragment paramFragment) {
    if (isStateSaved()) {
      if (DEBUG)
        Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mNonConfig.addRetainedFragment(paramFragment) && DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Updating retained Fragments: Added ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 111
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 22
    //   19: goto -> 111
    //   22: aload_0
    //   23: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   26: aload_0
    //   27: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: invokevirtual size : ()I
    //   33: iconst_1
    //   34: isub
    //   35: invokevirtual remove : (I)Ljava/lang/Object;
    //   38: checkcast java/lang/Integer
    //   41: invokevirtual intValue : ()I
    //   44: istore_2
    //   45: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   48: ifeq -> 97
    //   51: new java/lang/StringBuilder
    //   54: astore_3
    //   55: aload_3
    //   56: invokespecial <init> : ()V
    //   59: aload_3
    //   60: ldc_w 'Adding back stack index '
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_3
    //   68: iload_2
    //   69: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_3
    //   74: ldc_w ' with '
    //   77: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_3
    //   82: aload_1
    //   83: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: ldc 'FragmentManager'
    //   89: aload_3
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   101: iload_2
    //   102: aload_1
    //   103: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   106: pop
    //   107: aload_0
    //   108: monitorexit
    //   109: iload_2
    //   110: ireturn
    //   111: aload_0
    //   112: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   115: ifnonnull -> 131
    //   118: new java/util/ArrayList
    //   121: astore_3
    //   122: aload_3
    //   123: invokespecial <init> : ()V
    //   126: aload_0
    //   127: aload_3
    //   128: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   131: aload_0
    //   132: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   135: invokevirtual size : ()I
    //   138: istore_2
    //   139: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   142: ifeq -> 191
    //   145: new java/lang/StringBuilder
    //   148: astore_3
    //   149: aload_3
    //   150: invokespecial <init> : ()V
    //   153: aload_3
    //   154: ldc_w 'Setting back stack index '
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: pop
    //   161: aload_3
    //   162: iload_2
    //   163: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   166: pop
    //   167: aload_3
    //   168: ldc_w ' to '
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_3
    //   176: aload_1
    //   177: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: ldc 'FragmentManager'
    //   183: aload_3
    //   184: invokevirtual toString : ()Ljava/lang/String;
    //   187: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   190: pop
    //   191: aload_0
    //   192: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   195: aload_1
    //   196: invokevirtual add : (Ljava/lang/Object;)Z
    //   199: pop
    //   200: aload_0
    //   201: monitorexit
    //   202: iload_2
    //   203: ireturn
    //   204: astore_1
    //   205: aload_0
    //   206: monitorexit
    //   207: aload_1
    //   208: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	204	finally
    //   22	97	204	finally
    //   97	109	204	finally
    //   111	131	204	finally
    //   131	191	204	finally
    //   191	202	204	finally
    //   205	207	204	finally
  }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost == null) {
      this.mHost = paramFragmentHostCallback;
      this.mContainer = paramFragmentContainer;
      this.mParent = paramFragment;
      if (paramFragment != null)
        updateOnBackPressedCallbackEnabled(); 
      if (paramFragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
        Fragment fragment;
        OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner)paramFragmentHostCallback;
        this.mOnBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
        if (paramFragment != null)
          fragment = paramFragment; 
        this.mOnBackPressedDispatcher.addCallback(fragment, this.mOnBackPressedCallback);
      } 
      if (paramFragment != null) {
        this.mNonConfig = paramFragment.mFragmentManager.getChildNonConfig(paramFragment);
      } else if (paramFragmentHostCallback instanceof ViewModelStoreOwner) {
        this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)paramFragmentHostCallback).getViewModelStore());
      } else {
        this.mNonConfig = new FragmentManagerViewModel(false);
      } 
      return;
    } 
    throw new IllegalStateException("Already attached");
  }
  
  public void attachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("attach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded)
        if (!this.mAdded.contains(paramFragment)) {
          if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add from attach: ");
            stringBuilder.append(paramFragment);
            Log.v("FragmentManager", stringBuilder.toString());
          } 
          synchronized (this.mAdded) {
            this.mAdded.add(paramFragment);
            paramFragment.mAdded = true;
            if (isMenuAvailable(paramFragment))
              this.mNeedMenuInvalidate = true; 
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Fragment already added: ");
          stringBuilder.append(paramFragment);
          throw new IllegalStateException(stringBuilder.toString());
        }  
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  boolean checkForMenus() {
    Iterator<Fragment> iterator = this.mActive.values().iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      Fragment fragment = iterator.next();
      boolean bool1 = bool;
      if (fragment != null)
        bool1 = isMenuAvailable(fragment); 
      bool = bool1;
      if (bool1)
        return true; 
    } 
    return false;
  }
  
  void completeExecute(BackStackRecord paramBackStackRecord, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    if (paramBoolean1) {
      paramBackStackRecord.executePopOps(paramBoolean3);
    } else {
      paramBackStackRecord.executeOps();
    } 
    ArrayList<BackStackRecord> arrayList1 = new ArrayList(1);
    ArrayList<Boolean> arrayList = new ArrayList(1);
    arrayList1.add(paramBackStackRecord);
    arrayList.add(Boolean.valueOf(paramBoolean1));
    if (paramBoolean2)
      FragmentTransition.startTransitions(this, arrayList1, arrayList, 0, 1, true); 
    if (paramBoolean3)
      moveToState(this.mCurState, true); 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && paramBackStackRecord.interactsWith(fragment.mContainerId)) {
        if (fragment.mPostponedAlpha > 0.0F)
          fragment.mView.setAlpha(fragment.mPostponedAlpha); 
        if (paramBoolean3) {
          fragment.mPostponedAlpha = 0.0F;
          continue;
        } 
        fragment.mPostponedAlpha = -1.0F;
        fragment.mIsNewlyAdded = false;
      } 
    } 
  }
  
  void completeShowHideFragment(final Fragment fragment) {
    if (fragment.mView != null) {
      AnimationOrAnimator animationOrAnimator = loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
      if (animationOrAnimator != null && animationOrAnimator.animator != null) {
        animationOrAnimator.animator.setTarget(fragment.mView);
        if (fragment.mHidden) {
          if (fragment.isHideReplaced()) {
            fragment.setHideReplaced(false);
          } else {
            final ViewGroup container = fragment.mContainer;
            final View animatingView = fragment.mView;
            viewGroup.startViewTransition(view);
            animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
                  final FragmentManagerImpl this$0;
                  
                  final View val$animatingView;
                  
                  final ViewGroup val$container;
                  
                  final Fragment val$fragment;
                  
                  public void onAnimationEnd(Animator param1Animator) {
                    container.endViewTransition(animatingView);
                    param1Animator.removeListener((Animator.AnimatorListener)this);
                    if (fragment.mView != null && fragment.mHidden)
                      fragment.mView.setVisibility(8); 
                  }
                });
          } 
        } else {
          fragment.mView.setVisibility(0);
        } 
        animationOrAnimator.animator.start();
      } else {
        boolean bool;
        if (animationOrAnimator != null) {
          fragment.mView.startAnimation(animationOrAnimator.animation);
          animationOrAnimator.animation.start();
        } 
        if (fragment.mHidden && !fragment.isHideReplaced()) {
          bool = true;
        } else {
          bool = false;
        } 
        fragment.mView.setVisibility(bool);
        if (fragment.isHideReplaced())
          fragment.setHideReplaced(false); 
      } 
    } 
    if (fragment.mAdded && isMenuAvailable(fragment))
      this.mNeedMenuInvalidate = true; 
    fragment.mHiddenChanged = false;
    fragment.onHiddenChanged(fragment.mHidden);
  }
  
  public void detachFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("detach: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (DEBUG) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("remove from detach: ");
          stringBuilder.append(paramFragment);
          Log.v("FragmentManager", stringBuilder.toString());
        } 
        synchronized (this.mAdded) {
          this.mAdded.remove(paramFragment);
          if (isMenuAvailable(paramFragment))
            this.mNeedMenuInvalidate = true; 
          paramFragment.mAdded = false;
        } 
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(2);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performConfigurationChanged(paramConfiguration); 
    } 
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(1);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    int i = this.mCurState;
    boolean bool1 = false;
    if (i < 1)
      return false; 
    ArrayList<Fragment> arrayList = null;
    i = 0;
    boolean bool2;
    for (bool2 = false; i < this.mAdded.size(); bool2 = bool) {
      Fragment fragment = this.mAdded.get(i);
      ArrayList<Fragment> arrayList1 = arrayList;
      boolean bool = bool2;
      if (fragment != null) {
        arrayList1 = arrayList;
        bool = bool2;
        if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
          arrayList1 = arrayList;
          if (arrayList == null)
            arrayList1 = new ArrayList(); 
          arrayList1.add(fragment);
          bool = true;
        } 
      } 
      i++;
      arrayList = arrayList1;
    } 
    if (this.mCreatedMenus != null)
      for (i = bool1; i < this.mCreatedMenus.size(); i++) {
        Fragment fragment = this.mCreatedMenus.get(i);
        if (arrayList == null || !arrayList.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList;
    return bool2;
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    dispatchStateChange(0);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
    if (this.mOnBackPressedDispatcher != null) {
      this.mOnBackPressedCallback.remove();
      this.mOnBackPressedDispatcher = null;
    } 
  }
  
  public void dispatchDestroyView() {
    dispatchStateChange(1);
  }
  
  public void dispatchLowMemory() {
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performLowMemory(); 
    } 
  }
  
  public void dispatchMultiWindowModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performMultiWindowModeChanged(paramBoolean); 
    } 
  }
  
  void dispatchOnFragmentActivityCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentDetached(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPaused(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentPreAttached(Fragment paramFragment, Context paramContext, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(paramFragment, paramContext, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, paramFragment, paramContext); 
    } 
  }
  
  void dispatchOnFragmentPreCreated(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentResumed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentSaveInstanceState(Fragment paramFragment, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(paramFragment, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, paramFragment, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentStarted(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentStopped(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, paramFragment); 
    } 
  }
  
  void dispatchOnFragmentViewCreated(Fragment paramFragment, View paramView, Bundle paramBundle, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(paramFragment, paramView, paramBundle, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, paramFragment, paramView, paramBundle); 
    } 
  }
  
  void dispatchOnFragmentViewDestroyed(Fragment paramFragment, boolean paramBoolean) {
    Fragment fragment = this.mParent;
    if (fragment != null) {
      FragmentManager fragmentManager = fragment.getFragmentManager();
      if (fragmentManager instanceof FragmentManagerImpl)
        ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(paramFragment, true); 
    } 
    for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
      if (!paramBoolean || fragmentLifecycleCallbacksHolder.mRecursive)
        fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, paramFragment); 
    } 
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mCurState < 1)
      return false; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
        return true; 
    } 
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mCurState < 1)
      return; 
    for (byte b = 0; b < this.mAdded.size(); b++) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.performOptionsMenuClosed(paramMenu); 
    } 
  }
  
  public void dispatchPause() {
    dispatchStateChange(3);
  }
  
  public void dispatchPictureInPictureModeChanged(boolean paramBoolean) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null)
        fragment.performPictureInPictureModeChanged(paramBoolean); 
    } 
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    int i = this.mCurState;
    byte b = 0;
    if (i < 1)
      return false; 
    boolean bool;
    for (bool = false; b < this.mAdded.size(); bool = bool1) {
      Fragment fragment = this.mAdded.get(b);
      boolean bool1 = bool;
      if (fragment != null) {
        bool1 = bool;
        if (fragment.performPrepareOptionsMenu(paramMenu))
          bool1 = true; 
      } 
      b++;
    } 
    return bool;
  }
  
  void dispatchPrimaryNavigationFragmentChanged() {
    updateOnBackPressedCallbackEnabled();
    dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(4);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    this.mStopped = false;
    dispatchStateChange(3);
  }
  
  public void dispatchStop() {
    this.mStopped = true;
    dispatchStateChange(2);
  }
  
  void doPendingDeferredStart() {
    if (this.mHavePendingDeferredStart) {
      this.mHavePendingDeferredStart = false;
      startPendingDeferredFragments();
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #8
    //   9: aload #8
    //   11: aload_1
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload #8
    //   18: ldc_w '    '
    //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload #8
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: astore #8
    //   32: aload_0
    //   33: getfield mActive : Ljava/util/HashMap;
    //   36: invokevirtual isEmpty : ()Z
    //   39: ifne -> 138
    //   42: aload_3
    //   43: aload_1
    //   44: invokevirtual print : (Ljava/lang/String;)V
    //   47: aload_3
    //   48: ldc_w 'Active Fragments in '
    //   51: invokevirtual print : (Ljava/lang/String;)V
    //   54: aload_3
    //   55: aload_0
    //   56: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   59: invokestatic toHexString : (I)Ljava/lang/String;
    //   62: invokevirtual print : (Ljava/lang/String;)V
    //   65: aload_3
    //   66: ldc_w ':'
    //   69: invokevirtual println : (Ljava/lang/String;)V
    //   72: aload_0
    //   73: getfield mActive : Ljava/util/HashMap;
    //   76: invokevirtual values : ()Ljava/util/Collection;
    //   79: invokeinterface iterator : ()Ljava/util/Iterator;
    //   84: astore #9
    //   86: aload #9
    //   88: invokeinterface hasNext : ()Z
    //   93: ifeq -> 138
    //   96: aload #9
    //   98: invokeinterface next : ()Ljava/lang/Object;
    //   103: checkcast androidx/fragment/app/Fragment
    //   106: astore #10
    //   108: aload_3
    //   109: aload_1
    //   110: invokevirtual print : (Ljava/lang/String;)V
    //   113: aload_3
    //   114: aload #10
    //   116: invokevirtual println : (Ljava/lang/Object;)V
    //   119: aload #10
    //   121: ifnull -> 86
    //   124: aload #10
    //   126: aload #8
    //   128: aload_2
    //   129: aload_3
    //   130: aload #4
    //   132: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   135: goto -> 86
    //   138: aload_0
    //   139: getfield mAdded : Ljava/util/ArrayList;
    //   142: invokevirtual size : ()I
    //   145: istore #7
    //   147: iconst_0
    //   148: istore #6
    //   150: iload #7
    //   152: ifle -> 229
    //   155: aload_3
    //   156: aload_1
    //   157: invokevirtual print : (Ljava/lang/String;)V
    //   160: aload_3
    //   161: ldc_w 'Added Fragments:'
    //   164: invokevirtual println : (Ljava/lang/String;)V
    //   167: iconst_0
    //   168: istore #5
    //   170: iload #5
    //   172: iload #7
    //   174: if_icmpge -> 229
    //   177: aload_0
    //   178: getfield mAdded : Ljava/util/ArrayList;
    //   181: iload #5
    //   183: invokevirtual get : (I)Ljava/lang/Object;
    //   186: checkcast androidx/fragment/app/Fragment
    //   189: astore_2
    //   190: aload_3
    //   191: aload_1
    //   192: invokevirtual print : (Ljava/lang/String;)V
    //   195: aload_3
    //   196: ldc_w '  #'
    //   199: invokevirtual print : (Ljava/lang/String;)V
    //   202: aload_3
    //   203: iload #5
    //   205: invokevirtual print : (I)V
    //   208: aload_3
    //   209: ldc_w ': '
    //   212: invokevirtual print : (Ljava/lang/String;)V
    //   215: aload_3
    //   216: aload_2
    //   217: invokevirtual toString : ()Ljava/lang/String;
    //   220: invokevirtual println : (Ljava/lang/String;)V
    //   223: iinc #5, 1
    //   226: goto -> 170
    //   229: aload_0
    //   230: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   233: astore_2
    //   234: aload_2
    //   235: ifnull -> 323
    //   238: aload_2
    //   239: invokevirtual size : ()I
    //   242: istore #7
    //   244: iload #7
    //   246: ifle -> 323
    //   249: aload_3
    //   250: aload_1
    //   251: invokevirtual print : (Ljava/lang/String;)V
    //   254: aload_3
    //   255: ldc_w 'Fragments Created Menus:'
    //   258: invokevirtual println : (Ljava/lang/String;)V
    //   261: iconst_0
    //   262: istore #5
    //   264: iload #5
    //   266: iload #7
    //   268: if_icmpge -> 323
    //   271: aload_0
    //   272: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   275: iload #5
    //   277: invokevirtual get : (I)Ljava/lang/Object;
    //   280: checkcast androidx/fragment/app/Fragment
    //   283: astore_2
    //   284: aload_3
    //   285: aload_1
    //   286: invokevirtual print : (Ljava/lang/String;)V
    //   289: aload_3
    //   290: ldc_w '  #'
    //   293: invokevirtual print : (Ljava/lang/String;)V
    //   296: aload_3
    //   297: iload #5
    //   299: invokevirtual print : (I)V
    //   302: aload_3
    //   303: ldc_w ': '
    //   306: invokevirtual print : (Ljava/lang/String;)V
    //   309: aload_3
    //   310: aload_2
    //   311: invokevirtual toString : ()Ljava/lang/String;
    //   314: invokevirtual println : (Ljava/lang/String;)V
    //   317: iinc #5, 1
    //   320: goto -> 264
    //   323: aload_0
    //   324: getfield mBackStack : Ljava/util/ArrayList;
    //   327: astore_2
    //   328: aload_2
    //   329: ifnull -> 424
    //   332: aload_2
    //   333: invokevirtual size : ()I
    //   336: istore #7
    //   338: iload #7
    //   340: ifle -> 424
    //   343: aload_3
    //   344: aload_1
    //   345: invokevirtual print : (Ljava/lang/String;)V
    //   348: aload_3
    //   349: ldc_w 'Back Stack:'
    //   352: invokevirtual println : (Ljava/lang/String;)V
    //   355: iconst_0
    //   356: istore #5
    //   358: iload #5
    //   360: iload #7
    //   362: if_icmpge -> 424
    //   365: aload_0
    //   366: getfield mBackStack : Ljava/util/ArrayList;
    //   369: iload #5
    //   371: invokevirtual get : (I)Ljava/lang/Object;
    //   374: checkcast androidx/fragment/app/BackStackRecord
    //   377: astore_2
    //   378: aload_3
    //   379: aload_1
    //   380: invokevirtual print : (Ljava/lang/String;)V
    //   383: aload_3
    //   384: ldc_w '  #'
    //   387: invokevirtual print : (Ljava/lang/String;)V
    //   390: aload_3
    //   391: iload #5
    //   393: invokevirtual print : (I)V
    //   396: aload_3
    //   397: ldc_w ': '
    //   400: invokevirtual print : (Ljava/lang/String;)V
    //   403: aload_3
    //   404: aload_2
    //   405: invokevirtual toString : ()Ljava/lang/String;
    //   408: invokevirtual println : (Ljava/lang/String;)V
    //   411: aload_2
    //   412: aload #8
    //   414: aload_3
    //   415: invokevirtual dump : (Ljava/lang/String;Ljava/io/PrintWriter;)V
    //   418: iinc #5, 1
    //   421: goto -> 358
    //   424: aload_0
    //   425: monitorenter
    //   426: aload_0
    //   427: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   430: ifnull -> 518
    //   433: aload_0
    //   434: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   437: invokevirtual size : ()I
    //   440: istore #7
    //   442: iload #7
    //   444: ifle -> 518
    //   447: aload_3
    //   448: aload_1
    //   449: invokevirtual print : (Ljava/lang/String;)V
    //   452: aload_3
    //   453: ldc_w 'Back Stack Indices:'
    //   456: invokevirtual println : (Ljava/lang/String;)V
    //   459: iconst_0
    //   460: istore #5
    //   462: iload #5
    //   464: iload #7
    //   466: if_icmpge -> 518
    //   469: aload_0
    //   470: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   473: iload #5
    //   475: invokevirtual get : (I)Ljava/lang/Object;
    //   478: checkcast androidx/fragment/app/BackStackRecord
    //   481: astore_2
    //   482: aload_3
    //   483: aload_1
    //   484: invokevirtual print : (Ljava/lang/String;)V
    //   487: aload_3
    //   488: ldc_w '  #'
    //   491: invokevirtual print : (Ljava/lang/String;)V
    //   494: aload_3
    //   495: iload #5
    //   497: invokevirtual print : (I)V
    //   500: aload_3
    //   501: ldc_w ': '
    //   504: invokevirtual print : (Ljava/lang/String;)V
    //   507: aload_3
    //   508: aload_2
    //   509: invokevirtual println : (Ljava/lang/Object;)V
    //   512: iinc #5, 1
    //   515: goto -> 462
    //   518: aload_0
    //   519: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   522: ifnull -> 561
    //   525: aload_0
    //   526: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   529: invokevirtual size : ()I
    //   532: ifle -> 561
    //   535: aload_3
    //   536: aload_1
    //   537: invokevirtual print : (Ljava/lang/String;)V
    //   540: aload_3
    //   541: ldc_w 'mAvailBackStackIndices: '
    //   544: invokevirtual print : (Ljava/lang/String;)V
    //   547: aload_3
    //   548: aload_0
    //   549: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   552: invokevirtual toArray : ()[Ljava/lang/Object;
    //   555: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   558: invokevirtual println : (Ljava/lang/String;)V
    //   561: aload_0
    //   562: monitorexit
    //   563: aload_0
    //   564: getfield mPendingActions : Ljava/util/ArrayList;
    //   567: astore_2
    //   568: aload_2
    //   569: ifnull -> 655
    //   572: aload_2
    //   573: invokevirtual size : ()I
    //   576: istore #7
    //   578: iload #7
    //   580: ifle -> 655
    //   583: aload_3
    //   584: aload_1
    //   585: invokevirtual print : (Ljava/lang/String;)V
    //   588: aload_3
    //   589: ldc_w 'Pending Actions:'
    //   592: invokevirtual println : (Ljava/lang/String;)V
    //   595: iload #6
    //   597: istore #5
    //   599: iload #5
    //   601: iload #7
    //   603: if_icmpge -> 655
    //   606: aload_0
    //   607: getfield mPendingActions : Ljava/util/ArrayList;
    //   610: iload #5
    //   612: invokevirtual get : (I)Ljava/lang/Object;
    //   615: checkcast androidx/fragment/app/FragmentManagerImpl$OpGenerator
    //   618: astore_2
    //   619: aload_3
    //   620: aload_1
    //   621: invokevirtual print : (Ljava/lang/String;)V
    //   624: aload_3
    //   625: ldc_w '  #'
    //   628: invokevirtual print : (Ljava/lang/String;)V
    //   631: aload_3
    //   632: iload #5
    //   634: invokevirtual print : (I)V
    //   637: aload_3
    //   638: ldc_w ': '
    //   641: invokevirtual print : (Ljava/lang/String;)V
    //   644: aload_3
    //   645: aload_2
    //   646: invokevirtual println : (Ljava/lang/Object;)V
    //   649: iinc #5, 1
    //   652: goto -> 599
    //   655: aload_3
    //   656: aload_1
    //   657: invokevirtual print : (Ljava/lang/String;)V
    //   660: aload_3
    //   661: ldc_w 'FragmentManager misc state:'
    //   664: invokevirtual println : (Ljava/lang/String;)V
    //   667: aload_3
    //   668: aload_1
    //   669: invokevirtual print : (Ljava/lang/String;)V
    //   672: aload_3
    //   673: ldc_w '  mHost='
    //   676: invokevirtual print : (Ljava/lang/String;)V
    //   679: aload_3
    //   680: aload_0
    //   681: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   684: invokevirtual println : (Ljava/lang/Object;)V
    //   687: aload_3
    //   688: aload_1
    //   689: invokevirtual print : (Ljava/lang/String;)V
    //   692: aload_3
    //   693: ldc_w '  mContainer='
    //   696: invokevirtual print : (Ljava/lang/String;)V
    //   699: aload_3
    //   700: aload_0
    //   701: getfield mContainer : Landroidx/fragment/app/FragmentContainer;
    //   704: invokevirtual println : (Ljava/lang/Object;)V
    //   707: aload_0
    //   708: getfield mParent : Landroidx/fragment/app/Fragment;
    //   711: ifnull -> 734
    //   714: aload_3
    //   715: aload_1
    //   716: invokevirtual print : (Ljava/lang/String;)V
    //   719: aload_3
    //   720: ldc_w '  mParent='
    //   723: invokevirtual print : (Ljava/lang/String;)V
    //   726: aload_3
    //   727: aload_0
    //   728: getfield mParent : Landroidx/fragment/app/Fragment;
    //   731: invokevirtual println : (Ljava/lang/Object;)V
    //   734: aload_3
    //   735: aload_1
    //   736: invokevirtual print : (Ljava/lang/String;)V
    //   739: aload_3
    //   740: ldc_w '  mCurState='
    //   743: invokevirtual print : (Ljava/lang/String;)V
    //   746: aload_3
    //   747: aload_0
    //   748: getfield mCurState : I
    //   751: invokevirtual print : (I)V
    //   754: aload_3
    //   755: ldc_w ' mStateSaved='
    //   758: invokevirtual print : (Ljava/lang/String;)V
    //   761: aload_3
    //   762: aload_0
    //   763: getfield mStateSaved : Z
    //   766: invokevirtual print : (Z)V
    //   769: aload_3
    //   770: ldc_w ' mStopped='
    //   773: invokevirtual print : (Ljava/lang/String;)V
    //   776: aload_3
    //   777: aload_0
    //   778: getfield mStopped : Z
    //   781: invokevirtual print : (Z)V
    //   784: aload_3
    //   785: ldc_w ' mDestroyed='
    //   788: invokevirtual print : (Ljava/lang/String;)V
    //   791: aload_3
    //   792: aload_0
    //   793: getfield mDestroyed : Z
    //   796: invokevirtual println : (Z)V
    //   799: aload_0
    //   800: getfield mNeedMenuInvalidate : Z
    //   803: ifeq -> 826
    //   806: aload_3
    //   807: aload_1
    //   808: invokevirtual print : (Ljava/lang/String;)V
    //   811: aload_3
    //   812: ldc_w '  mNeedMenuInvalidate='
    //   815: invokevirtual print : (Ljava/lang/String;)V
    //   818: aload_3
    //   819: aload_0
    //   820: getfield mNeedMenuInvalidate : Z
    //   823: invokevirtual println : (Z)V
    //   826: return
    //   827: astore_1
    //   828: aload_0
    //   829: monitorexit
    //   830: aload_1
    //   831: athrow
    // Exception table:
    //   from	to	target	type
    //   426	442	827	finally
    //   447	459	827	finally
    //   469	512	827	finally
    //   518	561	827	finally
    //   561	563	827	finally
    //   828	830	827	finally
  }
  
  public void enqueueAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 63
    //   17: aload_0
    //   18: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   21: ifnonnull -> 27
    //   24: goto -> 63
    //   27: aload_0
    //   28: getfield mPendingActions : Ljava/util/ArrayList;
    //   31: ifnonnull -> 47
    //   34: new java/util/ArrayList
    //   37: astore_3
    //   38: aload_3
    //   39: invokespecial <init> : ()V
    //   42: aload_0
    //   43: aload_3
    //   44: putfield mPendingActions : Ljava/util/ArrayList;
    //   47: aload_0
    //   48: getfield mPendingActions : Ljava/util/ArrayList;
    //   51: aload_1
    //   52: invokevirtual add : (Ljava/lang/Object;)Z
    //   55: pop
    //   56: aload_0
    //   57: invokevirtual scheduleCommit : ()V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: iload_2
    //   64: ifeq -> 70
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: new java/lang/IllegalStateException
    //   73: astore_1
    //   74: aload_1
    //   75: ldc_w 'Activity has been destroyed'
    //   78: invokespecial <init> : (Ljava/lang/String;)V
    //   81: aload_1
    //   82: athrow
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   10	24	83	finally
    //   27	47	83	finally
    //   47	62	83	finally
    //   67	69	83	finally
    //   70	83	83	finally
    //   84	86	83	finally
  }
  
  void ensureInflatedFragmentView(Fragment paramFragment) {
    if (paramFragment.mFromLayout && !paramFragment.mPerformedCreateView) {
      paramFragment.performCreateView(paramFragment.performGetLayoutInflater(paramFragment.mSavedFragmentState), null, paramFragment.mSavedFragmentState);
      if (paramFragment.mView != null) {
        paramFragment.mInnerView = paramFragment.mView;
        paramFragment.mView.setSaveFromParentEnabled(false);
        if (paramFragment.mHidden)
          paramFragment.mView.setVisibility(8); 
        paramFragment.onViewCreated(paramFragment.mView, paramFragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(paramFragment, paramFragment.mView, paramFragment.mSavedFragmentState, false);
      } else {
        paramFragment.mInnerView = null;
      } 
    } 
  }
  
  public boolean execPendingActions() {
    ensureExecReady(true);
    boolean bool = false;
    while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
        cleanupExec();
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
    return bool;
  }
  
  public void execSingleAction(OpGenerator paramOpGenerator, boolean paramBoolean) {
    if (paramBoolean && (this.mHost == null || this.mDestroyed))
      return; 
    ensureExecReady(paramBoolean);
    if (paramOpGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
      this.mExecutingActions = true;
      try {
        removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
      } finally {
        cleanupExec();
      } 
    } 
    updateOnBackPressedCallbackEnabled();
    doPendingDeferredStart();
    burpActive();
  }
  
  public boolean executePendingTransactions() {
    boolean bool = execPendingActions();
    forcePostponedTransactions();
    return bool;
  }
  
  public Fragment findFragmentById(int paramInt) {
    for (int i = this.mAdded.size() - 1; i >= 0; i--) {
      Fragment fragment = this.mAdded.get(i);
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && fragment.mFragmentId == paramInt)
        return fragment; 
    } 
    return null;
  }
  
  public Fragment findFragmentByTag(String paramString) {
    if (paramString != null)
      for (int i = this.mAdded.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    if (paramString != null)
      for (Fragment fragment : this.mActive.values()) {
        if (fragment != null && paramString.equals(fragment.mTag))
          return fragment; 
      }  
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString) {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null) {
        fragment = fragment.findFragmentByWho(paramString);
        if (fragment != null)
          return fragment; 
      } 
    } 
    return null;
  }
  
  public void freeBackStackIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 32
    //   19: new java/util/ArrayList
    //   22: astore_2
    //   23: aload_2
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_2
    //   29: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   32: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   35: ifeq -> 70
    //   38: new java/lang/StringBuilder
    //   41: astore_2
    //   42: aload_2
    //   43: invokespecial <init> : ()V
    //   46: aload_2
    //   47: ldc_w 'Freeing back stack index '
    //   50: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_2
    //   55: iload_1
    //   56: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: ldc 'FragmentManager'
    //   62: aload_2
    //   63: invokevirtual toString : ()Ljava/lang/String;
    //   66: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   74: iload_1
    //   75: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   78: invokevirtual add : (Ljava/lang/Object;)Z
    //   81: pop
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: astore_2
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	32	85	finally
    //   32	70	85	finally
    //   70	84	85	finally
    //   86	88	85	finally
  }
  
  int getActiveFragmentCount() {
    return this.mActive.size();
  }
  
  List<Fragment> getActiveFragments() {
    return new ArrayList<Fragment>(this.mActive.values());
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    boolean bool;
    ArrayList<BackStackRecord> arrayList = this.mBackStack;
    if (arrayList != null) {
      bool = arrayList.size();
    } else {
      bool = false;
    } 
    return bool;
  }
  
  FragmentManagerViewModel getChildNonConfig(Fragment paramFragment) {
    return this.mNonConfig.getChildNonConfig(paramFragment);
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    String str = paramBundle.getString(paramString);
    if (str == null)
      return null; 
    Fragment fragment = this.mActive.get(str);
    if (fragment == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment no longer exists for key ");
      stringBuilder.append(paramString);
      stringBuilder.append(": unique id ");
      stringBuilder.append(str);
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    return fragment;
  }
  
  public FragmentFactory getFragmentFactory() {
    if (super.getFragmentFactory() == DEFAULT_FACTORY) {
      Fragment fragment = this.mParent;
      if (fragment != null)
        return fragment.mFragmentManager.getFragmentFactory(); 
      setFragmentFactory(new FragmentFactory() {
            final FragmentManagerImpl this$0;
            
            public Fragment instantiate(ClassLoader param1ClassLoader, String param1String) {
              return FragmentManagerImpl.this.mHost.instantiate(FragmentManagerImpl.this.mHost.getContext(), param1String, null);
            }
          });
    } 
    return super.getFragmentFactory();
  }
  
  public List<Fragment> getFragments() {
    if (this.mAdded.isEmpty())
      return Collections.emptyList(); 
    synchronized (this.mAdded) {
      return (List)this.mAdded.clone();
    } 
  }
  
  LayoutInflater.Factory2 getLayoutInflaterFactory() {
    return this;
  }
  
  public Fragment getPrimaryNavigationFragment() {
    return this.mPrimaryNav;
  }
  
  ViewModelStore getViewModelStore(Fragment paramFragment) {
    return this.mNonConfig.getViewModelStore(paramFragment);
  }
  
  void handleOnBackPressed() {
    execPendingActions();
    if (this.mOnBackPressedCallback.isEnabled()) {
      popBackStackImmediate();
    } else {
      this.mOnBackPressedDispatcher.onBackPressed();
    } 
  }
  
  public void hideFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("hide: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      paramFragment.mHiddenChanged = true ^ paramFragment.mHiddenChanged;
    } 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  boolean isPrimaryNavigation(Fragment paramFragment) {
    boolean bool = true;
    if (paramFragment == null)
      return true; 
    FragmentManagerImpl fragmentManagerImpl = paramFragment.mFragmentManager;
    if (paramFragment != fragmentManagerImpl.getPrimaryNavigationFragment() || !isPrimaryNavigation(fragmentManagerImpl.mParent))
      bool = false; 
    return bool;
  }
  
  boolean isStateAtLeast(int paramInt) {
    boolean bool;
    if (this.mCurState >= paramInt) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStateSaved() {
    return (this.mStateSaved || this.mStopped);
  }
  
  AnimationOrAnimator loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    int i = paramFragment.getNextAnim();
    boolean bool = false;
    paramFragment.setNextAnim(0);
    if (paramFragment.mContainer != null && paramFragment.mContainer.getLayoutTransition() != null)
      return null; 
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, i);
    if (animation != null)
      return new AnimationOrAnimator(animation); 
    Animator animator = paramFragment.onCreateAnimator(paramInt1, paramBoolean, i);
    if (animator != null)
      return new AnimationOrAnimator(animator); 
    if (i != 0) {
      boolean bool2 = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(i));
      boolean bool1 = bool;
      if (bool2)
        try {
          Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
          if (animation1 != null)
            return new AnimationOrAnimator(animation1); 
          bool1 = true;
        } catch (android.content.res.Resources.NotFoundException notFoundException) {
          throw notFoundException;
        } catch (RuntimeException runtimeException) {
          bool1 = bool;
        }  
      if (!bool1)
        try {
          animator = AnimatorInflater.loadAnimator(this.mHost.getContext(), i);
          if (animator != null)
            return new AnimationOrAnimator(animator); 
        } catch (RuntimeException runtimeException) {
          Animation animation1;
          if (!bool2) {
            animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), i);
            if (animation1 != null)
              return new AnimationOrAnimator(animation1); 
          } else {
            throw animation1;
          } 
        }  
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        break;
      case 6:
        return makeFadeAnimation(1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(1.0F, 1.075F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(0.975F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(1.0F, 0.975F, 1.0F, 0.0F);
      case 1:
        return makeOpenCloseAnimation(1.125F, 1.0F, 0.0F, 1.0F);
    } 
    if (paramInt1 == 0);
    return null;
  }
  
  void makeActive(Fragment paramFragment) {
    if (this.mActive.get(paramFragment.mWho) != null)
      return; 
    this.mActive.put(paramFragment.mWho, paramFragment);
    if (paramFragment.mRetainInstanceChangedWhileDetached) {
      if (paramFragment.mRetainInstance) {
        addRetainedFragment(paramFragment);
      } else {
        removeRetainedFragment(paramFragment);
      } 
      paramFragment.mRetainInstanceChangedWhileDetached = false;
    } 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Added fragment to active set ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (this.mActive.get(paramFragment.mWho) == null)
      return; 
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Removed fragment from active set ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null && paramFragment.mWho.equals(fragment.mTargetWho)) {
        fragment.mTarget = paramFragment;
        fragment.mTargetWho = null;
      } 
    } 
    this.mActive.put(paramFragment.mWho, null);
    removeRetainedFragment(paramFragment);
    if (paramFragment.mTargetWho != null)
      paramFragment.mTarget = this.mActive.get(paramFragment.mTargetWho); 
    paramFragment.initState();
  }
  
  void moveFragmentToExpectedState(Fragment paramFragment) {
    if (paramFragment == null)
      return; 
    if (!this.mActive.containsKey(paramFragment.mWho)) {
      if (DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ignoring moving ");
        stringBuilder.append(paramFragment);
        stringBuilder.append(" to state ");
        stringBuilder.append(this.mCurState);
        stringBuilder.append("since it is not added to ");
        stringBuilder.append(this);
        Log.v("FragmentManager", stringBuilder.toString());
      } 
      return;
    } 
    int j = this.mCurState;
    int i = j;
    if (paramFragment.mRemoving)
      if (paramFragment.isInBackStack()) {
        i = Math.min(j, 1);
      } else {
        i = Math.min(j, 0);
      }  
    moveToState(paramFragment, i, paramFragment.getNextTransition(), paramFragment.getNextTransitionStyle(), false);
    if (paramFragment.mView != null) {
      Fragment fragment = findFragmentUnder(paramFragment);
      if (fragment != null) {
        View view = fragment.mView;
        ViewGroup viewGroup = paramFragment.mContainer;
        i = viewGroup.indexOfChild(view);
        j = viewGroup.indexOfChild(paramFragment.mView);
        if (j < i) {
          viewGroup.removeViewAt(j);
          viewGroup.addView(paramFragment.mView, i);
        } 
      } 
      if (paramFragment.mIsNewlyAdded && paramFragment.mContainer != null) {
        if (paramFragment.mPostponedAlpha > 0.0F)
          paramFragment.mView.setAlpha(paramFragment.mPostponedAlpha); 
        paramFragment.mPostponedAlpha = 0.0F;
        paramFragment.mIsNewlyAdded = false;
        AnimationOrAnimator animationOrAnimator = loadAnimation(paramFragment, paramFragment.getNextTransition(), true, paramFragment.getNextTransitionStyle());
        if (animationOrAnimator != null)
          if (animationOrAnimator.animation != null) {
            paramFragment.mView.startAnimation(animationOrAnimator.animation);
          } else {
            animationOrAnimator.animator.setTarget(paramFragment.mView);
            animationOrAnimator.animator.start();
          }  
      } 
    } 
    if (paramFragment.mHiddenChanged)
      completeShowHideFragment(paramFragment); 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    if (this.mHost != null || paramInt == 0) {
      if (!paramBoolean && paramInt == this.mCurState)
        return; 
      this.mCurState = paramInt;
      int i = this.mAdded.size();
      for (paramInt = 0; paramInt < i; paramInt++)
        moveFragmentToExpectedState(this.mAdded.get(paramInt)); 
      for (Fragment fragment : this.mActive.values()) {
        if (fragment != null && (fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded)
          moveFragmentToExpectedState(fragment); 
      } 
      startPendingDeferredFragments();
      if (this.mNeedMenuInvalidate) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null && this.mCurState == 4) {
          fragmentHostCallback.onSupportInvalidateOptionsMenu();
          this.mNeedMenuInvalidate = false;
        } 
      } 
      return;
    } 
    throw new IllegalStateException("No activity");
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mAdded : Z
    //   4: istore #10
    //   6: iconst_1
    //   7: istore #9
    //   9: iconst_1
    //   10: istore #7
    //   12: iconst_1
    //   13: istore #8
    //   15: iload #10
    //   17: ifeq -> 33
    //   20: aload_1
    //   21: getfield mDetached : Z
    //   24: ifeq -> 30
    //   27: goto -> 33
    //   30: goto -> 47
    //   33: iload_2
    //   34: istore #6
    //   36: iload #6
    //   38: istore_2
    //   39: iload #6
    //   41: iconst_1
    //   42: if_icmple -> 47
    //   45: iconst_1
    //   46: istore_2
    //   47: iload_2
    //   48: istore #6
    //   50: aload_1
    //   51: getfield mRemoving : Z
    //   54: ifeq -> 94
    //   57: iload_2
    //   58: istore #6
    //   60: iload_2
    //   61: aload_1
    //   62: getfield mState : I
    //   65: if_icmple -> 94
    //   68: aload_1
    //   69: getfield mState : I
    //   72: ifne -> 88
    //   75: aload_1
    //   76: invokevirtual isInBackStack : ()Z
    //   79: ifeq -> 88
    //   82: iconst_1
    //   83: istore #6
    //   85: goto -> 94
    //   88: aload_1
    //   89: getfield mState : I
    //   92: istore #6
    //   94: iload #6
    //   96: istore_2
    //   97: aload_1
    //   98: getfield mDeferStart : Z
    //   101: ifeq -> 126
    //   104: iload #6
    //   106: istore_2
    //   107: aload_1
    //   108: getfield mState : I
    //   111: iconst_3
    //   112: if_icmpge -> 126
    //   115: iload #6
    //   117: istore_2
    //   118: iload #6
    //   120: iconst_2
    //   121: if_icmple -> 126
    //   124: iconst_2
    //   125: istore_2
    //   126: aload_1
    //   127: getfield mMaxState : Landroidx/lifecycle/Lifecycle$State;
    //   130: getstatic androidx/lifecycle/Lifecycle$State.CREATED : Landroidx/lifecycle/Lifecycle$State;
    //   133: if_acmpne -> 145
    //   136: iload_2
    //   137: iconst_1
    //   138: invokestatic min : (II)I
    //   141: istore_2
    //   142: goto -> 157
    //   145: iload_2
    //   146: aload_1
    //   147: getfield mMaxState : Landroidx/lifecycle/Lifecycle$State;
    //   150: invokevirtual ordinal : ()I
    //   153: invokestatic min : (II)I
    //   156: istore_2
    //   157: aload_1
    //   158: getfield mState : I
    //   161: iload_2
    //   162: if_icmpgt -> 1474
    //   165: aload_1
    //   166: getfield mFromLayout : Z
    //   169: ifeq -> 180
    //   172: aload_1
    //   173: getfield mInLayout : Z
    //   176: ifne -> 180
    //   179: return
    //   180: aload_1
    //   181: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   184: ifnonnull -> 194
    //   187: aload_1
    //   188: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   191: ifnull -> 216
    //   194: aload_1
    //   195: aconst_null
    //   196: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   199: aload_1
    //   200: aconst_null
    //   201: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   204: aload_0
    //   205: aload_1
    //   206: aload_1
    //   207: invokevirtual getStateAfterAnimating : ()I
    //   210: iconst_0
    //   211: iconst_0
    //   212: iconst_1
    //   213: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   216: aload_1
    //   217: getfield mState : I
    //   220: istore #4
    //   222: iload #4
    //   224: ifeq -> 259
    //   227: iload_2
    //   228: istore_3
    //   229: iload #4
    //   231: iconst_1
    //   232: if_icmpeq -> 880
    //   235: iload #4
    //   237: iconst_2
    //   238: if_icmpeq -> 256
    //   241: iload #4
    //   243: iconst_3
    //   244: if_icmpeq -> 253
    //   247: iload_2
    //   248: istore #6
    //   250: goto -> 2274
    //   253: goto -> 1398
    //   256: goto -> 1341
    //   259: iload_2
    //   260: istore_3
    //   261: iload_2
    //   262: ifle -> 880
    //   265: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   268: ifeq -> 307
    //   271: new java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial <init> : ()V
    //   278: astore #11
    //   280: aload #11
    //   282: ldc_w 'moveto CREATED: '
    //   285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   288: pop
    //   289: aload #11
    //   291: aload_1
    //   292: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: ldc 'FragmentManager'
    //   298: aload #11
    //   300: invokevirtual toString : ()Ljava/lang/String;
    //   303: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   306: pop
    //   307: iload_2
    //   308: istore_3
    //   309: aload_1
    //   310: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   313: ifnull -> 466
    //   316: aload_1
    //   317: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   320: aload_0
    //   321: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   324: invokevirtual getContext : ()Landroid/content/Context;
    //   327: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   330: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   333: aload_1
    //   334: aload_1
    //   335: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   338: ldc 'android:view_state'
    //   340: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   343: putfield mSavedViewState : Landroid/util/SparseArray;
    //   346: aload_0
    //   347: aload_1
    //   348: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   351: ldc 'android:target_state'
    //   353: invokevirtual getFragment : (Landroid/os/Bundle;Ljava/lang/String;)Landroidx/fragment/app/Fragment;
    //   356: astore #11
    //   358: aload #11
    //   360: ifnull -> 373
    //   363: aload #11
    //   365: getfield mWho : Ljava/lang/String;
    //   368: astore #11
    //   370: goto -> 376
    //   373: aconst_null
    //   374: astore #11
    //   376: aload_1
    //   377: aload #11
    //   379: putfield mTargetWho : Ljava/lang/String;
    //   382: aload_1
    //   383: getfield mTargetWho : Ljava/lang/String;
    //   386: ifnull -> 403
    //   389: aload_1
    //   390: aload_1
    //   391: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   394: ldc 'android:target_req_state'
    //   396: iconst_0
    //   397: invokevirtual getInt : (Ljava/lang/String;I)I
    //   400: putfield mTargetRequestCode : I
    //   403: aload_1
    //   404: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   407: ifnull -> 429
    //   410: aload_1
    //   411: aload_1
    //   412: getfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   415: invokevirtual booleanValue : ()Z
    //   418: putfield mUserVisibleHint : Z
    //   421: aload_1
    //   422: aconst_null
    //   423: putfield mSavedUserVisibleHint : Ljava/lang/Boolean;
    //   426: goto -> 443
    //   429: aload_1
    //   430: aload_1
    //   431: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   434: ldc 'android:user_visible_hint'
    //   436: iconst_1
    //   437: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   440: putfield mUserVisibleHint : Z
    //   443: iload_2
    //   444: istore_3
    //   445: aload_1
    //   446: getfield mUserVisibleHint : Z
    //   449: ifne -> 466
    //   452: aload_1
    //   453: iconst_1
    //   454: putfield mDeferStart : Z
    //   457: iload_2
    //   458: istore_3
    //   459: iload_2
    //   460: iconst_2
    //   461: if_icmple -> 466
    //   464: iconst_2
    //   465: istore_3
    //   466: aload_1
    //   467: aload_0
    //   468: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   471: putfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   474: aload_1
    //   475: aload_0
    //   476: getfield mParent : Landroidx/fragment/app/Fragment;
    //   479: putfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   482: aload_0
    //   483: getfield mParent : Landroidx/fragment/app/Fragment;
    //   486: astore #11
    //   488: aload #11
    //   490: ifnull -> 503
    //   493: aload #11
    //   495: getfield mChildFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   498: astore #11
    //   500: goto -> 512
    //   503: aload_0
    //   504: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   507: getfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   510: astore #11
    //   512: aload_1
    //   513: aload #11
    //   515: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   518: aload_1
    //   519: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   522: ifnull -> 657
    //   525: aload_0
    //   526: getfield mActive : Ljava/util/HashMap;
    //   529: aload_1
    //   530: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   533: getfield mWho : Ljava/lang/String;
    //   536: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   539: aload_1
    //   540: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   543: if_acmpne -> 591
    //   546: aload_1
    //   547: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   550: getfield mState : I
    //   553: iconst_1
    //   554: if_icmpge -> 572
    //   557: aload_0
    //   558: aload_1
    //   559: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   562: iconst_1
    //   563: iconst_0
    //   564: iconst_0
    //   565: iconst_1
    //   566: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   569: goto -> 572
    //   572: aload_1
    //   573: aload_1
    //   574: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   577: getfield mWho : Ljava/lang/String;
    //   580: putfield mTargetWho : Ljava/lang/String;
    //   583: aload_1
    //   584: aconst_null
    //   585: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   588: goto -> 657
    //   591: new java/lang/StringBuilder
    //   594: dup
    //   595: invokespecial <init> : ()V
    //   598: astore #11
    //   600: aload #11
    //   602: ldc_w 'Fragment '
    //   605: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: pop
    //   609: aload #11
    //   611: aload_1
    //   612: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   615: pop
    //   616: aload #11
    //   618: ldc_w ' declared target fragment '
    //   621: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   624: pop
    //   625: aload #11
    //   627: aload_1
    //   628: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   631: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   634: pop
    //   635: aload #11
    //   637: ldc_w ' that does not belong to this FragmentManager!'
    //   640: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: pop
    //   644: new java/lang/IllegalStateException
    //   647: dup
    //   648: aload #11
    //   650: invokevirtual toString : ()Ljava/lang/String;
    //   653: invokespecial <init> : (Ljava/lang/String;)V
    //   656: athrow
    //   657: aload_1
    //   658: getfield mTargetWho : Ljava/lang/String;
    //   661: ifnull -> 773
    //   664: aload_0
    //   665: getfield mActive : Ljava/util/HashMap;
    //   668: aload_1
    //   669: getfield mTargetWho : Ljava/lang/String;
    //   672: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   675: checkcast androidx/fragment/app/Fragment
    //   678: astore #11
    //   680: aload #11
    //   682: ifnull -> 707
    //   685: aload #11
    //   687: getfield mState : I
    //   690: iconst_1
    //   691: if_icmpge -> 773
    //   694: aload_0
    //   695: aload #11
    //   697: iconst_1
    //   698: iconst_0
    //   699: iconst_0
    //   700: iconst_1
    //   701: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   704: goto -> 773
    //   707: new java/lang/StringBuilder
    //   710: dup
    //   711: invokespecial <init> : ()V
    //   714: astore #11
    //   716: aload #11
    //   718: ldc_w 'Fragment '
    //   721: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   724: pop
    //   725: aload #11
    //   727: aload_1
    //   728: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   731: pop
    //   732: aload #11
    //   734: ldc_w ' declared target fragment '
    //   737: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   740: pop
    //   741: aload #11
    //   743: aload_1
    //   744: getfield mTargetWho : Ljava/lang/String;
    //   747: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   750: pop
    //   751: aload #11
    //   753: ldc_w ' that does not belong to this FragmentManager!'
    //   756: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   759: pop
    //   760: new java/lang/IllegalStateException
    //   763: dup
    //   764: aload #11
    //   766: invokevirtual toString : ()Ljava/lang/String;
    //   769: invokespecial <init> : (Ljava/lang/String;)V
    //   772: athrow
    //   773: aload_0
    //   774: aload_1
    //   775: aload_0
    //   776: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   779: invokevirtual getContext : ()Landroid/content/Context;
    //   782: iconst_0
    //   783: invokevirtual dispatchOnFragmentPreAttached : (Landroidx/fragment/app/Fragment;Landroid/content/Context;Z)V
    //   786: aload_1
    //   787: invokevirtual performAttach : ()V
    //   790: aload_1
    //   791: getfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   794: ifnonnull -> 808
    //   797: aload_0
    //   798: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   801: aload_1
    //   802: invokevirtual onAttachFragment : (Landroidx/fragment/app/Fragment;)V
    //   805: goto -> 816
    //   808: aload_1
    //   809: getfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   812: aload_1
    //   813: invokevirtual onAttachFragment : (Landroidx/fragment/app/Fragment;)V
    //   816: aload_0
    //   817: aload_1
    //   818: aload_0
    //   819: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   822: invokevirtual getContext : ()Landroid/content/Context;
    //   825: iconst_0
    //   826: invokevirtual dispatchOnFragmentAttached : (Landroidx/fragment/app/Fragment;Landroid/content/Context;Z)V
    //   829: aload_1
    //   830: getfield mIsCreated : Z
    //   833: ifne -> 867
    //   836: aload_0
    //   837: aload_1
    //   838: aload_1
    //   839: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   842: iconst_0
    //   843: invokevirtual dispatchOnFragmentPreCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   846: aload_1
    //   847: aload_1
    //   848: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   851: invokevirtual performCreate : (Landroid/os/Bundle;)V
    //   854: aload_0
    //   855: aload_1
    //   856: aload_1
    //   857: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   860: iconst_0
    //   861: invokevirtual dispatchOnFragmentCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   864: goto -> 880
    //   867: aload_1
    //   868: aload_1
    //   869: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   872: invokevirtual restoreChildFragmentState : (Landroid/os/Bundle;)V
    //   875: aload_1
    //   876: iconst_1
    //   877: putfield mState : I
    //   880: iload_3
    //   881: ifle -> 889
    //   884: aload_0
    //   885: aload_1
    //   886: invokevirtual ensureInflatedFragmentView : (Landroidx/fragment/app/Fragment;)V
    //   889: iload_3
    //   890: iconst_1
    //   891: if_icmple -> 1339
    //   894: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   897: ifeq -> 936
    //   900: new java/lang/StringBuilder
    //   903: dup
    //   904: invokespecial <init> : ()V
    //   907: astore #11
    //   909: aload #11
    //   911: ldc_w 'moveto ACTIVITY_CREATED: '
    //   914: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   917: pop
    //   918: aload #11
    //   920: aload_1
    //   921: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   924: pop
    //   925: ldc 'FragmentManager'
    //   927: aload #11
    //   929: invokevirtual toString : ()Ljava/lang/String;
    //   932: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   935: pop
    //   936: aload_1
    //   937: getfield mFromLayout : Z
    //   940: ifne -> 1301
    //   943: aload_1
    //   944: getfield mContainerId : I
    //   947: ifeq -> 1154
    //   950: aload_1
    //   951: getfield mContainerId : I
    //   954: iconst_m1
    //   955: if_icmpne -> 1008
    //   958: new java/lang/StringBuilder
    //   961: dup
    //   962: invokespecial <init> : ()V
    //   965: astore #11
    //   967: aload #11
    //   969: ldc_w 'Cannot create fragment '
    //   972: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   975: pop
    //   976: aload #11
    //   978: aload_1
    //   979: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   982: pop
    //   983: aload #11
    //   985: ldc_w ' for a container view with no id'
    //   988: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   991: pop
    //   992: aload_0
    //   993: new java/lang/IllegalArgumentException
    //   996: dup
    //   997: aload #11
    //   999: invokevirtual toString : ()Ljava/lang/String;
    //   1002: invokespecial <init> : (Ljava/lang/String;)V
    //   1005: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   1008: aload_0
    //   1009: getfield mContainer : Landroidx/fragment/app/FragmentContainer;
    //   1012: aload_1
    //   1013: getfield mContainerId : I
    //   1016: invokevirtual onFindViewById : (I)Landroid/view/View;
    //   1019: checkcast android/view/ViewGroup
    //   1022: astore #12
    //   1024: aload #12
    //   1026: astore #11
    //   1028: aload #12
    //   1030: ifnonnull -> 1157
    //   1033: aload #12
    //   1035: astore #11
    //   1037: aload_1
    //   1038: getfield mRestored : Z
    //   1041: ifne -> 1157
    //   1044: aload_1
    //   1045: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   1048: aload_1
    //   1049: getfield mContainerId : I
    //   1052: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   1055: astore #11
    //   1057: goto -> 1067
    //   1060: astore #11
    //   1062: ldc_w 'unknown'
    //   1065: astore #11
    //   1067: new java/lang/StringBuilder
    //   1070: dup
    //   1071: invokespecial <init> : ()V
    //   1074: astore #13
    //   1076: aload #13
    //   1078: ldc_w 'No view found for id 0x'
    //   1081: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1084: pop
    //   1085: aload #13
    //   1087: aload_1
    //   1088: getfield mContainerId : I
    //   1091: invokestatic toHexString : (I)Ljava/lang/String;
    //   1094: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1097: pop
    //   1098: aload #13
    //   1100: ldc_w ' ('
    //   1103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1106: pop
    //   1107: aload #13
    //   1109: aload #11
    //   1111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1114: pop
    //   1115: aload #13
    //   1117: ldc_w ') for fragment '
    //   1120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1123: pop
    //   1124: aload #13
    //   1126: aload_1
    //   1127: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1130: pop
    //   1131: aload_0
    //   1132: new java/lang/IllegalArgumentException
    //   1135: dup
    //   1136: aload #13
    //   1138: invokevirtual toString : ()Ljava/lang/String;
    //   1141: invokespecial <init> : (Ljava/lang/String;)V
    //   1144: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   1147: aload #12
    //   1149: astore #11
    //   1151: goto -> 1157
    //   1154: aconst_null
    //   1155: astore #11
    //   1157: aload_1
    //   1158: aload #11
    //   1160: putfield mContainer : Landroid/view/ViewGroup;
    //   1163: aload_1
    //   1164: aload_1
    //   1165: aload_1
    //   1166: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1169: invokevirtual performGetLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   1172: aload #11
    //   1174: aload_1
    //   1175: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1178: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)V
    //   1181: aload_1
    //   1182: getfield mView : Landroid/view/View;
    //   1185: ifnull -> 1296
    //   1188: aload_1
    //   1189: aload_1
    //   1190: getfield mView : Landroid/view/View;
    //   1193: putfield mInnerView : Landroid/view/View;
    //   1196: aload_1
    //   1197: getfield mView : Landroid/view/View;
    //   1200: iconst_0
    //   1201: invokevirtual setSaveFromParentEnabled : (Z)V
    //   1204: aload #11
    //   1206: ifnull -> 1218
    //   1209: aload #11
    //   1211: aload_1
    //   1212: getfield mView : Landroid/view/View;
    //   1215: invokevirtual addView : (Landroid/view/View;)V
    //   1218: aload_1
    //   1219: getfield mHidden : Z
    //   1222: ifeq -> 1234
    //   1225: aload_1
    //   1226: getfield mView : Landroid/view/View;
    //   1229: bipush #8
    //   1231: invokevirtual setVisibility : (I)V
    //   1234: aload_1
    //   1235: aload_1
    //   1236: getfield mView : Landroid/view/View;
    //   1239: aload_1
    //   1240: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1243: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   1246: aload_0
    //   1247: aload_1
    //   1248: aload_1
    //   1249: getfield mView : Landroid/view/View;
    //   1252: aload_1
    //   1253: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1256: iconst_0
    //   1257: invokevirtual dispatchOnFragmentViewCreated : (Landroidx/fragment/app/Fragment;Landroid/view/View;Landroid/os/Bundle;Z)V
    //   1260: aload_1
    //   1261: getfield mView : Landroid/view/View;
    //   1264: invokevirtual getVisibility : ()I
    //   1267: ifne -> 1284
    //   1270: aload_1
    //   1271: getfield mContainer : Landroid/view/ViewGroup;
    //   1274: ifnull -> 1284
    //   1277: iload #8
    //   1279: istore #5
    //   1281: goto -> 1287
    //   1284: iconst_0
    //   1285: istore #5
    //   1287: aload_1
    //   1288: iload #5
    //   1290: putfield mIsNewlyAdded : Z
    //   1293: goto -> 1301
    //   1296: aload_1
    //   1297: aconst_null
    //   1298: putfield mInnerView : Landroid/view/View;
    //   1301: aload_1
    //   1302: aload_1
    //   1303: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1306: invokevirtual performActivityCreated : (Landroid/os/Bundle;)V
    //   1309: aload_0
    //   1310: aload_1
    //   1311: aload_1
    //   1312: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1315: iconst_0
    //   1316: invokevirtual dispatchOnFragmentActivityCreated : (Landroidx/fragment/app/Fragment;Landroid/os/Bundle;Z)V
    //   1319: aload_1
    //   1320: getfield mView : Landroid/view/View;
    //   1323: ifnull -> 1334
    //   1326: aload_1
    //   1327: aload_1
    //   1328: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   1331: invokevirtual restoreViewState : (Landroid/os/Bundle;)V
    //   1334: aload_1
    //   1335: aconst_null
    //   1336: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1339: iload_3
    //   1340: istore_2
    //   1341: iload_2
    //   1342: iconst_2
    //   1343: if_icmple -> 1398
    //   1346: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1349: ifeq -> 1388
    //   1352: new java/lang/StringBuilder
    //   1355: dup
    //   1356: invokespecial <init> : ()V
    //   1359: astore #11
    //   1361: aload #11
    //   1363: ldc_w 'moveto STARTED: '
    //   1366: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1369: pop
    //   1370: aload #11
    //   1372: aload_1
    //   1373: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1376: pop
    //   1377: ldc 'FragmentManager'
    //   1379: aload #11
    //   1381: invokevirtual toString : ()Ljava/lang/String;
    //   1384: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1387: pop
    //   1388: aload_1
    //   1389: invokevirtual performStart : ()V
    //   1392: aload_0
    //   1393: aload_1
    //   1394: iconst_0
    //   1395: invokevirtual dispatchOnFragmentStarted : (Landroidx/fragment/app/Fragment;Z)V
    //   1398: iload_2
    //   1399: istore #6
    //   1401: iload_2
    //   1402: iconst_3
    //   1403: if_icmple -> 2274
    //   1406: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1409: ifeq -> 1448
    //   1412: new java/lang/StringBuilder
    //   1415: dup
    //   1416: invokespecial <init> : ()V
    //   1419: astore #11
    //   1421: aload #11
    //   1423: ldc_w 'moveto RESUMED: '
    //   1426: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1429: pop
    //   1430: aload #11
    //   1432: aload_1
    //   1433: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1436: pop
    //   1437: ldc 'FragmentManager'
    //   1439: aload #11
    //   1441: invokevirtual toString : ()Ljava/lang/String;
    //   1444: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1447: pop
    //   1448: aload_1
    //   1449: invokevirtual performResume : ()V
    //   1452: aload_0
    //   1453: aload_1
    //   1454: iconst_0
    //   1455: invokevirtual dispatchOnFragmentResumed : (Landroidx/fragment/app/Fragment;Z)V
    //   1458: aload_1
    //   1459: aconst_null
    //   1460: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1463: aload_1
    //   1464: aconst_null
    //   1465: putfield mSavedViewState : Landroid/util/SparseArray;
    //   1468: iload_2
    //   1469: istore #6
    //   1471: goto -> 2274
    //   1474: iload_2
    //   1475: istore #6
    //   1477: aload_1
    //   1478: getfield mState : I
    //   1481: iload_2
    //   1482: if_icmple -> 2274
    //   1485: aload_1
    //   1486: getfield mState : I
    //   1489: istore #6
    //   1491: iload #6
    //   1493: iconst_1
    //   1494: if_icmpeq -> 1889
    //   1497: iload #6
    //   1499: iconst_2
    //   1500: if_icmpeq -> 1641
    //   1503: iload #6
    //   1505: iconst_3
    //   1506: if_icmpeq -> 1581
    //   1509: iload #6
    //   1511: iconst_4
    //   1512: if_icmpeq -> 1521
    //   1515: iload_2
    //   1516: istore #6
    //   1518: goto -> 2274
    //   1521: iload_2
    //   1522: iconst_4
    //   1523: if_icmpge -> 1578
    //   1526: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1529: ifeq -> 1568
    //   1532: new java/lang/StringBuilder
    //   1535: dup
    //   1536: invokespecial <init> : ()V
    //   1539: astore #11
    //   1541: aload #11
    //   1543: ldc_w 'movefrom RESUMED: '
    //   1546: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1549: pop
    //   1550: aload #11
    //   1552: aload_1
    //   1553: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1556: pop
    //   1557: ldc 'FragmentManager'
    //   1559: aload #11
    //   1561: invokevirtual toString : ()Ljava/lang/String;
    //   1564: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1567: pop
    //   1568: aload_1
    //   1569: invokevirtual performPause : ()V
    //   1572: aload_0
    //   1573: aload_1
    //   1574: iconst_0
    //   1575: invokevirtual dispatchOnFragmentPaused : (Landroidx/fragment/app/Fragment;Z)V
    //   1578: goto -> 1581
    //   1581: iload_2
    //   1582: iconst_3
    //   1583: if_icmpge -> 1638
    //   1586: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1589: ifeq -> 1628
    //   1592: new java/lang/StringBuilder
    //   1595: dup
    //   1596: invokespecial <init> : ()V
    //   1599: astore #11
    //   1601: aload #11
    //   1603: ldc_w 'movefrom STARTED: '
    //   1606: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1609: pop
    //   1610: aload #11
    //   1612: aload_1
    //   1613: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1616: pop
    //   1617: ldc 'FragmentManager'
    //   1619: aload #11
    //   1621: invokevirtual toString : ()Ljava/lang/String;
    //   1624: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1627: pop
    //   1628: aload_1
    //   1629: invokevirtual performStop : ()V
    //   1632: aload_0
    //   1633: aload_1
    //   1634: iconst_0
    //   1635: invokevirtual dispatchOnFragmentStopped : (Landroidx/fragment/app/Fragment;Z)V
    //   1638: goto -> 1641
    //   1641: iload_2
    //   1642: iconst_2
    //   1643: if_icmpge -> 1889
    //   1646: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1649: ifeq -> 1688
    //   1652: new java/lang/StringBuilder
    //   1655: dup
    //   1656: invokespecial <init> : ()V
    //   1659: astore #11
    //   1661: aload #11
    //   1663: ldc_w 'movefrom ACTIVITY_CREATED: '
    //   1666: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1669: pop
    //   1670: aload #11
    //   1672: aload_1
    //   1673: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1676: pop
    //   1677: ldc 'FragmentManager'
    //   1679: aload #11
    //   1681: invokevirtual toString : ()Ljava/lang/String;
    //   1684: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1687: pop
    //   1688: aload_1
    //   1689: getfield mView : Landroid/view/View;
    //   1692: ifnull -> 1718
    //   1695: aload_0
    //   1696: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   1699: aload_1
    //   1700: invokevirtual onShouldSaveFragmentState : (Landroidx/fragment/app/Fragment;)Z
    //   1703: ifeq -> 1718
    //   1706: aload_1
    //   1707: getfield mSavedViewState : Landroid/util/SparseArray;
    //   1710: ifnonnull -> 1718
    //   1713: aload_0
    //   1714: aload_1
    //   1715: invokevirtual saveFragmentViewState : (Landroidx/fragment/app/Fragment;)V
    //   1718: aload_1
    //   1719: invokevirtual performDestroyView : ()V
    //   1722: aload_0
    //   1723: aload_1
    //   1724: iconst_0
    //   1725: invokevirtual dispatchOnFragmentViewDestroyed : (Landroidx/fragment/app/Fragment;Z)V
    //   1728: aload_1
    //   1729: getfield mView : Landroid/view/View;
    //   1732: ifnull -> 1856
    //   1735: aload_1
    //   1736: getfield mContainer : Landroid/view/ViewGroup;
    //   1739: ifnull -> 1856
    //   1742: aload_1
    //   1743: getfield mContainer : Landroid/view/ViewGroup;
    //   1746: aload_1
    //   1747: getfield mView : Landroid/view/View;
    //   1750: invokevirtual endViewTransition : (Landroid/view/View;)V
    //   1753: aload_1
    //   1754: getfield mView : Landroid/view/View;
    //   1757: invokevirtual clearAnimation : ()V
    //   1760: aload_1
    //   1761: invokevirtual getParentFragment : ()Landroidx/fragment/app/Fragment;
    //   1764: ifnull -> 1777
    //   1767: aload_1
    //   1768: invokevirtual getParentFragment : ()Landroidx/fragment/app/Fragment;
    //   1771: getfield mRemoving : Z
    //   1774: ifne -> 1856
    //   1777: aload_0
    //   1778: getfield mCurState : I
    //   1781: ifle -> 1824
    //   1784: aload_0
    //   1785: getfield mDestroyed : Z
    //   1788: ifne -> 1824
    //   1791: aload_1
    //   1792: getfield mView : Landroid/view/View;
    //   1795: invokevirtual getVisibility : ()I
    //   1798: ifne -> 1824
    //   1801: aload_1
    //   1802: getfield mPostponedAlpha : F
    //   1805: fconst_0
    //   1806: fcmpl
    //   1807: iflt -> 1824
    //   1810: aload_0
    //   1811: aload_1
    //   1812: iload_3
    //   1813: iconst_0
    //   1814: iload #4
    //   1816: invokevirtual loadAnimation : (Landroidx/fragment/app/Fragment;IZI)Landroidx/fragment/app/FragmentManagerImpl$AnimationOrAnimator;
    //   1819: astore #11
    //   1821: goto -> 1827
    //   1824: aconst_null
    //   1825: astore #11
    //   1827: aload_1
    //   1828: fconst_0
    //   1829: putfield mPostponedAlpha : F
    //   1832: aload #11
    //   1834: ifnull -> 1845
    //   1837: aload_0
    //   1838: aload_1
    //   1839: aload #11
    //   1841: iload_2
    //   1842: invokespecial animateRemoveFragment : (Landroidx/fragment/app/Fragment;Landroidx/fragment/app/FragmentManagerImpl$AnimationOrAnimator;I)V
    //   1845: aload_1
    //   1846: getfield mContainer : Landroid/view/ViewGroup;
    //   1849: aload_1
    //   1850: getfield mView : Landroid/view/View;
    //   1853: invokevirtual removeView : (Landroid/view/View;)V
    //   1856: aload_1
    //   1857: aconst_null
    //   1858: putfield mContainer : Landroid/view/ViewGroup;
    //   1861: aload_1
    //   1862: aconst_null
    //   1863: putfield mView : Landroid/view/View;
    //   1866: aload_1
    //   1867: aconst_null
    //   1868: putfield mViewLifecycleOwner : Landroidx/fragment/app/FragmentViewLifecycleOwner;
    //   1871: aload_1
    //   1872: getfield mViewLifecycleOwnerLiveData : Landroidx/lifecycle/MutableLiveData;
    //   1875: aconst_null
    //   1876: invokevirtual setValue : (Ljava/lang/Object;)V
    //   1879: aload_1
    //   1880: aconst_null
    //   1881: putfield mInnerView : Landroid/view/View;
    //   1884: aload_1
    //   1885: iconst_0
    //   1886: putfield mInLayout : Z
    //   1889: iload_2
    //   1890: istore #6
    //   1892: iload_2
    //   1893: iconst_1
    //   1894: if_icmpge -> 2274
    //   1897: aload_0
    //   1898: getfield mDestroyed : Z
    //   1901: ifeq -> 1953
    //   1904: aload_1
    //   1905: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1908: ifnull -> 1930
    //   1911: aload_1
    //   1912: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1915: astore #11
    //   1917: aload_1
    //   1918: aconst_null
    //   1919: invokevirtual setAnimatingAway : (Landroid/view/View;)V
    //   1922: aload #11
    //   1924: invokevirtual clearAnimation : ()V
    //   1927: goto -> 1953
    //   1930: aload_1
    //   1931: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1934: ifnull -> 1953
    //   1937: aload_1
    //   1938: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1941: astore #11
    //   1943: aload_1
    //   1944: aconst_null
    //   1945: invokevirtual setAnimator : (Landroid/animation/Animator;)V
    //   1948: aload #11
    //   1950: invokevirtual cancel : ()V
    //   1953: aload_1
    //   1954: invokevirtual getAnimatingAway : ()Landroid/view/View;
    //   1957: ifnonnull -> 2263
    //   1960: aload_1
    //   1961: invokevirtual getAnimator : ()Landroid/animation/Animator;
    //   1964: ifnull -> 1970
    //   1967: goto -> 2263
    //   1970: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   1973: ifeq -> 2012
    //   1976: new java/lang/StringBuilder
    //   1979: dup
    //   1980: invokespecial <init> : ()V
    //   1983: astore #11
    //   1985: aload #11
    //   1987: ldc_w 'movefrom CREATED: '
    //   1990: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1993: pop
    //   1994: aload #11
    //   1996: aload_1
    //   1997: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2000: pop
    //   2001: ldc 'FragmentManager'
    //   2003: aload #11
    //   2005: invokevirtual toString : ()Ljava/lang/String;
    //   2008: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   2011: pop
    //   2012: aload_1
    //   2013: getfield mRemoving : Z
    //   2016: ifeq -> 2031
    //   2019: aload_1
    //   2020: invokevirtual isInBackStack : ()Z
    //   2023: ifne -> 2031
    //   2026: iconst_1
    //   2027: istore_3
    //   2028: goto -> 2033
    //   2031: iconst_0
    //   2032: istore_3
    //   2033: iload_3
    //   2034: ifne -> 2059
    //   2037: aload_0
    //   2038: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2041: aload_1
    //   2042: invokevirtual shouldDestroy : (Landroidx/fragment/app/Fragment;)Z
    //   2045: ifeq -> 2051
    //   2048: goto -> 2059
    //   2051: aload_1
    //   2052: iconst_0
    //   2053: putfield mState : I
    //   2056: goto -> 2144
    //   2059: aload_0
    //   2060: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2063: astore #11
    //   2065: aload #11
    //   2067: instanceof androidx/lifecycle/ViewModelStoreOwner
    //   2070: ifeq -> 2085
    //   2073: aload_0
    //   2074: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2077: invokevirtual isCleared : ()Z
    //   2080: istore #8
    //   2082: goto -> 2117
    //   2085: iload #9
    //   2087: istore #8
    //   2089: aload #11
    //   2091: invokevirtual getContext : ()Landroid/content/Context;
    //   2094: instanceof android/app/Activity
    //   2097: ifeq -> 2117
    //   2100: iconst_1
    //   2101: aload_0
    //   2102: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2105: invokevirtual getContext : ()Landroid/content/Context;
    //   2108: checkcast android/app/Activity
    //   2111: invokevirtual isChangingConfigurations : ()Z
    //   2114: ixor
    //   2115: istore #8
    //   2117: iload_3
    //   2118: ifne -> 2126
    //   2121: iload #8
    //   2123: ifeq -> 2134
    //   2126: aload_0
    //   2127: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2130: aload_1
    //   2131: invokevirtual clearNonConfigState : (Landroidx/fragment/app/Fragment;)V
    //   2134: aload_1
    //   2135: invokevirtual performDestroy : ()V
    //   2138: aload_0
    //   2139: aload_1
    //   2140: iconst_0
    //   2141: invokevirtual dispatchOnFragmentDestroyed : (Landroidx/fragment/app/Fragment;Z)V
    //   2144: aload_1
    //   2145: invokevirtual performDetach : ()V
    //   2148: aload_0
    //   2149: aload_1
    //   2150: iconst_0
    //   2151: invokevirtual dispatchOnFragmentDetached : (Landroidx/fragment/app/Fragment;Z)V
    //   2154: iload_2
    //   2155: istore #6
    //   2157: iload #5
    //   2159: ifne -> 2274
    //   2162: iload_3
    //   2163: ifne -> 2252
    //   2166: aload_0
    //   2167: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   2170: aload_1
    //   2171: invokevirtual shouldDestroy : (Landroidx/fragment/app/Fragment;)Z
    //   2174: ifeq -> 2180
    //   2177: goto -> 2252
    //   2180: aload_1
    //   2181: aconst_null
    //   2182: putfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   2185: aload_1
    //   2186: aconst_null
    //   2187: putfield mParentFragment : Landroidx/fragment/app/Fragment;
    //   2190: aload_1
    //   2191: aconst_null
    //   2192: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   2195: iload_2
    //   2196: istore #6
    //   2198: aload_1
    //   2199: getfield mTargetWho : Ljava/lang/String;
    //   2202: ifnull -> 2274
    //   2205: aload_0
    //   2206: getfield mActive : Ljava/util/HashMap;
    //   2209: aload_1
    //   2210: getfield mTargetWho : Ljava/lang/String;
    //   2213: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   2216: checkcast androidx/fragment/app/Fragment
    //   2219: astore #11
    //   2221: iload_2
    //   2222: istore #6
    //   2224: aload #11
    //   2226: ifnull -> 2274
    //   2229: iload_2
    //   2230: istore #6
    //   2232: aload #11
    //   2234: invokevirtual getRetainInstance : ()Z
    //   2237: ifeq -> 2274
    //   2240: aload_1
    //   2241: aload #11
    //   2243: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   2246: iload_2
    //   2247: istore #6
    //   2249: goto -> 2274
    //   2252: aload_0
    //   2253: aload_1
    //   2254: invokevirtual makeInactive : (Landroidx/fragment/app/Fragment;)V
    //   2257: iload_2
    //   2258: istore #6
    //   2260: goto -> 2274
    //   2263: aload_1
    //   2264: iload_2
    //   2265: invokevirtual setStateAfterAnimating : (I)V
    //   2268: iload #7
    //   2270: istore_2
    //   2271: goto -> 2277
    //   2274: iload #6
    //   2276: istore_2
    //   2277: aload_1
    //   2278: getfield mState : I
    //   2281: iload_2
    //   2282: if_icmpeq -> 2361
    //   2285: new java/lang/StringBuilder
    //   2288: dup
    //   2289: invokespecial <init> : ()V
    //   2292: astore #11
    //   2294: aload #11
    //   2296: ldc_w 'moveToState: Fragment state for '
    //   2299: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2302: pop
    //   2303: aload #11
    //   2305: aload_1
    //   2306: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   2309: pop
    //   2310: aload #11
    //   2312: ldc_w ' not updated inline; expected state '
    //   2315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2318: pop
    //   2319: aload #11
    //   2321: iload_2
    //   2322: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2325: pop
    //   2326: aload #11
    //   2328: ldc_w ' found '
    //   2331: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2334: pop
    //   2335: aload #11
    //   2337: aload_1
    //   2338: getfield mState : I
    //   2341: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   2344: pop
    //   2345: ldc 'FragmentManager'
    //   2347: aload #11
    //   2349: invokevirtual toString : ()Ljava/lang/String;
    //   2352: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   2355: pop
    //   2356: aload_1
    //   2357: iload_2
    //   2358: putfield mState : I
    //   2361: return
    // Exception table:
    //   from	to	target	type
    //   1044	1057	1060	android/content/res/Resources$NotFoundException
  }
  
  public void noteStateNotSaved() {
    byte b = 0;
    this.mStateSaved = false;
    this.mStopped = false;
    int i = this.mAdded.size();
    while (b < i) {
      Fragment fragment = this.mAdded.get(b);
      if (fragment != null)
        fragment.noteStateNotSaved(); 
      b++;
    } 
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    boolean bool = "fragment".equals(paramString);
    paramString = null;
    if (!bool)
      return null; 
    String str2 = paramAttributeSet.getAttributeValue(null, "class");
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
    int i = 0;
    String str1 = str2;
    if (str2 == null)
      str1 = typedArray.getString(0); 
    int j = typedArray.getResourceId(1, -1);
    str2 = typedArray.getString(2);
    typedArray.recycle();
    if (str1 == null || !FragmentFactory.isFragmentClass(paramContext.getClassLoader(), str1))
      return null; 
    if (paramView != null)
      i = paramView.getId(); 
    if (i != -1 || j != -1 || str2 != null) {
      if (j != -1)
        fragment2 = findFragmentById(j); 
      Fragment fragment1 = fragment2;
      if (fragment2 == null) {
        fragment1 = fragment2;
        if (str2 != null)
          fragment1 = findFragmentByTag(str2); 
      } 
      Fragment fragment2 = fragment1;
      if (fragment1 == null) {
        fragment2 = fragment1;
        if (i != -1)
          fragment2 = findFragmentById(i); 
      } 
      if (DEBUG) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("onCreateView: id=0x");
        stringBuilder2.append(Integer.toHexString(j));
        stringBuilder2.append(" fname=");
        stringBuilder2.append(str1);
        stringBuilder2.append(" existing=");
        stringBuilder2.append(fragment2);
        Log.v("FragmentManager", stringBuilder2.toString());
      } 
      if (fragment2 == null) {
        int k;
        fragment2 = getFragmentFactory().instantiate(paramContext.getClassLoader(), str1);
        fragment2.mFromLayout = true;
        if (j != 0) {
          k = j;
        } else {
          k = i;
        } 
        fragment2.mFragmentId = k;
        fragment2.mContainerId = i;
        fragment2.mTag = str2;
        fragment2.mInLayout = true;
        fragment2.mFragmentManager = this;
        fragment2.mHost = this.mHost;
        fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
        addFragment(fragment2, true);
      } else if (!fragment2.mInLayout) {
        fragment2.mInLayout = true;
        fragment2.mHost = this.mHost;
        fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
      } else {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(paramAttributeSet.getPositionDescription());
        stringBuilder2.append(": Duplicate id 0x");
        stringBuilder2.append(Integer.toHexString(j));
        stringBuilder2.append(", tag ");
        stringBuilder2.append(str2);
        stringBuilder2.append(", or parent id 0x");
        stringBuilder2.append(Integer.toHexString(i));
        stringBuilder2.append(" with another fragment for ");
        stringBuilder2.append(str1);
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      if (this.mCurState < 1 && fragment2.mFromLayout) {
        moveToState(fragment2, 1, 0, 0, false);
      } else {
        moveToState(fragment2);
      } 
      if (fragment2.mView != null) {
        if (j != 0)
          fragment2.mView.setId(j); 
        if (fragment2.mView.getTag() == null)
          fragment2.mView.setTag(str2); 
        return fragment2.mView;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Fragment ");
      stringBuilder1.append(str1);
      stringBuilder1.append(" did not create a view.");
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramAttributeSet.getPositionDescription());
    stringBuilder.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
    stringBuilder.append(str1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return onCreateView((View)null, paramString, paramContext, paramAttributeSet);
  }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
      paramFragment.mDeferStart = false;
      moveToState(paramFragment, this.mCurState, 0, 0, false);
    } 
  }
  
  public void popBackStack() {
    enqueueAction(new PopBackStackState(null, -1, 0), false);
  }
  
  public void popBackStack(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      enqueueAction(new PopBackStackState(null, paramInt1, paramInt2), false);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void popBackStack(String paramString, int paramInt) {
    enqueueAction(new PopBackStackState(paramString, -1, paramInt), false);
  }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    return popBackStackImmediate((String)null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    execPendingActions();
    if (paramInt1 >= 0)
      return popBackStackImmediate((String)null, paramInt1, paramInt2); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad id: ");
    stringBuilder.append(paramInt1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    checkStateLoss();
    return popBackStackImmediate(paramString, -1, paramInt);
  }
  
  boolean popBackStackState(ArrayList<BackStackRecord> paramArrayList, ArrayList<Boolean> paramArrayList1, String paramString, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mBackStack : Ljava/util/ArrayList;
    //   4: astore #8
    //   6: aload #8
    //   8: ifnonnull -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_3
    //   14: ifnonnull -> 71
    //   17: iload #4
    //   19: ifge -> 71
    //   22: iload #5
    //   24: iconst_1
    //   25: iand
    //   26: ifne -> 71
    //   29: aload #8
    //   31: invokevirtual size : ()I
    //   34: iconst_1
    //   35: isub
    //   36: istore #4
    //   38: iload #4
    //   40: ifge -> 45
    //   43: iconst_0
    //   44: ireturn
    //   45: aload_1
    //   46: aload_0
    //   47: getfield mBackStack : Ljava/util/ArrayList;
    //   50: iload #4
    //   52: invokevirtual remove : (I)Ljava/lang/Object;
    //   55: invokevirtual add : (Ljava/lang/Object;)Z
    //   58: pop
    //   59: aload_2
    //   60: iconst_1
    //   61: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   64: invokevirtual add : (Ljava/lang/Object;)Z
    //   67: pop
    //   68: goto -> 326
    //   71: aload_3
    //   72: ifnonnull -> 89
    //   75: iload #4
    //   77: iflt -> 83
    //   80: goto -> 89
    //   83: iconst_m1
    //   84: istore #4
    //   86: goto -> 263
    //   89: aload_0
    //   90: getfield mBackStack : Ljava/util/ArrayList;
    //   93: invokevirtual size : ()I
    //   96: iconst_1
    //   97: isub
    //   98: istore #6
    //   100: iload #6
    //   102: iflt -> 162
    //   105: aload_0
    //   106: getfield mBackStack : Ljava/util/ArrayList;
    //   109: iload #6
    //   111: invokevirtual get : (I)Ljava/lang/Object;
    //   114: checkcast androidx/fragment/app/BackStackRecord
    //   117: astore #8
    //   119: aload_3
    //   120: ifnull -> 138
    //   123: aload_3
    //   124: aload #8
    //   126: invokevirtual getName : ()Ljava/lang/String;
    //   129: invokevirtual equals : (Ljava/lang/Object;)Z
    //   132: ifeq -> 138
    //   135: goto -> 162
    //   138: iload #4
    //   140: iflt -> 156
    //   143: iload #4
    //   145: aload #8
    //   147: getfield mIndex : I
    //   150: if_icmpne -> 156
    //   153: goto -> 162
    //   156: iinc #6, -1
    //   159: goto -> 100
    //   162: iload #6
    //   164: ifge -> 169
    //   167: iconst_0
    //   168: ireturn
    //   169: iload #6
    //   171: istore #7
    //   173: iload #5
    //   175: iconst_1
    //   176: iand
    //   177: ifeq -> 259
    //   180: iload #6
    //   182: iconst_1
    //   183: isub
    //   184: istore #5
    //   186: iload #5
    //   188: istore #7
    //   190: iload #5
    //   192: iflt -> 259
    //   195: aload_0
    //   196: getfield mBackStack : Ljava/util/ArrayList;
    //   199: iload #5
    //   201: invokevirtual get : (I)Ljava/lang/Object;
    //   204: checkcast androidx/fragment/app/BackStackRecord
    //   207: astore #8
    //   209: aload_3
    //   210: ifnull -> 229
    //   213: iload #5
    //   215: istore #6
    //   217: aload_3
    //   218: aload #8
    //   220: invokevirtual getName : ()Ljava/lang/String;
    //   223: invokevirtual equals : (Ljava/lang/Object;)Z
    //   226: ifne -> 180
    //   229: iload #5
    //   231: istore #7
    //   233: iload #4
    //   235: iflt -> 259
    //   238: iload #5
    //   240: istore #7
    //   242: iload #4
    //   244: aload #8
    //   246: getfield mIndex : I
    //   249: if_icmpne -> 259
    //   252: iload #5
    //   254: istore #6
    //   256: goto -> 180
    //   259: iload #7
    //   261: istore #4
    //   263: iload #4
    //   265: aload_0
    //   266: getfield mBackStack : Ljava/util/ArrayList;
    //   269: invokevirtual size : ()I
    //   272: iconst_1
    //   273: isub
    //   274: if_icmpne -> 279
    //   277: iconst_0
    //   278: ireturn
    //   279: aload_0
    //   280: getfield mBackStack : Ljava/util/ArrayList;
    //   283: invokevirtual size : ()I
    //   286: iconst_1
    //   287: isub
    //   288: istore #5
    //   290: iload #5
    //   292: iload #4
    //   294: if_icmple -> 326
    //   297: aload_1
    //   298: aload_0
    //   299: getfield mBackStack : Ljava/util/ArrayList;
    //   302: iload #5
    //   304: invokevirtual remove : (I)Ljava/lang/Object;
    //   307: invokevirtual add : (Ljava/lang/Object;)Z
    //   310: pop
    //   311: aload_2
    //   312: iconst_1
    //   313: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   316: invokevirtual add : (Ljava/lang/Object;)Z
    //   319: pop
    //   320: iinc #5, -1
    //   323: goto -> 290
    //   326: iconst_1
    //   327: ireturn
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mFragmentManager != this) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    paramBundle.putString(paramString, paramFragment.mWho);
  }
  
  public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks, boolean paramBoolean) {
    this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(paramFragmentLifecycleCallbacks, paramBoolean));
  }
  
  public void removeFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("remove: ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" nesting=");
      stringBuilder.append(paramFragment.mBackStackNesting);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    boolean bool = paramFragment.isInBackStack();
    if (!paramFragment.mDetached || (bool ^ true) != 0)
      synchronized (this.mAdded) {
        this.mAdded.remove(paramFragment);
        if (isMenuAvailable(paramFragment))
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
        paramFragment.mRemoving = true;
        return;
      }  
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
    if (arrayList != null)
      arrayList.remove(paramOnBackStackChangedListener); 
  }
  
  void removeRetainedFragment(Fragment paramFragment) {
    if (isStateSaved()) {
      if (DEBUG)
        Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved"); 
      return;
    } 
    if (this.mNonConfig.removeRetainedFragment(paramFragment) && DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Updating retained Fragments: Removed ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (byte b = 0; b < this.mBackStackChangeListeners.size(); b++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(b)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable paramParcelable, FragmentManagerNonConfig paramFragmentManagerNonConfig) {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner")); 
    this.mNonConfig.restoreFromSnapshot(paramFragmentManagerNonConfig);
    restoreSaveState(paramParcelable);
  }
  
  void restoreSaveState(Parcelable paramParcelable) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 5
    //   4: return
    //   5: aload_1
    //   6: checkcast androidx/fragment/app/FragmentManagerState
    //   9: astore #4
    //   11: aload #4
    //   13: getfield mActive : Ljava/util/ArrayList;
    //   16: ifnonnull -> 20
    //   19: return
    //   20: aload_0
    //   21: getfield mNonConfig : Landroidx/fragment/app/FragmentManagerViewModel;
    //   24: invokevirtual getRetainedFragments : ()Ljava/util/Collection;
    //   27: invokeinterface iterator : ()Ljava/util/Iterator;
    //   32: astore #6
    //   34: aload #6
    //   36: invokeinterface hasNext : ()Z
    //   41: ifeq -> 347
    //   44: aload #6
    //   46: invokeinterface next : ()Ljava/lang/Object;
    //   51: checkcast androidx/fragment/app/Fragment
    //   54: astore #5
    //   56: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   59: ifeq -> 95
    //   62: new java/lang/StringBuilder
    //   65: dup
    //   66: invokespecial <init> : ()V
    //   69: astore_1
    //   70: aload_1
    //   71: ldc_w 'restoreSaveState: re-attaching retained '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: aload #5
    //   81: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: ldc 'FragmentManager'
    //   87: aload_1
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   94: pop
    //   95: aload #4
    //   97: getfield mActive : Ljava/util/ArrayList;
    //   100: invokevirtual iterator : ()Ljava/util/Iterator;
    //   103: astore_3
    //   104: aload_3
    //   105: invokeinterface hasNext : ()Z
    //   110: ifeq -> 141
    //   113: aload_3
    //   114: invokeinterface next : ()Ljava/lang/Object;
    //   119: checkcast androidx/fragment/app/FragmentState
    //   122: astore_1
    //   123: aload_1
    //   124: getfield mWho : Ljava/lang/String;
    //   127: aload #5
    //   129: getfield mWho : Ljava/lang/String;
    //   132: invokevirtual equals : (Ljava/lang/Object;)Z
    //   135: ifeq -> 104
    //   138: goto -> 143
    //   141: aconst_null
    //   142: astore_1
    //   143: aload_1
    //   144: ifnonnull -> 233
    //   147: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   150: ifeq -> 204
    //   153: new java/lang/StringBuilder
    //   156: dup
    //   157: invokespecial <init> : ()V
    //   160: astore_1
    //   161: aload_1
    //   162: ldc_w 'Discarding retained Fragment '
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload_1
    //   170: aload #5
    //   172: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload_1
    //   177: ldc_w ' that was not found in the set of active Fragments '
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: aload_1
    //   185: aload #4
    //   187: getfield mActive : Ljava/util/ArrayList;
    //   190: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: ldc 'FragmentManager'
    //   196: aload_1
    //   197: invokevirtual toString : ()Ljava/lang/String;
    //   200: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   203: pop
    //   204: aload_0
    //   205: aload #5
    //   207: iconst_1
    //   208: iconst_0
    //   209: iconst_0
    //   210: iconst_0
    //   211: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   214: aload #5
    //   216: iconst_1
    //   217: putfield mRemoving : Z
    //   220: aload_0
    //   221: aload #5
    //   223: iconst_0
    //   224: iconst_0
    //   225: iconst_0
    //   226: iconst_0
    //   227: invokevirtual moveToState : (Landroidx/fragment/app/Fragment;IIIZ)V
    //   230: goto -> 34
    //   233: aload_1
    //   234: aload #5
    //   236: putfield mInstance : Landroidx/fragment/app/Fragment;
    //   239: aload #5
    //   241: aconst_null
    //   242: putfield mSavedViewState : Landroid/util/SparseArray;
    //   245: aload #5
    //   247: iconst_0
    //   248: putfield mBackStackNesting : I
    //   251: aload #5
    //   253: iconst_0
    //   254: putfield mInLayout : Z
    //   257: aload #5
    //   259: iconst_0
    //   260: putfield mAdded : Z
    //   263: aload #5
    //   265: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   268: ifnull -> 283
    //   271: aload #5
    //   273: getfield mTarget : Landroidx/fragment/app/Fragment;
    //   276: getfield mWho : Ljava/lang/String;
    //   279: astore_3
    //   280: goto -> 285
    //   283: aconst_null
    //   284: astore_3
    //   285: aload #5
    //   287: aload_3
    //   288: putfield mTargetWho : Ljava/lang/String;
    //   291: aload #5
    //   293: aconst_null
    //   294: putfield mTarget : Landroidx/fragment/app/Fragment;
    //   297: aload_1
    //   298: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   301: ifnull -> 34
    //   304: aload_1
    //   305: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   308: aload_0
    //   309: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   312: invokevirtual getContext : ()Landroid/content/Context;
    //   315: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   318: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   321: aload #5
    //   323: aload_1
    //   324: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   327: ldc 'android:view_state'
    //   329: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   332: putfield mSavedViewState : Landroid/util/SparseArray;
    //   335: aload #5
    //   337: aload_1
    //   338: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   341: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   344: goto -> 34
    //   347: aload_0
    //   348: getfield mActive : Ljava/util/HashMap;
    //   351: invokevirtual clear : ()V
    //   354: aload #4
    //   356: getfield mActive : Ljava/util/ArrayList;
    //   359: invokevirtual iterator : ()Ljava/util/Iterator;
    //   362: astore #6
    //   364: aload #6
    //   366: invokeinterface hasNext : ()Z
    //   371: ifeq -> 495
    //   374: aload #6
    //   376: invokeinterface next : ()Ljava/lang/Object;
    //   381: checkcast androidx/fragment/app/FragmentState
    //   384: astore_3
    //   385: aload_3
    //   386: ifnull -> 364
    //   389: aload_3
    //   390: aload_0
    //   391: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   394: invokevirtual getContext : ()Landroid/content/Context;
    //   397: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   400: aload_0
    //   401: invokevirtual getFragmentFactory : ()Landroidx/fragment/app/FragmentFactory;
    //   404: invokevirtual instantiate : (Ljava/lang/ClassLoader;Landroidx/fragment/app/FragmentFactory;)Landroidx/fragment/app/Fragment;
    //   407: astore_1
    //   408: aload_1
    //   409: aload_0
    //   410: putfield mFragmentManager : Landroidx/fragment/app/FragmentManagerImpl;
    //   413: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   416: ifeq -> 474
    //   419: new java/lang/StringBuilder
    //   422: dup
    //   423: invokespecial <init> : ()V
    //   426: astore #5
    //   428: aload #5
    //   430: ldc_w 'restoreSaveState: active ('
    //   433: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   436: pop
    //   437: aload #5
    //   439: aload_1
    //   440: getfield mWho : Ljava/lang/String;
    //   443: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   446: pop
    //   447: aload #5
    //   449: ldc_w '): '
    //   452: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload #5
    //   458: aload_1
    //   459: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   462: pop
    //   463: ldc 'FragmentManager'
    //   465: aload #5
    //   467: invokevirtual toString : ()Ljava/lang/String;
    //   470: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   473: pop
    //   474: aload_0
    //   475: getfield mActive : Ljava/util/HashMap;
    //   478: aload_1
    //   479: getfield mWho : Ljava/lang/String;
    //   482: aload_1
    //   483: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   486: pop
    //   487: aload_3
    //   488: aconst_null
    //   489: putfield mInstance : Landroidx/fragment/app/Fragment;
    //   492: goto -> 364
    //   495: aload_0
    //   496: getfield mAdded : Ljava/util/ArrayList;
    //   499: invokevirtual clear : ()V
    //   502: aload #4
    //   504: getfield mAdded : Ljava/util/ArrayList;
    //   507: ifnull -> 746
    //   510: aload #4
    //   512: getfield mAdded : Ljava/util/ArrayList;
    //   515: invokevirtual iterator : ()Ljava/util/Iterator;
    //   518: astore_3
    //   519: aload_3
    //   520: invokeinterface hasNext : ()Z
    //   525: ifeq -> 746
    //   528: aload_3
    //   529: invokeinterface next : ()Ljava/lang/Object;
    //   534: checkcast java/lang/String
    //   537: astore #5
    //   539: aload_0
    //   540: getfield mActive : Ljava/util/HashMap;
    //   543: aload #5
    //   545: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   548: checkcast androidx/fragment/app/Fragment
    //   551: astore_1
    //   552: aload_1
    //   553: ifnonnull -> 607
    //   556: new java/lang/StringBuilder
    //   559: dup
    //   560: invokespecial <init> : ()V
    //   563: astore #6
    //   565: aload #6
    //   567: ldc_w 'No instantiated fragment for ('
    //   570: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   573: pop
    //   574: aload #6
    //   576: aload #5
    //   578: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   581: pop
    //   582: aload #6
    //   584: ldc_w ')'
    //   587: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   590: pop
    //   591: aload_0
    //   592: new java/lang/IllegalStateException
    //   595: dup
    //   596: aload #6
    //   598: invokevirtual toString : ()Ljava/lang/String;
    //   601: invokespecial <init> : (Ljava/lang/String;)V
    //   604: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   607: aload_1
    //   608: iconst_1
    //   609: putfield mAdded : Z
    //   612: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   615: ifeq -> 671
    //   618: new java/lang/StringBuilder
    //   621: dup
    //   622: invokespecial <init> : ()V
    //   625: astore #6
    //   627: aload #6
    //   629: ldc_w 'restoreSaveState: added ('
    //   632: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   635: pop
    //   636: aload #6
    //   638: aload #5
    //   640: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: pop
    //   644: aload #6
    //   646: ldc_w '): '
    //   649: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   652: pop
    //   653: aload #6
    //   655: aload_1
    //   656: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   659: pop
    //   660: ldc 'FragmentManager'
    //   662: aload #6
    //   664: invokevirtual toString : ()Ljava/lang/String;
    //   667: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   670: pop
    //   671: aload_0
    //   672: getfield mAdded : Ljava/util/ArrayList;
    //   675: aload_1
    //   676: invokevirtual contains : (Ljava/lang/Object;)Z
    //   679: ifne -> 712
    //   682: aload_0
    //   683: getfield mAdded : Ljava/util/ArrayList;
    //   686: astore #5
    //   688: aload #5
    //   690: monitorenter
    //   691: aload_0
    //   692: getfield mAdded : Ljava/util/ArrayList;
    //   695: aload_1
    //   696: invokevirtual add : (Ljava/lang/Object;)Z
    //   699: pop
    //   700: aload #5
    //   702: monitorexit
    //   703: goto -> 519
    //   706: astore_1
    //   707: aload #5
    //   709: monitorexit
    //   710: aload_1
    //   711: athrow
    //   712: new java/lang/StringBuilder
    //   715: dup
    //   716: invokespecial <init> : ()V
    //   719: astore_3
    //   720: aload_3
    //   721: ldc_w 'Already added '
    //   724: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   727: pop
    //   728: aload_3
    //   729: aload_1
    //   730: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   733: pop
    //   734: new java/lang/IllegalStateException
    //   737: dup
    //   738: aload_3
    //   739: invokevirtual toString : ()Ljava/lang/String;
    //   742: invokespecial <init> : (Ljava/lang/String;)V
    //   745: athrow
    //   746: aload #4
    //   748: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   751: ifnull -> 925
    //   754: aload_0
    //   755: new java/util/ArrayList
    //   758: dup
    //   759: aload #4
    //   761: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   764: arraylength
    //   765: invokespecial <init> : (I)V
    //   768: putfield mBackStack : Ljava/util/ArrayList;
    //   771: iconst_0
    //   772: istore_2
    //   773: iload_2
    //   774: aload #4
    //   776: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   779: arraylength
    //   780: if_icmpge -> 930
    //   783: aload #4
    //   785: getfield mBackStack : [Landroidx/fragment/app/BackStackState;
    //   788: iload_2
    //   789: aaload
    //   790: aload_0
    //   791: invokevirtual instantiate : (Landroidx/fragment/app/FragmentManagerImpl;)Landroidx/fragment/app/BackStackRecord;
    //   794: astore_1
    //   795: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   798: ifeq -> 894
    //   801: new java/lang/StringBuilder
    //   804: dup
    //   805: invokespecial <init> : ()V
    //   808: astore_3
    //   809: aload_3
    //   810: ldc_w 'restoreAllState: back stack #'
    //   813: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   816: pop
    //   817: aload_3
    //   818: iload_2
    //   819: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   822: pop
    //   823: aload_3
    //   824: ldc_w ' (index '
    //   827: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   830: pop
    //   831: aload_3
    //   832: aload_1
    //   833: getfield mIndex : I
    //   836: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   839: pop
    //   840: aload_3
    //   841: ldc_w '): '
    //   844: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   847: pop
    //   848: aload_3
    //   849: aload_1
    //   850: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   853: pop
    //   854: ldc 'FragmentManager'
    //   856: aload_3
    //   857: invokevirtual toString : ()Ljava/lang/String;
    //   860: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   863: pop
    //   864: new java/io/PrintWriter
    //   867: dup
    //   868: new androidx/core/util/LogWriter
    //   871: dup
    //   872: ldc 'FragmentManager'
    //   874: invokespecial <init> : (Ljava/lang/String;)V
    //   877: invokespecial <init> : (Ljava/io/Writer;)V
    //   880: astore_3
    //   881: aload_1
    //   882: ldc_w '  '
    //   885: aload_3
    //   886: iconst_0
    //   887: invokevirtual dump : (Ljava/lang/String;Ljava/io/PrintWriter;Z)V
    //   890: aload_3
    //   891: invokevirtual close : ()V
    //   894: aload_0
    //   895: getfield mBackStack : Ljava/util/ArrayList;
    //   898: aload_1
    //   899: invokevirtual add : (Ljava/lang/Object;)Z
    //   902: pop
    //   903: aload_1
    //   904: getfield mIndex : I
    //   907: iflt -> 919
    //   910: aload_0
    //   911: aload_1
    //   912: getfield mIndex : I
    //   915: aload_1
    //   916: invokevirtual setBackStackIndex : (ILandroidx/fragment/app/BackStackRecord;)V
    //   919: iinc #2, 1
    //   922: goto -> 773
    //   925: aload_0
    //   926: aconst_null
    //   927: putfield mBackStack : Ljava/util/ArrayList;
    //   930: aload #4
    //   932: getfield mPrimaryNavActiveWho : Ljava/lang/String;
    //   935: ifnull -> 964
    //   938: aload_0
    //   939: getfield mActive : Ljava/util/HashMap;
    //   942: aload #4
    //   944: getfield mPrimaryNavActiveWho : Ljava/lang/String;
    //   947: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   950: checkcast androidx/fragment/app/Fragment
    //   953: astore_1
    //   954: aload_0
    //   955: aload_1
    //   956: putfield mPrimaryNav : Landroidx/fragment/app/Fragment;
    //   959: aload_0
    //   960: aload_1
    //   961: invokespecial dispatchParentPrimaryNavigationFragmentChanged : (Landroidx/fragment/app/Fragment;)V
    //   964: aload_0
    //   965: aload #4
    //   967: getfield mNextFragmentIndex : I
    //   970: putfield mNextFragmentIndex : I
    //   973: return
    // Exception table:
    //   from	to	target	type
    //   691	703	706	finally
    //   707	710	706	finally
  }
  
  @Deprecated
  FragmentManagerNonConfig retainNonConfig() {
    if (this.mHost instanceof ViewModelStoreOwner)
      throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner.")); 
    return this.mNonConfig.getSnapshot();
  }
  
  Parcelable saveAllState() {
    ArrayList arrayList;
    StringBuilder stringBuilder;
    forcePostponedTransactions();
    endAnimatingAwayFragments();
    execPendingActions();
    this.mStateSaved = true;
    boolean bool1 = this.mActive.isEmpty();
    Iterator<Fragment> iterator2 = null;
    if (bool1)
      return null; 
    ArrayList<FragmentState> arrayList1 = new ArrayList(this.mActive.size());
    Iterator<Fragment> iterator1 = this.mActive.values().iterator();
    boolean bool = false;
    int i = 0;
    while (iterator1.hasNext()) {
      arrayList = (ArrayList)iterator1.next();
      if (arrayList != null) {
        if (((Fragment)arrayList).mFragmentManager != this) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Failure saving state: active ");
          stringBuilder1.append(arrayList);
          stringBuilder1.append(" was removed from the FragmentManager");
          throwException(new IllegalStateException(stringBuilder1.toString()));
        } 
        FragmentState fragmentState = new FragmentState((Fragment)arrayList);
        arrayList1.add(fragmentState);
        if (((Fragment)arrayList).mState > 0 && fragmentState.mSavedFragmentState == null) {
          fragmentState.mSavedFragmentState = saveFragmentBasicState((Fragment)arrayList);
          if (((Fragment)arrayList).mTargetWho != null) {
            Fragment fragment1 = this.mActive.get(((Fragment)arrayList).mTargetWho);
            if (fragment1 == null) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Failure saving state: ");
              stringBuilder1.append(arrayList);
              stringBuilder1.append(" has target not in fragment manager: ");
              stringBuilder1.append(((Fragment)arrayList).mTargetWho);
              throwException(new IllegalStateException(stringBuilder1.toString()));
            } 
            if (fragmentState.mSavedFragmentState == null)
              fragmentState.mSavedFragmentState = new Bundle(); 
            putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment1);
            if (((Fragment)arrayList).mTargetRequestCode != 0)
              fragmentState.mSavedFragmentState.putInt("android:target_req_state", ((Fragment)arrayList).mTargetRequestCode); 
          } 
        } else {
          fragmentState.mSavedFragmentState = ((Fragment)arrayList).mSavedFragmentState;
        } 
        if (DEBUG) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Saved state of ");
          stringBuilder1.append(arrayList);
          stringBuilder1.append(": ");
          stringBuilder1.append(fragmentState.mSavedFragmentState);
          Log.v("FragmentManager", stringBuilder1.toString());
        } 
        i = 1;
      } 
    } 
    if (!i) {
      if (DEBUG)
        Log.v("FragmentManager", "saveAllState: no fragments!"); 
      return null;
    } 
    i = this.mAdded.size();
    if (i > 0) {
      ArrayList<String> arrayList3 = new ArrayList(i);
      Iterator<Fragment> iterator = this.mAdded.iterator();
      while (true) {
        arrayList = arrayList3;
        if (iterator.hasNext()) {
          Fragment fragment1 = iterator.next();
          arrayList3.add(fragment1.mWho);
          if (fragment1.mFragmentManager != this) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("Failure saving state: active ");
            stringBuilder1.append(fragment1);
            stringBuilder1.append(" was removed from the FragmentManager");
            throwException(new IllegalStateException(stringBuilder1.toString()));
          } 
          if (DEBUG) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("saveAllState: adding fragment (");
            stringBuilder1.append(fragment1.mWho);
            stringBuilder1.append("): ");
            stringBuilder1.append(fragment1);
            Log.v("FragmentManager", stringBuilder1.toString());
          } 
          continue;
        } 
        break;
      } 
    } else {
      arrayList = null;
    } 
    ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
    iterator1 = iterator2;
    if (arrayList2 != null) {
      int j = arrayList2.size();
      iterator1 = iterator2;
      if (j > 0) {
        BackStackState[] arrayOfBackStackState = new BackStackState[j];
        i = bool;
        while (true) {
          BackStackState[] arrayOfBackStackState1 = arrayOfBackStackState;
          if (i < j) {
            arrayOfBackStackState[i] = new BackStackState(this.mBackStack.get(i));
            if (DEBUG) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("saveAllState: adding back stack #");
              stringBuilder.append(i);
              stringBuilder.append(": ");
              stringBuilder.append(this.mBackStack.get(i));
              Log.v("FragmentManager", stringBuilder.toString());
            } 
            i++;
            continue;
          } 
          break;
        } 
      } 
    } 
    FragmentManagerState fragmentManagerState = new FragmentManagerState();
    fragmentManagerState.mActive = arrayList1;
    fragmentManagerState.mAdded = arrayList;
    fragmentManagerState.mBackStack = (BackStackState[])stringBuilder;
    Fragment fragment = this.mPrimaryNav;
    if (fragment != null)
      fragmentManagerState.mPrimaryNavActiveWho = fragment.mWho; 
    fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
    return fragmentManagerState;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    dispatchOnFragmentSaveInstanceState(paramFragment, this.mStateBundle, false);
    boolean bool = this.mStateBundle.isEmpty();
    Bundle bundle2 = null;
    if (!bool) {
      bundle2 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle1 = bundle2;
    if (paramFragment.mSavedViewState != null) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle2 = bundle1;
    if (!paramFragment.mUserVisibleHint) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle2;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    if (paramFragment.mFragmentManager != this) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Fragment ");
      stringBuilder.append(paramFragment);
      stringBuilder.append(" is not currently in the FragmentManager");
      throwException(new IllegalStateException(stringBuilder.toString()));
    } 
    int i = paramFragment.mState;
    Fragment.SavedState savedState2 = null;
    Fragment.SavedState savedState1 = savedState2;
    if (i > 0) {
      Bundle bundle = saveFragmentBasicState(paramFragment);
      savedState1 = savedState2;
      if (bundle != null)
        savedState1 = new Fragment.SavedState(bundle); 
    } 
    return savedState1;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView == null)
      return; 
    SparseArray<Parcelable> sparseArray = this.mStateArray;
    if (sparseArray == null) {
      this.mStateArray = new SparseArray();
    } else {
      sparseArray.clear();
    } 
    paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
    if (this.mStateArray.size() > 0) {
      paramFragment.mSavedViewState = this.mStateArray;
      this.mStateArray = null;
    } 
  }
  
  void scheduleCommit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   6: astore #4
    //   8: iconst_0
    //   9: istore_3
    //   10: aload #4
    //   12: ifnull -> 30
    //   15: aload_0
    //   16: getfield mPostponedTransactions : Ljava/util/ArrayList;
    //   19: invokevirtual isEmpty : ()Z
    //   22: ifne -> 30
    //   25: iconst_1
    //   26: istore_1
    //   27: goto -> 32
    //   30: iconst_0
    //   31: istore_1
    //   32: iload_3
    //   33: istore_2
    //   34: aload_0
    //   35: getfield mPendingActions : Ljava/util/ArrayList;
    //   38: ifnull -> 56
    //   41: iload_3
    //   42: istore_2
    //   43: aload_0
    //   44: getfield mPendingActions : Ljava/util/ArrayList;
    //   47: invokevirtual size : ()I
    //   50: iconst_1
    //   51: if_icmpne -> 56
    //   54: iconst_1
    //   55: istore_2
    //   56: iload_1
    //   57: ifne -> 64
    //   60: iload_2
    //   61: ifeq -> 97
    //   64: aload_0
    //   65: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   68: invokevirtual getHandler : ()Landroid/os/Handler;
    //   71: aload_0
    //   72: getfield mExecCommit : Ljava/lang/Runnable;
    //   75: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   78: aload_0
    //   79: getfield mHost : Landroidx/fragment/app/FragmentHostCallback;
    //   82: invokevirtual getHandler : ()Landroid/os/Handler;
    //   85: aload_0
    //   86: getfield mExecCommit : Ljava/lang/Runnable;
    //   89: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   92: pop
    //   93: aload_0
    //   94: invokespecial updateOnBackPressedCallbackEnabled : ()V
    //   97: aload_0
    //   98: monitorexit
    //   99: return
    //   100: astore #4
    //   102: aload_0
    //   103: monitorexit
    //   104: aload #4
    //   106: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	100	finally
    //   15	25	100	finally
    //   34	41	100	finally
    //   43	54	100	finally
    //   64	97	100	finally
    //   97	99	100	finally
    //   102	104	100	finally
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 25
    //   9: new java/util/ArrayList
    //   12: astore #5
    //   14: aload #5
    //   16: invokespecial <init> : ()V
    //   19: aload_0
    //   20: aload #5
    //   22: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   25: aload_0
    //   26: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   29: invokevirtual size : ()I
    //   32: istore #4
    //   34: iload #4
    //   36: istore_3
    //   37: iload_1
    //   38: iload #4
    //   40: if_icmpge -> 115
    //   43: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   46: ifeq -> 102
    //   49: new java/lang/StringBuilder
    //   52: astore #5
    //   54: aload #5
    //   56: invokespecial <init> : ()V
    //   59: aload #5
    //   61: ldc_w 'Setting back stack index '
    //   64: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload #5
    //   70: iload_1
    //   71: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload #5
    //   77: ldc_w ' to '
    //   80: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload #5
    //   86: aload_2
    //   87: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: ldc 'FragmentManager'
    //   93: aload #5
    //   95: invokevirtual toString : ()Ljava/lang/String;
    //   98: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   101: pop
    //   102: aload_0
    //   103: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   106: iload_1
    //   107: aload_2
    //   108: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   111: pop
    //   112: goto -> 281
    //   115: iload_3
    //   116: iload_1
    //   117: if_icmpge -> 213
    //   120: aload_0
    //   121: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   124: aconst_null
    //   125: invokevirtual add : (Ljava/lang/Object;)Z
    //   128: pop
    //   129: aload_0
    //   130: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   133: ifnonnull -> 152
    //   136: new java/util/ArrayList
    //   139: astore #5
    //   141: aload #5
    //   143: invokespecial <init> : ()V
    //   146: aload_0
    //   147: aload #5
    //   149: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   152: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   155: ifeq -> 195
    //   158: new java/lang/StringBuilder
    //   161: astore #5
    //   163: aload #5
    //   165: invokespecial <init> : ()V
    //   168: aload #5
    //   170: ldc_w 'Adding available back stack index '
    //   173: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload #5
    //   179: iload_3
    //   180: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: ldc 'FragmentManager'
    //   186: aload #5
    //   188: invokevirtual toString : ()Ljava/lang/String;
    //   191: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   194: pop
    //   195: aload_0
    //   196: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   199: iload_3
    //   200: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   203: invokevirtual add : (Ljava/lang/Object;)Z
    //   206: pop
    //   207: iinc #3, 1
    //   210: goto -> 115
    //   213: getstatic androidx/fragment/app/FragmentManagerImpl.DEBUG : Z
    //   216: ifeq -> 272
    //   219: new java/lang/StringBuilder
    //   222: astore #5
    //   224: aload #5
    //   226: invokespecial <init> : ()V
    //   229: aload #5
    //   231: ldc_w 'Adding back stack index '
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: pop
    //   238: aload #5
    //   240: iload_1
    //   241: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload #5
    //   247: ldc_w ' with '
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload #5
    //   256: aload_2
    //   257: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   260: pop
    //   261: ldc 'FragmentManager'
    //   263: aload #5
    //   265: invokevirtual toString : ()Ljava/lang/String;
    //   268: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   271: pop
    //   272: aload_0
    //   273: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   276: aload_2
    //   277: invokevirtual add : (Ljava/lang/Object;)Z
    //   280: pop
    //   281: aload_0
    //   282: monitorexit
    //   283: return
    //   284: astore_2
    //   285: aload_0
    //   286: monitorexit
    //   287: aload_2
    //   288: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	284	finally
    //   25	34	284	finally
    //   43	102	284	finally
    //   102	112	284	finally
    //   120	152	284	finally
    //   152	195	284	finally
    //   195	207	284	finally
    //   213	272	284	finally
    //   272	281	284	finally
    //   281	283	284	finally
    //   285	287	284	finally
  }
  
  public void setMaxLifecycle(Fragment paramFragment, Lifecycle.State paramState) {
    if (this.mActive.get(paramFragment.mWho) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this)) {
      paramFragment.mMaxState = paramState;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setPrimaryNavigationFragment(Fragment paramFragment) {
    if (paramFragment == null || (this.mActive.get(paramFragment.mWho) == paramFragment && (paramFragment.mHost == null || paramFragment.getFragmentManager() == this))) {
      Fragment fragment = this.mPrimaryNav;
      this.mPrimaryNav = paramFragment;
      dispatchParentPrimaryNavigationFragmentChanged(fragment);
      dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Fragment ");
    stringBuilder.append(paramFragment);
    stringBuilder.append(" is not an active fragment of FragmentManager ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void showFragment(Fragment paramFragment) {
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("show: ");
      stringBuilder.append(paramFragment);
      Log.v("FragmentManager", stringBuilder.toString());
    } 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      paramFragment.mHiddenChanged ^= 0x1;
    } 
  }
  
  void startPendingDeferredFragments() {
    for (Fragment fragment : this.mActive.values()) {
      if (fragment != null)
        performPendingDeferredStart(fragment); 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    Fragment fragment = this.mParent;
    if (fragment != null) {
      DebugUtils.buildShortClassTag(fragment, stringBuilder);
    } else {
      DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    } 
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks paramFragmentLifecycleCallbacks) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: iconst_0
    //   10: istore_2
    //   11: aload_0
    //   12: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   15: invokevirtual size : ()I
    //   18: istore_3
    //   19: iload_2
    //   20: iload_3
    //   21: if_icmpge -> 60
    //   24: aload_0
    //   25: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   28: iload_2
    //   29: invokevirtual get : (I)Ljava/lang/Object;
    //   32: checkcast androidx/fragment/app/FragmentManagerImpl$FragmentLifecycleCallbacksHolder
    //   35: getfield mCallback : Landroidx/fragment/app/FragmentManager$FragmentLifecycleCallbacks;
    //   38: aload_1
    //   39: if_acmpne -> 54
    //   42: aload_0
    //   43: getfield mLifecycleCallbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
    //   46: iload_2
    //   47: invokevirtual remove : (I)Ljava/lang/Object;
    //   50: pop
    //   51: goto -> 60
    //   54: iinc #2, 1
    //   57: goto -> 19
    //   60: aload #4
    //   62: monitorexit
    //   63: return
    //   64: astore_1
    //   65: aload #4
    //   67: monitorexit
    //   68: aload_1
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   11	19	64	finally
    //   24	51	64	finally
    //   60	63	64	finally
    //   65	68	64	finally
  }
  
  private static class AnimationOrAnimator {
    public final Animation animation = null;
    
    public final Animator animator;
    
    AnimationOrAnimator(Animator param1Animator) {
      this.animator = param1Animator;
      if (param1Animator != null)
        return; 
      throw new IllegalStateException("Animator cannot be null");
    }
    
    AnimationOrAnimator(Animation param1Animation) {
      this.animator = null;
      if (param1Animation != null)
        return; 
      throw new IllegalStateException("Animation cannot be null");
    }
  }
  
  private static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
    private boolean mAnimating = true;
    
    private final View mChild;
    
    private boolean mEnded;
    
    private final ViewGroup mParent;
    
    private boolean mTransitionEnded;
    
    EndViewTransitionAnimation(Animation param1Animation, ViewGroup param1ViewGroup, View param1View) {
      super(false);
      this.mParent = param1ViewGroup;
      this.mChild = param1View;
      addAnimation(param1Animation);
      this.mParent.post(this);
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation) {
      this.mAnimating = true;
      if (this.mEnded)
        return this.mTransitionEnded ^ true; 
      if (!super.getTransformation(param1Long, param1Transformation)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public boolean getTransformation(long param1Long, Transformation param1Transformation, float param1Float) {
      this.mAnimating = true;
      if (this.mEnded)
        return this.mTransitionEnded ^ true; 
      if (!super.getTransformation(param1Long, param1Transformation, param1Float)) {
        this.mEnded = true;
        OneShotPreDrawListener.add((View)this.mParent, this);
      } 
      return true;
    }
    
    public void run() {
      if (!this.mEnded && this.mAnimating) {
        this.mAnimating = false;
        this.mParent.post(this);
      } else {
        this.mParent.endViewTransition(this.mChild);
        this.mTransitionEnded = true;
      } 
    }
  }
  
  private static final class FragmentLifecycleCallbacksHolder {
    final FragmentManager.FragmentLifecycleCallbacks mCallback;
    
    final boolean mRecursive;
    
    FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks param1FragmentLifecycleCallbacks, boolean param1Boolean) {
      this.mCallback = param1FragmentLifecycleCallbacks;
      this.mRecursive = param1Boolean;
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = new int[] { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
  
  static interface OpGenerator {
    boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1);
  }
  
  private class PopBackStackState implements OpGenerator {
    final int mFlags;
    
    final int mId;
    
    final String mName;
    
    final FragmentManagerImpl this$0;
    
    PopBackStackState(String param1String, int param1Int1, int param1Int2) {
      this.mName = param1String;
      this.mId = param1Int1;
      this.mFlags = param1Int2;
    }
    
    public boolean generateOps(ArrayList<BackStackRecord> param1ArrayList, ArrayList<Boolean> param1ArrayList1) {
      return (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManagerImpl.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) ? false : FragmentManagerImpl.this.popBackStackState(param1ArrayList, param1ArrayList1, this.mName, this.mId, this.mFlags);
    }
  }
  
  static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
    final boolean mIsBack;
    
    private int mNumPostponed;
    
    final BackStackRecord mRecord;
    
    StartEnterTransitionListener(BackStackRecord param1BackStackRecord, boolean param1Boolean) {
      this.mIsBack = param1Boolean;
      this.mRecord = param1BackStackRecord;
    }
    
    public void cancelTransaction() {
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
    }
    
    public void completeTransaction() {
      int i = this.mNumPostponed;
      byte b = 0;
      if (i > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
      int j = fragmentManagerImpl.mAdded.size();
      while (b < j) {
        Fragment fragment = fragmentManagerImpl.mAdded.get(b);
        fragment.setOnStartEnterTransitionListener(null);
        if (i != 0 && fragment.isPostponed())
          fragment.startPostponedEnterTransition(); 
        b++;
      } 
      this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, i ^ 0x1, true);
    }
    
    public boolean isReady() {
      boolean bool;
      if (this.mNumPostponed == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onStartEnterTransition() {
      int i = this.mNumPostponed - 1;
      this.mNumPostponed = i;
      if (i != 0)
        return; 
      this.mRecord.mManager.scheduleCommit();
    }
    
    public void startListening() {
      this.mNumPostponed++;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\fragment\app\FragmentManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */