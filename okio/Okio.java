package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Okio {
  private static final Logger logger = Logger.getLogger(Okio.class.getName());
  
  public static Sink appendingSink(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return sink(new FileOutputStream(paramFile, true)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public static BufferedSink buffer(Sink paramSink) {
    if (paramSink != null)
      return new RealBufferedSink(paramSink); 
    throw new IllegalArgumentException("sink == null");
  }
  
  public static BufferedSource buffer(Source paramSource) {
    if (paramSource != null)
      return new RealBufferedSource(paramSource); 
    throw new IllegalArgumentException("source == null");
  }
  
  public static Sink sink(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return sink(new FileOutputStream(paramFile)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public static Sink sink(OutputStream paramOutputStream) {
    return sink(paramOutputStream, new Timeout());
  }
  
  private static Sink sink(final OutputStream out, final Timeout timeout) {
    if (out != null) {
      if (timeout != null)
        return new Sink() {
            final OutputStream val$out;
            
            final Timeout val$timeout;
            
            public void close() throws IOException {
              out.close();
            }
            
            public void flush() throws IOException {
              out.flush();
            }
            
            public Timeout timeout() {
              return timeout;
            }
            
            public String toString() {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("sink(");
              stringBuilder.append(out);
              stringBuilder.append(")");
              return stringBuilder.toString();
            }
            
            public void write(Buffer param1Buffer, long param1Long) throws IOException {
              Util.checkOffsetAndCount(param1Buffer.size, 0L, param1Long);
              while (param1Long > 0L) {
                timeout.throwIfReached();
                Segment segment = param1Buffer.head;
                int i = (int)Math.min(param1Long, (segment.limit - segment.pos));
                out.write(segment.data, segment.pos, i);
                segment.pos += i;
                long l2 = i;
                long l1 = param1Long - l2;
                param1Buffer.size -= l2;
                param1Long = l1;
                if (segment.pos == segment.limit) {
                  param1Buffer.head = segment.pop();
                  SegmentPool.recycle(segment);
                  param1Long = l1;
                } 
              } 
            }
          }; 
      throw new IllegalArgumentException("timeout == null");
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  public static Sink sink(Socket paramSocket) throws IOException {
    if (paramSocket != null) {
      AsyncTimeout asyncTimeout = timeout(paramSocket);
      return asyncTimeout.sink(sink(paramSocket.getOutputStream(), asyncTimeout));
    } 
    throw new IllegalArgumentException("socket == null");
  }
  
  public static Sink sink(Path paramPath, OpenOption... paramVarArgs) throws IOException {
    if (paramPath != null)
      return sink(Files.newOutputStream(paramPath, paramVarArgs)); 
    throw new IllegalArgumentException("path == null");
  }
  
  public static Source source(File paramFile) throws FileNotFoundException {
    if (paramFile != null)
      return source(new FileInputStream(paramFile)); 
    throw new IllegalArgumentException("file == null");
  }
  
  public static Source source(InputStream paramInputStream) {
    return source(paramInputStream, new Timeout());
  }
  
  private static Source source(final InputStream in, final Timeout timeout) {
    if (in != null) {
      if (timeout != null)
        return new Source() {
            final InputStream val$in;
            
            final Timeout val$timeout;
            
            public void close() throws IOException {
              in.close();
            }
            
            public long read(Buffer param1Buffer, long param1Long) throws IOException {
              int i = param1Long cmp 0L;
              if (i >= 0) {
                if (i == 0)
                  return 0L; 
                timeout.throwIfReached();
                Segment segment = param1Buffer.writableSegment(1);
                i = (int)Math.min(param1Long, (2048 - segment.limit));
                i = in.read(segment.data, segment.limit, i);
                if (i == -1)
                  return -1L; 
                segment.limit += i;
                param1Long = param1Buffer.size;
                long l = i;
                param1Buffer.size = param1Long + l;
                return l;
              } 
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("byteCount < 0: ");
              stringBuilder.append(param1Long);
              throw new IllegalArgumentException(stringBuilder.toString());
            }
            
            public Timeout timeout() {
              return timeout;
            }
            
            public String toString() {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("source(");
              stringBuilder.append(in);
              stringBuilder.append(")");
              return stringBuilder.toString();
            }
          }; 
      throw new IllegalArgumentException("timeout == null");
    } 
    throw new IllegalArgumentException("in == null");
  }
  
  public static Source source(Socket paramSocket) throws IOException {
    if (paramSocket != null) {
      AsyncTimeout asyncTimeout = timeout(paramSocket);
      return asyncTimeout.source(source(paramSocket.getInputStream(), asyncTimeout));
    } 
    throw new IllegalArgumentException("socket == null");
  }
  
  public static Source source(Path paramPath, OpenOption... paramVarArgs) throws IOException {
    if (paramPath != null)
      return source(Files.newInputStream(paramPath, paramVarArgs)); 
    throw new IllegalArgumentException("path == null");
  }
  
  private static AsyncTimeout timeout(final Socket socket) {
    return new AsyncTimeout() {
        final Socket val$socket;
        
        protected IOException newTimeoutException(IOException param1IOException) {
          SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
          if (param1IOException != null)
            socketTimeoutException.initCause(param1IOException); 
          return socketTimeoutException;
        }
        
        protected void timedOut() {
          try {
            socket.close();
          } catch (Exception exception) {
            Logger logger = Okio.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to close timed out socket ");
            stringBuilder.append(socket);
            logger.log(level, stringBuilder.toString(), exception);
          } catch (AssertionError assertionError) {
            if (assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed")) {
              Logger logger = Okio.logger;
              Level level = Level.WARNING;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Failed to close timed out socket ");
              stringBuilder.append(socket);
              logger.log(level, stringBuilder.toString(), assertionError);
            } else {
              throw assertionError;
            } 
          } 
        }
      };
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\Okio.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */