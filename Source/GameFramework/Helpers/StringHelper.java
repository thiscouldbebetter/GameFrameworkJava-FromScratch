
package GameFramework.Helpers;

public class StringHelper
{
	// Static class.

	public static String lowercaseFirstCharacter(String value)
	{
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	public static String padEnd
	(
		String stringToPad, int lengthToPadTo, String charToPadWith
	)
	{
		while (stringToPad.length() < lengthToPadTo)
		{
			stringToPad = stringToPad + charToPadWith;
		}
		return stringToPad;
	}

	public static String padStart
	(
		String stringToPad, int lengthToPadTo, String charToPadWith
	)
	{
		while (stringToPad.length() < lengthToPadTo)
		{
			stringToPad = charToPadWith + stringToPad;
		}
		return stringToPad;
	}

	public static String replaceAll
	(
		String stringToReplaceWithin,
		String stringToBeReplaced,
		String stringToReplaceWith
	)
	{
		return stringToReplaceWithin.split(stringToBeReplaced).join(stringToReplaceWith);
	}

	public static String toTitleCase(String value)
	{
		return
			value.substring(0, 1).toUpperCase()
			+ value.substring(1).toLowerCase();
	}
}
