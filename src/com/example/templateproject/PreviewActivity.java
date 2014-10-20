package com.example.templateproject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;


public class PreviewActivity extends Activity implements CvCameraViewListener2{
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    private AudioManager mAudioManager;
    private org.opencv.core.Size Size;
    private org.opencv.core.Size Bsize;
    private Point rectp1, rectp2, textp;
    private Scalar rects, texts, black;
    private Rect dstrect;
    private double[] p;
    private boolean calibrated = false;
    private boolean init = false;
    private MatOfInt[] mChannels;
    private Mat eMat;
    private Mat Hhist;
    private Mat Shist;
    private Mat Vhist;
    private Mat element;
    
    private MatOfInt mHistSize;
    private MatOfFloat mRanges;
    private MinMaxLocResult result;
    private int HistSize;
    private Moments moment;
    private Point mc;
    private screen Screen;

    private double Hmode, Smode, Vmode;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
              
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    
    private GestureDetector mGestureDetector;
     
    public PreviewActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
            	calibrated = true;
            	Screen.setVisibility(SurfaceView.VISIBLE);
            	//mOpenCvCameraView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            	//mOpenCvCameraView.setVisibility(SurfaceView.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

    	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.preview);
        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
        
        Screen = (screen) findViewById(R.id.myscreen);
        Screen.receiveAudio(mAudioManager);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        Screen.setVisibility(SurfaceView.INVISIBLE);
        Screen.setZOrderMediaOverlay(true);
        mOpenCvCameraView.setCvCameraViewListener(this);
       
        Size = new org.opencv.core.Size(3,3);
        Bsize = new org.opencv.core.Size(5,5);
        p = new double[3];
    	rectp1 = new Point(200,85);
    	rectp2 = new Point(300, 175);
    	rects = new Scalar(255,0,5);
    	black = new Scalar(0,0,0);
    	textp = new Point(5,50);
    	texts = new Scalar(0,0,0);
    	dstrect = new Rect(200, 85, 100, 90);
    	mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);
    	

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;

            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
                toastMesage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
                toastMesage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }

    public void onCameraViewStarted(int width, int height) {
    	mChannels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
    	mHistSize = new MatOfInt(17);
    	HistSize = 17;
    	mRanges = new MatOfFloat(0f, 256f);
    	eMat = new Mat();
    	Hhist = new Mat();
    	Shist = new Mat();
    	Vhist = new Mat();
    	Hmode = 0;
    	Smode = 0;
    	Vmode = 0;
    	result = new MinMaxLocResult();
    	element = Imgproc.getStructuringElement(  0,
                Bsize,
                new Point( 2, 2 ) );	
    	mc = new Point(255,120);
    }

    public void onCameraViewStopped() {
    	Log.d("wtf", "blablabla stopped");
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	Mat src=inputFrame.rgba();
    	
    	//if not init
    	if(calibrated){
    		
    		Imgproc.blur(src, src, Size);
    		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2HSV);
    		//Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2YCrCb);
    		
    		if(!init){
    			screenInit(src);
    		}

    		Core.inRange(src, new Scalar(Hmode*255/HistSize,(Smode-1)*255/HistSize-10,(Vmode*255/HistSize-40)), new Scalar((Hmode+1)*255/HistSize,(Smode+2)*255/HistSize+10,((Vmode+1)*255/HistSize+40)), src);
    		Imgproc.erode(src, src, element);
    		Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
    		int max=0;
    		if(!contours.isEmpty()){
    			for(int i = 0;i<contours.size();i++){
    				if(contours.get(i).total() >= contours.get(max).total() && contours.get(i).total()>100)
    					max = i;
    			}
				src.setTo(black);
				moment = Imgproc.moments(contours.get(max), true);
				mc = new Point(moment.get_m10()/moment.get_m00(), moment.get_m01()/moment.get_m00());
				Log.d("bala in onCameraFrame", "x " + mc.x + "y " + mc.y);
				changeMc();
    		}
			
		}
			
    		//Imgproc.blur(src, src, Bsize);
    		//Core.inRange(src, new Scalar(Hmode*255/HistSize-20,Smode*255/HistSize,(Vmode*255/HistSize-20)), new Scalar((Hmode+1)*255/HistSize+20,(Smode+1)*255/HistSize,(Vmode+1)*255/HistSize), src);
    		//Core.rectangle(src, rectp1, rectp2, rects, 2);
    		//Core.putText(src, "there are"+(int)(contours.size())+" contours ", textp, 3, 1, texts, 1);
    	//*ss
    	
    	else{
    		Core.rectangle(src, rectp1, rectp2, rects, 2);
    		Core.putText(src, "Tap to calibrate palm color", textp, 3, 1, texts, 1);
    	}
    		
    
    		
        return src;
    }

    
    private void screenInit(Mat src) {
		// TODO Auto-generated method stub
    	
    	Mat dst=new Mat(src, dstrect);
		determine(dst);
		init = true;
		
		decideMax(Hhist);
		Hmode = result.maxLoc.x + result.maxLoc.y*Hhist.width();
		decideMax(Shist);
		Smode = result.maxLoc.x + result.maxLoc.y*Shist.width();
		decideMax(Vhist);
		Vmode = result.maxLoc.x + result.maxLoc.y*Vhist.width();
		
	}

	private void decideMax(Mat dst) {
		// TODO Auto-generated method stub
    	result = Core.minMaxLoc(dst);
    
	}

	private void determine(Mat dst) {
		// TODO Auto-generated method stub
    	
    	
		Imgproc.calcHist(Arrays.asList(dst), mChannels[0], eMat, Hhist, mHistSize, mRanges);
		Imgproc.calcHist(Arrays.asList(dst), mChannels[1], eMat, Shist, mHistSize, mRanges);
		Imgproc.calcHist(Arrays.asList(dst), mChannels[2], eMat, Vhist, mHistSize, mRanges);
		

	}
	public void changeMc(){
		Screen.user = mc;
		Log.d("bala in changeMc", "x " + mc.x + "y " + mc.y);
	}
	
	
}
