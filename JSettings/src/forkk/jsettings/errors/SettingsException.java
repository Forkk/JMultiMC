package forkk.jsettings.errors;

public class SettingsException extends Exception
{
	private static final long serialVersionUID = 979667800897022514L;
	
	/**
	 * Creates a new instance of this class
	 * @param message Message describing the exception
	 */
	public SettingsException(String message)
	{
		super(message);
	}
}
