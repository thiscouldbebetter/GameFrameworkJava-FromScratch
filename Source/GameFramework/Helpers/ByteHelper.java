
package GameFramework.Helpers;

import java.util.*;

import GameFramework.Helpers.*;

public class ByteHelper
{
	public static int[] StringUTF8ToBytes(String stringToConvert)
	{
		var bytes = new ArrayList<Integer>();

		for (var i = 0; i < stringToConvert.length(); i++)
		{
			var byteRead = stringToConvert.charAt(i);
			bytes.push(byteRead);
		}

		return bytes.toArray(new int[] {});
	}

	public static String bytesToStringHexadecimal(int[] bytesToConvert)
	{
		var returnValue = "";

		for (var i = 0; i < bytesToConvert.length; i++)
		{
			var byteToConvert = bytesToConvert[i];
			var byteAsString = Integer.toHexadecimalString(byteToConvert);
			returnValue += byteAsString;
		}

		return returnValue;
	}

	public static String bytesToStringUTF8(int[] bytesToConvert)
	{
		var returnValue = "";

		for (var i = 0; i < bytesToConvert.length; i++)
		{
			var byteToConvert = bytesToConvert[i];
			var byteAsChar = "" + (char)byteToConvert;
			returnValue += byteAsChar;
		}

		return returnValue;
	}
}
