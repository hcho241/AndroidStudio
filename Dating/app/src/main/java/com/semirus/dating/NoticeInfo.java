package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeInfo extends Notice{
    TextView titleTxtView, dateTxtView, writerTxtView, contentsTxtView;
    Button backBtn;
    String titleTxt, dateTxt, writerTxt, contentsTxt = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_info);
        // text view
        titleTxtView = (TextView)findViewById(R.id.titleTxtView);
        dateTxtView = (TextView)findViewById(R.id.dateTxtView);
        writerTxtView = (TextView)findViewById(R.id.writerTxtView);
        contentsTxtView = (TextView)findViewById(R.id.contentsTxtView);
        // button
        backBtn = (Button)findViewById(R.id.backBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        selectedTitle = extra.getString("selectedTitle");
        selectedDate = extra.getString("selectedDate");
        selectedWriter = extra.getString("selectedWriter");
        selectedContents = extra.getString("selectedContents");
        System.out.println("Notice info bundle values : " + selectedTitle + " " + selectedDate + " " + selectedWriter + " " + selectedContents);
        // set textview's text
        titleTxt = titleTxtView.getText().toString() + selectedTitle;
        titleTxtView.setText(titleTxt);
        dateTxt = dateTxtView.getText().toString() + selectedDate;
        dateTxtView.setText(dateTxt);
        writerTxt = writerTxtView.getText().toString() + selectedWriter;
        writerTxtView.setText(writerTxt);
        contentsTxt = contentsTxtView.getText().toString() + selectedContents;
        contentsTxtView.setText(contentsTxt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                startActivity(intent);
            }
        });
    }
}
