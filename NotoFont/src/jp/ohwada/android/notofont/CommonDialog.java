package jp.ohwada.android.notofont;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Common Dialog
 */
public class CommonDialog extends Dialog {

	protected String TAG_SUB = "CommonDialog";
	
	// constant
	private final static float WIDTH_RATIO_FULL = 0.95f;

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public CommonDialog( Context context ) {
		super( context );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public CommonDialog( Context context, int theme ) {
		super( context, theme ); 
	}

	/**
	 * initButtonClose
	 */ 
	protected void initButtonClose() {	
		Button btnClose = (Button) findViewById( R.id.Button_dialog_close );
		btnClose.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				dismiss();
			}
		});
	}

	/**
	 * setLayout
	 */ 
	protected void setLayoutFull() {
		setLayout( getWidthFull() );
	}

	/**
	 * setLayout
	 * @param int width
	 */ 
	protected void setLayout( int width ) {
		getWindow().setLayout( width, ViewGroup.LayoutParams.WRAP_CONTENT );
	}

	/**
	 * getWidthFull
	 * @return int width
	 */ 
	protected int getWidthFull() {
		int width = (int)( getWindowWidth() * WIDTH_RATIO_FULL );
		return width;
	}	 

	/**
	 * getWindowWidth
	 * @return int width
	 */ 
	protected int getWindowWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService( Context.WINDOW_SERVICE );
		Display display = wm.getDefaultDisplay();
		Point point = new Point();  
		display.getSize( point );
		int width = point.x;  
		return width;
	}

	/**
	 * log_d
	 * @param String msg
	 */
	protected void log_d( String msg ) {		
		if (Constant.DEBUG) Log.d( Constant.TAG, TAG_SUB + " " + msg );
    }	
}
