package com.example.DragAndDraw;

import android.graphics.PointF;

/**
 * model object to define a 2D rectangle
 */
public class Box {

    private PointF mOrigin;
    private PointF mCurrent;


    public Box(PointF origin) {
        mOrigin = origin;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
}
