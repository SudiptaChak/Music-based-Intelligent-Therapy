package okhttp3;

import java.util.concurrent.TimeUnit;

public final class CacheControl {
  public static final CacheControl FORCE_CACHE;
  
  public static final CacheControl FORCE_NETWORK = (new Builder()).noCache().build();
  
  String headerValue;
  
  private final boolean isPrivate;
  
  private final boolean isPublic;
  
  private final int maxAgeSeconds;
  
  private final int maxStaleSeconds;
  
  private final int minFreshSeconds;
  
  private final boolean mustRevalidate;
  
  private final boolean noCache;
  
  private final boolean noStore;
  
  private final boolean noTransform;
  
  private final boolean onlyIfCached;
  
  private final int sMaxAgeSeconds;
  
  static {
    FORCE_CACHE = (new Builder()).onlyIfCached().maxStale(2147483647, TimeUnit.SECONDS).build();
  }
  
  private CacheControl(Builder paramBuilder) {
    this.noCache = paramBuilder.noCache;
    this.noStore = paramBuilder.noStore;
    this.maxAgeSeconds = paramBuilder.maxAgeSeconds;
    this.sMaxAgeSeconds = -1;
    this.isPrivate = false;
    this.isPublic = false;
    this.mustRevalidate = false;
    this.maxStaleSeconds = paramBuilder.maxStaleSeconds;
    this.minFreshSeconds = paramBuilder.minFreshSeconds;
    this.onlyIfCached = paramBuilder.onlyIfCached;
    this.noTransform = paramBuilder.noTransform;
  }
  
  private CacheControl(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt3, int paramInt4, boolean paramBoolean6, boolean paramBoolean7, String paramString) {
    this.noCache = paramBoolean1;
    this.noStore = paramBoolean2;
    this.maxAgeSeconds = paramInt1;
    this.sMaxAgeSeconds = paramInt2;
    this.isPrivate = paramBoolean3;
    this.isPublic = paramBoolean4;
    this.mustRevalidate = paramBoolean5;
    this.maxStaleSeconds = paramInt3;
    this.minFreshSeconds = paramInt4;
    this.onlyIfCached = paramBoolean6;
    this.noTransform = paramBoolean7;
    this.headerValue = paramString;
  }
  
  private String headerValue() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.noCache)
      stringBuilder.append("no-cache, "); 
    if (this.noStore)
      stringBuilder.append("no-store, "); 
    if (this.maxAgeSeconds != -1) {
      stringBuilder.append("max-age=");
      stringBuilder.append(this.maxAgeSeconds);
      stringBuilder.append(", ");
    } 
    if (this.sMaxAgeSeconds != -1) {
      stringBuilder.append("s-maxage=");
      stringBuilder.append(this.sMaxAgeSeconds);
      stringBuilder.append(", ");
    } 
    if (this.isPrivate)
      stringBuilder.append("private, "); 
    if (this.isPublic)
      stringBuilder.append("public, "); 
    if (this.mustRevalidate)
      stringBuilder.append("must-revalidate, "); 
    if (this.maxStaleSeconds != -1) {
      stringBuilder.append("max-stale=");
      stringBuilder.append(this.maxStaleSeconds);
      stringBuilder.append(", ");
    } 
    if (this.minFreshSeconds != -1) {
      stringBuilder.append("min-fresh=");
      stringBuilder.append(this.minFreshSeconds);
      stringBuilder.append(", ");
    } 
    if (this.onlyIfCached)
      stringBuilder.append("only-if-cached, "); 
    if (this.noTransform)
      stringBuilder.append("no-transform, "); 
    if (stringBuilder.length() == 0)
      return ""; 
    stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
    return stringBuilder.toString();
  }
  
  public static CacheControl parse(Headers paramHeaders) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual size : ()I
    //   4: istore #13
    //   6: iconst_0
    //   7: istore #7
    //   9: iconst_1
    //   10: istore_2
    //   11: aconst_null
    //   12: astore #28
    //   14: iconst_0
    //   15: istore #20
    //   17: iconst_0
    //   18: istore #19
    //   20: iconst_m1
    //   21: istore #6
    //   23: iconst_m1
    //   24: istore #5
    //   26: iconst_0
    //   27: istore #18
    //   29: iconst_0
    //   30: istore #17
    //   32: iconst_0
    //   33: istore #16
    //   35: iconst_m1
    //   36: istore #4
    //   38: iconst_m1
    //   39: istore_3
    //   40: iconst_0
    //   41: istore #14
    //   43: iconst_0
    //   44: istore #15
    //   46: iload #7
    //   48: iload #13
    //   50: if_icmpge -> 1090
    //   53: aload_0
    //   54: iload #7
    //   56: invokevirtual name : (I)Ljava/lang/String;
    //   59: astore #31
    //   61: aload_0
    //   62: iload #7
    //   64: invokevirtual value : (I)Ljava/lang/String;
    //   67: astore #30
    //   69: aload #31
    //   71: ldc 'Cache-Control'
    //   73: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   76: ifeq -> 94
    //   79: aload #28
    //   81: ifnull -> 87
    //   84: goto -> 153
    //   87: aload #30
    //   89: astore #28
    //   91: goto -> 155
    //   94: iload_2
    //   95: istore #11
    //   97: aload #28
    //   99: astore #29
    //   101: iload #20
    //   103: istore #22
    //   105: iload #19
    //   107: istore #21
    //   109: iload #6
    //   111: istore #8
    //   113: iload #5
    //   115: istore #10
    //   117: iload #18
    //   119: istore #25
    //   121: iload #17
    //   123: istore #27
    //   125: iload #16
    //   127: istore #26
    //   129: iload #4
    //   131: istore #9
    //   133: iload_3
    //   134: istore_1
    //   135: iload #14
    //   137: istore #24
    //   139: iload #15
    //   141: istore #23
    //   143: aload #31
    //   145: ldc 'Pragma'
    //   147: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   150: ifeq -> 1035
    //   153: iconst_0
    //   154: istore_2
    //   155: iconst_0
    //   156: istore #12
    //   158: iload_2
    //   159: istore #11
    //   161: aload #28
    //   163: astore #29
    //   165: iload #20
    //   167: istore #22
    //   169: iload #19
    //   171: istore #21
    //   173: iload #6
    //   175: istore #8
    //   177: iload #5
    //   179: istore #10
    //   181: iload #18
    //   183: istore #25
    //   185: iload #17
    //   187: istore #27
    //   189: iload #16
    //   191: istore #26
    //   193: iload #4
    //   195: istore #9
    //   197: iload_3
    //   198: istore_1
    //   199: iload #14
    //   201: istore #24
    //   203: iload #15
    //   205: istore #23
    //   207: iload #12
    //   209: aload #30
    //   211: invokevirtual length : ()I
    //   214: if_icmpge -> 1035
    //   217: aload #30
    //   219: iload #12
    //   221: ldc '=,;'
    //   223: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   226: istore_1
    //   227: aload #30
    //   229: iload #12
    //   231: iload_1
    //   232: invokevirtual substring : (II)Ljava/lang/String;
    //   235: invokevirtual trim : ()Ljava/lang/String;
    //   238: astore #31
    //   240: iload_1
    //   241: aload #30
    //   243: invokevirtual length : ()I
    //   246: if_icmpeq -> 361
    //   249: aload #30
    //   251: iload_1
    //   252: invokevirtual charAt : (I)C
    //   255: bipush #44
    //   257: if_icmpeq -> 361
    //   260: aload #30
    //   262: iload_1
    //   263: invokevirtual charAt : (I)C
    //   266: bipush #59
    //   268: if_icmpne -> 274
    //   271: goto -> 361
    //   274: aload #30
    //   276: iload_1
    //   277: iconst_1
    //   278: iadd
    //   279: invokestatic skipWhitespace : (Ljava/lang/String;I)I
    //   282: istore #8
    //   284: iload #8
    //   286: aload #30
    //   288: invokevirtual length : ()I
    //   291: if_icmpge -> 335
    //   294: aload #30
    //   296: iload #8
    //   298: invokevirtual charAt : (I)C
    //   301: bipush #34
    //   303: if_icmpne -> 335
    //   306: iinc #8, 1
    //   309: aload #30
    //   311: iload #8
    //   313: ldc '"'
    //   315: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   318: istore_1
    //   319: aload #30
    //   321: iload #8
    //   323: iload_1
    //   324: invokevirtual substring : (II)Ljava/lang/String;
    //   327: astore #29
    //   329: iinc #1, 1
    //   332: goto -> 367
    //   335: aload #30
    //   337: iload #8
    //   339: ldc ',;'
    //   341: invokestatic skipUntil : (Ljava/lang/String;ILjava/lang/String;)I
    //   344: istore_1
    //   345: aload #30
    //   347: iload #8
    //   349: iload_1
    //   350: invokevirtual substring : (II)Ljava/lang/String;
    //   353: invokevirtual trim : ()Ljava/lang/String;
    //   356: astore #29
    //   358: goto -> 367
    //   361: iinc #1, 1
    //   364: aconst_null
    //   365: astore #29
    //   367: ldc 'no-cache'
    //   369: aload #31
    //   371: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   374: ifeq -> 418
    //   377: iconst_1
    //   378: istore #21
    //   380: iload #19
    //   382: istore #22
    //   384: iload #6
    //   386: istore #8
    //   388: iload #5
    //   390: istore #9
    //   392: iload #18
    //   394: istore #23
    //   396: iload #17
    //   398: istore #24
    //   400: iload #16
    //   402: istore #25
    //   404: iload #4
    //   406: istore #10
    //   408: iload_3
    //   409: istore #11
    //   411: iload #14
    //   413: istore #26
    //   415: goto -> 990
    //   418: ldc 'no-store'
    //   420: aload #31
    //   422: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   425: ifeq -> 469
    //   428: iconst_1
    //   429: istore #22
    //   431: iload #20
    //   433: istore #21
    //   435: iload #6
    //   437: istore #8
    //   439: iload #5
    //   441: istore #9
    //   443: iload #18
    //   445: istore #23
    //   447: iload #17
    //   449: istore #24
    //   451: iload #16
    //   453: istore #25
    //   455: iload #4
    //   457: istore #10
    //   459: iload_3
    //   460: istore #11
    //   462: iload #14
    //   464: istore #26
    //   466: goto -> 990
    //   469: ldc 'max-age'
    //   471: aload #31
    //   473: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   476: ifeq -> 525
    //   479: aload #29
    //   481: iconst_m1
    //   482: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   485: istore #8
    //   487: iload #20
    //   489: istore #21
    //   491: iload #19
    //   493: istore #22
    //   495: iload #5
    //   497: istore #9
    //   499: iload #18
    //   501: istore #23
    //   503: iload #17
    //   505: istore #24
    //   507: iload #16
    //   509: istore #25
    //   511: iload #4
    //   513: istore #10
    //   515: iload_3
    //   516: istore #11
    //   518: iload #14
    //   520: istore #26
    //   522: goto -> 990
    //   525: ldc 's-maxage'
    //   527: aload #31
    //   529: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   532: ifeq -> 581
    //   535: aload #29
    //   537: iconst_m1
    //   538: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   541: istore #9
    //   543: iload #20
    //   545: istore #21
    //   547: iload #19
    //   549: istore #22
    //   551: iload #6
    //   553: istore #8
    //   555: iload #18
    //   557: istore #23
    //   559: iload #17
    //   561: istore #24
    //   563: iload #16
    //   565: istore #25
    //   567: iload #4
    //   569: istore #10
    //   571: iload_3
    //   572: istore #11
    //   574: iload #14
    //   576: istore #26
    //   578: goto -> 990
    //   581: ldc 'private'
    //   583: aload #31
    //   585: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   588: ifeq -> 632
    //   591: iconst_1
    //   592: istore #23
    //   594: iload #20
    //   596: istore #21
    //   598: iload #19
    //   600: istore #22
    //   602: iload #6
    //   604: istore #8
    //   606: iload #5
    //   608: istore #9
    //   610: iload #17
    //   612: istore #24
    //   614: iload #16
    //   616: istore #25
    //   618: iload #4
    //   620: istore #10
    //   622: iload_3
    //   623: istore #11
    //   625: iload #14
    //   627: istore #26
    //   629: goto -> 990
    //   632: ldc 'public'
    //   634: aload #31
    //   636: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   639: ifeq -> 683
    //   642: iconst_1
    //   643: istore #24
    //   645: iload #20
    //   647: istore #21
    //   649: iload #19
    //   651: istore #22
    //   653: iload #6
    //   655: istore #8
    //   657: iload #5
    //   659: istore #9
    //   661: iload #18
    //   663: istore #23
    //   665: iload #16
    //   667: istore #25
    //   669: iload #4
    //   671: istore #10
    //   673: iload_3
    //   674: istore #11
    //   676: iload #14
    //   678: istore #26
    //   680: goto -> 990
    //   683: ldc 'must-revalidate'
    //   685: aload #31
    //   687: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   690: ifeq -> 734
    //   693: iconst_1
    //   694: istore #25
    //   696: iload #20
    //   698: istore #21
    //   700: iload #19
    //   702: istore #22
    //   704: iload #6
    //   706: istore #8
    //   708: iload #5
    //   710: istore #9
    //   712: iload #18
    //   714: istore #23
    //   716: iload #17
    //   718: istore #24
    //   720: iload #4
    //   722: istore #10
    //   724: iload_3
    //   725: istore #11
    //   727: iload #14
    //   729: istore #26
    //   731: goto -> 990
    //   734: ldc 'max-stale'
    //   736: aload #31
    //   738: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   741: ifeq -> 791
    //   744: aload #29
    //   746: ldc 2147483647
    //   748: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   751: istore #10
    //   753: iload #20
    //   755: istore #21
    //   757: iload #19
    //   759: istore #22
    //   761: iload #6
    //   763: istore #8
    //   765: iload #5
    //   767: istore #9
    //   769: iload #18
    //   771: istore #23
    //   773: iload #17
    //   775: istore #24
    //   777: iload #16
    //   779: istore #25
    //   781: iload_3
    //   782: istore #11
    //   784: iload #14
    //   786: istore #26
    //   788: goto -> 990
    //   791: ldc 'min-fresh'
    //   793: aload #31
    //   795: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   798: ifeq -> 848
    //   801: aload #29
    //   803: iconst_m1
    //   804: invokestatic parseSeconds : (Ljava/lang/String;I)I
    //   807: istore #11
    //   809: iload #20
    //   811: istore #21
    //   813: iload #19
    //   815: istore #22
    //   817: iload #6
    //   819: istore #8
    //   821: iload #5
    //   823: istore #9
    //   825: iload #18
    //   827: istore #23
    //   829: iload #17
    //   831: istore #24
    //   833: iload #16
    //   835: istore #25
    //   837: iload #4
    //   839: istore #10
    //   841: iload #14
    //   843: istore #26
    //   845: goto -> 990
    //   848: ldc 'only-if-cached'
    //   850: aload #31
    //   852: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   855: ifeq -> 899
    //   858: iconst_1
    //   859: istore #26
    //   861: iload #20
    //   863: istore #21
    //   865: iload #19
    //   867: istore #22
    //   869: iload #6
    //   871: istore #8
    //   873: iload #5
    //   875: istore #9
    //   877: iload #18
    //   879: istore #23
    //   881: iload #17
    //   883: istore #24
    //   885: iload #16
    //   887: istore #25
    //   889: iload #4
    //   891: istore #10
    //   893: iload_3
    //   894: istore #11
    //   896: goto -> 990
    //   899: iload #20
    //   901: istore #21
    //   903: iload #19
    //   905: istore #22
    //   907: iload #6
    //   909: istore #8
    //   911: iload #5
    //   913: istore #9
    //   915: iload #18
    //   917: istore #23
    //   919: iload #17
    //   921: istore #24
    //   923: iload #16
    //   925: istore #25
    //   927: iload #4
    //   929: istore #10
    //   931: iload_3
    //   932: istore #11
    //   934: iload #14
    //   936: istore #26
    //   938: ldc 'no-transform'
    //   940: aload #31
    //   942: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   945: ifeq -> 990
    //   948: iconst_1
    //   949: istore #15
    //   951: iload #14
    //   953: istore #26
    //   955: iload_3
    //   956: istore #11
    //   958: iload #4
    //   960: istore #10
    //   962: iload #16
    //   964: istore #25
    //   966: iload #17
    //   968: istore #24
    //   970: iload #18
    //   972: istore #23
    //   974: iload #5
    //   976: istore #9
    //   978: iload #6
    //   980: istore #8
    //   982: iload #19
    //   984: istore #22
    //   986: iload #20
    //   988: istore #21
    //   990: iload_1
    //   991: istore #12
    //   993: iload #21
    //   995: istore #20
    //   997: iload #22
    //   999: istore #19
    //   1001: iload #8
    //   1003: istore #6
    //   1005: iload #9
    //   1007: istore #5
    //   1009: iload #23
    //   1011: istore #18
    //   1013: iload #24
    //   1015: istore #17
    //   1017: iload #25
    //   1019: istore #16
    //   1021: iload #10
    //   1023: istore #4
    //   1025: iload #11
    //   1027: istore_3
    //   1028: iload #26
    //   1030: istore #14
    //   1032: goto -> 158
    //   1035: iinc #7, 1
    //   1038: iload #11
    //   1040: istore_2
    //   1041: aload #29
    //   1043: astore #28
    //   1045: iload #22
    //   1047: istore #20
    //   1049: iload #21
    //   1051: istore #19
    //   1053: iload #8
    //   1055: istore #6
    //   1057: iload #10
    //   1059: istore #5
    //   1061: iload #25
    //   1063: istore #18
    //   1065: iload #27
    //   1067: istore #17
    //   1069: iload #26
    //   1071: istore #16
    //   1073: iload #9
    //   1075: istore #4
    //   1077: iload_1
    //   1078: istore_3
    //   1079: iload #24
    //   1081: istore #14
    //   1083: iload #23
    //   1085: istore #15
    //   1087: goto -> 46
    //   1090: iload_2
    //   1091: ifne -> 1100
    //   1094: aconst_null
    //   1095: astore #28
    //   1097: goto -> 1100
    //   1100: new okhttp3/CacheControl
    //   1103: dup
    //   1104: iload #20
    //   1106: iload #19
    //   1108: iload #6
    //   1110: iload #5
    //   1112: iload #18
    //   1114: iload #17
    //   1116: iload #16
    //   1118: iload #4
    //   1120: iload_3
    //   1121: iload #14
    //   1123: iload #15
    //   1125: aload #28
    //   1127: invokespecial <init> : (ZZIIZZZIIZZLjava/lang/String;)V
    //   1130: areturn
  }
  
  public boolean isPrivate() {
    return this.isPrivate;
  }
  
  public boolean isPublic() {
    return this.isPublic;
  }
  
  public int maxAgeSeconds() {
    return this.maxAgeSeconds;
  }
  
  public int maxStaleSeconds() {
    return this.maxStaleSeconds;
  }
  
  public int minFreshSeconds() {
    return this.minFreshSeconds;
  }
  
  public boolean mustRevalidate() {
    return this.mustRevalidate;
  }
  
  public boolean noCache() {
    return this.noCache;
  }
  
  public boolean noStore() {
    return this.noStore;
  }
  
  public boolean noTransform() {
    return this.noTransform;
  }
  
  public boolean onlyIfCached() {
    return this.onlyIfCached;
  }
  
  public int sMaxAgeSeconds() {
    return this.sMaxAgeSeconds;
  }
  
  public String toString() {
    String str = this.headerValue;
    if (str == null) {
      str = headerValue();
      this.headerValue = str;
    } 
    return str;
  }
  
  public static final class Builder {
    int maxAgeSeconds = -1;
    
    int maxStaleSeconds = -1;
    
    int minFreshSeconds = -1;
    
    boolean noCache;
    
    boolean noStore;
    
    boolean noTransform;
    
    boolean onlyIfCached;
    
    public CacheControl build() {
      return new CacheControl(this);
    }
    
    public Builder maxAge(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.maxAgeSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("maxAge < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder maxStale(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.maxStaleSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("maxStale < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder minFresh(int param1Int, TimeUnit param1TimeUnit) {
      if (param1Int >= 0) {
        long l = param1TimeUnit.toSeconds(param1Int);
        if (l > 2147483647L) {
          param1Int = Integer.MAX_VALUE;
        } else {
          param1Int = (int)l;
        } 
        this.minFreshSeconds = param1Int;
        return this;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("minFresh < 0: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Builder noCache() {
      this.noCache = true;
      return this;
    }
    
    public Builder noStore() {
      this.noStore = true;
      return this;
    }
    
    public Builder noTransform() {
      this.noTransform = true;
      return this;
    }
    
    public Builder onlyIfCached() {
      this.onlyIfCached = true;
      return this;
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\CacheControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */