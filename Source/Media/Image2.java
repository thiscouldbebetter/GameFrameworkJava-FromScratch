package Media;

import Geometry.*;
import Utility.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Image2 // implements MediaItemBase
{
	public String name;
	public String sourcePath;
	public boolean isLoaded;

	public Coords sizeInPixels;
	public Image systemImage;

	public Image2(String name, String sourcePath)
	{
		this.name = name;
		this.sourcePath = sourcePath;

		this.isLoaded = false;
	}

	// static methods

	public static Image2 create()
	{
		return new Image2(null, null);
	}

	/*
	public static Image2 fromImageAndBox(imageSource: Image2, box: Box): Image2
	{
		var display = Display2D.fromSizeAndIsInvisible
		(
			box.size, true // isInvisible
		).initialize(null);

		display.drawImagePartial
		(
			imageSource,
			Coords.Instances().Zeroes,
			box
		);

		var name = imageSource.name + box.toStringXY()
		var returnImage = display.toImage(name);

		return returnImage;
	}
	*/

	public static Image2 fromSourcePath(String sourcePath)
	{
		return new Image2(sourcePath, sourcePath);
	}

	/*
	public static Image2 fromSystemImage(String name, Image systemImage)
	{
		var returnValue = new Image2
		(
			name,
			systemImage.src
		);

		returnValue.systemImage = systemImage;
		returnValue.sizeInPixels = Coords.fromXY
		(
			systemImage.width, systemImage.height
		);
		returnValue.isLoaded = true;

		return returnValue;
	}
	*/

	/*
	toTiles(sizeInTiles: Coords): Image2[][]
	{
		var tilePosInTiles = Coords.create();
		var tilePosInPixels = Coords.create();
		var tileSizeInPixels =
			this.sizeInPixels.clone().divide(sizeInTiles);

		var imageRows = [];

		for (var y = 0; y < sizeInTiles.y; y++)
		{
			tilePosInTiles.y = y;

			var imagesInRow = [];

			for (var x = 0; x < sizeInTiles.x; x++)
			{
				tilePosInTiles.x = x;

				tilePosInPixels.overwriteWith
				(
					tilePosInTiles
				).multiply
				(
					tileSizeInPixels
				);

				var box = Box.fromMinAndSize
				(
					tilePosInPixels, tileSizeInPixels
				);

				var imageForTile =
					fromImageAndBox(this, box);

				imagesInRow.push(imageForTile);
			}

			imageRows.push(imagesInRow);
		}

		return imageRows;
	}
	*/

	// Clonable.

	public Image2 clone()
	{
		var returnValue = Image2.create();

		returnValue.name = this.name;
		returnValue.sourcePath = this.sourcePath;
		returnValue.sizeInPixels = this.sizeInPixels.clone();
		returnValue.systemImage = this.systemImage;
		returnValue.isLoaded = this.isLoaded;

		return returnValue;
	}

	// Loadable.

	public Image2 load(PlatformHelper platformHelper)
	{
		if (this.sourcePath != null)
		{
			var imageFile = new File(this.sourcePath);
			try
			{
				this.systemImage = ImageIO.read(imageFile);
				if (this.sizeInPixels == null)
				{
					this.sizeInPixels =
						platformHelper.imageSizeGet(this.systemImage);
				}
			}
			catch (IOException ex)
			{
				System.out.println(ex);
			}
		}

		return this;
	}

	public Image2 unload()
	{
		this.systemImage = null;
		return this;
	}
}
