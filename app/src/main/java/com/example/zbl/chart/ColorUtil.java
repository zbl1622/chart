package com.example.zbl.chart;

/**
 * Created by zbl on 2016/6/8.
 */
public class ColorUtil {
    public static int setAlpha(int color, int alpha) {
        color = color & 0x00FFFFFF;
        color = color | ((alpha << 24) & 0xFF000000);
        return color;
    }
}
