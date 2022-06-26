package com.workflow.test.framework;

import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncAwaitAction extends SingleAction
{
    private Predicate<Void> body;
    private String msg;

    private Thread th;
    private AtomicBoolean unlock;

    public AsyncAwaitAction(String actionName, Predicate<Void> f)
    {
        msg = actionName;
        body = f;
        unlock = new AtomicBoolean(false);
    }

    public void setPredicate( Predicate<Void> f) {
        body = f;
    }

    @Override
    protected State getState()
    {
        updateState();
        return state;
    }

    @Override
    public State execute()
    {
        updateState();
        if (state == State.blocked)
            return state;
        else
            return super.execute();
    }

    private void updateState()
    {
        if (state == State.idle)
        {
            state = State.blocked;

            th = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //SS body.apply(null);
                        body.test(null);
                    } catch (Exception e)
                    {
                    }
                    Journal.Log(wfContext.tag, "AsyncAwaitAction " + msg + " done");
                    synchronized (wfContext)
                    {
                        unlock.set(true);
                        wfContext.notify();
                    }
                }
            };
            th.setName("AsyncAwaitAction " + msg);
            th.start();

            Journal.Log(wfContext.tag, "AsyncAwaitAction " + msg + " awaiting");
        }

        if (state == State.blocked && unlock.get())
            state = State.running;
    }

    @Override
    protected State doStep()
    {
        state = State.completed;
        return state;
    }

    @Override
    public String getMsg()
    {
        return this.getClass().getSimpleName() + " " + msg;
    }

    @Override
    public String getBlockingActions()
    {
        if (state == State.blocked)
            return wfContext.actionPathForDump + getMsg();
        else
            return super.getBlockingActions();
    }

    @Override
    public void reset()
    {
        super.reset();
        unlock.set(false);
    }
}
