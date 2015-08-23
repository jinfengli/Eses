package com.example.lijinfeng.eses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

/**
 * MainActivity
 */
public class MainActivity extends Activity implements View.OnClickListener{

    private FloatingActionButton fabMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    private void setListener() {
        fabMenu.setOnClickListener(this);
    }

    protected void initView() {
        fabMenu = (FloatingActionButton) findViewById(R.id.fab);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivity(new Intent(this,AboutActivity.class));
        }
    }
}
