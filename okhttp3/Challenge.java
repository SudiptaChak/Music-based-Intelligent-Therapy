package okhttp3;

import okhttp3.internal.Util;

public final class Challenge {
  private final String realm;
  
  private final String scheme;
  
  public Challenge(String paramString1, String paramString2) {
    this.scheme = paramString1;
    this.realm = paramString2;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Challenge) {
      String str = this.scheme;
      paramObject = paramObject;
      if (Util.equal(str, ((Challenge)paramObject).scheme) && Util.equal(this.realm, ((Challenge)paramObject).realm))
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    byte b;
    String str = this.realm;
    int i = 0;
    if (str != null) {
      b = str.hashCode();
    } else {
      b = 0;
    } 
    str = this.scheme;
    if (str != null)
      i = str.hashCode(); 
    return (899 + b) * 31 + i;
  }
  
  public String realm() {
    return this.realm;
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.scheme);
    stringBuilder.append(" realm=\"");
    stringBuilder.append(this.realm);
    stringBuilder.append("\"");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Challenge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */