package com.semirus.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

// ====== SQLite DB ======
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "taskDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // autoincrement = automatically increment tID
        db.execSQL("CREATE TABLE taskInfo (tID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT, task TEXT, del TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS taskInfo");
        onCreate(db);
    }
    // SHOW task list at first time when user starts app
    public ArrayList showTodayTaskList(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM taskInfo WHERE del is NULL AND date=date('now') ORDER BY DATETIME(date);", null);
        while(cursor.moveToNext()){
            //int tid = cursor.getInt(cursor.getColumnIndex("tID"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String toDoList = cursor.getString(cursor.getColumnIndex("task"));
            //String del = cursor.getString(cursor.getColumnIndex("del"));
            list.add(toDoList + '\n' + date);
            //list.add(toDoList);
        }
        cursor.close();
        db.close();
        return list;
    }

    // get tid
    public int getTid(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        int result = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM taskInfo WHERE del is NULL AND date=date('now') ORDER BY DATETIME(date);", null);
        while(cursor.moveToNext()){
            int tid = cursor.getInt(cursor.getColumnIndex("tID"));
            result = tid;
        }
        cursor.close();
        db.close();
        return result;
    }

    // SHOW task list of tmrw
    public ArrayList showTmrwTaskList(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList list = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM taskInfo WHERE del is NULL AND date=date('now', '+1 day') ORDER BY DATETIME(date);", null);
        while(cursor.moveToNext()){
            int tid = cursor.getInt(cursor.getColumnIndex("tID"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String toDoList = cursor.getString(cursor.getColumnIndex("task"));
            String del = cursor.getString(cursor.getColumnIndex("del"));
            list.add(toDoList + '\n' + date);
            //list.add(toDoList);
        }
        cursor.close();
        db.close();
        return list;
    }
}
