package jp.ohwada.android.notofont;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Font Dialog
 */
public class FontDialog extends CommonDialog {

	private FontUtil mFontUtil;	
	private TextView 	mTextViewDefaultText;
	private TextView mTextViewNotoLabel;
	private TextView mTextViewNotoText;

	/**
	 * === Constructor ===
	 * @param Context context
	 */ 	
	public FontDialog( Context context ) {
		super( context );
		create();
	}

	/**
	 * === Constructor ===
	 * @param Context context
	 * @param int theme
	 */ 
	public FontDialog( Context context, int theme ) {
		super( context, theme ); 
		create();
	}

	/**
	 * create
	 */ 
	private void create() {
		TAG_SUB = "FontDialog";
		setContentView( R.layout.dialog_font );
		setLayoutFull();	

		setTitle( R.string.dialog_font_title );
		initButtonClose();

		mTextViewDefaultText = (TextView) findViewById( R.id.TextView_dialog_font_default_text );	
		mTextViewNotoLabel = (TextView) findViewById( R.id.TextView_dialog_font_noto_label );
		mTextViewNotoText = (TextView) findViewById( R.id.TextView_dialog_font_noto_text );	
			
		mFontUtil = new FontUtil();
	}

    /**
     * setFontName
     * @param String dir
     * @param String name
     */
	public void setFontName( String dir, String name ) {
		mTextViewNotoLabel.setText( name );
		Typeface typeface = mFontUtil.getTypeface( dir, name );
		int id = R.string.dialog_font_sample;
		if ( name.indexOf( "JP" ) != -1) {
			// japanese text if JP font
			id = R.string.dialog_font_sample_ja;
		}
		mTextViewDefaultText.setText( id );
    	if ( typeface != null  ) {
    		// show text
    		mTextViewNotoText.setText( id );
    		mTextViewNotoText.setTypeface( typeface );
		} else {
		    // show error if typeface is null
			mTextViewNotoText.setText( R.string.dialog_font_error );
		}
	}

}
