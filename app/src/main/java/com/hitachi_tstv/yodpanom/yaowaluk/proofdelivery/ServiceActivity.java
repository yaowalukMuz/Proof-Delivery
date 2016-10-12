package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceActivity extends AppCompatActivity {

    //Explicit
    private TextView nameDriverTextView, idDriverTextView;
    private Button jobListButton, closeButton;
    private ListView listView;
    private String[] loginStrings;
    private MyConstant myConstant = new MyConstant();
    private String[] planDateStrings, cntStoreStrings, planIdStrings;
    private Boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);


        //BindWidget
        nameDriverTextView = (TextView) findViewById(R.id.textView2);
        idDriverTextView = (TextView) findViewById(R.id.textView4);
        jobListButton = (Button) findViewById(R.id.button3);
        closeButton = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listJob);


        // Get value from Inten

        loginStrings = getIntent().getStringArrayExtra("Login");

        //Show name
        nameDriverTextView.setText(loginStrings[1]);

        //Syn Data SynDataWhereByDriverID
        SynDataWhereByDriverID synDataWhereByDriverID = new SynDataWhereByDriverID(ServiceActivity.this);
        synDataWhereByDriverID.execute(myConstant.getUrlDataWhereDriverId());


        //Close Controller
        closeButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }   //Main method

    //syn
    private class SynDataWhereByDriverID extends AsyncTask<String, Void, String> {
        // Explicit
        private Context context;

        public SynDataWhereByDriverID(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();


                return response.body().string();
            } catch (Exception e) {
                Log.d("12OctV1", "e-->doInback-->" + e.toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("12OctV1", "JSON--->" + s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                planDateStrings = new String[jsonArray.length()];
                cntStoreStrings = new String[jsonArray.length()];
                planIdStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    planDateStrings[i] = jsonObject.getString("planDate");
                    cntStoreStrings[i] = jsonObject.getString("cnt_store");
                    planIdStrings[i] = jsonObject.getString("planId");
                }// for

                if (aBoolean) {
                    //True : not click on button
                    jobListButton.setText("Job List : " + planDateStrings[0]);

                    CreateDetailList(planIdStrings[0]);


                }// IF

                //Get Event From Click
                jobListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ServiceActivity.this, JobListView.class);
                        intent.putExtra("Date", planDateStrings);
                        intent.putExtra("Store", cntStoreStrings);
                        intent.putExtra("Id", planIdStrings);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                Log.d("12OctV1", "e onPost-->" + e.toString());
            }


        }   //onPost
    }  //SynDataWhereByDriverID

    private void CreateDetailList(String planIdString) {

        SynDetail synDetail = new SynDetail(ServiceActivity.this, planIdString);
        synDetail.execute(myConstant.getUrlDataWhereDriverIdanDate());

    }//Create Detail List

    private class SynDetail extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;
        private String planIdString, planDateString;

        public SynDetail(Context context, String planIdString) {
            this.context = context;
            this.planIdString = planIdString;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("planId", planIdString)
                        .add("driver_id", loginStrings[0])
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                Log.d("12oCt2", "e doInback-->" + e.toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("12OctV2", "JSON-->" + s);

        }
    }   //SynDetail


}   //Main  Class
