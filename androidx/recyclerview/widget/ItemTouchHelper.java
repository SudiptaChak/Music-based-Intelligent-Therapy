package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.R;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
  static final int ACTION_MODE_DRAG_MASK = 16711680;
  
  private static final int ACTION_MODE_IDLE_MASK = 255;
  
  static final int ACTION_MODE_SWIPE_MASK = 65280;
  
  public static final int ACTION_STATE_DRAG = 2;
  
  public static final int ACTION_STATE_IDLE = 0;
  
  public static final int ACTION_STATE_SWIPE = 1;
  
  private static final int ACTIVE_POINTER_ID_NONE = -1;
  
  public static final int ANIMATION_TYPE_DRAG = 8;
  
  public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
  
  public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
  
  private static final boolean DEBUG = false;
  
  static final int DIRECTION_FLAG_COUNT = 8;
  
  public static final int DOWN = 2;
  
  public static final int END = 32;
  
  public static final int LEFT = 4;
  
  private static final int PIXELS_PER_SECOND = 1000;
  
  public static final int RIGHT = 8;
  
  public static final int START = 16;
  
  private static final String TAG = "ItemTouchHelper";
  
  public static final int UP = 1;
  
  private int mActionState = 0;
  
  int mActivePointerId = -1;
  
  Callback mCallback;
  
  private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
  
  private List<Integer> mDistances;
  
  private long mDragScrollStartTimeInMs;
  
  float mDx;
  
  float mDy;
  
  GestureDetectorCompat mGestureDetector;
  
  float mInitialTouchX;
  
  float mInitialTouchY;
  
  private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
  
  private float mMaxSwipeVelocity;
  
  private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
      final ItemTouchHelper this$0;
      
      public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        int i = param1MotionEvent.getActionMasked();
        boolean bool = true;
        if (i == 0) {
          ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(0);
          ItemTouchHelper.this.mInitialTouchX = param1MotionEvent.getX();
          ItemTouchHelper.this.mInitialTouchY = param1MotionEvent.getY();
          ItemTouchHelper.this.obtainVelocityTracker();
          if (ItemTouchHelper.this.mSelected == null) {
            ItemTouchHelper.RecoverAnimation recoverAnimation = ItemTouchHelper.this.findAnimation(param1MotionEvent);
            if (recoverAnimation != null) {
              ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
              itemTouchHelper2.mInitialTouchX -= recoverAnimation.mX;
              itemTouchHelper2 = ItemTouchHelper.this;
              itemTouchHelper2.mInitialTouchY -= recoverAnimation.mY;
              ItemTouchHelper.this.endRecoverAnimation(recoverAnimation.mViewHolder, true);
              if (ItemTouchHelper.this.mPendingCleanup.remove(recoverAnimation.mViewHolder.itemView))
                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, recoverAnimation.mViewHolder); 
              ItemTouchHelper.this.select(recoverAnimation.mViewHolder, recoverAnimation.mActionState);
              ItemTouchHelper itemTouchHelper1 = ItemTouchHelper.this;
              itemTouchHelper1.updateDxDy(param1MotionEvent, itemTouchHelper1.mSelectedFlags, 0);
            } 
          } 
        } else if (i == 3 || i == 1) {
          ItemTouchHelper.this.mActivePointerId = -1;
          ItemTouchHelper.this.select(null, 0);
        } else if (ItemTouchHelper.this.mActivePointerId != -1) {
          int j = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
          if (j >= 0)
            ItemTouchHelper.this.checkSelectForSwipe(i, param1MotionEvent, j); 
        } 
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mSelected == null)
          bool = false; 
        return bool;
      }
      
      public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {
        if (!param1Boolean)
          return; 
        ItemTouchHelper.this.select(null, 0);
      }
      
      public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
        ItemTouchHelper.this.mGestureDetector.onTouchEvent(param1MotionEvent);
        if (ItemTouchHelper.this.mVelocityTracker != null)
          ItemTouchHelper.this.mVelocityTracker.addMovement(param1MotionEvent); 
        if (ItemTouchHelper.this.mActivePointerId == -1)
          return; 
        int j = param1MotionEvent.getActionMasked();
        int i = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
        if (i >= 0)
          ItemTouchHelper.this.checkSelectForSwipe(j, param1MotionEvent, i); 
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
        if (viewHolder == null)
          return; 
        boolean bool = false;
        if (j != 1) {
          if (j != 2) {
            if (j != 3) {
              if (j == 6) {
                i = param1MotionEvent.getActionIndex();
                if (param1MotionEvent.getPointerId(i) == ItemTouchHelper.this.mActivePointerId) {
                  if (i == 0)
                    bool = true; 
                  ItemTouchHelper.this.mActivePointerId = param1MotionEvent.getPointerId(bool);
                  ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                  itemTouchHelper.updateDxDy(param1MotionEvent, itemTouchHelper.mSelectedFlags, i);
                } 
              } 
            } else {
              if (ItemTouchHelper.this.mVelocityTracker != null)
                ItemTouchHelper.this.mVelocityTracker.clear(); 
              ItemTouchHelper.this.select(null, 0);
              ItemTouchHelper.this.mActivePointerId = -1;
            } 
          } else if (i >= 0) {
            ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
            itemTouchHelper.updateDxDy(param1MotionEvent, itemTouchHelper.mSelectedFlags, i);
            ItemTouchHelper.this.moveIfNecessary(viewHolder);
            ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
            ItemTouchHelper.this.mScrollRunnable.run();
            ItemTouchHelper.this.mRecyclerView.invalidate();
          } 
          return;
        } 
        ItemTouchHelper.this.select(null, 0);
        ItemTouchHelper.this.mActivePointerId = -1;
      }
    };
  
  View mOverdrawChild = null;
  
  int mOverdrawChildPosition = -1;
  
  final List<View> mPendingCleanup = new ArrayList<View>();
  
  List<RecoverAnimation> mRecoverAnimations = new ArrayList<RecoverAnimation>();
  
  RecyclerView mRecyclerView;
  
  final Runnable mScrollRunnable = new Runnable() {
      final ItemTouchHelper this$0;
      
      public void run() {
        if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
          if (ItemTouchHelper.this.mSelected != null) {
            ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
            itemTouchHelper.moveIfNecessary(itemTouchHelper.mSelected);
          } 
          ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
          ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
        } 
      }
    };
  
  RecyclerView.ViewHolder mSelected = null;
  
  int mSelectedFlags;
  
  private float mSelectedStartX;
  
  private float mSelectedStartY;
  
  private int mSlop;
  
  private List<RecyclerView.ViewHolder> mSwapTargets;
  
  private float mSwipeEscapeVelocity;
  
  private final float[] mTmpPosition = new float[2];
  
  private Rect mTmpRect;
  
  VelocityTracker mVelocityTracker;
  
  public ItemTouchHelper(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private void addChildDrawingOrderCallback() {
    if (Build.VERSION.SDK_INT >= 21)
      return; 
    if (this.mChildDrawingOrderCallback == null)
      this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
          final ItemTouchHelper this$0;
          
          public int onGetChildDrawingOrder(int param1Int1, int param1Int2) {
            if (ItemTouchHelper.this.mOverdrawChild == null)
              return param1Int2; 
            int j = ItemTouchHelper.this.mOverdrawChildPosition;
            int i = j;
            if (j == -1) {
              i = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
              ItemTouchHelper.this.mOverdrawChildPosition = i;
            } 
            if (param1Int2 == param1Int1 - 1)
              return i; 
            if (param1Int2 >= i)
              param1Int2++; 
            return param1Int2;
          }
        }; 
    this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
  }
  
  private int checkHorizontalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0xC) != 0) {
      byte b1;
      float f1 = this.mDx;
      byte b2 = 8;
      if (f1 > 0.0F) {
        b1 = 8;
      } else {
        b1 = 4;
      } 
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null && this.mActivePointerId > -1) {
        velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        float f = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        f1 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (f <= 0.0F)
          b2 = 4; 
        f = Math.abs(f);
        if ((b2 & paramInt) != 0 && b1 == b2 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f1))
          return b2; 
      } 
      f1 = this.mRecyclerView.getWidth();
      float f2 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b1) != 0 && Math.abs(this.mDx) > f1 * f2)
        return b1; 
    } 
    return 0;
  }
  
  private int checkVerticalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    if ((paramInt & 0x3) != 0) {
      byte b1;
      float f1 = this.mDy;
      byte b2 = 2;
      if (f1 > 0.0F) {
        b1 = 2;
      } else {
        b1 = 1;
      } 
      VelocityTracker velocityTracker = this.mVelocityTracker;
      if (velocityTracker != null && this.mActivePointerId > -1) {
        velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
        f1 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        float f = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        if (f <= 0.0F)
          b2 = 1; 
        f = Math.abs(f);
        if ((b2 & paramInt) != 0 && b2 == b1 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f1))
          return b2; 
      } 
      float f2 = this.mRecyclerView.getHeight();
      f1 = this.mCallback.getSwipeThreshold(paramViewHolder);
      if ((paramInt & b1) != 0 && Math.abs(this.mDy) > f2 * f1)
        return b1; 
    } 
    return 0;
  }
  
  private void destroyCallbacks() {
    this.mRecyclerView.removeItemDecoration(this);
    this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
      this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
    } 
    this.mRecoverAnimations.clear();
    this.mOverdrawChild = null;
    this.mOverdrawChildPosition = -1;
    releaseVelocityTracker();
    stopGestureDetection();
  }
  
  private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder paramViewHolder) {
    List<RecyclerView.ViewHolder> list = this.mSwapTargets;
    if (list == null) {
      this.mSwapTargets = new ArrayList<RecyclerView.ViewHolder>();
      this.mDistances = new ArrayList<Integer>();
    } else {
      list.clear();
      this.mDistances.clear();
    } 
    int j = this.mCallback.getBoundingBoxMargin();
    int m = Math.round(this.mSelectedStartX + this.mDx) - j;
    int k = Math.round(this.mSelectedStartY + this.mDy) - j;
    int i = paramViewHolder.itemView.getWidth();
    j *= 2;
    int n = i + m + j;
    int i4 = paramViewHolder.itemView.getHeight() + k + j;
    int i1 = (m + n) / 2;
    int i3 = (k + i4) / 2;
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    int i2 = layoutManager.getChildCount();
    for (i = 0; i < i2; i++) {
      View view = layoutManager.getChildAt(i);
      if (view != paramViewHolder.itemView && view.getBottom() >= k && view.getTop() <= i4 && view.getRight() >= m && view.getLeft() <= n) {
        RecyclerView.ViewHolder viewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, viewHolder)) {
          int i5 = Math.abs(i1 - (view.getLeft() + view.getRight()) / 2);
          j = Math.abs(i3 - (view.getTop() + view.getBottom()) / 2);
          int i7 = i5 * i5 + j * j;
          int i6 = this.mSwapTargets.size();
          i5 = 0;
          j = 0;
          while (i5 < i6 && i7 > ((Integer)this.mDistances.get(i5)).intValue()) {
            j++;
            i5++;
          } 
          this.mSwapTargets.add(j, viewHolder);
          this.mDistances.add(j, Integer.valueOf(i7));
        } 
      } 
    } 
    return this.mSwapTargets;
  }
  
  private RecyclerView.ViewHolder findSwipedView(MotionEvent paramMotionEvent) {
    RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
    int i = this.mActivePointerId;
    if (i == -1)
      return null; 
    i = paramMotionEvent.findPointerIndex(i);
    float f4 = paramMotionEvent.getX(i);
    float f3 = this.mInitialTouchX;
    float f1 = paramMotionEvent.getY(i);
    float f2 = this.mInitialTouchY;
    f3 = Math.abs(f4 - f3);
    f1 = Math.abs(f1 - f2);
    i = this.mSlop;
    if (f3 < i && f1 < i)
      return null; 
    if (f3 > f1 && layoutManager.canScrollHorizontally())
      return null; 
    if (f1 > f3 && layoutManager.canScrollVertically())
      return null; 
    View view = findChildView(paramMotionEvent);
    return (view == null) ? null : this.mRecyclerView.getChildViewHolder(view);
  }
  
  private void getSelectedDxDy(float[] paramArrayOffloat) {
    if ((this.mSelectedFlags & 0xC) != 0) {
      paramArrayOffloat[0] = this.mSelectedStartX + this.mDx - this.mSelected.itemView.getLeft();
    } else {
      paramArrayOffloat[0] = this.mSelected.itemView.getTranslationX();
    } 
    if ((this.mSelectedFlags & 0x3) != 0) {
      paramArrayOffloat[1] = this.mSelectedStartY + this.mDy - this.mSelected.itemView.getTop();
    } else {
      paramArrayOffloat[1] = this.mSelected.itemView.getTranslationY();
    } 
  }
  
  private static boolean hitTest(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    boolean bool;
    if (paramFloat1 >= paramFloat3 && paramFloat1 <= paramFloat3 + paramView.getWidth() && paramFloat2 >= paramFloat4 && paramFloat2 <= paramFloat4 + paramView.getHeight()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void releaseVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private void setupCallbacks() {
    this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
    this.mRecyclerView.addItemDecoration(this);
    this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
    this.mRecyclerView.addOnChildAttachStateChangeListener(this);
    startGestureDetection();
  }
  
  private void startGestureDetection() {
    this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
    this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener)this.mItemTouchHelperGestureListener);
  }
  
  private void stopGestureDetection() {
    ItemTouchHelperGestureListener itemTouchHelperGestureListener = this.mItemTouchHelperGestureListener;
    if (itemTouchHelperGestureListener != null) {
      itemTouchHelperGestureListener.doNotReactToLongPress();
      this.mItemTouchHelperGestureListener = null;
    } 
    if (this.mGestureDetector != null)
      this.mGestureDetector = null; 
  }
  
  private int swipeIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mActionState == 2)
      return 0; 
    int j = this.mCallback.getMovementFlags(this.mRecyclerView, paramViewHolder);
    int i = (this.mCallback.convertToAbsoluteDirection(j, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 0xFF00) >> 8;
    if (i == 0)
      return 0; 
    int k = (j & 0xFF00) >> 8;
    if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
      j = checkHorizontalSwipe(paramViewHolder, i);
      if (j > 0)
        return ((k & j) == 0) ? Callback.convertToRelativeDirection(j, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) : j; 
      i = checkVerticalSwipe(paramViewHolder, i);
      if (i > 0)
        return i; 
    } else {
      j = checkVerticalSwipe(paramViewHolder, i);
      if (j > 0)
        return j; 
      j = checkHorizontalSwipe(paramViewHolder, i);
      if (j > 0) {
        i = j;
        if ((k & j) == 0)
          i = Callback.convertToRelativeDirection(j, ViewCompat.getLayoutDirection((View)this.mRecyclerView)); 
        return i;
      } 
    } 
    return 0;
  }
  
  public void attachToRecyclerView(RecyclerView paramRecyclerView) {
    RecyclerView recyclerView = this.mRecyclerView;
    if (recyclerView == paramRecyclerView)
      return; 
    if (recyclerView != null)
      destroyCallbacks(); 
    this.mRecyclerView = paramRecyclerView;
    if (paramRecyclerView != null) {
      Resources resources = paramRecyclerView.getResources();
      this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
      this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
      setupCallbacks();
    } 
  }
  
  void checkSelectForSwipe(int paramInt1, MotionEvent paramMotionEvent, int paramInt2) {
    if (this.mSelected == null && paramInt1 == 2 && this.mActionState != 2 && this.mCallback.isItemViewSwipeEnabled()) {
      if (this.mRecyclerView.getScrollState() == 1)
        return; 
      RecyclerView.ViewHolder viewHolder = findSwipedView(paramMotionEvent);
      if (viewHolder == null)
        return; 
      paramInt1 = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder) & 0xFF00) >> 8;
      if (paramInt1 == 0)
        return; 
      float f1 = paramMotionEvent.getX(paramInt2);
      float f2 = paramMotionEvent.getY(paramInt2);
      f1 -= this.mInitialTouchX;
      float f3 = f2 - this.mInitialTouchY;
      float f4 = Math.abs(f1);
      f2 = Math.abs(f3);
      paramInt2 = this.mSlop;
      if (f4 < paramInt2 && f2 < paramInt2)
        return; 
      if (f4 > f2) {
        if (f1 < 0.0F && (paramInt1 & 0x4) == 0)
          return; 
        if (f1 > 0.0F && (paramInt1 & 0x8) == 0)
          return; 
      } else {
        if (f3 < 0.0F && (paramInt1 & 0x1) == 0)
          return; 
        if (f3 > 0.0F && (paramInt1 & 0x2) == 0)
          return; 
      } 
      this.mDy = 0.0F;
      this.mDx = 0.0F;
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      select(viewHolder, 1);
    } 
  }
  
  void endRecoverAnimation(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean) {
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      if (recoverAnimation.mViewHolder == paramViewHolder) {
        recoverAnimation.mOverridden |= paramBoolean;
        if (!recoverAnimation.mEnded)
          recoverAnimation.cancel(); 
        this.mRecoverAnimations.remove(i);
        return;
      } 
    } 
  }
  
  RecoverAnimation findAnimation(MotionEvent paramMotionEvent) {
    if (this.mRecoverAnimations.isEmpty())
      return null; 
    View view = findChildView(paramMotionEvent);
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      if (recoverAnimation.mViewHolder.itemView == view)
        return recoverAnimation; 
    } 
    return null;
  }
  
  View findChildView(MotionEvent paramMotionEvent) {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    RecyclerView.ViewHolder viewHolder = this.mSelected;
    if (viewHolder != null) {
      View view = viewHolder.itemView;
      if (hitTest(view, f1, f2, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy))
        return view; 
    } 
    for (int i = this.mRecoverAnimations.size() - 1; i >= 0; i--) {
      RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
      View view = recoverAnimation.mViewHolder.itemView;
      if (hitTest(view, f1, f2, recoverAnimation.mX, recoverAnimation.mY))
        return view; 
    } 
    return this.mRecyclerView.findChildViewUnder(f1, f2);
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    paramRect.setEmpty();
  }
  
  boolean hasRunningRecoverAnim() {
    int i = this.mRecoverAnimations.size();
    for (byte b = 0; b < i; b++) {
      if (!((RecoverAnimation)this.mRecoverAnimations.get(b)).mEnded)
        return true; 
    } 
    return false;
  }
  
  void moveIfNecessary(RecyclerView.ViewHolder paramViewHolder) {
    if (this.mRecyclerView.isLayoutRequested())
      return; 
    if (this.mActionState != 2)
      return; 
    float f = this.mCallback.getMoveThreshold(paramViewHolder);
    int i = (int)(this.mSelectedStartX + this.mDx);
    int k = (int)(this.mSelectedStartY + this.mDy);
    if (Math.abs(k - paramViewHolder.itemView.getTop()) < paramViewHolder.itemView.getHeight() * f && Math.abs(i - paramViewHolder.itemView.getLeft()) < paramViewHolder.itemView.getWidth() * f)
      return; 
    List<RecyclerView.ViewHolder> list = findSwapTargets(paramViewHolder);
    if (list.size() == 0)
      return; 
    RecyclerView.ViewHolder viewHolder = this.mCallback.chooseDropTarget(paramViewHolder, list, i, k);
    if (viewHolder == null) {
      this.mSwapTargets.clear();
      this.mDistances.clear();
      return;
    } 
    int m = viewHolder.getAdapterPosition();
    int j = paramViewHolder.getAdapterPosition();
    if (this.mCallback.onMove(this.mRecyclerView, paramViewHolder, viewHolder))
      this.mCallback.onMoved(this.mRecyclerView, paramViewHolder, j, viewHolder, m, i, k); 
  }
  
  void obtainVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.recycle(); 
    this.mVelocityTracker = VelocityTracker.obtain();
  }
  
  public void onChildViewAttachedToWindow(View paramView) {}
  
  public void onChildViewDetachedFromWindow(View paramView) {
    removeChildDrawingOrderCallbackIfNecessary(paramView);
    RecyclerView.ViewHolder viewHolder1 = this.mRecyclerView.getChildViewHolder(paramView);
    if (viewHolder1 == null)
      return; 
    RecyclerView.ViewHolder viewHolder2 = this.mSelected;
    if (viewHolder2 != null && viewHolder1 == viewHolder2) {
      select(null, 0);
    } else {
      endRecoverAnimation(viewHolder1, false);
      if (this.mPendingCleanup.remove(viewHolder1.itemView))
        this.mCallback.clearView(this.mRecyclerView, viewHolder1); 
    } 
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    float f1;
    float f2;
    this.mOverdrawChildPosition = -1;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      float[] arrayOfFloat = this.mTmpPosition;
      f2 = arrayOfFloat[0];
      f1 = arrayOfFloat[1];
    } else {
      f2 = 0.0F;
      f1 = 0.0F;
    } 
    this.mCallback.onDraw(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f2, f1);
  }
  
  public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState) {
    float f1;
    float f2;
    if (this.mSelected != null) {
      getSelectedDxDy(this.mTmpPosition);
      float[] arrayOfFloat = this.mTmpPosition;
      f2 = arrayOfFloat[0];
      f1 = arrayOfFloat[1];
    } else {
      f2 = 0.0F;
      f1 = 0.0F;
    } 
    this.mCallback.onDrawOver(paramCanvas, paramRecyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f2, f1);
  }
  
  void postDispatchSwipe(final RecoverAnimation anim, final int swipeDir) {
    this.mRecyclerView.post(new Runnable() {
          final ItemTouchHelper this$0;
          
          final ItemTouchHelper.RecoverAnimation val$anim;
          
          final int val$swipeDir;
          
          public void run() {
            if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !anim.mOverridden && anim.mViewHolder.getAdapterPosition() != -1) {
              RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
              if ((itemAnimator == null || !itemAnimator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                ItemTouchHelper.this.mCallback.onSwiped(anim.mViewHolder, swipeDir);
              } else {
                ItemTouchHelper.this.mRecyclerView.post(this);
              } 
            } 
          }
        });
  }
  
  void removeChildDrawingOrderCallbackIfNecessary(View paramView) {
    if (paramView == this.mOverdrawChild) {
      this.mOverdrawChild = null;
      if (this.mChildDrawingOrderCallback != null)
        this.mRecyclerView.setChildDrawingOrderCallback((RecyclerView.ChildDrawingOrderCallback)null); 
    } 
  }
  
  boolean scrollIfNecessary() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   4: ifnonnull -> 16
    //   7: aload_0
    //   8: ldc2_w -9223372036854775808
    //   11: putfield mDragScrollStartTimeInMs : J
    //   14: iconst_0
    //   15: ireturn
    //   16: invokestatic currentTimeMillis : ()J
    //   19: lstore #6
    //   21: aload_0
    //   22: getfield mDragScrollStartTimeInMs : J
    //   25: lstore #4
    //   27: lload #4
    //   29: ldc2_w -9223372036854775808
    //   32: lcmp
    //   33: ifne -> 42
    //   36: lconst_0
    //   37: lstore #4
    //   39: goto -> 49
    //   42: lload #6
    //   44: lload #4
    //   46: lsub
    //   47: lstore #4
    //   49: aload_0
    //   50: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   53: invokevirtual getLayoutManager : ()Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   56: astore #8
    //   58: aload_0
    //   59: getfield mTmpRect : Landroid/graphics/Rect;
    //   62: ifnonnull -> 76
    //   65: aload_0
    //   66: new android/graphics/Rect
    //   69: dup
    //   70: invokespecial <init> : ()V
    //   73: putfield mTmpRect : Landroid/graphics/Rect;
    //   76: aload #8
    //   78: aload_0
    //   79: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   82: getfield itemView : Landroid/view/View;
    //   85: aload_0
    //   86: getfield mTmpRect : Landroid/graphics/Rect;
    //   89: invokevirtual calculateItemDecorationsForChild : (Landroid/view/View;Landroid/graphics/Rect;)V
    //   92: aload #8
    //   94: invokevirtual canScrollHorizontally : ()Z
    //   97: ifeq -> 198
    //   100: aload_0
    //   101: getfield mSelectedStartX : F
    //   104: aload_0
    //   105: getfield mDx : F
    //   108: fadd
    //   109: f2i
    //   110: istore_2
    //   111: iload_2
    //   112: aload_0
    //   113: getfield mTmpRect : Landroid/graphics/Rect;
    //   116: getfield left : I
    //   119: isub
    //   120: aload_0
    //   121: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   124: invokevirtual getPaddingLeft : ()I
    //   127: isub
    //   128: istore_1
    //   129: aload_0
    //   130: getfield mDx : F
    //   133: fconst_0
    //   134: fcmpg
    //   135: ifge -> 145
    //   138: iload_1
    //   139: ifge -> 145
    //   142: goto -> 200
    //   145: aload_0
    //   146: getfield mDx : F
    //   149: fconst_0
    //   150: fcmpl
    //   151: ifle -> 198
    //   154: iload_2
    //   155: aload_0
    //   156: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   159: getfield itemView : Landroid/view/View;
    //   162: invokevirtual getWidth : ()I
    //   165: iadd
    //   166: aload_0
    //   167: getfield mTmpRect : Landroid/graphics/Rect;
    //   170: getfield right : I
    //   173: iadd
    //   174: aload_0
    //   175: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   178: invokevirtual getWidth : ()I
    //   181: aload_0
    //   182: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   185: invokevirtual getPaddingRight : ()I
    //   188: isub
    //   189: isub
    //   190: istore_1
    //   191: iload_1
    //   192: ifle -> 198
    //   195: goto -> 200
    //   198: iconst_0
    //   199: istore_1
    //   200: aload #8
    //   202: invokevirtual canScrollVertically : ()Z
    //   205: ifeq -> 306
    //   208: aload_0
    //   209: getfield mSelectedStartY : F
    //   212: aload_0
    //   213: getfield mDy : F
    //   216: fadd
    //   217: f2i
    //   218: istore_3
    //   219: iload_3
    //   220: aload_0
    //   221: getfield mTmpRect : Landroid/graphics/Rect;
    //   224: getfield top : I
    //   227: isub
    //   228: aload_0
    //   229: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   232: invokevirtual getPaddingTop : ()I
    //   235: isub
    //   236: istore_2
    //   237: aload_0
    //   238: getfield mDy : F
    //   241: fconst_0
    //   242: fcmpg
    //   243: ifge -> 253
    //   246: iload_2
    //   247: ifge -> 253
    //   250: goto -> 308
    //   253: aload_0
    //   254: getfield mDy : F
    //   257: fconst_0
    //   258: fcmpl
    //   259: ifle -> 306
    //   262: iload_3
    //   263: aload_0
    //   264: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   267: getfield itemView : Landroid/view/View;
    //   270: invokevirtual getHeight : ()I
    //   273: iadd
    //   274: aload_0
    //   275: getfield mTmpRect : Landroid/graphics/Rect;
    //   278: getfield bottom : I
    //   281: iadd
    //   282: aload_0
    //   283: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   286: invokevirtual getHeight : ()I
    //   289: aload_0
    //   290: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   293: invokevirtual getPaddingBottom : ()I
    //   296: isub
    //   297: isub
    //   298: istore_2
    //   299: iload_2
    //   300: ifle -> 306
    //   303: goto -> 308
    //   306: iconst_0
    //   307: istore_2
    //   308: iload_1
    //   309: istore_3
    //   310: iload_1
    //   311: ifeq -> 346
    //   314: aload_0
    //   315: getfield mCallback : Landroidx/recyclerview/widget/ItemTouchHelper$Callback;
    //   318: aload_0
    //   319: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   322: aload_0
    //   323: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   326: getfield itemView : Landroid/view/View;
    //   329: invokevirtual getWidth : ()I
    //   332: iload_1
    //   333: aload_0
    //   334: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   337: invokevirtual getWidth : ()I
    //   340: lload #4
    //   342: invokevirtual interpolateOutOfBoundsScroll : (Landroidx/recyclerview/widget/RecyclerView;IIIJ)I
    //   345: istore_3
    //   346: iload_2
    //   347: ifeq -> 385
    //   350: aload_0
    //   351: getfield mCallback : Landroidx/recyclerview/widget/ItemTouchHelper$Callback;
    //   354: aload_0
    //   355: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   358: aload_0
    //   359: getfield mSelected : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    //   362: getfield itemView : Landroid/view/View;
    //   365: invokevirtual getHeight : ()I
    //   368: iload_2
    //   369: aload_0
    //   370: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   373: invokevirtual getHeight : ()I
    //   376: lload #4
    //   378: invokevirtual interpolateOutOfBoundsScroll : (Landroidx/recyclerview/widget/RecyclerView;IIIJ)I
    //   381: istore_2
    //   382: goto -> 385
    //   385: iload_3
    //   386: ifne -> 405
    //   389: iload_2
    //   390: ifeq -> 396
    //   393: goto -> 405
    //   396: aload_0
    //   397: ldc2_w -9223372036854775808
    //   400: putfield mDragScrollStartTimeInMs : J
    //   403: iconst_0
    //   404: ireturn
    //   405: aload_0
    //   406: getfield mDragScrollStartTimeInMs : J
    //   409: ldc2_w -9223372036854775808
    //   412: lcmp
    //   413: ifne -> 422
    //   416: aload_0
    //   417: lload #6
    //   419: putfield mDragScrollStartTimeInMs : J
    //   422: aload_0
    //   423: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
    //   426: iload_3
    //   427: iload_2
    //   428: invokevirtual scrollBy : (II)V
    //   431: iconst_1
    //   432: ireturn
  }
  
  void select(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    boolean bool;
    if (paramViewHolder == this.mSelected && paramInt == this.mActionState)
      return; 
    this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
    int i = this.mActionState;
    endRecoverAnimation(paramViewHolder, true);
    this.mActionState = paramInt;
    if (paramInt == 2)
      if (paramViewHolder != null) {
        this.mOverdrawChild = paramViewHolder.itemView;
        addChildDrawingOrderCallback();
      } else {
        throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
      }  
    final RecyclerView.ViewHolder prevSelected = this.mSelected;
    if (viewHolder != null) {
      if (viewHolder.itemView.getParent() != null) {
        float f1;
        float f2;
        final int swipeDir;
        if (i == 2) {
          j = 0;
        } else {
          j = swipeIfNecessary(viewHolder);
        } 
        releaseVelocityTracker();
        if (j != 1 && j != 2) {
          if (j != 4 && j != 8 && j != 16 && j != 32) {
            f1 = 0.0F;
            f2 = 0.0F;
          } else {
            f1 = Math.signum(this.mDx);
            float f = this.mRecyclerView.getWidth();
            f2 = 0.0F;
            f1 *= f;
          } 
        } else {
          float f = Math.signum(this.mDy);
          f2 = this.mRecyclerView.getHeight();
          f1 = 0.0F;
          f2 = f * f2;
        } 
        if (i == 2) {
          bool = true;
        } else if (j > 0) {
          bool = true;
        } else {
          bool = true;
        } 
        getSelectedDxDy(this.mTmpPosition);
        float[] arrayOfFloat = this.mTmpPosition;
        float f4 = arrayOfFloat[0];
        float f3 = arrayOfFloat[1];
        RecoverAnimation recoverAnimation = new RecoverAnimation(viewHolder, bool, i, f4, f3, f1, f2) {
            final ItemTouchHelper this$0;
            
            final RecyclerView.ViewHolder val$prevSelected;
            
            final int val$swipeDir;
            
            public void onAnimationEnd(Animator param1Animator) {
              super.onAnimationEnd(param1Animator);
              if (this.mOverridden)
                return; 
              if (swipeDir <= 0) {
                ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, prevSelected);
              } else {
                ItemTouchHelper.this.mPendingCleanup.add(prevSelected.itemView);
                this.mIsPendingCleanup = true;
                int i = swipeDir;
                if (i > 0)
                  ItemTouchHelper.this.postDispatchSwipe(this, i); 
              } 
              if (ItemTouchHelper.this.mOverdrawChild == prevSelected.itemView)
                ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView); 
            }
          };
        recoverAnimation.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, bool, f1 - f4, f2 - f3));
        this.mRecoverAnimations.add(recoverAnimation);
        recoverAnimation.start();
        bool = true;
      } else {
        removeChildDrawingOrderCallbackIfNecessary(viewHolder.itemView);
        this.mCallback.clearView(this.mRecyclerView, viewHolder);
        bool = false;
      } 
      this.mSelected = null;
    } else {
      bool = false;
    } 
    if (paramViewHolder != null) {
      this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, paramViewHolder) & (1 << paramInt * 8 + 8) - 1) >> this.mActionState * 8;
      this.mSelectedStartX = paramViewHolder.itemView.getLeft();
      this.mSelectedStartY = paramViewHolder.itemView.getTop();
      this.mSelected = paramViewHolder;
      if (paramInt == 2)
        paramViewHolder.itemView.performHapticFeedback(0); 
    } 
    ViewParent viewParent = this.mRecyclerView.getParent();
    if (viewParent != null) {
      boolean bool1;
      if (this.mSelected != null) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      viewParent.requestDisallowInterceptTouchEvent(bool1);
    } 
    if (!bool)
      this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout(); 
    this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
    this.mRecyclerView.invalidate();
  }
  
  public void startDrag(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasDragFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start drag has been called but dragging is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 2);
  }
  
  public void startSwipe(RecyclerView.ViewHolder paramViewHolder) {
    if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, paramViewHolder)) {
      Log.e("ItemTouchHelper", "Start swipe has been called but swiping is not enabled");
      return;
    } 
    if (paramViewHolder.itemView.getParent() != this.mRecyclerView) {
      Log.e("ItemTouchHelper", "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
      return;
    } 
    obtainVelocityTracker();
    this.mDy = 0.0F;
    this.mDx = 0.0F;
    select(paramViewHolder, 1);
  }
  
  void updateDxDy(MotionEvent paramMotionEvent, int paramInt1, int paramInt2) {
    float f2 = paramMotionEvent.getX(paramInt2);
    float f1 = paramMotionEvent.getY(paramInt2);
    f2 -= this.mInitialTouchX;
    this.mDx = f2;
    this.mDy = f1 - this.mInitialTouchY;
    if ((paramInt1 & 0x4) == 0)
      this.mDx = Math.max(0.0F, f2); 
    if ((paramInt1 & 0x8) == 0)
      this.mDx = Math.min(0.0F, this.mDx); 
    if ((paramInt1 & 0x1) == 0)
      this.mDy = Math.max(0.0F, this.mDy); 
    if ((paramInt1 & 0x2) == 0)
      this.mDy = Math.min(0.0F, this.mDy); 
  }
  
  public static abstract class Callback {
    private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
    
    public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
    
    public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
    
    private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000L;
    
    static final int RELATIVE_DIR_FLAGS = 3158064;
    
    private static final Interpolator sDragScrollInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          return param2Float * param2Float * param2Float * param2Float * param2Float;
        }
      };
    
    private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
        public float getInterpolation(float param2Float) {
          param2Float--;
          return param2Float * param2Float * param2Float * param2Float * param2Float + 1.0F;
        }
      };
    
    private int mCachedMaxScrollSpeed = -1;
    
    public static int convertToRelativeDirection(int param1Int1, int param1Int2) {
      int j = param1Int1 & 0xC0C0C;
      if (j == 0)
        return param1Int1; 
      int i = param1Int1 & (j ^ 0xFFFFFFFF);
      if (param1Int2 == 0) {
        param1Int1 = j << 2;
        param1Int2 = i;
        return param1Int2 | param1Int1;
      } 
      param1Int1 = j << 1;
      param1Int2 = i | 0xFFF3F3F3 & param1Int1;
      param1Int1 = (param1Int1 & 0xC0C0C) << 2;
      return param1Int2 | param1Int1;
    }
    
    public static ItemTouchUIUtil getDefaultUIUtil() {
      return ItemTouchUIUtilImpl.INSTANCE;
    }
    
    private int getMaxDragScroll(RecyclerView param1RecyclerView) {
      if (this.mCachedMaxScrollSpeed == -1)
        this.mCachedMaxScrollSpeed = param1RecyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame); 
      return this.mCachedMaxScrollSpeed;
    }
    
    public static int makeFlag(int param1Int1, int param1Int2) {
      return param1Int2 << param1Int1 * 8;
    }
    
    public static int makeMovementFlags(int param1Int1, int param1Int2) {
      int i = makeFlag(0, param1Int2 | param1Int1);
      param1Int2 = makeFlag(1, param1Int2);
      return makeFlag(2, param1Int1) | param1Int2 | i;
    }
    
    public boolean canDropOver(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2) {
      return true;
    }
    
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder param1ViewHolder, List<RecyclerView.ViewHolder> param1List, int param1Int1, int param1Int2) {
      int i1 = param1ViewHolder.itemView.getWidth();
      int j = param1ViewHolder.itemView.getHeight();
      int m = param1Int1 - param1ViewHolder.itemView.getLeft();
      int k = param1Int2 - param1ViewHolder.itemView.getTop();
      int n = param1List.size();
      RecyclerView.ViewHolder viewHolder = null;
      int i = -1;
      for (byte b = 0; b < n; b++) {
        RecyclerView.ViewHolder viewHolder1 = param1List.get(b);
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        int i2 = i;
        if (m > 0) {
          int i3 = viewHolder1.itemView.getRight() - param1Int1 + i1;
          viewHolder2 = viewHolder;
          i2 = i;
          if (i3 < 0) {
            viewHolder2 = viewHolder;
            i2 = i;
            if (viewHolder1.itemView.getRight() > param1ViewHolder.itemView.getRight()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = i;
              if (i3 > i) {
                viewHolder2 = viewHolder1;
                i2 = i3;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        i = i2;
        if (m < 0) {
          int i3 = viewHolder1.itemView.getLeft() - param1Int1;
          viewHolder = viewHolder2;
          i = i2;
          if (i3 > 0) {
            viewHolder = viewHolder2;
            i = i2;
            if (viewHolder1.itemView.getLeft() < param1ViewHolder.itemView.getLeft()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              i = i2;
              if (i3 > i2) {
                viewHolder = viewHolder1;
                i = i3;
              } 
            } 
          } 
        } 
        viewHolder2 = viewHolder;
        i2 = i;
        if (k < 0) {
          int i3 = viewHolder1.itemView.getTop() - param1Int2;
          viewHolder2 = viewHolder;
          i2 = i;
          if (i3 > 0) {
            viewHolder2 = viewHolder;
            i2 = i;
            if (viewHolder1.itemView.getTop() < param1ViewHolder.itemView.getTop()) {
              i3 = Math.abs(i3);
              viewHolder2 = viewHolder;
              i2 = i;
              if (i3 > i) {
                viewHolder2 = viewHolder1;
                i2 = i3;
              } 
            } 
          } 
        } 
        viewHolder = viewHolder2;
        i = i2;
        if (k > 0) {
          int i3 = viewHolder1.itemView.getBottom() - param1Int2 + j;
          viewHolder = viewHolder2;
          i = i2;
          if (i3 < 0) {
            viewHolder = viewHolder2;
            i = i2;
            if (viewHolder1.itemView.getBottom() > param1ViewHolder.itemView.getBottom()) {
              i3 = Math.abs(i3);
              viewHolder = viewHolder2;
              i = i2;
              if (i3 > i2) {
                i = i3;
                viewHolder = viewHolder1;
              } 
            } 
          } 
        } 
      } 
      return viewHolder;
    }
    
    public void clearView(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      ItemTouchUIUtilImpl.INSTANCE.clearView(param1ViewHolder.itemView);
    }
    
    public int convertToAbsoluteDirection(int param1Int1, int param1Int2) {
      int i = param1Int1 & 0x303030;
      if (i == 0)
        return param1Int1; 
      param1Int1 &= i ^ 0xFFFFFFFF;
      if (param1Int2 == 0) {
        param1Int2 = i >> 2;
        return param1Int1 | param1Int2;
      } 
      param1Int2 = i >> 1;
      param1Int1 |= 0xFFCFCFCF & param1Int2;
      param1Int2 = (param1Int2 & 0x303030) >> 2;
      return param1Int1 | param1Int2;
    }
    
    final int getAbsoluteMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return convertToAbsoluteDirection(getMovementFlags(param1RecyclerView, param1ViewHolder), ViewCompat.getLayoutDirection((View)param1RecyclerView));
    }
    
    public long getAnimationDuration(RecyclerView param1RecyclerView, int param1Int, float param1Float1, float param1Float2) {
      long l;
      RecyclerView.ItemAnimator itemAnimator = param1RecyclerView.getItemAnimator();
      if (itemAnimator == null) {
        if (param1Int == 8) {
          l = 200L;
        } else {
          l = 250L;
        } 
        return l;
      } 
      if (param1Int == 8) {
        l = itemAnimator.getMoveDuration();
      } else {
        l = itemAnimator.getRemoveDuration();
      } 
      return l;
    }
    
    public int getBoundingBoxMargin() {
      return 0;
    }
    
    public float getMoveThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public abstract int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder);
    
    public float getSwipeEscapeVelocity(float param1Float) {
      return param1Float;
    }
    
    public float getSwipeThreshold(RecyclerView.ViewHolder param1ViewHolder) {
      return 0.5F;
    }
    
    public float getSwipeVelocityThreshold(float param1Float) {
      return param1Float;
    }
    
    boolean hasDragFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      boolean bool;
      if ((getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder) & 0xFF0000) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean hasSwipeFlag(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      boolean bool;
      if ((getAbsoluteMovementFlags(param1RecyclerView, param1ViewHolder) & 0xFF00) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int interpolateOutOfBoundsScroll(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3, long param1Long) {
      int j = getMaxDragScroll(param1RecyclerView);
      param1Int3 = Math.abs(param1Int2);
      int i = (int)Math.signum(param1Int2);
      float f2 = param1Int3;
      float f1 = 1.0F;
      f2 = Math.min(1.0F, f2 * 1.0F / param1Int1);
      param1Int1 = (int)((i * j) * sDragViewScrollCapInterpolator.getInterpolation(f2));
      if (param1Long <= 2000L)
        f1 = (float)param1Long / 2000.0F; 
      param1Int3 = (int)(param1Int1 * sDragScrollInterpolator.getInterpolation(f1));
      param1Int1 = param1Int3;
      if (param1Int3 == 0)
        if (param1Int2 > 0) {
          param1Int1 = 1;
        } else {
          param1Int1 = -1;
        }  
      return param1Int1;
    }
    
    public boolean isItemViewSwipeEnabled() {
      return true;
    }
    
    public boolean isLongPressDragEnabled() {
      return true;
    }
    
    public void onChildDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      ItemTouchUIUtilImpl.INSTANCE.onDraw(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    public void onChildDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, float param1Float1, float param1Float2, int param1Int, boolean param1Boolean) {
      ItemTouchUIUtilImpl.INSTANCE.onDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder.itemView, param1Float1, param1Float2, param1Int, param1Boolean);
    }
    
    void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int j = param1List.size();
      int i;
      for (i = 0; i < j; i++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(i);
        recoverAnimation.update();
        int k = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        i = param1Canvas.save();
        onChildDraw(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(i);
      } 
    }
    
    void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder, List<ItemTouchHelper.RecoverAnimation> param1List, int param1Int, float param1Float1, float param1Float2) {
      int j = param1List.size();
      boolean bool = false;
      int i;
      for (i = 0; i < j; i++) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(i);
        int k = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
        param1Canvas.restoreToCount(k);
      } 
      if (param1ViewHolder != null) {
        i = param1Canvas.save();
        onChildDrawOver(param1Canvas, param1RecyclerView, param1ViewHolder, param1Float1, param1Float2, param1Int, true);
        param1Canvas.restoreToCount(i);
      } 
      param1Int = j - 1;
      i = bool;
      while (param1Int >= 0) {
        ItemTouchHelper.RecoverAnimation recoverAnimation = param1List.get(param1Int);
        if (recoverAnimation.mEnded && !recoverAnimation.mIsPendingCleanup) {
          param1List.remove(param1Int);
        } else if (!recoverAnimation.mEnded) {
          i = 1;
        } 
        param1Int--;
      } 
      if (i != 0)
        param1RecyclerView.invalidate(); 
    }
    
    public abstract boolean onMove(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2);
    
    public void onMoved(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder1, int param1Int1, RecyclerView.ViewHolder param1ViewHolder2, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutManager layoutManager = param1RecyclerView.getLayoutManager();
      if (layoutManager instanceof ItemTouchHelper.ViewDropHandler) {
        ((ItemTouchHelper.ViewDropHandler)layoutManager).prepareForDrop(param1ViewHolder1.itemView, param1ViewHolder2.itemView, param1Int3, param1Int4);
        return;
      } 
      if (layoutManager.canScrollHorizontally()) {
        if (layoutManager.getDecoratedLeft(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingLeft())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedRight(param1ViewHolder2.itemView) >= param1RecyclerView.getWidth() - param1RecyclerView.getPaddingRight())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
      if (layoutManager.canScrollVertically()) {
        if (layoutManager.getDecoratedTop(param1ViewHolder2.itemView) <= param1RecyclerView.getPaddingTop())
          param1RecyclerView.scrollToPosition(param1Int2); 
        if (layoutManager.getDecoratedBottom(param1ViewHolder2.itemView) >= param1RecyclerView.getHeight() - param1RecyclerView.getPaddingBottom())
          param1RecyclerView.scrollToPosition(param1Int2); 
      } 
    }
    
    public void onSelectedChanged(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      if (param1ViewHolder != null)
        ItemTouchUIUtilImpl.INSTANCE.onSelected(param1ViewHolder.itemView); 
    }
    
    public abstract void onSwiped(RecyclerView.ViewHolder param1ViewHolder, int param1Int);
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      return param1Float * param1Float * param1Float * param1Float * param1Float;
    }
  }
  
  static final class null implements Interpolator {
    public float getInterpolation(float param1Float) {
      param1Float--;
      return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
    }
  }
  
  private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
    private boolean mShouldReactToLongPress = true;
    
    final ItemTouchHelper this$0;
    
    void doNotReactToLongPress() {
      this.mShouldReactToLongPress = false;
    }
    
    public boolean onDown(MotionEvent param1MotionEvent) {
      return true;
    }
    
    public void onLongPress(MotionEvent param1MotionEvent) {
      if (!this.mShouldReactToLongPress)
        return; 
      View view = ItemTouchHelper.this.findChildView(param1MotionEvent);
      if (view != null) {
        RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(view);
        if (viewHolder != null) {
          if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, viewHolder))
            return; 
          if (param1MotionEvent.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
            int i = param1MotionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
            float f1 = param1MotionEvent.getX(i);
            float f2 = param1MotionEvent.getY(i);
            ItemTouchHelper.this.mInitialTouchX = f1;
            ItemTouchHelper.this.mInitialTouchY = f2;
            ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
            itemTouchHelper.mDy = 0.0F;
            itemTouchHelper.mDx = 0.0F;
            if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled())
              ItemTouchHelper.this.select(viewHolder, 2); 
          } 
        } 
      } 
    }
  }
  
  private static class RecoverAnimation implements Animator.AnimatorListener {
    final int mActionState;
    
    final int mAnimationType;
    
    boolean mEnded = false;
    
    private float mFraction;
    
    boolean mIsPendingCleanup;
    
    boolean mOverridden = false;
    
    final float mStartDx;
    
    final float mStartDy;
    
    final float mTargetX;
    
    final float mTargetY;
    
    private final ValueAnimator mValueAnimator;
    
    final RecyclerView.ViewHolder mViewHolder;
    
    float mX;
    
    float mY;
    
    RecoverAnimation(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      this.mActionState = param1Int2;
      this.mAnimationType = param1Int1;
      this.mViewHolder = param1ViewHolder;
      this.mStartDx = param1Float1;
      this.mStartDy = param1Float2;
      this.mTargetX = param1Float3;
      this.mTargetY = param1Float4;
      ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      this.mValueAnimator = valueAnimator;
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final ItemTouchHelper.RecoverAnimation this$0;
            
            public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
              ItemTouchHelper.RecoverAnimation.this.setFraction(param2ValueAnimator.getAnimatedFraction());
            }
          });
      this.mValueAnimator.setTarget(param1ViewHolder.itemView);
      this.mValueAnimator.addListener(this);
      setFraction(0.0F);
    }
    
    public void cancel() {
      this.mValueAnimator.cancel();
    }
    
    public void onAnimationCancel(Animator param1Animator) {
      setFraction(1.0F);
    }
    
    public void onAnimationEnd(Animator param1Animator) {
      if (!this.mEnded)
        this.mViewHolder.setIsRecyclable(true); 
      this.mEnded = true;
    }
    
    public void onAnimationRepeat(Animator param1Animator) {}
    
    public void onAnimationStart(Animator param1Animator) {}
    
    public void setDuration(long param1Long) {
      this.mValueAnimator.setDuration(param1Long);
    }
    
    public void setFraction(float param1Float) {
      this.mFraction = param1Float;
    }
    
    public void start() {
      this.mViewHolder.setIsRecyclable(false);
      this.mValueAnimator.start();
    }
    
    public void update() {
      float f2 = this.mStartDx;
      float f1 = this.mTargetX;
      if (f2 == f1) {
        this.mX = this.mViewHolder.itemView.getTranslationX();
      } else {
        this.mX = f2 + this.mFraction * (f1 - f2);
      } 
      f2 = this.mStartDy;
      f1 = this.mTargetY;
      if (f2 == f1) {
        this.mY = this.mViewHolder.itemView.getTranslationY();
      } else {
        this.mY = f2 + this.mFraction * (f1 - f2);
      } 
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    final ItemTouchHelper.RecoverAnimation this$0;
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      this.this$0.setFraction(param1ValueAnimator.getAnimatedFraction());
    }
  }
  
  public static abstract class SimpleCallback extends Callback {
    private int mDefaultDragDirs;
    
    private int mDefaultSwipeDirs;
    
    public SimpleCallback(int param1Int1, int param1Int2) {
      this.mDefaultSwipeDirs = param1Int2;
      this.mDefaultDragDirs = param1Int1;
    }
    
    public int getDragDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultDragDirs;
    }
    
    public int getMovementFlags(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return makeMovementFlags(getDragDirs(param1RecyclerView, param1ViewHolder), getSwipeDirs(param1RecyclerView, param1ViewHolder));
    }
    
    public int getSwipeDirs(RecyclerView param1RecyclerView, RecyclerView.ViewHolder param1ViewHolder) {
      return this.mDefaultSwipeDirs;
    }
    
    public void setDefaultDragDirs(int param1Int) {
      this.mDefaultDragDirs = param1Int;
    }
    
    public void setDefaultSwipeDirs(int param1Int) {
      this.mDefaultSwipeDirs = param1Int;
    }
  }
  
  public static interface ViewDropHandler {
    void prepareForDrop(View param1View1, View param1View2, int param1Int1, int param1Int2);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ItemTouchHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */