package okio;

import java.io.UnsupportedEncodingException;

final class Base64 {
  private static final byte[] MAP = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  private static final byte[] URL_MAP = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  public static byte[] decode(String paramString) {
    int k;
    for (k = paramString.length(); k > 0; k--) {
      char c = paramString.charAt(k - 1);
      if (c != '=' && c != '\n' && c != '\r' && c != ' ' && c != '\t')
        break; 
    } 
    int i1 = (int)(k * 6L / 8L);
    byte[] arrayOfByte2 = new byte[i1];
    int m = 0;
    byte b = 0;
    int i = b;
    int j = i;
    int n = i;
    while (true) {
      byte b1;
      int i2;
      if (m < k) {
        char c = paramString.charAt(m);
        if (c >= 'A' && c <= 'Z') {
          i = c - 65;
        } else if (c >= 'a' && c <= 'z') {
          i = c - 71;
        } else if (c >= '0' && c <= '9') {
          i = c + 4;
        } else if (c == '+' || c == '-') {
          i = 62;
        } else if (c == '/' || c == '_') {
          i = 63;
        } else {
          byte b2 = b;
          i = n;
          int i3 = j;
          if (c != '\n') {
            b2 = b;
            i = n;
            i3 = j;
            if (c != '\r') {
              b2 = b;
              i = n;
              i3 = j;
              if (c != ' ')
                if (c == '\t') {
                  b2 = b;
                  i = n;
                  i3 = j;
                } else {
                  return null;
                }  
            } 
          } 
          m++;
          b = b2;
          n = i;
          j = i3;
        } 
        n = n << 6 | (byte)i;
        b1 = ++b;
        i = n;
        i2 = j;
        if (b % 4 == 0) {
          i = j + 1;
          arrayOfByte2[j] = (byte)(n >> 16);
          j = i + 1;
          arrayOfByte2[i] = (byte)(n >> 8);
          arrayOfByte2[j] = (byte)n;
          i2 = j + 1;
          i = n;
          b1 = b;
        } 
      } else {
        break;
      } 
      m++;
      b = b1;
      n = i;
      j = i2;
    } 
    k = b % 4;
    if (k == 1)
      return null; 
    if (k == 2) {
      arrayOfByte2[j] = (byte)(n << 12 >> 16);
      i = j + 1;
    } else {
      i = j;
      if (k == 3) {
        m = n << 6;
        k = j + 1;
        arrayOfByte2[j] = (byte)(m >> 16);
        i = k + 1;
        arrayOfByte2[k] = (byte)(m >> 8);
      } 
    } 
    if (i == i1)
      return arrayOfByte2; 
    byte[] arrayOfByte1 = new byte[i];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
    return arrayOfByte1;
  }
  
  public static String encode(byte[] paramArrayOfbyte) {
    return encode(paramArrayOfbyte, MAP);
  }
  
  private static String encode(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    byte[] arrayOfByte = new byte[(paramArrayOfbyte1.length + 2) * 4 / 3];
    int k = paramArrayOfbyte1.length - paramArrayOfbyte1.length % 3;
    int j = 0;
    int i = 0;
    while (j < k) {
      int m = i + 1;
      arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[j] & 0xFF) >> 2];
      i = m + 1;
      byte b = paramArrayOfbyte1[j];
      int n = j + 1;
      arrayOfByte[m] = paramArrayOfbyte2[(b & 0x3) << 4 | (paramArrayOfbyte1[n] & 0xFF) >> 4];
      m = i + 1;
      b = paramArrayOfbyte1[n];
      n = j + 2;
      arrayOfByte[i] = paramArrayOfbyte2[(b & 0xF) << 2 | (paramArrayOfbyte1[n] & 0xFF) >> 6];
      i = m + 1;
      arrayOfByte[m] = paramArrayOfbyte2[paramArrayOfbyte1[n] & 0x3F];
      j += 3;
    } 
    j = paramArrayOfbyte1.length % 3;
    if (j != 1) {
      if (j == 2) {
        j = i + 1;
        arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xFF) >> 2];
        i = j + 1;
        byte b = paramArrayOfbyte1[k];
        arrayOfByte[j] = paramArrayOfbyte2[(paramArrayOfbyte1[++k] & 0xFF) >> 4 | (b & 0x3) << 4];
        j = i + 1;
        arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xF) << 2];
        i = j + 1;
        arrayOfByte[j] = 61;
      } 
    } else {
      j = i + 1;
      arrayOfByte[i] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0xFF) >> 2];
      i = j + 1;
      arrayOfByte[j] = paramArrayOfbyte2[(paramArrayOfbyte1[k] & 0x3) << 4];
      j = i + 1;
      arrayOfByte[i] = 61;
      i = j + 1;
      arrayOfByte[j] = 61;
    } 
    try {
      return new String(arrayOfByte, 0, i, "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new AssertionError(unsupportedEncodingException);
    } 
  }
  
  public static String encodeUrl(byte[] paramArrayOfbyte) {
    return encode(paramArrayOfbyte, URL_MAP);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */