package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParser;

public abstract class Transition implements Cloneable {
  static final boolean DBG = false;
  
  private static final int[] DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };
  
  private static final String LOG_TAG = "Transition";
  
  private static final int MATCH_FIRST = 1;
  
  public static final int MATCH_ID = 3;
  
  private static final String MATCH_ID_STR = "id";
  
  public static final int MATCH_INSTANCE = 1;
  
  private static final String MATCH_INSTANCE_STR = "instance";
  
  public static final int MATCH_ITEM_ID = 4;
  
  private static final String MATCH_ITEM_ID_STR = "itemId";
  
  private static final int MATCH_LAST = 4;
  
  public static final int MATCH_NAME = 2;
  
  private static final String MATCH_NAME_STR = "name";
  
  private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
      public Path getPath(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
        Path path = new Path();
        path.moveTo(param1Float1, param1Float2);
        path.lineTo(param1Float3, param1Float4);
        return path;
      }
    };
  
  private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<ArrayMap<Animator, AnimationInfo>>();
  
  private ArrayList<Animator> mAnimators = new ArrayList<Animator>();
  
  boolean mCanRemoveViews = false;
  
  ArrayList<Animator> mCurrentAnimators = new ArrayList<Animator>();
  
  long mDuration = -1L;
  
  private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mEndValuesList;
  
  private boolean mEnded = false;
  
  private EpicenterCallback mEpicenterCallback;
  
  private TimeInterpolator mInterpolator = null;
  
  private ArrayList<TransitionListener> mListeners = null;
  
  private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
  
  private String mName = getClass().getName();
  
  private ArrayMap<String, String> mNameOverrides;
  
  private int mNumInstances = 0;
  
  TransitionSet mParent = null;
  
  private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;
  
  private boolean mPaused = false;
  
  TransitionPropagation mPropagation;
  
  private ViewGroup mSceneRoot = null;
  
  private long mStartDelay = -1L;
  
  private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
  
  private ArrayList<TransitionValues> mStartValuesList;
  
  private ArrayList<View> mTargetChildExcludes = null;
  
  private ArrayList<View> mTargetExcludes = null;
  
  private ArrayList<Integer> mTargetIdChildExcludes = null;
  
  private ArrayList<Integer> mTargetIdExcludes = null;
  
  ArrayList<Integer> mTargetIds = new ArrayList<Integer>();
  
  private ArrayList<String> mTargetNameExcludes = null;
  
  private ArrayList<String> mTargetNames = null;
  
  private ArrayList<Class> mTargetTypeChildExcludes = null;
  
  private ArrayList<Class> mTargetTypeExcludes = null;
  
  private ArrayList<Class> mTargetTypes = null;
  
  ArrayList<View> mTargets = new ArrayList<View>();
  
  public Transition() {}
  
  public Transition(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION);
    XmlResourceParser xmlResourceParser = (XmlResourceParser)paramAttributeSet;
    long l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "duration", 1, -1);
    if (l >= 0L)
      setDuration(l); 
    l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)xmlResourceParser, "startDelay", 2, -1);
    if (l > 0L)
      setStartDelay(l); 
    int i = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)xmlResourceParser, "interpolator", 0, 0);
    if (i > 0)
      setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator(paramContext, i)); 
    String str = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)xmlResourceParser, "matchOrder", 3);
    if (str != null)
      setMatchOrder(parseMatchOrder(str)); 
    typedArray.recycle();
  }
  
  private void addUnmatched(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    byte b1;
    byte b3 = 0;
    byte b2 = 0;
    while (true) {
      b1 = b3;
      if (b2 < paramArrayMap1.size()) {
        TransitionValues transitionValues = (TransitionValues)paramArrayMap1.valueAt(b2);
        if (isValidTarget(transitionValues.view)) {
          this.mStartValuesList.add(transitionValues);
          this.mEndValuesList.add(null);
        } 
        b2++;
        continue;
      } 
      break;
    } 
    while (b1 < paramArrayMap2.size()) {
      TransitionValues transitionValues = (TransitionValues)paramArrayMap2.valueAt(b1);
      if (isValidTarget(transitionValues.view)) {
        this.mEndValuesList.add(transitionValues);
        this.mStartValuesList.add(null);
      } 
      b1++;
    } 
  }
  
  private static void addViewValues(TransitionValuesMaps paramTransitionValuesMaps, View paramView, TransitionValues paramTransitionValues) {
    paramTransitionValuesMaps.mViewValues.put(paramView, paramTransitionValues);
    int i = paramView.getId();
    if (i >= 0)
      if (paramTransitionValuesMaps.mIdValues.indexOfKey(i) >= 0) {
        paramTransitionValuesMaps.mIdValues.put(i, null);
      } else {
        paramTransitionValuesMaps.mIdValues.put(i, paramView);
      }  
    String str = ViewCompat.getTransitionName(paramView);
    if (str != null)
      if (paramTransitionValuesMaps.mNameValues.containsKey(str)) {
        paramTransitionValuesMaps.mNameValues.put(str, null);
      } else {
        paramTransitionValuesMaps.mNameValues.put(str, paramView);
      }  
    if (paramView.getParent() instanceof ListView) {
      ListView listView = (ListView)paramView.getParent();
      if (listView.getAdapter().hasStableIds()) {
        long l = listView.getItemIdAtPosition(listView.getPositionForView(paramView));
        if (paramTransitionValuesMaps.mItemIdValues.indexOfKey(l) >= 0) {
          paramView = (View)paramTransitionValuesMaps.mItemIdValues.get(l);
          if (paramView != null) {
            ViewCompat.setHasTransientState(paramView, false);
            paramTransitionValuesMaps.mItemIdValues.put(l, null);
          } 
        } else {
          ViewCompat.setHasTransientState(paramView, true);
          paramTransitionValuesMaps.mItemIdValues.put(l, paramView);
        } 
      } 
    } 
  }
  
  private static boolean alreadyContains(int[] paramArrayOfint, int paramInt) {
    int i = paramArrayOfint[paramInt];
    for (byte b = 0; b < paramInt; b++) {
      if (paramArrayOfint[b] == i)
        return true; 
    } 
    return false;
  }
  
  private void captureHierarchy(View paramView, boolean paramBoolean) {
    if (paramView == null)
      return; 
    int i = paramView.getId();
    ArrayList<Integer> arrayList2 = this.mTargetIdExcludes;
    if (arrayList2 != null && arrayList2.contains(Integer.valueOf(i)))
      return; 
    ArrayList<View> arrayList1 = this.mTargetExcludes;
    if (arrayList1 != null && arrayList1.contains(paramView))
      return; 
    ArrayList<Class> arrayList = this.mTargetTypeExcludes;
    byte b = 0;
    if (arrayList != null) {
      int j = arrayList.size();
      for (byte b1 = 0; b1 < j; b1++) {
        if (((Class)this.mTargetTypeExcludes.get(b1)).isInstance(paramView))
          return; 
      } 
    } 
    if (paramView.getParent() instanceof ViewGroup) {
      TransitionValues transitionValues = new TransitionValues();
      transitionValues.view = paramView;
      if (paramBoolean) {
        captureStartValues(transitionValues);
      } else {
        captureEndValues(transitionValues);
      } 
      transitionValues.mTargetedTransitions.add(this);
      capturePropagationValues(transitionValues);
      if (paramBoolean) {
        addViewValues(this.mStartValues, paramView, transitionValues);
      } else {
        addViewValues(this.mEndValues, paramView, transitionValues);
      } 
    } 
    if (paramView instanceof ViewGroup) {
      ArrayList<Integer> arrayList5 = this.mTargetIdChildExcludes;
      if (arrayList5 != null && arrayList5.contains(Integer.valueOf(i)))
        return; 
      ArrayList<View> arrayList4 = this.mTargetChildExcludes;
      if (arrayList4 != null && arrayList4.contains(paramView))
        return; 
      ArrayList<Class> arrayList3 = this.mTargetTypeChildExcludes;
      if (arrayList3 != null) {
        int j = arrayList3.size();
        for (byte b2 = 0; b2 < j; b2++) {
          if (((Class)this.mTargetTypeChildExcludes.get(b2)).isInstance(paramView))
            return; 
        } 
      } 
      ViewGroup viewGroup = (ViewGroup)paramView;
      for (byte b1 = b; b1 < viewGroup.getChildCount(); b1++)
        captureHierarchy(viewGroup.getChildAt(b1), paramBoolean); 
    } 
  }
  
  private ArrayList<Integer> excludeId(ArrayList<Integer> paramArrayList, int paramInt, boolean paramBoolean) {
    ArrayList<Integer> arrayList = paramArrayList;
    if (paramInt > 0)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, Integer.valueOf(paramInt));
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, Integer.valueOf(paramInt));
      }  
    return arrayList;
  }
  
  private static <T> ArrayList<T> excludeObject(ArrayList<T> paramArrayList, T paramT, boolean paramBoolean) {
    ArrayList<T> arrayList = paramArrayList;
    if (paramT != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramT);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramT);
      }  
    return arrayList;
  }
  
  private ArrayList<Class> excludeType(ArrayList<Class> paramArrayList, Class<?> paramClass, boolean paramBoolean) {
    ArrayList<Class> arrayList = paramArrayList;
    if (paramClass != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramClass);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramClass);
      }  
    return arrayList;
  }
  
  private ArrayList<View> excludeView(ArrayList<View> paramArrayList, View paramView, boolean paramBoolean) {
    ArrayList<View> arrayList = paramArrayList;
    if (paramView != null)
      if (paramBoolean) {
        arrayList = ArrayListManager.add(paramArrayList, paramView);
      } else {
        arrayList = ArrayListManager.remove(paramArrayList, paramView);
      }  
    return arrayList;
  }
  
  private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
    ArrayMap<Animator, AnimationInfo> arrayMap2 = sRunningAnimators.get();
    ArrayMap<Animator, AnimationInfo> arrayMap1 = arrayMap2;
    if (arrayMap2 == null) {
      arrayMap1 = new ArrayMap();
      sRunningAnimators.set(arrayMap1);
    } 
    return arrayMap1;
  }
  
  private static boolean isValidMatch(int paramInt) {
    boolean bool = true;
    if (paramInt < 1 || paramInt > 4)
      bool = false; 
    return bool;
  }
  
  private static boolean isValueChanged(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2, String paramString) {
    int i;
    paramTransitionValues1 = (TransitionValues)paramTransitionValues1.values.get(paramString);
    paramTransitionValues2 = (TransitionValues)paramTransitionValues2.values.get(paramString);
    byte b = 1;
    if (paramTransitionValues1 == null && paramTransitionValues2 == null) {
      i = 0;
    } else {
      i = b;
      if (paramTransitionValues1 != null)
        if (paramTransitionValues2 == null) {
          i = b;
        } else {
          i = true ^ paramTransitionValues1.equals(paramTransitionValues2);
        }  
    } 
    return i;
  }
  
  private void matchIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, SparseArray<View> paramSparseArray1, SparseArray<View> paramSparseArray2) {
    int i = paramSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramSparseArray2.get(paramSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues2 != null && transitionValues1 != null) {
            this.mStartValuesList.add(transitionValues2);
            this.mEndValuesList.add(transitionValues1);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchInstances(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2) {
    for (int i = paramArrayMap1.size() - 1; i >= 0; i--) {
      View view = (View)paramArrayMap1.keyAt(i);
      if (view != null && isValidTarget(view)) {
        TransitionValues transitionValues = (TransitionValues)paramArrayMap2.remove(view);
        if (transitionValues != null && transitionValues.view != null && isValidTarget(transitionValues.view)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.removeAt(i);
          this.mStartValuesList.add(transitionValues1);
          this.mEndValuesList.add(transitionValues);
        } 
      } 
    } 
  }
  
  private void matchItemIds(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, LongSparseArray<View> paramLongSparseArray1, LongSparseArray<View> paramLongSparseArray2) {
    int i = paramLongSparseArray1.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramLongSparseArray1.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramLongSparseArray2.get(paramLongSparseArray1.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues1 != null && transitionValues2 != null) {
            this.mStartValuesList.add(transitionValues1);
            this.mEndValuesList.add(transitionValues2);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchNames(ArrayMap<View, TransitionValues> paramArrayMap1, ArrayMap<View, TransitionValues> paramArrayMap2, ArrayMap<String, View> paramArrayMap3, ArrayMap<String, View> paramArrayMap4) {
    int i = paramArrayMap3.size();
    for (byte b = 0; b < i; b++) {
      View view = (View)paramArrayMap3.valueAt(b);
      if (view != null && isValidTarget(view)) {
        View view1 = (View)paramArrayMap4.get(paramArrayMap3.keyAt(b));
        if (view1 != null && isValidTarget(view1)) {
          TransitionValues transitionValues2 = (TransitionValues)paramArrayMap1.get(view);
          TransitionValues transitionValues1 = (TransitionValues)paramArrayMap2.get(view1);
          if (transitionValues2 != null && transitionValues1 != null) {
            this.mStartValuesList.add(transitionValues2);
            this.mEndValuesList.add(transitionValues1);
            paramArrayMap1.remove(view);
            paramArrayMap2.remove(view1);
          } 
        } 
      } 
    } 
  }
  
  private void matchStartAndEnd(TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2) {
    ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps1.mViewValues);
    ArrayMap<View, TransitionValues> arrayMap1 = new ArrayMap((SimpleArrayMap)paramTransitionValuesMaps2.mViewValues);
    byte b = 0;
    while (true) {
      int[] arrayOfInt = this.mMatchOrder;
      if (b < arrayOfInt.length) {
        int i = arrayOfInt[b];
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              if (i == 4)
                matchItemIds(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mItemIdValues, paramTransitionValuesMaps2.mItemIdValues); 
            } else {
              matchIds(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mIdValues, paramTransitionValuesMaps2.mIdValues);
            } 
          } else {
            matchNames(arrayMap2, arrayMap1, paramTransitionValuesMaps1.mNameValues, paramTransitionValuesMaps2.mNameValues);
          } 
        } else {
          matchInstances(arrayMap2, arrayMap1);
        } 
        b++;
        continue;
      } 
      addUnmatched(arrayMap2, arrayMap1);
      return;
    } 
  }
  
  private static int[] parseMatchOrder(String paramString) {
    StringBuilder stringBuilder;
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, ",");
    int[] arrayOfInt = new int[stringTokenizer.countTokens()];
    for (byte b = 0; stringTokenizer.hasMoreTokens(); b++) {
      String str = stringTokenizer.nextToken().trim();
      if ("id".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 3;
      } else if ("instance".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 1;
      } else if ("name".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 2;
      } else if ("itemId".equalsIgnoreCase(str)) {
        arrayOfInt[b] = 4;
      } else {
        int[] arrayOfInt1;
        if (str.isEmpty()) {
          arrayOfInt1 = new int[arrayOfInt.length - 1];
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, b);
          b--;
          arrayOfInt = arrayOfInt1;
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown match type in matchOrder: '");
          stringBuilder.append((String)arrayOfInt1);
          stringBuilder.append("'");
          throw new InflateException(stringBuilder.toString());
        } 
      } 
    } 
    return (int[])stringBuilder;
  }
  
  private void runAnimator(Animator paramAnimator, final ArrayMap<Animator, AnimationInfo> runningAnimators) {
    if (paramAnimator != null) {
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final Transition this$0;
            
            final ArrayMap val$runningAnimators;
            
            public void onAnimationEnd(Animator param1Animator) {
              runningAnimators.remove(param1Animator);
              Transition.this.mCurrentAnimators.remove(param1Animator);
            }
            
            public void onAnimationStart(Animator param1Animator) {
              Transition.this.mCurrentAnimators.add(param1Animator);
            }
          });
      animate(paramAnimator);
    } 
  }
  
  public Transition addListener(TransitionListener paramTransitionListener) {
    if (this.mListeners == null)
      this.mListeners = new ArrayList<TransitionListener>(); 
    this.mListeners.add(paramTransitionListener);
    return this;
  }
  
  public Transition addTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.add(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition addTarget(View paramView) {
    this.mTargets.add(paramView);
    return this;
  }
  
  public Transition addTarget(Class paramClass) {
    if (this.mTargetTypes == null)
      this.mTargetTypes = new ArrayList<Class<?>>(); 
    this.mTargetTypes.add(paramClass);
    return this;
  }
  
  public Transition addTarget(String paramString) {
    if (this.mTargetNames == null)
      this.mTargetNames = new ArrayList<String>(); 
    this.mTargetNames.add(paramString);
    return this;
  }
  
  protected void animate(Animator paramAnimator) {
    if (paramAnimator == null) {
      end();
    } else {
      if (getDuration() >= 0L)
        paramAnimator.setDuration(getDuration()); 
      if (getStartDelay() >= 0L)
        paramAnimator.setStartDelay(getStartDelay()); 
      if (getInterpolator() != null)
        paramAnimator.setInterpolator(getInterpolator()); 
      paramAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final Transition this$0;
            
            public void onAnimationEnd(Animator param1Animator) {
              Transition.this.end();
              param1Animator.removeListener((Animator.AnimatorListener)this);
            }
          });
      paramAnimator.start();
    } 
  }
  
  protected void cancel() {
    int i;
    for (i = this.mCurrentAnimators.size() - 1; i >= 0; i--)
      ((Animator)this.mCurrentAnimators.get(i)).cancel(); 
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList != null && arrayList.size() > 0) {
      arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
      int j = arrayList.size();
      for (i = 0; i < j; i++)
        ((TransitionListener)arrayList.get(i)).onTransitionCancel(this); 
    } 
  }
  
  public abstract void captureEndValues(TransitionValues paramTransitionValues);
  
  void capturePropagationValues(TransitionValues paramTransitionValues) {
    if (this.mPropagation != null && !paramTransitionValues.values.isEmpty()) {
      String[] arrayOfString = this.mPropagation.getPropagationProperties();
      if (arrayOfString == null)
        return; 
      boolean bool = false;
      byte b = 0;
      while (true) {
        if (b < arrayOfString.length) {
          if (!paramTransitionValues.values.containsKey(arrayOfString[b])) {
            b = bool;
            break;
          } 
          b++;
          continue;
        } 
        b = 1;
        break;
      } 
      if (b == 0)
        this.mPropagation.captureValues(paramTransitionValues); 
    } 
  }
  
  public abstract void captureStartValues(TransitionValues paramTransitionValues);
  
  void captureValues(ViewGroup paramViewGroup, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: invokevirtual clearValues : (Z)V
    //   5: aload_0
    //   6: getfield mTargetIds : Ljava/util/ArrayList;
    //   9: invokevirtual size : ()I
    //   12: istore_3
    //   13: iconst_0
    //   14: istore #5
    //   16: iload_3
    //   17: ifgt -> 30
    //   20: aload_0
    //   21: getfield mTargets : Ljava/util/ArrayList;
    //   24: invokevirtual size : ()I
    //   27: ifle -> 71
    //   30: aload_0
    //   31: getfield mTargetNames : Ljava/util/ArrayList;
    //   34: astore #7
    //   36: aload #7
    //   38: ifnull -> 49
    //   41: aload #7
    //   43: invokevirtual isEmpty : ()Z
    //   46: ifeq -> 71
    //   49: aload_0
    //   50: getfield mTargetTypes : Ljava/util/ArrayList;
    //   53: astore #7
    //   55: aload #7
    //   57: ifnull -> 80
    //   60: aload #7
    //   62: invokevirtual isEmpty : ()Z
    //   65: ifeq -> 71
    //   68: goto -> 80
    //   71: aload_0
    //   72: aload_1
    //   73: iload_2
    //   74: invokespecial captureHierarchy : (Landroid/view/View;Z)V
    //   77: goto -> 308
    //   80: iconst_0
    //   81: istore_3
    //   82: iload_3
    //   83: aload_0
    //   84: getfield mTargetIds : Ljava/util/ArrayList;
    //   87: invokevirtual size : ()I
    //   90: if_icmpge -> 204
    //   93: aload_1
    //   94: aload_0
    //   95: getfield mTargetIds : Ljava/util/ArrayList;
    //   98: iload_3
    //   99: invokevirtual get : (I)Ljava/lang/Object;
    //   102: checkcast java/lang/Integer
    //   105: invokevirtual intValue : ()I
    //   108: invokevirtual findViewById : (I)Landroid/view/View;
    //   111: astore #8
    //   113: aload #8
    //   115: ifnull -> 198
    //   118: new androidx/transition/TransitionValues
    //   121: dup
    //   122: invokespecial <init> : ()V
    //   125: astore #7
    //   127: aload #7
    //   129: aload #8
    //   131: putfield view : Landroid/view/View;
    //   134: iload_2
    //   135: ifeq -> 147
    //   138: aload_0
    //   139: aload #7
    //   141: invokevirtual captureStartValues : (Landroidx/transition/TransitionValues;)V
    //   144: goto -> 153
    //   147: aload_0
    //   148: aload #7
    //   150: invokevirtual captureEndValues : (Landroidx/transition/TransitionValues;)V
    //   153: aload #7
    //   155: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   158: aload_0
    //   159: invokevirtual add : (Ljava/lang/Object;)Z
    //   162: pop
    //   163: aload_0
    //   164: aload #7
    //   166: invokevirtual capturePropagationValues : (Landroidx/transition/TransitionValues;)V
    //   169: iload_2
    //   170: ifeq -> 187
    //   173: aload_0
    //   174: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   177: aload #8
    //   179: aload #7
    //   181: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   184: goto -> 198
    //   187: aload_0
    //   188: getfield mEndValues : Landroidx/transition/TransitionValuesMaps;
    //   191: aload #8
    //   193: aload #7
    //   195: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   198: iinc #3, 1
    //   201: goto -> 82
    //   204: iconst_0
    //   205: istore_3
    //   206: iload_3
    //   207: aload_0
    //   208: getfield mTargets : Ljava/util/ArrayList;
    //   211: invokevirtual size : ()I
    //   214: if_icmpge -> 308
    //   217: aload_0
    //   218: getfield mTargets : Ljava/util/ArrayList;
    //   221: iload_3
    //   222: invokevirtual get : (I)Ljava/lang/Object;
    //   225: checkcast android/view/View
    //   228: astore #7
    //   230: new androidx/transition/TransitionValues
    //   233: dup
    //   234: invokespecial <init> : ()V
    //   237: astore_1
    //   238: aload_1
    //   239: aload #7
    //   241: putfield view : Landroid/view/View;
    //   244: iload_2
    //   245: ifeq -> 256
    //   248: aload_0
    //   249: aload_1
    //   250: invokevirtual captureStartValues : (Landroidx/transition/TransitionValues;)V
    //   253: goto -> 261
    //   256: aload_0
    //   257: aload_1
    //   258: invokevirtual captureEndValues : (Landroidx/transition/TransitionValues;)V
    //   261: aload_1
    //   262: getfield mTargetedTransitions : Ljava/util/ArrayList;
    //   265: aload_0
    //   266: invokevirtual add : (Ljava/lang/Object;)Z
    //   269: pop
    //   270: aload_0
    //   271: aload_1
    //   272: invokevirtual capturePropagationValues : (Landroidx/transition/TransitionValues;)V
    //   275: iload_2
    //   276: ifeq -> 292
    //   279: aload_0
    //   280: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   283: aload #7
    //   285: aload_1
    //   286: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   289: goto -> 302
    //   292: aload_0
    //   293: getfield mEndValues : Landroidx/transition/TransitionValuesMaps;
    //   296: aload #7
    //   298: aload_1
    //   299: invokestatic addViewValues : (Landroidx/transition/TransitionValuesMaps;Landroid/view/View;Landroidx/transition/TransitionValues;)V
    //   302: iinc #3, 1
    //   305: goto -> 206
    //   308: iload_2
    //   309: ifne -> 443
    //   312: aload_0
    //   313: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   316: astore_1
    //   317: aload_1
    //   318: ifnull -> 443
    //   321: aload_1
    //   322: invokevirtual size : ()I
    //   325: istore #6
    //   327: new java/util/ArrayList
    //   330: dup
    //   331: iload #6
    //   333: invokespecial <init> : (I)V
    //   336: astore_1
    //   337: iconst_0
    //   338: istore_3
    //   339: iload #5
    //   341: istore #4
    //   343: iload_3
    //   344: iload #6
    //   346: if_icmpge -> 385
    //   349: aload_0
    //   350: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   353: iload_3
    //   354: invokevirtual keyAt : (I)Ljava/lang/Object;
    //   357: checkcast java/lang/String
    //   360: astore #7
    //   362: aload_1
    //   363: aload_0
    //   364: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   367: getfield mNameValues : Landroidx/collection/ArrayMap;
    //   370: aload #7
    //   372: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   375: invokevirtual add : (Ljava/lang/Object;)Z
    //   378: pop
    //   379: iinc #3, 1
    //   382: goto -> 339
    //   385: iload #4
    //   387: iload #6
    //   389: if_icmpge -> 443
    //   392: aload_1
    //   393: iload #4
    //   395: invokevirtual get : (I)Ljava/lang/Object;
    //   398: checkcast android/view/View
    //   401: astore #7
    //   403: aload #7
    //   405: ifnull -> 437
    //   408: aload_0
    //   409: getfield mNameOverrides : Landroidx/collection/ArrayMap;
    //   412: iload #4
    //   414: invokevirtual valueAt : (I)Ljava/lang/Object;
    //   417: checkcast java/lang/String
    //   420: astore #8
    //   422: aload_0
    //   423: getfield mStartValues : Landroidx/transition/TransitionValuesMaps;
    //   426: getfield mNameValues : Landroidx/collection/ArrayMap;
    //   429: aload #8
    //   431: aload #7
    //   433: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   436: pop
    //   437: iinc #4, 1
    //   440: goto -> 385
    //   443: return
  }
  
  void clearValues(boolean paramBoolean) {
    if (paramBoolean) {
      this.mStartValues.mViewValues.clear();
      this.mStartValues.mIdValues.clear();
      this.mStartValues.mItemIdValues.clear();
    } else {
      this.mEndValues.mViewValues.clear();
      this.mEndValues.mIdValues.clear();
      this.mEndValues.mItemIdValues.clear();
    } 
  }
  
  public Transition clone() {
    try {
      Transition transition = (Transition)super.clone();
      ArrayList<Animator> arrayList = new ArrayList();
      this();
      transition.mAnimators = arrayList;
      TransitionValuesMaps transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mStartValues = transitionValuesMaps;
      transitionValuesMaps = new TransitionValuesMaps();
      this();
      transition.mEndValues = transitionValuesMaps;
      transition.mStartValuesList = null;
      transition.mEndValuesList = null;
      return transition;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      return null;
    } 
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    return null;
  }
  
  protected void createAnimators(ViewGroup paramViewGroup, TransitionValuesMaps paramTransitionValuesMaps1, TransitionValuesMaps paramTransitionValuesMaps2, ArrayList<TransitionValues> paramArrayList1, ArrayList<TransitionValues> paramArrayList2) {
    Object object;
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    SparseIntArray sparseIntArray = new SparseIntArray();
    int j = paramArrayList1.size();
    long l = Long.MAX_VALUE;
    int i = 0;
    while (i < j) {
      TransitionValues transitionValues2 = paramArrayList1.get(i);
      TransitionValues transitionValues1 = paramArrayList2.get(i);
      TransitionValues transitionValues3 = transitionValues2;
      if (transitionValues2 != null) {
        transitionValues3 = transitionValues2;
        if (!transitionValues2.mTargetedTransitions.contains(this))
          transitionValues3 = null; 
      } 
      TransitionValues transitionValues4 = transitionValues1;
      if (transitionValues1 != null) {
        transitionValues4 = transitionValues1;
        if (!transitionValues1.mTargetedTransitions.contains(this))
          transitionValues4 = null; 
      } 
      if (transitionValues3 != null || transitionValues4 != null) {
        int m;
        if (transitionValues3 == null || transitionValues4 == null || isTransitionRequired(transitionValues3, transitionValues4)) {
          m = 1;
        } else {
          m = 0;
        } 
        if (m) {
          Animator animator = createAnimator(paramViewGroup, transitionValues3, transitionValues4);
          if (animator != null) {
            TransitionValues transitionValues5;
            View view;
            TransitionValues transitionValues6;
            if (transitionValues4 != null) {
              View view1 = transitionValues4.view;
              String[] arrayOfString = getTransitionProperties();
              if (view1 != null && arrayOfString != null && arrayOfString.length > 0) {
                transitionValues6 = new TransitionValues();
                transitionValues6.view = view1;
                transitionValues2 = (TransitionValues)paramTransitionValuesMaps2.mViewValues.get(view1);
                m = i;
                if (transitionValues2 != null) {
                  byte b = 0;
                  while (true) {
                    m = i;
                    if (b < arrayOfString.length) {
                      transitionValues6.values.put(arrayOfString[b], transitionValues2.values.get(arrayOfString[b]));
                      b++;
                      continue;
                    } 
                    break;
                  } 
                } 
                i = m;
                int n = arrayMap.size();
                m = 0;
                while (true) {
                  if (m < n) {
                    AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(arrayMap.keyAt(m));
                    if (animationInfo.mValues != null && animationInfo.mView == view1 && animationInfo.mName.equals(getName()) && animationInfo.mValues.equals(transitionValues6)) {
                      animator = null;
                      TransitionValues transitionValues = transitionValues6;
                      break;
                    } 
                    m++;
                    continue;
                  } 
                  transitionValues2 = transitionValues6;
                  break;
                } 
              } else {
                transitionValues2 = null;
              } 
              Animator animator1 = animator;
              transitionValues5 = transitionValues2;
              view = view1;
            } else {
              view = transitionValues3.view;
              TransitionValues transitionValues = null;
              transitionValues6 = transitionValues5;
              transitionValues5 = transitionValues;
            } 
            Object object2 = object;
            m = i;
            if (transitionValues6 != null) {
              TransitionPropagation transitionPropagation = this.mPropagation;
              object2 = object;
              if (transitionPropagation != null) {
                long l1 = transitionPropagation.getStartDelay(paramViewGroup, this, transitionValues3, transitionValues4);
                sparseIntArray.put(this.mAnimators.size(), (int)l1);
                l1 = Math.min(l1, object);
              } 
              arrayMap.put(transitionValues6, new AnimationInfo(view, getName(), this, ViewUtils.getWindowId((View)paramViewGroup), transitionValues5));
              this.mAnimators.add(transitionValues6);
              m = i;
            } 
            continue;
          } 
        } 
      } 
      Object object1 = object;
      int k = i;
      continue;
      i = SYNTHETIC_LOCAL_VARIABLE_7 + 1;
      object = SYNTHETIC_LOCAL_VARIABLE_12;
    } 
    if (object != 0L)
      for (i = 0; i < sparseIntArray.size(); i++) {
        int k = sparseIntArray.keyAt(i);
        Animator animator = this.mAnimators.get(k);
        animator.setStartDelay(sparseIntArray.valueAt(i) - object + animator.getStartDelay());
      }  
  }
  
  protected void end() {
    int i = this.mNumInstances - 1;
    this.mNumInstances = i;
    if (i == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionEnd(this); 
      } 
      for (i = 0; i < this.mStartValues.mItemIdValues.size(); i++) {
        View view = (View)this.mStartValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      for (i = 0; i < this.mEndValues.mItemIdValues.size(); i++) {
        View view = (View)this.mEndValues.mItemIdValues.valueAt(i);
        if (view != null)
          ViewCompat.setHasTransientState(view, false); 
      } 
      this.mEnded = true;
    } 
  }
  
  public Transition excludeChildren(int paramInt, boolean paramBoolean) {
    this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(View paramView, boolean paramBoolean) {
    this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeChildren(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(int paramInt, boolean paramBoolean) {
    this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, paramInt, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(View paramView, boolean paramBoolean) {
    this.mTargetExcludes = excludeView(this.mTargetExcludes, paramView, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(Class paramClass, boolean paramBoolean) {
    this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, paramClass, paramBoolean);
    return this;
  }
  
  public Transition excludeTarget(String paramString, boolean paramBoolean) {
    this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, paramString, paramBoolean);
    return this;
  }
  
  void forceToEnd(ViewGroup paramViewGroup) {
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    if (paramViewGroup != null) {
      WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.mView != null && windowIdImpl != null && windowIdImpl.equals(animationInfo.mWindowId))
          ((Animator)arrayMap.keyAt(i)).end(); 
        i--;
      } 
    } 
  }
  
  public long getDuration() {
    return this.mDuration;
  }
  
  public Rect getEpicenter() {
    EpicenterCallback epicenterCallback = this.mEpicenterCallback;
    return (epicenterCallback == null) ? null : epicenterCallback.onGetEpicenter(this);
  }
  
  public EpicenterCallback getEpicenterCallback() {
    return this.mEpicenterCallback;
  }
  
  public TimeInterpolator getInterpolator() {
    return this.mInterpolator;
  }
  
  TransitionValues getMatchedTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValues transitionValues;
    byte b1;
    ArrayList<TransitionValues> arrayList;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getMatchedTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      arrayList = this.mStartValuesList;
    } else {
      arrayList = this.mEndValuesList;
    } 
    View view = null;
    if (arrayList == null)
      return null; 
    int i = arrayList.size();
    byte b2 = -1;
    byte b = 0;
    while (true) {
      b1 = b2;
      if (b < i) {
        TransitionValues transitionValues1 = arrayList.get(b);
        if (transitionValues1 == null)
          return null; 
        if (transitionValues1.view == paramView) {
          b1 = b;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    paramView = view;
    if (b1 >= 0) {
      ArrayList<TransitionValues> arrayList1;
      if (paramBoolean) {
        arrayList1 = this.mEndValuesList;
      } else {
        arrayList1 = this.mStartValuesList;
      } 
      transitionValues = arrayList1.get(b1);
    } 
    return transitionValues;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public PathMotion getPathMotion() {
    return this.mPathMotion;
  }
  
  public TransitionPropagation getPropagation() {
    return this.mPropagation;
  }
  
  public long getStartDelay() {
    return this.mStartDelay;
  }
  
  public List<Integer> getTargetIds() {
    return this.mTargetIds;
  }
  
  public List<String> getTargetNames() {
    return this.mTargetNames;
  }
  
  public List<Class> getTargetTypes() {
    return this.mTargetTypes;
  }
  
  public List<View> getTargets() {
    return this.mTargets;
  }
  
  public String[] getTransitionProperties() {
    return null;
  }
  
  public TransitionValues getTransitionValues(View paramView, boolean paramBoolean) {
    TransitionValuesMaps transitionValuesMaps;
    TransitionSet transitionSet = this.mParent;
    if (transitionSet != null)
      return transitionSet.getTransitionValues(paramView, paramBoolean); 
    if (paramBoolean) {
      transitionValuesMaps = this.mStartValues;
    } else {
      transitionValuesMaps = this.mEndValues;
    } 
    return (TransitionValues)transitionValuesMaps.mViewValues.get(paramView);
  }
  
  public boolean isTransitionRequired(TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: iload #6
    //   5: istore #5
    //   7: aload_1
    //   8: ifnull -> 119
    //   11: iload #6
    //   13: istore #5
    //   15: aload_2
    //   16: ifnull -> 119
    //   19: aload_0
    //   20: invokevirtual getTransitionProperties : ()[Ljava/lang/String;
    //   23: astore #7
    //   25: aload #7
    //   27: ifnull -> 68
    //   30: aload #7
    //   32: arraylength
    //   33: istore #4
    //   35: iconst_0
    //   36: istore_3
    //   37: iload #6
    //   39: istore #5
    //   41: iload_3
    //   42: iload #4
    //   44: if_icmpge -> 119
    //   47: aload_1
    //   48: aload_2
    //   49: aload #7
    //   51: iload_3
    //   52: aaload
    //   53: invokestatic isValueChanged : (Landroidx/transition/TransitionValues;Landroidx/transition/TransitionValues;Ljava/lang/String;)Z
    //   56: ifeq -> 62
    //   59: goto -> 116
    //   62: iinc #3, 1
    //   65: goto -> 37
    //   68: aload_1
    //   69: getfield values : Ljava/util/Map;
    //   72: invokeinterface keySet : ()Ljava/util/Set;
    //   77: invokeinterface iterator : ()Ljava/util/Iterator;
    //   82: astore #7
    //   84: iload #6
    //   86: istore #5
    //   88: aload #7
    //   90: invokeinterface hasNext : ()Z
    //   95: ifeq -> 119
    //   98: aload_1
    //   99: aload_2
    //   100: aload #7
    //   102: invokeinterface next : ()Ljava/lang/Object;
    //   107: checkcast java/lang/String
    //   110: invokestatic isValueChanged : (Landroidx/transition/TransitionValues;Landroidx/transition/TransitionValues;Ljava/lang/String;)Z
    //   113: ifeq -> 84
    //   116: iconst_1
    //   117: istore #5
    //   119: iload #5
    //   121: ireturn
  }
  
  boolean isValidTarget(View paramView) {
    int i = paramView.getId();
    ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
    if (arrayList3 != null && arrayList3.contains(Integer.valueOf(i)))
      return false; 
    ArrayList<View> arrayList2 = this.mTargetExcludes;
    if (arrayList2 != null && arrayList2.contains(paramView))
      return false; 
    ArrayList<Class> arrayList1 = this.mTargetTypeExcludes;
    if (arrayList1 != null) {
      int j = arrayList1.size();
      for (byte b = 0; b < j; b++) {
        if (((Class)this.mTargetTypeExcludes.get(b)).isInstance(paramView))
          return false; 
      } 
    } 
    if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(paramView) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(paramView)))
      return false; 
    if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
      arrayList1 = this.mTargetTypes;
      if (arrayList1 == null || arrayList1.isEmpty()) {
        ArrayList<String> arrayList4 = this.mTargetNames;
        if (arrayList4 == null || arrayList4.isEmpty())
          return true; 
      } 
    } 
    if (this.mTargetIds.contains(Integer.valueOf(i)) || this.mTargets.contains(paramView))
      return true; 
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null && arrayList.contains(ViewCompat.getTransitionName(paramView)))
      return true; 
    if (this.mTargetTypes != null)
      for (byte b = 0; b < this.mTargetTypes.size(); b++) {
        if (((Class)this.mTargetTypes.get(b)).isInstance(paramView))
          return true; 
      }  
    return false;
  }
  
  public void pause(View paramView) {
    if (!this.mEnded) {
      ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
      int i = arrayMap.size();
      WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
      while (--i >= 0) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
        if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
          AnimatorUtils.pause((Animator)arrayMap.keyAt(i)); 
        i--;
      } 
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int j = arrayList.size();
        for (i = 0; i < j; i++)
          ((TransitionListener)arrayList.get(i)).onTransitionPause(this); 
      } 
      this.mPaused = true;
    } 
  }
  
  void playTransition(ViewGroup paramViewGroup) {
    this.mStartValuesList = new ArrayList<TransitionValues>();
    this.mEndValuesList = new ArrayList<TransitionValues>();
    matchStartAndEnd(this.mStartValues, this.mEndValues);
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    int i = arrayMap.size();
    WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)paramViewGroup);
    while (--i >= 0) {
      Animator animator = (Animator)arrayMap.keyAt(i);
      if (animator != null) {
        AnimationInfo animationInfo = (AnimationInfo)arrayMap.get(animator);
        if (animationInfo != null && animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId)) {
          boolean bool;
          TransitionValues transitionValues2 = animationInfo.mValues;
          View view = animationInfo.mView;
          TransitionValues transitionValues1 = getTransitionValues(view, true);
          TransitionValues transitionValues3 = getMatchedTransitionValues(view, true);
          if ((transitionValues1 != null || transitionValues3 != null) && animationInfo.mTransition.isTransitionRequired(transitionValues2, transitionValues3)) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool)
            if (animator.isRunning() || animator.isStarted()) {
              animator.cancel();
            } else {
              arrayMap.remove(animator);
            }  
        } 
      } 
      i--;
    } 
    createAnimators(paramViewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
    runAnimators();
  }
  
  public Transition removeListener(TransitionListener paramTransitionListener) {
    ArrayList<TransitionListener> arrayList = this.mListeners;
    if (arrayList == null)
      return this; 
    arrayList.remove(paramTransitionListener);
    if (this.mListeners.size() == 0)
      this.mListeners = null; 
    return this;
  }
  
  public Transition removeTarget(int paramInt) {
    if (paramInt != 0)
      this.mTargetIds.remove(Integer.valueOf(paramInt)); 
    return this;
  }
  
  public Transition removeTarget(View paramView) {
    this.mTargets.remove(paramView);
    return this;
  }
  
  public Transition removeTarget(Class paramClass) {
    ArrayList<Class> arrayList = this.mTargetTypes;
    if (arrayList != null)
      arrayList.remove(paramClass); 
    return this;
  }
  
  public Transition removeTarget(String paramString) {
    ArrayList<String> arrayList = this.mTargetNames;
    if (arrayList != null)
      arrayList.remove(paramString); 
    return this;
  }
  
  public void resume(View paramView) {
    if (this.mPaused) {
      if (!this.mEnded) {
        ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
        int i = arrayMap.size();
        WindowIdImpl windowIdImpl = ViewUtils.getWindowId(paramView);
        while (--i >= 0) {
          AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(i);
          if (animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId))
            AnimatorUtils.resume((Animator)arrayMap.keyAt(i)); 
          i--;
        } 
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
          arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
          int j = arrayList.size();
          for (i = 0; i < j; i++)
            ((TransitionListener)arrayList.get(i)).onTransitionResume(this); 
        } 
      } 
      this.mPaused = false;
    } 
  }
  
  protected void runAnimators() {
    start();
    ArrayMap<Animator, AnimationInfo> arrayMap = getRunningAnimators();
    for (Animator animator : this.mAnimators) {
      if (arrayMap.containsKey(animator)) {
        start();
        runAnimator(animator, arrayMap);
      } 
    } 
    this.mAnimators.clear();
    end();
  }
  
  void setCanRemoveViews(boolean paramBoolean) {
    this.mCanRemoveViews = paramBoolean;
  }
  
  public Transition setDuration(long paramLong) {
    this.mDuration = paramLong;
    return this;
  }
  
  public void setEpicenterCallback(EpicenterCallback paramEpicenterCallback) {
    this.mEpicenterCallback = paramEpicenterCallback;
  }
  
  public Transition setInterpolator(TimeInterpolator paramTimeInterpolator) {
    this.mInterpolator = paramTimeInterpolator;
    return this;
  }
  
  public void setMatchOrder(int... paramVarArgs) {
    if (paramVarArgs == null || paramVarArgs.length == 0) {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      return;
    } 
    byte b = 0;
    while (b < paramVarArgs.length) {
      if (isValidMatch(paramVarArgs[b])) {
        if (!alreadyContains(paramVarArgs, b)) {
          b++;
          continue;
        } 
        throw new IllegalArgumentException("matches contains a duplicate value");
      } 
      throw new IllegalArgumentException("matches contains invalid value");
    } 
    this.mMatchOrder = (int[])paramVarArgs.clone();
  }
  
  public void setPathMotion(PathMotion paramPathMotion) {
    if (paramPathMotion == null) {
      this.mPathMotion = STRAIGHT_PATH_MOTION;
    } else {
      this.mPathMotion = paramPathMotion;
    } 
  }
  
  public void setPropagation(TransitionPropagation paramTransitionPropagation) {
    this.mPropagation = paramTransitionPropagation;
  }
  
  Transition setSceneRoot(ViewGroup paramViewGroup) {
    this.mSceneRoot = paramViewGroup;
    return this;
  }
  
  public Transition setStartDelay(long paramLong) {
    this.mStartDelay = paramLong;
    return this;
  }
  
  protected void start() {
    if (this.mNumInstances == 0) {
      ArrayList<TransitionListener> arrayList = this.mListeners;
      if (arrayList != null && arrayList.size() > 0) {
        arrayList = (ArrayList<TransitionListener>)this.mListeners.clone();
        int i = arrayList.size();
        for (byte b = 0; b < i; b++)
          ((TransitionListener)arrayList.get(b)).onTransitionStart(this); 
      } 
      this.mEnded = false;
    } 
    this.mNumInstances++;
  }
  
  public String toString() {
    return toString("");
  }
  
  String toString(String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #4
    //   9: aload #4
    //   11: aload_1
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload #4
    //   18: aload_0
    //   19: invokevirtual getClass : ()Ljava/lang/Class;
    //   22: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload #4
    //   31: ldc_w '@'
    //   34: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: pop
    //   38: aload #4
    //   40: aload_0
    //   41: invokevirtual hashCode : ()I
    //   44: invokestatic toHexString : (I)Ljava/lang/String;
    //   47: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload #4
    //   53: ldc_w ': '
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload #4
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: astore #4
    //   67: aload #4
    //   69: astore_1
    //   70: aload_0
    //   71: getfield mDuration : J
    //   74: ldc2_w -1
    //   77: lcmp
    //   78: ifeq -> 126
    //   81: new java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial <init> : ()V
    //   88: astore_1
    //   89: aload_1
    //   90: aload #4
    //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: aload_1
    //   97: ldc_w 'dur('
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload_1
    //   105: aload_0
    //   106: getfield mDuration : J
    //   109: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_1
    //   114: ldc_w ') '
    //   117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload_1
    //   122: invokevirtual toString : ()Ljava/lang/String;
    //   125: astore_1
    //   126: aload_1
    //   127: astore #4
    //   129: aload_0
    //   130: getfield mStartDelay : J
    //   133: ldc2_w -1
    //   136: lcmp
    //   137: ifeq -> 191
    //   140: new java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial <init> : ()V
    //   147: astore #4
    //   149: aload #4
    //   151: aload_1
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload #4
    //   158: ldc_w 'dly('
    //   161: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: aload #4
    //   167: aload_0
    //   168: getfield mStartDelay : J
    //   171: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #4
    //   177: ldc_w ') '
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: aload #4
    //   186: invokevirtual toString : ()Ljava/lang/String;
    //   189: astore #4
    //   191: aload #4
    //   193: astore_1
    //   194: aload_0
    //   195: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   198: ifnull -> 246
    //   201: new java/lang/StringBuilder
    //   204: dup
    //   205: invokespecial <init> : ()V
    //   208: astore_1
    //   209: aload_1
    //   210: aload #4
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: aload_1
    //   217: ldc_w 'interp('
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: pop
    //   224: aload_1
    //   225: aload_0
    //   226: getfield mInterpolator : Landroid/animation/TimeInterpolator;
    //   229: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: aload_1
    //   234: ldc_w ') '
    //   237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   240: pop
    //   241: aload_1
    //   242: invokevirtual toString : ()Ljava/lang/String;
    //   245: astore_1
    //   246: aload_0
    //   247: getfield mTargetIds : Ljava/util/ArrayList;
    //   250: invokevirtual size : ()I
    //   253: ifgt -> 269
    //   256: aload_1
    //   257: astore #4
    //   259: aload_0
    //   260: getfield mTargets : Ljava/util/ArrayList;
    //   263: invokevirtual size : ()I
    //   266: ifle -> 548
    //   269: new java/lang/StringBuilder
    //   272: dup
    //   273: invokespecial <init> : ()V
    //   276: astore #4
    //   278: aload #4
    //   280: aload_1
    //   281: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   284: pop
    //   285: aload #4
    //   287: ldc_w 'tgts('
    //   290: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: aload #4
    //   296: invokevirtual toString : ()Ljava/lang/String;
    //   299: astore #4
    //   301: aload_0
    //   302: getfield mTargetIds : Ljava/util/ArrayList;
    //   305: invokevirtual size : ()I
    //   308: istore_2
    //   309: iconst_0
    //   310: istore_3
    //   311: aload #4
    //   313: astore_1
    //   314: iload_2
    //   315: ifle -> 412
    //   318: iconst_0
    //   319: istore_2
    //   320: aload #4
    //   322: astore_1
    //   323: iload_2
    //   324: aload_0
    //   325: getfield mTargetIds : Ljava/util/ArrayList;
    //   328: invokevirtual size : ()I
    //   331: if_icmpge -> 412
    //   334: aload #4
    //   336: astore_1
    //   337: iload_2
    //   338: ifle -> 369
    //   341: new java/lang/StringBuilder
    //   344: dup
    //   345: invokespecial <init> : ()V
    //   348: astore_1
    //   349: aload_1
    //   350: aload #4
    //   352: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   355: pop
    //   356: aload_1
    //   357: ldc_w ', '
    //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: pop
    //   364: aload_1
    //   365: invokevirtual toString : ()Ljava/lang/String;
    //   368: astore_1
    //   369: new java/lang/StringBuilder
    //   372: dup
    //   373: invokespecial <init> : ()V
    //   376: astore #4
    //   378: aload #4
    //   380: aload_1
    //   381: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload #4
    //   387: aload_0
    //   388: getfield mTargetIds : Ljava/util/ArrayList;
    //   391: iload_2
    //   392: invokevirtual get : (I)Ljava/lang/Object;
    //   395: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   398: pop
    //   399: aload #4
    //   401: invokevirtual toString : ()Ljava/lang/String;
    //   404: astore #4
    //   406: iinc #2, 1
    //   409: goto -> 320
    //   412: aload_1
    //   413: astore #4
    //   415: aload_0
    //   416: getfield mTargets : Ljava/util/ArrayList;
    //   419: invokevirtual size : ()I
    //   422: ifle -> 519
    //   425: iload_3
    //   426: istore_2
    //   427: aload_1
    //   428: astore #4
    //   430: iload_2
    //   431: aload_0
    //   432: getfield mTargets : Ljava/util/ArrayList;
    //   435: invokevirtual size : ()I
    //   438: if_icmpge -> 519
    //   441: aload_1
    //   442: astore #4
    //   444: iload_2
    //   445: ifle -> 480
    //   448: new java/lang/StringBuilder
    //   451: dup
    //   452: invokespecial <init> : ()V
    //   455: astore #4
    //   457: aload #4
    //   459: aload_1
    //   460: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   463: pop
    //   464: aload #4
    //   466: ldc_w ', '
    //   469: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   472: pop
    //   473: aload #4
    //   475: invokevirtual toString : ()Ljava/lang/String;
    //   478: astore #4
    //   480: new java/lang/StringBuilder
    //   483: dup
    //   484: invokespecial <init> : ()V
    //   487: astore_1
    //   488: aload_1
    //   489: aload #4
    //   491: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   494: pop
    //   495: aload_1
    //   496: aload_0
    //   497: getfield mTargets : Ljava/util/ArrayList;
    //   500: iload_2
    //   501: invokevirtual get : (I)Ljava/lang/Object;
    //   504: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   507: pop
    //   508: aload_1
    //   509: invokevirtual toString : ()Ljava/lang/String;
    //   512: astore_1
    //   513: iinc #2, 1
    //   516: goto -> 427
    //   519: new java/lang/StringBuilder
    //   522: dup
    //   523: invokespecial <init> : ()V
    //   526: astore_1
    //   527: aload_1
    //   528: aload #4
    //   530: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   533: pop
    //   534: aload_1
    //   535: ldc_w ')'
    //   538: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   541: pop
    //   542: aload_1
    //   543: invokevirtual toString : ()Ljava/lang/String;
    //   546: astore #4
    //   548: aload #4
    //   550: areturn
  }
  
  private static class AnimationInfo {
    String mName;
    
    Transition mTransition;
    
    TransitionValues mValues;
    
    View mView;
    
    WindowIdImpl mWindowId;
    
    AnimationInfo(View param1View, String param1String, Transition param1Transition, WindowIdImpl param1WindowIdImpl, TransitionValues param1TransitionValues) {
      this.mView = param1View;
      this.mName = param1String;
      this.mValues = param1TransitionValues;
      this.mWindowId = param1WindowIdImpl;
      this.mTransition = param1Transition;
    }
  }
  
  private static class ArrayListManager {
    static <T> ArrayList<T> add(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList == null)
        arrayList = new ArrayList<T>(); 
      if (!arrayList.contains(param1T))
        arrayList.add(param1T); 
      return arrayList;
    }
    
    static <T> ArrayList<T> remove(ArrayList<T> param1ArrayList, T param1T) {
      ArrayList<T> arrayList = param1ArrayList;
      if (param1ArrayList != null) {
        param1ArrayList.remove(param1T);
        arrayList = param1ArrayList;
        if (param1ArrayList.isEmpty())
          arrayList = null; 
      } 
      return arrayList;
    }
  }
  
  public static abstract class EpicenterCallback {
    public abstract Rect onGetEpicenter(Transition param1Transition);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MatchOrder {}
  
  public static interface TransitionListener {
    void onTransitionCancel(Transition param1Transition);
    
    void onTransitionEnd(Transition param1Transition);
    
    void onTransitionPause(Transition param1Transition);
    
    void onTransitionResume(Transition param1Transition);
    
    void onTransitionStart(Transition param1Transition);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\Transition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */