package okhttp3.internal.framed;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class FramedStream {
  static final boolean $assertionsDisabled = false;
  
  long bytesLeftInWriteWindow;
  
  private final FramedConnection connection;
  
  private ErrorCode errorCode = null;
  
  private final int id;
  
  private final StreamTimeout readTimeout = new StreamTimeout();
  
  private final List<Header> requestHeaders;
  
  private List<Header> responseHeaders;
  
  final FramedDataSink sink;
  
  private final FramedDataSource source;
  
  long unacknowledgedBytesRead = 0L;
  
  private final StreamTimeout writeTimeout = new StreamTimeout();
  
  FramedStream(int paramInt, FramedConnection paramFramedConnection, boolean paramBoolean1, boolean paramBoolean2, List<Header> paramList) {
    if (paramFramedConnection != null) {
      if (paramList != null) {
        this.id = paramInt;
        this.connection = paramFramedConnection;
        this.bytesLeftInWriteWindow = paramFramedConnection.peerSettings.getInitialWindowSize(65536);
        this.source = new FramedDataSource(paramFramedConnection.okHttpSettings.getInitialWindowSize(65536));
        this.sink = new FramedDataSink();
        FramedDataSource.access$102(this.source, paramBoolean2);
        FramedDataSink.access$202(this.sink, paramBoolean1);
        this.requestHeaders = paramList;
        return;
      } 
      throw new NullPointerException("requestHeaders == null");
    } 
    throw new NullPointerException("connection == null");
  }
  
  private void cancelStreamIfNecessary() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   6: invokestatic access$100 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;)Z
    //   9: ifne -> 47
    //   12: aload_0
    //   13: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   16: invokestatic access$300 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;)Z
    //   19: ifeq -> 47
    //   22: aload_0
    //   23: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   26: invokestatic access$200 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;)Z
    //   29: ifne -> 42
    //   32: aload_0
    //   33: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   36: invokestatic access$400 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;)Z
    //   39: ifeq -> 47
    //   42: iconst_1
    //   43: istore_1
    //   44: goto -> 49
    //   47: iconst_0
    //   48: istore_1
    //   49: aload_0
    //   50: invokevirtual isOpen : ()Z
    //   53: istore_2
    //   54: aload_0
    //   55: monitorexit
    //   56: iload_1
    //   57: ifeq -> 70
    //   60: aload_0
    //   61: getstatic okhttp3/internal/framed/ErrorCode.CANCEL : Lokhttp3/internal/framed/ErrorCode;
    //   64: invokevirtual close : (Lokhttp3/internal/framed/ErrorCode;)V
    //   67: goto -> 86
    //   70: iload_2
    //   71: ifne -> 86
    //   74: aload_0
    //   75: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   78: aload_0
    //   79: getfield id : I
    //   82: invokevirtual removeStream : (I)Lokhttp3/internal/framed/FramedStream;
    //   85: pop
    //   86: return
    //   87: astore_3
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_3
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	87	finally
    //   49	56	87	finally
    //   88	90	87	finally
  }
  
  private void checkOutNotClosed() throws IOException {
    if (!this.sink.closed) {
      if (!this.sink.finished) {
        if (this.errorCode == null)
          return; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stream was reset: ");
        stringBuilder.append(this.errorCode);
        throw new IOException(stringBuilder.toString());
      } 
      throw new IOException("stream finished");
    } 
    throw new IOException("stream closed");
  }
  
  private boolean closeInternal(ErrorCode paramErrorCode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   6: ifnull -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   17: invokestatic access$100 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;)Z
    //   20: ifeq -> 37
    //   23: aload_0
    //   24: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   27: invokestatic access$200 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;)Z
    //   30: ifeq -> 37
    //   33: aload_0
    //   34: monitorexit
    //   35: iconst_0
    //   36: ireturn
    //   37: aload_0
    //   38: aload_1
    //   39: putfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   42: aload_0
    //   43: invokevirtual notifyAll : ()V
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_0
    //   49: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   52: aload_0
    //   53: getfield id : I
    //   56: invokevirtual removeStream : (I)Lokhttp3/internal/framed/FramedStream;
    //   59: pop
    //   60: iconst_1
    //   61: ireturn
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	62	finally
    //   13	35	62	finally
    //   37	48	62	finally
    //   63	65	62	finally
  }
  
  private void waitForIo() throws InterruptedIOException {
    try {
      wait();
      return;
    } catch (InterruptedException interruptedException) {
      throw new InterruptedIOException();
    } 
  }
  
  void addBytesToWriteWindow(long paramLong) {
    this.bytesLeftInWriteWindow += paramLong;
    if (paramLong > 0L)
      notifyAll(); 
  }
  
  public void close(ErrorCode paramErrorCode) throws IOException {
    if (!closeInternal(paramErrorCode))
      return; 
    this.connection.writeSynReset(this.id, paramErrorCode);
  }
  
  public void closeLater(ErrorCode paramErrorCode) {
    if (!closeInternal(paramErrorCode))
      return; 
    this.connection.writeSynResetLater(this.id, paramErrorCode);
  }
  
  public FramedConnection getConnection() {
    return this.connection;
  }
  
  public ErrorCode getErrorCode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
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
  
  public int getId() {
    return this.id;
  }
  
  public List<Header> getRequestHeaders() {
    return this.requestHeaders;
  }
  
  public List<Header> getResponseHeaders() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield readTimeout : Lokhttp3/internal/framed/FramedStream$StreamTimeout;
    //   6: invokevirtual enter : ()V
    //   9: aload_0
    //   10: getfield responseHeaders : Ljava/util/List;
    //   13: ifnonnull -> 30
    //   16: aload_0
    //   17: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   20: ifnonnull -> 30
    //   23: aload_0
    //   24: invokespecial waitForIo : ()V
    //   27: goto -> 9
    //   30: aload_0
    //   31: getfield readTimeout : Lokhttp3/internal/framed/FramedStream$StreamTimeout;
    //   34: invokevirtual exitAndThrowIfTimedOut : ()V
    //   37: aload_0
    //   38: getfield responseHeaders : Ljava/util/List;
    //   41: ifnull -> 53
    //   44: aload_0
    //   45: getfield responseHeaders : Ljava/util/List;
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: areturn
    //   53: new java/io/IOException
    //   56: astore_1
    //   57: new java/lang/StringBuilder
    //   60: astore_2
    //   61: aload_2
    //   62: invokespecial <init> : ()V
    //   65: aload_2
    //   66: ldc 'stream was reset: '
    //   68: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload_2
    //   73: aload_0
    //   74: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   77: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_1
    //   82: aload_2
    //   83: invokevirtual toString : ()Ljava/lang/String;
    //   86: invokespecial <init> : (Ljava/lang/String;)V
    //   89: aload_1
    //   90: athrow
    //   91: astore_1
    //   92: aload_0
    //   93: getfield readTimeout : Lokhttp3/internal/framed/FramedStream$StreamTimeout;
    //   96: invokevirtual exitAndThrowIfTimedOut : ()V
    //   99: aload_1
    //   100: athrow
    //   101: astore_1
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	101	finally
    //   9	27	91	finally
    //   30	49	101	finally
    //   53	91	101	finally
    //   92	101	101	finally
  }
  
  public Sink getSink() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield responseHeaders : Ljava/util/List;
    //   6: ifnonnull -> 31
    //   9: aload_0
    //   10: invokevirtual isLocallyInitiated : ()Z
    //   13: ifeq -> 19
    //   16: goto -> 31
    //   19: new java/lang/IllegalStateException
    //   22: astore_1
    //   23: aload_1
    //   24: ldc 'reply before requesting the sink'
    //   26: invokespecial <init> : (Ljava/lang/String;)V
    //   29: aload_1
    //   30: athrow
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_0
    //   34: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   37: areturn
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	38	finally
    //   19	31	38	finally
    //   31	33	38	finally
    //   39	41	38	finally
  }
  
  public Source getSource() {
    return this.source;
  }
  
  public boolean isLocallyInitiated() {
    boolean bool;
    int i = this.id;
    boolean bool1 = true;
    if ((i & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.connection.client == bool) {
      bool = bool1;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOpen() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: iconst_0
    //   14: ireturn
    //   15: aload_0
    //   16: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   19: invokestatic access$100 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;)Z
    //   22: ifne -> 35
    //   25: aload_0
    //   26: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   29: invokestatic access$300 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;)Z
    //   32: ifeq -> 68
    //   35: aload_0
    //   36: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   39: invokestatic access$200 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;)Z
    //   42: ifne -> 55
    //   45: aload_0
    //   46: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   49: invokestatic access$400 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;)Z
    //   52: ifeq -> 68
    //   55: aload_0
    //   56: getfield responseHeaders : Ljava/util/List;
    //   59: astore_1
    //   60: aload_1
    //   61: ifnull -> 68
    //   64: aload_0
    //   65: monitorexit
    //   66: iconst_0
    //   67: ireturn
    //   68: aload_0
    //   69: monitorexit
    //   70: iconst_1
    //   71: ireturn
    //   72: astore_1
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	72	finally
    //   15	35	72	finally
    //   35	55	72	finally
    //   55	60	72	finally
  }
  
  public Timeout readTimeout() {
    return (Timeout)this.readTimeout;
  }
  
  void receiveData(BufferedSource paramBufferedSource, int paramInt) throws IOException {
    this.source.receive(paramBufferedSource, paramInt);
  }
  
  void receiveFin() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield source : Lokhttp3/internal/framed/FramedStream$FramedDataSource;
    //   6: iconst_1
    //   7: invokestatic access$102 : (Lokhttp3/internal/framed/FramedStream$FramedDataSource;Z)Z
    //   10: pop
    //   11: aload_0
    //   12: invokevirtual isOpen : ()Z
    //   15: istore_1
    //   16: aload_0
    //   17: invokevirtual notifyAll : ()V
    //   20: aload_0
    //   21: monitorexit
    //   22: iload_1
    //   23: ifne -> 38
    //   26: aload_0
    //   27: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   30: aload_0
    //   31: getfield id : I
    //   34: invokevirtual removeStream : (I)Lokhttp3/internal/framed/FramedStream;
    //   37: pop
    //   38: return
    //   39: astore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	39	finally
    //   40	42	39	finally
  }
  
  void receiveHeaders(List<Header> paramList, HeadersMode paramHeadersMode) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #4
    //   3: iconst_1
    //   4: istore_3
    //   5: aload_0
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield responseHeaders : Ljava/util/List;
    //   11: ifnonnull -> 48
    //   14: aload_2
    //   15: invokevirtual failIfHeadersAbsent : ()Z
    //   18: ifeq -> 28
    //   21: getstatic okhttp3/internal/framed/ErrorCode.PROTOCOL_ERROR : Lokhttp3/internal/framed/ErrorCode;
    //   24: astore_1
    //   25: goto -> 97
    //   28: aload_0
    //   29: aload_1
    //   30: putfield responseHeaders : Ljava/util/List;
    //   33: aload_0
    //   34: invokevirtual isOpen : ()Z
    //   37: istore_3
    //   38: aload_0
    //   39: invokevirtual notifyAll : ()V
    //   42: aload #4
    //   44: astore_1
    //   45: goto -> 97
    //   48: aload_2
    //   49: invokevirtual failIfHeadersPresent : ()Z
    //   52: ifeq -> 62
    //   55: getstatic okhttp3/internal/framed/ErrorCode.STREAM_IN_USE : Lokhttp3/internal/framed/ErrorCode;
    //   58: astore_1
    //   59: goto -> 97
    //   62: new java/util/ArrayList
    //   65: astore_2
    //   66: aload_2
    //   67: invokespecial <init> : ()V
    //   70: aload_2
    //   71: aload_0
    //   72: getfield responseHeaders : Ljava/util/List;
    //   75: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   80: pop
    //   81: aload_2
    //   82: aload_1
    //   83: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   88: pop
    //   89: aload_0
    //   90: aload_2
    //   91: putfield responseHeaders : Ljava/util/List;
    //   94: aload #4
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: ifnull -> 111
    //   103: aload_0
    //   104: aload_1
    //   105: invokevirtual closeLater : (Lokhttp3/internal/framed/ErrorCode;)V
    //   108: goto -> 127
    //   111: iload_3
    //   112: ifne -> 127
    //   115: aload_0
    //   116: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   119: aload_0
    //   120: getfield id : I
    //   123: invokevirtual removeStream : (I)Lokhttp3/internal/framed/FramedStream;
    //   126: pop
    //   127: return
    //   128: astore_1
    //   129: aload_0
    //   130: monitorexit
    //   131: aload_1
    //   132: athrow
    // Exception table:
    //   from	to	target	type
    //   7	25	128	finally
    //   28	42	128	finally
    //   48	59	128	finally
    //   62	94	128	finally
    //   97	99	128	finally
    //   129	131	128	finally
  }
  
  void receiveRstStream(ErrorCode paramErrorCode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   6: ifnonnull -> 18
    //   9: aload_0
    //   10: aload_1
    //   11: putfield errorCode : Lokhttp3/internal/framed/ErrorCode;
    //   14: aload_0
    //   15: invokevirtual notifyAll : ()V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  public void reply(List<Header> paramList, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: monitorenter
    //   4: aload_1
    //   5: ifnull -> 79
    //   8: aload_0
    //   9: getfield responseHeaders : Ljava/util/List;
    //   12: ifnonnull -> 62
    //   15: aload_0
    //   16: aload_1
    //   17: putfield responseHeaders : Ljava/util/List;
    //   20: iload_2
    //   21: ifne -> 35
    //   24: aload_0
    //   25: getfield sink : Lokhttp3/internal/framed/FramedStream$FramedDataSink;
    //   28: iconst_1
    //   29: invokestatic access$202 : (Lokhttp3/internal/framed/FramedStream$FramedDataSink;Z)Z
    //   32: pop
    //   33: iconst_1
    //   34: istore_3
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_0
    //   38: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   41: aload_0
    //   42: getfield id : I
    //   45: iload_3
    //   46: aload_1
    //   47: invokevirtual writeSynReply : (IZLjava/util/List;)V
    //   50: iload_3
    //   51: ifeq -> 61
    //   54: aload_0
    //   55: getfield connection : Lokhttp3/internal/framed/FramedConnection;
    //   58: invokevirtual flush : ()V
    //   61: return
    //   62: new java/lang/IllegalStateException
    //   65: astore_1
    //   66: aload_1
    //   67: ldc_w 'reply already sent'
    //   70: invokespecial <init> : (Ljava/lang/String;)V
    //   73: aload_1
    //   74: athrow
    //   75: astore_1
    //   76: goto -> 92
    //   79: new java/lang/NullPointerException
    //   82: astore_1
    //   83: aload_1
    //   84: ldc_w 'responseHeaders == null'
    //   87: invokespecial <init> : (Ljava/lang/String;)V
    //   90: aload_1
    //   91: athrow
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_1
    //   95: athrow
    // Exception table:
    //   from	to	target	type
    //   8	20	75	finally
    //   24	33	75	finally
    //   35	37	75	finally
    //   62	75	75	finally
    //   79	92	75	finally
    //   92	94	75	finally
  }
  
  public Timeout writeTimeout() {
    return (Timeout)this.writeTimeout;
  }
  
  final class FramedDataSink implements Sink {
    static final boolean $assertionsDisabled = false;
    
    private static final long EMIT_BUFFER_SIZE = 16384L;
    
    private boolean closed;
    
    private boolean finished;
    
    private final Buffer sendBuffer = new Buffer();
    
    final FramedStream this$0;
    
    private void emitDataFrame(boolean param1Boolean) throws IOException {
      synchronized (FramedStream.this) {
        FramedStream.this.writeTimeout.enter();
        try {
          while (FramedStream.this.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && FramedStream.this.errorCode == null)
            FramedStream.this.waitForIo(); 
          FramedStream.this.writeTimeout.exitAndThrowIfTimedOut();
          FramedStream.this.checkOutNotClosed();
          long l = Math.min(FramedStream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
          FramedStream framedStream = FramedStream.this;
          framedStream.bytesLeftInWriteWindow -= l;
          FramedStream.this.writeTimeout.enter();
        } finally {
          FramedStream.this.writeTimeout.exitAndThrowIfTimedOut();
        } 
      } 
    }
    
    public void close() throws IOException {
      synchronized (FramedStream.this) {
        if (this.closed)
          return; 
        if (!FramedStream.this.sink.finished)
          if (this.sendBuffer.size() > 0L) {
            while (this.sendBuffer.size() > 0L)
              emitDataFrame(true); 
          } else {
            FramedStream.this.connection.writeData(FramedStream.this.id, true, null, 0L);
          }  
        synchronized (FramedStream.this) {
          this.closed = true;
          FramedStream.this.connection.flush();
          FramedStream.this.cancelStreamIfNecessary();
          return;
        } 
      } 
    }
    
    public void flush() throws IOException {
      synchronized (FramedStream.this) {
        FramedStream.this.checkOutNotClosed();
        while (this.sendBuffer.size() > 0L) {
          emitDataFrame(false);
          FramedStream.this.connection.flush();
        } 
        return;
      } 
    }
    
    public Timeout timeout() {
      return (Timeout)FramedStream.this.writeTimeout;
    }
    
    public void write(Buffer param1Buffer, long param1Long) throws IOException {
      this.sendBuffer.write(param1Buffer, param1Long);
      while (this.sendBuffer.size() >= 16384L)
        emitDataFrame(false); 
    }
  }
  
  private final class FramedDataSource implements Source {
    static final boolean $assertionsDisabled = false;
    
    private boolean closed;
    
    private boolean finished;
    
    private final long maxByteCount;
    
    private final Buffer readBuffer = new Buffer();
    
    private final Buffer receiveBuffer = new Buffer();
    
    final FramedStream this$0;
    
    private FramedDataSource(long param1Long) {
      this.maxByteCount = param1Long;
    }
    
    private void checkNotClosed() throws IOException {
      if (!this.closed) {
        if (FramedStream.this.errorCode == null)
          return; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stream was reset: ");
        stringBuilder.append(FramedStream.this.errorCode);
        throw new IOException(stringBuilder.toString());
      } 
      throw new IOException("stream closed");
    }
    
    private void waitUntilReadable() throws IOException {
      FramedStream.this.readTimeout.enter();
      try {
        while (this.readBuffer.size() == 0L && !this.finished && !this.closed && FramedStream.this.errorCode == null)
          FramedStream.this.waitForIo(); 
        return;
      } finally {
        FramedStream.this.readTimeout.exitAndThrowIfTimedOut();
      } 
    }
    
    public void close() throws IOException {
      synchronized (FramedStream.this) {
        this.closed = true;
        this.readBuffer.clear();
        FramedStream.this.notifyAll();
        FramedStream.this.cancelStreamIfNecessary();
        return;
      } 
    }
    
    public long read(Buffer param1Buffer, long param1Long) throws IOException {
      if (param1Long >= 0L)
        synchronized (FramedStream.this) {
          waitUntilReadable();
          checkNotClosed();
          if (this.readBuffer.size() == 0L)
            return -1L; 
          param1Long = this.readBuffer.read(param1Buffer, Math.min(param1Long, this.readBuffer.size()));
          FramedStream framedStream = FramedStream.this;
          framedStream.unacknowledgedBytesRead += param1Long;
          if (FramedStream.this.unacknowledgedBytesRead >= (FramedStream.this.connection.okHttpSettings.getInitialWindowSize(65536) / 2)) {
            FramedStream.this.connection.writeWindowUpdateLater(FramedStream.this.id, FramedStream.this.unacknowledgedBytesRead);
            FramedStream.this.unacknowledgedBytesRead = 0L;
          } 
          synchronized (FramedStream.this.connection) {
            FramedConnection framedConnection = FramedStream.this.connection;
            framedConnection.unacknowledgedBytesRead += param1Long;
            if (FramedStream.this.connection.unacknowledgedBytesRead >= (FramedStream.this.connection.okHttpSettings.getInitialWindowSize(65536) / 2)) {
              FramedStream.this.connection.writeWindowUpdateLater(0, FramedStream.this.connection.unacknowledgedBytesRead);
              FramedStream.this.connection.unacknowledgedBytesRead = 0L;
            } 
            return param1Long;
          } 
        }  
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(param1Long);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    void receive(BufferedSource param1BufferedSource, long param1Long) throws IOException {
      while (param1Long > 0L) {
        synchronized (FramedStream.this) {
          boolean bool1;
          boolean bool = this.finished;
          long l1 = this.readBuffer.size();
          long l2 = this.maxByteCount;
          boolean bool2 = true;
          if (l1 + param1Long > l2) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          if (bool1) {
            param1BufferedSource.skip(param1Long);
            FramedStream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
            return;
          } 
          if (bool) {
            param1BufferedSource.skip(param1Long);
            return;
          } 
          l1 = param1BufferedSource.read(this.receiveBuffer, param1Long);
          if (l1 != -1L) {
            param1Long -= l1;
            synchronized (FramedStream.this) {
              if (this.readBuffer.size() == 0L) {
                bool1 = bool2;
              } else {
                bool1 = false;
              } 
              this.readBuffer.writeAll((Source)this.receiveBuffer);
              if (bool1)
                FramedStream.this.notifyAll(); 
            } 
            continue;
          } 
          throw new EOFException();
        } 
      } 
    }
    
    public Timeout timeout() {
      return (Timeout)FramedStream.this.readTimeout;
    }
  }
  
  class StreamTimeout extends AsyncTimeout {
    final FramedStream this$0;
    
    public void exitAndThrowIfTimedOut() throws IOException {
      if (!exit())
        return; 
      throw newTimeoutException(null);
    }
    
    protected IOException newTimeoutException(IOException param1IOException) {
      SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
      if (param1IOException != null)
        socketTimeoutException.initCause(param1IOException); 
      return socketTimeoutException;
    }
    
    protected void timedOut() {
      FramedStream.this.closeLater(ErrorCode.CANCEL);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\FramedStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */