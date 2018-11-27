package com.rdc.bms.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.rdc.bms.entity.User;
import com.rdc.bms.util.UserUtil;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        App.mContext = mContext;
    }

    public static User getUser() {
        return mUser != null ? mUser:UserUtil.getTemporaryUser();
    }

    public static void setUser(User mUser) {
        App.mUser = mUser;
        UserUtil.saveTemporaryUser(mUser);
    }
}
