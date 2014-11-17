package com.lx.android.map.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.lx.android.map.BmobActivity;
import com.lx.android.map.R;
import com.lx.android.map.R.layout;
import com.lx.android.map.modle.LocationInfo;
import com.lx.android.map.modle.User;
import com.lx.android.map.util.Util;

/**
 * start activity
 * 
 * 1:read local username,password,if not exist open login window
 * 2:if exist and equal to Bmob data,open Main window,if not equal to Bmob data
 * 	open login window and set the input field by current username
 * 
 * @author cainiaoliu
 */
public class MainActivity extends BmobActivity implements AMapLocationListener{
	private String TAG = "CQX";
	private SharedPreferences sp;
	private String username;
	private String password;
	private List<User> users;
	private boolean isExist;
	private LocationInfo location;
	private LocationManagerProxy mAMapLocationManager;
	@Override
	public void onCreate(Bundle savedInstanceState){
		isExist = false;
		users = new ArrayList<User>();
		location = new LocationInfo();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);//默认xml格式存储
		username = sp.getString("username", "");
		password = sp.getString("password", "");
		System.out.println("username = "+username);
		System.out.println("password = "+password);
		if("".equals(username) && !"".equals(password)){
			System.out.println("Bmobquery");
			BmobQuery<User> query = new BmobQuery<User>();
			query.addWhereEqualTo("username", username);
			query.findObjects(MainActivity.this,new FindListener<User>() {
				@Override
				public void onSuccess(List<User> user) {
					System.out.println("读取成功");
					handle(password,user);
				}
				@Override
				public void onError(int arg0, String arg1) {
					System.out.println("读取出错");
					toast("读取数据失败");
				}
			} );
		}else{
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	private void handle(String password,List<User> user){
		if(user.size() == 1){
			String objectId = user.get(0).getObjectId();
			System.out.println(objectId);
			if(!"".equals(objectId)){//打开主界面
				System.out.println("进入地图读取位置");
				mAMapLocationManager = LocationManagerProxy.getInstance(this);
				mAMapLocationManager.requestLocationData(
						LocationProviderProxy.AMapNetwork, -1, 10, this);//只定位一次，自动removeUpdate
			}else{//密码错误，传用户名过去
				Intent intent = new Intent(this,LoginActivity.class);//显示意图
				intent.putExtra("com.lx.android.map.username", username);
				startActivity(intent);
				finish();
			}
		}
	}
	@Override
	public void onLocationChanged(Location arg0) {
		
	}
	@Override
	public void onProviderDisabled(String arg0) {
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		//将当前的用户手机Id写进Bmob，位置信息更新
		String address = "";
		String latitude = "";
		String longitude = "";
		String phoneId = Util.getImeiId(this);
		System.out.println("phoneId = "+phoneId);
		if (amapLocation != null && 
				amapLocation.getAMapException().getErrorCode() == 0) {
			address = amapLocation.getAddress();
			latitude = Double.toString(amapLocation.getLatitude());
			longitude = Double.toString(amapLocation.getLongitude());
			System.out.println("我的位置 = " + amapLocation.getAddress());
		} else {
			toast("无法捕捉位置");
		}
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setPhoneId(phoneId);
		location.setAddress(address);
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", username);
		query.findObjects(this, new FindListener<User>() {
			@Override
			public void onSuccess(List<User> object) {
				if(object.size() == 1){
					isExist = true;
				}
				saveLocation(location,isExist,username);
			}
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
	}
	private void saveLocation(LocationInfo location,boolean isExist,String username){
		if(isExist){//已存在此用户
			location.update(this, username,new UpdateListener(){
				@Override
				public void onSuccess() {
					toast("更新位置成功");
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}else{
			location.save(this, new SaveListener(){
				@Override
				public void onSuccess() {
					toast("插入位置成功");
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}
		//打开主界面
		Intent intent = new Intent(this,MonitorLocation.class);
		startActivity(intent);
		finish();
	}
}
