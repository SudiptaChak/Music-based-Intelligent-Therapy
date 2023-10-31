package okhttp3;

import java.lang.ref.Reference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import okhttp3.internal.Internal;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.Util;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;

public final class ConnectionPool {
  static final boolean $assertionsDisabled = false;
  
  private static final Executor executor = new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
  
  private final Runnable cleanupRunnable = new Runnable() {
      final ConnectionPool this$0;
      
      public void run() {
        while (true) {
          long l = ConnectionPool.this.cleanup(System.nanoTime());
          if (l == -1L)
            return; 
          if (l > 0L) {
            long l1 = l / 1000000L;
            synchronized (ConnectionPool.this) {
              ConnectionPool.this.wait(l1, (int)(l - 1000000L * l1));
            } 
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
          } 
        } 
      }
    };
  
  boolean cleanupRunning;
  
  private final Deque<RealConnection> connections = new ArrayDeque<RealConnection>();
  
  private final long keepAliveDurationNs;
  
  private final int maxIdleConnections;
  
  final RouteDatabase routeDatabase = new RouteDatabase();
  
  public ConnectionPool() {
    this(5, 5L, TimeUnit.MINUTES);
  }
  
  public ConnectionPool(int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    this.maxIdleConnections = paramInt;
    this.keepAliveDurationNs = paramTimeUnit.toNanos(paramLong);
    if (paramLong > 0L)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("keepAliveDuration <= 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private int pruneAndGetAllocationCount(RealConnection paramRealConnection, long paramLong) {
    List<Reference> list = paramRealConnection.allocations;
    byte b = 0;
    while (b < list.size()) {
      if (((Reference)list.get(b)).get() != null) {
        b++;
        continue;
      } 
      Logger logger = Internal.logger;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("A connection to ");
      stringBuilder.append(paramRealConnection.route().address().url());
      stringBuilder.append(" was leaked. Did you forget to close a response body?");
      logger.warning(stringBuilder.toString());
      list.remove(b);
      paramRealConnection.noNewStreams = true;
      if (list.isEmpty()) {
        paramRealConnection.idleAtNanos = paramLong - this.keepAliveDurationNs;
        return 0;
      } 
    } 
    return list.size();
  }
  
  long cleanup(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connections : Ljava/util/Deque;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore #12
    //   13: aconst_null
    //   14: astore #10
    //   16: ldc2_w -9223372036854775808
    //   19: lstore #6
    //   21: iconst_0
    //   22: istore_3
    //   23: iconst_0
    //   24: istore #4
    //   26: aload #12
    //   28: invokeinterface hasNext : ()Z
    //   33: ifeq -> 103
    //   36: aload #12
    //   38: invokeinterface next : ()Ljava/lang/Object;
    //   43: checkcast okhttp3/internal/io/RealConnection
    //   46: astore #11
    //   48: aload_0
    //   49: aload #11
    //   51: lload_1
    //   52: invokespecial pruneAndGetAllocationCount : (Lokhttp3/internal/io/RealConnection;J)I
    //   55: ifle -> 64
    //   58: iinc #4, 1
    //   61: goto -> 26
    //   64: iload_3
    //   65: iconst_1
    //   66: iadd
    //   67: istore #5
    //   69: lload_1
    //   70: aload #11
    //   72: getfield idleAtNanos : J
    //   75: lsub
    //   76: lstore #8
    //   78: iload #5
    //   80: istore_3
    //   81: lload #8
    //   83: lload #6
    //   85: lcmp
    //   86: ifle -> 26
    //   89: aload #11
    //   91: astore #10
    //   93: lload #8
    //   95: lstore #6
    //   97: iload #5
    //   99: istore_3
    //   100: goto -> 26
    //   103: lload #6
    //   105: aload_0
    //   106: getfield keepAliveDurationNs : J
    //   109: lcmp
    //   110: ifge -> 165
    //   113: iload_3
    //   114: aload_0
    //   115: getfield maxIdleConnections : I
    //   118: if_icmple -> 124
    //   121: goto -> 165
    //   124: iload_3
    //   125: ifle -> 140
    //   128: aload_0
    //   129: getfield keepAliveDurationNs : J
    //   132: lstore_1
    //   133: aload_0
    //   134: monitorexit
    //   135: lload_1
    //   136: lload #6
    //   138: lsub
    //   139: lreturn
    //   140: iload #4
    //   142: ifle -> 154
    //   145: aload_0
    //   146: getfield keepAliveDurationNs : J
    //   149: lstore_1
    //   150: aload_0
    //   151: monitorexit
    //   152: lload_1
    //   153: lreturn
    //   154: aload_0
    //   155: iconst_0
    //   156: putfield cleanupRunning : Z
    //   159: aload_0
    //   160: monitorexit
    //   161: ldc2_w -1
    //   164: lreturn
    //   165: aload_0
    //   166: getfield connections : Ljava/util/Deque;
    //   169: aload #10
    //   171: invokeinterface remove : (Ljava/lang/Object;)Z
    //   176: pop
    //   177: aload_0
    //   178: monitorexit
    //   179: aload #10
    //   181: invokevirtual socket : ()Ljava/net/Socket;
    //   184: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   187: lconst_0
    //   188: lreturn
    //   189: astore #10
    //   191: aload_0
    //   192: monitorexit
    //   193: aload #10
    //   195: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	189	finally
    //   26	58	189	finally
    //   69	78	189	finally
    //   103	121	189	finally
    //   128	135	189	finally
    //   145	152	189	finally
    //   154	161	189	finally
    //   165	179	189	finally
    //   191	193	189	finally
  }
  
  boolean connectionBecameIdle(RealConnection paramRealConnection) {
    if (paramRealConnection.noNewStreams || this.maxIdleConnections == 0) {
      this.connections.remove(paramRealConnection);
      return true;
    } 
    notifyAll();
    return false;
  }
  
  public int connectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield connections : Ljava/util/Deque;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public void evictAll() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield connections : Ljava/util/Deque;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_2
    //   20: aload_2
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 73
    //   29: aload_2
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast okhttp3/internal/io/RealConnection
    //   38: astore_3
    //   39: aload_3
    //   40: getfield allocations : Ljava/util/List;
    //   43: invokeinterface isEmpty : ()Z
    //   48: ifeq -> 20
    //   51: aload_3
    //   52: iconst_1
    //   53: putfield noNewStreams : Z
    //   56: aload_1
    //   57: aload_3
    //   58: invokeinterface add : (Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_2
    //   65: invokeinterface remove : ()V
    //   70: goto -> 20
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: invokeinterface iterator : ()Ljava/util/Iterator;
    //   81: astore_1
    //   82: aload_1
    //   83: invokeinterface hasNext : ()Z
    //   88: ifeq -> 109
    //   91: aload_1
    //   92: invokeinterface next : ()Ljava/lang/Object;
    //   97: checkcast okhttp3/internal/io/RealConnection
    //   100: invokevirtual socket : ()Ljava/net/Socket;
    //   103: invokestatic closeQuietly : (Ljava/net/Socket;)V
    //   106: goto -> 82
    //   109: return
    //   110: astore_1
    //   111: aload_0
    //   112: monitorexit
    //   113: aload_1
    //   114: athrow
    // Exception table:
    //   from	to	target	type
    //   10	20	110	finally
    //   20	70	110	finally
    //   73	75	110	finally
    //   111	113	110	finally
  }
  
  RealConnection get(Address paramAddress, StreamAllocation paramStreamAllocation) {
    for (RealConnection realConnection : this.connections) {
      if (realConnection.allocations.size() < realConnection.allocationLimit && paramAddress.equals((realConnection.route()).address) && !realConnection.noNewStreams) {
        paramStreamAllocation.acquire(realConnection);
        return realConnection;
      } 
    } 
    return null;
  }
  
  public int idleConnectionCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: aload_0
    //   5: getfield connections : Ljava/util/Deque;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_3
    //   14: aload_3
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 51
    //   23: aload_3
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast okhttp3/internal/io/RealConnection
    //   32: getfield allocations : Ljava/util/List;
    //   35: invokeinterface isEmpty : ()Z
    //   40: istore_2
    //   41: iload_2
    //   42: ifeq -> 14
    //   45: iinc #1, 1
    //   48: goto -> 14
    //   51: aload_0
    //   52: monitorexit
    //   53: iload_1
    //   54: ireturn
    //   55: astore_3
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_3
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   4	14	55	finally
    //   14	41	55	finally
  }
  
  void put(RealConnection paramRealConnection) {
    if (!this.cleanupRunning) {
      this.cleanupRunning = true;
      executor.execute(this.cleanupRunnable);
    } 
    this.connections.add(paramRealConnection);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\ConnectionPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */