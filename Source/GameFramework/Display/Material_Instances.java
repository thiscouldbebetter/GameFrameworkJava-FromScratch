
package GameFramework.Display;

public class Material_Instances
{
	public Material Default;

	public Material_Instances()
	{
		this.Default = new Material
		(
			"Default",
			Color.Instances().Blue, // colorStroke
			Color.Instances().Yellow, // colorFill
			null // texture
		);
	}
}