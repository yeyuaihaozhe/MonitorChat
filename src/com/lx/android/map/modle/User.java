package com.lx.android.map.modle;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser{

	private static final long serialVersionUID = 1L;
	private String phoneId;//�ֻ�Ψһ��ʶID
	private BmobFile photo;//ͷ��
	private boolean sex;//�Ա�male=true  female=false;
	private String type;//�ֻ�����ϵͳ����android,apple

	public String getPhoneId() {
		return phoneId;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public BmobFile getPhoto() {
		return photo;
	}
	public void setPhoto(BmobFile photo) {
		this.photo = photo;
	}
}
