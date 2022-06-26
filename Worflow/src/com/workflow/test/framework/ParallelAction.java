package com.workflow.test.framework;

import java.util.ArrayList;
import java.util.List;

public class ParallelAction extends Action
{
    private String name;
    private String msg;
    private List<Action> par;
    boolean allExecuted;
    int currentActionIndex;
    private boolean firstTime;


    protected ParallelAction(String parallelName)
    {
        name = parallelName;
        msg = "ParallelAction " + parallelName;
        par = new ArrayList<>();
        allExecuted = false;
        currentActionIndex = 0;
        firstTime = true;
    }

    public ParallelAction start(Action a)
    {
        par.add(a);
        return this;
    }

    public ParallelAction andConcurrently(Action a)
    {
        par.add(a);
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
        //SS Journal.Log(wfContext.tag, getMsg() + " doStep");
        if (!allExecuted)
        {
            boolean someBlocked = anyBlocked();
            Journal.Log(wfContext.tag, getMsg() + " someBlocked:" + someBlocked);

            findNextExecutableAction();

            if (currentActionIndex >= 0)
            {
                Journal.Log(wfContext.tag, getMsg() + " executing:" + currentActionIndex);
                par.get(currentActionIndex).execute();
                return State.running;
            } else if (someBlocked)
            {
                Journal.Log(wfContext.tag, getMsg() + " blocked:" );
                return State.blocked;
            } else
            {
                Journal.Log(wfContext.tag, getMsg() + " completed");
                allExecuted = true;
                return State.completed;
            }
        }
        return State.completed;
    }

    @Override
    protected State getState()
    {
        if (completed())
            return State.completed;
        else
        {
            if (anyExecutable())
                return State.running;
            else
                return State.blocked;
        }
    }

    private boolean anyExecutable()
    {
        for (Action a : par)
            if (a.getState() == State.idle || a.getState() == State.running)
                return true;
        return false;
    }

    private boolean anyBlocked()
    {
        for (Action a : par)
            if (a.getState() == State.blocked)
                return true;
        return false;
    }

    private void findNextExecutableAction()
    {
        int initialCurrentIndex = currentActionIndex;
        do
        {
            moveToNext();
            Action current = par.get(currentActionIndex);
            if (current.getState() == State.idle || current.getState() == State.running)
                return;
        } while (currentActionIndex != initialCurrentIndex);
        currentActionIndex = -1;
    }

    private void moveToNext()
    {
        if (currentActionIndex < par.size() - 1)
            ++currentActionIndex;
        else
            currentActionIndex = 0;
    }

    @Override
    public String getMsg()
    {
        return msg;
    }

    @Override
    public boolean completed()
    {
        if (allExecuted)
            return true;

        for (Action a : par)
            if (a.getState() != State.completed)
                return false;

        allExecuted = true;
        return true;
    }

    @Override
    public String getBlockingActions()
    {
        String prevPath = wfContext.actionPathForDump;
        wfContext.actionPathForDump = wfContext.actionPathForDump + name + ".";

        StringBuffer sb = new StringBuffer();
        for (Action a : par)
        //if (a.getState() == State.blocked)
        {
            sb.append( name ) ;
            sb.append( "." ) ;
            sb.append(a.getBlockingActions());
            sb.append("; ");
        }
        String res = sb.toString();
        wfContext.actionPathForDump = prevPath;
        return res;
    }
}
