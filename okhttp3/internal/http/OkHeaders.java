package okhttp3.internal.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import okhttp3.Challenge;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;

public final class OkHeaders {
  static final String PREFIX = Platform.get().getPrefix();
  
  public static final String RECEIVED_MILLIS;
  
  public static final String RESPONSE_SOURCE;
  
  public static final String SELECTED_PROTOCOL;
  
  public static final String SENT_MILLIS;
  
  static {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(PREFIX);
    stringBuilder.append("-Sent-Millis");
    SENT_MILLIS = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(PREFIX);
    stringBuilder.append("-Received-Millis");
    RECEIVED_MILLIS = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(PREFIX);
    stringBuilder.append("-Selected-Protocol");
    SELECTED_PROTOCOL = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(PREFIX);
    stringBuilder.append("-Response-Source");
    RESPONSE_SOURCE = stringBuilder.toString();
  }
  
  public static long contentLength(Headers paramHeaders) {
    return stringToLong(paramHeaders.get("Content-Length"));
  }
  
  public static long contentLength(Request paramRequest) {
    return contentLength(paramRequest.headers());
  }
  
  public static long contentLength(Response paramResponse) {
    return contentLength(paramResponse.headers());
  }
  
  public static boolean hasVaryAll(Headers paramHeaders) {
    return varyFields(paramHeaders).contains("*");
  }
  
  public static boolean hasVaryAll(Response paramResponse) {
    return hasVaryAll(paramResponse.headers());
  }
  
  static boolean isEndToEnd(String paramString) {
    boolean bool;
    if (!"Connection".equalsIgnoreCase(paramString) && !"Keep-Alive".equalsIgnoreCase(paramString) && !"Proxy-Authenticate".equalsIgnoreCase(paramString) && !"Proxy-Authorization".equalsIgnoreCase(paramString) && !"TE".equalsIgnoreCase(paramString) && !"Trailers".equalsIgnoreCase(paramString) && !"Transfer-Encoding".equalsIgnoreCase(paramString) && !"Upgrade".equalsIgnoreCase(paramString)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static List<Challenge> parseChallenges(Headers paramHeaders, String paramString) {
    ArrayList<Challenge> arrayList = new ArrayList();
    int i = paramHeaders.size();
    for (byte b = 0; b < i; b++) {
      if (paramString.equalsIgnoreCase(paramHeaders.name(b))) {
        String str = paramHeaders.value(b);
        int j = 0;
        while (j < str.length()) {
          int k = HeaderParser.skipUntil(str, j, " ");
          String str2 = str.substring(j, k).trim();
          j = HeaderParser.skipWhitespace(str, k);
          if (!str.regionMatches(true, j, "realm=\"", 0, 7))
            break; 
          k = j + 7;
          j = HeaderParser.skipUntil(str, k, "\"");
          String str1 = str.substring(k, j);
          j = HeaderParser.skipWhitespace(str, HeaderParser.skipUntil(str, j + 1, ",") + 1);
          arrayList.add(new Challenge(str2, str1));
        } 
      } 
    } 
    return arrayList;
  }
  
  private static long stringToLong(String paramString) {
    long l = -1L;
    if (paramString == null)
      return -1L; 
    try {
      long l1 = Long.parseLong(paramString);
      l = l1;
    } catch (NumberFormatException numberFormatException) {}
    return l;
  }
  
  public static Set<String> varyFields(Headers paramHeaders) {
    Set<?> set = Collections.emptySet();
    int i = paramHeaders.size();
    for (byte b = 0; b < i; b++) {
      if ("Vary".equalsIgnoreCase(paramHeaders.name(b))) {
        String str = paramHeaders.value(b);
        Set<?> set1 = set;
        if (set.isEmpty())
          set1 = new TreeSet(String.CASE_INSENSITIVE_ORDER); 
        String[] arrayOfString = str.split(",");
        int j = arrayOfString.length;
        byte b1 = 0;
        while (true) {
          set = set1;
          if (b1 < j) {
            set1.add(arrayOfString[b1].trim());
            b1++;
            continue;
          } 
          break;
        } 
      } 
    } 
    return (Set)set;
  }
  
  private static Set<String> varyFields(Response paramResponse) {
    return varyFields(paramResponse.headers());
  }
  
  public static Headers varyHeaders(Headers paramHeaders1, Headers paramHeaders2) {
    Set<String> set = varyFields(paramHeaders2);
    if (set.isEmpty())
      return (new Headers.Builder()).build(); 
    Headers.Builder builder = new Headers.Builder();
    byte b = 0;
    int i = paramHeaders1.size();
    while (b < i) {
      String str = paramHeaders1.name(b);
      if (set.contains(str))
        builder.add(str, paramHeaders1.value(b)); 
      b++;
    } 
    return builder.build();
  }
  
  public static Headers varyHeaders(Response paramResponse) {
    return varyHeaders(paramResponse.networkResponse().request().headers(), paramResponse.headers());
  }
  
  public static boolean varyMatches(Response paramResponse, Headers paramHeaders, Request paramRequest) {
    for (String str : varyFields(paramResponse)) {
      if (!Util.equal(paramHeaders.values(str), paramRequest.headers(str)))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\http\OkHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */