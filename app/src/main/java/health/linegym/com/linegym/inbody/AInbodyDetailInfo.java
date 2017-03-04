package health.linegym.com.linegym.inbody;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import health.linegym.com.linegym.BaseLineGymActivity;
import health.linegym.com.linegym.R;
import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.object.Inbody;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2017-02-08.
 */

public class AInbodyDetailInfo extends BaseLineGymActivity implements IResultListener{

    private LineChart mChart;

    MemberInfo mMyInfo;
    Inbody inbody = new Inbody();

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;
    LineDataSet mWeightData, mMuscleData, mBodyFatData, mBodyFatPerData, mShipFat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_layout_inbody_detail_graph);
        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        title.setText("인바디현황");
        mMyInfo = (MemberInfo) getIntent().getSerializableExtra("my_info");

        mChart = (LineChart) findViewById(R.id.inbody_detail_info_chart);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setData(new LineData());

        initDataSet();

        HttpConnector conn = new HttpConnector("Inbody", this);
        conn.getInbodyList(mMyInfo.getInbody_mem_no());
    }

    @Override
    public void onSuccess(String type, String result_string) {
        Gson gson = new Gson();
        inbody = gson.fromJson(result_string, Inbody.class);
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

            for(int i = inbody.getRows().size()-1; i >= 0; i--) {
                Inbody.InbodyData data = inbody.getRows().get(i);
                mWeightData.addEntry(new Entry((float)inbody.getRows().size()- i, Float.parseFloat(data.getWeight())));
                mMuscleData.addEntry(new Entry((float)inbody.getRows().size()- i, Float.parseFloat(data.getMuscle())));
                mBodyFatData.addEntry(new Entry((float)inbody.getRows().size()- i, Float.parseFloat(data.getFat())));
                mBodyFatPerData.addEntry(new Entry((float)inbody.getRows().size()- i, Float.parseFloat(data.getFat_per())));
            }

            mWeightData.notifyDataSetChanged();
            mMuscleData.notifyDataSetChanged();
            mBodyFatData.notifyDataSetChanged();
            mBodyFatPerData.notifyDataSetChanged();

            mChart.getData().addDataSet(mWeightData);
            mChart.getData().addDataSet(mMuscleData);
            mChart.getData().addDataSet(mBodyFatData);
            mChart.getData().addDataSet(mBodyFatPerData);

            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    };

    private void initDataSet() {
        mWeightData = new LineDataSet(null, "몸무게");
        mWeightData.setLineWidth(2.5f);
        mWeightData.setCircleRadius(4.5f);
        mWeightData.setColor(Color.rgb(240, 99, 99));
        mWeightData.setCircleColor(Color.rgb(240, 99, 99));
        mWeightData.setHighLightColor(Color.rgb(190, 190, 190));
        mWeightData.setAxisDependency(YAxis.AxisDependency.LEFT);
        mWeightData.setValueTextSize(10f);

        mMuscleData = new LineDataSet(null, "근육량");
        mMuscleData.setLineWidth(2.5f);
        mMuscleData.setCircleRadius(4.5f);
        mMuscleData.setColor(Color.rgb(110, 35, 35));
        mMuscleData.setCircleColor(Color.rgb(110, 35, 35));
        mMuscleData.setHighLightColor(Color.rgb(110, 35, 35));
        mMuscleData.setAxisDependency(YAxis.AxisDependency.LEFT);
        mMuscleData.setValueTextSize(10f);

        mBodyFatData = new LineDataSet(null, "체지방량");
        mBodyFatData.setLineWidth(2.5f);
        mBodyFatData.setCircleRadius(4.5f);
        mBodyFatData.setColor(Color.rgb(170, 210, 195));
        mBodyFatData.setCircleColor(Color.rgb(170, 210, 195));
        mBodyFatData.setHighLightColor(Color.rgb(190, 190, 190));
        mBodyFatData.setAxisDependency(YAxis.AxisDependency.LEFT);
        mBodyFatData.setValueTextSize(10f);

        mBodyFatPerData = new LineDataSet(null, "체지방률");
        mBodyFatPerData.setLineWidth(2.5f);
        mBodyFatPerData.setCircleRadius(4.5f);
        mBodyFatPerData.setColor(Color.rgb(240, 210, 4));
        mBodyFatPerData.setCircleColor(Color.rgb(240, 210, 4));
        mBodyFatPerData.setHighLightColor(Color.rgb(190, 190, 190));
        mBodyFatPerData.setAxisDependency(YAxis.AxisDependency.LEFT);
        mBodyFatPerData.setValueTextSize(10f);


    }

}
