package com.workflow.test.framework;


public class MethodReferenceAction extends SingleAction
{
	private Predicate<Void> body;
	private String msg;

	public MethodReferenceAction(String actionName, Predicate<Void> f )
	{
		msg = actionName;
		body = f;
	}

	@Override
	protected State doStep()
	{
		try
		{
			boolean completed = body.test(null);
			if (completed)
				return State.completed;
			else
				return State.running;
		}
		catch( Exception e )
		{
			return State.completed;
		}
	}

	@Override
	public String getMsg()
	{
		return msg;
	}
}
