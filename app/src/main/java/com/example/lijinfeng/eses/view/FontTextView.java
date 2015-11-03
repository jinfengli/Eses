package com.example.lijinfeng.eses.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.example.lijinfeng.eses.R;

/**
 * 使用第三方字体，引入时通过 app:fontType="xxx"    xxx表示你放在asserts下的一个tff文件的文件名
 */
public class FontTextView  extends TextView {
  private String fontType = "DarkistheNight";

  public FontTextView(Context context) {
    super(context);
    init(null, 0);
  }

  public FontTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public FontTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle) {
    final TypedArray array = getContext().obtainStyledAttributes(
        attrs, R.styleable.tvFontStyle, defStyle, 0);

    fontType = array.getString(R.styleable.tvFontStyle_fontType);
    Typeface font = FontManager.getInstance(getContext().getAssets()).getFont("DarkistheNight");
    if (fontType.equals("Marlboro")) {
      font = FontManager.getInstance(getContext().getAssets()).getFont("Marlboro");
    } else if (fontType.equals("TheLastCall-Regular")) {
      font = FontManager.getInstance(getContext().getAssets()).getFont("TheLastCall-Regular");
    }
    setTypeface(font, Typeface.NORMAL);

    array.recycle();
  }
}
