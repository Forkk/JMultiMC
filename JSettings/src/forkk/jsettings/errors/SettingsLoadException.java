package forkk.jsettings.errors;

/**
 * Exception thrown due to an issue when the settings are loading
 * @author Andrew Okin
 */
public class SettingsLoadException extends SettingsException
{
	public SettingsLoadException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = -5979825861705252460L;
}
