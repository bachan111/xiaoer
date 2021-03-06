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
        titleBar.setTitle("????????????");
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            if (id != null) {
                titleBar.setTitle("????????????");
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
                    showToast("??????????????????");
                    break;
                }

                if (TextUtils.isEmpty(noteContent)) {
                    showToast("????????????????????????");
                    break;
                }

                if (id != null) {//????????????
                    if (mSQLiteHelper.updateData(id, titleContent, noteContent, DBUtils.getTime())) {
                        showToast("????????????");
                        updateCalendar(titleContent, noteContent);
                    } else {
                        showToast("????????????");
                    }
                } else {
                    //???????????????????????????
                    if (mSQLiteHelper.insertData(titleContent, noteContent, DBUtils.getTime())) {
                        showToast("????????????");
                        updateCalendar(titleContent, noteContent);
                    } else {
                        showToast("????????????");
                    }
                }
                break;
        }
    }

    private void updateCalendar(String titleContent, String noteContent) {
        AlertDialog dialog = new AlertDialog.Builder(RecordActivity.this).create();
        dialog.setTitle("??????");
        dialog.setMessage("????????????????????????");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//???????????????
                DatimePicker picker = new DatimePicker(RecordActivity.this);
                final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
                picker.setOnDatimePickedListener(new OnDatimePickedListener() {
                    @Override
                    public void onDatimePicked(int year, int month, int day, int hour, int minute, int second) {
                        String text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        long times = TimeUtil.dateToMs(text, "yyyy-MM-dd HH:mm:ss");//"2020-01-13 17:26:10"

                        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
                        permissionItems.add(new PermissionItem(Manifest.permission.READ_CALENDAR, "????????????", R.drawable.permission_ic_calendar));
                        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_CALENDAR, "????????????", R.drawable.permission_ic_calendar));

                        HiPermission.create(RecordActivity.this)
                                .title("????????????")
                                .permissions(permissionItems)
                                .filterColor(ResourcesCompat.getColor(getResources(), R.color.common_yellow, getTheme()))//???????????????
                                .msg("????????????????????????????????????????????????????????????????????????????????????????????????????????????\n\n")
                                .style(R.style.PermissionDefaultBlueStyle)//????????????
                                .checkMutiPermission(new PermissionCallback() {
                                    @Override
                                    public void onClose() {

                                    }

                                    @Override
                                    public void onFinish() {
                                        boolean result = CalendarUtils.insertCalendarEvent(RecordActivity.this, titleContent, noteContent, times, 0);
                                        if (result) {
                                            showToast("????????????");
                                            setResult(2);
                                            finish();
                                        } else {
                                            showToast("????????????");
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
                wheelLayout.setDateLabel("???", "???", "???");
                wheelLayout.setTimeLabel("???", "???", "???");
                picker.show();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//???????????????
            }
        });
        dialog.show();//???????????????
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

    }

    public void showToast(String message) {
        Toast.makeText(RecordActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}