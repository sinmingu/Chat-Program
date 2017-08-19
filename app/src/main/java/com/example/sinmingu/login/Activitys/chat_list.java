package com.example.sinmingu.login.Activitys;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.sinmingu.login.DataBase.DBHelper;
import com.example.sinmingu.login.R;

import static com.example.sinmingu.login.DataBase.DBHelper.DATABASE_NAME;
import static com.example.sinmingu.login.DataBase.DBHelper.DATABASE_VERSION;

public class chat_list extends BaseActivity {

    ImageButton friend_seach; // 친구검색 버튼
    TextView text_chat_status; // 상위 아이디 표시란
    EditText edit_search_friend; //친구검색란
    String friend_id;
    String friend_name;
    int friend_list_num;
    ListView friend_list;
    boolean friend_search;
    SimpleCursorAdapter adapter;

    String user_id;

    DBHelper helper;
    SQLiteDatabase db;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //로그인 정보 받아오기
        Intent intent=getIntent();
        String nickname=intent.getExtras().getString("nickname");
        user_id=intent.getExtras().getString("id");
        int user_num=intent.getExtras().getInt("user_num");

        Log.d("온크리에이트","온크리에이트");
        helper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        try{
            db = helper.getWritableDatabase();
        }catch(SQLiteException ex){
            db = helper.getReadableDatabase();
        }

        friend_list=(ListView)findViewById(R.id.friend_list);
        cursor = db.rawQuery("SELECT id as _id, friend_id FROM FRIENDS WHERE id='"+user_id+"';",null);

        /*
        if(cursor!=null){
            Log.d("커서",String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            while(cursor.moveToNext())
                Log.d("유저id:",cursor.getString(cursor.getColumnIndex("_id")));
        }
        */

        startManagingCursor(cursor);
        String[] from = {"friend_id" };
        int[] to = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(chat_list.this, android.R.layout.simple_list_item_2, cursor, from, to);

        Log.d("아이템",adapter.getItem(0).toString());
        friend_list.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        friend_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
/*
        if (cursor != null && cursor.getCount() != 0) {

            Toast.makeText(getApplicationContext(), "커서가 비어있습니다.",Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "커서가 있습니다.",Toast.LENGTH_SHORT).show();



            startManagingCursor(cursor);
            String[] from = {"id", "friend_id" };
            int[] to = {android.R.id.text1,android.R.id.text2};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(chat_list.this, android.R.layout.simple_list_item_2, cursor, from, to);

            friend_list.setAdapter(adapter);



            cursor.moveToFirst();
            //한 행씩 db탐색
            while(cursor.moveToNext()){
                String db_id=cursor.getString(cursor.getColumnIndex("id"));
                String db_friend=cursor.getString(cursor.getColumnIndex("friend_id"));
                int i= 0;
                i=cursor.getPosition();

                Toast.makeText(getApplicationContext(), ""+i,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), ""+db_id,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), ""+db_friend,Toast.LENGTH_SHORT).show();


            }

         //   adapter.notifyDataSetChanged();


        }

*/

        friend_list_num=user_num;

        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, member_list.get(user_num).getFriend_list());

        //탭 호스트
        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        // Tab1 친구
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1").setIndicator("",getResources().getDrawable(R.drawable.friend));
        tabSpec1.setContent(R.id.tab1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 채팅
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2").setIndicator("",getResources().getDrawable(R.drawable.chat));
        tabSpec2.setContent(R.id.tab2); // Tab Content
        tabHost.addTab(tabSpec2);

        // Tab3 Setting
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Tab3").setIndicator("",getResources().getDrawable(R.drawable.news));
        tabSpec3.setContent(R.id.tab3); // Tab Content
        tabHost.addTab(tabSpec3);

        // Tab4 Setting
        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("Tab4").setIndicator("",getResources().getDrawable(R.drawable.setup));
        tabSpec4.setContent(R.id.tab4); // Tab Content
        tabHost.addTab(tabSpec4);

        // 탭 호스트 이미지 변경
        tabHost.setCurrentTab(0);

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) {

            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_custom);

        }

        // 탭 호스트 글꼴 변경

        /*
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/HoonWhitecatR.ttf");

        for (int i=0; i<tabHost.getTabWidget().getChildCount(); i++) {
            LinearLayout relLayout = (LinearLayout) tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView)relLayout.getChildAt(1);
            tv.setTypeface(font);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(16);
        }
        */

        text_chat_status=(TextView)findViewById(R.id.text_chat_status);
        edit_search_friend=(EditText)findViewById(R.id.edit_search_friend);
        friend_seach=(ImageButton)findViewById(R.id.friend_search);
        Glide.with(this).load(R.drawable.search).fitCenter().into(friend_seach);

        text_chat_status.setText("친구 ("+user_id+")");

        Toast.makeText(getApplicationContext(),"'"+nickname+"'님이 로그인 되었습니다.",Toast.LENGTH_SHORT).show();

        //친구검색 버튼 클릭
        friend_seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = db.rawQuery("SELECT id, nickname FROM USER",null);

                //한 행씩 db탐색
                while(cursor.moveToNext()){
                    String db_id=cursor.getString(cursor.getColumnIndex("id"));
                    String db_nickname=cursor.getString(cursor.getColumnIndex("nickname"));
                    if(edit_search_friend.getText().toString().equals(db_id)){
                        friend_id = db_id;
                        friend_name = db_nickname;

                        ShowDialog();
                    }

                }

                /*
                for(int i=0; i<member_list.size(); i++){

                    if(edit_search_friend.getText().toString().equals(member_list.get(i).getUser_id())){

                        friend_name=member_list.get(i).getUser_name();
                        friend_id=member_list.get(i).getUser_id();

                        //대화상자 실행
                        ShowDialog();

                    }

                }
                */

            }
        });
/*
        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;
                // TODO : use strText
            }
        }) ;
*/
        /* 각각의 호스트 클릭 이벤트
        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text_chat_status.setText("친구");

            }
        });

        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text_chat_status.setText("채팅");

            }
        });

        tabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text_chat_status.setText("기타1");

            }
        });

        tabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text_chat_status.setText("기타2");

            }
        });

        */

    }

    private void ShowDialog()
    {

        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.friend_information, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.setTitle("해당유저 친구추가 여부");

        TextView infor_name = (TextView) dialogLayout.findViewById(R.id.infor_name);
        TextView infor_input_name = (TextView) dialogLayout.findViewById(R.id.infor_input_name);
        TextView infor_id = (TextView) dialogLayout.findViewById(R.id.infor_id);
        TextView infor_input_id = (TextView) dialogLayout.findViewById(R.id.infor_input_id);

        Button btn_ok=(Button)dialogLayout.findViewById(R.id.btn_ok);
        Button btn_cancle=(Button)dialogLayout.findViewById(R.id.btn_cancle);

        infor_input_name.setText(friend_name);
        infor_input_id.setText(friend_id);

        //글꼴 변경
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/HoonWhitecatR.ttf");
        infor_name.setTypeface(font);
        infor_input_name.setTypeface(font);
        infor_id.setTypeface(font);
        infor_input_id.setTypeface(font);
        btn_ok.setTypeface(font);
        btn_cancle.setTypeface(font);

        //추가 버튼
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = db.rawQuery("SELECT id, friend_id FROM FRIENDS",null);

                //입력한 친구아이디가 목록에 있으면 추가하지 않음
                while(cursor.moveToNext()){
                    String db_id=cursor.getString(cursor.getColumnIndex("id"));
                    String db_friend_id=cursor.getString(cursor.getColumnIndex("friend_id"));
                    if(db_id.equals(user_id)&&edit_search_friend.getText().toString().equals(db_friend_id)){
                        Toast.makeText(getApplicationContext(), "'"+edit_search_friend.getText().toString()+"' 가 이미 친구입니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                /*
                member_list.get(friend_list_num).add_friend_list(friend_id);
                Toast.makeText(getApplicationContext(), "'"+edit_search_friend.getText().toString()+"' 가 친구되었습니다.",Toast.LENGTH_SHORT).show();
                */

                //입력한 아아디 친구등록
                db.execSQL("INSERT INTO FRIENDS (id, friend_id) VALUES ('"+user_id+"','"+friend_id+"');");
                Toast.makeText(getApplicationContext(), "'"+edit_search_friend.getText().toString()+"' 가 친구되었습니다.",Toast.LENGTH_SHORT).show();

                cursor = db.rawQuery("SELECT id as _id, friend_id FROM FRIENDS WHERE id='"+user_id+"';",null);
                String[] from = {"friend_id" };
                int[] to = {android.R.id.text1};
                adapter = new SimpleCursorAdapter(chat_list.this, android.R.layout.simple_list_item_2, cursor, from, to);
                friend_list.setAdapter(adapter);

                edit_search_friend.setText("");
                friend_id="";
                friend_name="";
                myDialog.cancel();

            }
        });

        //취소 버튼
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_search_friend.setText("");
                friend_id="";
                friend_name="";
                myDialog.cancel();

            }
        });

        myDialog.setContentView(dialogLayout);
        myDialog.show();

    }

}
