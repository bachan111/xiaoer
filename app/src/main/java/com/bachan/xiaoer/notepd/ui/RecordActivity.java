package com.bachan.xiaoer.notepd.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bachan.xiaoer.R;
import com.bachan.xiaoer.base.BaseActivity;
import com.bachan.xiaoer.notepd.database.SQLiteHelper;
import com.bachan.xiaoer.utils.DBUtils;

public class RecordActivity extends BaseActivity implements View.OnClickListener{

    private TextView note_time;
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
        note_time = (TextView)findViewById(R.id.tv_time);
        content = (EditText) findViewById(R.id.note_content);
        delete = (ImageView) findViewById(R.id.delete);
        note_save = (ImageView) findViewById(R.id.note_save);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);

        mSQLiteHelper = new SQLiteHelper(this);
        titleBar.setTitle("添加记录");
        Intent intent = getIntent();
        if(intent!= null){
            id = intent.getStringExtra("id");
            if (id != null){
                titleBar.setTitle("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
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
                String noteContent=content.getText().toString().trim();
                if (id != null){//修改操作
                    if (noteContent.length()>0){
                        if (mSQLiteHelper.updateData(id, noteContent, DBUtils.getTime())){
                            showToast("修改成功");
                            setResult(2);
                            finish();
                        }else {
                            showToast("修改失败");
                        }
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }else {
                    //向数据库中添加数据
                    if (noteContent.length()>0){
                        if (mSQLiteHelper.insertData(noteContent, DBUtils.getTime())){
                            showToast("保存成功");
                            setResult(2);
                            finish();
                        }else {
                            showToast("保存失败");
                        }
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }
                break;
        }
    }
    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}