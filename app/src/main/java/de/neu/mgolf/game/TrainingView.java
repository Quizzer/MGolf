package de.neu.mgolf.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import de.neu.mgolf.R;

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
    private SoundPool soundPool;
    private int collisionSoundId;
    private int holeSoundId;
    private boolean inHole;

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
        white.setColor(0xFFe8e8e8);

        red = new Paint();
        red.setColor(0xFFFF0000);
        red.setAntiAlias(true);

        black = new Paint();
        black.setColor(0xFF000000);
        black.setAntiAlias(true);

        // Sound
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        collisionSoundId = soundPool.load(getContext(), R.raw.dung, 0);
        holeSoundId = soundPool.load(getContext(), R.raw.tada, 0);
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

        int radius = 20;
        canvas.drawCircle(xHole, yHole, radius, black);
        if (inHole) {
            // Todo: Count Schläge - Toast mit kurzer Display Zeit?
            // Todo: New Game
            return;
        }

        if (touched) {
            canvas.drawLine(xTouch, yTouch, xBall, yBall, example_color);
        }
        canvas.drawCircle(xBall, yBall, radius, red);

        // Wenn Geschwindigkeit
        if (vx != 0 || vy != 0) {
            // Ballposition proportional zur Geschw. verschieben
            xBall += vx;
            yBall += vy;

            // Bei Bandenkontakt reflektieren
            if (xBall < d + radius || xBall > w-d - radius) {
                vx = -vx;
                xBall += vx;
                // Todo: Sound Stereo je nach x und y Koordinate
                soundPool.play(collisionSoundId, 1, 1, 1, 0, 1);
            }
            if (yBall < d + radius || yBall > h-d - radius) {
                vy = -vy;
                yBall += vy;
                soundPool.play(collisionSoundId, 1, 1, 1, 0, 1);
            }

            // Ist der Ball im Loch
            float dx = xBall - xHole;
            float dy = yBall - yHole;

            if (dx*dx + dy*dy < 100) {
                soundPool.play(holeSoundId, 1, 1, 1, 0, 1);
                xBall = xHole;
                yBall = yHole;
                vx = vy = 0;
                inHole = true;
            }

            // Reibung berücksichtigen
            vx *= 0.98;
            vy *= 0.98;
            if (Math.abs(vx) < 0.01) vx = 0;
            if (Math.abs(vy) < 0.01) vy = 0;


            // Neuzeichnen anfordern
            invalidate();
        }
    }

    public void setAcceleration(float ax, float ay) {
        vx += ax * 0.08;
        vy += ay * 0.08;
    }
}
