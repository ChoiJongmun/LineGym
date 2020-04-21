package health.linegym.com.linegym.attend;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import health.linegym.com.linegym.BaseLineGymActivity;
import health.linegym.com.linegym.R;
import health.linegym.com.linegym.http.HttpConnector;
import health.linegym.com.linegym.http.IResultListener;
import health.linegym.com.linegym.object.AttendDay;
import health.linegym.com.linegym.object.AttendMonth;
import health.linegym.com.linegym.object.MainData;
import health.linegym.com.linegym.object.MemberInfo;

/**
 * Created by innotree12 on 2016-10-28.
 */

public class AMonthAttend extends BaseLineGymActivity implements OnChartValueSelectedListener, View.OnClickListener, IResultListener {

    protected HorizontalBarChart mChart;
    private CaldroidFragment caldroidFragment;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    private LinearLayout mPerDayLayout;
    private LinearLayout mPerMonthLayout;

    private TextView mPerMonthTv, mPerDayTv;
    private TextView mCurrnentMonthAttendCount;
    private View     mPerMonthUnder, mPerDayUnder;
    private String  mCurrentDate;
    private HashMap<String,AttendDay> mAttendDayMap = new HashMap<>();
    float spaceForBar = 10f;
    MemberInfo mMyInfo;
    MainData mMainData;
    private boolean is_init = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_attend_month_layout);
        TextView title = (TextView) findViewById(R.id.tv_comm_title);
        title.setText("출석현황");
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);
        FrameLayout btn_per_day = (FrameLayout) findViewById(R.id.btn_per_day_attend);
        FrameLayout btn_per_month = (FrameLayout) findViewById(R.id.btn_per_month_attend);

        mCurrnentMonthAttendCount = (TextView) findViewById(R.id.tv_attend_day_count);
        mPerDayTv = (TextView) findViewById(R.id.btn_per_day_attend_tv);
        mPerDayUnder = findViewById(R.id.btn_per_day_attend_under_line);

        mPerMonthTv = (TextView) findViewById(R.id.btn_per_month_attend_tv);
        mPerMonthUnder = findViewById(R.id.btn_per_month_attend_under_line);

        btn_per_day.setOnClickListener(this);
        btn_per_month.setOnClickListener(this);

        mPerDayLayout = (LinearLayout) findViewById(R.id.layout_per_day_calendar);
        mPerMonthLayout = (LinearLayout) findViewById(R.id.layout_month_per_chart);

        mMyInfo = (MemberInfo) getIntent().getSerializableExtra("my_info");
        mMainData = (MainData) getIntent().getSerializableExtra("main_data");
//        setCalendarData();
        mCurrnentMonthAttendCount.setText(mMainData.getCount());

        mPerMonthTv.setTextColor(getResources().getColor(R.color.default_attend_btn_color));
        mPerMonthUnder.setBackgroundColor(getResources().getColor(R.color.default_attend_btn_color));
        mPerDayTv.setTextColor(getResources().getColor(R.color.day_attend_btn_color));
        mPerDayUnder.setBackgroundColor(getResources().getColor(R.color.day_attend_btn_color));

        Calendar cal = Calendar.getInstance();
        mCurrentDate = Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString(cal.get(Calendar.MONTH)+1);
        getAttendDay(Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1));
        getAttendMonth(Integer.toString(cal.get(Calendar.YEAR)));
    }

    private void setCalendarData() {

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****


        // If Activity is created after rotation

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

        // Uncomment this to customize startDayOfWeek
        // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
        // CaldroidFragment.TUESDAY); // Tuesday

        // Uncomment this line to use Caldroid in compact mode
        // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

        // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

        caldroidFragment.setArguments(args);

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1,caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {}

            @Override
            public void onChangeMonth(int month, int year) {

                AttendDay currentMonth = mAttendDayMap.get(String.format("%d%d",year,month));
                if(currentMonth != null) {
                    mCurrnentMonthAttendCount.setText(Integer.toString(currentMonth.getRows().size()));
                }
                if(month == 1) {
                    month = 12;
                    mCurrentDate = String.format("%d%d", year-1,month);
                    getAttendDay(Integer.toString(year-1), Integer.toString(month));
                }else {
                    mCurrentDate = String.format("%d%d", year,month-1);
                    getAttendDay(Integer.toString(year), Integer.toString(month-1));
                }

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

//                    }
//                });
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
//        caldroidFragment.setSixWeeksInCalendar(false);
    }

    private void setChartData(ArrayList<String> month){
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(31);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);
        xl.setLabelCount(12);
        xl.setTextColor(R.color.chart_xl_color);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return (Math.round(value/spaceForBar))+1 + "월";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yl.setGranularity(1f);
        yl.setAxisMaximum(31f);
        yl.setTextColor(Color.rgb(0,0,0));
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yr.setTextColor(Color.rgb(255,255,255));
        yr.setInverted(true);
        setData(month, 31);
        mChart.setFitBars(true);

        mChart.animateY(1000);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.rgb(0,0,0));
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData(ArrayList<String> month, float range) {

        float barWidth = 6f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        String[] labels = new String[12];
        for (int i = 0; i < month.size(); i++) {
            yVals1.add(new BarEntry(i * spaceForBar, Float.parseFloat(month.get(i))));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setStackLabels(labels);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "출석일수");

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setValueTextColor(Color.rgb(0,0,0));
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    String tmp = Float.toString(value);
                    return tmp.substring(0, tmp.lastIndexOf("."));
                }
            });
            data.setBarWidth(barWidth);

            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onSuccess(String type, String result_string) {
        switch (type) {
            case "PER_MONTH" :{
                Gson gson = new GsonBuilder().create();
                AttendMonth month = new AttendMonth();
                month = gson.fromJson(result_string, AttendMonth.class);
                setChartData(month.getRows());
                break;
            }
            case "PER_DAY" : {
                System.out.println("result = " + result_string);
                Gson gson = new GsonBuilder().create();
                final AttendDay days = gson.fromJson(result_string, AttendDay.class);
                mAttendDayMap.put(mCurrentDate, days);
                setDaysData(days);
//                caldroidFragment.getDatePagerAdapters().get(0).notifyDataSetChanged();
//                caldroidFragment.getDatePagerAdapters().get(caldroidFragment.getDateViewPager().getCurrentItem()).notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onFailed(String type, String result_string) {

    }

    @Override
    public void onException(String type, String error_message) {

    }

    private void setDaysData(final AttendDay days) {
        Drawable green = getResources().getDrawable(R.drawable.btn_meeting_alarm_off);
        if(caldroidFragment == null) {
            caldroidFragment = new CaldroidFragment();
        }

        for(String day : days.getRows()) {
            caldroidFragment.setBackgroundDrawableForDate(green, day);
            caldroidFragment.setTextColorForDate(R.color.white, day);
        }

        if(is_init == false) {
            setCalendarData();
            is_init=true;
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_per_day_attend:{
                mPerMonthLayout.setVisibility(View.INVISIBLE);
                mPerDayLayout.setVisibility(View.VISIBLE);

                mPerMonthTv.setTextColor(getResources().getColor(R.color.default_attend_btn_color));
                mPerMonthUnder.setBackgroundColor(getResources().getColor(R.color.default_attend_btn_color));
                mPerDayTv.setTextColor(getResources().getColor(R.color.day_attend_btn_color));
                mPerDayUnder.setBackgroundColor(getResources().getColor(R.color.day_attend_btn_color));
                break;
            }
            case R.id.btn_per_month_attend:{

                mPerMonthLayout.setVisibility(View.VISIBLE);
                mPerDayLayout.setVisibility(View.INVISIBLE);

                mPerDayTv.setTextColor(getResources().getColor(R.color.default_attend_btn_color));
                mPerDayUnder.setBackgroundColor(getResources().getColor(R.color.default_attend_btn_color));
                mPerMonthTv.setTextColor(getResources().getColor(R.color.month_attend_btn_color));
                mPerMonthUnder.setBackgroundColor(getResources().getColor(R.color.month_attend_btn_color));

                break;
            }
        }
    }

    public void getAttendDay(String year, String month){
        if(mAttendDayMap.get(year+month) == null) {
            HttpConnector conn = new HttpConnector("PER_DAY", AMonthAttend.this);
            conn.getAttendDays(mMyInfo.getName(), year, month);
        }else {
            setDaysData(mAttendDayMap.get(year+month));
        }


    }

    public void getAttendMonth(String year) {
        HttpConnector conn = new HttpConnector("PER_MONTH", AMonthAttend.this);
        conn.getAttendMonth(mMyInfo.getName());
        System.out.println("asdfasdf")
    }

}
