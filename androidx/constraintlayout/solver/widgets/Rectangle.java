package androidx.constraintlayout.solver.widgets;

public class Rectangle {
  public int height;
  
  public int width;
  
  public int x;
  
  public int y;
  
  public boolean contains(int paramInt1, int paramInt2) {
    int i = this.x;
    if (paramInt1 >= i && paramInt1 < i + this.width) {
      paramInt1 = this.y;
      if (paramInt2 >= paramInt1 && paramInt2 < paramInt1 + this.height)
        return true; 
    } 
    return false;
  }
  
  public int getCenterX() {
    return (this.x + this.width) / 2;
  }
  
  public int getCenterY() {
    return (this.y + this.height) / 2;
  }
  
  void grow(int paramInt1, int paramInt2) {
    this.x -= paramInt1;
    this.y -= paramInt2;
    this.width += paramInt1 * 2;
    this.height += paramInt2 * 2;
  }
  
  boolean intersects(Rectangle paramRectangle) {
    int j = this.x;
    int i = paramRectangle.x;
    if (j >= i && j < i + paramRectangle.width) {
      j = this.y;
      i = paramRectangle.y;
      if (j >= i && j < i + paramRectangle.height)
        return true; 
    } 
    return false;
  }
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.x = paramInt1;
    this.y = paramInt2;
    this.width = paramInt3;
    this.height = paramInt4;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\constraintlayout\solver\widgets\Rectangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */