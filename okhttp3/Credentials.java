package okhttp3;

import java.io.UnsupportedEncodingException;
import okio.ByteString;

public final class Credentials {
  public static String basic(String paramString1, String paramString2) {
    try {
      StringBuilder stringBuilder2 = new StringBuilder();
      this();
      stringBuilder2.append(paramString1);
      stringBuilder2.append(":");
      stringBuilder2.append(paramString2);
      paramString2 = ByteString.of(stringBuilder2.toString().getBytes("ISO-8859-1")).base64();
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append("Basic ");
      stringBuilder1.append(paramString2);
      return stringBuilder1.toString();
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError();
    } 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Credentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */