package okhttp3;

import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

public final class ConnectionSpec {
  private static final CipherSuite[] APPROVED_CIPHER_SUITES = new CipherSuite[] { 
      CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, 
      CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA };
  
  public static final ConnectionSpec CLEARTEXT;
  
  public static final ConnectionSpec COMPATIBLE_TLS;
  
  public static final ConnectionSpec MODERN_TLS = (new Builder(true)).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0 }).supportsTlsExtensions(true).build();
  
  private final String[] cipherSuites;
  
  private final boolean supportsTlsExtensions;
  
  private final boolean tls;
  
  private final String[] tlsVersions;
  
  static {
    COMPATIBLE_TLS = (new Builder(MODERN_TLS)).tlsVersions(new TlsVersion[] { TlsVersion.TLS_1_0 }).supportsTlsExtensions(true).build();
    CLEARTEXT = (new Builder(false)).build();
  }
  
  private ConnectionSpec(Builder paramBuilder) {
    this.tls = paramBuilder.tls;
    this.cipherSuites = paramBuilder.cipherSuites;
    this.tlsVersions = paramBuilder.tlsVersions;
    this.supportsTlsExtensions = paramBuilder.supportsTlsExtensions;
  }
  
  private static boolean nonEmptyIntersection(String[] paramArrayOfString1, String[] paramArrayOfString2) {
    if (paramArrayOfString1 != null && paramArrayOfString2 != null && paramArrayOfString1.length != 0 && paramArrayOfString2.length != 0) {
      int i = paramArrayOfString1.length;
      for (byte b = 0; b < i; b++) {
        if (Util.contains(paramArrayOfString2, paramArrayOfString1[b]))
          return true; 
      } 
    } 
    return false;
  }
  
  private ConnectionSpec supportedSpec(SSLSocket paramSSLSocket, boolean paramBoolean) {
    String[] arrayOfString1 = this.cipherSuites;
    if (arrayOfString1 != null) {
      arrayOfString1 = (String[])Util.intersect(String.class, (Object[])arrayOfString1, (Object[])paramSSLSocket.getEnabledCipherSuites());
    } else {
      arrayOfString1 = paramSSLSocket.getEnabledCipherSuites();
    } 
    String[] arrayOfString2 = this.tlsVersions;
    if (arrayOfString2 != null) {
      arrayOfString2 = (String[])Util.intersect(String.class, (Object[])arrayOfString2, (Object[])paramSSLSocket.getEnabledProtocols());
    } else {
      arrayOfString2 = paramSSLSocket.getEnabledProtocols();
    } 
    String[] arrayOfString3 = arrayOfString1;
    if (paramBoolean) {
      arrayOfString3 = arrayOfString1;
      if (Util.contains(paramSSLSocket.getSupportedCipherSuites(), "TLS_FALLBACK_SCSV"))
        arrayOfString3 = Util.concat(arrayOfString1, "TLS_FALLBACK_SCSV"); 
    } 
    return (new Builder(this)).cipherSuites(arrayOfString3).tlsVersions(arrayOfString2).build();
  }
  
  void apply(SSLSocket paramSSLSocket, boolean paramBoolean) {
    ConnectionSpec connectionSpec = supportedSpec(paramSSLSocket, paramBoolean);
    String[] arrayOfString = connectionSpec.tlsVersions;
    if (arrayOfString != null)
      paramSSLSocket.setEnabledProtocols(arrayOfString); 
    arrayOfString = connectionSpec.cipherSuites;
    if (arrayOfString != null)
      paramSSLSocket.setEnabledCipherSuites(arrayOfString); 
  }
  
  public List<CipherSuite> cipherSuites() {
    String[] arrayOfString = this.cipherSuites;
    if (arrayOfString == null)
      return null; 
    CipherSuite[] arrayOfCipherSuite = new CipherSuite[arrayOfString.length];
    byte b = 0;
    while (true) {
      arrayOfString = this.cipherSuites;
      if (b < arrayOfString.length) {
        arrayOfCipherSuite[b] = CipherSuite.forJavaName(arrayOfString[b]);
        b++;
        continue;
      } 
      return Util.immutableList((Object[])arrayOfCipherSuite);
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof ConnectionSpec))
      return false; 
    if (paramObject == this)
      return true; 
    paramObject = paramObject;
    boolean bool = this.tls;
    if (bool != ((ConnectionSpec)paramObject).tls)
      return false; 
    if (bool) {
      if (!Arrays.equals((Object[])this.cipherSuites, (Object[])((ConnectionSpec)paramObject).cipherSuites))
        return false; 
      if (!Arrays.equals((Object[])this.tlsVersions, (Object[])((ConnectionSpec)paramObject).tlsVersions))
        return false; 
      if (this.supportsTlsExtensions != ((ConnectionSpec)paramObject).supportsTlsExtensions)
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    byte b;
    if (this.tls) {
      b = ((527 + Arrays.hashCode((Object[])this.cipherSuites)) * 31 + Arrays.hashCode((Object[])this.tlsVersions)) * 31 + (this.supportsTlsExtensions ^ true);
    } else {
      b = 17;
    } 
    return b;
  }
  
  public boolean isCompatible(SSLSocket paramSSLSocket) {
    if (!this.tls)
      return false; 
    String[] arrayOfString = this.tlsVersions;
    if (arrayOfString != null && !nonEmptyIntersection(arrayOfString, paramSSLSocket.getEnabledProtocols()))
      return false; 
    arrayOfString = this.cipherSuites;
    return !(arrayOfString != null && !nonEmptyIntersection(arrayOfString, paramSSLSocket.getEnabledCipherSuites()));
  }
  
  public boolean isTls() {
    return this.tls;
  }
  
  public boolean supportsTlsExtensions() {
    return this.supportsTlsExtensions;
  }
  
  public List<TlsVersion> tlsVersions() {
    String[] arrayOfString = this.tlsVersions;
    if (arrayOfString == null)
      return null; 
    TlsVersion[] arrayOfTlsVersion = new TlsVersion[arrayOfString.length];
    byte b = 0;
    while (true) {
      String[] arrayOfString1 = this.tlsVersions;
      if (b < arrayOfString1.length) {
        arrayOfTlsVersion[b] = TlsVersion.forJavaName(arrayOfString1[b]);
        b++;
        continue;
      } 
      return Util.immutableList((Object[])arrayOfTlsVersion);
    } 
  }
  
  public String toString() {
    String str1;
    if (!this.tls)
      return "ConnectionSpec()"; 
    String[] arrayOfString = this.cipherSuites;
    String str2 = "[all enabled]";
    if (arrayOfString != null) {
      str1 = cipherSuites().toString();
    } else {
      str1 = "[all enabled]";
    } 
    if (this.tlsVersions != null)
      str2 = tlsVersions().toString(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConnectionSpec(cipherSuites=");
    stringBuilder.append(str1);
    stringBuilder.append(", tlsVersions=");
    stringBuilder.append(str2);
    stringBuilder.append(", supportsTlsExtensions=");
    stringBuilder.append(this.supportsTlsExtensions);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public static final class Builder {
    private String[] cipherSuites;
    
    private boolean supportsTlsExtensions;
    
    private boolean tls;
    
    private String[] tlsVersions;
    
    public Builder(ConnectionSpec param1ConnectionSpec) {
      this.tls = param1ConnectionSpec.tls;
      this.cipherSuites = param1ConnectionSpec.cipherSuites;
      this.tlsVersions = param1ConnectionSpec.tlsVersions;
      this.supportsTlsExtensions = param1ConnectionSpec.supportsTlsExtensions;
    }
    
    Builder(boolean param1Boolean) {
      this.tls = param1Boolean;
    }
    
    public Builder allEnabledCipherSuites() {
      if (this.tls) {
        this.cipherSuites = null;
        return this;
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder allEnabledTlsVersions() {
      if (this.tls) {
        this.tlsVersions = null;
        return this;
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
    
    public ConnectionSpec build() {
      return new ConnectionSpec(this);
    }
    
    public Builder cipherSuites(String... param1VarArgs) {
      if (this.tls) {
        if (param1VarArgs.length != 0) {
          this.cipherSuites = (String[])param1VarArgs.clone();
          return this;
        } 
        throw new IllegalArgumentException("At least one cipher suite is required");
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder cipherSuites(CipherSuite... param1VarArgs) {
      if (this.tls) {
        String[] arrayOfString = new String[param1VarArgs.length];
        for (byte b = 0; b < param1VarArgs.length; b++)
          arrayOfString[b] = (param1VarArgs[b]).javaName; 
        return cipherSuites(arrayOfString);
      } 
      throw new IllegalStateException("no cipher suites for cleartext connections");
    }
    
    public Builder supportsTlsExtensions(boolean param1Boolean) {
      if (this.tls) {
        this.supportsTlsExtensions = param1Boolean;
        return this;
      } 
      throw new IllegalStateException("no TLS extensions for cleartext connections");
    }
    
    public Builder tlsVersions(String... param1VarArgs) {
      if (this.tls) {
        if (param1VarArgs.length != 0) {
          this.tlsVersions = (String[])param1VarArgs.clone();
          return this;
        } 
        throw new IllegalArgumentException("At least one TLS version is required");
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
    
    public Builder tlsVersions(TlsVersion... param1VarArgs) {
      if (this.tls) {
        String[] arrayOfString = new String[param1VarArgs.length];
        for (byte b = 0; b < param1VarArgs.length; b++)
          arrayOfString[b] = (param1VarArgs[b]).javaName; 
        return tlsVersions(arrayOfString);
      } 
      throw new IllegalStateException("no TLS versions for cleartext connections");
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\ConnectionSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */