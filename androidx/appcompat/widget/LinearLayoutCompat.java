package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
  
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        if (bool) {
          i = getPaddingLeft();
        } else {
          i = getWidth() - getPaddingRight();
          int k = this.mDividerWidth;
          i -= k;
        } 
      } else {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          i = view.getLeft() - layoutParams.leftMargin;
          k = this.mDividerWidth;
        } else {
          i = view.getRight() + layoutParams.rightMargin;
          drawVerticalDivider(paramCanvas, i);
        } 
        i -= k;
      } 
    } else {
      return;
    } 
    drawVerticalDivider(paramCanvas, i);
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        i = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, i);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int j = getChildCount();
    int i = this.mBaselineAlignedChildIndex;
    if (j > i) {
      View view = getChildAt(i);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      j = this.mBaselineChildTop;
      i = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        i = j;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              i = j;
            } else {
              i = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            i = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return i + ((LayoutParams)view.getLayoutParams()).topMargin + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramInt == 0) {
      bool1 = bool3;
      if ((this.mShowDividers & 0x1) != 0)
        bool1 = true; 
      return bool1;
    } 
    if (paramInt == getChildCount()) {
      if ((this.mShowDividers & 0x4) != 0)
        bool1 = true; 
      return bool1;
    } 
    bool1 = bool2;
    if ((this.mShowDividers & 0x2) != 0) {
      paramInt--;
      while (true) {
        bool1 = bool2;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8) {
            bool1 = true;
            break;
          } 
          paramInt--;
          continue;
        } 
        break;
      } 
    } 
    return bool1;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool2 = ViewUtils.isLayoutRtl((View)this);
    int k = getPaddingTop();
    int n = paramInt4 - paramInt2;
    int m = getPaddingBottom();
    int i1 = getPaddingBottom();
    int j = getVirtualChildCount();
    paramInt2 = this.mGravity;
    paramInt4 = paramInt2 & 0x70;
    boolean bool1 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    paramInt2 = GravityCompat.getAbsoluteGravity(0x800007 & paramInt2, ViewCompat.getLayoutDirection((View)this));
    boolean bool = true;
    if (paramInt2 != 1) {
      if (paramInt2 != 5) {
        paramInt2 = getPaddingLeft();
      } else {
        paramInt2 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt2 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
    } 
    if (bool2) {
      b2 = j - 1;
      b1 = -1;
    } else {
      b2 = 0;
      b1 = 1;
    } 
    int i = 0;
    paramInt3 = paramInt4;
    paramInt4 = k;
    while (i < j) {
      int i2 = b2 + b1 * i;
      View view = getVirtualChildAt(i2);
      if (view == null) {
        paramInt2 += measureNullChild(i2);
      } else if (view.getVisibility() != 8) {
        int i5 = view.getMeasuredWidth();
        int i6 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool1 && layoutParams.height != -1) {
          i3 = view.getBaseline();
        } else {
          i3 = -1;
        } 
        int i4 = layoutParams.gravity;
        paramInt1 = i4;
        if (i4 < 0)
          paramInt1 = paramInt3; 
        paramInt1 &= 0x70;
        if (paramInt1 != 16) {
          if (paramInt1 != 48) {
            if (paramInt1 != 80) {
              paramInt1 = paramInt4;
            } else {
              i4 = n - m - i6 - layoutParams.bottomMargin;
              paramInt1 = i4;
              if (i3 != -1) {
                paramInt1 = view.getMeasuredHeight();
                paramInt1 = i4 - arrayOfInt2[2] - paramInt1 - i3;
              } 
            } 
          } else {
            i4 = layoutParams.topMargin + paramInt4;
            paramInt1 = i4;
            if (i3 != -1)
              paramInt1 = i4 + arrayOfInt1[1] - i3; 
          } 
        } else {
          paramInt1 = (n - k - i1 - i6) / 2 + paramInt4 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        bool = true;
        int i3 = paramInt2;
        if (hasDividerBeforeChildAt(i2))
          i3 = paramInt2 + this.mDividerWidth; 
        paramInt2 = layoutParams.leftMargin + i3;
        setChildFrame(view, paramInt2 + getLocationOffset(view), paramInt1, i5, i6);
        paramInt1 = layoutParams.rightMargin;
        i3 = getNextLocationOffset(view);
        i += getChildrenSkipCount(view, i2);
        paramInt2 += i5 + paramInt1 + i3;
      } else {
        bool = true;
      } 
      i++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int n = getPaddingRight();
    int m = getPaddingRight();
    int i1 = getVirtualChildCount();
    int k = this.mGravity;
    paramInt1 = k & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
    } 
    for (paramInt2 = 0; paramInt2 < i1; paramInt2++) {
      View view = getVirtualChildAt(paramInt2);
      if (view == null) {
        paramInt3 = paramInt1 + measureNullChild(paramInt2);
      } else {
        paramInt3 = paramInt1;
        if (view.getVisibility() != 8) {
          int i3 = view.getMeasuredWidth();
          int i2 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt4 = layoutParams.gravity;
          paramInt3 = paramInt4;
          if (paramInt4 < 0)
            paramInt3 = k & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + i;
            } else {
              paramInt4 = j - n - i3;
              paramInt3 = layoutParams.rightMargin;
              paramInt3 = paramInt4 - paramInt3;
            } 
          } else {
            paramInt4 = (j - i - m - i3) / 2 + i + layoutParams.leftMargin;
            paramInt3 = layoutParams.rightMargin;
            paramInt3 = paramInt4 - paramInt3;
          } 
          paramInt4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            paramInt4 = paramInt1 + this.mDividerHeight; 
          paramInt1 = paramInt4 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i3, i2);
          paramInt3 = layoutParams.bottomMargin;
          paramInt4 = getNextLocationOffset(view);
          paramInt2 += getChildrenSkipCount(view, paramInt2);
          paramInt1 += i2 + paramInt3 + paramInt4;
          continue;
        } 
      } 
      paramInt1 = paramInt3;
      continue;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: aload_0
    //   6: invokevirtual getVirtualChildCount : ()I
    //   9: istore #16
    //   11: iload_1
    //   12: invokestatic getMode : (I)I
    //   15: istore #22
    //   17: iload_2
    //   18: invokestatic getMode : (I)I
    //   21: istore #21
    //   23: aload_0
    //   24: getfield mMaxAscent : [I
    //   27: ifnull -> 37
    //   30: aload_0
    //   31: getfield mMaxDescent : [I
    //   34: ifnonnull -> 51
    //   37: aload_0
    //   38: iconst_4
    //   39: newarray int
    //   41: putfield mMaxAscent : [I
    //   44: aload_0
    //   45: iconst_4
    //   46: newarray int
    //   48: putfield mMaxDescent : [I
    //   51: aload_0
    //   52: getfield mMaxAscent : [I
    //   55: astore #27
    //   57: aload_0
    //   58: getfield mMaxDescent : [I
    //   61: astore #26
    //   63: aload #27
    //   65: iconst_3
    //   66: iconst_m1
    //   67: iastore
    //   68: aload #27
    //   70: iconst_2
    //   71: iconst_m1
    //   72: iastore
    //   73: aload #27
    //   75: iconst_1
    //   76: iconst_m1
    //   77: iastore
    //   78: aload #27
    //   80: iconst_0
    //   81: iconst_m1
    //   82: iastore
    //   83: aload #26
    //   85: iconst_3
    //   86: iconst_m1
    //   87: iastore
    //   88: aload #26
    //   90: iconst_2
    //   91: iconst_m1
    //   92: iastore
    //   93: aload #26
    //   95: iconst_1
    //   96: iconst_m1
    //   97: iastore
    //   98: aload #26
    //   100: iconst_0
    //   101: iconst_m1
    //   102: iastore
    //   103: aload_0
    //   104: getfield mBaselineAligned : Z
    //   107: istore #24
    //   109: aload_0
    //   110: getfield mUseLargestChild : Z
    //   113: istore #25
    //   115: ldc 1073741824
    //   117: istore #14
    //   119: iload #22
    //   121: ldc 1073741824
    //   123: if_icmpne -> 132
    //   126: iconst_1
    //   127: istore #15
    //   129: goto -> 135
    //   132: iconst_0
    //   133: istore #15
    //   135: iconst_0
    //   136: istore #8
    //   138: iconst_0
    //   139: istore #7
    //   141: iload #7
    //   143: istore #13
    //   145: iload #13
    //   147: istore #6
    //   149: iload #6
    //   151: istore #11
    //   153: iload #11
    //   155: istore #12
    //   157: iload #12
    //   159: istore #9
    //   161: iload #9
    //   163: istore #10
    //   165: iconst_1
    //   166: istore #5
    //   168: fconst_0
    //   169: fstore_3
    //   170: iload #8
    //   172: iload #16
    //   174: if_icmpge -> 887
    //   177: aload_0
    //   178: iload #8
    //   180: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   183: astore #29
    //   185: aload #29
    //   187: ifnonnull -> 224
    //   190: aload_0
    //   191: aload_0
    //   192: getfield mTotalLength : I
    //   195: aload_0
    //   196: iload #8
    //   198: invokevirtual measureNullChild : (I)I
    //   201: iadd
    //   202: putfield mTotalLength : I
    //   205: iload #8
    //   207: istore #17
    //   209: iload #14
    //   211: istore #8
    //   213: iload #17
    //   215: istore #14
    //   217: iload #9
    //   219: istore #17
    //   221: goto -> 866
    //   224: aload #29
    //   226: invokevirtual getVisibility : ()I
    //   229: bipush #8
    //   231: if_icmpne -> 250
    //   234: iload #8
    //   236: aload_0
    //   237: aload #29
    //   239: iload #8
    //   241: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   244: iadd
    //   245: istore #8
    //   247: goto -> 205
    //   250: aload_0
    //   251: iload #8
    //   253: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   256: ifeq -> 272
    //   259: aload_0
    //   260: aload_0
    //   261: getfield mTotalLength : I
    //   264: aload_0
    //   265: getfield mDividerWidth : I
    //   268: iadd
    //   269: putfield mTotalLength : I
    //   272: aload #29
    //   274: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   277: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   280: astore #28
    //   282: fload_3
    //   283: aload #28
    //   285: getfield weight : F
    //   288: fadd
    //   289: fstore_3
    //   290: iload #22
    //   292: iload #14
    //   294: if_icmpne -> 406
    //   297: aload #28
    //   299: getfield width : I
    //   302: ifne -> 406
    //   305: aload #28
    //   307: getfield weight : F
    //   310: fconst_0
    //   311: fcmpl
    //   312: ifle -> 406
    //   315: iload #15
    //   317: ifeq -> 343
    //   320: aload_0
    //   321: aload_0
    //   322: getfield mTotalLength : I
    //   325: aload #28
    //   327: getfield leftMargin : I
    //   330: aload #28
    //   332: getfield rightMargin : I
    //   335: iadd
    //   336: iadd
    //   337: putfield mTotalLength : I
    //   340: goto -> 372
    //   343: aload_0
    //   344: getfield mTotalLength : I
    //   347: istore #14
    //   349: aload_0
    //   350: iload #14
    //   352: aload #28
    //   354: getfield leftMargin : I
    //   357: iload #14
    //   359: iadd
    //   360: aload #28
    //   362: getfield rightMargin : I
    //   365: iadd
    //   366: invokestatic max : (II)I
    //   369: putfield mTotalLength : I
    //   372: iload #24
    //   374: ifeq -> 400
    //   377: iconst_0
    //   378: iconst_0
    //   379: invokestatic makeMeasureSpec : (II)I
    //   382: istore #14
    //   384: aload #29
    //   386: iload #14
    //   388: iload #14
    //   390: invokevirtual measure : (II)V
    //   393: iload #7
    //   395: istore #14
    //   397: goto -> 590
    //   400: iconst_1
    //   401: istore #12
    //   403: goto -> 594
    //   406: aload #28
    //   408: getfield width : I
    //   411: ifne -> 437
    //   414: aload #28
    //   416: getfield weight : F
    //   419: fconst_0
    //   420: fcmpl
    //   421: ifle -> 437
    //   424: aload #28
    //   426: bipush #-2
    //   428: putfield width : I
    //   431: iconst_0
    //   432: istore #14
    //   434: goto -> 442
    //   437: ldc_w -2147483648
    //   440: istore #14
    //   442: fload_3
    //   443: fconst_0
    //   444: fcmpl
    //   445: ifne -> 457
    //   448: aload_0
    //   449: getfield mTotalLength : I
    //   452: istore #17
    //   454: goto -> 460
    //   457: iconst_0
    //   458: istore #17
    //   460: aload_0
    //   461: aload #29
    //   463: iload #8
    //   465: iload_1
    //   466: iload #17
    //   468: iload_2
    //   469: iconst_0
    //   470: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   473: iload #14
    //   475: ldc_w -2147483648
    //   478: if_icmpeq -> 488
    //   481: aload #28
    //   483: iload #14
    //   485: putfield width : I
    //   488: aload #29
    //   490: invokevirtual getMeasuredWidth : ()I
    //   493: istore #17
    //   495: iload #15
    //   497: ifeq -> 533
    //   500: aload_0
    //   501: aload_0
    //   502: getfield mTotalLength : I
    //   505: aload #28
    //   507: getfield leftMargin : I
    //   510: iload #17
    //   512: iadd
    //   513: aload #28
    //   515: getfield rightMargin : I
    //   518: iadd
    //   519: aload_0
    //   520: aload #29
    //   522: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   525: iadd
    //   526: iadd
    //   527: putfield mTotalLength : I
    //   530: goto -> 572
    //   533: aload_0
    //   534: getfield mTotalLength : I
    //   537: istore #14
    //   539: aload_0
    //   540: iload #14
    //   542: iload #14
    //   544: iload #17
    //   546: iadd
    //   547: aload #28
    //   549: getfield leftMargin : I
    //   552: iadd
    //   553: aload #28
    //   555: getfield rightMargin : I
    //   558: iadd
    //   559: aload_0
    //   560: aload #29
    //   562: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   565: iadd
    //   566: invokestatic max : (II)I
    //   569: putfield mTotalLength : I
    //   572: iload #7
    //   574: istore #14
    //   576: iload #25
    //   578: ifeq -> 590
    //   581: iload #17
    //   583: iload #7
    //   585: invokestatic max : (II)I
    //   588: istore #14
    //   590: iload #14
    //   592: istore #7
    //   594: ldc 1073741824
    //   596: istore #19
    //   598: iload #21
    //   600: ldc 1073741824
    //   602: if_icmpeq -> 623
    //   605: aload #28
    //   607: getfield height : I
    //   610: iconst_m1
    //   611: if_icmpne -> 623
    //   614: iconst_1
    //   615: istore #14
    //   617: iconst_1
    //   618: istore #10
    //   620: goto -> 626
    //   623: iconst_0
    //   624: istore #14
    //   626: aload #28
    //   628: getfield topMargin : I
    //   631: aload #28
    //   633: getfield bottomMargin : I
    //   636: iadd
    //   637: istore #17
    //   639: aload #29
    //   641: invokevirtual getMeasuredHeight : ()I
    //   644: iload #17
    //   646: iadd
    //   647: istore #18
    //   649: iload #9
    //   651: aload #29
    //   653: invokevirtual getMeasuredState : ()I
    //   656: invokestatic combineMeasuredStates : (II)I
    //   659: istore #20
    //   661: iload #24
    //   663: ifeq -> 750
    //   666: aload #29
    //   668: invokevirtual getBaseline : ()I
    //   671: istore #23
    //   673: iload #23
    //   675: iconst_m1
    //   676: if_icmpeq -> 750
    //   679: aload #28
    //   681: getfield gravity : I
    //   684: ifge -> 696
    //   687: aload_0
    //   688: getfield mGravity : I
    //   691: istore #9
    //   693: goto -> 703
    //   696: aload #28
    //   698: getfield gravity : I
    //   701: istore #9
    //   703: iload #9
    //   705: bipush #112
    //   707: iand
    //   708: iconst_4
    //   709: ishr
    //   710: bipush #-2
    //   712: iand
    //   713: iconst_1
    //   714: ishr
    //   715: istore #9
    //   717: aload #27
    //   719: iload #9
    //   721: aload #27
    //   723: iload #9
    //   725: iaload
    //   726: iload #23
    //   728: invokestatic max : (II)I
    //   731: iastore
    //   732: aload #26
    //   734: iload #9
    //   736: aload #26
    //   738: iload #9
    //   740: iaload
    //   741: iload #18
    //   743: iload #23
    //   745: isub
    //   746: invokestatic max : (II)I
    //   749: iastore
    //   750: iload #13
    //   752: iload #18
    //   754: invokestatic max : (II)I
    //   757: istore #13
    //   759: iload #5
    //   761: ifeq -> 779
    //   764: aload #28
    //   766: getfield height : I
    //   769: iconst_m1
    //   770: if_icmpne -> 779
    //   773: iconst_1
    //   774: istore #5
    //   776: goto -> 782
    //   779: iconst_0
    //   780: istore #5
    //   782: aload #28
    //   784: getfield weight : F
    //   787: fconst_0
    //   788: fcmpl
    //   789: ifle -> 816
    //   792: iload #14
    //   794: ifeq -> 800
    //   797: goto -> 804
    //   800: iload #18
    //   802: istore #17
    //   804: iload #11
    //   806: iload #17
    //   808: invokestatic max : (II)I
    //   811: istore #9
    //   813: goto -> 841
    //   816: iload #14
    //   818: ifeq -> 824
    //   821: goto -> 828
    //   824: iload #18
    //   826: istore #17
    //   828: iload #6
    //   830: iload #17
    //   832: invokestatic max : (II)I
    //   835: istore #6
    //   837: iload #11
    //   839: istore #9
    //   841: aload_0
    //   842: aload #29
    //   844: iload #8
    //   846: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   849: iload #8
    //   851: iadd
    //   852: istore #14
    //   854: iload #20
    //   856: istore #17
    //   858: iload #9
    //   860: istore #11
    //   862: iload #19
    //   864: istore #8
    //   866: iload #8
    //   868: istore #9
    //   870: iload #14
    //   872: iconst_1
    //   873: iadd
    //   874: istore #8
    //   876: iload #9
    //   878: istore #14
    //   880: iload #17
    //   882: istore #9
    //   884: goto -> 170
    //   887: aload_0
    //   888: getfield mTotalLength : I
    //   891: ifle -> 916
    //   894: aload_0
    //   895: iload #16
    //   897: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   900: ifeq -> 916
    //   903: aload_0
    //   904: aload_0
    //   905: getfield mTotalLength : I
    //   908: aload_0
    //   909: getfield mDividerWidth : I
    //   912: iadd
    //   913: putfield mTotalLength : I
    //   916: aload #27
    //   918: iconst_1
    //   919: iaload
    //   920: iconst_m1
    //   921: if_icmpne -> 958
    //   924: aload #27
    //   926: iconst_0
    //   927: iaload
    //   928: iconst_m1
    //   929: if_icmpne -> 958
    //   932: aload #27
    //   934: iconst_2
    //   935: iaload
    //   936: iconst_m1
    //   937: if_icmpne -> 958
    //   940: aload #27
    //   942: iconst_3
    //   943: iaload
    //   944: iconst_m1
    //   945: if_icmpeq -> 951
    //   948: goto -> 958
    //   951: iload #13
    //   953: istore #8
    //   955: goto -> 1016
    //   958: iload #13
    //   960: aload #27
    //   962: iconst_3
    //   963: iaload
    //   964: aload #27
    //   966: iconst_0
    //   967: iaload
    //   968: aload #27
    //   970: iconst_1
    //   971: iaload
    //   972: aload #27
    //   974: iconst_2
    //   975: iaload
    //   976: invokestatic max : (II)I
    //   979: invokestatic max : (II)I
    //   982: invokestatic max : (II)I
    //   985: aload #26
    //   987: iconst_3
    //   988: iaload
    //   989: aload #26
    //   991: iconst_0
    //   992: iaload
    //   993: aload #26
    //   995: iconst_1
    //   996: iaload
    //   997: aload #26
    //   999: iconst_2
    //   1000: iaload
    //   1001: invokestatic max : (II)I
    //   1004: invokestatic max : (II)I
    //   1007: invokestatic max : (II)I
    //   1010: iadd
    //   1011: invokestatic max : (II)I
    //   1014: istore #8
    //   1016: iload #9
    //   1018: istore #13
    //   1020: iload #8
    //   1022: istore #14
    //   1024: iload #25
    //   1026: ifeq -> 1215
    //   1029: iload #22
    //   1031: ldc_w -2147483648
    //   1034: if_icmpeq -> 1046
    //   1037: iload #8
    //   1039: istore #14
    //   1041: iload #22
    //   1043: ifne -> 1215
    //   1046: aload_0
    //   1047: iconst_0
    //   1048: putfield mTotalLength : I
    //   1051: iconst_0
    //   1052: istore #9
    //   1054: iload #8
    //   1056: istore #14
    //   1058: iload #9
    //   1060: iload #16
    //   1062: if_icmpge -> 1215
    //   1065: aload_0
    //   1066: iload #9
    //   1068: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1071: astore #29
    //   1073: aload #29
    //   1075: ifnonnull -> 1096
    //   1078: aload_0
    //   1079: aload_0
    //   1080: getfield mTotalLength : I
    //   1083: aload_0
    //   1084: iload #9
    //   1086: invokevirtual measureNullChild : (I)I
    //   1089: iadd
    //   1090: putfield mTotalLength : I
    //   1093: goto -> 1119
    //   1096: aload #29
    //   1098: invokevirtual getVisibility : ()I
    //   1101: bipush #8
    //   1103: if_icmpne -> 1122
    //   1106: iload #9
    //   1108: aload_0
    //   1109: aload #29
    //   1111: iload #9
    //   1113: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1116: iadd
    //   1117: istore #9
    //   1119: goto -> 1209
    //   1122: aload #29
    //   1124: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1127: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1130: astore #28
    //   1132: iload #15
    //   1134: ifeq -> 1170
    //   1137: aload_0
    //   1138: aload_0
    //   1139: getfield mTotalLength : I
    //   1142: aload #28
    //   1144: getfield leftMargin : I
    //   1147: iload #7
    //   1149: iadd
    //   1150: aload #28
    //   1152: getfield rightMargin : I
    //   1155: iadd
    //   1156: aload_0
    //   1157: aload #29
    //   1159: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1162: iadd
    //   1163: iadd
    //   1164: putfield mTotalLength : I
    //   1167: goto -> 1119
    //   1170: aload_0
    //   1171: getfield mTotalLength : I
    //   1174: istore #14
    //   1176: aload_0
    //   1177: iload #14
    //   1179: iload #14
    //   1181: iload #7
    //   1183: iadd
    //   1184: aload #28
    //   1186: getfield leftMargin : I
    //   1189: iadd
    //   1190: aload #28
    //   1192: getfield rightMargin : I
    //   1195: iadd
    //   1196: aload_0
    //   1197: aload #29
    //   1199: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1202: iadd
    //   1203: invokestatic max : (II)I
    //   1206: putfield mTotalLength : I
    //   1209: iinc #9, 1
    //   1212: goto -> 1054
    //   1215: aload_0
    //   1216: getfield mTotalLength : I
    //   1219: aload_0
    //   1220: invokevirtual getPaddingLeft : ()I
    //   1223: aload_0
    //   1224: invokevirtual getPaddingRight : ()I
    //   1227: iadd
    //   1228: iadd
    //   1229: istore #8
    //   1231: aload_0
    //   1232: iload #8
    //   1234: putfield mTotalLength : I
    //   1237: iload #8
    //   1239: aload_0
    //   1240: invokevirtual getSuggestedMinimumWidth : ()I
    //   1243: invokestatic max : (II)I
    //   1246: iload_1
    //   1247: iconst_0
    //   1248: invokestatic resolveSizeAndState : (III)I
    //   1251: istore #18
    //   1253: ldc_w 16777215
    //   1256: iload #18
    //   1258: iand
    //   1259: aload_0
    //   1260: getfield mTotalLength : I
    //   1263: isub
    //   1264: istore #17
    //   1266: iload #12
    //   1268: ifne -> 1401
    //   1271: iload #17
    //   1273: ifeq -> 1285
    //   1276: fload_3
    //   1277: fconst_0
    //   1278: fcmpl
    //   1279: ifle -> 1285
    //   1282: goto -> 1401
    //   1285: iload #6
    //   1287: iload #11
    //   1289: invokestatic max : (II)I
    //   1292: istore #9
    //   1294: iload #25
    //   1296: ifeq -> 1386
    //   1299: iload #22
    //   1301: ldc 1073741824
    //   1303: if_icmpeq -> 1386
    //   1306: iconst_0
    //   1307: istore #6
    //   1309: iload #6
    //   1311: iload #16
    //   1313: if_icmpge -> 1386
    //   1316: aload_0
    //   1317: iload #6
    //   1319: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1322: astore #26
    //   1324: aload #26
    //   1326: ifnull -> 1380
    //   1329: aload #26
    //   1331: invokevirtual getVisibility : ()I
    //   1334: bipush #8
    //   1336: if_icmpne -> 1342
    //   1339: goto -> 1380
    //   1342: aload #26
    //   1344: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1347: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1350: getfield weight : F
    //   1353: fconst_0
    //   1354: fcmpl
    //   1355: ifle -> 1380
    //   1358: aload #26
    //   1360: iload #7
    //   1362: ldc 1073741824
    //   1364: invokestatic makeMeasureSpec : (II)I
    //   1367: aload #26
    //   1369: invokevirtual getMeasuredHeight : ()I
    //   1372: ldc 1073741824
    //   1374: invokestatic makeMeasureSpec : (II)I
    //   1377: invokevirtual measure : (II)V
    //   1380: iinc #6, 1
    //   1383: goto -> 1309
    //   1386: iload #16
    //   1388: istore #8
    //   1390: iload #14
    //   1392: istore #6
    //   1394: iload #9
    //   1396: istore #7
    //   1398: goto -> 2137
    //   1401: aload_0
    //   1402: getfield mWeightSum : F
    //   1405: fstore #4
    //   1407: fload #4
    //   1409: fconst_0
    //   1410: fcmpl
    //   1411: ifle -> 1417
    //   1414: fload #4
    //   1416: fstore_3
    //   1417: aload #27
    //   1419: iconst_3
    //   1420: iconst_m1
    //   1421: iastore
    //   1422: aload #27
    //   1424: iconst_2
    //   1425: iconst_m1
    //   1426: iastore
    //   1427: aload #27
    //   1429: iconst_1
    //   1430: iconst_m1
    //   1431: iastore
    //   1432: aload #27
    //   1434: iconst_0
    //   1435: iconst_m1
    //   1436: iastore
    //   1437: aload #26
    //   1439: iconst_3
    //   1440: iconst_m1
    //   1441: iastore
    //   1442: aload #26
    //   1444: iconst_2
    //   1445: iconst_m1
    //   1446: iastore
    //   1447: aload #26
    //   1449: iconst_1
    //   1450: iconst_m1
    //   1451: iastore
    //   1452: aload #26
    //   1454: iconst_0
    //   1455: iconst_m1
    //   1456: iastore
    //   1457: aload_0
    //   1458: iconst_0
    //   1459: putfield mTotalLength : I
    //   1462: iconst_m1
    //   1463: istore #11
    //   1465: iload #13
    //   1467: istore #9
    //   1469: iconst_0
    //   1470: istore #13
    //   1472: iload #5
    //   1474: istore #8
    //   1476: iload #16
    //   1478: istore #7
    //   1480: iload #9
    //   1482: istore #5
    //   1484: iload #6
    //   1486: istore #9
    //   1488: iload #17
    //   1490: istore #6
    //   1492: iload #13
    //   1494: iload #7
    //   1496: if_icmpge -> 2003
    //   1499: aload_0
    //   1500: iload #13
    //   1502: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1505: astore #28
    //   1507: aload #28
    //   1509: ifnull -> 1997
    //   1512: aload #28
    //   1514: invokevirtual getVisibility : ()I
    //   1517: bipush #8
    //   1519: if_icmpne -> 1525
    //   1522: goto -> 1997
    //   1525: aload #28
    //   1527: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1530: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1533: astore #29
    //   1535: aload #29
    //   1537: getfield weight : F
    //   1540: fstore #4
    //   1542: fload #4
    //   1544: fconst_0
    //   1545: fcmpl
    //   1546: ifle -> 1709
    //   1549: iload #6
    //   1551: i2f
    //   1552: fload #4
    //   1554: fmul
    //   1555: fload_3
    //   1556: fdiv
    //   1557: f2i
    //   1558: istore #14
    //   1560: iload_2
    //   1561: aload_0
    //   1562: invokevirtual getPaddingTop : ()I
    //   1565: aload_0
    //   1566: invokevirtual getPaddingBottom : ()I
    //   1569: iadd
    //   1570: aload #29
    //   1572: getfield topMargin : I
    //   1575: iadd
    //   1576: aload #29
    //   1578: getfield bottomMargin : I
    //   1581: iadd
    //   1582: aload #29
    //   1584: getfield height : I
    //   1587: invokestatic getChildMeasureSpec : (III)I
    //   1590: istore #17
    //   1592: aload #29
    //   1594: getfield width : I
    //   1597: ifne -> 1642
    //   1600: iload #22
    //   1602: ldc 1073741824
    //   1604: if_icmpeq -> 1610
    //   1607: goto -> 1642
    //   1610: iload #14
    //   1612: ifle -> 1622
    //   1615: iload #14
    //   1617: istore #12
    //   1619: goto -> 1625
    //   1622: iconst_0
    //   1623: istore #12
    //   1625: aload #28
    //   1627: iload #12
    //   1629: ldc 1073741824
    //   1631: invokestatic makeMeasureSpec : (II)I
    //   1634: iload #17
    //   1636: invokevirtual measure : (II)V
    //   1639: goto -> 1678
    //   1642: aload #28
    //   1644: invokevirtual getMeasuredWidth : ()I
    //   1647: iload #14
    //   1649: iadd
    //   1650: istore #16
    //   1652: iload #16
    //   1654: istore #12
    //   1656: iload #16
    //   1658: ifge -> 1664
    //   1661: iconst_0
    //   1662: istore #12
    //   1664: aload #28
    //   1666: iload #12
    //   1668: ldc 1073741824
    //   1670: invokestatic makeMeasureSpec : (II)I
    //   1673: iload #17
    //   1675: invokevirtual measure : (II)V
    //   1678: iload #5
    //   1680: aload #28
    //   1682: invokevirtual getMeasuredState : ()I
    //   1685: ldc_w -16777216
    //   1688: iand
    //   1689: invokestatic combineMeasuredStates : (II)I
    //   1692: istore #5
    //   1694: fload_3
    //   1695: fload #4
    //   1697: fsub
    //   1698: fstore_3
    //   1699: iload #6
    //   1701: iload #14
    //   1703: isub
    //   1704: istore #6
    //   1706: goto -> 1709
    //   1709: iload #15
    //   1711: ifeq -> 1750
    //   1714: aload_0
    //   1715: aload_0
    //   1716: getfield mTotalLength : I
    //   1719: aload #28
    //   1721: invokevirtual getMeasuredWidth : ()I
    //   1724: aload #29
    //   1726: getfield leftMargin : I
    //   1729: iadd
    //   1730: aload #29
    //   1732: getfield rightMargin : I
    //   1735: iadd
    //   1736: aload_0
    //   1737: aload #28
    //   1739: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1742: iadd
    //   1743: iadd
    //   1744: putfield mTotalLength : I
    //   1747: goto -> 1792
    //   1750: aload_0
    //   1751: getfield mTotalLength : I
    //   1754: istore #12
    //   1756: aload_0
    //   1757: iload #12
    //   1759: aload #28
    //   1761: invokevirtual getMeasuredWidth : ()I
    //   1764: iload #12
    //   1766: iadd
    //   1767: aload #29
    //   1769: getfield leftMargin : I
    //   1772: iadd
    //   1773: aload #29
    //   1775: getfield rightMargin : I
    //   1778: iadd
    //   1779: aload_0
    //   1780: aload #28
    //   1782: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1785: iadd
    //   1786: invokestatic max : (II)I
    //   1789: putfield mTotalLength : I
    //   1792: iload #21
    //   1794: ldc 1073741824
    //   1796: if_icmpeq -> 1814
    //   1799: aload #29
    //   1801: getfield height : I
    //   1804: iconst_m1
    //   1805: if_icmpne -> 1814
    //   1808: iconst_1
    //   1809: istore #12
    //   1811: goto -> 1817
    //   1814: iconst_0
    //   1815: istore #12
    //   1817: aload #29
    //   1819: getfield topMargin : I
    //   1822: aload #29
    //   1824: getfield bottomMargin : I
    //   1827: iadd
    //   1828: istore #17
    //   1830: aload #28
    //   1832: invokevirtual getMeasuredHeight : ()I
    //   1835: iload #17
    //   1837: iadd
    //   1838: istore #16
    //   1840: iload #11
    //   1842: iload #16
    //   1844: invokestatic max : (II)I
    //   1847: istore #14
    //   1849: iload #12
    //   1851: ifeq -> 1861
    //   1854: iload #17
    //   1856: istore #11
    //   1858: goto -> 1865
    //   1861: iload #16
    //   1863: istore #11
    //   1865: iload #9
    //   1867: iload #11
    //   1869: invokestatic max : (II)I
    //   1872: istore #11
    //   1874: iload #8
    //   1876: ifeq -> 1894
    //   1879: aload #29
    //   1881: getfield height : I
    //   1884: iconst_m1
    //   1885: if_icmpne -> 1894
    //   1888: iconst_1
    //   1889: istore #8
    //   1891: goto -> 1897
    //   1894: iconst_0
    //   1895: istore #8
    //   1897: iload #24
    //   1899: ifeq -> 1986
    //   1902: aload #28
    //   1904: invokevirtual getBaseline : ()I
    //   1907: istore #12
    //   1909: iload #12
    //   1911: iconst_m1
    //   1912: if_icmpeq -> 1986
    //   1915: aload #29
    //   1917: getfield gravity : I
    //   1920: ifge -> 1932
    //   1923: aload_0
    //   1924: getfield mGravity : I
    //   1927: istore #9
    //   1929: goto -> 1939
    //   1932: aload #29
    //   1934: getfield gravity : I
    //   1937: istore #9
    //   1939: iload #9
    //   1941: bipush #112
    //   1943: iand
    //   1944: iconst_4
    //   1945: ishr
    //   1946: bipush #-2
    //   1948: iand
    //   1949: iconst_1
    //   1950: ishr
    //   1951: istore #9
    //   1953: aload #27
    //   1955: iload #9
    //   1957: aload #27
    //   1959: iload #9
    //   1961: iaload
    //   1962: iload #12
    //   1964: invokestatic max : (II)I
    //   1967: iastore
    //   1968: aload #26
    //   1970: iload #9
    //   1972: aload #26
    //   1974: iload #9
    //   1976: iaload
    //   1977: iload #16
    //   1979: iload #12
    //   1981: isub
    //   1982: invokestatic max : (II)I
    //   1985: iastore
    //   1986: iload #11
    //   1988: istore #9
    //   1990: iload #14
    //   1992: istore #11
    //   1994: goto -> 1997
    //   1997: iinc #13, 1
    //   2000: goto -> 1492
    //   2003: aload_0
    //   2004: aload_0
    //   2005: getfield mTotalLength : I
    //   2008: aload_0
    //   2009: invokevirtual getPaddingLeft : ()I
    //   2012: aload_0
    //   2013: invokevirtual getPaddingRight : ()I
    //   2016: iadd
    //   2017: iadd
    //   2018: putfield mTotalLength : I
    //   2021: aload #27
    //   2023: iconst_1
    //   2024: iaload
    //   2025: iconst_m1
    //   2026: if_icmpne -> 2063
    //   2029: aload #27
    //   2031: iconst_0
    //   2032: iaload
    //   2033: iconst_m1
    //   2034: if_icmpne -> 2063
    //   2037: aload #27
    //   2039: iconst_2
    //   2040: iaload
    //   2041: iconst_m1
    //   2042: if_icmpne -> 2063
    //   2045: aload #27
    //   2047: iconst_3
    //   2048: iaload
    //   2049: iconst_m1
    //   2050: if_icmpeq -> 2056
    //   2053: goto -> 2063
    //   2056: iload #11
    //   2058: istore #6
    //   2060: goto -> 2121
    //   2063: iload #11
    //   2065: aload #27
    //   2067: iconst_3
    //   2068: iaload
    //   2069: aload #27
    //   2071: iconst_0
    //   2072: iaload
    //   2073: aload #27
    //   2075: iconst_1
    //   2076: iaload
    //   2077: aload #27
    //   2079: iconst_2
    //   2080: iaload
    //   2081: invokestatic max : (II)I
    //   2084: invokestatic max : (II)I
    //   2087: invokestatic max : (II)I
    //   2090: aload #26
    //   2092: iconst_3
    //   2093: iaload
    //   2094: aload #26
    //   2096: iconst_0
    //   2097: iaload
    //   2098: aload #26
    //   2100: iconst_1
    //   2101: iaload
    //   2102: aload #26
    //   2104: iconst_2
    //   2105: iaload
    //   2106: invokestatic max : (II)I
    //   2109: invokestatic max : (II)I
    //   2112: invokestatic max : (II)I
    //   2115: iadd
    //   2116: invokestatic max : (II)I
    //   2119: istore #6
    //   2121: iload #5
    //   2123: istore #13
    //   2125: iload #8
    //   2127: istore #5
    //   2129: iload #7
    //   2131: istore #8
    //   2133: iload #9
    //   2135: istore #7
    //   2137: iload #5
    //   2139: ifne -> 2152
    //   2142: iload #21
    //   2144: ldc 1073741824
    //   2146: if_icmpeq -> 2152
    //   2149: goto -> 2156
    //   2152: iload #6
    //   2154: istore #7
    //   2156: aload_0
    //   2157: iload #18
    //   2159: iload #13
    //   2161: ldc_w -16777216
    //   2164: iand
    //   2165: ior
    //   2166: iload #7
    //   2168: aload_0
    //   2169: invokevirtual getPaddingTop : ()I
    //   2172: aload_0
    //   2173: invokevirtual getPaddingBottom : ()I
    //   2176: iadd
    //   2177: iadd
    //   2178: aload_0
    //   2179: invokevirtual getSuggestedMinimumHeight : ()I
    //   2182: invokestatic max : (II)I
    //   2185: iload_2
    //   2186: iload #13
    //   2188: bipush #16
    //   2190: ishl
    //   2191: invokestatic resolveSizeAndState : (III)I
    //   2194: invokevirtual setMeasuredDimension : (II)V
    //   2197: iload #10
    //   2199: ifeq -> 2209
    //   2202: aload_0
    //   2203: iload #8
    //   2205: iload_1
    //   2206: invokespecial forceUniformHeight : (II)V
    //   2209: return
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i5 = getVirtualChildCount();
    int i8 = View.MeasureSpec.getMode(paramInt1);
    int i6 = View.MeasureSpec.getMode(paramInt2);
    int i9 = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    int j = 0;
    int i4 = 0;
    int n = i4;
    int i = n;
    int m = i;
    int i2 = m;
    int i3 = i2;
    int i1 = i3;
    float f = 0.0F;
    int k = 1;
    while (i2 < i5) {
      View view = getVirtualChildAt(i2);
      if (view == null) {
        this.mTotalLength += measureNullChild(i2);
      } else if (view.getVisibility() == 8) {
        i2 += getChildrenSkipCount(view, i2);
      } else {
        if (hasDividerBeforeChildAt(i2))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (i6 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i3 = this.mTotalLength;
          this.mTotalLength = Math.max(i3, layoutParams.topMargin + i3 + layoutParams.bottomMargin);
          i3 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i10 = 0;
          } else {
            i10 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i11 = this.mTotalLength;
          } else {
            i11 = 0;
          } 
          measureChildBeforeLayout(view, i2, paramInt1, 0, paramInt2, i11);
          if (i10 != Integer.MIN_VALUE)
            layoutParams.height = i10; 
          int i11 = view.getMeasuredHeight();
          int i10 = this.mTotalLength;
          this.mTotalLength = Math.max(i10, i10 + i11 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          if (bool)
            n = Math.max(i11, n); 
        } 
        if (i9 >= 0 && i9 == i2 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i2 >= i9 || layoutParams.weight <= 0.0F) {
          if (i8 != 1073741824 && layoutParams.width == -1) {
            i10 = 1;
            i1 = 1;
          } else {
            i10 = 0;
          } 
          int i12 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i11 = view.getMeasuredWidth() + i12;
          i4 = Math.max(i4, i11);
          int i13 = View.combineMeasuredStates(j, view.getMeasuredState());
          if (k && layoutParams.width == -1) {
            j = 1;
          } else {
            j = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i10)
              i11 = i12; 
            i = Math.max(i, i11);
            k = m;
          } else {
            if (!i10)
              i12 = i11; 
            k = Math.max(m, i12);
          } 
          i11 = getChildrenSkipCount(view, i2);
          m = k;
          int i10 = i13;
          i2 = i11 + i2;
          k = j;
          j = i10;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i2++;
    } 
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i5))
      this.mTotalLength += this.mDividerHeight; 
    if (bool && (i6 == Integer.MIN_VALUE || i6 == 0)) {
      this.mTotalLength = 0;
      for (i2 = 0; i2 < i5; i2++) {
        View view = getVirtualChildAt(i2);
        if (view == null) {
          this.mTotalLength += measureNullChild(i2);
        } else if (view.getVisibility() == 8) {
          i2 += getChildrenSkipCount(view, i2);
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          int i10 = this.mTotalLength;
          this.mTotalLength = Math.max(i10, i10 + n + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
        } 
      } 
    } 
    i2 = this.mTotalLength + getPaddingTop() + getPaddingBottom();
    this.mTotalLength = i2;
    int i7 = View.resolveSizeAndState(Math.max(i2, getSuggestedMinimumHeight()), paramInt2, 0);
    i2 = (0xFFFFFF & i7) - this.mTotalLength;
    if (i3 != 0 || (i2 != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      n = i2;
      i = j;
      i2 = 0;
      j = n;
      n = i4;
      while (i2 < i5) {
        View view = getVirtualChildAt(i2);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i4 = (int)(j * f1 / f);
            int i14 = getPaddingLeft();
            i9 = getPaddingRight();
            int i13 = layoutParams.leftMargin;
            int i12 = layoutParams.rightMargin;
            int i11 = layoutParams.width;
            i3 = j - i4;
            i11 = getChildMeasureSpec(paramInt1, i14 + i9 + i13 + i12, i11);
            if (layoutParams.height != 0 || i6 != 1073741824) {
              i4 = view.getMeasuredHeight() + i4;
              j = i4;
              if (i4 < 0)
                j = 0; 
              view.measure(i11, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } else {
              if (i4 > 0) {
                j = i4;
              } else {
                j = 0;
              } 
              view.measure(i11, View.MeasureSpec.makeMeasureSpec(j, 1073741824));
            } 
            i = View.combineMeasuredStates(i, view.getMeasuredState() & 0xFFFFFF00);
            f -= f1;
            j = i3;
          } 
          i4 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i10 = view.getMeasuredWidth() + i4;
          i3 = Math.max(n, i10);
          if (i8 != 1073741824 && layoutParams.width == -1) {
            n = 1;
          } else {
            n = 0;
          } 
          if (n != 0) {
            n = i4;
          } else {
            n = i10;
          } 
          m = Math.max(m, n);
          if (k != 0 && layoutParams.width == -1) {
            k = 1;
          } else {
            k = 0;
          } 
          n = this.mTotalLength;
          this.mTotalLength = Math.max(n, view.getMeasuredHeight() + n + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          n = i3;
        } 
        i2++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
      j = i;
      i = m;
    } else {
      m = Math.max(m, i);
      if (bool && i6 != 1073741824)
        for (i = 0; i < i5; i++) {
          View view = getVirtualChildAt(i);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(n, 1073741824)); 
        }  
      i = m;
      n = i4;
    } 
    if (k != 0 || i8 == 1073741824)
      i = n; 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(i + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, j), i7);
    if (i1 != 0)
      forceUniformWidth(i5, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    } 
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    int i = this.mGravity;
    if ((0x800007 & i) != paramInt) {
      this.mGravity = paramInt | 0xFF7FFFF8 & i;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    int i = this.mGravity;
    if ((i & 0x70) != paramInt) {
      this.mGravity = paramInt | i & 0xFFFFFF8F;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\widget\LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */