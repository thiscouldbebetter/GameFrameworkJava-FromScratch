
package GameFramework.Media;

import java.lang.reflect.*;
import java.util.*;
import GameFramework.*;
import GameFramework.Helpers.*;

public class MediaLibrary
{
	private List<Image2> images;
	private List<Sound> sounds;
	private List<Video> videos;
	private List<Font> fonts;
	private List<TextString> textStrings;

	private HashMap<String, Image2> imagesByName;
	private HashMap<String, Sound> soundsByName;
	private HashMap<String, Video> videosByName;
	private HashMap<String, Font> fontsByName;
	private HashMap<String, TextString> textStringsByName;

	private List<List<MediaLibraryItem>> collectionsAll;
	private HashMap<String, HashMap<String, MediaLibraryItem>> collectionsByName;

	private Object timer;

	public MediaLibrary
	(
		List<Image2> images,
		List<Sound> sounds,
		List<Video> videos,
		List<Font> fonts,
		List<TextString> textStrings
	)
	{
		this.images = (images == null ? new ArrayList<Image2>() {} : images);
		this.imagesByName = ArrayHelper.addLookupsByName(this.images);
		this.sounds = (sounds == null ? new ArrayList<Sound>() {} : sounds);
		this.soundsByName = ArrayHelper.addLookupsByName(this.sounds);
		this.videos = (videos == null ? new ArrayList<Video>() {} : videos);
		this.videosByName = ArrayHelper.addLookupsByName(this.videos);
		this.fonts = (fonts == null ? new ArrayList<Font>() {} : fonts);
		this.fontsByName = ArrayHelper.addLookupsByName(this.fonts);
		this.textStrings = (textStrings == null ? new ArrayList<TextString>() {} : textStrings);
		this.textStringsByName = ArrayHelper.addLookupsByName(this.textStrings);

		var imagesAsMediaLibraryItems =
			ArrayHelper.addMany(new ArrayList<MediaLibraryItem>(), this.images);
		var soundsAsMediaLibraryItems =
			ArrayHelper.addMany(new ArrayList<MediaLibraryItem>(), this.sounds);
		var videosAsMediaLibraryItems =
			ArrayHelper.addMany(new ArrayList<MediaLibraryItem>(), this.videos);
		var fontsAsMediaLibraryItems =
			ArrayHelper.addMany(new ArrayList<MediaLibraryItem>(), this.fonts);
		var textStringsAsMediaLibraryItems =
			ArrayHelper.addMany(new ArrayList<MediaLibraryItem>(), this.textStrings);

		this.collectionsAll = new ArrayList<List<MediaLibraryItem>>
		(
			List.of
			(
				imagesAsMediaLibraryItems,
				soundsAsMediaLibraryItems,
				videosAsMediaLibraryItems,
				fontsAsMediaLibraryItems,
				textStringsAsMediaLibraryItems
			)
		);

		this.collectionsByName = new HashMap<String, HashMap<String, MediaLibraryItem> >()
		{{
			put
			(
				"Images", ArrayHelper.addLookupsByName(imagesAsMediaLibraryItems)
			);
			put
			(
				"Sounds", ArrayHelper.addLookupsByName(soundsAsMediaLibraryItems)
			);
			put
			(
				"Videos", ArrayHelper.addLookupsByName(videosAsMediaLibraryItems)
			);
			put
			(
				"Fonts", ArrayHelper.addLookupsByName(fontsAsMediaLibraryItems)
			);
			put
			(
				"TextStrings", ArrayHelper.addLookupsByName(textStringsAsMediaLibraryItems)
			);
		}};
	}

	public static MediaLibrary create()
	{
		return MediaLibrary.fromFilePaths(new String[] {});
	}

	public static MediaLibrary fromFilePaths(String[] mediaFilePaths)
	{
		var images = new ArrayList<Image2>();
		var sounds = new ArrayList<Sound>();
		var videos = new ArrayList<Video>();
		var fonts = new ArrayList<Font>();
		var textStrings = new ArrayList<TextString>();

		var imageTypeDirectoryNameAndList =
			new Object[] { Image2.class, "Images", images };
		var soundTypeDirectoryNameAndList =
			new Object[] { Sound.class, "Audio", sounds };
		var textStringTypeDirectoryNameAndList =
			new Object[] { TextString.class, "Text", textStrings };

		var typesDirectoryNamesAndListsByFileExtension = new HashMap<String,Object[]>()
		{{
			put("jpg", imageTypeDirectoryNameAndList);
			put("png", imageTypeDirectoryNameAndList);
			put("svg", imageTypeDirectoryNameAndList);

			put("mp3", soundTypeDirectoryNameAndList);
			put("wav", soundTypeDirectoryNameAndList);

			put("webm", new Object[] { Video.class, "Video", videos } );

			put("ttf", new Object[] { Font.class, "Fonts", fonts } );

			put("json", textStringTypeDirectoryNameAndList);
			put("txt", textStringTypeDirectoryNameAndList);
		}};

		for (var i = 0; i < mediaFilePaths.length; i++)
		{
			var filePath = mediaFilePaths[i];

			var fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);
			var typeDirectoryNameAndList =
				typesDirectoryNamesAndListsByFileExtension.get(fileExtension);
			var mediaType = (Class)typeDirectoryNameAndList[0];
			var mediaTypeConstructor =
				mediaType.getDeclaredConstructors()[0];
			var mediaDirectoryName = (String)typeDirectoryNameAndList[1];
			@SuppressWarnings("unchecked") // todo - Make this unnecessary.
			var mediaArray = (List<MediaLibraryItem>)(typeDirectoryNameAndList[2]);

			var filePathParts = Arrays.asList(filePath.split("/"));
			var filePathPartIndexForMediaType =
				filePathParts.indexOf(mediaDirectoryName);
			for (var fpp = 0; fpp < filePathPartIndexForMediaType + 1; fpp++)
			{
				filePathParts.remove(0);
			}
			var fileName = String.join("_", filePathParts);
			var fileStemAndExtension = fileName.split(".");
			var fileStem = fileStemAndExtension[0];

			try
			{
				var mediaObject =
					(MediaLibraryItem)mediaTypeConstructor.newInstance(fileStem, filePath);
				mediaArray.add(mediaObject);
			}
			catch (Exception ex)
			{
				// Do nothing.
			}
		}

		var returnValue = new MediaLibrary
		(
			images, sounds, videos, fonts, textStrings
		);

		return returnValue;
	}

	public static MediaLibrary fromFileNamesByCategory
	(
		String contentPath,
		String[] imageFileNames,
		String[] effectFileNames,
		String[] musicFileNames,
		String[] videoFileNames,
		String[] fontFileNames,
		String[] textStringFileNames
	)
	{
		var mediaTypesPathsAndFileNames = new Object[][]
		{
			new Object[] { Image2.class, "Images", imageFileNames },
			new Object[] { Sound.class, "Audio/Effects", effectFileNames },
			new Object[] { Sound.class, "Audio/Music", musicFileNames },
			new Object[] { Video.class, "Video", videoFileNames },
			new Object[] { Font.class, "Fonts", fontFileNames },
			new Object[] { TextString.class, "Text", textStringFileNames },
		};

		var mediaCollectionsByPath =
			new HashMap<String,List<MediaLibraryItem>>();

		for (var t = 0; t < mediaTypesPathsAndFileNames.length; t++)
		{
			Object[] mediaTypePathAndFileNames = mediaTypesPathsAndFileNames[t];
			var mediaType = (Class)mediaTypePathAndFileNames[0];
			var mediaTypeConstructor =
				mediaType.getDeclaredConstructors()[0];
			String mediaPath = (String)mediaTypePathAndFileNames[1];
			String[] mediaFileNames = (String[])mediaTypePathAndFileNames[2];
			var mediaCollection = new ArrayList<MediaLibraryItem>();

			var filePathRoot = contentPath + mediaPath + "/";
			for (var i = 0; i < mediaFileNames.length; i++)
			{
				var fileName = mediaFileNames[i];
				var id = fileName.substring(0, fileName.indexOf("."));
				var filePath = filePathRoot + fileName;
				try
				{
					var mediaObject =
						(MediaLibraryItem)mediaTypeConstructor.newInstance(id, filePath);
					mediaCollection.add(mediaObject);
				}
				catch (Exception ex)
				{
					// Do nothing.
				}
			}

			mediaCollectionsByPath.put(mediaPath, mediaCollection);
		}

		var images = ArrayHelper.addManyWithCast
		(
			new ArrayList<Image2>(), mediaCollectionsByPath.get("Images")
		);
		var soundEffects = ArrayHelper.addManyWithCast
		(
			new ArrayList<Sound>(), mediaCollectionsByPath.get("Audio/Effects")
		);
		var soundMusics = ArrayHelper.addManyWithCast
		(
			new ArrayList<Sound>(), mediaCollectionsByPath.get("Audio/Music")
		);
		var videos = ArrayHelper.addManyWithCast
		(
			new ArrayList<Video>(), mediaCollectionsByPath.get("Video")
		);
		var fonts = ArrayHelper.addManyWithCast
		(
			new ArrayList<Font>(), mediaCollectionsByPath.get("Fonts")
		);
		var textStrings = ArrayHelper.addManyWithCast
		(
			new ArrayList<TextString>(), mediaCollectionsByPath.get("Text")
		);

		var sounds = ArrayHelper.addMany(soundEffects, soundMusics);

		var returnValue = new MediaLibrary
		(
			images, sounds, videos, fonts, textStrings
		);

		return returnValue;
	}

	/*

	// Instance methods.

	public boolean areAllItemsLoaded()
	{
		var areAllItemsLoadedSoFar = true;

		for (var c = 0; c < this.collectionsAll.length; c++)
		{
			var collection = this.collectionsAll[c];
			for (var i = 0; i < collection.length; i++)
			{
				var item = collection[i];
				if (item.isLoaded == false)
				{
					areAllItemsLoadedSoFar = false;
					break;
				}
			}

			if (areAllItemsLoadedSoFar == false)
			{
				break;
			}
		}

		return areAllItemsLoadedSoFar;
	}

	public void waitForItemToLoad
	(
		String collectionName, String itemName, callback: ()=>void
	)
	{
		var itemToLoad = this.collectionsByName.get(collectionName).get(itemName);
		this.timer = setInterval
		(
			this.waitForItemToLoad_TimerTick.bind(this, itemToLoad, callback),
			100 // milliseconds
		);
	}

	waitForItemToLoad_TimerTick(itemToLoad: Object, callback: ()=>void ): void
	{
		if (itemToLoad.isLoaded)
		{
			clearInterval(this.timer);
			callback();
		}
	}

	waitForItemsAllToLoad(callback: ()=>void): void
	{
		this.timer = setInterval
		(
			this.waitForItemsAllToLoad_TimerTick.bind(this, callback),
			100 // milliseconds
		);
	}

	waitForItemsAllToLoad_TimerTick(callback: ()=>void)
	{
		if (this.areAllItemsLoaded())
		{
			clearInterval(this.timer);
			callback();
		}
	}

	// accessors

	imagesAdd(images: List<Image2>): void
	{
		for (var i = 0; i < images.length; i++)
		{
			var image = images[i];
			if (this.imagesByName.get(image.name) == null)
			{
				this.images.add(image);
				this.imagesByName.set(image.name, image);
			}
		}
	}
	*/

	public Font fontGetByName(String name)
	{
		return this.fontsByName.get(name);
	}

	public Image2 imageGetByName(String name)
	{
		return this.imagesByName.get(name);
	}

	public Sound soundGetByName(String name)
	{
		return this.soundsByName.get(name);
	}

	public TextString textStringGetByName(String name)
	{
		return this.textStringsByName.get(name);
	}

	public Video videoGetByName(String name)
	{
		return this.videosByName.get(name);
	}
}
