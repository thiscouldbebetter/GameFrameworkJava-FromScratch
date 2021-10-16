
package GameFramework.Controls;

import java.util.function.*;

public class DataBinding<C,V>
{
	public C context;
	public Function<C,V> _get;
	public BiConsumer<C,V> _set;

	public DataBinding
	(
		C context,
		Function<C,V> _get,
		BiConsumer<C,V> _set
	)
	{
		this.context = context;
		this._get = get;
		this._set = set;
	}

	public static <C> DataBinding<Object, C> fromContext(C context)
	{
		return new DataBinding<Object, C>(context, null, null);
	}

	public static <C,V> DataBinding<C,V> fromContextAndGet
	(
		C context,
		Function<C,V> get
	)
	{
		return new DataBinding(context, get, null);
	}

	public static <C,V> DataBinding<C,V> fromGet
	(
		Function<C,V> get
	)
	{
		return new DataBinding(null, get, null);
	}

	public static DataBinding<Object,Boolean> fromTrue()
	{
		return DataBinding.fromContext<Boolean>(true);
	}

	public DataBinding<C, V> contextSet(C value)
	{
		this.context = value;
		return this;
	}

	public V get()
	{
		return
		(
			this._get == null
			? this.context
			: this._get.call(this.context)
		);
	}

	public void set(V value)
	{
		if (this._set == null)
		{
			this.context = (C)value;
		}
		else
		{
			this._set.call(this.context, value);
		}
	}
}
