package okhttp3.internal.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.RouteDatabase;

public final class RouteSelector {
  private final Address address;
  
  private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
  
  private InetSocketAddress lastInetSocketAddress;
  
  private Proxy lastProxy;
  
  private int nextInetSocketAddressIndex;
  
  private int nextProxyIndex;
  
  private final List<Route> postponedRoutes = new ArrayList<Route>();
  
  private List<Proxy> proxies = Collections.emptyList();
  
  private final RouteDatabase routeDatabase;
  
  public RouteSelector(Address paramAddress, RouteDatabase paramRouteDatabase) {
    this.address = paramAddress;
    this.routeDatabase = paramRouteDatabase;
    resetNextProxy(paramAddress.url(), paramAddress.proxy());
  }
  
  static String getHostString(InetSocketAddress paramInetSocketAddress) {
    InetAddress inetAddress = paramInetSocketAddress.getAddress();
    return (inetAddress == null) ? paramInetSocketAddress.getHostName() : inetAddress.getHostAddress();
  }
  
  private boolean hasNextInetSocketAddress() {
    boolean bool;
    if (this.nextInetSocketAddressIndex < this.inetSocketAddresses.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean hasNextPostponed() {
    return this.postponedRoutes.isEmpty() ^ true;
  }
  
  private boolean hasNextProxy() {
    boolean bool;
    if (this.nextProxyIndex < this.proxies.size()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private InetSocketAddress nextInetSocketAddress() throws IOException {
    if (hasNextInetSocketAddress()) {
      List<InetSocketAddress> list = this.inetSocketAddresses;
      int i = this.nextInetSocketAddressIndex;
      this.nextInetSocketAddressIndex = i + 1;
      return list.get(i);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No route to ");
    stringBuilder.append(this.address.url().host());
    stringBuilder.append("; exhausted inet socket addresses: ");
    stringBuilder.append(this.inetSocketAddresses);
    throw new SocketException(stringBuilder.toString());
  }
  
  private Route nextPostponed() {
    return this.postponedRoutes.remove(0);
  }
  
  private Proxy nextProxy() throws IOException {
    if (hasNextProxy()) {
      List<Proxy> list = this.proxies;
      int i = this.nextProxyIndex;
      this.nextProxyIndex = i + 1;
      Proxy proxy = list.get(i);
      resetNextInetSocketAddress(proxy);
      return proxy;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No route to ");
    stringBuilder.append(this.address.url().host());
    stringBuilder.append("; exhausted proxy configurations: ");
    stringBuilder.append(this.proxies);
    throw new SocketException(stringBuilder.toString());
  }
  
  private void resetNextInetSocketAddress(Proxy paramProxy) throws IOException {
    int i;
    String str;
    InetAddress inetAddress;
    this.inetSocketAddresses = new ArrayList<InetSocketAddress>();
    if (paramProxy.type() == Proxy.Type.DIRECT || paramProxy.type() == Proxy.Type.SOCKS) {
      str = this.address.url().host();
      i = this.address.url().port();
    } else {
      SocketAddress socketAddress = paramProxy.address();
      if (socketAddress instanceof InetSocketAddress) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
        str = getHostString(inetSocketAddress);
        i = inetSocketAddress.getPort();
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Proxy.address() is not an InetSocketAddress: ");
        stringBuilder.append(str.getClass());
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    if (i >= 1 && i <= 65535) {
      if (stringBuilder.type() == Proxy.Type.SOCKS) {
        this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(str, i));
      } else {
        List<InetAddress> list = this.address.dns().lookup(str);
        int j = list.size();
        for (byte b = 0; b < j; b++) {
          inetAddress = list.get(b);
          this.inetSocketAddresses.add(new InetSocketAddress(inetAddress, i));
        } 
      } 
      this.nextInetSocketAddressIndex = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No route to ");
    stringBuilder.append((String)inetAddress);
    stringBuilder.append(":");
    stringBuilder.append(i);
    stringBuilder.append("; port is out of range");
    throw new SocketException(stringBuilder.toString());
  }
  
  private void resetNextProxy(HttpUrl paramHttpUrl, Proxy paramProxy) {
    if (paramProxy != null) {
      this.proxies = Collections.singletonList(paramProxy);
    } else {
      this.proxies = new ArrayList<Proxy>();
      List<Proxy> list = this.address.proxySelector().select(paramHttpUrl.uri());
      if (list != null)
        this.proxies.addAll(list); 
      this.proxies.removeAll(Collections.singleton(Proxy.NO_PROXY));
      this.proxies.add(Proxy.NO_PROXY);
    } 
    this.nextProxyIndex = 0;
  }
  
  public void connectFailed(Route paramRoute, IOException paramIOException) {
    if (paramRoute.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null)
      this.address.proxySelector().connectFailed(this.address.url().uri(), paramRoute.proxy().address(), paramIOException); 
    this.routeDatabase.failed(paramRoute);
  }
  
  public boolean hasNext() {
    return (hasNextInetSocketAddress() || hasNextProxy() || hasNextPostponed());
  }
  
  public Route next() throws IOException {
    if (!hasNextInetSocketAddress()) {
      if (!hasNextProxy()) {
        if (hasNextPostponed())
          return nextPostponed(); 
        throw new NoSuchElementException();
      } 
      this.lastProxy = nextProxy();
    } 
    this.lastInetSocketAddress = nextInetSocketAddress();
    Route route2 = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
    Route route1 = route2;
    if (this.routeDatabase.shouldPostpone(route2)) {
      this.postponedRoutes.add(route2);
      route1 = next();
    } 
    return route1;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\RouteSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */