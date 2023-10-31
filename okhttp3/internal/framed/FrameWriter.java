package okhttp3.internal.framed;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import okio.Buffer;

public interface FrameWriter extends Closeable {
  void ackSettings(Settings paramSettings) throws IOException;
  
  void connectionPreface() throws IOException;
  
  void data(boolean paramBoolean, int paramInt1, Buffer paramBuffer, int paramInt2) throws IOException;
  
  void flush() throws IOException;
  
  void goAway(int paramInt, ErrorCode paramErrorCode, byte[] paramArrayOfbyte) throws IOException;
  
  void headers(int paramInt, List<Header> paramList) throws IOException;
  
  int maxDataLength();
  
  void ping(boolean paramBoolean, int paramInt1, int paramInt2) throws IOException;
  
  void pushPromise(int paramInt1, int paramInt2, List<Header> paramList) throws IOException;
  
  void rstStream(int paramInt, ErrorCode paramErrorCode) throws IOException;
  
  void settings(Settings paramSettings) throws IOException;
  
  void synReply(boolean paramBoolean, int paramInt, List<Header> paramList) throws IOException;
  
  void synStream(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, List<Header> paramList) throws IOException;
  
  void windowUpdate(int paramInt, long paramLong) throws IOException;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\FrameWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */