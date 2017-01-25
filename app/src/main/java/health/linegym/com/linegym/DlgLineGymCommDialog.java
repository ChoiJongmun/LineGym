package health.linegym.com.linegym;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Created by innotree12 on 2016-11-07.
 */

public class DlgLineGymCommDialog extends Dialog {


    TextView mMessage, mTitle;
    Button mBtnConfirm;

    public DlgLineGymCommDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflater.inflate(R.layout.dlg_linegym_comm_pop, null));

        mMessage = (TextView)findViewById(R.id.tv_dlg_comm_message);
        mTitle = (TextView) findViewById(R.id.dlg_comm_title);

        mBtnConfirm = (Button) findViewById(R.id.btn_dlg_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setDlgMessage(String message) {
        mMessage.setText(message);
    }

    public void setDlgTitle(String title) {
        mTitle.setText(title);
    }

}
