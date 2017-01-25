package health.linegym.com.linegym.inbody;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import health.linegym.com.linegym.BaseLineGymActivity;
import health.linegym.com.linegym.R;

/**
 * Created by innotree12 on 2016-11-08.
 */

public class ALastInbodyData extends BaseLineGymActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_inbody_detail_graph);
        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        title.setText("이전 측정 내역 영역");
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);
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

}
