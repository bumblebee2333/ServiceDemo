package com.lxy.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private int count = 0;
    private IBinder myBinder;
    private boolean quit = false;

    class MyBinder extends Binder{
        MyService  getService(){
            return MyService.this;
        }
    }

    /**
     * 可以对一些数据进行初始化 只会调用一次
     */
    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        super.onCreate();
        myBinder = new MyBinder();
    }

    /**
     * @param intent 启动时组件传递过来的intent
     * @param flags 表示启动请求时是否有额外数据
     * @param startId 指明当前服务的唯一id 可能会有多个启动service的请求，通过startId来区分它们
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"startCommand()");
        new Thread(){
            @Override
            public void run() {
                while (!quit){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.quit = true;
        Log.d(TAG,"onDestory()");
        super.onDestroy();
    }

    /**
     * 绑定服务时才会调用
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind()");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG,"onRebind()");
        super.onRebind(intent);
    }

    public int getCount(){
        return count;
    }
}
