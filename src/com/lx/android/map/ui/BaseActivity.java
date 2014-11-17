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
 * ����
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
		//�û�����
		userManager = BmobUserManager.getInstance(this);
		//ȫ�ֵ���
		mApplication = CustomApplication.getmInstance();
	}
	public void updateUserInfos(){
		//��ѯ���û��ĺ����б�(��������б���ȥ���������û���Ŷ),Ŀǰ֧�ֵĲ�ѯ���Ѹ���Ϊ100�������޸����ڵ����������ǰ����BmobConfig.LIMIT_CONTACTS���ɡ�
		//����Ĭ�ϲ�ȡ���ǵ�½�ɹ�֮�󼴽������б�洢�����ݿ��У������µ���ǰ�ڴ���,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
			@Override
			public void onSuccess(List<BmobChatUser> users) {
				//���浽Application�з���Ƚ�
				CustomApplication.getmInstance().setContactList(Util.list2map(users));
			}
			@Override
			public void onError(int arg0, String arg1) {
				if(arg0==BmobConfig.CODE_COMMON_NONE){
					showLog(arg1);
				}else{
					showLog("��ѯ�����б�ʧ�ܣ�"+arg1);
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
