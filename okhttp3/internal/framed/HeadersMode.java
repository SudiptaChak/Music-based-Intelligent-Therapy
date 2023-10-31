package okhttp3.internal.framed;

public enum HeadersMode {
  HTTP_20_HEADERS, SPDY_HEADERS, SPDY_REPLY, SPDY_SYN_STREAM;
  
  private static final HeadersMode[] $VALUES;
  
  static {
    SPDY_REPLY = new HeadersMode("SPDY_REPLY", 1);
    SPDY_HEADERS = new HeadersMode("SPDY_HEADERS", 2);
    HeadersMode headersMode = new HeadersMode("HTTP_20_HEADERS", 3);
    HTTP_20_HEADERS = headersMode;
    $VALUES = new HeadersMode[] { SPDY_SYN_STREAM, SPDY_REPLY, SPDY_HEADERS, headersMode };
  }
  
  public boolean failIfHeadersAbsent() {
    boolean bool;
    if (this == SPDY_HEADERS) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean failIfHeadersPresent() {
    boolean bool;
    if (this == SPDY_REPLY) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean failIfStreamAbsent() {
    return (this == SPDY_REPLY || this == SPDY_HEADERS);
  }
  
  public boolean failIfStreamPresent() {
    boolean bool;
    if (this == SPDY_SYN_STREAM) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\HeadersMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */