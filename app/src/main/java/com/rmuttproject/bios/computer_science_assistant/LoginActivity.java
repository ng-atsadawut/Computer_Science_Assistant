package com.rmuttproject.bios.computer_science_assistant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private TextView ToRegister;
    private Button Login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView ToForgotPassword;
    private ImageView ImgQuestion;

    AnimationDrawable animationDrawable;//bganime
    ConstraintLayout myLayout;

    public static String Username = "";
    public TextView messUsername;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();

        ImgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });


        myLayout = (ConstraintLayout) findViewById(R.id.LoginActivity); //bganime
        //bg anime
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    validate(Email.getText().toString(), Password.getText().toString());
            }
        });


        ToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Registration.class));
            }
        });

        ToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

    }

    private void initWidget(){
        Email = (EditText) findViewById(R.id.edtEmail);
        Password = (EditText) findViewById(R.id.edtPasswprd);
        Login = (Button) findViewById(R.id.btnLogin);
        ToRegister = (TextView) findViewById(R.id.tvRegister);
        ToForgotPassword = (TextView)  findViewById(R.id.tvForgot);
        messUsername = (TextView) findViewById(R.id.txtName) ;
        ImgQuestion = (ImageView)findViewById(R.id.imgQuestion);
    }

    private  void  validate(String userName, String userPassword){

        if(isConnected()) {

            progressDialog.setMessage("กำลังทำการเข้าสู่ระบบ...");
            progressDialog.show();

            if (!(userName.isEmpty() || userPassword.isEmpty())) {
                firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            checkEmailVerification();
                        } else {

                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(LoginActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(LoginActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(LoginActivity.this, "รูปแบบอีเมลไม่ได้ถูกต้อง", Toast.LENGTH_LONG).show();
                                    Email.setError("รูปแบบอีเมลไม่ได้ถูกต้อง");
                                    Email.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(LoginActivity.this, "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                                    Password.setError("รหัสผ่านไม่ถูกต้อง");
                                    Password.requestFocus();
                                    Password.setText("");
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(LoginActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(LoginActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(LoginActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(LoginActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(LoginActivity.this, "บัญชีผู้ใช้ถูกปิดใช้งานโดยผู้ดูแลระบบ", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(LoginActivity.this, "ไม่พบอีเมลนี้ในระบบ", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(LoginActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(LoginActivity.this, "รหัสผ่านที่ระบุไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                                    Password.setError("รหัสผ่านไม่ถูกต้อง ต้องมีอย่างน้อย 6 ตัวอักษร");
                                    Password.requestFocus();
                                    break;
                            }
                            progressDialog.dismiss();

                        }

                    }

                });
            } else {
                if (userName.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (userName.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show();
                } else if (userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }else {
            Toast.makeText(LoginActivity.this, "กรุณาเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            LoginActivity.this.finish();
            Toast.makeText(LoginActivity.this, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();

//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            //Read Username Setting
//            DatabaseReference myRefUsername = database.getReference(firebaseAuth.getUid()).child("info_user").child("username");
//            myRefUsername.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    String value = dataSnapshot.getValue(String.class);
//                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                    SharedPreferences.Editor editor = sp.edit();
//
//                    editor.putString("Username", value);
//                    editor.commit();
//
//                    Username = value.toString();
//                    messUsername.setText(Username);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Failed to read value
//                    Log.d("J", "Failed to read value.", databaseError.toException());
//                }
//            });
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else{
            Toast.makeText(LoginActivity.this,"กรุณายืนยันที่อยู่อีเมล",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }


    }

    //****************************** onBackPressed ***********************************
    //Do you want to exit?
    public void onBackPressed() {

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
