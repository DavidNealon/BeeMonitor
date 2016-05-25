package com.example.david_nealon.beemonitorsecond;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SecondScreen extends AppCompatActivity {

    private RelativeLayout resultView;
    private LineChart resultChart;
    ArrayList<String> humListR = new ArrayList(50);
    ArrayList<String> tempListR = new ArrayList(50);
    ArrayList<String> massListR = new ArrayList(50);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        humListR = i.getStringArrayListExtra("humData");
        tempListR = i.getStringArrayListExtra("tempData");
        massListR = i.getStringArrayListExtra("massData");

        setContentView(R.layout.activity_second_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Graph View");

        resultView = (RelativeLayout)  findViewById(R.id.resultView);
        resultChart = new LineChart(this);
        resultView.addView(resultChart,  new AbsListView.LayoutParams (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        resultChart.setDescription("Last 50 readings of beehive");
        resultChart.setDescriptionColor(Color.BLUE);

        resultChart.setHighlightPerDragEnabled(true);
        resultChart.setHighlightPerTapEnabled(true);
        resultChart.setDragEnabled(true);
        resultChart.setScaleEnabled(true);
        resultChart.setDrawGridBackground(false);
        resultChart.setPinchZoom(true);
        resultChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        resultChart.setData(data);

        //Legend legend = resultChart.getLegend();

        XAxis x1 =  resultChart.getXAxis();
        x1.setAvoidFirstLastClipping(true);
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(false);

        YAxis y1 =  resultChart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setDrawGridLines(true);
        y1.setAxisMaxValue(100f);
        y1.setAxisMinValue(-10f);

        YAxis y2 = resultChart.getAxisRight();
        y2.setEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(String t : tempListR){
                                addEntryTemp(t);
                            }
                            for(String h : humListR){
                                addEntryHum(h);
                            }
                            for(String m : massListR){
                                addEntryMass(m);
                            }
                        }
                    });
                }
            }
        }).start();
    }


    // method to add a temperature entry to chart
    private void addEntryTemp(String t) {
        LineData dataTemp = resultChart.getData();


        if(dataTemp != null){
            LineDataSet set1 = (LineDataSet) dataTemp.getDataSetByIndex(0);

            if (set1 == null){
                set1 = createSet1();
                dataTemp.addDataSet(set1);
            }


            int tempInt = Integer.parseInt(t);
            dataTemp.addXValue("");
            dataTemp.addEntry(new Entry((tempInt), set1.getEntryCount()), 0);

            resultChart.notifyDataSetChanged();

            resultChart.setVisibleXRange(15f, 15f);
            resultChart.moveViewToX(dataTemp.getXValCount() - 1);

        }



    }

    // method to add a humidity entry to chart
    private void addEntryHum(String h) {
        LineData dataHum = resultChart.getData();


        if(dataHum != null){
            LineDataSet set2 = (LineDataSet) dataHum.getDataSetByIndex(1);

            if (set2 == null){
                set2 = createSet2();
                dataHum.addDataSet(set2);
            }


            int humInt = Integer.parseInt(h);
            dataHum.addXValue("");
            dataHum.addEntry(new Entry((humInt), set2.getEntryCount()), 1);

            resultChart.notifyDataSetChanged();

            resultChart.setVisibleXRange(15f, 15f);
            resultChart.moveViewToX(dataHum.getXValCount() - 1);

        }


    }

    // method to add a mass entry to chart
    private void addEntryMass(String m) {

        LineData dataMass = resultChart.getData();


        if(dataMass != null){
            LineDataSet set3 = (LineDataSet) dataMass.getDataSetByIndex(2);

            if (set3 == null){
                set3 = createSet3();
                dataMass.addDataSet(set3);
            }

            float massInt = Float.parseFloat(m);
            dataMass.addXValue("");
            dataMass.addEntry(new Entry((massInt), set3.getEntryCount()), 2);

            resultChart.notifyDataSetChanged();

            resultChart.setVisibleXRange(15f, 15f);
            resultChart.moveViewToX(dataMass.getXValCount() - 1);

        }


    }


    private LineDataSet createSet1() {
        LineDataSet set1 = new LineDataSet(null, "Temperature (c)");
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        //set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244,117,177));
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setValueTextSize(7f);


        return set1;
    }

    private LineDataSet createSet2() {

        LineDataSet set2 = new LineDataSet(null, "Humidity (%)");
        set2.setDrawCubic(true);
        set2.setCubicIntensity(0.2f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(2f);
        //set2.setCircleSize(4f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setHighLightColor(Color.rgb(244,117,177));
        set2.setValueTextColor(Color.RED);
        set2.setValueTextSize(7f);

        return set2;
    }

    private LineDataSet createSet3() {

        LineDataSet set3 = new LineDataSet(null, "Mass (kgs)");
        set3.setDrawCubic(true);
        set3.setCubicIntensity(0.2f);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setColor(Color.BLACK);
        set3.setCircleColor(Color.BLACK);
        set3.setLineWidth(2f);
        //set3.setCircleSize(4f);
        set3.setFillAlpha(65);
        set3.setFillColor(Color.BLACK);
        set3.setHighLightColor(Color.rgb(244,117,177));
        set3.setValueTextColor(Color.BLACK);
        set3.setValueTextSize(7f);

        return set3;
    }
}
