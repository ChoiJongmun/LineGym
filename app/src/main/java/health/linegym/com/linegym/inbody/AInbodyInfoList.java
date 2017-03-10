package health.linegym.com.linegym.inbody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

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

public class AInbodyInfoList extends BaseLineGymActivity implements IResultListener {

    ListView mInbodyList;
    LinearLayout mEmptyLayout;

    MemberInfo mMyInfo;
    Inbody inbody = new Inbody();

    private final int MESSAGE_SET_LIST_DATA = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_detail_indoby);

        mMyInfo = (MemberInfo) getIntent().getSerializableExtra("my_info");

        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        title.setText("인바디현황");
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);

        mInbodyList = (ListView) findViewById(R.id.lv_inbody_item_list);
        mEmptyLayout = (LinearLayout) findViewById(R.id.inbody_item_empty_layout);

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

        Gson gson = new Gson();
        inbody = gson.fromJson(result_string, Inbody.class);
        mHandler.sendEmptyMessage(MESSAGE_SET_LIST_DATA);

    };

    @Override
    public void onFailed(String type, String result_string) {

    }

    @Override
    public void onException(String type, String error_message) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_SET_LIST_DATA: {
                    if(inbody.getRows().size()> 0) {
                        final AdapterInbodyItemList adapter = new AdapterInbodyItemList(AInbodyInfoList.this, inbody.getRows());
                        mInbodyList.setAdapter(adapter);
                        mInbodyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(AInbodyInfoList.this, ADetailItemList.class);

                            }
                        });
                    }else {
                        mInbodyList.setVisibility(View.INVISIBLE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }
        }
    };

}
