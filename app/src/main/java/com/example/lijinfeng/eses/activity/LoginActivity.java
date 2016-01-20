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
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.NetworkUtil;
import com.example.lijinfeng.eses.util.PreferenceUtils;
import com.example.lijinfeng.eses.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * TODO: Login ES
 *
 * @author li.jf
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

        initView();
        setlistener();
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
        Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        CommonUtil.hideSoftKeyBoard(LoginActivity.this);

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        // check network, uername, password
        if(!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showToastS(this, R.string.network_unconnect);
            return;
        }

        if(TextUtils.isEmpty(username)) {
            ToastUtil.showToastS(this, R.string.username_should_not_null);
            return;
        }

        if(TextUtils.isEmpty(password)) {
            ToastUtil.showToastS(this, R.string.pwd_should_not_null);
            return;
        }

        showProgressDialog();
        loginLeanCloudServer();
    }

    /**
     * login LeanCloud background server
     */
    private void loginLeanCloudServer() {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override public void done(AVUser avUser, AVException e) {
                if (avUser != null) {
                    Log.d(TAG, "avUser.getEmail = " + avUser.getEmail());
                    dismissProgressDialog();
                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_NAME, username);
                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_PWD, password);
                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_EMAIL, avUser.getEmail());
                    Date date = avUser.getCreatedAt();
                    String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

                    PreferenceUtils.setPrefString(LoginActivity.this, ESConstants.USER_REGISTER_TIME, str);

                    ToastUtil.showToastS(LoginActivity.this, R.string.login_success);
                    // after login success
                    gotoMainActivity();
                } else {
                    dismissProgressDialog();
                    ToastUtil.showToastS(LoginActivity.this, R.string.login_error);
                }
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "",
            getResources().getString(R.string.logining), true, false);
    }

    private void dismissProgressDialog() {
       if(progressDialog !=null) {
           progressDialog.dismiss();
       }
    }
}
