package health.linegym.com.linegym;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import health.linegym.com.linegym.http.HttpConnector;

/**
 * Created by jongmoon on 2016-12-10.
 */

public class ATermAgreeActivity extends BaseLineGymActivity{

    CheckBox mMemberCheck, mPrivateInfoCheck, mCheckAll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_layout_term_agree);
        mCheckAll = (CheckBox) findViewById(R.id.check_all);
        mMemberCheck = (CheckBox) findViewById(R.id.check_member);
        mPrivateInfoCheck = (CheckBox) findViewById(R.id.check_private);

        mCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mMemberCheck.setChecked(true);
                    mPrivateInfoCheck.setChecked(true);
                }else {
                    mMemberCheck.setChecked(false);
                    mPrivateInfoCheck.setChecked(false);
                }
            }
        });

        findViewById(R.id.btn_term_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMemberCheck.isChecked() && mPrivateInfoCheck.isChecked()) {
                    SharedPreferences pref = getSharedPreferences(LineGymDefine.KEY_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(LineGymDefine.KEY_PREF_KEY_AGREE_TERM, true);
                    editor.commit();
                    Intent intent = new Intent(ATermAgreeActivity.this, ALoginActivity.class);
                    startActivity(intent);
                }else {
                    if(mMemberCheck.isChecked() == false) {
                        DlgLineGymCommDialog dlg = new DlgLineGymCommDialog(ATermAgreeActivity.this);
                        dlg.setDlgMessage("회원 약관에 동의해주세요");
                        dlg.show();
                    }else if(mPrivateInfoCheck.isChecked() == false) {
                        DlgLineGymCommDialog dlg = new DlgLineGymCommDialog(ATermAgreeActivity.this);
                        dlg.setDlgMessage("개인정보 활용약관에 동의해주세요");
                        dlg.show();
                    }
                }
            }
        });
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
}
