package jp.ohwada.android.notofont;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * MainActivity
 */
public class MainActivity extends ListActivity {

	private final static String TAG_SUB = "MainActivity";

	private static final String FONT_DIR = "fonts";
	private static final String[] FONT_EXTS = { "ttf", "otf" };
	
	private NoteDownloadManager mDownloadManager;
	private FileCommon mFileCommon;

	private List<String> mList = new ArrayList<String>();
	private ArrayAdapter<String>	mAdapter = null;

	/**
	 * === onCreate ===
	 */		
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

        Button btnDownload = (Button) findViewById( R.id.Button_download );
        btnDownload.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				showDownloadDialog();
			}	
		});

		// apapter
		mAdapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, mList );

	    ListView listview = getListView();
		listview.setAdapter( mAdapter ); 
		listview.setOnItemClickListener( new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				execItemClick( position );
			}
		});

		mDownloadManager = new NoteDownloadManager( this );
		mDownloadManager.init();
		mDownloadManager.setFontDir( FONT_DIR );
		mDownloadManager.setOnListener( new NoteDownloadManager.OnListener() {
			@Override
			public void onComplete() {			
				showList();	
			}
		});

		mFileCommon = new FileCommon();
	}

	/*
	 * === onResume ===
	 */
	@Override
	protected void onResume() {
		super.onResume();
		showList();
	}
	
	/*
	 * === onDestroy ===
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDownloadManager.close();
	}

	/*
	 * showList
	 */
	private void showList() {	
		mList.clear();
		mList.addAll( mFileCommon.getList( FONT_DIR, FONT_EXTS ) );
		mAdapter.notifyDataSetChanged();	
	}

	/** 
	 * execItemClick
	 * @param int position	 
	 */
	private void execItemClick( int position ) {	
		// check position
		if (( position < 0 )||( position >= mList.size() )) {
		   	return;
		}

		// get name
		String name = mList.get( position );
   		if ( name == null ) {
   			return;
   		}

		showDialogFont( name );			
	}

	/** 
	 * showDialogFont
	 * @param String name 
	 */
	private void showDialogFont( String name ) {
		FontDialog dialog = new FontDialog( this );
	    dialog.setFontName( FONT_DIR, name );
		dialog.show();
	}

	/** 
	 * showDownloadDialog
	 */
	private void showDownloadDialog() {
		DownloadDialog dialog = new DownloadDialog( this );
		dialog.show();
		dialog.setOnListener( new DownloadDialog.OnListener() {
			@Override
			public void onSelect( int position ) {			
				mDownloadManager.download( position );
			}
		});
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
