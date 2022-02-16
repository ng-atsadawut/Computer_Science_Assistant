package com.rmuttproject.bios.computer_science_assistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText passwordEmail;
    private Button ResetPassword;
    private FirebaseAuth firebaseAuth;

    AnimationDrawable animationDrawable;//bganime
    ConstraintLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        myLayout = (ConstraintLayout) findViewById(R.id.ForgotPassword); //bganime
        //bg anime
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        passwordEmail = (EditText) findViewById(R.id.edtPasswordEmail);
        ResetPassword = (Button) findViewById(R.id.btnResetPassword);
        firebaseAuth =FirebaseAuth.getInstance();

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserEmail = passwordEmail.getText().toString().trim();

                if(passwordEmail.getText().toString().trim().isEmpty()){
                    passwordEmail.setText("");
                }

                if(UserEmail.equals("")) {
                    Toast.makeText(ForgotPassword.this, "กรุณากรอกอีเมลที่ต้องการจะกู้คืนรหัสผ่าน", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(isConnected()) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "ข้อความถูกส่งไปทางอีเมล กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(ForgotPassword.this, "เกิดข้อผิดพลาดในการส่งอีเมล", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ForgotPassword.this, "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.d("J Exception", e.getMessage());
        }
        return connected;
    }
}
