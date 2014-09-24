package com.divino.antiaddiction;


import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AlertDialog extends Activity {
	private String PUBLISHER_ID = "56OJwEL4uN8w3fs+1u";
	private String InlinePPID = "16TLuVClAp2cHNU066X9aU-s";
	Context mContext = this;
	int mDelaySecond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_alert);
        
        final Button mDialogButtonOk = (Button)findViewById(R.id.buttonOk);
        //mDialogButtonOk.setVisibility(View.INVISIBLE);
    	mDialogButtonOk.setEnabled(false);
		mDelaySecond = 15;
    	mDialogButtonOk.postDelayed(new Runnable(){
    		   @Override
    		   public void run() {
    			   //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
    			   
    			   mDialogButtonOk.setText("　" + mDelaySecond + "秒内禁止操作　");
    			   mDelaySecond--;
    			   
    			   if (mDelaySecond != 0)
    				   mDialogButtonOk.postDelayed(this, 1000);
    			   else {
    				   mDialogButtonOk.setText("　好的，谢谢提醒　");
    				   mDialogButtonOk.setEnabled(true);
    			   }
    				   	
    				   
    		   } 
    		}, 1000);
    	
		mDialogButtonOk.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			//mIsAlertLater = true;
    			//mIsAlertShow = false;
    			//dialog.dismiss();
    			finish();
    			
    		}
    	});
		
       
        RelativeLayout mDialogAdContainer = (RelativeLayout)findViewById(R.id.adcontainer);
		DomobAdView mDialogAdview = new DomobAdView(mContext, PUBLISHER_ID, InlinePPID);
		mDialogAdview.setAdEventListener(new DomobAdEventListener(){

			@Override
			public void onDomobAdClicked(DomobAdView arg0) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void onDomobAdOverlayDismissed(DomobAdView arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDomobAdOverlayPresented(DomobAdView arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Context onDomobAdRequiresCurrentContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onDomobAdReturned(DomobAdView arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDomobLeaveApplication(DomobAdView arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDomobAdFailed(DomobAdView arg0,
					cn.domob.android.ads.DomobAdManager.ErrorCode arg1) {
				// TODO Auto-generated method stub
				
			}

			
			
		});
		RelativeLayout.LayoutParams adLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mDialogAdview.setLayoutParams(adLayout);
		mDialogAdview.setAdSize(DomobAdView.INLINE_SIZE_600X500);
		mDialogAdContainer.addView(mDialogAdview);
    }


    @Override
    protected void onUserLeaveHint() {
            super.onUserLeaveHint();

            Intent intent = new Intent(this, AlertDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);

}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
          switch (keyCode) {
                case KeyEvent.KEYCODE_HOME:
                	return true;
                case KeyEvent.KEYCODE_BACK:
                	return true;
          }
     
          return super.onKeyDown(keyCode, event);
    }
}
