package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;

public class ChangeBounds extends Transition {
  private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
  
  private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
  
  private static final Property<View, PointF> POSITION_PROPERTY;
  
  private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
  
  private static final String PROPNAME_CLIP = "android:changeBounds:clip";
  
  private static final String PROPNAME_PARENT = "android:changeBounds:parent";
  
  private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
  
  private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
  
  private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
  
  private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
  
  private static RectEvaluator sRectEvaluator;
  
  private static final String[] sTransitionProperties = new String[] { "android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY" };
  
  private boolean mReparent = false;
  
  private boolean mResizeClip = false;
  
  private int[] mTempLocation = new int[2];
  
  static {
    DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();
        
        public PointF get(Drawable param1Drawable) {
          param1Drawable.copyBounds(this.mBounds);
          return new PointF(this.mBounds.left, this.mBounds.top);
        }
        
        public void set(Drawable param1Drawable, PointF param1PointF) {
          param1Drawable.copyBounds(this.mBounds);
          this.mBounds.offsetTo(Math.round(param1PointF.x), Math.round(param1PointF.y));
          param1Drawable.setBounds(this.mBounds);
        }
      };
    TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setTopLeft(param1PointF);
        }
      };
    BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight") {
        public PointF get(ChangeBounds.ViewBounds param1ViewBounds) {
          return null;
        }
        
        public void set(ChangeBounds.ViewBounds param1ViewBounds, PointF param1PointF) {
          param1ViewBounds.setBottomRight(param1PointF);
        }
      };
    BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, param1View.getLeft(), param1View.getTop(), Math.round(param1PointF.x), Math.round(param1PointF.y));
        }
      };
    TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          ViewUtils.setLeftTopRightBottom(param1View, Math.round(param1PointF.x), Math.round(param1PointF.y), param1View.getRight(), param1View.getBottom());
        }
      };
    POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position") {
        public PointF get(View param1View) {
          return null;
        }
        
        public void set(View param1View, PointF param1PointF) {
          int i = Math.round(param1PointF.x);
          int j = Math.round(param1PointF.y);
          ViewUtils.setLeftTopRightBottom(param1View, i, j, param1View.getWidth() + i, param1View.getHeight() + j);
        }
      };
    sRectEvaluator = new RectEvaluator();
  }
  
  public ChangeBounds() {}
  
  public ChangeBounds(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.CHANGE_BOUNDS);
    boolean bool = TypedArrayUtils.getNamedBoolean(typedArray, (XmlPullParser)paramAttributeSet, "resizeClip", 0, false);
    typedArray.recycle();
    setResizeClip(bool);
  }
  
  private void captureValues(TransitionValues paramTransitionValues) {
    View view = paramTransitionValues.view;
    if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
      paramTransitionValues.values.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
      paramTransitionValues.values.put("android:changeBounds:parent", paramTransitionValues.view.getParent());
      if (this.mReparent) {
        paramTransitionValues.view.getLocationInWindow(this.mTempLocation);
        paramTransitionValues.values.put("android:changeBounds:windowX", Integer.valueOf(this.mTempLocation[0]));
        paramTransitionValues.values.put("android:changeBounds:windowY", Integer.valueOf(this.mTempLocation[1]));
      } 
      if (this.mResizeClip)
        paramTransitionValues.values.put("android:changeBounds:clip", ViewCompat.getClipBounds(view)); 
    } 
  }
  
  private boolean parentMatches(View paramView1, View paramView2) {
    boolean bool = this.mReparent;
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (bool) {
      TransitionValues transitionValues = getMatchedTransitionValues(paramView1, true);
      if (transitionValues == null) {
        if (paramView1 == paramView2)
          return bool2; 
      } else if (paramView2 == transitionValues.view) {
        return bool2;
      } 
      bool1 = false;
    } 
    return bool1;
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues) {
    captureValues(paramTransitionValues);
  }
  
  public Animator createAnimator(ViewGroup paramViewGroup, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2) {
    // Byte code:
    //   0: aload_2
    //   1: ifnull -> 1101
    //   4: aload_3
    //   5: ifnonnull -> 11
    //   8: goto -> 1101
    //   11: aload_2
    //   12: getfield values : Ljava/util/Map;
    //   15: astore #19
    //   17: aload_3
    //   18: getfield values : Ljava/util/Map;
    //   21: astore #20
    //   23: aload #19
    //   25: ldc 'android:changeBounds:parent'
    //   27: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   32: checkcast android/view/ViewGroup
    //   35: astore #19
    //   37: aload #20
    //   39: ldc 'android:changeBounds:parent'
    //   41: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   46: checkcast android/view/ViewGroup
    //   49: astore #21
    //   51: aload #19
    //   53: ifnull -> 1099
    //   56: aload #21
    //   58: ifnonnull -> 64
    //   61: goto -> 1099
    //   64: aload_3
    //   65: getfield view : Landroid/view/View;
    //   68: astore #20
    //   70: aload_0
    //   71: aload #19
    //   73: aload #21
    //   75: invokespecial parentMatches : (Landroid/view/View;Landroid/view/View;)Z
    //   78: ifeq -> 847
    //   81: aload_2
    //   82: getfield values : Ljava/util/Map;
    //   85: ldc 'android:changeBounds:bounds'
    //   87: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   92: checkcast android/graphics/Rect
    //   95: astore #19
    //   97: aload_3
    //   98: getfield values : Ljava/util/Map;
    //   101: ldc 'android:changeBounds:bounds'
    //   103: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast android/graphics/Rect
    //   111: astore_1
    //   112: aload #19
    //   114: getfield left : I
    //   117: istore #10
    //   119: aload_1
    //   120: getfield left : I
    //   123: istore #15
    //   125: aload #19
    //   127: getfield top : I
    //   130: istore #12
    //   132: aload_1
    //   133: getfield top : I
    //   136: istore #16
    //   138: aload #19
    //   140: getfield right : I
    //   143: istore #17
    //   145: aload_1
    //   146: getfield right : I
    //   149: istore #13
    //   151: aload #19
    //   153: getfield bottom : I
    //   156: istore #7
    //   158: aload_1
    //   159: getfield bottom : I
    //   162: istore #9
    //   164: iload #17
    //   166: iload #10
    //   168: isub
    //   169: istore #11
    //   171: iload #7
    //   173: iload #12
    //   175: isub
    //   176: istore #14
    //   178: iload #13
    //   180: iload #15
    //   182: isub
    //   183: istore #8
    //   185: iload #9
    //   187: iload #16
    //   189: isub
    //   190: istore #18
    //   192: aload_2
    //   193: getfield values : Ljava/util/Map;
    //   196: ldc 'android:changeBounds:clip'
    //   198: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   203: checkcast android/graphics/Rect
    //   206: astore_2
    //   207: aload_3
    //   208: getfield values : Ljava/util/Map;
    //   211: ldc 'android:changeBounds:clip'
    //   213: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   218: checkcast android/graphics/Rect
    //   221: astore #19
    //   223: iload #11
    //   225: ifeq -> 233
    //   228: iload #14
    //   230: ifne -> 243
    //   233: iload #8
    //   235: ifeq -> 296
    //   238: iload #18
    //   240: ifeq -> 296
    //   243: iload #10
    //   245: iload #15
    //   247: if_icmpne -> 266
    //   250: iload #12
    //   252: iload #16
    //   254: if_icmpeq -> 260
    //   257: goto -> 266
    //   260: iconst_0
    //   261: istore #6
    //   263: goto -> 269
    //   266: iconst_1
    //   267: istore #6
    //   269: iload #17
    //   271: iload #13
    //   273: if_icmpne -> 287
    //   276: iload #6
    //   278: istore #5
    //   280: iload #7
    //   282: iload #9
    //   284: if_icmpeq -> 299
    //   287: iload #6
    //   289: iconst_1
    //   290: iadd
    //   291: istore #5
    //   293: goto -> 299
    //   296: iconst_0
    //   297: istore #5
    //   299: aload_2
    //   300: ifnull -> 312
    //   303: aload_2
    //   304: aload #19
    //   306: invokevirtual equals : (Ljava/lang/Object;)Z
    //   309: ifeq -> 329
    //   312: iload #5
    //   314: istore #6
    //   316: aload_2
    //   317: ifnonnull -> 335
    //   320: iload #5
    //   322: istore #6
    //   324: aload #19
    //   326: ifnull -> 335
    //   329: iload #5
    //   331: iconst_1
    //   332: iadd
    //   333: istore #6
    //   335: iload #6
    //   337: ifle -> 940
    //   340: aload_0
    //   341: getfield mResizeClip : Z
    //   344: ifne -> 606
    //   347: aload #20
    //   349: iload #10
    //   351: iload #12
    //   353: iload #17
    //   355: iload #7
    //   357: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   360: iload #6
    //   362: iconst_2
    //   363: if_icmpne -> 523
    //   366: iload #11
    //   368: iload #8
    //   370: if_icmpne -> 413
    //   373: iload #14
    //   375: iload #18
    //   377: if_icmpne -> 413
    //   380: aload_0
    //   381: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   384: iload #10
    //   386: i2f
    //   387: iload #12
    //   389: i2f
    //   390: iload #15
    //   392: i2f
    //   393: iload #16
    //   395: i2f
    //   396: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   399: astore_1
    //   400: aload #20
    //   402: getstatic androidx/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   405: aload_1
    //   406: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   409: astore_1
    //   410: goto -> 806
    //   413: new androidx/transition/ChangeBounds$ViewBounds
    //   416: dup
    //   417: aload #20
    //   419: invokespecial <init> : (Landroid/view/View;)V
    //   422: astore_2
    //   423: aload_0
    //   424: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   427: iload #10
    //   429: i2f
    //   430: iload #12
    //   432: i2f
    //   433: iload #15
    //   435: i2f
    //   436: iload #16
    //   438: i2f
    //   439: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   442: astore_1
    //   443: aload_2
    //   444: getstatic androidx/transition/ChangeBounds.TOP_LEFT_PROPERTY : Landroid/util/Property;
    //   447: aload_1
    //   448: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   451: astore_3
    //   452: aload_0
    //   453: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   456: iload #17
    //   458: i2f
    //   459: iload #7
    //   461: i2f
    //   462: iload #13
    //   464: i2f
    //   465: iload #9
    //   467: i2f
    //   468: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   471: astore_1
    //   472: aload_2
    //   473: getstatic androidx/transition/ChangeBounds.BOTTOM_RIGHT_PROPERTY : Landroid/util/Property;
    //   476: aload_1
    //   477: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   480: astore #19
    //   482: new android/animation/AnimatorSet
    //   485: dup
    //   486: invokespecial <init> : ()V
    //   489: astore_1
    //   490: aload_1
    //   491: iconst_2
    //   492: anewarray android/animation/Animator
    //   495: dup
    //   496: iconst_0
    //   497: aload_3
    //   498: aastore
    //   499: dup
    //   500: iconst_1
    //   501: aload #19
    //   503: aastore
    //   504: invokevirtual playTogether : ([Landroid/animation/Animator;)V
    //   507: aload_1
    //   508: new androidx/transition/ChangeBounds$7
    //   511: dup
    //   512: aload_0
    //   513: aload_2
    //   514: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroidx/transition/ChangeBounds$ViewBounds;)V
    //   517: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   520: goto -> 806
    //   523: iload #10
    //   525: iload #15
    //   527: if_icmpne -> 573
    //   530: iload #12
    //   532: iload #16
    //   534: if_icmpeq -> 540
    //   537: goto -> 573
    //   540: aload_0
    //   541: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   544: iload #17
    //   546: i2f
    //   547: iload #7
    //   549: i2f
    //   550: iload #13
    //   552: i2f
    //   553: iload #9
    //   555: i2f
    //   556: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   559: astore_1
    //   560: aload #20
    //   562: getstatic androidx/transition/ChangeBounds.BOTTOM_RIGHT_ONLY_PROPERTY : Landroid/util/Property;
    //   565: aload_1
    //   566: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   569: astore_1
    //   570: goto -> 806
    //   573: aload_0
    //   574: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   577: iload #10
    //   579: i2f
    //   580: iload #12
    //   582: i2f
    //   583: iload #15
    //   585: i2f
    //   586: iload #16
    //   588: i2f
    //   589: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   592: astore_1
    //   593: aload #20
    //   595: getstatic androidx/transition/ChangeBounds.TOP_LEFT_ONLY_PROPERTY : Landroid/util/Property;
    //   598: aload_1
    //   599: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   602: astore_1
    //   603: goto -> 806
    //   606: aload #20
    //   608: iload #10
    //   610: iload #12
    //   612: iload #11
    //   614: iload #8
    //   616: invokestatic max : (II)I
    //   619: iload #10
    //   621: iadd
    //   622: iload #14
    //   624: iload #18
    //   626: invokestatic max : (II)I
    //   629: iload #12
    //   631: iadd
    //   632: invokestatic setLeftTopRightBottom : (Landroid/view/View;IIII)V
    //   635: iload #10
    //   637: iload #15
    //   639: if_icmpne -> 657
    //   642: iload #12
    //   644: iload #16
    //   646: if_icmpeq -> 652
    //   649: goto -> 657
    //   652: aconst_null
    //   653: astore_1
    //   654: goto -> 687
    //   657: aload_0
    //   658: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   661: iload #10
    //   663: i2f
    //   664: iload #12
    //   666: i2f
    //   667: iload #15
    //   669: i2f
    //   670: iload #16
    //   672: i2f
    //   673: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   676: astore_1
    //   677: aload #20
    //   679: getstatic androidx/transition/ChangeBounds.POSITION_PROPERTY : Landroid/util/Property;
    //   682: aload_1
    //   683: invokestatic ofPointF : (Ljava/lang/Object;Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/ObjectAnimator;
    //   686: astore_1
    //   687: aload_2
    //   688: ifnonnull -> 708
    //   691: new android/graphics/Rect
    //   694: dup
    //   695: iconst_0
    //   696: iconst_0
    //   697: iload #11
    //   699: iload #14
    //   701: invokespecial <init> : (IIII)V
    //   704: astore_2
    //   705: goto -> 708
    //   708: aload #19
    //   710: ifnonnull -> 730
    //   713: new android/graphics/Rect
    //   716: dup
    //   717: iconst_0
    //   718: iconst_0
    //   719: iload #8
    //   721: iload #18
    //   723: invokespecial <init> : (IIII)V
    //   726: astore_3
    //   727: goto -> 733
    //   730: aload #19
    //   732: astore_3
    //   733: aload_2
    //   734: aload_3
    //   735: invokevirtual equals : (Ljava/lang/Object;)Z
    //   738: ifne -> 798
    //   741: aload #20
    //   743: aload_2
    //   744: invokestatic setClipBounds : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   747: aload #20
    //   749: ldc_w 'clipBounds'
    //   752: getstatic androidx/transition/ChangeBounds.sRectEvaluator : Landroidx/transition/RectEvaluator;
    //   755: iconst_2
    //   756: anewarray java/lang/Object
    //   759: dup
    //   760: iconst_0
    //   761: aload_2
    //   762: aastore
    //   763: dup
    //   764: iconst_1
    //   765: aload_3
    //   766: aastore
    //   767: invokestatic ofObject : (Ljava/lang/Object;Ljava/lang/String;Landroid/animation/TypeEvaluator;[Ljava/lang/Object;)Landroid/animation/ObjectAnimator;
    //   770: astore_2
    //   771: aload_2
    //   772: new androidx/transition/ChangeBounds$8
    //   775: dup
    //   776: aload_0
    //   777: aload #20
    //   779: aload #19
    //   781: iload #15
    //   783: iload #16
    //   785: iload #13
    //   787: iload #9
    //   789: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/View;Landroid/graphics/Rect;IIII)V
    //   792: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   795: goto -> 800
    //   798: aconst_null
    //   799: astore_2
    //   800: aload_1
    //   801: aload_2
    //   802: invokestatic mergeAnimators : (Landroid/animation/Animator;Landroid/animation/Animator;)Landroid/animation/Animator;
    //   805: astore_1
    //   806: aload #20
    //   808: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   811: instanceof android/view/ViewGroup
    //   814: ifeq -> 845
    //   817: aload #20
    //   819: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   822: checkcast android/view/ViewGroup
    //   825: astore_2
    //   826: aload_2
    //   827: iconst_1
    //   828: invokestatic suppressLayout : (Landroid/view/ViewGroup;Z)V
    //   831: aload_0
    //   832: new androidx/transition/ChangeBounds$9
    //   835: dup
    //   836: aload_0
    //   837: aload_2
    //   838: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/ViewGroup;)V
    //   841: invokevirtual addListener : (Landroidx/transition/Transition$TransitionListener;)Landroidx/transition/Transition;
    //   844: pop
    //   845: aload_1
    //   846: areturn
    //   847: aload_2
    //   848: getfield values : Ljava/util/Map;
    //   851: ldc 'android:changeBounds:windowX'
    //   853: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   858: checkcast java/lang/Integer
    //   861: invokevirtual intValue : ()I
    //   864: istore #6
    //   866: aload_2
    //   867: getfield values : Ljava/util/Map;
    //   870: ldc 'android:changeBounds:windowY'
    //   872: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   877: checkcast java/lang/Integer
    //   880: invokevirtual intValue : ()I
    //   883: istore #7
    //   885: aload_3
    //   886: getfield values : Ljava/util/Map;
    //   889: ldc 'android:changeBounds:windowX'
    //   891: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   896: checkcast java/lang/Integer
    //   899: invokevirtual intValue : ()I
    //   902: istore #8
    //   904: aload_3
    //   905: getfield values : Ljava/util/Map;
    //   908: ldc 'android:changeBounds:windowY'
    //   910: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   915: checkcast java/lang/Integer
    //   918: invokevirtual intValue : ()I
    //   921: istore #5
    //   923: iload #6
    //   925: iload #8
    //   927: if_icmpne -> 942
    //   930: iload #7
    //   932: iload #5
    //   934: if_icmpeq -> 940
    //   937: goto -> 942
    //   940: aconst_null
    //   941: areturn
    //   942: aload_1
    //   943: aload_0
    //   944: getfield mTempLocation : [I
    //   947: invokevirtual getLocationInWindow : ([I)V
    //   950: aload #20
    //   952: invokevirtual getWidth : ()I
    //   955: aload #20
    //   957: invokevirtual getHeight : ()I
    //   960: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   963: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   966: astore_2
    //   967: aload #20
    //   969: new android/graphics/Canvas
    //   972: dup
    //   973: aload_2
    //   974: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   977: invokevirtual draw : (Landroid/graphics/Canvas;)V
    //   980: new android/graphics/drawable/BitmapDrawable
    //   983: dup
    //   984: aload_2
    //   985: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   988: astore_2
    //   989: aload #20
    //   991: invokestatic getTransitionAlpha : (Landroid/view/View;)F
    //   994: fstore #4
    //   996: aload #20
    //   998: fconst_0
    //   999: invokestatic setTransitionAlpha : (Landroid/view/View;F)V
    //   1002: aload_1
    //   1003: invokestatic getOverlay : (Landroid/view/View;)Landroidx/transition/ViewOverlayImpl;
    //   1006: aload_2
    //   1007: invokeinterface add : (Landroid/graphics/drawable/Drawable;)V
    //   1012: aload_0
    //   1013: invokevirtual getPathMotion : ()Landroidx/transition/PathMotion;
    //   1016: astore_3
    //   1017: aload_0
    //   1018: getfield mTempLocation : [I
    //   1021: astore #19
    //   1023: aload_3
    //   1024: iload #6
    //   1026: aload #19
    //   1028: iconst_0
    //   1029: iaload
    //   1030: isub
    //   1031: i2f
    //   1032: iload #7
    //   1034: aload #19
    //   1036: iconst_1
    //   1037: iaload
    //   1038: isub
    //   1039: i2f
    //   1040: iload #8
    //   1042: aload #19
    //   1044: iconst_0
    //   1045: iaload
    //   1046: isub
    //   1047: i2f
    //   1048: iload #5
    //   1050: aload #19
    //   1052: iconst_1
    //   1053: iaload
    //   1054: isub
    //   1055: i2f
    //   1056: invokevirtual getPath : (FFFF)Landroid/graphics/Path;
    //   1059: astore_3
    //   1060: aload_2
    //   1061: iconst_1
    //   1062: anewarray android/animation/PropertyValuesHolder
    //   1065: dup
    //   1066: iconst_0
    //   1067: getstatic androidx/transition/ChangeBounds.DRAWABLE_ORIGIN_PROPERTY : Landroid/util/Property;
    //   1070: aload_3
    //   1071: invokestatic ofPointF : (Landroid/util/Property;Landroid/graphics/Path;)Landroid/animation/PropertyValuesHolder;
    //   1074: aastore
    //   1075: invokestatic ofPropertyValuesHolder : (Ljava/lang/Object;[Landroid/animation/PropertyValuesHolder;)Landroid/animation/ObjectAnimator;
    //   1078: astore_3
    //   1079: aload_3
    //   1080: new androidx/transition/ChangeBounds$10
    //   1083: dup
    //   1084: aload_0
    //   1085: aload_1
    //   1086: aload_2
    //   1087: aload #20
    //   1089: fload #4
    //   1091: invokespecial <init> : (Landroidx/transition/ChangeBounds;Landroid/view/ViewGroup;Landroid/graphics/drawable/BitmapDrawable;Landroid/view/View;F)V
    //   1094: invokevirtual addListener : (Landroid/animation/Animator$AnimatorListener;)V
    //   1097: aload_3
    //   1098: areturn
    //   1099: aconst_null
    //   1100: areturn
    //   1101: aconst_null
    //   1102: areturn
  }
  
  public boolean getResizeClip() {
    return this.mResizeClip;
  }
  
  public String[] getTransitionProperties() {
    return sTransitionProperties;
  }
  
  public void setResizeClip(boolean paramBoolean) {
    this.mResizeClip = paramBoolean;
  }
  
  private static class ViewBounds {
    private int mBottom;
    
    private int mBottomRightCalls;
    
    private int mLeft;
    
    private int mRight;
    
    private int mTop;
    
    private int mTopLeftCalls;
    
    private View mView;
    
    ViewBounds(View param1View) {
      this.mView = param1View;
    }
    
    private void setLeftTopRightBottom() {
      ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
      this.mTopLeftCalls = 0;
      this.mBottomRightCalls = 0;
    }
    
    void setBottomRight(PointF param1PointF) {
      this.mRight = Math.round(param1PointF.x);
      this.mBottom = Math.round(param1PointF.y);
      int i = this.mBottomRightCalls + 1;
      this.mBottomRightCalls = i;
      if (this.mTopLeftCalls == i)
        setLeftTopRightBottom(); 
    }
    
    void setTopLeft(PointF param1PointF) {
      this.mLeft = Math.round(param1PointF.x);
      this.mTop = Math.round(param1PointF.y);
      int i = this.mTopLeftCalls + 1;
      this.mTopLeftCalls = i;
      if (i == this.mBottomRightCalls)
        setLeftTopRightBottom(); 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\ChangeBounds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */