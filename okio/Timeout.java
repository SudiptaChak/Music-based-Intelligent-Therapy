package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
  public static final Timeout NONE = new Timeout() {
      public Timeout deadlineNanoTime(long param1Long) {
        return this;
      }
      
      public void throwIfReached() throws IOException {}
      
      public Timeout timeout(long param1Long, TimeUnit param1TimeUnit) {
        return this;
      }
    };
  
  private long deadlineNanoTime;
  
  private boolean hasDeadline;
  
  private long timeoutNanos;
  
  public Timeout clearDeadline() {
    this.hasDeadline = false;
    return this;
  }
  
  public Timeout clearTimeout() {
    this.timeoutNanos = 0L;
    return this;
  }
  
  public final Timeout deadline(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong > 0L) {
      if (paramTimeUnit != null)
        return deadlineNanoTime(System.nanoTime() + paramTimeUnit.toNanos(paramLong)); 
      throw new IllegalArgumentException("unit == null");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("duration <= 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long deadlineNanoTime() {
    if (this.hasDeadline)
      return this.deadlineNanoTime; 
    throw new IllegalStateException("No deadline");
  }
  
  public Timeout deadlineNanoTime(long paramLong) {
    this.hasDeadline = true;
    this.deadlineNanoTime = paramLong;
    return this;
  }
  
  public boolean hasDeadline() {
    return this.hasDeadline;
  }
  
  public void throwIfReached() throws IOException {
    if (!Thread.interrupted()) {
      if (!this.hasDeadline || this.deadlineNanoTime - System.nanoTime() > 0L)
        return; 
      throw new InterruptedIOException("deadline reached");
    } 
    throw new InterruptedIOException("thread interrupted");
  }
  
  public Timeout timeout(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong >= 0L) {
      if (paramTimeUnit != null) {
        this.timeoutNanos = paramTimeUnit.toNanos(paramLong);
        return this;
      } 
      throw new IllegalArgumentException("unit == null");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("timeout < 0: ");
    stringBuilder.append(paramLong);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public long timeoutNanos() {
    return this.timeoutNanos;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\Timeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */