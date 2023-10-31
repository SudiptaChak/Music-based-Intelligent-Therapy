package okio;

import java.io.IOException;
import java.util.zip.Deflater;

public final class DeflaterSink implements Sink {
  private boolean closed;
  
  private final Deflater deflater;
  
  private final BufferedSink sink;
  
  DeflaterSink(BufferedSink paramBufferedSink, Deflater paramDeflater) {
    if (paramBufferedSink != null) {
      if (paramDeflater != null) {
        this.sink = paramBufferedSink;
        this.deflater = paramDeflater;
        return;
      } 
      throw new IllegalArgumentException("inflater == null");
    } 
    throw new IllegalArgumentException("source == null");
  }
  
  public DeflaterSink(Sink paramSink, Deflater paramDeflater) {
    this(Okio.buffer(paramSink), paramDeflater);
  }
  
  private void deflate(boolean paramBoolean) throws IOException {
    Buffer buffer = this.sink.buffer();
    while (true) {
      int i;
      Segment segment = buffer.writableSegment(1);
      if (paramBoolean) {
        i = this.deflater.deflate(segment.data, segment.limit, 2048 - segment.limit, 2);
      } else {
        i = this.deflater.deflate(segment.data, segment.limit, 2048 - segment.limit);
      } 
      if (i > 0) {
        segment.limit += i;
        buffer.size += i;
        this.sink.emitCompleteSegments();
        continue;
      } 
      if (this.deflater.needsInput()) {
        if (segment.pos == segment.limit) {
          buffer.head = segment.pop();
          SegmentPool.recycle(segment);
        } 
        return;
      } 
    } 
  }
  
  public void close() throws IOException {
    Exception exception1;
    if (this.closed)
      return; 
    Exception exception2 = null;
    try {
      finishDeflate();
    } finally {}
    try {
    
    } finally {
      Exception exception = null;
      exception1 = exception2;
    } 
    try {
    
    } finally {
      Exception exception = null;
      exception2 = exception1;
    } 
    this.closed = true;
    if (exception2 != null)
      Util.sneakyRethrow(exception2); 
  }
  
  void finishDeflate() throws IOException {
    this.deflater.finish();
    deflate(false);
  }
  
  public void flush() throws IOException {
    deflate(true);
    this.sink.flush();
  }
  
  public Timeout timeout() {
    return this.sink.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DeflaterSink(");
    stringBuilder.append(this.sink);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    Util.checkOffsetAndCount(paramBuffer.size, 0L, paramLong);
    while (paramLong > 0L) {
      Segment segment = paramBuffer.head;
      int i = (int)Math.min(paramLong, (segment.limit - segment.pos));
      this.deflater.setInput(segment.data, segment.pos, i);
      deflate(false);
      long l1 = paramBuffer.size;
      long l2 = i;
      paramBuffer.size = l1 - l2;
      segment.pos += i;
      if (segment.pos == segment.limit) {
        paramBuffer.head = segment.pop();
        SegmentPool.recycle(segment);
      } 
      paramLong -= l2;
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\DeflaterSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */