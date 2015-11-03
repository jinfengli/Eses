package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.constants.ESConstants;
import com.example.lijinfeng.eses.util.ToastUtil;
import java.util.regex.Pattern;

/*
 * TODO：find account or password
 *
 * @date: 2015-10-20
 * @author: li.jf
 */
public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private EditText etForgetPwdEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initTitleView();
        initView();
    }

    protected void initTitleView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mToolbar.setTitle(R.string.find_accout_or_pwd);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClicker);
    }

    protected void initView() {
        etForgetPwdEmail = (EditText) findViewById(R.id.et_forget_pwd_email);
    }

    /**
     * 发送找回密码邮件
     */
    private void sendFindPwdMail() {
        String email = etForgetPwdEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showCustomToastL(this, R.string.email_should_not_null);
            return;
        } else {
            // email不为空，检验一下格式是否正确
            Pattern regex = Pattern.compile(ESConstants.EMAIL_REGEX);
            if(!regex.matcher(email).matches()) {
                ToastUtil.showCustomToastL(this, R.string.email_format_error);
                return;
            }
        }

        requestLeanCloudPwdReset(email);
    }

    /**
     * send reset password email
     * @param email
     */
    private void requestLeanCloudPwdReset(String email) {
        AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override public void done(AVException e) {
                // 回调无异常
                if (e == null) {
                    // 邮件发送成功，到邮件收取邮件重新登录
                    ToastUtil.showCustomToastS(ForgetPasswordActivity.this, R.string.send_email_success);
                    gotoLoginActivity();
                } else {
                    // 邮件发送异常，请检查您的邮件地址
                    Log.e(TAG, "send email fail:" + e.getMessage().toString());
                    ToastUtil.showCustomToastS(ForgetPasswordActivity.this, R.string.send_email_fail);
                }
            }
        });
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClicker = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.find_pwd:
                    sendFindPwdMail();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
