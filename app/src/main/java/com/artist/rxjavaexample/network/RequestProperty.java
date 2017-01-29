package com.artist.rxjavaexample.network;

/**
 * Created by heartbreaker on 2017/1/29.
 */
public class RequestProperty {

    private String key;
    private String value;

    public enum PROPERTY_KEY {
        Authorization("Authorization"),
        Content_Type("Content-Type");

        private String value;

        PROPERTY_KEY(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    private RequestProperty() {}

    public RequestProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RequestProperty{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
