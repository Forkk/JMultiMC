package forkk.multimc.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import forkk.multimc.data.Version;
import forkk.multimc.gui.SelectionWindow;

public class UpdateCheck extends BackgroundTask
{
	public static URL getUpdateURL()
	{
		try
		{
			return new URL("http://multimc.tk/MultiMC/version");
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void TaskStart()
	{
		OnTaskStart();
		setStatus("Checking for updates...");
		String newVersionStr = "";
		try
		{
			Scanner vScanner = new Scanner(getUpdateURL().openStream());
			
			while (vScanner.hasNext())
				newVersionStr += vScanner.next();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
			OnErrorMessage("Failed to check for updates:\nCouldn't resolve hostname " + e.getMessage());
		} catch (IOException e)
		{
			e.printStackTrace();
			OnErrorMessage("Failed to check for updates:\n" + e.getMessage());
		}
		Version newVersion = Version.parseString(newVersionStr);
		if (newVersion != null)
		{
			this.updateAvailable = newVersion.compareTo(SelectionWindow.currentVersion) > 0;
		}
		OnTaskEnd();
	}
	
	boolean updateAvailable;
	public boolean isUpdateAvailable()
	{
		return updateAvailable;
	}
}
