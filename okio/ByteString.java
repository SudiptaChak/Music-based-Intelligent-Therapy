package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ByteString implements Serializable, Comparable<ByteString> {
  public static final ByteString EMPTY;
  
  static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'a', 'b', 'c', 'd', 'e', 'f' };
  
  private static final long serialVersionUID = 1L;
  
  final byte[] data;
  
  transient int hashCode;
  
  transient String utf8;
  
  static {
    EMPTY = of(new byte[0]);
  }
  
  ByteString(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  public static ByteString decodeBase64(String paramString) {
    if (paramString != null) {
      byte[] arrayOfByte = Base64.decode(paramString);
      if (arrayOfByte != null) {
        ByteString byteString = new ByteString(arrayOfByte);
      } else {
        arrayOfByte = null;
      } 
      return (ByteString)arrayOfByte;
    } 
    throw new IllegalArgumentException("base64 == null");
  }
  
  public static ByteString decodeHex(String paramString) {
    if (paramString != null) {
      if (paramString.length() % 2 == 0) {
        int i = paramString.length() / 2;
        byte[] arrayOfByte = new byte[i];
        for (byte b = 0; b < i; b++) {
          int j = b * 2;
          arrayOfByte[b] = (byte)((decodeHexDigit(paramString.charAt(j)) << 4) + decodeHexDigit(paramString.charAt(j + 1)));
        } 
        return of(arrayOfByte);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected hex string: ");
      stringBuilder.append(paramString);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("hex == null");
  }
  
  private static int decodeHexDigit(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    byte b = 97;
    if (paramChar < 'a' || paramChar > 'f') {
      b = 65;
      if (paramChar < 'A' || paramChar > 'F') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected hex digit: ");
        stringBuilder.append(paramChar);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    return paramChar - b + 10;
  }
  
  private ByteString digest(String paramString) {
    try {
      return of(MessageDigest.getInstance(paramString).digest(this.data));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } 
  }
  
  public static ByteString encodeUtf8(String paramString) {
    if (paramString != null) {
      ByteString byteString = new ByteString(paramString.getBytes(Util.UTF_8));
      byteString.utf8 = paramString;
      return byteString;
    } 
    throw new IllegalArgumentException("s == null");
  }
  
  public static ByteString of(byte... paramVarArgs) {
    if (paramVarArgs != null)
      return new ByteString((byte[])paramVarArgs.clone()); 
    throw new IllegalArgumentException("data == null");
  }
  
  public static ByteString of(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      Util.checkOffsetAndCount(paramArrayOfbyte.length, paramInt1, paramInt2);
      byte[] arrayOfByte = new byte[paramInt2];
      System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, paramInt2);
      return new ByteString(arrayOfByte);
    } 
    throw new IllegalArgumentException("data == null");
  }
  
  public static ByteString read(InputStream paramInputStream, int paramInt) throws IOException {
    if (paramInputStream != null) {
      if (paramInt >= 0) {
        byte[] arrayOfByte = new byte[paramInt];
        int i = 0;
        while (i < paramInt) {
          int j = paramInputStream.read(arrayOfByte, i, paramInt - i);
          if (j != -1) {
            i += j;
            continue;
          } 
          throw new EOFException();
        } 
        return new ByteString(arrayOfByte);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("byteCount < 0: ");
      stringBuilder.append(paramInt);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("in == null");
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
    ByteString byteString = read(paramObjectInputStream, paramObjectInputStream.readInt());
    try {
      Field field = ByteString.class.getDeclaredField("data");
      field.setAccessible(true);
      field.set(this, byteString.data);
      return;
    } catch (NoSuchFieldException noSuchFieldException) {
      throw new AssertionError();
    } catch (IllegalAccessException illegalAccessException) {
      throw new AssertionError();
    } 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.writeInt(this.data.length);
    paramObjectOutputStream.write(this.data);
  }
  
  public String base64() {
    return Base64.encode(this.data);
  }
  
  public String base64Url() {
    return Base64.encodeUrl(this.data);
  }
  
  public int compareTo(ByteString paramByteString) {
    int i = size();
    int k = paramByteString.size();
    int j = Math.min(i, k);
    byte b = 0;
    while (true) {
      byte b1 = -1;
      if (b < j) {
        int n = getByte(b) & 0xFF;
        int m = paramByteString.getByte(b) & 0xFF;
        if (n == m) {
          b++;
          continue;
        } 
        if (n >= m)
          b1 = 1; 
        return b1;
      } 
      if (i == k)
        return 0; 
      if (i >= k)
        b1 = 1; 
      return b1;
    } 
  }
  
  public boolean equals(Object paramObject) {
    null = true;
    if (paramObject == this)
      return true; 
    if (paramObject instanceof ByteString) {
      ByteString byteString = (ByteString)paramObject;
      int i = byteString.size();
      paramObject = this.data;
      if (i == paramObject.length && byteString.rangeEquals(0, (byte[])paramObject, 0, paramObject.length))
        return null; 
    } 
    return false;
  }
  
  public byte getByte(int paramInt) {
    return this.data[paramInt];
  }
  
  public int hashCode() {
    int i = this.hashCode;
    if (i == 0) {
      i = Arrays.hashCode(this.data);
      this.hashCode = i;
    } 
    return i;
  }
  
  public String hex() {
    byte[] arrayOfByte = this.data;
    char[] arrayOfChar = new char[arrayOfByte.length * 2];
    int j = arrayOfByte.length;
    byte b = 0;
    int i = 0;
    while (b < j) {
      byte b1 = arrayOfByte[b];
      int k = i + 1;
      char[] arrayOfChar1 = HEX_DIGITS;
      arrayOfChar[i] = arrayOfChar1[b1 >> 4 & 0xF];
      i = k + 1;
      arrayOfChar[k] = arrayOfChar1[b1 & 0xF];
      b++;
    } 
    return new String(arrayOfChar);
  }
  
  public ByteString md5() {
    return digest("MD5");
  }
  
  public boolean rangeEquals(int paramInt1, ByteString paramByteString, int paramInt2, int paramInt3) {
    return paramByteString.rangeEquals(paramInt2, this.data, paramInt1, paramInt3);
  }
  
  public boolean rangeEquals(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
    boolean bool;
    byte[] arrayOfByte = this.data;
    if (paramInt1 <= arrayOfByte.length - paramInt3 && paramInt2 <= paramArrayOfbyte.length - paramInt3 && Util.arrayRangeEquals(arrayOfByte, paramInt1, paramArrayOfbyte, paramInt2, paramInt3)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public ByteString sha256() {
    return digest("SHA-256");
  }
  
  public int size() {
    return this.data.length;
  }
  
  public ByteString substring(int paramInt) {
    return substring(paramInt, this.data.length);
  }
  
  public ByteString substring(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0) {
      byte[] arrayOfByte = this.data;
      if (paramInt2 <= arrayOfByte.length) {
        int i = paramInt2 - paramInt1;
        if (i >= 0) {
          if (paramInt1 == 0 && paramInt2 == arrayOfByte.length)
            return this; 
          arrayOfByte = new byte[i];
          System.arraycopy(this.data, paramInt1, arrayOfByte, 0, i);
          return new ByteString(arrayOfByte);
        } 
        throw new IllegalArgumentException("endIndex < beginIndex");
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("endIndex > length(");
      stringBuilder.append(this.data.length);
      stringBuilder.append(")");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("beginIndex < 0");
  }
  
  public ByteString toAsciiLowercase() {
    int i = 0;
    while (true) {
      byte[] arrayOfByte = this.data;
      if (i < arrayOfByte.length) {
        byte b = arrayOfByte[i];
        if (b < 65 || b > 90) {
          i++;
          continue;
        } 
        arrayOfByte = (byte[])arrayOfByte.clone();
        int j = i + 1;
        arrayOfByte[i] = (byte)(b + 32);
        for (i = j; i < arrayOfByte.length; i++) {
          j = arrayOfByte[i];
          if (j >= 65 && j <= 90)
            arrayOfByte[i] = (byte)(j + 32); 
        } 
        return new ByteString(arrayOfByte);
      } 
      return this;
    } 
  }
  
  public ByteString toAsciiUppercase() {
    int i = 0;
    while (true) {
      byte[] arrayOfByte = this.data;
      if (i < arrayOfByte.length) {
        byte b = arrayOfByte[i];
        if (b < 97 || b > 122) {
          i++;
          continue;
        } 
        arrayOfByte = (byte[])arrayOfByte.clone();
        int j = i + 1;
        arrayOfByte[i] = (byte)(b - 32);
        for (i = j; i < arrayOfByte.length; i++) {
          j = arrayOfByte[i];
          if (j >= 97 && j <= 122)
            arrayOfByte[i] = (byte)(j - 32); 
        } 
        return new ByteString(arrayOfByte);
      } 
      return this;
    } 
  }
  
  public byte[] toByteArray() {
    return (byte[])this.data.clone();
  }
  
  public String toString() {
    byte[] arrayOfByte = this.data;
    return (arrayOfByte.length == 0) ? "ByteString[size=0]" : ((arrayOfByte.length <= 16) ? String.format("ByteString[size=%s data=%s]", new Object[] { Integer.valueOf(arrayOfByte.length), hex() }) : String.format("ByteString[size=%s md5=%s]", new Object[] { Integer.valueOf(arrayOfByte.length), md5().hex() }));
  }
  
  public String utf8() {
    String str = this.utf8;
    if (str == null) {
      str = new String(this.data, Util.UTF_8);
      this.utf8 = str;
    } 
    return str;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream != null) {
      paramOutputStream.write(this.data);
      return;
    } 
    throw new IllegalArgumentException("out == null");
  }
  
  void write(Buffer paramBuffer) {
    byte[] arrayOfByte = this.data;
    paramBuffer.write(arrayOfByte, 0, arrayOfByte.length);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\ByteString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */