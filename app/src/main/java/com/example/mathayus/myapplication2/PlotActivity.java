package com.example.mathayus.myapplication2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class PlotActivity extends AppCompatActivity {
    public ArrayList<Double> humidityData;
    public ArrayList<Double> humidityData2;
    public ArrayList<Double> times;
    private GraphView graph1;
    private GraphView graph2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool1);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
        Bundle bundle = intent.getExtras();
        if (bundle!= null) {
            times = (ArrayList<Double>) intent.getSerializableExtra("times");
            humidityData = (ArrayList<Double>) intent.getSerializableExtra("humidity_data");
            humidityData2 = (ArrayList<Double>) intent.getSerializableExtra("humidity_data2");
        }

        graph1 = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i< humidityData.size(); i++){
            series.appendData(new DataPoint(times.get(i), humidityData.get(i)), false, 5000);
        }
        series.setColor(Color.BLUE);
        series.setThickness(1);
        series.setTitle("1st Data");
//        series.setShape(PointsGraphSeries.Shape.POINT);
//        series.setSize((float)1.0);
        graph1.addSeries(series);

        graph1.getLegendRenderer().setVisible(true);
        graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph2 = (GraphView) findViewById(R.id.graph2);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
        for (int i = 0; i< humidityData2.size(); i++){
            series2.appendData(new DataPoint(times.get(i), humidityData2.get(i)), false, 5000);
        }
        series2.setColor(Color.RED);
        //series2.setSize((float)1.0);
        series2.setTitle("2nd Data");
        series2.setThickness(1);
        //series2.setShape(PointsGraphSeries.Shape.POINT);
        graph2.addSeries(series2);

        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the "scan" option to the menu
        getMenuInflater().inflate(R.menu.main2, menu);
        //Add any device elements we've discovered to the overflow menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_humid:
                graph1.setVisibility(View.VISIBLE);
                graph2.setVisibility(View.INVISIBLE);
                return true;
            case R.id.action_temp:
                graph2.setVisibility(View.VISIBLE);
                graph1.setVisibility(View.INVISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
