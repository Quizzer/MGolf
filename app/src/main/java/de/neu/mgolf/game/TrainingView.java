package de.neu.mgolf.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.EventLog;
import android.view.MotionEvent;
import android.view.View;

public class TrainingView extends View {

    private Paint black;
    private Paint red;
    private Paint white;
    private Paint example_color;
    private int h;
    private int w;
    private float xBall;
    private float yBall;
    private int xHole;
    private int yHole;

    private final int d = 20;
    private float xTouch;
    private float yTouch;
    private boolean touched;
    private float vx;
    private float vy;

    public TrainingView(Context context) {
        super(context);
        init();
    }

    public TrainingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrainingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TrainingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init () {
        example_color = new Paint();
        example_color.setColor(0xFF002244);
        example_color.setStrokeWidth(5);
        example_color.setAntiAlias(true);
        example_color.setStyle(Paint.Style.STROKE);
        example_color.setStrokeCap(Paint.Cap.ROUND);
        example_color.setStrokeJoin(Paint.Join.MITER);

        white = new Paint();
        white.setColor(0xFFFFFFFF);

        red = new Paint();
        red.setColor(0xFFFF0000);
        red.setAntiAlias(true);

        black = new Paint();
        black.setColor(0xFF000000);
        black.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;

        this.xBall = w/2;
        this.yBall = h * 0.8f;

        this.xHole = w/2;
        this.yHole = (int) (h * 0.2);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                xTouch = event.getX();
                yTouch = event.getY();
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                vx = (xBall - xTouch) * 0.04f;
                vy = (yBall - yTouch) * 0.04f;

                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFF008800);
        canvas.drawRect(d,d,w-d, h-d, white);
        canvas.drawCircle(xHole, yHole, 20, black);
        canvas.drawCircle(xBall, yBall, 20, red);
        if (touched) {
            canvas.drawLine(xTouch, yTouch, xBall, yBall, black);
        }

        // Wenn Geschwindigkeit
        if (vx != 0 || vy != 0) {
            // Ballposition proportional zur Geschw. verschieben
            xBall += vx;
            yBall += vy;

            // Bei Bandenkontakt reflektieren
            if (xBall < d + 10 || xBall > w-d - 10) {
                vx = -vx;
            }
            if (yBall < d + 10 || yBall > h-d - 10) {
                vy = -vy;
            }

            // Neuzeichnen anfordern
            invalidate();
        }
    }
}
