package health.linegym.com.linegym.inbody;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import health.linegym.com.linegym.BaseLineGymActivity;
import health.linegym.com.linegym.R;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2017-02-06.
 */

public class ADetailItemList extends BaseLineGymActivity {

    MemberInfo mMyInfo;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.a_list_inbody_detail_item);
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);

        mMyInfo = (MemberInfo)getIntent().getSerializableExtra("my_info");

        findViewById(R.id.item_body_point).setOnClickListener(mClickListener);
        findViewById(R.id.item_muscle_fat).setOnClickListener(mClickListener);
        findViewById(R.id.item_body_balance).setOnClickListener(mClickListener);
        findViewById(R.id.item_bodywater).setOnClickListener(mClickListener);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("my_info", mMyInfo);
            switch (view.getId()) {
                case R.id.item_body_point: {
                    intent.setClass(ADetailItemList.this, AInbodyBodyPoint.class);
                    break;
                }
                case R.id.item_muscle_fat : {
                    intent.setClass(ADetailItemList.this, AInbodyMuscleFat.class);
                    break;
                }
                case R.id.item_bodywater : {
                    intent.setClass(ADetailItemList.this, AInbodyBodyWater.class);
                    break;
                }
                case R.id.item_body_balance : {
                    intent.setClass(ADetailItemList.this, AInbodyWeightControl.class);
                    break;
                }
            }
            startActivity(intent);
        }
    };
}
