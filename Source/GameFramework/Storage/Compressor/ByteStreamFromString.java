
package GameFramework.Storage.Compressor;

import java.util.*;

public class ByteStreamFromString implements ByteStream
{
	public String bytesAsString;
	public int byteIndexCurrent;

	public ByteStreamFromString(String bytesAsString)
	{
		this.bytesAsString = bytesAsString;
		this.byteIndexCurrent = 0;
	}

	public boolean hasMoreBytes()
	{
		return this.byteIndexCurrent < this.bytesAsString.length();
	}

	public int peekByteCurrent()
	{
		return this.bytesAsString.charAt(this.byteIndexCurrent);
	}

	public int readByte()
	{
		var byteRead = this.bytesAsString.charAt(this.byteIndexCurrent);
		this.byteIndexCurrent++;
		return byteRead;
	}

	public int[] readBytes(int byteCount)
	{
		var bytesRead = new ArrayList<Integer>();
		for (var i = 0; i < byteCount; i++)
		{
			var byteRead = this.readByte();
			bytesRead.add(byteRead);
		}
		return bytesRead;
	}

	public String readStringOfLength(int lengthOfString)
	{
		var returnValue = "";

		for (var i = 0; i < lengthOfString; i++)
		{
			var byteRead = this.readByte();

			if (byteRead != 0)
			{
				var byteAsChar = (char)byteRead;
				returnValue += byteAsChar;
			}
		}

		return returnValue;
	}

	public void writeByte(int byteToWrite)
	{
		// todo - This'll be slow.
		this.bytesAsString += (char)byteToWrite;
		this.byteIndexCurrent++;
	}

	public void writeBytes(int bytesToWrite[])
	{
		bytesToWrite.stream().forEach(x -> this.writeByte(x));
	}

	public void writeStringPaddedToLength(String stringToWrite, int lengthPadded)
	{
		for (var i = 0; i < stringToWrite.length(); i++)
		{
			var charAsByte = (int)(stringToWrite.charAt(i));
			this.writeByte(charAsByte);
		}

		var intOfPaddingChars = lengthPadded - stringToWrite.length();
		for (var i = 0; i < intOfPaddingChars; i++)
		{
			this.writeByte(0);
		}
	}
}
