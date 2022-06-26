package com.workflow.test.framework;


class Workflow
{
    private final Action start;
    private WorkflowContext ctx;

    public Workflow(WorkflowContext context, Action startAction) {
        ctx = context;
        start = startAction;
    }

    public boolean completed()
    {
        return start.completed();
    }


    public Action.State progress()
    {
       return start.execute();
    }


    public WorkflowContext getContext()
    {
        return ctx;
    }

    public String getBlockingActions()
    {
        ctx.actionPathForDump = "";
        return start.getBlockingActions();
    }
}
