package com.workflow.test;

import com.workflow.test.framework.Journal;
import com.workflow.test.framework.SingleAction;

public class ActionTest extends SingleAction
{
    String name;
    Simulation simulation = new Simulation();

    ActionTest(String n)
    {
        name = n;
    }

    @Override
    protected State doStep()
    {
        simulation.update();
        if(simulation.isTerminate())
            Journal.Log(wfContext.tag, name + " isTerminate() " + simulation.isTerminate());
        return (simulation.isTerminate()) ? State.completed : State.running;
        //SS return State.completed;
    }

    public String getMsg()
    {
        //SS Log.i( wfContext.tag, "msg " +  getClass().getSimpleName() );
        return getClass().getSimpleName() + " " + name;
    }
}
