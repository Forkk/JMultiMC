package forkk.multimc.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import de.schlichtherle.truezip.file.TFile;
import forkk.multimc.data.Instance;

public class JarBuilder extends BackgroundTask
{
	Instance instance;
	
	public JarBuilder(Instance inst)
	{
		this.instance = inst;
	}
	
	@Override
	public void TaskStart()
	{
		
	}
	
	private void recursiveAdd(File file, TFile jarFile, File pathInJar) throws IOException
	{
		if (file.isDirectory() && !Files.isSymbolicLink(file.toPath()))
		{
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(file.toPath());
			for (Path p : dirStream)
			{
				recursiveAdd(p.toFile(), jarFile, 
						pathInJar.toPath().resolve(file.toPath()).toFile());
			}
		}
		else if (file.isFile())
		{
			if (file.getName().substring(file.getName().lastIndexOf('.')).equals(".zip"))
			{
				
			}
			else
			{
				
			}
		}
	}
	
	private void recursiveAddFromZip(TFile zfile, TFile jarFile, File pathInJar) throws IOException
	{
		for (TFile f : zfile.listFiles())
		{
			
		}
	}
}
