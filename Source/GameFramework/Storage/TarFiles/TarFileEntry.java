
package GameFramework.Storage.TarFiles;

import java.util.*;

import GameFramework.Helpers.*;
import GameFramework.Storage.*;
import GameFramework.Storage.Compressor.*;

public class TarFileEntry //
{
	public TarFileEntryHeader header;
	public List<Integer> dataAsBytes;

	public TarFileEntry(TarFileEntryHeader header, int[] dataAsBytes)
	{
		this.header = header;
		this.dataAsBytes = Arrays.asList(dataAsBytes);
	}

	// methods

	// static methods

	public static TarFileEntry directoryNew(String directoryName)
	{
		var header = TarFileEntryHeader.directoryNew(directoryName);

		var entry = new TarFileEntry(header, new ArrayList<Integer>());

		return entry;
	}

	public static TarFileEntry fileNew(String fileName, int fileContentsAsBytes[])
	{
		var header = TarFileEntryHeader.fileNew(fileName, fileContentsAsBytes);

		var entry = new TarFileEntry(header, fileContentsAsBytes);

		return entry;
	}

	public static TarFileEntry fromBytes(int[] chunkAsBytes, ByteStream reader)
	{
		var chunkSize = TarFile.ChunkSize;

		var header = TarFileEntryHeader.fromBytes
		(
			chunkAsBytes
		);

		var sizeOfDataEntryInBytesUnpadded = header.fileSizeInBytes;

		var intOfChunksOccupiedByDataEntry = Math.ceil
		(
			sizeOfDataEntryInBytesUnpadded / chunkSize
		);

		var sizeOfDataEntryInBytesPadded =
			intOfChunksOccupiedByDataEntry
			* chunkSize;

		var dataAsBytesToTrim =
			reader.readBytes(sizeOfDataEntryInBytesPadded);

		var dataAsBytes = new int[sizeOfDataEntryInBytesUnpadded];
		for (var i = 0; i < dataAsBytes.length; i++)
		{
			dataAsBytes[i] = dataAsBytesToTrim[i];
		}

		var entry = new TarFileEntry(header, dataAsBytes);

		return entry;
	}

	public static List<TarFileEntry> manyFromByteArrays
	(
		String fileNamePrefix, String fileNameSuffix,
		int[][] entriesAsByteArrays
	)
	{
		var returnValues = new ArrayList<TarFileEntry>();

		for (var i = 0; i < entriesAsByteArrays.length; i++)
		{
			var entryAsBytes = entriesAsByteArrays[i];
			var entry = TarFileEntry.fileNew
			(
				fileNamePrefix + i + fileNameSuffix,
				entryAsBytes
			);

			returnValues.add(entry);
		}

		return returnValues;
	}

	// instance methods

	public void download(Object event)
	{
		new FileHelper().saveBytesToFileWithName
		(
			this.dataAsBytes,
			this.header.fileName
		);
	}

	public void remove(Object event)
	{
		throw new Exception("Not yet implemented!");
	}

	public List<Integer> toBytes()
	{
		var entryAsBytes = new ArrayList<Integer>();

		var chunkSize = TarFile.ChunkSize;

		var headerAsBytes = this.header.toBytes();
		entryAsBytes.addAll(headerAsBytes);

		entryAsBytes.addAll(this.dataAsBytes);

		var sizeOfDataEntryInBytesUnpadded = this.header.fileSizeInBytes;

		var intOfChunksOccupiedByDataEntry = Math.ceil
		(
			sizeOfDataEntryInBytesUnpadded / chunkSize
		);

		var sizeOfDataEntryInBytesPadded =
			intOfChunksOccupiedByDataEntry
			* chunkSize;

		var intOfBytesOfPadding =
			sizeOfDataEntryInBytesPadded - sizeOfDataEntryInBytesUnpadded;

		for (var i = 0; i < intOfBytesOfPadding; i++)
		{
			entryAsBytes.add(0);
		}

		return entryAsBytes;
	}

	// strings

	public String toString()
	{
		var newline = "\n";

		var headerAsString = this.header.toString();

		var dataAsHexadecimalString = ByteHelper.bytesToStringHexadecimal
		(
			this.dataAsBytes
		);

		var returnValue =
			"[TarFileEntry]" + newline
			+ headerAsString
			+ "[Data]"
			+ dataAsHexadecimalString
			+ "[/Data]" + newline
			+ "[/TarFileEntry]"
			+ newline;

		return returnValue;
	}

}
