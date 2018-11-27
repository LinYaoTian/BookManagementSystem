package com.rdc.bms.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class OkHttpCookieJar implements CookieJar {
    private static List<Cookie> sCookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        sCookies = cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (null != sCookies){
            return sCookies;
        }else {
            return new ArrayList<>();
        }
    }

    public static void printCookie(){
        if (sCookies != null) {
            for (int i = 0; i < sCookies.size(); i++) {
                Log.e("cc", sCookies.get(i).value());
            }
        }
    }
    public static boolean isCookiesNull(){
        if (sCookies == null) {
            return true;
        }
        return false;
    }
    public static void resetCookies(){
        sCookies = null;
    }

    public static List<Cookie> getCookies(){
        return sCookies;
    }
}
