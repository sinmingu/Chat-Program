package com.example.sinmingu.login.FirebaseService;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ihc on 2017-08-13.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //TODO: 토큰이 변경되었을 때, 서버에 업로드(ui스레드가 아니기 때문에 async를 써야하는지? -> 일단 async없이 통신해보기)
        String token = FirebaseInstanceId.getInstance().getToken();

    }

    class uploadTokenToServerTask extends AsyncTask<String, Void, Boolean> {
        //background작업 들어가기 전 호출
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        //background작업(params는 기준 날짜이다. db에서 파라미터 날짜로부터 일정개수만큼 이전날짜의 메세지를 가져온다)
        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }

        //background작업 후 호출
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }
}
