package com.lx.android.map;

import cn.bmob.v3.Bmob;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BmobActivity extends Activity{
	private String TAG = "CQX";
	private String APPID = "3fb18ddadd605f7d1b9cfabd45b15b25";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, APPID);
	}
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
}
