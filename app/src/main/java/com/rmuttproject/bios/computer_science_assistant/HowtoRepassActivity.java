package com.rmuttproject.bios.computer_science_assistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class HowtoRepassActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView Titletoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto_repass);
        SetToolbar();
    }

    private void SetToolbar() {
        toolbar = (Toolbar) findViewById(R.id.Custom_Toolbar);
        Titletoolbar = (TextView) findViewById(R.id.text_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Titletoolbar.setText("วิธีการกู้คืนรหัสผ่าน");
    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected( item );
    }
}
