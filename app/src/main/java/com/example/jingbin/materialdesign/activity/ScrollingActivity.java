package com.example.jingbin.materialdesign.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.jingbin.materialdesign.R;
import com.example.jingbin.materialdesign.main.utils.SnackbarUtil;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getDataFromBrowser();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 从deep link中获取数据
     */
    private void getDataFromBrowser() {
        Uri data = getIntent().getData();
        try {
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            // 从网页传过来的数据
            String testId = params.get(0);
            String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "params: " + testId;
            Log.e("ScrollingActivity", text);
            SnackbarUtil.show(this, testId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
