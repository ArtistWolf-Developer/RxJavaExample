package com.artist.rxjavaexample.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by heartbreaker on 2017/1/28.
 */
public class RxJavaConnect {

    private String domain;
    private int timeout;
    private HTTP_REQUEST_METHOD httpRequestMethod;
    private HashMap<RequestProperty.PROPERTY_KEY, RequestProperty> requestPropertyHashMap;
    private HashMap<String, String> parameterHashMap;

    public enum HTTP_REQUEST_METHOD {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private String value;

        HTTP_REQUEST_METHOD(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private RxJavaConnect() {
    }

    public RxJavaConnect(String domain) {
        this.domain = domain;
        this.timeout = 30;
        this.httpRequestMethod = HTTP_REQUEST_METHOD.GET;
        this.requestPropertyHashMap = new HashMap<RequestProperty.PROPERTY_KEY, RequestProperty>();
        this.parameterHashMap = new HashMap<String, String>();
    }

    public RxJavaConnect(String domain, int timeout) {
        this(domain);
        this.timeout = timeout;
    }

    public RxJavaConnect(String domain, int timeout, HTTP_REQUEST_METHOD httpRequestMethod) {
        this(domain, timeout);
        this.httpRequestMethod = httpRequestMethod;
    }

    public RxJavaConnect(String domain, int timeout, HTTP_REQUEST_METHOD httpRequestMethod, HashMap<RequestProperty.PROPERTY_KEY, RequestProperty> requestPropertyHashMap) {
        this(domain, timeout, httpRequestMethod);
        this.requestPropertyHashMap = requestPropertyHashMap;
    }

    public RxJavaConnect(String domain, int timeout, HTTP_REQUEST_METHOD httpRequestMethod, HashMap<RequestProperty.PROPERTY_KEY, RequestProperty> requestPropertyHashMap, HashMap<String, String> parameterHashMap) {
        this(domain, timeout, httpRequestMethod, requestPropertyHashMap);
        this.parameterHashMap = parameterHashMap;
    }

    public void connect(Observer observer) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(domain + getParameterFormat()).openConnection();
                    for (Map.Entry<RequestProperty.PROPERTY_KEY, RequestProperty> entry : requestPropertyHashMap.entrySet()) {
                        RequestProperty requestProperty = requestPropertyHashMap.get(entry.getKey());
                        httpsURLConnection.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
                    }
                    httpsURLConnection.setRequestMethod(httpRequestMethod.getValue());
                    httpsURLConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String msg;
                    while ((msg = bufferedReader.readLine()) != null) {
                        stringBuilder.append(msg + "\n");
                    }
                    subscriber.onNext(stringBuilder.toString());
                    subscriber.onCompleted();
                    httpsURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).timeout(timeout, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private String getParameterFormat() {
        if (parameterHashMap.isEmpty()) {
            return "";
        }

        StringBuilder parameterURL = new StringBuilder("?");
        for (Map.Entry<String, String> entry : parameterHashMap.entrySet()) {
            String key = entry.getKey();
            String value = parameterHashMap.get(key);
            parameterURL.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
        }
        return parameterURL.toString().substring(0, parameterURL.toString().length() - 1);
    }
}
