package okhttp3.internal.http;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.io.RealConnection;
import okio.Sink;

public final class StreamAllocation {
  public final Address address;
  
  private boolean canceled;
  
  private RealConnection connection;
  
  private final ConnectionPool connectionPool;
  
  private boolean released;
  
  private Route route;
  
  private RouteSelector routeSelector;
  
  private HttpStream stream;
  
  public StreamAllocation(ConnectionPool paramConnectionPool, Address paramAddress) {
    this.connectionPool = paramConnectionPool;
    this.address = paramAddress;
    this.routeSelector = new RouteSelector(paramAddress, routeDatabase());
  }
  
  private void deallocate(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   4: astore #6
    //   6: aload #6
    //   8: monitorenter
    //   9: aconst_null
    //   10: astore #5
    //   12: iload_3
    //   13: ifeq -> 29
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield stream : Lokhttp3/internal/http/HttpStream;
    //   21: goto -> 29
    //   24: astore #4
    //   26: goto -> 177
    //   29: iload_2
    //   30: ifeq -> 38
    //   33: aload_0
    //   34: iconst_1
    //   35: putfield released : Z
    //   38: aload #5
    //   40: astore #4
    //   42: aload_0
    //   43: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   46: ifnull -> 160
    //   49: iload_1
    //   50: ifeq -> 61
    //   53: aload_0
    //   54: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   57: iconst_1
    //   58: putfield noNewStreams : Z
    //   61: aload #5
    //   63: astore #4
    //   65: aload_0
    //   66: getfield stream : Lokhttp3/internal/http/HttpStream;
    //   69: ifnonnull -> 160
    //   72: aload_0
    //   73: getfield released : Z
    //   76: ifne -> 93
    //   79: aload #5
    //   81: astore #4
    //   83: aload_0
    //   84: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   87: getfield noNewStreams : Z
    //   90: ifeq -> 160
    //   93: aload_0
    //   94: aload_0
    //   95: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   98: invokespecial release : (Lokhttp3/internal/io/RealConnection;)V
    //   101: aload_0
    //   102: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   105: getfield allocations : Ljava/util/List;
    //   108: invokeinterface isEmpty : ()Z
    //   113: ifeq -> 152
    //   116: aload_0
    //   117: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   120: invokestatic nanoTime : ()J
    //   123: putfield idleAtNanos : J
    //   126: getstatic okhttp3/internal/Internal.instance : Lokhttp3/internal/Internal;
    //   129: aload_0
    //   130: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   133: aload_0
    //   134: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   137: invokevirtual connectionBecameIdle : (Lokhttp3/ConnectionPool;Lokhttp3/internal/io/RealConnection;)Z
    //   140: ifeq -> 152
    //   143: aload_0
    //   144: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   147: astore #4
    //   149: goto -> 155
    //   152: aconst_null
    //   153: astore #4
    //   155: aload_0
    //   156: aconst_null
    //   157: putfield connection : Lokhttp3/internal/io/RealConnection;
    //   160: aload #6
    //   162: monitorexit
    //   163: aload #4
    //   165: ifnull -> 176
    //   168: aload #4
    //   170: invokevirtual socket : ()Ljava/net/Socket;
    //   173: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   176: return
    //   177: aload #6
    //   179: monitorexit
    //   180: aload #4
    //   182: athrow
    // Exception table:
    //   from	to	target	type
    //   16	21	24	finally
    //   33	38	24	finally
    //   42	49	24	finally
    //   53	61	24	finally
    //   65	79	24	finally
    //   83	93	24	finally
    //   93	149	24	finally
    //   155	160	24	finally
    //   160	163	24	finally
    //   177	180	24	finally
  }
  
  private RealConnection findConnection(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) throws IOException, RouteException {
    synchronized (this.connectionPool) {
      if (!this.released) {
        if (this.stream == null) {
          if (!this.canceled) {
            RealConnection realConnection = this.connection;
            if (realConnection != null && !realConnection.noNewStreams)
              return realConnection; 
            realConnection = Internal.instance.get(this.connectionPool, this.address, this);
            if (realConnection != null) {
              this.connection = realConnection;
              return realConnection;
            } 
            Route route2 = this.route;
            Route route1 = route2;
            if (route2 == null) {
              route1 = this.routeSelector.next();
              synchronized (this.connectionPool) {
                this.route = route1;
              } 
            } 
            null = new RealConnection(route1);
            acquire(null);
            synchronized (this.connectionPool) {
              Internal.instance.put(this.connectionPool, null);
              this.connection = null;
              if (!this.canceled) {
                null.connect(paramInt1, paramInt2, paramInt3, this.address.connectionSpecs(), paramBoolean);
                routeDatabase().connected(null.route());
                return null;
              } 
              IOException iOException1 = new IOException();
              this("Canceled");
              throw iOException1;
            } 
          } 
          IOException iOException = new IOException();
          this("Canceled");
          throw iOException;
        } 
        IllegalStateException illegalStateException1 = new IllegalStateException();
        this("stream != null");
        throw illegalStateException1;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this("released");
      throw illegalStateException;
    } 
  }
  
  private RealConnection findHealthyConnection(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2) throws IOException, RouteException {
    while (true) {
      null = findConnection(paramInt1, paramInt2, paramInt3, paramBoolean1);
      synchronized (this.connectionPool) {
        if (null.successCount == 0)
          return null; 
        if (null.isHealthy(paramBoolean2))
          return null; 
        connectionFailed(new IOException());
      } 
    } 
  }
  
  private boolean isRecoverable(IOException paramIOException) {
    return (paramIOException instanceof java.net.ProtocolException) ? false : ((paramIOException instanceof java.io.InterruptedIOException) ? (paramIOException instanceof java.net.SocketTimeoutException) : ((paramIOException instanceof javax.net.ssl.SSLHandshakeException && paramIOException.getCause() instanceof java.security.cert.CertificateException) ? false : (!(paramIOException instanceof javax.net.ssl.SSLPeerUnverifiedException))));
  }
  
  private void release(RealConnection paramRealConnection) {
    int i = paramRealConnection.allocations.size();
    for (byte b = 0; b < i; b++) {
      if (((Reference<StreamAllocation>)paramRealConnection.allocations.get(b)).get() == this) {
        paramRealConnection.allocations.remove(b);
        return;
      } 
    } 
    throw new IllegalStateException();
  }
  
  private RouteDatabase routeDatabase() {
    return Internal.instance.routeDatabase(this.connectionPool);
  }
  
  public void acquire(RealConnection paramRealConnection) {
    paramRealConnection.allocations.add(new WeakReference<StreamAllocation>(this));
  }
  
  public void cancel() {
    synchronized (this.connectionPool) {
      this.canceled = true;
      HttpStream httpStream = this.stream;
      RealConnection realConnection = this.connection;
      if (httpStream != null) {
        httpStream.cancel();
      } else if (realConnection != null) {
        realConnection.cancel();
      } 
      return;
    } 
  }
  
  public RealConnection connection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void connectionFailed(IOException paramIOException) {
    synchronized (this.connectionPool) {
      if (this.connection != null && this.connection.successCount == 0) {
        if (this.route != null && paramIOException != null)
          this.routeSelector.connectFailed(this.route, paramIOException); 
        this.route = null;
      } 
      deallocate(true, false, true);
      return;
    } 
  }
  
  public HttpStream newStream(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2) throws RouteException, IOException {
    try {
      RealConnection realConnection = findHealthyConnection(paramInt1, paramInt2, paramInt3, paramBoolean1, paramBoolean2);
      if (realConnection.framedConnection != null) {
        Http2xStream http2xStream = new Http2xStream();
        this(this, realConnection.framedConnection);
      } else {
        realConnection.socket().setSoTimeout(paramInt2);
        realConnection.source.timeout().timeout(paramInt2, TimeUnit.MILLISECONDS);
        realConnection.sink.timeout().timeout(paramInt3, TimeUnit.MILLISECONDS);
        null = new Http1xStream(this, realConnection.source, realConnection.sink);
      } 
      synchronized (this.connectionPool) {
        this.stream = null;
        return null;
      } 
    } catch (IOException iOException) {
      throw new RouteException(iOException);
    } 
  }
  
  public void noNewStreams() {
    deallocate(true, false, false);
  }
  
  public boolean recover(IOException paramIOException, Sink paramSink) {
    boolean bool;
    if (this.connection != null)
      connectionFailed(paramIOException); 
    if (paramSink == null || paramSink instanceof RetryableSink) {
      bool = true;
    } else {
      bool = false;
    } 
    RouteSelector routeSelector = this.routeSelector;
    return !((routeSelector != null && !routeSelector.hasNext()) || !isRecoverable(paramIOException) || !bool);
  }
  
  public void release() {
    deallocate(false, true, false);
  }
  
  public HttpStream stream() {
    synchronized (this.connectionPool) {
      return this.stream;
    } 
  }
  
  public void streamFinished(boolean paramBoolean, HttpStream paramHttpStream) {
    // Byte code:
    //   0: aload_0
    //   1: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_2
    //   8: ifnull -> 48
    //   11: aload_2
    //   12: aload_0
    //   13: getfield stream : Lokhttp3/internal/http/HttpStream;
    //   16: if_acmpne -> 48
    //   19: iload_1
    //   20: ifne -> 38
    //   23: aload_0
    //   24: getfield connection : Lokhttp3/internal/io/RealConnection;
    //   27: astore_2
    //   28: aload_2
    //   29: aload_2
    //   30: getfield successCount : I
    //   33: iconst_1
    //   34: iadd
    //   35: putfield successCount : I
    //   38: aload_3
    //   39: monitorexit
    //   40: aload_0
    //   41: iload_1
    //   42: iconst_0
    //   43: iconst_1
    //   44: invokespecial deallocate : (ZZZ)V
    //   47: return
    //   48: new java/lang/IllegalStateException
    //   51: astore #4
    //   53: new java/lang/StringBuilder
    //   56: astore #5
    //   58: aload #5
    //   60: invokespecial <init> : ()V
    //   63: aload #5
    //   65: ldc_w 'expected '
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload #5
    //   74: aload_0
    //   75: getfield stream : Lokhttp3/internal/http/HttpStream;
    //   78: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: aload #5
    //   84: ldc_w ' but was '
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload #5
    //   93: aload_2
    //   94: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload #4
    //   100: aload #5
    //   102: invokevirtual toString : ()Ljava/lang/String;
    //   105: invokespecial <init> : (Ljava/lang/String;)V
    //   108: aload #4
    //   110: athrow
    //   111: astore_2
    //   112: aload_3
    //   113: monitorexit
    //   114: aload_2
    //   115: athrow
    // Exception table:
    //   from	to	target	type
    //   11	19	111	finally
    //   23	38	111	finally
    //   38	40	111	finally
    //   48	111	111	finally
    //   112	114	111	finally
  }
  
  public String toString() {
    return this.address.toString();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\StreamAllocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */