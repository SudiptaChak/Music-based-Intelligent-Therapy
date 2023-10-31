package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface BufferedSink extends Sink {
  Buffer buffer();
  
  BufferedSink emit() throws IOException;
  
  BufferedSink emitCompleteSegments() throws IOException;
  
  OutputStream outputStream();
  
  BufferedSink write(ByteString paramByteString) throws IOException;
  
  BufferedSink write(Source paramSource, long paramLong) throws IOException;
  
  BufferedSink write(byte[] paramArrayOfbyte) throws IOException;
  
  BufferedSink write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  long writeAll(Source paramSource) throws IOException;
  
  BufferedSink writeByte(int paramInt) throws IOException;
  
  BufferedSink writeDecimalLong(long paramLong) throws IOException;
  
  BufferedSink writeHexadecimalUnsignedLong(long paramLong) throws IOException;
  
  BufferedSink writeInt(int paramInt) throws IOException;
  
  BufferedSink writeIntLe(int paramInt) throws IOException;
  
  BufferedSink writeLong(long paramLong) throws IOException;
  
  BufferedSink writeLongLe(long paramLong) throws IOException;
  
  BufferedSink writeShort(int paramInt) throws IOException;
  
  BufferedSink writeShortLe(int paramInt) throws IOException;
  
  BufferedSink writeString(String paramString, int paramInt1, int paramInt2, Charset paramCharset) throws IOException;
  
  BufferedSink writeString(String paramString, Charset paramCharset) throws IOException;
  
  BufferedSink writeUtf8(String paramString) throws IOException;
  
  BufferedSink writeUtf8(String paramString, int paramInt1, int paramInt2) throws IOException;
  
  BufferedSink writeUtf8CodePoint(int paramInt) throws IOException;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\BufferedSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */