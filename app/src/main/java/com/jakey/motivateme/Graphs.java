package com.jakey.motivateme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jakey.motivateme.models.DailyLog;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class Graphs extends ActionBarActivity {

    ArrayList<DailyLog> dailyLogs = (ArrayList<DailyLog>) DailyLog.listAll(DailyLog.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        getDiet1();
        GraphView wGraph = (GraphView) findViewById(R.id.weightGraph);
        GraphView dWGraph = (GraphView) findViewById(R.id.dietWorkGraph);


/*<Weight Graph>*/
            if(dailyLogs.size()<2){
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, dailyLogs.get(0).getWeight()),
                        new DataPoint(1, dailyLogs.get(0).getWeight())

                });
                wGraph.addSeries(series);
                series.setColor(Color.RED);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(5f);
                wGraph.setTitle("Weight(Days)");

            }
            else if(dailyLogs.size()<14){
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(getWeight1());
                wGraph.addSeries(series);
                series.setColor(Color.RED);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(5f);
                wGraph.setTitle("Weight(Days)");
                double numOfPoints = dailyLogs.size();
                System.out.println(dailyLogs.size());
                wGraph.getViewport().setMinX(1);
                wGraph.getViewport().setMaxX(numOfPoints);
            }
            else{
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(getWeight2());
                wGraph.addSeries(series);
                series.setColor(Color.RED);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(5f);
                wGraph.setTitle("Weight(Weeks)");
                double numOfPoints = dailyLogs.size()/7;
                int points = ((int) numOfPoints);
                if(points>4){
                    wGraph.getViewport().setMinX(points -4);
                    wGraph.getViewport().setMaxX(points);
                }
                else{
                    wGraph.getViewport().setMinX(1);
                    wGraph.getViewport().setMaxX(points);
                }
            }


        double numOfPoints = dailyLogs.size()/7;
        int points = ((int) numOfPoints);

        wGraph.getViewport().setScrollable(true);


        wGraph.getViewport().setXAxisBoundsManual(true);




       /*</Weight Graph>*/

        /*<dWGraph>*/
        if(dailyLogs.size()>=7) {
            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(getDiet1());
            dWGraph.addSeries(series1);
            series1.setColor(Color.GREEN);
            series1.setDrawDataPoints(true);
            series1.setDataPointsRadius(5f);


            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(getWorkout1());
            dWGraph.addSeries(series2);
            series2.setColor(Color.YELLOW);
            series2.setDrawDataPoints(true);
            series2.setDataPointsRadius(5f);



            series1.setTitle("Healthy Diet %");
            series2.setTitle("Workout %");

        }

        dWGraph.getViewport().setXAxisBoundsManual(true);


        if(points-4<1){
            dWGraph.getViewport().setMinX(1);
            dWGraph.getViewport().setMaxX(points);
        }
        else{
            dWGraph.getViewport().setMinX(points-4);
            dWGraph.getViewport().setMaxX(points);
        }

        dWGraph.getViewport().setYAxisBoundsManual(true);
        dWGraph.getViewport().setMinY(0);
        dWGraph.getViewport().setMaxY(100);

        dWGraph.getViewport().setScrollable(true);

        dWGraph.setTitle("Diet and Workout % Per Week");
        dWGraph.getLegendRenderer().setVisible(true);
        dWGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        /*</dWGraph>*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private DataPoint[] getWeight1(){
        DataPoint [] values =  new DataPoint [dailyLogs.size()];

        int prevWeight = 0;
        for(int i=0;i<dailyLogs.size(); i++){
            DailyLog log = dailyLogs.get(i);
            Integer weight = log.getWeight();
            if (weight == null) {
                weight = prevWeight;
            } else {
                prevWeight = weight;
            }
            DataPoint v = new DataPoint(i+1,weight);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] getWeight2(){
        double numOfPoints = dailyLogs.size()/7;
        int points = ((int)numOfPoints);
        int end = points*7;
        int xVal = 1;
        int vCount = 0;
        DataPoint [] values =  new DataPoint [points];
        List<DailyLog> logs = (dailyLogs.subList(0, end));

        for(int i = 0; i<logs.size(); i+=7){
            double weight =logs.get(i).getWeight();
            int yVal = ((int) weight);
            DataPoint v = new DataPoint(xVal,yVal);
            values[vCount] = v;

            vCount++;
            xVal++;

        }

        return values;

    }


    private DataPoint [] getDiet1(){
        double numOfPoints = dailyLogs.size()/7;
        int points = ((int)numOfPoints);
        int end = points*7;
        int xVal = 1;
        int vCount = 0;
        DataPoint [] values =  new DataPoint [points];
        List<DailyLog> logs = (dailyLogs.subList(0, end));
        for(int i = 0; i<logs.size(); i+=7){
            double hCount = 0;
            for(int j = 0; j<7; j++){
                if(logs.get(i+j).getDiet().equals("Healthy")){
                    hCount++;
                }

            }
            hCount = (hCount/7)*100;
            DataPoint v = new DataPoint(xVal,hCount);
            values[vCount] = v;
            vCount++;
            xVal++;

        }

        return values;

    }

    private DataPoint [] getWorkout1(){
        double numOfPoints = dailyLogs.size()/7;
        int points = ((int)numOfPoints);
        int end = points*7;
        int xVal = 1;
        int vCount = 0;
        DataPoint [] values =  new DataPoint [points];
        List<DailyLog> logs = (dailyLogs.subList(0, end));
        for(int i = 0; i<logs.size(); i+=7){
            double hCount = 0;
            for(int j = 0; j<7; j++){
                DailyLog log = logs.get(i+j);
                Boolean workout = log.getWorkout();
                if(workout != null && workout){
                    hCount++;
                }

            }
            hCount = (hCount/7)*100;
            DataPoint v = new DataPoint(xVal,hCount);
            values[vCount] = v;
            vCount++;
            xVal++;

        }

        return values;

    }

    public void toMain(View view){

        Intent i = new Intent(this, Dashboard.class);
        startActivity(i);
    }
}
