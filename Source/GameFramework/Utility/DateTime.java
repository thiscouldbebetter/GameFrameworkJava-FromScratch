
package GameFramework.Utility;

import java.util.*;

import GameFramework.Helpers.*;

public class DateTime
{
	public int year;
	public int month;
	public int day;
	public int hours;
	public int minutes;
	public int seconds;

	public DateTime
	(
		int year, int month, int day, int hours, int minutes, int seconds
	)
	{
		this.year = year;
		this.month = month;
		this.day = day;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	// static methods

	public static DateTime fromSystemDate(Date systemDate)
	{
		var returnValue = new DateTime
		(
			systemDate.getFullYear(),
			systemDate.getMonth() + 1,
			systemDate.getDate(),
			systemDate.getHours(),
			systemDate.getMinutes(),
			systemDate.getSeconds()
		);

		return returnValue;
	}

	public static DateTime now()
	{
		return DateTime.fromSystemDate(new Date());
	}

	// instance methods

	public boolean equals(DateTime other)
	{
		var returnValue =
		(
			this.year == other.year
			&& this.month == other.month
			&& this.day == other.day
			&& this.hours == other.hours
			&& this.minutes == other.minutes
			&& this.seconds == other.seconds
		);

		return returnValue;
	}

	public String toStringMMDD_HHMM_SS()
	{
		var returnValue =
			""
			+ StringHelper.padStart("" + this.month, 2, "0")
			+ StringHelper.padStart("" + this.day, 2, "0")
			+ "-"
			+ StringHelper.padStart("" + this.hours, 2, "0")
			+ StringHelper.padStart("" + this.minutes, 2, "0")
			+ "-"
			+ StringHelper.padStart("" + this.seconds, 2, "0");

		return returnValue;
	}

	public String toStringHH_MM_SS()
	{
		var returnValue =
			StringHelper.padStart("" + this.hours, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.minutes, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.seconds, 2, "0");

		return returnValue;
	}

	public String toStringTimestamp()
	{
		var returnValue =
			""
			+ this.year
			+ "/"
			+ StringHelper.padStart("" + this.month, 2, "0")
			+ "/"
			+ StringHelper.padStart("" + this.day, 2, "0")
			+ "-"
			+ StringHelper.padStart("" + this.hours, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.minutes, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.seconds, 2, "0");

		return returnValue;
	}

	public String toStringYYYY_MM_DD()
	{
		var returnValue =
			"" + this.year
			+ "/" + StringHelper.padStart("" + this.month, 2, "0")
			+ "/" + StringHelper.padStart("" + this.day, 2, "0");

		return returnValue;
	}

	public String toStringYYYYMMDD_HHMM_SS()
	{
		return this.year + "-" + this.toStringMMDD_HHMM_SS();
	}

	public String toStringYYYY_MM_DD_HH_MM_SS()
	{
		var returnValue =
			"" + this.year
			+ "/"
			+ StringHelper.padStart("" + this.month, 2, "0")
			+ "/"
			+ StringHelper.padStart("" + this.day, 2, "0")
			+ "-"
			+ StringHelper.padStart("" + this.hours, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.minutes, 2, "0")
			+ ":"
			+ StringHelper.padStart("" + this.seconds, 2, "0");

		return returnValue;
	}
}
