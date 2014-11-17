package com.lx.android.map.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.lx.android.map.BmobActivity;
import com.lx.android.map.R;
import com.lx.android.map.R.id;
import com.lx.android.map.R.layout;
import com.lx.android.map.modle.User;
import com.lx.android.map.util.Util;
/**
 * add monitor user 
 * @author cainiao liu
 *
 */
public class AddMonitorObject extends BmobActivity implements OnClickListener{

	private LayoutInflater inflater;//打气筒，充气泵
	
	private EditText mSearchName;
	private Button mSearchBtn;
	private ListView mUsersList;
	private List<User> users = new ArrayList<User>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_activity);
		mSearchName = (EditText) this.findViewById(R.id.et_searchName);
		mSearchBtn = (Button) this.findViewById(R.id.bt_search);
		mUsersList = (ListView) this.findViewById(R.id.lv_user);
		mSearchBtn.setOnClickListener(this);
		
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		
		findBmobUser();
		mUsersList.setAdapter(new MyAdapt());
		mUsersList.setOnItemClickListener(new OnItemClickListener() {
			//parent是listView，view是getView中返回的view对象
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TextView tv = (TextView) view.findViewById(R.id.tv_username);
				String userName = tv.getText().toString();
				Intent data = new Intent();
				data.putExtra("userName", userName);
				setResult(0,data);
				finish();
			}
		});
	}
	private class MyAdapt extends BaseAdapter{

		@Override
		public int getCount() {
			return users.size();
		}

		@Override
		public Object getItem(int position) {
			return users.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = inflater.inflate(R.layout.user_list, null);
			ImageView im_Photo = (ImageView) view.findViewById(R.id.im_photo);
			TextView tv = (TextView) view.findViewById(R.id.tv_username);
			System.out.println("position = "+position);
			BmobFile photo = users.get(position).getPhoto();
			if(photo != null){
				photo.loadImage(getApplicationContext(), im_Photo,100,100);//bmob封装的
			}
//			System.out.println(photo);
//			try {
//				System.out.println("进来了");
//				Bitmap bitmap = BitmapFactory.decodeFile(photo.getFilename());
//				System.out.println(bitmap);
//				im_Photo.setImageBitmap(bitmap);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			tv.setTextSize(25);
			tv.setText(users.get(position).getUsername());
			return view;
		}
	}
	/**
	 * 查询用户
	 */
	private void findBmobUser() {
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("phoneId", Util.getImeiId(this));
		query.findObjects(this, new FindListener<User>() {
			@Override
			public void onSuccess(List<User> object) {
				toast("查询用户成功：" + object.size());
				users = object;
			}
			@Override
			public void onError(int code, String msg) {
				toast("查询用户失败：" + msg);
			}
		});
		System.out.println("query = "+query);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_search:{
			
		}break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}



}
