package com.divino.antiaddiction;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AntiAddictionService extends Service {
	
	private Handler mPeriodicEventHandler;  
	private Context mContext = this;
    private final int PERIODIC_EVENT_TIMEOUT = 10000;  
    private Date mScreenOnTime = new Date();
    private Boolean mIsScreenOn = false;    
    private Boolean mIsAlertLater = false;
    
    
    
    private final BroadcastReceiver screenStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	
        	if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
	        	mScreenOnTime = new Date();
	        	mIsScreenOn = true;
        	}
        	else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        		mIsScreenOn = false;
        	}
        }
    };
    
    @Override  
    public void onCreate() {  
      super.onCreate();  
  
      //注册屏幕亮灭的
      registerScreenActionReceiver();
      
      mPeriodicEventHandler = new Handler();  
      mPeriodicEventHandler.postDelayed(doPeriodicTask, PERIODIC_EVENT_TIMEOUT);  
    }  
    
    private void registerScreenActionReceiver(){  
        final IntentFilter filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_SCREEN_OFF);  
        filter.addAction(Intent.ACTION_SCREEN_ON);  
        registerReceiver(screenStatusReceiver, filter);  
    } 
  
    private Runnable doPeriodicTask = new Runnable()  
    {  
        public void run()   
        {  
        	
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		
    		Date currentDate = new Date();
        	String currentTime = df.format(currentDate);
        	String screenOnTime = df.format(mScreenOnTime);
        	
        	if (mIsScreenOn) {
        	
           	
        		int min = (int) (((currentDate.getTime())-mScreenOnTime.getTime()) / 1000 / 60);
        		if (min > 1) {
        			
        			//弹出警告
        			Intent it  = new Intent(mContext, AlertActivity.class);
        			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       			 	startActivity(it);
       			 	

        			 
        			//已提示重置时间
       			 	mScreenOnTime = new Date();
        			
        			Toast.makeText(mContext, "手机防沉迷卫士：ScreenOnTime:" + screenOnTime + " CurrentTime:" + currentTime, Toast.LENGTH_LONG).show();
        			Log.i("手机防沉迷卫士", "手机防沉迷卫士：ScreenOnTime:" + screenOnTime + " CurrentTime:" + currentTime);
        		}
       		
        	}
        	
        	
        	//输出LOG


        	
			Toast.makeText(mContext, "轮询手机防沉迷卫士" + currentTime, Toast.LENGTH_LONG).show();
			Log.i("手机防沉迷卫士", "轮询手机防沉迷卫士" + currentTime);
			
			//等待下一次执行
			if (mIsAlertLater)
			{
				mPeriodicEventHandler.postDelayed(doPeriodicTask, 1000 * 60 * 5);		//5分钟后
				mIsAlertLater = false;
			}
			else
				mPeriodicEventHandler.postDelayed(doPeriodicTask, PERIODIC_EVENT_TIMEOUT);
        }  
    };  
  
    @Override  
    public void onDestroy() {  
  
    	unregisterReceiver(screenStatusReceiver);
    	
        mPeriodicEventHandler.removeCallbacks(doPeriodicTask);        
        super.onDestroy();  
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	} 
}
