package forkk.multimc.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils
{
	/**
	 * Recursively copies a file or directory to dest
	 * @param files the list of files and directories to copy
	 * @param dest the destination
	 */
	public static void RecursiveCopy(File file, File dest) throws IOException
	{
		if (!dest.exists())
			dest.mkdir();
		
		if (file.isDirectory() && !Files.isSymbolicLink(file.toPath()))
		{
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(file.toPath());
			for (Path p : dirStream)
			{
				RecursiveCopy(p.toFile(), 
						dest.toPath().resolve(file.getName()).toFile());
			}
		}
		else if (file.isFile())
		{
			Files.copy(file.toPath(), dest.toPath().resolve(file.toPath().getFileName()));
		}
	}
}
