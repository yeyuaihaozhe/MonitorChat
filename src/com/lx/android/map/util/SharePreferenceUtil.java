package com.lx.android.map.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ��ѡ����� (�𶯡�����������֪ͨ)
 * @author cainiaoliu
 *
 */
public class SharePreferenceUtil {
	private SharedPreferences mSharedPreferences;
	public static SharedPreferences.Editor editor;
	
	public SharePreferenceUtil(Context context,String name) {
		mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}
	
}
