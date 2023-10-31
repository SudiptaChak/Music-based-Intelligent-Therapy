package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pools;
import java.util.concurrent.ArrayBlockingQueue;

public final class AsyncLayoutInflater {
  private static final String TAG = "AsyncLayoutInflater";
  
  Handler mHandler;
  
  private Handler.Callback mHandlerCallback = new Handler.Callback() {
      final AsyncLayoutInflater this$0;
      
      public boolean handleMessage(Message param1Message) {
        AsyncLayoutInflater.InflateRequest inflateRequest = (AsyncLayoutInflater.InflateRequest)param1Message.obj;
        if (inflateRequest.view == null)
          inflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false); 
        inflateRequest.callback.onInflateFinished(inflateRequest.view, inflateRequest.resid, inflateRequest.parent);
        AsyncLayoutInflater.this.mInflateThread.releaseRequest(inflateRequest);
        return true;
      }
    };
  
  InflateThread mInflateThread;
  
  LayoutInflater mInflater;
  
  public AsyncLayoutInflater(Context paramContext) {
    this.mInflater = new BasicInflater(paramContext);
    this.mHandler = new Handler(this.mHandlerCallback);
    this.mInflateThread = InflateThread.getInstance();
  }
  
  public void inflate(int paramInt, ViewGroup paramViewGroup, OnInflateFinishedListener paramOnInflateFinishedListener) {
    if (paramOnInflateFinishedListener != null) {
      InflateRequest inflateRequest = this.mInflateThread.obtainRequest();
      inflateRequest.inflater = this;
      inflateRequest.resid = paramInt;
      inflateRequest.parent = paramViewGroup;
      inflateRequest.callback = paramOnInflateFinishedListener;
      this.mInflateThread.enqueue(inflateRequest);
      return;
    } 
    throw new NullPointerException("callback argument may not be null!");
  }
  
  private static class BasicInflater extends LayoutInflater {
    private static final String[] sClassPrefixList = new String[] { "android.widget.", "android.webkit.", "android.app." };
    
    BasicInflater(Context param1Context) {
      super(param1Context);
    }
    
    public LayoutInflater cloneInContext(Context param1Context) {
      return new BasicInflater(param1Context);
    }
    
    protected View onCreateView(String param1String, AttributeSet param1AttributeSet) throws ClassNotFoundException {
      String[] arrayOfString = sClassPrefixList;
      int i = arrayOfString.length;
      byte b = 0;
      while (true) {
        if (b < i) {
          String str = arrayOfString[b];
          try {
            View view = createView(param1String, str, param1AttributeSet);
            if (view != null)
              return view; 
          } catch (ClassNotFoundException classNotFoundException) {}
          b++;
          continue;
        } 
        return super.onCreateView(param1String, param1AttributeSet);
      } 
    }
  }
  
  private static class InflateRequest {
    AsyncLayoutInflater.OnInflateFinishedListener callback;
    
    AsyncLayoutInflater inflater;
    
    ViewGroup parent;
    
    int resid;
    
    View view;
  }
  
  private static class InflateThread extends Thread {
    private static final InflateThread sInstance;
    
    private ArrayBlockingQueue<AsyncLayoutInflater.InflateRequest> mQueue = new ArrayBlockingQueue<AsyncLayoutInflater.InflateRequest>(10);
    
    private Pools.SynchronizedPool<AsyncLayoutInflater.InflateRequest> mRequestPool = new Pools.SynchronizedPool(10);
    
    static {
      InflateThread inflateThread = new InflateThread();
      sInstance = inflateThread;
      inflateThread.start();
    }
    
    public static InflateThread getInstance() {
      return sInstance;
    }
    
    public void enqueue(AsyncLayoutInflater.InflateRequest param1InflateRequest) {
      try {
        this.mQueue.put(param1InflateRequest);
        return;
      } catch (InterruptedException interruptedException) {
        throw new RuntimeException("Failed to enqueue async inflate request", interruptedException);
      } 
    }
    
    public AsyncLayoutInflater.InflateRequest obtainRequest() {
      AsyncLayoutInflater.InflateRequest inflateRequest2 = (AsyncLayoutInflater.InflateRequest)this.mRequestPool.acquire();
      AsyncLayoutInflater.InflateRequest inflateRequest1 = inflateRequest2;
      if (inflateRequest2 == null)
        inflateRequest1 = new AsyncLayoutInflater.InflateRequest(); 
      return inflateRequest1;
    }
    
    public void releaseRequest(AsyncLayoutInflater.InflateRequest param1InflateRequest) {
      param1InflateRequest.callback = null;
      param1InflateRequest.inflater = null;
      param1InflateRequest.parent = null;
      param1InflateRequest.resid = 0;
      param1InflateRequest.view = null;
      this.mRequestPool.release(param1InflateRequest);
    }
    
    public void run() {
      while (true)
        runInner(); 
    }
    
    public void runInner() {
      try {
        AsyncLayoutInflater.InflateRequest inflateRequest = this.mQueue.take();
        try {
          inflateRequest.view = inflateRequest.inflater.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
        } catch (RuntimeException runtimeException) {
          Log.w("AsyncLayoutInflater", "Failed to inflate resource in the background! Retrying on the UI thread", runtimeException);
        } 
        Message.obtain(inflateRequest.inflater.mHandler, 0, inflateRequest).sendToTarget();
        return;
      } catch (InterruptedException interruptedException) {
        Log.w("AsyncLayoutInflater", interruptedException);
        return;
      } 
    }
  }
  
  public static interface OnInflateFinishedListener {
    void onInflateFinished(View param1View, int param1Int, ViewGroup param1ViewGroup);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\asynclayoutinflater\view\AsyncLayoutInflater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */