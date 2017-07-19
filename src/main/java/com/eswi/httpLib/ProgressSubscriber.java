package com.eswi.httpLib;

import android.content.Context;

import com.example.eswilib.Utils.NetworkUtils;
import com.example.eswilib.view.Loading;
import com.example.eswilib.view.ProgressListener;

import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by admin on 2017/2/8.
 * 自定义Subscriber，在网络请求的时候弹出一个Dialog，实现在数据返回之后取消Subscriber的订阅  rxjava2.0以下用Subscriber 以上用ResourceSubscriber
 *ResourceSubscriber：允许异步取消其订阅相关资源，节省内存而且是线程安全。
 *DisposableSubscriber：通过实现Desposable异步删除。此实现调用dispose()是无效的;
 *
 */

 abstract class ProgressSubscriber<T> extends ResourceSubscriber<T> implements ProgressListener {

Loading dialogHandler;
    public ProgressSubscriber(Context context) {
        dialogHandler=new Loading(context,null, true);
        showDialog();
    }
    public void showDialog() {
        if (null != dialogHandler) {
            dialogHandler.show();
        }
    }
    public void disMissDialog() {
        if (null != dialogHandler) {
            dialogHandler.dismiss();
            dialogHandler.removeCallbacksAndMessages(null);
            dialogHandler = null;
        }
    }

    @Override
    public void onComplete() {
       disMissDialog();
    }



    @Override
    public void onError(Throwable e) {

        disMissDialog();
        if (!NetworkUtils.isNetworkAvailable()) {
            _onError("网络连接不可用,请检查网络!");
        } else {
            if(e!=null&&e.getMessage()!=null) {
                _onError(e.getMessage().toString());
            }else {
                _onError("请求失败，请稍后再试");
            }
        }

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void cancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }



    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
//
}
