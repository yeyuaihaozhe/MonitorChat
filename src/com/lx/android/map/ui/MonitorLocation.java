package com.lx.android.map.ui;
/**
 * 	Q���ߵµ�ͼAPI�ķ��������ʲô���ƣ�
	A������/�������룬ÿ��ÿKey��������200000�Σ�ÿ10�����ڵ��ô�������10000��
	Place��ѯ��ÿ��ÿKey��������100000�Σ�ÿ10�����ڵ��ô�������50000��
	������ʾ��ÿ��ÿKey��������100000�Σ�ÿ10�����ڵ��ô�������50000��
	·�߹滮��ÿ��ÿKey��������100000�Σ�ÿ10�����ڵ��ô�������5000��
	��·��ѯ��ÿ��ÿKey��������25000�Σ�ÿ10�����ڵ��ô�������2500��
	��̬��ͼ��ÿ��ÿKey��������25000�Σ�ÿ10�����ڵ��ô�������2500��
	��λ��ÿ��ÿKey��������100000�Σ�ÿ10�����ڵ��ô�������5000��
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
		mapView.onCreate(savedInstanceState);// ����Ҫд
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
		myLocationStyle.strokeWidth(10);// �뾶
		aMap.setMyLocationStyle(myLocationStyle);

		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ��λͼ�����
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

				System.out.println("�︣�壺γ�� = "
						+ address.get(0).getLatLonPoint().getLatitude()
						+ "���� = "
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
				CameraUpdateFactory factory = new CameraUpdateFactory();// ������������
				aMap.moveCamera(factory.newLatLngZoom(latLng, 1000 * 60));// ����ͼ����������
			}
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		System.out.println("into onRegeocodeSearched");
	}

	// ���λ
	@Override
	public void activate(OnLocationChangedListener listener) {
		System.out.println("���λ");
		mListener = listener;
		System.out.println("mListener = " + mListener);
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
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
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
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
				mListener.onLocationChanged(amapLocation);// ��ʾС����
				String address = amapLocation.getAddress();
				String latitude = Double.toString(amapLocation.getLatitude());
				String longitude = Double.toString(amapLocation.getLongitude());
				System.out.println("�ҵ�λ�� = " + amapLocation.getAddress());
			}
		} else {
			Toast.makeText(MonitorLocation.this, "�޷���׽λ��", Toast.LENGTH_LONG)
					.show();
		}
	}

	//���������activity������ʱ���ͻ�ִ�д˴���
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
