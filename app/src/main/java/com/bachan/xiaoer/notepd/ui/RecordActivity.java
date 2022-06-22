package com.bachan.xiaoer.notepd.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.bachan.xiaoer.MainActivity;
import com.bachan.xiaoer.R;
import com.bachan.xiaoer.base.BaseActivity;
import com.bachan.xiaoer.notepd.database.SQLiteHelper;
import com.bachan.xiaoer.utils.CalendarUtils;
import com.bachan.xiaoer.utils.DBUtils;
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

public class RecordActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = RecordActivity.class.getSimpleName();
    private EditText mTitle;
    private EditText content;
    private ImageView delete;
    private ImageView note_save;
    private SQLiteHelper mSQLiteHelper;
    private String id;

    @Override
    protected int setContentViewRes() {
        return R.layout.activity_record;
    }

    @Override
    protected void initData() {
        mTitle = (EditText) findViewById(R.id.mTitle);
        content = (EditText) findViewById(R.id.note_content);
        delete = (ImageView) findViewById(R.id.delete);
        note_save = (ImageView) findViewById(R.id.note_save);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);

        mSQLiteHelper = new SQLiteHelper(this);
        titleBar.setTitle("添加记录");
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            if (id != null) {
                titleBar.setTitle("修改记录");
                content.setText(intent.getStringExtra("content"));
                mTitle.setText(intent.getStringExtra("title"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                content.setText("");
                break;
            case R.id.note_save:
                String noteContent = content.getText().toString().trim();
                String titleContent = mTitle.getText().toString().trim();
                if (TextUtils.isEmpty(titleContent)) {
                    showToast("标题不能为空");
                    break;
                }

                if (TextUtils.isEmpty(noteContent)) {
                    showToast("修改内容不能为空");
                    break;
                }

                if (id != null) {//修改操作
                    if (mSQLiteHelper.updateData(id, titleContent, noteContent, DBUtils.getTime())) {
                        showToast("修改成功");
                        updateCalendar(titleContent, noteContent);
                    } else {
                        showToast("修改失败");
                    }
                } else {
                    //向数据库中添加数据
                    if (mSQLiteHelper.insertData(titleContent, noteContent, DBUtils.getTime())) {
                        showToast("保存成功");
                        updateCalendar(titleContent, noteContent);
                    } else {
                        showToast("保存失败");
                    }
                }
                break;
        }
    }

    private void updateCalendar(String titleContent, String noteContent) {
        AlertDialog dialog = new AlertDialog.Builder(RecordActivity.this).create();
        dialog.setTitle("提示");
        dialog.setMessage("是否添加到日历？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框
                DatimePicker picker = new DatimePicker(RecordActivity.this);
                final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
                picker.setOnDatimePickedListener(new OnDatimePickedListener() {
                    @Override
                    public void onDatimePicked(int year, int month, int day, int hour, int minute, int second) {
                        String text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        long times = TimeUtil.dateToMs(text, "yyyy-MM-dd HH:mm:ss");//"2020-01-13 17:26:10"

                        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
                        permissionItems.add(new PermissionItem(Manifest.permission.READ_CALENDAR, "读取日历", R.drawable.permission_ic_calendar));
                        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_CALENDAR, "写入日历", R.drawable.permission_ic_calendar));

                        HiPermission.create(RecordActivity.this)
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
                                        boolean result = CalendarUtils.insertCalendarEvent(RecordActivity.this, titleContent, noteContent, times, 0);
                                        if (result) {
                                            showToast("添加成功");
                                            setResult(2);
                                            finish();
                                        } else {
                                            showToast("添加失败");
                                        }
                                        Log.d(TAG, "onDatimePicked: " + times + ";result:" + result);
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
                wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
                wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
                wheelLayout.setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(10));
                wheelLayout.setDateLabel("年", "月", "日");
                wheelLayout.setTimeLabel("时", "分", "秒");
                picker.show();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.show();//显示对话框
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

    }

    public void showToast(String message) {
        Toast.makeText(RecordActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}