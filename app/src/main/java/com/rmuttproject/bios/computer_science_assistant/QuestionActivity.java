package com.rmuttproject.bios.computer_science_assistant;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    Animation animation;
    Animation animation2;////
    ListView listView;//listv
    TextView tv;
    String selectedItem;

    LinearLayout menuSetting;///

    AnimationDrawable animationDrawable;//bganime
    ConstraintLayout myLayout;

    private Toolbar toolbar;
    private TextView Titletoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        SetToolbar();
        myLayout = (ConstraintLayout) findViewById(R.id.myLayout); //bganime

        menuSetting = (LinearLayout) findViewById(R.id.menuSetting);///

        listView = (ListView) findViewById(R.id.listview);
        tv = (TextView) findViewById(R.id.tv);

        animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.mytransition2);////
        menuSetting.setAnimation(animation2);////


        //list menu
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("วิธีการสมัครสมาชิก");
        arrList.add("วิธีการเข้าสู่ระบบ");
        arrList.add("วิธีการสั่งงานด้วยคำสั่งเสียง");
        arrList.add("วิธีการสั่งงานด้วยการพิมพ์");
        arrList.add("วิธีการกู้คืนรหัสผ่าน");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, arrList);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                selectedItem = (String) parent.getItemAtPosition(position);


                if (selectedItem == "วิธีการสมัครสมาชิก") {
                    Intent intent = new Intent(QuestionActivity.this, HowtoRegisterActivity.class);
                    startActivity(intent);
                } else if (selectedItem == "วิธีการเข้าสู่ระบบ") {
                    Intent intent = new Intent(QuestionActivity.this, HowtoLoginActivity.class);
                    startActivity(intent);
/*                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
                    startActivityForResult(myIntent, 0);*/
                } else if (selectedItem == "วิธีการสั่งงานด้วยคำสั่งเสียง") {
                    Intent intent = new Intent(QuestionActivity.this, HowtoUseVoiceActivity.class);
                    startActivity(intent);
                } else if (selectedItem == "วิธีการสั่งงานด้วยการพิมพ์") {
                    Intent intent = new Intent(QuestionActivity.this, HowtoUseKeyboardActivity.class);
                    startActivity(intent);
                } else if (selectedItem == "วิธีการกู้คืนรหัสผ่าน") {
                    Intent intent = new Intent(QuestionActivity.this, HowtoRepassActivity.class);
                    startActivity(intent);
                }
            }
        });




        //end list menu


        //bg anime
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        //bganime

    }

    private void SetToolbar() {
        toolbar = (Toolbar) findViewById(R.id.Custom_Toolbar);
        Titletoolbar = (TextView) findViewById(R.id.text_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Titletoolbar.setText("คำแนะนำ");
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
