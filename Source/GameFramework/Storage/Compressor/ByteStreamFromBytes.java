
package GameFramework.Storage.Compressor;

import java.util.*;
import java.util.stream.*;

public class ByteStreamFromBytes implements ByteStream
{
	public List<Integer> bytes;
	public int byteIndexCurrent;

	public ByteStreamFromBytes()
	{
		this(new ArrayList<Integer>());
	}

	public ByteStreamFromBytes(List<Integer> bytes)
	{
		this.bytes = bytes;
		this.byteIndexCurrent = 0;
	}

	public boolean hasMoreBytes()
	{
		return this.byteIndexCurrent < this.bytes.size();
	}

	public int peekByteCurrent()
	{
		return this.bytes.get(this.byteIndexCurrent);
	}

	public int readByte()
	{
		var byteRead = this.bytes.get(this.byteIndexCurrent);
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
		return (int[])( bytesRead.toArray(new int[] {}) );
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
		this.bytes.set(this.byteIndexCurrent, byteToWrite);
		this.byteIndexCurrent++;
	}

	public void writeBytes(int[] bytesToWrite)
	{
		Arrays.asList(bytesToWrite).stream().forEach(x -> this.writeByte(x));
	}

	public void writeStringPaddedToLength(String stringToWrite, int lengthPadded)
	{
		for (var i = 0; i < stringToWrite.length(); i++)
		{
			var charAsByte = stringToWrite.charAt(i);
			this.writeByte(charAsByte);
		}

		var intOfPaddingChars = lengthPadded - stringToWrite.length();
		for (var i = 0; i < intOfPaddingChars; i++)
		{
			this.writeByte(0);
		}
	}

}
