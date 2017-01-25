package health.linegym.com.linegym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;

import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2016-10-28.
 */

public class ALoginActivity extends BaseLineGymActivity implements IResultListener{

    private CheckBox mAutoLogin;
    private EditText mMemName;
    private EditText mMemPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login_layout);

        mMemName = (EditText) findViewById(R.id.edt_mem_name);
        mMemPhoneNo = (EditText) findViewById(R.id.edt_mem_phone_no);
        mAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);

        Button mLoginButton = (Button)findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpConnector connector = new HttpConnector("login", ALoginActivity.this);
                String phone_num = mMemPhoneNo.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMemPhoneNo.setText(PhoneNumberUtils.formatNumber(phone_num, Locale.getDefault().getCountry()));
                } else {
                    mMemPhoneNo.setText(PhoneNumberUtils.formatNumber(phone_num)); //Deprecated method
                }

                connector.login(mMemName.getText().toString(), mMemPhoneNo.getText().toString());
//                startActivity(new Intent(ALoginActivity.this, MainActivity.class));
//                finish();
            }
        });
    }

    @Override
    public void onSuccess(String type, String result_string) {
        System.out.println("success = " + result_string);

        Gson gson = new GsonBuilder().create();
        MemberInfo mem_info = new MemberInfo();

        mem_info = gson.fromJson(result_string, MemberInfo.class);
        if(mAutoLogin.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences("member", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mem_name", mMemName.getText().toString());
            editor.putString("mem_phone", mem_info.getPhone());
            editor.commit();
        }
        Intent intent = new Intent(ALoginActivity.this, MainActivity.class);
        intent.putExtra("mem_info", mem_info);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed(String type, String result_string) {
        System.out.println("fail = " + result_string);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onException(String type, String error_message) {
        System.out.println("exception = " + error_message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            DlgLineGymCommDialog dlg = new DlgLineGymCommDialog(ALoginActivity.this);
            dlg.setDlgTitle("로그인 실패");
            dlg.setDlgMessage("로그인 정보를 다시 확인해 주세요");
            dlg.show();
        }
    };

}
