package com.lx.android.map.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import cn.bmob.im.bean.BmobChatUser;

public class Util {
	/**
	 * get phone IMEI
	 * @param context
	 * @return
	 */
	public static String getImeiId(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if(!isEmpty(imei)){
			return imei;
		}
		return "";
	}
	/**
	 * get phone wifi mac address
	 * @param context
	 * @return
	 */
	public static String getWifiMac(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String wifiMac = info.getMacAddress();
		if(!isEmpty(wifiMac)){
			return wifiMac;
		}
		return "";
	}
	/**
	 * get SN code
	 * @param context
	 * @return
	 */
	public static String getSNCode(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String sn = tm.getSimSerialNumber();
		if(!isEmpty(sn)){
			return sn;
		}
		return "";
	}
	/**
	 * get randomUUID
	 * @return
	 */
	public static String getUuid(){
		String uuid = UUID.randomUUID().toString();
		if(!isEmpty(uuid)){
			return uuid;
		}
		return "";
	}
	public static String paserToJson(Object obj){
		String result="";
//		JsonReader reader = new JsonReader(in)
		return result;
	}
	private static boolean isEmpty(String str) {
		if("".equals(str)){
			return true;
		}
		return false;
	}
	/**
	 * 读文件返回文件内容
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readFile(File file) throws Exception{
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len=0;
		while((len = fis.read(buffer)) != -1){
			bos.write(buffer,0,len);
		}
		byte[] result = bos.toByteArray();
		String content = new String(result);
		bos.flush();
		bos.close();
		fis.close();
		return content;
	}
	/**
	 * 联系人List转为用户名为Key,BmobChatuser为value的map
	 * @param users
	 * @return
	 */
	public static Map<String,BmobChatUser> list2map(List<BmobChatUser>users){
		Map<String,BmobChatUser> map = new HashMap<String,BmobChatUser>();
		for(BmobChatUser user:users){
			String username = user.getUsername();
			map.put(username, user);
		}
		return map;
	}
	/**
	 * 检查网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			return info.isAvailable();
		}
		return false;
	}
	/**
	 * 获取网络信息
	 * @param context
	 * @return
	 */
	private static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
}
