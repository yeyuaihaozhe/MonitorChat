package com.lx.android.map.ui;

import java.io.File;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.lx.android.map.R;
import com.lx.android.map.modle.User;
import com.lx.android.map.util.Util;

/**
 * 用户注册界面
 * 注册成功后，广播所有用户
 * @author cainiaoliu
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener{

	private EditText et_account;
	private EditText et_password;
	private EditText et_pwconfirm;
	private EditText et_email;
	private Button bt_register;
	private CheckBox cb_sex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		et_account = (EditText) this.findViewById(R.id.et_account);
		et_password = (EditText) this.findViewById(R.id.et_password);
		et_pwconfirm = (EditText) this.findViewById(R.id.et_pwconfirm);
		et_email = (EditText) this.findViewById(R.id.et_email);
		bt_register = (Button) this.findViewById(R.id.btn_register);
		cb_sex = (CheckBox) this.findViewById(R.id.cb_sex);
		
		bt_register.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_register:{
			register();
		}break;
		}
	}
	private void register() {
		String username = et_account.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String pwConfirm = et_pwconfirm.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		if("".equals(username)){
			toast(R.string.register_tips_name_empty);
			return;
		}
		if(password.length() < 6){
			toast(R.string.register_tips_password_wrong);
			return;
		}
		if(!password.equals(pwConfirm)){
			toast(R.string.register_tips_password_notmatch);
			return;
		}
		if("".equals(email)){
			toast(R.string.register_tips_email_empty);
			return;
		}
		if(!Util.isNetworkAvailable(this)){
			toast(R.string.network_tips);
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setType("android");
		user.setPhoneId(Util.getImeiId(RegisterActivity.this));
		File file = null;
		Resources r = this.getResources();
		if(cb_sex.isChecked()){
			String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				    + r.getResourcePackageName(R.drawable.defaultmale) + "/"
				    + r.getResourceTypeName(R.drawable.defaultmale) + "/"
				    + r.getResourceEntryName(R.drawable.defaultmale);
			System.out.println("path = "+path);
			file = new File(path);
			user.setSex(true);
		}else{
			String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				    + r.getResourcePackageName(R.drawable.defaultfemale) + "/"
				    + r.getResourceTypeName(R.drawable.defaultfemale) + "/"
				    + r.getResourceEntryName(R.drawable.defaultfemale);
			System.out.println("path = "+path);
			file = new File(path);
			user.setSex(false);
		}
		BmobFile bFile = new BmobFile(file);
		System.out.println("file = "+file);
		user.setPhoto(bFile);
		user.signUp(RegisterActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				toast(R.string.register_tips_register_success);
				Intent intent = new Intent(RegisterActivity.this,MonitorLocation.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(int code, String arg1) {
				toast(R.string.register_tips_register_fail+arg1+" code="+code);
			}
		});
	}

}
