package com.example.slidkuang;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;





public class MainActivity extends Activity implements OnTouchListener {
private static int menuwidth;

private static int padding=80;

private static int right=0;
private LinearLayout.LayoutParams menuparams; 
private  static int leftmargin;
private View content,menu;
private int screenwidth;
private int left;
private float downx;
private float movex;
private float onx;
private int distance;
private static boolean ismenu=false;
private VelocityTracker mVelocityTracker;
public static final int SNAP_VELOCITY = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       content=findViewById(R.id.content);
        menu=findViewById(R.id.menu);
        init();
        content.setOnTouchListener(this);
        
    }
    public void init(){
    	WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	screenwidth= window.getDefaultDisplay().getWidth();
    	menuparams=(LinearLayout.LayoutParams) menu.getLayoutParams();
    	menuparams.width=screenwidth-padding;
    	left=-menuparams.width;
    	menuparams.leftMargin=padding-screenwidth;
    	
    }
    public boolean onTouch(View v, MotionEvent event){
    	createVelocityTracker(event);  
    	 switch (event.getAction()){
    		case  MotionEvent.ACTION_DOWN:
    		downx=event.getRawX();
    		break;
    		case MotionEvent.ACTION_MOVE:
    			movex=event.getRawX();
    			distance=(int)(movex-downx);
    			if(ismenu){
    				menuparams.leftMargin=right+distance;
    		Log.i("lkk", "ssb");
    			}
    			else{
    				menuparams.leftMargin=left+distance;
    			}
    			 menu.setLayoutParams(menuparams);  
    			break;
    		case MotionEvent.ACTION_UP:
    			onx=event.getRawX();
    			if(ismenu&&(downx-onx>0)){
    				if(wantcontent()){
    					showcontent();
    				}
    			}
    			else if(ismenu&&(onx-downx>0)){
    				if(wantmenu()){
    					showmenu();
    				}
    			}
    			break;
    			default:
    				break;
    	}
    	 return true;
    }
    private void createVelocityTracker(MotionEvent event) {  
        if (mVelocityTracker == null) {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
    }  
    private boolean wantcontent(){
    	return (getScrollVelocity() > SNAP_VELOCITY)||((downx-onx+padding)>screenwidth/2);
    }
    private boolean wantmenu(){
    	return (getScrollVelocity() > SNAP_VELOCITY)||((onx-downx)>screenwidth/2);
    }
    private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) mVelocityTracker.getXVelocity();  
        return Math.abs(velocity);  
    }  
  private void showmenu(){
	  new task().execute(30);
  }
  private void showcontent(){
	  new task().execute(-30);
  }
   class task extends AsyncTask<Integer, Integer, Integer>{

	@Override
	protected Integer doInBackground(Integer... speed) {
		// TODO Auto-generated method stub
		 int leftmargin = menuparams.leftMargin; 
		while(true){
			leftmargin=leftmargin+speed[0];
			if(leftmargin<left){
				leftmargin=left;
				
				break;
			}
			else if(leftmargin>right){
				leftmargin=right;
				
				break;
			}
			publishProgress(leftmargin);
			sleep(20);
		}
		if(speed[0]>0){
			ismenu=true;
		}
		else if(speed[0]<0){
			ismenu=false;
		}
		
		return leftmargin;
	}
	protected void onProgressUpdate(Integer... leftMargin) {  
        menuparams.leftMargin = leftMargin[0];  
        menu.setLayoutParams(menuparams);  
    }  
	protected void onPostExecute(Integer leftMargin) {  
        menuparams.leftMargin = leftMargin;  
        menu.setLayoutParams(menuparams);  
    }  
  
	  
   private void sleep(long millis) {  
       try {  
           Thread.sleep(millis);  
       } catch (InterruptedException e) {  
           e.printStackTrace();  
       }  
   } } 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
