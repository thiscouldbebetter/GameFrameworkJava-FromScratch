
package GameFramework.Display

imports GameFramework.Model.*;

public class DisplayRecorder
{
	public int ticksPerFrame;
	public int bufferSizeInFrames;
	public boolean isCircular;

	public Object[] framesRecordedAsArrayBuffers;
	public boolean isRecording;

	public boolean shouldDownload; // test

	public DisplayRecorder
	(
		int ticksPerFrame, int bufferSizeInFrames, boolean isCircular
	)
	{
		this.ticksPerFrame = ticksPerFrame;
		this.bufferSizeInFrames = bufferSizeInFrames;
		this.isCircular = isCircular;

		this.framesRecordedAsArrayBuffers = new Array<ArrayBuffer>();
		this.isRecording = false;

		this.shouldDownload = false;
	}

	public static Action actionStartStop()
	{
		return new Action
		(
			"Recording Start/Stop",
			DisplayRecorder.actionStartStopPerform
		)
	}

	public static void actionStartStopPerform
	(
		UniverseWorldPlaceEntities uwpe
	)
	{
		var universe = uwpe.universe;
		var recorder = universe.displayRecorder;
		if (recorder.isRecording)
		{
			recorder.stop();
			recorder.framesRecordedDownload(universe);
		}
		else
		{
			recorder.start();
		}
	}

	public void clear()
	{
		this.framesRecordedAsArrayBuffers.length = 0;
	}

	public void frameRecord(Display display)
	{
		var recorder = this;

		var framesRecorded = recorder.framesRecordedAsArrayBuffers;
		while (framesRecorded.length >= recorder.bufferSizeInFrames)
		{
			framesRecorded.splice(0, 1);
		}

		var displayAsImage = display.toImage();
		var displayAsCanvas = displayAsImage.systemImage;
		displayAsCanvas.toBlob
		(
			(any displayAsBlob) =>
			{
				var reader = new FileReader();
				reader.onload = () =>
				{
					var displayAsArrayBuffer = reader.result as ArrayBuffer;
					framesRecorded.push
					(
						displayAsArrayBuffer
					);
				};
				reader.readAsArrayBuffer(displayAsBlob);
			}
		);
	}

	public void framesRecordedDownload(Universe universe)
	{
		var universeName = universe.name.split(" ").join("_");
		var fileNameToSaveAs = universeName + "-Recording.tar";

		var framesRecordedAsTarFile = TarFile.create(fileNameToSaveAs);
		var digitsToPadTo = 6; // 2 hours x 24 frames/second = 172,800 frames.

		var frameCount = this.framesRecordedAsArrayBuffers.length;
		for (var i = 0; i < frameCount; i++)
		{
			var frameIndex = i;
			var frameAsArrayBuffer =
				this.framesRecordedAsArrayBuffers[frameIndex];
			if (frameAsArrayBuffer == null)
			{
				break;
			}
			var frameAsUint8Array = new Uint8Array(frameAsArrayBuffer);
			var frameAsBytes = [...frameAsUint8Array];

			var frameIndexPadded =
				StringHelper.padStart("" + frameIndex, digitsToPadTo, "0");
			var displayAsTarFileEntry = TarFileEntry.fileNew
			(
				"Frame" + frameIndexPadded + ".png", frameAsBytes
			);
			framesRecordedAsTarFile.entries.push(displayAsTarFileEntry);
		}

		var script =
			"#!/bin/sh"
			+ "\n\n"
			+ "# The PNG files in this TAR file, once extracted, "
			+ "can then be converted to an animated GIF "
			+ "with ffmpeg, using this command line:"
			+ "\n\n"
			+ "ffmpeg -pattern_type glob -i '*.png' " + universeName + ".gif";
		var scriptAsBytes = ByteHelper.stringUTF8ToBytes(script);
		var scriptAsTarFileEntry =
			TarFileEntry.fileNew("PngsToGif.sh", scriptAsBytes);
		framesRecordedAsTarFile.entries.push(scriptAsTarFileEntry);

		framesRecordedAsTarFile.downloadAs(fileNameToSaveAs);
	}

	public void logStartOrStop()
	{
		var startedOrStoppedText =
			(this.isRecording ? "started" : "stopped");

		var logMessage =
			DisplayRecorder.name + " " + startedOrStoppedText
			+ ", ticksPerFrame: " + this.ticksPerFrame
			+ ", bufferSizeInFrames:" + this.bufferSizeInFrames
			+ ", isCircular: " + this.isCircular;

		console.log(logMessage);
	}

	public void start()
	{
		this.isRecording = true;
		this.logStartOrStop();
	}

	public void stop()
	{
		this.isRecording = false;
		this.logStartOrStop();
	}

	public void updateForTimerTick(Universe universe)
	{
		if
		(
			this.isRecording
			&& universe.timerHelper.ticksSoFar % this.ticksPerFrame == 0
		)
		{
			this.frameRecord(universe.display);
			if
			(
				this.isCircular == false
				&& this.framesRecordedAsArrayBuffers.length >= this.bufferSizeInFrames
			)
			{
				this.stop();
			}
		}
	}
}

}