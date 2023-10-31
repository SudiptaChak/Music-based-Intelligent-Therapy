package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
    } else {
      this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
    } 
  }
  
  public boolean isLongpressEnabled() {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean) {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static {
    
    }
    
    GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context != null) {
        if (this.mListener != null) {
          this.mIsLongpressEnabled = true;
          ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
          int i = viewConfiguration.getScaledTouchSlop();
          int j = viewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = i * i;
          this.mDoubleTapSlopSquare = j * j;
          return;
        } 
        throw new IllegalArgumentException("OnGestureListener must not be null");
      } 
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool1 = this.mAlwaysInBiggerTapRegion;
      boolean bool = false;
      if (!bool1)
        return false; 
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
        return false; 
      int j = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
      int i = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
      if (j * j + i * i < this.mDoubleTapSlopSquare)
        bool = true; 
      return bool;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore #6
      //   6: aload_0
      //   7: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   10: ifnonnull -> 20
      //   13: aload_0
      //   14: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   17: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   20: aload_0
      //   21: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   24: aload_1
      //   25: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   28: iload #6
      //   30: sipush #255
      //   33: iand
      //   34: istore #10
      //   36: iconst_0
      //   37: istore #12
      //   39: iload #10
      //   41: bipush #6
      //   43: if_icmpne -> 52
      //   46: iconst_1
      //   47: istore #6
      //   49: goto -> 55
      //   52: iconst_0
      //   53: istore #6
      //   55: iload #6
      //   57: ifeq -> 69
      //   60: aload_1
      //   61: invokevirtual getActionIndex : ()I
      //   64: istore #7
      //   66: goto -> 72
      //   69: iconst_m1
      //   70: istore #7
      //   72: aload_1
      //   73: invokevirtual getPointerCount : ()I
      //   76: istore #9
      //   78: iconst_0
      //   79: istore #8
      //   81: fconst_0
      //   82: fstore_3
      //   83: fconst_0
      //   84: fstore_2
      //   85: iload #8
      //   87: iload #9
      //   89: if_icmpge -> 126
      //   92: iload #7
      //   94: iload #8
      //   96: if_icmpne -> 102
      //   99: goto -> 120
      //   102: fload_3
      //   103: aload_1
      //   104: iload #8
      //   106: invokevirtual getX : (I)F
      //   109: fadd
      //   110: fstore_3
      //   111: fload_2
      //   112: aload_1
      //   113: iload #8
      //   115: invokevirtual getY : (I)F
      //   118: fadd
      //   119: fstore_2
      //   120: iinc #8, 1
      //   123: goto -> 85
      //   126: iload #6
      //   128: ifeq -> 140
      //   131: iload #9
      //   133: iconst_1
      //   134: isub
      //   135: istore #6
      //   137: goto -> 144
      //   140: iload #9
      //   142: istore #6
      //   144: iload #6
      //   146: i2f
      //   147: fstore #4
      //   149: fload_3
      //   150: fload #4
      //   152: fdiv
      //   153: fstore_3
      //   154: fload_2
      //   155: fload #4
      //   157: fdiv
      //   158: fstore_2
      //   159: iload #10
      //   161: ifeq -> 909
      //   164: iload #10
      //   166: iconst_1
      //   167: if_icmpeq -> 641
      //   170: iload #10
      //   172: iconst_2
      //   173: if_icmpeq -> 393
      //   176: iload #10
      //   178: iconst_3
      //   179: if_icmpeq -> 382
      //   182: iload #10
      //   184: iconst_5
      //   185: if_icmpeq -> 351
      //   188: iload #10
      //   190: bipush #6
      //   192: if_icmpeq -> 202
      //   195: iload #12
      //   197: istore #11
      //   199: goto -> 1176
      //   202: aload_0
      //   203: fload_3
      //   204: putfield mLastFocusX : F
      //   207: aload_0
      //   208: fload_3
      //   209: putfield mDownFocusX : F
      //   212: aload_0
      //   213: fload_2
      //   214: putfield mLastFocusY : F
      //   217: aload_0
      //   218: fload_2
      //   219: putfield mDownFocusY : F
      //   222: aload_0
      //   223: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   226: sipush #1000
      //   229: aload_0
      //   230: getfield mMaximumFlingVelocity : I
      //   233: i2f
      //   234: invokevirtual computeCurrentVelocity : (IF)V
      //   237: aload_1
      //   238: invokevirtual getActionIndex : ()I
      //   241: istore #7
      //   243: aload_1
      //   244: iload #7
      //   246: invokevirtual getPointerId : (I)I
      //   249: istore #6
      //   251: aload_0
      //   252: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   255: iload #6
      //   257: invokevirtual getXVelocity : (I)F
      //   260: fstore_2
      //   261: aload_0
      //   262: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   265: iload #6
      //   267: invokevirtual getYVelocity : (I)F
      //   270: fstore_3
      //   271: iconst_0
      //   272: istore #6
      //   274: iload #12
      //   276: istore #11
      //   278: iload #6
      //   280: iload #9
      //   282: if_icmpge -> 1176
      //   285: iload #6
      //   287: iload #7
      //   289: if_icmpne -> 295
      //   292: goto -> 345
      //   295: aload_1
      //   296: iload #6
      //   298: invokevirtual getPointerId : (I)I
      //   301: istore #8
      //   303: aload_0
      //   304: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   307: iload #8
      //   309: invokevirtual getXVelocity : (I)F
      //   312: fload_2
      //   313: fmul
      //   314: aload_0
      //   315: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   318: iload #8
      //   320: invokevirtual getYVelocity : (I)F
      //   323: fload_3
      //   324: fmul
      //   325: fadd
      //   326: fconst_0
      //   327: fcmpg
      //   328: ifge -> 345
      //   331: aload_0
      //   332: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   335: invokevirtual clear : ()V
      //   338: iload #12
      //   340: istore #11
      //   342: goto -> 1176
      //   345: iinc #6, 1
      //   348: goto -> 274
      //   351: aload_0
      //   352: fload_3
      //   353: putfield mLastFocusX : F
      //   356: aload_0
      //   357: fload_3
      //   358: putfield mDownFocusX : F
      //   361: aload_0
      //   362: fload_2
      //   363: putfield mLastFocusY : F
      //   366: aload_0
      //   367: fload_2
      //   368: putfield mDownFocusY : F
      //   371: aload_0
      //   372: invokespecial cancelTaps : ()V
      //   375: iload #12
      //   377: istore #11
      //   379: goto -> 1176
      //   382: aload_0
      //   383: invokespecial cancel : ()V
      //   386: iload #12
      //   388: istore #11
      //   390: goto -> 1176
      //   393: aload_0
      //   394: getfield mInLongPress : Z
      //   397: ifeq -> 407
      //   400: iload #12
      //   402: istore #11
      //   404: goto -> 1176
      //   407: aload_0
      //   408: getfield mLastFocusX : F
      //   411: fload_3
      //   412: fsub
      //   413: fstore #5
      //   415: aload_0
      //   416: getfield mLastFocusY : F
      //   419: fload_2
      //   420: fsub
      //   421: fstore #4
      //   423: aload_0
      //   424: getfield mIsDoubleTapping : Z
      //   427: ifeq -> 447
      //   430: iconst_0
      //   431: aload_0
      //   432: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   435: aload_1
      //   436: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   441: ior
      //   442: istore #11
      //   444: goto -> 1176
      //   447: aload_0
      //   448: getfield mAlwaysInTapRegion : Z
      //   451: ifeq -> 584
      //   454: fload_3
      //   455: aload_0
      //   456: getfield mDownFocusX : F
      //   459: fsub
      //   460: f2i
      //   461: istore #6
      //   463: fload_2
      //   464: aload_0
      //   465: getfield mDownFocusY : F
      //   468: fsub
      //   469: f2i
      //   470: istore #7
      //   472: iload #6
      //   474: iload #6
      //   476: imul
      //   477: iload #7
      //   479: iload #7
      //   481: imul
      //   482: iadd
      //   483: istore #6
      //   485: iload #6
      //   487: aload_0
      //   488: getfield mTouchSlopSquare : I
      //   491: if_icmple -> 556
      //   494: aload_0
      //   495: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   498: aload_0
      //   499: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   502: aload_1
      //   503: fload #5
      //   505: fload #4
      //   507: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   512: istore #12
      //   514: aload_0
      //   515: fload_3
      //   516: putfield mLastFocusX : F
      //   519: aload_0
      //   520: fload_2
      //   521: putfield mLastFocusY : F
      //   524: aload_0
      //   525: iconst_0
      //   526: putfield mAlwaysInTapRegion : Z
      //   529: aload_0
      //   530: getfield mHandler : Landroid/os/Handler;
      //   533: iconst_3
      //   534: invokevirtual removeMessages : (I)V
      //   537: aload_0
      //   538: getfield mHandler : Landroid/os/Handler;
      //   541: iconst_1
      //   542: invokevirtual removeMessages : (I)V
      //   545: aload_0
      //   546: getfield mHandler : Landroid/os/Handler;
      //   549: iconst_2
      //   550: invokevirtual removeMessages : (I)V
      //   553: goto -> 559
      //   556: iconst_0
      //   557: istore #12
      //   559: iload #12
      //   561: istore #11
      //   563: iload #6
      //   565: aload_0
      //   566: getfield mTouchSlopSquare : I
      //   569: if_icmple -> 906
      //   572: aload_0
      //   573: iconst_0
      //   574: putfield mAlwaysInBiggerTapRegion : Z
      //   577: iload #12
      //   579: istore #11
      //   581: goto -> 906
      //   584: fload #5
      //   586: invokestatic abs : (F)F
      //   589: fconst_1
      //   590: fcmpl
      //   591: ifge -> 608
      //   594: iload #12
      //   596: istore #11
      //   598: fload #4
      //   600: invokestatic abs : (F)F
      //   603: fconst_1
      //   604: fcmpl
      //   605: iflt -> 1176
      //   608: aload_0
      //   609: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   612: aload_0
      //   613: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   616: aload_1
      //   617: fload #5
      //   619: fload #4
      //   621: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   626: istore #11
      //   628: aload_0
      //   629: fload_3
      //   630: putfield mLastFocusX : F
      //   633: aload_0
      //   634: fload_2
      //   635: putfield mLastFocusY : F
      //   638: goto -> 1176
      //   641: aload_0
      //   642: iconst_0
      //   643: putfield mStillDown : Z
      //   646: aload_1
      //   647: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   650: astore #13
      //   652: aload_0
      //   653: getfield mIsDoubleTapping : Z
      //   656: ifeq -> 676
      //   659: aload_0
      //   660: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   663: aload_1
      //   664: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   669: iconst_0
      //   670: ior
      //   671: istore #11
      //   673: goto -> 843
      //   676: aload_0
      //   677: getfield mInLongPress : Z
      //   680: ifeq -> 699
      //   683: aload_0
      //   684: getfield mHandler : Landroid/os/Handler;
      //   687: iconst_3
      //   688: invokevirtual removeMessages : (I)V
      //   691: aload_0
      //   692: iconst_0
      //   693: putfield mInLongPress : Z
      //   696: goto -> 819
      //   699: aload_0
      //   700: getfield mAlwaysInTapRegion : Z
      //   703: ifeq -> 748
      //   706: aload_0
      //   707: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   710: aload_1
      //   711: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   716: istore #11
      //   718: aload_0
      //   719: getfield mDeferConfirmSingleTap : Z
      //   722: ifeq -> 745
      //   725: aload_0
      //   726: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   729: astore #14
      //   731: aload #14
      //   733: ifnull -> 745
      //   736: aload #14
      //   738: aload_1
      //   739: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   744: pop
      //   745: goto -> 843
      //   748: aload_0
      //   749: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   752: astore #14
      //   754: aload_1
      //   755: iconst_0
      //   756: invokevirtual getPointerId : (I)I
      //   759: istore #6
      //   761: aload #14
      //   763: sipush #1000
      //   766: aload_0
      //   767: getfield mMaximumFlingVelocity : I
      //   770: i2f
      //   771: invokevirtual computeCurrentVelocity : (IF)V
      //   774: aload #14
      //   776: iload #6
      //   778: invokevirtual getYVelocity : (I)F
      //   781: fstore_2
      //   782: aload #14
      //   784: iload #6
      //   786: invokevirtual getXVelocity : (I)F
      //   789: fstore_3
      //   790: fload_2
      //   791: invokestatic abs : (F)F
      //   794: aload_0
      //   795: getfield mMinimumFlingVelocity : I
      //   798: i2f
      //   799: fcmpl
      //   800: ifgt -> 825
      //   803: fload_3
      //   804: invokestatic abs : (F)F
      //   807: aload_0
      //   808: getfield mMinimumFlingVelocity : I
      //   811: i2f
      //   812: fcmpl
      //   813: ifle -> 819
      //   816: goto -> 825
      //   819: iconst_0
      //   820: istore #11
      //   822: goto -> 843
      //   825: aload_0
      //   826: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   829: aload_0
      //   830: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   833: aload_1
      //   834: fload_3
      //   835: fload_2
      //   836: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   841: istore #11
      //   843: aload_0
      //   844: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   847: astore_1
      //   848: aload_1
      //   849: ifnull -> 856
      //   852: aload_1
      //   853: invokevirtual recycle : ()V
      //   856: aload_0
      //   857: aload #13
      //   859: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   862: aload_0
      //   863: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   866: astore_1
      //   867: aload_1
      //   868: ifnull -> 880
      //   871: aload_1
      //   872: invokevirtual recycle : ()V
      //   875: aload_0
      //   876: aconst_null
      //   877: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   880: aload_0
      //   881: iconst_0
      //   882: putfield mIsDoubleTapping : Z
      //   885: aload_0
      //   886: iconst_0
      //   887: putfield mDeferConfirmSingleTap : Z
      //   890: aload_0
      //   891: getfield mHandler : Landroid/os/Handler;
      //   894: iconst_1
      //   895: invokevirtual removeMessages : (I)V
      //   898: aload_0
      //   899: getfield mHandler : Landroid/os/Handler;
      //   902: iconst_2
      //   903: invokevirtual removeMessages : (I)V
      //   906: goto -> 1176
      //   909: aload_0
      //   910: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   913: ifnull -> 1027
      //   916: aload_0
      //   917: getfield mHandler : Landroid/os/Handler;
      //   920: iconst_3
      //   921: invokevirtual hasMessages : (I)Z
      //   924: istore #11
      //   926: iload #11
      //   928: ifeq -> 939
      //   931: aload_0
      //   932: getfield mHandler : Landroid/os/Handler;
      //   935: iconst_3
      //   936: invokevirtual removeMessages : (I)V
      //   939: aload_0
      //   940: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   943: astore #13
      //   945: aload #13
      //   947: ifnull -> 1014
      //   950: aload_0
      //   951: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   954: astore #14
      //   956: aload #14
      //   958: ifnull -> 1014
      //   961: iload #11
      //   963: ifeq -> 1014
      //   966: aload_0
      //   967: aload #13
      //   969: aload #14
      //   971: aload_1
      //   972: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   975: ifeq -> 1014
      //   978: aload_0
      //   979: iconst_1
      //   980: putfield mIsDoubleTapping : Z
      //   983: aload_0
      //   984: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   987: aload_0
      //   988: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   991: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   996: iconst_0
      //   997: ior
      //   998: aload_0
      //   999: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1002: aload_1
      //   1003: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   1008: ior
      //   1009: istore #6
      //   1011: goto -> 1030
      //   1014: aload_0
      //   1015: getfield mHandler : Landroid/os/Handler;
      //   1018: iconst_3
      //   1019: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   1022: i2l
      //   1023: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   1026: pop
      //   1027: iconst_0
      //   1028: istore #6
      //   1030: aload_0
      //   1031: fload_3
      //   1032: putfield mLastFocusX : F
      //   1035: aload_0
      //   1036: fload_3
      //   1037: putfield mDownFocusX : F
      //   1040: aload_0
      //   1041: fload_2
      //   1042: putfield mLastFocusY : F
      //   1045: aload_0
      //   1046: fload_2
      //   1047: putfield mDownFocusY : F
      //   1050: aload_0
      //   1051: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1054: astore #13
      //   1056: aload #13
      //   1058: ifnull -> 1066
      //   1061: aload #13
      //   1063: invokevirtual recycle : ()V
      //   1066: aload_0
      //   1067: aload_1
      //   1068: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   1071: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1074: aload_0
      //   1075: iconst_1
      //   1076: putfield mAlwaysInTapRegion : Z
      //   1079: aload_0
      //   1080: iconst_1
      //   1081: putfield mAlwaysInBiggerTapRegion : Z
      //   1084: aload_0
      //   1085: iconst_1
      //   1086: putfield mStillDown : Z
      //   1089: aload_0
      //   1090: iconst_0
      //   1091: putfield mInLongPress : Z
      //   1094: aload_0
      //   1095: iconst_0
      //   1096: putfield mDeferConfirmSingleTap : Z
      //   1099: aload_0
      //   1100: getfield mIsLongpressEnabled : Z
      //   1103: ifeq -> 1140
      //   1106: aload_0
      //   1107: getfield mHandler : Landroid/os/Handler;
      //   1110: iconst_2
      //   1111: invokevirtual removeMessages : (I)V
      //   1114: aload_0
      //   1115: getfield mHandler : Landroid/os/Handler;
      //   1118: iconst_2
      //   1119: aload_0
      //   1120: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1123: invokevirtual getDownTime : ()J
      //   1126: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1129: i2l
      //   1130: ladd
      //   1131: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT : I
      //   1134: i2l
      //   1135: ladd
      //   1136: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1139: pop
      //   1140: aload_0
      //   1141: getfield mHandler : Landroid/os/Handler;
      //   1144: iconst_1
      //   1145: aload_0
      //   1146: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1149: invokevirtual getDownTime : ()J
      //   1152: getstatic androidx/core/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1155: i2l
      //   1156: ladd
      //   1157: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1160: pop
      //   1161: iload #6
      //   1163: aload_0
      //   1164: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1167: aload_1
      //   1168: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   1173: ior
      //   1174: istore #11
      //   1176: iload #11
      //   1178: ireturn
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mIsLongpressEnabled = param1Boolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDoubleTapListener = param1OnDoubleTapListener;
    }
    
    private class GestureHandler extends Handler {
      final GestureDetectorCompat.GestureDetectorCompatImplBase this$0;
      
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) {
        super(param2Handler.getLooper());
      }
      
      public void handleMessage(Message param2Message) {
        int i = param2Message.what;
        if (i != 1) {
          if (i != 2) {
            if (i == 3) {
              if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null)
                if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
                  GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                } else {
                  GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                }  
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Unknown message ");
              stringBuilder.append(param2Message);
              throw new RuntimeException(stringBuilder.toString());
            } 
          } else {
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          } 
        } else {
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
        } 
      }
    }
  }
  
  private class GestureHandler extends Handler {
    final GestureDetectorCompat.GestureDetectorCompatImplBase this$0;
    
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) {
      super(param1Handler.getLooper());
    }
    
    public void handleMessage(Message param1Message) {
      int i = param1Message.what;
      if (i != 1) {
        if (i != 2) {
          if (i == 3) {
            if (this.this$0.mDoubleTapListener != null)
              if (!this.this$0.mStillDown) {
                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
              } else {
                this.this$0.mDeferConfirmSingleTap = true;
              }  
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown message ");
            stringBuilder.append(param1Message);
            throw new RuntimeException(stringBuilder.toString());
          } 
        } else {
          this.this$0.dispatchLongPress();
        } 
      } else {
        this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
      } 
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler);
    }
    
    public boolean isLongpressEnabled() {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      return this.mDetector.onTouchEvent(param1MotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mDetector.setIsLongpressEnabled(param1Boolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\view\GestureDetectorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */