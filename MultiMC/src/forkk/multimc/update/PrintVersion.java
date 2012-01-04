package forkk.multimc.update;

import forkk.multimc.data.Version;

public class PrintVersion
{
	/**
	 * Prints the latest version's version number to stdout and quits.
	 * This is used on the server side with a shell script that runs this class
	 * through CGI. This allows the version number to update automatically.
	 */
	public static void main(String[] args)
	{
		System.out.println(Version.currentVersion.toString());
	}
}
