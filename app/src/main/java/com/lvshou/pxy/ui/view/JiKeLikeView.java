package com.lvshou.pxy.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.lvshou.pxy.R;

/**
 * Describe :
 * Created by Knight on 2018/12/19
 * 点滴之行,看世界
 **/
public class JiKeLikeView extends View {

    //点赞数量
    private int likeNumber;
    //文字上下移动的最大距离
    private int textMaxMove;
    //文字上下移动的距离
    private float textMoveDistance;
    //点亮的透明度 0位隐藏 255是出现
    private float shiningAlpha;
    //点亮的缩放系数
    private float shiningScale;
    //文字的透明度系数
    private float textAlpha;
    //动画播放时间
    private int duration = 250;
    //文字显示范围
    private Rect textRounds;
    //数字位数
    private float[] widths;

    //图像画笔
    private Paint bitmapPaint;
    //文字画笔
    private Paint textPaint;
    //之前的文字画笔
    private Paint oldTextPaint;
    //没有点赞的图像
    private Bitmap unLikeBitmap;
    //点赞后的图像
    private Bitmap likeBitmap;
    //点亮的图像
    private Bitmap shiningBitmap;

    //是否点赞
    private boolean isLike = false;
    //小手的缩放倍数
    private float handScale = 1.0f;
    //刚进入 数字不要加一
    private boolean isFirst;
    //圆形缩放系数
    private float shingCircleScale;
    //圆形透明度系数
    private float shingCircleAlpha;
    //圆画笔
    private Paint circlePaint;


    public JiKeLikeView(Context context) {
        this(context, null);
    }

    public JiKeLikeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiKeLikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取attrs文件下配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JiKeLikeView);
        //点赞数量 第一个参数就是属性集合里面的属性 固定格式R.styleable+自定义属性名字
        //第二个参数，如果没有设置这个属性，则会取设置的默认值
        likeNumber = typedArray.getInt(R.styleable.JiKeLikeView_like_number, 1999);
        //记得把TypedArray对象回收
        typedArray.recycle();
        init();
    }

    private void init() {
        //创建文本显示范围
        textRounds = new Rect();
        //点赞数暂时8位
        widths = new float[8];
        //Paint.ANTI_ALIAS_FLAG 属性是位图抗锯齿
        //bitmapPaint是图像画笔
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //这是绘制原来数字的画笔 加入没点赞之前是45 那么点赞后就是46 点赞是46 那么没点赞就是45
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //文字颜色大小配置 颜色灰色 字体大小为14
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(sp2px(getContext(), 14));
        oldTextPaint.setColor(Color.GRAY);
        oldTextPaint.setTextSize(sp2px(getContext(), 14));
        //圆画笔初始化 Paint.Style.STROKE只绘制图形轮廓
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        //设置轮廓宽度
        circlePaint.setStrokeWidth(dp2px(getContext(), 2));
        //设置模糊效果 第一个参数是模糊半径，越大越模糊，第二个参数是阴影的横向偏移距离，正值向下偏移 负值向上偏移
        //第三个参数是纵向偏移距离，正值向下偏移，负值向上偏移 第四个参数是画笔的颜色
        circlePaint.setShadowLayer(dp2px(getContext(), 1), dp2px(getContext(), 1), dp2px(getContext(), 1), Color.RED);

    }

    /**
     * 这个方法是在Activity resume的时候被调用的，Activity对应的window被添加的时候
     * 每个view只会调用一次，可以做一些初始化操作
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("pxy","onAttachedToWindow");
        Resources resources = getResources();
        //构造Bitmap对象，通过BitmapFactory工厂类的static Bitmap decodeResource根据给定的资源id解析成位图
        unLikeBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_message_unlike);
        likeBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_message_like);
        shiningBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_message_like_shining);
    }


    /**
     * 和onAttachedToWindow对应，在destroy view的时候调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("pxy","onDetachedFromWindow");
        //回收bitmap
        unLikeBitmap.recycle();
        likeBitmap.recycle();
        shiningBitmap.recycle();
    }

    /**
     * 测量宽高
     * 这两个参数是由父视图经过计算后传递给子视图
     * @param widthMeasureSpec 宽度
     * @param heightMeasureSpec 高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec值由specMode和specSize共同组成，onMeasure两个参数的作用根据specMode的不同，有所区别。
        //当specMode为EXACTLY时，子视图的大小会根据specSize的大小来设置，对于布局参数中的match_parent或者精确大小值
        //当specMode为AT_MOST时，这两个参数只表示了子视图当前可以使用的最大空间大小，而子视图的实际大小不一定是specSize。所以我们自定义View时，重写onMeasure方法主要是在AT_MOST模式时，为子视图设置一个默认的大小，对于布局参数wrap_content。
        //高度默认是bitmap的高度加上下margin各10dp
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(unLikeBitmap.getHeight() + dp2px(getContext(), 20), MeasureSpec.EXACTLY);
        //宽度默认是bitmap的宽度加左右margin各10dp和文字宽度和文字右侧10dp likeNumber是文本数字
        String textnum = String.valueOf(likeNumber);
        //得到文本的宽度
        float textWidth = textPaint.measureText(textnum, 0, textnum.length());
        //计算整个View的宽度 小手宽度 + 文本宽度 + 30px
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(((int) (unLikeBitmap.getWidth() + textWidth + dp2px(getContext(), 30))), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取正个View的高度
        int height = getHeight();
        //取中心
        int centerY = height / 2;
        //小手根据有没有点赞进行改变
        Bitmap handBitmap = isLike ? likeBitmap : unLikeBitmap;
        //得到图像宽度
        int handBitmapWidth = handBitmap.getWidth();
        //得到图像高度
        int handBitmapHeight = handBitmap.getHeight();

        //画小手
        int handTop = (height - handBitmapHeight) / 2;
        //先保存画布的状态
        canvas.save();
        //根据bitmap中心进行缩放
        canvas.scale(handScale, handScale, handBitmapWidth / 2, centerY);
        //画bitmap小手，第一个是参数对应的bitmap，第二个参数是左上角坐标，第三个参数上顶部坐标，第四个是画笔
        canvas.drawBitmap(handBitmap, dp2px(getContext(), 10), handTop, bitmapPaint);
        //读取之前没有缩放画布的状态
        canvas.restore();

        //画上面四点闪亮
        //先确定顶部
        int shiningTop = handTop - shiningBitmap.getHeight() + dp2px(getContext(), 40);
        Log.d("pxy",shiningAlpha+"");
        //根据隐藏系数设置点亮的透明度
        bitmapPaint.setAlpha((int) (255 * shiningAlpha));
        //保存画布状态
        canvas.save();
        //画布根据点亮的缩放系数进行缩放
        canvas.scale(shiningScale, shiningScale, handBitmapWidth / 2, handTop);
        //画出点亮的bitmap
        //恢复画笔之前的状态
        canvas.restore();
        //并且恢复画笔bitmapPaint透明度
        bitmapPaint.setAlpha(255);
        if(isLike){
            canvas.drawBitmap(shiningBitmap, dp2px(getContext(), 15), shiningTop, bitmapPaint);
        }else{
            canvas.save();
            //并且恢复画笔bitmapPaint透明度
            bitmapPaint.setAlpha(0);
            canvas.drawBitmap(shiningBitmap, dp2px(getContext(), 15), shiningTop, bitmapPaint);
            canvas.restore();
            //并且恢复画笔bitmapPaint透明度
            bitmapPaint.setAlpha(255);
        }

        //画文字
        String textValue = String.valueOf(likeNumber);
        //如果点赞了，之前的数值就是点赞数-1，如果取消点赞，那么之前数值（对比点赞后）就是现在显示的
        String textCancelValue;
        if (isLike) {
            textCancelValue = String.valueOf(likeNumber - 1);
        } else {
            if (isFirst) {
                textCancelValue = String.valueOf(likeNumber + 1);
            } else {
                isFirst = !isFirst;
                textCancelValue = String.valueOf(likeNumber);
            }
        }
        //文本的长度
        int textLength = textValue.length();
        //获取绘制文字的坐标 getTextBounds 返回所有文本的联合边界
        textPaint.getTextBounds(textValue, 0, textValue.length(), textRounds);
        //确定X坐标 距离手差10dp
        int textX = handBitmapWidth + dp2px(getContext(), 20);
        //确定Y坐标 距离 大图像的一半减去 文字区域高度的一半 即可得出 getTextBounds里的rect参数得到数值后，
        // 查看它的属性值 top、bottom会发现top是一个负数；bottom有时候是0，有时候是正数。结合第一点很容易理解，因为baseline坐标看成原点（0,0），
        // 那么相对位置top在它上面就是负数，bottom跟它重合就为0，在它下面就为负数。像小写字母j g y等，它们的bounds bottom都是正数，
        // 因为它们都有降部（在西文字体排印学中，降部指的是一个字体中，字母向下延伸超过基线的笔画部分）。
        int textY = height / 2 - (textRounds.top + textRounds.bottom) / 2;
        //绘制文字 这种情况针对不同位数变化 如 99 到100 999到10000
        if (textLength != textCancelValue.length() || textMaxMove == 0) {
            //第一个参数就是文字内容，第二个参数是文字的X坐标，第三个参数是文字的Y坐标，注意这个坐标
            //并不是文字的左上角 而是与左下角比较接近的位置
            //canvas.drawText(textValue,  textX, textY, textPaint);
            //点赞
            if (isLike) {
                //圆的画笔根据设置的透明度进行变化
                circlePaint.setAlpha((int) (255 * shingCircleAlpha));
                //画圆
                canvas.drawCircle(handBitmapWidth / 2 + dp2px(getContext(), 10), handBitmapHeight / 2 + dp2px(getContext(), 10), ((handBitmapHeight / 2 + dp2px(getContext(), 2)) * shingCircleScale), circlePaint);
                //根据透明度进行变化
                oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                //绘制之前的数字
                canvas.drawText(textCancelValue, textX, textY - textMaxMove + textMoveDistance, oldTextPaint);
                //设置新数字的透明度
                textPaint.setAlpha((int) (255 * textAlpha));
                //绘制新数字（点赞后或者取消点赞）
                canvas.drawText(textValue, textX, textY + textMoveDistance, textPaint);

            } else {
                oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                canvas.drawText(textCancelValue, textX, textY + textMaxMove + textMoveDistance, oldTextPaint);
                textPaint.setAlpha((int) (255 * textAlpha));
                canvas.drawText(textValue, textX, textY + textMoveDistance, textPaint);
            }
            return;
        }
        //下面这种情况区别与99 999 9999这种 就是相同位数变化
        //把文字拆解成一个一个字符 就是获取字符串中每个字符的宽度，把结果填入参数widths
        //相当于measureText()的一个快捷方法，计算等价于对字符串中的每个字符分别调用measureText()，并把
        //它们的计算结果分别填入widths的不同元素
        textPaint.getTextWidths(textValue, widths);
        //将字符串转换为字符数组
        char[] chars = textValue.toCharArray();
        char[] oldChars = textCancelValue.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == oldChars[i]) {
                textPaint.setAlpha(255);
                canvas.drawText(String.valueOf(chars[i]), textX, textY, textPaint);

            } else {
                //点赞
                if (isLike) {
                    circlePaint.setAlpha((int) (255 * shingCircleAlpha));
                    canvas.drawCircle(handBitmapWidth / 2 + dp2px(getContext(), 10), handBitmapHeight / 2 + dp2px(getContext(), 10), ((handBitmapHeight / 2 + dp2px(getContext(), 2)) * shingCircleScale), circlePaint);
                    oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                    canvas.drawText(String.valueOf(oldChars[i]), textX, textY - textMaxMove + textMoveDistance, oldTextPaint);
                    textPaint.setAlpha((int) (255 * textAlpha));
                    canvas.drawText(String.valueOf(chars[i]), textX, textY + textMoveDistance, textPaint);
                } else {
                    oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                    canvas.drawText(String.valueOf(oldChars[i]), textX, textY + textMaxMove + textMoveDistance, oldTextPaint);
                    textPaint.setAlpha((int) (255 * textAlpha));
                    canvas.drawText(String.valueOf(chars[i]), textX, textY + textMoveDistance, textPaint);
                }
            }
            //下一位数字x坐标要加上前一位的宽度
            textX += widths[i];


        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                    jump();

                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 点赞事件触发
     */
    private void jump() {
        isLike = !isLike;
        if (isLike) {
            ++likeNumber;
            setLikeNum();
            //自定义属性 在ObjectAnimator中，是先根据属性值拼装成对应的set函数名字，比如下面handScale的拼装方法就是
            //将属性的第一个字母强制大写后与set拼接，所以就是setHandScale，然后通过反射找到对应控件的setHandScale(float handScale)函数
            //将当前数字值做为setHandScale（float handScale）的参数传入 set函数调用每隔十几毫秒就会被用一次
            //ObjectAnimator只负责把当前运动动画的数值传给set函数，set函数怎么来做就在里面写就行
            ObjectAnimator handScaleAnim = ObjectAnimator.ofFloat(this, "handScale", 1f, 0.8f, 1f);
            //设置动画时间
            handScaleAnim.setDuration(duration);

            //动画 点亮手指的四点 从0 - 1出现
            ObjectAnimator shingAlphaAnim = ObjectAnimator.ofFloat(this, "shingAlpha", 0f, 1f);
            // shingAlphaAnim.setDuration(duration);

            //放大 点亮手指的四点
            ObjectAnimator shingScaleAnim = ObjectAnimator.ofFloat(this, "shingScale", 0f, 1f);

            //画中心圆形有内到外扩散
            ObjectAnimator shingClicleAnim = ObjectAnimator.ofFloat(this, "shingCircleScale", 0.6f, 1f);
            //画出圆形有1到0消失
            ObjectAnimator shingCircleAlphaAnim = ObjectAnimator.ofFloat(this, "shingCircleAlpha", 0.3f, 0f);


            //动画集一起播放
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(handScaleAnim, shingAlphaAnim, shingScaleAnim, shingClicleAnim, shingCircleAlphaAnim);
            animatorSet.start();


        } else {
            //取消点赞
            --likeNumber;
            setLikeNum();
            ObjectAnimator handScaleAnim = ObjectAnimator.ofFloat(this, "handScale", 1f, 0.8f, 1f);
            handScaleAnim.setDuration(duration);
            handScaleAnim.start();

            //手指上的四点消失，透明度设置为0
            setShingAlpha(0);


        }
    }

    /**
     * 手指缩放方法
     *
     * @param handScale
     */
    public void setHandScale(float handScale) {
        //传递缩放系数
        this.handScale = handScale;
        //请求重绘View树，即draw过程，视图发生大小没有变化就不会调用layout过程，并且重绘那些“需要重绘的”视图
        //如果是view就绘制该view，如果是ViewGroup,就绘制整个ViewGroup
        invalidate();
    }


    /**
     * 手指上四点从0到1出现方法
     *
     * @param shingAlpha
     */

    public void setShingAlpha(float shingAlpha) {
        this.shiningAlpha = shingAlpha;
        invalidate();
    }

    /**
     * 手指上四点缩放方法
     *
     * @param shingScale
     */
    @Keep
    public void setShingScale(float shingScale) {
        this.shiningScale = shingScale;
        invalidate();
    }


    /**
     * 设置数字变化
     */
    public void setLikeNum() {
        //开始移动的Y坐标
        float startY;
        //最大移动的高度
        textMaxMove = dp2px(getContext(), 20);
        //如果点赞了 就下往上移
        if (isLike) {
            startY = textMaxMove;
        } else {
            startY = -textMaxMove;
        }
        ObjectAnimator textInAlphaAnim = ObjectAnimator.ofFloat(this, "textAlpha", 0f, 1f);
        textInAlphaAnim.setDuration(duration);
        ObjectAnimator textMoveAnim = ObjectAnimator.ofFloat(this, "textTranslate", startY, 0);
        textMoveAnim.setDuration(duration);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(textInAlphaAnim, textMoveAnim);
        animatorSet.start();
    }


    /**
     * 设置数值透明度
     */

    public void setTextAlpha(float textAlpha) {
        this.textAlpha = textAlpha;
        invalidate();

    }

    /**
     * 设置数值移动
     */

    public void setTextTranslate(float textTranslate) {
        textMoveDistance = textTranslate;
        invalidate();
    }

    /**
     * 画出圆形波纹
     *
     * @param shingCircleScale
     */
    public void setShingCircleScale(float shingCircleScale) {
        this.shingCircleScale = shingCircleScale;
        invalidate();
    }

    /**
     * 圆形透明度设置
     *
     * @param shingCircleAlpha
     */
    public void setShingCircleAlpha(float shingCircleAlpha) {
        this.shingCircleAlpha = shingCircleAlpha;
        invalidate();
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public  int dp2px(Context context,float dpVal){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context
     * @param spVal
     * @return
     *
     */
    public  int sp2px(Context context,float spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,context.getResources().getDisplayMetrics());
    }


}
