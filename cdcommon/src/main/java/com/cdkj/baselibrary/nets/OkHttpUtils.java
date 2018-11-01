package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Okhttp OkHttpClient 使用封装
 * Created by Administrator on 2016-09-05.
 */
public class OkHttpUtils {

    private final static int CONNECT_TIMEOUT = 45;//连接超时
    private final static int READ_TIMEOUT = 45;//数据返回超时
    private final static int WRITE_TIMEOUT = 45;//请求超时

    // This should be less than the lowest "normal" upload bandwidth times SOCKET_TIMEOUT_SECS,
    // but not too low or upload speed with long fat networks will suffer.
    // Because it also affects the TCP window size it should preferably be a power of two.
    private static final int SEND_WINDOW_SIZE_BYTES = (int) Math.pow(2, 16); // 64 KiB

    private static String CER_BKY = "-----BEGIN CERTIFICATE-----\n" +
            "MIIHgTCCBmmgAwIBAgIQChWkeoN6cFv/H2K22emQ4DANBgkqhkiG9w0BAQsFADB1\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
            "d3cuZGlnaWNlcnQuY29tMTQwMgYDVQQDEytEaWdpQ2VydCBTSEEyIEV4dGVuZGVk\n" +
            "IFZhbGlkYXRpb24gU2VydmVyIENBMB4XDTE3MTIyNTAwMDAwMFoXDTE5MTIyNTEy\n" +
            "MDAwMFowgZUxHTAbBgNVBA8MFFByaXZhdGUgT3JnYW5pemF0aW9uMRMwEQYLKwYB\n" +
            "BAGCNzwCAQMTAkhLMQ8wDQYDVQQFEwYyNTg5NjQxCzAJBgNVBAYTAkhLMRAwDgYD\n" +
            "VQQHEwdNT05HS09LMRgwFgYDVQQKEw9CRUlDT0lOIExJTUlURUQxFTATBgNVBAMT\n" +
            "DHd3dy5iY29pbi5pbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJfy\n" +
            "24/p7OcctSuCljeF+1yQbLRsnnjPJ2CGvYzsvYhN8e+gjLsXOAO7m/z/7v8xLKL6\n" +
            "L+M/QyfonWqCb8YbUCGtdHNVnpiuC+J0+cVnHZ6wWZ8CI4JfjgtUPkAv4LozWjc1\n" +
            "1b3E0jPg9lWja6KPna1d4quenApwOFLLUHpRvUjywlR6OyGsfweniZ3JpSiN2mpp\n" +
            "kcUcyY72BQj6XZEneX1iFjQVVbfyA5rJqpVJ19z0BGSLiOOo7j1aGeXkMx03FHst\n" +
            "IufZG5v/VfnXGuusPY89ZVigR+GOjpTfCDfIpTrcGGOaDpLJqvYNnJNeD5BtBXIQ\n" +
            "3YIrHG96lA+AQgg79okCAwEAAaOCA+owggPmMB8GA1UdIwQYMBaAFD3TUKXWoK3u\n" +
            "80pgCmXTIdT4+NYPMB0GA1UdDgQWBBQ+Zxrzp2pRkZTiV9B5JMeLEtdmojAhBgNV\n" +
            "HREEGjAYggx3d3cuYmNvaW4uaW2CCGJjb2luLmltMA4GA1UdDwEB/wQEAwIFoDAd\n" +
            "BgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwdQYDVR0fBG4wbDA0oDKgMIYu\n" +
            "aHR0cDovL2NybDMuZGlnaWNlcnQuY29tL3NoYTItZXYtc2VydmVyLWcyLmNybDA0\n" +
            "oDKgMIYuaHR0cDovL2NybDQuZGlnaWNlcnQuY29tL3NoYTItZXYtc2VydmVyLWcy\n" +
            "LmNybDBLBgNVHSAERDBCMDcGCWCGSAGG/WwCATAqMCgGCCsGAQUFBwIBFhxodHRw\n" +
            "czovL3d3dy5kaWdpY2VydC5jb20vQ1BTMAcGBWeBDAEBMIGIBggrBgEFBQcBAQR8\n" +
            "MHowJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2ljZXJ0LmNvbTBSBggrBgEF\n" +
            "BQcwAoZGaHR0cDovL2NhY2VydHMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0U0hBMkV4\n" +
            "dGVuZGVkVmFsaWRhdGlvblNlcnZlckNBLmNydDAJBgNVHRMEAjAAMIIB9gYKKwYB\n" +
            "BAHWeQIEAgSCAeYEggHiAeAAdQCkuQmQtBhYFIe7E6LMZ3AKPDWYBPkb37jjd80O\n" +
            "yA3cEAAAAWCLVaCcAAAEAwBGMEQCIGrt+tvFrO2OdgUHF5YAGweIY7zo+8bEgYLN\n" +
            "dJNhBYKpAiAfJHn/riyjKJBoWgmQj1ozaSgKL7wa+vhtQOFSb6NTpgB2AFYUBpov\n" +
            "18Ls0/XhvUSyPsdGdrm8mRFcwO+UmFXWidDdAAABYItVoW0AAAQDAEcwRQIgFpHG\n" +
            "dDYnDduZmFeXtfZ4BSU3iZac+PaDH18a+3edX/UCIQCiqVZD+mm7BeB3bGgCKf/k\n" +
            "3pCzBBJVwiBmkXj2ZnDMsAB3AO5Lvbd1zmC64UJpH6vhnmajD35fsHLYgwDEe4l6\n" +
            "qP3LAAABYItVo5YAAAQDAEgwRgIhAPArCUR/ddnmX1fKeDUIrpKbIVpHqO93KMJx\n" +
            "x3a8H4L8AiEA+Q4pUX1R4A+gEAtU1H8GLmvr2pS76MbnoU8XvCgDR7sAdgC72d+8\n" +
            "H4pxtZOUI5eqkntHOFeVCqtS6BqQlmQ2jh7RhQAAAWCLVaCLAAAEAwBHMEUCIFem\n" +
            "NKIf43W2qVuE7Thm7DOPkQnorVsnRoFOFUyPpXU/AiEAou5Bq4k7TTdNPMpTxwsM\n" +
            "RzuMkC8D5iNQvxtgCcFWpiwwDQYJKoZIhvcNAQELBQADggEBAJu3OJ3c2TvqTVIT\n" +
            "jVaDcwB83JsAIbDPmMUBgqWi52EFHWHGjbwAvp2q1WNqdTuEpsUiGzoVKbqhYARu\n" +
            "QmQYJx2vQrXYr9tStXDOy2H0KR/pbpKtE431gJx5p/zngvwT/bKRqWZCG5evTcdk\n" +
            "4d7fgqPYam67wjFUeBgia2kT1Dgdf+guwrhln5/tqK9qN59bzFZQ0Y1vrdz9C5lE\n" +
            "hkzzRLwYXdhvDsmR+iQjyr6zXBPHXJAnMYuU17fIrX8AGeBoGLwCU4UddNFTsFes\n" +
            "gosT0wk6hW232wNasvPUPTwWWOEgOlN231MHFQ/Fd9/KPqz9148EJyrbjYte+BlS\n" +
            "YNUTr/w=\n" +
            "-----END CERTIFICATE-----";

    public OkHttpUtils() {}

    private static OkHttpClient client;

    /**
     * 获取 OkHttpClient 对象
     * @return OkHttpClient
     */
    public static OkHttpClient getInstance() {
        if(client==null){


            client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//允许失败重试
                    .cookieJar(new CookiesManager())  //cookie 管理
                    .addInterceptor(getInterceptor(LogUtil.isLog))    //网络日志
                    .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
//                    .hostnameVerifier(new TrustAllHostnameVerifier())
                    .build();
//
        }

        return client;
    }


    /**
     * 网络请求拦截器
     *
     * @param isShow 控制请求日志的显示
     * @return interceptor
     */
    private static HttpLoggingInterceptor getInterceptor(boolean isShow) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    LogUtil.I("okhttp: " + URLDecoder.decode(message, "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.I("okhttp日志错误: ");
                }

            }
        });

        if (isShow) {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return interceptor;
    }

    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(setCertificates(new Buffer().writeUtf8(CER_BKY).inputStream()));

            sc.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    public static KeyStore setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
            return keyStore;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
