package okhttp3.internal.tls;

import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public final class CertificateChainCleaner {
  private static final int MAX_SIGNERS = 9;
  
  private final TrustRootIndex trustRootIndex;
  
  public CertificateChainCleaner(TrustRootIndex paramTrustRootIndex) {
    this.trustRootIndex = paramTrustRootIndex;
  }
  
  private boolean verifySignature(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2) {
    if (!paramX509Certificate1.getIssuerDN().equals(paramX509Certificate2.getSubjectDN()))
      return false; 
    try {
      paramX509Certificate1.verify(paramX509Certificate2.getPublicKey());
      return true;
    } catch (GeneralSecurityException generalSecurityException) {
      return false;
    } 
  }
  
  public List<Certificate> clean(List<Certificate> paramList) throws SSLPeerUnverifiedException {
    StringBuilder stringBuilder1;
    ArrayDeque<Certificate> arrayDeque = new ArrayDeque<Certificate>(paramList);
    paramList = new ArrayList<Certificate>();
    paramList.add(arrayDeque.removeFirst());
    byte b = 0;
    boolean bool = false;
    while (b < 9) {
      X509Certificate x509Certificate1 = (X509Certificate)paramList.get(paramList.size() - 1);
      X509Certificate x509Certificate2 = this.trustRootIndex.findByIssuerAndSignature(x509Certificate1);
      if (x509Certificate2 != null) {
        if (paramList.size() > 1 || !x509Certificate1.equals(x509Certificate2))
          paramList.add(x509Certificate2); 
        if (verifySignature(x509Certificate2, x509Certificate2))
          return paramList; 
        bool = true;
      } else {
        Iterator<Certificate> iterator = arrayDeque.iterator();
        while (iterator.hasNext()) {
          x509Certificate2 = (X509Certificate)iterator.next();
          if (verifySignature(x509Certificate1, x509Certificate2)) {
            iterator.remove();
            paramList.add(x509Certificate2);
            continue;
          } 
        } 
        if (bool)
          return paramList; 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Failed to find a trusted cert that signed ");
        stringBuilder1.append(x509Certificate1);
        throw new SSLPeerUnverifiedException(stringBuilder1.toString());
      } 
      b++;
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Certificate chain too long: ");
    stringBuilder2.append(stringBuilder1);
    throw new SSLPeerUnverifiedException(stringBuilder2.toString());
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\tls\CertificateChainCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */