package okhttp3.internal.http;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Sink;

public interface HttpStream {
  public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;
  
  void cancel();
  
  Sink createRequestBody(Request paramRequest, long paramLong) throws IOException;
  
  void finishRequest() throws IOException;
  
  ResponseBody openResponseBody(Response paramResponse) throws IOException;
  
  Response.Builder readResponseHeaders() throws IOException;
  
  void setHttpEngine(HttpEngine paramHttpEngine);
  
  void writeRequestBody(RetryableSink paramRetryableSink) throws IOException;
  
  void writeRequestHeaders(Request paramRequest) throws IOException;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\HttpStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */