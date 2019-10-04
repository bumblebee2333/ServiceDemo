package com.lxy.servicedemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start;
    private Button stop;
    private ServiceConnection mServiceConnection;
    private MyService myService;
    private Button bind;
    private Button unbind;
    private Button data;

    private static final String TAG = "MainAcitvity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        bind = findViewById(R.id.bind);
        unbind = findViewById(R.id.unbind);
        data = findViewById(R.id.data);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        bind.setOnClickListener(this);
        unbind.setOnClickListener(this);
        data.setOnClickListener(this);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG,"绑定成功后调用onServiceConnected()");
                MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG,"onServiceDisconnected()");
                myService = null;
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MyService.class);
        int id = v.getId();
        switch (id){
            case R.id.start:
                startService(intent);
                Log.d(TAG,"startService()");
                break;
            case R.id.stop:
                stopService(intent);
                break;
            case R.id.bind:
                //BIND_AUTO_CREATE 表示在活动和服务进行绑定后自动启动服务
                bindService(intent,mServiceConnection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                if(myService != null){
                    myService = null;
                    unbindService(mServiceConnection);
                }
                break;
            case R.id.data:
                if(myService != null){
                    Log.d(TAG,"从服务端获取数据："+ myService.getCount());
                }else {
                    Log.d(TAG,"还没绑定呢，不绑定，无法从服务端获取数据");
                }
                break;
        }
    }
}
