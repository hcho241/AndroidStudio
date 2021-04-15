package com.semirus.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import static java.sql.Types.INTEGER;

public class MainActivity extends AppCompatActivity {
    DBHelper myHelper;
    SQLiteDatabase sqlDB;
    TextView current;
    EditText task;
    Button addBtn, delBtn, swipeBtn;
    ListView taskList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long now = System.currentTimeMillis();
    String today = dateFormat.format(new Date(now));
    //int tid;
    String date, toDoList, del, sql, text;
    //ArrayList list = new ArrayList();
    //Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = (TextView)findViewById(R.id.current);
        task = (EditText)findViewById(R.id.task);
        addBtn = (Button)findViewById(R.id.addBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
        swipeBtn = (Button)findViewById(R.id.swipeBtn);
        taskList = (ListView)findViewById(R.id.taskList);
        myHelper = new DBHelper(this);
        // show tasks list
        ArrayList list = myHelper.showTodayTaskList();
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list);
        taskList.setAdapter(adapter);
        // show today's date
        current.setText(current.getText() + " " + today);
        // show tasks list of today
        //showTaskList();
        Parcelable state = taskList.onSaveInstanceState();
        int tid = myHelper.getTid();
        // add button
        addBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                text = task.getText().toString();
                sql = "INSERT INTO taskInfo(date, task) VALUES (date('now'), '"  + text + "');";
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL(sql);
                sqlDB.close();
                list.clear();
                //showTaskList();

                list.addAll(myHelper.showTodayTaskList());
                Toast.makeText(getApplicationContext(), "ADDED", 0).show();
                adapter.notifyDataSetChanged();
                taskList.invalidateViews();
                taskList.onRestoreInstanceState(state);
                //taskList.refreshDrawableState();
            }
        });
        // delete button
        delBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //delBtn.setTag(position);
                //taskList.onRestoreInstanceState(state);
                sql = "UPDATE taskInfo SET del='del' WHERE tID=" + tid + ";";
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL(sql);
                sqlDB.close();
                list.clear();
                //showTaskList();
                //taskList.onRestoreInstanceState(state);
                list.addAll(myHelper.showTodayTaskList());
                Toast.makeText(getApplicationContext(), "DELETED", 0).show();
                adapter.notifyDataSetChanged();
                taskList.invalidateViews();
                taskList.onRestoreInstanceState(state);
                //taskList.refreshDrawableState();
            }
        });
        // swipe button
        swipeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sql = "UPDATE taskInfo SET date=date('now', '+1 day') WHERE tID=" + tid + ";";
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL(sql);
                sqlDB.close();
                taskList.onRestoreInstanceState(state);
                Intent intent = new Intent(getApplicationContext(), TomorrowListView.class);
                startActivity(intent);
            }
        });
        // selected item of taskList change its color
        taskList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long t = taskList.getItemIdAtPosition(position) + 1;
                //String s = String.valueOf(t);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                sql = "UPDATE taskInfo SET del='del' WHERE tID=" + t + ";";
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL(sql);
                sqlDB.close();
                view.setBackgroundColor(Color.YELLOW);
                taskList.onRestoreInstanceState(state);
            }
        });
    }
    // SHOW task list
    /*
    public void showTaskList(){
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM taskInfo WHERE del is NULL AND date=date('now') ORDER BY DATETIME(date);", null);
        while(cursor.moveToNext()){
            //tid = cursor.getInt(cursor.getColumnIndex("tID"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            toDoList = cursor.getString(cursor.getColumnIndex("task"));
            //del = cursor.getString(cursor.getColumnIndex("del"));
            list.add(toDoList + '\n' + date);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        taskList.setAdapter(adapter);
        state = taskList.onSaveInstanceState();
        cursor.close();
        sqlDB.close();
    }

     */
}
