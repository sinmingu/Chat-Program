package com.example.sinmingu.login.Activitys;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinmingu.login.DataBase.DBHelper;
import com.example.sinmingu.login.R;
import com.example.sinmingu.login.RecyclerViewScroll.EndlessRecyclerOnScrollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sinmingu on 2017-08-11.
 */

public class ChatRoom extends SupportActivity {
    private Button msgSend = null;
    private Button addImg = null;
    private EditText inputMsg = null;
    private ImageButton scrolldeep = null;
    private RecyclerView chatListView = null;
    private ChatRoomAdapter adapter = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private DBHelper dbHelper = null;
    //현재 채팅방에 표시된 메세지의 개수, 이전 채팅방에 표시되었던 메세지의 개수
    private long msgCount = 15, prevMsgCount = 0;
    //현재 가장 상단에 위치하는 메세지의 regdate.
    private long positionInterval;
    //채팅방에 db상 모든 메세지가 로딩된 경우 true로 활성화.
    private boolean isAllMsgLoaded = false;


    //test 객체
    private ArrayList<String> chatlist = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        //초기화
        init();


    }


    private void init(){
        //db객체 생성
        dbHelper = new DBHelper(getApplicationContext(),DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
        dbHelper.openDB();


        //스크롤 위치 체크 및 아래로 이동 버튼
        scrolldeep = (ImageButton)findViewById(R.id.act_chatroom_scrolldeep);
        scrolldeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //리스트뷰 맨 아래로 이동
                chatListView.scrollToPosition(adapter.getItemCount()-1);
                scrolldeep.setVisibility(View.GONE);
            }
        });


        //이미지 추가 버튼
        addImg = (Button)findViewById(R.id.act_chatroom_addImgBtn);
        addImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbHelper.insertChatroom(10);
                dbHelper.insert("임현창","010101");
                return false;
            }
        });

        //메세지 전송 버튼
        msgSend = (Button)findViewById(R.id.act_chatroom_sendBtn);
        msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 메시지 전송버튼 클릭 시 작업
                dbHelper.insertMessage(10,"임현창",inputMsg.getText().toString());

                //새로운 cursor를 db로부터 가져온다.(background)
                //
                new getChatMessageTask().execute(String.valueOf(msgCount+1));
            }
        });


        //메세지 내용 EditText
        inputMsg = (EditText)findViewById(R.id.act_chatroom_msg);
        inputMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //입력되기 전에
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //입력 중일때
                String message = inputMsg.getText().toString();
                if(message.replace(" ","").length() > 0) {
                    msgSend.setVisibility(View.VISIBLE);
                    addImg.setVisibility(View.GONE);
                }else{
                    msgSend.setVisibility(View.GONE);
                    addImg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //입력 후에
            }
        });


        //리스트뷰 어댑터
        adapter = new ChatRoomAdapter( getApplicationContext());

        //리스트뷰 레이아웃관련 설정 객체 생성(리스트뷰형태)
        layoutManager = new LinearLayoutManager(this);

        //리스트뷰(RecyclerView)
        chatListView = (RecyclerView)findViewById(R.id.act_chatroom_recyclerview);
        chatListView.setHasFixedSize(true);
        chatListView.setLayoutManager(layoutManager);
        chatListView.setAdapter(adapter);
        //메세지 데이터읽어옴
        new getChatMessageTask().execute(String.valueOf(msgCount));

        /**
         * @onLastPosition: 리스트뷰의 마지막에 닿으면 호출됨
         * @onFirstPosition: 리스트뷰의 처음에 닿으면 호출됨
         * **/
        chatListView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onFirstPosition(int firstPagePosition, int lastPagePosition) {
                isAllMsgLoaded = true;
                positionInterval = lastPagePosition;
                //모든 메세지를 다 불러왔을때.
                if(dbHelper.getChatMsgTableAllRowCount() == adapter.getItemCount()){
                    return;
                }

                //맨 위일 떄.
                //메세지 추가 불러오기(현재 메세지에서 30% 더 가져온다.)
                prevMsgCount = msgCount;
                msgCount+= (int)msgCount *0.5;
                new getChatMessageTask().execute(String.valueOf(msgCount));
            }

            @Override
            public void onLastPosition(int lastPagePosition) {
                scrolldeep.setVisibility(View.GONE);
            }

            @Override
            public void onToggleMoveToDown(boolean toggle) {
                if(toggle){
                    scrolldeep.setVisibility(View.VISIBLE);
                }else{
                    scrolldeep.setVisibility(View.GONE);
                }
            }
        });
    }

    class ChatRoomAdapter extends RecyclerView.Adapter{
        private Cursor mCursor;
        private Context mCtx;
        private long lastPosition = -1;
        LinearLayout.LayoutParams left_params = null;
        LinearLayout.LayoutParams right_params = null;

        public ChatRoomAdapter(Context context){
            mCtx = context;
            left_params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            left_params.gravity = Gravity.START;
            left_params.setMargins(10,20,10,20);
            right_params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            right_params.gravity = Gravity.END;
            right_params.setMargins(10,20,10,20);
        }
        public ChatRoomAdapter(Cursor cursor, Context context){
            this(context);
            mCursor = cursor;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            lastPosition = position;

            mCursor.moveToPosition(position);
            //textview에 텍스트 적용
            ((ViewHolder)holder).msg.setText(mCursor.getString(mCursor.getColumnIndex("message")));
            if(position % 3 == 0) {
                ((ViewHolder)holder).msg.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.l_msg_balloon));
                ((ViewHolder)holder).msgLayout.setLayoutParams(left_params);
                ((ViewHolder)holder).rightcount.setText(MillToDate(mCursor.getLong(mCursor.getColumnIndex("regdate"))));
                ((ViewHolder)holder).leftcount.setVisibility(View.GONE);
                ((ViewHolder)holder).rightcount.setVisibility(View.VISIBLE);
            }
            else {
                ((ViewHolder)holder).msg.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.r_msg_balloon));
                ((ViewHolder)holder).msgLayout.setLayoutParams(right_params);
                ((ViewHolder)holder).leftcount.setText(MillToDate(mCursor.getLong(mCursor.getColumnIndex("regdate"))));
                ((ViewHolder)holder).leftcount.setVisibility(View.VISIBLE);
                ((ViewHolder)holder).rightcount.setVisibility(View.GONE);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_chatroom_chatmsg_design,parent,false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }


        @Override
        public int getItemCount() {
            if (mCursor != null) {
                return mCursor.getCount();
            }
            else return 0;
        }


        public void setCursor(Cursor cur) {
            Log.d("커서바뀜","커서바뀜");
            if(this.mCursor != null){
                this.mCursor.close();
            }
            mCursor = cur;
        }


        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView msg;
            public TextView leftcount;
            public TextView rightcount;
            public LinearLayout msgLayout;

            public ViewHolder(View view){
                super(view);
                msgLayout = (LinearLayout)view.findViewById(R.id.chatmsg_design_msg_layout);
                msg = (TextView)view.findViewById(R.id.chatmsg_design_msg);
                msg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(mCtx,"길게눌림",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                leftcount = (TextView)view.findViewById(R.id.chatmsg_design_readcount_left);
                rightcount = (TextView)view.findViewById(R.id.chatmsg_design_readcount_right);
            }
        }

    }

    /*
    * @params[0] : 얻으려는 메세지의 개수
    * @params[1] : 페이지 이동하여 얻는 메세지 인지 아닌지(메세지 로딩) 여부(true: 메세지로딩 false:메세지로딩x)
    */
    class getChatMessageTask extends AsyncTask<String, Void, Boolean> {
        //background작업 들어가기 전 호출
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //edittext의 내용을 비운다.
            inputMsg.setText(null);
        }

        //background작업(params는 기준 날짜이다. db에서 파라미터 날짜로부터 일정개수만큼 이전날짜의 메세지를 가져온다)
        @Override
        protected Boolean doInBackground(String... params) {
            Cursor cursor = null;
            //파라미터1이 ""과 같다면 getBeforeMsg에 null전달
            //커서 가져와서 adapter에 삽입
            cursor = dbHelper.getBeforeMsg(Integer.valueOf(params[0]));

            if(cursor == null){
                return false;
            }else{
                adapter.setCursor(cursor);
            }
            return true;
        }

        //background작업 후 호출
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                adapter.notifyDataSetChanged();
                msgCount = adapter.getItemCount();
                //recyclerview의 스크롤을 맨 밑으로 내림
                //메세지로딩일 경우는 내리지 않고, 메세지가 추가되거나 내용을 한번에 가져올 경우 내린다.
                if(!isAllMsgLoaded) {
                    chatListView.scrollToPosition(adapter.getItemCount() - 1);
                }else{
                    Log.d("현재 메세지 개수: ",String.valueOf(msgCount));
                    Log.d("이전메세지개수: ",String.valueOf(prevMsgCount));
                    Log.d("추가된메세지개수: ",String.valueOf(msgCount-prevMsgCount));
                    Log.d("수정된 포지션: ",String.valueOf(msgCount-1-prevMsgCount));
                    chatListView.scrollToPosition((int)(msgCount-1 - prevMsgCount + positionInterval));

                }
                isAllMsgLoaded = false;
            }
        }
    }

    //밀리세컨드를 날짜로 변환
    public String MillToDate(long mills) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date ( mills );
        String resultDate = formatter.format(date);
        return resultDate;
    }

    //날짜를 밀리세컨드로 변환
    public long dateToMill(String convDate){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long result = 0;
        try{
            Date sTom = dt.parse(convDate);
            result = sTom.getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}