package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
  private final Headers headers;
  
  private final BufferedSource source;
  
  public RealResponseBody(Headers paramHeaders, BufferedSource paramBufferedSource) {
    this.headers = paramHeaders;
    this.source = paramBufferedSource;
  }
  
  public long contentLength() {
    return OkHeaders.contentLength(this.headers);
  }
  
  public MediaType contentType() {
    String str = this.headers.get("Content-Type");
    if (str != null) {
      MediaType mediaType = MediaType.parse(str);
    } else {
      str = null;
    } 
    return (MediaType)str;
  }
  
  public BufferedSource source() {
    return this.source;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\RealResponseBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */