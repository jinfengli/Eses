package com.example.lijinfeng.eses.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.example.lijinfeng.eses.util.PreferenceUtils;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO: user register
 *
 * @author li.jf
 * @date 15-9-19 下午3:18
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private EditText etUserName;
    private EditText etRegisterEmail;
    private EditText etPassword;
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

    protected void initTitleView() {
        CommonUtil.configToolBarParams(this);

        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.user_register);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
    }

    protected void initView() {
        etUserName = (EditText) findViewById(R.id.et_register_userName);
        etRegisterEmail = (EditText) findViewById(R.id.et_register_email);
        etPassword = (EditText) findViewById(R.id.et_register_pwd);
        etPasswordAgain = (EditText) findViewById(R.id.et_register_pwd_again);
        btnRegister = (Button) findViewById(R.id.btn_now_register);
    }

    private void setListener() {
        btnRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.btn_now_register) {
            register();
        }
    }

    /**
     * register ES
     */
    private void register() {
        CommonUtil.hideSoftKeyBoard(RegisterActivity.this);

        userName = etUserName.getText().toString();
        password = etPassword.getText().toString().trim();
        passwordAgain = etPasswordAgain.getText().toString().trim();
        userEmail = etRegisterEmail.getText().toString().trim();

        if (TextUtils.isEmpty(userName)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordAgain)
                || TextUtils.isEmpty(userEmail)) {
            ToastUtil.showToastL(this, R.string.please_check_input);
        } else {
            if (password.equals(passwordAgain)) {
                progressDialogShow();
                registerLeanCloud();
            } else {
                // twice password unequal.
                ToastUtil.showToastS(RegisterActivity.this,
                        getResources().getString(R.string.twice_pwd_not_equal));
            }
        }
    }

    private void registerLeanCloud() {
        AVUser user = new AVUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(userEmail);

        signUpLeanCloudServer(user);
    }

    /**
     * 注册ES用户到LeanCloud server
     */
    private void signUpLeanCloudServer(AVUser user) {
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                progressDialogDismiss();
                if (e == null) {
                    PreferenceUtils.setPrefString(RegisterActivity.this, ESConstants.USER_NAME,
                        userName);
                    PreferenceUtils.setPrefString(RegisterActivity.this, ESConstants.USER_EMAIL,
                        userEmail);

                    // register success
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    RegisterActivity.this.finish();
                } else {
                    switch (e.getCode()) {
                        case 202:
                            ToastUtil.showToastS(RegisterActivity.this, R.string.error_register_username_repeat);
                            break;
                        case 203:
                            ToastUtil.showToastS(RegisterActivity.this,
                                    R.string.email_has_registered);
                            break;
                        default:
                            ToastUtil.showToastS(RegisterActivity.this, R.string.network_error);
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
                .show(RegisterActivity.this, "", getResources().getString(R.string.please_waiting), true, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
