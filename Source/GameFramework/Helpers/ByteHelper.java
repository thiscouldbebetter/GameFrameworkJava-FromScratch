
package GameFramework.Helpers;

public class ByteHelper
{
	public static int[] StringUTF8ToBytes(String StringToConvert)
	{
		var bytes = new ArrayList<int>();

		for (var i = 0; i < StringToConvert.length; i++)
		{
			var byteRead = StringToConvert.charCodeAt(i);
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
			var byteAsString = byteToConvert.toString(16);
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
			var byteAsChar = String.fromCharCode(byteToConvert);
			returnValue += byteAsChar;
		}

		return returnValue;
	}
}
