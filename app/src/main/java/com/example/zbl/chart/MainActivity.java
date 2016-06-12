package com.example.zbl.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        chartView = (ChartView) findViewById(R.id.chartView);
        DataBean dataBean = getData();
        dataBean.color = 0xFFFF0000;
        chartView.addData(dataBean);
//        dataBean = getData();
//        dataBean.color = 0xFF00FF00;
//        chartView.addData(dataBean);
        dataBean = getData();
        dataBean.color = 0xFF0000FF;
        chartView.addData(dataBean);
    }

    private DataBean getData() {
        DataBean dataBean = new DataBean();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int randomNumber = random.nextInt(200);
            dataBean.list.add(randomNumber);

            if(randomNumber > dataBean.max){
                dataBean.max = randomNumber;
            }
            if(randomNumber < dataBean.min){
                dataBean.min = randomNumber;
            }
        }
        return dataBean;
    }
}
