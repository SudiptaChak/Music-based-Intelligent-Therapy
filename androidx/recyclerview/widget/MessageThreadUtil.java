package androidx.recyclerview.widget;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MessageThreadUtil<T> implements ThreadUtil<T> {
  public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(final ThreadUtil.BackgroundCallback<T> callback) {
    return new ThreadUtil.BackgroundCallback<T>() {
        static final int LOAD_TILE = 3;
        
        static final int RECYCLE_TILE = 4;
        
        static final int REFRESH = 1;
        
        static final int UPDATE_RANGE = 2;
        
        private Runnable mBackgroundRunnable = new Runnable() {
            final MessageThreadUtil.null this$1;
            
            public void run() {
              while (true) {
                MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next();
                if (syncQueueItem == null) {
                  MessageThreadUtil.null.this.mBackgroundRunning.set(false);
                  return;
                } 
                int i = syncQueueItem.what;
                if (i != 1) {
                  if (i != 2) {
                    if (i != 3) {
                      if (i != 4) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unsupported message, what=");
                        stringBuilder.append(syncQueueItem.what);
                        Log.e("ThreadUtil", stringBuilder.toString());
                        continue;
                      } 
                      callback.recycleTile((TileList.Tile)syncQueueItem.data);
                      continue;
                    } 
                    callback.loadTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    continue;
                  } 
                  MessageThreadUtil.null.this.mQueue.removeMessages(2);
                  MessageThreadUtil.null.this.mQueue.removeMessages(3);
                  callback.updateRange(syncQueueItem.arg1, syncQueueItem.arg2, syncQueueItem.arg3, syncQueueItem.arg4, syncQueueItem.arg5);
                  continue;
                } 
                MessageThreadUtil.null.this.mQueue.removeMessages(1);
                callback.refresh(syncQueueItem.arg1);
              } 
            }
          };
        
        AtomicBoolean mBackgroundRunning = new AtomicBoolean(false);
        
        private final Executor mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        final MessageThreadUtil this$0;
        
        final ThreadUtil.BackgroundCallback val$callback;
        
        private void maybeExecuteBackgroundRunnable() {
          if (this.mBackgroundRunning.compareAndSet(false, true))
            this.mExecutor.execute(this.mBackgroundRunnable); 
        }
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        private void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessageAtFrontOfQueue(param1SyncQueueItem);
          maybeExecuteBackgroundRunnable();
        }
        
        public void loadTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void recycleTile(TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(4, 0, param1Tile));
        }
        
        public void refresh(int param1Int) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int, (Object)null));
        }
        
        public void updateRange(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
          sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, null));
        }
      };
  }
  
  public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(final ThreadUtil.MainThreadCallback<T> callback) {
    return new ThreadUtil.MainThreadCallback<T>() {
        static final int ADD_TILE = 2;
        
        static final int REMOVE_TILE = 3;
        
        static final int UPDATE_ITEM_COUNT = 1;
        
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
        
        private Runnable mMainThreadRunnable = new Runnable() {
            final MessageThreadUtil.null this$1;
            
            public void run() {
              for (MessageThreadUtil.SyncQueueItem syncQueueItem = MessageThreadUtil.null.this.mQueue.next(); syncQueueItem != null; syncQueueItem = MessageThreadUtil.null.this.mQueue.next()) {
                int i = syncQueueItem.what;
                if (i != 1) {
                  if (i != 2) {
                    if (i != 3) {
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("Unsupported message, what=");
                      stringBuilder.append(syncQueueItem.what);
                      Log.e("ThreadUtil", stringBuilder.toString());
                    } else {
                      callback.removeTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    } 
                  } else {
                    callback.addTile(syncQueueItem.arg1, (TileList.Tile)syncQueueItem.data);
                  } 
                } else {
                  callback.updateItemCount(syncQueueItem.arg1, syncQueueItem.arg2);
                } 
              } 
            }
          };
        
        final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();
        
        final MessageThreadUtil this$0;
        
        final ThreadUtil.MainThreadCallback val$callback;
        
        private void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
          this.mQueue.sendMessage(param1SyncQueueItem);
          this.mMainThreadHandler.post(this.mMainThreadRunnable);
        }
        
        public void addTile(int param1Int, TileList.Tile<T> param1Tile) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(2, param1Int, param1Tile));
        }
        
        public void removeTile(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, param1Int1, param1Int2));
        }
        
        public void updateItemCount(int param1Int1, int param1Int2) {
          sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(1, param1Int1, param1Int2));
        }
      };
  }
  
  static class MessageQueue {
    private MessageThreadUtil.SyncQueueItem mRoot;
    
    MessageThreadUtil.SyncQueueItem next() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: astore_1
      //   7: aload_1
      //   8: ifnonnull -> 15
      //   11: aload_0
      //   12: monitorexit
      //   13: aconst_null
      //   14: areturn
      //   15: aload_0
      //   16: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   19: astore_1
      //   20: aload_0
      //   21: aload_0
      //   22: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   25: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   28: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   31: aload_0
      //   32: monitorexit
      //   33: aload_1
      //   34: areturn
      //   35: astore_1
      //   36: aload_0
      //   37: monitorexit
      //   38: aload_1
      //   39: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	35	finally
      //   15	31	35	finally
    }
    
    void removeMessages(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: ifnull -> 43
      //   9: aload_0
      //   10: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   13: getfield what : I
      //   16: iload_1
      //   17: if_icmpne -> 43
      //   20: aload_0
      //   21: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   24: astore_2
      //   25: aload_0
      //   26: aload_0
      //   27: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   30: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   33: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   36: aload_2
      //   37: invokevirtual recycle : ()V
      //   40: goto -> 2
      //   43: aload_0
      //   44: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   47: ifnull -> 99
      //   50: aload_0
      //   51: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   54: astore_3
      //   55: aload_3
      //   56: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   59: astore_2
      //   60: aload_2
      //   61: ifnull -> 99
      //   64: aload_2
      //   65: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   68: astore #4
      //   70: aload_2
      //   71: getfield what : I
      //   74: iload_1
      //   75: if_icmpne -> 91
      //   78: aload_3
      //   79: aload #4
      //   81: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   84: aload_2
      //   85: invokevirtual recycle : ()V
      //   88: goto -> 93
      //   91: aload_2
      //   92: astore_3
      //   93: aload #4
      //   95: astore_2
      //   96: goto -> 60
      //   99: aload_0
      //   100: monitorexit
      //   101: return
      //   102: astore_2
      //   103: aload_0
      //   104: monitorexit
      //   105: aload_2
      //   106: athrow
      // Exception table:
      //   from	to	target	type
      //   2	40	102	finally
      //   43	60	102	finally
      //   64	88	102	finally
    }
    
    void sendMessage(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   6: ifnonnull -> 17
      //   9: aload_0
      //   10: aload_1
      //   11: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   14: aload_0
      //   15: monitorexit
      //   16: return
      //   17: aload_0
      //   18: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   21: astore_2
      //   22: aload_2
      //   23: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   26: ifnull -> 37
      //   29: aload_2
      //   30: getfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   33: astore_2
      //   34: goto -> 22
      //   37: aload_2
      //   38: aload_1
      //   39: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   42: aload_0
      //   43: monitorexit
      //   44: return
      //   45: astore_1
      //   46: aload_0
      //   47: monitorexit
      //   48: aload_1
      //   49: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	45	finally
      //   17	22	45	finally
      //   22	34	45	finally
      //   37	42	45	finally
    }
    
    void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem param1SyncQueueItem) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: aload_0
      //   4: getfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   7: putfield next : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   10: aload_0
      //   11: aload_1
      //   12: putfield mRoot : Landroidx/recyclerview/widget/MessageThreadUtil$SyncQueueItem;
      //   15: aload_0
      //   16: monitorexit
      //   17: return
      //   18: astore_1
      //   19: aload_0
      //   20: monitorexit
      //   21: aload_1
      //   22: athrow
      // Exception table:
      //   from	to	target	type
      //   2	15	18	finally
    }
  }
  
  static class SyncQueueItem {
    private static SyncQueueItem sPool;
    
    private static final Object sPoolLock = new Object();
    
    public int arg1;
    
    public int arg2;
    
    public int arg3;
    
    public int arg4;
    
    public int arg5;
    
    public Object data;
    
    SyncQueueItem next;
    
    public int what;
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3) {
      return obtainMessage(param1Int1, param1Int2, param1Int3, 0, 0, 0, null);
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, Object param1Object) {
      synchronized (sPoolLock) {
        SyncQueueItem syncQueueItem;
        if (sPool == null) {
          syncQueueItem = new SyncQueueItem();
          this();
        } else {
          syncQueueItem = sPool;
          sPool = sPool.next;
          syncQueueItem.next = null;
        } 
        syncQueueItem.what = param1Int1;
        syncQueueItem.arg1 = param1Int2;
        syncQueueItem.arg2 = param1Int3;
        syncQueueItem.arg3 = param1Int4;
        syncQueueItem.arg4 = param1Int5;
        syncQueueItem.arg5 = param1Int6;
        syncQueueItem.data = param1Object;
        return syncQueueItem;
      } 
    }
    
    static SyncQueueItem obtainMessage(int param1Int1, int param1Int2, Object param1Object) {
      return obtainMessage(param1Int1, param1Int2, 0, 0, 0, 0, param1Object);
    }
    
    void recycle() {
      this.next = null;
      this.arg5 = 0;
      this.arg4 = 0;
      this.arg3 = 0;
      this.arg2 = 0;
      this.arg1 = 0;
      this.what = 0;
      this.data = null;
      synchronized (sPoolLock) {
        if (sPool != null)
          this.next = sPool; 
        sPool = this;
        return;
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\MessageThreadUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */