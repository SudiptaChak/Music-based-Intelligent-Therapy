package okhttp3.internal;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;
import okhttp3.internal.tls.AndroidTrustRootIndex;
import okhttp3.internal.tls.RealTrustRootIndex;
import okhttp3.internal.tls.TrustRootIndex;
import okio.Buffer;

public class Platform {
  private static final Platform PLATFORM = findPlatform();
  
  static byte[] concatLengthPrefixed(List<Protocol> paramList) {
    Buffer buffer = new Buffer();
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      Protocol protocol = paramList.get(b);
      if (protocol != Protocol.HTTP_1_0) {
        buffer.writeByte(protocol.toString().length());
        buffer.writeUtf8(protocol.toString());
      } 
    } 
    return buffer.readByteArray();
  }
  
  private static Platform findPlatform() {
    // Byte code:
    //   0: ldc 'com.android.org.conscrypt.SSLParametersImpl'
    //   2: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   5: astore_1
    //   6: goto -> 19
    //   9: astore_0
    //   10: ldc 'org.apache.harmony.xnet.provider.jsse.SSLParametersImpl'
    //   12: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   15: astore_1
    //   16: goto -> 6
    //   19: new okhttp3/internal/OptionalMethod
    //   22: astore_3
    //   23: aload_3
    //   24: aconst_null
    //   25: ldc 'setUseSessionTickets'
    //   27: iconst_1
    //   28: anewarray java/lang/Class
    //   31: dup
    //   32: iconst_0
    //   33: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   36: aastore
    //   37: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
    //   40: new okhttp3/internal/OptionalMethod
    //   43: astore #4
    //   45: aload #4
    //   47: aconst_null
    //   48: ldc 'setHostname'
    //   50: iconst_1
    //   51: anewarray java/lang/Class
    //   54: dup
    //   55: iconst_0
    //   56: ldc java/lang/String
    //   58: aastore
    //   59: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
    //   62: ldc 'android.net.Network'
    //   64: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   67: pop
    //   68: new okhttp3/internal/OptionalMethod
    //   71: astore_0
    //   72: aload_0
    //   73: ldc [B
    //   75: ldc 'getAlpnSelectedProtocol'
    //   77: iconst_0
    //   78: anewarray java/lang/Class
    //   81: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
    //   84: new okhttp3/internal/OptionalMethod
    //   87: astore_2
    //   88: aload_2
    //   89: aconst_null
    //   90: ldc 'setAlpnProtocols'
    //   92: iconst_1
    //   93: anewarray java/lang/Class
    //   96: dup
    //   97: iconst_0
    //   98: ldc [B
    //   100: aastore
    //   101: invokespecial <init> : (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
    //   104: goto -> 112
    //   107: astore_0
    //   108: aconst_null
    //   109: astore_0
    //   110: aconst_null
    //   111: astore_2
    //   112: new okhttp3/internal/Platform$Android
    //   115: dup
    //   116: aload_1
    //   117: aload_3
    //   118: aload #4
    //   120: aload_0
    //   121: aload_2
    //   122: invokespecial <init> : (Ljava/lang/Class;Lokhttp3/internal/OptionalMethod;Lokhttp3/internal/OptionalMethod;Lokhttp3/internal/OptionalMethod;Lokhttp3/internal/OptionalMethod;)V
    //   125: astore_0
    //   126: aload_0
    //   127: areturn
    //   128: astore_0
    //   129: ldc 'org.eclipse.jetty.alpn.ALPN'
    //   131: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   134: astore_0
    //   135: new java/lang/StringBuilder
    //   138: astore_1
    //   139: aload_1
    //   140: invokespecial <init> : ()V
    //   143: aload_1
    //   144: ldc 'org.eclipse.jetty.alpn.ALPN'
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_1
    //   151: ldc '$Provider'
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload_1
    //   158: invokevirtual toString : ()Ljava/lang/String;
    //   161: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   164: astore_1
    //   165: new java/lang/StringBuilder
    //   168: astore_2
    //   169: aload_2
    //   170: invokespecial <init> : ()V
    //   173: aload_2
    //   174: ldc 'org.eclipse.jetty.alpn.ALPN'
    //   176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload_2
    //   181: ldc '$ClientProvider'
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_2
    //   188: invokevirtual toString : ()Ljava/lang/String;
    //   191: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   194: astore_2
    //   195: new java/lang/StringBuilder
    //   198: astore_3
    //   199: aload_3
    //   200: invokespecial <init> : ()V
    //   203: aload_3
    //   204: ldc 'org.eclipse.jetty.alpn.ALPN'
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_3
    //   211: ldc '$ServerProvider'
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_3
    //   218: invokevirtual toString : ()Ljava/lang/String;
    //   221: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   224: astore_3
    //   225: new okhttp3/internal/Platform$JdkWithJettyBootPlatform
    //   228: dup
    //   229: aload_0
    //   230: ldc 'put'
    //   232: iconst_2
    //   233: anewarray java/lang/Class
    //   236: dup
    //   237: iconst_0
    //   238: ldc javax/net/ssl/SSLSocket
    //   240: aastore
    //   241: dup
    //   242: iconst_1
    //   243: aload_1
    //   244: aastore
    //   245: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   248: aload_0
    //   249: ldc 'get'
    //   251: iconst_1
    //   252: anewarray java/lang/Class
    //   255: dup
    //   256: iconst_0
    //   257: ldc javax/net/ssl/SSLSocket
    //   259: aastore
    //   260: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   263: aload_0
    //   264: ldc 'remove'
    //   266: iconst_1
    //   267: anewarray java/lang/Class
    //   270: dup
    //   271: iconst_0
    //   272: ldc javax/net/ssl/SSLSocket
    //   274: aastore
    //   275: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   278: aload_2
    //   279: aload_3
    //   280: invokespecial <init> : (Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/Class;Ljava/lang/Class;)V
    //   283: astore_0
    //   284: aload_0
    //   285: areturn
    //   286: astore_0
    //   287: new okhttp3/internal/Platform
    //   290: dup
    //   291: invokespecial <init> : ()V
    //   294: areturn
    //   295: astore_2
    //   296: goto -> 110
    // Exception table:
    //   from	to	target	type
    //   0	6	9	java/lang/ClassNotFoundException
    //   10	16	128	java/lang/ClassNotFoundException
    //   19	62	128	java/lang/ClassNotFoundException
    //   62	84	107	java/lang/ClassNotFoundException
    //   84	104	295	java/lang/ClassNotFoundException
    //   112	126	128	java/lang/ClassNotFoundException
    //   129	284	286	java/lang/ClassNotFoundException
    //   129	284	286	java/lang/NoSuchMethodException
  }
  
  public static Platform get() {
    return PLATFORM;
  }
  
  static <T> T readFieldOrNull(Object paramObject, Class<T> paramClass, String paramString) {
    Class<?> clazz = paramObject.getClass();
    while (clazz != Object.class) {
      try {
        Field field = clazz.getDeclaredField(paramString);
        field.setAccessible(true);
        null = field.get(paramObject);
        return (null == null || !paramClass.isInstance(null)) ? null : paramClass.cast(null);
      } catch (NoSuchFieldException noSuchFieldException) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException illegalAccessException) {
        throw new AssertionError();
      } 
    } 
    if (!paramString.equals("delegate")) {
      illegalAccessException = readFieldOrNull(illegalAccessException, Object.class, "delegate");
      if (illegalAccessException != null)
        return readFieldOrNull(illegalAccessException, paramClass, paramString); 
    } 
    return null;
  }
  
  public void afterHandshake(SSLSocket paramSSLSocket) {}
  
  public void configureTlsExtensions(SSLSocket paramSSLSocket, String paramString, List<Protocol> paramList) {}
  
  public void connectSocket(Socket paramSocket, InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException {
    paramSocket.connect(paramInetSocketAddress, paramInt);
  }
  
  public String getPrefix() {
    return "OkHttp";
  }
  
  public String getSelectedProtocol(SSLSocket paramSSLSocket) {
    return null;
  }
  
  public void log(String paramString) {
    System.out.println(paramString);
  }
  
  public void logW(String paramString) {
    System.out.println(paramString);
  }
  
  public X509TrustManager trustManager(SSLSocketFactory paramSSLSocketFactory) {
    try {
      paramSSLSocketFactory = readFieldOrNull(paramSSLSocketFactory, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
      return (paramSSLSocketFactory == null) ? null : readFieldOrNull(paramSSLSocketFactory, X509TrustManager.class, "trustManager");
    } catch (ClassNotFoundException classNotFoundException) {
      return null;
    } 
  }
  
  public TrustRootIndex trustRootIndex(X509TrustManager paramX509TrustManager) {
    return (TrustRootIndex)new RealTrustRootIndex(paramX509TrustManager.getAcceptedIssuers());
  }
  
  private static class Android extends Platform {
    private static final int MAX_LOG_LENGTH = 4000;
    
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    
    private final OptionalMethod<Socket> setAlpnProtocols;
    
    private final OptionalMethod<Socket> setHostname;
    
    private final OptionalMethod<Socket> setUseSessionTickets;
    
    private final Class<?> sslParametersClass;
    
    public Android(Class<?> param1Class, OptionalMethod<Socket> param1OptionalMethod1, OptionalMethod<Socket> param1OptionalMethod2, OptionalMethod<Socket> param1OptionalMethod3, OptionalMethod<Socket> param1OptionalMethod4) {
      this.sslParametersClass = param1Class;
      this.setUseSessionTickets = param1OptionalMethod1;
      this.setHostname = param1OptionalMethod2;
      this.getAlpnSelectedProtocol = param1OptionalMethod3;
      this.setAlpnProtocols = param1OptionalMethod4;
    }
    
    public void configureTlsExtensions(SSLSocket param1SSLSocket, String param1String, List<Protocol> param1List) {
      if (param1String != null) {
        this.setUseSessionTickets.invokeOptionalWithoutCheckedException(param1SSLSocket, new Object[] { Boolean.valueOf(true) });
        this.setHostname.invokeOptionalWithoutCheckedException(param1SSLSocket, new Object[] { param1String });
      } 
      OptionalMethod<Socket> optionalMethod = this.setAlpnProtocols;
      if (optionalMethod != null && optionalMethod.isSupported(param1SSLSocket)) {
        byte[] arrayOfByte = concatLengthPrefixed(param1List);
        this.setAlpnProtocols.invokeWithoutCheckedException(param1SSLSocket, new Object[] { arrayOfByte });
      } 
    }
    
    public void connectSocket(Socket param1Socket, InetSocketAddress param1InetSocketAddress, int param1Int) throws IOException {
      try {
        param1Socket.connect(param1InetSocketAddress, param1Int);
        return;
      } catch (AssertionError assertionError) {
        if (Util.isAndroidGetsocknameError(assertionError))
          throw new IOException(assertionError); 
        throw assertionError;
      } catch (SecurityException securityException) {
        IOException iOException = new IOException("Exception in connect");
        iOException.initCause(securityException);
        throw iOException;
      } 
    }
    
    public String getSelectedProtocol(SSLSocket param1SSLSocket) {
      String str;
      OptionalMethod<Socket> optionalMethod = this.getAlpnSelectedProtocol;
      SSLSocket sSLSocket = null;
      if (optionalMethod == null)
        return null; 
      if (!optionalMethod.isSupported(param1SSLSocket))
        return null; 
      byte[] arrayOfByte = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(param1SSLSocket, new Object[0]);
      param1SSLSocket = sSLSocket;
      if (arrayOfByte != null)
        str = new String(arrayOfByte, Util.UTF_8); 
      return str;
    }
    
    public void log(String param1String) {
      int j = param1String.length();
      int i = 0;
      label16: while (i < j) {
        int k = param1String.indexOf('\n', i);
        if (k == -1)
          k = j; 
        while (true) {
          int m = Math.min(k, i + 4000);
          Log.d("OkHttp", param1String.substring(i, m));
          if (m >= k) {
            i = m + 1;
            continue label16;
          } 
          i = m;
        } 
      } 
    }
    
    public X509TrustManager trustManager(SSLSocketFactory param1SSLSocketFactory) {
      Object object = readFieldOrNull(param1SSLSocketFactory, (Class)this.sslParametersClass, "sslParameters");
      classNotFoundException = (ClassNotFoundException)object;
      if (object == null)
        try {
          classNotFoundException = readFieldOrNull(param1SSLSocketFactory, (Class)Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, param1SSLSocketFactory.getClass().getClassLoader()), "sslParameters");
        } catch (ClassNotFoundException classNotFoundException) {
          return super.trustManager(param1SSLSocketFactory);
        }  
      X509TrustManager x509TrustManager = readFieldOrNull(classNotFoundException, X509TrustManager.class, "x509TrustManager");
      return (x509TrustManager != null) ? x509TrustManager : readFieldOrNull(classNotFoundException, X509TrustManager.class, "trustManager");
    }
    
    public TrustRootIndex trustRootIndex(X509TrustManager param1X509TrustManager) {
      TrustRootIndex trustRootIndex = AndroidTrustRootIndex.get(param1X509TrustManager);
      return (trustRootIndex != null) ? trustRootIndex : super.trustRootIndex(param1X509TrustManager);
    }
  }
  
  private static class JdkWithJettyBootPlatform extends Platform {
    private final Class<?> clientProviderClass;
    
    private final Method getMethod;
    
    private final Method putMethod;
    
    private final Method removeMethod;
    
    private final Class<?> serverProviderClass;
    
    public JdkWithJettyBootPlatform(Method param1Method1, Method param1Method2, Method param1Method3, Class<?> param1Class1, Class<?> param1Class2) {
      this.putMethod = param1Method1;
      this.getMethod = param1Method2;
      this.removeMethod = param1Method3;
      this.clientProviderClass = param1Class1;
      this.serverProviderClass = param1Class2;
    }
    
    public void afterHandshake(SSLSocket param1SSLSocket) {
      try {
        this.removeMethod.invoke(null, new Object[] { param1SSLSocket });
        return;
      } catch (IllegalAccessException|InvocationTargetException illegalAccessException) {
        throw new AssertionError();
      } 
    }
    
    public void configureTlsExtensions(SSLSocket param1SSLSocket, String param1String, List<Protocol> param1List) {
      ArrayList<String> arrayList = new ArrayList(param1List.size());
      int i = param1List.size();
      for (byte b = 0; b < i; b++) {
        Protocol protocol = param1List.get(b);
        if (protocol != Protocol.HTTP_1_0)
          arrayList.add(protocol.toString()); 
      } 
      try {
        ClassLoader classLoader = Platform.class.getClassLoader();
        Class<?> clazz1 = this.clientProviderClass;
        Class<?> clazz2 = this.serverProviderClass;
        Platform.JettyNegoProvider jettyNegoProvider = new Platform.JettyNegoProvider();
        this(arrayList);
        Object object = Proxy.newProxyInstance(classLoader, new Class[] { clazz1, clazz2 }, jettyNegoProvider);
        this.putMethod.invoke(null, new Object[] { param1SSLSocket, object });
        return;
      } catch (InvocationTargetException invocationTargetException) {
      
      } catch (IllegalAccessException illegalAccessException) {}
      throw new AssertionError(illegalAccessException);
    }
    
    public String getSelectedProtocol(SSLSocket param1SSLSocket) {
      try {
        String str;
        Method method = this.getMethod;
        Platform.JettyNegoProvider jettyNegoProvider2 = null;
        Platform.JettyNegoProvider jettyNegoProvider1 = (Platform.JettyNegoProvider)Proxy.getInvocationHandler(method.invoke(null, new Object[] { param1SSLSocket }));
        if (!jettyNegoProvider1.unsupported && jettyNegoProvider1.selected == null) {
          Internal.logger.log(Level.INFO, "ALPN callback dropped: SPDY and HTTP/2 are disabled. Is alpn-boot on the boot class path?");
          return null;
        } 
        if (jettyNegoProvider1.unsupported) {
          jettyNegoProvider1 = jettyNegoProvider2;
        } else {
          str = jettyNegoProvider1.selected;
        } 
        return str;
      } catch (InvocationTargetException|IllegalAccessException invocationTargetException) {
        throw new AssertionError();
      } 
    }
  }
  
  private static class JettyNegoProvider implements InvocationHandler {
    private final List<String> protocols;
    
    private String selected;
    
    private boolean unsupported;
    
    public JettyNegoProvider(List<String> param1List) {
      this.protocols = param1List;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      String str = param1Method.getName();
      Class<?> clazz = param1Method.getReturnType();
      param1Object = param1ArrayOfObject;
      if (param1ArrayOfObject == null)
        param1Object = Util.EMPTY_STRING_ARRAY; 
      if (str.equals("supports") && boolean.class == clazz)
        return Boolean.valueOf(true); 
      if (str.equals("unsupported") && void.class == clazz) {
        this.unsupported = true;
        return null;
      } 
      if (str.equals("protocols") && param1Object.length == 0)
        return this.protocols; 
      if ((str.equals("selectProtocol") || str.equals("select")) && String.class == clazz && param1Object.length == 1 && param1Object[0] instanceof List) {
        param1Object = param1Object[0];
        int i = param1Object.size();
        for (byte b = 0; b < i; b++) {
          if (this.protocols.contains(param1Object.get(b))) {
            param1Object = param1Object.get(b);
            this.selected = (String)param1Object;
            return param1Object;
          } 
        } 
        param1Object = this.protocols.get(0);
        this.selected = (String)param1Object;
        return param1Object;
      } 
      if ((str.equals("protocolSelected") || str.equals("selected")) && param1Object.length == 1) {
        this.selected = (String)param1Object[0];
        return null;
      } 
      return param1Method.invoke(this, (Object[])param1Object);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */