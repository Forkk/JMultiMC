package forkk.multimc.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppSettings
{
	private static Properties settings = initSettings();
	private static final String settingsFileName = "multimc.cfg";
	
	private static Properties initSettings()
	{
		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(new File(settingsFileName)));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return props;
	}
	
//	private static Properties getDefaultSettings()
//	{
//		Properties defaults = new Properties();
//		defaults.put("LauncherFile", "launcher.jar");
//		defaults.put("InitialMemAlloc", ((Integer) 512).toString());
//		defaults.put("InitialMemAlloc", ((Integer) 1024).toString());
//		
//		defaults.put("ShowConsole", ((Boolean) false).toString());
//		defaults.put("AutoCloseConsole", ((Boolean) false).toString());
//		
//		defaults.put("AutoCheckUpdates", ((Boolean) true).toString());
//		return defaults;
//	}
	
	private static final boolean shouldAutosave = true;
	
	private static long lastAutosave = 0;
	
	private static void autosave()
	{
		// Wait 2 seconds between auto-saves
		if (shouldAutosave && System.currentTimeMillis() - lastAutosave > 2000)
			saveSettings();
	}
	
	public static void saveSettings()
	{
		try
		{
			if (!new File(settingsFileName).exists())
				new File(settingsFileName).createNewFile();
			
			settings.store(new FileOutputStream(settingsFileName),
					"Unless you know what you're doing, it's probably not a good " +
					"idea to edit this file. If you do edit the file and you mess" +
					"it up, MultiMC will crash. Keep that in mind.\n" +
					"There's really no reason to edit this anyways, since all of " +
					"the options here can be found in the settings menu.");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Settings
	
	//////////////////////////////////////////// INSTANCE LAUNCH SETTINGS
	//					Launcher Filename
	public static String getLauncherFilename()
		{ return settings.getProperty("LauncherFile", "launcher.jar"); }
	public static void setLauncherFilename(String value)
		{ settings.setProperty("LauncherFile", value); autosave(); }
	
	//					Initial memory allocation
	public static Integer getInitialMemAlloc()
		{ return Integer.parseInt(settings.getProperty("InitialMemAlloc", "512")); }
	public static void setInitialMemAlloc(Integer value)
		{ settings.setProperty("InitialMemAlloc", value.toString()); autosave(); }
	
	//					Maximum memory allocation
	public static Integer getMaxMemAlloc()
		{ return Integer.parseInt(settings.getProperty("MaxMemAlloc", "1024")); }
	public static void setMaxMemAlloc(Integer value)
		{ settings.setProperty("MaxMemAlloc", value.toString()); autosave(); }
	
	
	///////////////////////////////////// CONSOLE SETTINGS
	//					Show console
	public static boolean getShowConsole()
		{ return Boolean.parseBoolean(settings.getProperty("ShowConsole")); }
	public static void setShowConsole(Boolean value)
		{ settings.setProperty("ShowConsole", value.toString()); autosave(); }
	
	//					Auto close console
	public static boolean getAutoCloseConsole()
	{ return Boolean.parseBoolean(settings.getProperty("AutoCloseConsole")); }
	public static void setAutoCloseConsole(Boolean value)
	{ settings.setProperty("AutoCloseConsole", value.toString()); autosave(); }
	
	//////////////////////////////////////////// OTHER
	//					Check updates on start
	public static boolean getAutoUpdate()
	{ return Boolean.parseBoolean(settings.getProperty("AutoCheckUpdates")); }
	public static void setCheckUpdates(Boolean value)
	{ settings.setProperty("AutoCheckUpdates", value.toString()); autosave(); }
}
