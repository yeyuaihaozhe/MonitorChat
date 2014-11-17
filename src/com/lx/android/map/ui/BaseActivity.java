package com.lx.android.map.ui;


import java.util.List;

import com.lx.android.map.CustomApplication;
import com.lx.android.map.util.Util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.FindListener;

/**
 * 基类
 * @author cainiaoliu
 *
 */
public class BaseActivity extends FragmentActivity{
	BmobUserManager userManager;
	CustomApplication mApplication;
	private String TAG = "CQX";
	private String APPID = "3fb18ddadd605f7d1b9cfabd45b15b25";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, APPID);
		//用户管理
		userManager = BmobUserManager.getInstance(this);
		//全局单例
		mApplication = CustomApplication.getmInstance();
	}
	public void updateUserInfos(){
		//查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
		//这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
			@Override
			public void onSuccess(List<BmobChatUser> users) {
				//保存到Application中方便比较
				CustomApplication.getmInstance().setContactList(Util.list2map(users));
			}
			@Override
			public void onError(int arg0, String arg1) {
				if(arg0==BmobConfig.CODE_COMMON_NONE){
					showLog(arg1);
				}else{
					showLog("查询好友列表失败："+arg1);
				}
			}
		});
	}
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
	public void toast(int id){
		String msg = this.getResources().getString(id);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
	public void showLog(String msg){
		Log.d(TAG,msg);
	}
}
