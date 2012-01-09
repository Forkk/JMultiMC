package forkk.multimc.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends Task
{
	String description;
	
	/**
	 * Creates a downloader to download from the given URL
	 * @param downloadUrl the URL to download from
	 * @param dlTarget the file to download to
	 */
	public Downloader(URL downloadUrl, File dlTarget)
	{
		this.downloadUrl = downloadUrl;
		this.dlTarget = dlTarget;
	}
	
	/**
	 * Creates a downloader to download from the given URL
	 * @param downloadUrl the URL to download from
	 * @param dlTarget the file to download to
	 * @param desc the status message displayed
	 */
	public Downloader(URL downloadUrl, File dlTarget, String desc)
	{
		this.downloadUrl = downloadUrl;
		this.dlTarget = dlTarget;
		this.description = desc;
	}
	
	/**
	 * @return the URL the downloader is downloading from
	 */
	public URL getDownloadURL()
	{
		return downloadUrl;
	}
	private URL downloadUrl;
	
	/**
	 * @return the file the downloader is downloading to
	 */
	public File getDownloadTarget()
	{
		return this.dlTarget;
	}
	private File dlTarget;
	
	@Override
	public void TaskStart()
	{
		OnTaskStart();
		setStatus((description != null? description : "Downloading file..."));
		try
		{
			File outFile = getDownloadTarget();
			if (outFile.exists())
				outFile.delete();
			
			URLConnection dlConnection = getDownloadURL().openConnection();
			
			InputStream is = dlConnection.getInputStream();
			FileOutputStream fos = new FileOutputStream(outFile);
			
			int bytesRead = 0;
			int count = 0;
			byte[] buffer = new byte[512];
			while ((count = is.read(buffer, 0, buffer.length)) != -1)
			{
				bytesRead += count;
				setProgress((is.available() > 0? bytesRead / is.available() : 100));
				fos.write(buffer, 0, count);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			OnErrorMessage("Download failed: " + e.toString());
		}
		OnTaskEnd();
	}
	
	@Override
	public boolean isProgressIndeterminate()
	{
		return false;
	}
}
