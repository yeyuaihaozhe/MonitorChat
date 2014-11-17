package com.lx.android.map.ui;
/**
 * 	Q：高德地图API的服务调用有什么限制？
	A：地理/逆地理编码，每日每Key调用限制200000次，每10分钟内调用次数限制10000次
	Place查询，每日每Key调用限制100000次，每10分钟内调用次数限制50000次
	输入提示，每日每Key调用限制100000次，每10分钟内调用次数限制50000次
	路线规划，每日每Key调用限制100000次，每10分钟内调用次数限制5000次
	道路查询，每日每Key调用限制25000次，每10分钟内调用次数限制2500次
	静态地图，每日每Key调用限制25000次，每10分钟内调用次数限制2500次
	定位，每日每Key调用限制100000次，每10分钟内调用次数限制5000次
 */
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lx.android.map.BmobActivity;
import com.lx.android.map.R;
import com.lx.android.map.R.drawable;
import com.lx.android.map.R.id;
import com.lx.android.map.R.layout;

public class MonitorLocation extends BmobActivity implements AMapLocationListener,
		OnGeocodeSearchListener, LocationSource, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private Button mAddBtn;
	private Button mMonitoringBtn;
	private Button mMyLocationBtn;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	private GeocodeSearch mSearch = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocation_activity);
		mapView = (MapView) this.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		mAddBtn = (Button) this.findViewById(R.id.add);
		mMonitoringBtn = (Button) this.findViewById(R.id.monitoring);
		mMyLocationBtn = (Button) this.findViewById(R.id.mylocation);
		mAddBtn.setOnClickListener(this);
		mMonitoringBtn.setOnClickListener(this);
		mMyLocationBtn.setOnClickListener(this);

		init();
		mSearch = new GeocodeSearch(this);
		mSearch.setOnGeocodeSearchListener(this);
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		setupMap();
	}

	private void setupMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.BLACK);
		myLocationStyle.strokeWidth(10);// 半径
		aMap.setMyLocationStyle(myLocationStyle);

		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 定位图标可用
		aMap.setMyLocationEnabled(true);
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		System.out.println("aMap = " + aMap);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		System.out.println("into onGeocodeSearched");
		if (rCode == 0) {
			List<GeocodeAddress> address = result.getGeocodeAddressList();
			if (address.size() >= 0) {

				System.out.println("田福村：纬度 = "
						+ address.get(0).getLatLonPoint().getLatitude()
						+ "经度 = "
						+ address.get(0).getLatLonPoint().getLongitude());
				String latitude = String.valueOf(+address.get(0)
						.getLatLonPoint().getLatitude());
				String longitude = String.valueOf(+address.get(0)
						.getLatLonPoint().getLongitude());
				MarkerOptions marker = new MarkerOptions();
				LatLng latLng = new LatLng(address.get(0).getLatLonPoint()
						.getLatitude(), address.get(0).getLatLonPoint()
						.getLongitude());
				marker.position(latLng);
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location));
				aMap.addMarker(marker);
				CameraUpdateFactory factory = new CameraUpdateFactory();// 可视区域设置
				aMap.moveCamera(factory.newLatLngZoom(latLng, 1000 * 60));// 将地图移至可视区
			}
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		System.out.println("into onRegeocodeSearched");
	}

	// 激活定位
	@Override
	public void activate(OnLocationChangedListener listener) {
		System.out.println("激活定位");
		mListener = listener;
		System.out.println("mListener = " + mListener);
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
			System.out.println("AMapNetwork = "
					+ LocationProviderProxy.AMapNetwork);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				System.out.println("amapLocation = " + amapLocation);
				mListener.onLocationChanged(amapLocation);// 显示小蓝点
				String address = amapLocation.getAddress();
				String latitude = Double.toString(amapLocation.getLatitude());
				String longitude = Double.toString(amapLocation.getLongitude());
				System.out.println("我的位置 = " + amapLocation.getAddress());
			}
		} else {
			Toast.makeText(MonitorLocation.this, "无法捕捉位置", Toast.LENGTH_LONG)
					.show();
		}
	}

	//当被激活的activity被销毁时，就会执行此代码
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			String userName = data.getStringExtra("userName");
			System.out.println("userName = "+userName);
			toast("userName = "+userName);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add: {
			Intent intent = new Intent(this, AddMonitorObject.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.monitoring: {

		}
			break;
		case R.id.mylocation: {

		}
			break;
		default:
			break;
		}
	}

}
