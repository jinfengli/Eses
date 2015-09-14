package com.example.lijinfeng.eses.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * TODO：自定义布局 (避免软键盘遮挡EditText,在AddRecordActivity中使用)
 *
 * @author: li.jf
 * @date 2015-09-11
 */
public class CustomLayout extends RelativeLayout {

    public static final int KEYBOARD_HIDE = 0;
    public static final int KEYBOARD_SHOW = 1;

    public static final int SOFT_KEYBOARD_MIN_HEIGHT = 40;

    private KeyboardStateListener keyboardStateListener;

    public Handler mHandler = new Handler();

    public void setKeyboardStateListener(KeyboardStateListener keyboardStateListener) {
        this.keyboardStateListener = keyboardStateListener;
    }

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, final int h, int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(oldh - h > SOFT_KEYBOARD_MIN_HEIGHT) {
                    keyboardStateListener.setChanged(KEYBOARD_SHOW);
                } else {
                    if(keyboardStateListener != null) {
                        keyboardStateListener.setChanged(KEYBOARD_HIDE);
                    }
                }
            }
        });
    }

    public interface KeyboardStateListener {
        // 键盘状态改变时回调
        public void setChanged(int state);
    }
}
