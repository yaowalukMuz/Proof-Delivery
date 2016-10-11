package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    //Explicit
    public EditText userEditText, passwordEditText;
    private Button button;
    private String userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);


        // Button controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Value From Edit text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check space
                if (userString.equals("")||passwordString.equals("")) {
                    //Have Space
                    MyAlert myAlert = new MyAlert(MainActivity.this);
                    myAlert.myErrorDialog(new MyConstant().getIconAnInt(),
                                          new MyConstant().getTitleHaveSpaceString(),
                                          new MyConstant().getMessageHaveeSpaceString());

                } else {
                    // No Space
                    SynUser synUser = new SynUser(MainActivity.this);
                    MyConstant  myConstant = new MyConstant();
                    synUser.execute(myConstant.getUrlUserString());


                }


            }//Onclick
        });


    }   //Main method

    //Create Inner Class
    private class SynUser extends AsyncTask<String, Void, String> {

        private Context context;

        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).build();
                Response response = okHttpClient.newCall(request).execute();


                return response.body().string();
            } catch (Exception e) {
                Log.d("11octV1", "e doInback-->" + e.toString());
                return null;
            }

        }   // doInback;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("11octV1", "JSON---->" + s);
        }   //onPost

    }   //SynUser Class




}   //Main Class
