package com.example.lijinfeng.eses.view;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.Map;


public class FontManager {
  private static FontManager instance;
  private AssetManager assetManager;
  private Map<String, Typeface> fonts;

  private FontManager(AssetManager assetManager) {
    this.assetManager = assetManager;
    fonts = new HashMap<String, Typeface>();
  }

  public static FontManager getInstance(AssetManager assetManager) {
    if (instance == null) {
      instance = new FontManager(assetManager);
    }
    return instance;
  }

  public Typeface getFont(String asset) {
    if (fonts.containsKey(asset)) return fonts.get(asset);

    String path = "fonts/" + asset + ".ttf";
    Typeface font = Typeface.createFromAsset(assetManager, path);
    fonts.put(asset, font);

    return font;
  }
}
