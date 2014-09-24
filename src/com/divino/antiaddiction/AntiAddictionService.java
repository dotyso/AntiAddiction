package com.divino.antiaddiction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobAdManager.ErrorCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class AntiAddictionService extends Service {
	
	private Handler mPeriodicEventHandler;  
	private Context mContext = this;
    private final int PERIODIC_EVENT_TIMEOUT = 1000 * 60;  
    private Date mScreenOnTime = new Date();
    private Boolean mIsScreenOn = false;    
    private Boolean mIsAlertShow = false;
    
	private String PUBLISHER_ID = "56OJwEL4uN8w3fs+1u";
	private String InlinePPID = "16TLuVClAp2cHNU066X9aU-s";

    //������Ļ״̬
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
  
      //ע����Ļ����Ľ���
      registerScreenActionReceiver();
      
      mScreenOnTime = new Date();
      mIsScreenOn = true;
      
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
        	
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    		
    		Date currentDate = new Date();
        	String currentTime = df.format(currentDate);
        	String screenOnTime = df.format(mScreenOnTime);
        	
        	if (mIsScreenOn && !mIsAlertShow) {
        	
        		PeriodSetting hisPeriodSetting = PeriodSettingManager.getInstance().checkHit(currentDate);
        		if (hisPeriodSetting != null) {
	        		int min = (int) (((currentDate.getTime())-mScreenOnTime.getTime()) / 1000 / 60);
	        		if (min >= hisPeriodSetting.getExceedMin()) {
	        			
	        			popupDialog(hisPeriodSetting.getAlertType());		        			//��������
	        			
	       			 	mScreenOnTime = new Date();				//����ʾ������ʱ��
	        			
	        			Log.i("�ֻ���������ʿ", "�����ֻ���������ʿ��ScreenOnTime:" + screenOnTime + " CurrentTime:" + currentTime);
	        		}
        		}
        	}
        	
        	
        	//���LOG
			Toast.makeText(mContext, "��ѯ�ֻ���������ʿ" + currentTime, Toast.LENGTH_LONG).show();
			Log.i("�ֻ���������ʿ", "��ѯ�ֻ���������ʿ" + currentTime);
			
			//�ȴ���һ��ִ��
			mPeriodicEventHandler.postDelayed(doPeriodicTask, PERIODIC_EVENT_TIMEOUT);
        }


    };  
  
    Integer mDelaySecond;
    private void popupDialog(int alertType) {

    	/*
    	Intent intent = new Intent(mContext, com.divino.antiaddiction.AlertDialog.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    	*/
    	
    	final Context mContext = getApplicationContext();
 
    	LayoutInflater inflater = LayoutInflater.from(mContext);
    	View layout = inflater.inflate(R.layout.activity_alert, null);
    	    
    	AlertDialog.Builder builder =new AlertDialog.Builder(mContext);
    	builder.setView(layout);
    	builder.setTitle("�ֻ���������ʿ");
    	builder.setCancelable(false);
    	final AlertDialog dialog = builder.create();

    	final Button mDialogButtonOk = (Button)layout.findViewById(R.id.buttonOk);
    	mDialogButtonOk.setEnabled(false);
    	
    	if (alertType == PeriodSetting.ALERT_TYPE_DELAY_1_MIN)
    		mDelaySecond = 60;
    	else if (alertType == PeriodSetting.ALERT_TYPE_DELAY_10_MIN)
    		mDelaySecond = 600;
    		
		
    	mDialogButtonOk.postDelayed(new Runnable(){
    		   @Override
    		   public void run() {
    			   //Ҫ�������飬�����ٴε��ô�Runnable������ʵ��ÿ����ʵ��һ�εĶ�ʱ������
    			   
    			   mDialogButtonOk.setText("��" + mDelaySecond + "���ڽ�ֹ������");
    			   mDelaySecond--;
    			   
    			   if (mDelaySecond != 0)
    				   mDialogButtonOk.postDelayed(this, 1000);
    			   else {
    				   mDialogButtonOk.setText("���õģ�лл���ѡ�");
    				   mDialogButtonOk.setEnabled(true);
    			   }
    				   	
    				   
    		   } 
    		}, 1000);
    	
		mDialogButtonOk.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			mIsAlertShow = false;
    			dialog.dismiss();
    		}
    	});
		
		
		//���˹��

		RelativeLayout mDialogAdContainer = (RelativeLayout)layout.findViewById(R.id.adcontainer);
		DomobAdView mDialogAdview = new DomobAdView(mContext, PUBLISHER_ID, InlinePPID);
		mDialogAdview.setAdEventListener(new DomobAdEventListener(){

			@Override
			public void onDomobAdClicked(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdClicked", Toast.LENGTH_LONG);
			}

			@Override
			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdFailed", Toast.LENGTH_LONG);			
			}

			@Override
			public void onDomobAdOverlayDismissed(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdOverlayDismissed", Toast.LENGTH_LONG);	
			}

			@Override
			public void onDomobAdOverlayPresented(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdOverlayPresented", Toast.LENGTH_LONG);
			}

			@Override
			public Context onDomobAdRequiresCurrentContext() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdRequiresCurrentContext", Toast.LENGTH_LONG);
				return null;
			}

			@Override
			public void onDomobAdReturned(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobAdReturned", Toast.LENGTH_LONG);
			}

			@Override
			public void onDomobLeaveApplication(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "onDomobLeaveApplication", Toast.LENGTH_LONG);
			}

			
			
		});
		RelativeLayout.LayoutParams adLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mDialogAdview.setLayoutParams(adLayout);
		mDialogAdview.setAdSize(DomobAdView.INLINE_SIZE_FLEXIBLE);
		mDialogAdContainer.addView(mDialogAdview);
		
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    	dialog.show();
    	
    	mIsAlertShow = true;
    }
    
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
