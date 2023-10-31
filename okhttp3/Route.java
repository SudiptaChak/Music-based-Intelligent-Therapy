package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;

public final class Route {
  final Address address;
  
  final InetSocketAddress inetSocketAddress;
  
  final Proxy proxy;
  
  public Route(Address paramAddress, Proxy paramProxy, InetSocketAddress paramInetSocketAddress) {
    if (paramAddress != null) {
      if (paramProxy != null) {
        if (paramInetSocketAddress != null) {
          this.address = paramAddress;
          this.proxy = paramProxy;
          this.inetSocketAddress = paramInetSocketAddress;
          return;
        } 
        throw new NullPointerException("inetSocketAddress == null");
      } 
      throw new NullPointerException("proxy == null");
    } 
    throw new NullPointerException("address == null");
  }
  
  public Address address() {
    return this.address;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Route;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.address.equals(((Route)paramObject).address)) {
        bool1 = bool2;
        if (this.proxy.equals(((Route)paramObject).proxy)) {
          bool1 = bool2;
          if (this.inetSocketAddress.equals(((Route)paramObject).inetSocketAddress))
            bool1 = true; 
        } 
      } 
    } 
    return bool1;
  }
  
  public int hashCode() {
    return ((527 + this.address.hashCode()) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
  }
  
  public Proxy proxy() {
    return this.proxy;
  }
  
  public boolean requiresTunnel() {
    boolean bool;
    if (this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public InetSocketAddress socketAddress() {
    return this.inetSocketAddress;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Route.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */