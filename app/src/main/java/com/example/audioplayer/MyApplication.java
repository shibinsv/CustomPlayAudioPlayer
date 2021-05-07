package com.example.audioplayer;


import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mApplicaationInstance;

    public static MyApplication getmApplicaationInstance() {
        return mApplicaationInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplicaationInstance=this;
    }
}
