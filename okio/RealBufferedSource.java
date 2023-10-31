package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

final class RealBufferedSource implements BufferedSource {
  public final Buffer buffer;
  
  private boolean closed;
  
  public final Source source;
  
  public RealBufferedSource(Source paramSource) {
    this(paramSource, new Buffer());
  }
  
  public RealBufferedSource(Source paramSource, Buffer paramBuffer) {
    if (paramSource != null) {
      this.buffer = paramBuffer;
      this.source = paramSource;
      return;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  private boolean rangeEquals(long paramLong, ByteString paramByteString) throws IOException {
    boolean bool;
    if (request(paramByteString.size() + paramLong) && this.buffer.rangeEquals(paramLong, paramByteString)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Buffer buffer() {
    return this.buffer;
  }
  
  public void close() throws IOException {
    if (this.closed)
      return; 
    this.closed = true;
    this.source.close();
    this.buffer.clear();
  }
  
  public boolean exhausted() throws IOException {
    if (!this.closed) {
      boolean bool;
      if (this.buffer.exhausted() && this.source.read(this.buffer, 2048L) == -1L) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
    throw new IllegalStateException("closed");
  }
  
  public long indexOf(byte paramByte) throws IOException {
    return indexOf(paramByte, 0L);
  }
  
  public long indexOf(byte paramByte, long paramLong) throws IOException {
    if (!this.closed) {
      long l;
      while (true) {
        l = paramLong;
        if (paramLong >= this.buffer.size) {
          if (this.source.read(this.buffer, 2048L) == -1L)
            return -1L; 
          continue;
        } 
        break;
      } 
      while (true) {
        paramLong = this.buffer.indexOf(paramByte, l);
        if (paramLong == -1L) {
          l = this.buffer.size;
          if (this.source.read(this.buffer, 2048L) == -1L)
            return -1L; 
          continue;
        } 
        return paramLong;
      } 
    } 
    throw new IllegalStateException("closed");
  }
  
  public long indexOf(ByteString paramByteString) throws IOException {
    return indexOf(paramByteString, 0L);
  }
  
  public long indexOf(ByteString paramByteString, long paramLong) throws IOException {
    if (paramByteString.size() != 0)
      while (true) {
        paramLong = indexOf(paramByteString.getByte(0), paramLong);
        if (paramLong == -1L)
          return -1L; 
        if (rangeEquals(paramLong, paramByteString))
          return paramLong; 
        paramLong++;
      }  
    throw new IllegalArgumentException("bytes is empty");
  }
  
  public long indexOfElement(ByteString paramByteString) throws IOException {
    return indexOfElement(paramByteString, 0L);
  }
  
  public long indexOfElement(ByteString paramByteString, long paramLong) throws IOException {
    if (!this.closed) {
      long l;
      while (true) {
        l = paramLong;
        if (paramLong >= this.buffer.size) {
          if (this.source.read(this.buffer, 2048L) == -1L)
            return -1L; 
          continue;
        } 
        break;
      } 
      while (true) {
        paramLong = this.buffer.indexOfElement(paramByteString, l);
        if (paramLong == -1L) {
          l = this.buffer.size;
          if (this.source.read(this.buffer, 2048L) == -1L)
            return -1L; 
          continue;
        } 
        return paramLong;
      } 
    } 
    throw new IllegalStateException("closed");
  }
  
  public InputStream inputStream() {
    return new InputStream() {
        final RealBufferedSource this$0;
        
        public int available() throws IOException {
          if (!RealBufferedSource.this.closed)
            return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L); 
          throw new IOException("closed");
        }
        
        public void close() throws IOException {
          RealBufferedSource.this.close();
        }
        
        public int read() throws IOException {
          if (!RealBufferedSource.this.closed)
            return (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L) == -1L) ? -1 : (RealBufferedSource.this.buffer.readByte() & 0xFF); 
          throw new IOException("closed");
        }
        
        public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
          if (!RealBufferedSource.this.closed) {
            Util.checkOffsetAndCount(param1ArrayOfbyte.length, param1Int1, param1Int2);
            return (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L) == -1L) ? -1 : RealBufferedSource.this.buffer.read(param1ArrayOfbyte, param1Int1, param1Int2);
          } 
          throw new IOException("closed");
        }
        
        public String toString() {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(RealBufferedSource.this);
          stringBuilder.append(".inputStream()");
          return stringBuilder.toString();
        }
      };
  }
  
  public int read(byte[] paramArrayOfbyte) throws IOException {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    long l1 = paramArrayOfbyte.length;
    long l2 = paramInt1;
    long l3 = paramInt2;
    Util.checkOffsetAndCount(l1, l2, l3);
    if (this.buffer.size == 0L && this.source.read(this.buffer, 2048L) == -1L)
      return -1; 
    paramInt2 = (int)Math.min(l3, this.buffer.size);
    return this.buffer.read(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public long read(Buffer paramBuffer, long paramLong) throws IOException {
    if (paramBuffer != null) {
      if (paramLong >= 0L) {
        if (!this.closed) {
          if (this.buffer.size == 0L && this.source.read(this.buffer, 2048L) == -1L)
            return -1L; 
          paramLong = Math.min(paramLong, this.buffer.size);
          return this.buffer.read(paramBuffer, paramLong);
        } 
        throw new IllegalStateException("closed");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public long readAll(Sink paramSink) throws IOException {
    if (paramSink != null) {
      long l1 = 0L;
      while (this.source.read(this.buffer, 2048L) != -1L) {
        long l = this.buffer.completeSegmentByteCount();
        if (l > 0L) {
          l1 += l;
          paramSink.write(this.buffer, l);
        } 
      } 
      long l2 = l1;
      if (this.buffer.size() > 0L) {
        l2 = l1 + this.buffer.size();
        Buffer buffer = this.buffer;
        paramSink.write(buffer, buffer.size());
      } 
      return l2;
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public byte readByte() throws IOException {
    require(1L);
    return this.buffer.readByte();
  }
  
  public byte[] readByteArray() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readByteArray();
  }
  
  public byte[] readByteArray(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readByteArray(paramLong);
  }
  
  public ByteString readByteString() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readByteString();
  }
  
  public ByteString readByteString(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readByteString(paramLong);
  }
  
  public long readDecimalLong() throws IOException {
    require(1L);
    int i = 0;
    while (true) {
      int j = i + 1;
      if (request(j)) {
        byte b = this.buffer.getByte(i);
        if ((b < 48 || b > 57) && (i != 0 || b != 45)) {
          if (i != 0)
            break; 
          throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[] { Byte.valueOf(b) }));
        } 
        i = j;
        continue;
      } 
      break;
    } 
    return this.buffer.readDecimalLong();
  }
  
  public void readFully(Buffer paramBuffer, long paramLong) throws IOException {
    try {
      require(paramLong);
      this.buffer.readFully(paramBuffer, paramLong);
      return;
    } catch (EOFException eOFException) {
      paramBuffer.writeAll(this.buffer);
      throw eOFException;
    } 
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws IOException {
    try {
      require(paramArrayOfbyte.length);
      this.buffer.readFully(paramArrayOfbyte);
      return;
    } catch (EOFException eOFException) {
      int i = 0;
      while (this.buffer.size > 0L) {
        Buffer buffer = this.buffer;
        int j = buffer.read(paramArrayOfbyte, i, (int)buffer.size);
        if (j != -1) {
          i += j;
          continue;
        } 
        throw new AssertionError();
      } 
      throw eOFException;
    } 
  }
  
  public long readHexadecimalUnsignedLong() throws IOException {
    require(1L);
    int i = 0;
    while (true) {
      int j = i + 1;
      if (request(j)) {
        byte b = this.buffer.getByte(i);
        if ((b < 48 || b > 57) && (b < 97 || b > 102) && (b < 65 || b > 70)) {
          if (i != 0)
            break; 
          throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[] { Byte.valueOf(b) }));
        } 
        i = j;
        continue;
      } 
      break;
    } 
    return this.buffer.readHexadecimalUnsignedLong();
  }
  
  public int readInt() throws IOException {
    require(4L);
    return this.buffer.readInt();
  }
  
  public int readIntLe() throws IOException {
    require(4L);
    return this.buffer.readIntLe();
  }
  
  public long readLong() throws IOException {
    require(8L);
    return this.buffer.readLong();
  }
  
  public long readLongLe() throws IOException {
    require(8L);
    return this.buffer.readLongLe();
  }
  
  public short readShort() throws IOException {
    require(2L);
    return this.buffer.readShort();
  }
  
  public short readShortLe() throws IOException {
    require(2L);
    return this.buffer.readShortLe();
  }
  
  public String readString(long paramLong, Charset paramCharset) throws IOException {
    require(paramLong);
    if (paramCharset != null)
      return this.buffer.readString(paramLong, paramCharset); 
    throw new IllegalArgumentException("charset == null");
  }
  
  public String readString(Charset paramCharset) throws IOException {
    if (paramCharset != null) {
      this.buffer.writeAll(this.source);
      return this.buffer.readString(paramCharset);
    } 
    throw new IllegalArgumentException("charset == null");
  }
  
  public String readUtf8() throws IOException {
    this.buffer.writeAll(this.source);
    return this.buffer.readUtf8();
  }
  
  public String readUtf8(long paramLong) throws IOException {
    require(paramLong);
    return this.buffer.readUtf8(paramLong);
  }
  
  public int readUtf8CodePoint() throws IOException {
    require(1L);
    byte b = this.buffer.getByte(0L);
    if ((b & 0xE0) == 192) {
      require(2L);
    } else if ((b & 0xF0) == 224) {
      require(3L);
    } else if ((b & 0xF8) == 240) {
      require(4L);
    } 
    return this.buffer.readUtf8CodePoint();
  }
  
  public String readUtf8Line() throws IOException {
    long l = indexOf((byte)10);
    if (l == -1L) {
      String str;
      if (this.buffer.size != 0L) {
        str = readUtf8(this.buffer.size);
      } else {
        str = null;
      } 
      return str;
    } 
    return this.buffer.readUtf8Line(l);
  }
  
  public String readUtf8LineStrict() throws IOException {
    long l = indexOf((byte)10);
    if (l != -1L)
      return this.buffer.readUtf8Line(l); 
    Buffer buffer1 = new Buffer();
    Buffer buffer2 = this.buffer;
    buffer2.copyTo(buffer1, 0L, Math.min(32L, buffer2.size()));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\\n not found: size=");
    stringBuilder.append(this.buffer.size());
    stringBuilder.append(" content=");
    stringBuilder.append(buffer1.readByteString().hex());
    stringBuilder.append("...");
    throw new EOFException(stringBuilder.toString());
  }
  
  public boolean request(long paramLong) throws IOException {
    if (paramLong >= 0L) {
      if (!this.closed) {
        while (this.buffer.size < paramLong) {
          if (this.source.read(this.buffer, 2048L) == -1L)
            return false; 
        } 
        return true;
      } 
      throw new IllegalStateException("closed");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void require(long paramLong) throws IOException {
    if (request(paramLong))
      return; 
    throw new EOFException();
  }
  
  public void skip(long paramLong) throws IOException {
    if (!this.closed) {
      while (paramLong > 0L) {
        if (this.buffer.size != 0L || this.source.read(this.buffer, 2048L) != -1L) {
          long l = Math.min(paramLong, this.buffer.size());
          this.buffer.skip(l);
          paramLong -= l;
          continue;
        } 
        throw new EOFException();
      } 
      return;
    } 
    throw new IllegalStateException("closed");
  }
  
  public Timeout timeout() {
    return this.source.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("buffer(");
    stringBuilder.append(this.source);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\RealBufferedSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */