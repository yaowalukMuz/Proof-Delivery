package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class JobListView extends AppCompatActivity {

    //Explicit
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list_view);

        //BindWidget
        listView = (ListView) findViewById(R.id.livShowDate);

        //Create ListView
        final String[] dateStrings = getIntent().getStringArrayExtra("Date");
        String[] storeStrings = getIntent().getStringArrayExtra("Store");
        final String[] planStrings = getIntent().getStringArrayExtra("PlanId");
        final String[] login  = getIntent().getStringArrayExtra("Login");
        DateAdapter dateAdapter = new DateAdapter(JobListView.this, dateStrings, storeStrings, planStrings);
        listView.setAdapter(dateAdapter);


        //onClicklistView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JobListView.this, ServiceActivity.class);
                intent.putExtra("Login", login);
                intent.putExtra("PlanId", planStrings[position]);
                intent.putExtra("Date",dateStrings[position]);
                startActivity(intent);
                finish();
            }
        });


    }   //Main method
}   //Main Class
