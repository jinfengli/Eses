package com.example.lijinfeng.eses.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.util.ToastUtil;

/*
 * TODO：找回用户名或密码
 *
 * @date 2015-10-20
 * @author li.jf
 *
 */
public class ForgetPasswordActivity extends AppCompatActivity {

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
        mToolbar.setTitle("找回帐号或密码");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.statusbar_bg));
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
            ToastUtil.showCustomToastL(this, "邮箱不能为空");
            // 这儿最好是再判断一下邮件的格式是否正确
            return;
        }

        AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override public void done(AVException e) {
                // 回调无异常
                if (e == null) {
                    // 邮件发送成功，到邮件收取邮件重新登录
                    ToastUtil.showCustomToastL(ForgetPasswordActivity.this, "邮件发送成功，请检查邮件并重置密码");
                    gotoLoginActivity();
                } else {
                    // 邮件发送异常，请检查您的邮件地址
                    ToastUtil.showCustomToastL(ForgetPasswordActivity.this, "邮件发送失败，请检查邮件地址");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClicker = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.find_pwd:
                    sendFindPwdMail();
                    break;
            }
            return false;
        }
    };

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
