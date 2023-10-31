package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.framed.Header;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Http2xStream implements HttpStream {
  private static final ByteString CONNECTION = ByteString.encodeUtf8("connection");
  
  private static final ByteString ENCODING;
  
  private static final ByteString HOST = ByteString.encodeUtf8("host");
  
  private static final List<ByteString> HTTP_2_SKIPPED_REQUEST_HEADERS;
  
  private static final List<ByteString> HTTP_2_SKIPPED_RESPONSE_HEADERS;
  
  private static final ByteString KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
  
  private static final ByteString PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
  
  private static final List<ByteString> SPDY_3_SKIPPED_REQUEST_HEADERS;
  
  private static final List<ByteString> SPDY_3_SKIPPED_RESPONSE_HEADERS;
  
  private static final ByteString TE;
  
  private static final ByteString TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");
  
  private static final ByteString UPGRADE;
  
  private final FramedConnection framedConnection;
  
  private HttpEngine httpEngine;
  
  private FramedStream stream;
  
  private final StreamAllocation streamAllocation;
  
  static {
    TE = ByteString.encodeUtf8("te");
    ENCODING = ByteString.encodeUtf8("encoding");
    UPGRADE = ByteString.encodeUtf8("upgrade");
    SPDY_3_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])new ByteString[] { 
          CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TRANSFER_ENCODING, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY, Header.TARGET_HOST, 
          Header.VERSION });
    SPDY_3_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])new ByteString[] { CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TRANSFER_ENCODING });
    HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])new ByteString[] { 
          CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE, Header.TARGET_METHOD, Header.TARGET_PATH, 
          Header.TARGET_SCHEME, Header.TARGET_AUTHORITY, Header.TARGET_HOST, Header.VERSION });
    HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])new ByteString[] { CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE });
  }
  
  public Http2xStream(StreamAllocation paramStreamAllocation, FramedConnection paramFramedConnection) {
    this.streamAllocation = paramStreamAllocation;
    this.framedConnection = paramFramedConnection;
  }
  
  public static List<Header> http2HeadersList(Request paramRequest) {
    Headers headers = paramRequest.headers();
    ArrayList<Header> arrayList = new ArrayList(headers.size() + 4);
    arrayList.add(new Header(Header.TARGET_METHOD, paramRequest.method()));
    arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(paramRequest.url())));
    ByteString byteString = Header.TARGET_AUTHORITY;
    HttpUrl httpUrl = paramRequest.url();
    byte b = 0;
    arrayList.add(new Header(byteString, Util.hostHeader(httpUrl, false)));
    arrayList.add(new Header(Header.TARGET_SCHEME, paramRequest.url().scheme()));
    int i = headers.size();
    while (b < i) {
      ByteString byteString1 = ByteString.encodeUtf8(headers.name(b).toLowerCase(Locale.US));
      if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(byteString1))
        arrayList.add(new Header(byteString1, headers.value(b))); 
      b++;
    } 
    return arrayList;
  }
  
  private static String joinOnNull(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder(paramString1);
    stringBuilder.append(false);
    stringBuilder.append(paramString2);
    return stringBuilder.toString();
  }
  
  public static Response.Builder readHttp2HeadersList(List<Header> paramList) throws IOException {
    Headers.Builder builder = new Headers.Builder();
    int i = paramList.size();
    String str = null;
    byte b = 0;
    while (b < i) {
      String str1;
      ByteString byteString = ((Header)paramList.get(b)).name;
      String str2 = ((Header)paramList.get(b)).value.utf8();
      if (byteString.equals(Header.RESPONSE_STATUS)) {
        str1 = str2;
      } else {
        str1 = str;
        if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
          builder.add(byteString.utf8(), str2);
          str1 = str;
        } 
      } 
      b++;
      str = str1;
    } 
    if (str != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("HTTP/1.1 ");
      stringBuilder.append(str);
      StatusLine statusLine = StatusLine.parse(stringBuilder.toString());
      return (new Response.Builder()).protocol(Protocol.HTTP_2).code(statusLine.code).message(statusLine.message).headers(builder.build());
    } 
    throw new ProtocolException("Expected ':status' header not present");
  }
  
  public static Response.Builder readSpdy3HeadersList(List<Header> paramList) throws IOException {
    Headers.Builder builder = new Headers.Builder();
    int i = paramList.size();
    String str2 = null;
    String str1 = "HTTP/1.1";
    for (byte b = 0; b < i; b++) {
      ByteString byteString = ((Header)paramList.get(b)).name;
      String str = ((Header)paramList.get(b)).value.utf8();
      int j = 0;
      while (j < str.length()) {
        String str3;
        String str5;
        int m = str.indexOf(false, j);
        int k = m;
        if (m == -1)
          k = str.length(); 
        String str4 = str.substring(j, k);
        if (byteString.equals(Header.RESPONSE_STATUS)) {
          str3 = str4;
          str5 = str1;
        } else if (byteString.equals(Header.VERSION)) {
          str3 = str2;
          str5 = str4;
        } else {
          str3 = str2;
          str5 = str1;
          if (!SPDY_3_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
            builder.add(byteString.utf8(), str4);
            str5 = str1;
            str3 = str2;
          } 
        } 
        j = k + 1;
        str2 = str3;
        str1 = str5;
      } 
    } 
    if (str2 != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(" ");
      stringBuilder.append(str2);
      StatusLine statusLine = StatusLine.parse(stringBuilder.toString());
      return (new Response.Builder()).protocol(Protocol.SPDY_3).code(statusLine.code).message(statusLine.message).headers(builder.build());
    } 
    throw new ProtocolException("Expected ':status' header not present");
  }
  
  public static List<Header> spdy3HeadersList(Request paramRequest) {
    Headers headers = paramRequest.headers();
    ArrayList<Header> arrayList = new ArrayList(headers.size() + 5);
    arrayList.add(new Header(Header.TARGET_METHOD, paramRequest.method()));
    arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(paramRequest.url())));
    arrayList.add(new Header(Header.VERSION, "HTTP/1.1"));
    arrayList.add(new Header(Header.TARGET_HOST, Util.hostHeader(paramRequest.url(), false)));
    arrayList.add(new Header(Header.TARGET_SCHEME, paramRequest.url().scheme()));
    LinkedHashSet<ByteString> linkedHashSet = new LinkedHashSet();
    int i = headers.size();
    for (byte b = 0; b < i; b++) {
      ByteString byteString = ByteString.encodeUtf8(headers.name(b).toLowerCase(Locale.US));
      if (!SPDY_3_SKIPPED_REQUEST_HEADERS.contains(byteString)) {
        String str = headers.value(b);
        if (linkedHashSet.add(byteString)) {
          arrayList.add(new Header(byteString, str));
        } else {
          for (byte b1 = 0; b1 < arrayList.size(); b1++) {
            if (((Header)arrayList.get(b1)).name.equals(byteString)) {
              arrayList.set(b1, new Header(byteString, joinOnNull(((Header)arrayList.get(b1)).value.utf8(), str)));
              break;
            } 
          } 
        } 
      } 
    } 
    return arrayList;
  }
  
  public void cancel() {
    FramedStream framedStream = this.stream;
    if (framedStream != null)
      framedStream.closeLater(ErrorCode.CANCEL); 
  }
  
  public Sink createRequestBody(Request paramRequest, long paramLong) throws IOException {
    return this.stream.getSink();
  }
  
  public void finishRequest() throws IOException {
    this.stream.getSink().close();
  }
  
  public ResponseBody openResponseBody(Response paramResponse) throws IOException {
    StreamFinishingSource streamFinishingSource = new StreamFinishingSource(this.stream.getSource());
    return new RealResponseBody(paramResponse.headers(), Okio.buffer((Source)streamFinishingSource));
  }
  
  public Response.Builder readResponseHeaders() throws IOException {
    Response.Builder builder;
    if (this.framedConnection.getProtocol() == Protocol.HTTP_2) {
      builder = readHttp2HeadersList(this.stream.getResponseHeaders());
    } else {
      builder = readSpdy3HeadersList(this.stream.getResponseHeaders());
    } 
    return builder;
  }
  
  public void setHttpEngine(HttpEngine paramHttpEngine) {
    this.httpEngine = paramHttpEngine;
  }
  
  public void writeRequestBody(RetryableSink paramRetryableSink) throws IOException {
    paramRetryableSink.writeToSocket(this.stream.getSink());
  }
  
  public void writeRequestHeaders(Request paramRequest) throws IOException {
    List<Header> list;
    if (this.stream != null)
      return; 
    this.httpEngine.writingRequestHeaders();
    boolean bool = this.httpEngine.permitsRequestBody(paramRequest);
    if (this.framedConnection.getProtocol() == Protocol.HTTP_2) {
      list = http2HeadersList(paramRequest);
    } else {
      list = spdy3HeadersList((Request)list);
    } 
    FramedStream framedStream = this.framedConnection.newStream(list, bool, true);
    this.stream = framedStream;
    framedStream.readTimeout().timeout(this.httpEngine.client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
    this.stream.writeTimeout().timeout(this.httpEngine.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
  }
  
  class StreamFinishingSource extends ForwardingSource {
    final Http2xStream this$0;
    
    public StreamFinishingSource(Source param1Source) {
      super(param1Source);
    }
    
    public void close() throws IOException {
      Http2xStream.this.streamAllocation.streamFinished(false, Http2xStream.this);
      super.close();
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\Http2xStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */