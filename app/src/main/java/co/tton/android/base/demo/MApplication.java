package co.tton.android.base.demo;

import android.app.Application;

import co.tton.android.lib.qrcodescanner.activity.ZXingLibrary;

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
}
