package forkk.multimc.settings;

/**
 * Thrown when the program tries to get a setting whose type is different than
 * the specified type
 * @author Andrew Okin
 */
public class SettingTypeError extends Error
{
	private static final long serialVersionUID = -5573828041136575231L;

	public SettingTypeError(String msg)
	{
		super(msg);
	}
}
