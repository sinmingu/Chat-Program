package com.example.sinmingu.login.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.sinmingu.login.DataBase.DBHelper;
import com.example.sinmingu.login.R;

import static com.example.sinmingu.login.DataBase.DBHelper.DATABASE_NAME;
import static com.example.sinmingu.login.DataBase.DBHelper.DATABASE_VERSION;

public class MainActivity extends BaseActivity {

    TextView title;
    TextView member_join;
    ImageView id_img, password_img;
    static EditText id_input, pw_input;
    Button login_check;
    static CheckBox auto_login;
    static boolean auto_login_status=false;

    private BackPressCloseHandler backPressCloseHandler;

    String user_id;
    String user_nickname;
    String user_password;

    static int login_user_num;

    DBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);
        auto_login=(CheckBox)findViewById(R.id.auto_login);
        id_input=(EditText)findViewById(R.id.id_input);
        pw_input=(EditText)findViewById(R.id.pw_input);

        SharedPreferences prefs =getSharedPreferences("User_infor", MODE_PRIVATE);
        user_id=prefs.getString("User_id","0");
        user_nickname=prefs.getString("User_nickname","0");
        user_password=prefs.getString("User_password","0");
        auto_login_status=prefs.getBoolean("Auto_login", false);

        auto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto_login.isChecked()==true)
                    auto_login_status=true;
                else if(auto_login.isChecked()==false)
                    auto_login_status=false;
            }
        });


        if(auto_login_status==true){
            auto_login.setChecked(true);
            startActivity(new Intent(this,chat_list.class));
        }
        else if(auto_login_status==false){
            auto_login.setChecked(false);
        }



        //startActivity(new Intent(this,ChatRoom.class));

        helper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        try{
            db = helper.getWritableDatabase();
        }catch(SQLiteException ex){
            db = helper.getReadableDatabase();
        }

        //db.execSQL("delete from USER");
        //db.execSQL("delete from FRIENDS");

        title=(TextView)findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Typo_DecoSolid.ttf");
        title.setTypeface(font);

        member_join=(TextView)findViewById(R.id.member_join);
        login_check=(Button)findViewById(R.id.login_check);
        id_img=(ImageView)findViewById(R.id.id_img);
        password_img=(ImageView)findViewById(R.id.password_img);


        Glide.with(this).load(R.drawable.id_card).fitCenter().into(id_img);
        Glide.with(this).load(R.drawable.padlock).fitCenter().into(password_img);

        member_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,new_member_join.class);
                startActivity(intent);

            }
        });

        login_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor;
                cursor = db.rawQuery("SELECT id, nickname, password FROM USER",null);

                //한 행씩 db탐색
                while(cursor.moveToNext()){
                    String db_id=cursor.getString(cursor.getColumnIndex("id"));
                    String db_password=cursor.getString(cursor.getColumnIndex("password"));
                    String db_nickname=cursor.getString(cursor.getColumnIndex("nickname"));
                    if(id_input.getText().toString().equals(db_id)&&pw_input.getText().toString().equals(db_password)){


                        //기존 로그인값 저장
                        SharedPreferences pref =getSharedPreferences("User_infor", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("User_id",id_input.getText().toString()); //키값, 저장값
                        editor.putString("User_nickname",db_nickname );
                        editor.putString("User_password",db_password );
                        editor.putBoolean("Auto_login",auto_login_status);
                        editor.commit();


                        Intent intent=new Intent(MainActivity.this,chat_list.class);
                        //intent.putExtra("id",id_input.getText().toString());
                        //intent.putExtra("nickname", db_nickname);
                        startActivity(intent);




                    }

                }

                /*
                for(int i=0; i<member_list.size(); i++){

                    if(id_input.getText().toString().equals(member_list.get(i).getUser_id())&&
                    pw_input.getText().toString().equals(member_list.get(i).getUser_pw())){

                        Intent intent=new Intent(MainActivity.this,chat_list.class);
                        intent.putExtra("name",member_list.get(i).getUser_name());
                        intent.putExtra("id",member_list.get(i).getUser_id());
                        intent.putExtra("user_num",i);
                        startActivity(intent);

                    }
                    if(!(id_input.getText().toString().equals(member_list.get(i).getUser_id())&&
                            pw_input.getText().toString().equals(member_list.get(i).getUser_pw()))){

                        Toast.makeText(getApplicationContext(),"아이디나 비밀번호를 확인하세요",Toast.LENGTH_SHORT).show();

                    }

                }
                */

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        backPressCloseHandler.onBackPressed();

    }
}
