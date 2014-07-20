package jp.ohwada.android.notofont;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ZipUtil
 */
public class ZipUtil extends FileCommon{

	/**
	 * === Constructor ===
	 */
	public ZipUtil() {
		super();
		TAG_SUB = "FontUtil";
	}

	/**
	 * unzip
	 * @param String indir
	 * @param String name
	 * @param String outdir 	 
	 */
	public void unzip( String indir, String name, String outdir ) {
		File file = getFile( indir, name );
		unzip( file, outdir );
	}

	/**
	 * unzip
	 * @param File file
	 * @param String outdir 	 
	 */
	public void unzip( File file, String outdir ) {
		if ( file == null ) return;
		ZipInputStream zis = getZipInputStream( file );
		unzip( zis, outdir );
		if ( zis != null ) {
			try {
				zis.close();
			} catch (IOException e) {
				if (D) e.printStackTrace();
 			}
 		}	
	}

	/**
	 * unzip
	 * @param ZipInputStream zis
	 * @param String outdir 	 
	 */
	private void unzip( ZipInputStream zis, String outdir ) {
		if ( zis == null ) return;
 		try {
 		    ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count;
				while ((count = zis.read(buffer)) != -1) {
				 	baos.write(buffer, 0, count);
				}
				write( outdir, ze.getName(), baos.toByteArray() );
			}
		} catch (IOException e) {
			if (D) e.printStackTrace();
 		}
	}

	/**
	 * getZipInputStream
	 * @param File file
	 * @return ZipInputStream
	 */	
	private ZipInputStream getZipInputStream( File file ) {		
		ZipInputStream zis = null;
		try {
			FileInputStream fis = new FileInputStream( file );
			zis = new ZipInputStream( fis );
		} catch (Exception e) {
			if (D) e.printStackTrace();
		}
		if ( zis == null ) {
			log_d( "Zip file is null" );	
		}
		return zis;
	}

	/**
	 * write
	 * @param String dir
	 * @param String name
	 * @param byte[] bytes
	 */ 
	private void write( String dir, String name, byte[] bytes ) {
		File file = getFile( dir, name );
		write( file, bytes );
	}
    	
}
