package com.rmuttproject.bios.computer_science_assistant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private EditText UserName, Email, Password1, Password2;
    private Button Register;
    private FirebaseAuth firebaseAuth;

    ProgressDialog loadingDialog;

    AnimationDrawable animationDrawable;//bganime
    ConstraintLayout myLayout;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initWidget();

        myLayout = (ConstraintLayout) findViewById(R.id.RegisterActivity); //bganime
        //bg anime
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        firebaseAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog = ProgressDialog.show(Registration.this, "สมัครสมาชิก...", "โหลด...", true, false);
                if(validate()){
                    //Upload data to the database
                    String user_email = Email.getText().toString().trim();
                    String user_password = Password1.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (isConnected()) {

                                if (task.isSuccessful()) {
//                                Toast.makeText(Registration.this,"สมัครสมาชิกสำเร็จ",Toast.LENGTH_SHORT).show();
                                    sendEmailverification();
                                } else {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                    switch (errorCode) {

                                        case "ERROR_EMAIL_ALREADY_IN_USE":
                                            Toast.makeText(Registration.this, "The email has already been used by others.", Toast.LENGTH_LONG).show();
                                            Email.setError("The email has already been used by others. Try changing email addresses.");
                                            Email.requestFocus();
                                            break;

                                        case "ERROR_WEAK_PASSWORD":
                                            Toast.makeText(Registration.this, "The specified password is invalid.", Toast.LENGTH_LONG).show();
                                            Password1.setError("password is incorrect Must have at least 6 characters.");
                                            Password1.requestFocus();
                                            break;

                                    }
                                    loadingDialog.dismiss();
                                }
                            }else {
                                loadingDialog.dismiss();
                                Toast.makeText(Registration.this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void initWidget(){
        UserName = (EditText) findViewById(R.id.edtName);
        Email = (EditText) findViewById(R.id.edtEmail);
        Password1 = (EditText) findViewById(R.id.edtPasword1);
        Password2 = (EditText) findViewById(R.id.edtPassword2);
        Register = (Button) findViewById(R.id.btnRegister);
    }

    private Boolean validate(){

        Boolean result = false;
        String userName = UserName.getText().toString();
        String userPassword1 = Password1.getText().toString();
        String userPassword2 = Password2.getText().toString();
        String email = Email.getText().toString();

        if(userName.isEmpty() || email.isEmpty() || userPassword1.isEmpty() || userPassword2.isEmpty()){
            Toast.makeText(this,"Please fill in all information.",Toast.LENGTH_SHORT).show();
        }else if((userName.trim().replace(" ","").equals("")
                || email.trim().replace(" ","").equals("")
                || userPassword1.trim().replace(" ","").equals("")
                || userPassword2.trim().replace(" ","").equals(""))) {
            Toast.makeText(this, "Not allowed to fill blank.", Toast.LENGTH_SHORT).show();

            if(userName.trim().replace(" ","").equals("")){
                UserName.setText("");
                UserName.requestFocus();
            }else if(email.trim().replace(" ","").equals("")){
                Email.setText("");
                Email.requestFocus();
            }else if(userPassword1.trim().replace(" ","").equals("")){
                Password1.setText("");
                Password1.requestFocus();
            }else if(userPassword2.trim().replace(" ","").equals("")){
                Password2.setText("");
                Password2.requestFocus();
            }

        }else if(!(userPassword1.equals(userPassword2))){
            Toast.makeText(this,"password mismatch",Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }

    private void sendEmailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this,"Successful subscribers, please confirm email.",Toast.LENGTH_SHORT).show();
                        sendDataSetting();
                        firebaseAuth.signOut();
                        loadingDialog.dismiss();
                        finish();
                        startActivity(new Intent(Registration.this, LoginActivity.class));
                    }else{
                        loadingDialog.dismiss();
                        Toast.makeText(Registration.this,"Has not sent a confirmation email",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendDataSetting(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Refusername = database.getReference(firebaseAuth.getUid()).child("info_user").child("username");
        Refusername.setValue(UserName.getText().toString());
        DatabaseReference Refemail = database.getReference(firebaseAuth.getUid()).child("info_user").child("email");
        Refemail.setValue(Email.getText().toString());
        DatabaseReference RefbgDefault = database.getReference(firebaseAuth.getUid()).child("setting").child("setBG");
        RefbgDefault.setValue("Fire");
        DatabaseReference myRefFontSize = database.getReference(firebaseAuth.getUid()).child("setting").child("setFontSize");
        myRefFontSize.setValue("กลาง");

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
