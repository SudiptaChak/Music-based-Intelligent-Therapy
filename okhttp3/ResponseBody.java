package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public abstract class ResponseBody implements Closeable {
  private Reader reader;
  
  private Charset charset() {
    Charset charset;
    MediaType mediaType = contentType();
    if (mediaType != null) {
      charset = mediaType.charset(Util.UTF_8);
    } else {
      charset = Util.UTF_8;
    } 
    return charset;
  }
  
  public static ResponseBody create(final MediaType contentType, final long contentLength, final BufferedSource content) {
    if (content != null)
      return new ResponseBody() {
          final BufferedSource val$content;
          
          final long val$contentLength;
          
          final MediaType val$contentType;
          
          public long contentLength() {
            return contentLength;
          }
          
          public MediaType contentType() {
            return contentType;
          }
          
          public BufferedSource source() {
            return content;
          }
        }; 
    throw new NullPointerException("source == null");
  }
  
  public static ResponseBody create(MediaType paramMediaType, String paramString) {
    Charset charset = Util.UTF_8;
    MediaType mediaType = paramMediaType;
    if (paramMediaType != null) {
      Charset charset1 = paramMediaType.charset();
      charset = charset1;
      mediaType = paramMediaType;
      if (charset1 == null) {
        charset = Util.UTF_8;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramMediaType);
        stringBuilder.append("; charset=utf-8");
        mediaType = MediaType.parse(stringBuilder.toString());
      } 
    } 
    Buffer buffer = (new Buffer()).writeString(paramString, charset);
    return create(mediaType, buffer.size(), (BufferedSource)buffer);
  }
  
  public static ResponseBody create(MediaType paramMediaType, byte[] paramArrayOfbyte) {
    Buffer buffer = (new Buffer()).write(paramArrayOfbyte);
    return create(paramMediaType, paramArrayOfbyte.length, (BufferedSource)buffer);
  }
  
  public final InputStream byteStream() {
    return source().inputStream();
  }
  
  public final byte[] bytes() throws IOException {
    long l = contentLength();
    if (l <= 2147483647L) {
      BufferedSource bufferedSource = source();
      try {
        byte[] arrayOfByte = bufferedSource.readByteArray();
        Util.closeQuietly((Closeable)bufferedSource);
        if (l == -1L || l == arrayOfByte.length)
          return arrayOfByte; 
        throw new IOException("Content-Length and stream length disagree");
      } finally {
        Util.closeQuietly((Closeable)bufferedSource);
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot buffer entire body for content length: ");
    stringBuilder.append(l);
    throw new IOException(stringBuilder.toString());
  }
  
  public final Reader charStream() {
    Reader reader = this.reader;
    if (reader == null) {
      reader = new InputStreamReader(byteStream(), charset());
      this.reader = reader;
    } 
    return reader;
  }
  
  public void close() {
    Util.closeQuietly((Closeable)source());
  }
  
  public abstract long contentLength();
  
  public abstract MediaType contentType();
  
  public abstract BufferedSource source();
  
  public final String string() throws IOException {
    return new String(bytes(), charset().name());
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\ResponseBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */