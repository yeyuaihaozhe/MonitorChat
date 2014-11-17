package com.lx.android.map.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.lx.android.map.R;
import com.lx.android.map.util.Util;

/**
 * 用户登录注册界面
 * 登录成功后，将用户名及密码写进本地缓存。
 * @author cainiaoliu
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

	private EditText et_username;
	private EditText et_password;
	private Button bt_login;
	private TextView bt_register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		et_username = (EditText) this.findViewById(R.id.et_username);
		et_password = (EditText) this.findViewById(R.id.et_password);
		bt_login = (Button) this.findViewById(R.id.btn_login);
		bt_register = (TextView) this.findViewById(R.id.btn_register);
		
		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		
		et_username.setText(username);
		bt_login.setOnClickListener(this);
		bt_register.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_login:{
			if(!Util.isNetworkAvailable(this)){
				toast(R.string.network_tips);
				return;
			}
			login();
		}break;
		case R.id.btn_register:{
			Intent intent = new Intent(this,RegisterActivity.class);
			startActivity(intent);
		}break;
		}
	}
	private void login() {
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(username)){
			toast("用户名不能为空");
		}else if(TextUtils.isEmpty(password)){
			toast("密码不能为空");
		}else{
			userManager.login(username, password, new SaveListener() {
				@Override
				public void onSuccess() {
					toast("登陆成功");
					Intent intent = new Intent(LoginActivity.this,MonitorLocation.class);
					startActivity(intent);
					finish();
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					toast("登陆失败");
				}
			});
		}
	}

}
