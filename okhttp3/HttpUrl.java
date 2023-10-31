package okhttp3;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import okhttp3.internal.Util;
import okio.Buffer;

public final class HttpUrl {
  static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
  
  static final String FRAGMENT_ENCODE_SET = "";
  
  static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
  
  private static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
  
  static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
  
  static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
  
  static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
  
  static final String QUERY_ENCODE_SET = " \"'<>#";
  
  static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
  
  private final String fragment;
  
  private final String host;
  
  private final String password;
  
  private final List<String> pathSegments;
  
  private final int port;
  
  private final List<String> queryNamesAndValues;
  
  private final String scheme;
  
  private final String url;
  
  private final String username;
  
  private HttpUrl(Builder paramBuilder) {
    String str;
    this.scheme = paramBuilder.scheme;
    this.username = percentDecode(paramBuilder.encodedUsername, false);
    this.password = percentDecode(paramBuilder.encodedPassword, false);
    this.host = paramBuilder.host;
    this.port = paramBuilder.effectivePort();
    this.pathSegments = percentDecode(paramBuilder.encodedPathSegments, false);
    List<String> list = paramBuilder.encodedQueryNamesAndValues;
    List list1 = null;
    if (list != null) {
      list = percentDecode(paramBuilder.encodedQueryNamesAndValues, true);
    } else {
      list = null;
    } 
    this.queryNamesAndValues = list;
    list = list1;
    if (paramBuilder.encodedFragment != null)
      str = percentDecode(paramBuilder.encodedFragment, false); 
    this.fragment = str;
    this.url = paramBuilder.toString();
  }
  
  static String canonicalize(String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    for (int i = paramInt1; i < paramInt2; i += Character.charCount(j)) {
      int j = paramString1.codePointAt(i);
      if (j < 32 || j == 127 || (j >= 128 && paramBoolean4) || paramString2.indexOf(j) != -1 || (j == 37 && (!paramBoolean1 || (paramBoolean2 && !percentEncoded(paramString1, i, paramInt2)))) || (j == 43 && paramBoolean3)) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(paramString1, paramInt1, i);
        canonicalize(buffer, paramString1, i, paramInt2, paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
        return buffer.readUtf8();
      } 
    } 
    return paramString1.substring(paramInt1, paramInt2);
  }
  
  static String canonicalize(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    return canonicalize(paramString1, 0, paramString1.length(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
  }
  
  static void canonicalize(Buffer paramBuffer, String paramString1, int paramInt1, int paramInt2, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    for (Object object = null; paramInt1 < paramInt2; object = SYNTHETIC_LOCAL_VARIABLE_13) {
      int i = paramString1.codePointAt(paramInt1);
      if (paramBoolean1) {
        Object object1 = object;
        if (i != 9) {
          object1 = object;
          if (i != 10) {
            object1 = object;
            if (i != 12) {
              if (i == 13) {
                object1 = object;
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            continue;
          } 
        } else {
          continue;
        } 
      } 
      if (i == 43 && paramBoolean3) {
        String str;
        if (paramBoolean1) {
          str = "+";
        } else {
          str = "%2B";
        } 
        paramBuffer.writeUtf8(str);
        Object object1 = object;
      } else if (i < 32 || i == 127 || (i >= 128 && paramBoolean4) || paramString2.indexOf(i) != -1 || (i == 37 && (!paramBoolean1 || (paramBoolean2 && !percentEncoded(paramString1, paramInt1, paramInt2))))) {
        Object object1 = object;
        if (object == null)
          object1 = new Buffer(); 
        object1.writeUtf8CodePoint(i);
        while (true) {
          Object object2 = object1;
          if (!object1.exhausted()) {
            int j = object1.readByte() & 0xFF;
            paramBuffer.writeByte(37);
            paramBuffer.writeByte(HEX_DIGITS[j >> 4 & 0xF]);
            paramBuffer.writeByte(HEX_DIGITS[j & 0xF]);
            continue;
          } 
          break;
        } 
      } else {
        paramBuffer.writeUtf8CodePoint(i);
        Object object1 = object;
      } 
      continue;
      paramInt1 += Character.charCount(SYNTHETIC_LOCAL_VARIABLE_9);
    } 
  }
  
  static int decodeHexDigit(char paramChar) {
    if (paramChar >= '0' && paramChar <= '9')
      return paramChar - 48; 
    byte b = 97;
    if (paramChar < 'a' || paramChar > 'f') {
      b = 65;
      if (paramChar < 'A' || paramChar > 'F')
        return -1; 
    } 
    return paramChar - b + 10;
  }
  
  public static int defaultPort(String paramString) {
    return paramString.equals("http") ? 80 : (paramString.equals("https") ? 443 : -1);
  }
  
  public static HttpUrl get(URI paramURI) {
    return parse(paramURI.toString());
  }
  
  public static HttpUrl get(URL paramURL) {
    return parse(paramURL.toString());
  }
  
  static HttpUrl getChecked(String paramString) throws MalformedURLException, UnknownHostException {
    StringBuilder stringBuilder;
    Builder builder = new Builder();
    Builder.ParseResult parseResult = builder.parse(null, paramString);
    int i = null.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[parseResult.ordinal()];
    if (i != 1) {
      if (i != 2) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid URL: ");
        stringBuilder.append(parseResult);
        stringBuilder.append(" for ");
        stringBuilder.append(paramString);
        throw new MalformedURLException(stringBuilder.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Invalid host: ");
      stringBuilder1.append(paramString);
      throw new UnknownHostException(stringBuilder1.toString());
    } 
    return stringBuilder.build();
  }
  
  static void namesAndValuesToQueryString(StringBuilder paramStringBuilder, List<String> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b += 2) {
      String str2 = paramList.get(b);
      String str1 = paramList.get(b + 1);
      if (b > 0)
        paramStringBuilder.append('&'); 
      paramStringBuilder.append(str2);
      if (str1 != null) {
        paramStringBuilder.append('=');
        paramStringBuilder.append(str1);
      } 
    } 
  }
  
  public static HttpUrl parse(String paramString) {
    Builder builder = new Builder();
    HttpUrl httpUrl = null;
    if (builder.parse(null, paramString) == Builder.ParseResult.SUCCESS)
      httpUrl = builder.build(); 
    return httpUrl;
  }
  
  static void pathSegmentsToString(StringBuilder paramStringBuilder, List<String> paramList) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++) {
      paramStringBuilder.append('/');
      paramStringBuilder.append(paramList.get(b));
    } 
  }
  
  static String percentDecode(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    for (int i = paramInt1; i < paramInt2; i++) {
      char c = paramString.charAt(i);
      if (c == '%' || (c == '+' && paramBoolean)) {
        Buffer buffer = new Buffer();
        buffer.writeUtf8(paramString, paramInt1, i);
        percentDecode(buffer, paramString, i, paramInt2, paramBoolean);
        return buffer.readUtf8();
      } 
    } 
    return paramString.substring(paramInt1, paramInt2);
  }
  
  static String percentDecode(String paramString, boolean paramBoolean) {
    return percentDecode(paramString, 0, paramString.length(), paramBoolean);
  }
  
  private List<String> percentDecode(List<String> paramList, boolean paramBoolean) {
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    for (String str : paramList) {
      if (str != null) {
        str = percentDecode(str, paramBoolean);
      } else {
        str = null;
      } 
      arrayList.add(str);
    } 
    return Collections.unmodifiableList(arrayList);
  }
  
  static void percentDecode(Buffer paramBuffer, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: iload_3
    //   2: if_icmpge -> 123
    //   5: aload_1
    //   6: iload_2
    //   7: invokevirtual codePointAt : (I)I
    //   10: istore #6
    //   12: iload #6
    //   14: bipush #37
    //   16: if_icmpne -> 83
    //   19: iload_2
    //   20: iconst_2
    //   21: iadd
    //   22: istore #5
    //   24: iload #5
    //   26: iload_3
    //   27: if_icmpge -> 83
    //   30: aload_1
    //   31: iload_2
    //   32: iconst_1
    //   33: iadd
    //   34: invokevirtual charAt : (I)C
    //   37: invokestatic decodeHexDigit : (C)I
    //   40: istore #8
    //   42: aload_1
    //   43: iload #5
    //   45: invokevirtual charAt : (I)C
    //   48: invokestatic decodeHexDigit : (C)I
    //   51: istore #7
    //   53: iload #8
    //   55: iconst_m1
    //   56: if_icmpeq -> 105
    //   59: iload #7
    //   61: iconst_m1
    //   62: if_icmpeq -> 105
    //   65: aload_0
    //   66: iload #8
    //   68: iconst_4
    //   69: ishl
    //   70: iload #7
    //   72: iadd
    //   73: invokevirtual writeByte : (I)Lokio/Buffer;
    //   76: pop
    //   77: iload #5
    //   79: istore_2
    //   80: goto -> 112
    //   83: iload #6
    //   85: bipush #43
    //   87: if_icmpne -> 105
    //   90: iload #4
    //   92: ifeq -> 105
    //   95: aload_0
    //   96: bipush #32
    //   98: invokevirtual writeByte : (I)Lokio/Buffer;
    //   101: pop
    //   102: goto -> 112
    //   105: aload_0
    //   106: iload #6
    //   108: invokevirtual writeUtf8CodePoint : (I)Lokio/Buffer;
    //   111: pop
    //   112: iload_2
    //   113: iload #6
    //   115: invokestatic charCount : (I)I
    //   118: iadd
    //   119: istore_2
    //   120: goto -> 0
    //   123: return
  }
  
  static boolean percentEncoded(String paramString, int paramInt1, int paramInt2) {
    int i = paramInt1 + 2;
    boolean bool = true;
    if (i >= paramInt2 || paramString.charAt(paramInt1) != '%' || decodeHexDigit(paramString.charAt(paramInt1 + 1)) == -1 || decodeHexDigit(paramString.charAt(i)) == -1)
      bool = false; 
    return bool;
  }
  
  static List<String> queryStringToNamesAndValues(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    for (int i = 0; i <= paramString.length(); i = j + 1) {
      int k = paramString.indexOf('&', i);
      int j = k;
      if (k == -1)
        j = paramString.length(); 
      k = paramString.indexOf('=', i);
      if (k == -1 || k > j) {
        arrayList.add(paramString.substring(i, j));
        arrayList.add(null);
      } else {
        arrayList.add(paramString.substring(i, k));
        arrayList.add(paramString.substring(k + 1, j));
      } 
    } 
    return arrayList;
  }
  
  public String encodedFragment() {
    if (this.fragment == null)
      return null; 
    int i = this.url.indexOf('#');
    return this.url.substring(i + 1);
  }
  
  public String encodedPassword() {
    if (this.password.isEmpty())
      return ""; 
    int j = this.url.indexOf(':', this.scheme.length() + 3);
    int i = this.url.indexOf('@');
    return this.url.substring(j + 1, i);
  }
  
  public String encodedPath() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), "?#");
    return this.url.substring(i, j);
  }
  
  public List<String> encodedPathSegments() {
    int i = this.url.indexOf('/', this.scheme.length() + 3);
    String str = this.url;
    int j = Util.delimiterOffset(str, i, str.length(), "?#");
    ArrayList<String> arrayList = new ArrayList();
    while (i < j) {
      int k = i + 1;
      i = Util.delimiterOffset(this.url, k, j, '/');
      arrayList.add(this.url.substring(k, i));
    } 
    return arrayList;
  }
  
  public String encodedQuery() {
    if (this.queryNamesAndValues == null)
      return null; 
    int i = this.url.indexOf('?') + 1;
    String str = this.url;
    int j = Util.delimiterOffset(str, i + 1, str.length(), '#');
    return this.url.substring(i, j);
  }
  
  public String encodedUsername() {
    if (this.username.isEmpty())
      return ""; 
    int j = this.scheme.length() + 3;
    String str = this.url;
    int i = Util.delimiterOffset(str, j, str.length(), ":@");
    return this.url.substring(j, i);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject instanceof HttpUrl && ((HttpUrl)paramObject).url.equals(this.url)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public String fragment() {
    return this.fragment;
  }
  
  public int hashCode() {
    return this.url.hashCode();
  }
  
  public String host() {
    return this.host;
  }
  
  public boolean isHttps() {
    return this.scheme.equals("https");
  }
  
  public Builder newBuilder() {
    byte b;
    Builder builder = new Builder();
    builder.scheme = this.scheme;
    builder.encodedUsername = encodedUsername();
    builder.encodedPassword = encodedPassword();
    builder.host = this.host;
    if (this.port != defaultPort(this.scheme)) {
      b = this.port;
    } else {
      b = -1;
    } 
    builder.port = b;
    builder.encodedPathSegments.clear();
    builder.encodedPathSegments.addAll(encodedPathSegments());
    builder.encodedQuery(encodedQuery());
    builder.encodedFragment = encodedFragment();
    return builder;
  }
  
  public Builder newBuilder(String paramString) {
    Builder builder = new Builder();
    if (builder.parse(this, paramString) == Builder.ParseResult.SUCCESS) {
      Builder builder1 = builder;
    } else {
      paramString = null;
    } 
    return (Builder)paramString;
  }
  
  public String password() {
    return this.password;
  }
  
  public List<String> pathSegments() {
    return this.pathSegments;
  }
  
  public int pathSize() {
    return this.pathSegments.size();
  }
  
  public int port() {
    return this.port;
  }
  
  public String query() {
    if (this.queryNamesAndValues == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder();
    namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
    return stringBuilder.toString();
  }
  
  public String queryParameter(String paramString) {
    List<String> list = this.queryNamesAndValues;
    if (list == null)
      return null; 
    byte b = 0;
    int i = list.size();
    while (b < i) {
      if (paramString.equals(this.queryNamesAndValues.get(b)))
        return this.queryNamesAndValues.get(b + 1); 
      b += 2;
    } 
    return null;
  }
  
  public String queryParameterName(int paramInt) {
    return this.queryNamesAndValues.get(paramInt * 2);
  }
  
  public Set<String> queryParameterNames() {
    if (this.queryNamesAndValues == null)
      return Collections.emptySet(); 
    LinkedHashSet<? extends String> linkedHashSet = new LinkedHashSet();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      linkedHashSet.add(this.queryNamesAndValues.get(b));
      b += 2;
    } 
    return Collections.unmodifiableSet(linkedHashSet);
  }
  
  public String queryParameterValue(int paramInt) {
    return this.queryNamesAndValues.get(paramInt * 2 + 1);
  }
  
  public List<String> queryParameterValues(String paramString) {
    if (this.queryNamesAndValues == null)
      return Collections.emptyList(); 
    ArrayList<? extends String> arrayList = new ArrayList();
    byte b = 0;
    int i = this.queryNamesAndValues.size();
    while (b < i) {
      if (paramString.equals(this.queryNamesAndValues.get(b)))
        arrayList.add(this.queryNamesAndValues.get(b + 1)); 
      b += 2;
    } 
    return Collections.unmodifiableList(arrayList);
  }
  
  public int querySize() {
    boolean bool;
    List<String> list = this.queryNamesAndValues;
    if (list != null) {
      bool = list.size() / 2;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public HttpUrl resolve(String paramString) {
    Builder builder = newBuilder(paramString);
    if (builder != null) {
      HttpUrl httpUrl = builder.build();
    } else {
      builder = null;
    } 
    return (HttpUrl)builder;
  }
  
  public String scheme() {
    return this.scheme;
  }
  
  public String toString() {
    return this.url;
  }
  
  public URI uri() {
    String str = newBuilder().reencodeForUri().toString();
    try {
      return new URI(str);
    } catch (URISyntaxException uRISyntaxException) {
      try {
        return URI.create(str.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
      } catch (Exception exception) {
        throw new RuntimeException(uRISyntaxException);
      } 
    } 
  }
  
  public URL url() {
    try {
      return new URL(this.url);
    } catch (MalformedURLException malformedURLException) {
      throw new RuntimeException(malformedURLException);
    } 
  }
  
  public String username() {
    return this.username;
  }
  
  public static final class Builder {
    String encodedFragment;
    
    String encodedPassword = "";
    
    final List<String> encodedPathSegments;
    
    List<String> encodedQueryNamesAndValues;
    
    String encodedUsername = "";
    
    String host;
    
    int port = -1;
    
    String scheme;
    
    public Builder() {
      ArrayList<String> arrayList = new ArrayList();
      this.encodedPathSegments = arrayList;
      arrayList.add("");
    }
    
    private Builder addPathSegments(String param1String, boolean param1Boolean) {
      int i = 0;
      while (true) {
        boolean bool;
        int j = Util.delimiterOffset(param1String, i, param1String.length(), "/\\");
        if (j < param1String.length()) {
          bool = true;
        } else {
          bool = false;
        } 
        push(param1String, i, j, bool, param1Boolean);
        i = ++j;
        if (j > param1String.length())
          return this; 
      } 
    }
    
    private static String canonicalizeHost(String param1String, int param1Int1, int param1Int2) {
      byte[] arrayOfByte;
      param1String = HttpUrl.percentDecode(param1String, param1Int1, param1Int2, false);
      if (param1String.contains(":")) {
        InetAddress inetAddress;
        if (param1String.startsWith("[") && param1String.endsWith("]")) {
          inetAddress = decodeIpv6(param1String, 1, param1String.length() - 1);
        } else {
          inetAddress = decodeIpv6((String)inetAddress, 0, inetAddress.length());
        } 
        if (inetAddress == null)
          return null; 
        arrayOfByte = inetAddress.getAddress();
        if (arrayOfByte.length == 16)
          return inet6AddressToAscii(arrayOfByte); 
        throw new AssertionError();
      } 
      return Util.domainToAscii((String)arrayOfByte);
    }
    
    private static boolean decodeIpv4Suffix(String param1String, int param1Int1, int param1Int2, byte[] param1ArrayOfbyte, int param1Int3) {
      int j = param1Int3;
      int i = param1Int1;
      while (i < param1Int2) {
        if (j == param1ArrayOfbyte.length)
          return false; 
        param1Int1 = i;
        if (j != param1Int3) {
          if (param1String.charAt(i) != '.')
            return false; 
          param1Int1 = i + 1;
        } 
        i = param1Int1;
        int k = 0;
        while (i < param1Int2) {
          char c = param1String.charAt(i);
          if (c < '0' || c > '9')
            break; 
          if (!k && param1Int1 != i)
            return false; 
          k = k * 10 + c - 48;
          if (k > 255)
            return false; 
          i++;
        } 
        if (i - param1Int1 == 0)
          return false; 
        param1ArrayOfbyte[j] = (byte)k;
        j++;
      } 
      return !(j != param1Int3 + 4);
    }
    
    private static InetAddress decodeIpv6(String param1String, int param1Int1, int param1Int2) {
      int j;
      int n;
      byte[] arrayOfByte = new byte[16];
      int k = -1;
      int m = -1;
      int i = 0;
      while (true) {
        j = i;
        n = k;
        if (param1Int1 < param1Int2) {
          if (i == 16)
            return null; 
          n = param1Int1 + 2;
          if (n <= param1Int2 && param1String.regionMatches(param1Int1, "::", 0, 2)) {
            if (k != -1)
              return null; 
            j = i + 2;
            param1Int1 = j;
            if (n == param1Int2) {
              n = param1Int1;
              break;
            } 
            m = n;
            i = j;
            k = param1Int1;
            param1Int1 = m;
          } else {
            j = param1Int1;
            if (i != 0)
              if (param1String.regionMatches(param1Int1, ":", 0, 1)) {
                j = param1Int1 + 1;
              } else {
                if (param1String.regionMatches(param1Int1, ".", 0, 1)) {
                  if (!decodeIpv4Suffix(param1String, m, param1Int2, arrayOfByte, i - 2))
                    return null; 
                  j = i + 2;
                  n = k;
                  break;
                } 
                return null;
              }  
            param1Int1 = j;
          } 
          m = 0;
          for (j = param1Int1; j < param1Int2; j++) {
            n = HttpUrl.decodeHexDigit(param1String.charAt(j));
            if (n == -1)
              break; 
            m = (m << 4) + n;
          } 
          n = j - param1Int1;
          if (n == 0 || n > 4)
            return null; 
          n = i + 1;
          arrayOfByte[i] = (byte)(m >>> 8 & 0xFF);
          i = n + 1;
          arrayOfByte[n] = (byte)(m & 0xFF);
          m = param1Int1;
          param1Int1 = j;
          continue;
        } 
        break;
      } 
      if (j != 16) {
        if (n == -1)
          return null; 
        param1Int1 = j - n;
        System.arraycopy(arrayOfByte, n, arrayOfByte, 16 - param1Int1, param1Int1);
        Arrays.fill(arrayOfByte, n, 16 - j + n, (byte)0);
      } 
      try {
        return InetAddress.getByAddress(arrayOfByte);
      } catch (UnknownHostException unknownHostException) {
        throw new AssertionError();
      } 
    }
    
    private static String inet6AddressToAscii(byte[] param1ArrayOfbyte) {
      boolean bool = false;
      int k = -1;
      int i = 0;
      int j;
      for (j = 0; i < param1ArrayOfbyte.length; j = n) {
        int m;
        for (m = i; m < 16 && param1ArrayOfbyte[m] == 0 && param1ArrayOfbyte[m + 1] == 0; m += 2);
        int i1 = m - i;
        int n = j;
        if (i1 > j) {
          n = i1;
          k = i;
        } 
        i = m + 2;
      } 
      Buffer buffer = new Buffer();
      for (i = bool; i < param1ArrayOfbyte.length; i += 2) {
        if (i == k) {
          buffer.writeByte(58);
          int m = i + j;
          i = m;
          if (m == 16) {
            buffer.writeByte(58);
            i = m;
          } 
          continue;
        } 
        if (i > 0)
          buffer.writeByte(58); 
        buffer.writeHexadecimalUnsignedLong(((param1ArrayOfbyte[i] & 0xFF) << 8 | param1ArrayOfbyte[i + 1] & 0xFF));
      } 
      return buffer.readUtf8();
    }
    
    private boolean isDot(String param1String) {
      return (param1String.equals(".") || param1String.equalsIgnoreCase("%2e"));
    }
    
    private boolean isDotDot(String param1String) {
      return (param1String.equals("..") || param1String.equalsIgnoreCase("%2e.") || param1String.equalsIgnoreCase(".%2e") || param1String.equalsIgnoreCase("%2e%2e"));
    }
    
    private static int parsePort(String param1String, int param1Int1, int param1Int2) {
      try {
        param1Int1 = Integer.parseInt(HttpUrl.canonicalize(param1String, param1Int1, param1Int2, "", false, false, false, true));
        if (param1Int1 > 0 && param1Int1 <= 65535)
          return param1Int1; 
      } catch (NumberFormatException numberFormatException) {}
      return -1;
    }
    
    private void pop() {
      List<String> list = this.encodedPathSegments;
      if (((String)list.remove(list.size() - 1)).isEmpty() && !this.encodedPathSegments.isEmpty()) {
        list = this.encodedPathSegments;
        list.set(list.size() - 1, "");
      } else {
        this.encodedPathSegments.add("");
      } 
    }
    
    private static int portColonOffset(String param1String, int param1Int1, int param1Int2) {
      while (param1Int1 < param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c != ':') {
          int i = param1Int1;
          if (c != '[') {
            i = param1Int1;
          } else {
            while (true) {
              param1Int1 = i + 1;
              i = param1Int1;
              if (param1Int1 < param1Int2) {
                i = param1Int1;
                if (param1String.charAt(param1Int1) == ']') {
                  i = param1Int1;
                  break;
                } 
                continue;
              } 
              break;
            } 
          } 
          param1Int1 = i + 1;
          continue;
        } 
        return param1Int1;
      } 
      return param1Int2;
    }
    
    private void push(String param1String, int param1Int1, int param1Int2, boolean param1Boolean1, boolean param1Boolean2) {
      param1String = HttpUrl.canonicalize(param1String, param1Int1, param1Int2, " \"<>^`{}|/\\?#", param1Boolean2, false, false, true);
      if (isDot(param1String))
        return; 
      if (isDotDot(param1String)) {
        pop();
        return;
      } 
      List<String> list = this.encodedPathSegments;
      if (((String)list.get(list.size() - 1)).isEmpty()) {
        list = this.encodedPathSegments;
        list.set(list.size() - 1, param1String);
      } else {
        this.encodedPathSegments.add(param1String);
      } 
      if (param1Boolean1)
        this.encodedPathSegments.add(""); 
    }
    
    private void removeAllCanonicalQueryParameters(String param1String) {
      for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
        if (param1String.equals(this.encodedQueryNamesAndValues.get(i))) {
          this.encodedQueryNamesAndValues.remove(i + 1);
          this.encodedQueryNamesAndValues.remove(i);
          if (this.encodedQueryNamesAndValues.isEmpty()) {
            this.encodedQueryNamesAndValues = null;
            return;
          } 
        } 
      } 
    }
    
    private void resolvePath(String param1String, int param1Int1, int param1Int2) {
      if (param1Int1 == param1Int2)
        return; 
      char c = param1String.charAt(param1Int1);
      if (c == '/' || c == '\\') {
        this.encodedPathSegments.clear();
        this.encodedPathSegments.add("");
      } else {
        List<String> list = this.encodedPathSegments;
        list.set(list.size() - 1, "");
        while (true) {
          if (param1Int1 < param1Int2) {
            boolean bool;
            int i = Util.delimiterOffset(param1String, param1Int1, param1Int2, "/\\");
            if (i < param1Int2) {
              bool = true;
            } else {
              bool = false;
            } 
            push(param1String, param1Int1, i, bool, true);
            param1Int1 = i;
            if (bool) {
              param1Int1 = i;
            } else {
              continue;
            } 
          } else {
            break;
          } 
          param1Int1++;
        } 
        return;
      } 
      param1Int1++;
      continue;
    }
    
    private static int schemeDelimiterOffset(String param1String, int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_2
      //   1: iload_1
      //   2: isub
      //   3: iconst_2
      //   4: if_icmpge -> 9
      //   7: iconst_m1
      //   8: ireturn
      //   9: aload_0
      //   10: iload_1
      //   11: invokevirtual charAt : (I)C
      //   14: istore #4
      //   16: iload #4
      //   18: bipush #97
      //   20: if_icmplt -> 32
      //   23: iload_1
      //   24: istore_3
      //   25: iload #4
      //   27: bipush #122
      //   29: if_icmple -> 51
      //   32: iload #4
      //   34: bipush #65
      //   36: if_icmplt -> 154
      //   39: iload_1
      //   40: istore_3
      //   41: iload #4
      //   43: bipush #90
      //   45: if_icmple -> 51
      //   48: goto -> 154
      //   51: iload_3
      //   52: iconst_1
      //   53: iadd
      //   54: istore_1
      //   55: iload_1
      //   56: iload_2
      //   57: if_icmpge -> 154
      //   60: aload_0
      //   61: iload_1
      //   62: invokevirtual charAt : (I)C
      //   65: istore #4
      //   67: iload #4
      //   69: bipush #97
      //   71: if_icmplt -> 83
      //   74: iload_1
      //   75: istore_3
      //   76: iload #4
      //   78: bipush #122
      //   80: if_icmple -> 51
      //   83: iload #4
      //   85: bipush #65
      //   87: if_icmplt -> 99
      //   90: iload_1
      //   91: istore_3
      //   92: iload #4
      //   94: bipush #90
      //   96: if_icmple -> 51
      //   99: iload #4
      //   101: bipush #48
      //   103: if_icmplt -> 115
      //   106: iload_1
      //   107: istore_3
      //   108: iload #4
      //   110: bipush #57
      //   112: if_icmple -> 51
      //   115: iload_1
      //   116: istore_3
      //   117: iload #4
      //   119: bipush #43
      //   121: if_icmpeq -> 51
      //   124: iload_1
      //   125: istore_3
      //   126: iload #4
      //   128: bipush #45
      //   130: if_icmpeq -> 51
      //   133: iload #4
      //   135: bipush #46
      //   137: if_icmpne -> 145
      //   140: iload_1
      //   141: istore_3
      //   142: goto -> 51
      //   145: iload #4
      //   147: bipush #58
      //   149: if_icmpne -> 154
      //   152: iload_1
      //   153: ireturn
      //   154: iconst_m1
      //   155: ireturn
    }
    
    private static int slashCount(String param1String, int param1Int1, int param1Int2) {
      byte b = 0;
      while (param1Int1 < param1Int2) {
        char c = param1String.charAt(param1Int1);
        if (c == '\\' || c == '/') {
          b++;
          param1Int1++;
        } 
      } 
      return b;
    }
    
    public Builder addEncodedPathSegment(String param1String) {
      if (param1String != null) {
        push(param1String, 0, param1String.length(), false, true);
        return this;
      } 
      throw new IllegalArgumentException("encodedPathSegment == null");
    }
    
    public Builder addEncodedPathSegments(String param1String) {
      if (param1String != null)
        return addPathSegments(param1String, true); 
      throw new IllegalArgumentException("encodedPathSegments == null");
    }
    
    public Builder addEncodedQueryParameter(String param1String1, String param1String2) {
      if (param1String1 != null) {
        if (this.encodedQueryNamesAndValues == null)
          this.encodedQueryNamesAndValues = new ArrayList<String>(); 
        this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " \"'<>#&=", true, false, true, true));
        List<String> list = this.encodedQueryNamesAndValues;
        if (param1String2 != null) {
          param1String1 = HttpUrl.canonicalize(param1String2, " \"'<>#&=", true, false, true, true);
        } else {
          param1String1 = null;
        } 
        list.add(param1String1);
        return this;
      } 
      throw new IllegalArgumentException("encodedName == null");
    }
    
    public Builder addPathSegment(String param1String) {
      if (param1String != null) {
        push(param1String, 0, param1String.length(), false, false);
        return this;
      } 
      throw new IllegalArgumentException("pathSegment == null");
    }
    
    public Builder addPathSegments(String param1String) {
      if (param1String != null)
        return addPathSegments(param1String, false); 
      throw new IllegalArgumentException("pathSegments == null");
    }
    
    public Builder addQueryParameter(String param1String1, String param1String2) {
      if (param1String1 != null) {
        if (this.encodedQueryNamesAndValues == null)
          this.encodedQueryNamesAndValues = new ArrayList<String>(); 
        this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(param1String1, " \"'<>#&=", false, false, true, true));
        List<String> list = this.encodedQueryNamesAndValues;
        if (param1String2 != null) {
          param1String1 = HttpUrl.canonicalize(param1String2, " \"'<>#&=", false, false, true, true);
        } else {
          param1String1 = null;
        } 
        list.add(param1String1);
        return this;
      } 
      throw new IllegalArgumentException("name == null");
    }
    
    public HttpUrl build() {
      if (this.scheme != null) {
        if (this.host != null)
          return new HttpUrl(this); 
        throw new IllegalStateException("host == null");
      } 
      throw new IllegalStateException("scheme == null");
    }
    
    int effectivePort() {
      int i = this.port;
      if (i == -1)
        i = HttpUrl.defaultPort(this.scheme); 
      return i;
    }
    
    public Builder encodedFragment(String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", true, false, false, false);
      } else {
        param1String = null;
      } 
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder encodedPassword(String param1String) {
      if (param1String != null) {
        this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
        return this;
      } 
      throw new IllegalArgumentException("encodedPassword == null");
    }
    
    public Builder encodedPath(String param1String) {
      if (param1String != null) {
        if (param1String.startsWith("/")) {
          resolvePath(param1String, 0, param1String.length());
          return this;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected encodedPath: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("encodedPath == null");
    }
    
    public Builder encodedQuery(String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", true, false, true, true));
      } else {
        param1String = null;
      } 
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    public Builder encodedUsername(String param1String) {
      if (param1String != null) {
        this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
        return this;
      } 
      throw new IllegalArgumentException("encodedUsername == null");
    }
    
    public Builder fragment(String param1String) {
      if (param1String != null) {
        param1String = HttpUrl.canonicalize(param1String, "", false, false, false, false);
      } else {
        param1String = null;
      } 
      this.encodedFragment = param1String;
      return this;
    }
    
    public Builder host(String param1String) {
      if (param1String != null) {
        String str = canonicalizeHost(param1String, 0, param1String.length());
        if (str != null) {
          this.host = str;
          return this;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected host: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("host == null");
    }
    
    ParseResult parse(HttpUrl param1HttpUrl, String param1String) {
      // Byte code:
      //   0: aload_2
      //   1: iconst_0
      //   2: aload_2
      //   3: invokevirtual length : ()I
      //   6: invokestatic skipLeadingAsciiWhitespace : (Ljava/lang/String;II)I
      //   9: istore_3
      //   10: aload_2
      //   11: iload_3
      //   12: aload_2
      //   13: invokevirtual length : ()I
      //   16: invokestatic skipTrailingAsciiWhitespace : (Ljava/lang/String;II)I
      //   19: istore #8
      //   21: aload_2
      //   22: iload_3
      //   23: iload #8
      //   25: invokestatic schemeDelimiterOffset : (Ljava/lang/String;II)I
      //   28: iconst_m1
      //   29: if_icmpeq -> 91
      //   32: aload_2
      //   33: iconst_1
      //   34: iload_3
      //   35: ldc_w 'https:'
      //   38: iconst_0
      //   39: bipush #6
      //   41: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   44: ifeq -> 60
      //   47: aload_0
      //   48: ldc_w 'https'
      //   51: putfield scheme : Ljava/lang/String;
      //   54: iinc #3, 6
      //   57: goto -> 103
      //   60: aload_2
      //   61: iconst_1
      //   62: iload_3
      //   63: ldc_w 'http:'
      //   66: iconst_0
      //   67: iconst_5
      //   68: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
      //   71: ifeq -> 87
      //   74: aload_0
      //   75: ldc_w 'http'
      //   78: putfield scheme : Ljava/lang/String;
      //   81: iinc #3, 5
      //   84: goto -> 103
      //   87: getstatic okhttp3/HttpUrl$Builder$ParseResult.UNSUPPORTED_SCHEME : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   90: areturn
      //   91: aload_1
      //   92: ifnull -> 717
      //   95: aload_0
      //   96: aload_1
      //   97: invokestatic access$100 : (Lokhttp3/HttpUrl;)Ljava/lang/String;
      //   100: putfield scheme : Ljava/lang/String;
      //   103: aload_2
      //   104: iload_3
      //   105: iload #8
      //   107: invokestatic slashCount : (Ljava/lang/String;II)I
      //   110: istore #6
      //   112: iload #6
      //   114: iconst_2
      //   115: if_icmpge -> 228
      //   118: aload_1
      //   119: ifnull -> 228
      //   122: aload_1
      //   123: invokestatic access$100 : (Lokhttp3/HttpUrl;)Ljava/lang/String;
      //   126: aload_0
      //   127: getfield scheme : Ljava/lang/String;
      //   130: invokevirtual equals : (Ljava/lang/Object;)Z
      //   133: ifne -> 139
      //   136: goto -> 228
      //   139: aload_0
      //   140: aload_1
      //   141: invokevirtual encodedUsername : ()Ljava/lang/String;
      //   144: putfield encodedUsername : Ljava/lang/String;
      //   147: aload_0
      //   148: aload_1
      //   149: invokevirtual encodedPassword : ()Ljava/lang/String;
      //   152: putfield encodedPassword : Ljava/lang/String;
      //   155: aload_0
      //   156: aload_1
      //   157: invokestatic access$200 : (Lokhttp3/HttpUrl;)Ljava/lang/String;
      //   160: putfield host : Ljava/lang/String;
      //   163: aload_0
      //   164: aload_1
      //   165: invokestatic access$300 : (Lokhttp3/HttpUrl;)I
      //   168: putfield port : I
      //   171: aload_0
      //   172: getfield encodedPathSegments : Ljava/util/List;
      //   175: invokeinterface clear : ()V
      //   180: aload_0
      //   181: getfield encodedPathSegments : Ljava/util/List;
      //   184: aload_1
      //   185: invokevirtual encodedPathSegments : ()Ljava/util/List;
      //   188: invokeinterface addAll : (Ljava/util/Collection;)Z
      //   193: pop
      //   194: iload_3
      //   195: iload #8
      //   197: if_icmpeq -> 213
      //   200: iload_3
      //   201: istore #4
      //   203: aload_2
      //   204: iload_3
      //   205: invokevirtual charAt : (I)C
      //   208: bipush #35
      //   210: if_icmpne -> 598
      //   213: aload_0
      //   214: aload_1
      //   215: invokevirtual encodedQuery : ()Ljava/lang/String;
      //   218: invokevirtual encodedQuery : (Ljava/lang/String;)Lokhttp3/HttpUrl$Builder;
      //   221: pop
      //   222: iload_3
      //   223: istore #4
      //   225: goto -> 598
      //   228: iconst_0
      //   229: istore #5
      //   231: iconst_0
      //   232: istore #4
      //   234: iload_3
      //   235: iload #6
      //   237: iadd
      //   238: istore #6
      //   240: iload #5
      //   242: istore_3
      //   243: iload #6
      //   245: istore #5
      //   247: aload_2
      //   248: iload #5
      //   250: iload #8
      //   252: ldc_w '@/\?#'
      //   255: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   258: istore #7
      //   260: iload #7
      //   262: iload #8
      //   264: if_icmpeq -> 278
      //   267: aload_2
      //   268: iload #7
      //   270: invokevirtual charAt : (I)C
      //   273: istore #6
      //   275: goto -> 281
      //   278: iconst_m1
      //   279: istore #6
      //   281: iload #6
      //   283: iconst_m1
      //   284: if_icmpeq -> 506
      //   287: iload #6
      //   289: bipush #35
      //   291: if_icmpeq -> 506
      //   294: iload #6
      //   296: bipush #47
      //   298: if_icmpeq -> 506
      //   301: iload #6
      //   303: bipush #92
      //   305: if_icmpeq -> 506
      //   308: iload #6
      //   310: bipush #63
      //   312: if_icmpeq -> 506
      //   315: iload #6
      //   317: bipush #64
      //   319: if_icmpeq -> 325
      //   322: goto -> 503
      //   325: iload_3
      //   326: ifne -> 444
      //   329: aload_2
      //   330: iload #5
      //   332: iload #7
      //   334: bipush #58
      //   336: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   339: istore #6
      //   341: aload_2
      //   342: iload #5
      //   344: iload #6
      //   346: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   349: iconst_1
      //   350: iconst_0
      //   351: iconst_0
      //   352: iconst_1
      //   353: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   356: astore #9
      //   358: aload #9
      //   360: astore_1
      //   361: iload #4
      //   363: ifeq -> 403
      //   366: new java/lang/StringBuilder
      //   369: dup
      //   370: invokespecial <init> : ()V
      //   373: astore_1
      //   374: aload_1
      //   375: aload_0
      //   376: getfield encodedUsername : Ljava/lang/String;
      //   379: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   382: pop
      //   383: aload_1
      //   384: ldc_w '%40'
      //   387: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   390: pop
      //   391: aload_1
      //   392: aload #9
      //   394: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   397: pop
      //   398: aload_1
      //   399: invokevirtual toString : ()Ljava/lang/String;
      //   402: astore_1
      //   403: aload_0
      //   404: aload_1
      //   405: putfield encodedUsername : Ljava/lang/String;
      //   408: iload #6
      //   410: iload #7
      //   412: if_icmpeq -> 438
      //   415: aload_0
      //   416: aload_2
      //   417: iload #6
      //   419: iconst_1
      //   420: iadd
      //   421: iload #7
      //   423: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   426: iconst_1
      //   427: iconst_0
      //   428: iconst_0
      //   429: iconst_1
      //   430: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   433: putfield encodedPassword : Ljava/lang/String;
      //   436: iconst_1
      //   437: istore_3
      //   438: iconst_1
      //   439: istore #4
      //   441: goto -> 497
      //   444: new java/lang/StringBuilder
      //   447: dup
      //   448: invokespecial <init> : ()V
      //   451: astore_1
      //   452: aload_1
      //   453: aload_0
      //   454: getfield encodedPassword : Ljava/lang/String;
      //   457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   460: pop
      //   461: aload_1
      //   462: ldc_w '%40'
      //   465: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   468: pop
      //   469: aload_1
      //   470: aload_2
      //   471: iload #5
      //   473: iload #7
      //   475: ldc_w ' "':;<=>@[]^`{}|/\?#'
      //   478: iconst_1
      //   479: iconst_0
      //   480: iconst_0
      //   481: iconst_1
      //   482: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   485: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   488: pop
      //   489: aload_0
      //   490: aload_1
      //   491: invokevirtual toString : ()Ljava/lang/String;
      //   494: putfield encodedPassword : Ljava/lang/String;
      //   497: iload #7
      //   499: iconst_1
      //   500: iadd
      //   501: istore #5
      //   503: goto -> 247
      //   506: aload_2
      //   507: iload #5
      //   509: iload #7
      //   511: invokestatic portColonOffset : (Ljava/lang/String;II)I
      //   514: istore_3
      //   515: iload_3
      //   516: iconst_1
      //   517: iadd
      //   518: istore #4
      //   520: iload #4
      //   522: iload #7
      //   524: if_icmpge -> 561
      //   527: aload_0
      //   528: aload_2
      //   529: iload #5
      //   531: iload_3
      //   532: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   535: putfield host : Ljava/lang/String;
      //   538: aload_2
      //   539: iload #4
      //   541: iload #7
      //   543: invokestatic parsePort : (Ljava/lang/String;II)I
      //   546: istore_3
      //   547: aload_0
      //   548: iload_3
      //   549: putfield port : I
      //   552: iload_3
      //   553: iconst_m1
      //   554: if_icmpne -> 583
      //   557: getstatic okhttp3/HttpUrl$Builder$ParseResult.INVALID_PORT : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   560: areturn
      //   561: aload_0
      //   562: aload_2
      //   563: iload #5
      //   565: iload_3
      //   566: invokestatic canonicalizeHost : (Ljava/lang/String;II)Ljava/lang/String;
      //   569: putfield host : Ljava/lang/String;
      //   572: aload_0
      //   573: aload_0
      //   574: getfield scheme : Ljava/lang/String;
      //   577: invokestatic defaultPort : (Ljava/lang/String;)I
      //   580: putfield port : I
      //   583: aload_0
      //   584: getfield host : Ljava/lang/String;
      //   587: ifnonnull -> 594
      //   590: getstatic okhttp3/HttpUrl$Builder$ParseResult.INVALID_HOST : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   593: areturn
      //   594: iload #7
      //   596: istore #4
      //   598: aload_2
      //   599: iload #4
      //   601: iload #8
      //   603: ldc_w '?#'
      //   606: invokestatic delimiterOffset : (Ljava/lang/String;IILjava/lang/String;)I
      //   609: istore #5
      //   611: aload_0
      //   612: aload_2
      //   613: iload #4
      //   615: iload #5
      //   617: invokespecial resolvePath : (Ljava/lang/String;II)V
      //   620: iload #5
      //   622: istore_3
      //   623: iload #5
      //   625: iload #8
      //   627: if_icmpge -> 678
      //   630: iload #5
      //   632: istore_3
      //   633: aload_2
      //   634: iload #5
      //   636: invokevirtual charAt : (I)C
      //   639: bipush #63
      //   641: if_icmpne -> 678
      //   644: aload_2
      //   645: iload #5
      //   647: iload #8
      //   649: bipush #35
      //   651: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
      //   654: istore_3
      //   655: aload_0
      //   656: aload_2
      //   657: iload #5
      //   659: iconst_1
      //   660: iadd
      //   661: iload_3
      //   662: ldc_w ' "'<>#'
      //   665: iconst_1
      //   666: iconst_0
      //   667: iconst_1
      //   668: iconst_1
      //   669: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   672: invokestatic queryStringToNamesAndValues : (Ljava/lang/String;)Ljava/util/List;
      //   675: putfield encodedQueryNamesAndValues : Ljava/util/List;
      //   678: iload_3
      //   679: iload #8
      //   681: if_icmpge -> 713
      //   684: aload_2
      //   685: iload_3
      //   686: invokevirtual charAt : (I)C
      //   689: bipush #35
      //   691: if_icmpne -> 713
      //   694: aload_0
      //   695: aload_2
      //   696: iconst_1
      //   697: iload_3
      //   698: iadd
      //   699: iload #8
      //   701: ldc ''
      //   703: iconst_1
      //   704: iconst_0
      //   705: iconst_0
      //   706: iconst_0
      //   707: invokestatic canonicalize : (Ljava/lang/String;IILjava/lang/String;ZZZZ)Ljava/lang/String;
      //   710: putfield encodedFragment : Ljava/lang/String;
      //   713: getstatic okhttp3/HttpUrl$Builder$ParseResult.SUCCESS : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   716: areturn
      //   717: getstatic okhttp3/HttpUrl$Builder$ParseResult.MISSING_SCHEME : Lokhttp3/HttpUrl$Builder$ParseResult;
      //   720: areturn
    }
    
    public Builder password(String param1String) {
      if (param1String != null) {
        this.encodedPassword = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
        return this;
      } 
      throw new IllegalArgumentException("password == null");
    }
    
    public Builder port(int param1Int) {
      if (param1Int > 0 && param1Int <= 65535) {
        this.port = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unexpected port: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder query(String param1String) {
      if (param1String != null) {
        List<String> list = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(param1String, " \"'<>#", false, false, true, true));
      } else {
        param1String = null;
      } 
      this.encodedQueryNamesAndValues = (List<String>)param1String;
      return this;
    }
    
    Builder reencodeForUri() {
      int i = this.encodedPathSegments.size();
      boolean bool = false;
      byte b;
      for (b = 0; b < i; b++) {
        String str1 = this.encodedPathSegments.get(b);
        this.encodedPathSegments.set(b, HttpUrl.canonicalize(str1, "[]", true, true, false, true));
      } 
      List<String> list = this.encodedQueryNamesAndValues;
      if (list != null) {
        i = list.size();
        for (b = bool; b < i; b++) {
          String str1 = this.encodedQueryNamesAndValues.get(b);
          if (str1 != null)
            this.encodedQueryNamesAndValues.set(b, HttpUrl.canonicalize(str1, "\\^`{|}", true, true, true, true)); 
        } 
      } 
      String str = this.encodedFragment;
      if (str != null)
        this.encodedFragment = HttpUrl.canonicalize(str, " \"#<>\\^`{|}", true, true, false, false); 
      return this;
    }
    
    public Builder removeAllEncodedQueryParameters(String param1String) {
      if (param1String != null) {
        if (this.encodedQueryNamesAndValues == null)
          return this; 
        removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " \"'<>#&=", true, false, true, true));
        return this;
      } 
      throw new IllegalArgumentException("encodedName == null");
    }
    
    public Builder removeAllQueryParameters(String param1String) {
      if (param1String != null) {
        if (this.encodedQueryNamesAndValues == null)
          return this; 
        removeAllCanonicalQueryParameters(HttpUrl.canonicalize(param1String, " \"'<>#&=", false, false, true, true));
        return this;
      } 
      throw new IllegalArgumentException("name == null");
    }
    
    public Builder removePathSegment(int param1Int) {
      this.encodedPathSegments.remove(param1Int);
      if (this.encodedPathSegments.isEmpty())
        this.encodedPathSegments.add(""); 
      return this;
    }
    
    public Builder scheme(String param1String) {
      if (param1String != null) {
        if (param1String.equalsIgnoreCase("http")) {
          this.scheme = "http";
        } else {
          if (param1String.equalsIgnoreCase("https")) {
            this.scheme = "https";
            return this;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("unexpected scheme: ");
          stringBuilder.append(param1String);
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
        return this;
      } 
      throw new IllegalArgumentException("scheme == null");
    }
    
    public Builder setEncodedPathSegment(int param1Int, String param1String) {
      if (param1String != null) {
        String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", true, false, false, true);
        this.encodedPathSegments.set(param1Int, str);
        if (!isDot(str) && !isDotDot(str))
          return this; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected path segment: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("encodedPathSegment == null");
    }
    
    public Builder setEncodedQueryParameter(String param1String1, String param1String2) {
      removeAllEncodedQueryParameters(param1String1);
      addEncodedQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public Builder setPathSegment(int param1Int, String param1String) {
      if (param1String != null) {
        String str = HttpUrl.canonicalize(param1String, 0, param1String.length(), " \"<>^`{}|/\\?#", false, false, false, true);
        if (!isDot(str) && !isDotDot(str)) {
          this.encodedPathSegments.set(param1Int, str);
          return this;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected path segment: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("pathSegment == null");
    }
    
    public Builder setQueryParameter(String param1String1, String param1String2) {
      removeAllQueryParameters(param1String1);
      addQueryParameter(param1String1, param1String2);
      return this;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.scheme);
      stringBuilder.append("://");
      if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
        stringBuilder.append(this.encodedUsername);
        if (!this.encodedPassword.isEmpty()) {
          stringBuilder.append(':');
          stringBuilder.append(this.encodedPassword);
        } 
        stringBuilder.append('@');
      } 
      if (this.host.indexOf(':') != -1) {
        stringBuilder.append('[');
        stringBuilder.append(this.host);
        stringBuilder.append(']');
      } else {
        stringBuilder.append(this.host);
      } 
      int i = effectivePort();
      if (i != HttpUrl.defaultPort(this.scheme)) {
        stringBuilder.append(':');
        stringBuilder.append(i);
      } 
      HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
      if (this.encodedQueryNamesAndValues != null) {
        stringBuilder.append('?');
        HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
      } 
      if (this.encodedFragment != null) {
        stringBuilder.append('#');
        stringBuilder.append(this.encodedFragment);
      } 
      return stringBuilder.toString();
    }
    
    public Builder username(String param1String) {
      if (param1String != null) {
        this.encodedUsername = HttpUrl.canonicalize(param1String, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
        return this;
      } 
      throw new IllegalArgumentException("username == null");
    }
    
    enum ParseResult {
      INVALID_HOST, INVALID_PORT, MISSING_SCHEME, SUCCESS, UNSUPPORTED_SCHEME;
      
      private static final ParseResult[] $VALUES;
      
      static {
        ParseResult parseResult = new ParseResult("INVALID_HOST", 4);
        INVALID_HOST = parseResult;
        $VALUES = new ParseResult[] { SUCCESS, MISSING_SCHEME, UNSUPPORTED_SCHEME, INVALID_PORT, parseResult };
      }
    }
  }
  
  enum ParseResult {
    INVALID_HOST, INVALID_PORT, MISSING_SCHEME, SUCCESS, UNSUPPORTED_SCHEME;
    
    private static final ParseResult[] $VALUES;
    
    static {
      INVALID_PORT = new ParseResult("INVALID_PORT", 3);
      ParseResult parseResult = new ParseResult("INVALID_HOST", 4);
      INVALID_HOST = parseResult;
      $VALUES = new ParseResult[] { SUCCESS, MISSING_SCHEME, UNSUPPORTED_SCHEME, INVALID_PORT, parseResult };
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\HttpUrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */