package com.divino.antiaddiction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlertActivity extends Activity {

	private Boolean mIsAlertLater = false;
    	
    	private Button mButtonOk;
    	private Button mButtonLater;
    	
    	
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_alert);
    		
    		mButtonOk = (Button)this.findViewById(R.id.buttonOk);
    		mButtonLater = (Button)this.findViewById(R.id.buttonLater);
    		
    		mButtonOk.setOnClickListener(mButtonOkOnClick);
    		mButtonLater.setOnClickListener(mButtonLaternOnClick);
    	}
    	
    	Button.OnClickListener mButtonLaternOnClick = new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			mIsAlertLater = true;
    			finish();
    		}
    	};

    	Button.OnClickListener mButtonOkOnClick = new Button.OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			finish();
    		}
    	};

}
