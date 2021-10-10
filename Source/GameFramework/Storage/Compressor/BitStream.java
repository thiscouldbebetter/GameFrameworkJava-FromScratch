
package GameFramework.Storage.Compressor;

public class BitStream
{
	public static int BitsPerByte = 8;
	public static double NaturalLogarithmOf2 = Math.log(2);

	ByteStream byteStream;
	int byteOffset;
	int bitOffsetWithinByteCurrent;
	int byteCurrent;

	public BitStream(ByteStream byteStream)
	{
		if (byteStream == null)
		{
			byteStream = new ByteStreamFromBytes([]);
		}

		this.byteStream = byteStream;
		this.byteOffset = 0;
		this.bitOffsetWithinByteCurrent = 0;
		this.byteCurrent = 0;
	}

	// static methods

	public static String convertIntegerToBitString(int intToConvert)
	{
		var returnValue = "";
		var numberOfBitsNeeded = Math.ceil
		(
			Math.log(intToConvert + 1)
			/ BitStream.NaturalLogarithmOf2
		);

		if (numberOfBitsNeeded == 0)
		{
			numberOfBitsNeeded = 1;
		}

		for (var b = 0; b < numberOfBitsNeeded; b++)
		{
			var bitValue = (intToConvert >> b) & 1;
			returnValue = "" + bitValue + returnValue;
		}

		return returnValue;
	}

	// instance methods

	public void close()
	{
		if (this.bitOffsetWithinByteCurrent > 0)
		{
			this.byteStream.writeByte(this.byteCurrent);
		}
	}

	public int readBit()
	{
		this.byteCurrent = this.byteStream.peekByteCurrent();
		var returnValue = (this.byteCurrent >> this.bitOffsetWithinByteCurrent) & 1;
		this.bitOffsetWithinByteCurrent++;

		if (this.bitOffsetWithinByteCurrent >= BitStream.BitsPerByte)
		{
			this.byteOffset++;
			this.bitOffsetWithinByteCurrent = 0;
			if (this.byteStream.hasMoreBytes())
			{
				this.byteCurrent = this.byteStream.readByte();
			}
		}
		return returnValue;
	}

	public void readInteger(int numberOfBitsInNumber)
	{
		var returnValue = 0;

		for (var i = 0; i < numberOfBitsInNumber; i++)
		{
			var bitRead = this.readBit();
			returnValue |= (bitRead << i);
		}

		return returnValue;
	}

	public void writeBit(int bitToWrite)
	{
		this.byteCurrent |= (bitToWrite << this.bitOffsetWithinByteCurrent);
		this.bitOffsetWithinByteCurrent++;

		if (this.bitOffsetWithinByteCurrent >= BitStream.BitsPerByte)
		{
			this.byteStream.writeByte(this.byteCurrent);
			this.byteOffset++;
			this.bitOffsetWithinByteCurrent = 0;
			this.byteCurrent = 0;
		}
	}

	public void writeInteger(int intToWrite, int numberOfBitsToUse)
	{
		for (var b = 0; b < numberOfBitsToUse; b++)
		{
			var bitValue = (intToWrite >> b) & 1;
			this.writeBit(bitValue);
		}
	}
}
