package com.rdc.bms.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * http请求工具类, 封装了OkHttp3
 */

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static OkHttpUtil sOkHttpUtil;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private OkHttpUtil() {
        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new OkHttpCookieJar())
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(6, TimeUnit.SECONDS);
        mOkHttpClient = mOkHttpClientBuilder.build();
        mHandler = new Handler(Looper.getMainLooper());

    }

    public static OkHttpUtil getInstance() {
        if (sOkHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (sOkHttpUtil == null) {
                    sOkHttpUtil = new OkHttpUtil();
                }
            }
        }
        return sOkHttpUtil;
    }

    public OkHttpClient getOkHttpClient() {
        mOkHttpClient.newBuilder();
        return mOkHttpClient;
    }

    /**
     * 异步的Get请求
     */
    public void getAsync(String url, OkHttpResultCallback okHttpResultCallback) {
        Request request = new Request.Builder().url(url).build();
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步带请求头的get请求
     *
     * @param url
     * @param okHttpResultCallback
     * @param headers
     */
    public void getAsync(String url, OkHttpResultCallback okHttpResultCallback, Map<String, String> headers) {
        Request request = buildGetRequest(url, headers);
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 异步的post请求
     */
    public void postAsync(String url, OkHttpResultCallback okHttpResultCallback, Map<String, String> params, List<File> files) {
        Request request = buildPostRequest(url, params, files);
        deliveryResult(okHttpResultCallback, request);
    }


    /**
     * 异步的post请求,无文件传输
     */
    public void postAsync(String url, OkHttpResultCallback okHttpResultCallback, Map<String, String> params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(okHttpResultCallback, request);
    }

    /**
     * 构造get请求
     *
     * @param url
     * @param headers
     * @return
     */
    private Request buildGetRequest(String url, Map<String, String> headers) {

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Set<Map.Entry<String, String>> paramsEntries = headers.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                headersBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        return new Request.Builder()
                .url(url)
                .headers(headersBuilder.build())
                .build();
    }

    /**
     * 构建post请求参数
     */
    private Request buildPostRequest(String url, Map<String, String> params, List<File> files) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (files != null) {
            for (File file :
                    files) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipartBodyBuilder.addFormDataPart("file", file.getName(), requestBody);
            }
        }
        MultipartBody multipartBody = multipartBodyBuilder.setType(MultipartBody.FORM).build();


//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        if (params != null) {
//            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
//            for (Map.Entry<String, String> entry : paramsEntries) {
//                formBodyBuilder.add(entry.getKey(), entry.getValue());
//            }        
//        }
//        RequestBody requestBody = formBodyBuilder.build();
        return new Request.Builder()
                .url(url)
                //.post(requestBody)
                .post(multipartBody)
                .build();
    }

    /**
     * 构建post请求参数, 无文件传输
     */
    private Request buildPostRequest(String url, Map<String, String> params) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntries) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = formBodyBuilder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    /**
     * 调用call.enqueue，将call加入调度队列，执行完成后在callback中得到结果
     */
    private void deliveryResult(final OkHttpResultCallback okHttpResultCallback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendFailedCallback(call, e, okHttpResultCallback);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    switch (response.code()) {
                        case 200:
                            sendSuccessCallback(response.body().bytes(), okHttpResultCallback);
                            break;
                        case 302:
                            sendSuccessCallback(response.body().bytes(), okHttpResultCallback);
                            break;
                        default:
                            throw new IOException();
                    }
                } catch (IOException e) {
                    sendFailedCallback(call, e, okHttpResultCallback);
                }
            }
        });
    }


    /**
     * 调用请求失败对应的回调方法，利用handler.post使得回调方法在UI线程中执行
     */
    private void sendFailedCallback(final Call call, final Exception e, final OkHttpResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(call, e);
                }
            }
        });
    }

    /**
     * 调用请求成功对应的回调方法，利用handler.post使得回调方法在UI线程中执行
     */
    private void sendSuccessCallback(final byte[] bytes, final OkHttpResultCallback okHttpResultCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (okHttpResultCallback != null) {
                    okHttpResultCallback.onResponse(bytes);
                }
            }
        });
    }

    /**
     * 取消所有请求，防止进入新界面时，旧界面请求仍在进行，造成阻塞
     */
    public void cancelAllRequest() {
        mOkHttpClient.dispatcher().cancelAll();
    }

}