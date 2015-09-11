package com.example.lijinfeng.eses.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.activity.MainActivity;
import com.example.lijinfeng.eses.activity.SettingsActivity;

/**
 *
 */
public class MorePopupWindow extends PopupWindow implements OnClickListener {
    
    private LinearLayout meLayout;
    private LinearLayout setLayout;
    private LinearLayout switchThemelayout;

    private View mMenuView;
    private TextView userTextView;
    
    private MainActivity context;
    
    public MorePopupWindow(final MainActivity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater =
            (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_more, null);
        
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.right_top_pop_style);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        
        initView();
    }
    
    private void initView() {
        meLayout = (LinearLayout)mMenuView.findViewById(R.id.meLayout);
        setLayout = (LinearLayout)mMenuView.findViewById(R.id.setLayout);
        switchThemelayout = (LinearLayout) mMenuView.findViewById(R.id.switch_theme);

        userTextView = (TextView)mMenuView.findViewById(R.id.userTV);
        userTextView.setText("关于");
        
        meLayout.setOnClickListener(this);
        setLayout.setOnClickListener(this);
        switchThemelayout.setOnClickListener(this);
        
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.meLayout:
//			context.startActivity(new Intent().setClass(context, AboutActivity.class));
                break;

            case R.id.setLayout:
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivityForResult(intent, 0);
                break;

            case R.id.switch_theme:
//                changeThemeWithColorful();
                break;
            default:
                break;
        }
        dismiss();
	}

}
