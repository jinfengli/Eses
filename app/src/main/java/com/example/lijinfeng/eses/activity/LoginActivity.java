package com.example.lijinfeng.eses.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.PreferenceUtils;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO: Login ES
 *
 * @date 15-9-21 下午9:09
 * Copyright (C) li.jf All rights reserved.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgetPassword;

    private ProgressDialog progressDialog;

    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initTitleView();
        initView();
        setlistener();
    }

    @Override
    protected void initTitleView() {
        // 登录界面隐藏头部
    }

    @Override
    protected void initView() {
        etUsername = (EditText) findViewById(R.id.et_login_username);
        etPassword = (EditText) findViewById(R.id.et_login_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
    }

    private void setlistener() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_login) {
            login();
        } else if(view.getId() == R.id.tv_register) {
            gotoRegisterActivity();
        } else if(view.getId() == R.id.tv_forget_password) {
            gotoForgetPwdActivity();
        }
    }

    private void gotoRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoForgetPwdActivity() {
        Intent intent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if(TextUtils.isEmpty(username)) {
            ToastUtil.showCustomToast(this, "用户名不能为空");
            return;
        }

        if(TextUtils.isEmpty(password)) {
            ToastUtil.showCustomToast(this, "密码不能为空");
            return;
        }

        showProgressDialog();
        loginLeanCloudServer();
    }

    /**
     * 登录LeanCloud后台
     */
    private void loginLeanCloudServer() {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (avUser != null) {
                    Log.d(TAG, avUser.getEmail());
                    dismissProgressDialog();
                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_NAME, username);
                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_PWD,password);

                    ToastUtil.showCustomToast(LoginActivity.this, "登录成功");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    dismissProgressDialog();
                    ToastUtil.showCustomToast(LoginActivity.this, "登录失败,请确认用户名和密码后重试.");
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", "登录中...", true, false);
    }

    private void dismissProgressDialog() {
       if(progressDialog !=null) {
           progressDialog.dismiss();
       }
    }
}
