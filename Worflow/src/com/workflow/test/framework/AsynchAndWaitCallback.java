package com.workflow.test.framework;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AsynchAndWaitCallback extends SingleAction {
    private AtomicBoolean unlock;

    public AsynchAndWaitCallback() {
        unlock = new AtomicBoolean();
    }

    @Override
    protected Action.State getState() {
        updateState();
        return state;
    }

    @Override
    public Action.State execute() {
        updateState();
        if (state == Action.State.blocked)
            return state;
        else
            return super.execute();
    }

    protected void updateState() {
//        if (state != Action.State.completed && unlock.get())
        // state = Action.State.running;

        if (state == Action.State.idle) {
            state = Action.State.blocked;
            triggerCallback();
        }

        if (state == State.blocked && unlock.get())
            state = State.running;
    }

    @Override
    protected Action.State doStep() {
        state = Action.State.completed;
        return state;
    }

    @Override
    public void reset() {
        super.reset();
        unlock.set(false);
    }

    protected void signalCallbackCompleted() {
        synchronized (wfContext)
        {
            unlock.set(true);
            wfContext.notify();
        }
    }

    protected abstract void triggerCallback();
}
