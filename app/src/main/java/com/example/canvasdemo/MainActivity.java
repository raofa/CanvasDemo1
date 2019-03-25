package com.example.canvasdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,View.OnTouchListener {
    private Button repaint,save;
    private ImageView canvasimg;
    
    private Bitmap basebitmap;
    
    private Canvas canvas;
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        
        canvasimg =findViewById(R.id.canvas);
        repaint = findViewById(R.id.repaint);
        save = findViewById(R.id.save);
        
        repaint.setOnClickListener(this);
        save.setOnClickListener(this);
        
        canvasimg.setOnTouchListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.save:
            savebmp();
            break;
        case R.id.repaint:
            resumecanvsa();
            break;
            default:
                break;
        }
    }

    float x ,y;
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN :
            if(basebitmap == null){
                basebitmap = Bitmap.createBitmap(canvasimg.getWidth(),canvasimg.getHeight(),
                        Bitmap.Config.ARGB_8888);
                canvas = new Canvas(basebitmap);
                canvas.drawColor(Color.WHITE);
            }
            x = event.getX();
            y = event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            float x1 = event.getX();
            float y1 = event.getY();
            canvas.drawLine(x,y,x1,y1,paint);
            x =event.getX();
            y =event.getY();
            
            canvasimg.setImageBitmap(basebitmap);
            break;
        case MotionEvent.ACTION_UP:
            break;
            default:
                break;
        }
        return  true;
    }
    
    private void savebmp(){
        try {
             // 保存图片到SD卡上
           File file = new File(Environment.getExternalStorageDirectory(),
            System.currentTimeMillis() + ".png");
            FileOutputStream stream = new FileOutputStream(file);
            basebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Toast.makeText(MainActivity.this, "保存图片成功", Toast.LENGTH_SHORT).show();

                  // Android设备Gallery应用只会在启动的时候扫描系统文件夹
                      // 这里模拟一个媒体装载的广播，用于使保存的图片可以在Gallery中查看
                    Intent intent = new Intent();
                     intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
                 intent.setData(Uri.fromFile(Environment
                                       .getExternalStorageDirectory()));
                     sendBroadcast(intent);
                 } catch (Exception e) {
                      Toast.makeText(MainActivity.this, "保存图片失败", Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                  }
    }
    
    private void resumecanvsa(){
        if(basebitmap != null) {
            basebitmap = Bitmap.createBitmap(canvasimg.getWidth(),canvasimg.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(basebitmap);
            canvas.drawColor(Color.WHITE);
            canvasimg.setImageBitmap(basebitmap);
            Toast.makeText(MainActivity.this,"清除画板成功，请重新绘制图画",Toast.LENGTH_SHORT).show();
        }
    }
}
