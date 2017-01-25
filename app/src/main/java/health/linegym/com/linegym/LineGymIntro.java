package health.linegym.com.linegym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2016-10-27.
 */

public class LineGymIntro extends BaseLineGymActivity implements IResultListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_intro_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mHandler.sendEmptyMessageDelayed(0, 1500);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            SharedPreferences sharedPreferences = getSharedPreferences("member", Context.MODE_PRIVATE);
            String mem_name = sharedPreferences.getString("mem_name", "");
            String mem_phone = sharedPreferences.getString("mem_phone", "");
            if(mem_name.isEmpty()) {
                SharedPreferences pref = getSharedPreferences(LineGymDefine.KEY_PREF_NAME, Context.MODE_PRIVATE);
                if (pref.getBoolean(LineGymDefine.KEY_PREF_KEY_AGREE_TERM, false)) {
                    startActivity(new Intent(LineGymIntro.this, ALoginActivity.class));
                } else {
                    startActivity(new Intent(LineGymIntro.this, ATermAgreeActivity.class));
                }
                finish();
            }else {
                HttpConnector connector = new HttpConnector("login", LineGymIntro.this);
                connector.login(mem_name, mem_phone);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSuccess(String type, String result_string) {
        Gson gson = new GsonBuilder().create();
        MemberInfo mem_info = new MemberInfo();

        mem_info = gson.fromJson(result_string, MemberInfo.class);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mem_info", mem_info);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed(String type, String result_string) {
        SharedPreferences sharedPreferences = getSharedPreferences("member", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onException(String type, String error_message) {
        SharedPreferences sharedPreferences = getSharedPreferences("member", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        mHandler.sendEmptyMessage(0);
    }

}
