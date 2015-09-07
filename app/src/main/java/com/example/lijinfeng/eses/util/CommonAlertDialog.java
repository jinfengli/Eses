package com.example.lijinfeng.eses.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;


public class CommonAlertDialog extends Dialog implements OnClickListener{


    public interface OnSubmitListener{
        //确定
        public void ok();
    }

    public interface OnConfirmExitListener {
        // APP退出时确认
        public void confirm();
    }

    private TextView titleTextView;
    private TextView alertTextView;
    private Button cancelButton,okButton;
    private View viewBelowTitle;
    protected OnSubmitListener onSubmitListener = null;
    protected OnConfirmExitListener onConfirmExitListener = null;

    protected Context context;
    private String title;
    private String msg;

    public CommonAlertDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_common_alert);
        titleTextView = (TextView) findViewById(R.id.titleBar);
        viewBelowTitle = findViewById(R.id.view_line_below_title);
        alertTextView = (TextView) findViewById(R.id.et_dialog_msg);
        cancelButton = (Button) findViewById(R.id.cancelBtn);
        okButton = (Button) findViewById(R.id.okBtn);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }


    public void setOnConfirmExitListener(OnConfirmExitListener onConfirmExitListener) {
        this.onConfirmExitListener = onConfirmExitListener;
    }


    /**
     * 设置Dialog的标题及下面的一条线是否可见
     */
    public void setTitleVisibityGone() {
        if(titleTextView != null) {
            // 这个地方竟然偶尔会报空指针，我对这个textView进行了判null
            titleTextView.setVisibility(View.GONE);
        }
        viewBelowTitle.setVisibility(View.INVISIBLE);
    }

    public void setMeaage(String msg){
        alertTextView.setText(msg + "");
    }
    public void setMessage(int id){
        alertTextView.setText(context.getResources().getString(id));
    }

    public void setComfirmText(int id){
        okButton.setText(context.getResources().getString(id));
    }

    public void setCancelText(int id){
        cancelButton.setText(context.getResources().getString(id));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            if (onSubmitListener != null) {
                onSubmitListener.ok();
            }

            if (onConfirmExitListener != null) {
                onConfirmExitListener.confirm();
            }
            dismiss();
        } else if (v.getId() == R.id.cancelBtn) {
            dismiss();
        }
    }

    @Override
    public void show() {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            super.show();
        }
    }
}