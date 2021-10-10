
package GameFramework.Storage.Compressor;

import java.util.*;

public class CompressorLZW
{
	private static int ControlSymbolCount = 2;
	private static int SymbolForBitWidthIncrease = 256;
	private static int SymbolForBitStreamEnd =
		CompressorLZW.SymbolForBitWidthIncrease + 1;

	// instance methods

	public BitStream compressByteStreamToBitStream
	(
		ByteStream byteStreamToCompress, BitStream bitStream
	)
	{
		// Adapted from pseudocode found at the URL:
		// http://oldwww.rasip.fer.hr/research/compress/algorithms/fund/lz/lzw.html

		var symbolsByPattern = this.initializeSymbolsByPattern();
		var pattern = "";
		var symbolForBitWidthIncrease =
			CompressorLZW.SymbolForBitWidthIncrease;
		var symbolWidthInBitsCurrent = Math.ceil
		(
			Math.log(symbolForBitWidthIncrease + 1)
			/ BitStream.NaturalLogarithmOf2
		);

		while (byteStreamToCompress.hasMoreBytes())
		{
			var byteToCompress = byteStreamToCompress.readByte();
			var character = String.fromCharCode(byteToCompress);
			var patternPlusCharacter = pattern + character;
			if (symbolsByPattern.has(patternPlusCharacter) == false)
			{
				var symbolNext =
					symbolsByPattern.size + CompressorLZW.ControlSymbolCount;
				symbolsByPattern.set(patternPlusCharacter, symbolNext);
				var patternEncoded = symbolsByPattern.get(pattern);
				var numberOfBitsRequired = Math.ceil(Math.log(patternEncoded + 1)
					/ BitStream.NaturalLogarithmOf2);
				if (numberOfBitsRequired > symbolWidthInBitsCurrent)
				{
					bitStream.writeNumber
					(
						symbolForBitWidthIncrease, symbolWidthInBitsCurrent
					);
					symbolWidthInBitsCurrent = numberOfBitsRequired;
				}
				bitStream.writeNumber(patternEncoded, symbolWidthInBitsCurrent);
				pattern = character;
			}
			else
			{
				pattern = patternPlusCharacter;
			}
		}

		var patternEncoded = symbolsByPattern.get(pattern);
		bitStream.writeNumber(patternEncoded, symbolWidthInBitsCurrent);
		bitStream.writeNumber
		(
			CompressorLZW.SymbolForBitStreamEnd, symbolWidthInBitsCurrent
		);
		bitStream.close();

		return bitStream;
	}

	public int[] compressBytes(int[] bytesToCompress)
	{
		var byteStreamCompressed = new ByteStreamFromBytes([]);
		var bitStreamCompressed = new BitStream(byteStreamCompressed);
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromBytes(bytesToCompress), bitStreamCompressed
		);
		return byteStreamCompressed.bytes;
	}

	public String compressString(String stringToCompress)
	{
		var bitStream = new BitStream(new ByteStreamFromString(""));
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromString(stringToCompress), bitStream
		);
		var byteStream = bitStream.byteStream;
		var returnValue = (byteStream as ByteStreamFromString).bytesAsString;
		return returnValue;
	}

	public int[] compressStringToBytes(String stringToCompress)
	{
		var bitStream = new BitStream(new ByteStreamFromBytes([]));
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromString(stringToCompress), bitStream
		);
		var byteStream = bitStream.byteStream;
		var returnValues = (byteStream as ByteStreamFromBytes).bytes;
		return returnValues;
	}

	public ByteStream decompressByteStream
	(
		ByteStream byteStreamToDecode, ByteStream byteStreamDecompressed
	)
	{
		// Adapted from pseudocode found at the URL:
		// http://oldwww.rasip.fer.hr/research/compress/algorithms/fund/lz/lzw.html

		var patternsBySymbol = this.initializePatternsBySymbol();
		var symbolsByPattern = this.initializeSymbolsByPattern();
		var bitStream = new BitStream(byteStreamToDecode);
		var symbolForBitStreamEnd = CompressorLZW.SymbolForBitStreamEnd;
		var symbolForBitWidthIncrease = CompressorLZW.SymbolForBitWidthIncrease;
		var symbolWidthInBitsCurrent =
			Math.ceil(Math.log(symbolForBitWidthIncrease + 1)
			/ BitStream.NaturalLogarithmOf2);
		var symbolToDecode = bitStream.readNumber(symbolWidthInBitsCurrent);
		var symbolDecoded = patternsBySymbol[symbolToDecode];

		for (var i = 0; i < symbolDecoded.length; i++)
		{
			var byteToWrite = symbolDecoded.charCodeAt(i);
			byteStreamDecompressed.writeByte(byteToWrite);
		}

		var pattern;
		var character;
		var patternPlusCharacter;

		while (true)
		{
			pattern = patternsBySymbol[symbolToDecode];
			var symbolNext = bitStream.readNumber(symbolWidthInBitsCurrent);
			if (symbolNext == symbolForBitWidthIncrease)
			{
				symbolWidthInBitsCurrent++;
			}
			else if (symbolNext == symbolForBitStreamEnd)
			{
				break;
			}
			else
			{
				symbolToDecode = symbolNext;
				symbolDecoded = patternsBySymbol[symbolToDecode];
				if (symbolDecoded == null)
				{
					character = pattern[0];
					patternPlusCharacter = pattern + character;
					for (var i = 0; i < patternPlusCharacter.length; i++)
					{
						var byteToWrite = patternPlusCharacter.charCodeAt(i);
						byteStreamDecompressed.writeByte(byteToWrite);
					}
				}
				else
				{
					for (var i = 0; i < symbolDecoded.length; i++)
					{
						var byteToWrite = symbolDecoded.charCodeAt(i);
						byteStreamDecompressed.writeByte(byteToWrite);
					}
					character = symbolDecoded[0];
					patternPlusCharacter = pattern + character;
				}

				var symbolNext =
					symbolsByPattern.size + CompressorLZW.ControlSymbolCount;
				symbolsByPattern.set(patternPlusCharacter, symbolNext);
				patternsBySymbol[symbolNext] = patternPlusCharacter;
			}
		}

		return byteStreamDecompressed;
	}

	public int[] decompressBytes(number bytesToDecode[])
	{
		var byteStreamToDecode = new ByteStreamFromBytes(bytesToDecode);
		var byteStreamDecompressed = new ByteStreamFromBytes([]);
		this.decompressByteStream
		(
			byteStreamToDecode, byteStreamDecompressed
		);
		var bytesDecompressed = byteStreamDecompressed.bytes;

		return bytesDecompressed;
	}

	public String decompressString(String stringToDecode)
	{
		var byteStreamToDecode = new ByteStreamFromString(stringToDecode);
		var byteStreamDecompressed = new ByteStreamFromString("");
		this.decompressByteStream
		(
			byteStreamToDecode, byteStreamDecompressed
		);
		var stringDecompressed = byteStreamDecompressed.bytesAsString;

		return stringDecompressed;
	}

	public Map<Integer,String> initializePatternsBySymbol()
	{
		var patternsBySymbol = new Map<Integer,String>;
		var firstControlSymbol = CompressorLZW.SymbolForBitWidthIncrease;

		for (var i = 0; i < firstControlSymbol; i++)
		{
			var charCode = String.fromCharCode(i);
			patternsBySymbol.put(i, charCode);
		}

		patternsBySymbol.put(CompressorLZW.SymbolForBitWidthIncrease, "[WIDEN]");
		patternsBySymbol.put(CompressorLZW.SymbolForBitStreamEnd, "[END]");

		return patternsBySymbol;
	}

	public Map<String,Integer> initializeSymbolsByPattern()
	{
		var symbolsByPattern = new HashMap<String,Integer>();
		var firstControlSymbol = CompressorLZW.SymbolForBitWidthIncrease;
		for (var i = 0; i < firstControlSymbol; i++)
		{
			var charCode = String.fromCharCode(i);
			symbolsByPattern.put(charCode, i);
		}
		return symbolsByPattern;
	}
}

}
