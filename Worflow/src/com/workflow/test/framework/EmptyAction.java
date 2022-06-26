package com.workflow.test.framework;

public class EmptyAction extends SingleAction
{
    private String name;

    EmptyAction(String n)
    {
        name = n;
    }

    EmptyAction()
    {
    }

    @Override
    protected State doStep()
    {
        if(name != null)
            Journal.Log(wfContext.tag, name );
        return State.completed;
    }

    @Override
    protected void logExecution()
    {
        //SS Journal.Log(wfContext.tag, "executing " + getMsg());
    }

}
