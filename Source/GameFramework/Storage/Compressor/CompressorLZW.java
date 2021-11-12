
package GameFramework.Storage.Compressor;

import java.util.*;
import java.util.stream.*;

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
		var symbolWidthInBitsCurrent = (int)
		(
			Math.ceil
			(
				Math.log(symbolForBitWidthIncrease + 1)
				/ BitStream.NaturalLogarithmOf2
			)
		);

		while (byteStreamToCompress.hasMoreBytes())
		{
			var byteToCompress = byteStreamToCompress.readByte();
			var character = (char)byteToCompress;
			var patternPlusCharacter = pattern + character;
			if (symbolsByPattern.containsKey(patternPlusCharacter) == false)
			{
				var symbolNext =
					symbolsByPattern.size()
					+ CompressorLZW.ControlSymbolCount;
				symbolsByPattern.put(patternPlusCharacter, symbolNext);
				var patternEncoded = symbolsByPattern.get(pattern);
				var numberOfBitsRequired = (int)
				(
					Math.ceil(
						Math.log(patternEncoded + 1)
						/ BitStream.NaturalLogarithmOf2
					)
				);
				if (numberOfBitsRequired > symbolWidthInBitsCurrent)
				{
					bitStream.writeInteger
					(
						symbolForBitWidthIncrease, symbolWidthInBitsCurrent
					);
					symbolWidthInBitsCurrent = numberOfBitsRequired;
				}
				bitStream.writeInteger(patternEncoded, symbolWidthInBitsCurrent);
				pattern = "" + character;
			}
			else
			{
				pattern = patternPlusCharacter;
			}
		}

		var patternEncoded = symbolsByPattern.get(pattern);
		bitStream.writeInteger(patternEncoded, symbolWidthInBitsCurrent);
		bitStream.writeInteger
		(
			CompressorLZW.SymbolForBitStreamEnd, symbolWidthInBitsCurrent
		);
		bitStream.close();

		return bitStream;
	}

	public int[] compressBytes(int[] bytesToCompress)
	{
		var byteStreamCompressed = new ByteStreamFromBytes();
		var bitStreamCompressed = new BitStream(byteStreamCompressed);
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromBytes(Arrays.asList(bytesToCompress)),
			bitStreamCompressed
		);
		return Arrays.asList(byteStreamCompressed.bytes);
	}

	public String compressString(String stringToCompress)
	{
		var bitStream = new BitStream(new ByteStreamFromString(""));
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromString(stringToCompress), bitStream
		);
		var byteStream = bitStream.byteStream;
		var returnValue = ((ByteStreamFromString)byteStream).bytesAsString;
		return returnValue;
	}

	public Integer[] compressStringToBytes(String stringToCompress)
	{
		var bitStream = new BitStream(new ByteStreamFromBytes());
		this.compressByteStreamToBitStream
		(
			new ByteStreamFromString(stringToCompress), bitStream
		);
		var byteStream = bitStream.byteStream;
		var returnBytesAsList = ((ByteStreamFromBytes)byteStream).bytes;
		var returnBytes = new Integer[returnBytesAsList.size()];
		for (var i = 0; i < returnBytesAsList.size(); i++)
		{
			returnBytes[i] = returnBytesAsList.get(i);
		}
		return returnBytes;
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
			(int)
			(
				Math.ceil
				(
					Math.log(symbolForBitWidthIncrease + 1)
					/ BitStream.NaturalLogarithmOf2
				)
			);
		var symbolToDecode = bitStream.readInteger(symbolWidthInBitsCurrent);
		var symbolDecoded = patternsBySymbol.get(symbolToDecode);

		for (var i = 0; i < symbolDecoded.length(); i++)
		{
			var byteToWrite = symbolDecoded.charAt(i);
			byteStreamDecompressed.writeByte(byteToWrite);
		}

		String pattern;
		String character;
		String patternPlusCharacter;

		while (true)
		{
			pattern = patternsBySymbol.get(symbolToDecode);
			var symbolNext = bitStream.readInteger(symbolWidthInBitsCurrent);
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
				symbolDecoded = patternsBySymbol.get(symbolToDecode);
				if (symbolDecoded == null)
				{
					character = "" + pattern.charAt(0);
					patternPlusCharacter = pattern + character;
					for (var i = 0; i < patternPlusCharacter.length(); i++)
					{
						var byteToWrite = patternPlusCharacter.charAt(i);
						byteStreamDecompressed.writeByte(byteToWrite);
					}
				}
				else
				{
					for (var i = 0; i < symbolDecoded.length(); i++)
					{
						var byteToWrite = symbolDecoded.charAt(i);
						byteStreamDecompressed.writeByte(byteToWrite);
					}
					character = "" + symbolDecoded.charAt(0);
					patternPlusCharacter = pattern + character;
				}

				symbolNext =
					symbolsByPattern.size() + CompressorLZW.ControlSymbolCount;
				symbolsByPattern.put(patternPlusCharacter, symbolNext);
				patternsBySymbol.put(symbolNext, patternPlusCharacter);
			}
		}

		return byteStreamDecompressed;
	}

	public int[] decompressBytes(int[] bytesToDecode)
	{
		var bytesToDecodeAsList = new ArrayList<Integer>();
		for (var i = 0; i < bytesToDecode.length; i++)
		{
			bytesToDecodeAsList.add(bytesToDecode[i]);
		}

		var byteStreamToDecode =
			new ByteStreamFromBytes(bytesToDecodeAsList);
		var byteStreamDecompressed = new ByteStreamFromBytes();
		this.decompressByteStream
		(
			byteStreamToDecode, byteStreamDecompressed
		);
		var bytesDecompressed = byteStreamDecompressed.bytes;

		var returnBytes = new int[bytesDecompressed.size()];
		for (var i = 0; i < bytesDecompressed.size(); i++)
		{
			returnBytes[i] = bytesDecompressed.get(i);
		}
		return returnBytes;
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
		var patternsBySymbol = new HashMap<Integer,String>();
		var firstControlSymbol = CompressorLZW.SymbolForBitWidthIncrease;

		for (var i = 0; i < firstControlSymbol; i++)
		{
			var charCode = "" + (char)i;
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
			var charCode = "" + (char)i;
			symbolsByPattern.put(charCode, i);
		}
		return symbolsByPattern;
	}
}
