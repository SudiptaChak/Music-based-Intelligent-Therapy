package okhttp3;

import java.io.IOException;

public interface Call {
  void cancel();
  
  void enqueue(Callback paramCallback);
  
  Response execute() throws IOException;
  
  boolean isCanceled();
  
  boolean isExecuted();
  
  Request request();
  
  public static interface Factory {
    Call newCall(Request param1Request);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Call.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */