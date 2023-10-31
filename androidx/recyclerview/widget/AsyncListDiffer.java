package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T> {
  private static final Executor sMainThreadExecutor = new MainThreadExecutor();
  
  final AsyncDifferConfig<T> mConfig;
  
  private List<T> mList;
  
  final Executor mMainThreadExecutor;
  
  int mMaxScheduledGeneration;
  
  private List<T> mReadOnlyList = Collections.emptyList();
  
  private final ListUpdateCallback mUpdateCallback;
  
  public AsyncListDiffer(ListUpdateCallback paramListUpdateCallback, AsyncDifferConfig<T> paramAsyncDifferConfig) {
    this.mUpdateCallback = paramListUpdateCallback;
    this.mConfig = paramAsyncDifferConfig;
    if (paramAsyncDifferConfig.getMainThreadExecutor() != null) {
      this.mMainThreadExecutor = paramAsyncDifferConfig.getMainThreadExecutor();
    } else {
      this.mMainThreadExecutor = sMainThreadExecutor;
    } 
  }
  
  public AsyncListDiffer(RecyclerView.Adapter paramAdapter, DiffUtil.ItemCallback<T> paramItemCallback) {
    this(new AdapterListUpdateCallback(paramAdapter), (new AsyncDifferConfig.Builder<T>(paramItemCallback)).build());
  }
  
  public List<T> getCurrentList() {
    return this.mReadOnlyList;
  }
  
  void latchList(List<T> paramList, DiffUtil.DiffResult paramDiffResult) {
    this.mList = paramList;
    this.mReadOnlyList = Collections.unmodifiableList(paramList);
    paramDiffResult.dispatchUpdatesTo(this.mUpdateCallback);
  }
  
  public void submitList(final List<T> newList) {
    final int runGeneration = this.mMaxScheduledGeneration + 1;
    this.mMaxScheduledGeneration = i;
    final List<T> oldList = this.mList;
    if (newList == list)
      return; 
    if (newList == null) {
      i = list.size();
      this.mList = null;
      this.mReadOnlyList = Collections.emptyList();
      this.mUpdateCallback.onRemoved(0, i);
      return;
    } 
    if (list == null) {
      this.mList = newList;
      this.mReadOnlyList = Collections.unmodifiableList(newList);
      this.mUpdateCallback.onInserted(0, newList.size());
      return;
    } 
    this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
          final AsyncListDiffer this$0;
          
          final List val$newList;
          
          final List val$oldList;
          
          final int val$runGeneration;
          
          public void run() {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                  final AsyncListDiffer.null this$1;
                  
                  public boolean areContentsTheSame(int param2Int1, int param2Int2) {
                    Object object1 = oldList.get(param2Int1);
                    Object object2 = newList.get(param2Int2);
                    if (object1 != null && object2 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(object1, object2); 
                    if (object1 == null && object2 == null)
                      return true; 
                    throw new AssertionError();
                  }
                  
                  public boolean areItemsTheSame(int param2Int1, int param2Int2) {
                    boolean bool;
                    Object object2 = oldList.get(param2Int1);
                    Object object1 = newList.get(param2Int2);
                    if (object2 != null && object1 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(object2, object1); 
                    if (object2 == null && object1 == null) {
                      bool = true;
                    } else {
                      bool = false;
                    } 
                    return bool;
                  }
                  
                  public Object getChangePayload(int param2Int1, int param2Int2) {
                    Object object1 = oldList.get(param2Int1);
                    Object object2 = newList.get(param2Int2);
                    if (object1 != null && object2 != null)
                      return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(object1, object2); 
                    throw new AssertionError();
                  }
                  
                  public int getNewListSize() {
                    return newList.size();
                  }
                  
                  public int getOldListSize() {
                    return oldList.size();
                  }
                });
            AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                  final AsyncListDiffer.null this$1;
                  
                  final DiffUtil.DiffResult val$result;
                  
                  public void run() {
                    if (AsyncListDiffer.this.mMaxScheduledGeneration == runGeneration)
                      AsyncListDiffer.this.latchList(newList, result); 
                  }
                });
          }
        });
  }
  
  private static class MainThreadExecutor implements Executor {
    final Handler mHandler = new Handler(Looper.getMainLooper());
    
    public void execute(Runnable param1Runnable) {
      this.mHandler.post(param1Runnable);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\AsyncListDiffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */