package forkk.jsettings.errors;

/**
 * A fatal error pertaining to the settings
 * @author Andrew Okin
 */
public class SettingsError extends Error
{
	private static final long serialVersionUID = -1571443585365715723L;
	
	public SettingsError(String message)
	{
		super(message);
	}
}
