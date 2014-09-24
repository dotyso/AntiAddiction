package com.divino.antiaddiction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PeriodSettingManager {
	
	
	
	private static PeriodSettingManager mPeriodSettingManager;
	
	public static PeriodSettingManager getInstance() {
		if (mPeriodSettingManager == null) {
			mPeriodSettingManager = new PeriodSettingManager();
		}
		
		return mPeriodSettingManager;
		
	}
	
	public PeriodSetting checkHit(Date currentDate) {
		List<PeriodSetting> periodSettings = PeriodSettingManager.getInstance().getPeriodSetting();
		
		//倒序找到满足条件的设置
		PeriodSetting hitPeriodSetting = null;
		for (int i=periodSettings.size()-1; i>=0; i--) {
			
			if (periodSettings.get(i).getIsEnabled()) {
				
				if (!periodSettings.get(i).getIsNormal()) {
					if (periodSettings.get(i).getBeginTime() <= periodSettings.get(i).getEndTime()) {
						if (periodSettings.get(i).getBeginTime() <= currentDate.getHours() && currentDate.getHours() <= periodSettings.get(i).getEndTime()) {
							hitPeriodSetting = periodSettings.get(i);
							break;
						}
					}
					else {
						if (periodSettings.get(i).getBeginTime() <= currentDate.getHours() || currentDate.getHours() <= periodSettings.get(i).getEndTime()) {
							hitPeriodSetting = periodSettings.get(i);
							break;
						}
					}
				}
				else {
					hitPeriodSetting = periodSettings.get(i);
					break;
				}
			}
		}
		
		return hitPeriodSetting;
	}  
	
	public void initPeriodSetting() {
		List<PeriodSetting> settings = new ArrayList<PeriodSetting>();
		settings.add(new PeriodSetting(true, true, new boolean[]{true, true, true, true, true, true, true}, 0, 24, 30, 1));
		settings.add(new PeriodSetting(false, true, new boolean[]{true, true, true, true, true, true, true}, 21, 7, 15, 2));				
		settings.add(new PeriodSetting(false, false, new boolean[]{true, true, true, true, true, true, true}, 0, 24, 15, 2));				
	
    	Type type = new TypeToken<List<PeriodSetting>>(){}.getType();
    	Gson gson = new GsonBuilder().create();
    	String value = gson.toJson(settings, type);//.fromJson(result.getHttpResult(), type);
    	
		SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences("AntiAddiction", Context.MODE_PRIVATE); //私有数据
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString("PeriodSetting", value);
		editor.commit();
		
	}
	

	public List<PeriodSetting> getPeriodSetting() {
		SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences("AntiAddiction", Context.MODE_PRIVATE); //私有数据
		String value = sharedPreferences.getString("PeriodSetting", "");
		if (value.equals("")) {
			initPeriodSetting();
			value = sharedPreferences.getString("PeriodSetting", "");
		}
		
    	Type type = new TypeToken<List<PeriodSetting>>(){}.getType();
    	Gson gson = new GsonBuilder().create();
    	List<PeriodSetting> periodSettings = (List<PeriodSetting>)gson.fromJson(value, type);
		return periodSettings;
	}
	
	public void savePeriodSetting(List<PeriodSetting> settings) {
		Type type = new TypeToken<List<PeriodSetting>>(){}.getType();
    	Gson gson = new GsonBuilder().create();
    	String value = gson.toJson(settings, type);
    	
		SharedPreferences sharedPreferences = ContextUtil.getInstance().getSharedPreferences("AntiAddiction", Context.MODE_PRIVATE); //私有数据
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString("PeriodSetting", value);
		editor.commit();
	}
}
