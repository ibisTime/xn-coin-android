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

//    这个是 KKK环境的证书
    private static  String CER_BKY="-----BEGIN CERTIFICATE-----\n" +
        "MIIHOTCCBt6gAwIBAgIQAxn/ej+YD3aGRAmUX33HXDAKBggqhkjOPQQDAjCBkjEL\n" +
        "MAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UE\n" +
        "BxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxODA2BgNVBAMT\n" +
        "L0NPTU9ETyBFQ0MgRG9tYWluIFZhbGlkYXRpb24gU2VjdXJlIFNlcnZlciBDQSAy\n" +
        "MB4XDTE4MTExMDAwMDAwMFoXDTE5MDUxOTIzNTk1OVowbDEhMB8GA1UECxMYRG9t\n" +
        "YWluIENvbnRyb2wgVmFsaWRhdGVkMSEwHwYDVQQLExhQb3NpdGl2ZVNTTCBNdWx0\n" +
        "aS1Eb21haW4xJDAiBgNVBAMTG3NzbDM3MjMyOS5jbG91ZGZsYXJlc3NsLmNvbTBZ\n" +
        "MBMGByqGSM49AgEGCCqGSM49AwEHA0IABEDLr/sSo1FX52750FRPeXoqh0+tyjbu\n" +
        "z+MaTERqocqO6tJtk+Cc5+w0lWRFjB0RJ4kzI4HxjXVE9kdtFoop45OjggU5MIIF\n" +
        "NTAfBgNVHSMEGDAWgBRACWFn8LyDcU/eEggsb9TUK3Y9ljAdBgNVHQ4EFgQUUBEF\n" +
        "0gYZBknWlEhUfNbZ3klhQrMwDgYDVR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAw\n" +
        "HQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCME8GA1UdIARIMEYwOgYLKwYB\n" +
        "BAGyMQECAgcwKzApBggrBgEFBQcCARYdaHR0cHM6Ly9zZWN1cmUuY29tb2RvLmNv\n" +
        "bS9DUFMwCAYGZ4EMAQIBMFYGA1UdHwRPME0wS6BJoEeGRWh0dHA6Ly9jcmwuY29t\n" +
        "b2RvY2E0LmNvbS9DT01PRE9FQ0NEb21haW5WYWxpZGF0aW9uU2VjdXJlU2VydmVy\n" +
        "Q0EyLmNybDCBiAYIKwYBBQUHAQEEfDB6MFEGCCsGAQUFBzAChkVodHRwOi8vY3J0\n" +
        "LmNvbW9kb2NhNC5jb20vQ09NT0RPRUNDRG9tYWluVmFsaWRhdGlvblNlY3VyZVNl\n" +
        "cnZlckNBMi5jcnQwJQYIKwYBBQUHMAGGGWh0dHA6Ly9vY3NwLmNvbW9kb2NhNC5j\n" +
        "b20wggJ4BgNVHREEggJvMIICa4Ibc3NsMzcyMzI5LmNsb3VkZmxhcmVzc2wuY29t\n" +
        "ghsqLmFmcmljYW5zYWxlc2NvbXBhbnkuY28uemGCFCouYmljeWNsZXBvd2VyLmNv\n" +
        "LnphghQqLmNyb3NzZml0am96aS5jby56YYIRKi5mbGlja2VybGVhcC5jb22CDCou\n" +
        "Zmx5bGluay5pb4IOKi5nb2xlYXAuY28uemGCFiouaXNhYmVsbGFnYXJjaWEuY28u\n" +
        "emGCFCouamlmZnlzdGVhbWVyLmNvLnphggwqLmtra290Yy5jb22CESoubGl0aWdh\n" +
        "dG9yLmNvLnphggwqLm1heGltdXMuZGWCESoubW90aGVydGh5bWUuY29tghIqLnJl\n" +
        "YmVsc3RvcmUuY28uemGCFCoucmllc3MtYW1iaWVudGUubmV0gg8qLnJpdmVycy5j\n" +
        "aHVyY2iCECouc2ZvbnRoZWJheS5jb22CGWFmcmljYW5zYWxlc2NvbXBhbnkuY28u\n" +
        "emGCEmJpY3ljbGVwb3dlci5jby56YYISY3Jvc3NmaXRqb3ppLmNvLnphgg9mbGlj\n" +
        "a2VybGVhcC5jb22CCmZseWxpbmsuaW+CDGdvbGVhcC5jby56YYIUaXNhYmVsbGFn\n" +
        "YXJjaWEuY28uemGCEmppZmZ5c3RlYW1lci5jby56YYIKa2trb3RjLmNvbYIPbGl0\n" +
        "aWdhdG9yLmNvLnphggptYXhpbXVzLmRlgg9tb3RoZXJ0aHltZS5jb22CEHJlYmVs\n" +
        "c3RvcmUuY28uemGCEnJpZXNzLWFtYmllbnRlLm5ldIINcml2ZXJzLmNodXJjaIIO\n" +
        "c2ZvbnRoZWJheS5jb20wggEEBgorBgEEAdZ5AgQCBIH1BIHyAPAAdgDuS723dc5g\n" +
        "uuFCaR+r4Z5mow9+X7By2IMAxHuJeqj9ywAAAWb9e/w4AAAEAwBHMEUCIC2g3Y+L\n" +
        "VSq91fq2TffXjDR63A+vOd3cf3OQA4hTpjl5AiEAuDJqkYqJk+mK0NOGTFg96Lhx\n" +
        "swT3ZKDzQm0V5XkUYzoAdgB0ftqDMa0zEJEhnM4lT0Jwwr/9XkIgCMY3NXnmEHvM\n" +
        "VgAAAWb9e/x3AAAEAwBHMEUCIAS/kDxnwVHJg2i8Is71IVcHK+zoa4WgCNygaITx\n" +
        "UIP1AiEA5lazvzsZ4i8AgHx3xkRD3y20hi8zQI1u/kcXou1msn8wCgYIKoZIzj0E\n" +
        "AwIDSQAwRgIhAKanhWOX5aeHeVBo7AOz+utQX4Jjh3PNSVhzGKluYHzoAiEA3tDe\n" +
        "TXHK8Ky6qLc38oQWLrU2ZEBs1NFY8MIzvjDWRvw=\n" +
        "-----END CERTIFICATE-----";
    //这个是love环境的
//    private static String CER_BKY = "-----BEGIN CERTIFICATE-----\n" +
//            "MIIE5DCCBImgAwIBAgIQDuj3xuEcL3w/jCoBnp1PyzAKBggqhkjOPQQDAjBvMQsw\n" +
//            "CQYDVQQGEwJVUzELMAkGA1UECBMCQ0ExFjAUBgNVBAcTDVNhbiBGcmFuY2lzY28x\n" +
//            "GTAXBgNVBAoTEENsb3VkRmxhcmUsIEluYy4xIDAeBgNVBAMTF0Nsb3VkRmxhcmUg\n" +
//            "SW5jIEVDQyBDQS0yMB4XDTE4MDkyODAwMDAwMFoXDTE5MDkyODEyMDAwMFowbTEL\n" +
//            "MAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRYwFAYDVQQHEw1TYW4gRnJhbmNpc2Nv\n" +
//            "MRkwFwYDVQQKExBDbG91ZEZsYXJlLCBJbmMuMR4wHAYDVQQDExVzbmkuY2xvdWRm\n" +
//            "bGFyZXNzbC5jb20wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAASXHlUg/o0Km95l\n" +
//            "BumxrEfTDpuXWSNH+K3cfaBUuP4yoGNAKpzH67y3wTp3R8Rgv1eSCjw8oua7s8Uu\n" +
//            "0ElqWdTdo4IDBzCCAwMwHwYDVR0jBBgwFoAUPnQtH89FdQR+P8Cihz5MQ4NRE8Yw\n" +
//            "HQYDVR0OBBYEFKxYRS7zyH8k4jiYCbxECz7g8F5NMDwGA1UdEQQ1MDOCFXNuaS5j\n" +
//            "bG91ZGZsYXJlc3NsLmNvbYILbG92ZW90Yy5jb22CDSoubG92ZW90Yy5jb20wDgYD\n" +
//            "VR0PAQH/BAQDAgeAMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjB5BgNV\n" +
//            "HR8EcjBwMDagNKAyhjBodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vQ2xvdWRGbGFy\n" +
//            "ZUluY0VDQ0NBMi5jcmwwNqA0oDKGMGh0dHA6Ly9jcmw0LmRpZ2ljZXJ0LmNvbS9D\n" +
//            "bG91ZEZsYXJlSW5jRUNDQ0EyLmNybDBMBgNVHSAERTBDMDcGCWCGSAGG/WwBATAq\n" +
//            "MCgGCCsGAQUFBwIBFhxodHRwczovL3d3dy5kaWdpY2VydC5jb20vQ1BTMAgGBmeB\n" +
//            "DAECAjB2BggrBgEFBQcBAQRqMGgwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRp\n" +
//            "Z2ljZXJ0LmNvbTBABggrBgEFBQcwAoY0aHR0cDovL2NhY2VydHMuZGlnaWNlcnQu\n" +
//            "Y29tL0Nsb3VkRmxhcmVJbmNFQ0NDQS0yLmNydDAMBgNVHRMBAf8EAjAAMIIBAwYK\n" +
//            "KwYBBAHWeQIEAgSB9ASB8QDvAHYAu9nfvB+KcbWTlCOXqpJ7RzhXlQqrUugakJZk\n" +
//            "No4e0YUAAAFmH3lJvgAABAMARzBFAiA34nN1G278lC8vX9gBjbsvlPtkN+1DyuW5\n" +
//            "A6SnovbFPAIhAI8d7GCDf/FF2lkN59uGSsR/pi1JnnS2vMfXNStufl9dAHUAdH7a\n" +
//            "gzGtMxCRIZzOJU9CcMK//V5CIAjGNzV55hB7zFYAAAFmH3lJngAABAMARjBEAiAg\n" +
//            "y6VKrSaW8ty9f95E2NRtfqURleY/bhfHv8QtMAvllQIgNxae1/T9McY35wOYTsqP\n" +
//            "y6WuoITPTo3eVO4odQKBw50wCgYIKoZIzj0EAwIDSQAwRgIhAJyORV0CkYG2JZa/\n" +
//            "GpD/NhfTLbAFJSAvEsknNvdf9et2AiEAzACmZ6r269gw5VuuX+C3xFekAnoWVoKE\n" +
//            "A96yOuLVP/M=\n" +
//            "-----END CERTIFICATE-----";


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
