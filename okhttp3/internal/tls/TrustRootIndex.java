package okhttp3.internal.tls;

import java.security.cert.X509Certificate;

public interface TrustRootIndex {
  X509Certificate findByIssuerAndSignature(X509Certificate paramX509Certificate);
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\tls\TrustRootIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */