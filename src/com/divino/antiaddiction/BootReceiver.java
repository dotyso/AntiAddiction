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
	    	//��������
    		
	    	Intent serviceIntent = new Intent(context, AntiAddictionService.class);          
	        context.startService(serviceIntent);
	        
			Toast.makeText(context, "�ֻ���������ʿ������", Toast.LENGTH_SHORT).show();
	        /*
	        Intent activityIntent = new Intent(context, MessageActivity.class);  
	        //  Ҫ����Service������Activity�������������±�־  
	        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        context.startActivity(activityIntent);
	        */ 
    	}
    }
}