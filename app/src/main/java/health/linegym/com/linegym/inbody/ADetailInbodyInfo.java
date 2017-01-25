package health.linegym.com.linegym.inbody;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.Member;

import health.linegym.com.linegym.BaseLineGymActivity;
import health.linegym.com.linegym.R;
import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.inbody.adapter.AdapterInbodyItemList;
import health.linegym.com.linegym.object.Inbody;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2016-10-31.
 */

public class ADetailInbodyInfo extends BaseLineGymActivity implements IResultListener {

    ListView mList;
    MemberInfo mMyInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_detail_indoby);

        mMyInfo = (MemberInfo) getIntent().getSerializableExtra("my_info");

        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        title.setText("인바디 상세현황");
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);

        mList = (ListView) findViewById(R.id.lv_inbody_item_list);

        HttpConnector conn = new HttpConnector("Inbody", this);
        conn.getInbodyList(mMyInfo.getInbody_mem_no());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess(String type, String result_string) {
        Inbody inbody = new Inbody();
        Gson gson = new Gson();
        inbody = gson.fromJson(result_string, Inbody.class);
        final AdapterInbodyItemList adapter = new AdapterInbodyItemList(this, inbody.getRows());
        mList.post(new Runnable() {
            @Override
            public void run() {
                mList.setAdapter(adapter);
            }
        });
    };

    @Override
    public void onFailed(String type, String result_string) {

    }

    @Override
    public void onException(String type, String error_message) {

    }
}
