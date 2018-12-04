package com.lvshou.pxy.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lvshou.pxy.R;

/**
 * @desc： Created by JamesPxy on 2018/12/4 14:45
 */
public class PathwayView extends View {

    private Paint paint;
    private Matrix matrix;
    private float[] pos;
    private float[] tan;
    private Bitmap bitmap;
    private double currentValue = 0.0;

    public PathwayView(Context context) {
        super(context);
        init();
    }

    public PathwayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathwayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PathwayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        matrix = new Matrix();
        pos = new float[2];
        tan = new float[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        //缩放图片
        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //路径
        Path path = new Path();
    /*    path.lineTo(200, 200);
        path.lineTo(600, 200);
        path.lineTo(400, 500);
        path.lineTo(100, 700);
        path.lineTo(200, 700);
//        RectF rectF = new RectF(100, 700, 300, 900);
//        path.addArc(rectF, 0, 270);
        path.cubicTo(200, 700, 300, 800, 200, 900);*/
        path.cubicTo(200, 700, 300, 800, 200, 900);
        path.lineTo(200, 700);
        path.lineTo(100, 700);
        path.lineTo(400, 500);
        path.lineTo(600, 200);
        path.lineTo(200, 200);

        //进度
        currentValue += 0.05;
        if (currentValue > 1) {
            currentValue = 0;
        }

        PathMeasure pathMeasure = new PathMeasure(path, false);
        int length = (int) (pathMeasure.getLength() * currentValue);
        pathMeasure.getPosTan(length, pos, tan);

        //获取旋转角度
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);

        //设置图片旋转角度和偏移量，matrix的方法可以类比canvas的操作方法
        matrix.reset();
        matrix.postRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);

        canvas.drawPath(path, paint);
        canvas.drawBitmap(bitmap, matrix, paint);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //最好使用 线程 或者 ValueAnimator 来控制界面的刷新
        invalidate();
    }
}
