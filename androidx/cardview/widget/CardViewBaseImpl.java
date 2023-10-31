package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

class CardViewBaseImpl implements CardViewImpl {
  final RectF mCornerRect = new RectF();
  
  private RoundRectDrawableWithShadow createBackground(Context paramContext, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3) {
    return new RoundRectDrawableWithShadow(paramContext.getResources(), paramColorStateList, paramFloat1, paramFloat2, paramFloat3);
  }
  
  private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate paramCardViewDelegate) {
    return (RoundRectDrawableWithShadow)paramCardViewDelegate.getCardBackground();
  }
  
  public ColorStateList getBackgroundColor(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getColor();
  }
  
  public float getElevation(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getShadowSize();
  }
  
  public float getMaxElevation(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMaxShadowSize();
  }
  
  public float getMinHeight(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMinHeight();
  }
  
  public float getMinWidth(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getMinWidth();
  }
  
  public float getRadius(CardViewDelegate paramCardViewDelegate) {
    return getShadowBackground(paramCardViewDelegate).getCornerRadius();
  }
  
  public void initStatic() {
    RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
        final CardViewBaseImpl this$0;
        
        public void drawRoundRect(Canvas param1Canvas, RectF param1RectF, float param1Float, Paint param1Paint) {
          float f2 = 2.0F * param1Float;
          float f3 = param1RectF.width() - f2 - 1.0F;
          float f1 = param1RectF.height();
          if (param1Float >= 1.0F) {
            float f5 = param1Float + 0.5F;
            RectF rectF = CardViewBaseImpl.this.mCornerRect;
            float f4 = -f5;
            rectF.set(f4, f4, f5, f5);
            int i = param1Canvas.save();
            param1Canvas.translate(param1RectF.left + f5, param1RectF.top + f5);
            param1Canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f3, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f1 - f2 - 1.0F, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.translate(f3, 0.0F);
            param1Canvas.rotate(90.0F);
            param1Canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, param1Paint);
            param1Canvas.restoreToCount(i);
            param1Canvas.drawRect(param1RectF.left + f5 - 1.0F, param1RectF.top, param1RectF.right - f5 + 1.0F, param1RectF.top + f5, param1Paint);
            param1Canvas.drawRect(param1RectF.left + f5 - 1.0F, param1RectF.bottom - f5, param1RectF.right - f5 + 1.0F, param1RectF.bottom, param1Paint);
          } 
          param1Canvas.drawRect(param1RectF.left, param1RectF.top + param1Float, param1RectF.right, param1RectF.bottom - param1Float, param1Paint);
        }
      };
  }
  
  public void initialize(CardViewDelegate paramCardViewDelegate, Context paramContext, ColorStateList paramColorStateList, float paramFloat1, float paramFloat2, float paramFloat3) {
    RoundRectDrawableWithShadow roundRectDrawableWithShadow = createBackground(paramContext, paramColorStateList, paramFloat1, paramFloat2, paramFloat3);
    roundRectDrawableWithShadow.setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    paramCardViewDelegate.setCardBackground(roundRectDrawableWithShadow);
    updatePadding(paramCardViewDelegate);
  }
  
  public void onCompatPaddingChanged(CardViewDelegate paramCardViewDelegate) {}
  
  public void onPreventCornerOverlapChanged(CardViewDelegate paramCardViewDelegate) {
    getShadowBackground(paramCardViewDelegate).setAddPaddingForCorners(paramCardViewDelegate.getPreventCornerOverlap());
    updatePadding(paramCardViewDelegate);
  }
  
  public void setBackgroundColor(CardViewDelegate paramCardViewDelegate, ColorStateList paramColorStateList) {
    getShadowBackground(paramCardViewDelegate).setColor(paramColorStateList);
  }
  
  public void setElevation(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setShadowSize(paramFloat);
  }
  
  public void setMaxElevation(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setMaxShadowSize(paramFloat);
    updatePadding(paramCardViewDelegate);
  }
  
  public void setRadius(CardViewDelegate paramCardViewDelegate, float paramFloat) {
    getShadowBackground(paramCardViewDelegate).setCornerRadius(paramFloat);
    updatePadding(paramCardViewDelegate);
  }
  
  public void updatePadding(CardViewDelegate paramCardViewDelegate) {
    Rect rect = new Rect();
    getShadowBackground(paramCardViewDelegate).getMaxShadowAndCornerPadding(rect);
    paramCardViewDelegate.setMinWidthHeightInternal((int)Math.ceil(getMinWidth(paramCardViewDelegate)), (int)Math.ceil(getMinHeight(paramCardViewDelegate)));
    paramCardViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\cardview\widget\CardViewBaseImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */