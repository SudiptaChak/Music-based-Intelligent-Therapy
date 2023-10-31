package okhttp3.internal.http;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.InternalCache;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.io.RealConnection;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class HttpEngine {
  private static final ResponseBody EMPTY_BODY = new ResponseBody() {
      public long contentLength() {
        return 0L;
      }
      
      public MediaType contentType() {
        return null;
      }
      
      public BufferedSource source() {
        return (BufferedSource)new Buffer();
      }
    };
  
  public static final int MAX_FOLLOW_UPS = 20;
  
  public final boolean bufferRequestBody;
  
  private BufferedSink bufferedRequestBody;
  
  private Response cacheResponse;
  
  private CacheStrategy cacheStrategy;
  
  private final boolean callerWritesRequestBody;
  
  final OkHttpClient client;
  
  private final boolean forWebSocket;
  
  private HttpStream httpStream;
  
  private Request networkRequest;
  
  private final Response priorResponse;
  
  private Sink requestBodyOut;
  
  long sentRequestMillis = -1L;
  
  private CacheRequest storeRequest;
  
  public final StreamAllocation streamAllocation;
  
  private boolean transparentGzip;
  
  private final Request userRequest;
  
  private Response userResponse;
  
  public HttpEngine(OkHttpClient paramOkHttpClient, Request paramRequest, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, StreamAllocation paramStreamAllocation, RetryableSink paramRetryableSink, Response paramResponse) {
    this.client = paramOkHttpClient;
    this.userRequest = paramRequest;
    this.bufferRequestBody = paramBoolean1;
    this.callerWritesRequestBody = paramBoolean2;
    this.forWebSocket = paramBoolean3;
    if (paramStreamAllocation == null)
      paramStreamAllocation = new StreamAllocation(paramOkHttpClient.connectionPool(), createAddress(paramOkHttpClient, paramRequest)); 
    this.streamAllocation = paramStreamAllocation;
    this.requestBodyOut = paramRetryableSink;
    this.priorResponse = paramResponse;
  }
  
  private Response cacheWritingResponse(final CacheRequest cacheRequest, Response paramResponse) throws IOException {
    if (cacheRequest == null)
      return paramResponse; 
    Sink sink = cacheRequest.body();
    if (sink == null)
      return paramResponse; 
    Source source = new Source() {
        boolean cacheRequestClosed;
        
        final HttpEngine this$0;
        
        final BufferedSink val$cacheBody;
        
        final CacheRequest val$cacheRequest;
        
        final BufferedSource val$source;
        
        public void close() throws IOException {
          if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
            this.cacheRequestClosed = true;
            cacheRequest.abort();
          } 
          source.close();
        }
        
        public long read(Buffer param1Buffer, long param1Long) throws IOException {
          try {
            param1Long = source.read(param1Buffer, param1Long);
            if (param1Long == -1L) {
              if (!this.cacheRequestClosed) {
                this.cacheRequestClosed = true;
                cacheBody.close();
              } 
              return -1L;
            } 
            param1Buffer.copyTo(cacheBody.buffer(), param1Buffer.size() - param1Long, param1Long);
            cacheBody.emitCompleteSegments();
            return param1Long;
          } catch (IOException iOException) {
            if (!this.cacheRequestClosed) {
              this.cacheRequestClosed = true;
              cacheRequest.abort();
            } 
            throw iOException;
          } 
        }
        
        public Timeout timeout() {
          return source.timeout();
        }
      };
    return paramResponse.newBuilder().body(new RealResponseBody(paramResponse.headers(), Okio.buffer(source))).build();
  }
  
  private static Headers combine(Headers paramHeaders1, Headers paramHeaders2) throws IOException {
    Headers.Builder builder = new Headers.Builder();
    int i = paramHeaders1.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      String str1 = paramHeaders1.name(b);
      String str2 = paramHeaders1.value(b);
      if ((!"Warning".equalsIgnoreCase(str1) || !str2.startsWith("1")) && (!OkHeaders.isEndToEnd(str1) || paramHeaders2.get(str1) == null))
        builder.add(str1, str2); 
    } 
    i = paramHeaders2.size();
    for (b = bool; b < i; b++) {
      String str = paramHeaders2.name(b);
      if (!"Content-Length".equalsIgnoreCase(str) && OkHeaders.isEndToEnd(str))
        builder.add(str, paramHeaders2.value(b)); 
    } 
    return builder.build();
  }
  
  private HttpStream connect() throws RouteException, RequestException, IOException {
    boolean bool = this.networkRequest.method().equals("GET");
    return this.streamAllocation.newStream(this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis(), this.client.retryOnConnectionFailure(), bool ^ true);
  }
  
  private String cookieHeader(List<Cookie> paramList) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      if (b > 0)
        stringBuilder.append("; "); 
      Cookie cookie = paramList.get(b);
      stringBuilder.append(cookie.name());
      stringBuilder.append('=');
      stringBuilder.append(cookie.value());
    } 
    return stringBuilder.toString();
  }
  
  private static Address createAddress(OkHttpClient paramOkHttpClient, Request paramRequest) {
    HostnameVerifier hostnameVerifier1;
    SSLSocketFactory sSLSocketFactory;
    HostnameVerifier hostnameVerifier2;
    if (paramRequest.isHttps()) {
      sSLSocketFactory = paramOkHttpClient.sslSocketFactory();
      hostnameVerifier1 = paramOkHttpClient.hostnameVerifier();
      hostnameVerifier2 = (HostnameVerifier)paramOkHttpClient.certificatePinner();
    } else {
      sSLSocketFactory = null;
      hostnameVerifier1 = null;
      hostnameVerifier2 = hostnameVerifier1;
    } 
    return new Address(paramRequest.url().host(), paramRequest.url().port(), paramOkHttpClient.dns(), paramOkHttpClient.socketFactory(), sSLSocketFactory, hostnameVerifier1, (CertificatePinner)hostnameVerifier2, paramOkHttpClient.proxyAuthenticator(), paramOkHttpClient.proxy(), paramOkHttpClient.protocols(), paramOkHttpClient.connectionSpecs(), paramOkHttpClient.proxySelector());
  }
  
  public static boolean hasBody(Response paramResponse) {
    if (paramResponse.request().method().equals("HEAD"))
      return false; 
    int i = paramResponse.code();
    return ((i < 100 || i >= 200) && i != 204 && i != 304) ? true : ((OkHeaders.contentLength(paramResponse) != -1L || "chunked".equalsIgnoreCase(paramResponse.header("Transfer-Encoding"))));
  }
  
  private void maybeCache() throws IOException {
    InternalCache internalCache = Internal.instance.internalCache(this.client);
    if (internalCache == null)
      return; 
    if (!CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
      if (HttpMethod.invalidatesCache(this.networkRequest.method()))
        try {
          internalCache.remove(this.networkRequest);
        } catch (IOException iOException) {} 
      return;
    } 
    this.storeRequest = iOException.put(stripBody(this.userResponse));
  }
  
  private Request networkRequest(Request paramRequest) throws IOException {
    Request.Builder builder = paramRequest.newBuilder();
    if (paramRequest.header("Host") == null)
      builder.header("Host", Util.hostHeader(paramRequest.url(), false)); 
    if (paramRequest.header("Connection") == null)
      builder.header("Connection", "Keep-Alive"); 
    if (paramRequest.header("Accept-Encoding") == null) {
      this.transparentGzip = true;
      builder.header("Accept-Encoding", "gzip");
    } 
    List<Cookie> list = this.client.cookieJar().loadForRequest(paramRequest.url());
    if (!list.isEmpty())
      builder.header("Cookie", cookieHeader(list)); 
    if (paramRequest.header("User-Agent") == null)
      builder.header("User-Agent", Version.userAgent()); 
    return builder.build();
  }
  
  private Response readNetworkResponse() throws IOException {
    this.httpStream.finishRequest();
    Response response2 = this.httpStream.readResponseHeaders().request(this.networkRequest).handshake(this.streamAllocation.connection().handshake()).header(OkHeaders.SENT_MILLIS, Long.toString(this.sentRequestMillis)).header(OkHeaders.RECEIVED_MILLIS, Long.toString(System.currentTimeMillis())).build();
    Response response1 = response2;
    if (!this.forWebSocket)
      response1 = response2.newBuilder().body(this.httpStream.openResponseBody(response2)).build(); 
    if ("close".equalsIgnoreCase(response1.request().header("Connection")) || "close".equalsIgnoreCase(response1.header("Connection")))
      this.streamAllocation.noNewStreams(); 
    return response1;
  }
  
  private static Response stripBody(Response paramResponse) {
    Response response = paramResponse;
    if (paramResponse != null) {
      response = paramResponse;
      if (paramResponse.body() != null)
        response = paramResponse.newBuilder().body(null).build(); 
    } 
    return response;
  }
  
  private Response unzip(Response paramResponse) throws IOException {
    Response response = paramResponse;
    if (this.transparentGzip)
      if (!"gzip".equalsIgnoreCase(this.userResponse.header("Content-Encoding"))) {
        response = paramResponse;
      } else {
        if (paramResponse.body() == null)
          return paramResponse; 
        GzipSource gzipSource = new GzipSource((Source)paramResponse.body().source());
        Headers headers = paramResponse.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
        response = paramResponse.newBuilder().headers(headers).body(new RealResponseBody(headers, Okio.buffer((Source)gzipSource))).build();
      }  
    return response;
  }
  
  private static boolean validate(Response paramResponse1, Response paramResponse2) {
    if (paramResponse2.code() == 304)
      return true; 
    Date date = paramResponse1.headers().getDate("Last-Modified");
    if (date != null) {
      Date date1 = paramResponse2.headers().getDate("Last-Modified");
      if (date1 != null && date1.getTime() < date.getTime())
        return true; 
    } 
    return false;
  }
  
  private boolean writeRequestHeadersEagerly() {
    boolean bool;
    if (this.callerWritesRequestBody && permitsRequestBody(this.networkRequest) && this.requestBodyOut == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void cancel() {
    this.streamAllocation.cancel();
  }
  
  public StreamAllocation close() {
    BufferedSink bufferedSink = this.bufferedRequestBody;
    if (bufferedSink != null) {
      Util.closeQuietly((Closeable)bufferedSink);
    } else {
      Sink sink = this.requestBodyOut;
      if (sink != null)
        Util.closeQuietly((Closeable)sink); 
    } 
    Response response = this.userResponse;
    if (response != null) {
      Util.closeQuietly((Closeable)response.body());
    } else {
      this.streamAllocation.connectionFailed(null);
    } 
    return this.streamAllocation;
  }
  
  public Request followUpRequest() throws IOException {
    if (this.userResponse != null) {
      Proxy proxy;
      RealConnection realConnection = this.streamAllocation.connection();
      if (realConnection != null) {
        Route route = realConnection.route();
      } else {
        realConnection = null;
      } 
      int i = this.userResponse.code();
      String str2 = this.userRequest.method();
      if (i != 307 && i != 308) {
        Sink sink;
        if (i != 401) {
          if (i != 407) {
            if (i != 408) {
              switch (i) {
                default:
                  return null;
                case 300:
                case 301:
                case 302:
                case 303:
                  break;
              } 
            } else {
              sink = this.requestBodyOut;
              if (sink == null || sink instanceof RetryableSink) {
                i = 1;
              } else {
                i = 0;
              } 
              return (this.callerWritesRequestBody && i == 0) ? null : this.userRequest;
            } 
          } else {
            if (sink != null) {
              proxy = sink.proxy();
            } else {
              proxy = this.client.proxy();
            } 
            if (proxy.type() != Proxy.Type.HTTP)
              throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy"); 
            return this.client.authenticator().authenticate((Route)sink, this.userResponse);
          } 
        } else {
          return this.client.authenticator().authenticate((Route)sink, this.userResponse);
        } 
      } else if (!proxy.equals("GET") && !proxy.equals("HEAD")) {
        return null;
      } 
      if (!this.client.followRedirects())
        return null; 
      String str1 = this.userResponse.header("Location");
      if (str1 == null)
        return null; 
      HttpUrl httpUrl = this.userRequest.url().resolve(str1);
      if (httpUrl == null)
        return null; 
      if (!httpUrl.scheme().equals(this.userRequest.url().scheme()) && !this.client.followSslRedirects())
        return null; 
      Request.Builder builder = this.userRequest.newBuilder();
      if (HttpMethod.permitsRequestBody((String)proxy)) {
        if (HttpMethod.redirectsToGet((String)proxy)) {
          builder.method("GET", null);
        } else {
          builder.method((String)proxy, null);
        } 
        builder.removeHeader("Transfer-Encoding");
        builder.removeHeader("Content-Length");
        builder.removeHeader("Content-Type");
      } 
      if (!sameConnection(httpUrl))
        builder.removeHeader("Authorization"); 
      return builder.url(httpUrl).build();
    } 
    throw new IllegalStateException();
  }
  
  public BufferedSink getBufferedRequestBody() {
    BufferedSink bufferedSink = this.bufferedRequestBody;
    if (bufferedSink != null)
      return bufferedSink; 
    Sink sink = getRequestBody();
    if (sink != null) {
      BufferedSink bufferedSink1 = Okio.buffer(sink);
      this.bufferedRequestBody = bufferedSink1;
    } else {
      sink = null;
    } 
    return (BufferedSink)sink;
  }
  
  public Connection getConnection() {
    return (Connection)this.streamAllocation.connection();
  }
  
  public Request getRequest() {
    return this.userRequest;
  }
  
  public Sink getRequestBody() {
    if (this.cacheStrategy != null)
      return this.requestBodyOut; 
    throw new IllegalStateException();
  }
  
  public Response getResponse() {
    Response response = this.userResponse;
    if (response != null)
      return response; 
    throw new IllegalStateException();
  }
  
  public boolean hasResponse() {
    boolean bool;
    if (this.userResponse != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean permitsRequestBody(Request paramRequest) {
    return HttpMethod.permitsRequestBody(paramRequest.method());
  }
  
  public void readResponse() throws IOException {
    if (this.userResponse != null)
      return; 
    if (this.networkRequest != null || this.cacheResponse != null) {
      Response response2;
      InternalCache internalCache;
      Request request = this.networkRequest;
      if (request == null)
        return; 
      if (this.forWebSocket) {
        this.httpStream.writeRequestHeaders(request);
        response2 = readNetworkResponse();
      } else if (!this.callerWritesRequestBody) {
        response2 = (new NetworkInterceptorChain(0, this.networkRequest)).proceed(this.networkRequest);
      } else {
        BufferedSink bufferedSink = this.bufferedRequestBody;
        if (bufferedSink != null && bufferedSink.buffer().size() > 0L)
          this.bufferedRequestBody.emit(); 
        if (this.sentRequestMillis == -1L) {
          if (OkHeaders.contentLength(this.networkRequest) == -1L) {
            Sink sink1 = this.requestBodyOut;
            if (sink1 instanceof RetryableSink) {
              long l = ((RetryableSink)sink1).contentLength();
              this.networkRequest = this.networkRequest.newBuilder().header("Content-Length", Long.toString(l)).build();
            } 
          } 
          this.httpStream.writeRequestHeaders(this.networkRequest);
        } 
        Sink sink = this.requestBodyOut;
        if (sink != null) {
          bufferedSink = this.bufferedRequestBody;
          if (bufferedSink != null) {
            bufferedSink.close();
          } else {
            sink.close();
          } 
          Sink sink1 = this.requestBodyOut;
          if (sink1 instanceof RetryableSink)
            this.httpStream.writeRequestBody((RetryableSink)sink1); 
        } 
        response2 = readNetworkResponse();
      } 
      receiveHeaders(response2.headers());
      Response response3 = this.cacheResponse;
      if (response3 != null) {
        if (validate(response3, response2)) {
          this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).headers(combine(this.cacheResponse.headers(), response2.headers())).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody(response2)).build();
          response2.body().close();
          releaseStreamAllocation();
          internalCache = Internal.instance.internalCache(this.client);
          internalCache.trackConditionalCacheHit();
          internalCache.update(this.cacheResponse, stripBody(this.userResponse));
          this.userResponse = unzip(this.userResponse);
          return;
        } 
        Util.closeQuietly((Closeable)this.cacheResponse.body());
      } 
      Response response1 = internalCache.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody((Response)internalCache)).build();
      this.userResponse = response1;
      if (hasBody(response1)) {
        maybeCache();
        this.userResponse = unzip(cacheWritingResponse(this.storeRequest, this.userResponse));
      } 
      return;
    } 
    throw new IllegalStateException("call sendRequest() first!");
  }
  
  public void receiveHeaders(Headers paramHeaders) throws IOException {
    if (this.client.cookieJar() == CookieJar.NO_COOKIES)
      return; 
    List list = Cookie.parseAll(this.userRequest.url(), paramHeaders);
    if (list.isEmpty())
      return; 
    this.client.cookieJar().saveFromResponse(this.userRequest.url(), list);
  }
  
  public HttpEngine recover(IOException paramIOException) {
    return recover(paramIOException, this.requestBodyOut);
  }
  
  public HttpEngine recover(IOException paramIOException, Sink paramSink) {
    if (!this.streamAllocation.recover(paramIOException, paramSink))
      return null; 
    if (!this.client.retryOnConnectionFailure())
      return null; 
    StreamAllocation streamAllocation = close();
    return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, streamAllocation, (RetryableSink)paramSink, this.priorResponse);
  }
  
  public void releaseStreamAllocation() throws IOException {
    this.streamAllocation.release();
  }
  
  public boolean sameConnection(HttpUrl paramHttpUrl) {
    boolean bool;
    HttpUrl httpUrl = this.userRequest.url();
    if (httpUrl.host().equals(paramHttpUrl.host()) && httpUrl.port() == paramHttpUrl.port() && httpUrl.scheme().equals(paramHttpUrl.scheme())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void sendRequest() throws RequestException, RouteException, IOException {
    if (this.cacheStrategy != null)
      return; 
    if (this.httpStream == null) {
      Response response;
      null = networkRequest(this.userRequest);
      InternalCache internalCache = Internal.instance.internalCache(this.client);
      if (internalCache != null) {
        response = internalCache.get(null);
      } else {
        response = null;
      } 
      CacheStrategy cacheStrategy = (new CacheStrategy.Factory(System.currentTimeMillis(), null, response)).get();
      this.cacheStrategy = cacheStrategy;
      this.networkRequest = cacheStrategy.networkRequest;
      this.cacheResponse = this.cacheStrategy.cacheResponse;
      if (internalCache != null)
        internalCache.trackResponse(this.cacheStrategy); 
      if (response != null && this.cacheResponse == null)
        Util.closeQuietly((Closeable)response.body()); 
      if (this.networkRequest == null && this.cacheResponse == null) {
        this.userResponse = (new Response.Builder()).request(this.userRequest).priorResponse(stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).build();
        return;
      } 
      if (this.networkRequest == null) {
        response = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).build();
        this.userResponse = response;
        this.userResponse = unzip(response);
        return;
      } 
      try {
        HttpStream httpStream = connect();
        this.httpStream = httpStream;
        httpStream.setHttpEngine(this);
        return;
      } finally {
        if (response != null)
          Util.closeQuietly((Closeable)response.body()); 
      } 
    } 
    throw new IllegalStateException();
  }
  
  public void writingRequestHeaders() {
    if (this.sentRequestMillis == -1L) {
      this.sentRequestMillis = System.currentTimeMillis();
      return;
    } 
    throw new IllegalStateException();
  }
  
  class NetworkInterceptorChain implements Interceptor.Chain {
    private int calls;
    
    private final int index;
    
    private final Request request;
    
    final HttpEngine this$0;
    
    NetworkInterceptorChain(int param1Int, Request param1Request) {
      this.index = param1Int;
      this.request = param1Request;
    }
    
    public Connection connection() {
      return (Connection)HttpEngine.this.streamAllocation.connection();
    }
    
    public Response proceed(Request param1Request) throws IOException {
      StringBuilder stringBuilder1;
      Interceptor interceptor;
      this.calls++;
      if (this.index > 0) {
        Interceptor interceptor1 = HttpEngine.this.client.networkInterceptors().get(this.index - 1);
        Address address = connection().route().address();
        if (param1Request.url().host().equals(address.url().host()) && param1Request.url().port() == address.url().port()) {
          if (this.calls > 1) {
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append("network interceptor ");
            stringBuilder1.append(interceptor1);
            stringBuilder1.append(" must call proceed() exactly once");
            throw new IllegalStateException(stringBuilder1.toString());
          } 
        } else {
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("network interceptor ");
          stringBuilder1.append(interceptor1);
          stringBuilder1.append(" must retain the same host and port");
          throw new IllegalStateException(stringBuilder1.toString());
        } 
      } 
      if (this.index < HttpEngine.this.client.networkInterceptors().size()) {
        NetworkInterceptorChain networkInterceptorChain = new NetworkInterceptorChain(this.index + 1, (Request)stringBuilder1);
        interceptor = HttpEngine.this.client.networkInterceptors().get(this.index);
        Response response1 = interceptor.intercept(networkInterceptorChain);
        if (networkInterceptorChain.calls == 1) {
          if (response1 != null)
            return response1; 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append("network interceptor ");
          stringBuilder3.append(interceptor);
          stringBuilder3.append(" returned null");
          throw new NullPointerException(stringBuilder3.toString());
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("network interceptor ");
        stringBuilder.append(interceptor);
        stringBuilder.append(" must call proceed() exactly once");
        throw new IllegalStateException(stringBuilder.toString());
      } 
      HttpEngine.this.httpStream.writeRequestHeaders((Request)interceptor);
      HttpEngine.access$102(HttpEngine.this, (Request)interceptor);
      if (HttpEngine.this.permitsRequestBody((Request)interceptor) && interceptor.body() != null) {
        BufferedSink bufferedSink = Okio.buffer(HttpEngine.this.httpStream.createRequestBody((Request)interceptor, interceptor.body().contentLength()));
        interceptor.body().writeTo(bufferedSink);
        bufferedSink.close();
      } 
      Response response = HttpEngine.this.readNetworkResponse();
      int i = response.code();
      if ((i != 204 && i != 205) || response.body().contentLength() <= 0L)
        return response; 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("HTTP ");
      stringBuilder2.append(i);
      stringBuilder2.append(" had non-zero Content-Length: ");
      stringBuilder2.append(response.body().contentLength());
      throw new ProtocolException(stringBuilder2.toString());
    }
    
    public Request request() {
      return this.request;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\HttpEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */