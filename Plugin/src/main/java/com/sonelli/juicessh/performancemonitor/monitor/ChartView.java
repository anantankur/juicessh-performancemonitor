package com.sonelli.juicessh.performancemonitor.monitor;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sonelli.juicessh.performancemonitor.R;
import com.sonelli.juicessh.performancemonitor.helpers.PreferenceHelper;

public class ChartView extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    private int lastX = 0;
    public static int cpuStat;
    private final Handler mHandler = new Handler();
    private Runnable mTimer;

    public static boolean chartActive = false;

    @Override
    public void onStart() {
        super.onStart();
        chartActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        chartActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        Log.d("chartview", "print: "+preferenceHelper.getKeepScreenOnFlag());
        if(preferenceHelper.getKeepScreenOnFlag()){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        GraphView graph = findViewById(R.id.graph);

        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(20);
        viewport.setScrollable(true);
        viewport.setScalable(true);


        series.setDrawDataPoints(true);
        series.setDrawBackground(true);
        series.setAnimated(true);
        series.setDrawAsPath(true);
        series.setColor(Color.rgb(156, 39, 176));
        series.setBackgroundColor(Color.argb(40, 76, 175, 80));
        graph.addSeries(series);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                addEntry(cpuStat);
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mTimer, 1500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimer);
    }

    public void updater(int cpu) {
        cpuStat = cpu;
        addEntry(cpu);
    }

    public void addEntry(int cpu) {
        Log.d("chart", "addEntry: "+cpu);
        series.appendData(new DataPoint(lastX++, cpu), true, 21);
    }

}
