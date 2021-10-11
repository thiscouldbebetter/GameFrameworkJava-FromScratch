
package GameFramework.Storage;

public class StorageHelper
{
	private String propertyNamePrefix;
	private Serializer serializer;
	private CompressorLZW compressor;

	public StorageHelper
	(
		String propertyNamePrefix, Serializer serializer, CompressorLZW compressor
	)
	{
		this.propertyNamePrefix = propertyNamePrefix;
		if (this.propertyNamePrefix == null)
		{
			this.propertyNamePrefix = "";
		}

		this.serializer = serializer;
		this.compressor = compressor;
	}

	public void delete(String propertyName)
	{
		var propertyNamePrefixed =
			this.propertyNamePrefix + propertyName;

		//localStorage.removeItem(propertyNamePrefixed);
	}

	public void deleteAll()
	{
		/*
		var keysAll = Object.keys(localStorage);
		var keysWithPrefix = keysAll.filter(x => x.startsWith(this.propertyNamePrefix));
		for (var key in keysWithPrefix)
		{
			var itemToDelete = localStorage.getItem(key);
			localStorage.removeItem(itemToDelete);
		}
		*/
	}

	public Object load(String propertyName)
	{
		Object returnValue;

		var propertyNamePrefixed =
			this.propertyNamePrefix + propertyName;

		var returnValueAsStringCompressed = "todo";
		/*localStorage.getItem
		(
			propertyNamePrefixed
		);*/

		if (returnValueAsStringCompressed == null)
		{
			returnValue = null;
		}
		else
		{
			var returnValueDecompressed = this.compressor.decompressString
			(
				returnValueAsStringCompressed
			);
			returnValue = this.serializer.deserialize
			(
				returnValueDecompressed
			);
		}

		return returnValue;
	}

	public void save(String propertyName, Object valueToSave)
	{
		var valueToSaveSerialized = this.serializer.serialize
		(
			valueToSave, false // pretty-print
		);

		var valueToSaveCompressed = this.compressor.compressString
		(
			valueToSaveSerialized
		);

		var propertyNamePrefixed =
			this.propertyNamePrefix + propertyName;

		/*
		localStorage.setItem
		(
			propertyNamePrefixed,
			valueToSaveCompressed
		);
		*/
	}
}
