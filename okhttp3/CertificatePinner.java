package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;
import okio.ByteString;

public final class CertificatePinner {
  public static final CertificatePinner DEFAULT = (new Builder()).build();
  
  private final List<Pin> pins;
  
  private final TrustRootIndex trustRootIndex;
  
  private CertificatePinner(Builder paramBuilder) {
    this.pins = Util.immutableList(paramBuilder.pins);
    this.trustRootIndex = paramBuilder.trustRootIndex;
  }
  
  public static String pin(Certificate paramCertificate) {
    if (paramCertificate instanceof X509Certificate) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("sha256/");
      stringBuilder.append(sha256((X509Certificate)paramCertificate).base64());
      return stringBuilder.toString();
    } 
    throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
  }
  
  static ByteString sha1(X509Certificate paramX509Certificate) {
    return Util.sha1(ByteString.of(paramX509Certificate.getPublicKey().getEncoded()));
  }
  
  static ByteString sha256(X509Certificate paramX509Certificate) {
    return Util.sha256(ByteString.of(paramX509Certificate.getPublicKey().getEncoded()));
  }
  
  public void check(String paramString, List<Certificate> paramList) throws SSLPeerUnverifiedException {
    List<Pin> list1 = findMatchingPins(paramString);
    if (list1.isEmpty())
      return; 
    List<Certificate> list = paramList;
    if (this.trustRootIndex != null)
      list = (new CertificateChainCleaner(this.trustRootIndex)).clean(paramList); 
    int j = list.size();
    boolean bool = false;
    byte b;
    for (b = 0; b < j; b++) {
      X509Certificate x509Certificate = (X509Certificate)list.get(b);
      int k = list1.size();
      ByteString byteString = null;
      byte b1 = 0;
      paramList = null;
      while (b1 < k) {
        Pin pin = list1.get(b1);
        if (pin.hashAlgorithm.equals("sha256/")) {
          ByteString byteString1 = byteString;
          if (byteString == null)
            byteString1 = sha256(x509Certificate); 
          byteString = byteString1;
          if (pin.hash.equals(byteString1))
            return; 
        } else if (pin.hashAlgorithm.equals("sha1/")) {
          ByteString byteString2;
          List<Certificate> list2 = paramList;
          if (paramList == null)
            byteString2 = sha1(x509Certificate); 
          ByteString byteString1 = byteString2;
          if (pin.hash.equals(byteString2))
            return; 
        } else {
          throw new AssertionError();
        } 
        b1++;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Certificate pinning failure!");
    stringBuilder.append("\n  Peer certificate chain:");
    int i = list.size();
    for (b = 0; b < i; b++) {
      X509Certificate x509Certificate = (X509Certificate)list.get(b);
      stringBuilder.append("\n    ");
      stringBuilder.append(pin(x509Certificate));
      stringBuilder.append(": ");
      stringBuilder.append(x509Certificate.getSubjectDN().getName());
    } 
    stringBuilder.append("\n  Pinned certificates for ");
    stringBuilder.append(paramString);
    stringBuilder.append(":");
    i = list1.size();
    for (b = bool; b < i; b++) {
      Pin pin = list1.get(b);
      stringBuilder.append("\n    ");
      stringBuilder.append(pin);
    } 
    throw new SSLPeerUnverifiedException(stringBuilder.toString());
  }
  
  public void check(String paramString, Certificate... paramVarArgs) throws SSLPeerUnverifiedException {
    check(paramString, Arrays.asList(paramVarArgs));
  }
  
  List<Pin> findMatchingPins(String paramString) {
    List<?> list = Collections.emptyList();
    for (Pin pin : this.pins) {
      if (pin.matches(paramString)) {
        List<?> list1 = list;
        if (list.isEmpty())
          list1 = new ArrayList(); 
        list1.add(pin);
        list = list1;
      } 
    } 
    return (List)list;
  }
  
  Builder newBuilder() {
    return new Builder(this);
  }
  
  public static final class Builder {
    private final List<CertificatePinner.Pin> pins;
    
    private TrustRootIndex trustRootIndex;
    
    public Builder() {
      this.pins = new ArrayList<CertificatePinner.Pin>();
    }
    
    Builder(CertificatePinner param1CertificatePinner) {
      ArrayList<CertificatePinner.Pin> arrayList = new ArrayList();
      this.pins = arrayList;
      arrayList.addAll(param1CertificatePinner.pins);
      this.trustRootIndex = param1CertificatePinner.trustRootIndex;
    }
    
    public Builder add(String param1String, String... param1VarArgs) {
      if (param1String != null) {
        int i = param1VarArgs.length;
        for (byte b = 0; b < i; b++) {
          String str = param1VarArgs[b];
          this.pins.add(new CertificatePinner.Pin(param1String, str));
        } 
        return this;
      } 
      throw new IllegalArgumentException("pattern == null");
    }
    
    public CertificatePinner build() {
      return new CertificatePinner(this);
    }
    
    public Builder trustRootIndex(TrustRootIndex param1TrustRootIndex) {
      this.trustRootIndex = param1TrustRootIndex;
      return this;
    }
  }
  
  static final class Pin {
    final ByteString hash;
    
    final String hashAlgorithm;
    
    final String pattern;
    
    Pin(String param1String1, String param1String2) {
      this.pattern = param1String1;
      if (param1String2.startsWith("sha1/")) {
        this.hashAlgorithm = "sha1/";
        this.hash = ByteString.decodeBase64(param1String2.substring(5));
      } else if (param1String2.startsWith("sha256/")) {
        this.hashAlgorithm = "sha256/";
        this.hash = ByteString.decodeBase64(param1String2.substring(7));
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("pins must start with 'sha256/' or 'sha1/': ");
        stringBuilder1.append(param1String2);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      if (this.hash != null)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("pins must be base64: ");
      stringBuilder.append(param1String2);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof Pin) {
        String str = this.pattern;
        param1Object = param1Object;
        if (str.equals(((Pin)param1Object).pattern) && this.hashAlgorithm.equals(((Pin)param1Object).hashAlgorithm) && this.hash.equals(((Pin)param1Object).hash))
          return true; 
      } 
      return false;
    }
    
    public int hashCode() {
      return ((527 + this.pattern.hashCode()) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
    }
    
    boolean matches(String param1String) {
      boolean bool = this.pattern.equals(param1String);
      null = true;
      if (bool)
        return true; 
      int i = param1String.indexOf('.');
      if (this.pattern.startsWith("*.")) {
        String str = this.pattern;
        if (param1String.regionMatches(false, i + 1, str, 2, str.length() - 2))
          return null; 
      } 
      return false;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.hashAlgorithm);
      stringBuilder.append(this.hash.base64());
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\CertificatePinner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */