package com.metrosoft.arafat.salebook.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.metrosoft.arafat.rxpad.R;


public class GraphViewActivity extends AppCompatActivity {
    GraphView graph;
 //   LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        graph = (GraphView) findViewById(R.id.graph);
        ShowGraph();
    }

    private void ShowGraph() {
     /*   LineGraphSeries<DataPoint>   series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 4),
                new DataPoint(5, 5),
                new DataPoint(6, 6),
                new DataPoint(7, 7),
                new DataPoint(8, 8),
                new DataPoint(9, 9),
                new DataPoint(10, 10),
                new DataPoint(11, 11),
                new DataPoint(12, 12)

        });*/
        // first series is a line
        DataPoint[] points = new DataPoint[110];
        for (int i = 0; i < points.length; i++) {
            double value = (Math.sin(i*0.5) * 10*(Math.random()*10+1));
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 10*(Math.random()*10+1));
            Log.e("X axis values",""+value);
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        // styling series
        series.setTitle("<--Total Yearly sales (Month Wise) -->");
        series.setColor(Color.DKGRAY);
      //  series.setDrawDataPoints(true);
     //   series.setDataPointsRadius(10);
        series.setAnimated(true);
    //    series.setThickness(5);
      // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-200);
        graph.getViewport().setMaxY(200);


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(5);
        graph.getViewport().setMaxX(120);

        // custom label formatter to show currency "EUR"
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " Sale (%)";
                }
            }
        /*    @Override
            public String formatLabel (double value, boolean isValueY){
                return super.formatLabel(value,isValueY)+"Sale (tk)";
            }*/
        });
// legend

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        // enable scaling and scrolling
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.addSeries(series);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(), "Yearly Sales: For this Month: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


    }
}

