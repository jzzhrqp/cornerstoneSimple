package com.aoshuotec.iron.cornerstone.netKit;

import com.blankj.utilcode.utils.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iron on 17-12-27.
 */

public class Http {

   static HttpService httpService= RetrofitHelper.getInstance().getRetrofit().create(HttpService.class);


    //无需控制的网络请求
    public static <T> void invoke(Observable<T> observable, Observer<T> callback) {

        if (!NetworkUtils.isConnected()) {
//            ToastUtil.showMsg("网络连接已断开");
            callback.onError(new Throwable("网络连接已断开"));
            return;
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }




}
