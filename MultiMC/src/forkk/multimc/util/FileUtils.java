package forkk.multimc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Scanner;

public class FileUtils
{
	/**
	 * Recursively copies a file or directory to dest
	 * @param files the list of files and directories to copy
	 * @param dest the destination
	 * @param overwrite if true, existing files will be overwritten. Otherwise,
	 * they will be ignored.
	 */
	public static void recursiveCopy(File file, File dest, boolean overwrite)
			throws IOException
	{
//		System.out.println("Recursive copying " + file);
		if (!dest.exists())
			dest.mkdir();
		
		if (file.isDirectory())
		{
//			System.out.println("Recursing into " + file);
			for (File f : file.listFiles())
			{
				recursiveCopy(f, new File(dest, file.getName()), overwrite);
			}
		}
		else if (file.isFile())
		{
			try
			{
//				System.out.println("Copying file to " + dest);
				copyFile(file, new File(dest, file.getName()), true);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	// Test case
//	public static void main(String[] args)
//	{
//		try
//		{
//			recursiveCopy(new File("/home/forkk/Desktop/ModTest/mcJar"),
//					new File("/home/forkk/Desktop/ModTest/mcJar2"), true);
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Copies <tt>source</tt> to <tt>dest</tt>.
	 * @param source The file to copy
	 * @param dest The file to copy to
	 * @param overwrite If false, an <tt>IOException</tt> will be thrown if <tt>dest</tt>
	 * already exists.
	 * @throws IOException If an error occurs while copying the file. Or if the
	 * file is a directory.
	 */
	public static void copyFile(File source, File dest, boolean overwrite)
			throws IOException
	{
		if (!overwrite && dest.exists())
			throw new IOException("Can't copy " + source + " to " + dest + ". " +
					"The file already exists.");
		if (source.isDirectory())
			throw new IOException("Can't copy directories! Use recursiveCopy() " +
					"instead.");
		
		else if (overwrite && dest.exists())
			dest.delete();
		dest.createNewFile();
		
		// Declare file channels
		FileChannel sourceChan = null;
		FileChannel destChan = null;
		
		// Declare file IO streams
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		
		try
		{
			// Initialize IO streams
			fIn = new FileInputStream(source);
			fOut = new FileOutputStream(dest);
			
			// Get channels
			sourceChan = fIn.getChannel();
			destChan =  fOut.getChannel();
			
			// Transfer data
			long transfered = 0;
			long total = sourceChan.size();
			while (transfered < total)
			{
				transfered += destChan.transferFrom(sourceChan, 0, sourceChan.size());
				destChan.position(transfered);
			}
//			System.out.println("Transfered "  + transfered + " bytes from " +
//					source.toString() + " to " + dest.toString());
		} finally
		{
			// Make sure streams and channels get closed.
			if (sourceChan != null)
				sourceChan.close();
			else if (fIn != null)
					fIn.close();
			
			if (destChan != null)
				destChan.close();
			else if (fOut != null)
				fOut.close();
		}
	}
	
	public static String getFileText(File file) throws FileNotFoundException
	{
		if (!file.exists())
			throw new FileNotFoundException();
		else
		{
			Scanner scan = new Scanner(file);
			String fileString = "";
			while (scan.hasNext())
					fileString += scan.next();
			scan.close();
			return fileString;
		}
	}
	
	/**
	 * Deletes the given file. If it is a directory, all of it's contents will 
	 * be deleted as well.
	 * @param file the file or directory to delete
	 * @return false if the delete failed.
	 */
	public static boolean recursiveDelete(File file)
	{
		boolean fileIsSymlink;
		try
		{
			fileIsSymlink = isSymlink(file);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		if (file.isDirectory() && !fileIsSymlink)
		{
			for (File f : file.listFiles())
			{
				boolean success = recursiveDelete(f);
				if (!success)
					return false;
			}
		}
		
		file.delete();
		return true;
	}
	
	/**
	 * Checks if the given file is a symbolic link.
	 * @param file the file to check
	 * @return true if the given file is a symbolic link
	 */
	public static boolean isSymlink(File file) throws IOException
	{
		File canonicalFile;
		if (file.getParent() == null)
		{
			canonicalFile = file;
		} else
		{
			File canonicalDir = file.getParentFile().getCanonicalFile();
			canonicalFile = new File(canonicalDir, file.getName());
		}
		return !canonicalFile.getCanonicalFile().equals(canonicalFile.getAbsoluteFile());
	}
}
