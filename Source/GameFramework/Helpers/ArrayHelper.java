
package GameFramework.Helpers;

import java.util.*;
import java.util.function.*;

import GameFramework.Utility.*;

public class ArrayHelper
{
	/*
	public static add(List<Object> list, Object element): List<Object>
	{
		list.add(element);
		return list;
	}
	*/

	public static <T, U extends T> List<T> addMany
	(
		List<T> list, List<U> elements
	)
	{
		list.addAll(elements);
		return list;
	}

	public static <U extends T, T> List<U> addManyWithCast
	(
		List<U> list, List<T> elements
	)
	{
		for (var i = 0; i < elements.size(); i++)
		{
			var element = elements.get(i);
			var elementAsU = (U)element;
			list.add(elementAsU);
		}
		return list;
	}

	public static <K,E> HashMap<K,E> addLookups
	(
		E[] list, Function<E,K> getKeyForElement
	)
	{
		var returnLookup = new HashMap<K,E>();
		for (var i = 0; i < list.length; i++)
		{
			var element = list[i];
			var key = getKeyForElement.apply(element);
			returnLookup.put(key, element);
		}
		return returnLookup;
	}

	public static <K,E> HashMap<K,E> addLookups
	(
		List<E> list,
		Function<E,K> getKeyForElement
	)
	{
		var returnLookup = new HashMap<K, E>();
		for (var i = 0; i < list.size(); i++)
		{
			var element = list.get(i);
			var key = getKeyForElement.apply(element);
			returnLookup.put(key, element);
		}
		return returnLookup;
	}

	public static <E extends Namable> HashMap<String,E> addLookupsByName
	(
		E[] array
	)
	{
		return ArrayHelper.addLookups
		(
			array, (E element) -> element.name()
		);
	}

	public static <E extends Namable> HashMap<String,E> addLookupsByName
	(
		List<E> list
	)
	{
		return ArrayHelper.addLookups
		(
			list, (E element) -> element.name()
		);
	}

	public static <K,E> Map<K,E> addLookupsMultiple
	(
		List<E> list, Function<E,K[]> getKeysForElement
	)
	{
		var returnLookup = new HashMap<K,E>();
		for (var i = 0; i < list.size(); i++)
		{
			var element = list.get(i);
			var keys = getKeysForElement.apply(element);
			for (var k = 0; k < keys.length; k++)
			{
				var key = keys[k];
				returnLookup.put(key, element);
			}
		}
		return returnLookup;
	}
	
	/*

	static append(list: any[], other: any[]): any[]
	{
		for (var i = 0; i < other.length; i++)
		{
			var element = other[i];
			list.add(element);
		}
		return list;
	}

	static areEqual(list0: any[], list1: any[]): boolean
	{
		var areListsEqual = true;

		if (list0.length != list1.length)
		{
			areListsEqual = false;
		}
		else
		{
			for (var i = 0; i < list0.length; i++)
			{
				var element0 = list0[i];
				var element1 = list1[i];
				if (element0 == element1)
				{
					// Do nothing.
				}
				else if
				(
					element0.equals != null
					&& element1.equals != null
					&& element0.equals(element1)
				)
				{
					// Do nothing.
				}
				else
				{
					var element0AsJson = JSON.stringify(element0);
					var element1AsJson = JSON.stringify(element1);
					if (element0AsJson != element1AsJson)
					{
						areListsEqual = false;
						break;
					}
				}
			}
		}

		return areListsEqual;
	}
	*/

	public static <T> List<T> clear(List<T> list)
	{
		list.clear();
		return list;
	}

	public static double[] clone(double[] arrayToClone)
	{
		double[] returnValues = null;

		if (arrayToClone != null)
		{
			returnValues = new double[arrayToClone.length];
				
			for (var i = 0; i < arrayToClone.length; i++)
			{
				var element = arrayToClone[i];
				var elementCloned = element.clone();
				returnValues[i] = elementCloned;
			}
		}

		return returnValues;
	}

	public static <T extends Clonable<T>> T[] clone(T[] arrayToClone)
	{
		T[] returnValues = null;

		if (arrayToClone != null)
		{
			returnValues =
				//new T[arrayToClone.length];
				(T[]) Array.newInstance(T.getClass(), arrayToClone.length);

			for (var i = 0; i < arrayToClone.length; i++)
			{
				var element = arrayToClone[i];
				var elementCloned = element.clone();
				returnValues[i] = elementCloned;
			}
		}

		return returnValues;
	}

	public static <T extends Clonable<T>> List<T> clone(List<T> list)
	{
		List<T> returnValue = null;

		if (list != null)
		{
			returnValue = new ArrayList<T>();

			for (var i = 0; i < list.size(); i++)
			{
				var element = list.get(i);
				var elementCloned = element.clone();
				returnValue.add(elementCloned);
			}
		}

		return returnValue;
	}

	/*
	public static <T> List<T> flattenListOfLists(listOfLists: List<List<T>>)
	{
		var listFlattened: any[] = [];

		for (var i = 0; i < listOfLists.length; i++)
		{
			var childList = listOfLists[i];
			listFlattened =
				listFlattened.concat(childList);
		}

		return listFlattened;
	}

	static contains(list: any, elementToFind: any): boolean
	{
		return (list.indexOf(elementToFind) >= 0);
	}
	*/

	public static <T> boolean equals(T[] list, T[] other)
	{
		var areEqualSoFar = true;

		if (list.length != other.length)
		{
			areEqualSoFar = false;
		}
		else
		{
			for (var i = 0; i < list.length; i++)
			{
				areEqualSoFar = list[i].equals(other[i]);
				if (areEqualSoFar == false)
				{
					break;
				}
			}
		}

		return areEqualSoFar;
	}

	public static boolean <T> equals(List<T> list, List<T> other)
	{
		var areEqualSoFar = true;

		if (list.size() != other.size())
		{
			areEqualSoFar = false;
		}
		else
		{
			for (var i = 0; i < list.size(); i++)
			{
				areEqualSoFar = list.get(i).equals(other.get(i));
				if (areEqualSoFar == false)
				{
					break;
				}
			}
		}

		return areEqualSoFar;
	}

	/*
	static insertElementAfterOther
	(
		list: any[], elementToInsert: any, other: any
	): any[]
	{
		var index = list.indexOf(other);
		if (index >= 0)
		{
			list.splice(index + 1, 0, elementToInsert);
		}
		else
		{
			list.add(elementToInsert);
		}
		return list;
	}

	static insertElementAt
	(
		list: any[], element: any, index: number
	): any[]
	{
		list.splice(index, 0, element);
		return list;
	}

	static intersectLists(list0: any[], list1: any[]): any[]
	{
		var elementsInBothLists = new List<any>();

		for (var i = 0; i < list0.length; i++)
		{
			var element = list0[i];

			var isElementInList1 = (list1.indexOf(element) >= 0);
			if (isElementInList1)
			{
				elementsInBothLists.add(element);
			}
		}
		return elementsInBothLists;
	}
	*/

	public static double[] overwriteWith(double[] arrayToOverwrite, double[] other)
	{
		for (var i = 0; i < arrayToOverwrite.length; i++)
		{
			arrayToOverwrite[i] = other[i];
		}

		return arrayToOverwrite;
	}

	public static <T extends Clonable<T>> T[] overwriteWith(T[] array, T[] other)
	{
		for (var i = 0; i < array.length; i++)
		{
			var elementThis = array[i];
			var elementOther = other[i];
			elementThis.overwriteWith(elementOther);
		}

		return array;
	}

	public static <T extends Clonable<T>> List<T> overwriteWith(List<T> list, List<T> other)
	{
		for (var i = 0; i < list.size(); i++)
		{
			var elementThis = list.get(i);
			var elementOther = other.get(i);
			elementThis.overwriteWith(elementOther);
		}

		return list;
	}

	/*
	static prepend(list: any, other: any): any[]
	{
		for (var i = 0; i < other.length; i++)
		{
			var element = other[i];
			list.splice(0, 0, element);
		}
		return list;
	}

	static random(list: any, randomizer: Randomizer): any
	{
		return list[ Math.floor(randomizer.getNextRandom() * list.length) ];
	}
	*/

	public static <T> List<T> remove(List<T> list, T elementToRemove)
	{
		var indexToRemoveAt = list.indexOf(elementToRemove);
		if (indexToRemoveAt >= 0)
		{
			list.remove(indexToRemoveAt);
		}
		return list;
	}

	public static <T> List<T> removeAt(List<T> list, int index)
	{
		list.remove(index);
		return list;
	}

}
