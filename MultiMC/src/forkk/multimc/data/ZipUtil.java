package forkk.multimc.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil
{
	/**
	 * Extracts entry from zf into dest
	 * @param zf the zip file to extract from
	 * @param entry the entry in the zip to extract
	 * @param dest the destination to extract to
	 */
	public static void ExtractFromZip(ZipFile zf, ZipEntry entry, File dest) throws IOException
	{
		ZipInputStream zin = new ZipInputStream(zf.getInputStream(entry));
		FileOutputStream fos = new FileOutputStream(dest);
		
		int count = 0;
		byte[] buffer = new byte[512];
		while ((count = zin.read(buffer, 0, buffer.length)) != -1)
		{
			fos.write(buffer, 0, count);
		}
		fos.flush();
		fos.close();
		zin.close();
	}
}
