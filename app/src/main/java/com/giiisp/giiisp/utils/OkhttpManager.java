package com.giiisp.giiisp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Gao on 2017/10/12.
 * 网络请求管理HTTPS
 */

public class OkhttpManager {

    static private OkhttpManager mOkhttpManager = null;
    private List<InputStream> mTrustrCertificate;

    static public OkhttpManager getInstance() {
        if (mOkhttpManager == null) {
            mOkhttpManager = new OkhttpManager();
        }
        return mOkhttpManager;
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private X509TrustManager trustManagerForCertificates(List<InputStream> in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (InputStream s : in) {
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(s);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }
        }
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }


    public void setTrustrCertificates() {
        mTrustrCertificate = new ArrayList<>();
    }

    public OkHttpClient build(OkHttpClient.Builder builder) {
        OkHttpClient okHttpClient = null;
        final X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;

        TrustManager[] trustManager1 = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };

        try {
            trustManager = trustManagerForCertificates(mTrustrCertificate);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManager1, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        okHttpClient = builder
                .sslSocketFactory(sslSocketFactory, trustManager)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
//                .hostnameVerifier((hostname, session) -> {
//                    Log.d("OkhttpManager", "地址：" + hostname);// 2017/10/13 这里判断地址是否正确
//                    return BuildConfig.BASE_URL.contains(hostname)
//                            || BuildConfig.WX_BASE_URL.contains(hostname)
//                            || "https://api.weixin.qq.com".contains(hostname);
//                })
                .build();
        return okHttpClient;
    }
}
