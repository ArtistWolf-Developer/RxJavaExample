package com.artist.rxjavaexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.artist.rxjavaexample.network.RequestProperty;
import com.artist.rxjavaexample.network.RxJavaConnect;

import java.util.HashMap;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Artist";

    private static final String RC_DOMAIN = "https://t-if3.insnergy.com/if";
    private static final String CHECK_ACTIVATION_URL = RC_DOMAIN + "/3/common/check_activation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Subscriber Item: " + s);
                TextView msg = (TextView) findViewById(R.id.msg);
                msg.setText(s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Subscriber Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Subscriber Error!");
            }

            @Override
            public void onStart() {
                Log.d(TAG, "Subscriber Start!");
            }
        };
        HashMap<RequestProperty.PROPERTY_KEY, RequestProperty> requestPropertyHashMap = new HashMap<RequestProperty.PROPERTY_KEY, RequestProperty>();
        requestPropertyHashMap
                .put(RequestProperty.PROPERTY_KEY.Authorization, new RequestProperty(RequestProperty.PROPERTY_KEY.Authorization.getValue(), "Basic ZGRhMjFiMWUtMjhkYy00YmY4LWIyNTQtMzc4MzUxZDFiYTRhOmZmNDQ5MmRlLWNhYWMtNGNmZi1hODdiLTY1Mjg4Njc5ODU5Nw=="));
        requestPropertyHashMap
                .put(RequestProperty.PROPERTY_KEY.Content_Type, new RequestProperty(RequestProperty.PROPERTY_KEY.Content_Type.getValue(), "application/x-www-form-urlencoded"));
        new RxJavaConnect(CHECK_ACTIVATION_URL, 30, RxJavaConnect.HTTP_REQUEST_METHOD.GET, requestPropertyHashMap).connect(subscriber);
    }
}
