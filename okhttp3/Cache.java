package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.internal.DiskLruCache;
import okhttp3.internal.InternalCache;
import okhttp3.internal.Util;
import okhttp3.internal.http.CacheRequest;
import okhttp3.internal.http.CacheStrategy;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache implements Closeable, Flushable {
  private static final int ENTRY_BODY = 1;
  
  private static final int ENTRY_COUNT = 2;
  
  private static final int ENTRY_METADATA = 0;
  
  private static final int VERSION = 201105;
  
  private final DiskLruCache cache;
  
  private int hitCount;
  
  final InternalCache internalCache = new InternalCache() {
      final Cache this$0;
      
      public Response get(Request param1Request) throws IOException {
        return Cache.this.get(param1Request);
      }
      
      public CacheRequest put(Response param1Response) throws IOException {
        return Cache.this.put(param1Response);
      }
      
      public void remove(Request param1Request) throws IOException {
        Cache.this.remove(param1Request);
      }
      
      public void trackConditionalCacheHit() {
        Cache.this.trackConditionalCacheHit();
      }
      
      public void trackResponse(CacheStrategy param1CacheStrategy) {
        Cache.this.trackResponse(param1CacheStrategy);
      }
      
      public void update(Response param1Response1, Response param1Response2) throws IOException {
        Cache.this.update(param1Response1, param1Response2);
      }
    };
  
  private int networkCount;
  
  private int requestCount;
  
  private int writeAbortCount;
  
  private int writeSuccessCount;
  
  public Cache(File paramFile, long paramLong) {
    this(paramFile, paramLong, FileSystem.SYSTEM);
  }
  
  Cache(File paramFile, long paramLong, FileSystem paramFileSystem) {
    this.cache = DiskLruCache.create(paramFileSystem, paramFile, 201105, 2, paramLong);
  }
  
  private void abortQuietly(DiskLruCache.Editor paramEditor) {
    if (paramEditor != null)
      try {
        paramEditor.abort();
      } catch (IOException iOException) {} 
  }
  
  private CacheRequest put(Response paramResponse) throws IOException {
    String str = paramResponse.request().method();
    if (HttpMethod.invalidatesCache(paramResponse.request().method())) {
      try {
        remove(paramResponse.request());
      } catch (IOException null) {}
      return null;
    } 
    if (!str.equals("GET"))
      return null; 
    if (OkHeaders.hasVaryAll((Response)iOException))
      return null; 
    Entry entry = new Entry((Response)iOException);
    try {
      DiskLruCache.Editor editor = this.cache.edit(urlToKey(iOException.request()));
      if (editor == null)
        return null; 
      try {
        entry.writeTo(editor);
        return new CacheRequestImpl(editor);
      } catch (IOException iOException1) {}
    } catch (IOException iOException) {
      iOException = null;
    } 
    abortQuietly((DiskLruCache.Editor)iOException);
    return null;
  }
  
  private static int readInt(BufferedSource paramBufferedSource) throws IOException {
    try {
      long l = paramBufferedSource.readDecimalLong();
      String str = paramBufferedSource.readUtf8LineStrict();
      if (l >= 0L && l <= 2147483647L && str.isEmpty())
        return (int)l; 
      IOException iOException = new IOException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("expected an int but was \"");
      stringBuilder.append(l);
      stringBuilder.append(str);
      stringBuilder.append("\"");
      this(stringBuilder.toString());
      throw iOException;
    } catch (NumberFormatException numberFormatException) {
      throw new IOException(numberFormatException.getMessage());
    } 
  }
  
  private void remove(Request paramRequest) throws IOException {
    this.cache.remove(urlToKey(paramRequest));
  }
  
  private void trackConditionalCacheHit() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield hitCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield hitCount : I
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  private void trackResponse(CacheStrategy paramCacheStrategy) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield requestCount : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield requestCount : I
    //   12: aload_1
    //   13: getfield networkRequest : Lokhttp3/Request;
    //   16: ifnull -> 32
    //   19: aload_0
    //   20: aload_0
    //   21: getfield networkCount : I
    //   24: iconst_1
    //   25: iadd
    //   26: putfield networkCount : I
    //   29: goto -> 49
    //   32: aload_1
    //   33: getfield cacheResponse : Lokhttp3/Response;
    //   36: ifnull -> 49
    //   39: aload_0
    //   40: aload_0
    //   41: getfield hitCount : I
    //   44: iconst_1
    //   45: iadd
    //   46: putfield hitCount : I
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	52	finally
    //   32	49	52	finally
  }
  
  private void update(Response paramResponse1, Response paramResponse2) {
    Entry entry = new Entry(paramResponse2);
    DiskLruCache.Snapshot snapshot = ((CacheResponseBody)paramResponse1.body()).snapshot;
    try {
      DiskLruCache.Editor editor = snapshot.edit();
      if (editor != null) {
        try {
          entry.writeTo(editor);
          editor.commit();
          return;
        } catch (IOException iOException1) {}
      } else {
        return;
      } 
    } catch (IOException iOException) {
      iOException = null;
    } 
    abortQuietly((DiskLruCache.Editor)iOException);
  }
  
  private static String urlToKey(Request paramRequest) {
    return Util.md5Hex(paramRequest.url().toString());
  }
  
  public void close() throws IOException {
    this.cache.close();
  }
  
  public void delete() throws IOException {
    this.cache.delete();
  }
  
  public File directory() {
    return this.cache.getDirectory();
  }
  
  public void evictAll() throws IOException {
    this.cache.evictAll();
  }
  
  public void flush() throws IOException {
    this.cache.flush();
  }
  
  Response get(Request paramRequest) {
    String str = urlToKey(paramRequest);
    try {
      Response response;
      DiskLruCache.Snapshot snapshot = this.cache.get(str);
      if (snapshot == null)
        return null; 
      try {
        Entry entry = new Entry(snapshot.getSource(0));
        response = entry.response(snapshot);
        if (!entry.matches(paramRequest, response)) {
          Util.closeQuietly(response.body());
          return null;
        } 
        return response;
      } catch (IOException iOException) {
        Util.closeQuietly((Closeable)response);
      } 
    } catch (IOException iOException) {}
    return null;
  }
  
  public int hitCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hitCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void initialize() throws IOException {
    this.cache.initialize();
  }
  
  public boolean isClosed() {
    return this.cache.isClosed();
  }
  
  public long maxSize() {
    return this.cache.getMaxSize();
  }
  
  public int networkCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield networkCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int requestCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield requestCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public long size() throws IOException {
    return this.cache.size();
  }
  
  public Iterator<String> urls() throws IOException {
    return new Iterator<String>() {
        boolean canRemove;
        
        final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
        
        String nextUrl;
        
        final Cache this$0;
        
        public boolean hasNext() {
          if (this.nextUrl != null)
            return true; 
          this.canRemove = false;
          while (this.delegate.hasNext()) {
            DiskLruCache.Snapshot snapshot = this.delegate.next();
            try {
              this.nextUrl = Okio.buffer(snapshot.getSource(0)).readUtf8LineStrict();
              return true;
            } catch (IOException iOException) {
            
            } finally {
              snapshot.close();
            } 
          } 
          return false;
        }
        
        public String next() {
          if (hasNext()) {
            String str = this.nextUrl;
            this.nextUrl = null;
            this.canRemove = true;
            return str;
          } 
          throw new NoSuchElementException();
        }
        
        public void remove() {
          if (this.canRemove) {
            this.delegate.remove();
            return;
          } 
          throw new IllegalStateException("remove() before next()");
        }
      };
  }
  
  public int writeAbortCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeAbortCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public int writeSuccessCount() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield writeSuccessCount : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  private final class CacheRequestImpl implements CacheRequest {
    private Sink body;
    
    private Sink cacheOut;
    
    private boolean done;
    
    private final DiskLruCache.Editor editor;
    
    final Cache this$0;
    
    public CacheRequestImpl(final DiskLruCache.Editor editor) throws IOException {
      this.editor = editor;
      this.cacheOut = editor.newSink(1);
      this.body = (Sink)new ForwardingSink(this.cacheOut) {
          final Cache.CacheRequestImpl this$1;
          
          final DiskLruCache.Editor val$editor;
          
          final Cache val$this$0;
          
          public void close() throws IOException {
            synchronized (Cache.this) {
              if (Cache.CacheRequestImpl.this.done)
                return; 
              Cache.CacheRequestImpl.access$702(Cache.CacheRequestImpl.this, true);
              Cache.access$808(Cache.this);
              super.close();
              editor.commit();
              return;
            } 
          }
        };
    }
    
    public void abort() {
      synchronized (Cache.this) {
        if (this.done)
          return; 
        this.done = true;
        Cache.access$908(Cache.this);
        Util.closeQuietly((Closeable)this.cacheOut);
        try {
          this.editor.abort();
        } catch (IOException iOException) {}
      } 
    }
    
    public Sink body() {
      return this.body;
    }
  }
  
  class null extends ForwardingSink {
    final Cache.CacheRequestImpl this$1;
    
    final DiskLruCache.Editor val$editor;
    
    final Cache val$this$0;
    
    null(Sink param1Sink) {
      super(param1Sink);
    }
    
    public void close() throws IOException {
      synchronized (Cache.this) {
        if (this.this$1.done)
          return; 
        Cache.CacheRequestImpl.access$702(this.this$1, true);
        Cache.access$808(Cache.this);
        super.close();
        editor.commit();
        return;
      } 
    }
  }
  
  private static class CacheResponseBody extends ResponseBody {
    private final BufferedSource bodySource;
    
    private final String contentLength;
    
    private final String contentType;
    
    private final DiskLruCache.Snapshot snapshot;
    
    public CacheResponseBody(final DiskLruCache.Snapshot snapshot, String param1String1, String param1String2) {
      this.snapshot = snapshot;
      this.contentType = param1String1;
      this.contentLength = param1String2;
      this.bodySource = Okio.buffer((Source)new ForwardingSource(snapshot.getSource(1)) {
            final Cache.CacheResponseBody this$0;
            
            final DiskLruCache.Snapshot val$snapshot;
            
            public void close() throws IOException {
              snapshot.close();
              super.close();
            }
          });
    }
    
    public long contentLength() {
      long l2 = -1L;
      long l1 = l2;
      try {
        if (this.contentLength != null)
          l1 = Long.parseLong(this.contentLength); 
      } catch (NumberFormatException numberFormatException) {
        l1 = l2;
      } 
      return l1;
    }
    
    public MediaType contentType() {
      String str = this.contentType;
      if (str != null) {
        MediaType mediaType = MediaType.parse(str);
      } else {
        str = null;
      } 
      return (MediaType)str;
    }
    
    public BufferedSource source() {
      return this.bodySource;
    }
  }
  
  class null extends ForwardingSource {
    final Cache.CacheResponseBody this$0;
    
    final DiskLruCache.Snapshot val$snapshot;
    
    null(Source param1Source) {
      super(param1Source);
    }
    
    public void close() throws IOException {
      snapshot.close();
      super.close();
    }
  }
  
  private static final class Entry {
    private final int code;
    
    private final Handshake handshake;
    
    private final String message;
    
    private final Protocol protocol;
    
    private final String requestMethod;
    
    private final Headers responseHeaders;
    
    private final String url;
    
    private final Headers varyHeaders;
    
    public Entry(Response param1Response) {
      this.url = param1Response.request().url().toString();
      this.varyHeaders = OkHeaders.varyHeaders(param1Response);
      this.requestMethod = param1Response.request().method();
      this.protocol = param1Response.protocol();
      this.code = param1Response.code();
      this.message = param1Response.message();
      this.responseHeaders = param1Response.headers();
      this.handshake = param1Response.handshake();
    }
    
    public Entry(Source param1Source) throws IOException {
      try {
        BufferedSource bufferedSource = Okio.buffer(param1Source);
        this.url = bufferedSource.readUtf8LineStrict();
        this.requestMethod = bufferedSource.readUtf8LineStrict();
        Headers.Builder builder2 = new Headers.Builder();
        this();
        int i = Cache.readInt(bufferedSource);
        boolean bool = false;
        byte b;
        for (b = 0; b < i; b++)
          builder2.addLenient(bufferedSource.readUtf8LineStrict()); 
        this.varyHeaders = builder2.build();
        StatusLine statusLine = StatusLine.parse(bufferedSource.readUtf8LineStrict());
        this.protocol = statusLine.protocol;
        this.code = statusLine.code;
        this.message = statusLine.message;
        Headers.Builder builder1 = new Headers.Builder();
        this();
        i = Cache.readInt(bufferedSource);
        for (b = bool; b < i; b++)
          builder1.addLenient(bufferedSource.readUtf8LineStrict()); 
        this.responseHeaders = builder1.build();
        boolean bool1 = isHttps();
        builder1 = null;
        if (bool1) {
          List<Certificate> list;
          String str = bufferedSource.readUtf8LineStrict();
          if (str.length() <= 0) {
            TlsVersion tlsVersion;
            CipherSuite cipherSuite = CipherSuite.forJavaName(bufferedSource.readUtf8LineStrict());
            List<Certificate> list1 = readCertificateList(bufferedSource);
            list = readCertificateList(bufferedSource);
            if (!bufferedSource.exhausted())
              tlsVersion = TlsVersion.forJavaName(bufferedSource.readUtf8LineStrict()); 
            this.handshake = Handshake.get(tlsVersion, cipherSuite, list1, list);
          } else {
            IOException iOException = new IOException();
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("expected \"\" but was \"");
            stringBuilder.append((String)list);
            stringBuilder.append("\"");
            this(stringBuilder.toString());
            throw iOException;
          } 
        } else {
          this.handshake = null;
        } 
        return;
      } finally {
        param1Source.close();
      } 
    }
    
    private boolean isHttps() {
      return this.url.startsWith("https://");
    }
    
    private List<Certificate> readCertificateList(BufferedSource param1BufferedSource) throws IOException {
      int i = Cache.readInt(param1BufferedSource);
      if (i == -1)
        return Collections.emptyList(); 
      try {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ArrayList<Certificate> arrayList = new ArrayList();
        this(i);
        for (byte b = 0; b < i; b++) {
          String str = param1BufferedSource.readUtf8LineStrict();
          Buffer buffer = new Buffer();
          this();
          buffer.write(ByteString.decodeBase64(str));
          arrayList.add(certificateFactory.generateCertificate(buffer.inputStream()));
        } 
        return arrayList;
      } catch (CertificateException certificateException) {
        throw new IOException(certificateException.getMessage());
      } 
    }
    
    private void writeCertList(BufferedSink param1BufferedSink, List<Certificate> param1List) throws IOException {
      try {
        param1BufferedSink.writeDecimalLong(param1List.size());
        param1BufferedSink.writeByte(10);
        byte b = 0;
        int i = param1List.size();
        while (b < i) {
          param1BufferedSink.writeUtf8(ByteString.of(((Certificate)param1List.get(b)).getEncoded()).base64());
          param1BufferedSink.writeByte(10);
          b++;
        } 
        return;
      } catch (CertificateEncodingException certificateEncodingException) {
        throw new IOException(certificateEncodingException.getMessage());
      } 
    }
    
    public boolean matches(Request param1Request, Response param1Response) {
      boolean bool;
      if (this.url.equals(param1Request.url().toString()) && this.requestMethod.equals(param1Request.method()) && OkHeaders.varyMatches(param1Response, this.varyHeaders, param1Request)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Response response(DiskLruCache.Snapshot param1Snapshot) {
      String str2 = this.responseHeaders.get("Content-Type");
      String str1 = this.responseHeaders.get("Content-Length");
      Request request = (new Request.Builder()).url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
      return (new Response.Builder()).request(request).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new Cache.CacheResponseBody(param1Snapshot, str2, str1)).handshake(this.handshake).build();
    }
    
    public void writeTo(DiskLruCache.Editor param1Editor) throws IOException {
      boolean bool = false;
      BufferedSink bufferedSink = Okio.buffer(param1Editor.newSink(0));
      bufferedSink.writeUtf8(this.url);
      bufferedSink.writeByte(10);
      bufferedSink.writeUtf8(this.requestMethod);
      bufferedSink.writeByte(10);
      bufferedSink.writeDecimalLong(this.varyHeaders.size());
      bufferedSink.writeByte(10);
      int i = this.varyHeaders.size();
      byte b;
      for (b = 0; b < i; b++) {
        bufferedSink.writeUtf8(this.varyHeaders.name(b));
        bufferedSink.writeUtf8(": ");
        bufferedSink.writeUtf8(this.varyHeaders.value(b));
        bufferedSink.writeByte(10);
      } 
      bufferedSink.writeUtf8((new StatusLine(this.protocol, this.code, this.message)).toString());
      bufferedSink.writeByte(10);
      bufferedSink.writeDecimalLong(this.responseHeaders.size());
      bufferedSink.writeByte(10);
      i = this.responseHeaders.size();
      for (b = bool; b < i; b++) {
        bufferedSink.writeUtf8(this.responseHeaders.name(b));
        bufferedSink.writeUtf8(": ");
        bufferedSink.writeUtf8(this.responseHeaders.value(b));
        bufferedSink.writeByte(10);
      } 
      if (isHttps()) {
        bufferedSink.writeByte(10);
        bufferedSink.writeUtf8(this.handshake.cipherSuite().javaName());
        bufferedSink.writeByte(10);
        writeCertList(bufferedSink, this.handshake.peerCertificates());
        writeCertList(bufferedSink, this.handshake.localCertificates());
        if (this.handshake.tlsVersion() != null) {
          bufferedSink.writeUtf8(this.handshake.tlsVersion().javaName());
          bufferedSink.writeByte(10);
        } 
      } 
      bufferedSink.close();
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */