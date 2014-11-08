package org.crazyit.sound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.datatype.DatatypeConstants.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class CaptureImage extends Activity
{
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	// ����ϵͳ���õ������
	Camera camera;
	// �Ƿ���Ԥ����
	boolean isPreview = false;
	
	
	private MyView myView;
	private AnimationDrawable anim1;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		// ��ȡ���ڹ�����
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// ��ȡ��Ļ�Ŀ�͸�
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// ��ȡ������SurfaceView���
		sView = (SurfaceView) findViewById(R.id.sView);
		// ���ø�Surface����Ҫ�Լ�ά��������
		sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ���SurfaceView��SurfaceHolder
		surfaceHolder = sView.getHolder();
		// ΪsurfaceHolder���һ���ص�������
		
		
		FrameLayout frame=new FrameLayout(this);
		setContentView(frame);
		myView=new MyView(this);
		myView.setVisibility(View.INVISIBLE);
		anim1=(AnimationDrawable) myView.getBackground();
		frame.addView(myView);
		frame.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View source,MotionEvent event){
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					anim1.stop();
					float x=event.getX();
					float y=event.getY();
					myView.setLocation((int)y-40,(int)x-20);
					myView.setVisibility(View.VISIBLE);
					anim1.start();
				}
				return false;
			}
			
		});
			
		
		surfaceHolder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height)
			{
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				// ������ͷ
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				// ���camera��Ϊnull ,�ͷ�����ͷ
				if (camera != null)
				{
					if (isPreview) camera.stopPreview();
					camera.release();
					camera = null;
				}
			}
		});
	}
	
	class MyView extends ImageView{
		public MyView(Context context){
			super(context);
		}

		public void setLocation(int top, int left) {
			this.setFrame(left,top,left+40,top+40);
		}
		
	}

	protected void onDraw(Canvas canvas){
		try{
			java.lang.reflect.Field field=AnimationDrawable.class.getDeclaredField("mCurFrame");
		field.setAccessible(true);
		int curFrame=field.getInt(anim1);
		if (curFrame==anim1.getNumberOfFrames()-1){
			myView.setVisibility(View.INVISIBLE);
		}
		}
		catch (Exception e){
	}
		
	}
	
	private void initCamera()
	{
		if (!isPreview)
		{
			// �˴�Ĭ�ϴ򿪺�������ͷ��
			// ͨ������������Դ�ǰ������ͷ
			camera = Camera.open(0);  //��
			camera.setDisplayOrientation(90);
		}
		if (camera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = camera.getParameters();
				// ����Ԥ����Ƭ�Ĵ�С
				parameters.setPreviewSize(screenWidth, screenHeight);
				// ����Ԥ����Ƭʱÿ����ʾ����֡����Сֵ�����ֵ
				parameters.setPreviewFpsRange(30, 60);
				// ����ͼƬ��ʽ
				parameters.setPictureFormat(ImageFormat.JPEG);
				// ͨ��SurfaceView��ʾȡ������
				camera.setPreviewDisplay(surfaceHolder);  //��
				// ��ʼԤ��
				camera.startPreview();  //��
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isPreview = true;
		}
	}


}


