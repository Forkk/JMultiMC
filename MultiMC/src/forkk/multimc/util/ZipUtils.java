package forkk.multimc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{
	/**
	 * Extracts entry from zf into dest
	 * @param zf the zip file to extract from
	 * @param entry the entry in the zip to extract
	 * @param dest the destination to extract to
	 */
	public static void ExtractFromZip(ZipFile zf, ZipEntry entry, File dest)
			throws IOException
	{
		if (entry.isDirectory())
		{
			dest.mkdirs();
			return;
		}
		
		//if (!dest.getParentFile().exists())
		dest.getParentFile().mkdirs();
		
		if (!dest.exists())
			dest.createNewFile();
		
		int bufSize = 1024;
		
		InputStream is = zf.getInputStream(entry);
		BufferedInputStream in = new BufferedInputStream(is, bufSize);
		
		FileOutputStream fos = new FileOutputStream(dest);
		BufferedOutputStream out = new BufferedOutputStream(fos, bufSize);
		
		int count = 0;
		byte[] buffer = new byte[bufSize];
		while ((count = in.read(buffer, 0, buffer.length)) != -1)
		{
			out.write(buffer, 0, count);
		}
		out.flush();
		out.close();
		is.close();
	}
	
	public static void ExtractAllFromZip(ZipFile zf, 
			File dest) throws IOException
	{
		if (!dest.isDirectory())
			throw new InvalidParameterException("Destination must be a directory!");
		Enumeration<? extends ZipEntry> entries = zf.entries();
		
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
//			System.out.println("Extracting " + entry.toString());
			ExtractFromZip(zf, entry, new File(dest, entry.getName()));
		}
	}
	
	public static void recursiveAddToZip(File source, File zipFile,
			String pathInZip) throws IOException
	{
		recursiveAddToZip(new File[] { source }, zipFile, pathInZip);
	}
	
	public static void recursiveAddToZip(File[] files, File zipFile,
			String pathInZip) throws IOException
	{
		FileOutputStream fOut = null;
		ZipOutputStream zipOut = null;
		
		try
		{
			fOut = new FileOutputStream(zipFile);
			zipOut = new ZipOutputStream(fOut);
			
			for (File f : files)
			{
				recursiveAddToZip(f, zipOut, pathInZip);
			}
		} finally
		{
			if (zipOut != null)
				zipOut.close();
			else if (fOut != null)
				fOut.close();
		}
	}
	
	private static void recursiveAddToZip(File source, ZipOutputStream zipOut,
			String pathInZip) throws IOException
	{
		if (source.isDirectory())
		{
			if (pathInZip.equals(""))
				pathInZip = source.getName();
			else
				pathInZip = pathInZip + "/" + source.getName();
//			System.out.println("Recursing into " + source + " with path " + pathInZip);
			// Recurse
			for (File f : source.listFiles())
			{
				recursiveAddToZip(f, zipOut, pathInZip);
			}
		}
		else if (source.isFile())
		{
//			System.out.println("Adding " + source.toString() + " to " + 
//					zipFile.toString());
			addToZip(zipOut, source, pathInZip);
		}
	}
	
	public static void addToZip(File zipFile, File source, String pathInZip)
			throws IOException
	{
		addToZip(zipFile, new File[] { source }, pathInZip);
	}
	
	/**
	 * Adds the given file to the given zip file.
	 * @param zipFile the zip file to add to
	 * @param files file to add
	 */
	public static void addToZip(File zipFile, File[] files, String pathInZip)
			throws IOException
	{
		FileOutputStream fOut = null;
		ZipOutputStream zipOut = null;
		
		try
		{
			fOut = new FileOutputStream(zipFile);
			zipOut = new ZipOutputStream(fOut);
			
			for (File f : files)
			{
				addToZip(zipOut, f, pathInZip);
			}
		} finally
		{
			if (zipOut != null)
				zipOut.close();
			else if (fOut != null)
				fOut.close();
		}
	}
	
	private static void addToZip(ZipOutputStream zipOut, File source, String pathInZip)
			throws IOException
	{
		BufferedOutputStream out = null;
		
		FileInputStream fIn = null;
		BufferedInputStream in = null;
		
		final int bufSize = 512;
		try
		{
			// Open our streams
			fIn = new FileInputStream(source);
			in = new BufferedInputStream(fIn, bufSize);
			
			String filePathInZip = pathInZip + "/" + source.getName();
			if (pathInZip.equals(""))
				filePathInZip = source.getName();
			
//			System.out.println("Adding " + filePathInZip);
			ZipEntry entry = new ZipEntry(filePathInZip);
			zipOut.putNextEntry(entry);
			
			out = new BufferedOutputStream(zipOut, bufSize);
			
			int count = 0;
			byte[] buffer = new byte[bufSize];
			while ((count = in.read(buffer, 0, buffer.length)) > -1)
			{
				out.write(buffer, 0, count);
			}
			out.flush();
			zipOut.closeEntry();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if (in != null)
				in.close();
			else if (fIn != null)
				fIn.close();
		}
	}
	
	// Test case
//	public static void main(String args[])
//	{
//		
//	}
}
