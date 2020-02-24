package com.logincore.hackernotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.logincore.hackernotes.adapter.NotesAdapter;
import com.logincore.hackernotes.bean.NotesBean;
import com.logincore.hackernotes.bean.NotesDbBean;
import com.logincore.hackernotes.dao.DBOpenHlper;
import com.logincore.hackernotes.utils.LogUtils;
import com.logincore.hackernotes.utils.SysUtil;
import com.logincore.hackernotes.utils.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private ImageView addNotes;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private NotesAdapter notesAdapter;

    @Override
    protected int getRequestWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    protected void initBeforeData() {
        ArrayList<NotesDbBean> dbBeans = utilsDataHelper.queryNotes("");

        for (int i = 0; i < dbBeans.size(); i++) {
            LogUtils.i(this,"id="+dbBeans.get(i).id+",content="+dbBeans.get(i).content);
        }

        notesAdapter = new NotesAdapter(this,dbBeans);
        listView.setAdapter(notesAdapter);

    }


    @Override
    protected void initView() {
        addNotes = findViewById(R.id.main_add_note);
        listView = findViewById(R.id.lv_show_main);
        addNotes.setOnClickListener(this);

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                new AlertDialog.Builder(MainActivity.this).
//                        setMessage("是否要删除此便签")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                utilsDataHelper.deleteNotes(String.valueOf(i));
//                            }
//                        }).create().show();
//                return true;
//            }
//        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {   //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        //.setTitle("确定要删除此便签？")
                        .setMessage("确定要删除此便签？")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                utilsDataHelper.deleteNotes(String.valueOf(which));
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }

    @Override
    protected int setContentViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_add_note:
                addNewOneNotes();
                break;
        }
    }

    private void addNewOneNotes() {
        //1,创建笔记先跳转到NewNotesActivity
        Intent intent = new Intent(this, NewNoteActivity.class);
        ToastUtils.show(this,"创建一条笔记");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        intent.putExtra("time",time);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i(this,"onStart执行");
        ArrayList<NotesDbBean> dbBeans = utilsDataHelper.queryNotes("");
        notesAdapter.setAdapterData(dbBeans);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LogUtils.i(this,"onResume执行");
    }

    //
//    private void initListener() {
//        saveNotes.setOnClickListener(this);
//        deleteNotes.setOnClickListener(this);
//        btn_load_main.setOnClickListener(this);
//        btn_update_main.setOnClickListener(this);
//        adapterData = new ArrayList<>();
//        utilsDataHelper = new UtilsDataHelper(this);
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
//        sdf.applyPattern("yyyyMMddHHmmssSSS");// a为am/pm的标记
//        Date date = new Date();// 获取当前时间
//        System.out.println("现在时间：" +  sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
//    }
//
//    private void initViews() {
//        et_write_main = findViewById(R.id.et_write_main);
//        et_updateid_main = findViewById(R.id.et_updateid_main);
//        et_deleteid_main = findViewById(R.id.et_deleteid_main);
//        saveNotes = findViewById(R.id.btn_save_main);
//        btn_load_main = findViewById(R.id.btn_load_main);
//        deleteNotes = findViewById(R.id.btn_delete_main);
//        btn_update_main = findViewById(R.id.btn_update_main);
//        listView = findViewById(R.id.lv_show_main);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_save_main:
//                String notes = et_write_main.getText().toString();
//                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(et_write_main.getWindowToken(), 0);
//                saveNote(notes);
//                loadData();
//                break;
//            case R.id.btn_load_main:
//                loadData();
//                break;
//            case R.id.btn_update_main:
//                String s = et_updateid_main.getText().toString();
//                updateNotes(s);
//                break;
//            case R.id.btn_delete_main:
//                String s1 = et_deleteid_main.getText().toString();
//                deleteNotes(s1);
//                break;
//        }
//    }
//
//    private void updateNotes(String id) {
//        //去数据库里查询该条笔记数据
//        //将查询出来的笔记展示到修改笔记目录里
//        //修改后重新保存
//        utilsDataHelper.updateNotes("content","name",id);
//        loadData();
//    }
//
//    private void deleteNotes(String item) {
//        ToastUtils.show(this,item);
//        utilsDataHelper.deleteNotes(item);
//        loadData();
//    }
//
//    private void loadData() {
//        if (adapterData!=null){
//            adapterData.clear();
//        }
//        LogUtils.i(this,"开始加载查询数据");
//        ArrayList<String> queryNotes = utilsDataHelper.queryNotes("11", adapterData);
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterData);
//        listView.setAdapter(stringArrayAdapter);
//    }
//
//    private void saveNote(String notes) {
//        LogUtils.i(this,notes);
//        utilsDataHelper.insert(this,notes);
//    }
}
