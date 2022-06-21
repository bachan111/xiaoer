package com.bachan.xiaoer;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.hjq.bar.TitleBar;
import com.hjq.bar.style.LightBarStyle;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 TitleBar 默认样式
        TitleBar.setDefaultStyle(new LightBarStyle() {

            @Override
            public TextView newTitleView(Context context) {
                return new AppCompatTextView(context);
            }

            @Override
            public TextView newLeftView(Context context) {
                return new AppCompatTextView(context);
            }

            @Override
            public TextView newRightView(Context context) {
                return new AppCompatTextView(context);
            }
        });
    }
}
