package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class TransitionUtils {
  private static final boolean HAS_IS_ATTACHED_TO_WINDOW;
  
  private static final boolean HAS_OVERLAY;
  
  private static final boolean HAS_PICTURE_BITMAP;
  
  private static final int MAX_IMAGE_SIZE = 1048576;
  
  static {
    boolean bool1;
    int i = Build.VERSION.SDK_INT;
    boolean bool2 = true;
    if (i >= 19) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    HAS_IS_ATTACHED_TO_WINDOW = bool1;
    if (Build.VERSION.SDK_INT >= 18) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    HAS_OVERLAY = bool1;
    if (Build.VERSION.SDK_INT >= 28) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    HAS_PICTURE_BITMAP = bool1;
  }
  
  static View copyViewImage(ViewGroup paramViewGroup, View paramView1, View paramView2) {
    Matrix matrix = new Matrix();
    matrix.setTranslate(-paramView2.getScrollX(), -paramView2.getScrollY());
    ViewUtils.transformMatrixToGlobal(paramView1, matrix);
    ViewUtils.transformMatrixToLocal((View)paramViewGroup, matrix);
    RectF rectF = new RectF(0.0F, 0.0F, paramView1.getWidth(), paramView1.getHeight());
    matrix.mapRect(rectF);
    int i = Math.round(rectF.left);
    int k = Math.round(rectF.top);
    int j = Math.round(rectF.right);
    int m = Math.round(rectF.bottom);
    ImageView imageView = new ImageView(paramView1.getContext());
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    Bitmap bitmap = createViewBitmap(paramView1, matrix, rectF, paramViewGroup);
    if (bitmap != null)
      imageView.setImageBitmap(bitmap); 
    imageView.measure(View.MeasureSpec.makeMeasureSpec(j - i, 1073741824), View.MeasureSpec.makeMeasureSpec(m - k, 1073741824));
    imageView.layout(i, k, j, m);
    return (View)imageView;
  }
  
  private static Bitmap createViewBitmap(View paramView, Matrix paramMatrix, RectF paramRectF, ViewGroup paramViewGroup) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    Bitmap bitmap;
    ViewGroup viewGroup;
    if (HAS_IS_ATTACHED_TO_WINDOW) {
      bool1 = paramView.isAttachedToWindow() ^ true;
      if (paramViewGroup == null) {
        bool3 = false;
      } else {
        bool3 = paramViewGroup.isAttachedToWindow();
      } 
    } else {
      bool1 = false;
      bool3 = false;
    } 
    boolean bool = HAS_OVERLAY;
    Canvas canvas2 = null;
    if (bool && bool1) {
      if (!bool3)
        return null; 
      viewGroup = (ViewGroup)paramView.getParent();
      bool2 = viewGroup.indexOfChild(paramView);
      paramViewGroup.getOverlay().add(paramView);
    } else {
      bool2 = false;
      viewGroup = null;
    } 
    int j = Math.round(paramRectF.width());
    int i = Math.round(paramRectF.height());
    Canvas canvas1 = canvas2;
    if (j > 0) {
      canvas1 = canvas2;
      if (i > 0) {
        float f = Math.min(1.0F, 1048576.0F / (j * i));
        j = Math.round(j * f);
        i = Math.round(i * f);
        paramMatrix.postTranslate(-paramRectF.left, -paramRectF.top);
        paramMatrix.postScale(f, f);
        if (HAS_PICTURE_BITMAP) {
          Picture picture = new Picture();
          canvas1 = picture.beginRecording(j, i);
          canvas1.concat(paramMatrix);
          paramView.draw(canvas1);
          picture.endRecording();
          bitmap = Bitmap.createBitmap(picture);
        } else {
          bitmap = Bitmap.createBitmap(j, i, Bitmap.Config.ARGB_8888);
          Canvas canvas = new Canvas(bitmap);
          canvas.concat(paramMatrix);
          paramView.draw(canvas);
        } 
      } 
    } 
    if (HAS_OVERLAY && bool1) {
      paramViewGroup.getOverlay().remove(paramView);
      viewGroup.addView(paramView, bool2);
    } 
    return bitmap;
  }
  
  static Animator mergeAnimators(Animator paramAnimator1, Animator paramAnimator2) {
    if (paramAnimator1 == null)
      return paramAnimator2; 
    if (paramAnimator2 == null)
      return paramAnimator1; 
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(new Animator[] { paramAnimator1, paramAnimator2 });
    return (Animator)animatorSet;
  }
  
  static class MatrixEvaluator implements TypeEvaluator<Matrix> {
    final float[] mTempEndValues = new float[9];
    
    final Matrix mTempMatrix = new Matrix();
    
    final float[] mTempStartValues = new float[9];
    
    public Matrix evaluate(float param1Float, Matrix param1Matrix1, Matrix param1Matrix2) {
      param1Matrix1.getValues(this.mTempStartValues);
      param1Matrix2.getValues(this.mTempEndValues);
      for (byte b = 0; b < 9; b++) {
        float[] arrayOfFloat1 = this.mTempEndValues;
        float f1 = arrayOfFloat1[b];
        float[] arrayOfFloat2 = this.mTempStartValues;
        float f2 = arrayOfFloat2[b];
        arrayOfFloat1[b] = arrayOfFloat2[b] + (f1 - f2) * param1Float;
      } 
      this.mTempMatrix.setValues(this.mTempEndValues);
      return this.mTempMatrix;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\transition\TransitionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */