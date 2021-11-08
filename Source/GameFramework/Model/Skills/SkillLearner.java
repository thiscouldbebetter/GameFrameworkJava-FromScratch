
package GameFramework.Model.Skills;

import java.util.*;
import java.util.stream.*;

import GameFramework.Controls.*;
import GameFramework.Geometry.*;
import GameFramework.Model.*;

public class SkillLearner implements EntityProperty<SkillLearner>
{
	public String skillBeingLearnedName;
	public double learningAccumulated;
	public List<String> skillsKnownNames;

	public String skillSelectedName;

	public SkillLearner
	(
		String skillBeingLearnedName,
		double learningAccumulated,
		String[] skillsKnownNames
	)
	{
		this.skillBeingLearnedName = skillBeingLearnedName;
		this.learningAccumulated = learningAccumulated;
		this.skillsKnownNames = Arrays.asList(skillsKnownNames);
	}

	public static SkillLearner _default()
	{
		return new SkillLearner(null, 0.0, new String[] {});
	}

	public boolean isLearningInProgress()
	{
		return (this.learningAccumulated > 0);
	}

	public boolean isSkillBeingLearned()
	{
		return (this.skillBeingLearnedName != null);
	}

	public Skill skillCheapestAvailable(Skill[] skillsAll)
	{
		Skill skillCheapest = null;

		var skillsAvailable = this.skillsAvailableToLearn(skillsAll);
		if (skillsAvailable.length > 0)
		{
			skillCheapest = skillsAvailable.sorted
			(
				(Skill x, Skill y) -> x.learningRequired - y.learningRequired
			)[0];
		}
		return skillCheapest;
	}

	public Object learningIncrement
	(
		Skill[] skillsAll,
		Map<String,Skill> skillsByName,
		double amountToIncrement
	)
	{
		String message = null;

		var skillBeingLearned = this.skillBeingLearned(skillsByName);

		if (skillBeingLearned == null)
		{
			var skillCheapest = this.skillCheapestAvailable(skillsAll);
			if (skillCheapest != null)
			{
				skillBeingLearned = skillCheapest;
				this.skillBeingLearnedName = skillCheapest.name;
				message = "Now learning '" + this.skillBeingLearnedName + "'.";
			}
		}

		if (skillBeingLearned != null)
		{
			this.learningAccumulated += amountToIncrement;

			var learningRequired = skillBeingLearned.learningRequired;
			if (this.learningAccumulated >= learningRequired)
			{
				message = "Learned skill '" + this.skillBeingLearnedName + "'.";
				this.skillsKnownNames.add
				(
					this.skillBeingLearnedName
				);
				this.skillBeingLearnedName = null;
				this.learningAccumulated = 0;
			}
		}

		return message;
	}

	public String learningAccumulatedOverRequired
	(
		Map<String,Skill> skillsAllByName
	)
	{
		return this.learningAccumulated + "/" + this.learningRequired(skillsAllByName);
	}

	public double learningRequired(Map<String,Skill> skillsAllByName)
	{
		var skillBeingLearned = this.skillBeingLearned(skillsAllByName);
		var returnValue =
		(
			skillBeingLearned == null
			? 0
			: skillBeingLearned.learningRequired
		);
		return returnValue;
	}

	public Skill skillSelected(Map<String,Skill> skillsAllByName)
	{
		var returnValue =
		(
			this.skillSelectedName == null
			? null
			: skillsAllByName.get(this.skillSelectedName)
		);

		return returnValue;
	}

	public Skill[] skillsAvailableToLearn(Skill skillsAll[])
	{
		var skillsUnknown = new ArrayList<Skill>();

		for (var i = 0; i < skillsAll.length; i++)
		{
			var skill = skillsAll[i];
			var skillName = skill.name;

			var isAlreadyKnown =
				this.skillsKnownNames.stream().anyMatch(x -> x == skillName);

			if (isAlreadyKnown == false)
			{
				skillsUnknown.add(skill);
			}
		}

		var skillsUnknownWithKnownPrerequisites = new ArrayList<Skill>();

		for (var i = 0; i < skillsUnknown.size(); i++)
		{
			var skill = skillsUnknown.get(i);
			var prerequisites = skill.namesOfPrerequisiteSkills;

			var areAllPrerequisitesKnown = true;

			for (var p = 0; p < prerequisites.length; p++)
			{
				var prerequisite = prerequisites[p];
				var isPrerequisiteKnown =
				(
					this.skillsKnownNames.stream().anyMatch(x -> x == prerequisite)
				);

				if (isPrerequisiteKnown == false)
				{
					areAllPrerequisitesKnown = false;
					break;
				}
			}

			if (areAllPrerequisitesKnown)
			{
				skillsUnknownWithKnownPrerequisites.add
				(
					skill
				);
			}
		}

		return skillsUnknownWithKnownPrerequisites;
	}

	public List<Skill> skillsKnown(Map<String,Skill> skillsAllByName)
	{
		var returnValues = new ArrayList<Skill>();

		for (var i = 0; i < this.skillsKnownNames.length; i++)
		{
			var skillName = this.skillsKnownNames[i];
			var skill = skillsAllByName.get(skillName);
			returnValues.add(skill);
		}

		return returnValues;
	}

	public Skill skillBeingLearned(Map<String,Skill> skillsAllByName)
	{
		var returnValue = skillsAllByName.get(this.skillBeingLearnedName);

		return returnValue;
	}

	// EntityProperty.

	public void finalize(UniverseWorldPlaceEntities uwpe){}
	public void initialize(UniverseWorldPlaceEntities uwpe){}

	public void updateForTimerTick(UniverseWorldPlaceEntities uwpe)
	{
		// Do nothing.
	}

	// controls

	public ControlBase toControl
	(
		Universe universe,
		Coords size,
		Entity entity,
		Venue venueToReturnTo,
		boolean includeTitle
	)
	{
		var display = universe.display;
		//var size = display.sizeInPixels.clone();
		var labelHeight = display.fontHeightInPixels() * 1.2;
		var margin = 20;
		var labelHeightLarge = labelHeight * 2;

		size = size.clone().addDimensions(0, 30, 0); // hack

		var listSize = Coords.fromXY
		(
			(size.x - margin * 3) / 2, 150
		);

		var defns = universe.world.defn;
		var skillLearner = this;
		var skillClassName = Skill.class.getName();
		var skillsAll = defns.defnArraysByTypeName.get(skillClassName); // todo - Just use the -ByName lookup.
		var skillsAllByName = defns.defnsByNameByTypeName.get(skillClassName);

		var returnValue = ControlContainer.from4
		(
			"Skills", // name,
			Coords.create(), // pos,
			size.clone(),
			// children
			new ControlBase[]
			{
				new ControlLabel
				(
					"labelSkillsKnown", // name,
					Coords.fromXY(margin, 40), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					"Skills Known:", //text
					labelHeight // fontHeightInPixels
				),

				ControlList.from6
				(
					"listSkillsKnown",
					Coords.fromXY(margin, 60), // pos
					listSize,
					// items
					DataBinding.fromContext(this.skillsKnownNames),
					DataBinding.fromContext(null), // bindingForItemText
					labelHeight // fontHeightInPixels
				),

				new ControlLabel
				(
					"labelSkillsAvailable", // name,
					Coords.fromXY(size.x - margin - listSize.x, 40), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					DataBinding.fromContext("Skills Available:"), // text
					labelHeight // fontHeightInPixels
				),

				ControlList.from10
				(
					"listSkillsAvailable", // name,
					Coords.fromXY(size.x - margin - listSize.x, 60), // pos,
					listSize,
					// items,
					DataBinding.fromContextAndGet
					(
						skillLearner,
						(SkillLearner c) ->
							c.skillsAvailableToLearn(skillsAll)
					),
					DataBinding.fromGet
					(
						(Skill c) -> c.name
					), // bindingForItemText
					labelHeight, // fontHeightInPixels
					new DataBinding
					(
						skillLearner,
						(SkillLearner c) ->
						{
							return c.skillSelected(skillsAllByName);
						},
						(SkillLearner c, Skill v) ->
						{
							var skillName = v.name;
							c.skillSelectedName = skillName;
						}
					), // bindingForItemSelected
					null, // bindingForItemValue
					DataBinding.fromTrue(), // isEnabled
					(Universe u) ->
					{
						skillLearner.skillBeingLearnedName =
							skillLearner.skillSelectedName;
					} // confirm
				),

				ControlLabel.from5
				(
					"labelSkillSelected", // name,
					Coords.fromXY(margin, 220), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					"Selected:" // text
				),

				ControlLabel.from5
				(
					"labelSkillSelected", // name,
					Coords.fromXY(80, 220), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					DataBinding.fromContextAndGet
					(
						skillLearner,
						(SkillLearner c) -> (c.skillSelectedName != null ? c.skillSelectedName : "-")
					)
				),

				new ControlLabel
				(
					"labelSkillSelectedDescription", // name,
					Coords.fromXY(margin, 232), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					DataBinding.fromContextAndGet
					(
						skillLearner,
						(SkillLearner c) ->
						{
							var skill = c.skillSelected(skillsAllByName);
							return (skill == null ? "-" : skill.description);
						}
					),
					null
				),

				ControlLabel.from5
				(
					"labelSkillBeingLearned", // name,
					Coords.fromXY(margin, size.y - margin - labelHeight * 2), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					"Skill Being Learned:" // text
				),

				new ControlLabel
				(
					"textSkillBeingLearned", // name,
					Coords.fromXY(145, size.y - margin - labelHeight * 2), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					DataBinding.fromContextAndGet
					(
						skillLearner,
						(SkillLearner c) ->
						{
							return (c.skillBeingLearnedName || "-");
						}
					),
					null
				),

				ControlLabel.from5
				(
					"labelLearningAccumulated", // name,
					Coords.fromXY(margin, size.y - margin - labelHeight), // pos,
					Coords.fromXY(size.x - margin * 2, labelHeight), // size,
					false, // isTextCentered,
					"Learning Accumulated:" // text
				),

				ControlLabel.from5
				(
					"textLearningAccumulated", // name,
					Coords.fromXY(145, size.y - margin - labelHeight), // pos,
					Coords.fromXY(30, labelHeight), // size,
					false, // isTextCentered,
					DataBinding.fromContextAndGet
					(
						skillLearner,
						(SkillLearner c) ->
							c.learningAccumulatedOverRequired(skillsAllByName)
					) // text
				),
			}
		);

		if (includeTitle)
		{
			returnValue.children.add
			(
				0, // indexToInsertAt
				new ControlLabel
				(
					"labelSkills",
					Coords.fromXY(200, 20), // pos
					Coords.fromXY(120, 25), // size
					true, // isTextCentered
					"Skills",
					labelHeightLarge
				)
			);
		}
		else
		{
			var titleHeightInverted = Coords.fromXY(0, -30);
			returnValue.size.add(titleHeightInverted);
			returnValue.shiftChildPositions(titleHeightInverted);
		}

		return returnValue;
	}

	// Clonable.

	public SkillLearner clone() { return this; }
	public SkillLearner overwriteWith(SkillLearner other) { return this; }
}
