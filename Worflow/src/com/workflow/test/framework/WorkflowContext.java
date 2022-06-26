package com.workflow.test.framework;

public class WorkflowContext
{
    private String rawTag;
    public String tag;
    public String actionPathForDump;

    public WorkflowContext( String tag )
    {
        this.tag = setTag(tag);
        Action.wfContext = this;
    }

    private String setTag(String tag)
    {
        rawTag = tag;
        return "wf." + tag;
    }

    public void nestTag(String origin )
    {
        this.tag = setTag( origin + "." + rawTag );
    }
}
