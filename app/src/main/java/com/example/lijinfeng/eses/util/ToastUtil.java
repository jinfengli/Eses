package com.example.lijinfeng.eses.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijinfeng.eses.R;

/**
 * TODO：Toast 工具类
 *
 * @author: li.jf
 * @date: 2015/9/11 15:20
 */
public class ToastUtil {

    public static void toastLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(Context context, String resContent) {
        Toast.makeText(context, resContent, Toast.LENGTH_LONG).show();
    }


    public static void toastShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String resContent) {
        Toast.makeText(context, resContent, Toast.LENGTH_SHORT).show();
    }


//    private void showToast3(){
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.customtoast,
//                (ViewGroup) findViewById(R.id.llToast));
//        ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
//        image.setImageResource(R.drawable.page);
//        TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
//        title.setText("Attention");
//        TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
//        text.setText("Hello, This is Andy!");
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }

}


