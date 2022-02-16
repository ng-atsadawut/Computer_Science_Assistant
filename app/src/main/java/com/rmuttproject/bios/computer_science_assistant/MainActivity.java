package com.rmuttproject.bios.computer_science_assistant;

// CS Assistant
// RMUTT Project
// Atsadawut Ngernngokngarm
// Adison meesin

import android.Manifest;
import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;
import android.view.WindowManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.support.v4.app.ActivityCompat;
import android.transition.TransitionManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    //
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet1 = new ConstraintSet();
    private ConstraintSet constraintSet2 = new ConstraintSet();
    private ConstraintSet constraintSet3 = new ConstraintSet();
    private ConstraintSet constraintSet4 = new ConstraintSet();
    private ConstraintSet constraintSet5 = new ConstraintSet();

    AnimationDrawable animationDrawable;//bganime
    ConstraintLayout myLayout;

    private ImageView img, img2, img3, imgshow, Imgsetting, ImgQuestion, Btn_Keyborad;

    public String Sound;

    public String PostMessage = "";
    public String ReceiveMessage = "";
    public String ReceiveMessage_TTS = "";
    public String PostMessage2 = "";
    public String action = "";
    public String url_image = "";
    public String link = "";
    public String tel = "";
    public String teacher = "";
    public String UserNamePref, BG;
    public int state = 0;

    public int count_tts = 0;

    public String[] Btn_Setting = {"ตั้งค่า", "ออกจากระบบ"};

    public EditText edt;
    public Button btn;

    public TextView mess1, mess2, messUsername, messNetwork;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    private FirebaseAuth firebaseAuth;

    final static int INTENT_CHECK_TTS = 0;
    TextToSpeech TTS;

    private GestureDetector mGestureDetector;//fling

    public static String clID;

    public static String Username = "";

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        myLayout = (ConstraintLayout) findViewById(R.id.activity_main); //bganime
        //bg anime
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        Log.d("J onCreate", "Start onCreate");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insertDummyContactWrapper();
        }

        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

//        //BG Animation
//        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(10000L);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                final float progress = (float) animation.getAnimatedValue();
//                final float width = backgroundOne.getWidth();
//                final float translationX = width * progress;
//                backgroundOne.setTranslationX(translationX);
//                backgroundTwo.setTranslationX(translationX - width);
//            }
//        });
//        animator.start();

        clID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mGestureDetector = new GestureDetector(this, mGestureListener);//fling

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("th"));

        initWidget();

        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, INTENT_CHECK_TTS);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null){
                    state = 0;
                    mess1.setText("กรุณารอสักครู่");
                    mess1.setVisibility(View.VISIBLE);
                    PostMessage2 = matches.get(0);
                    PostMessage = DataKeeper.URL_PHP_SERVICE + matches.get(0);
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else {
                    Toast.makeText(MainActivity.this,"Matches null",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                img.animate().rotationBy(360).withEndAction(this).setDuration(5000).setInterpolator(new LinearInterpolator()).start();
                img2.animate().rotationBy(360).withEndAction(this).setDuration(5000).setInterpolator(new LinearInterpolator()).start();//use for rotate img2 always_1
                img3.animate().rotationBy(-360).withEndAction(this).setDuration(2000).setInterpolator(new LinearInterpolator()).start();//use for rotate img3 always_1
            }
        };

//        img.animate().rotationBy(360).withEndAction(runnable).setDuration(5000).setInterpolator(new LinearInterpolator()).start();
        img2.animate().rotationBy(360).withEndAction(runnable).setDuration(5000).setInterpolator(new LinearInterpolator()).start();//use for rotate img2 always_1
        img3.animate().rotationBy(-360).withEndAction(runnable).setDuration(2000).setInterpolator(new LinearInterpolator()).start();//use for rotate img3 always_1

        Imgsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.logocomsci);
                builder.setCancelable(true);
                builder.setTitle("เมนู");
                builder.setItems(Btn_Setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if ("ตั้งค่า".equals(Btn_Setting[which])){
                            startActivity(new Intent(MainActivity.this, Setting.class));
                        }else if ("ออกจากระบบ".equals(Btn_Setting[which])){
                            Logout();
                            Toast.makeText(MainActivity.this, "ออกจากระบบ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();

            }
        });

        Btn_Keyborad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                constraintSet4.applyTo(constraintLayout);//use for constrainanimation
                SetUI_NOT_Error();
                state = 0;
                edt.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                mSpeechRecognizer.cancel();
                mess1.setText("");
                mess2.setText("");
                mess1.setVisibility(View.INVISIBLE);
                mess2.setVisibility(View.INVISIBLE);
                edt.setText("");
                edt.requestFocus();
                btn.isEnabled();
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        ImgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

                TTS.stop();

                if(state == 0) {
                    Log.d("J setOnClickListener", "Open speech");
                    if (!isConnected()) {

                        SetUI_Error();

                        mess1.setText("");
                        mess1.setVisibility(View.INVISIBLE);
                        messNetwork .setText("");
                        mess2.setText("กรุณาเชื่อมต่ออินเตอร์เน็ตก่อนครับ");
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                        TTS.speak("กรุณาเชื่อมต่ออินเตอร์เน็ตก่อนครับ", TextToSpeech.QUEUE_FLUSH, null);
                        mess2.setVisibility(View.VISIBLE);
                    } else {

                        SetUI_NOT_Error();
                        img.setImageResource(R.drawable.circleconcave);
                        img2.setImageResource(R.drawable.ringconcave);
                        img3.setImageResource(R.drawable.ringconcave);

                        state = 1;
                        mess1.setVisibility(View.VISIBLE);
                        mess1.setText("กำลังฟังอยู่ครับ");
                        mess2.setText("");
                        mess2.setVisibility(View.INVISIBLE);
                        messNetwork .setText("");
                        action = "";

                        imgshow.setVisibility(View.INVISIBLE);
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                        img.animate().rotation(0).start();//use for rotate img to 0 degree
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet2.applyTo(constraintLayout);//use for constrainanimation

                    }
                }else if(state == 1){

                    SetUI_NOT_Error();



                    Log.d("J setOnClickListener", "Stop speech");
                    state = 0;
                    mess1.setText("");
                    mess2.setText("");
                    mess1.setVisibility(View.INVISIBLE);
                    mess2.setVisibility(View.INVISIBLE);
                    messNetwork .setText("");
                    mSpeechRecognizer.cancel();
                    imgshow.setVisibility(View.INVISIBLE);
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String CHECK_NULL_MESSAGEBOX = edt.getText().toString().replace(" ","");

                if(!(CHECK_NULL_MESSAGEBOX.equals(""))) {

                    if(state == 0) {
                        mSpeechRecognizer.cancel();
                    }

                        Log.d("J Send Message", "Send Message");
                        mess1.setText("กรุณารอสักครู่");
                        mess1.setVisibility(View.VISIBLE);
                        mess2.setText("");
                        mess2.setVisibility(View.INVISIBLE);
                        messNetwork.setText("");
                        edt.setVisibility(View.INVISIBLE);
                        btn.setVisibility(View.INVISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                        SetUI_NOT_Error();
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                        PostMessage2 = edt.getText().toString();
                        PostMessage = DataKeeper.URL_PHP_SERVICE + edt.getText().toString();
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute();

                }
            }
        });

    }

    public void SetUI_Error(){
        img.setImageResource(R.drawable.error);
        img2.setImageResource(R.drawable.ringerror);
        img3.setImageResource(R.drawable.ringerror);
    }

    public void SetUI_NOT_Error(){
        img.setImageResource(R.drawable.circleconvex);
        img2.setImageResource(R.drawable.ringconvex);
        img3.setImageResource(R.drawable.ringconvex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    //Show Hide Keyboard
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getY() > e2.getY()){
                TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                constraintSet4.applyTo(constraintLayout);//use for constrainanimation
                SetUI_NOT_Error();
                state = 0;
                edt.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                mSpeechRecognizer.cancel();
                mess1.setText("");
                mess2.setText("");
                mess1.setVisibility(View.INVISIBLE);
                mess2.setVisibility(View.INVISIBLE);
                edt.setText("");
                edt.requestFocus();
                btn.isEnabled();
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }

            if(e1.getY() < e2.getY()){
                imgshow.setImageResource(R.color.common_google_signin_btn_text_dark_disabled);
                edt.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.INVISIBLE);
                mSpeechRecognizer.cancel();
                mess1.setText("");
                mess2.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

                SetUI_NOT_Error();
                TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                mess1.setVisibility(View.INVISIBLE);
                mess2.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("J onResume","onResume");
        firebaseAuth = FirebaseAuth.getInstance();

        if(count_tts < 1 ) {
            Log.d("J onResume", "count_tts = " + count_tts);
            count_tts++;
            TTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    //SET START TTS
                    Locale thLocale = new Locale("th");
                    TTS.setLanguage(thLocale);
                    TTS.setPitch((float) 0.9);
                    TTS.setSpeechRate((float) 1.3);
                    }
            });
        }

        try {
            readDataSetting();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

//        if(count_tts == 0) {
        setting();
//        }

        if(state == 0) {
            mSpeechRecognizer.cancel();
        }
        try{
            sendDataSetting();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    //****************************** onBackPressed ***********************************
    //Do you want to exit?
    public void onBackPressed() {
        mSpeechRecognizer.cancel();

//        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()) {
//
//        }else{
//            edt.setVisibility(View.INVISIBLE);
//            btn.setVisibility(View.INVISIBLE);
//        }

        mess1.setText("");
        mess1.setVisibility(View.INVISIBLE);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("CS Assistant");
        dialog.setIcon(R.drawable.logocomsci);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากแอปพลิเคชัน. \n ใช่ หรือ ไม่");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    //AsyncTaskRunner
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        //            When AsyncTask to use it start firsty
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("J onPreExecute ","onPreExecute");
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            Log.d("J doInBackground ", "doInBackground");
            try {
                Log.d("J doInBackground", "Try");
                Log.d("J doInBackground", PostMessage);
                URL url = new URL(PostMessage);
                URLConnection urlConnection = url.openConnection();
                Log.d("J doInBackground", "URL");


                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                Log.d("J doInBackground", "httpURLConnection");


                InputStream inputStream = null;
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("J doInBackground", "HTTP_OK");
                    inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }

                    inputStream.close();

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    if(jsonObject.getString("response") == "" || jsonObject.getString("response") == null){
                        ReceiveMessage = "";
                        mess1.setText("");
                        mess1.setVisibility(View.INVISIBLE);
                        mess2.setText("ขออภัยครับ ผมไม่สามารถติดต่อเซิร์ฟเวอร์ได้ในขณะนี้");
                        mess2.setVisibility(View.VISIBLE);
                        //imgshow.setImageResource(R.drawable.khongthep);
                        img.animate().rotation(-180).start();//use for rotate img to -180 degree
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        }
                        constraintSet2.applyTo(constraintLayout);//use for constrainanimation
                    }else {
                        ReceiveMessage = "";
                        ReceiveMessage = jsonObject.getString("response"); //for php
                        action = jsonObject.getString("action").toString();
                        url_image = jsonObject.getString("url_image").toString();
                        tel = jsonObject.getString("tel_teacher").toString();
                        teacher = jsonObject.getString("teacher");
                        link = jsonObject.getString("link");
                    }
                } else{
                    ReceiveMessage = "";
                    mess1.setText("");
                    mess1.setVisibility(View.INVISIBLE);
                    mess2.setText("ขออภัยครับ ผมไม่สามารถติดต่อเซิร์ฟเวอร์ได้ในขณะนี้");
                    SetUI_NOT_Error();
                    //imgshow.setImageResource(R.drawable.khongthep);
                    img.animate().rotation(-180).start();//use for rotate img to -180 degree
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                    mess2.setVisibility(View.VISIBLE);
                }

                ReceiveMessage_TTS = ReceiveMessage.replace("คุณ",Username)
                                                    .replace("บอท","Bot")
                                                    .replace("ปิยนันท์","ปิยะนัน")
                                                    .replace("นิลพฤกษ์","นินละพฤกษ์")
                                                    .replace("กาญจนเสถียร","กานจะนะเสถียร")
                                                    .replace("คุณากรวงศ์","คุ นา กร วง")
                                                    .replace("พิเชฐ","พิเชษ")
                                                    .replace("จันทร์รัชชกูล","จันทร์รัชช่ะกูล")
                                                    .replace("ศรากรวงศ์","คุ นา กร วง")
                                                    .replace("ม.ค.","มกราคม")
                                                    .replace("ก.พ.","กุมภาพันธ์")
                                                    .replace("มี.ค.","มีนาคม")
                                                    .replace("เม.ย.","เมษายน")
                                                    .replace("พ.ค.","พฤษภาคม")
                                                    .replace("มิ.ย.","มิถุนายน")
                                                    .replace("ก.ค.","กรกฎาคม")
                                                    .replace("ส.ค.","สิงหาคม")
                                                    .replace("ก.ย.","กันยายน")
                                                    .replace("ต.ค.","ตุลาคม")
                                                    .replace("พ.ย.","พฤศจิกายน ")
                                                    .replace("ธ.ค.","ธันวาคม")
                                                    .replace("อนิเมชั่น","แอ นิ เม ชั่น")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","")
                                                    .replace("","");



                Log.d("J doInBackground MS_TTS", ReceiveMessage_TTS);
                Log.d("J doInBackground action", action);
                Log.d("J doIn url_image", url_image);
                Log.d("J doIn teacher", teacher);
                Log.d("J doIn Receive", "ReceiveMessage = " + ReceiveMessage);
                Log.d("J doInBackground Post", "PostMessage = " + PostMessage);

            } catch (MalformedURLException e) {
                Log.d("J doInBackground catch", "MalformedURLException");
                action = "Exception";
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("J doInBackground catch", "JSONException");
                action = "Exception";
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("J doInBackground catch", "IOException");
                action = "Exception";
                e.printStackTrace();
            } catch (RuntimeException e) {
                Log.d("J doInBackground catch", "RuntimeException");
                action = "Exception";
                e.printStackTrace();
            }finally {
                ReceiveMessage = ReceiveMessage.toString();
            }

            return action;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.d("J onPostExecute","Start onPostExecute");
            Log.d("J onPostExecute aVoid", aVoid);

            mess1.setText("กรุณารอสักครู่");
            mess1.setVisibility(View.VISIBLE);

            if(!(ReceiveMessage.equals("") || ReceiveMessage.isEmpty())) {
                if(aVoid.equals("image")){
                    Log.d("J onPostExecute image", "image");

                    mess1.setVisibility(View.INVISIBLE);
                    mess1.setText("");

                    if(teacher.equals("No")){
//                        imgshow.setImageResource(R.drawable.);
                        TTS.speak("ไม่มีชื่ออาจารย์ท่านนี้ในระบบครับ", TextToSpeech.QUEUE_FLUSH, null);
                        SetUI_NOT_Error();
                        img.animate().rotation(-180).start();//use for rotate img to -180 degree
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                        mess1.setVisibility(View.VISIBLE);
                        mess1.setText(PostMessage2);
                        mess2.setVisibility(View.VISIBLE);
                        mess2.setText("ไม่มีชื่ออาจารย์ท่านนี้ในระบบครับ");
                    }else {

                        mess1.setText("กรุณารอสักครู่");
                        mess1.setVisibility(View.VISIBLE);
                        mess2.setText("");
                        mess2.setVisibility(View.INVISIBLE);
                        imgshow.setVisibility(View.VISIBLE);
                        Picasso.with(MainActivity.this)
                                .load(url_image)
                                .into(imgshow, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        SetUI_NOT_Error();
                                        img.animate().rotation(-180).start();//use for rotate img to -180 degree
                                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                                        constraintSet3.applyTo(constraintLayout);//use for constrainanimation
                                        mess2.setText(ReceiveMessage);
                                        mess1.setVisibility(View.VISIBLE);
                                        mess1.setText("");
                                        mess1.setVisibility(View.INVISIBLE);
                                        TTS.speak(ReceiveMessage_TTS, TextToSpeech.QUEUE_FLUSH, null);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                    }

                }else if(aVoid.equals("call")){
                    if(teacher.equals("No")){
                        mess1.setText(PostMessage2);
                        mess1.setVisibility(View.VISIBLE);
                        mess2.setText("ไม่มีชื่ออาจารย์ท่านนี้ในระบบครับ");
                        mess2.setVisibility(View.VISIBLE);
//                        imgshow.setImageResource(R.drawable.);
                        TTS.speak("ไม่มีชื่ออาจารย์ท่านนี้ในระบบครับ", TextToSpeech.QUEUE_FLUSH, null);
                        SetUI_NOT_Error();
                        img.animate().rotation(-180).start();//use for rotate img to -180 degree
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                        mess1.setVisibility(View.VISIBLE);
                        mess2.setVisibility(View.VISIBLE);
                    }else {
                        Log.d("J onPostExecute call", "call");
                        mess1.setText(PostMessage2.toString());
                        mess1.setVisibility(View.VISIBLE);
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:" + tel));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        startActivity(i);

                        mess1.setText("");
                        img.animate().rotation(-180).start();//use for rotate img to -180 degree
                        TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                        constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                        mess1.setVisibility(View.INVISIBLE);
                        mess2.setVisibility(View.INVISIBLE);
                    }
                }else if(aVoid.equals("Link")){
                    mess1.setText(PostMessage2.toString());
                    mess2.setText(ReceiveMessage);
                    messNetwork.setText(link);
                    messNetwork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("J Post Link", link);

                        }
                    });
                    SetUI_NOT_Error();
                    img.animate().rotation(-180).start();//use for rotate img to -180 degree
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet1.applyTo(constraintLayout);//use for constrainanimation

                    mess1.setVisibility(View.VISIBLE);
                    mess2.setVisibility(View.VISIBLE);
                    TTS.speak(ReceiveMessage_TTS, TextToSpeech.QUEUE_FLUSH, null);
                }else if(action.equals("Exception")){
                    TTS.speak("มีข้อผิดพลาดเกิดขึ้น โปรดลองใหม่อีกครั้ง", TextToSpeech.QUEUE_FLUSH, null);
                    mess1.setText("");
                    mess1.setVisibility(View.INVISIBLE);
                    mess2.setText("มีข้อผิดพลาดเกิดขึ้น โปรดลองใหม่อีกครั้ง");
                    //imgshow.setImageResource(R.drawable.khongthep);
                    img.animate().rotation(-180).start();//use for rotate img to -180 degree
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                    mess2.setVisibility(View.VISIBLE);
                }else if(ReceiveMessage.length() > 100){
                    SetUI_NOT_Error();
                    img.animate().rotation(-180).start();//use for rotate img to -180 degree
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet5.applyTo(constraintLayout);//use for constrainanimation
                    mess2.setText(ReceiveMessage);
                    mess1.setVisibility(View.VISIBLE);
                    mess1.setText("");
                    mess1.setVisibility(View.INVISIBLE);
                    TTS.speak(ReceiveMessage_TTS, TextToSpeech.QUEUE_FLUSH, null);
                }else{
                    Log.d("J onPostExecute image", "No image");
                    mess1.setText(PostMessage2.toString());
                    mess2.setText(ReceiveMessage);
                    SetUI_NOT_Error();
                    img.animate().rotation(-180).start();//use for rotate img to -180 degree
                    TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                    constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                    mess1.setVisibility(View.VISIBLE);
                    mess2.setVisibility(View.VISIBLE);
                    TTS.speak(ReceiveMessage_TTS, TextToSpeech.QUEUE_FLUSH, null);
                }
            }else{
                TTS.speak("ขออภัยครับ ผมไม่สามารถติดต่อเซิร์ฟเวอร์ได้ในขณะนี้", TextToSpeech.QUEUE_FLUSH, null);
                mess1.setText("");
                mess1.setVisibility(View.INVISIBLE);
                mess2.setText("ขออภัยครับ ผมไม่สามารถติดต่อเซิร์ฟเวอร์ได้ในขณะนี้");
                //imgshow.setImageResource(R.drawable.khongthep);
                img.animate().rotation(-180).start();//use for rotate img to -180 degree
                TransitionManager.beginDelayedTransition(constraintLayout);//use for constrainanimation
                constraintSet1.applyTo(constraintLayout);//use for constrainanimation
                mess2.setVisibility(View.VISIBLE);
            }



            Log.d("J onPostExecute","End onPostExecute");
            return;
        }

    }

    //******************** Start TTS **************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("J onActivityResult","onActivityResult");
        if (requestCode == INTENT_CHECK_TTS) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.d("J", data.toString());
                TTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {

                        //SET START TTS
                        Locale thLocale = new Locale("th");
                        TTS.setLanguage(thLocale);
                        TTS.setPitch((float) 0.5);
                        TTS.setSpeechRate((float)1.3);
                        TTS.speak("สวัสดีครับ มีอะไรให้รับใช้ครับ", TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
            }

        } else {

            Intent intent = new Intent();
            intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(intent);
        }
        TTS.speak("สวัสดีครับ มีอะไรให้ผมรับใช้ครับ", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTS.shutdown();
        edt.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TTS.stop();
        edt.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(state == 0) {
            mSpeechRecognizer.cancel();
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    //****************************** initWidget *********************************
    private void initWidget() {

        mess1 = (TextView) findViewById(R.id.txt);
        mess2 = (TextView) findViewById(R.id.txt2);

        messNetwork = (TextView) findViewById(R.id.txt3);
        messUsername = (TextView) findViewById(R.id.txtName);

        img = (ImageView) findViewById(R.id.img);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        imgshow = (ImageView) findViewById(R.id.imgshow);

        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);//use for constrainanimation
        constraintSet1.clone(constraintLayout);//use for constrainanimation
        constraintSet2.clone(this, R.layout.activity_main2);//use for constrainanimation
        constraintSet3.clone(this, R.layout.activity_picture);//use for constrainanimation
        constraintSet4.clone(this, R.layout.activity_keyb);//use for
        constraintSet5.clone(this, R.layout.activity_length);//use for constrainanimation

        edt = (EditText)findViewById(R.id.message_send);
        btn = (Button) findViewById(R.id.btn_send);

        Imgsetting = (ImageView) findViewById(R.id.imgLogout);
        Btn_Keyborad = (ImageView) findViewById(R.id.btnkeyborad);
        ImgQuestion = (ImageView)findViewById(R.id.imgQuestion);

    }

    //******************************  Logout **************************************
    private void Logout() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    //****************************  Menu Toolbar **********************************
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_logout: {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setTitle("CS Assistant");
//                dialog.setIcon(R.drawable.logocomsci);
//                dialog.setCancelable(true);
//                dialog.setMessage("คุณต้องการออกจากระบบ \n ใช่ หรือ ไม่");
//                dialog.setPositiveButton("ไช่", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Logout();
//                    }
//                });
//
//                dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                dialog.show();
//                break;
//            }
////            case R.id.action_manual: {
////                startActivity(new Intent(MainActivity.this, User_manual.class));
////                break;
////            }
//            case R.id.action_setting: {
//                startActivity(new Intent(MainActivity.this, Setting.class));
//                break;
//            }
////            case R.id.action_about: {
////                //startActivity(new Intent(MainActivity.this, action_about.class));
////                break;
////            }
////            case R.id.action_microphone: {
////
//////                Toast.makeText(MainActivity.this,"กำลังฟังอยู่ครับ...",Toast.LENGTH_SHORT).show();Listening...
////                if(!(isConnected())) {
////                    Toast.makeText(MainActivity.this,"Please connect to the internet.",Toast.LENGTH_SHORT).show();
////                }
////                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
////                //Toast.makeText(MainActivity.this,"Listening...",Toast.LENGTH_SHORT).show();
////                loadingDialog = ProgressDialog.show(this, "Listening",
////                        "...", true, false);
////
////
////
////                break;
////            }
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //****************************  isConnected   **********************************
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.d("J Exception", e.getMessage());
        }
        return connected;
    }

    //********************************  setting *************************************
    private void setting() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

//        //Read BG Setting
        String Sound_set = sp.getString(Setting.listpref_sound, "background");
        Sound = Sound_set.toString();
        if (Sound_set.equals("เสียง1")) {
            TTS.setPitch((float) 0.5);
            TTS.setSpeechRate((float)1.3);
        }
        if (Sound_set.equals("เสียง2")) {
            TTS.setPitch((float) 0.7);
            TTS.setSpeechRate((float)0.9);
        }
        if (Sound_set.equals("เสียง3")) {
            TTS.setPitch((float) 1.2);
            TTS.setSpeechRate((float)1.0);
        }

        //Read UserNamePref Setting
        UserNamePref = sp.getString(Setting.Usernamepref,Username);
        messUsername.setText(UserNamePref);

        String background_set = sp.getString(Setting.listBG, "background");
        BG = background_set.toString();
        if (background_set.equals("Normal")) {

        }
        if (background_set.equals("Sky")) {

        }
        if (background_set.equals("Dark")) {

        }

//        //Read FontSize Setting
//        String text_size = sp.getString(Setting.listpref_text_size, "text_size");
//        FontSize = text_size.toString();
//        SharedPreferences.Editor editor = sp.edit();
//        if (text_size.equals("เล็ก")) {
//
//            editor.putString("list_preference_text_size", "เล็ก");
//            editor.commit();
//        }
//        if (text_size.equals("กลาง")) {
//
//            editor.commit();
//        }
//        if (text_size.equals("ใหญ่")) {
//
//            editor.putString("list_preference_text_size", "ใหญ่");
//            editor.commit();
//        }
//
        //Read setSound Setting
//        String Bubble_Right = sp.getString(Setting, "BubbleColor");
//        Sound = Bubble_Right.toString();
//        if (Bubble_Right.equals("สีขาว")) {
//
//            editor.putString("list_preference_bbr", "สีขาว");
//            editor.commit();
//        }
//        if (Bubble_Right.equals("สีเขียว")) {
//
//            editor.putString("list_preference_bbr", "สีเขียว");
//            editor.commit();
//        }


    }

    //ReadDataSetting from Firebase
    private void readDataSetting() {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Read Username Setting
        DatabaseReference myRefUsername = database.getReference(firebaseAuth.getUid()).child("info_user").child("username");
        myRefUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                SharedPreferences.Editor editor = sp.edit();
//
//                editor.putString("edit_text_preference_1", value);
//                editor.commit();

                Username = value.toString();
                messUsername.setText(Username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.d("J", "Failed to read value.", databaseError.toException());
            }
        });

//        //Read BG Setting
        DatabaseReference myRefBG = database.getReference(firebaseAuth.getUid()).child("setting").child("setBG");
        myRefBG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sp.edit();

                if (value.equals("Normal")) {
                    editor.putString("list_background", "Normal");
                    editor.commit();
                }
                if (value.equals("Sky")) {
                    editor.putString("list_background", "Sky");
                    editor.commit();
                }
                if (value.equals("Dark")) {
                    editor.putString("list_background", "Dark");
                    editor.commit();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("J", "Failed to read value.", error.toException());
            }
        });

        //Read Sound Setting
        DatabaseReference myRefSound = database.getReference(firebaseAuth.getUid()).child("setting").child("setSound");
        myRefSound.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sp.edit();

                if (value.equals("เสียง1")) {
                    editor.putString("list_sound", "เสียง1");
                    editor.commit();
                    Log.d("J","เสียง1");
                }
                if (value.equals("เสียง2")) {

                    editor.putString("เสียง2", "เสียง2");
                    editor.commit();
                    Log.d("J","เสียง2");
                }
                if (value.equals("เสียง3")) {

                    editor.putString("list_sound", "เสียง3");
                    editor.commit();
                    Log.d("J","เสียง3");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.d("J", "Failed to read value.", databaseError.toException());
            }
        });



    }

    //SendDataSetting To Firebase
    private void sendDataSetting() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefBG = database.getReference(firebaseAuth.getUid()).child("setting").child("setBG");
        myRefBG.setValue(BG);
//        DatabaseReference myRefFontSize = database.getReference(firebaseAuth.getUid()).child("setting").child("setFontSize");
//        myRefFontSize.setValue(FontSize);
        DatabaseReference myRefSound = database.getReference(firebaseAuth.getUid()).child("setting").child("setSound");
        myRefSound.setValue(Sound);
        DatabaseReference myRefUsername = database.getReference(firebaseAuth.getUid()).child("info_user").child("username");
        myRefUsername.setValue(UserNamePref);
//
//
    }

    //Request Permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("การโทรและจัดการการโทร");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("การบันทึกเสียง");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "คุณต้องให้สิทธิ์การเข้าถึง " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("ตกลง", okListener)
                .setNegativeButton("ยกเลิก", null)
                .create()
                .show();
    }


}

