package health.linegym.com.linegym;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by innotree12 on 2016-11-08.
 */

public class BaseLineGymActivity extends AppCompatActivity{

    public View.OnClickListener onBaseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.comm_title_btn_navi_back : {
                    finish();
                }
            }
        }
    };

}
