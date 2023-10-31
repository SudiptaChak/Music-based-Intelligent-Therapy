package okhttp3.internal.io;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.ConnectionSpecSelector;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.http.Http1xStream;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.tls.OkHostnameVerifier;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection extends FramedConnection.Listener implements Connection {
  public int allocationLimit;
  
  public final List<Reference<StreamAllocation>> allocations = new ArrayList<Reference<StreamAllocation>>();
  
  public volatile FramedConnection framedConnection;
  
  private Handshake handshake;
  
  public long idleAtNanos = Long.MAX_VALUE;
  
  public boolean noNewStreams;
  
  private Protocol protocol;
  
  private Socket rawSocket;
  
  private final Route route;
  
  public BufferedSink sink;
  
  public Socket socket;
  
  public BufferedSource source;
  
  public int successCount;
  
  public RealConnection(Route paramRoute) {
    this.route = paramRoute;
  }
  
  private void connectSocket(int paramInt1, int paramInt2, int paramInt3, ConnectionSpecSelector paramConnectionSpecSelector) throws IOException {
    this.rawSocket.setSoTimeout(paramInt2);
    try {
      Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), paramInt1);
      this.source = Okio.buffer(Okio.source(this.rawSocket));
      this.sink = Okio.buffer(Okio.sink(this.rawSocket));
      if (this.route.address().sslSocketFactory() != null) {
        connectTls(paramInt2, paramInt3, paramConnectionSpecSelector);
      } else {
        this.protocol = Protocol.HTTP_1_1;
        this.socket = this.rawSocket;
      } 
      if (this.protocol == Protocol.SPDY_3 || this.protocol == Protocol.HTTP_2) {
        this.socket.setSoTimeout(0);
        FramedConnection framedConnection = (new FramedConnection.Builder(true)).socket(this.socket, this.route.address().url().host(), this.source, this.sink).protocol(this.protocol).listener(this).build();
        framedConnection.sendConnectionPreface();
        this.allocationLimit = framedConnection.maxConcurrentStreams();
        this.framedConnection = framedConnection;
        return;
      } 
      this.allocationLimit = 1;
      return;
    } catch (ConnectException connectException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to connect to ");
      stringBuilder.append(this.route.socketAddress());
      throw new ConnectException(stringBuilder.toString());
    } 
  }
  
  private void connectTls(int paramInt1, int paramInt2, ConnectionSpecSelector paramConnectionSpecSelector) throws IOException {
    SSLPeerUnverifiedException sSLPeerUnverifiedException;
    if (this.route.requiresTunnel())
      createTunnel(paramInt1, paramInt2); 
    Address address = this.route.address();
    SSLSocketFactory sSLSocketFactory = address.sslSocketFactory();
    Handshake handshake = null;
    ConnectionSpec connectionSpec = null;
    ConnectionSpecSelector connectionSpecSelector2 = null;
    try {
      SSLSocket sSLSocket = (SSLSocket)sSLSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
      try {
        connectionSpec = paramConnectionSpecSelector.configureSecureSocket(sSLSocket);
        if (connectionSpec.supportsTlsExtensions())
          Platform.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols()); 
        sSLSocket.startHandshake();
        handshake = Handshake.get(sSLSocket.getSession());
        if (address.hostnameVerifier().verify(address.url().host(), sSLSocket.getSession())) {
          String str;
          Protocol protocol;
          address.certificatePinner().check(address.url().host(), handshake.peerCertificates());
          paramConnectionSpecSelector = connectionSpecSelector2;
          if (connectionSpec.supportsTlsExtensions())
            str = Platform.get().getSelectedProtocol(sSLSocket); 
          this.socket = sSLSocket;
          this.source = Okio.buffer(Okio.source(sSLSocket));
          this.sink = Okio.buffer(Okio.sink(this.socket));
          this.handshake = handshake;
          if (str != null) {
            protocol = Protocol.get(str);
          } else {
            protocol = Protocol.HTTP_1_1;
          } 
          this.protocol = protocol;
          return;
        } 
        X509Certificate x509Certificate = handshake.peerCertificates().get(0);
        sSLPeerUnverifiedException = new SSLPeerUnverifiedException();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Hostname ");
        stringBuilder.append(address.url().host());
        stringBuilder.append(" not verified:");
        stringBuilder.append("\n    certificate: ");
        stringBuilder.append(CertificatePinner.pin(x509Certificate));
        stringBuilder.append("\n    DN: ");
        stringBuilder.append(x509Certificate.getSubjectDN().getName());
        stringBuilder.append("\n    subjectAltNames: ");
        stringBuilder.append(OkHostnameVerifier.allSubjectAltNames(x509Certificate));
        this(stringBuilder.toString());
        throw sSLPeerUnverifiedException;
      } catch (AssertionError assertionError1) {
        SSLSocket sSLSocket1 = sSLSocket;
      } finally {
        AssertionError assertionError1;
        paramConnectionSpecSelector = null;
      } 
    } catch (AssertionError assertionError) {
      SSLPeerUnverifiedException sSLPeerUnverifiedException1 = sSLPeerUnverifiedException;
    } finally {}
    ConnectionSpecSelector connectionSpecSelector1 = paramConnectionSpecSelector;
    if (Util.isAndroidGetsocknameError(assertionError)) {
      connectionSpecSelector1 = paramConnectionSpecSelector;
      IOException iOException = new IOException();
      connectionSpecSelector1 = paramConnectionSpecSelector;
      this(assertionError);
      connectionSpecSelector1 = paramConnectionSpecSelector;
      throw iOException;
    } 
    connectionSpecSelector1 = paramConnectionSpecSelector;
    throw assertionError;
  }
  
  private void createTunnel(int paramInt1, int paramInt2) throws IOException {
    Request request = createTunnelRequest();
    HttpUrl httpUrl = request.url();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CONNECT ");
    stringBuilder.append(Util.hostHeader(httpUrl, true));
    stringBuilder.append(" HTTP/1.1");
    String str = stringBuilder.toString();
    while (true) {
      Http1xStream http1xStream = new Http1xStream(null, this.source, this.sink);
      this.source.timeout().timeout(paramInt1, TimeUnit.MILLISECONDS);
      this.sink.timeout().timeout(paramInt2, TimeUnit.MILLISECONDS);
      http1xStream.writeRequest(request.headers(), str);
      http1xStream.finishRequest();
      Response response = http1xStream.readResponse().request(request).build();
      long l2 = OkHeaders.contentLength(response);
      long l1 = l2;
      if (l2 == -1L)
        l1 = 0L; 
      Source source = http1xStream.newFixedLengthSource(l1);
      Util.skipAll(source, 2147483647, TimeUnit.MILLISECONDS);
      source.close();
      int i = response.code();
      if (i != 200) {
        Request request1;
        if (i == 407) {
          request1 = this.route.address().proxyAuthenticator().authenticate(this.route, response);
          if (request1 != null)
            continue; 
          throw new IOException("Failed to authenticate with proxy");
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Unexpected response code for CONNECT: ");
        stringBuilder1.append(request1.code());
        throw new IOException(stringBuilder1.toString());
      } 
      if (this.source.buffer().exhausted() && this.sink.buffer().exhausted())
        return; 
      throw new IOException("TLS tunnel buffered too many bytes!");
    } 
  }
  
  private Request createTunnelRequest() throws IOException {
    return (new Request.Builder()).url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
  }
  
  public void cancel() {
    Util.closeQuietly(this.rawSocket);
  }
  
  public void connect(int paramInt1, int paramInt2, int paramInt3, List<ConnectionSpec> paramList, boolean paramBoolean) throws RouteException {
    if (this.protocol == null) {
      RouteException routeException;
      ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(paramList);
      Proxy proxy = this.route.proxy();
      Address address = this.route.address();
      if (this.route.address().sslSocketFactory() != null || paramList.contains(ConnectionSpec.CLEARTEXT)) {
        paramList = null;
        while (this.protocol == null) {
          try {
            Socket socket;
            if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP) {
              socket = address.socketFactory().createSocket();
            } else {
              socket = new Socket(proxy);
            } 
            this.rawSocket = socket;
            connectSocket(paramInt1, paramInt2, paramInt3, connectionSpecSelector);
          } catch (IOException iOException) {
            Util.closeQuietly(this.socket);
            Util.closeQuietly(this.rawSocket);
            this.socket = null;
            this.rawSocket = null;
            this.source = null;
            this.sink = null;
            this.handshake = null;
            this.protocol = null;
            if (paramList == null) {
              routeException = new RouteException(iOException);
            } else {
              routeException.addConnectException(iOException);
            } 
            if (paramBoolean && connectionSpecSelector.connectionFailed(iOException))
              continue; 
            throw routeException;
          } 
        } 
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("CLEARTEXT communication not supported: ");
      stringBuilder.append(routeException);
      throw new RouteException(new UnknownServiceException(stringBuilder.toString()));
    } 
    throw new IllegalStateException("already connected");
  }
  
  public Handshake handshake() {
    return this.handshake;
  }
  
  boolean isConnected() {
    boolean bool;
    if (this.protocol != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isHealthy(boolean paramBoolean) {
    if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown())
      return false; 
    if (this.framedConnection != null)
      return true; 
    if (paramBoolean)
      try {
        int i = this.socket.getSoTimeout();
        try {
          this.socket.setSoTimeout(1);
          paramBoolean = this.source.exhausted();
          if (paramBoolean)
            return false; 
          return true;
        } finally {
          this.socket.setSoTimeout(i);
        } 
      } catch (SocketTimeoutException socketTimeoutException) {
      
      } catch (IOException iOException) {
        return false;
      }  
    return true;
  }
  
  public boolean isMultiplexed() {
    boolean bool;
    if (this.framedConnection != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onSettings(FramedConnection paramFramedConnection) {
    this.allocationLimit = paramFramedConnection.maxConcurrentStreams();
  }
  
  public void onStream(FramedStream paramFramedStream) throws IOException {
    paramFramedStream.close(ErrorCode.REFUSED_STREAM);
  }
  
  public Protocol protocol() {
    if (this.framedConnection == null) {
      Protocol protocol = this.protocol;
      if (protocol == null)
        protocol = Protocol.HTTP_1_1; 
      return protocol;
    } 
    return this.framedConnection.getProtocol();
  }
  
  public Route route() {
    return this.route;
  }
  
  public Socket socket() {
    return this.socket;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Connection{");
    stringBuilder.append(this.route.address().url().host());
    stringBuilder.append(":");
    stringBuilder.append(this.route.address().url().port());
    stringBuilder.append(", proxy=");
    stringBuilder.append(this.route.proxy());
    stringBuilder.append(" hostAddress=");
    stringBuilder.append(this.route.socketAddress());
    stringBuilder.append(" cipherSuite=");
    Handshake handshake = this.handshake;
    if (handshake != null) {
      CipherSuite cipherSuite = handshake.cipherSuite();
    } else {
      str = "none";
    } 
    stringBuilder.append(str);
    stringBuilder.append(" protocol=");
    stringBuilder.append(this.protocol);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\io\RealConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */