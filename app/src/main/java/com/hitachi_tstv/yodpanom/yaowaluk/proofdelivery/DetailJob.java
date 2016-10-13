package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailJob extends AppCompatActivity implements View.OnClickListener {
    //Explicit
    private TextView jobNoTextView,storeCodeTextView, storeNameTextView, arrivalTextView, intentToCallTextView;
    private ListView listView;
    private ImageView firstImageView, secondImageView, thirdImageView;
    private Button arrivalButton, takeImageButton, confirmButton,signatureButton;
    private MyConstant myConstant = new MyConstant();
    private String planDtl2_id, pathFirstImageString,driverUserNameString,getTimeDate;
    private LocationManager locationManager;
    private Criteria criteria;
    private String[] loginStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);

        //BindWidget
        bindWidget();


        // Get value From Inten
        planDtl2_id = getIntent().getStringExtra("planDtl2_id");
        loginStrings  = getIntent().getStringArrayExtra("Login");
        driverUserNameString = loginStrings[2];

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        getTimeDate = dateFormat.format(date);





                //Load data
        SynData synData = new SynData(DetailJob.this);
        synData.execute(myConstant.getUrlDetailWherePlanId(),planDtl2_id);

        //Get Event from Click Button or Image
        firstImageView.setOnClickListener(DetailJob.this);
        secondImageView.setOnClickListener(DetailJob.this);
        thirdImageView.setOnClickListener(DetailJob.this);
        arrivalButton.setOnClickListener(DetailJob.this);
        takeImageButton.setOnClickListener(DetailJob.this);
        confirmButton.setOnClickListener(DetailJob.this);
        signatureButton.setOnClickListener(DetailJob.this);




    }   //Main method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: // From  Take Photo
                if (resultCode == RESULT_OK) {
                    Log.d("12OctV6", "Take Photo save Success");
                }
             break;
            case 1: //From Click firstImage
                if (resultCode == RESULT_OK) {

                    Log.d("12OctV6", "Choose Photo Success");
                    Uri uri = data.getData();
                    pathFirstImageString = myFindPathImage(uri);
                    Log.d("12OctV5", "Path-->" + pathFirstImageString);
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        firstImageView.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                   // firstImageView.setImageBitmap(BitmapFactory.decodeFile(pathFirstImageString));

                   // firstImageView.setImageURI(uri);
                }


                break;
            case 2:
                break;
            case 3:
                break;


        }


    }//OnActivityResult

    private String myFindPathImage(Uri uri) {
        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);
        } else {
            result = uri.getPath();
        }

        return result;

    }   // MyFindPathImage



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView3:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(Intent.createChooser(intent1,"Please choose photo"),1);


                break;
            case R.id.imageView4:

                break;
            case R.id.imageView5:

                break;
            case R.id.button4:// Arrival BTN
                String strLat = "Unknown";
                String strLng = "Unknown";
                setupLocation();
                Location networkLocation = requestLocation(LocationManager.NETWORK_PROVIDER, "No Internet");
                if (networkLocation != null) {
                    strLat = String.format("%.7f", networkLocation.getLatitude());
                    strLng = String.format("%.7f", networkLocation.getLongitude());
                }

                Location gpsLocation = requestLocation(LocationManager.GPS_PROVIDER, "No GPS card");
                if (gpsLocation != null) {
                    strLat = String.format("%.7f", gpsLocation.getLatitude());
                    strLng = String.format("%.7f", gpsLocation.getLongitude());
                }




                if(strLat.equals("Unknown") && strLng.equals("Unknown")){
                    Toast.makeText(this,"Failure Lat/Lng is Unknown",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("13OctV1", " ++++++++++Latitue.-> " + strLat + " Longitue.-> " + strLng);
                    SynGPStoServer synGPStoServer = new SynGPStoServer(DetailJob.this);
                    synGPStoServer.execute(myConstant.getUrlArrivalGPS(), strLat, strLng, getTimeDate, driverUserNameString);

                    Toast.makeText(this,"Update To Server success",Toast.LENGTH_SHORT).show();
                }






                break;
            case R.id.button5:  //Take Photo
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
               startActivityForResult(intent,0);

               // Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              //  startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;
            case R.id.button6:

                break;
            case R.id.button7:

                break;

        }
    } //onClick

    private class SynData extends AsyncTask<String, Void, String> {
        //Explicit
        private Context context;

        public SynData(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            //
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("planDtl2_id", params[1]).build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();

            } catch (Exception e) {
                Log.d("12OctV4", "e-->doInBack" + e.toString());
                return null;
            }





        }// doInback

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("12OctV4", "JSON-->" + s);

            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                jobNoTextView.setText("Job No: " + jsonObject.getString("work_sheet_no"));
                storeCodeTextView.setText("Store Code: " + jsonObject.getString("store_code"));
                storeNameTextView.setText("Store Name: " + jsonObject.getString("store_nameEng"));
                arrivalTextView.setText("Arrival:" + jsonObject.getString("plan_arrivalDateTime"));
                intentToCallTextView.setText("Call" + jsonObject.getString("store_tel"));

            } catch (Exception e) {
                Log.d("12OctV5", "e-Onpost-->" + e.toString());
            }




        }   //OnPost
    }

    //Syn  GPS TO SERVER
    private class SynGPStoServer extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public SynGPStoServer(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd","true")
                        .add("Lat",params[1])
                        .add("Lng",params[2])
                        .add("stamp",params[3])
                        .add("drv_username",params[4])
                        .add("planDtl2_id",planDtl2_id)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(params[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
               return  response.body().string();
            } catch (Exception e) {
                Log.d("13OctV1", "doInBackSynGPS--->" + e.toString());
                return null;
            }



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13OctV1", "JSON__GPS->" + s);
            if(s.equals("SUCCESS")){

            }else{

            }

        }   //onPostExcute
    }// Class Syn GPS TO SERVER



    private void bindWidget() {
        jobNoTextView = (TextView) findViewById(R.id.textView14);
        storeCodeTextView = (TextView) findViewById(R.id.textView15);
        storeNameTextView = (TextView) findViewById(R.id.textView16);
        arrivalTextView = (TextView) findViewById(R.id.textView17);
        intentToCallTextView = (TextView) findViewById(R.id.textView18);

        listView = (ListView) findViewById(R.id.livContainter);

        firstImageView = (ImageView) findViewById(R.id.imageView3);
        secondImageView = (ImageView) findViewById(R.id.imageView4);
        thirdImageView = (ImageView) findViewById(R.id.imageView5);

        arrivalButton = (Button) findViewById(R.id.button4);
        takeImageButton = (Button) findViewById(R.id.button5);
        confirmButton = (Button) findViewById(R.id.button6);
        signatureButton = (Button) findViewById(R.id.button7);
    }


    public Location requestLocation(String strProvider, String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("GPS", strError);
        }


        return location;
    }


    public final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            latTextView.setText(String.format("%.7f", location.getLatitude()));
//            lngTextView.setText(String.format("%.7f", location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void setupLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);


    }   // setupLocation


}//Main Class
