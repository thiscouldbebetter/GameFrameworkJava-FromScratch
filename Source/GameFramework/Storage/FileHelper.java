
package GameFramework.Storage;

public class FileHelper
{
	public void loadFileAsBinaryString
	(
		Object systemFileToLoad, Object callback, Object contextForCallback
	)
	{
		/*
		var fileReader = new FileReader();
		fileReader.onload = (event) ->
		{
			this.loadFile_FileLoaded(event, callback, contextForCallback, systemFileToLoad.name);
		}
		fileReader.readAsBinaryString(systemFileToLoad);
		*/
	}

	loadFileAsText(Object systemFileToLoad, Object callback, Object contextForCallback)
	{
		/*
		var fileReader = new FileReader();
		fileReader.onload = (event) ->
		{
			this.loadFile_FileLoaded(event, callback, contextForCallback, systemFileToLoad.name);
		}
		fileReader.readAsText(systemFileToLoad);
		*/
	}

	loadFile_FileLoaded(Object fileLoadedEvent, Object callback, Object contextForCallback, String fileName)
	{
		/*
		var fileReader = fileLoadedEvent.target;
		var contentsOfFileLoaded = fileReader.result;
		*/

		callback.call(contextForCallback, contentsOfFileLoaded);
	}

	public void saveBinaryStringToFileWithName
	(
		String fileAsBinaryString, String fileName
	)
	{
		/*
		var fileAsArrayBuffer = new ArrayBuffer(fileAsBinaryString.length);
		var fileAsArrayUnsigned = new Uint8Array(fileAsArrayBuffer);
		for (var i = 0; i < fileAsBinaryString.length; i++)
		{
			fileAsArrayUnsigned[i] = fileAsBinaryString.charCodeAt(i);
		}

		var Object blobTypeAsLookup = {};
		blobTypeAsLookup["type"] = "unknown/unknown";
		var fileAsBlob = new Blob([fileAsArrayBuffer], blobTypeAsLookup);

		var link = document.createElement("a");
		link.href = window.URL.createObjectURL(fileAsBlob);
		link.download = fileName;
		link.click();
		*/
	}

	public void saveBytesToFileWithName(int[] fileAsBytes, String fileName)
	{
		/*
		var fileAsArrayBuffer = new ArrayBuffer(fileAsBytes.length);
		var fileAsArrayUnsigned = new Uint8Array(fileAsArrayBuffer);
		for (var i = 0; i < fileAsBytes.length; i++)
		{
			fileAsArrayUnsigned[i] = fileAsBytes[i];
		}

		var Object blobTypeAsLookup = {};
		blobTypeAsLookup["type"] = "unknown/unknown";
		var fileAsBlob = new Blob([fileAsArrayBuffer], blobTypeAsLookup);

		var link = document.createElement("a");
		link.href = window.URL.createObjectURL(fileAsBlob);
		link.download = fileName;
		link.click();
		*/
	}

	public void saveTextStringToFileWithName
	(
		String textToSave, String fileNameToSaveAs
	)
	{
		/*
		var Object blobTypeAsLookup = {};
		blobTypeAsLookup["type"] = "text/plain";
		var textToSaveAsBlob = new Blob([textToSave], blobTypeAsLookup);
		var link = document.createElement("a");
		link.href = window.URL.createObjectURL(textToSaveAsBlob);
		link.download = fileNameToSaveAs;
		link.click();
		*/
	}
}

}
