package jp.ohwada.android.notofont;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

/**
 * FileCommon
 */
public class FileCommon {

	protected boolean D = Constant.DEBUG;
	protected String TAG_SUB = "FileCommon";

	/**
	 * === Constructor ===
	 */
	public FileCommon() {
		// dummy
	}

	/**
	 * makeExternalDir
	 * @param String dir
	 */	
	public void makeExternalDir( String dir ) {	
		File path = new File( getDirPath( dir ) );
		if ( !path.exists() ) {
			path.mkdirs();
		}	 
	}

	/**
	 * write
	 * @param File file
	 * @param byte[] bytes 
	 */ 
	public void write( File file, byte[] bytes ) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream( file, true );
			os.write( bytes );
		} catch ( FileNotFoundException e ) {
			if (D) e.printStackTrace();
		} catch ( IOException e ) {
			if (D) e.printStackTrace();
		}
		if ( os != null ) {
			try {
				os.close();
			} catch ( IOException e ) {
				if (D) e.printStackTrace();
			}
		}
	}

	/*
	 * getList
	 */
	public List<String> getList( String dir, String[] ext_array ) {	
		String dir_path = getDirPath( dir );
		File file_dir = new File( dir_path );
		List<String> list = new ArrayList<String>();
		for ( File f: file_dir.listFiles() ) {
			String name = f.getName();
			String ext = parseExt( name );
			if ( matchExts( ext, ext_array ) ) {
				list.add( name );
			}
		}
		return list;
	}

	/**
	 * matchExt
	 * @param String ext
	 * @param String[] ext_array
	 * @return boolean
	 */
	public boolean matchExts( String ext, String[] ext_array ) {	
		for ( String e: ext_array ) {
			if ( ext.equals( e ) ) {
				return true;
			}
		}
		return false;
	}
		
	/**
	 * parseExt
	 * @param String name 
	 * @return String
	 */
	public String parseExt( String name ) {
		int point = name.lastIndexOf(".");
		if ( point != -1 ) {
			return name.substring( point + 1 );
		}
		return "";
	}

	/**
	 * getExternalStorageDirectory(
	 * @return File
	 */ 
	public File getExternalStorageDirectory() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * get dirctory path
	 * @param String dir
	 * @return String
	 */ 
	public String getDirPath( String dir ) {
		String path = getExternalStorageDirectory().getPath();
		path += File.separator + dir;
		return path;
	}

	/**
	 * getFile
	 * @param String dir
	 * @param String name
	 * @return File
	 */ 
	public File getFile( String dir, String name ) {
		String path = getDirPath( dir ) + File.separator + name;
		File file = new File( path );
		return file;
	}

	/**
	 * log_d
	 * @param String msg
	 */
	protected void log_d( String msg ) {		
		if (D) Log.d( Constant.TAG, TAG_SUB + " " + msg );
    }
    	
}
