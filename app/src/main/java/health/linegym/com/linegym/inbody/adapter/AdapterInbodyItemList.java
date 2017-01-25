package health.linegym.com.linegym.inbody.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import health.linegym.com.linegym.R;
import health.linegym.com.linegym.object.Inbody;

/**
 * Created by innotree12 on 2016-11-01.
 */

public class AdapterInbodyItemList extends BaseAdapter {

    List<Inbody.InbodyData> mDataList;
    LayoutInflater mInflater;
    Context mContext;
    public AdapterInbodyItemList(Context context, List<Inbody.InbodyData> data_list) {
        mContext = context;
        mDataList = data_list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Inbody.InbodyData getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int postion, View view, ViewGroup viewGroup) {
        Inbody.InbodyData data = getItem(postion);
        InbodyItemViewHolder vHolder;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_inbody_list_view, null);
            vHolder = new InbodyItemViewHolder();
            vHolder.mBodyPoint = (TextView) view.findViewById(R.id.tv_body_point);
            vHolder.mDateTime = (TextView) view.findViewById(R.id.tv_inbody_date);
            vHolder.mWeight = (TextView) view.findViewById(R.id.tv_inbody_weight);
            vHolder.mMuscle = (TextView) view.findViewById(R.id.tv_inbody_murcle);
            vHolder.mFat = (TextView) view.findViewById(R.id.tv_inbody_fat);
            vHolder.mFatPer = (TextView) view.findViewById(R.id.tv_inbody_fat_percent);

            view.setTag(vHolder);
        }else {
            vHolder = (InbodyItemViewHolder)view.getTag();
        }

        vHolder.mBodyPoint.setText(data.getBody_point().substring(0,data.getBody_point().lastIndexOf(".")));
//        String datatime = data.getDatetime().substring(0, 9);
        String year = data.getDatetime().substring(0,4);
        String month = data.getDatetime().substring(4,6);
        String day = data.getDatetime().substring(6,8);
//        vHolder.mTvTitle.setText(titles[i]);
        vHolder.mDateTime.setText(String.format("%s.%s.%s", year, month, day));
        vHolder.mWeight.setText("몸무게 : " + Float.parseFloat(data.getWeight()) + "kg");
        vHolder.mMuscle.setText("근육량 : " + Float.parseFloat(data.getMuscle())  + "kg");
        vHolder.mFat.setText("체지방량 : " + Float.parseFloat(data.getFat()) + "kg");
        vHolder.mFatPer.setText("체지방률 : " + Float.parseFloat(data.getFat_per()) + "%");

        return view;
    }
}
