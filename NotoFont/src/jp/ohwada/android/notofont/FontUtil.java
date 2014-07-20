package jp.ohwada.android.notofont;

import java.io.File;

import android.graphics.Typeface;

/**
 * FontUtil
 */
public class FontUtil extends FileCommon{

	/**
	 * === Constructor ===
	 */
	public FontUtil() {
		super();
		TAG_SUB = "FontUtil";
	}

	/**
	 * getTypeface
	 * @param String dir
	 * @param String name
	 * @return Typeface
	 */
	public Typeface getTypeface( String dir, String name ) {
		File file = getFile( dir, name );
		return getTypeface( file );
	}

	/**
	 * getTypeface
	 * @param File file
	 * @return Typeface
	 */
	public Typeface getTypeface( File file ) {
		if ( file == null ) return null;
		Typeface type = null;
		try {
			type = Typeface.createFromFile( file );
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		return type;	
	}
    	
}
