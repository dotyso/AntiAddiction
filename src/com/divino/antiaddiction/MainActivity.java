package com.divino.antiaddiction;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.divino.antiaddiction.AboutActivity;
import com.divino.antiaddiction.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private initPeriodSettingControl data = new initPeriodSettingControl(this);
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getOverflowMenu();
		
    	Intent serviceIntent = new Intent(data.mContext, AntiAddictionService.class);  
		startService(serviceIntent);
		
		Button okButton = (Button)this.findViewById(R.id.okButton);
		okButton.setOnClickListener(okButtonOnClick);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//初始化控件
		List<PeriodSetting> settings = PeriodSettingManager.getInstance().getPeriodSetting();
		initPeriodSettingControl(settings.get(0), R.id.switchNormal, 0, 0, R.id.textBoxNormalExceed, R.id.radioButtonNormalForbid1M, R.id.radioButtonNormalForbid10M);
		initPeriodSettingControl(settings.get(1), R.id.switchSpecial1, R.id.textBoxSpecial1Begin, R.id.textBoxSpecial1To, R.id.textBoxSpecial1Exceed, R.id.radioButtonSpecial1Forbid1M, R.id.radioButtonSpecial1Forbid10M);	
		initPeriodSettingControl(settings.get(2), R.id.switchSpecial2, R.id.textBoxSpecial2Begin, R.id.textBoxSpecial2To, R.id.textBoxSpecial2Exceed, R.id.radioButtonSpecial2Forbid1M, R.id.radioButtonSpecial2Forbid10M);	
		
	};
	
	Button.OnClickListener okButtonOnClick = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {

			int[] editBoxExceedIds = new int[3];
			editBoxExceedIds[0] = R.id.textBoxNormalExceed;
			editBoxExceedIds[1] = R.id.textBoxSpecial1Exceed;
			editBoxExceedIds[2] = R.id.textBoxSpecial2Exceed;
			
			if (!checkExceed(editBoxExceedIds)) {
				Toast.makeText(mContext, "超时时间必须不于2分钟", Toast.LENGTH_LONG).show();
				return;
			}
			
			int[] editBoxFromToIds = new int[4];
			editBoxFromToIds[0] = R.id.textBoxSpecial1Begin;
			editBoxFromToIds[1] = R.id.textBoxSpecial1To;
			editBoxFromToIds[2] = R.id.textBoxSpecial2Begin;
			editBoxFromToIds[3] = R.id.textBoxSpecial2To;
			
			if (!checkFromTo(editBoxFromToIds)) {
				Toast.makeText(mContext, "时间范围必须在0至24时之间", Toast.LENGTH_LONG).show();
				return;
			}
			
			List<PeriodSetting> settings = new ArrayList<PeriodSetting>();
			settings.add(readPeriodSettingControl(0, R.id.switchNormal, 0, 0, R.id.textBoxNormalExceed, R.id.radioButtonNormalForbid1M));
			settings.add(readPeriodSettingControl(1, R.id.switchSpecial1, R.id.textBoxSpecial1Begin, R.id.textBoxSpecial1To, R.id.textBoxSpecial1Exceed, R.id.radioButtonSpecial1Forbid1M));
			settings.add(readPeriodSettingControl(2, R.id.switchSpecial2, R.id.textBoxSpecial2Begin, R.id.textBoxSpecial2To, R.id.textBoxSpecial2Exceed, R.id.radioButtonSpecial2Forbid1M));
			
	    	PeriodSettingManager.getInstance().savePeriodSetting(settings);
	    	PeriodSettingManager.getInstance().UpdateCachePeriodSetting();
	    	
	    	Intent serviceIntent = new Intent(data.mContext, AntiAddictionService.class);  
			startService(serviceIntent);
			
			Toast.makeText(mContext, "保存设置成功，已启动防沉迷服务程序", Toast.LENGTH_LONG).show();
			
		}
	};
	
	private boolean checkExceed(int[] editBoxIds) {
		
		for (int editBoxId : editBoxIds) {
			int min = Integer.parseInt(((EditText)findViewById(editBoxId)).getText().toString().trim());
			if (min < 2)
				return false;
		}
		
		return true;
	}
	
	private boolean checkFromTo(int[] editBoxIds) {
		
		for (int editBoxId : editBoxIds) {
			int hour = Integer.parseInt(((EditText)findViewById(editBoxId)).getText().toString().trim());
			if (hour < 0 || hour > 24)
				return false;
		}
		
		return true;
	}
	
	private void initPeriodSettingControl(PeriodSetting setting, int switchIsEnabledId, int textBeginTimeId, int textEndTimeId, int textExceedMinId, int radioAlertType1Id, int radioAlertType2Id) {
		Switch switchIsEnabled = (Switch)findViewById(switchIsEnabledId);
		switchIsEnabled.setChecked(setting.getIsEnabled());
		
		if (textBeginTimeId != 0) {
			EditText textBeginTime = (EditText)findViewById(textBeginTimeId);
			textBeginTime.setText(String.valueOf(setting.getBeginTime()));
		}
		
		if (textEndTimeId != 0) {		
			EditText textEndTime = (EditText)findViewById(textEndTimeId);
			textEndTime.setText(String.valueOf(setting.getEndTime()));
		}
		
		EditText textExceedMin = (EditText)findViewById(textExceedMinId);
		textExceedMin.setText(String.valueOf(setting.getExceedMin()));
		
		RadioButton radioAlertType1 = (RadioButton)findViewById(radioAlertType1Id);
		RadioButton radioAlertType2 = (RadioButton)findViewById(radioAlertType2Id);		
		switch (setting.getAlertType()) {
		case 1:
			radioAlertType1.setChecked(true);
			radioAlertType2.setChecked(false);			
			break;
		case 2:
			radioAlertType1.setChecked(false);
			radioAlertType2.setChecked(true);			
			break;
		}
	}
	
	private PeriodSetting readPeriodSettingControl(int index, int switchIsEnabledId, int textBeginTimeId, int textEndTimeId, int textExceedMinId, int radioAlertType1Id) {
		
		PeriodSetting periodSetting = new PeriodSetting();
		
		if (index == 0)
			periodSetting.setIsNormal(true);
		else
			periodSetting.setIsNormal(false);
		
		Switch switchIsEnabled = (Switch)findViewById(switchIsEnabledId);
		periodSetting.setIsEnabled(switchIsEnabled.isChecked());
		
		if (textBeginTimeId != 0) {
			EditText textBeginTime = (EditText)findViewById(textBeginTimeId);
			periodSetting.setBeginTime(Integer.parseInt(textBeginTime.getText().toString()));
		}
		
		if (textEndTimeId != 0) {		
			EditText textEndTime = (EditText)findViewById(textEndTimeId);
			periodSetting.setEndTime(Integer.parseInt(textEndTime.getText().toString()));
		}
		
		EditText textExceedMin = (EditText)findViewById(textExceedMinId);
		periodSetting.setExceedMin(Integer.parseInt(textExceedMin.getText().toString()));
		
		RadioButton radioAlertType1 = (RadioButton)findViewById(radioAlertType1Id);
		if (radioAlertType1.isChecked()) {
			periodSetting.setAlertType(1);
		}
		else {
			periodSetting.setAlertType(2);
		}
		return periodSetting;
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
				intent.setClass(data.mContext, AboutActivity.class);
				startActivity(intent);  
				break;
		

	    }
	    return true;
	}
	

	

}
