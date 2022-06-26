package com.workflow.test.framework;

class WorkflowThread extends Thread
{
    private final Workflow wf;

    public WorkflowThread(Workflow wf)
    {
        this.setName( "workflow " + Action.wfContext.tag);
        this.wf = wf;
    }

    @Override
    public void run()
    {
        Journal.Log(  Action.wfContext.tag, "started");
        while( ! wf.completed() )
        {
           Action.State workflowState = wf.progress();
           if(workflowState == Action.State.blocked )
           {
               Journal.Log(  Action.wfContext.tag, "workflow blocked; blocking actions:");
               Journal.Log(  Action.wfContext.tag, wf.getBlockingActions());
               try
               {
                   synchronized ( wf.getContext())
                   {
                       wf.getContext().wait();
                   }
                   Journal.Log(  Action.wfContext.tag, "workflow unlocked");
               } catch (Exception e)
               {
                   Journal.Log(  Action.wfContext.tag, "trapped exception " + e.getStackTrace());
               }
           }
        }
        Journal.Log(  Action.wfContext.tag, "completed");
    }
}
