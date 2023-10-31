package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
  private static final Pattern DAY_OF_MONTH_PATTERN;
  
  private static final Pattern MONTH_PATTERN;
  
  private static final Pattern TIME_PATTERN;
  
  private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
  
  private final String domain;
  
  private final long expiresAt;
  
  private final boolean hostOnly;
  
  private final boolean httpOnly;
  
  private final String name;
  
  private final String path;
  
  private final boolean persistent;
  
  private final boolean secure;
  
  private final String value;
  
  static {
    MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
  }
  
  private Cookie(String paramString1, String paramString2, long paramLong, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    this.name = paramString1;
    this.value = paramString2;
    this.expiresAt = paramLong;
    this.domain = paramString3;
    this.path = paramString4;
    this.secure = paramBoolean1;
    this.httpOnly = paramBoolean2;
    this.hostOnly = paramBoolean3;
    this.persistent = paramBoolean4;
  }
  
  private Cookie(Builder paramBuilder) {
    if (paramBuilder.name != null) {
      if (paramBuilder.value != null) {
        if (paramBuilder.domain != null) {
          this.name = paramBuilder.name;
          this.value = paramBuilder.value;
          this.expiresAt = paramBuilder.expiresAt;
          this.domain = paramBuilder.domain;
          this.path = paramBuilder.path;
          this.secure = paramBuilder.secure;
          this.httpOnly = paramBuilder.httpOnly;
          this.persistent = paramBuilder.persistent;
          this.hostOnly = paramBuilder.hostOnly;
          return;
        } 
        throw new IllegalArgumentException("builder.domain == null");
      } 
      throw new IllegalArgumentException("builder.value == null");
    } 
    throw new IllegalArgumentException("builder.name == null");
  }
  
  private static int dateCharacterOffset(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    while (paramInt1 < paramInt2) {
      char c = paramString.charAt(paramInt1);
      if ((c < ' ' && c != '\t') || c >= '' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ':') {
        c = '\001';
      } else {
        c = Character.MIN_VALUE;
      } 
      if (c == (paramBoolean ^ true))
        return paramInt1; 
      paramInt1++;
    } 
    return paramInt2;
  }
  
  private static boolean domainMatch(HttpUrl paramHttpUrl, String paramString) {
    String str = paramHttpUrl.host();
    return str.equals(paramString) ? true : ((str.endsWith(paramString) && str.charAt(str.length() - paramString.length() - 1) == '.' && !Util.verifyAsIpAddress(str)));
  }
  
  static Cookie parse(long paramLong, HttpUrl paramHttpUrl, String paramString) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual length : ()I
    //   4: istore #5
    //   6: aload_3
    //   7: iconst_0
    //   8: iload #5
    //   10: bipush #59
    //   12: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   15: istore #6
    //   17: aload_3
    //   18: iconst_0
    //   19: iload #6
    //   21: bipush #61
    //   23: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   26: istore #4
    //   28: iload #4
    //   30: iload #6
    //   32: if_icmpne -> 37
    //   35: aconst_null
    //   36: areturn
    //   37: aload_3
    //   38: iconst_0
    //   39: iload #4
    //   41: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   44: astore #27
    //   46: aload #27
    //   48: invokevirtual isEmpty : ()Z
    //   51: ifeq -> 56
    //   54: aconst_null
    //   55: areturn
    //   56: aload_3
    //   57: iload #4
    //   59: iconst_1
    //   60: iadd
    //   61: iload #6
    //   63: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   66: astore #28
    //   68: iload #6
    //   70: iconst_1
    //   71: iadd
    //   72: istore #4
    //   74: iconst_0
    //   75: istore #18
    //   77: iconst_0
    //   78: istore #19
    //   80: iload #19
    //   82: istore #16
    //   84: aconst_null
    //   85: astore #24
    //   87: iconst_1
    //   88: istore #17
    //   90: ldc2_w -1
    //   93: lstore #10
    //   95: ldc2_w 253402300799999
    //   98: lstore #8
    //   100: aconst_null
    //   101: astore #23
    //   103: iload #4
    //   105: iload #5
    //   107: if_icmpge -> 481
    //   110: aload_3
    //   111: iload #4
    //   113: iload #5
    //   115: bipush #59
    //   117: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   120: istore #7
    //   122: aload_3
    //   123: iload #4
    //   125: iload #7
    //   127: bipush #61
    //   129: invokestatic delimiterOffset : (Ljava/lang/String;IIC)I
    //   132: istore #6
    //   134: aload_3
    //   135: iload #4
    //   137: iload #6
    //   139: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   142: astore #29
    //   144: iload #6
    //   146: iload #7
    //   148: if_icmpge -> 166
    //   151: aload_3
    //   152: iload #6
    //   154: iconst_1
    //   155: iadd
    //   156: iload #7
    //   158: invokestatic trimSubstring : (Ljava/lang/String;II)Ljava/lang/String;
    //   161: astore #25
    //   163: goto -> 170
    //   166: ldc ''
    //   168: astore #25
    //   170: aload #29
    //   172: ldc 'expires'
    //   174: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   177: ifeq -> 200
    //   180: aload #25
    //   182: iconst_0
    //   183: aload #25
    //   185: invokevirtual length : ()I
    //   188: invokestatic parseExpires : (Ljava/lang/String;II)J
    //   191: lstore #12
    //   193: lload #12
    //   195: lstore #8
    //   197: goto -> 221
    //   200: aload #29
    //   202: ldc 'max-age'
    //   204: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   207: ifeq -> 251
    //   210: aload #25
    //   212: invokestatic parseMaxAge : (Ljava/lang/String;)J
    //   215: lstore #12
    //   217: lload #12
    //   219: lstore #10
    //   221: iconst_1
    //   222: istore #22
    //   224: aload #24
    //   226: astore #25
    //   228: aload #23
    //   230: astore #26
    //   232: iload #18
    //   234: istore #21
    //   236: iload #17
    //   238: istore #20
    //   240: lload #10
    //   242: lstore #12
    //   244: lload #8
    //   246: lstore #14
    //   248: goto -> 444
    //   251: aload #29
    //   253: ldc 'domain'
    //   255: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   258: ifeq -> 294
    //   261: aload #25
    //   263: invokestatic parseDomain : (Ljava/lang/String;)Ljava/lang/String;
    //   266: astore #25
    //   268: iconst_0
    //   269: istore #20
    //   271: aload #23
    //   273: astore #26
    //   275: iload #18
    //   277: istore #21
    //   279: iload #16
    //   281: istore #22
    //   283: lload #10
    //   285: lstore #12
    //   287: lload #8
    //   289: lstore #14
    //   291: goto -> 444
    //   294: aload #29
    //   296: ldc 'path'
    //   298: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   301: ifeq -> 335
    //   304: aload #25
    //   306: astore #26
    //   308: aload #24
    //   310: astore #25
    //   312: iload #18
    //   314: istore #21
    //   316: iload #17
    //   318: istore #20
    //   320: iload #16
    //   322: istore #22
    //   324: lload #10
    //   326: lstore #12
    //   328: lload #8
    //   330: lstore #14
    //   332: goto -> 444
    //   335: aload #29
    //   337: ldc 'secure'
    //   339: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   342: ifeq -> 375
    //   345: iconst_1
    //   346: istore #21
    //   348: aload #24
    //   350: astore #25
    //   352: aload #23
    //   354: astore #26
    //   356: iload #17
    //   358: istore #20
    //   360: iload #16
    //   362: istore #22
    //   364: lload #10
    //   366: lstore #12
    //   368: lload #8
    //   370: lstore #14
    //   372: goto -> 444
    //   375: aload #24
    //   377: astore #25
    //   379: aload #23
    //   381: astore #26
    //   383: iload #18
    //   385: istore #21
    //   387: iload #17
    //   389: istore #20
    //   391: iload #16
    //   393: istore #22
    //   395: lload #10
    //   397: lstore #12
    //   399: lload #8
    //   401: lstore #14
    //   403: aload #29
    //   405: ldc 'httponly'
    //   407: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   410: ifeq -> 444
    //   413: iconst_1
    //   414: istore #19
    //   416: lload #8
    //   418: lstore #14
    //   420: lload #10
    //   422: lstore #12
    //   424: iload #16
    //   426: istore #22
    //   428: iload #17
    //   430: istore #20
    //   432: iload #18
    //   434: istore #21
    //   436: aload #23
    //   438: astore #26
    //   440: aload #24
    //   442: astore #25
    //   444: iload #7
    //   446: iconst_1
    //   447: iadd
    //   448: istore #4
    //   450: aload #25
    //   452: astore #24
    //   454: aload #26
    //   456: astore #23
    //   458: iload #21
    //   460: istore #18
    //   462: iload #20
    //   464: istore #17
    //   466: iload #22
    //   468: istore #16
    //   470: lload #12
    //   472: lstore #10
    //   474: lload #14
    //   476: lstore #8
    //   478: goto -> 103
    //   481: ldc2_w -9223372036854775808
    //   484: lstore #12
    //   486: lload #10
    //   488: ldc2_w -9223372036854775808
    //   491: lcmp
    //   492: ifne -> 501
    //   495: lload #12
    //   497: lstore_0
    //   498: goto -> 570
    //   501: lload #10
    //   503: ldc2_w -1
    //   506: lcmp
    //   507: ifeq -> 567
    //   510: lload #10
    //   512: ldc2_w 9223372036854775
    //   515: lcmp
    //   516: ifgt -> 530
    //   519: lload #10
    //   521: ldc2_w 1000
    //   524: lmul
    //   525: lstore #8
    //   527: goto -> 535
    //   530: ldc2_w 9223372036854775807
    //   533: lstore #8
    //   535: lload_0
    //   536: lload #8
    //   538: ladd
    //   539: lstore #8
    //   541: lload #8
    //   543: lload_0
    //   544: lcmp
    //   545: iflt -> 560
    //   548: lload #8
    //   550: lstore_0
    //   551: lload #8
    //   553: ldc2_w 253402300799999
    //   556: lcmp
    //   557: ifle -> 498
    //   560: ldc2_w 253402300799999
    //   563: lstore_0
    //   564: goto -> 570
    //   567: lload #8
    //   569: lstore_0
    //   570: aload #24
    //   572: ifnonnull -> 583
    //   575: aload_2
    //   576: invokevirtual host : ()Ljava/lang/String;
    //   579: astore_3
    //   580: goto -> 597
    //   583: aload_2
    //   584: aload #24
    //   586: invokestatic domainMatch : (Lokhttp3/HttpUrl;Ljava/lang/String;)Z
    //   589: ifne -> 594
    //   592: aconst_null
    //   593: areturn
    //   594: aload #24
    //   596: astore_3
    //   597: ldc '/'
    //   599: astore #24
    //   601: aload #23
    //   603: ifnull -> 625
    //   606: aload #23
    //   608: ldc '/'
    //   610: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   613: ifne -> 619
    //   616: goto -> 625
    //   619: aload #23
    //   621: astore_2
    //   622: goto -> 657
    //   625: aload_2
    //   626: invokevirtual encodedPath : ()Ljava/lang/String;
    //   629: astore #23
    //   631: aload #23
    //   633: bipush #47
    //   635: invokevirtual lastIndexOf : (I)I
    //   638: istore #4
    //   640: aload #24
    //   642: astore_2
    //   643: iload #4
    //   645: ifeq -> 657
    //   648: aload #23
    //   650: iconst_0
    //   651: iload #4
    //   653: invokevirtual substring : (II)Ljava/lang/String;
    //   656: astore_2
    //   657: new okhttp3/Cookie
    //   660: dup
    //   661: aload #27
    //   663: aload #28
    //   665: lload_0
    //   666: aload_3
    //   667: aload_2
    //   668: iload #18
    //   670: iload #19
    //   672: iload #17
    //   674: iload #16
    //   676: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V
    //   679: areturn
    //   680: astore #25
    //   682: aload #24
    //   684: astore #25
    //   686: aload #23
    //   688: astore #26
    //   690: iload #18
    //   692: istore #21
    //   694: iload #17
    //   696: istore #20
    //   698: iload #16
    //   700: istore #22
    //   702: lload #10
    //   704: lstore #12
    //   706: lload #8
    //   708: lstore #14
    //   710: goto -> 444
    // Exception table:
    //   from	to	target	type
    //   180	193	680	java/lang/IllegalArgumentException
    //   210	217	680	java/lang/NumberFormatException
    //   261	268	680	java/lang/IllegalArgumentException
  }
  
  public static Cookie parse(HttpUrl paramHttpUrl, String paramString) {
    return parse(System.currentTimeMillis(), paramHttpUrl, paramString);
  }
  
  public static List<Cookie> parseAll(HttpUrl paramHttpUrl, Headers paramHeaders) {
    List<?> list;
    ArrayList<Cookie> arrayList;
    List<String> list1 = paramHeaders.values("Set-Cookie");
    int i = list1.size();
    Headers headers = null;
    byte b = 0;
    while (b < i) {
      ArrayList<Cookie> arrayList1;
      Cookie cookie = parse(paramHttpUrl, list1.get(b));
      if (cookie == null) {
        paramHeaders = headers;
      } else {
        paramHeaders = headers;
        if (headers == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(cookie);
      } 
      b++;
      arrayList = arrayList1;
    } 
    if (arrayList != null) {
      list = Collections.unmodifiableList(arrayList);
    } else {
      list = Collections.emptyList();
    } 
    return (List)list;
  }
  
  private static String parseDomain(String paramString) {
    if (!paramString.endsWith(".")) {
      String str = paramString;
      if (paramString.startsWith("."))
        str = paramString.substring(1); 
      paramString = Util.domainToAscii(str);
      if (paramString != null)
        return paramString; 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  private static long parseExpires(String paramString, int paramInt1, int paramInt2) {
    int i1 = dateCharacterOffset(paramString, paramInt1, paramInt2, false);
    Matcher matcher = TIME_PATTERN.matcher(paramString);
    int i2 = -1;
    int n = -1;
    paramInt1 = n;
    int i = paramInt1;
    int j = i;
    int k = j;
    int m = paramInt1;
    paramInt1 = i2;
    while (i1 < paramInt2) {
      int i3;
      int i4;
      int i5;
      int i6;
      int i7 = dateCharacterOffset(paramString, i1 + 1, paramInt2, true);
      matcher.region(i1, i7);
      if (n == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
        i1 = Integer.parseInt(matcher.group(1));
        i5 = Integer.parseInt(matcher.group(2));
        i6 = Integer.parseInt(matcher.group(3));
        i2 = paramInt1;
        i3 = m;
        i4 = i;
      } else if (m == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
        i3 = Integer.parseInt(matcher.group(1));
        i2 = paramInt1;
        i1 = n;
        i4 = i;
        i5 = j;
        i6 = k;
      } else if (i == -1 && matcher.usePattern(MONTH_PATTERN).matches()) {
        String str = matcher.group(1).toLowerCase(Locale.US);
        i4 = MONTH_PATTERN.pattern().indexOf(str) / 4;
        i2 = paramInt1;
        i1 = n;
        i3 = m;
        i5 = j;
        i6 = k;
      } else {
        i2 = paramInt1;
        i1 = n;
        i3 = m;
        i4 = i;
        i5 = j;
        i6 = k;
        if (paramInt1 == -1) {
          i2 = paramInt1;
          i1 = n;
          i3 = m;
          i4 = i;
          i5 = j;
          i6 = k;
          if (matcher.usePattern(YEAR_PATTERN).matches()) {
            i2 = Integer.parseInt(matcher.group(1));
            i6 = k;
            i5 = j;
            i4 = i;
            i3 = m;
            i1 = n;
          } 
        } 
      } 
      i7 = dateCharacterOffset(paramString, i7 + 1, paramInt2, false);
      paramInt1 = i2;
      n = i1;
      m = i3;
      i = i4;
      j = i5;
      k = i6;
      i1 = i7;
    } 
    paramInt2 = paramInt1;
    if (paramInt1 >= 70) {
      paramInt2 = paramInt1;
      if (paramInt1 <= 99)
        paramInt2 = paramInt1 + 1900; 
    } 
    paramInt1 = paramInt2;
    if (paramInt2 >= 0) {
      paramInt1 = paramInt2;
      if (paramInt2 <= 69)
        paramInt1 = paramInt2 + 2000; 
    } 
    if (paramInt1 >= 1601) {
      if (i != -1) {
        if (m >= 1 && m <= 31) {
          if (n >= 0 && n <= 23) {
            if (j >= 0 && j <= 59) {
              if (k >= 0 && k <= 59) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
                gregorianCalendar.setLenient(false);
                gregorianCalendar.set(1, paramInt1);
                gregorianCalendar.set(2, i - 1);
                gregorianCalendar.set(5, m);
                gregorianCalendar.set(11, n);
                gregorianCalendar.set(12, j);
                gregorianCalendar.set(13, k);
                gregorianCalendar.set(14, 0);
                return gregorianCalendar.getTimeInMillis();
              } 
              throw new IllegalArgumentException();
            } 
            throw new IllegalArgumentException();
          } 
          throw new IllegalArgumentException();
        } 
        throw new IllegalArgumentException();
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  private static long parseMaxAge(String paramString) {
    long l = Long.MIN_VALUE;
    try {
      long l1 = Long.parseLong(paramString);
      if (l1 > 0L)
        l = l1; 
      return l;
    } catch (NumberFormatException numberFormatException) {
      if (paramString.matches("-?\\d+")) {
        if (!paramString.startsWith("-"))
          l = Long.MAX_VALUE; 
        return l;
      } 
      throw numberFormatException;
    } 
  }
  
  private static boolean pathMatch(HttpUrl paramHttpUrl, String paramString) {
    String str = paramHttpUrl.encodedPath();
    if (str.equals(paramString))
      return true; 
    if (str.startsWith(paramString)) {
      if (paramString.endsWith("/"))
        return true; 
      if (str.charAt(paramString.length()) == '/')
        return true; 
    } 
    return false;
  }
  
  public String domain() {
    return this.domain;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Cookie;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (((Cookie)paramObject).name.equals(this.name)) {
      bool = bool1;
      if (((Cookie)paramObject).value.equals(this.value)) {
        bool = bool1;
        if (((Cookie)paramObject).domain.equals(this.domain)) {
          bool = bool1;
          if (((Cookie)paramObject).path.equals(this.path)) {
            bool = bool1;
            if (((Cookie)paramObject).expiresAt == this.expiresAt) {
              bool = bool1;
              if (((Cookie)paramObject).secure == this.secure) {
                bool = bool1;
                if (((Cookie)paramObject).httpOnly == this.httpOnly) {
                  bool = bool1;
                  if (((Cookie)paramObject).persistent == this.persistent) {
                    bool = bool1;
                    if (((Cookie)paramObject).hostOnly == this.hostOnly)
                      bool = true; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool;
  }
  
  public long expiresAt() {
    return this.expiresAt;
  }
  
  public int hashCode() {
    int i = this.name.hashCode();
    int m = this.value.hashCode();
    int k = this.domain.hashCode();
    int j = this.path.hashCode();
    long l = this.expiresAt;
    return ((((((((527 + i) * 31 + m) * 31 + k) * 31 + j) * 31 + (int)(l ^ l >>> 32L)) * 31 + (this.secure ^ true)) * 31 + (this.httpOnly ^ true)) * 31 + (this.persistent ^ true)) * 31 + (this.hostOnly ^ true);
  }
  
  public boolean hostOnly() {
    return this.hostOnly;
  }
  
  public boolean httpOnly() {
    return this.httpOnly;
  }
  
  public boolean matches(HttpUrl paramHttpUrl) {
    boolean bool;
    if (this.hostOnly) {
      bool = paramHttpUrl.host().equals(this.domain);
    } else {
      bool = domainMatch(paramHttpUrl, this.domain);
    } 
    return !bool ? false : (!pathMatch(paramHttpUrl, this.path) ? false : (!(this.secure && !paramHttpUrl.isHttps())));
  }
  
  public String name() {
    return this.name;
  }
  
  public String path() {
    return this.path;
  }
  
  public boolean persistent() {
    return this.persistent;
  }
  
  public boolean secure() {
    return this.secure;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.name);
    stringBuilder.append('=');
    stringBuilder.append(this.value);
    if (this.persistent)
      if (this.expiresAt == Long.MIN_VALUE) {
        stringBuilder.append("; max-age=0");
      } else {
        stringBuilder.append("; expires=");
        stringBuilder.append(HttpDate.format(new Date(this.expiresAt)));
      }  
    if (!this.hostOnly) {
      stringBuilder.append("; domain=");
      stringBuilder.append(this.domain);
    } 
    stringBuilder.append("; path=");
    stringBuilder.append(this.path);
    if (this.secure)
      stringBuilder.append("; secure"); 
    if (this.httpOnly)
      stringBuilder.append("; httponly"); 
    return stringBuilder.toString();
  }
  
  public String value() {
    return this.value;
  }
  
  public static final class Builder {
    String domain;
    
    long expiresAt = 253402300799999L;
    
    boolean hostOnly;
    
    boolean httpOnly;
    
    String name;
    
    String path = "/";
    
    boolean persistent;
    
    boolean secure;
    
    String value;
    
    private Builder domain(String param1String, boolean param1Boolean) {
      if (param1String != null) {
        String str = Util.domainToAscii(param1String);
        if (str != null) {
          this.domain = str;
          this.hostOnly = param1Boolean;
          return this;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected domain: ");
        stringBuilder.append(param1String);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      throw new IllegalArgumentException("domain == null");
    }
    
    public Cookie build() {
      return new Cookie(this);
    }
    
    public Builder domain(String param1String) {
      return domain(param1String, false);
    }
    
    public Builder expiresAt(long param1Long) {
      long l = param1Long;
      if (param1Long <= 0L)
        l = Long.MIN_VALUE; 
      param1Long = l;
      if (l > 253402300799999L)
        param1Long = 253402300799999L; 
      this.expiresAt = param1Long;
      this.persistent = true;
      return this;
    }
    
    public Builder hostOnlyDomain(String param1String) {
      return domain(param1String, true);
    }
    
    public Builder httpOnly() {
      this.httpOnly = true;
      return this;
    }
    
    public Builder name(String param1String) {
      if (param1String != null) {
        if (param1String.trim().equals(param1String)) {
          this.name = param1String;
          return this;
        } 
        throw new IllegalArgumentException("name is not trimmed");
      } 
      throw new NullPointerException("name == null");
    }
    
    public Builder path(String param1String) {
      if (param1String.startsWith("/")) {
        this.path = param1String;
        return this;
      } 
      throw new IllegalArgumentException("path must start with '/'");
    }
    
    public Builder secure() {
      this.secure = true;
      return this;
    }
    
    public Builder value(String param1String) {
      if (param1String != null) {
        if (param1String.trim().equals(param1String)) {
          this.value = param1String;
          return this;
        } 
        throw new IllegalArgumentException("value is not trimmed");
      } 
      throw new NullPointerException("value == null");
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Cookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */