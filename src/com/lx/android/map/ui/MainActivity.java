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
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);//Ĭ��xml��ʽ�洢
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
					System.out.println("��ȡ�ɹ�");
					handle(password,user);
				}
				@Override
				public void onError(int arg0, String arg1) {
					System.out.println("��ȡ����");
					toast("��ȡ����ʧ��");
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
			if(!"".equals(objectId)){//��������
				System.out.println("�����ͼ��ȡλ��");
				mAMapLocationManager = LocationManagerProxy.getInstance(this);
				mAMapLocationManager.requestLocationData(
						LocationProviderProxy.AMapNetwork, -1, 10, this);//ֻ��λһ�Σ��Զ�removeUpdate
			}else{//������󣬴��û�����ȥ
				Intent intent = new Intent(this,LoginActivity.class);//��ʾ��ͼ
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
		//����ǰ���û��ֻ�Idд��Bmob��λ����Ϣ����
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
			System.out.println("�ҵ�λ�� = " + amapLocation.getAddress());
		} else {
			toast("�޷���׽λ��");
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
		if(isExist){//�Ѵ��ڴ��û�
			location.update(this, username,new UpdateListener(){
				@Override
				public void onSuccess() {
					toast("����λ�óɹ�");
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}else{
			location.save(this, new SaveListener(){
				@Override
				public void onSuccess() {
					toast("����λ�óɹ�");
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}
		//��������
		Intent intent = new Intent(this,MonitorLocation.class);
		startActivity(intent);
		finish();
	}
}
