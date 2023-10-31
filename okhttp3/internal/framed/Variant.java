package okhttp3.internal.framed;

import okhttp3.Protocol;
import okio.BufferedSink;
import okio.BufferedSource;

public interface Variant {
  Protocol getProtocol();
  
  FrameReader newReader(BufferedSource paramBufferedSource, boolean paramBoolean);
  
  FrameWriter newWriter(BufferedSink paramBufferedSink, boolean paramBoolean);
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\Variant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */