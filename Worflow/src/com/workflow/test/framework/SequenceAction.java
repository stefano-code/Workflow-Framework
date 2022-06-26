package com.workflow.test.framework;


import java.util.ArrayList;
import java.util.List;

public class SequenceAction extends Action
{
    private String name;
    private String msg;
    private List<Action> seq;
    private int numExecuted;
    private boolean firstTime;

    protected SequenceAction(String sequenceName)
    {
        name = sequenceName;
        msg = "SequenceAction " + sequenceName;
        seq = new ArrayList<>();
        numExecuted = 0;
        firstTime = true;
    }

    public SequenceAction start(Action a)
    {
        seq.add(a);
        return this;
    }

    public SequenceAction andThen(Action a)
    {
        seq.add(a);
        return this;
    }

    @Override
    public State execute()
    {
        if (firstTime)
        {
            Journal.Log(wfContext.tag, getMsg() + " started");
            firstTime = false;
        }

        State res = doStep();
        return res;
    }

    @Override
    protected State doStep()
    {
        Action current = seq.get(numExecuted);
        current.execute();
        if (current.completed())
        {
            ++numExecuted;

            boolean end = completed();
            if (end)
            {
                Journal.Log(wfContext.tag, getMsg() + " completed");
                return State.completed;
            }
            else
                return State.running;
        }
        return current.getState();
    }

    @Override
    protected State getState()
    {
        if( completed() )
            return State.completed;
        else
        {
            Action current = seq.get(numExecuted);
            return  current.getState();
        }

    }

    @Override
    public String getMsg()
    {
        return msg;
    }

    @Override
    public String getBlockingActions()
    {
        if( completed() )
            return "(completed)";
        else
        {
            String prevPath = wfContext.actionPathForDump;
            wfContext.actionPathForDump =  wfContext.actionPathForDump + name+ ".";
            Action current = seq.get(numExecuted);
            String res = /* name+ "." +*/ current.getBlockingActions();
            wfContext.actionPathForDump = prevPath;
            return res;
        }
    }

    @Override
    public boolean completed()
    {
        return numExecuted == seq.size();
    }

    @Override
    public void reset()
    {
        super.reset();
        for(Action action : seq)
            action.reset();
        numExecuted = 0;
        firstTime = true;
    }
}
