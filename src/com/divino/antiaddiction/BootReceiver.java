package com.divino.antiaddiction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
    //private PendingIntent mAlarmSender;
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
	    	//启动服务
    		
	    	Intent serviceIntent = new Intent(context, AntiAddictionService.class);          
	        context.startService(serviceIntent);
	        
			Toast.makeText(context, "手机防沉迷卫士已启动", Toast.LENGTH_SHORT).show();
	        /*
	        Intent activityIntent = new Intent(context, MessageActivity.class);  
	        //  要想在Service中启动Activity，必须设置如下标志  
	        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        context.startActivity(activityIntent);
	        */ 
    	}
    }
}