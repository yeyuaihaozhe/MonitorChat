package com.lx.android.map;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;

import com.lx.android.map.util.SharePreferenceUtil;

public class CustomApplication extends Application{
	//����ģʽ�����������������ڿ���
	public static CustomApplication mInstance;
	private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

	@Override
	public void onCreate() {
		super.onCreate();
		BmobChat.DEBUG_MODE = true;
		mInstance = this;
		init();
	}
	private void init() {
		
	}
	
	public static CustomApplication getmInstance() {
		return mInstance;
	}
	public static void setmInstance(CustomApplication mInstance) {
		CustomApplication.mInstance = mInstance;
	}
	
	public Map<String, BmobChatUser> getContactList() {
		return contactList;
	}
	//�������б��浽�ڴ���
	public void setContactList(Map<String, BmobChatUser> contactList) {
		if (this.contactList != null) {
			this.contactList.clear();
		}
		this.contactList = contactList;
	}
	// ����ģʽ�����ܼ�ʱ��������
	SharePreferenceUtil mSpUtil;
}
