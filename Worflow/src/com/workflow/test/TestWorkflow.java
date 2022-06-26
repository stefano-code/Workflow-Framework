package com.workflow.test;

import com.workflow.test.framework.*;

import java.util.Random;

import static com.workflow.test.framework.WorkflowEngine.Parallel;
import static com.workflow.test.framework.WorkflowEngine.Sequence;

public class TestWorkflow {
    public static void main( String[] arg )
    {
        WorkflowEngine wfe = WorkflowEngine.getInstance();
        System.out.println("TEST WORKFLOW");

        WorkflowContext wfc = new WorkflowContext("test_workflow");
            wfe.CreateWorkflow(wfc,Sequence("SequenceRoot").
                    start(new ActionTest("Action0")).
                    andThen(Parallel("Parallel0").
                            start(Parallel("Parallel1").
                                    start(Sequence("Sequence1").
                                            start(new ActionTest("Action1")).
                                            andThen(new AsyncActionTest("Action2", new RandomPredicate()))).
                                    andConcurrently(Sequence("Sequence2").
                                            start(new ActionTest("Action3")).
                                            andThen(new ActionTest("Action4"))).
                                    andConcurrently(Sequence("Sequence3").
                                            start(new AsyncActionTest("Action5", new RandomPredicate())).
                                            andThen(new CheckAction("Predicate1", new RandomPredicate()).
                                                    thenElse(new ActionTest("Action6"), new ActionTest("Action7"))))).
                            andConcurrently(Parallel("Parallel2").
                                    start(new AsyncActionTest("Action8", new RandomPredicate())).
                                    andConcurrently(new CheckAction("Predicate2", new RandomPredicate()).
                                            thenElse(new ActionTest("Action9"), new ActionTest("Action10"))))).
                    andThen(new ActionTest("FinalAction"))
        );

        wfe.execute();
        wfe.waitToFinish();
    }
}
