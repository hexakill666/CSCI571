package com.hw9.test.untils;

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
