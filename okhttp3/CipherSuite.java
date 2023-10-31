package okhttp3;

public enum CipherSuite {
  TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA,
  TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA,
  TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
  TLS_DHE_DSS_WITH_AES_128_CBC_SHA256,
  TLS_DHE_DSS_WITH_AES_128_GCM_SHA256,
  TLS_DHE_DSS_WITH_AES_256_CBC_SHA,
  TLS_DHE_DSS_WITH_AES_256_CBC_SHA256,
  TLS_DHE_DSS_WITH_AES_256_GCM_SHA384,
  TLS_DHE_DSS_WITH_DES_CBC_SHA,
  TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA,
  TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA,
  TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
  TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,
  TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
  TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
  TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,
  TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
  TLS_DHE_RSA_WITH_DES_CBC_SHA,
  TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA,
  TLS_DH_anon_EXPORT_WITH_RC4_40_MD5,
  TLS_DH_anon_WITH_3DES_EDE_CBC_SHA,
  TLS_DH_anon_WITH_AES_128_CBC_SHA,
  TLS_DH_anon_WITH_AES_128_CBC_SHA256,
  TLS_DH_anon_WITH_AES_128_GCM_SHA256,
  TLS_DH_anon_WITH_AES_256_CBC_SHA,
  TLS_DH_anon_WITH_AES_256_CBC_SHA256,
  TLS_DH_anon_WITH_AES_256_GCM_SHA384,
  TLS_DH_anon_WITH_DES_CBC_SHA,
  TLS_DH_anon_WITH_RC4_128_MD5,
  TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA,
  TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
  TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,
  TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
  TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
  TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384,
  TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
  TLS_ECDHE_ECDSA_WITH_NULL_SHA,
  TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
  TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA,
  TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
  TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
  TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
  TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
  TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
  TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
  TLS_ECDHE_RSA_WITH_NULL_SHA,
  TLS_ECDHE_RSA_WITH_RC4_128_SHA,
  TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA,
  TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA,
  TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256,
  TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256,
  TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA,
  TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384,
  TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384,
  TLS_ECDH_ECDSA_WITH_NULL_SHA,
  TLS_ECDH_ECDSA_WITH_RC4_128_SHA,
  TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA,
  TLS_ECDH_RSA_WITH_AES_128_CBC_SHA,
  TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256,
  TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256,
  TLS_ECDH_RSA_WITH_AES_256_CBC_SHA,
  TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384,
  TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384,
  TLS_ECDH_RSA_WITH_NULL_SHA,
  TLS_ECDH_RSA_WITH_RC4_128_SHA,
  TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA,
  TLS_ECDH_anon_WITH_AES_128_CBC_SHA,
  TLS_ECDH_anon_WITH_AES_256_CBC_SHA,
  TLS_ECDH_anon_WITH_NULL_SHA,
  TLS_ECDH_anon_WITH_RC4_128_SHA,
  TLS_EMPTY_RENEGOTIATION_INFO_SCSV,
  TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5,
  TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA,
  TLS_KRB5_EXPORT_WITH_RC4_40_MD5,
  TLS_KRB5_EXPORT_WITH_RC4_40_SHA,
  TLS_KRB5_WITH_3DES_EDE_CBC_MD5,
  TLS_KRB5_WITH_3DES_EDE_CBC_SHA,
  TLS_KRB5_WITH_DES_CBC_MD5,
  TLS_KRB5_WITH_DES_CBC_SHA,
  TLS_KRB5_WITH_RC4_128_MD5,
  TLS_KRB5_WITH_RC4_128_SHA,
  TLS_RSA_EXPORT_WITH_DES40_CBC_SHA,
  TLS_RSA_EXPORT_WITH_RC4_40_MD5,
  TLS_RSA_WITH_3DES_EDE_CBC_SHA,
  TLS_RSA_WITH_AES_128_CBC_SHA,
  TLS_RSA_WITH_AES_128_CBC_SHA256,
  TLS_RSA_WITH_AES_128_GCM_SHA256,
  TLS_RSA_WITH_AES_256_CBC_SHA,
  TLS_RSA_WITH_AES_256_CBC_SHA256,
  TLS_RSA_WITH_AES_256_GCM_SHA384,
  TLS_RSA_WITH_DES_CBC_SHA,
  TLS_RSA_WITH_NULL_MD5("SSL_RSA_WITH_NULL_MD5", 1, 5246, 6, 10),
  TLS_RSA_WITH_NULL_SHA("SSL_RSA_WITH_NULL_SHA", 2, 5246, 6, 10),
  TLS_RSA_WITH_NULL_SHA256("SSL_RSA_WITH_NULL_SHA", 2, 5246, 6, 10),
  TLS_RSA_WITH_RC4_128_MD5("SSL_RSA_WITH_NULL_SHA", 2, 5246, 6, 10),
  TLS_RSA_WITH_RC4_128_SHA("SSL_RSA_WITH_NULL_SHA", 2, 5246, 6, 10);
  
  private static final CipherSuite[] $VALUES;
  
  final String javaName;
  
  static {
    TLS_RSA_EXPORT_WITH_RC4_40_MD5 = new CipherSuite("TLS_RSA_EXPORT_WITH_RC4_40_MD5", 2, "SSL_RSA_EXPORT_WITH_RC4_40_MD5", 3, 4346, 6, 10);
    TLS_RSA_WITH_RC4_128_MD5 = new CipherSuite("TLS_RSA_WITH_RC4_128_MD5", 3, "SSL_RSA_WITH_RC4_128_MD5", 4, 5246, 6, 10);
    TLS_RSA_WITH_RC4_128_SHA = new CipherSuite("TLS_RSA_WITH_RC4_128_SHA", 4, "SSL_RSA_WITH_RC4_128_SHA", 5, 5246, 6, 10);
    TLS_RSA_EXPORT_WITH_DES40_CBC_SHA = new CipherSuite("TLS_RSA_EXPORT_WITH_DES40_CBC_SHA", 5, "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA", 8, 4346, 6, 10);
    TLS_RSA_WITH_DES_CBC_SHA = new CipherSuite("TLS_RSA_WITH_DES_CBC_SHA", 6, "SSL_RSA_WITH_DES_CBC_SHA", 9, 5469, 6, 10);
    TLS_RSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_RSA_WITH_3DES_EDE_CBC_SHA", 7, "SSL_RSA_WITH_3DES_EDE_CBC_SHA", 10, 5246, 6, 10);
    TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA = new CipherSuite("TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 8, "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", 17, 4346, 6, 10);
    TLS_DHE_DSS_WITH_DES_CBC_SHA = new CipherSuite("TLS_DHE_DSS_WITH_DES_CBC_SHA", 9, "SSL_DHE_DSS_WITH_DES_CBC_SHA", 18, 5469, 6, 10);
    TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 10, "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 19, 5246, 6, 10);
    TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = new CipherSuite("TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", 11, "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", 20, 4346, 6, 10);
    TLS_DHE_RSA_WITH_DES_CBC_SHA = new CipherSuite("TLS_DHE_RSA_WITH_DES_CBC_SHA", 12, "SSL_DHE_RSA_WITH_DES_CBC_SHA", 21, 5469, 6, 10);
    TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 13, "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 22, 5246, 6, 10);
    TLS_DH_anon_EXPORT_WITH_RC4_40_MD5 = new CipherSuite("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", 14, "SSL_DH_anon_EXPORT_WITH_RC4_40_MD5", 23, 4346, 6, 10);
    TLS_DH_anon_WITH_RC4_128_MD5 = new CipherSuite("TLS_DH_anon_WITH_RC4_128_MD5", 15, "SSL_DH_anon_WITH_RC4_128_MD5", 24, 5246, 6, 10);
    TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA = new CipherSuite("TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 16, "SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA", 25, 4346, 6, 10);
    TLS_DH_anon_WITH_DES_CBC_SHA = new CipherSuite("TLS_DH_anon_WITH_DES_CBC_SHA", 17, "SSL_DH_anon_WITH_DES_CBC_SHA", 26, 5469, 6, 10);
    TLS_DH_anon_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_DH_anon_WITH_3DES_EDE_CBC_SHA", 18, "SSL_DH_anon_WITH_3DES_EDE_CBC_SHA", 27, 5246, 6, 10);
    TLS_KRB5_WITH_DES_CBC_SHA = new CipherSuite("TLS_KRB5_WITH_DES_CBC_SHA", 19, "TLS_KRB5_WITH_DES_CBC_SHA", 30, 2712, 6, 2147483647);
    TLS_KRB5_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_KRB5_WITH_3DES_EDE_CBC_SHA", 20, "TLS_KRB5_WITH_3DES_EDE_CBC_SHA", 31, 2712, 6, 2147483647);
    TLS_KRB5_WITH_RC4_128_SHA = new CipherSuite("TLS_KRB5_WITH_RC4_128_SHA", 21, "TLS_KRB5_WITH_RC4_128_SHA", 32, 2712, 6, 2147483647);
    TLS_KRB5_WITH_DES_CBC_MD5 = new CipherSuite("TLS_KRB5_WITH_DES_CBC_MD5", 22, "TLS_KRB5_WITH_DES_CBC_MD5", 34, 2712, 6, 2147483647);
    TLS_KRB5_WITH_3DES_EDE_CBC_MD5 = new CipherSuite("TLS_KRB5_WITH_3DES_EDE_CBC_MD5", 23, "TLS_KRB5_WITH_3DES_EDE_CBC_MD5", 35, 2712, 6, 2147483647);
    TLS_KRB5_WITH_RC4_128_MD5 = new CipherSuite("TLS_KRB5_WITH_RC4_128_MD5", 24, "TLS_KRB5_WITH_RC4_128_MD5", 36, 2712, 6, 2147483647);
    TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA = new CipherSuite("TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", 25, "TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", 38, 2712, 6, 2147483647);
    TLS_KRB5_EXPORT_WITH_RC4_40_SHA = new CipherSuite("TLS_KRB5_EXPORT_WITH_RC4_40_SHA", 26, "TLS_KRB5_EXPORT_WITH_RC4_40_SHA", 40, 2712, 6, 2147483647);
    TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5 = new CipherSuite("TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5", 27, "TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5", 41, 2712, 6, 2147483647);
    TLS_KRB5_EXPORT_WITH_RC4_40_MD5 = new CipherSuite("TLS_KRB5_EXPORT_WITH_RC4_40_MD5", 28, "TLS_KRB5_EXPORT_WITH_RC4_40_MD5", 43, 2712, 6, 2147483647);
    TLS_RSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA", 29, "TLS_RSA_WITH_AES_128_CBC_SHA", 47, 5246, 6, 10);
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 30, "TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 50, 5246, 6, 10);
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 31, "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 51, 5246, 6, 10);
    TLS_DH_anon_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_DH_anon_WITH_AES_128_CBC_SHA", 32, "TLS_DH_anon_WITH_AES_128_CBC_SHA", 52, 5246, 6, 10);
    TLS_RSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_RSA_WITH_AES_256_CBC_SHA", 33, "TLS_RSA_WITH_AES_256_CBC_SHA", 53, 5246, 6, 10);
    TLS_DHE_DSS_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 34, "TLS_DHE_DSS_WITH_AES_256_CBC_SHA", 56, 5246, 6, 10);
    TLS_DHE_RSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 35, "TLS_DHE_RSA_WITH_AES_256_CBC_SHA", 57, 5246, 6, 10);
    TLS_DH_anon_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_DH_anon_WITH_AES_256_CBC_SHA", 36, "TLS_DH_anon_WITH_AES_256_CBC_SHA", 58, 5246, 6, 10);
    TLS_RSA_WITH_NULL_SHA256 = new CipherSuite("TLS_RSA_WITH_NULL_SHA256", 37, "TLS_RSA_WITH_NULL_SHA256", 59, 5246, 7, 21);
    TLS_RSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_RSA_WITH_AES_128_CBC_SHA256", 38, "TLS_RSA_WITH_AES_128_CBC_SHA256", 60, 5246, 7, 21);
    TLS_RSA_WITH_AES_256_CBC_SHA256 = new CipherSuite("TLS_RSA_WITH_AES_256_CBC_SHA256", 39, "TLS_RSA_WITH_AES_256_CBC_SHA256", 61, 5246, 7, 21);
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 40, "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 64, 5246, 7, 21);
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 41, "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 103, 5246, 7, 21);
    TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 = new CipherSuite("TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 42, "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 106, 5246, 7, 21);
    TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 = new CipherSuite("TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 43, "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 107, 5246, 7, 21);
    TLS_DH_anon_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_DH_anon_WITH_AES_128_CBC_SHA256", 44, "TLS_DH_anon_WITH_AES_128_CBC_SHA256", 108, 5246, 7, 21);
    TLS_DH_anon_WITH_AES_256_CBC_SHA256 = new CipherSuite("TLS_DH_anon_WITH_AES_256_CBC_SHA256", 45, "TLS_DH_anon_WITH_AES_256_CBC_SHA256", 109, 5246, 7, 21);
    TLS_RSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_RSA_WITH_AES_128_GCM_SHA256", 46, "TLS_RSA_WITH_AES_128_GCM_SHA256", 156, 5288, 8, 21);
    TLS_RSA_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_RSA_WITH_AES_256_GCM_SHA384", 47, "TLS_RSA_WITH_AES_256_GCM_SHA384", 157, 5288, 8, 21);
    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 48, "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 158, 5288, 8, 21);
    TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 49, "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 159, 5288, 8, 21);
    TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 50, "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 162, 5288, 8, 21);
    TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 51, "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 163, 5288, 8, 21);
    TLS_DH_anon_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_DH_anon_WITH_AES_128_GCM_SHA256", 52, "TLS_DH_anon_WITH_AES_128_GCM_SHA256", 166, 5288, 8, 21);
    TLS_DH_anon_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_DH_anon_WITH_AES_256_GCM_SHA384", 53, "TLS_DH_anon_WITH_AES_256_GCM_SHA384", 167, 5288, 8, 21);
    TLS_EMPTY_RENEGOTIATION_INFO_SCSV = new CipherSuite("TLS_EMPTY_RENEGOTIATION_INFO_SCSV", 54, "TLS_EMPTY_RENEGOTIATION_INFO_SCSV", 255, 5746, 6, 14);
    TLS_ECDH_ECDSA_WITH_NULL_SHA = new CipherSuite("TLS_ECDH_ECDSA_WITH_NULL_SHA", 55, "TLS_ECDH_ECDSA_WITH_NULL_SHA", 49153, 4492, 7, 14);
    TLS_ECDH_ECDSA_WITH_RC4_128_SHA = new CipherSuite("TLS_ECDH_ECDSA_WITH_RC4_128_SHA", 56, "TLS_ECDH_ECDSA_WITH_RC4_128_SHA", 49154, 4492, 7, 14);
    TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", 57, "TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", 49155, 4492, 7, 14);
    TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", 58, "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA", 49156, 4492, 7, 14);
    TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", 59, "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA", 49157, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_NULL_SHA = new CipherSuite("TLS_ECDHE_ECDSA_WITH_NULL_SHA", 60, "TLS_ECDHE_ECDSA_WITH_NULL_SHA", 49158, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_RC4_128_SHA = new CipherSuite("TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", 61, "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", 49159, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", 62, "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", 49160, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 63, "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 49161, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 64, "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 49162, 4492, 7, 14);
    TLS_ECDH_RSA_WITH_NULL_SHA = new CipherSuite("TLS_ECDH_RSA_WITH_NULL_SHA", 65, "TLS_ECDH_RSA_WITH_NULL_SHA", 49163, 4492, 7, 14);
    TLS_ECDH_RSA_WITH_RC4_128_SHA = new CipherSuite("TLS_ECDH_RSA_WITH_RC4_128_SHA", 66, "TLS_ECDH_RSA_WITH_RC4_128_SHA", 49164, 4492, 7, 14);
    TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", 67, "TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", 49165, 4492, 7, 14);
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 68, "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 49166, 4492, 7, 14);
    TLS_ECDH_RSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", 69, "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA", 49167, 4492, 7, 14);
    TLS_ECDHE_RSA_WITH_NULL_SHA = new CipherSuite("TLS_ECDHE_RSA_WITH_NULL_SHA", 70, "TLS_ECDHE_RSA_WITH_NULL_SHA", 49168, 4492, 7, 14);
    TLS_ECDHE_RSA_WITH_RC4_128_SHA = new CipherSuite("TLS_ECDHE_RSA_WITH_RC4_128_SHA", 71, "TLS_ECDHE_RSA_WITH_RC4_128_SHA", 49169, 4492, 7, 14);
    TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", 72, "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", 49170, 4492, 7, 14);
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 73, "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 49171, 4492, 7, 14);
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", 74, "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", 49172, 4492, 7, 14);
    TLS_ECDH_anon_WITH_NULL_SHA = new CipherSuite("TLS_ECDH_anon_WITH_NULL_SHA", 75, "TLS_ECDH_anon_WITH_NULL_SHA", 49173, 4492, 7, 14);
    TLS_ECDH_anon_WITH_RC4_128_SHA = new CipherSuite("TLS_ECDH_anon_WITH_RC4_128_SHA", 76, "TLS_ECDH_anon_WITH_RC4_128_SHA", 49174, 4492, 7, 14);
    TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA = new CipherSuite("TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", 77, "TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", 49175, 4492, 7, 14);
    TLS_ECDH_anon_WITH_AES_128_CBC_SHA = new CipherSuite("TLS_ECDH_anon_WITH_AES_128_CBC_SHA", 78, "TLS_ECDH_anon_WITH_AES_128_CBC_SHA", 49176, 4492, 7, 14);
    TLS_ECDH_anon_WITH_AES_256_CBC_SHA = new CipherSuite("TLS_ECDH_anon_WITH_AES_256_CBC_SHA", 79, "TLS_ECDH_anon_WITH_AES_256_CBC_SHA", 49177, 4492, 7, 14);
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 80, "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 49187, 5289, 7, 21);
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 81, "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 49188, 5289, 7, 21);
    TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 82, "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 49189, 5289, 7, 21);
    TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 83, "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 49190, 5289, 7, 21);
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 84, "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 49191, 5289, 7, 21);
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 85, "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 49192, 5289, 7, 21);
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 = new CipherSuite("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 86, "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 49193, 5289, 7, 21);
    TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 = new CipherSuite("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 87, "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 49194, 5289, 7, 21);
    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 88, "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 49195, 5289, 8, 21);
    TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 89, "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 49196, 5289, 8, 21);
    TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 90, "TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 49197, 5289, 8, 21);
    TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 91, "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 49198, 5289, 8, 21);
    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 92, "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 49199, 5289, 8, 21);
    TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 = new CipherSuite("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 93, "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 49200, 5289, 8, 21);
    TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 = new CipherSuite("TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 94, "TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 49201, 5289, 8, 21);
    CipherSuite cipherSuite = new CipherSuite("TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 95, "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 49202, 5289, 8, 21);
    TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 = cipherSuite;
    $VALUES = new CipherSuite[] { 
        TLS_RSA_WITH_NULL_MD5, TLS_RSA_WITH_NULL_SHA, TLS_RSA_EXPORT_WITH_RC4_40_MD5, TLS_RSA_WITH_RC4_128_MD5, TLS_RSA_WITH_RC4_128_SHA, TLS_RSA_EXPORT_WITH_DES40_CBC_SHA, TLS_RSA_WITH_DES_CBC_SHA, TLS_RSA_WITH_3DES_EDE_CBC_SHA, TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA, TLS_DHE_DSS_WITH_DES_CBC_SHA, 
        TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA, TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA, TLS_DHE_RSA_WITH_DES_CBC_SHA, TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA, TLS_DH_anon_EXPORT_WITH_RC4_40_MD5, TLS_DH_anon_WITH_RC4_128_MD5, TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA, TLS_DH_anon_WITH_DES_CBC_SHA, TLS_DH_anon_WITH_3DES_EDE_CBC_SHA, TLS_KRB5_WITH_DES_CBC_SHA, 
        TLS_KRB5_WITH_3DES_EDE_CBC_SHA, TLS_KRB5_WITH_RC4_128_SHA, TLS_KRB5_WITH_DES_CBC_MD5, TLS_KRB5_WITH_3DES_EDE_CBC_MD5, TLS_KRB5_WITH_RC4_128_MD5, TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA, TLS_KRB5_EXPORT_WITH_RC4_40_SHA, TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5, TLS_KRB5_EXPORT_WITH_RC4_40_MD5, TLS_RSA_WITH_AES_128_CBC_SHA, 
        TLS_DHE_DSS_WITH_AES_128_CBC_SHA, TLS_DHE_RSA_WITH_AES_128_CBC_SHA, TLS_DH_anon_WITH_AES_128_CBC_SHA, TLS_RSA_WITH_AES_256_CBC_SHA, TLS_DHE_DSS_WITH_AES_256_CBC_SHA, TLS_DHE_RSA_WITH_AES_256_CBC_SHA, TLS_DH_anon_WITH_AES_256_CBC_SHA, TLS_RSA_WITH_NULL_SHA256, TLS_RSA_WITH_AES_128_CBC_SHA256, TLS_RSA_WITH_AES_256_CBC_SHA256, 
        TLS_DHE_DSS_WITH_AES_128_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DH_anon_WITH_AES_128_CBC_SHA256, TLS_DH_anon_WITH_AES_256_CBC_SHA256, TLS_RSA_WITH_AES_128_GCM_SHA256, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, 
        TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, TLS_DHE_DSS_WITH_AES_256_GCM_SHA384, TLS_DH_anon_WITH_AES_128_GCM_SHA256, TLS_DH_anon_WITH_AES_256_GCM_SHA384, TLS_EMPTY_RENEGOTIATION_INFO_SCSV, TLS_ECDH_ECDSA_WITH_NULL_SHA, TLS_ECDH_ECDSA_WITH_RC4_128_SHA, TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA, TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA, TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA, 
        TLS_ECDHE_ECDSA_WITH_NULL_SHA, TLS_ECDHE_ECDSA_WITH_RC4_128_SHA, TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, TLS_ECDH_RSA_WITH_NULL_SHA, TLS_ECDH_RSA_WITH_RC4_128_SHA, TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA, TLS_ECDH_RSA_WITH_AES_128_CBC_SHA, TLS_ECDH_RSA_WITH_AES_256_CBC_SHA, 
        TLS_ECDHE_RSA_WITH_NULL_SHA, TLS_ECDHE_RSA_WITH_RC4_128_SHA, TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, TLS_ECDH_anon_WITH_NULL_SHA, TLS_ECDH_anon_WITH_RC4_128_SHA, TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA, TLS_ECDH_anon_WITH_AES_128_CBC_SHA, TLS_ECDH_anon_WITH_AES_256_CBC_SHA, 
        TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, 
        TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256, TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256, cipherSuite };
  }
  
  CipherSuite(String paramString1, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.javaName = paramString1;
  }
  
  public static CipherSuite forJavaName(String paramString) {
    CipherSuite cipherSuite;
    if (paramString.startsWith("SSL_")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("TLS_");
      stringBuilder.append(paramString.substring(4));
      cipherSuite = valueOf(stringBuilder.toString());
    } else {
      cipherSuite = valueOf((String)cipherSuite);
    } 
    return cipherSuite;
  }
  
  public String javaName() {
    return this.javaName;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\CipherSuite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */