package com.bachan.xiaoer.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bachan.xiaoer.R;
import com.bachan.xiaoer.utils.StatusBarUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

public abstract class BaseActivity extends AppCompatActivity {

    protected TitleBar titleBar;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewRes());
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

        initTitle();

        initData();
    }

    /**
     *     <com.hjq.bar.TitleBar
     *         android:id="@+id/tb_main_bar"
     *         android:layout_width="wrap_content"
     *         android:layout_height="wrap_content"
     *         android:layout_marginTop="20dp"
     *         app:leftIcon="@mipmap/ic_launcher"
     *         app:leftTitle="左边"
     *         app:rightIcon="@mipmap/ic_launcher"
     *         app:rightTitle="右边"
     *         app:title="监听标题栏点击事件" />
     */
    private void initTitle() {
        titleBar = findViewById(R.id.tb_main_bar);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(TitleBar titleBar) {
//                ToastUtils.show("左项 View 被点击");
                finish();
            }

            @Override
            public void onTitleClick(TitleBar titleBar) {
//                ToastUtils.show("中间 View 被点击");
            }

            @Override
            public void onRightClick(TitleBar titleBar) {
//                ToastUtils.show("右项 View 被点击");
            }
        });
    }

    protected abstract int setContentViewRes();
    protected abstract void initData();
}
