package okhttp3.internal.framed;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.List;
import java.util.zip.Deflater;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.DeflaterSink;
import okio.Okio;
import okio.Sink;

public final class Spdy3 implements Variant {
  static final byte[] DICTIONARY;
  
  static final int FLAG_FIN = 1;
  
  static final int FLAG_UNIDIRECTIONAL = 2;
  
  static final int TYPE_DATA = 0;
  
  static final int TYPE_GOAWAY = 7;
  
  static final int TYPE_HEADERS = 8;
  
  static final int TYPE_PING = 6;
  
  static final int TYPE_RST_STREAM = 3;
  
  static final int TYPE_SETTINGS = 4;
  
  static final int TYPE_SYN_REPLY = 2;
  
  static final int TYPE_SYN_STREAM = 1;
  
  static final int TYPE_WINDOW_UPDATE = 9;
  
  static final int VERSION = 3;
  
  static {
    try {
      DICTIONARY = "\000\000\000\007options\000\000\000\004head\000\000\000\004post\000\000\000\003put\000\000\000\006delete\000\000\000\005trace\000\000\000\006accept\000\000\000\016accept-charset\000\000\000\017accept-encoding\000\000\000\017accept-language\000\000\000\raccept-ranges\000\000\000\003age\000\000\000\005allow\000\000\000\rauthorization\000\000\000\rcache-control\000\000\000\nconnection\000\000\000\fcontent-base\000\000\000\020content-encoding\000\000\000\020content-language\000\000\000\016content-length\000\000\000\020content-location\000\000\000\013content-md5\000\000\000\rcontent-range\000\000\000\fcontent-type\000\000\000\004date\000\000\000\004etag\000\000\000\006expect\000\000\000\007expires\000\000\000\004from\000\000\000\004host\000\000\000\bif-match\000\000\000\021if-modified-since\000\000\000\rif-none-match\000\000\000\bif-range\000\000\000\023if-unmodified-since\000\000\000\rlast-modified\000\000\000\blocation\000\000\000\fmax-forwards\000\000\000\006pragma\000\000\000\022proxy-authenticate\000\000\000\023proxy-authorization\000\000\000\005range\000\000\000\007referer\000\000\000\013retry-after\000\000\000\006server\000\000\000\002te\000\000\000\007trailer\000\000\000\021transfer-encoding\000\000\000\007upgrade\000\000\000\nuser-agent\000\000\000\004vary\000\000\000\003via\000\000\000\007warning\000\000\000\020www-authenticate\000\000\000\006method\000\000\000\003get\000\000\000\006status\000\000\000\006200 OK\000\000\000\007version\000\000\000\bHTTP/1.1\000\000\000\003url\000\000\000\006public\000\000\000\nset-cookie\000\000\000\nkeep-alive\000\000\000\006origin100101201202205206300302303304305306307402405406407408409410411412413414415416417502504505203 Non-Authoritative Information204 No Content301 Moved Permanently400 Bad Request401 Unauthorized403 Forbidden404 Not Found500 Internal Server Error501 Not Implemented503 Service UnavailableJan Feb Mar Apr May Jun Jul Aug Sept Oct Nov Dec 00:00:00 Mon, Tue, Wed, Thu, Fri, Sat, Sun, GMTchunked,text/html,image/png,image/jpg,image/gif,application/xml,application/xhtml+xml,text/plain,text/javascript,publicprivatemax-age=gzip,deflate,sdchcharset=utf-8charset=iso-8859-1,utf-,*,enq=0.".getBytes(Util.UTF_8.name());
      return;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError();
    } 
  }
  
  public Protocol getProtocol() {
    return Protocol.SPDY_3;
  }
  
  public FrameReader newReader(BufferedSource paramBufferedSource, boolean paramBoolean) {
    return new Reader(paramBufferedSource, paramBoolean);
  }
  
  public FrameWriter newWriter(BufferedSink paramBufferedSink, boolean paramBoolean) {
    return new Writer(paramBufferedSink, paramBoolean);
  }
  
  static final class Reader implements FrameReader {
    private final boolean client;
    
    private final NameValueBlockReader headerBlockReader;
    
    private final BufferedSource source;
    
    Reader(BufferedSource param1BufferedSource, boolean param1Boolean) {
      this.source = param1BufferedSource;
      this.headerBlockReader = new NameValueBlockReader(this.source);
      this.client = param1Boolean;
    }
    
    private static IOException ioException(String param1String, Object... param1VarArgs) throws IOException {
      throw new IOException(String.format(param1String, param1VarArgs));
    }
    
    private void readGoAway(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      if (param1Int2 == 8) {
        param1Int1 = this.source.readInt();
        param1Int2 = this.source.readInt();
        ErrorCode errorCode = ErrorCode.fromSpdyGoAway(param1Int2);
        if (errorCode != null) {
          param1Handler.goAway(param1Int1 & Integer.MAX_VALUE, errorCode, ByteString.EMPTY);
          return;
        } 
        throw ioException("TYPE_GOAWAY unexpected error code: %d", new Object[] { Integer.valueOf(param1Int2) });
      } 
      throw ioException("TYPE_GOAWAY length: %d != 8", new Object[] { Integer.valueOf(param1Int2) });
    }
    
    private void readHeaders(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      param1Handler.headers(false, false, this.source.readInt() & Integer.MAX_VALUE, -1, this.headerBlockReader.readNameValueBlock(param1Int2 - 4), HeadersMode.SPDY_HEADERS);
    }
    
    private void readPing(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      boolean bool = true;
      if (param1Int2 == 4) {
        boolean bool1;
        param1Int1 = this.source.readInt();
        boolean bool2 = this.client;
        if ((param1Int1 & 0x1) == 1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        if (bool2 == bool1) {
          bool1 = bool;
        } else {
          bool1 = false;
        } 
        param1Handler.ping(bool1, param1Int1, 0);
        return;
      } 
      throw ioException("TYPE_PING length: %d != 4", new Object[] { Integer.valueOf(param1Int2) });
    }
    
    private void readRstStream(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      if (param1Int2 == 8) {
        param1Int1 = this.source.readInt();
        param1Int2 = this.source.readInt();
        ErrorCode errorCode = ErrorCode.fromSpdy3Rst(param1Int2);
        if (errorCode != null) {
          param1Handler.rstStream(param1Int1 & Integer.MAX_VALUE, errorCode);
          return;
        } 
        throw ioException("TYPE_RST_STREAM unexpected error code: %d", new Object[] { Integer.valueOf(param1Int2) });
      } 
      throw ioException("TYPE_RST_STREAM length: %d != 8", new Object[] { Integer.valueOf(param1Int2) });
    }
    
    private void readSettings(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      int i = this.source.readInt();
      boolean bool = false;
      if (param1Int2 == i * 8 + 4) {
        Settings settings = new Settings();
        for (param1Int2 = 0; param1Int2 < i; param1Int2++) {
          int j = this.source.readInt();
          settings.set(j & 0xFFFFFF, (0xFF000000 & j) >>> 24, this.source.readInt());
        } 
        if ((param1Int1 & 0x1) != 0)
          bool = true; 
        param1Handler.settings(bool, settings);
        return;
      } 
      throw ioException("TYPE_SETTINGS length: %d != 4 + 8 * %d", new Object[] { Integer.valueOf(param1Int2), Integer.valueOf(i) });
    }
    
    private void readSynReply(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      boolean bool;
      int i = this.source.readInt();
      List<Header> list = this.headerBlockReader.readNameValueBlock(param1Int2 - 4);
      if ((param1Int1 & 0x1) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      param1Handler.headers(false, bool, i & Integer.MAX_VALUE, -1, list, HeadersMode.SPDY_REPLY);
    }
    
    private void readSynStream(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      boolean bool1;
      boolean bool2;
      int i = this.source.readInt();
      int j = this.source.readInt();
      this.source.readShort();
      List<Header> list = this.headerBlockReader.readNameValueBlock(param1Int2 - 10);
      if ((param1Int1 & 0x1) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if ((param1Int1 & 0x2) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      param1Handler.headers(bool2, bool1, i & Integer.MAX_VALUE, j & Integer.MAX_VALUE, list, HeadersMode.SPDY_SYN_STREAM);
    }
    
    private void readWindowUpdate(FrameReader.Handler param1Handler, int param1Int1, int param1Int2) throws IOException {
      if (param1Int2 == 8) {
        param1Int1 = this.source.readInt();
        long l = (this.source.readInt() & Integer.MAX_VALUE);
        if (l != 0L) {
          param1Handler.windowUpdate(param1Int1 & Integer.MAX_VALUE, l);
          return;
        } 
        throw ioException("windowSizeIncrement was 0", new Object[] { Long.valueOf(l) });
      } 
      throw ioException("TYPE_WINDOW_UPDATE length: %d != 8", new Object[] { Integer.valueOf(param1Int2) });
    }
    
    public void close() throws IOException {
      this.headerBlockReader.close();
    }
    
    public boolean nextFrame(FrameReader.Handler param1Handler) throws IOException {
      boolean bool = false;
      try {
        StringBuilder stringBuilder;
        int i;
        int j = this.source.readInt();
        int k = this.source.readInt();
        if ((Integer.MIN_VALUE & j) != 0) {
          i = 1;
        } else {
          i = 0;
        } 
        int m = (0xFF000000 & k) >>> 24;
        k &= 0xFFFFFF;
        if (i) {
          i = (0x7FFF0000 & j) >>> 16;
          if (i == 3) {
            switch (j & 0xFFFF) {
              default:
                this.source.skip(k);
                return true;
              case 9:
                readWindowUpdate(param1Handler, m, k);
                return true;
              case 8:
                readHeaders(param1Handler, m, k);
                return true;
              case 7:
                readGoAway(param1Handler, m, k);
                return true;
              case 6:
                readPing(param1Handler, m, k);
                return true;
              case 4:
                readSettings(param1Handler, m, k);
                return true;
              case 3:
                readRstStream(param1Handler, m, k);
                return true;
              case 2:
                readSynReply(param1Handler, m, k);
                return true;
              case 1:
                break;
            } 
            readSynStream(param1Handler, m, k);
            return true;
          } 
          stringBuilder = new StringBuilder();
          stringBuilder.append("version != 3: ");
          stringBuilder.append(i);
          throw new ProtocolException(stringBuilder.toString());
        } 
        if ((m & 0x1) != 0)
          bool = true; 
        stringBuilder.data(bool, j & Integer.MAX_VALUE, this.source, k);
        return true;
      } catch (IOException iOException) {
        return false;
      } 
    }
    
    public void readConnectionPreface() {}
  }
  
  static final class Writer implements FrameWriter {
    private final boolean client;
    
    private boolean closed;
    
    private final Buffer headerBlockBuffer;
    
    private final BufferedSink headerBlockOut;
    
    private final BufferedSink sink;
    
    Writer(BufferedSink param1BufferedSink, boolean param1Boolean) {
      this.sink = param1BufferedSink;
      this.client = param1Boolean;
      Deflater deflater = new Deflater();
      deflater.setDictionary(Spdy3.DICTIONARY);
      this.headerBlockBuffer = new Buffer();
      this.headerBlockOut = Okio.buffer((Sink)new DeflaterSink((Sink)this.headerBlockBuffer, deflater));
    }
    
    private void writeNameValueBlockToBuffer(List<Header> param1List) throws IOException {
      this.headerBlockOut.writeInt(param1List.size());
      int i = param1List.size();
      for (byte b = 0; b < i; b++) {
        ByteString byteString = ((Header)param1List.get(b)).name;
        this.headerBlockOut.writeInt(byteString.size());
        this.headerBlockOut.write(byteString);
        byteString = ((Header)param1List.get(b)).value;
        this.headerBlockOut.writeInt(byteString.size());
        this.headerBlockOut.write(byteString);
      } 
      this.headerBlockOut.flush();
    }
    
    public void ackSettings(Settings param1Settings) {}
    
    public void close() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_1
      //   4: putfield closed : Z
      //   7: aload_0
      //   8: getfield sink : Lokio/BufferedSink;
      //   11: aload_0
      //   12: getfield headerBlockOut : Lokio/BufferedSink;
      //   15: invokestatic closeAll : (Ljava/io/Closeable;Ljava/io/Closeable;)V
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
    
    public void connectionPreface() {
      /* monitor enter ThisExpression{InnerObjectType{ObjectType{okhttp3/internal/framed/Spdy3}.Lokhttp3/internal/framed/Spdy3$Writer;}} */
      /* monitor exit ThisExpression{InnerObjectType{ObjectType{okhttp3/internal/framed/Spdy3}.Lokhttp3/internal/framed/Spdy3$Writer;}} */
    }
    
    public void data(boolean param1Boolean, int param1Int1, Buffer param1Buffer, int param1Int2) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: iload_1
      //   3: ifeq -> 12
      //   6: iconst_1
      //   7: istore #5
      //   9: goto -> 15
      //   12: iconst_0
      //   13: istore #5
      //   15: aload_0
      //   16: iload_2
      //   17: iload #5
      //   19: aload_3
      //   20: iload #4
      //   22: invokevirtual sendDataFrame : (IILokio/Buffer;I)V
      //   25: aload_0
      //   26: monitorexit
      //   27: return
      //   28: astore_3
      //   29: aload_0
      //   30: monitorexit
      //   31: aload_3
      //   32: athrow
      // Exception table:
      //   from	to	target	type
      //   15	25	28	finally
    }
    
    public void flush() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 21
      //   9: aload_0
      //   10: getfield sink : Lokio/BufferedSink;
      //   13: invokeinterface flush : ()V
      //   18: aload_0
      //   19: monitorexit
      //   20: return
      //   21: new java/io/IOException
      //   24: astore_1
      //   25: aload_1
      //   26: ldc 'closed'
      //   28: invokespecial <init> : (Ljava/lang/String;)V
      //   31: aload_1
      //   32: athrow
      //   33: astore_1
      //   34: aload_0
      //   35: monitorexit
      //   36: aload_1
      //   37: athrow
      // Exception table:
      //   from	to	target	type
      //   2	18	33	finally
      //   21	33	33	finally
    }
    
    public void goAway(int param1Int, ErrorCode param1ErrorCode, byte[] param1ArrayOfbyte) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 90
      //   9: aload_2
      //   10: getfield spdyGoAwayCode : I
      //   13: iconst_m1
      //   14: if_icmpeq -> 78
      //   17: aload_0
      //   18: getfield sink : Lokio/BufferedSink;
      //   21: ldc -2147287033
      //   23: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   28: pop
      //   29: aload_0
      //   30: getfield sink : Lokio/BufferedSink;
      //   33: bipush #8
      //   35: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   40: pop
      //   41: aload_0
      //   42: getfield sink : Lokio/BufferedSink;
      //   45: iload_1
      //   46: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   51: pop
      //   52: aload_0
      //   53: getfield sink : Lokio/BufferedSink;
      //   56: aload_2
      //   57: getfield spdyGoAwayCode : I
      //   60: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   65: pop
      //   66: aload_0
      //   67: getfield sink : Lokio/BufferedSink;
      //   70: invokeinterface flush : ()V
      //   75: aload_0
      //   76: monitorexit
      //   77: return
      //   78: new java/lang/IllegalArgumentException
      //   81: astore_2
      //   82: aload_2
      //   83: ldc 'errorCode.spdyGoAwayCode == -1'
      //   85: invokespecial <init> : (Ljava/lang/String;)V
      //   88: aload_2
      //   89: athrow
      //   90: new java/io/IOException
      //   93: astore_2
      //   94: aload_2
      //   95: ldc 'closed'
      //   97: invokespecial <init> : (Ljava/lang/String;)V
      //   100: aload_2
      //   101: athrow
      //   102: astore_2
      //   103: aload_0
      //   104: monitorexit
      //   105: aload_2
      //   106: athrow
      // Exception table:
      //   from	to	target	type
      //   2	75	102	finally
      //   78	90	102	finally
      //   90	102	102	finally
    }
    
    public void headers(int param1Int, List<Header> param1List) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 86
      //   9: aload_0
      //   10: aload_2
      //   11: invokespecial writeNameValueBlockToBuffer : (Ljava/util/List;)V
      //   14: aload_0
      //   15: getfield headerBlockBuffer : Lokio/Buffer;
      //   18: invokevirtual size : ()J
      //   21: ldc2_w 4
      //   24: ladd
      //   25: l2i
      //   26: istore_3
      //   27: aload_0
      //   28: getfield sink : Lokio/BufferedSink;
      //   31: ldc -2147287032
      //   33: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   38: pop
      //   39: aload_0
      //   40: getfield sink : Lokio/BufferedSink;
      //   43: iload_3
      //   44: ldc 16777215
      //   46: iand
      //   47: iconst_0
      //   48: ior
      //   49: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   54: pop
      //   55: aload_0
      //   56: getfield sink : Lokio/BufferedSink;
      //   59: iload_1
      //   60: ldc 2147483647
      //   62: iand
      //   63: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   68: pop
      //   69: aload_0
      //   70: getfield sink : Lokio/BufferedSink;
      //   73: aload_0
      //   74: getfield headerBlockBuffer : Lokio/Buffer;
      //   77: invokeinterface writeAll : (Lokio/Source;)J
      //   82: pop2
      //   83: aload_0
      //   84: monitorexit
      //   85: return
      //   86: new java/io/IOException
      //   89: astore_2
      //   90: aload_2
      //   91: ldc 'closed'
      //   93: invokespecial <init> : (Ljava/lang/String;)V
      //   96: aload_2
      //   97: athrow
      //   98: astore_2
      //   99: aload_0
      //   100: monitorexit
      //   101: aload_2
      //   102: athrow
      // Exception table:
      //   from	to	target	type
      //   2	83	98	finally
      //   86	98	98	finally
    }
    
    public int maxDataLength() {
      return 16383;
    }
    
    public void ping(boolean param1Boolean, int param1Int1, int param1Int2) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 111
      //   9: aload_0
      //   10: getfield client : Z
      //   13: istore #6
      //   15: iconst_0
      //   16: istore #5
      //   18: iload_2
      //   19: iconst_1
      //   20: iand
      //   21: iconst_1
      //   22: if_icmpne -> 31
      //   25: iconst_1
      //   26: istore #4
      //   28: goto -> 34
      //   31: iconst_0
      //   32: istore #4
      //   34: iload #6
      //   36: iload #4
      //   38: if_icmpeq -> 44
      //   41: iconst_1
      //   42: istore #5
      //   44: iload_1
      //   45: iload #5
      //   47: if_icmpne -> 96
      //   50: aload_0
      //   51: getfield sink : Lokio/BufferedSink;
      //   54: ldc -2147287034
      //   56: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   61: pop
      //   62: aload_0
      //   63: getfield sink : Lokio/BufferedSink;
      //   66: iconst_4
      //   67: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   72: pop
      //   73: aload_0
      //   74: getfield sink : Lokio/BufferedSink;
      //   77: iload_2
      //   78: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   83: pop
      //   84: aload_0
      //   85: getfield sink : Lokio/BufferedSink;
      //   88: invokeinterface flush : ()V
      //   93: aload_0
      //   94: monitorexit
      //   95: return
      //   96: new java/lang/IllegalArgumentException
      //   99: astore #7
      //   101: aload #7
      //   103: ldc 'payload != reply'
      //   105: invokespecial <init> : (Ljava/lang/String;)V
      //   108: aload #7
      //   110: athrow
      //   111: new java/io/IOException
      //   114: astore #7
      //   116: aload #7
      //   118: ldc 'closed'
      //   120: invokespecial <init> : (Ljava/lang/String;)V
      //   123: aload #7
      //   125: athrow
      //   126: astore #7
      //   128: aload_0
      //   129: monitorexit
      //   130: aload #7
      //   132: athrow
      // Exception table:
      //   from	to	target	type
      //   2	15	126	finally
      //   50	93	126	finally
      //   96	111	126	finally
      //   111	126	126	finally
    }
    
    public void pushPromise(int param1Int1, int param1Int2, List<Header> param1List) throws IOException {}
    
    public void rstStream(int param1Int, ErrorCode param1ErrorCode) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 91
      //   9: aload_2
      //   10: getfield spdyRstCode : I
      //   13: iconst_m1
      //   14: if_icmpeq -> 81
      //   17: aload_0
      //   18: getfield sink : Lokio/BufferedSink;
      //   21: ldc -2147287037
      //   23: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   28: pop
      //   29: aload_0
      //   30: getfield sink : Lokio/BufferedSink;
      //   33: bipush #8
      //   35: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   40: pop
      //   41: aload_0
      //   42: getfield sink : Lokio/BufferedSink;
      //   45: iload_1
      //   46: ldc 2147483647
      //   48: iand
      //   49: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   54: pop
      //   55: aload_0
      //   56: getfield sink : Lokio/BufferedSink;
      //   59: aload_2
      //   60: getfield spdyRstCode : I
      //   63: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   68: pop
      //   69: aload_0
      //   70: getfield sink : Lokio/BufferedSink;
      //   73: invokeinterface flush : ()V
      //   78: aload_0
      //   79: monitorexit
      //   80: return
      //   81: new java/lang/IllegalArgumentException
      //   84: astore_2
      //   85: aload_2
      //   86: invokespecial <init> : ()V
      //   89: aload_2
      //   90: athrow
      //   91: new java/io/IOException
      //   94: astore_2
      //   95: aload_2
      //   96: ldc 'closed'
      //   98: invokespecial <init> : (Ljava/lang/String;)V
      //   101: aload_2
      //   102: athrow
      //   103: astore_2
      //   104: aload_0
      //   105: monitorexit
      //   106: aload_2
      //   107: athrow
      // Exception table:
      //   from	to	target	type
      //   2	78	103	finally
      //   81	91	103	finally
      //   91	103	103	finally
    }
    
    void sendDataFrame(int param1Int1, int param1Int2, Buffer param1Buffer, int param1Int3) throws IOException {
      if (!this.closed) {
        long l = param1Int3;
        if (l <= 16777215L) {
          this.sink.writeInt(param1Int1 & Integer.MAX_VALUE);
          this.sink.writeInt((param1Int2 & 0xFF) << 24 | 0xFFFFFF & param1Int3);
          if (param1Int3 > 0)
            this.sink.write(param1Buffer, l); 
          return;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FRAME_TOO_LARGE max size is 16Mib: ");
        stringBuilder.append(param1Int3);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IOException("closed");
    }
    
    public void settings(Settings param1Settings) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 143
      //   9: aload_1
      //   10: invokevirtual size : ()I
      //   13: istore_3
      //   14: aload_0
      //   15: getfield sink : Lokio/BufferedSink;
      //   18: ldc -2147287036
      //   20: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   25: pop
      //   26: aload_0
      //   27: getfield sink : Lokio/BufferedSink;
      //   30: astore #4
      //   32: iconst_0
      //   33: istore_2
      //   34: aload #4
      //   36: iload_3
      //   37: bipush #8
      //   39: imul
      //   40: iconst_4
      //   41: iadd
      //   42: ldc 16777215
      //   44: iand
      //   45: iconst_0
      //   46: ior
      //   47: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   52: pop
      //   53: aload_0
      //   54: getfield sink : Lokio/BufferedSink;
      //   57: iload_3
      //   58: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   63: pop
      //   64: iload_2
      //   65: bipush #10
      //   67: if_icmpgt -> 131
      //   70: aload_1
      //   71: iload_2
      //   72: invokevirtual isSet : (I)Z
      //   75: ifne -> 81
      //   78: goto -> 125
      //   81: aload_1
      //   82: iload_2
      //   83: invokevirtual flags : (I)I
      //   86: istore_3
      //   87: aload_0
      //   88: getfield sink : Lokio/BufferedSink;
      //   91: iload_3
      //   92: sipush #255
      //   95: iand
      //   96: bipush #24
      //   98: ishl
      //   99: iload_2
      //   100: ldc 16777215
      //   102: iand
      //   103: ior
      //   104: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   109: pop
      //   110: aload_0
      //   111: getfield sink : Lokio/BufferedSink;
      //   114: aload_1
      //   115: iload_2
      //   116: invokevirtual get : (I)I
      //   119: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   124: pop
      //   125: iinc #2, 1
      //   128: goto -> 64
      //   131: aload_0
      //   132: getfield sink : Lokio/BufferedSink;
      //   135: invokeinterface flush : ()V
      //   140: aload_0
      //   141: monitorexit
      //   142: return
      //   143: new java/io/IOException
      //   146: astore_1
      //   147: aload_1
      //   148: ldc 'closed'
      //   150: invokespecial <init> : (Ljava/lang/String;)V
      //   153: aload_1
      //   154: athrow
      //   155: astore_1
      //   156: aload_0
      //   157: monitorexit
      //   158: aload_1
      //   159: athrow
      // Exception table:
      //   from	to	target	type
      //   2	32	155	finally
      //   34	64	155	finally
      //   70	78	155	finally
      //   81	125	155	finally
      //   131	140	155	finally
      //   143	155	155	finally
    }
    
    public void synReply(boolean param1Boolean, int param1Int, List<Header> param1List) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 118
      //   9: aload_0
      //   10: aload_3
      //   11: invokespecial writeNameValueBlockToBuffer : (Ljava/util/List;)V
      //   14: iload_1
      //   15: ifeq -> 24
      //   18: iconst_1
      //   19: istore #4
      //   21: goto -> 27
      //   24: iconst_0
      //   25: istore #4
      //   27: aload_0
      //   28: getfield headerBlockBuffer : Lokio/Buffer;
      //   31: invokevirtual size : ()J
      //   34: ldc2_w 4
      //   37: ladd
      //   38: l2i
      //   39: istore #5
      //   41: aload_0
      //   42: getfield sink : Lokio/BufferedSink;
      //   45: ldc -2147287038
      //   47: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   52: pop
      //   53: aload_0
      //   54: getfield sink : Lokio/BufferedSink;
      //   57: iload #4
      //   59: sipush #255
      //   62: iand
      //   63: bipush #24
      //   65: ishl
      //   66: iload #5
      //   68: ldc 16777215
      //   70: iand
      //   71: ior
      //   72: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   77: pop
      //   78: aload_0
      //   79: getfield sink : Lokio/BufferedSink;
      //   82: iload_2
      //   83: ldc 2147483647
      //   85: iand
      //   86: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   91: pop
      //   92: aload_0
      //   93: getfield sink : Lokio/BufferedSink;
      //   96: aload_0
      //   97: getfield headerBlockBuffer : Lokio/Buffer;
      //   100: invokeinterface writeAll : (Lokio/Source;)J
      //   105: pop2
      //   106: aload_0
      //   107: getfield sink : Lokio/BufferedSink;
      //   110: invokeinterface flush : ()V
      //   115: aload_0
      //   116: monitorexit
      //   117: return
      //   118: new java/io/IOException
      //   121: astore_3
      //   122: aload_3
      //   123: ldc 'closed'
      //   125: invokespecial <init> : (Ljava/lang/String;)V
      //   128: aload_3
      //   129: athrow
      //   130: astore_3
      //   131: aload_0
      //   132: monitorexit
      //   133: aload_3
      //   134: athrow
      // Exception table:
      //   from	to	target	type
      //   2	14	130	finally
      //   27	115	130	finally
      //   118	130	130	finally
    }
    
    public void synStream(boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2, List<Header> param1List) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 147
      //   9: aload_0
      //   10: aload #5
      //   12: invokespecial writeNameValueBlockToBuffer : (Ljava/util/List;)V
      //   15: aload_0
      //   16: getfield headerBlockBuffer : Lokio/Buffer;
      //   19: invokevirtual size : ()J
      //   22: ldc2_w 10
      //   25: ladd
      //   26: l2i
      //   27: istore #7
      //   29: iload_2
      //   30: ifeq -> 39
      //   33: iconst_2
      //   34: istore #6
      //   36: goto -> 42
      //   39: iconst_0
      //   40: istore #6
      //   42: aload_0
      //   43: getfield sink : Lokio/BufferedSink;
      //   46: ldc -2147287039
      //   48: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   53: pop
      //   54: aload_0
      //   55: getfield sink : Lokio/BufferedSink;
      //   58: iload_1
      //   59: iload #6
      //   61: ior
      //   62: sipush #255
      //   65: iand
      //   66: bipush #24
      //   68: ishl
      //   69: iload #7
      //   71: ldc 16777215
      //   73: iand
      //   74: ior
      //   75: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   80: pop
      //   81: aload_0
      //   82: getfield sink : Lokio/BufferedSink;
      //   85: iload_3
      //   86: ldc 2147483647
      //   88: iand
      //   89: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   94: pop
      //   95: aload_0
      //   96: getfield sink : Lokio/BufferedSink;
      //   99: ldc 2147483647
      //   101: iload #4
      //   103: iand
      //   104: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   109: pop
      //   110: aload_0
      //   111: getfield sink : Lokio/BufferedSink;
      //   114: iconst_0
      //   115: invokeinterface writeShort : (I)Lokio/BufferedSink;
      //   120: pop
      //   121: aload_0
      //   122: getfield sink : Lokio/BufferedSink;
      //   125: aload_0
      //   126: getfield headerBlockBuffer : Lokio/Buffer;
      //   129: invokeinterface writeAll : (Lokio/Source;)J
      //   134: pop2
      //   135: aload_0
      //   136: getfield sink : Lokio/BufferedSink;
      //   139: invokeinterface flush : ()V
      //   144: aload_0
      //   145: monitorexit
      //   146: return
      //   147: new java/io/IOException
      //   150: astore #5
      //   152: aload #5
      //   154: ldc 'closed'
      //   156: invokespecial <init> : (Ljava/lang/String;)V
      //   159: aload #5
      //   161: athrow
      //   162: astore #5
      //   164: aload_0
      //   165: monitorexit
      //   166: aload #5
      //   168: athrow
      // Exception table:
      //   from	to	target	type
      //   2	29	162	finally
      //   42	144	162	finally
      //   147	162	162	finally
    }
    
    public void windowUpdate(int param1Int, long param1Long) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield closed : Z
      //   6: ifne -> 125
      //   9: lload_2
      //   10: lconst_0
      //   11: lcmp
      //   12: ifeq -> 82
      //   15: lload_2
      //   16: ldc2_w 2147483647
      //   19: lcmp
      //   20: ifgt -> 82
      //   23: aload_0
      //   24: getfield sink : Lokio/BufferedSink;
      //   27: ldc -2147287031
      //   29: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   34: pop
      //   35: aload_0
      //   36: getfield sink : Lokio/BufferedSink;
      //   39: bipush #8
      //   41: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   46: pop
      //   47: aload_0
      //   48: getfield sink : Lokio/BufferedSink;
      //   51: iload_1
      //   52: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   57: pop
      //   58: aload_0
      //   59: getfield sink : Lokio/BufferedSink;
      //   62: lload_2
      //   63: l2i
      //   64: invokeinterface writeInt : (I)Lokio/BufferedSink;
      //   69: pop
      //   70: aload_0
      //   71: getfield sink : Lokio/BufferedSink;
      //   74: invokeinterface flush : ()V
      //   79: aload_0
      //   80: monitorexit
      //   81: return
      //   82: new java/lang/IllegalArgumentException
      //   85: astore #4
      //   87: new java/lang/StringBuilder
      //   90: astore #5
      //   92: aload #5
      //   94: invokespecial <init> : ()V
      //   97: aload #5
      //   99: ldc 'windowSizeIncrement must be between 1 and 0x7fffffff: '
      //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   104: pop
      //   105: aload #5
      //   107: lload_2
      //   108: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   111: pop
      //   112: aload #4
      //   114: aload #5
      //   116: invokevirtual toString : ()Ljava/lang/String;
      //   119: invokespecial <init> : (Ljava/lang/String;)V
      //   122: aload #4
      //   124: athrow
      //   125: new java/io/IOException
      //   128: astore #4
      //   130: aload #4
      //   132: ldc 'closed'
      //   134: invokespecial <init> : (Ljava/lang/String;)V
      //   137: aload #4
      //   139: athrow
      //   140: astore #4
      //   142: aload_0
      //   143: monitorexit
      //   144: aload #4
      //   146: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	140	finally
      //   23	79	140	finally
      //   82	125	140	finally
      //   125	140	140	finally
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\Spdy3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */