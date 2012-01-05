package forkk.multimc.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
	
//	/**
//	 * Adds file to entry in zf
//	 * @param zipFile the zip file to add to
//	 * @param entry the entry in the zip to add to
//	 * @param file the file to add
//	 */
//	public static void AddToZip(ZipFile zipFile, ZipEntry entry, File file) throws IOException
//	{
//		FileInputStream zin = new ZipInputStream(zipFile.getInputStream(entry));
//		ZipOutputStream fos = new ZipOutputStream(new FileOutputStream(zipFile));
//		
//		int count = 0;
//		byte[] buffer = new byte[512];
//		while ((count = zin.read(buffer, 0, buffer.length)) != -1)
//		{
//			fos.write(buffer, 0, count);
//		}
//		fos.flush();
//		fos.close();
//		zin.close();
//	}
//	
	/**
	 * Transfers the entry in source to dest
	 * @param source the source zip file
	 * @param entry the entry
	 * @param dest the destination zip file
	 */
	public static void Transfer(ZipFile source, ZipEntry entry, File dest) throws IOException
	{
		ZipInputStream zin = new ZipInputStream(source.getInputStream(entry));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
		
		int count = 0;
		byte[] buffer = new byte[512];
		while ((count = zin.read(buffer, 0, buffer.length)) != -1)
		{
			zos.write(buffer, 0, count);
		}
		zos.flush();
		zos.close();
		zin.close();
	}
}
