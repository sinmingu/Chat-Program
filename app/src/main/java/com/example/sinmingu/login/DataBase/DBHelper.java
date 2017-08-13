package com.example.sinmingu.login.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sinmingu on 2017-08-04.
 */
public class DBHelper extends SQLiteOpenHelper {
    //데이터베이스 버젼
    public static final int DATABASE_VERSION = 1;

    //데이터베이스 이름
    public static final String DATABASE_NAME = "CHATPROGRAM";

    private SQLiteDatabase db = null;

    //테이블이름 및 생성 쿼리
    final String USER = "CREATE TABLE USER (id TEXT PRIMARY KEY NOT NULL, nickname TEXT NOT NULL, password TEXT, regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
    public final static int USER_NUMBER = 0;
    //    final String CHATROOM = "CREATE TABLE CHATROOM (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);";
    final String CHATROOM = "CREATE TABLE CHATROOM (id INTEGER PRIMARY KEY NOT NULL, regdate regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
    final String CHATROOMMEMBERLIST = "CREATE TABLE CHATROOMMEMBERLIST (f_chatroom_id INTEGER NOT NULL, f_user_id TEXT NOT NULL, readdate TIMESTAMP, FOREIGN KEY(f_chatroom_id) REFERENCES CHATROOM(id) ON DELETE CASCADE ON UPDATE CASCADE , FOREIGN KEY(f_user_id) REFERENCES USER(id) ON UPDATE CASCADE ON DELETE CASCADE, PRIMARY KEY(f_chatroom_id,f_user_id));";
    //    final String CHATMESSAGE = "CREATE TABLE CHATMESSAGE (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, f_chatroom_id INTEGER NOT NULL, f_send_id TEXT NOT NULL, message TEXT NOT NULL, regdate TIMESTAMP, FOREIGN KEY(f_chatroom_id) REFERENCES CHATROOM(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(f_send_id) REFERENCES USER(id) ON UPDATE CASCADE);";
    final String CHATMESSAGE = "CREATE TABLE CHATMESSAGE (id INTEGER PRIMARY KEY NOT NULL, f_chatroom_id INTEGER NOT NULL, f_send_id TEXT NOT NULL, message TEXT NOT NULL, regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(f_chatroom_id) REFERENCES CHATROOM(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(f_send_id) REFERENCES USER(id) ON UPDATE CASCADE);";
    final String FRIENDS = "CREATE TABLE FRIENDS (id TEXT NOT NULL, friend_id TEXT NOT NULL, regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(id) REFERENCES USER(id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY(friend_id) REFERENCES USER(id) ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY(id,friend_id));";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        db.execSQL(USER);
        db.execSQL(CHATROOM);
        db.execSQL(CHATROOMMEMBERLIST);
        db.execSQL(CHATMESSAGE);
        db.execSQL(FRIENDS);
        Log.d("테이블생성","테이블생성");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //테이블 삭제
        db.execSQL("DROP TABLE IF EXISTS " + CHATMESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + CHATROOMMEMBERLIST);
        db.execSQL("DROP TABLE IF EXISTS " + CHATROOM);
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        db.execSQL("DROP TABLE IF EXISTS " + FRIENDS);
        //테이블 재생성
        onCreate(db);
    }

    public long getChatMsgTableAllRowCount(){
        Cursor mCount = db.rawQuery("select count(*) from CHATMESSAGE", null);
        mCount.moveToFirst();
        long count= mCount.getInt(0);
        mCount.close();
        return count;
    }

    public void openDB(){
        db = getWritableDatabase();
    }
    public void closeDB(){
        db.close();
    }

    public Cursor getAllUserData(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from USER",null);
    }

    //특정 날짜 이전의 메세지를 count개수만큼 가져온다.
    //최신 메세지부터 count만큼 불러온다.
    public Cursor getBeforeMsg(int count){
        SQLiteDatabase db = getWritableDatabase();
        //오름차순
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT * FROM CHATMESSAGE ORDER BY regdate DESC LIMIT "+String.valueOf(count)+") ORDER BY regdate ASC;",null);
        return cursor;
    }


    public Cursor getMessageData(int roomid){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from CHATMESSAGE WHERE f_chatroom_id = 10",null);
        return cursor;
    }

    public void insertChatroom(int id){
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO CHATROOM VALUES('" + id + "','" + null + "');");
    }

    public void insertMessage(int chatroomnum, String userid, String message){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO CHATMESSAGE VALUES(" + null + ", " + chatroomnum + ", '" + userid + "', '" + message + "','" + System.currentTimeMillis() + "');");
    }

    //test
    public void insert(String id, String password){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO USER VALUES('" + null + "', '" + id + "', '" + password + "', '" + null + "');");
        db.close();
    }

    //test
    public void delete(String id){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM USER WHERE id = '" + id + "';");
        db.close();
    }

//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//    public String MillToDate(long mills) {
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//        String date = (String) formatter.format(new Timestamp(mills));
//
//        return date;
//    }
//    public long DateToMill(String date) {
//
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//
//        Date trans_date = null;
//        try {
//            trans_date = formatter.parse(date);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return trans_date.getTime();
//    }


}
