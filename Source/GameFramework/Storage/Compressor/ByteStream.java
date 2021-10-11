
package GameFramework.Storage.Compressor;

public interface ByteStream
{
	boolean hasMoreBytes();
	int peekByteCurrent();
	int readByte();
	int[] readBytes(int byteCount);
	String readStringOfLength(int lengthOfString);
	void writeByte(int byteToWrite);
	void writeBytes(int[] bytesToWrite);
	void writeStringPaddedToLength(String stringToWrite, int lengthPadded);
}
