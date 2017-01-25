package health.linegym.com.linegym;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.object.MemberInfo;
import health.linegym.com.linegym.object.MyLastDate;

/**
 * Created by jongmun on 2017-01-14.
 */

public class AMyInfoActivity extends BaseLineGymActivity implements IResultListener{

    MemberInfo mMyInfo;
    ImageView mBarcord;
    TextView mLimitDate, mLastAttendDate, mLastInbodyDate;
    MyLastDate lastdate = new MyLastDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_info);
        mMyInfo = (MemberInfo)getIntent().getSerializableExtra("my_info");
        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);
        title.setText("나의 정보");
        mBarcord = (ImageView) findViewById(R.id.iv_barcode);
        mBarcord.setImageBitmap(createBarcode(mMyInfo.getMem_no()));
        mLimitDate = (TextView) findViewById(R.id.tv_my_limit_date);
        mLimitDate.setText(mMyInfo.getLimit_date());
        mLastAttendDate = (TextView) findViewById(R.id.tv_my_last_attend_date);
        mLastInbodyDate = (TextView) findViewById(R.id.tv_last_inbody_date);
        HttpConnector conn = new HttpConnector("my_last_date", this);
        conn.getMyLastDate(mMyInfo.getName(), mMyInfo.getInbody_mem_no());
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

    public Bitmap createBarcode(String code){


        Bitmap bitmap =null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int WIDTH = 840;
            final int HEIGHT = 160;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_39, WIDTH, HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public void onSuccess(String type, String result_string) {

        Gson gson = new Gson();
        lastdate = gson.fromJson(result_string, MyLastDate.class);
        mHandler.sendEmptyMessage(0);
    }

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
            if(lastdate.getAttend_last_date().equals("none")) {
                mLastAttendDate.setText("아직 출석하신 적이 없네요!");
            }else {
                mLastAttendDate.setText(lastdate.getAttend_last_date());
            }
            if(lastdate.getInbody_last_date().equals("none")) {
                mLastInbodyDate.setText("아직 인바디 측정을 하신적이 없네요!");
            }else {
                String datetimes = lastdate.getInbody_last_date();
                String year = datetimes.substring(0,4);
                String month = datetimes.substring(4,6);
                String day = datetimes.substring(6,8);
                mLastInbodyDate.setText(String.format("%s-%s-%s",year, month, day));
            }
        }
    };

}
