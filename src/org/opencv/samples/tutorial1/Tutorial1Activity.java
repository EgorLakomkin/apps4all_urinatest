package org.opencv.samples.tutorial1;


import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    static private int 				 numPhoto = 1;
    private boolean 			 setPhoto;
    private List<Mat>            listPhotos; 
    private boolean 			 sendInited;
    private Mat 				 rectangle;
    private Mat 				 hsv;
    private boolean  			 thereIsRect;
    private Mat 				 fullSizeRect;
    private Mat					 edge;
    private Mat					 gauss;
    
    
    private	Mat 				 hsvRect;
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

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial1_surface_view);

        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
        
        Button photo = (Button)findViewById(R.id.button1);
        OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(setPhoto == true) {
					setPhoto = false;
				} else {
					setPhoto = true;
				}
			}
		};
		photo.setOnClickListener(listener);
		listPhotos = new LinkedList<Mat>();
		sendInited = false;
		thereIsRect = false;
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
    public List<Integer> processing(List<Mat> image) {
    	List<Integer> res = Arrays.asList(new Integer[] { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0});
    	Mat img = image.get(0);
    	Mat resizedImage = new Mat(img.height(),img.width(), CvType.CV_8UC3);
    	
    	List<Mat> hsvList = new LinkedList<Mat>();
    	Core.split(resizedImage, hsvList);
    	Mat s = hsvList.get(1);
    	Mat h = hsvList.get(0);
    	Mat v = hsvList.get(2);
    	
    	
    	// medfilter
    	Mat s_thresholded = new Mat(resizedImage.height(), resizedImage.width(), CvType.CV_8UC1);
    	Imgproc.threshold(s, s_thresholded, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
    	Mat morphRect = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
    	Mat s_dilated = new Mat(resizedImage.height(), resizedImage.width(), CvType.CV_8UC1);
    	
    	Imgproc.dilate(s_thresholded, s_dilated, morphRect);
    	List<MatOfPoint> contours = new LinkedList<MatOfPoint>();
    	Mat hierarchy = new Mat(); 
    	Imgproc.findContours(s_dilated, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
    	List<MatOfPoint> approxCurve = new LinkedList<MatOfPoint>();
    	MatOfPoint2f temp = new MatOfPoint2f();
    	MatOfPoint2f temp2 = new MatOfPoint2f();
    	Log.d("contours","number_contours_" + String.valueOf(contours.size()));
    	
    	for(int i = 0; i < contours.size() ; i++) {
    		contours.get(i).convertTo(temp, CvType.CV_32FC2);
    		Imgproc.approxPolyDP(temp, temp2, 3, true);
    		MatOfPoint newMOP = new MatOfPoint();
    		
    		temp2.convertTo(newMOP,CvType.CV_32S );
    		
    		approxCurve.add( newMOP );
    	}
    	Log.d("contours","number_curves_" + String.valueOf(approxCurve.size()));
    	return res;
    }
    public void onCameraViewStarted(int width, int height) {
    	fullSizeRect = new Mat(height,width , CvType.CV_8UC4);
    	edge = new Mat(height,width , CvType.CV_8UC4);
    	hsv = new Mat(height,width , CvType.CV_8UC4);
    	gauss = new Mat(height,width , CvType.CV_8UC4);

		rectangle = new Mat(400, 80, CvType.CV_8UC4);
		hsvRect = new Mat(400, 80 , CvType.CV_8UC4);
    }

    public void onCameraViewStopped() {
    	
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	Mat img = inputFrame.rgba();
    	int widthImage = img.rows();
    	int colsImage = img.cols();
    	int widthStripe = 450;
    	int heightStripe = 110;
    	if(thereIsRect) {

        	Core.rectangle(img, new Point(colsImage/2 - heightStripe/2, widthImage/2 - widthStripe/2), new Point(colsImage/2 + heightStripe/2, widthImage/2 + widthStripe/2), new Scalar(0,120,0));
            
        	return img;
    	} else {
	    	if(setPhoto) {
	    			listPhotos.clear();
	    			listPhotos.add(inputFrame.rgba().submat(new Rect(
							new Point(colsImage/2 - heightStripe/2, widthImage/2 - widthStripe/2),
							new Point(colsImage/2 + heightStripe/2, widthImage/2 + widthStripe/2)))
							);
	    			List<Integer> res = processing(listPhotos);
	    			if (res == null) {
	    				setPhoto = false;
	    				listPhotos.clear();
	    			} else {
	    				if(!sendInited) {
	    					rectangle = img.submat(
	    									new Rect(
	    											new Point(colsImage/2 - heightStripe/2, widthImage/2 - widthStripe/2),
	    											new Point(colsImage/2 + heightStripe/2, widthImage/2 + widthStripe/2)
	    											)
	    									);
	    					thereIsRect = true;				
	    					setPhoto = false;
	    					Mat mIntermediateMat = new Mat();
	    					Imgproc.cvtColor(rectangle, mIntermediateMat, Imgproc.COLOR_RGBA2BGR, 3);
	    					File path =
						    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
						    String filename = "test.jpg";
						    File file = new File(path, filename);
						    Boolean bool = null;
						  	filename = file.toString();
						  	bool = Highgui.imwrite(filename, mIntermediateMat);
						  
							try {
								byte[] preved_img = IOUtil.readFile(file);
								Log.i("success", "LEN_" + String.valueOf(preved_img.length));
								new ByteArrayPost(this, Constants.servAddress + "/upload_jpeg",preved_img).execute();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Log.e("success", "error "+e.toString());
								e.printStackTrace();
							}
	    				}
	    			}
	    		
	    	}
    	}
    	Core.rectangle(img, new Point(colsImage/2 - heightStripe/2, widthImage/2 - widthStripe/2), new Point(colsImage/2 + heightStripe/2, widthImage/2 + widthStripe/2), new Scalar(0,120,0));
        /*
    	

    	*/
        return img;
    }
    
    
    void sendImage(String data) { 	// here we get user id
    	String user_id = "M5mMzrLAcU";
    	
    	    	
    	PostResultAsyncTask postResults = new PostResultAsyncTask(this);
    	postResults.execute(Constants.servAddress+"/push_result?user_id=" + user_id + "&image=" + data );
     }
    
    public static void sendAnalysisData(Activity _activity, List<Integer> _data)
    {
    	// here we get user id
    	String user_id = "M5mMzrLAcU";
    	String analysis = "";
    	for (int i = 0; i < _data.size(); i++)
    	{
    		analysis += _data.get(i).toString();
    		if (i != _data.size() - 1)
    			analysis += ";";
    	}
    		
    	    	
    	PostResultAsyncTask postResults = new PostResultAsyncTask(_activity);
    	postResults.execute(Constants.servAddress+"/push_result?user_id=" + user_id + "&analysis=" + analysis );
    }
}
