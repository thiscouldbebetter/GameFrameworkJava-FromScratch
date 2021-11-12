
package GameFramework.Storage;

public class Serializer
{
	public <T> T deserialize(String stringToDeserialize)
	{
		throw new Exception("todo");

		/*
		var nodeRoot = JSON.parse(StringToDeserialize);
		var typeNames = nodeRoot["typeNames"];
		nodeRoot.__proto__ = SerializerNode.prototype;
		nodeRoot.prototypesAssign();
		var returnValue = nodeRoot.unwrap(typeNames, []);
		return returnValue;
		*/
	}

	public <T> String serialize(T objectToSerialize, boolean prettyPrint)
	{
		throw new Exception("todo");
		/*
		var nodeRoot = new SerializerNode(objectToSerialize);

		var typeNames = new List<String>();
		nodeRoot.wrap
		(
			typeNames,
			new HashMap<String, Integer>(), // typeIdsByName
			new ArrayList<Object>(), // objectsAlreadyWrapped 
			new ArrayList<Object>() // objectIndexToNodeLookup
		);

		// nodeRoot["typeNames"] = typeNames;

		var nodeRootSerialized = JSON.Stringify
		(
			nodeRoot,
			null, // ?
			(prettyPrint == true ? 4 : null) // pretty-print indent size
		);

		return nodeRootSerialized;
		*/
	}
}

/*
public class SerializerNode
{
	public int t;
	public int i;
	public int r;
	public Map<String,Object> o;
	public Object c; // todo - Map<String, Object> - Tricky.

	public SerializerNode(Object objectWrapped)
	{
		this.o = objectWrapped;
	}

	public void wrap
	(
		List<String> typeNamesSoFar,
		Map<String, Integer> typeIdsByName,
		List<Object> objectsAlreadyWrapped,
		List<Object> objectIndexToNodeLookup
	)
	{
		var objectWrapped = this.o;
		if (objectWrapped != null)
		{
			var typeName = objectWrapped.constructor.name;

			if (typeIdsByName.has(typeName) == false)
			{
				typeIdsByName.set(typeName, typeNamesSoFar.length);
				typeNamesSoFar.push(typeName);
			}

			var typeId = typeIdsByName.get(typeName);

			var objectIndexExisting =
				objectsAlreadyWrapped.indexOf(objectWrapped);

			if (objectIndexExisting >= 0)
			{
				var nodeForObjectExisting = objectIndexToNodeLookup[objectIndexExisting];
				this.i = nodeForObjectExisting.i;
				this.r = 1; // isReference
				//delete this.o; // objectWrapped
			}
			else
			{
				// this.r = 0; // isReference
				var objectIndex = objectsAlreadyWrapped.length;
				this.i = objectIndex;
				objectsAlreadyWrapped.push(objectWrapped);
				objectIndexToNodeLookup[objectIndex] = this;

				this.t = typeId;

				if (typeName == Function.name)
				{
					this.o = objectWrapped.toString();
				}
				else
				{
					var children = new HashMap<String, Object>();
					this.c = children;

					if (typeName == Map.name)
					{
						// Maps don't serialize well with JSON.Stringify(),
						// so convert it to a generic object.
						var objectWrappedAsObject = new HashMap<String, Object>;
						for (var key of objectWrapped.keys())
						{
							var value = objectWrapped.get(key);
							objectWrappedAsObject.put(key, value);
						}
						objectWrapped = objectWrappedAsObject;
					}

					for (var propertyName in objectWrapped)
					{
						if (true) // objectWrapped.__proto__[propertyName] == null)
						{
							var propertyValue = objectWrapped.get(propertyName);

							if (propertyValue == null)
							{
								child = null;
							}
							else
							{
								var propertyValueTypeName = propertyValue.constructor.name;

								if
								(
									propertyValueTypeName == Boolean.name
									|| propertyValueTypeName == Number.name
									|| propertyValueTypeName == String.name
								)
								{
									child = propertyValue;
								}
								else
								{
									child = new SerializerNode
									(
										propertyValue
									);
								}

							}

							children[propertyName] = child;
						}
					}

					delete this.o;

					for (var childName in children)
					{
						var child = children[childName];
						if (child != null)
						{
							var childTypeName = child.GetType().name;
							if (childTypeName == SerializerNode.name)
							{
								child.wrap
								(
									typeNamesSoFar,
									typeIdsByName,
									objectsAlreadyWrapped,
									objectIndexToNodeLookup
								);
							}
						}
					}
				}
			}

		} // end if objectWrapped != null

		return this;

	}

	public void prototypesAssign()
	{
		var children = this.c;
		if (children != null)
		{
			for (var childName in children)
			{
				var child = children[childName];
				if (child != null)
				{
					var childTypeName = child.constructor.name;
					if (childTypeName == Object.name)
					{
						Object.setPrototypeOf(child, SerializerNode.prototype);
						child.prototypesAssign();
					}
				}
			}
		}
	}

	public Object unwrap
	(
		String typeNames[], Object nodesAlreadyProcessed
	)
	{
		var isReference = (this.r == 1);
		if (isReference)
		{
			var nodeExisting = nodesAlreadyProcessed[this.i];
			this.o = nodeExisting.o; // objectWrapped
		}
		else
		{
			nodesAlreadyProcessed[this.i] = this;
			var typeId = this.t;
			var typeName = typeNames[typeId];
			if (typeName == null)
			{
				// Value is null.  Do nothing.
			}
			else if (typeName == Array.name)
			{
				this.o = [];
			}
			else if (typeName == Function.name)
			{
				this.o = eval("(" + this.o + ")");
			}
			else if (typeName == Map.name)
			{
				this.o = new Map();
			}
			else if
			(
				typeName == Boolean.name
				|| typeName == Number.name
				|| typeName == String.name
			)
			{
				// Primitive types. Do nothing.
			}
			else
			{
				this.o = {};
				var objectWrappedType = eval("(" + typeName + ")");
				this.o.__proto__ = objectWrappedType.prototype;
			}

			var children = this.c;
			if (children != null)
			{
				for (var childName in children)
				{
					var child = children[childName];

					if (child != null)
					{
						if (child.constructor.name == SerializerNode.name)
						{
							child = child.unwrap
							(
								typeNames, nodesAlreadyProcessed
							);
						}
					}

					if (this.o.constructor.name == Map.name)
					{
						this.o.set(childName, child);
					}
					else
					{
						this.o[childName] = child;
					}
				}
			}

		}

		return this.o; // objectWrapped
	}
}
*/

