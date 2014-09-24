package com.divino.antiaddiction;

public class PeriodSetting {

	public PeriodSetting() {
		
	}
	
	public static final int ALERT_TYPE_DELAY_1_MIN = 1;
	public static final int ALERT_TYPE_DELAY_10_MIN = 2;
	
	public PeriodSetting(boolean isNormal, boolean isEnalbed, boolean[] weekday, int beginTime, int endTime, int exceedMin, int alertType) {
		
		mIsNormal = isNormal;
		mIsEnabled = isEnalbed;
		mWeekday = weekday;
		mBeginTime = beginTime;
		mEndTime = endTime;
		mExceedMin = exceedMin;
		mAlertType = alertType;
	}
	
	private boolean mIsNormal = false;
	private boolean[] mWeekday = new boolean[7];
	private int mAlertType;
	private boolean mIsEnabled;
	private int mBeginTime;
	private int mEndTime;
	private int mExceedMin;
	
	public boolean getIsNormal() {
		return mIsNormal;
	}
	public void setIsNormal(boolean isNormal) {
		this.mIsNormal = isNormal;
	}
	
	public boolean[] getWeekday() {
		return mWeekday;
	}
	public void setWeekday(boolean[] weekday) {
		this.mWeekday = weekday;
	}
	public int getAlertType() {
		return mAlertType;
	}
	public void setAlertType(int alertType) {
		this.mAlertType = alertType;
	}
	public boolean getIsEnabled() {
		return mIsEnabled;
	}
	public void setIsEnabled(boolean isEnabled) {
		this.mIsEnabled = isEnabled;
	}
	public int getBeginTime() {
		return mBeginTime;
	}
	public void setBeginTime(int beginTime) {
		this.mBeginTime = beginTime;
	}
	public int getEndTime() {
		return mEndTime;
	}
	public void setEndTime(int endTime) {
		this.mEndTime = endTime;
	}
	public int getExceedMin() {
		return mExceedMin;
	}
	public void setExceedMin(int exceedMin) {
		this.mExceedMin = exceedMin;
	}
}
