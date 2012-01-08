package forkk.multimc.update;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import forkk.multimc.compat.OS;
import forkk.multimc.compat.OSUtils;
import forkk.multimc.util.FileUtils;

public class UpdateFile
{
	/**
	 * Replaces the file specified by args[0] with this file and then quits.
	 * This is used for updating, since files that are running cannot be modified.
	 * @param args The first argument must specify the file to update.
	 * The second one is a string of options that do the following:<br>
	 * <ul>
	 * <li>r: Runs the file after it is updated</li>
	 * </ul>
	 */
	public static void main(String[] args)
	{
		File currentPath = null;
		File targetPath = null;
		try
		{
			if (args.length < 1)
			{
				System.out.println("Update file must have at least one argument!");
				JOptionPane.showMessageDialog(null, "Error updating MultiMC: " + 
						"Missing argument" + getCurrentFilePath(), "Update Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			currentPath = new File(getCurrentFilePath());
			targetPath = new File(args[0]);
			long waitStart = System.currentTimeMillis();
			while (true)
			{
				try
				{
					FileUtils.copyFile(currentPath, targetPath, true);
				} catch (IOException e)
				{
					if (e.getMessage().contains("being used"))
					{
						if (System.currentTimeMillis() < waitStart + (3 * 1000))
						{
							try { Thread.sleep(100); } catch (InterruptedException ie) { }
							continue;
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Couldn't " +
									"update because the operation timed out " +
									"while waiting for MultiMC to close.\n" + 
									"Path Replacing: " + targetPath + "\n" + 
									"Current File: " + getCurrentFilePath() + "\n" +
									"Args: " + Arrays.toString(args) + "\n" + 
									"Error Message: " + e.toString(),
									"Update Timed Out", JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
					}
					else
					{
						throw e;
					}
				}
				break;
			}
			JOptionPane.showMessageDialog(null, "MultiMC updated successfully.", 
					"Update Success", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Error occurred, update failed.");
			JOptionPane.showMessageDialog(null, "Error updating MultiMC: " + 
					e.toString(), "Update Failed", JOptionPane.ERROR_MESSAGE);
		} finally
		{
			if (args.length >= 2)
			{
				if (args[1].lastIndexOf('r') > -1 && targetPath != null)
				{
					ProcessBuilder procBuilder = new ProcessBuilder(
							"java", 
							"-jar",
							targetPath.toString());
					try
					{
						System.out.println("Starting " + targetPath.toString());
						procBuilder.start();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * @return A path to the currently executing file
	 */
	public static String getCurrentFilePath()
	{
		File path = new File(UpdateFile.class.getClass().
				getResource("/forkk/multimc/update/UpdateFile.class").getPath());
		return path.toString().substring((OSUtils.getCurrentOS() == OS.WINDOWS? 6 : 5), 
				path.toString().lastIndexOf('!'));
	}
}
