package forkk.multimc.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import forkk.multimc.data.Version;

public class UpdateCheck extends Task
{
	/**
	 * The standard version file ID. This gets the latest version
	 */
	public static final int VF_STANDARD = 0;
	
	/**
	 * The debug version file ID. This always returns 999.999.999 and is used 
	 * for testing the update system
	 */
	public static final int VF_DEBUG = 1;
	
	/**
	 * @param versionFileID the ID of the version file to get.
	 * @return the URL to download the latest version info from
	 */
	public static URL getUpdateURL(int versionFileID)
	{
		try
		{
			switch (versionFileID)
			{
			case UpdateCheck.VF_DEBUG:
				return new URL("http://multimc.tk/MultiMC/version-debug.sh");
			
			default:
				return new URL("http://multimc.tk/MultiMC/version.sh");
			}
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return the URL to download the latest update from
	 */
	public static URL getLatestVersionURL()
	{
		try
		{
			return new URL("http://multimc.tk/MultiMC/MultiMC.jar");
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private int versionFileID;
	
	public UpdateCheck(int versionFileID)
	{
		this.versionFileID = versionFileID;
	}
	
	@Override
	public void TaskStart()
	{
		OnTaskStart();
		setStatus("Checking for updates...");
		String newVersionStr = "";
		try
		{
			Scanner vScanner = new Scanner(getUpdateURL(versionFileID).openStream());
			
			while (vScanner.hasNext())
				newVersionStr += vScanner.next();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
			OnErrorMessage("Failed to check for updates:\n" + e.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
			OnErrorMessage("Failed to check for updates:\n" + e.toString());
		}
		
		latestVersion = Version.parseString(newVersionStr);
		OnTaskEnd();
	}
	
	Version latestVersion;
	public Version getLatestVersion()
	{
		return latestVersion;
	}
	
	public boolean isUpdateAvailable()
	{
		if (latestVersion != null)
		{
			return latestVersion.compareTo(Version.currentVersion) < 0;
		}
		else return false;
	}
}
