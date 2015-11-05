package com.example.lijinfeng.eses.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.lijinfeng.eses.R;
import com.example.lijinfeng.eses.db.EsesDBHelper;
import com.example.lijinfeng.eses.util.CommonUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 *  TODO: 图表统计记录
 *
 *  @Date: 15-9-1 下午11:26
 *  Copyright (C) li.jf All rights reserved.
 */
public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener, View.OnClickListener {
    private static final String TAG = ChartActivity.class.getSimpleName();

    private Toolbar toolbar;
    /** 饼状图展示 */
    private PieChart mChart;
    private Typeface tf;
    private String[] mParties /*= new String[] {"8","6","6","7","8","9","12","10","6","5","6","4"}*/;
    private String[] keys = new String[10];
    private int [] values = new int[10];

    /** 睡眠时间差列表 */
    private ArrayList<String> timeDiffs;
    /** 数据库操作工具类 */
    private EsesDBHelper dbHelper;

//    Map<String, Integer> map = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initTitleView();
        init();
        initChartView();

    }

    private void init() {
        dbHelper = new EsesDBHelper(this);
        timeDiffs = new ArrayList<String>();
        timeDiffs = dbHelper.getTimeDiffs();
        Log.d(TAG, "timeDiffs ========================"+timeDiffs);
        mParties = getStringTimeDiffs();
    }

    public  String[] getStringTimeDiffs(){
        String []s = new String [timeDiffs.size()];
        for (int i=0; i < timeDiffs.size(); i++) {
            if(timeDiffs.get(i).contains("h")) {
                s[i] = timeDiffs.get(i).split("h")[0];
            } else if(timeDiffs.get(i).contains("mins")  && !timeDiffs.get(i).contains("h")) {
                s[i] = "0";
            }
        }
        return s;
    }

    protected void initTitleView() {
        CommonUtil.configToolBarParams(ChartActivity.this);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("图表");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
        setSupportActionBar(toolbar);
    }

    private void initChartView() {
        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setDragDecelerationFrictionCoef(0.65f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        mChart.setCenterText("睡眠时长\n 统计");
        setData(getTimeMethod(mParties), 100);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setXEntrySpace(10.0f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(10f);
    }

    private int getTimeMethod(String[] mParties) {
        int i = 0;
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (String str : mParties) {
            Integer num = map.get(str);
            num = (null == num) ? 1 : num + 1;
            map.put(str, num);
        }

        Set<java.util.Map.Entry<String,Integer>> entrySet = map.entrySet();
        Iterator<java.util.Map.Entry<String,Integer>> it = entrySet.iterator();

        while (it.hasNext()) {
            java.util.Map.Entry<String, Integer> entry = it.next();

            Log.e(TAG, entry.getKey() + " --- " + entry.getValue());

            keys[i] = entry.getKey();
            switch (Integer.valueOf(entry.getKey())) {
                case 0:
                    keys[i] = "< 1h";
                    break;
                case 1:
                    keys[i] = "1~2h";
                    break;
                case 2:
                    keys[i] = "2~3h";
                    break;
                case 3:
                    keys[i] = "3~4h";
                    break;
                case 4:
                    keys[i] = "4~5h";
                    break;
                case 5:
                    keys[i] = "5~6h";
                    break;
                case 6:
                    keys[i] = "6~7h";
                    break;
                case 7:
                    keys[i] = "7~8h";
                    break;
                case 8:
                    keys[i] = "8~9h";
                    break;
                case 9:
                    keys[i] = "9~10h";
                    break;
                case 10:
                    keys[i] = "10~11h";
                    break;
                case 11:
                    keys[i] = "11~12h";
                    break;
                default:
                    keys[i] = ">12h";
                    break;
            }
            values[i] = entry.getValue();
            i++;
        }

        return i;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal()
                        + ", xIndex: "
                        + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(int count, float range) {
        float mult = range;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
//        }


        for (int i = 0; i < count; i++) {
            yVals1.add(new Entry((float) values[i] / mParties.length, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
//            xVals.add(mParties[i % mParties.length]);
            xVals.add(keys[i]);
        }

        //PieDataSet dataSet = new PieDataSet(yVals1, "Result");
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        // 点击后扇形圆饼放大的程度
        dataSet.setSelectionShift(5.0f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(true);
        data.setValueTextSize(12.0f);
        data.setValueTextColor(Color.BLUE);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }



    @Override
    public void onClick(View view) {

    }




}