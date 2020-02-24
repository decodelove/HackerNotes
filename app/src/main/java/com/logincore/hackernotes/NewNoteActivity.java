package com.logincore.hackernotes;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.logincore.hackernotes.bean.NotesDbBean;
import com.logincore.hackernotes.utils.LogUtils;
import com.logincore.hackernotes.utils.ToastUtils;
import java.util.ArrayList;

public class NewNoteActivity extends BaseActivity {

    private EditText etNotes;
    private TextView tvTime;
    private int items;
    private String time;

    @Override
    protected int getRequestWindowFeature() {
        return 0;
    }

    @Override
    protected void initBeforeData() {
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        LogUtils.i(this,"time============="+time);
        if (time!=null){
            tvTime.setText(time);
        }else{
            items = intent.getIntExtra("items", 0);
            ArrayList<NotesDbBean> notesDbBeans = utilsDataHelper.queryNotes(String.valueOf(items));
            for (int i = 0; i < notesDbBeans.size(); i++) {
                tvTime.setText(notesDbBeans.get(i).getTime());
                etNotes.setText(notesDbBeans.get(i).getContent());
            }
        }
    }

    @Override
    protected void initView() {
        etNotes = findViewById(R.id.et_content_newnotesactivity);
        tvTime = findViewById(R.id.tv_time_newnotesactivity);
    }

    @Override
    protected int setContentViewLayout() {
        return R.layout.newnoteactivity_notes;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        saveNotes();
        return super.onKeyDown(keyCode, event);
    }

    private void saveNotes() {
        if (!etNotes.getText().toString().trim().isEmpty()){
            String notes = etNotes.getText().toString();
            //如果是修改笔记执行updateNotes
            if (time!=null){//如果是新增的insert
                LogUtils.i(this,"返回了 插入操作"+etNotes.getText().toString()+"time=========="+time);
                utilsDataHelper.insert(this,time,notes);
            }else {
                LogUtils.i(this,"返回了 更新操作"+etNotes.getText().toString()+"time=========="+time);
                utilsDataHelper.updateNotes(time,notes,String.valueOf(items));
            }
        }
    }
}
