package androidx.recyclerview.widget;

public interface ListUpdateCallback {
  void onChanged(int paramInt1, int paramInt2, Object paramObject);
  
  void onInserted(int paramInt1, int paramInt2);
  
  void onMoved(int paramInt1, int paramInt2);
  
  void onRemoved(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ListUpdateCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */