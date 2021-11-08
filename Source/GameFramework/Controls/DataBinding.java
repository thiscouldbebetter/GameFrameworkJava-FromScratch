
package GameFramework.Controls;

import java.util.function.*;

public class DataBinding<TContext,TValue>
{
	public TContext context;
	public Function<TContext,TValue> _get;
	public BiConsumer<TContext,TValue> _set;

	public DataBinding
	(
		TContext context,
		Function<TContext,TValue> get,
		BiConsumer<TContext,TValue> set
	)
	{
		this.context = context;
		this._get = get;
		this._set = set;
	}

	public static <TContext> DataBinding<TContext,TContext> fromContext
	(
		TContext context
	)
	{
		return new DataBinding<TContext, TContext>
		(
			context,
			(TContext context2) -> context,
			(TContext context3, TContext value) -> {} // set
		);
	}

	public static <TContext,TValue> DataBinding<TContext,TValue> fromContextAndGet
	(
		TContext context,
		Function<TContext,TValue> get
	)
	{
		return new DataBinding<TContext,TValue>
		(
			context, get, (TContext context2, TValue value) -> {}
		);
	}

	public static <TContext,TValue> DataBinding<TContext,TValue> fromGet
	(
		Function<TContext,TValue> get
	)
	{
		return new DataBinding<TContext,TValue>
		(
			null, get, (TContext context, TValue value) -> {}
		);
	}

	public static <TContext> DataBinding<TContext,Boolean> fromTrue(TContext context)
	{
		return DataBinding.fromGet
		(
			(TContext context2) -> (Boolean)true
		);
	}

	public static DataBinding fromTrue()
	{
		return DataBinding.fromContext((Boolean)true);
	}

	public DataBinding<TContext,TValue> contextSet(TContext value)
	{
		this.context = value;
		return this;
	}

	public TValue get()
	{
		return this._get.apply(this.context);
	}

	public void set(TValue value)
	{
		if (this._set == null)
		{
			this.context = (TContext)value;
		}
		else
		{
			this._set.accept(this.context, value);
		}
	}
}
