package com.aoshuotec.iron.cornerstone.netKit;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitHelper {
    private static RetrofitHelper instance     = null;
    private        Retrofit       mRetrofit    = null;
    private OkHttpClient mClient      = getClient(getOkHttpBuilder());
    private        Retrofit       mRetrofitNew = null;
    private  static String baseUrl="http://www.aoshuotec.com";

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        RetrofitHelper.baseUrl = baseUrl;
    }

    public static RetrofitHelper getInstance() {

        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }


    private RetrofitHelper() {

        init();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Retrofit getRetrofitNew() {
        return mRetrofitNew;
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        mRetrofit = createRetrofit(new Retrofit.Builder(), mClient);
//        mRetrofitNew = createRetrofit(new Retrofit.Builder(), mClient, com.pancool.pancoolpublibrary.baseui.Constant.URL2);
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private OkHttpClient getClient(OkHttpClient.Builder builder) {
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        try {
            // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
            final X509TrustManager trustAllCert =
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    };
            final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            builder.sslSocketFactory(sslSocketFactory, trustAllCert);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }

    private OkHttpClient.Builder getOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

}
