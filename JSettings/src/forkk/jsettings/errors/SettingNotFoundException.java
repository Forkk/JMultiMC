package forkk.jsettings.errors;

/**
 * Thrown when the program tries to access a setting that doesn't exist
 * @author Andrew Okin
 */
public class SettingNotFoundException extends SettingsError
{
	private static final long serialVersionUID = 4390769598018387891L;

	public SettingNotFoundException(String message)
	{
		super(message);
	}
}
