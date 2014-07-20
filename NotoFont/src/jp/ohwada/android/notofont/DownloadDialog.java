package jp.ohwada.android.notofont;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Download Dialog
 */
public class DownloadDialog extends CommonDialog {

// callback
	private OnListener mListener = null;

    /**
     * The callback interface 
     */
	public interface OnListener {
		public void onSelect( int position );
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public DownloadDialog( Context context ) {
		super( context );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public DownloadDialog( Context context, int theme ) {
		super( context, theme ); 
		create();
	}

	/**
	 * create
	 */ 
	private void create() {
		TAG_SUB = "DownloadDialog";
		setContentView( R.layout.dialog_download );
		setLayoutFull();	

		setTitle( R.string.dialog_download_title );
		initButtonClose();

		// list
		List<String> list = new ArrayList<String>();
		String n2 = "";
		for( String n1: Constant.DOWNLOAD_NAMES ) {
			// remove "-unhinted.zip" "-hinted.zip"
			n2 = n1.replace( "-unhinted.zip", "" ).replace( "-hinted.zip", "" );
			list.add( n2 );
		}

		// apapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getContext(), android.R.layout.simple_list_item_1, list );

		ListView listview = (ListView) findViewById( R.id.ListView_dialog_download );
		listview.setAdapter( adapter ); 
		listview.setOnItemClickListener( new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				dismiss();
				notifySelect( position );
			}
		});

	}
    
	/**
     * setOnListener
     * @param OnListener listener
     */
	public void setOnListener( OnListener listener ) {
		mListener = listener;
	}

	/**
	 * notifySelect
	 * @param int position
	 */
	private void notifySelect( int position ) {
		if ( mListener != null ) {
			mListener.onSelect( position );
		}
	}
	
}
