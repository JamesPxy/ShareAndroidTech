package com.lvshou.pxy.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lvshou.pxy.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoActivity extends AppCompatActivity implements Runnable {
    // 预览图片范围
    private SurfaceView surfaceView;
    private TextView tv_time;
    // 倒计时拍摄
    private int cameratime = 3;
    private Camera camera;
    private boolean preview = false;
    // 文件名字
    private String filename;
    // 日期对象
    private Date date;
    // 控制线程
    boolean stopThread = false;
    private File file;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 222:
                    tv_time.setText("" + cameratime);
                    if ("0".equals(tv_time.getText().toString())) {
                        tv_time.setText("拍摄成功！");
                        takePhoto();
                    }
                    break;

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题，全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置手机常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 锁定横屏
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_take_photo2);
        setFinishOnTouchOutside(false);
        // 初始化数据
        surfaceView = findViewById(R.id.surfaceView);
        tv_time = findViewById(R.id.tv_time);

        surfaceView.getHolder().addCallback(new SufaceListener());
        /* 下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前 */
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(200, 200); // 设置分辨率

        String[] perms = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
        ActivityCompat.requestPermissions(this, perms, 100);
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 开启线程
        new Thread(this).start();
    }

    private final class SufaceListener implements SurfaceHolder.Callback {
        /**
         * surface改变
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        /**
         * surface创建
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    CameraInfo info = new CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == CameraInfo.CAMERA_FACING_BACK) {// 这就是前置摄像头，亲。
                        camera = Camera.open(i);
                    }
                }
                System.out.println("打开摄像头");
                Camera.Parameters parameters = camera.getParameters();
                /* 每秒从摄像头捕获5帧画面， */
                parameters.setPreviewFrameRate(5);
                /* 设置照片的输出格式:jpg */
                parameters.setPictureFormat(PixelFormat.JPEG);
                /* 照片质量 */
                parameters.set("jpeg-quality", 85);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceView.getHolder());// 通过SurfaceView显示取景画面
                camera.startPreview();
                preview = true;
            } catch (Exception e) {

            }
        }

        /**
         * surface销毁
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                if (preview)
                    camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }

    private boolean  hasTake=false;
    /**
     * 拍摄照片
     */
    private void takePhoto() {
        if(hasTake)return;
        hasTake=true;
        // 执行拍照效果
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    String timeString = formatDate();
                    // File file=new
                    // File("/data/data/com.example.pujiejiaapp/image");
                    //如果手机已获得root权限就可以写此路径，否则写下边的路径。
//					filename = "/data/data/com.example.pujiejiaapp/images/"
//							+ timeString + ".jpg";
                    filename = Environment.getExternalStorageDirectory().getPath() + "/"
                            + timeString + ".jpg";
                    System.out.println("哈哈哈哈文件路径" + filename);
                    File file = new File(filename);
                    boolean createNewFile = file.createNewFile();
                    System.out.println("创建文件夹成功没有" + createNewFile);
                    System.out.println(file);
                    FileOutputStream outStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                    outStream.flush();
                    outStream.close();
                    // 重新浏览
                    camera.stopPreview();
                    camera.startPreview();
                    preview = true;
//                    mHandler.sendEmptyMessage(111);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
    }

    @Override
    public void run() {
        while (!stopThread) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cameratime--;
            mHandler.sendEmptyMessage(222);
            if (cameratime <= 0) {
                break;
            }
        }
    }

    // 格式化系统的时间
    public String formatDate() {
        date = new Date(System.currentTimeMillis());
        // 日期格式
        // 格式化时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHms");
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        // 线程已关闭
        super.onDestroy();
        stopThread = true;
    }

}
