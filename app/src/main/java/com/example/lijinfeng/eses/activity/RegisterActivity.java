package com.example.lijinfeng.eses.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.PreferenceUtils;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO:
 * @author li.jf
 * Copyright (C) 15-9-19 下午3:18 wonhigh.cn All rights reserved.
 */

public class RegisterActivity extends BaseActivity {

    private ImageView ivHeadBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    private EditText etUserName;
    private EditText etRegisterEmail;
    private  EditText etPassword;
    private EditText etPasswordAgain;

    private Button btnRegister;
    private ProgressDialog progressDialog;

    private String userName;
    private String password;
    private String passwordAgain;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTitleView();
        initView();
        setListener();
    }

    @Override
    protected void initTitleView() {
        ivHeadBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText("注册");
        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
//        ivHeadRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_ok));
        ivHeadRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {
        etUserName = (EditText) findViewById(R.id.et_register_userName);
        etRegisterEmail = (EditText) findViewById(R.id.et_register_email);
        etPassword = (EditText) findViewById(R.id.et_register_pwd);
        etPasswordAgain = (EditText) findViewById(R.id.et_register_pwd_again);
        btnRegister = (Button) findViewById(R.id.btn_now_register);
    }

    private void setListener() {
        ivHeadBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_now_register) {
            register();
        } else if(view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void register() {
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        passwordAgain = etPasswordAgain.getText().toString();
        userEmail = etRegisterEmail.getText().toString();

        if (TextUtils.isEmpty(userName)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordAgain)
                || TextUtils.isEmpty(userEmail)) {
            ToastUtil.showCustomToast(this, "请检查输入项");
        } else {
            if (password.equals(passwordAgain)) {
                progressDialogShow();
                registerLeanCloud();
            } else {
                // 两次输入的密码不一致
                ToastUtil.showCustomToast(RegisterActivity.this, "两次输入密码不一致");
            }
        }
    }

    private void registerLeanCloud() {
            AVUser user = new AVUser();
            user.setUsername(userName);
            user.setPassword(password);
            user.setEmail(userEmail);
            user.signUpInBackground(new SignUpCallback() {
                public void done(AVException e) {
                    progressDialogDismiss();
                    if (e == null) {
                        PreferenceUtils.setPrefString(RegisterActivity.this, ESConstants.USER_NAME,userName);
                        PreferenceUtils.setPrefString(RegisterActivity.this,ESConstants.USER_EMAIL,userEmail);

                        // 显示注册成功
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        RegisterActivity.this.finish();
                    } else {
                        switch (e.getCode()) {
                            case 202:
//                                用户名已被注册，请重新填写
                                ToastUtil.showCustomToast(RegisterActivity.this,"该用户名已被占用，请选择其他的用户名");
//                                showError(activity.getString(R.string.error_register_user_name_repeat));
                                break;
                            case 203:
//                                邮箱已被注册
                                ToastUtil.showCustomToast(RegisterActivity.this,"邮箱已被占用，请通过找回密码进行后续操作");
                                break;
                            default:
                                ToastUtil.showCustomToast(RegisterActivity.this, "网络错误，请检查网络连接");
                                break;
                        }
                    }
                }
            });
    }

    private void progressDialogDismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void progressDialogShow() {
        progressDialog = ProgressDialog
                .show(RegisterActivity.this, "", "请稍候...", true, false);
    }
}
