package com.divino.antiaddiction;

import java.lang.reflect.Field;

import com.divino.antiaddiction.AboutActivity;
import com.divino.antiaddiction.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class MainActivity extends Activity {
	
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getOverflowMenu();
		
    	Intent serviceIntent = new Intent(mContext, AntiAddictionService.class);  
		startService(serviceIntent);
	}

	//force to show overflow menu in actionbar
	private void getOverflowMenu() {
	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.activity_main, menu);
		 return super.onCreateOptionsMenu(menu);
	}

	//菜单项被选中时执行该方法
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.about_menuitem:
				Intent intent = new Intent();
				intent.setClass(mContext, AboutActivity.class);
				startActivity(intent);  
				break;
	    }
	    return true;
	}

}
