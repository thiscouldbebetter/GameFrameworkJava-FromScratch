
package GameFramework.Model.Skills;

public class Skill
{
	public String name;
	public double learningRequired;
	public String namesOfPrerequisiteSkills[];
	public String description;

	public Skill
	(
		String name,
		double learningRequired,
		String namesOfPrerequisiteSkills[],
		String description
	)
	{
		this.name = name;
		this.learningRequired = learningRequired;
		this.namesOfPrerequisiteSkills = namesOfPrerequisiteSkills;
		this.description = description;
	}

	public static Skill[] skillsDemo()
	{
		var returnValues = new Skill[]
		{
			// Skill(name, cost, prerequisites, description)

			new Skill
			(
				"Jumping", 4, new String[] {},
				"A jump.  Upwards.  Into the air."
			),
			new Skill
			(
				"Running", 4, new String[] {},
				"Like walking, but faster and harder."
			),
			new Skill
			(
				"Sneaking", 4, new String[] {},
				"Like walking, but slower and quieter."
			),
			new Skill
			(
				"Strafing", 4, new String[] {},
				"Like walking, but sideways."
			),

			new Skill
			(
				"Hiding", 8, new String[] { "Running" },
				"Like standing, but less noticable."
			),
			new Skill
			(
				"JumpingHigher", 8, new String[] { "Jumping" },
				"Like jumping, but higher."
			),
			new Skill
			(
				"RunningFaster", 8, new String[] { "Running" },
				"Like running, but faster."
			),

			new Skill
			(
				"Dashing", 16, new String[] { "RunningFaster", "JumpingHigher" },
				"Like running, but less civic-minded."
			),

			new Skill
			(
				"HidingLonger", 16, new String[] { "Hiding" },
				"Like hiding, but longer."
			),

			new Skill
			(
				"Teleporting", 32, new String[] { "Dashing", "HidingLonger" },
				"Fzamph!  Now you're over here."
			),
		};

		return returnValues;
	}
}
