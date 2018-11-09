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

    //这个是 KKK环境的证书
//    private static  String CER_BKY="-----BEGIN CERTIFICATE-----\n" +
//            "MIIE4jCCBImgAwIBAgIQA4sfR0/lWgOixfdd6VTcOjAKBggqhkjOPQQDAjBvMQsw\n" +
//            "CQYDVQQGEwJVUzELMAkGA1UECBMCQ0ExFjAUBgNVBAcTDVNhbiBGcmFuY2lzY28x\n" +
//            "GTAXBgNVBAoTEENsb3VkRmxhcmUsIEluYy4xIDAeBgNVBAMTF0Nsb3VkRmxhcmUg\n" +
//            "SW5jIEVDQyBDQS0yMB4XDTE4MTAxMTAwMDAwMFoXDTE5MTAxMTEyMDAwMFowbTEL\n" +
//            "MAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1TYW4gRnJhbmNpc2Nv\n" +
//            "MRkwFwYDVQQKExBDbG91ZEZsYXJlLCBJbmMuMR4wHAYDVQQDExVzbmkuY2xvdWRm\n" +
//            "bGFyZXNzbC5jb20wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQ2Rrgg9NW75Bpd\n" +
//            "06VjBgG6VblrywmtPfOueI7sTzFdCboE0mPcZ4Q65mu6RSBgO0JkY+AE8va+a0Ao\n" +
//            "sA+v4a50o4IDBzCCAwMwHwYDVR0jBBgwFoAUPnQtH89FdQR+P8Cihz5MQ4NRE8Yw\n" +
//            "HQYDVR0OBBYEFFkjNF9+cIy+FhpQzMYemKz+h6KkMDoGA1UdEQQzMDGCDCoua2tr\n" +
//            "b3RjLmNvbYIKa2trb3RjLmNvbYIVc25pLmNsb3VkZmxhcmVzc2wuY29tMA4GA1Ud\n" +
//            "DwEB/wQEAwIHgDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIweQYDVR0f\n" +
//            "BHIwcDA2oDSgMoYwaHR0cDovL2NybDMuZGlnaWNlcnQuY29tL0Nsb3VkRmxhcmVJ\n" +
//            "bmNFQ0NDQTIuY3JsMDagNKAyhjBodHRwOi8vY3JsNC5kaWdpY2VydC5jb20vQ2xv\n" +
//            "dWRGbGFyZUluY0VDQ0NBMi5jcmwwTAYDVR0gBEUwQzA3BglghkgBhv1sAQEwKjAo\n" +
//            "BggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAIBgZngQwB\n" +
//            "AgIwdgYIKwYBBQUHAQEEajBoMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdp\n" +
//            "Y2VydC5jb20wQAYIKwYBBQUHMAKGNGh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNv\n" +
//            "bS9DbG91ZEZsYXJlSW5jRUNDQ0EtMi5jcnQwDAYDVR0TAQH/BAIwADCCAQUGCisG\n" +
//            "AQQB1nkCBAIEgfYEgfMA8QB2ALvZ37wfinG1k5Qjl6qSe0c4V5UKq1LoGpCWZDaO\n" +
//            "HtGFAAABZmKuzhkAAAQDAEcwRQIhAIPqHEoDbFYH+w+EJ9xMWdVoUrVoR/O03OkC\n" +
//            "zvCoUnzRAiAGjF2FzyN0Lz/26OCf8ffCe1YR0d3SHrpBfxZO++5qJwB3AHR+2oMx\n" +
//            "rTMQkSGcziVPQnDCv/1eQiAIxjc1eeYQe8xWAAABZmKuzjUAAAQDAEgwRgIhAPjb\n" +
//            "xFvO4XORkw2PHddzqH0v3Qqq+jaUl9INoPX4tpSjAiEAjy4kz/YCbc43uCPw0dhk\n" +
//            "+GGpYx2IRQR+2BXJFtI6lNswCgYIKoZIzj0EAwIDRwAwRAIga1kbZMVmImSEBnw1\n" +
//            "5aHb9DpuQh8XeUtW9IRjJHKAmrACIEUWXQwUkuNEGeNCqql5lPKL2uD182558MJD\n" +
//            "Y6s2dyek\n" +
//            "-----END CERTIFICATE-----";
    //这个是love环境的
    private static String CER_BKY = "-----BEGIN CERTIFICATE-----\n" +
            "MIIE5DCCBImgAwIBAgIQDuj3xuEcL3w/jCoBnp1PyzAKBggqhkjOPQQDAjBvMQsw\n" +
            "CQYDVQQGEwJVUzELMAkGA1UECBMCQ0ExFjAUBgNVBAcTDVNhbiBGcmFuY2lzY28x\n" +
            "GTAXBgNVBAoTEENsb3VkRmxhcmUsIEluYy4xIDAeBgNVBAMTF0Nsb3VkRmxhcmUg\n" +
            "SW5jIEVDQyBDQS0yMB4XDTE4MDkyODAwMDAwMFoXDTE5MDkyODEyMDAwMFowbTEL\n" +
            "MAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1TYW4gRnJhbmNpc2Nv\n" +
            "MRkwFwYDVQQKExBDbG91ZEZsYXJlLCBJbmMuMR4wHAYDVQQDExVzbmkuY2xvdWRm\n" +
            "bGFyZXNzbC5jb20wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAASXHlUg/o0Km95l\n" +
            "BumxrEfTDpuXWSNH+K3cfaBUuP4yoGNAKpzH67y3wTp3R8Rgv1eSCjw8oua7s8Uu\n" +
            "0ElqWdTdo4IDBzCCAwMwHwYDVR0jBBgwFoAUPnQtH89FdQR+P8Cihz5MQ4NRE8Yw\n" +
            "HQYDVR0OBBYEFKxYRS7zyH8k4jiYCbxECz7g8F5NMDwGA1UdEQQ1MDOCFXNuaS5j\n" +
            "bG91ZGZsYXJlc3NsLmNvbYILbG92ZW90Yy5jb22CDSoubG92ZW90Yy5jb20wDgYD\n" +
            "VR0PAQH/BAQDAgeAMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjB5BgNV\n" +
            "HR8EcjBwMDagNKAyhjBodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vQ2xvdWRGbGFy\n" +
            "ZUluY0VDQ0NBMi5jcmwwNqA0oDKGMGh0dHA6Ly9jcmw0LmRpZ2ljZXJ0LmNvbS9D\n" +
            "bG91ZEZsYXJlSW5jRUNDQ0EyLmNybDBMBgNVHSAERTBDMDcGCWCGSAGG/WwBATAq\n" +
            "MCgGCCsGAQUFBwIBFhxodHRwczovL3d3dy5kaWdpY2VydC5jb20vQ1BTMAgGBmeB\n" +
            "DAECAjB2BggrBgEFBQcBAQRqMGgwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRp\n" +
            "Z2ljZXJ0LmNvbTBABggrBgEFBQcwAoY0aHR0cDovL2NhY2VydHMuZGlnaWNlcnQu\n" +
            "Y29tL0Nsb3VkRmxhcmVJbmNFQ0NDQS0yLmNydDAMBgNVHRMBAf8EAjAAMIIBAwYK\n" +
            "KwYBBAHWeQIEAgSB9ASB8QDvAHYAu9nfvB+KcbWTlCOXqpJ7RzhXlQqrUugakJZk\n" +
            "No4e0YUAAAFmH3lJvgAABAMARzBFAiA34nN1G278lC8vX9gBjbsvlPtkN+1DyuW5\n" +
            "A6SnovbFPAIhAI8d7GCDf/FF2lkN59uGSsR/pi1JnnS2vMfXNStufl9dAHUAdH7a\n" +
            "gzGtMxCRIZzOJU9CcMK//V5CIAjGNzV55hB7zFYAAAFmH3lJngAABAMARjBEAiAg\n" +
            "y6VKrSaW8ty9f95E2NRtfqURleY/bhfHv8QtMAvllQIgNxae1/T9McY35wOYTsqP\n" +
            "y6WuoITPTo3eVO4odQKBw50wCgYIKoZIzj0EAwIDSQAwRgIhAJyORV0CkYG2JZa/\n" +
            "GpD/NhfTLbAFJSAvEsknNvdf9et2AiEAzACmZ6r269gw5VuuX+C3xFekAnoWVoKE\n" +
            "A96yOuLVP/M=\n" +
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
