package com.bachan.xiaoer;

import android.content.Intent;
import android.view.View;

import com.bachan.xiaoer.base.BaseActivity;
import com.bachan.xiaoer.notepd.ui.NotepadActivity;

public class MainActivity extends BaseActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected int setContentViewRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        findViewById(R.id.mNotepad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotepadActivity.class));
            }
        });

    }
}