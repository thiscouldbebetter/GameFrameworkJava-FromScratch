
package GameFramework;

public class Input
{
	public String name;

	public boolean isActive;
	public int ticksActive;

	public Input(String name)
	{
		this.name = name;

		this.isActive = true;
		this.ticksActive = 0;
	}

	public static Input_Names _names;
	public static Names()
	{
		if (Input._names == null)
		{
			Input._names = new Input_Names();
		}

		return Input._names;
	}
}

public class Input_Names
{
	public String ArrowDown;
	public String ArrowLeft;
	public String ArrowRight;
	public String ArrowUp;
	public String Backspace;
	public String Control;
	public String Enter;
	public String Escape;
	public GamepadButton0: String;
	public GamepadButton1: String;
	public String GamepadMoveDown;
	public String GamepadMoveLeft;
	public String GamepadMoveRight;
	public String GamepadMoveUp;
	public String MouseClick;
	public String MouseMove;
	public String Shift;
	public String Space;
	public String Tab;

	public String[] _All[];
	public Map<String,String> _AllByName;

	public Input()
	{
		this.ArrowDown = "ArrowDown";
		this.ArrowLeft = "ArrowLeft";
		this.ArrowRight = "ArrowRight";
		this.ArrowUp = "ArrowUp";
		this.Backspace = "Backspace";
		this.Control = "Control";
		this.Enter = "Enter";
		this.Escape = "Escape";
		this.GamepadButton0 = "GamepadButton0_";
		this.GamepadButton1 = "GamepadButton1_";
		this.GamepadMoveDown = "GamepadMoveDown_";
		this.GamepadMoveLeft = "GamepadMoveLeft_";
		this.GamepadMoveRight = "GamepadMoveRight_";
		this.GamepadMoveUp = "GamepadMoveUp_";
		this.MouseClick = "MouseClick";
		this.MouseMove = "MouseMove";
		this.Shift = "Shift";
		this.Space = "_";
		this.Tab = "Tab";

		this._All =
		[
			this.ArrowDown,
			this.ArrowLeft,
			this.ArrowRight,
			this.ArrowUp,
			this.Backspace,
			this.Control,
			this.Enter,
			this.Escape,
			this.GamepadButton0,
			this.GamepadButton1,
			this.GamepadMoveDown,
			this.GamepadMoveLeft,
			this.GamepadMoveRight,
			this.GamepadMoveUp,
			this.MouseClick,
			this.MouseMove,
			this.Shift,
			this.Space,
			this.Tab
		];

		this._AllByName =
			ArrayHelper.addLookups(this._All, (String x) -> x);
	}
}

}
