package com.workflow.test.framework;

public abstract class SingleAction extends Action
{
    protected State state;

    public SingleAction()
    {
        state = State.idle;
    }

    @Override
    public State execute()
    {
        logExecution();

        if (state == State.idle)
            state = State.running;

        state = doStep();

        return state;
    }

    @Override
    protected State getState()
    {
        return state;
    }

    protected boolean completed()
    {
        return state == State.completed;
    }

    @Override
    public String getBlockingActions()
    {
        return  getMsg() + " (" + state + ")";
    }

    @Override
    public void reset()
    {
        state = State.idle;
    }

}
