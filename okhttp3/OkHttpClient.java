package okhttp3;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Internal;
import okhttp3.internal.InternalCache;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.Util;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.tls.TrustRootIndex;

public class OkHttpClient implements Cloneable, Call.Factory {
  private static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
  
  private static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableList((Object[])new Protocol[] { Protocol.HTTP_2, Protocol.SPDY_3, Protocol.HTTP_1_1 });
  
  final Authenticator authenticator;
  
  final Cache cache;
  
  final CertificatePinner certificatePinner;
  
  final int connectTimeout;
  
  final ConnectionPool connectionPool;
  
  final List<ConnectionSpec> connectionSpecs;
  
  final CookieJar cookieJar;
  
  final Dispatcher dispatcher;
  
  final Dns dns;
  
  final boolean followRedirects;
  
  final boolean followSslRedirects;
  
  final HostnameVerifier hostnameVerifier;
  
  final List<Interceptor> interceptors;
  
  final InternalCache internalCache;
  
  final List<Interceptor> networkInterceptors;
  
  final List<Protocol> protocols;
  
  final Proxy proxy;
  
  final Authenticator proxyAuthenticator;
  
  final ProxySelector proxySelector;
  
  final int readTimeout;
  
  final boolean retryOnConnectionFailure;
  
  final SocketFactory socketFactory;
  
  final SSLSocketFactory sslSocketFactory;
  
  final TrustRootIndex trustRootIndex;
  
  final int writeTimeout;
  
  static {
    DEFAULT_CONNECTION_SPECS = Util.immutableList((Object[])new ConnectionSpec[] { ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT });
    Internal.instance = new Internal() {
        public void addLenient(Headers.Builder param1Builder, String param1String) {
          param1Builder.addLenient(param1String);
        }
        
        public void addLenient(Headers.Builder param1Builder, String param1String1, String param1String2) {
          param1Builder.addLenient(param1String1, param1String2);
        }
        
        public void apply(ConnectionSpec param1ConnectionSpec, SSLSocket param1SSLSocket, boolean param1Boolean) {
          param1ConnectionSpec.apply(param1SSLSocket, param1Boolean);
        }
        
        public StreamAllocation callEngineGetStreamAllocation(Call param1Call) {
          return ((RealCall)param1Call).engine.streamAllocation;
        }
        
        public void callEnqueue(Call param1Call, Callback param1Callback, boolean param1Boolean) {
          ((RealCall)param1Call).enqueue(param1Callback, param1Boolean);
        }
        
        public boolean connectionBecameIdle(ConnectionPool param1ConnectionPool, RealConnection param1RealConnection) {
          return param1ConnectionPool.connectionBecameIdle(param1RealConnection);
        }
        
        public RealConnection get(ConnectionPool param1ConnectionPool, Address param1Address, StreamAllocation param1StreamAllocation) {
          return param1ConnectionPool.get(param1Address, param1StreamAllocation);
        }
        
        public HttpUrl getHttpUrlChecked(String param1String) throws MalformedURLException, UnknownHostException {
          return HttpUrl.getChecked(param1String);
        }
        
        public InternalCache internalCache(OkHttpClient param1OkHttpClient) {
          return param1OkHttpClient.internalCache();
        }
        
        public void put(ConnectionPool param1ConnectionPool, RealConnection param1RealConnection) {
          param1ConnectionPool.put(param1RealConnection);
        }
        
        public RouteDatabase routeDatabase(ConnectionPool param1ConnectionPool) {
          return param1ConnectionPool.routeDatabase;
        }
        
        public void setCache(OkHttpClient.Builder param1Builder, InternalCache param1InternalCache) {
          param1Builder.setInternalCache(param1InternalCache);
        }
      };
  }
  
  public OkHttpClient() {
    this(new Builder());
  }
  
  private OkHttpClient(Builder paramBuilder) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_0
    //   5: aload_1
    //   6: getfield dispatcher : Lokhttp3/Dispatcher;
    //   9: putfield dispatcher : Lokhttp3/Dispatcher;
    //   12: aload_0
    //   13: aload_1
    //   14: getfield proxy : Ljava/net/Proxy;
    //   17: putfield proxy : Ljava/net/Proxy;
    //   20: aload_0
    //   21: aload_1
    //   22: getfield protocols : Ljava/util/List;
    //   25: putfield protocols : Ljava/util/List;
    //   28: aload_0
    //   29: aload_1
    //   30: getfield connectionSpecs : Ljava/util/List;
    //   33: putfield connectionSpecs : Ljava/util/List;
    //   36: aload_0
    //   37: aload_1
    //   38: getfield interceptors : Ljava/util/List;
    //   41: invokestatic immutableList : (Ljava/util/List;)Ljava/util/List;
    //   44: putfield interceptors : Ljava/util/List;
    //   47: aload_0
    //   48: aload_1
    //   49: getfield networkInterceptors : Ljava/util/List;
    //   52: invokestatic immutableList : (Ljava/util/List;)Ljava/util/List;
    //   55: putfield networkInterceptors : Ljava/util/List;
    //   58: aload_0
    //   59: aload_1
    //   60: getfield proxySelector : Ljava/net/ProxySelector;
    //   63: putfield proxySelector : Ljava/net/ProxySelector;
    //   66: aload_0
    //   67: aload_1
    //   68: getfield cookieJar : Lokhttp3/CookieJar;
    //   71: putfield cookieJar : Lokhttp3/CookieJar;
    //   74: aload_0
    //   75: aload_1
    //   76: getfield cache : Lokhttp3/Cache;
    //   79: putfield cache : Lokhttp3/Cache;
    //   82: aload_0
    //   83: aload_1
    //   84: getfield internalCache : Lokhttp3/internal/InternalCache;
    //   87: putfield internalCache : Lokhttp3/internal/InternalCache;
    //   90: aload_0
    //   91: aload_1
    //   92: getfield socketFactory : Ljavax/net/SocketFactory;
    //   95: putfield socketFactory : Ljavax/net/SocketFactory;
    //   98: aload_0
    //   99: getfield connectionSpecs : Ljava/util/List;
    //   102: invokeinterface iterator : ()Ljava/util/Iterator;
    //   107: astore #4
    //   109: iconst_0
    //   110: istore_2
    //   111: aload #4
    //   113: invokeinterface hasNext : ()Z
    //   118: ifeq -> 148
    //   121: aload #4
    //   123: invokeinterface next : ()Ljava/lang/Object;
    //   128: checkcast okhttp3/ConnectionSpec
    //   131: astore_3
    //   132: iload_2
    //   133: ifne -> 143
    //   136: aload_3
    //   137: invokevirtual isTls : ()Z
    //   140: ifeq -> 109
    //   143: iconst_1
    //   144: istore_2
    //   145: goto -> 111
    //   148: aload_1
    //   149: getfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   152: ifnonnull -> 195
    //   155: iload_2
    //   156: ifne -> 162
    //   159: goto -> 195
    //   162: ldc 'TLS'
    //   164: invokestatic getInstance : (Ljava/lang/String;)Ljavax/net/ssl/SSLContext;
    //   167: astore_3
    //   168: aload_3
    //   169: aconst_null
    //   170: aconst_null
    //   171: aconst_null
    //   172: invokevirtual init : ([Ljavax/net/ssl/KeyManager;[Ljavax/net/ssl/TrustManager;Ljava/security/SecureRandom;)V
    //   175: aload_0
    //   176: aload_3
    //   177: invokevirtual getSocketFactory : ()Ljavax/net/ssl/SSLSocketFactory;
    //   180: putfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   183: goto -> 203
    //   186: astore_1
    //   187: new java/lang/AssertionError
    //   190: dup
    //   191: invokespecial <init> : ()V
    //   194: athrow
    //   195: aload_0
    //   196: aload_1
    //   197: getfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   200: putfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   203: aload_0
    //   204: getfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   207: ifnull -> 321
    //   210: aload_1
    //   211: getfield trustRootIndex : Lokhttp3/internal/tls/TrustRootIndex;
    //   214: ifnonnull -> 321
    //   217: invokestatic get : ()Lokhttp3/internal/Platform;
    //   220: aload_0
    //   221: getfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   224: invokevirtual trustManager : (Ljavax/net/ssl/SSLSocketFactory;)Ljavax/net/ssl/X509TrustManager;
    //   227: astore_3
    //   228: aload_3
    //   229: ifnull -> 267
    //   232: aload_0
    //   233: invokestatic get : ()Lokhttp3/internal/Platform;
    //   236: aload_3
    //   237: invokevirtual trustRootIndex : (Ljavax/net/ssl/X509TrustManager;)Lokhttp3/internal/tls/TrustRootIndex;
    //   240: putfield trustRootIndex : Lokhttp3/internal/tls/TrustRootIndex;
    //   243: aload_0
    //   244: aload_1
    //   245: getfield certificatePinner : Lokhttp3/CertificatePinner;
    //   248: invokevirtual newBuilder : ()Lokhttp3/CertificatePinner$Builder;
    //   251: aload_0
    //   252: getfield trustRootIndex : Lokhttp3/internal/tls/TrustRootIndex;
    //   255: invokevirtual trustRootIndex : (Lokhttp3/internal/tls/TrustRootIndex;)Lokhttp3/CertificatePinner$Builder;
    //   258: invokevirtual build : ()Lokhttp3/CertificatePinner;
    //   261: putfield certificatePinner : Lokhttp3/CertificatePinner;
    //   264: goto -> 337
    //   267: new java/lang/StringBuilder
    //   270: dup
    //   271: invokespecial <init> : ()V
    //   274: astore_1
    //   275: aload_1
    //   276: ldc 'Unable to extract the trust manager on '
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload_1
    //   283: invokestatic get : ()Lokhttp3/internal/Platform;
    //   286: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload_1
    //   291: ldc ', sslSocketFactory is '
    //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: pop
    //   297: aload_1
    //   298: aload_0
    //   299: getfield sslSocketFactory : Ljavax/net/ssl/SSLSocketFactory;
    //   302: invokevirtual getClass : ()Ljava/lang/Class;
    //   305: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: new java/lang/IllegalStateException
    //   312: dup
    //   313: aload_1
    //   314: invokevirtual toString : ()Ljava/lang/String;
    //   317: invokespecial <init> : (Ljava/lang/String;)V
    //   320: athrow
    //   321: aload_0
    //   322: aload_1
    //   323: getfield trustRootIndex : Lokhttp3/internal/tls/TrustRootIndex;
    //   326: putfield trustRootIndex : Lokhttp3/internal/tls/TrustRootIndex;
    //   329: aload_0
    //   330: aload_1
    //   331: getfield certificatePinner : Lokhttp3/CertificatePinner;
    //   334: putfield certificatePinner : Lokhttp3/CertificatePinner;
    //   337: aload_0
    //   338: aload_1
    //   339: getfield hostnameVerifier : Ljavax/net/ssl/HostnameVerifier;
    //   342: putfield hostnameVerifier : Ljavax/net/ssl/HostnameVerifier;
    //   345: aload_0
    //   346: aload_1
    //   347: getfield proxyAuthenticator : Lokhttp3/Authenticator;
    //   350: putfield proxyAuthenticator : Lokhttp3/Authenticator;
    //   353: aload_0
    //   354: aload_1
    //   355: getfield authenticator : Lokhttp3/Authenticator;
    //   358: putfield authenticator : Lokhttp3/Authenticator;
    //   361: aload_0
    //   362: aload_1
    //   363: getfield connectionPool : Lokhttp3/ConnectionPool;
    //   366: putfield connectionPool : Lokhttp3/ConnectionPool;
    //   369: aload_0
    //   370: aload_1
    //   371: getfield dns : Lokhttp3/Dns;
    //   374: putfield dns : Lokhttp3/Dns;
    //   377: aload_0
    //   378: aload_1
    //   379: getfield followSslRedirects : Z
    //   382: putfield followSslRedirects : Z
    //   385: aload_0
    //   386: aload_1
    //   387: getfield followRedirects : Z
    //   390: putfield followRedirects : Z
    //   393: aload_0
    //   394: aload_1
    //   395: getfield retryOnConnectionFailure : Z
    //   398: putfield retryOnConnectionFailure : Z
    //   401: aload_0
    //   402: aload_1
    //   403: getfield connectTimeout : I
    //   406: putfield connectTimeout : I
    //   409: aload_0
    //   410: aload_1
    //   411: getfield readTimeout : I
    //   414: putfield readTimeout : I
    //   417: aload_0
    //   418: aload_1
    //   419: getfield writeTimeout : I
    //   422: putfield writeTimeout : I
    //   425: return
    // Exception table:
    //   from	to	target	type
    //   162	183	186	java/security/GeneralSecurityException
  }
  
  public Authenticator authenticator() {
    return this.authenticator;
  }
  
  public Cache cache() {
    return this.cache;
  }
  
  public CertificatePinner certificatePinner() {
    return this.certificatePinner;
  }
  
  public int connectTimeoutMillis() {
    return this.connectTimeout;
  }
  
  public ConnectionPool connectionPool() {
    return this.connectionPool;
  }
  
  public List<ConnectionSpec> connectionSpecs() {
    return this.connectionSpecs;
  }
  
  public CookieJar cookieJar() {
    return this.cookieJar;
  }
  
  public Dispatcher dispatcher() {
    return this.dispatcher;
  }
  
  public Dns dns() {
    return this.dns;
  }
  
  public boolean followRedirects() {
    return this.followRedirects;
  }
  
  public boolean followSslRedirects() {
    return this.followSslRedirects;
  }
  
  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  public List<Interceptor> interceptors() {
    return this.interceptors;
  }
  
  InternalCache internalCache() {
    InternalCache internalCache;
    Cache cache = this.cache;
    if (cache != null) {
      internalCache = cache.internalCache;
    } else {
      internalCache = this.internalCache;
    } 
    return internalCache;
  }
  
  public List<Interceptor> networkInterceptors() {
    return this.networkInterceptors;
  }
  
  public Builder newBuilder() {
    return new Builder(this);
  }
  
  public Call newCall(Request paramRequest) {
    return new RealCall(this, paramRequest);
  }
  
  public List<Protocol> protocols() {
    return this.protocols;
  }
  
  public Proxy proxy() {
    return this.proxy;
  }
  
  public Authenticator proxyAuthenticator() {
    return this.proxyAuthenticator;
  }
  
  public ProxySelector proxySelector() {
    return this.proxySelector;
  }
  
  public int readTimeoutMillis() {
    return this.readTimeout;
  }
  
  public boolean retryOnConnectionFailure() {
    return this.retryOnConnectionFailure;
  }
  
  public SocketFactory socketFactory() {
    return this.socketFactory;
  }
  
  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public int writeTimeoutMillis() {
    return this.writeTimeout;
  }
  
  public static final class Builder {
    Authenticator authenticator;
    
    Cache cache;
    
    CertificatePinner certificatePinner;
    
    int connectTimeout;
    
    ConnectionPool connectionPool;
    
    List<ConnectionSpec> connectionSpecs;
    
    CookieJar cookieJar;
    
    Dispatcher dispatcher = new Dispatcher();
    
    Dns dns;
    
    boolean followRedirects;
    
    boolean followSslRedirects;
    
    HostnameVerifier hostnameVerifier;
    
    final List<Interceptor> interceptors = new ArrayList<Interceptor>();
    
    InternalCache internalCache;
    
    final List<Interceptor> networkInterceptors = new ArrayList<Interceptor>();
    
    List<Protocol> protocols;
    
    Proxy proxy;
    
    Authenticator proxyAuthenticator;
    
    ProxySelector proxySelector;
    
    int readTimeout;
    
    boolean retryOnConnectionFailure;
    
    SocketFactory socketFactory;
    
    SSLSocketFactory sslSocketFactory;
    
    TrustRootIndex trustRootIndex;
    
    int writeTimeout;
    
    public Builder() {
      this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
      this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
      this.proxySelector = ProxySelector.getDefault();
      this.cookieJar = CookieJar.NO_COOKIES;
      this.socketFactory = SocketFactory.getDefault();
      this.hostnameVerifier = (HostnameVerifier)OkHostnameVerifier.INSTANCE;
      this.certificatePinner = CertificatePinner.DEFAULT;
      this.proxyAuthenticator = Authenticator.NONE;
      this.authenticator = Authenticator.NONE;
      this.connectionPool = new ConnectionPool();
      this.dns = Dns.SYSTEM;
      this.followSslRedirects = true;
      this.followRedirects = true;
      this.retryOnConnectionFailure = true;
      this.connectTimeout = 10000;
      this.readTimeout = 10000;
      this.writeTimeout = 10000;
    }
    
    Builder(OkHttpClient param1OkHttpClient) {
      this.proxy = param1OkHttpClient.proxy;
      this.protocols = param1OkHttpClient.protocols;
      this.connectionSpecs = param1OkHttpClient.connectionSpecs;
      this.interceptors.addAll(param1OkHttpClient.interceptors);
      this.networkInterceptors.addAll(param1OkHttpClient.networkInterceptors);
      this.proxySelector = param1OkHttpClient.proxySelector;
      this.cookieJar = param1OkHttpClient.cookieJar;
      this.internalCache = param1OkHttpClient.internalCache;
      this.cache = param1OkHttpClient.cache;
      this.socketFactory = param1OkHttpClient.socketFactory;
      this.sslSocketFactory = param1OkHttpClient.sslSocketFactory;
      this.trustRootIndex = param1OkHttpClient.trustRootIndex;
      this.hostnameVerifier = param1OkHttpClient.hostnameVerifier;
      this.certificatePinner = param1OkHttpClient.certificatePinner;
      this.proxyAuthenticator = param1OkHttpClient.proxyAuthenticator;
      this.authenticator = param1OkHttpClient.authenticator;
      this.connectionPool = param1OkHttpClient.connectionPool;
      this.dns = param1OkHttpClient.dns;
      this.followSslRedirects = param1OkHttpClient.followSslRedirects;
      this.followRedirects = param1OkHttpClient.followRedirects;
      this.retryOnConnectionFailure = param1OkHttpClient.retryOnConnectionFailure;
      this.connectTimeout = param1OkHttpClient.connectTimeout;
      this.readTimeout = param1OkHttpClient.readTimeout;
      this.writeTimeout = param1OkHttpClient.writeTimeout;
    }
    
    public Builder addInterceptor(Interceptor param1Interceptor) {
      this.interceptors.add(param1Interceptor);
      return this;
    }
    
    public Builder addNetworkInterceptor(Interceptor param1Interceptor) {
      this.networkInterceptors.add(param1Interceptor);
      return this;
    }
    
    public Builder authenticator(Authenticator param1Authenticator) {
      if (param1Authenticator != null) {
        this.authenticator = param1Authenticator;
        return this;
      } 
      throw new NullPointerException("authenticator == null");
    }
    
    public OkHttpClient build() {
      return new OkHttpClient(this);
    }
    
    public Builder cache(Cache param1Cache) {
      this.cache = param1Cache;
      this.internalCache = null;
      return this;
    }
    
    public Builder certificatePinner(CertificatePinner param1CertificatePinner) {
      if (param1CertificatePinner != null) {
        this.certificatePinner = param1CertificatePinner;
        return this;
      } 
      throw new NullPointerException("certificatePinner == null");
    }
    
    public Builder connectTimeout(long param1Long, TimeUnit param1TimeUnit) {
      int i = param1Long cmp 0L;
      if (i >= 0) {
        if (param1TimeUnit != null) {
          param1Long = param1TimeUnit.toMillis(param1Long);
          if (param1Long <= 2147483647L) {
            if (param1Long != 0L || i <= 0) {
              this.connectTimeout = (int)param1Long;
              return this;
            } 
            throw new IllegalArgumentException("Timeout too small.");
          } 
          throw new IllegalArgumentException("Timeout too large.");
        } 
        throw new IllegalArgumentException("unit == null");
      } 
      throw new IllegalArgumentException("timeout < 0");
    }
    
    public Builder connectionPool(ConnectionPool param1ConnectionPool) {
      if (param1ConnectionPool != null) {
        this.connectionPool = param1ConnectionPool;
        return this;
      } 
      throw new NullPointerException("connectionPool == null");
    }
    
    public Builder connectionSpecs(List<ConnectionSpec> param1List) {
      this.connectionSpecs = Util.immutableList(param1List);
      return this;
    }
    
    public Builder cookieJar(CookieJar param1CookieJar) {
      if (param1CookieJar != null) {
        this.cookieJar = param1CookieJar;
        return this;
      } 
      throw new NullPointerException("cookieJar == null");
    }
    
    public Builder dispatcher(Dispatcher param1Dispatcher) {
      if (param1Dispatcher != null) {
        this.dispatcher = param1Dispatcher;
        return this;
      } 
      throw new IllegalArgumentException("dispatcher == null");
    }
    
    public Builder dns(Dns param1Dns) {
      if (param1Dns != null) {
        this.dns = param1Dns;
        return this;
      } 
      throw new NullPointerException("dns == null");
    }
    
    public Builder followRedirects(boolean param1Boolean) {
      this.followRedirects = param1Boolean;
      return this;
    }
    
    public Builder followSslRedirects(boolean param1Boolean) {
      this.followSslRedirects = param1Boolean;
      return this;
    }
    
    public Builder hostnameVerifier(HostnameVerifier param1HostnameVerifier) {
      if (param1HostnameVerifier != null) {
        this.hostnameVerifier = param1HostnameVerifier;
        return this;
      } 
      throw new NullPointerException("hostnameVerifier == null");
    }
    
    public List<Interceptor> interceptors() {
      return this.interceptors;
    }
    
    public List<Interceptor> networkInterceptors() {
      return this.networkInterceptors;
    }
    
    public Builder protocols(List<Protocol> param1List) {
      param1List = Util.immutableList(param1List);
      if (param1List.contains(Protocol.HTTP_1_1)) {
        if (!param1List.contains(Protocol.HTTP_1_0)) {
          if (!param1List.contains(null)) {
            this.protocols = Util.immutableList(param1List);
            return this;
          } 
          throw new IllegalArgumentException("protocols must not contain null");
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("protocols must not contain http/1.0: ");
        stringBuilder1.append(param1List);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("protocols doesn't contain http/1.1: ");
      stringBuilder.append(param1List);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder proxy(Proxy param1Proxy) {
      this.proxy = param1Proxy;
      return this;
    }
    
    public Builder proxyAuthenticator(Authenticator param1Authenticator) {
      if (param1Authenticator != null) {
        this.proxyAuthenticator = param1Authenticator;
        return this;
      } 
      throw new NullPointerException("proxyAuthenticator == null");
    }
    
    public Builder proxySelector(ProxySelector param1ProxySelector) {
      this.proxySelector = param1ProxySelector;
      return this;
    }
    
    public Builder readTimeout(long param1Long, TimeUnit param1TimeUnit) {
      int i = param1Long cmp 0L;
      if (i >= 0) {
        if (param1TimeUnit != null) {
          param1Long = param1TimeUnit.toMillis(param1Long);
          if (param1Long <= 2147483647L) {
            if (param1Long != 0L || i <= 0) {
              this.readTimeout = (int)param1Long;
              return this;
            } 
            throw new IllegalArgumentException("Timeout too small.");
          } 
          throw new IllegalArgumentException("Timeout too large.");
        } 
        throw new IllegalArgumentException("unit == null");
      } 
      throw new IllegalArgumentException("timeout < 0");
    }
    
    public Builder retryOnConnectionFailure(boolean param1Boolean) {
      this.retryOnConnectionFailure = param1Boolean;
      return this;
    }
    
    void setInternalCache(InternalCache param1InternalCache) {
      this.internalCache = param1InternalCache;
      this.cache = null;
    }
    
    public Builder socketFactory(SocketFactory param1SocketFactory) {
      if (param1SocketFactory != null) {
        this.socketFactory = param1SocketFactory;
        return this;
      } 
      throw new NullPointerException("socketFactory == null");
    }
    
    public Builder sslSocketFactory(SSLSocketFactory param1SSLSocketFactory) {
      if (param1SSLSocketFactory != null) {
        this.sslSocketFactory = param1SSLSocketFactory;
        this.trustRootIndex = null;
        return this;
      } 
      throw new NullPointerException("sslSocketFactory == null");
    }
    
    public Builder writeTimeout(long param1Long, TimeUnit param1TimeUnit) {
      int i = param1Long cmp 0L;
      if (i >= 0) {
        if (param1TimeUnit != null) {
          param1Long = param1TimeUnit.toMillis(param1Long);
          if (param1Long <= 2147483647L) {
            if (param1Long != 0L || i <= 0) {
              this.writeTimeout = (int)param1Long;
              return this;
            } 
            throw new IllegalArgumentException("Timeout too small.");
          } 
          throw new IllegalArgumentException("Timeout too large.");
        } 
        throw new IllegalArgumentException("unit == null");
      } 
      throw new IllegalArgumentException("timeout < 0");
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\OkHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */