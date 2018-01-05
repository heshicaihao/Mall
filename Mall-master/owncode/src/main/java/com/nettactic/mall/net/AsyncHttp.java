package com.nettactic.mall.net;

/**
 * Created by heshicaihao on 2017/2/17.
 */

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ${火龙裸先生} on 2016/9/28.
 * 邮箱：791335000@qq.com
 * <p/>
 * AsyncHttp OKhttp封装类
 */
public class AsyncHttp {
    private OkHttpClient client;
    private static AsyncHttp asyncHttp;
    private Handler mHandler;

    /**
     * 单例获取 AsyncHttp实例
     */
    private static AsyncHttp getInstance() {
        if (asyncHttp == null) {
            asyncHttp = new AsyncHttp();
        }
        return asyncHttp;
    }

    private AsyncHttp() {
        client = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    //******************  内部逻辑处理方法  ******************/

    private void p_getAsync(String url, final AsyncCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    private void p_postAsync(String url, Map<String, String> params, final AsyncCallBack callBack) {
        RequestBody requestBody = null;

        if (params == null) {
            params = new HashMap<String, String>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = null;
            if (entry.getValue() == null) {
                value = "";
            } else {
                value = entry.getValue().toString();
            }
            builder.add(key, value);
        }
        requestBody = builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    private void p_postAsync(String url,final AsyncCallBack callBack) {

        RequestBody requestBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        requestBody = builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    /**
     * 数据分发的方法
     */
    private void deliverDataFailure(final Call call, final IOException e, final AsyncCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(call, e);
                }
            }
        });
    }

    private void deliverDataSuccess(final String result, final AsyncCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }
        });
    }


    /******************
     * 对外公布的方法
     *****************/
    public static void getAsync(String url, AsyncCallBack callBack) {
        getInstance().p_getAsync(url, callBack);//异步GET请求
    }

    public static void postAsync(String url, Map<String, String> params, AsyncCallBack callBack) {
        getInstance().p_postAsync(url, params, callBack);//异步POST请求
    }

    public static void postAsync(String url, AsyncCallBack callBack) {
        getInstance().p_postAsync(url,callBack);//异步POST请求
    }


}
