package com.example.lijinfeng.eses.activity;

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
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.base.BaseActivity;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO：找回用户名或密码
 * @author li.jf
 * Copyright (C) 15-9-19 下午3:19 wonhigh.cn All rights reserved.
 */

public class ForgetPasswordActivity extends BaseActivity {

    private ImageView ivHeadBack;
    private TextView tvHeadTitle;
    private ImageView ivHeadRight;

    private EditText etForgetPwdEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initTitleView();
        initView();
        setListener();
    }

    @Override
    protected void initTitleView() {

        ivHeadBack = (ImageView) findViewById(R.id.ivBack);
        tvHeadTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        tvHeadTitle.setText("找回帐号或密码");

        ivHeadRight = (ImageView) findViewById(R.id.ivHeaderRight);
        ivHeadRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_ok));
    }

    @Override
    protected void initView() {
        etForgetPwdEmail = (EditText) findViewById(R.id.et_forget_pwd_email);
    }

    private void setListener() {
        ivHeadBack.setOnClickListener(this);
        ivHeadRight.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivHeaderRight) {
            sendFindPwdMail();
        } else if(view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    private void sendFindPwdMail() {
        String email = etForgetPwdEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showCustomToast(this, "邮箱不能为空");
            // 这儿最好是再判断一下邮件的格式是否正确
            return;
        }

        AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                // 回调无异常
                if (e == null) {
                    // 邮件发送成功，到邮件收取邮件重新登录
                    ToastUtil.showCustomToast(ForgetPasswordActivity.this, "邮件发送成功，请检查邮件并重置密码");
                    gotoLoginActivity();

                } else {
                    // 邮件发送异常，请检查您的邮件地址
                    ToastUtil.showCustomToast(ForgetPasswordActivity.this, "邮件发送失败，请检查邮件地址");

                }
            }
        });
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
