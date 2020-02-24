package com.logincore.hackernotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.logincore.hackernotes.bean.NotesBean;
import com.logincore.hackernotes.bean.NotesDbBean;
import com.logincore.hackernotes.dao.DBOpenHlper;
import com.logincore.hackernotes.utils.LogUtils;
import com.logincore.hackernotes.utils.ToastUtils;

import java.util.ArrayList;

public class UtilsDataHelper {

    private final Context mContext;
    private static DBOpenHlper utilsDataHelper;
    private static SQLiteDatabase database = null;
    private static ArrayList<NotesDbBean> adapterData;

    public UtilsDataHelper(Context context){
        this.mContext = context;
        utilsDataHelper = new DBOpenHlper(context, "notes.db", null, 1);
        database = utilsDataHelper.getWritableDatabase();
    }

    public static void updateNotes(String tiem, String contentNotes, String id) {
        LogUtils.i("notes","执行数据库更新操作"+id+contentNotes);
        ContentValues values = new ContentValues();
        values.put("time",tiem);
        values.put("content",contentNotes);
        database.update("notes",values,"id=?",new String[]{id});
    }

    public static void deleteNotes(String item) {
        database.delete("notes","id=?",new String[]{item});
    }

    public static ArrayList<NotesDbBean> queryNotes(String items) {
        LogUtils.i("notes","执行数据库查询操作"+items);
        Cursor cursor = null;
        if (adapterData==null){
            adapterData = new ArrayList<>();
        }else{
            adapterData.clear();
        }
        if (items.isEmpty()){
        cursor = database.query("notes", null, null, null, null, null, null);
        }else{
            cursor = database.query("notes", null, "id=?", new String[]{items}, null, null, null);
        }
        if (cursor.moveToFirst()){
            do {
                NotesDbBean notesDbBean = new NotesDbBean();
                notesDbBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                notesDbBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
                notesDbBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                adapterData.add(notesDbBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = 0; i < adapterData.size(); i++) {
            LogUtils.i("返回查询数据notes","id:"+adapterData.get(i).id+"time:"+adapterData.get(i).time+adapterData.get(i).content);
        }
        return adapterData;
    }

    public static void insert(Context context, String time, String notes) {
        LogUtils.i(context,"执行数据库插入操作"+time+notes);
            ContentValues values = new ContentValues();
            values.put("time",time);
            values.put("content",notes);
            database.insert("notes",null,values);
    }
}
