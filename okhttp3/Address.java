package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Util;

public final class Address {
  final CertificatePinner certificatePinner;
  
  final List<ConnectionSpec> connectionSpecs;
  
  final Dns dns;
  
  final HostnameVerifier hostnameVerifier;
  
  final List<Protocol> protocols;
  
  final Proxy proxy;
  
  final Authenticator proxyAuthenticator;
  
  final ProxySelector proxySelector;
  
  final SocketFactory socketFactory;
  
  final SSLSocketFactory sslSocketFactory;
  
  final HttpUrl url;
  
  public Address(String paramString, int paramInt, Dns paramDns, SocketFactory paramSocketFactory, SSLSocketFactory paramSSLSocketFactory, HostnameVerifier paramHostnameVerifier, CertificatePinner paramCertificatePinner, Authenticator paramAuthenticator, Proxy paramProxy, List<Protocol> paramList, List<ConnectionSpec> paramList1, ProxySelector paramProxySelector) {
    String str;
    HttpUrl.Builder builder = new HttpUrl.Builder();
    if (paramSSLSocketFactory != null) {
      str = "https";
    } else {
      str = "http";
    } 
    this.url = builder.scheme(str).host(paramString).port(paramInt).build();
    if (paramDns != null) {
      this.dns = paramDns;
      if (paramSocketFactory != null) {
        this.socketFactory = paramSocketFactory;
        if (paramAuthenticator != null) {
          this.proxyAuthenticator = paramAuthenticator;
          if (paramList != null) {
            this.protocols = Util.immutableList(paramList);
            if (paramList1 != null) {
              this.connectionSpecs = Util.immutableList(paramList1);
              if (paramProxySelector != null) {
                this.proxySelector = paramProxySelector;
                this.proxy = paramProxy;
                this.sslSocketFactory = paramSSLSocketFactory;
                this.hostnameVerifier = paramHostnameVerifier;
                this.certificatePinner = paramCertificatePinner;
                return;
              } 
              throw new IllegalArgumentException("proxySelector == null");
            } 
            throw new IllegalArgumentException("connectionSpecs == null");
          } 
          throw new IllegalArgumentException("protocols == null");
        } 
        throw new IllegalArgumentException("proxyAuthenticator == null");
      } 
      throw new IllegalArgumentException("socketFactory == null");
    } 
    throw new IllegalArgumentException("dns == null");
  }
  
  public CertificatePinner certificatePinner() {
    return this.certificatePinner;
  }
  
  public List<ConnectionSpec> connectionSpecs() {
    return this.connectionSpecs;
  }
  
  public Dns dns() {
    return this.dns;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Address;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.url.equals(((Address)paramObject).url)) {
        bool1 = bool2;
        if (this.dns.equals(((Address)paramObject).dns)) {
          bool1 = bool2;
          if (this.proxyAuthenticator.equals(((Address)paramObject).proxyAuthenticator)) {
            bool1 = bool2;
            if (this.protocols.equals(((Address)paramObject).protocols)) {
              bool1 = bool2;
              if (this.connectionSpecs.equals(((Address)paramObject).connectionSpecs)) {
                bool1 = bool2;
                if (this.proxySelector.equals(((Address)paramObject).proxySelector)) {
                  bool1 = bool2;
                  if (Util.equal(this.proxy, ((Address)paramObject).proxy)) {
                    bool1 = bool2;
                    if (Util.equal(this.sslSocketFactory, ((Address)paramObject).sslSocketFactory)) {
                      bool1 = bool2;
                      if (Util.equal(this.hostnameVerifier, ((Address)paramObject).hostnameVerifier)) {
                        bool1 = bool2;
                        if (Util.equal(this.certificatePinner, ((Address)paramObject).certificatePinner))
                          bool1 = true; 
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  public int hashCode() {
    byte b1;
    byte b2;
    byte b3;
    int k = this.url.hashCode();
    int m = this.dns.hashCode();
    int n = this.proxyAuthenticator.hashCode();
    int i2 = this.protocols.hashCode();
    int j = this.connectionSpecs.hashCode();
    int i1 = this.proxySelector.hashCode();
    Proxy proxy = this.proxy;
    int i = 0;
    if (proxy != null) {
      b1 = proxy.hashCode();
    } else {
      b1 = 0;
    } 
    SSLSocketFactory sSLSocketFactory = this.sslSocketFactory;
    if (sSLSocketFactory != null) {
      b2 = sSLSocketFactory.hashCode();
    } else {
      b2 = 0;
    } 
    HostnameVerifier hostnameVerifier = this.hostnameVerifier;
    if (hostnameVerifier != null) {
      b3 = hostnameVerifier.hashCode();
    } else {
      b3 = 0;
    } 
    CertificatePinner certificatePinner = this.certificatePinner;
    if (certificatePinner != null)
      i = certificatePinner.hashCode(); 
    return (((((((((527 + k) * 31 + m) * 31 + n) * 31 + i2) * 31 + j) * 31 + i1) * 31 + b1) * 31 + b2) * 31 + b3) * 31 + i;
  }
  
  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
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
  
  public SocketFactory socketFactory() {
    return this.socketFactory;
  }
  
  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }
  
  public HttpUrl url() {
    return this.url;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Address.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */