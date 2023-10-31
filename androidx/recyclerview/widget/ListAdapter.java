package androidx.recyclerview.widget;

import java.util.List;

public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
  private final AsyncListDiffer<T> mHelper;
  
  protected ListAdapter(AsyncDifferConfig<T> paramAsyncDifferConfig) {
    this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), paramAsyncDifferConfig);
  }
  
  protected ListAdapter(DiffUtil.ItemCallback<T> paramItemCallback) {
    this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder<T>(paramItemCallback)).build());
  }
  
  protected T getItem(int paramInt) {
    return this.mHelper.getCurrentList().get(paramInt);
  }
  
  public int getItemCount() {
    return this.mHelper.getCurrentList().size();
  }
  
  public void submitList(List<T> paramList) {
    this.mHelper.submitList(paramList);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */