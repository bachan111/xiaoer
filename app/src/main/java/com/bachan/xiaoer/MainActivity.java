package com.bachan.xiaoer;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.bachan.xiaoer.base.BaseActivity;
import com.bachan.xiaoer.notepd.ui.NotepadActivity;
import com.bachan.xiaoer.utils.CalendarUtils;
import com.bachan.xiaoer.utils.TimeUtil;
import com.github.gzuliyujiang.wheelpicker.DatimePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatimePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.widget.DatimeWheelLayout;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

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

        findViewById(R.id.mTimePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatimePicker picker = new DatimePicker(MainActivity.this);
                final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
                picker.setOnDatimePickedListener(new OnDatimePickedListener() {
                    @Override
                    public void onDatimePicked(int year, int month, int day, int hour, int minute, int second) {
                        String text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
//                        text += wheelLayout.getTimeWheelLayout().isAnteMeridiem() ? " 上午" : " 下午";
                        long times = TimeUtil.dateToMs(text, "yyyy-MM-dd HH:mm:ss");//"2020-01-13 17:26:10"
                        boolean result = CalendarUtils.insertCalendarEvent(MainActivity.this, "测试11", "测试内容sdasdasdasdasd", times, 0);
                        Log.d(TAG, "onDatimePicked: " + times + ";result:" + result);

                    }
                });
                wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
                wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
                wheelLayout.setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(10));
                wheelLayout.setDateLabel("年", "月", "日");
                wheelLayout.setTimeLabel("时", "分", "秒");
                picker.show();
            }
        });

        findViewById(R.id.mAddDataCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
                permissionItems.add(new PermissionItem(Manifest.permission.READ_CALENDAR, "读取日历", R.drawable.permission_ic_calendar));
                permissionItems.add(new PermissionItem(Manifest.permission.WRITE_CALENDAR, "写入日历", R.drawable.permission_ic_calendar));

                HiPermission.create(MainActivity.this)
                        .title("权限申请")
                        .permissions(permissionItems)
                        .filterColor(ResourcesCompat.getColor(getResources(), R.color.common_yellow, getTheme()))//图标的颜色
                        .msg("为了您能正常使用该程序，需要申请以下权限，请点击下一步，再允许各项权限。\n\n")
                        .style(R.style.PermissionDefaultBlueStyle)//设置主题
                        .checkMutiPermission(new PermissionCallback() {
                            @Override
                            public void onClose() {

                            }

                            @Override
                            public void onFinish() {
                                Toast.makeText(MainActivity.this, "权限申请完毕", Toast.LENGTH_SHORT).show();
                                CalendarUtils.insertCalendarEvent(MainActivity.this, "测试", "测试内容", System.currentTimeMillis(), System.currentTimeMillis() + 30 * 60 * 1000);
                            }

                            @Override
                            public void onDeny(String permission, int position) {

                            }

                            @Override
                            public void onGuarantee(String permission, int position) {

                            }
                        });
            }
        });
    }
}