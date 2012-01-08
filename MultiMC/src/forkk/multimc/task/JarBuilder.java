package forkk.multimc.task;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import forkk.multimc.data.Instance;
import forkk.multimc.gui.SelectionWindow;
import forkk.multimc.util.FileUtils;
import forkk.multimc.util.ZipUtils;

public class JarBuilder extends BackgroundTask
{
	Instance instance;
	
	public JarBuilder(Instance inst)
	{
		this.instance = inst;
		buildLog = Logger.getLogger(SelectionWindow.getMainLogName() + ".JarBuildLogger");
	}
	
	Logger buildLog;
	
	private static final String jarBackupName = "mcbackup.jar";
	
	/*
	 * Protocol:
	 * 
	 * 1. Create a folder called mctmp that will store all of the files that we're
	 *    going to be adding.
	 * 2. Iterate through items in instMods and do the following:
	 *      If the item is a directory, recurse into it
	 *      
	 *      If the item is a zip file, extract it to a temp folder and set the
	 *      creation date / time for the extracted files to that of the zip file 
	 *      they came from. Once they are extracted, perform this step again for
	 *      the files that were extracted.
	 *      
	 *      If the item is a file and is not a zip file, do the following:
	 *          If mctmp doesn't already have an item with the same name, simply
	 *          copy the item to mctmp.
	 *      
	 *      If mctmp does have an item with the same name, keep whichever one is
	 *      newer.
	 *      
	 * 3. If there isn't already a file called mcbackup.jar, copy minecraft.jar to it.
	 * 4. Delete minecraft.jar
	 * 5. Extract mcbackup.jar to a temporary directory called mcjar.
	 * 6. Copy everything from mctmp to mcjar and delete mctmp.
	 * 7. Zip mcjar back into minecraft.jar and delete mcjar.
	 */
	@Override
	public void TaskStart()
	{
		OnTaskStart();
		try
		{
		
			buildLog.info("Creating mctmp...");
			File mctmp = new File(instance.getRootDir(), "mctmp");
			if (mctmp.exists())
				FileUtils.recursiveDelete(mctmp);
			mctmp.mkdir();
			
			buildLog.info("Adding files to mctmp...");
			int pcount = 0;
			for (File f : instance.getInstMods().listFiles())
			{
				recursiveAdd(f, mctmp);
				pcount++;
				setProgress(100 / pcount);
			}
			
			// Backup minecraft.jar and delete it
			File jarFile = new File(instance.getBinDir(), "minecraft.jar");
			File jarBackup = new File(instance.getBinDir(), jarBackupName);
			
			if (!jarBackup.exists())
			{
				buildLog.info("Backing up minecraft.jar");
				FileUtils.copyFile(jarFile, jarBackup, false);
			}
			jarFile.delete();
			
			// Extract the jar to mcjar and copy everything from mctmp to it.
			buildLog.info("Extracting jar file");
			File mcjar = new File(instance.getRootDir(), "mcjar");
			if (mcjar.exists())
				mcjar.delete();
			mcjar.mkdir();
			
			ZipFile jarZip = new ZipFile(jarBackup);
			ZipUtils.ExtractAllFromZip(jarZip, mcjar);
			
			File metaInf = new File(mcjar, "META-INF");
			if (metaInf.exists())
				FileUtils.recursiveDelete(metaInf);
			
			// Copy mctmp to mcjar
			buildLog.info("Adding mod files");
			for (File f : mctmp.listFiles())
			{
				FileUtils.recursiveCopy(f, mcjar, true);
			}
			FileUtils.recursiveDelete(mctmp);
			
			// Re-zip mcjar to minecraft.jar and delete it.
			buildLog.info("Re-zipping jar");
			
			ZipUtils.recursiveAddToZip(mcjar.listFiles(), jarFile, "");
			
			FileUtils.recursiveDelete(mcjar);
			
			buildLog.info("Build complete");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			buildLog.fine("Error adding file to mctmp.");
			OnErrorMessage("Error building jar: " + e.toString());
		}
		OnTaskEnd();
	}
	
	private void recursiveAdd(File file, File dest) throws IOException
	{
		if (file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				buildLog.info("Recursing into directory " + f.toString());
				recursiveAdd(f, new File(dest, f.getName()));
			}
		}
		else if (file.isFile())
		{
			// Zip files
			if (file.getName().lastIndexOf('.') > 0 &&
					file.getName().substring(file.getName().lastIndexOf('.')).equals(".zip"))
			{
				buildLog.info("Adding zip file " + file);
				
				File zipTmp = new File(instance.getRootDir(), file.getName().
						substring(0, file.getName().lastIndexOf('.')));
				if (zipTmp.exists())
					FileUtils.recursiveDelete(zipTmp);
				zipTmp.mkdir();
				
				try
				{
					ZipUtils.ExtractAllFromZip(new ZipFile(file), zipTmp);
				} catch (ZipException e)
				{
					e.printStackTrace();
					buildLog.warning("Failed to open zip file: " + file +
							"\n" + e.getMessage());
				}
				
				instance.recursiveSetInstallTime(zipTmp, instance.getInstallTime(file));
				
				for (File f : zipTmp.listFiles())
				{
					recursiveAdd(f, dest);
				}
				zipTmp.delete();
			}
			// Other files
			else
			{
				buildLog.info("Adding file " + file);
				
				File tcopy = new File(dest, file.getName());
				if (!tcopy.exists() || 
						instance.getInstallTime(tcopy) < instance.getInstallTime(file))
				{
					if (!tcopy.getParentFile().exists())
						tcopy.getParentFile().mkdirs();
					FileUtils.copyFile(file, tcopy, true);
				}
			}
		}
	}
}
