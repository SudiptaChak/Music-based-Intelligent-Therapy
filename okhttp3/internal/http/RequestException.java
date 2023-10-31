package okhttp3.internal.http;

import java.io.IOException;

public final class RequestException extends Exception {
  public RequestException(IOException paramIOException) {
    super(paramIOException);
  }
  
  public IOException getCause() {
    return (IOException)super.getCause();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\RequestException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */