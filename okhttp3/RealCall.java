package okhttp3;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.http.HttpEngine;
import okhttp3.internal.http.RequestException;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.StreamAllocation;

final class RealCall implements Call {
  volatile boolean canceled;
  
  private final OkHttpClient client;
  
  HttpEngine engine;
  
  private boolean executed;
  
  Request originalRequest;
  
  protected RealCall(OkHttpClient paramOkHttpClient, Request paramRequest) {
    this.client = paramOkHttpClient;
    this.originalRequest = paramRequest;
  }
  
  private Response getResponseWithInterceptorChain(boolean paramBoolean) throws IOException {
    return (new ApplicationInterceptorChain(0, this.originalRequest, paramBoolean)).proceed(this.originalRequest);
  }
  
  private String toLoggableString() {
    String str;
    if (this.canceled) {
      str = "canceled call";
    } else {
      str = "call";
    } 
    HttpUrl httpUrl = this.originalRequest.url().resolve("/...");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(" to ");
    stringBuilder.append(httpUrl);
    return stringBuilder.toString();
  }
  
  public void cancel() {
    this.canceled = true;
    HttpEngine httpEngine = this.engine;
    if (httpEngine != null)
      httpEngine.cancel(); 
  }
  
  public void enqueue(Callback paramCallback) {
    enqueue(paramCallback, false);
  }
  
  void enqueue(Callback paramCallback, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifne -> 38
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield executed : Z
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_0
    //   17: getfield client : Lokhttp3/OkHttpClient;
    //   20: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   23: new okhttp3/RealCall$AsyncCall
    //   26: dup
    //   27: aload_0
    //   28: aload_1
    //   29: iload_2
    //   30: aconst_null
    //   31: invokespecial <init> : (Lokhttp3/RealCall;Lokhttp3/Callback;ZLokhttp3/RealCall$1;)V
    //   34: invokevirtual enqueue : (Lokhttp3/RealCall$AsyncCall;)V
    //   37: return
    //   38: new java/lang/IllegalStateException
    //   41: astore_1
    //   42: aload_1
    //   43: ldc 'Already Executed'
    //   45: invokespecial <init> : (Ljava/lang/String;)V
    //   48: aload_1
    //   49: athrow
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	50	finally
    //   38	50	50	finally
    //   51	53	50	finally
  }
  
  public Response execute() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: ifne -> 76
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield executed : Z
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_0
    //   17: getfield client : Lokhttp3/OkHttpClient;
    //   20: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   23: aload_0
    //   24: invokevirtual executed : (Lokhttp3/RealCall;)V
    //   27: aload_0
    //   28: iconst_0
    //   29: invokespecial getResponseWithInterceptorChain : (Z)Lokhttp3/Response;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull -> 50
    //   37: aload_0
    //   38: getfield client : Lokhttp3/OkHttpClient;
    //   41: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   44: aload_0
    //   45: invokevirtual finished : (Lokhttp3/Call;)V
    //   48: aload_1
    //   49: areturn
    //   50: new java/io/IOException
    //   53: astore_1
    //   54: aload_1
    //   55: ldc 'Canceled'
    //   57: invokespecial <init> : (Ljava/lang/String;)V
    //   60: aload_1
    //   61: athrow
    //   62: astore_1
    //   63: aload_0
    //   64: getfield client : Lokhttp3/OkHttpClient;
    //   67: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
    //   70: aload_0
    //   71: invokevirtual finished : (Lokhttp3/Call;)V
    //   74: aload_1
    //   75: athrow
    //   76: new java/lang/IllegalStateException
    //   79: astore_1
    //   80: aload_1
    //   81: ldc 'Already Executed'
    //   83: invokespecial <init> : (Ljava/lang/String;)V
    //   86: aload_1
    //   87: athrow
    //   88: astore_1
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_1
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	88	finally
    //   16	33	62	finally
    //   50	62	62	finally
    //   76	88	88	finally
    //   89	91	88	finally
  }
  
  Response getResponse(Request paramRequest, boolean paramBoolean) throws IOException {
    RequestBody requestBody = paramRequest.body();
    Request request = paramRequest;
    if (requestBody != null) {
      Request.Builder builder = paramRequest.newBuilder();
      MediaType mediaType = requestBody.contentType();
      if (mediaType != null)
        builder.header("Content-Type", mediaType.toString()); 
      long l = requestBody.contentLength();
      if (l != -1L) {
        builder.header("Content-Length", Long.toString(l));
        builder.removeHeader("Transfer-Encoding");
      } else {
        builder.header("Transfer-Encoding", "chunked");
        builder.removeHeader("Content-Length");
      } 
      request = builder.build();
    } 
    this.engine = new HttpEngine(this.client, request, false, false, paramBoolean, null, null, null);
    byte b = 0;
    while (!this.canceled) {
      boolean bool = true;
      try {
        this.engine.sendRequest();
        this.engine.readResponse();
        Response response = this.engine.getResponse();
        Request request1 = this.engine.followUpRequest();
        if (request1 == null)
          return response; 
        StreamAllocation streamAllocation = this.engine.close();
        if (++b <= 20) {
          if (!this.engine.sameConnection(request1.url())) {
            streamAllocation.release();
            streamAllocation = null;
          } 
          this.engine = new HttpEngine(this.client, request1, false, false, paramBoolean, streamAllocation, null, response);
          continue;
        } 
        streamAllocation.release();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Too many follow-up requests: ");
        stringBuilder.append(b);
        throw new ProtocolException(stringBuilder.toString());
      } catch (RequestException requestException) {
        throw requestException.getCause();
      } catch (RouteException routeException) {
        HttpEngine httpEngine = this.engine.recover(routeException.getLastConnectException(), null);
        if (httpEngine != null) {
          this.engine = httpEngine;
          continue;
        } 
        throw routeException.getLastConnectException();
      } catch (IOException iOException) {
        HttpEngine httpEngine = this.engine.recover(iOException, null);
      } finally {
        paramRequest = null;
      } 
      if (b != 0)
        this.engine.close().release(); 
      throw paramRequest;
    } 
    this.engine.releaseStreamAllocation();
    throw new IOException("Canceled");
  }
  
  public boolean isCanceled() {
    return this.canceled;
  }
  
  public boolean isExecuted() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executed : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Request request() {
    return this.originalRequest;
  }
  
  Object tag() {
    return this.originalRequest.tag();
  }
  
  class ApplicationInterceptorChain implements Interceptor.Chain {
    private final boolean forWebSocket;
    
    private final int index;
    
    private final Request request;
    
    final RealCall this$0;
    
    ApplicationInterceptorChain(int param1Int, Request param1Request, boolean param1Boolean) {
      this.index = param1Int;
      this.request = param1Request;
      this.forWebSocket = param1Boolean;
    }
    
    public Connection connection() {
      return null;
    }
    
    public Response proceed(Request param1Request) throws IOException {
      Interceptor interceptor;
      if (this.index < RealCall.this.client.interceptors().size()) {
        ApplicationInterceptorChain applicationInterceptorChain = new ApplicationInterceptorChain(this.index + 1, param1Request, this.forWebSocket);
        interceptor = RealCall.this.client.interceptors().get(this.index);
        Response response = interceptor.intercept(applicationInterceptorChain);
        if (response != null)
          return response; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("application interceptor ");
        stringBuilder.append(interceptor);
        stringBuilder.append(" returned null");
        throw new NullPointerException(stringBuilder.toString());
      } 
      return RealCall.this.getResponse((Request)interceptor, this.forWebSocket);
    }
    
    public Request request() {
      return this.request;
    }
  }
  
  final class AsyncCall extends NamedRunnable {
    private final boolean forWebSocket;
    
    private final Callback responseCallback;
    
    final RealCall this$0;
    
    private AsyncCall(Callback param1Callback, boolean param1Boolean) {
      super("OkHttp %s", new Object[] { this$0.originalRequest.url().toString() });
      this.responseCallback = param1Callback;
      this.forWebSocket = param1Boolean;
    }
    
    void cancel() {
      RealCall.this.cancel();
    }
    
    protected void execute() {
      // Byte code:
      //   0: iconst_1
      //   1: istore_1
      //   2: aload_0
      //   3: getfield this$0 : Lokhttp3/RealCall;
      //   6: aload_0
      //   7: getfield forWebSocket : Z
      //   10: invokestatic access$100 : (Lokhttp3/RealCall;Z)Lokhttp3/Response;
      //   13: astore_3
      //   14: aload_0
      //   15: getfield this$0 : Lokhttp3/RealCall;
      //   18: getfield canceled : Z
      //   21: istore_2
      //   22: iload_2
      //   23: ifeq -> 62
      //   26: aload_0
      //   27: getfield responseCallback : Lokhttp3/Callback;
      //   30: astore_3
      //   31: aload_0
      //   32: getfield this$0 : Lokhttp3/RealCall;
      //   35: astore #4
      //   37: new java/io/IOException
      //   40: astore #5
      //   42: aload #5
      //   44: ldc 'Canceled'
      //   46: invokespecial <init> : (Ljava/lang/String;)V
      //   49: aload_3
      //   50: aload #4
      //   52: aload #5
      //   54: invokeinterface onFailure : (Lokhttp3/Call;Ljava/io/IOException;)V
      //   59: goto -> 165
      //   62: aload_0
      //   63: getfield responseCallback : Lokhttp3/Callback;
      //   66: aload_0
      //   67: getfield this$0 : Lokhttp3/RealCall;
      //   70: aload_3
      //   71: invokeinterface onResponse : (Lokhttp3/Call;Lokhttp3/Response;)V
      //   76: goto -> 165
      //   79: astore_3
      //   80: goto -> 90
      //   83: astore_3
      //   84: goto -> 180
      //   87: astore_3
      //   88: iconst_0
      //   89: istore_1
      //   90: iload_1
      //   91: ifeq -> 151
      //   94: getstatic okhttp3/internal/Internal.logger : Ljava/util/logging/Logger;
      //   97: astore #4
      //   99: getstatic java/util/logging/Level.INFO : Ljava/util/logging/Level;
      //   102: astore #6
      //   104: new java/lang/StringBuilder
      //   107: astore #5
      //   109: aload #5
      //   111: invokespecial <init> : ()V
      //   114: aload #5
      //   116: ldc 'Callback failure for '
      //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   121: pop
      //   122: aload #5
      //   124: aload_0
      //   125: getfield this$0 : Lokhttp3/RealCall;
      //   128: invokestatic access$200 : (Lokhttp3/RealCall;)Ljava/lang/String;
      //   131: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   134: pop
      //   135: aload #4
      //   137: aload #6
      //   139: aload #5
      //   141: invokevirtual toString : ()Ljava/lang/String;
      //   144: aload_3
      //   145: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
      //   148: goto -> 165
      //   151: aload_0
      //   152: getfield responseCallback : Lokhttp3/Callback;
      //   155: aload_0
      //   156: getfield this$0 : Lokhttp3/RealCall;
      //   159: aload_3
      //   160: invokeinterface onFailure : (Lokhttp3/Call;Ljava/io/IOException;)V
      //   165: aload_0
      //   166: getfield this$0 : Lokhttp3/RealCall;
      //   169: invokestatic access$300 : (Lokhttp3/RealCall;)Lokhttp3/OkHttpClient;
      //   172: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
      //   175: aload_0
      //   176: invokevirtual finished : (Lokhttp3/RealCall$AsyncCall;)V
      //   179: return
      //   180: aload_0
      //   181: getfield this$0 : Lokhttp3/RealCall;
      //   184: invokestatic access$300 : (Lokhttp3/RealCall;)Lokhttp3/OkHttpClient;
      //   187: invokevirtual dispatcher : ()Lokhttp3/Dispatcher;
      //   190: aload_0
      //   191: invokevirtual finished : (Lokhttp3/RealCall$AsyncCall;)V
      //   194: aload_3
      //   195: athrow
      // Exception table:
      //   from	to	target	type
      //   2	22	87	java/io/IOException
      //   2	22	83	finally
      //   26	59	79	java/io/IOException
      //   26	59	83	finally
      //   62	76	79	java/io/IOException
      //   62	76	83	finally
      //   94	148	83	finally
      //   151	165	83	finally
    }
    
    RealCall get() {
      return RealCall.this;
    }
    
    String host() {
      return RealCall.this.originalRequest.url().host();
    }
    
    Request request() {
      return RealCall.this.originalRequest;
    }
    
    Object tag() {
      return RealCall.this.originalRequest.tag();
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\RealCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */