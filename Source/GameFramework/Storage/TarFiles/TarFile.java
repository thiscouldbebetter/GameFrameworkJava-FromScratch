
package GameFramework.Storage.TarFiles;

import java.util.*;
import java.util.stream.*;

import GameFramework.Storage.*;
import GameFramework.Storage.Compressor.*;

public class TarFile //
{
	public String fileName;
	public List<TarFileEntry> entries;

	public TarFile(String fileName, List<TarFileEntry> entries)
	{
		this.fileName = fileName;
		this.entries = entries;
	}

	// constants

	public static int ChunkSize = 512;

	// static methods

	public static TarFile fromBytes(String fileName, int[] bytes)
	{
		var reader = new ByteStreamFromBytes(Arrays.asList(bytes));

		var entries = new ArrayList<TarFileEntry>();

		var chunkSize = TarFile.ChunkSize;

		var intOfConsecutiveZeroChunks = 0;

		while (reader.hasMoreBytes() == true)
		{
			var chunkAsBytes = reader.readBytes(chunkSize);

			var areAllBytesInChunkZeroes = true;

			for (var b = 0; b < chunkAsBytes.length; b++)
			{
				if (chunkAsBytes[b] != 0)
				{
					areAllBytesInChunkZeroes = false;
					break;
				}
			}

			if (areAllBytesInChunkZeroes == true)
			{
				intOfConsecutiveZeroChunks++;

				if (intOfConsecutiveZeroChunks == 2)
				{
					break;
				}
			}
			else
			{
				intOfConsecutiveZeroChunks = 0;

				var entry = TarFileEntry.fromBytes(chunkAsBytes, reader);

				entries.push(entry);
			}
		}

		var returnValue = new TarFile(fileName, entries);

		returnValue.consolidateLongPathEntries();

		return returnValue;
	}

	public static TarFile create(String fileName)
	{
		return new TarFile
		(
			fileName, new ArrayList<TarFileEntry>()
		);
	}

	// instance methods

	public void consolidateLongPathEntries()
	{
		// TAR file entries with paths longer than 99 chars require cheating,
		// by prepending them with a entry of type "L" whose data contains the path.
		var typeFlagLongPathName = TarFileTypeFlag.Instances().LongFilePath.name;
		var entries = this.entries;
		for (var i = 0; i < entries.size(); i++)
		{
			var entry = entries.get(i);
			if (entry.header.typeFlag.name == typeFlagLongPathName)
			{
				var entryNext = entries.get(i + 1);
				entryNext.header.fileName = entry.dataAsBytes.stream().reduce
				(
					(a, b) -> a += "" + (char)b,
					""
				);
				entries.remove(i);
				i--;
			}
		}
	}

	public void downloadAs(String fileNameToSaveAs)
	{
		new FileHelper().saveBytesToFileWithName
		(
			this.toBytes(), fileNameToSaveAs
		);
	}

	public List<TarFileEntry> entriesForDirectories()
	{
		var typeFlagDirectoryName =
			TarFileTypeFlag.Instances().Directory.name;

		return this.entries.stream().filter
		(
			x ->
				x.header.typeFlag.name == typeFlagDirectoryName
		).collect(Collectors.toList());
	}

	public int[] toBytes()
	{
		this.toBytes_PrependLongPathEntriesAsNeeded();

		var fileAsBytes = new ArrayList<Integer>();

		// hack - For easier debugging.
		var entriesAsByteArrays =
			this.entries.stream().map(x -> x.toBytes());

		// Now that we've written the bytes for long path entries,
		// put it back the way it was.
		this.consolidateLongPathEntries();

		for (var i = 0; i < entriesAsByteArrays.size(); i++)
		{
			var entryAsBytes = entriesAsByteArrays.get(i);
			fileAsBytes = fileAsBytes.concat(entryAsBytes);
		}

		var chunkSize = TarFile.ChunkSize;

		var intOfZeroChunksToWrite = 2;

		for (var i = 0; i < intOfZeroChunksToWrite; i++)
		{
			for (var b = 0; b < chunkSize; b++)
			{
				fileAsBytes.add(0);
			}
		}

		return fileAsBytes.toArray(new int[] {});
	}

	public void toBytes_PrependLongPathEntriesAsNeeded()
	{
		// TAR file entries with paths longer than 99 chars require cheating,
		// by prepending them with a entry of type "L" whose data contains the path.

		var typeFlagLongPath = TarFileTypeFlag.Instances().LongFilePath;
		var maxLength = TarFileEntryHeader.FileNameMaxLength;

		var entries = this.entries;
		for (var i = 0; i < entries.size(); i++)
		{
			var entry = entries.get(i);
			var entryHeader = entry.header;
			var entryFileName = entryHeader.fileName;
			if (entryFileName.length() > maxLength)
			{
				var entryFileNameAsBytes =
					entryFileName.split("").stream().map(x -> x.charAt(0));
				var entryContainingLongPathToPrepend = TarFileEntry.fileNew
				(
					typeFlagLongPath.name, entryFileNameAsBytes
				);
				entryContainingLongPathToPrepend.header.typeFlag = typeFlagLongPath;
				entryContainingLongPathToPrepend.header.timeModifiedInUnixFormat =
					entryHeader.timeModifiedInUnixFormat;
				entryContainingLongPathToPrepend.header.checksumCalculate();
				entryHeader.fileName =
					entryFileName.substring(0, maxLength) + "" + (char)0;
				entries.insert(i, entryContainingLongPathToPrepend);
				i++;
			}
		}
	}

	// strings

	public String toString()
	{
		var newline = "\n";

		var returnValue = "[TarFile]" + newline;

		for (var i = 0; i < this.entries.size(); i++)
		{
			var entry = this.entries.get(i);
			var entryAsString = entry.toString();
			returnValue += entryAsString;
		}

		returnValue += "[/TarFile]" + newline;

		return returnValue;
	}
}
