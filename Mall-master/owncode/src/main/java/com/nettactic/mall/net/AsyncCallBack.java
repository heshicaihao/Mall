package com.nettactic.mall.net;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by heshicaihao on 2017/2/17.
 */

public interface AsyncCallBack {

    public void onSuccess(String result);

    public void onFailure(Call call, IOException e);

}
