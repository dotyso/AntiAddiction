package com.divino.antiaddiction;

import com.divino.antiaddiction.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		Button feedbackButton = (Button)this.findViewById(R.id.feedbackButton);
		feedbackButton.setOnClickListener(feedbackButtonOnClick);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
	}
	*/
	
	Button.OnClickListener feedbackButtonOnClick = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
	
			Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);  
		    myIntent.setType("plain/text");  
		    myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "divino@163.com");  
		    myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "图片压缩精英-反馈意见");  
		    startActivity(Intent.createChooser(myIntent, "填写反馈意见")); 
		}
	};
}
