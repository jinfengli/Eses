package com.example.lijinfeng.eses.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.PushService;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO: 登录
 * @author li.jf
 * Copyright (C) 15-9-19 下午3:09 li.jf All rights reserved.
 */

public class LoginActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin;

    private Button btnRegister;
    private Button btnForgetPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initTitleView();
        initView();
        setlistener();

//        gotoMainActivity();
    }



//    private void gotoMainActivity() {
//        if(getUserId() != null) {
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
//            this.finish();
//        }
//    }

    @Override
    protected void initTitleView() {
        // 登录界面隐藏头部
    }

    @Override
    protected void initView() {
        etUsername = (EditText) findViewById(R.id.et_login_username);
        etPassword = (EditText) findViewById(R.id.et_login_pwd);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnForgetPassword = (Button) findViewById(R.id.btn_forget_password);
    }

    private void setlistener() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_login) {
            login();
        } else if(view.getId() == R.id.btn_register) {
            gotoRegisterActivity();
        } else if(view.getId() == R.id.btn_forget_password) {
            gotoForgetPwdActivity();
        }
    }

    private void gotoRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void gotoForgetPwdActivity() {
        Intent intent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(username)) {
            ToastUtil.showCustomToast(this,"用户名不能为空");
            return;
        }

        if(TextUtils.isEmpty(password)) {
            ToastUtil.showCustomToast(this,"密码不能为空");
            return;
        }

        showProgressDialog();

        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (avUser != null) {
                    dismissProgressDialog();
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
