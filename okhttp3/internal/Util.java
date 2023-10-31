package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.IDN;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.HttpUrl;
import okio.Buffer;
import okio.ByteString;
import okio.Source;

public final class Util {
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public static final TimeZone UTC;
  
  public static final Charset UTF_8 = Charset.forName("UTF-8");
  
  private static final Pattern VERIFY_AS_IP_ADDRESS;
  
  static {
    UTC = TimeZone.getTimeZone("GMT");
    VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
  }
  
  public static void checkOffsetAndCount(long paramLong1, long paramLong2, long paramLong3) {
    if ((paramLong2 | paramLong3) >= 0L && paramLong2 <= paramLong1 && paramLong1 - paramLong2 >= paramLong3)
      return; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public static void closeAll(Closeable paramCloseable1, Closeable paramCloseable2) throws IOException {
    Exception exception;
    try {
      paramCloseable1.close();
      paramCloseable1 = null;
    } finally {}
    try {
    
    } finally {
      Exception exception1 = null;
      paramCloseable2 = paramCloseable1;
    } 
    if (exception == null)
      return; 
    if (!(exception instanceof IOException)) {
      if (!(exception instanceof RuntimeException)) {
        if (exception instanceof Error)
          throw (Error)exception; 
        throw new AssertionError(exception);
      } 
      throw (RuntimeException)exception;
    } 
    throw (IOException)exception;
  }
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(ServerSocket paramServerSocket) {
    if (paramServerSocket != null)
      try {
        paramServerSocket.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static void closeQuietly(Socket paramSocket) {
    if (paramSocket != null)
      try {
        paramSocket.close();
      } catch (AssertionError assertionError) {
        if (!isAndroidGetsocknameError(assertionError))
          throw assertionError; 
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  public static String[] concat(String[] paramArrayOfString, String paramString) {
    int i = paramArrayOfString.length + 1;
    String[] arrayOfString = new String[i];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    arrayOfString[i - 1] = paramString;
    return arrayOfString;
  }
  
  public static boolean contains(String[] paramArrayOfString, String paramString) {
    return Arrays.<String>asList(paramArrayOfString).contains(paramString);
  }
  
  private static boolean containsInvalidHostnameAsciiCodes(String paramString) {
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c <= '\037' || c >= '')
        return true; 
      if (" #%/:?@[\\]".indexOf(c) != -1)
        return true; 
    } 
    return false;
  }
  
  public static int delimiterOffset(String paramString, int paramInt1, int paramInt2, char paramChar) {
    while (paramInt1 < paramInt2) {
      if (paramString.charAt(paramInt1) == paramChar)
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static int delimiterOffset(String paramString1, int paramInt1, int paramInt2, String paramString2) {
    while (paramInt1 < paramInt2) {
      if (paramString2.indexOf(paramString1.charAt(paramInt1)) != -1)
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static boolean discard(Source paramSource, int paramInt, TimeUnit paramTimeUnit) {
    try {
      return skipAll(paramSource, paramInt, paramTimeUnit);
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public static String domainToAscii(String paramString) {
    try {
      paramString = IDN.toASCII(paramString).toLowerCase(Locale.US);
      if (paramString.isEmpty())
        return null; 
      boolean bool = containsInvalidHostnameAsciiCodes(paramString);
      return bool ? null : paramString;
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  public static boolean equal(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public static String hostHeader(HttpUrl paramHttpUrl, boolean paramBoolean) {
    String str;
    if (paramHttpUrl.host().contains(":")) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("[");
      stringBuilder1.append(paramHttpUrl.host());
      stringBuilder1.append("]");
      str = stringBuilder1.toString();
    } else {
      str = paramHttpUrl.host();
    } 
    if (!paramBoolean) {
      String str1 = str;
      if (paramHttpUrl.port() != HttpUrl.defaultPort(paramHttpUrl.scheme())) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(":");
        stringBuilder1.append(paramHttpUrl.port());
        return stringBuilder1.toString();
      } 
      return str1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(":");
    stringBuilder.append(paramHttpUrl.port());
    return stringBuilder.toString();
  }
  
  public static <T> List<T> immutableList(List<T> paramList) {
    return Collections.unmodifiableList(new ArrayList<T>(paramList));
  }
  
  public static <T> List<T> immutableList(T... paramVarArgs) {
    return Collections.unmodifiableList(Arrays.asList((T[])paramVarArgs.clone()));
  }
  
  public static <K, V> Map<K, V> immutableMap(Map<K, V> paramMap) {
    return Collections.unmodifiableMap(new LinkedHashMap<K, V>(paramMap));
  }
  
  private static <T> List<T> intersect(T[] paramArrayOfT1, T[] paramArrayOfT2) {
    ArrayList<T> arrayList = new ArrayList();
    int i = paramArrayOfT1.length;
    for (byte b = 0; b < i; b++) {
      T t = paramArrayOfT1[b];
      int j = paramArrayOfT2.length;
      for (byte b1 = 0; b1 < j; b1++) {
        T t1 = paramArrayOfT2[b1];
        if (t.equals(t1)) {
          arrayList.add(t1);
          break;
        } 
      } 
    } 
    return arrayList;
  }
  
  public static <T> T[] intersect(Class<T> paramClass, T[] paramArrayOfT1, T[] paramArrayOfT2) {
    List<T> list = intersect(paramArrayOfT1, paramArrayOfT2);
    return list.toArray((T[])Array.newInstance(paramClass, list.size()));
  }
  
  public static boolean isAndroidGetsocknameError(AssertionError paramAssertionError) {
    boolean bool;
    if (paramAssertionError.getCause() != null && paramAssertionError.getMessage() != null && paramAssertionError.getMessage().contains("getsockname failed")) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String md5Hex(String paramString) {
    try {
      return ByteString.of(MessageDigest.getInstance("MD5").digest(paramString.getBytes("UTF-8"))).hex();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
    
    } catch (UnsupportedEncodingException unsupportedEncodingException) {}
    throw new AssertionError(unsupportedEncodingException);
  }
  
  public static ByteString sha1(ByteString paramByteString) {
    try {
      return ByteString.of(MessageDigest.getInstance("SHA-1").digest(paramByteString.toByteArray()));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } 
  }
  
  public static ByteString sha256(ByteString paramByteString) {
    try {
      return ByteString.of(MessageDigest.getInstance("SHA-256").digest(paramByteString.toByteArray()));
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new AssertionError(noSuchAlgorithmException);
    } 
  }
  
  public static String shaBase64(String paramString) {
    try {
      return ByteString.of(MessageDigest.getInstance("SHA-1").digest(paramString.getBytes("UTF-8"))).base64();
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
    
    } catch (UnsupportedEncodingException unsupportedEncodingException) {}
    throw new AssertionError(unsupportedEncodingException);
  }
  
  public static boolean skipAll(Source paramSource, int paramInt, TimeUnit paramTimeUnit) throws IOException {
    long l1;
    long l2 = System.nanoTime();
    if (paramSource.timeout().hasDeadline()) {
      l1 = paramSource.timeout().deadlineNanoTime() - l2;
    } else {
      l1 = Long.MAX_VALUE;
    } 
    paramSource.timeout().deadlineNanoTime(Math.min(l1, paramTimeUnit.toNanos(paramInt)) + l2);
    try {
      Buffer buffer = new Buffer();
      this();
      while (paramSource.read(buffer, 2048L) != -1L)
        buffer.clear(); 
      return true;
    } catch (InterruptedIOException interruptedIOException) {
      return false;
    } finally {
      if (l1 == Long.MAX_VALUE) {
        paramSource.timeout().clearDeadline();
      } else {
        paramSource.timeout().deadlineNanoTime(l2 + l1);
      } 
    } 
  }
  
  public static int skipLeadingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    while (paramInt1 < paramInt2) {
      char c = paramString.charAt(paramInt1);
      if (c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != ' ')
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  public static int skipTrailingAsciiWhitespace(String paramString, int paramInt1, int paramInt2) {
    while (--paramInt2 >= paramInt1) {
      char c = paramString.charAt(paramInt2);
      if (c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != ' ')
        return paramInt2 + 1; 
      paramInt2--;
    } 
    return paramInt1;
  }
  
  public static ThreadFactory threadFactory(final String name, final boolean daemon) {
    return new ThreadFactory() {
        final boolean val$daemon;
        
        final String val$name;
        
        public Thread newThread(Runnable param1Runnable) {
          param1Runnable = new Thread(param1Runnable, name);
          param1Runnable.setDaemon(daemon);
          return (Thread)param1Runnable;
        }
      };
  }
  
  public static String toHumanReadableAscii(String paramString) {
    String str;
    int j = paramString.length();
    int i = 0;
    while (true) {
      str = paramString;
      if (i < j) {
        int k = paramString.codePointAt(i);
        if (k > 31 && k < 127) {
          i += Character.charCount(k);
          continue;
        } 
        Buffer buffer = new Buffer();
        buffer.writeUtf8(paramString, 0, i);
        while (i < j) {
          int m = paramString.codePointAt(i);
          if (m > 31 && m < 127) {
            k = m;
          } else {
            k = 63;
          } 
          buffer.writeUtf8CodePoint(k);
          i += Character.charCount(m);
        } 
        str = buffer.readUtf8();
      } 
      break;
    } 
    return str;
  }
  
  public static String trimSubstring(String paramString, int paramInt1, int paramInt2) {
    paramInt1 = skipLeadingAsciiWhitespace(paramString, paramInt1, paramInt2);
    return paramString.substring(paramInt1, skipTrailingAsciiWhitespace(paramString, paramInt1, paramInt2));
  }
  
  public static boolean verifyAsIpAddress(String paramString) {
    return VERIFY_AS_IP_ADDRESS.matcher(paramString).matches();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */