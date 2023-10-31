package okio;

import java.io.IOException;

public abstract class ForwardingSink implements Sink {
  private final Sink delegate;
  
  public ForwardingSink(Sink paramSink) {
    if (paramSink != null) {
      this.delegate = paramSink;
      return;
    } 
    throw new IllegalArgumentException("delegate == null");
  }
  
  public void close() throws IOException {
    this.delegate.close();
  }
  
  public final Sink delegate() {
    return this.delegate;
  }
  
  public void flush() throws IOException {
    this.delegate.flush();
  }
  
  public Timeout timeout() {
    return this.delegate.timeout();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append("(");
    stringBuilder.append(this.delegate.toString());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void write(Buffer paramBuffer, long paramLong) throws IOException {
    this.delegate.write(paramBuffer, paramLong);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\ForwardingSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */