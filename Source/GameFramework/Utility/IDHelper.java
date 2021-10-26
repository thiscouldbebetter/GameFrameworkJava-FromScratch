
package GameFramework.Utility;

public class IDHelper
{
	private int _idNext;

	private static IDHelper _instance;

	public IDHelper()
	{
		this._idNext = 0;
	}

	public static IDHelper Instance()
	{
		if (IDHelper._instance == null)
		{
			IDHelper._instance = new IDHelper();
		}
		return IDHelper._instance;
	}

	public int idNext()
	{
		var returnValue = this._idNext;
		this._idNext++;
		if (this._idNext >= Integer.MAX_VALUE)
		{
			throw new Exception("IDHelper is out of IDs!");
		}
		return returnValue;
	}
}
