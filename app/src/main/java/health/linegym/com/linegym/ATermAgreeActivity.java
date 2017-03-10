package health.linegym.com.linegym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Created by jongmoon on 2016-12-10.
 */

public class ATermAgreeActivity extends BaseLineGymActivity{

    TextView mTitlePrivate, mTitleServie, mBtnCancel, mBtnAgree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_layout_term_agree);
        mTitlePrivate = (TextView) findViewById(R.id.tv_title_private);
        mTitlePrivate.setText(Html.fromHtml("<u>개인정보 취급 방침</u>"));
        mTitleServie = (TextView) findViewById(R.id.tv_title_service);
        mTitleServie.setText(Html.fromHtml("<u>서비스 이용방침</u>"));
        mBtnCancel = (TextView) findViewById(R.id.btn_term_cancel);
        mBtnAgree = (TextView) findViewById(R.id.btn_term_agree);

        mTitlePrivate.setOnClickListener(mClickListner);
        mTitleServie.setOnClickListener(mClickListner);
        mBtnAgree.setOnClickListener(mClickListner);
        mBtnCancel.setOnClickListener(mClickListner);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    View.OnClickListener mClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.tv_title_private: {
                    Intent intent = new Intent(ATermAgreeActivity.this, AWebView.class);
                    intent.putExtra("is_private", true);
                    startActivity(intent);
                    break;
                }
                case R.id.tv_title_service: {
                    Intent intent = new Intent(ATermAgreeActivity.this, AWebView.class);
                    intent.putExtra("is_private", false);
                    startActivity(intent);
                    break;
                }
                case R.id.btn_term_cancel: {
                    System.exit(0);
                    break;
                }
                case R.id.btn_term_agree: {
                    SharedPreferences pref = getSharedPreferences(LineGymDefine.KEY_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(LineGymDefine.KEY_PREF_KEY_AGREE_TERM, true);
                    editor.commit();

                    Intent intent = new Intent(ATermAgreeActivity.this, ALoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    };
}
