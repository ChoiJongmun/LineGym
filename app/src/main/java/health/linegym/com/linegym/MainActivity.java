package health.linegym.com.linegym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.triggertrap.seekarc.SeekArc;

import java.lang.reflect.Member;

import health.linegym.com.linegym.attend.AMonthAttend;
import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.inbody.ADetailInbodyInfo;
import health.linegym.com.linegym.object.MainData;
import health.linegym.com.linegym.object.MemberInfo;

public class MainActivity extends BaseLineGymActivity implements IResultListener{

    final int MESSAGE_SET_DATA = 0;
    LinearLayout mBodyPointLayout, mEmptyInbody;
    TextView mTitleBodyPoint, mAttendDayCount, mLastBodyPoint;
    SeekArc mAttendSeekArc, mBodyPointSeekArc;
    MainData main_data;
    MemberInfo mem_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mem_info = (MemberInfo)getIntent().getSerializableExtra("mem_info");

        mBodyPointLayout = (LinearLayout) findViewById(R.id.layout_body_point_view);
        mEmptyInbody = (LinearLayout) findViewById(R.id.layout_inbody_empty);

        mAttendSeekArc = (SeekArc) findViewById(R.id.seekArcAttention);
        mBodyPointSeekArc = (SeekArc) findViewById(R.id.seekArcBody);

        mTitleBodyPoint = (TextView) findViewById(R.id.tv_title_body_point);
        mTitleBodyPoint.setText(Html.fromHtml(getString(R.string.title_body_point)));
        mAttendDayCount = (TextView) findViewById(R.id.tv_attend_day);
        mLastBodyPoint = (TextView) findViewById(R.id.tv_current_body_point);

        FrameLayout mAttendLayout = (FrameLayout) findViewById(R.id.layout_main_attend_view);
        FrameLayout mInbodyLayout = (FrameLayout) findViewById(R.id.layout_main_body_point_view);
        mAttendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AMonthAttend.class);
                intent.putExtra("my_info", mem_info);
                intent.putExtra("main_data", main_data);
                startActivity(intent);
            }
        });
        mInbodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ADetailInbodyInfo.class);
                intent.putExtra("my_info", mem_info);
                startActivity(intent);
            }
        });
        HttpConnector connector = new HttpConnector("main", MainActivity.this);
        connector.getMainData(mem_info.getName(), mem_info.getInbody_mem_no());


        ImageView mIvShowMyInfo = (ImageView) findViewById(R.id.btn_dlg_my_btn);
        mIvShowMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DlgLineGymMyInfo dlg = new DlgLineGymMyInfo(MainActivity.this);
//                dlg.show();
                Intent intent = new Intent(MainActivity.this, AMyInfoActivity.class);
                intent.putExtra("my_info", mem_info);
                startActivity(intent);
            }
        });

        ImageView mIvCallLineGym = (ImageView) findViewById(R.id.btn_call_linegyme);
        mIvCallLineGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:029807007"));
                startActivity(intent);
            }
        });

        ImageView mLogout = (ImageView) findViewById(R.id.btn_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("member", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, ALoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onSuccess(String type, String result_string) {
        System.out.println("result_string = " + result_string);
        Gson gson = new GsonBuilder().create();
        main_data = new MainData();
        main_data = gson.fromJson(result_string, MainData.class);
        mHandler.sendEmptyMessage(MESSAGE_SET_DATA);
    }

    @Override
    public void onFailed(String type, String result_string) {

    }

    @Override
    public void onException(String type, String error_message) {

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_SET_DATA :{
                    mAttendDayCount.setText(main_data.getCount());
                    mAttendSeekArc.setProgress(Integer.parseInt(main_data.getCount()));
                    if(main_data.getFitness().equals("none")) {
                        mBodyPointLayout.setVisibility(View.GONE);
                        mEmptyInbody.setVisibility(View.VISIBLE);
                    }else {
                        String fitness = main_data.getFitness().substring(0, main_data.getFitness().lastIndexOf("."));
                        mLastBodyPoint.setText(fitness);
                        mBodyPointSeekArc.setProgress(Integer.parseInt(fitness));
                    }

                }
            }
        }
    };
}
