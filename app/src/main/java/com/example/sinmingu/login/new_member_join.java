package com.example.sinmingu.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.sinmingu.login.DBHelper.DATABASE_NAME;
import static com.example.sinmingu.login.DBHelper.DATABASE_VERSION;

public class new_member_join extends BaseActivity {


    boolean new_member_status=true;
    boolean same_password_status=true;
    EditText Join_user_name, Join_user_id, Join_user_pw, Join_user_pw_check;
    Button btn_Join_user;
    static ArrayList<Member> member_list=new ArrayList<Member>();
    String input_name, input_id, input_pw;

    EditText certification_number;
    Button certification_btn;
    int certification_token = 0;

    DBHelper helper;
    SQLiteDatabase db;

    BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member_join);



        helper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        try{
            db = helper.getWritableDatabase();
        }catch(SQLiteException ex){
            db = helper.getReadableDatabase();
        }





        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);

        btn_Join_user=(Button)findViewById(R.id.btn_Join_user);
        Join_user_name=(EditText)findViewById(R.id.Join_user_name);
        Join_user_id=(EditText)findViewById(R.id.Join_user_id);
        Join_user_pw=(EditText)findViewById(R.id.Join_user_pw);
        Join_user_pw_check=(EditText)findViewById(R.id.Join_user_pw_check);

        certification_number=(EditText)findViewById(R.id.certification_number);
        certification_btn=(Button)findViewById(R.id.certification_btn);

        //sms dynamic recv
        // 인텐트 필터 설정
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("recvSMS");
        // 동적리시버 생성

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(),intent.getStringExtra("smsnumber"),Toast.LENGTH_SHORT).show();
                certification_number.setText(intent.getStringExtra("smsnumber"));

            }
        };


        // 위에서 설정한 인텐트필터+리시버정보로 리시버 등록
        registerReceiver(receiver, intentFilter);

        requester.create().request(android.Manifest.permission.RECEIVE_SMS,
                10000,
                new PermissionRequester.OnClickDenyButtonListener(){
                    @Override
                    public void onClick(Activity activity){
                        Toast.makeText(getApplicationContext(),"권한을 얻지 못했습니다",Toast.LENGTH_SHORT).show();
                    }
                });

        certification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(certification_number.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"서버로 인증번호 요청",Toast.LENGTH_SHORT).show();
                    certification_btn.setText("확인");
                }else if(certification_btn.getText().toString().equals("확인") && !(certification_number.getText().toString().equals(""))){
                    //Toast.makeText(getApplicationContext(),"인증 번호 확인",Toast.LENGTH_SHORT).show();
                    if(certification_number.getText().toString().equals("123456")){
                        certification_token=1;
                        certification_btn.setText("인증 완료");
                        certification_btn.setActivated(false);
                    }
                }
            }
        });

        btn_Join_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Cursor cursor;
                cursor = db.rawQuery("SELECT id FROM USER",null);


                while(cursor.moveToNext()){
                    String db_id=cursor.getString(cursor.getColumnIndex("id"));
                    if(Join_user_id.getText().toString().equals(db_id)){
                        new_member_status = false;
                        Toast.makeText(getApplicationContext(),"해당 아이디가 이미 있습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }



                /*
                // 동일 아이디가 있는지 검사
                for(int i=0; i<member_list.size(); i++){

                    if(Join_user_id.getText().toString().equals(member_list.get(i).getUser_id())){

                        new_member_status = false;
                        Toast.makeText(getApplicationContext(),"해당 아이디가 이미 있습니다.",Toast.LENGTH_SHORT).show();
                        return;

                    }
                    else{
                        new_member_status = true;
                    }

                }
                */

                if(Join_user_name.getText().toString().equals("")||Join_user_pw.getText().toString().equals("")
                        ||Join_user_pw.getText().toString().equals("")||Join_user_pw_check.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();

                }
                else if(!(Join_user_pw.getText().toString().equals(Join_user_pw_check.getText().toString()))){

                    Toast.makeText(getApplicationContext(),"비밀번호를 정확히 입력하세요.",Toast.LENGTH_SHORT).show();
                    same_password_status=false;

                }
                else if(certification_token == 0){
                    Toast.makeText(getApplicationContext(),"인증 번호 오류",Toast.LENGTH_SHORT).show();
                }
                else{

                    input_name=Join_user_name.getText().toString();
                    input_id=Join_user_id.getText().toString();
                    input_pw=Join_user_pw.getText().toString();

                    db.execSQL("INSERT INTO USER (id, nickname, password) VALUES ('"+input_id+"','"+input_name+"','"+input_pw+"');");

                    //db.execSQL("INSERT INTO USER VALUES ('" + input_id + "','" + input_name + "','" + input_pw + "');");
                    //member_list.add(new Member(input_name, input_id, input_pw));
                    Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                    same_password_status=true;

                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
