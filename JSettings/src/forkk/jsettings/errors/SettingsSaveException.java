package forkk.jsettings.errors;

/**
 * Exception thrown due to an issue when the settings are saving
 * @author Andrew Okin
 */
public class SettingsSaveException extends SettingsException
{
	public SettingsSaveException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = -8840064775459016511L;
}
