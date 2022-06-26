package com.workflow.test.framework;

public class WorkflowEngine
{
	private static WorkflowEngine instance;
	private Workflow wf;
	private WorkflowThread wth;

	private WorkflowEngine()
	{
	}

	public static  WorkflowEngine getInstance()
	{
		if( instance == null )
			instance = new WorkflowEngine();
		return instance;
	}

	public static SequenceAction Sequence(String sequenceName)
	{
		return new SequenceAction(sequenceName);
	}

	public static ParallelAction Parallel(String parallelName)
	{
		return new ParallelAction(parallelName);
	}

	public static CheckAction Check( Predicate<Void> p )
	{
		return new CheckAction(p);
	}

	public static CheckAction Check( String conditionName, Predicate<Void> p)
	{
		return new CheckAction(conditionName, p);
	}

	public static CheckAction Check(Pair<String, Predicate<Void>> p)
	{
		return new CheckAction(p.v1, p.v2);
	}

	public static EmptyAction EmptyAction(String sequenceName)
	{
		return new EmptyAction(sequenceName);
	}

	public void CreateWorkflow(WorkflowContext context, Action startAction)
	{
		wf = new Workflow(context, startAction);
		wth = new WorkflowThread(wf);
	}

	public void execute()
	{
	wth.start();
	}

	public void waitToFinish()
	{
		try {
			wth.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
