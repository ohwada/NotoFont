package jp.ohwada.android.notofont;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * NoteDownloadManager
 */	
public class NoteDownloadManager {

	private final static String TAG_SUB = "NoteDownloadManager";

// download
	private static final String AUTHORITY = "www.google.com";
	private static final String BASE_PATH = "/get/noto/pkgs/";
	private static final String MIME_TYPE = "application/zip";

	private static final long DOWN_ID_UNDEFINED = MyDownloadManager.ID_UNDEFINED;	
	private static final String DOWN_DIR = MyDownloadManager.DEFAULT_DIR;
	
	private Context mContext;
	private MyDownloadManager mDownloadManager;
	private ZipUtil mZipUtil;

	private String mFontDir = "";

	private int mMaxId = 0;		
	private long[] mDownIds = null;

// callback
	private OnListener mListener = null;

    /**
     * The callback interface 
     */
	public interface OnListener {
		public void onComplete();
	}

    /**
     * === Constructor ===
     * @param Context context
     */
	public NoteDownloadManager( Context context ) {
		mContext = context;
		mDownloadManager = new MyDownloadManager( context );
		mDownloadManager.setOnListener( new MyDownloadManager.OnListener() {
			@Override
			public void onComplete( long down_id ) {
				unzip( down_id );				
			}
		});
		
		mZipUtil = new ZipUtil();
		mMaxId = Constant.DOWNLOAD_NAMES.length;
		mDownIds = new long[ mMaxId ];
	}

    /**
     * init
     */
	public void init() {
		mDownloadManager.open();
		mDownloadManager.setAuthority( AUTHORITY );		
		mDownloadManager.setMimeType( MIME_TYPE );
		mDownloadManager.makeExternalPublicDir();
		initDownIds();
	}

    /**
     * setFontDir
     * @param String dir
     */
	public void setFontDir( String dir ) {
		mFontDir = dir;
		mZipUtil.makeExternalDir( mFontDir );
	}

    /**
     * close
     */
	public void close() {
		mDownloadManager.close();
	}

	/** 
	 * download
	 * @param int id
	 */
	public void download( int zip_id ) {
		if ( !checkZipId( zip_id )) return;
		String name = getZipName( zip_id );
		File file = mZipUtil.getFile( DOWN_DIR, name );
		if ( file.exists() ) {
			// show alert if file exists
			showAlert( zip_id );
		} else {
			execDownload( zip_id );
		}
	}
				
	/**
	 * download
	 * @return long
	 */
	private void execDownload( int zip_id ) {
		toast_show( R.string.downloading );
		String name = getZipName( zip_id );
		File file = mZipUtil.getFile( DOWN_DIR, name );
		if ( file.exists() ) {
			// delete file if exist
			file.delete();
		}
		mDownloadManager.setPath( BASE_PATH + name );
		mDownloadManager.setExternalSubPath( name );
		mDownIds[ zip_id ] = mDownloadManager.download();	
	}

	/**
	 * unzip
	 * @param long down_id
	 */ 
	private void unzip( long down_id ) {	
		int zip_id = getZipId( down_id );
		if ( !checkZipId( zip_id )) return;
		String name = getZipName( zip_id );
		mZipUtil.unzip( DOWN_DIR, name, mFontDir );
		toast_show( R.string.downloaded );
		notifyComplete();
	}

	/**
	 * checkZipId
	 * @param int zip_id
	 * @return boolean
	 */ 
	private boolean checkZipId( int zip_id ) {		
		if (( zip_id >= 0 )&&( zip_id < mMaxId )) {
			return true;
		}
		return false;
	}

	/**
	 * getZipId
	 * @param long down_id
	 * @return int
	 */ 
	private int getZipId( long down_id ) {
		for ( int i = 0; i < mMaxId ; i++ ) {
			if ( down_id == mDownIds[ i ] ) {
				mDownIds[ i ] = DOWN_ID_UNDEFINED;
				return i;
			}
		}
		return (int)DOWN_ID_UNDEFINED;
	}

	/**
	 * initDownIds
	 */ 
	private void initDownIds() {
		for ( int i = 0; i < mMaxId ; i++ ) {
			mDownIds[ i ] = DOWN_ID_UNDEFINED;
		}
	}

	/**
	 * getZipName
	 * @param int zip_id 
	 * @return String
	 */ 
	private String getZipName( int zip_id ) {	
		return Constant.DOWNLOAD_NAMES[ zip_id ];
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
	 */
	private void notifyComplete() {
		if ( mListener != null ) {
			mListener.onComplete();
		}
	}

	/** 
	 * showAlert
	 * @param int zip_id
	 */
	private void showAlert( int zip_id ) {		
        final int id = zip_id;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( mContext );
        alertDialogBuilder.setTitle( R.string.dialog_alert_title );
        alertDialogBuilder.setMessage( R.string.dialog_alert_message );
        alertDialogBuilder.setPositiveButton( 
        	R.string.button_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					// clock Yes to download
					execDownload( id );
				}
			}
		);
        alertDialogBuilder.setNegativeButton( R.string.button_no, null );
        alertDialogBuilder.setCancelable( true );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}

	/**
	 * toast_show
	 * @param int res_id
	 */ 
	private void toast_show( int res_id ) {
		ToastMaster.showText( mContext, res_id, Toast.LENGTH_SHORT );
	}
            
	/**
	 * logcat
	 * @param String msg
	 */
	@SuppressWarnings("unused")
	private void log_d( String msg ) {
		if (Constant.DEBUG) Log.d( Constant.TAG, TAG_SUB + " " + msg );
	}	
}
