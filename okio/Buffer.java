package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable {
  private static final byte[] DIGITS = new byte[] { 
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
      97, 98, 99, 100, 101, 102 };
  
  static final int REPLACEMENT_CHARACTER = 65533;
  
  Segment head;
  
  long size;
  
  private void readFrom(InputStream paramInputStream, long paramLong, boolean paramBoolean) throws IOException {
    if (paramInputStream != null)
      while (true) {
        if (paramLong > 0L || paramBoolean) {
          Segment segment = writableSegment(1);
          int i = (int)Math.min(paramLong, (2048 - segment.limit));
          i = paramInputStream.read(segment.data, segment.limit, i);
          if (i == -1) {
            if (paramBoolean)
              return; 
            throw new EOFException();
          } 
          segment.limit += i;
          long l1 = this.size;
          long l2 = i;
          this.size = l1 + l2;
          paramLong -= l2;
          continue;
        } 
        return;
      }  
    throw new IllegalArgumentException("in == null");
  }
  
  public Buffer buffer() {
    return this;
  }
  
  public void clear() {
    try {
      skip(this.size);
      return;
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public Buffer clone() {
    Buffer buffer = new Buffer();
    if (this.size == 0L)
      return buffer; 
    Segment segment = new Segment(this.head);
    buffer.head = segment;
    segment.prev = segment;
    segment.next = segment;
    segment = this.head;
    while (true) {
      segment = segment.next;
      if (segment != this.head) {
        buffer.head.prev.push(new Segment(segment));
        continue;
      } 
      buffer.size = this.size;
      return buffer;
    } 
  }
  
  public void close() {}
  
  public long completeSegmentByteCount() {
    long l2 = this.size;
    if (l2 == 0L)
      return 0L; 
    Segment segment = this.head.prev;
    long l1 = l2;
    if (segment.limit < 2048) {
      l1 = l2;
      if (segment.owner)
        l1 = l2 - (segment.limit - segment.pos); 
    } 
    return l1;
  }
  
  public Buffer copyTo(OutputStream paramOutputStream) throws IOException {
    return copyTo(paramOutputStream, 0L, this.size);
  }
  
  public Buffer copyTo(OutputStream paramOutputStream, long paramLong1, long paramLong2) throws IOException {
    if (paramOutputStream != null) {
      long l1;
      long l2;
      Segment segment2;
      Util.checkOffsetAndCount(this.size, paramLong1, paramLong2);
      if (paramLong2 == 0L)
        return this; 
      Segment segment1 = this.head;
      while (true) {
        segment2 = segment1;
        l1 = paramLong1;
        l2 = paramLong2;
        if (paramLong1 >= (segment1.limit - segment1.pos)) {
          paramLong1 -= (segment1.limit - segment1.pos);
          segment1 = segment1.next;
          continue;
        } 
        break;
      } 
      while (l2 > 0L) {
        int j = (int)(segment2.pos + l1);
        int i = (int)Math.min((segment2.limit - j), l2);
        paramOutputStream.write(segment2.data, j, i);
        l2 -= i;
        segment2 = segment2.next;
        l1 = 0L;
      } 
      return this;
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public Buffer copyTo(Buffer paramBuffer, long paramLong1, long paramLong2) {
    if (paramBuffer != null) {
      long l1;
      long l2;
      Segment segment2;
      Util.checkOffsetAndCount(this.size, paramLong1, paramLong2);
      if (paramLong2 == 0L)
        return this; 
      paramBuffer.size += paramLong2;
      Segment segment1 = this.head;
      while (true) {
        segment2 = segment1;
        l1 = paramLong1;
        l2 = paramLong2;
        if (paramLong1 >= (segment1.limit - segment1.pos)) {
          paramLong1 -= (segment1.limit - segment1.pos);
          segment1 = segment1.next;
          continue;
        } 
        break;
      } 
      while (l2 > 0L) {
        segment1 = new Segment(segment2);
        segment1.pos = (int)(segment1.pos + l1);
        segment1.limit = Math.min(segment1.pos + (int)l2, segment1.limit);
        Segment segment = paramBuffer.head;
        if (segment == null) {
          segment1.prev = segment1;
          segment1.next = segment1;
          paramBuffer.head = segment1;
        } else {
          segment.prev.push(segment1);
        } 
        l2 -= (segment1.limit - segment1.pos);
        segment2 = segment2.next;
        l1 = 0L;
      } 
      return this;
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public BufferedSink emit() {
    return this;
  }
  
  public Buffer emitCompleteSegments() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof Buffer))
      return false; 
    paramObject = paramObject;
    long l2 = this.size;
    if (l2 != ((Buffer)paramObject).size)
      return false; 
    long l1 = 0L;
    if (l2 == 0L)
      return true; 
    Segment segment = this.head;
    paramObject = ((Buffer)paramObject).head;
    int j = segment.pos;
    int i = ((Segment)paramObject).pos;
    while (l1 < this.size) {
      l2 = Math.min(segment.limit - j, ((Segment)paramObject).limit - i);
      int k = 0;
      while (k < l2) {
        if (segment.data[j] != ((Segment)paramObject).data[i])
          return false; 
        k++;
        j++;
        i++;
      } 
      Segment segment1 = segment;
      k = j;
      if (j == segment.limit) {
        segment1 = segment.next;
        k = segment1.pos;
      } 
      int m = i;
      Object object = paramObject;
      if (i == ((Segment)paramObject).limit) {
        object = ((Segment)paramObject).next;
        m = ((Segment)object).pos;
      } 
      l1 += l2;
      segment = segment1;
      j = k;
      i = m;
      paramObject = object;
    } 
    return true;
  }
  
  public boolean exhausted() {
    boolean bool;
    if (this.size == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void flush() {}
  
  public byte getByte(long paramLong) {
    Util.checkOffsetAndCount(this.size, paramLong, 1L);
    for (Segment segment = this.head;; segment = segment.next) {
      long l = (segment.limit - segment.pos);
      if (paramLong < l)
        return segment.data[segment.pos + (int)paramLong]; 
      paramLong -= l;
    } 
  }
  
  public int hashCode() {
    Segment segment = this.head;
    if (segment == null)
      return 0; 
    int i = 1;
    while (true) {
      int k = segment.pos;
      int m = segment.limit;
      int j = i;
      while (k < m) {
        j = j * 31 + segment.data[k];
        k++;
      } 
      Segment segment1 = segment.next;
      segment = segment1;
      i = j;
      if (segment1 == this.head)
        return j; 
    } 
  }
  
  public long indexOf(byte paramByte) {
    return indexOf(paramByte, 0L);
  }
  
  public long indexOf(byte paramByte, long paramLong) {
    if (paramLong >= 0L) {
      Segment segment = this.head;
      if (segment == null)
        return -1L; 
      long l = 0L;
      while (true) {
        long l1 = (segment.limit - segment.pos);
        if (paramLong >= l1) {
          paramLong -= l1;
        } else {
          byte[] arrayOfByte = segment.data;
          int i = (int)(segment.pos + paramLong);
          int j = segment.limit;
          while (i < j) {
            if (arrayOfByte[i] == paramByte)
              return l + i - segment.pos; 
            i++;
          } 
          paramLong = 0L;
        } 
        l += l1;
        Segment segment1 = segment.next;
        segment = segment1;
        if (segment1 == this.head)
          return -1L; 
      } 
    } 
    throw new IllegalArgumentException("fromIndex < 0");
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
  
  public long indexOfElement(ByteString paramByteString) {
    return indexOfElement(paramByteString, 0L);
  }
  
  public long indexOfElement(ByteString paramByteString, long paramLong) {
    if (paramLong >= 0L) {
      Segment segment2 = this.head;
      if (segment2 == null)
        return -1L; 
      byte[] arrayOfByte = paramByteString.toByteArray();
      long l = 0L;
      Segment segment1 = segment2;
      while (true) {
        long l1 = (segment1.limit - segment1.pos);
        if (paramLong >= l1) {
          paramLong -= l1;
        } else {
          byte[] arrayOfByte1 = segment1.data;
          paramLong = segment1.pos + paramLong;
          long l2 = segment1.limit;
          while (paramLong < l2) {
            byte b1 = arrayOfByte1[(int)paramLong];
            int i = arrayOfByte.length;
            for (byte b = 0; b < i; b++) {
              if (b1 == arrayOfByte[b])
                return l + paramLong - segment1.pos; 
            } 
            paramLong++;
          } 
          paramLong = 0L;
        } 
        l += l1;
        segment1 = segment1.next;
        if (segment1 == this.head)
          return -1L; 
      } 
    } 
    throw new IllegalArgumentException("fromIndex < 0");
  }
  
  public InputStream inputStream() {
    return new InputStream() {
        final Buffer this$0;
        
        public int available() {
          return (int)Math.min(Buffer.this.size, 2147483647L);
        }
        
        public void close() {}
        
        public int read() {
          return (Buffer.this.size > 0L) ? (Buffer.this.readByte() & 0xFF) : -1;
        }
        
        public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
          return Buffer.this.read(param1ArrayOfbyte, param1Int1, param1Int2);
        }
        
        public String toString() {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(Buffer.this);
          stringBuilder.append(".inputStream()");
          return stringBuilder.toString();
        }
      };
  }
  
  public OutputStream outputStream() {
    return new OutputStream() {
        final Buffer this$0;
        
        public void close() {}
        
        public void flush() {}
        
        public String toString() {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(this);
          stringBuilder.append(".outputStream()");
          return stringBuilder.toString();
        }
        
        public void write(int param1Int) {
          Buffer.this.writeByte((byte)param1Int);
        }
        
        public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
          Buffer.this.write(param1ArrayOfbyte, param1Int1, param1Int2);
        }
      };
  }
  
  boolean rangeEquals(long paramLong, ByteString paramByteString) {
    int i = paramByteString.size();
    if (this.size - paramLong < i)
      return false; 
    for (byte b = 0; b < i; b++) {
      if (getByte(b + paramLong) != paramByteString.getByte(b))
        return false; 
    } 
    return true;
  }
  
  public int read(byte[] paramArrayOfbyte) {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
    Segment segment = this.head;
    if (segment == null)
      return -1; 
    paramInt2 = Math.min(paramInt2, segment.limit - segment.pos);
    System.arraycopy(segment.data, segment.pos, paramArrayOfbyte, paramInt1, paramInt2);
    segment.pos += paramInt2;
    this.size -= paramInt2;
    if (segment.pos == segment.limit) {
      this.head = segment.pop();
      SegmentPool.recycle(segment);
    } 
    return paramInt2;
  }
  
  public long read(Buffer paramBuffer, long paramLong) {
    if (paramBuffer != null) {
      if (paramLong >= 0L) {
        long l2 = this.size;
        if (l2 == 0L)
          return -1L; 
        long l1 = paramLong;
        if (paramLong > l2)
          l1 = l2; 
        paramBuffer.write(this, l1);
        return l1;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("sink == null");
  }
  
  public long readAll(Sink paramSink) throws IOException {
    long l = this.size;
    if (l > 0L)
      paramSink.write(this, l); 
    return l;
  }
  
  public byte readByte() {
    if (this.size != 0L) {
      Segment segment = this.head;
      int j = segment.pos;
      int k = segment.limit;
      byte[] arrayOfByte = segment.data;
      int i = j + 1;
      byte b = arrayOfByte[j];
      this.size--;
      if (i == k) {
        this.head = segment.pop();
        SegmentPool.recycle(segment);
      } else {
        segment.pos = i;
      } 
      return b;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public byte[] readByteArray() {
    try {
      return readByteArray(this.size);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public byte[] readByteArray(long paramLong) throws EOFException {
    Util.checkOffsetAndCount(this.size, 0L, paramLong);
    if (paramLong <= 2147483647L) {
      byte[] arrayOfByte = new byte[(int)paramLong];
      readFully(arrayOfByte);
      return arrayOfByte;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public ByteString readByteString() {
    return new ByteString(readByteArray());
  }
  
  public ByteString readByteString(long paramLong) throws EOFException {
    return new ByteString(readByteArray(paramLong));
  }
  
  public long readDecimalLong() {
    long l2 = this.size;
    long l1 = 0L;
    if (l2 != 0L) {
      l2 = -7L;
      byte b = 0;
      boolean bool = false;
      for (int i = 0;; i = k) {
        int k;
        Buffer buffer;
        Segment segment = this.head;
        byte[] arrayOfByte = segment.data;
        int j = segment.pos;
        int m = segment.limit;
        while (true) {
          k = i;
          if (j < m) {
            byte b1 = arrayOfByte[j];
            if (b1 >= 48 && b1 <= 57) {
              int n = 48 - b1;
              k = l1 cmp -922337203685477580L;
              if (k < 0 || (k == 0 && n < l2)) {
                buffer = (new Buffer()).writeDecimalLong(l1).writeByte(b1);
                if (!bool)
                  buffer.readByte(); 
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Number too large: ");
                stringBuilder.append(buffer.readUtf8());
                throw new NumberFormatException(stringBuilder.toString());
              } 
              l1 = l1 * 10L + n;
            } else if (b1 == 45 && !b) {
              l2--;
              bool = true;
            } else {
              if (b) {
                k = 1;
                break;
              } 
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Expected leading [0-9] or '-' character but was 0x");
              stringBuilder.append(Integer.toHexString(b1));
              throw new NumberFormatException(stringBuilder.toString());
            } 
            j++;
            b++;
            continue;
          } 
          break;
        } 
        if (j == m) {
          this.head = buffer.pop();
          SegmentPool.recycle((Segment)buffer);
        } else {
          ((Segment)buffer).pos = j;
        } 
        if (k != 0 || this.head == null)
          break; 
      } 
      this.size -= b;
      if (!bool)
        l1 = -l1; 
      return l1;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public Buffer readFrom(InputStream paramInputStream) throws IOException {
    readFrom(paramInputStream, Long.MAX_VALUE, true);
    return this;
  }
  
  public Buffer readFrom(InputStream paramInputStream, long paramLong) throws IOException {
    if (paramLong >= 0L) {
      readFrom(paramInputStream, paramLong, false);
      return this;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("byteCount < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void readFully(Buffer paramBuffer, long paramLong) throws EOFException {
    long l = this.size;
    if (l >= paramLong) {
      paramBuffer.write(this, paramLong);
      return;
    } 
    paramBuffer.write(this, l);
    throw new EOFException();
  }
  
  public void readFully(byte[] paramArrayOfbyte) throws EOFException {
    int i = 0;
    while (i < paramArrayOfbyte.length) {
      int j = read(paramArrayOfbyte, i, paramArrayOfbyte.length - i);
      if (j != -1) {
        i += j;
        continue;
      } 
      throw new EOFException();
    } 
  }
  
  public long readHexadecimalUnsignedLong() {
    if (this.size != 0L) {
      int i;
      long l2;
      int j = 0;
      byte b = 0;
      long l1 = 0L;
      while (true) {
        byte b1;
        Buffer buffer;
        Segment segment = this.head;
        byte[] arrayOfByte = segment.data;
        int k = segment.pos;
        int m = segment.limit;
        l2 = l1;
        i = j;
        while (true) {
          b1 = b;
          if (k < m) {
            b1 = arrayOfByte[k];
            if (b1 >= 48 && b1 <= 57) {
              j = b1 - 48;
            } else {
              if (b1 >= 97 && b1 <= 102) {
                j = b1 - 97;
              } else if (b1 >= 65 && b1 <= 70) {
                j = b1 - 65;
              } else {
                if (i != 0) {
                  b1 = 1;
                  break;
                } 
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("Expected leading [0-9a-fA-F] character but was 0x");
                stringBuilder1.append(Integer.toHexString(b1));
                throw new NumberFormatException(stringBuilder1.toString());
              } 
              j += 10;
            } 
            if ((0xF000000000000000L & l2) == 0L) {
              l2 = l2 << 4L | j;
              k++;
              i++;
              continue;
            } 
            buffer = (new Buffer()).writeHexadecimalUnsignedLong(l2).writeByte(b1);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Number too large: ");
            stringBuilder.append(buffer.readUtf8());
            throw new NumberFormatException(stringBuilder.toString());
          } 
          break;
        } 
        if (k == m) {
          this.head = buffer.pop();
          SegmentPool.recycle((Segment)buffer);
        } else {
          ((Segment)buffer).pos = k;
        } 
        if (b1 == 0) {
          j = i;
          b = b1;
          l1 = l2;
          if (this.head == null)
            break; 
          continue;
        } 
        break;
      } 
      this.size -= i;
      return l2;
    } 
    throw new IllegalStateException("size == 0");
  }
  
  public int readInt() {
    if (this.size >= 4L) {
      Segment segment = this.head;
      int j = segment.pos;
      int i = segment.limit;
      if (i - j < 4)
        return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF; 
      byte[] arrayOfByte = segment.data;
      int k = j + 1;
      j = arrayOfByte[j];
      int m = k + 1;
      k = arrayOfByte[k];
      int n = m + 1;
      byte b = arrayOfByte[m];
      m = n + 1;
      n = arrayOfByte[n];
      this.size -= 4L;
      if (m == i) {
        this.head = segment.pop();
        SegmentPool.recycle(segment);
      } else {
        segment.pos = m;
      } 
      return (j & 0xFF) << 24 | (k & 0xFF) << 16 | (b & 0xFF) << 8 | n & 0xFF;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 4: ");
    stringBuilder.append(this.size);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public int readIntLe() {
    return Util.reverseBytesInt(readInt());
  }
  
  public long readLong() {
    if (this.size >= 8L) {
      Segment segment = this.head;
      int k = segment.pos;
      int i = segment.limit;
      if (i - k < 8)
        return (readInt() & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL & readInt(); 
      byte[] arrayOfByte = segment.data;
      int j = k + 1;
      long l5 = arrayOfByte[k];
      k = j + 1;
      long l2 = arrayOfByte[j];
      j = k + 1;
      long l3 = arrayOfByte[k];
      k = j + 1;
      long l7 = arrayOfByte[j];
      j = k + 1;
      long l4 = arrayOfByte[k];
      k = j + 1;
      long l8 = arrayOfByte[j];
      j = k + 1;
      long l6 = arrayOfByte[k];
      k = j + 1;
      long l1 = arrayOfByte[j];
      this.size -= 8L;
      if (k == i) {
        this.head = segment.pop();
        SegmentPool.recycle(segment);
      } else {
        segment.pos = k;
      } 
      return l1 & 0xFFL | (l5 & 0xFFL) << 56L | (l2 & 0xFFL) << 48L | (l3 & 0xFFL) << 40L | (l7 & 0xFFL) << 32L | (l4 & 0xFFL) << 24L | (l8 & 0xFFL) << 16L | (l6 & 0xFFL) << 8L;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 8: ");
    stringBuilder.append(this.size);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public long readLongLe() {
    return Util.reverseBytesLong(readLong());
  }
  
  public short readShort() {
    if (this.size >= 2L) {
      Segment segment = this.head;
      int k = segment.pos;
      int i = segment.limit;
      if (i - k < 2)
        return (short)((readByte() & 0xFF) << 8 | readByte() & 0xFF); 
      byte[] arrayOfByte = segment.data;
      int j = k + 1;
      byte b = arrayOfByte[k];
      k = j + 1;
      j = arrayOfByte[j];
      this.size -= 2L;
      if (k == i) {
        this.head = segment.pop();
        SegmentPool.recycle(segment);
      } else {
        segment.pos = k;
      } 
      return (short)((b & 0xFF) << 8 | j & 0xFF);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size < 2: ");
    stringBuilder.append(this.size);
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public short readShortLe() {
    return Util.reverseBytesShort(readShort());
  }
  
  public String readString(long paramLong, Charset paramCharset) throws EOFException {
    Util.checkOffsetAndCount(this.size, 0L, paramLong);
    if (paramCharset != null) {
      if (paramLong <= 2147483647L) {
        if (paramLong == 0L)
          return ""; 
        Segment segment = this.head;
        if (segment.pos + paramLong > segment.limit)
          return new String(readByteArray(paramLong), paramCharset); 
        String str = new String(segment.data, segment.pos, (int)paramLong, paramCharset);
        segment.pos = (int)(segment.pos + paramLong);
        this.size -= paramLong;
        if (segment.pos == segment.limit) {
          this.head = segment.pop();
          SegmentPool.recycle(segment);
        } 
        return str;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount > Integer.MAX_VALUE: ");
      stringBuilder.append(paramLong);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("charset == null");
  }
  
  public String readString(Charset paramCharset) {
    try {
      return readString(this.size, paramCharset);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public String readUtf8() {
    try {
      return readString(this.size, Util.UTF_8);
    } catch (EOFException eOFException) {
      throw new AssertionError(eOFException);
    } 
  }
  
  public String readUtf8(long paramLong) throws EOFException {
    return readString(paramLong, Util.UTF_8);
  }
  
  public int readUtf8CodePoint() throws EOFException {
    if (this.size != 0L) {
      int i;
      byte b1;
      int j;
      byte b = getByte(0L);
      byte b2 = 1;
      if ((b & 0x80) == 0) {
        i = b & Byte.MAX_VALUE;
        j = 0;
        b1 = 1;
      } else if ((b & 0xE0) == 192) {
        i = b & 0x1F;
        b1 = 2;
        j = 128;
      } else if ((b & 0xF0) == 224) {
        i = b & 0xF;
        b1 = 3;
        j = 2048;
      } else if ((b & 0xF8) == 240) {
        i = b & 0x7;
        b1 = 4;
        j = 65536;
      } else {
        skip(1L);
        return 65533;
      } 
      long l2 = this.size;
      long l1 = b1;
      if (l2 >= l1) {
        while (b2 < b1) {
          l2 = b2;
          b = getByte(l2);
          if ((b & 0xC0) == 128) {
            i = i << 6 | b & 0x3F;
            b2++;
            continue;
          } 
          skip(l2);
          return 65533;
        } 
        skip(l1);
        return (i > 1114111) ? 65533 : ((i >= 55296 && i <= 57343) ? 65533 : ((i < j) ? 65533 : i));
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("size < ");
      stringBuilder.append(b1);
      stringBuilder.append(": ");
      stringBuilder.append(this.size);
      stringBuilder.append(" (to read code point prefixed 0x");
      stringBuilder.append(Integer.toHexString(b));
      stringBuilder.append(")");
      throw new EOFException(stringBuilder.toString());
    } 
    throw new EOFException();
  }
  
  public String readUtf8Line() throws EOFException {
    long l = indexOf((byte)10);
    if (l == -1L) {
      String str;
      l = this.size;
      if (l != 0L) {
        str = readUtf8(l);
      } else {
        str = null;
      } 
      return str;
    } 
    return readUtf8Line(l);
  }
  
  String readUtf8Line(long paramLong) throws EOFException {
    if (paramLong > 0L) {
      long l = paramLong - 1L;
      if (getByte(l) == 13) {
        String str1 = readUtf8(l);
        skip(2L);
        return str1;
      } 
    } 
    String str = readUtf8(paramLong);
    skip(1L);
    return str;
  }
  
  public String readUtf8LineStrict() throws EOFException {
    long l = indexOf((byte)10);
    if (l != -1L)
      return readUtf8Line(l); 
    Buffer buffer = new Buffer();
    copyTo(buffer, 0L, Math.min(32L, this.size));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\\n not found: size=");
    stringBuilder.append(size());
    stringBuilder.append(" content=");
    stringBuilder.append(buffer.readByteString().hex());
    stringBuilder.append("...");
    throw new EOFException(stringBuilder.toString());
  }
  
  public boolean request(long paramLong) {
    boolean bool;
    if (this.size >= paramLong) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void require(long paramLong) throws EOFException {
    if (this.size >= paramLong)
      return; 
    throw new EOFException();
  }
  
  List<Integer> segmentSizes() {
    if (this.head == null)
      return Collections.emptyList(); 
    ArrayList<Integer> arrayList = new ArrayList();
    arrayList.add(Integer.valueOf(this.head.limit - this.head.pos));
    Segment segment = this.head;
    while (true) {
      segment = segment.next;
      if (segment != this.head) {
        arrayList.add(Integer.valueOf(segment.limit - segment.pos));
        continue;
      } 
      return arrayList;
    } 
  }
  
  public long size() {
    return this.size;
  }
  
  public void skip(long paramLong) throws EOFException {
    while (paramLong > 0L) {
      Segment segment = this.head;
      if (segment != null) {
        int i = (int)Math.min(paramLong, (segment.limit - this.head.pos));
        long l1 = this.size;
        long l2 = i;
        this.size = l1 - l2;
        l1 = paramLong - l2;
        segment = this.head;
        segment.pos += i;
        paramLong = l1;
        if (this.head.pos == this.head.limit) {
          segment = this.head;
          this.head = segment.pop();
          SegmentPool.recycle(segment);
          paramLong = l1;
        } 
        continue;
      } 
      throw new EOFException();
    } 
  }
  
  public ByteString snapshot() {
    long l = this.size;
    if (l <= 2147483647L)
      return snapshot((int)l); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("size > Integer.MAX_VALUE: ");
    stringBuilder.append(this.size);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public ByteString snapshot(int paramInt) {
    return (paramInt == 0) ? ByteString.EMPTY : new SegmentedByteString(this, paramInt);
  }
  
  public Timeout timeout() {
    return Timeout.NONE;
  }
  
  public String toString() {
    long l = this.size;
    if (l == 0L)
      return "Buffer[size=0]"; 
    if (l <= 16L) {
      ByteString byteString = clone().readByteString();
      return String.format("Buffer[size=%s data=%s]", new Object[] { Long.valueOf(this.size), byteString.hex() });
    } 
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
      Segment segment = this.head;
      while (true) {
        segment = segment.next;
        if (segment != this.head) {
          messageDigest.update(segment.data, segment.pos, segment.limit - segment.pos);
          continue;
        } 
        return String.format("Buffer[size=%s md5=%s]", new Object[] { Long.valueOf(this.size), ByteString.of(messageDigest.digest()).hex() });
      } 
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError();
    } 
  }
  
  Segment writableSegment(int paramInt) {
    Segment segment;
    if (paramInt >= 1 && paramInt <= 2048) {
      Segment segment1 = this.head;
      if (segment1 == null) {
        segment1 = SegmentPool.take();
        this.head = segment1;
        segment1.prev = segment1;
        segment1.next = segment1;
        return segment1;
      } 
      segment = segment1.prev;
      if (segment.limit + paramInt <= 2048) {
        segment1 = segment;
        return !segment.owner ? segment.push(SegmentPool.take()) : segment1;
      } 
    } else {
      throw new IllegalArgumentException();
    } 
    return segment.push(SegmentPool.take());
  }
  
  public Buffer write(ByteString paramByteString) {
    if (paramByteString != null) {
      paramByteString.write(this);
      return this;
    } 
    throw new IllegalArgumentException("byteString == null");
  }
  
  public Buffer write(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null)
      return write(paramArrayOfbyte, 0, paramArrayOfbyte.length); 
    throw new IllegalArgumentException("source == null");
  }
  
  public Buffer write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      long l1 = paramArrayOfbyte.length;
      long l2 = paramInt1;
      long l3 = paramInt2;
      Util.checkOffsetAndCount(l1, l2, l3);
      paramInt2 += paramInt1;
      while (paramInt1 < paramInt2) {
        Segment segment = writableSegment(1);
        int i = Math.min(paramInt2 - paramInt1, 2048 - segment.limit);
        System.arraycopy(paramArrayOfbyte, paramInt1, segment.data, segment.limit, i);
        paramInt1 += i;
        segment.limit += i;
      } 
      this.size += l3;
      return this;
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public BufferedSink write(Source paramSource, long paramLong) throws IOException {
    while (paramLong > 0L) {
      long l = paramSource.read(this, paramLong);
      if (l != -1L) {
        paramLong -= l;
        continue;
      } 
      throw new EOFException();
    } 
    return this;
  }
  
  public void write(Buffer paramBuffer, long paramLong) {
    if (paramBuffer != null) {
      if (paramBuffer != this) {
        Util.checkOffsetAndCount(paramBuffer.size, 0L, paramLong);
        while (paramLong > 0L) {
          if (paramLong < (paramBuffer.head.limit - paramBuffer.head.pos)) {
            Segment segment = this.head;
            if (segment != null) {
              segment = segment.prev;
            } else {
              segment = null;
            } 
            if (segment != null && segment.owner) {
              int i;
              long l1 = segment.limit;
              if (segment.shared) {
                i = 0;
              } else {
                i = segment.pos;
              } 
              if (l1 + paramLong - i <= 2048L) {
                paramBuffer.head.writeTo(segment, (int)paramLong);
                paramBuffer.size -= paramLong;
                this.size += paramLong;
                return;
              } 
            } 
            paramBuffer.head = paramBuffer.head.split((int)paramLong);
          } 
          Segment segment1 = paramBuffer.head;
          long l = (segment1.limit - segment1.pos);
          paramBuffer.head = segment1.pop();
          Segment segment2 = this.head;
          if (segment2 == null) {
            this.head = segment1;
            segment1.prev = segment1;
            segment1.next = segment1;
          } else {
            segment2.prev.push(segment1).compact();
          } 
          paramBuffer.size -= l;
          this.size += l;
          paramLong -= l;
        } 
        return;
      } 
      throw new IllegalArgumentException("source == this");
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public long writeAll(Source paramSource) throws IOException {
    if (paramSource != null) {
      long l = 0L;
      while (true) {
        long l1 = paramSource.read(this, 2048L);
        if (l1 != -1L) {
          l += l1;
          continue;
        } 
        return l;
      } 
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public Buffer writeByte(int paramInt) {
    Segment segment = writableSegment(1);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit;
    segment.limit = i + 1;
    arrayOfByte[i] = (byte)paramInt;
    this.size++;
    return this;
  }
  
  public Buffer writeDecimalLong(long paramLong) {
    int j = paramLong cmp 0L;
    if (j == 0)
      return writeByte(48); 
    boolean bool = false;
    int i = 1;
    long l = paramLong;
    if (j < 0) {
      l = -paramLong;
      if (l < 0L)
        return writeUtf8("-9223372036854775808"); 
      bool = true;
    } 
    if (l < 100000000L) {
      if (l < 10000L) {
        if (l < 100L) {
          if (l >= 10L)
            i = 2; 
        } else if (l < 1000L) {
          i = 3;
        } else {
          i = 4;
        } 
      } else if (l < 1000000L) {
        if (l < 100000L) {
          i = 5;
        } else {
          i = 6;
        } 
      } else if (l < 10000000L) {
        i = 7;
      } else {
        i = 8;
      } 
    } else if (l < 1000000000000L) {
      if (l < 10000000000L) {
        if (l < 1000000000L) {
          i = 9;
        } else {
          i = 10;
        } 
      } else if (l < 100000000000L) {
        i = 11;
      } else {
        i = 12;
      } 
    } else if (l < 1000000000000000L) {
      if (l < 10000000000000L) {
        i = 13;
      } else if (l < 100000000000000L) {
        i = 14;
      } else {
        i = 15;
      } 
    } else if (l < 100000000000000000L) {
      if (l < 10000000000000000L) {
        i = 16;
      } else {
        i = 17;
      } 
    } else if (l < 1000000000000000000L) {
      i = 18;
    } else {
      i = 19;
    } 
    j = i;
    if (bool)
      j = i + 1; 
    Segment segment = writableSegment(j);
    byte[] arrayOfByte = segment.data;
    i = segment.limit + j;
    while (l != 0L) {
      int k = (int)(l % 10L);
      arrayOfByte[--i] = DIGITS[k];
      l /= 10L;
    } 
    if (bool)
      arrayOfByte[i - 1] = 45; 
    segment.limit += j;
    this.size += j;
    return this;
  }
  
  public Buffer writeHexadecimalUnsignedLong(long paramLong) {
    if (paramLong == 0L)
      return writeByte(48); 
    int k = Long.numberOfTrailingZeros(Long.highestOneBit(paramLong)) / 4 + 1;
    Segment segment = writableSegment(k);
    byte[] arrayOfByte = segment.data;
    int i = segment.limit + k - 1;
    int j = segment.limit;
    while (i >= j) {
      arrayOfByte[i] = DIGITS[(int)(0xFL & paramLong)];
      paramLong >>>= 4L;
      i--;
    } 
    segment.limit += k;
    this.size += k;
    return this;
  }
  
  public Buffer writeInt(int paramInt) {
    Segment segment = writableSegment(4);
    byte[] arrayOfByte = segment.data;
    int j = segment.limit;
    int i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 24 & 0xFF);
    j = i + 1;
    arrayOfByte[i] = (byte)(paramInt >>> 16 & 0xFF);
    i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[i] = (byte)(paramInt & 0xFF);
    segment.limit = i + 1;
    this.size += 4L;
    return this;
  }
  
  public Buffer writeIntLe(int paramInt) {
    return writeInt(Util.reverseBytesInt(paramInt));
  }
  
  public Buffer writeLong(long paramLong) {
    Segment segment = writableSegment(8);
    byte[] arrayOfByte = segment.data;
    int j = segment.limit;
    int i = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 56L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 48L & 0xFFL);
    int k = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 40L & 0xFFL);
    i = k + 1;
    arrayOfByte[k] = (byte)(int)(paramLong >>> 32L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 24L & 0xFFL);
    i = j + 1;
    arrayOfByte[j] = (byte)(int)(paramLong >>> 16L & 0xFFL);
    j = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >>> 8L & 0xFFL);
    arrayOfByte[j] = (byte)(int)(paramLong & 0xFFL);
    segment.limit = j + 1;
    this.size += 8L;
    return this;
  }
  
  public Buffer writeLongLe(long paramLong) {
    return writeLong(Util.reverseBytesLong(paramLong));
  }
  
  public Buffer writeShort(int paramInt) {
    Segment segment = writableSegment(2);
    byte[] arrayOfByte = segment.data;
    int j = segment.limit;
    int i = j + 1;
    arrayOfByte[j] = (byte)(paramInt >>> 8 & 0xFF);
    arrayOfByte[i] = (byte)(paramInt & 0xFF);
    segment.limit = i + 1;
    this.size += 2L;
    return this;
  }
  
  public Buffer writeShortLe(int paramInt) {
    return writeShort(Util.reverseBytesShort((short)paramInt));
  }
  
  public Buffer writeString(String paramString, int paramInt1, int paramInt2, Charset paramCharset) {
    if (paramString != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= paramInt1) {
          byte[] arrayOfByte;
          if (paramInt2 <= paramString.length()) {
            if (paramCharset != null) {
              if (paramCharset.equals(Util.UTF_8))
                return writeUtf8(paramString); 
              arrayOfByte = paramString.substring(paramInt1, paramInt2).getBytes(paramCharset);
              return write(arrayOfByte, 0, arrayOfByte.length);
            } 
            throw new IllegalArgumentException("charset == null");
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("endIndex > string.length: ");
          stringBuilder2.append(paramInt2);
          stringBuilder2.append(" > ");
          stringBuilder2.append(arrayOfByte.length());
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("endIndex < beginIndex: ");
        stringBuilder1.append(paramInt2);
        stringBuilder1.append(" < ");
        stringBuilder1.append(paramInt1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("beginIndex < 0: ");
      stringBuilder.append(paramInt1);
      throw new IllegalAccessError(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("string == null");
  }
  
  public Buffer writeString(String paramString, Charset paramCharset) {
    return writeString(paramString, 0, paramString.length(), paramCharset);
  }
  
  public Buffer writeTo(OutputStream paramOutputStream) throws IOException {
    return writeTo(paramOutputStream, this.size);
  }
  
  public Buffer writeTo(OutputStream paramOutputStream, long paramLong) throws IOException {
    if (paramOutputStream != null) {
      Util.checkOffsetAndCount(this.size, 0L, paramLong);
      Segment segment = this.head;
      while (paramLong > 0L) {
        int i = (int)Math.min(paramLong, (segment.limit - segment.pos));
        paramOutputStream.write(segment.data, segment.pos, i);
        segment.pos += i;
        long l1 = this.size;
        long l2 = i;
        this.size = l1 - l2;
        l1 = paramLong - l2;
        paramLong = l1;
        if (segment.pos == segment.limit) {
          Segment segment1 = segment.pop();
          this.head = segment1;
          SegmentPool.recycle(segment);
          segment = segment1;
          paramLong = l1;
        } 
      } 
      return this;
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public Buffer writeUtf8(String paramString) {
    return writeUtf8(paramString, 0, paramString.length());
  }
  
  public Buffer writeUtf8(String paramString, int paramInt1, int paramInt2) {
    if (paramString != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= paramInt1) {
          if (paramInt2 <= paramString.length()) {
            while (paramInt1 < paramInt2) {
              char c = paramString.charAt(paramInt1);
              if (c < '') {
                Segment segment = writableSegment(1);
                byte[] arrayOfByte = segment.data;
                int j = segment.limit - paramInt1;
                int k = Math.min(paramInt2, 2048 - j);
                int i = paramInt1 + 1;
                arrayOfByte[paramInt1 + j] = (byte)c;
                for (paramInt1 = i; paramInt1 < k; paramInt1++) {
                  i = paramString.charAt(paramInt1);
                  if (i >= 128)
                    break; 
                  arrayOfByte[paramInt1 + j] = (byte)i;
                } 
                i = j + paramInt1 - segment.limit;
                segment.limit += i;
                this.size += i;
                continue;
              } 
              if (c < 'ࠀ') {
                writeByte(c >> 6 | 0xC0);
                writeByte(c & 0x3F | 0x80);
              } else if (c < '?' || c > '?') {
                writeByte(c >> 12 | 0xE0);
                writeByte(c >> 6 & 0x3F | 0x80);
                writeByte(c & 0x3F | 0x80);
              } else {
                int j = paramInt1 + 1;
                if (j < paramInt2) {
                  i = paramString.charAt(j);
                } else {
                  i = 0;
                } 
                if (c > '?' || i < 56320 || i > 57343) {
                  writeByte(63);
                  paramInt1 = j;
                  continue;
                } 
                int i = ((c & 0xFFFF27FF) << 10 | 0xFFFF23FF & i) + 65536;
                writeByte(i >> 18 | 0xF0);
                writeByte(i >> 12 & 0x3F | 0x80);
                writeByte(i >> 6 & 0x3F | 0x80);
                writeByte(i & 0x3F | 0x80);
                paramInt1 += 2;
                continue;
              } 
              paramInt1++;
            } 
            return this;
          } 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("endIndex > string.length: ");
          stringBuilder2.append(paramInt2);
          stringBuilder2.append(" > ");
          stringBuilder2.append(paramString.length());
          throw new IllegalArgumentException(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("endIndex < beginIndex: ");
        stringBuilder1.append(paramInt2);
        stringBuilder1.append(" < ");
        stringBuilder1.append(paramInt1);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("beginIndex < 0: ");
      stringBuilder.append(paramInt1);
      throw new IllegalAccessError(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("string == null");
  }
  
  public Buffer writeUtf8CodePoint(int paramInt) {
    if (paramInt < 128) {
      writeByte(paramInt);
    } else if (paramInt < 2048) {
      writeByte(paramInt >> 6 | 0xC0);
      writeByte(paramInt & 0x3F | 0x80);
    } else {
      if (paramInt < 65536) {
        if (paramInt < 55296 || paramInt > 57343) {
          writeByte(paramInt >> 12 | 0xE0);
          writeByte(paramInt >> 6 & 0x3F | 0x80);
          writeByte(paramInt & 0x3F | 0x80);
          return this;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Unexpected code point: ");
        stringBuilder1.append(Integer.toHexString(paramInt));
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      if (paramInt <= 1114111) {
        writeByte(paramInt >> 18 | 0xF0);
        writeByte(paramInt >> 12 & 0x3F | 0x80);
        writeByte(paramInt >> 6 & 0x3F | 0x80);
        writeByte(paramInt & 0x3F | 0x80);
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected code point: ");
      stringBuilder.append(Integer.toHexString(paramInt));
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return this;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\Buffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */