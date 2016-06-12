package com.example.zbl.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by zbl on 2016/6/7.
 */
public class ChartView extends View {

    float density;
    //View尺寸
    int width, height;
    //边距
    float paddingTop, paddingLeft, paddingBottom, paddingRight;
    //刻度
    float x_step, y_step;
    //标注数
    int x_count = 4, y_count = 4;
    //最大值
    float max;

    //画笔
    private static final int COLOR_COORDINATE = 0x60000000;
    private Paint linePaint, chartPaint;
    private Path outlinePath = new Path();
    private Path fillPath = new Path();

    private ArrayList<DataBean> dataList = new ArrayList<>();

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.density = displayMetrics.density;

        paddingLeft = 40 * density;
        paddingRight = 20 * density;
        paddingTop = 20 * density;
        paddingBottom = 20 * density;

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(COLOR_COORDINATE);
        linePaint.setStrokeWidth(1 * density);

        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        chartPaint.setStrokeWidth(1 * density);
    }

    public void addData(DataBean dataBean) {
        this.dataList.add(dataBean);
        for (DataBean bean : dataList) {
            if (bean.max > this.max) {
                this.max = bean.max;
            }
        }
        this.max = this.max * 1.1f;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long currentTime = System.currentTimeMillis();
        width = getWidth();
        height = getHeight();
        //绘制背景
        canvas.drawColor(Color.WHITE);

        //虚线
        PathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 1);
        linePaint.setPathEffect(dashPathEffect);
        y_step = (height - paddingTop - paddingBottom) / y_count;
        Path path = new Path();
        for (int i = 1; i < y_count; i++) {
            path.moveTo(paddingLeft, paddingTop + i * y_step);
            path.lineTo(width - paddingRight, paddingTop + i * y_step);
        }
        canvas.drawPath(path,linePaint);

        //画折线
        for (DataBean dataBean : dataList) {
            if (dataBean != null && dataBean.list.size() > 0) {
                outlinePath.reset();
                fillPath.reset();
                float x, y;
                float chart_height = height - paddingTop - paddingBottom;
                x_step = (width - paddingLeft - paddingRight) / (dataBean.list.size() - 1);
                x = paddingLeft;
                y = height - paddingBottom - dataBean.list.get(0) * chart_height / max;
                outlinePath.moveTo(x, y);
                for (int data : dataBean.list) {
                    y = height - paddingBottom - data * chart_height / max;
                    outlinePath.lineTo(x, y);
                    x += x_step;
                }
                Log.i("ChartView", "ChartView createPath cost " + (System.currentTimeMillis() - currentTime) + "ms");
                fillPath.addPath(outlinePath);
                fillPath.lineTo(width - paddingRight, height - paddingBottom);
                fillPath.lineTo(paddingLeft, height - paddingBottom);
                fillPath.close();
                LinearGradient shader = new LinearGradient(0, height - paddingBottom - dataBean.max * chart_height / max, 0,
                        height - paddingBottom, ColorUtil.setAlpha(dataBean.color, 0x80), 0x00FFFFFF, Shader.TileMode.CLAMP);
                chartPaint.setStyle(Paint.Style.FILL);
                chartPaint.setColor(ColorUtil.setAlpha(dataBean.color, 0x80));
                chartPaint.setShader(shader);
                canvas.drawPath(fillPath, chartPaint);
                chartPaint.setStyle(Paint.Style.STROKE);
                chartPaint.setColor(dataBean.color);
                canvas.drawPath(outlinePath, chartPaint);
            }
        }

        //坐标轴外框
        float radius = 10 * density;
        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, 0, 0};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
        drawable.getPaint().setColor(COLOR_COORDINATE);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setStrokeWidth(1 * density);
        drawable.setBounds((int) paddingLeft, (int) paddingTop, (int) (width - paddingRight), (int) (height - paddingBottom));
        drawable.draw(canvas);

        Log.i("ChartView", "ChartView onDraw cost " + (System.currentTimeMillis() - currentTime) + "ms");
    }
}
