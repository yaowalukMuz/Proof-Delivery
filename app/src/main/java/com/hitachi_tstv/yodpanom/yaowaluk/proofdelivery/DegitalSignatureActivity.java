package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DegitalSignatureActivity extends AppCompatActivity {
    //Explicit
    private LinearLayout mContent;
    private EditText fullNameEditText;
    private Button saveButton,clearButton;
   // private signature mSignature;
    private View view;
    DrawingView dv;
    private Paint mPaint;
    Bitmap bitmap;
    File file;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degital_signature);

        //BindWidget
        mContent = (LinearLayout) findViewById(R.id.linSign);
        saveButton = (Button) findViewById(R.id.button10);
        clearButton = (Button) findViewById(R.id.button9);
        saveButton.setEnabled(false);
        view = mContent;
        //


        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }



        // Drawing signature
        dv = new DrawingView(this);
        mContent.addView(dv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        saveButton.setEnabled(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                dv.save(view, StoredPath);

             //   Toast.makeText(getApplicationContext(),"Successfully saved",Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("13OctV3", "OnClick-----Clear Btn++++++++");
                dv.clear();

                saveButton.setEnabled(false);
            }
        });


      /*
        dv = new DrawingView(getApplicationContext());
        dv.setBackgroundColor(Color.WHITE);

        mContent.addView(dv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        saveButton.setEnabled(false);
        view = mContent;*/

    }// Main Method


    public class DrawingView extends View{
        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;




        public DrawingView(Context context) {
            super(context);
            context = context;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLACK);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

        }
        public Bitmap save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", "e Exception-----File->"+ e.toString());
            }
        return null;
        }

        public void clear() {
           // circlePath.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawRect(0, 0, 0, 0, mPaint);
            mPath.reset();
            invalidate();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0,mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX,mY;
        private static final float TOUCH_TOLERANCE = 4;


        private void touch_start(float x,float y){
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

        }

        private void touch_move(float x ,float y){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y -mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y +mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up(){
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            saveButton.setEnabled(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE :
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP :
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }//Class DrawingView


    private class SynUploadImageTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private  Bitmap mPhotoBitMap;
        private String mUploadedFileName;
        private UploadImageUtils uploadImageUtils;

        public SynUploadImageTask(Context context, Bitmap mPhotoBitMap) {
            this.context = context;
            this.mPhotoBitMap = mPhotoBitMap;
        }

        @Override
        protected String doInBackground(Void... params) {

            String urlServer = "http://service.eternity.co.th/TrackingInOut/upload.php";
            uploadImageUtils = new UploadImageUtils();

            mUploadedFileName = uploadImageUtils.getRandomFileName();
            final String result = uploadImageUtils.uploadFile(mUploadedFileName, urlServer, mPhotoBitMap);
            Log.d("TAG", "Do in back after save:-->" + result);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Call", "Success");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Add Image Successful!!", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }





}//Main Class
