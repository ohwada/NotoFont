package jp.ohwada.android.notofont;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * MyDownloadManager
 */	
public class MyDownloadManager {

	private final static String TAG_SUB = "MyDownloadManager";

	public static final long ID_UNDEFINED = -1;
	public static final String DEFAULT_DIR = Environment.DIRECTORY_DOWNLOADS;

	private Context mContext;
	private DownloadManager mDownloadManager;

	private String mScheme = "http";
	private String mAuthority = "";
	private String mPath = "";
	private String mMimeType = "";
	private String mExternalPublicDir = DEFAULT_DIR;
	private String mExternalSubPath = "";

// callback
    private OnListener mListener = null;

    /**
     * The callback interface 
     */
    public interface OnListener {
        public void onComplete( long id );
    }

    /**
     * === Constructor ===
     * @param Context context
     */
	public MyDownloadManager( Context context ) {
		mContext = context;
		mDownloadManager = (DownloadManager) 
			context.getSystemService( Context.DOWNLOAD_SERVICE );
	}

	/**
	 * makeExternalPublicDir
	 */	
	public void makeExternalPublicDir() {	
		File dir = Environment.getExternalStoragePublicDirectory( 
			mExternalPublicDir );
		if ( !dir.exists() ) {
			dir.mkdirs();
		}	 
	}

	/**
	 * open
	 */	
	public void open() {
		IntentFilter filter1 = new IntentFilter( 
			DownloadManager.ACTION_DOWNLOAD_COMPLETE );
		mContext.registerReceiver( mReceiver, filter1 );
		IntentFilter filter2 = new IntentFilter( 
			DownloadManager.ACTION_NOTIFICATION_CLICKED );
		mContext.registerReceiver( mReceiver, filter2 );					
//		IntentFilter filter3 = new IntentFilter( 
//			DownloadManager.ACTION_VIEW_DOWNLOADS );
//		mContext.registerReceiver( mReceiver, filter3 );		
	}

	/**
	 * close
	 */
	public void close() {
		mContext.unregisterReceiver( mReceiver );
	}

	/**
	 * setScheme
	 * @param String scheme
	 */
	public void setScheme( String scheme ) {
		mScheme = scheme;
	}

	/**
	 * setAuthority
	 * @param String authority
	 */
	public void setAuthority( String authority ) {
		mAuthority = authority;
	}

	/**
	 * setPath
	 * @param String path
	 */
	public void setPath( String path ) {
		mPath = path;
	}

	/**
	 * setExternalPublicDir
	 * @param String dir
	 */
	public void setExternalPublicDir( String dir ) {
		mExternalPublicDir = dir;
	}

	/**
	 * setExternalSubPath
	 * @param String path
	 */
	public void setExternalSubPath( String path ) {
		mExternalSubPath = path;
	}

	/**
	 * setMimeType
	 * @param String type
	 */
	public void setMimeType( String type ) {
		mMimeType = type;
	}
		
	/**
	 * download
	 * @return long
	 */
	public long download() {	
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.scheme( mScheme );
		uriBuilder.authority( mAuthority );
		uriBuilder.path( mPath );
		Request request = new Request( uriBuilder.build() );
		request.setDestinationInExternalPublicDir( 
			mExternalPublicDir, mExternalSubPath );
		request.setAllowedNetworkTypes( 
			DownloadManager.Request.NETWORK_WIFI );
		request.setMimeType( mMimeType );
		long id = mDownloadManager.enqueue( request );
		return id;
	}

	/**
	 * remove
	 * @param long id
	 * @return int
	 */
	public int remove( long id ) {
		return mDownloadManager.remove( id );
	}
	
     /**
     * setOnListener
     * @param OnListener listener
     */
    public void setOnListener( OnListener listener ) {
        mListener = listener;
    }

	/**
	 * notifyComplete
	 * @param long id
	 */
	private void notifyComplete( long id ) {
		if ( mListener != null ) {
			mListener.onComplete( id );
		}
	}

	/**
	 * --- BroadcastReceiver ---
	 */
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
    	public void onReceive( Context context, Intent intent ) {
  			String action = intent.getAction();
			if ( DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals( action )) {
				long id = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, ID_UNDEFINED );
   				if ( id != ID_UNDEFINED ) {
   					logStatus( id );
   					notifyComplete( id );
   				}	
   			}
    	}
	};

	/**
	 * logStatus
	 * @param long id
	 */
	private void logStatus( long id ) {
		Query query = new Query();
		query.setFilterById( id );
		Cursor cursor = mDownloadManager.query( query );
		if ( cursor == null ) {
			log_d( "cannot get Cursor" );
			return;
		}
		cursor.moveToFirst();
		int status = cursor.getInt( 
			cursor.getColumnIndex( DownloadManager.COLUMN_STATUS ));
		int reason = cursor.getInt(
			cursor.getColumnIndex( DownloadManager.COLUMN_REASON ));
		cursor.close();	
		if ( status == DownloadManager.STATUS_FAILED ) {	
			log_d( "Failed reason " + Integer.toString(reason) );
		}
	}
	                    
	/**
	 * logcat
	 * @param String msg
	 */
	private void log_d( String msg ) {
		if (Constant.DEBUG) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}	
}
