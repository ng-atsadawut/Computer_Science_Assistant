package com.rmuttproject.bios.computer_science_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class customPopup extends AppCompatActivity {
    Button btnSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_popup);
//        btnSuccess = (Button) findViewById(R.id.btnSuccess);
//
//    btnSuccess.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent i = new Intent(customPopup.this,MainActivity.class);
//            customPopup.this.startActivity(i);
//        }
//    });
    }
}
